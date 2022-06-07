package io.github.erp.internal.report;

/*-
 * Erp System - Mark II No 7 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final static Logger log = LoggerFactory.getLogger(PDFAssemblyService.class);

    private final PDFReportsService simpleJasperReportsService;
    private final TemplatePresentation templatePresentation;

    public PDFAssemblyService(PDFReportsService simpleJasperReportsService, TemplatePresentation templatePresentation) {
        this.simpleJasperReportsService = simpleJasperReportsService;
        this.templatePresentation = templatePresentation;
    }

    @Override
    public String createReport(PdfReportRequisitionDTO dto, String fileExtension) {
        String fileName = templatePresentation.presentTemplate(dto.getReportTemplate());

        Map<String, Object> parameters = new HashMap<>();

        parameters.put("title", dto.getReportName());
        parameters.put("description", dto.getReportTemplate().getDescription());

        return simpleJasperReportsService.generateReport(
            fileName,
            dto.getReportId().toString().concat(fileExtension),
            dto.getOwnerPassword(),
            dto.getUserPassword(),
            parameters
        );
    }
}
