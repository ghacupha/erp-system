package io.github.erp.internal.report;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.io.IOException;
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

        PdfReportRequisitionDTO attached = null;
        one.ifPresent(
            dto -> {
                byte[] reportResource = new byte[0];
                try {
                    reportResource = fileStorageService.load(dto.getReportId().toString().concat(".pdf")).getInputStream().readAllBytes();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                dto.setReportAttachment(reportResource);}
        );

        return one;
    }
}
