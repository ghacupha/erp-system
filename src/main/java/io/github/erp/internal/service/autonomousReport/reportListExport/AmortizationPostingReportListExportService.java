package io.github.erp.internal.service.autonomousReport.reportListExport;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
public class AmortizationPostingReportListExportService extends AbstractReportListCSVExportService<AmortizationPostingReportDTO> implements ReportListExportService<AmortizationPostingReportDTO> {

    private final InternalApplicationUserDetailService userDetailService;
    private final ApplicationUserMapper applicationUserMapper;

    public AmortizationPostingReportListExportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        AutonomousReportService autonomousReportService,
        InternalApplicationUserDetailService userDetailService,
        ApplicationUserMapper applicationUserMapper) {
        super(reportsProperties, fileStorageService, autonomousReportService);
        this.userDetailService = userDetailService;
        this.applicationUserMapper = applicationUserMapper;
    }

    public void executeReport(List<AmortizationPostingReportDTO> reportList, LocalDate reportDate, String fileName, String reportName) throws IOException {

        super.executeReport(reportList, reportDate, fileName, reportName);
    }

    protected ApplicationUserDTO getCreatedBy() {
        return applicationUserMapper.toDto(userDetailService.getCurrentApplicationUser().get());
    }

}
