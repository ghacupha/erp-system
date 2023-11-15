package io.github.erp.internal.report.attachment;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.internal.model.BusinessDocumentFSO;
import io.github.erp.internal.model.mapping.BusinessDocumentFSOMapping;
import io.github.erp.service.BusinessDocumentService;
import io.github.erp.service.dto.AlgorithmDTO;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractFileAttachmentService implements FileAttachmentService<BusinessDocumentFSO> {

    private static final Logger log = LoggerFactory.getLogger("BusinessDocumentRetrieval");

    private final FileStorageService fileStorageService;
    private final MatchesChecksum<AlgorithmDTO> matchesChecksum;
    private final BusinessDocumentService businessDocumentService;
    private final BusinessDocumentFSOMapping businessDocumentFSOMapping;

    public AbstractFileAttachmentService(FileStorageService fileStorageService, MatchesChecksum<AlgorithmDTO> matchesChecksum, BusinessDocumentService businessDocumentService, BusinessDocumentFSOMapping businessDocumentFSOMapping) {
        this.fileStorageService = fileStorageService;
        this.matchesChecksum = matchesChecksum;
        this.businessDocumentService = businessDocumentService;
        this.businessDocumentFSOMapping = businessDocumentFSOMapping;
    }

    @NotNull
    @SneakyThrows
    public BusinessDocumentFSO attach(BusinessDocumentFSO businessDocument) {
        log.debug("File designation {} has been mapped successfully for attachment. Commencing attachment", businessDocument.getDocumentTitle());

        long startup = System.currentTimeMillis();

        log.debug("Fetching document name : {}", businessDocument.getDocumentTitle());

        String documentFileName = businessDocument.getDocumentSerial().toString().concat(".").concat(FileUtils.getFileExtension(businessDocument.getFileContentType()));

        log.debug("Fetching document named : {}", documentFileName);

        BusinessDocumentFSO updated = businessDocumentFSOMapping.toValue1(businessDocumentService.save(businessDocumentFSOMapping.toValue2(businessDocument.isTampered(matchesChecksum.checksumIsMatching(businessDocument.getFileChecksum(), documentFileName, businessDocument.getChecksumAlgorithm())))));

        log.debug("File attachment completed successfully in {} milliseconds; sending attached report to the client  ", System.currentTimeMillis() - startup);

        byte[] reportAttachment = fileStorageService.load(documentFileName).getInputStream().readAllBytes();
        log.debug("Attaching document retrieved to DTO designation : {} ", businessDocument.getDocumentTitle());
        updated.setDocumentFile(reportAttachment);

        return updated;
    }
}
