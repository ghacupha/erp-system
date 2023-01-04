package io.github.erp.internal.report.templates;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0-SNAPSHOT
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
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.files.JRXMLMultipartFile;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.service.PlaceholderQueryService;
import io.github.erp.service.criteria.PlaceholderCriteria;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.dto.ReportTemplateDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

/**
 * A simple dump-to-file template-presentation for the report-template-dto
 */
@Service
@Transactional
public class TemplatePresentation implements ReportTemplatePresentation<ReportTemplateDTO> {

    private final FileStorageService fileStorageService;
    private final ReportsProperties reportsProperties;
    private final PlaceholderQueryService placeholderQueryService;

    public TemplatePresentation(@Qualifier("reportsFSStorageService") final FileStorageService fileStorageService,
                                final ReportsProperties reportsProperties,
                                final PlaceholderQueryService placeholderQueryService) {
        this.fileStorageService = fileStorageService;
        this.reportsProperties = reportsProperties;
        this.placeholderQueryService = placeholderQueryService;
    }

    public String presentTemplate(ReportTemplateDTO reportTemplate) {

        PlaceholderCriteria placeholderCriteria = new PlaceholderCriteria();
        // The report-template needs to match the placeholder description
        StringFilter descriptionFilter = new StringFilter();
        descriptionFilter.setContains(reportTemplate.getCatalogueNumber());

        placeholderCriteria.setDescription(descriptionFilter);

        PlaceholderDTO plCandidate = null;

        if (!placeholderQueryService.findByCriteria(placeholderCriteria).isEmpty()) {
            plCandidate = placeholderQueryService.findByCriteria(placeholderCriteria).get(0);
        }

        if (plCandidate != null) {
            fileStorageService.save(
                new JRXMLMultipartFile(
                    reportTemplate.getReportFile(),
                    reportsProperties.getReportsDirectory(),
                    reportTemplate.getCatalogueNumber().concat(".jrxml")
                ),
                plCandidate.getToken()
            );
        } else {
            fileStorageService.save(
                new JRXMLMultipartFile(
                    reportTemplate.getReportFile(),
                    reportsProperties.getReportsDirectory(),
                    reportTemplate.getCatalogueNumber().concat(".jrxml")
                )
            );
        }

        return reportTemplate.getCatalogueNumber().concat(".jrxml");
    }
}
