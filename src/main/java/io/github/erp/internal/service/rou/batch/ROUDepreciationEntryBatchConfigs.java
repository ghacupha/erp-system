package io.github.erp.internal.service.rou.batch;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.domain.RouDepreciationEntry;
import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.internal.service.rou.InternalRouModelMetadataService;
import io.github.erp.internal.service.rou.ROUDepreciationEntryCompilationService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.dto.RouModelMetadataDTO;
import io.github.erp.service.mapper.RouDepreciationEntryMapper;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

@Configuration
public class ROUDepreciationEntryBatchConfigs {

    public static final String PERSISTENCE_JOB_NAME = "rouDepreciationEntryPersistenceJob";
    private static final String READ_FILE_STEP_NAME = "readROUModelMetadataFromDB";
    private static final String UPDATE_OUTSTANDING_AMOUNT_STEP_NAME = "updateOutstandingAmountStep";
    private static final String PERSISTENCE_READER_NAME = "rouDepreciationEntryItemReader";
    private static final String PERSISTENCE_PROCESSOR_NAME = "rouDepreciationEntryItemProcessor";
    private static final String UPDATE_OUTSTANDING_AMOUNT_ITEM_PROCESSOR_NAME = "updateOutstandingAmountStepItemProcessor";
    private static final String UPDATE_OUTSTANDING_AMOUNT_ITEM_READER_NAME = "updateOutstandingAmountStepItemReader";
    private static final String UPDATE_OUTSTANDING_AMOUNT_ITEM_READER_QUERY = "SELECT r FROM RouDepreciationEntry r WHERE r.outstandingAmount <= :thresholdAmount ORDER BY r.leasePeriod.sequenceNumber";
    private static final String UPDATE_OUTSTANDING_AMOUNT_ITEM_READER_PARAMETER = "thresholdAmount";
    private static final String UPDATE_OUTSTANDING_AMOUNT_ITEM_WRITER_NAME = "updateOutstandingAmountStepItemWriter";
    private static final String PERSISTENCE_WRITER_NAME = "rouDepreciationEntryListItemsWriter";

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['rouDepreciationRequestId']}")
    private static long rouDepreciationRequestId;

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['jobToken']}")
    private static String jobToken;

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['batchJobIdentifier']}")
    private static String batchJobIdentifier;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private InternalRouModelMetadataService rouModelMetadataService;

    @Autowired
    private ROUDepreciationEntryCompilationService rouDepreciationEntryCompilationService;

    @Autowired
    private InternalRouDepreciationEntryService rouDepreciationEntryService;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private RouDepreciationEntryMapper rouDepreciationEntryMapper;

    @Autowired
    private InternalRouDepreciationEntryService depreciationEntryService;

    @Bean(PERSISTENCE_READER_NAME)
    @StepScope
    public ItemReader<RouModelMetadataDTO> rouModelMetadataItemReader(@Value("#{jobParameters['rouDepreciationRequestId']}") long rouDepreciationRequestId) {
        return new RouModelMetadataItemReader(rouModelMetadataService, rouDepreciationRequestId);
    }

    @Bean(PERSISTENCE_PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<RouModelMetadataDTO, List<RouDepreciationEntryDTO>> rouModelMetadataDTOListItemProcessor() {
        return new ROUDepreciationEntryItemProcessor(rouDepreciationEntryCompilationService);
    }

    @Bean(PERSISTENCE_WRITER_NAME)
    @StepScope
    public ItemWriter<List<RouDepreciationEntryDTO>> rouDepreciationEntryWriter() {
        return new ROUDepreciationEntryItemWriter(rouDepreciationEntryService);
    }

    @Bean(PERSISTENCE_JOB_NAME)
    public Job depreciationBatchJob(EntityManagerFactory entityManagerFactory) {
        return jobBuilderFactory.get(PERSISTENCE_JOB_NAME)
            .start(updateDepreciationAmountStep())
            .next(updateOutstandingAmountStep(entityManagerFactory))
            .build();
    }

    @Bean(READ_FILE_STEP_NAME)
    public Step updateDepreciationAmountStep() {
        return stepBuilderFactory.get(READ_FILE_STEP_NAME)
            .<RouModelMetadataDTO, List<RouDepreciationEntryDTO>>chunk(50)
            .reader(rouModelMetadataItemReader(rouDepreciationRequestId))
            .processor(rouModelMetadataDTOListItemProcessor())
            .writer(rouDepreciationEntryWriter())
            .build();
    }

    @Bean(UPDATE_OUTSTANDING_AMOUNT_STEP_NAME)
    public Step updateOutstandingAmountStep() {
        return stepBuilderFactory.get(UPDATE_OUTSTANDING_AMOUNT_STEP_NAME)
            .<RouDepreciationEntry, RouDepreciationEntryDTO>chunk(50)
            .reader(updateOutstandingAmountItemReader())
            .processor(updateOutstandingAmountProcessor())
            .writer(updateOutstandingAmountItemWriter())
            .build();
    }

    @Bean(UPDATE_OUTSTANDING_AMOUNT_ITEM_PROCESSOR_NAME)
    public ItemProcessor<RouDepreciationEntry, RouDepreciationEntryDTO> updateOutstandingAmountProcessor() {
        return new UpdateOutstandingAmountProcessor(rouDepreciationEntryMapper, depreciationEntryService);
    }

    @Bean(UPDATE_OUTSTANDING_AMOUNT_ITEM_WRITER_NAME)
    @StepScope
    public ItemWriter<RouDepreciationEntryDTO> updateOutstandingAmountItemWriter() {
        return new UpdateOutstandingAmountItemWriter(rouDepreciationEntryService);
    }

    @Bean(UPDATE_OUTSTANDING_AMOUNT_ITEM_READER_NAME)
    @StepScope
    public ItemReader<RouDepreciationEntry> updateOutstandingAmountItemReader() {
        return new JpaPagingItemReaderBuilder<RouDepreciationEntry>()
            .name(UPDATE_OUTSTANDING_AMOUNT_ITEM_READER_NAME)
            .entityManagerFactory(entityManagerFactory)
            .queryString(UPDATE_OUTSTANDING_AMOUNT_ITEM_READER_QUERY)
            .parameterValues(Collections.singletonMap(UPDATE_OUTSTANDING_AMOUNT_ITEM_READER_PARAMETER, new BigDecimal("10.00")))
            .pageSize(100)
            .build();
    }

}
