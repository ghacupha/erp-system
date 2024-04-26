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
import io.github.erp.internal.model.BusinessDocumentFSO;
import io.github.erp.internal.model.mapping.BusinessDocumentFSOMapping;
import io.github.erp.internal.report.attachment.AbstractFileAttachmentService;
import io.github.erp.internal.report.attachment.MatchesChecksum;
import io.github.erp.service.BusinessDocumentService;
import io.github.erp.service.dto.AlgorithmDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BusinessDocumentFileAttachmentService extends AbstractFileAttachmentService implements FileAttachmentService<BusinessDocumentFSO> {


    public BusinessDocumentFileAttachmentService(
        @Qualifier("businessDocumentFSStorageService") FileStorageService fileStorageService,
        MatchesChecksum<AlgorithmDTO> matchesChecksum,
        BusinessDocumentService businessDocumentService,
        BusinessDocumentFSOMapping businessDocumentFSOMapping) {
        super(fileStorageService, matchesChecksum, businessDocumentService, businessDocumentFSOMapping);
    }

    @Override
    public @NotNull BusinessDocumentFSO attach(BusinessDocumentFSO attachment) {
        return super.attach(attachment);
    }
}
