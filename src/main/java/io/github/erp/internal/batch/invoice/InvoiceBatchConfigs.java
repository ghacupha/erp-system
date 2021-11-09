package io.github.erp.internal.batch.invoice;

import com.google.common.collect.ImmutableList;
import io.github.erp.domain.Invoice;
import io.github.erp.internal.framework.BatchService;
import io.github.erp.internal.framework.FileUploadsProperties;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.framework.batch.*;
import io.github.erp.internal.framework.excel.ExcelFileDeserializer;
import io.github.erp.internal.framework.model.FileUploadHasDataFile;
import io.github.erp.internal.framework.service.DataFileContainer;
import io.github.erp.internal.framework.service.DeletionUploadService;
import io.github.erp.internal.model.InvoiceBEO;
import io.github.erp.internal.model.InvoiceEVM;
import io.github.erp.service.dto.InvoiceDTO;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
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
public class InvoiceBatchConfigs {

    private static final String PERSISTENCE_JOB_NAME = "invoiceListPersistenceJob";
    private static final String DELETION_JOB_NAME = "invoiceListDeletionJob";
    private static final String READ_FILE_STEP_NAME = "readInvoiceListFromFile";
    private static final String DELETION_STEP_NAME = "deleteInvoiceListFromFile";
    private static final String DELETION_PROCESSOR_NAME = "invoiceDeletionProcessor";
    private static final String DELETION_WRITER_NAME = "invoiceDeletionWriter";
    private static final String DELETION_READER_NAME = "invoiceDeletionReader";
    private static final String PERSISTENCE_READER_NAME = "invoiceListItemReader";
    private static final String PERSISTENCE_PROCESSOR_NAME = "invoiceListItemProcessor";
    private static final String PERSISTENCE_WRITER_NAME = "invoiceEntityListItemsWriter";

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
    private ExcelFileDeserializer<InvoiceEVM> invoiceDeserializer;

    @Autowired
    private BatchService<InvoiceDTO> batchService;

    @Autowired
    private DeletionService<Invoice> invoiceDeletionService;

    @Autowired
    private Mapping<InvoiceEVM, InvoiceDTO> mapping;

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private BatchPersistentFileUploadService fileUploadService;

    @Autowired
    private DeletionUploadService<InvoiceBEO> fileUploadDeletionService;

    @Autowired
    private DataFileContainer<FileUploadHasDataFile> dataFileContainer;

    @Bean(PERSISTENCE_READER_NAME)
    @JobScope
    public EntityItemsReader<InvoiceEVM> listItemReader(
        @Value("#{jobParameters['fileId']}") long fileId
    ) {
        return new EntityItemsReader<>(invoiceDeserializer, fileUploadService, fileId, fileUploadsProperties);
    }

    @Bean(PERSISTENCE_PROCESSOR_NAME)
    @JobScope
    public ItemProcessor<List<InvoiceEVM>, List<InvoiceDTO>> listItemsProcessor(
        @Value("#{jobParameters['messageToken']}") String jobUploadToken
    ) {
        return evms ->
            evms.stream().map(mapping::toValue2).peek(d -> d.setFileUploadToken(jobUploadToken)).collect(ImmutableList.toImmutableList());
    }

    @Bean(PERSISTENCE_WRITER_NAME)
    @JobScope
    public EntityListItemsWriter<InvoiceDTO> listItemsWriter() {
        return new EntityListItemsWriter<>(batchService);
    }

    @Bean(READ_FILE_STEP_NAME)
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

    // invoiceDeletionJob
    @Bean(DELETION_JOB_NAME)
    public Job invoiceDeletionJob() {
        return new SingleStepEntityJob(DELETION_JOB_NAME, deletionJobListener, deleteEntityListFromFile(), jobBuilderFactory);
    }

    // deleteInvoiceListFromFile step
    @Bean(DELETION_STEP_NAME)
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
    @JobScope
    public ItemReader<List<Long>> deletionReader(@Value("#{jobParameters['fileId']}") long fileId) {
        return new EntityItemsDeletionReader(fileId, fileUploadDeletionService, fileUploadsProperties, dataFileContainer);
    }

    @Bean(DELETION_PROCESSOR_NAME)
    public ItemProcessor<List<Long>, List<Invoice>> deletionProcessor() {
        return new EntityDeletionProcessor<>(invoiceDeletionService);
    }

    @Bean(DELETION_WRITER_NAME)
    public ItemWriter<? super List<Invoice>> deletionWriter() {
        return deletables -> {};
    }
}
