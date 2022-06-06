package io.github.erp.internal.report;

import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class XLSXAssemblyService implements ReportAssemblyService<XlsxReportRequisitionDTO> {


    private final static Logger log = LoggerFactory.getLogger(XLSXAssemblyService.class);

    private final TemplatePresentation templatePresentation;
    private final XLSXReportsService reportsService;

    public XLSXAssemblyService(TemplatePresentation templatePresentation, XLSXReportsService reportsService) {
        this.templatePresentation = templatePresentation;
        this.reportsService = reportsService;
    }

    @Override
    public String createReport(XlsxReportRequisitionDTO dto, String fileExtension) {
        String fileName = templatePresentation.presentTemplate(dto.getReportTemplate());

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("title", dto.getReportName());
        parameters.put("description", dto.getReportTemplate().getDescription());

        return reportsService.generateReport(
            fileName,
            dto.getReportId().toString().concat(fileExtension),
            dto.getUserPassword(),
            dto.getUserPassword(),
            parameters
        );

    }
}
