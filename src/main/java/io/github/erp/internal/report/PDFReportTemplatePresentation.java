package io.github.erp.internal.report;

/*-
 * Erp System - Mark II No 4 (Artaxerxes Series)
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

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.files.JRXMLMultipartFile;
import io.github.erp.internal.files.PDFMultipartFile;
import io.github.erp.service.PlaceholderQueryService;
import io.github.erp.service.criteria.PlaceholderCriteria;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import io.github.erp.service.dto.PlaceholderDTO;
import org.springframework.stereotype.Component;
import tech.jhipster.service.filter.StringFilter;

import java.util.Objects;

/**
 * Selects the appropriate report in accordance with the report requisition and saves it into an
 * accessible place in the file system
 */
@Component
public class PDFReportTemplatePresentation implements ReportTemplatePresentation<PdfReportRequisitionDTO> {

    private final FileStorageService fileStorageService;
    private final ReportsProperties reportsProperties;
    private final PlaceholderQueryService placeholderQueryService;

    public PDFReportTemplatePresentation(FileStorageService fileStorageService, ReportsProperties reportsProperties, PlaceholderQueryService placeholderQueryService) {
        this.fileStorageService = fileStorageService;
        this.reportsProperties = reportsProperties;
        this.placeholderQueryService = placeholderQueryService;
    }

    @Override
    public String presentTemplate(PdfReportRequisitionDTO dto) {

        PlaceholderCriteria placeholderCriteria = new PlaceholderCriteria();
        // The report-template needs to match the placeholder description
        StringFilter descriptionFilter = new StringFilter();
        descriptionFilter.setContains(dto.getReportTemplate().getCatalogueNumber());

        placeholderCriteria.setDescription(descriptionFilter);

        PlaceholderDTO plCandidate = null;

        if (!placeholderQueryService.findByCriteria(placeholderCriteria).isEmpty()) {
            plCandidate = placeholderQueryService.findByCriteria(placeholderCriteria).get(0);
        }

        if (plCandidate != null) {
            fileStorageService.save(
                new JRXMLMultipartFile(
                    dto.getReportTemplate().getReportFile(),
                    reportsProperties.getReportsDirectory(),
                    dto.getReportTemplate().getCatalogueNumber().concat(".jrxml")
                ),
                plCandidate.getToken()
            );
        } else {
            fileStorageService.save(
                new PDFMultipartFile(
                    dto.getReportTemplate().getReportFile(),
                    reportsProperties.getReportsDirectory(),
                    dto.getReportTemplate().getCatalogueNumber().concat(".jrxml")
                )
            );
        }

        return dto.getReportTemplate().getCatalogueNumber().concat(".jrxml");
    }
}
