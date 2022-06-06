package io.github.erp.internal.report;

import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.Map;

public class XLSXReportRequisitionService  implements ReportRequisitionService<XlsxReportRequisitionDTO> {


    private final static Logger log = LoggerFactory.getLogger(XLSXReportRequisitionService.class);

    private final ReportTemplatePresentation<XlsxReportRequisitionDTO> reportTemplatePresentation;
    private final XLSXReportsService reportsService;

    public XLSXReportRequisitionService(ReportTemplatePresentation<XlsxReportRequisitionDTO> reportTemplatePresentation, XLSXReportsService reportsService) {
        this.reportTemplatePresentation = reportTemplatePresentation;
        this.reportsService = reportsService;
    }

    @Async
    @Override
    public void createReport(XlsxReportRequisitionDTO dto) {
        String fileName = reportTemplatePresentation.presentTemplate(dto);

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("title", dto.getReportName());
        parameters.put("description", dto.getReportTemplate().getDescription());

        String reportPath =
            reportsService.generateReport(
                fileName,
                dto.getReportId().toString().concat(".xlsx"),
                dto.getUserPassword(),
                dto.getUserPassword(),
                parameters
            );

        log.debug("The report is successfully generated on the path: {}", reportPath);
    }
}
