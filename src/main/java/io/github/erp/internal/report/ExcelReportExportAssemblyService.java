package io.github.erp.internal.report;

import io.github.erp.service.ReportDesignService;
import io.github.erp.service.dto.ExcelReportExportDTO;
import io.github.erp.service.dto.ReportDesignDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Uses instructions from the excel-report-export to create a Jasper report in excel format using the
 * template from the report-design model
 */
@Service
public class ExcelReportExportAssemblyService implements ReportAssemblyService<ExcelReportExportDTO> {


    private final ReportTemplatePresentation<ReportDesignDTO> templatePresentation;
    private final XLSXReportsService reportsService;
    private final ReportDesignService reportDesignService;

    // private final ReportDesignRepository reportDesignRepository;

    public ExcelReportExportAssemblyService(ReportTemplatePresentation<ReportDesignDTO> templatePresentation, XLSXReportsService reportsService, ReportDesignService reportDesignService) {
        this.templatePresentation = templatePresentation;
        this.reportsService = reportsService;
        this.reportDesignService = reportDesignService;
    }

    @Override
    public String createReport(ExcelReportExportDTO dto, String fileExtension) {
        Map<String, Object> parameters = new HashMap<>();
        final String[] fileName = {""};

        reportDesignService.findOne(dto.getReportDesign().getId()).ifPresent(template -> {

            fileName[0] = templatePresentation.presentTemplate(template);

            parameters.put("title", dto.getReportName());
            parameters.put("description", dto.getReportDesign().getDescription());

            dto.getParameters().forEach(p -> {
                parameters.put(p.getUniversalKey(), p.getMappedValue());
            });
        });

        return reportsService.generateReport(
            fileName[0],
            dto.getReportId().toString().concat(fileExtension),
            dto.getReportPassword(),
            dto.getReportPassword(),
            parameters
        );
    }
}
