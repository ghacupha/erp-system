package io.github.erp.internal.report;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.files.JRXMLMultipartFile;
import io.github.erp.internal.files.PDFMultipartFile;
import io.github.erp.service.PlaceholderQueryService;
import io.github.erp.service.criteria.PlaceholderCriteria;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.dto.ReportTemplateDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.filter.StringFilter;

@Service
@Transactional
public class TemplatePresentation implements ReportTemplatePresentation<ReportTemplateDTO> {

    private final FileStorageService fileStorageService;
    private final ReportsProperties reportsProperties;
    private final PlaceholderQueryService placeholderQueryService;

    public TemplatePresentation(final FileStorageService fileStorageService,
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
                new PDFMultipartFile(
                    reportTemplate.getReportFile(),
                    reportsProperties.getReportsDirectory(),
                    reportTemplate.getCatalogueNumber().concat(".jrxml")
                )
            );
        }

        return reportTemplate.getCatalogueNumber().concat(".jrxml");
    }
}
