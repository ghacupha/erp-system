package io.github.erp.internal.service.rou.batch;

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
import io.github.erp.internal.service.rou.InternalRouDepreciationEntryService;
import io.github.erp.internal.service.rou.InternalRouModelMetadataService;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.dto.RouModelMetadataDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InvalidateDepreciationBatchConfig {

    public static final String INVALIDATE_DEPRECIATION_JOB_NAME = "invalidateDepreciationJob";
    private static final String INVALIDATE_ENTRIES_STEP_NAME = "invalidateEntriesStep";
    private static final String UPDATE_METADATA_STEP_NAME = "updateMetadataStep";
    private static final String INVALIDATE_DEPRECIATION_ENTRY_READER_NAME = "invalidateDepreciationEntryReader";
    private static final String INVALIDATE_DEPRECIATION_ENTRY_PROCESSOR_NAME = "invalidateDepreciationEntryProcessor";
    private static final String INVALIDATE_DEPRECIATION_ENTRY_WRITER_NAME = "invalidateDepreciationEntryWriter";
    private static final String INVALIDATE_METADATA_AMORTIZATION_READER_NAME = "invalidateMetadataAmortizationReader";
    private static final String INVALIDATE_METADATA_AMORTIZATION_PROCESSOR_NAME = "invalidateMetadataAmortizationProcessor";
    private static final String INVALIDATE_METADATA_AMORTIZATION_WRITER_NAME = "invalidateMetadataAmortizationWriter";

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['batchJobIdentifier']}")
    private static String batchJobIdentifier;

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['previousBatchJobIdentifier']}")
    private static String previousBatchJobIdentifier;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private InternalRouDepreciationEntryService internalRouDepreciationEntryService;

    @Autowired
    private InternalRouModelMetadataService rouModelMetadataService;

    @Autowired
    private InternalRouModelMetadataService internalRouModelMetadataService;

    @Bean(INVALIDATE_DEPRECIATION_JOB_NAME)
    public Job invalidateDepreciationJob() {
        return jobBuilderFactory.get(INVALIDATE_DEPRECIATION_JOB_NAME)
            .start(invalidateEntriesStep())
            .next(updateMetadataStep())
            .build();
    }

    @Bean(INVALIDATE_ENTRIES_STEP_NAME)
    public Step invalidateEntriesStep() {
        return stepBuilderFactory.get(INVALIDATE_ENTRIES_STEP_NAME)
            .<RouDepreciationEntryDTO, RouDepreciationEntryDTO>chunk(24) // Assuming processing of 2 years at a time
            .reader(invalidateDepreciationEntryReader(previousBatchJobIdentifier))
            .processor(invalidateDepreciationEntryProcessor(batchJobIdentifier))
            .writer(invalidateDepreciationEntryWriter())
            .build();
    }

    @Bean(INVALIDATE_DEPRECIATION_ENTRY_READER_NAME)
    @StepScope
    public InvalidateDepreciationEntryReader invalidateDepreciationEntryReader(@Value("#{jobParameters['previousBatchJobIdentifier']}") String previousBatchJobIdentifier) {

        return new InvalidateDepreciationEntryReader(internalRouDepreciationEntryService, previousBatchJobIdentifier);
    }

    @Bean(INVALIDATE_DEPRECIATION_ENTRY_PROCESSOR_NAME)
    @StepScope
    public InvalidateDepreciationEntryProcessor invalidateDepreciationEntryProcessor(@Value("#{jobParameters['batchJobIdentifier']}") String batchJobIdentifier) {

        return new InvalidateDepreciationEntryProcessor(batchJobIdentifier);
    }

    @Bean(INVALIDATE_DEPRECIATION_ENTRY_WRITER_NAME)
    public InvalidateDepreciationEntryWriter invalidateDepreciationEntryWriter() {

        return new InvalidateDepreciationEntryWriter(internalRouDepreciationEntryService);
    }

    @Bean(UPDATE_METADATA_STEP_NAME)
    public Step updateMetadataStep() {
        return stepBuilderFactory.get(UPDATE_METADATA_STEP_NAME)
            .<RouModelMetadataDTO, RouModelMetadataDTO>chunk(10)
            .reader(invalidateMetadataAmortizationReader(previousBatchJobIdentifier))
            .processor(invalidateMetadataAmortizationProcessor(batchJobIdentifier))
            .writer(invalidateMetadataAmortizationWriter())
            .build();
    }

    @Bean(INVALIDATE_METADATA_AMORTIZATION_READER_NAME)
    @StepScope
    public ItemReader<RouModelMetadataDTO> invalidateMetadataAmortizationReader(@Value("#{jobParameters['previousBatchJobIdentifier']}") String previousBatchJobIdentifier) {
        return new InvalidateMetadataAmortizationReader(rouModelMetadataService, previousBatchJobIdentifier);
    }

    @Bean(INVALIDATE_METADATA_AMORTIZATION_PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<RouModelMetadataDTO, RouModelMetadataDTO> invalidateMetadataAmortizationProcessor(@Value("#{jobParameters['batchJobIdentifier']}") String batchJobIdentifier) {
        return new InvalidatedMetadataAmortizationProcessor(batchJobIdentifier);
    }

    @Bean(INVALIDATE_METADATA_AMORTIZATION_WRITER_NAME)
    public ItemWriter<RouModelMetadataDTO> invalidateMetadataAmortizationWriter() {
        return new InvalidateMetadataAmortizationWriter(internalRouModelMetadataService);
    }
}
