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
