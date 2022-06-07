package io.github.erp.internal.batch.dealer;

/*-
 * Erp System - Mark II No 7 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.Dealer;
import io.github.erp.internal.framework.BatchService;
import io.github.erp.internal.framework.FileUploadsProperties;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.batch.*;
import io.github.erp.internal.framework.excel.ExcelFileDeserializer;
import io.github.erp.internal.framework.model.FileUploadHasDataFile;
import io.github.erp.internal.framework.service.DataFileContainer;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.DealerBEO;
import io.github.erp.internal.model.DealerEVM;
import io.github.erp.service.dto.DealerDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class DealerBatchConfigs {

    private static final String PERSISTENCE_JOB_NAME = "dealerListPersistenceJob";
    private static final String DELETION_JOB_NAME = "dealerListDeletionJob";
    private static final String READ_FILE_STEP_NAME = "readDealerListFromFile";
    private static final String DELETION_STEP_NAME = "deleteDealerListFromFile";
    private static final String DELETION_PROCESSOR_NAME = "dealerDeletionProcessor";
    private static final String DELETION_WRITER_NAME = "dealerDeletionWriter";
    private static final String DELETION_READER_NAME = "dealerDeletionReader";
    private static final String PERSISTENCE_READER_NAME = "dealerListItemReader";
    private static final String PERSISTENCE_PROCESSOR_NAME = "dealerListItemProcessor";
    private static final String PERSISTENCE_WRITER_NAME = "dealerEntityListItemsWriter";

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['fileId']}")
    private static long fileId;

    @SuppressWarnings("SpringElStaticFieldInjectionInspection")
    @Value("#{jobParameters['messageToken']}")
    private static String jobUploadToken;

    @Autowired
    private FileUploadsProperties fileUploadsProperties;

    @Autowired
    @Qualifier("persistenceJobListener")
    private JobExecutionListener persistenceJobListener;

    @Autowired
    @Qualifier("deletionJobListener")
    private JobExecutionListener deletionJobListener;

    @Autowired
    private ExcelFileDeserializer<DealerEVM> dealerDeserializer;

    @Autowired
    private BatchService<DealerDTO> batchService;

    @Autowired
    private DeletionService<Dealer> dealerDeletionService;

    @Autowired
    private Mapping<DealerEVM, DealerDTO> mapping;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BatchPersistentFileUploadService fileUploadService;

    @Autowired
    private DeletionUploadService<DealerBEO> fileUploadDeletionService;

    @Autowired
    private DataFileContainer<FileUploadHasDataFile> dataFileContainer;

    @Bean(PERSISTENCE_READER_NAME)
    @StepScope
    public EntityItemsReader<DealerEVM> listItemReader(@Value("#{jobParameters['fileId']}") long fileId ) {
        return new EntityItemsReader<>(dealerDeserializer, fileUploadService, fileId, fileUploadsProperties);
    }

    @Bean(PERSISTENCE_PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<List<DealerEVM>, List<DealerDTO>> listItemsProcessor(@Value("#{jobParameters['messageToken']}") String jobUploadToken) {
        return new DealerPersistenceProcessor(mapping, jobUploadToken);
    }

    @Bean(PERSISTENCE_WRITER_NAME)
    @StepScope
    public EntityListItemsWriter<DealerDTO> listItemsWriter() {
        return new EntityListItemsWriter<>(batchService);
    }

    @Bean(READ_FILE_STEP_NAME)
    @JobScope
    public Step readFile() {
        return new ReadFileStep<>(
            READ_FILE_STEP_NAME,
            listItemReader(fileId),
            listItemsProcessor(jobUploadToken),
            listItemsWriter(),
            stepBuilderFactory
        );
    }

    @Bean(PERSISTENCE_JOB_NAME)
    public Job persistenceJob() {
        return new SingleStepEntityJob(PERSISTENCE_JOB_NAME, persistenceJobListener, readFile(), jobBuilderFactory);
    }

    // dealerDeletionJob
    @Bean(DELETION_JOB_NAME)
    public Job dealerDeletionJob() {
        return new SingleStepEntityJob(DELETION_JOB_NAME, deletionJobListener, deleteEntityListFromFile(), jobBuilderFactory);
    }

    @Bean(DELETION_STEP_NAME)
    @JobScope
    public Step deleteEntityListFromFile() {
        return new DataDeletionStep<>(
            stepBuilderFactory,
            DELETION_STEP_NAME,
            deletionReader(fileId),
            deletionProcessor(),
            deletionWriter()
        );
    }



    @Bean(DELETION_READER_NAME)
    @StepScope
    public ItemReader<List<Long>> deletionReader(@Value("#{jobParameters['fileId']}") long fileId) {
        return new EntityItemsDeletionReader(fileId, fileUploadDeletionService, fileUploadsProperties, dataFileContainer);
    }

    @Bean(DELETION_PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<List<Long>, List<Dealer>> deletionProcessor() {
        return new EntityDeletionProcessor<>(dealerDeletionService);
    }

    @Bean(DELETION_WRITER_NAME)
    @StepScope
    public ItemWriter<? super List<Dealer>> deletionWriter() {
        return new NoOpsItemWriter<>();
    }
}
