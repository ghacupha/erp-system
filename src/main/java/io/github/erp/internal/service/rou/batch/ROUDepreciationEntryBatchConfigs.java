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

import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.PaymentBEO;
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
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ROUDepreciationEntryBatchConfigs {

    public static final String PERSISTENCE_JOB_NAME = "rouDepreciationEntryPersistenceJob";
    private static final String READ_FILE_STEP_NAME = "readROUModelMetadataFromDB";
    private static final String PERSISTENCE_READER_NAME = "rouDepreciationEntryItemReader";
    private static final String PERSISTENCE_PROCESSOR_NAME = "rouDepreciationEntryItemProcessor";
    private static final String PERSISTENCE_WRITER_NAME = "rouDepreciationEntryListItemsWriter";

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['rouDepreciationRequestId']}")
    private static long rouDepreciationRequestId;

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['messageToken']}")
    private static String jobUploadToken;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DeletionUploadService<PaymentBEO> fileUploadDeletionService;

    @Autowired
    private InternalRouModelMetadataService rouModelMetadataService;

    @Autowired
    private ROUDepreciationEntryCompilationService rouDepreciationEntryCompilationService;

    @Autowired
    private InternalRouDepreciationEntryService rouDepreciationEntryService;

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
    public Job depreciationBatchJob() {
        return jobBuilderFactory.get(PERSISTENCE_JOB_NAME)
            .start(depreciationBatchStep())
            .build();
    }

    @Bean(READ_FILE_STEP_NAME)
    public Step depreciationBatchStep() {
        return stepBuilderFactory.get(READ_FILE_STEP_NAME)
            .<RouModelMetadataDTO, List<RouDepreciationEntryDTO>>chunk(10)
            .reader(rouModelMetadataItemReader(rouDepreciationRequestId))
            .processor(rouModelMetadataDTOListItemProcessor())
            .writer(rouDepreciationEntryWriter())
            .build();
    }
}
