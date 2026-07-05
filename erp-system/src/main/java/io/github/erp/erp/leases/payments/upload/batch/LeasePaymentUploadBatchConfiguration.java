package io.github.erp.erp.leases.payments.upload.batch;

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
import io.github.erp.internal.service.leases.InternalLeasePaymentService;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.dto.LeasePaymentUploadDTO;
import java.beans.PropertyEditor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.validation.BindException;

@Configuration
public class LeasePaymentUploadBatchConfiguration {

    public static final String JOB_NAME = "leasePaymentUploadJob";
    public static final String STEP_NAME = "leasePaymentUploadStep";
    public static final String ITEM_READER_NAME = "leasePaymentCsvItemReader";
    public static final String ITEM_PROCESSOR_NAME = "leasePaymentProcessor";
    public static final int CHUNK_SIZE = 6;

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final InternalLeasePaymentService internalLeasePaymentService;

    public LeasePaymentUploadBatchConfiguration(
        JobRepository jobRepository,
        PlatformTransactionManager transactionManager,
        InternalLeasePaymentService internalLeasePaymentService
    ) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager != null ? transactionManager : new ResourcelessTransactionManager();
        this.internalLeasePaymentService = internalLeasePaymentService;
    }

    @Bean(ITEM_READER_NAME)
    @StepScope
    public FlatFileItemReader<LeasePaymentCsvRow> leasePaymentCsvItemReader(@Value("#{jobParameters['filePath']}") String filePath) {
        FlatFileItemReader<LeasePaymentCsvRow> reader = new FlatFileItemReader<>();
        reader.setResource(new FileSystemResource(filePath));
        reader.setLinesToSkip(1);

        DefaultLineMapper<LeasePaymentCsvRow> lineMapper = new DefaultLineMapper<>();
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setDelimiter(",");
        tokenizer.setNames("paymentDate", "paymentAmount");
        lineMapper.setLineTokenizer(tokenizer);

        BeanWrapperFieldSetMapper<LeasePaymentCsvRow> fieldSetMapper = new BeanWrapperFieldSetMapper<>() {
            @Override
            public LeasePaymentCsvRow mapFieldSet(org.springframework.batch.item.file.transform.FieldSet fs) throws BindException {
                return super.mapFieldSet(fs);
            }
        };
        fieldSetMapper.setTargetType(LeasePaymentCsvRow.class);
        Map<Class<?>, PropertyEditor> editors = new HashMap<>();
        editors.put(BigDecimal.class, new CommaStrippingBigDecimalEditor());
        editors.put(LocalDate.class, new LocalDatePropertyEditor());
        fieldSetMapper.setCustomEditors(editors);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        reader.setLineMapper(lineMapper);
        return reader;
    }

    @Bean(ITEM_PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<LeasePaymentCsvRow, LeasePaymentDTO> leasePaymentProcessor(
        @Value("#{jobParameters['leaseContractId']}") Long leaseContractId,
        @Value("#{jobParameters['uploadId']}") Long uploadId
    ) {
        return row -> {
            if (row.getPaymentAmount() == null || row.getPaymentDate() == null) {
                throw new IllegalArgumentException("Payment amount and date are required for each row");
            }
            LeasePaymentDTO dto = new LeasePaymentDTO();
            dto.setPaymentAmount(row.getPaymentAmount());
            dto.setPaymentDate(row.getPaymentDate());
            dto.setActive(row.getActive() != null ? row.getActive() : Boolean.TRUE);

            IFRS16LeaseContractDTO contractDTO = new IFRS16LeaseContractDTO();
            contractDTO.setId(leaseContractId);
            dto.setLeaseContract(contractDTO);

            LeasePaymentUploadDTO uploadDTO = new LeasePaymentUploadDTO();
            uploadDTO.setId(uploadId);
            dto.setLeasePaymentUpload(uploadDTO);
            return dto;
        };
    }

    @Bean
    public ItemWriter<LeasePaymentDTO> leasePaymentUploadWriter() {
        return items -> items.forEach(internalLeasePaymentService::save);
    }

    @Bean(STEP_NAME)
    public Step leasePaymentUploadStep(
        @Qualifier(ITEM_READER_NAME) FlatFileItemReader<LeasePaymentCsvRow> reader,
        @Qualifier(ITEM_PROCESSOR_NAME) ItemProcessor<LeasePaymentCsvRow, LeasePaymentDTO> processor,
        ItemWriter<LeasePaymentDTO> writer
    ) {
        return new StepBuilder(STEP_NAME)
            .repository(jobRepository)
            .<LeasePaymentCsvRow, LeasePaymentDTO>chunk(CHUNK_SIZE)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .transactionManager(transactionManager)
            .build();
    }

    @Bean(JOB_NAME)
    public Job leasePaymentUploadJob(
        @Qualifier(STEP_NAME) Step step,
        @Qualifier("leasePaymentUploadJobListener") org.springframework.batch.core.JobExecutionListener uploadListener
    ) {
        return new JobBuilder(JOB_NAME).repository(jobRepository).start(step).listener(uploadListener).build();
    }
}
