package io.github.erp.internal.report;

import io.github.erp.service.dto.PdfReportRequisitionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * This service asynchronously creates a PDF report as the method is called on the report resource
 */
@Service
@Transactional
public class PDFReportRequisitionService implements ReportRequisitionService<PdfReportRequisitionDTO> {

    private final static Logger log = LoggerFactory.getLogger(PDFReportRequisitionService.class);

    private final ReportTemplatePresentation<PdfReportRequisitionDTO> reportTemplatePresentation;
    private final PDFReportsService simpleJasperReportsService;

    public PDFReportRequisitionService(ReportTemplatePresentation<PdfReportRequisitionDTO> reportTemplatePresentation, PDFReportsService simpleJasperReportsService) {
        this.reportTemplatePresentation = reportTemplatePresentation;
        this.simpleJasperReportsService = simpleJasperReportsService;
    }

    @Async
    @Override
    public void createReport(PdfReportRequisitionDTO dto) {
        String fileName = reportTemplatePresentation.presentTemplate(dto);

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("title", dto.getReportName());
        parameters.put("description", dto.getReportTemplate().getDescription());

        String reportPath =
            simpleJasperReportsService.generateReport(
                fileName,
                dto.getReportId().toString().concat(".pdf"),
                dto.getOwnerPassword(),
                dto.getUserPassword(),
                parameters
            );

        log.debug("The report is successfully generated on the path: {}", reportPath);
    }
}
