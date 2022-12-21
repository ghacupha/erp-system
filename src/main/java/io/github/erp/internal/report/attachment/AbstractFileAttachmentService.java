package io.github.erp.internal.report.attachment;

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
import io.github.erp.internal.files.FileUtils;
import io.github.erp.internal.files.documents.FileAttachmentService;
import io.github.erp.service.dto.AlgorithmDTO;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFileAttachmentService<FSO> implements FileAttachmentService<RetrievedDocument<FSO, AlgorithmDTO>> {

    private static final Logger log = LoggerFactory.getLogger("BusinessDocumentRetrieval");

    private final FileStorageService fileStorageService;
    private final MatchesChecksum<AlgorithmDTO> matchesChecksum;

    public AbstractFileAttachmentService(FileStorageService fileStorageService, MatchesChecksum<AlgorithmDTO> matchesChecksum) {
        this.fileStorageService = fileStorageService;
        this.matchesChecksum = matchesChecksum;
    }

    @NotNull
    @SneakyThrows
    public RetrievedDocument<FSO, AlgorithmDTO> attach(RetrievedDocument<FSO, AlgorithmDTO> attachment) {
        log.debug("File designation {} has been mapped successfully for attachment. Commencing attachment", attachment.getDocumentTitle());

        long startup = System.currentTimeMillis();

        log.debug("Fetching document name : {}", attachment.getDocumentTitle());

        String documentFileName = attachment.getDocumentSerial().toString().concat(".").concat(FileUtils.getFileExtension(attachment.getFileContentType()));

        log.debug("Fetching report named : {}", documentFileName);

        byte[] reportAttachment = fileStorageService.load(documentFileName).getInputStream().readAllBytes();

        log.debug("Attaching document retrieved to DTO designation : {} ", attachment.getDocumentTitle());
        attachment.setDocumentFile(reportAttachment);

        attachment.isTampered(!matchesChecksum.checksumIsMatching(attachment.getFileChecksum(), documentFileName, attachment.getChecksumAlgorithm()));

        log.debug("File attachment completed successfully in {} milliseconds; sending attached report to the client  ", System.currentTimeMillis() - startup);

        return attachment;
    }
}
