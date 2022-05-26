package io.github.erp.internal.report;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

/**
 * This services attaches a PDF report to a pdf-report-requisition-dto
 */
@Service
public class PDFReportAttachmentService implements ReportAttachmentService<Optional<PdfReportRequisitionDTO>> {

    private final static Logger log = LoggerFactory.getLogger(PDFReportAttachmentService.class);
    private final FileStorageService fileStorageService;

    public PDFReportAttachmentService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @SneakyThrows
    @Override
    public Optional<PdfReportRequisitionDTO> attachReport(Optional<PdfReportRequisitionDTO> one) {

        one.ifPresent(
            dto -> {
                log.debug("Fetching report name : {}", dto.getReportName());
                byte[] reportResource = new byte[0];
                try {
                    reportResource = fileStorageService.load(dto.getReportId().toString().concat(".pdf")).getInputStream().readAllBytes();
                } catch (IOException e) {
                    log.error("We were unable to find the generated report with id: " + dto.getReportId().toString().concat(".pdf"), e);
                }
                dto.setReportAttachment(reportResource);}
        );

        return one;
    }
}
