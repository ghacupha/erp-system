package io.github.erp.internal.report;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import lombok.SneakyThrows;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PDFReportAttachmentService implements ReportAttachmentService<Optional<PdfReportRequisitionDTO>> {

    private final FileStorageService fileStorageService;

    public PDFReportAttachmentService(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @SneakyThrows
    @Override
    public Optional<PdfReportRequisitionDTO> attachReport(Optional<PdfReportRequisitionDTO> one) {

        byte[] reportResource = fileStorageService.load("employeeReport.pdf").getInputStream().readAllBytes();

        PdfReportRequisitionDTO attached = null;
        one.ifPresent(
            pdfReportRequisitionDTO -> pdfReportRequisitionDTO.setReportAttachment(reportResource)
        );

        return one;
    }
}
