package io.github.erp.internal.files;

import io.github.erp.internal.report.PDFReportRequisitionService;
import io.github.erp.service.PdfReportRequisitionService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FilesSystemInit implements ApplicationListener<ApplicationReadyEvent> {

    private final FileStorageService storageService;
    private final PdfReportRequisitionService pdfReportRequisitionService;

    public FilesSystemInit(FileStorageService storageService, PdfReportRequisitionService pdfReportRequisitionService) {
        this.storageService = storageService;
        this.pdfReportRequisitionService = pdfReportRequisitionService;
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        // Delete all report entities from the system
        pdfReportRequisitionService.findAll(Pageable.unpaged())
            .forEach((report) -> pdfReportRequisitionService.delete(report.getId()));

        // delete the report files from the system
        storageService.deleteAll();

        // initialize storage
        storageService.init();
    }
}
