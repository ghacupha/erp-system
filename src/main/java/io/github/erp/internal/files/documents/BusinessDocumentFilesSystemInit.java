package io.github.erp.internal.files.documents;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
