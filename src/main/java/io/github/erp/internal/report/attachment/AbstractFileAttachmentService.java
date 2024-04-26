package io.github.erp.internal.report.attachment;

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
