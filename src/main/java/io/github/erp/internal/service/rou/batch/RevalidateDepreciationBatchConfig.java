package io.github.erp.internal.service.rou.batch;

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
public class RevalidateDepreciationBatchConfig {

    public static final String REVALIDATE_DEPRECIATION_JOB_NAME = "revalidateDepreciationJob";
    private static final String REVALIDATE_ENTRIES_STEP_NAME = "revalidateEntriesStep";
    private static final String UPDATE_REVALIDATED_METADATA_STEP_NAME = "updateRevalidatedMetadataStep";
    private static final String REVALIDATE_DEPRECIATION_ENTRY_READER_NAME = "revalidateDepreciationEntryReader";
    private static final String REVALIDATE_DEPRECIATION_ENTRY_PROCESSOR_NAME = "revalidateDepreciationEntryProcessor";
    private static final String REVALIDATE_DEPRECIATION_ENTRY_WRITER_NAME = "revalidateDepreciationEntryWriter";
    private static final String REVALIDATE_METADATA_AMORTIZATION_READER_NAME = "revalidateMetadataAmortizationReader";
    private static final String REVALIDATE_METADATA_AMORTIZATION_PROCESSOR_NAME = "revalidateMetadataAmortizationProcessor";
    private static final String REVALIDATE_METADATA_AMORTIZATION_WRITER_NAME = "revalidateMetadataAmortizationWriter";

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

    @Bean(REVALIDATE_DEPRECIATION_JOB_NAME)
    public Job revalidateDepreciationJob() {
        return jobBuilderFactory.get(REVALIDATE_DEPRECIATION_JOB_NAME)
            .start(revalidateEntriesStep())
            .next(updateRevalidatedMetadataStep())
            .build();
    }

    @Bean(REVALIDATE_ENTRIES_STEP_NAME)
    public Step revalidateEntriesStep() {
        return stepBuilderFactory.get(REVALIDATE_ENTRIES_STEP_NAME)
            .<RouDepreciationEntryDTO, RouDepreciationEntryDTO>chunk(24) // Assuming processing of 2 years at a time
            .reader(revalidateDepreciationEntryReader(previousBatchJobIdentifier))
            .processor(revalidateDepreciationEntryProcessor(batchJobIdentifier))
            .writer(revalidateDepreciationEntryWriter())
            .build();
    }

    @Bean(REVALIDATE_DEPRECIATION_ENTRY_READER_NAME)
    @StepScope
    public RevalidateDepreciationEntryReader revalidateDepreciationEntryReader(@Value("#{jobParameters['previousBatchJobIdentifier']}") String previousBatchJobIdentifier) {

        return new RevalidateDepreciationEntryReader(internalRouDepreciationEntryService, previousBatchJobIdentifier);
    }

    @Bean(REVALIDATE_DEPRECIATION_ENTRY_PROCESSOR_NAME)
    @StepScope
    public RevalidateDepreciationEntryProcessor revalidateDepreciationEntryProcessor(@Value("#{jobParameters['batchJobIdentifier']}") String batchJobIdentifier) {

        return new RevalidateDepreciationEntryProcessor(batchJobIdentifier);
    }

    @Bean(REVALIDATE_DEPRECIATION_ENTRY_WRITER_NAME)
    public RevalidateDepreciationEntryWriter revalidateDepreciationEntryWriter() {

        return new RevalidateDepreciationEntryWriter(internalRouDepreciationEntryService);
    }

    @Bean(UPDATE_REVALIDATED_METADATA_STEP_NAME)
    public Step updateRevalidatedMetadataStep() {
        return stepBuilderFactory.get(UPDATE_REVALIDATED_METADATA_STEP_NAME)
            .<RouModelMetadataDTO, RouModelMetadataDTO>chunk(10)
            .reader(revalidateMetadataAmortizationReader(previousBatchJobIdentifier))
            .processor(revalidateMetadataAmortizationProcessor(batchJobIdentifier))
            .writer(revalidateMetadataAmortizationWriter())
            .build();
    }

    @Bean(REVALIDATE_METADATA_AMORTIZATION_READER_NAME)
    @StepScope
    public ItemReader<RouModelMetadataDTO> revalidateMetadataAmortizationReader(@Value("#{jobParameters['previousBatchJobIdentifier']}") String previousBatchJobIdentifier) {
        return new RevalidateMetadataAmortizationReader(rouModelMetadataService, previousBatchJobIdentifier);
    }

    @Bean(REVALIDATE_METADATA_AMORTIZATION_PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<RouModelMetadataDTO, RouModelMetadataDTO> revalidateMetadataAmortizationProcessor(@Value("#{jobParameters['batchJobIdentifier']}") String batchJobIdentifier) {
        return new RevalidatedMetadataAmortizationProcessor(batchJobIdentifier);
    }

    @Bean(REVALIDATE_METADATA_AMORTIZATION_WRITER_NAME)
    public ItemWriter<RouModelMetadataDTO> revalidateMetadataAmortizationWriter() {
        return new RevalidateMetadataAmortizationWriter(internalRouModelMetadataService);
    }
}
