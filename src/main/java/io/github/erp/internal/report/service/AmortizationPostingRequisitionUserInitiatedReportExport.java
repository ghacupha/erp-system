package io.github.erp.internal.report.service;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.CSVListExportService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.prepayments.InternalAmortizationPostingReportRequisitionService;
import io.github.erp.internal.service.prepayments.InternalAmortizationPostingReportService;
import io.github.erp.internal.service.prepayments.InternalPrepaymentReportRequisitionService;
import io.github.erp.internal.service.prepayments.InternalPrepaymentReportService;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
import io.github.erp.service.dto.PrepaymentReportDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Execution of the amortization-posting-report on the file system as a csv file
 */
@Transactional
@Service("amortizationPostingRequisitionUserInitiatedReportExport")
public class AmortizationPostingRequisitionUserInitiatedReportExport
    extends CSVListExportService<AmortizationPostingReportDTO>
    implements ExportReportService<AmortizationPostingReportRequisitionDTO>{

    private final InternalAmortizationPostingReportService internalAmortizationPostingReportService;
    private final InternalAmortizationPostingReportRequisitionService internalAmortizationPostingReportRequisitionService;

    public AmortizationPostingRequisitionUserInitiatedReportExport (
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        InternalAmortizationPostingReportService internalAmortizationPostingReportService,
        InternalAmortizationPostingReportRequisitionService internalAmortizationPostingReportRequisitionService) {
        super(reportsProperties, fileStorageService);

        this.internalAmortizationPostingReportService = internalAmortizationPostingReportService;
        this.internalAmortizationPostingReportRequisitionService = internalAmortizationPostingReportRequisitionService;
    }

    @Override
    public void exportReport(AmortizationPostingReportRequisitionDTO reportRequisition) {

        Optional<List<AmortizationPostingReportDTO>> reportListItems = getEntries(reportRequisition);

        reportListItems.ifPresent(reportList -> {

            try {
                UUID fileName = UUID.randomUUID();

                String fileChecksum = super.executeReport(reportList, fileName.toString());

                reportRequisition.setFileChecksum(fileChecksum);
                reportRequisition.setFilename(fileName);
                reportRequisition.setReportParameters(getReportParameters(reportRequisition));

                internalAmortizationPostingReportRequisitionService.save(reportRequisition);

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private String getReportParameters(AmortizationPostingReportRequisitionDTO reportRequisition) {

        return "Amort. Period: ".concat(reportRequisition.getAmortizationPeriod().getPeriodCode());
    }

    private Optional<List<AmortizationPostingReportDTO>> getEntries(AmortizationPostingReportRequisitionDTO reportRequisition) {

        return internalAmortizationPostingReportService.findAllByReportDate(reportRequisition.getAmortizationPeriod().getEndDate());
    }
}
