package io.github.erp.internal.files.documents;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.model.BusinessDocumentFSO;
import io.github.erp.internal.report.attachment.AbstractFileAttachmentService;
import io.github.erp.internal.report.attachment.MatchesChecksum;
import io.github.erp.internal.report.attachment.RetrievedDocument;
import io.github.erp.service.dto.AlgorithmDTO;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class BusinessDocumentFileAttachmentService extends AbstractFileAttachmentService<BusinessDocumentFSO> implements FileAttachmentService<RetrievedDocument<BusinessDocumentFSO, AlgorithmDTO>> {


    public BusinessDocumentFileAttachmentService(@Qualifier("businessDocumentFSStorageService") FileStorageService fileStorageService, MatchesChecksum<AlgorithmDTO> matchesChecksum) {
        super(fileStorageService, matchesChecksum);
    }

    @Override
    public @NotNull RetrievedDocument<BusinessDocumentFSO, AlgorithmDTO> attach(RetrievedDocument<BusinessDocumentFSO, AlgorithmDTO> attachment) {
        return super.attach(attachment);
    }
}
