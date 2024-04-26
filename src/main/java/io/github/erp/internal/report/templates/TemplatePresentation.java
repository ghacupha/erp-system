package io.github.erp.internal.report.templates;

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
