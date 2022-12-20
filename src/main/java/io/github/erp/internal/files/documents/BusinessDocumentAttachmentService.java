package io.github.erp.internal.files.documents;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.8-SNAPSHOT
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

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.model.BusinessDocumentFSO;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;


import static io.github.erp.internal.files.FileUtils.getFileExtension;

/**
 * Service to attach document to the FileSystemObject during a get call
 */
@Service
@Qualifier("businessDocumentAttachmentService")
public class BusinessDocumentAttachmentService implements FileAttachmentService<BusinessDocumentFSO> {

    private final static Logger log = LoggerFactory.getLogger(BusinessDocumentAttachmentService.class);
    private final FileStorageService fileStorageService;

    public BusinessDocumentAttachmentService(
        @Qualifier("businessDocumentFSStorageService") FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @SneakyThrows
    @Override
    public BusinessDocumentFSO attach(BusinessDocumentFSO one) {
        String filename = one.getDocumentSerial()+"."+getFileExtension(one.getDocumentFileContentType());

        log.debug("Report designation {} has been mapped successfully for attachment. Commencing attachment", filename);

        long startup = System.currentTimeMillis();

        log.debug("Fetching business document name : {}", one.getDocumentSerial());

        log.debug("Fetching report named : {}", one.getDocumentSerial());


        byte[] reportAttachment = fileStorageService.load(filename).getInputStream().readAllBytes();
        String md5CheckSum = fileStorageService.calculateMD5CheckSum(filename);

        log.debug("Attaching document retrieved to DTO designation : {} ", one.getDocumentSerial());
        one.setDocumentFile(reportAttachment);
        // TODO checksum verification one.setReportFileCheckSum("MD5 :" + md5CheckSum);

        log.debug("Report attachment completed successfully in {} milliseconds; sending attached report to the client  ", System.currentTimeMillis() - startup);
        return one;
    }
}
