
/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.erp.internal.batch.paymentCategory;

import io.github.erp.domain.PaymentCategory;
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
import io.github.erp.internal.model.PaymentCategoryBEO;
import io.github.erp.internal.model.PaymentCategoryEVM;
import io.github.erp.service.dto.PaymentCategoryDTO;
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
public class PaymentCategoryBatchConfigs {

    private static final String PERSISTENCE_JOB_NAME = "paymentCategoryListPersistenceJob";
    private static final String DELETION_JOB_NAME = "paymentCategoryListDeletionJob";
    private static final String READ_FILE_STEP_NAME = "readPaymentCategoryListFromFile";
    private static final String DELETION_STEP_NAME = "deletePaymentCategoryListFromFile";
    private static final String DELETION_PROCESSOR_NAME = "paymentCategoryDeletionProcessor";
    private static final String DELETION_WRITER_NAME = "paymentCategoryDeletionWriter";
    private static final String DELETION_READER_NAME = "paymentCategoryDeletionReader";
    private static final String PERSISTENCE_READER_NAME = "paymentCategoryListItemReader";
    private static final String PERSISTENCE_PROCESSOR_NAME = "paymentCategoryListItemProcessor";
    private static final String PERSISTENCE_WRITER_NAME = "paymentCategoryEntityListItemsWriter";

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
    private ExcelFileDeserializer<PaymentCategoryEVM> paymentCategoryDeserializer;

    @Autowired
    private BatchService<PaymentCategoryDTO> batchService;

    @Autowired
    private DeletionService<PaymentCategory> paymentCategoryDeletionService;

    @Autowired
    private Mapping<PaymentCategoryEVM, PaymentCategoryDTO> mapping;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BatchPersistentFileUploadService fileUploadService;

    @Autowired
    private DeletionUploadService<PaymentCategoryBEO> fileUploadDeletionService;

    @Autowired
    private DataFileContainer<FileUploadHasDataFile> dataFileContainer;

    @Bean(PERSISTENCE_READER_NAME)
    @StepScope
    public EntityItemsReader<PaymentCategoryEVM> listItemReader(@Value("#{jobParameters['fileId']}") long fileId ) {
        return new EntityItemsReader<>(paymentCategoryDeserializer, fileUploadService, fileId, fileUploadsProperties);
    }

    @Bean(PERSISTENCE_PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<List<PaymentCategoryEVM>, List<PaymentCategoryDTO>> listItemsProcessor(@Value("#{jobParameters['messageToken']}") String jobUploadToken) {
        return new PaymentCategoryPersistenceProcessor(mapping, jobUploadToken);
    }

    @Bean(PERSISTENCE_WRITER_NAME)
    @StepScope
    public EntityListItemsWriter<PaymentCategoryDTO> listItemsWriter() {
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

    // paymentCategoryDeletionJob
    @Bean(DELETION_JOB_NAME)
    public Job paymentCategoryDeletionJob() {
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
    public ItemProcessor<List<Long>, List<PaymentCategory>> deletionProcessor() {
        return new EntityDeletionProcessor<>(paymentCategoryDeletionService);
    }

    @Bean(DELETION_WRITER_NAME)
    @StepScope
    public ItemWriter<? super List<PaymentCategory>> deletionWriter() {
        return new NoOpsItemWriter<>();
    }
}
