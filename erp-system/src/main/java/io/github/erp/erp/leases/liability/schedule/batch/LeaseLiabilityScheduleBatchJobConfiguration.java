package io.github.erp.erp.leases.liability.schedule.batch;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.github.erp.erp.leases.liability.schedule.batch.support.CommaStrippingBigDecimalEditor;
import io.github.erp.erp.leases.liability.schedule.batch.support.LocalDatePropertyEditor;
import io.github.erp.erp.leases.liability.schedule.batch.support.RowItem;
import io.github.erp.erp.leases.liability.schedule.batch.support.RowItemLineMapper;
import io.github.erp.erp.leases.liability.schedule.model.LeaseLiabilityScheduleItemQueueItem;
import java.beans.PropertyEditor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.BindException;

@Configuration
public class LeaseLiabilityScheduleBatchJobConfiguration {

    private static final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleBatchJobConfiguration.class);

    public static final String[] CSV_FIELDS = new String[] {
        "sequenceNumber",
        "openingBalance",
        "cashPayment",
        "principalPayment",
        "interestPayment",
        "outstandingBalance",
        "interestPayableOpening",
        "interestAccrued",
        "interestPayableClosing",
        "leasePeriodId",
        "active"
    };

    public static final String JOB_NAME = "leaseLiabilityScheduleUploadJob";
    public static final String STEP_NAME = "leaseLiabilityScheduleUploadStep";
    public static final String ITEM_READER_NAME = "leaseLiabilityScheduleCsvItemReader";
    public static final String VALIDATION_PROCESSOR = "leaseLiabilityScheduleValidationProcessor";
    public static final String METADATA_PROCESSOR = "leaseLiabilityScheduleMetadataProcessor";
    public static final String COMPOSITE_PROCESSOR = "leaseLiabilityScheduleCompositeProcessor";
    public static final String KAFKA_ITEM_WRITER = "leaseLiabilityScheduleKafkaItemWriter";
    public static final String SKIP_LISTENER = "leaseLiabilityScheduleSkipListener";

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    public LeaseLiabilityScheduleBatchJobConfiguration(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager != null ? transactionManager : new ResourcelessTransactionManager();
    }

    @Bean(ITEM_READER_NAME)
    @StepScope
    public FlatFileItemReader<RowItem<LeaseLiabilityScheduleItemQueueItem>> leaseLiabilityScheduleCsvItemReader(
        @Value("#{jobParameters['filePath']}") String filePath
    ) {
        FlatFileItemReader<RowItem<LeaseLiabilityScheduleItemQueueItem>> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1);

        DefaultLineMapper<LeaseLiabilityScheduleItemQueueItem> delegate = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames(CSV_FIELDS);
        delegate.setLineTokenizer(tokenizer);

        BeanPropertyMapper fieldSetMapper = new BeanPropertyMapper();
        delegate.setFieldSetMapper(fieldSetMapper);

        RowItemLineMapper<LeaseLiabilityScheduleItemQueueItem> lineMapper = new RowItemLineMapper<>(delegate);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean(VALIDATION_PROCESSOR)
    @StepScope
    public ItemProcessor<RowItem<LeaseLiabilityScheduleItemQueueItem>, RowItem<LeaseLiabilityScheduleItemQueueItem>> validationProcessor() {
        return rowItem -> {
            LeaseLiabilityScheduleItemQueueItem item = rowItem.getItem();
            if (item.getSequenceNumber() == null) {
                throw new IllegalArgumentException("Missing sequence number at row " + rowItem.getRowNumber());
            }
            if (item.getLeasePeriodId() == null) {
                throw new IllegalArgumentException("Missing lease period identifier at row " + rowItem.getRowNumber());
            }
            return rowItem;
        };
    }

    @Bean(METADATA_PROCESSOR)
    @StepScope
    public ItemProcessor<LeaseLiabilityScheduleItemQueueItem, LeaseLiabilityScheduleItemQueueItem> metadataProcessor(
        @Value("#{jobParameters['leaseLiabilityId']}") Long leaseLiabilityId,
        @Value("#{jobParameters['leaseAmortizationScheduleId']}") Long leaseAmortizationScheduleId,
        @Value("#{jobParameters['leaseLiabilityCompilationId']}") Long leaseLiabilityCompilationId,
        @Value("#{jobParameters['uploadId']}") Long uploadId
    ) {
        return item -> {
            item.setLeaseLiabilityId(leaseLiabilityId);
            item.setLeaseLiabilityCompilationId(leaseLiabilityCompilationId);
            item.setUploadId(uploadId);
            if (leaseAmortizationScheduleId != null) {
                item.setLeaseAmortizationScheduleId(leaseAmortizationScheduleId);
            }
            if (item.getUuid() == null) {
                item.setUuid(UUID.randomUUID());
            }
            if (item.getActive() == null) {
                item.setActive(Boolean.TRUE);
            }
            return item;
        };
    }

    @Bean(COMPOSITE_PROCESSOR)
    public CompositeItemProcessor<RowItem<LeaseLiabilityScheduleItemQueueItem>, LeaseLiabilityScheduleItemQueueItem> compositeProcessor(
        @Qualifier(VALIDATION_PROCESSOR)
        ItemProcessor<RowItem<LeaseLiabilityScheduleItemQueueItem>, RowItem<LeaseLiabilityScheduleItemQueueItem>> validationProcessor,
        @Qualifier(METADATA_PROCESSOR)
        ItemProcessor<LeaseLiabilityScheduleItemQueueItem, LeaseLiabilityScheduleItemQueueItem> metadataProcessor
    ) {
        CompositeItemProcessor<RowItem<LeaseLiabilityScheduleItemQueueItem>, LeaseLiabilityScheduleItemQueueItem> processor =
            new CompositeItemProcessor<>();
        processor.setDelegates(Arrays.asList(validationProcessor, RowItem::getItem, metadataProcessor));
        return processor;
    }

    @Bean(KAFKA_ITEM_WRITER)
    public ItemWriter<LeaseLiabilityScheduleItemQueueItem> kafkaItemWriter(
        @Qualifier("leaseLiabilityScheduleKafkaTemplate")
        KafkaTemplate<String, LeaseLiabilityScheduleItemQueueItem> kafkaTemplate,
        @Value("${spring.kafka.topics.lease-liability-schedule.topic.name:lease-liability-schedule-items}") String topicName
    ) {
        return items -> {
            for (LeaseLiabilityScheduleItemQueueItem item : items) {
                kafkaTemplate.send(topicName, item.getUuid().toString(), item);
            }
        };
    }

    @Bean(SKIP_LISTENER)
    public org.springframework.batch.core.listener.SkipListener<RowItem<LeaseLiabilityScheduleItemQueueItem>, LeaseLiabilityScheduleItemQueueItem> skipListener() {
        return new org.springframework.batch.core.listener.SkipListener<>() {
            @Override
            public void onSkipInRead(Throwable t) {
                log.warn("Skipping row during read: {}", t.getMessage());
            }

            @Override
            public void onSkipInProcess(RowItem<LeaseLiabilityScheduleItemQueueItem> item, Throwable t) {
                log.warn("Skipping row {} due to processing error: {}", item.getRowNumber(), t.getMessage());
            }

            @Override
            public void onSkipInWrite(LeaseLiabilityScheduleItemQueueItem item, Throwable t) {
                log.warn("Skipping item {} due to write error: {}", item.getUuid(), t.getMessage());
            }
        };
    }

    @Bean(STEP_NAME)
    public Step leaseLiabilityScheduleUploadStep(
        @Qualifier(ITEM_READER_NAME) FlatFileItemReader<RowItem<LeaseLiabilityScheduleItemQueueItem>> reader,
        @Qualifier(COMPOSITE_PROCESSOR)
        CompositeItemProcessor<RowItem<LeaseLiabilityScheduleItemQueueItem>, LeaseLiabilityScheduleItemQueueItem> processor,
        @Qualifier(KAFKA_ITEM_WRITER) ItemWriter<LeaseLiabilityScheduleItemQueueItem> writer,
        @Qualifier(SKIP_LISTENER)
        org.springframework.batch.core.listener.SkipListener<RowItem<LeaseLiabilityScheduleItemQueueItem>, LeaseLiabilityScheduleItemQueueItem> skipListener
    ) {
        return new StepBuilder(STEP_NAME, jobRepository)
            .<RowItem<LeaseLiabilityScheduleItemQueueItem>, LeaseLiabilityScheduleItemQueueItem>chunk(100, transactionManager)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .faultTolerant()
            .skip(Exception.class)
            .skipLimit(200)
            .listener(skipListener)
            .build();
    }

    @Bean(JOB_NAME)
    public Job leaseLiabilityScheduleUploadJob(
        @Qualifier(STEP_NAME) Step step,
        @Qualifier("persistenceJobListener") org.springframework.batch.core.JobExecutionListener persistenceJobListener,
        @Qualifier("leaseLiabilityScheduleUploadJobListener") org.springframework.batch.core.JobExecutionListener uploadListener
    ) {
        return new JobBuilder(JOB_NAME, jobRepository)
            .start(step)
            .listener(persistenceJobListener)
            .listener(uploadListener)
            .build();
    }

    /**
     * Custom field set mapper that plugs in the property editors required by the
     * CSV reader.
     */
    static class BeanPropertyMapper implements FieldSetMapper<LeaseLiabilityScheduleItemQueueItem> {

        private final org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper<LeaseLiabilityScheduleItemQueueItem> delegate;

        BeanPropertyMapper() {
            this.delegate = new org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper<>();
            delegate.setTargetType(LeaseLiabilityScheduleItemQueueItem.class);
            Map<Class<?>, PropertyEditor> editors = new HashMap<>();
            editors.put(BigDecimal.class, new CommaStrippingBigDecimalEditor());
            editors.put(LocalDate.class, new LocalDatePropertyEditor());
            delegate.setCustomEditors(editors);
        }

        @Override
        public LeaseLiabilityScheduleItemQueueItem mapFieldSet(org.springframework.batch.item.file.transform.FieldSet fieldSet) throws BindException {
            return delegate.mapFieldSet(fieldSet);
        }
    }
}

