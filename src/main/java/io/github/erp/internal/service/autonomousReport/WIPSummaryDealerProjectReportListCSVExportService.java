package io.github.erp.internal.service.autonomousReport;

import io.github.erp.domain.WorkInProgressReportREPO;
import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.AutonomousReportDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service("wipSummaryDealerProjectReportListCSVExportService")
public class WIPSummaryDealerProjectReportListCSVExportService implements ReportListExportService<WorkInProgressReportREPO> {

    private final ReportsProperties reportsProperties;
    private final FileStorageService fileStorageService;
    private final AutonomousReportService autonomousReportService;
    private final InternalApplicationUserDetailService userDetailService;
    private final ApplicationUserMapper applicationUserMapper;


    public WIPSummaryDealerProjectReportListCSVExportService(
        ReportsProperties reportsProperties,
        @Qualifier("reportsFSStorageService") FileStorageService fileStorageService,
        AutonomousReportService autonomousReportService,
        InternalApplicationUserDetailService userDetailService,
        ApplicationUserMapper applicationUserMapper) {
        this.reportsProperties = reportsProperties;
        this.fileStorageService = fileStorageService;
        this.autonomousReportService = autonomousReportService;
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
    public void executeReport(List<WorkInProgressReportREPO> reportList, LocalDate reportDate, String fileName, String reportName) throws IOException {

        ByteArrayOutputStream csvByteArray = CSVDynamicConverterService.convertToCSV(reportList);

        String reportPath = reportsProperties.getReportsDirectory().concat("/").concat(fileName).concat(".csv");

        try (FileOutputStream fileOutputStream = new FileOutputStream(reportPath)) {
            csvByteArray.writeTo(fileOutputStream);
        }

        String fileChecksum = fileStorageService.calculateSha512CheckSum(fileName + ".csv");

        AutonomousReportDTO autoReport = new AutonomousReportDTO();
        autoReport.setReportName(reportName);
        autoReport.setReportParameters("Report Date: " + reportDate.format(DateTimeFormatter.ISO_LOCAL_DATE));
        autoReport.setCreatedAt(ZonedDateTime.now());
        autoReport.setReportFilename(UUID.fromString(fileName));
        autoReport.setReportFileContentType("text/csv");
        autoReport.setCreatedBy(getCreatedBy());
        autoReport.setFileChecksum(fileChecksum);
        autoReport.setReportTampered(false);
        // Save report
        autonomousReportService.save(autoReport);
    }

    protected ApplicationUserDTO getCreatedBy() {
        return applicationUserMapper.toDto(userDetailService.getCurrentApplicationUser().get());
    }
}
