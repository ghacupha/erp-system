package io.github.erp.internal.report.attachment;

import io.github.erp.internal.files.FileStorageService;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This attachment service runs a check on the file and compares the same with the
 * SHA512 checksum from database to see if the file saved in the database is different
 * from the one created by the system albeit having the same filename
 */
public class AbstractUnTamperedReportAttachmentService<DTO> {

    private static final Logger log = LoggerFactory.getLogger("UnTamperedReportAttachmentService");

    private final FileStorageService fileStorageService;

    public AbstractUnTamperedReportAttachmentService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @NotNull
    @SneakyThrows
    public AttachedUnTamperedReport<DTO> attachReport(AttachedUnTamperedReport<DTO> one, String fileExtension) {
        log.debug("Report designation {} has been mapped successfully for attachment. Commencing attachment", one.getReportName());

        long startup = System.currentTimeMillis();

        log.debug("Fetching report name : {}", one.getReportName());

        String reportFileName = one.getReportId().toString().concat(fileExtension);

        log.debug("Fetching report named : {}", reportFileName);

        byte[] reportAttachment = fileStorageService.load(reportFileName).getInputStream().readAllBytes();

        log.debug("Attaching report retrieved to DTO designation : {} ", one.getReportName());
        one.setReportAttachment(reportAttachment);

        one.setReportTampered(!fileStorageService.fileRemainsUnTampered(reportFileName, one.getFileChecksum()));

        log.debug("Report attachment completed successfully in {} milliseconds; sending attached report to the client  ", System.currentTimeMillis() - startup);
        return one;
    }
}
