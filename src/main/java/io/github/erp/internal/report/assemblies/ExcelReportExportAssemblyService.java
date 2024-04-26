package io.github.erp.internal.report.assemblies;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.internal.report.templates.ReportTemplatePresentation;
import io.github.erp.service.ReportDesignService;
import io.github.erp.service.UniversallyUniqueMappingService;
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
    private final UniversallyUniqueMappingService mappingService;

    public ExcelReportExportAssemblyService(ReportTemplatePresentation<ReportDesignDTO> templatePresentation, XLSXReportsService reportsService, ReportDesignService reportDesignService, UniversallyUniqueMappingService mappingService) {
        this.templatePresentation = templatePresentation;
        this.reportsService = reportsService;
        this.reportDesignService = reportDesignService;
        this.mappingService = mappingService;
    }

    @Override
    public String createReport(ExcelReportExportDTO dto, String fileExtension) {
        Map<String, Object> parameters = new HashMap<>();
        final String[] fileName = {""};

        // TODO Get parameters from DTO object

        reportDesignService.findOne(dto.getReportDesign().getId()).ifPresent(template -> {

            fileName[0] = templatePresentation.presentTemplate(template);

            parameters.put("title", dto.getReportName());
            parameters.put("description", dto.getReportDesign().getDescription());

            dto.getParameters().forEach(p -> {
                // TODO Use parameter service to fetch actual instances from the DB
                mappingService.findOne(p.getId()).ifPresent(map -> parameters.put(p.getUniversalKey(), p.getMappedValue()));
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
