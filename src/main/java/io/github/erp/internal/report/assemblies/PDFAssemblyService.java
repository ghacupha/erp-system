package io.github.erp.internal.report.assemblies;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.7-SNAPSHOT
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
import io.github.erp.internal.report.templates.TemplatePresentation;
import io.github.erp.service.ReportTemplateService;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * This service asynchronously creates a PDF report as the method is called on the report resource
 */
@Service
@Transactional
public class PDFAssemblyService implements ReportAssemblyService<PdfReportRequisitionDTO> {

    private final PDFReportsService simpleJasperReportsService;
    private final TemplatePresentation templatePresentation;
    private final ReportTemplateService reportTemplateService;

    public PDFAssemblyService(PDFReportsService simpleJasperReportsService, TemplatePresentation templatePresentation, ReportTemplateService reportTemplateService) {
        this.simpleJasperReportsService = simpleJasperReportsService;
        this.templatePresentation = templatePresentation;
        this.reportTemplateService = reportTemplateService;
    }

    @Override
    public String createReport(PdfReportRequisitionDTO dto, String fileExtension) {

        Map<String, Object> parameters = new HashMap<>();
        final String[] fileName = {""};

        reportTemplateService.findOne(dto.getReportTemplate().getId()).ifPresent(template -> {

            fileName[0] = templatePresentation.presentTemplate(template);

            parameters.put("title", dto.getReportName());
            parameters.put("description", dto.getReportTemplate().getDescription());

            dto.getParameters().forEach(p -> {
                parameters.put(p.getUniversalKey(), p.getMappedValue());
            });
        });

        return simpleJasperReportsService.generateReport(
            fileName[0],
            dto.getReportId().toString().concat(fileExtension),
            dto.getOwnerPassword(),
            dto.getUserPassword(),
            parameters
        );
    }
}
