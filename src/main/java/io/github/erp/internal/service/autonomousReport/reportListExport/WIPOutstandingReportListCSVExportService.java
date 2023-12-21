package io.github.erp.internal.service.autonomousReport.reportListExport;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.WorkInProgressOutstandingReportDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class WIPOutstandingReportListCSVExportService extends AbstractReportListCSVExportService<WorkInProgressOutstandingReportDTO> implements ReportListExportService<WorkInProgressOutstandingReportDTO> {

    private final InternalApplicationUserDetailService userDetailService;
    private final ApplicationUserMapper applicationUserMapper;


    public WIPOutstandingReportListCSVExportService (
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        AutonomousReportService autonomousReportService,
        InternalApplicationUserDetailService userDetailService,
        ApplicationUserMapper applicationUserMapper) {

        super(reportsProperties, fileStorageService, autonomousReportService);
        this.userDetailService = userDetailService;
        this.applicationUserMapper = applicationUserMapper;
    }

    /**
     * @param reportList List items to be exported
     * @param reportDate report's date (this is the parameter for the report)
     * @param fileName   filename to be used on the file system
     * @param reportName name of the report as is to be saved on the DB
     * @throws IOException can happen
     */
    @Override
    public void  executeReport(List<WorkInProgressOutstandingReportDTO> reportList, LocalDate reportDate, String fileName, String reportName) throws IOException {

        super.executeReport(reportList, reportDate, fileName, reportName);
    }

    protected ApplicationUserDTO getCreatedBy() {
        return applicationUserMapper.toDto(userDetailService.getCurrentApplicationUser().get());
    }
}
