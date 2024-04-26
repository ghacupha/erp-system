package io.github.erp.internal.batch;

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
import io.github.erp.internal.framework.fileProcessing.BatchSupportedFileUploadProcessor;
import io.github.erp.internal.framework.fileProcessing.FileUploadProcessorChain;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.github.erp.domain.enumeration.FileModelType.*;


/**
 * This object maintains a list of all existing processors. This is a short in the dark about automatically configuring the chain at start up
 */
@Configuration
public class FileUploadProcessorContainer {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("fixedAssetAcquisitionListPersistenceJob")
    private Job fixedAssetAcquisitionListPersistenceJob;

    @Autowired
    @Qualifier("fixedAssetAcquisitionListDeletionJob")
    private Job fixedAssetAcquisitionListDeletionJob;

    @Autowired
    @Qualifier("fixedAssetDepreciationListPersistenceJob")
    private Job fixedAssetDepreciationListPersistenceJob;

    @Autowired
    @Qualifier("fixedAssetDepreciationListDeletionJob")
    private Job fixedAssetDepreciationListDeletionJob;

    @Autowired
    @Qualifier("fixedAssetNetBookValueListPersistenceJob")
    private Job fixedAssetNetBookValueListPersistenceJob;

    @Autowired
    @Qualifier("fixedAssetNetBookValueListDeletionJob")
    private Job fixedAssetNetBookValueListDeletionJob;

    @Autowired
    @Qualifier("paymentLabelListPersistenceJob")
    private Job paymentLabelListPersistenceJob;

    @Autowired
    @Qualifier("paymentLabelListDeletionJob")
    private Job paymentLabelListDeletionJob;

    @Autowired
    @Qualifier("paymentCategoryListPersistenceJob")
    private Job paymentCategoryListPersistenceJob;

    @Autowired
    @Qualifier("paymentCategoryListDeletionJob")
    private Job paymentCategoryListDeletionJob;

    @Autowired
    @Qualifier("paymentListPersistenceJob")
    private Job paymentListPersistenceJob;

    @Autowired
    @Qualifier("paymentListDeletionJob")
    private Job paymentListDeletionJob;

    @Autowired
    @Qualifier("dealerListPersistenceJob")
    private Job dealerListPersistenceJob;

    @Autowired
    @Qualifier("dealerListDeletionJob")
    private Job dealerListDeletionJob;

    @Autowired
    @Qualifier("signedPaymentListPersistenceJob")
    private Job signedPaymentListPersistenceJob;

    @Autowired
    @Qualifier("signedPaymentListDeletionJob")
    private Job signedPaymentListDeletionJob;

    @Autowired
    @Qualifier("invoiceListPersistenceJob")
    private Job invoiceListPersistenceJob;

    @Autowired
    @Qualifier("invoiceListDeletionJob")
    private Job invoiceListDeletionJob;

    @Bean("fileUploadProcessorChain")
    public FileUploadProcessorChain fileUploadProcessorChain() {

        FileUploadProcessorChain theChain = new FileUploadProcessorChain();

        // Create the chain, each should match against it's specific key of data-model type
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetAcquisitionListPersistenceJob, FIXED_ASSET_ACQUISITION));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetDepreciationListPersistenceJob, FIXED_ASSET_DEPRECIATION));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetNetBookValueListPersistenceJob, FIXED_ASSET_NBV));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, paymentLabelListPersistenceJob, PAYMENT_LABEL));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, paymentCategoryListPersistenceJob, PAYMENT_CATEGORY));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, paymentListPersistenceJob, PAYMENT));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, dealerListPersistenceJob, DEALER));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, signedPaymentListPersistenceJob, SIGNED_PAYMENT));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, invoiceListPersistenceJob, INVOICE));
        return theChain;
    }

    // configuration for deletion jobs
    @Bean("fileUploadDeletionProcessorChain")
    public FileUploadProcessorChain fileUploadDeletionProcessorChain() {
        FileUploadProcessorChain theChain = new FileUploadProcessorChain();

        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetAcquisitionListDeletionJob, FIXED_ASSET_ACQUISITION));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetDepreciationListDeletionJob, FIXED_ASSET_DEPRECIATION));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetNetBookValueListDeletionJob, FIXED_ASSET_NBV));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, paymentLabelListDeletionJob, PAYMENT_LABEL));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, paymentCategoryListDeletionJob, PAYMENT_CATEGORY));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, paymentListDeletionJob, PAYMENT));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, dealerListDeletionJob, DEALER));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, signedPaymentListDeletionJob, SIGNED_PAYMENT));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, invoiceListDeletionJob, INVOICE));

        return theChain;
    }

}
