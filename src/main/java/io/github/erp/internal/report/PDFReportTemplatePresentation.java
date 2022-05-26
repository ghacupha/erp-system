package io.github.erp.internal.report;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.files.PDFMultipartFile;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
import org.springframework.stereotype.Component;

/**
 * Selects the appropriate report in accordance with the report requisition and saves it into an
 * accessible place in the file system
 */
@Component
public class PDFReportTemplatePresentation implements ReportTemplatePresentation<PdfReportRequisitionDTO> {

    private final FileStorageService fileStorageService;

    // TODO Externalize this configuration
    private static final String DEST_PATH = "reports-directory/";


    public PDFReportTemplatePresentation(FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    public String presentTemplate(PdfReportRequisitionDTO dto) {

        fileStorageService.save(
            new PDFMultipartFile(
                dto.getReportTemplate().getReportFile(),
                DEST_PATH,
                dto.getReportTemplate().getCatalogueNumber().concat(".jrxml")
            )
        );

        return dto.getReportTemplate().getCatalogueNumber().concat(".jrxml");
    }
}
