package io.github.erp.internal.report.service;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.AbstractCSVListExportService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.prepayments.InternalPrepaymentReportService;
import io.github.erp.service.dto.PrepaymentReportDTO;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class PrepaymentReportExportService
    extends AbstractCSVListExportService<PrepaymentReportDTO>
    implements ExportReportService<PrepaymentReportRequisitionDTO> {

    private final InternalPrepaymentReportService internalPrepaymentReportService;

    public PrepaymentReportExportService(
        ReportsProperties reportsProperties,
        InternalPrepaymentReportService internalPrepaymentReportService,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService) {
        super(reportsProperties, fileStorageService);
        this.internalPrepaymentReportService = internalPrepaymentReportService;
    }

    @Override
    public void exportReport(PrepaymentReportRequisitionDTO reportRequisition) {

        Optional<List<PrepaymentReportDTO>> reportListItems = getEntries(reportRequisition);

        reportListItems.ifPresent(reportList -> {

            try {
                UUID fileName = UUID.randomUUID();

                String fileChecksum = super.executeReport(reportList, fileName.toString());

                reportRequisition.setFileChecksum(fileChecksum);
                reportRequisition.setFilename(fileName);
                reportRequisition.setReportParameters(getReportParameters(reportRequisition));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getReportParameters(PrepaymentReportRequisitionDTO reportRequisition) {

        // TODO get report-date parameter
        return reportRequisition.getReportParameters();
    }

    private Optional<List<PrepaymentReportDTO>> getEntries(PrepaymentReportRequisitionDTO reportRequisition) {

        // TODO get report-date parameter from the requisition
        return internalPrepaymentReportService.getReportListByReportDate(reportRequisition.getTimeOfRequisition().toLocalDate());
    }
}
