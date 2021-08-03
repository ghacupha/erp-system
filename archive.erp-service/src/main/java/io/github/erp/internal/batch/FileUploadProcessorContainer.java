package io.github.erp.internal.batch;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

    @Bean("fileUploadProcessorChain")
    public FileUploadProcessorChain fileUploadProcessorChain() {

        FileUploadProcessorChain theChain = new FileUploadProcessorChain();

        // Create the chain, each should match against it's specific key of data-model type
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetAcquisitionListPersistenceJob, FIXED_ASSET_ACQUISITION));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetDepreciationListPersistenceJob, FIXED_ASSET_DEPRECIATION));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetNetBookValueListPersistenceJob, FIXED_ASSET_NBV));
        return theChain;
    }

    // configuration for deletion jobs
    @Bean("fileUploadDeletionProcessorChain")
    public FileUploadProcessorChain fileUploadDeletionProcessorChain() {
        FileUploadProcessorChain theChain = new FileUploadProcessorChain();

        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetAcquisitionListDeletionJob, FIXED_ASSET_ACQUISITION));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetDepreciationListDeletionJob, FIXED_ASSET_DEPRECIATION));
        theChain.addProcessor(new BatchSupportedFileUploadProcessor(jobLauncher, fixedAssetNetBookValueListDeletionJob, FIXED_ASSET_NBV));

        return theChain;
    }

}
