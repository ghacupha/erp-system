package io.github.erp.internal.report;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.files.JRXMLMultipartFile;
import io.github.erp.service.dto.ReportDesignDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * This services checks the repository for the report templated previously saved and creates the same
 * on the file-system using the file-storage-service.
 */
@Service
@Transactional
public class ReportDesignTemplatePresentation implements ReportTemplatePresentation<ReportDesignDTO>  {

    private final FileStorageService fileStorageService;
    private final ReportsProperties reportsProperties;

    public ReportDesignTemplatePresentation(final FileStorageService fileStorageService,
                                final ReportsProperties reportsProperties) {
        this.fileStorageService = fileStorageService;
        this.reportsProperties = reportsProperties;
    }

    public String presentTemplate(ReportDesignDTO reportTemplate) {

            fileStorageService.save(
                new JRXMLMultipartFile(
                    reportTemplate.getReportFile(),
                    reportsProperties.getReportsDirectory(),
                    reportTemplate.getCatalogueNumber().toString().concat(".jrxml")
                )
            );


        return reportTemplate.getCatalogueNumber().toString().concat(".jrxml");
    }
}
