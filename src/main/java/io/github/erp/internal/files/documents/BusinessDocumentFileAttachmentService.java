package io.github.erp.internal.files.documents;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
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
