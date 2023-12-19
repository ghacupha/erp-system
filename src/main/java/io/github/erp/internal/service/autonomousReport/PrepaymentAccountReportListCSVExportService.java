package io.github.erp.internal.service.autonomousReport;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.AutonomousReportDTO;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service("prepaymentAccountReportListCSVExportService")
@Transactional
public class PrepaymentAccountReportListCSVExportService implements ReportListExportService<PrepaymentAccountReportDTO> {


    private final ReportsProperties reportsProperties;
    private final FileStorageService fileStorageService;
    private final AutonomousReportService autonomousReportService;
    private final InternalApplicationUserDetailService userDetailService;
    private final ApplicationUserMapper applicationUserMapper;


    public PrepaymentAccountReportListCSVExportService(
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

    public void executeReport(List<PrepaymentAccountReportDTO> reportList, LocalDate reportDate, String fileName, String reportName) throws IOException {

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
