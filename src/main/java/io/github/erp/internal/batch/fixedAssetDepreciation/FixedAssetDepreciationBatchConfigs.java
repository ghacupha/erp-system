
/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.1.9-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
/*-
 * Erp System - Mark II No 19 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
/*-
 *  Server - Leases and assets management platform
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
package io.github.erp.internal.batch.fixedAssetDepreciation;

import io.github.erp.domain.FixedAssetDepreciation;
import io.github.erp.internal.framework.BatchService;
import io.github.erp.internal.framework.FileUploadsProperties;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.batch.BatchPersistentFileUploadService;
import io.github.erp.internal.framework.batch.DataDeletionStep;
import io.github.erp.internal.framework.batch.DeletionService;
import io.github.erp.internal.framework.batch.EntityDeletionProcessor;
import io.github.erp.internal.framework.batch.EntityItemsDeletionReader;
import io.github.erp.internal.framework.batch.EntityItemsReader;
import io.github.erp.internal.framework.batch.EntityListItemsWriter;
import io.github.erp.internal.framework.batch.NoOpsItemWriter;
import io.github.erp.internal.framework.batch.ReadFileStep;
import io.github.erp.internal.framework.batch.SingleStepEntityJob;
import io.github.erp.internal.framework.excel.ExcelFileDeserializer;
import io.github.erp.internal.framework.model.FileUploadHasDataFile;
import io.github.erp.internal.framework.service.DataFileContainer;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.FixedAssetDepreciationBEO;
import io.github.erp.internal.model.FixedAssetDepreciationEVM;
import io.github.erp.service.dto.FixedAssetDepreciationDTO;
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
public class FixedAssetDepreciationBatchConfigs {

    private static final String PERSISTENCE_JOB_NAME = "fixedAssetDepreciationListPersistenceJob";
    private static final String DELETION_JOB_NAME = "fixedAssetDepreciationListDeletionJob";
    private static final String READ_FILE_STEP_NAME = "readFixedAssetDepreciationListFromFile";
    private static final String DELETION_STEP_NAME = "deleteFixedAssetDepreciationListFromFile";
    private static final String DELETION_PROCESSOR_NAME = "fixedAssetDepreciationDeletionProcessor";
    private static final String DELETION_WRITER_NAME = "fixedAssetDepreciationDeletionWriter";
    private static final String DELETION_READER_NAME = "fixedAssetsDepreciationDeletionReader";
    private static final String PERSISTENCE_READER_NAME = "fixedAssetDepreciationListItemReader";
    private static final String PERSISTENCE_PROCESSOR_NAME = "fixedAssetsDepreciationListItemProcessor";
    private static final String PERSISTENCE_WRITER_NAME = "fixedAssetDepreciationEntityListItemsWriter";

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
    private ExcelFileDeserializer<FixedAssetDepreciationEVM> fixedAssetDepreciationDeserializer;

    @Autowired
    private BatchService<FixedAssetDepreciationDTO> batchService;

    @Autowired
    private DeletionService<FixedAssetDepreciation> fixedAssetDepreciationDeletionService;

    @Autowired
    private Mapping<FixedAssetDepreciationEVM, FixedAssetDepreciationDTO> mapping;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BatchPersistentFileUploadService fileUploadService;

    @Autowired
    private DeletionUploadService<FixedAssetDepreciationBEO> fileUploadDeletionService;

    @Autowired
    private DataFileContainer<FileUploadHasDataFile> dataFileContainer;

    @Bean(PERSISTENCE_READER_NAME)
    @StepScope
    public EntityItemsReader<FixedAssetDepreciationEVM> listItemReader(@Value("#{jobParameters['fileId']}") long fileId ) {
        return new EntityItemsReader<>(fixedAssetDepreciationDeserializer, fileUploadService, fileId, fileUploadsProperties);
    }

    @Bean(PERSISTENCE_PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<List<FixedAssetDepreciationEVM>, List<FixedAssetDepreciationDTO>> listItemsProcessor(@Value("#{jobParameters['messageToken']}") String jobUploadToken) {
        return new FixedAssetDepreciationPersistenceProcessor(mapping, jobUploadToken);
    }

    @Bean(PERSISTENCE_WRITER_NAME)
    @StepScope
    public EntityListItemsWriter<FixedAssetDepreciationDTO> listItemsWriter() {
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

    // fixedAssetDepreciationDeletionJob
    @Bean(DELETION_JOB_NAME)
    public Job fixedAssetDepreciationDeletionJob() {
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
    public ItemProcessor<List<Long>, List<FixedAssetDepreciation>> deletionProcessor() {
        return new EntityDeletionProcessor<>(fixedAssetDepreciationDeletionService);
    }

    @Bean(DELETION_WRITER_NAME)
    @StepScope
    public ItemWriter<? super List<FixedAssetDepreciation>> deletionWriter() {
        return new NoOpsItemWriter<>();
    }
}
