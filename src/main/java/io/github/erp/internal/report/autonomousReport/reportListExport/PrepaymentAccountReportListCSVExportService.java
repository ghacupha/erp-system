package io.github.erp.internal.report.autonomousReport.reportListExport;


import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service("prepaymentAccountReportListCSVExportService")
@Transactional
public class PrepaymentAccountReportListCSVExportService extends AbstractReportListCSVExportService<PrepaymentAccountReportDTO> implements ReportListExportService<PrepaymentAccountReportDTO> {


    private final InternalApplicationUserDetailService userDetailService;

    public PrepaymentAccountReportListCSVExportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        AutonomousReportService autonomousReportService,
        InternalApplicationUserDetailService userDetailService) {

        super(reportsProperties, fileStorageService, autonomousReportService);
        this.userDetailService = userDetailService;
    }

    public void executeReport(List<PrepaymentAccountReportDTO> reportList, LocalDate reportDate, String fileName, String reportName) throws IOException {

        super.executeReport(reportList, reportDate, fileName, reportName);
    }

    protected ApplicationUserDTO getCreatedBy() {
        return userDetailService.getCurrentApplicationUser().get();
    }

}
