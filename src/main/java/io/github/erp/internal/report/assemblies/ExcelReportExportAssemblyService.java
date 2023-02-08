package io.github.erp.internal.report.assemblies;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.6.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.internal.report.templates.ReportTemplatePresentation;
import io.github.erp.service.ReportDesignService;
import io.github.erp.service.dto.ExcelReportExportDTO;
import io.github.erp.service.dto.ReportDesignDTO;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * Uses instructions from the excel-report-export entity to create a Jasper report in excel format using the
 * template from the report-design model
 */
@Service
public class ExcelReportExportAssemblyService implements ReportAssemblyService<ExcelReportExportDTO> {


    private final ReportTemplatePresentation<ReportDesignDTO> templatePresentation;
    private final XLSXReportsService reportsService;
    private final ReportDesignService reportDesignService;

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
