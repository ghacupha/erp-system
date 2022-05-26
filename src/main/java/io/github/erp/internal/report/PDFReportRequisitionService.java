package io.github.erp.internal.report;

import io.github.erp.service.dto.PdfReportRequisitionDTO;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This service asynchronously creates a PDF report as the method is called on the report resource
 */
@Service
@Transactional
public class PDFReportRequisitionService implements ReportRequisitionService<PdfReportRequisitionDTO> {

    private final ReportTemplatePresentation<PdfReportRequisitionDTO> reportTemplatePresentation;
    private final SimpleJasperReportsService simpleJasperReportsService;

    public PDFReportRequisitionService(ReportTemplatePresentation<PdfReportRequisitionDTO> reportTemplatePresentation, SimpleJasperReportsService simpleJasperReportsService) {
        this.reportTemplatePresentation = reportTemplatePresentation;
        this.simpleJasperReportsService = simpleJasperReportsService;
    }

    @Async
    @Override
    public void createReport(PdfReportRequisitionDTO dto) {
        String fileName = reportTemplatePresentation.presentTemplate(dto);
        // todo implement report-template presentation
        String reportPath =
            simpleJasperReportsService.generatePDFReport(
                fileName,
                dto.getReportId().toString().concat(".pdf"),
                dto.getOwnerPassword(),
                dto.getUserPassword());
    }
}
