package io.github.erp.internal.service.rou.batch;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.internal.service.rou.InternalRouModelMetadataService;
import io.github.erp.internal.service.rou.ROUDepreciationEntryCompilationService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.dto.RouModelMetadataDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
    private static final String UPDATE_OUTSTANDING_AMOUNT_ITEM_WRITER_NAME = "updateOutstandingAmountStepItemWriter";
    private static final String PERSISTENCE_WRITER_NAME = "rouDepreciationEntryListItemsWriter";
    private static final String UPDATE_FULLY_AMORTISED_STEP_NAME = "updateFullyAmortisedStep";
    private static final String UPDATE_FULLY_AMORTISED_ITEM_READER_NAME = "updateFullyAmortisedItemReader";
    private static final String UPDATE_FULLY_AMORTISED_ITEM_PROCESSOR_NAME = "updateFullyAmortisedItemProcessor";
    private static final String UPDATE_FULLY_AMORTISED_ITEM_WRITER_NAME = "updateFullyAmortisedItemWriter";

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['rouDepreciationRequestId']}")
    private static long rouDepreciationRequestId;

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['jobToken']}")
    private static String jobToken;

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['batchJobIdentifier']}")
    private static String batchJobIdentifier;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final InternalRouModelMetadataService rouModelMetadataService;

    private final ROUDepreciationEntryCompilationService rouDepreciationEntryCompilationService;

    private final InternalRouDepreciationEntryService rouDepreciationEntryService;

    private final InternalRouDepreciationEntryService depreciationEntryService;

    private final InternalRouDepreciationEntryService internalRouDepreciationEntryService;

    private final InternalRouModelMetadataService internalRouModelMetadataService;

    public ROUDepreciationEntryBatchConfigs(
        JobBuilderFactory jobBuilderFactory,
        StepBuilderFactory stepBuilderFactory,
        InternalRouModelMetadataService rouModelMetadataService,
        ROUDepreciationEntryCompilationService rouDepreciationEntryCompilationService,
        InternalRouDepreciationEntryService rouDepreciationEntryService,
        InternalRouDepreciationEntryService depreciationEntryService,
        InternalRouDepreciationEntryService internalRouDepreciationEntryService,
        InternalRouModelMetadataService internalRouModelMetadataService) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.rouModelMetadataService = rouModelMetadataService;
        this.rouDepreciationEntryCompilationService = rouDepreciationEntryCompilationService;
        this.rouDepreciationEntryService = rouDepreciationEntryService;
        this.depreciationEntryService = depreciationEntryService;
        this.internalRouDepreciationEntryService = internalRouDepreciationEntryService;
        this.internalRouModelMetadataService = internalRouModelMetadataService;
    }

    @Bean(PERSISTENCE_READER_NAME)
    @StepScope
    public RouModelMetadataItemReader rouModelMetadataItemReader(
        @Value("#{jobParameters['rouDepreciationRequestId']}") long rouDepreciationRequestId,
        @Value("#{jobParameters['batchJobIdentifier']}") String batchJobIdentifier) {
        return new RouModelMetadataItemReader(rouModelMetadataService, rouDepreciationRequestId, batchJobIdentifier);
    }

    @Bean(PERSISTENCE_PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<RouModelMetadataDTO, List<RouDepreciationEntryDTO>> rouModelMetadataDTOListItemProcessor(
        @Value("#{jobParameters['batchJobIdentifier']}") String batchJobIdentifier
    ) {
        return new ROUDepreciationEntryItemProcessor(batchJobIdentifier, rouDepreciationEntryCompilationService);
    }

    @Bean(PERSISTENCE_WRITER_NAME)
    @StepScope
    public ItemWriter<List<RouDepreciationEntryDTO>> rouDepreciationEntryWriter() {
        return new ROUDepreciationEntryItemWriter(rouDepreciationEntryService);
    }

    @Bean(PERSISTENCE_JOB_NAME)
    public Job depreciationBatchJob() {
        return jobBuilderFactory.get(PERSISTENCE_JOB_NAME)
            .start(updateDepreciationAmountStep())
            .next(updateOutstandingAmountStep())
            .next(updateFullyAmortisedStep())
            .build();
    }

    @Bean(READ_FILE_STEP_NAME)
    public Step updateDepreciationAmountStep() {
        return stepBuilderFactory.get(READ_FILE_STEP_NAME)
            .<RouModelMetadataDTO, List<RouDepreciationEntryDTO>>chunk(50)
            .reader(rouModelMetadataItemReader(rouDepreciationRequestId, batchJobIdentifier))
            .processor(rouModelMetadataDTOListItemProcessor(batchJobIdentifier))
            .writer(rouDepreciationEntryWriter())
            .build();
    }

    @Bean(UPDATE_OUTSTANDING_AMOUNT_STEP_NAME)
    public Step updateOutstandingAmountStep() {
        return stepBuilderFactory.get(UPDATE_OUTSTANDING_AMOUNT_STEP_NAME)
            .<RouDepreciationEntryDTO, RouDepreciationEntryDTO>chunk(50)
            .reader(updateOutstandingAmountItemReader())
            .processor(updateOutstandingAmountProcessor())
            .writer(updateOutstandingAmountItemWriter())
            .build();
    }

    @Bean(UPDATE_OUTSTANDING_AMOUNT_ITEM_PROCESSOR_NAME)
    public ItemProcessor<RouDepreciationEntryDTO, RouDepreciationEntryDTO> updateOutstandingAmountProcessor() {
        return new UpdateOutstandingAmountProcessor(depreciationEntryService);
    }

    @Bean(UPDATE_OUTSTANDING_AMOUNT_ITEM_WRITER_NAME)
    @StepScope
    public ItemWriter<RouDepreciationEntryDTO> updateOutstandingAmountItemWriter() {
        return new UpdateOutstandingAmountItemWriter(rouDepreciationEntryService);
    }

    @Bean(UPDATE_OUTSTANDING_AMOUNT_ITEM_READER_NAME)
    @StepScope
    public UpdateOutstandingAmountItemReader updateOutstandingAmountItemReader() {

        return new UpdateOutstandingAmountItemReader(internalRouDepreciationEntryService);
    }

    @Bean(UPDATE_FULLY_AMORTISED_STEP_NAME)
    public Step updateFullyAmortisedStep() {
        return stepBuilderFactory.get(UPDATE_FULLY_AMORTISED_STEP_NAME)
            .<RouModelMetadataDTO, RouModelMetadataDTO>chunk(100)
            .reader(updateFullyAmortisedItemReader(batchJobIdentifier))
            .processor(updateFullyAmortisedProcessor())
            .writer(updateFullyAmortisedItemWriter())
            .build();
    }

    @Bean(UPDATE_FULLY_AMORTISED_ITEM_READER_NAME)
    @StepScope
    public UpdateFullyAmortisedItemReader updateFullyAmortisedItemReader(@Value("#{jobParameters['batchJobIdentifier']}") String batchJobIdentifier) {
        return new UpdateFullyAmortisedItemReader(internalRouModelMetadataService, batchJobIdentifier);
    }

    @Bean(UPDATE_FULLY_AMORTISED_ITEM_PROCESSOR_NAME)
    public ItemProcessor<RouModelMetadataDTO, RouModelMetadataDTO> updateFullyAmortisedProcessor() {
        return new UpdateFullyAmortisedProcessor();
    }

    @Bean(UPDATE_FULLY_AMORTISED_ITEM_WRITER_NAME)
    @StepScope
    public ItemWriter<RouModelMetadataDTO> updateFullyAmortisedItemWriter() {
        return new UpdateFullyAmortisedItemWriter(rouModelMetadataService);
    }
}
