package io.github.erp.internal.files.documents;

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
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.files.ReportsFilesSystemInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BusinessDocumentFilesSystemInit implements ApplicationListener<ApplicationReadyEvent> {


    private static final Logger log = LoggerFactory.getLogger(ReportsFilesSystemInit.class);

    private final FileStorageService storageService;

    public BusinessDocumentFilesSystemInit(@Qualifier("businessDocumentFSStorageService") FileStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {

        log.info("Initializing file-system for business-documents directory, and cleanup services. Standby...");

        // initialize storage
        storageService.init();

        log.info("File-system initialization sequence complete, reports directory is now restored and ready for instructions.");
    }
}
