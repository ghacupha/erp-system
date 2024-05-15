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

import io.github.erp.domain.Payment;
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
import io.github.erp.internal.model.PaymentBEO;
import io.github.erp.internal.model.PaymentEVM;
import io.github.erp.service.dto.PaymentDTO;
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
public class ROUDepreciationEntryBatchConfigs {

    public static final String PERSISTENCE_JOB_NAME = "rouDepreciationEntryPersistenceJob";
    private static final String DELETION_JOB_NAME = "rouDepreciationEntryDeletionJob";
    private static final String READ_FILE_STEP_NAME = "readROUModelMetadataFromDB";
    private static final String DELETION_STEP_NAME = "deleteROUDepreciationEntryItemsFromDB";
    private static final String DELETION_PROCESSOR_NAME = "rouDepreciationEntryDeletionProcessor";
    private static final String DELETION_WRITER_NAME = "rouDepreciationEntryDeletionWriter";
    private static final String DELETION_READER_NAME = "rouDepreciationEntryDeletionReader";
    private static final String PERSISTENCE_READER_NAME = "rouDepreciationEntryItemReader";
    private static final String PERSISTENCE_PROCESSOR_NAME = "rouDepreciationEntryItemProcessor";
    private static final String PERSISTENCE_WRITER_NAME = "rouDepreciationEntryListItemsWriter";

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
    private ExcelFileDeserializer<PaymentEVM> paymentDeserializer;

    @Autowired
    private BatchService<PaymentDTO> batchService;

    @Autowired
    private DeletionService<Payment> paymentDeletionService;

    @Autowired
    private Mapping<PaymentEVM, PaymentDTO> mapping;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BatchPersistentFileUploadService fileUploadService;

    @Autowired
    private DeletionUploadService<PaymentBEO> fileUploadDeletionService;

    @Autowired
    private DataFileContainer<FileUploadHasDataFile> dataFileContainer;

    @Bean(PERSISTENCE_READER_NAME)
    @StepScope
    public EntityItemsReader<PaymentEVM> listItemReader(@Value("#{jobParameters['fileId']}") long fileId ) {
        return new EntityItemsReader<>(paymentDeserializer, fileUploadService, fileId, fileUploadsProperties);
    }

    @Bean(PERSISTENCE_PROCESSOR_NAME)
    @StepScope
    public ItemProcessor<List<PaymentEVM>, List<PaymentDTO>> listItemsProcessor(@Value("#{jobParameters['messageToken']}") String jobUploadToken) {
        return new PaymentPersistenceProcessor(mapping, jobUploadToken);
    }

    @Bean(PERSISTENCE_WRITER_NAME)
    @StepScope
    public EntityListItemsWriter<PaymentDTO> listItemsWriter() {
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

    // paymentDeletionJob
    @Bean(DELETION_JOB_NAME)
    public Job paymentDeletionJob() {
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
    public ItemProcessor<List<Long>, List<Payment>> deletionProcessor() {
        return new EntityDeletionProcessor<>(paymentDeletionService);
    }

    @Bean(DELETION_WRITER_NAME)
    @StepScope
    public ItemWriter<? super List<Payment>> deletionWriter() {
        return new NoOpsItemWriter<>();
    }
}
