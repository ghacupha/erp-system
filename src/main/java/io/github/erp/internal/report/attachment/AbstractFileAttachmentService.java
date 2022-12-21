package io.github.erp.internal.report.attachment;

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
