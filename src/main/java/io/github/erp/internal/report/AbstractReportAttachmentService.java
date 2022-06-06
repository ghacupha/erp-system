package io.github.erp.internal.report;

import io.github.erp.internal.files.FileStorageService;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AbstractReportAttachmentService<DTO> {

    private static final Logger log = LoggerFactory.getLogger("ReportAttachmentService");

    private final FileStorageService fileStorageService;

    public AbstractReportAttachmentService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @NotNull
    @SneakyThrows
    public AttachedReport<DTO> attachReport(AttachedReport<DTO> one) {
        log.debug("Report designation {} has been mapped successfully for attachment. Commencing attachment", one.getReportName());

        long startup = System.currentTimeMillis();

        log.debug("Fetching report name : {}", one.getReportName());

        String reportFileName = one.getReportId().toString().concat(".pdf");

        log.debug("Fetching report named : {}", reportFileName);

        byte[] reportAttachment = fileStorageService.load(reportFileName).getInputStream().readAllBytes();

        log.debug("Attaching report retrieved to DTO designation : {} ", one.getReportName());
        one.setReportAttachment(reportAttachment);

        log.debug("Report attachment completed successfully in {} milliseconds; sending attached report to the client  ", System.currentTimeMillis() - startup);
        return one;
    }
}
