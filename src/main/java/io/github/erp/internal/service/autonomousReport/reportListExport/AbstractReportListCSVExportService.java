package io.github.erp.internal.service.autonomousReport.reportListExport;

import io.github.erp.internal.files.FileStorageService;
import io.github.erp.internal.report.ReportsProperties;
import io.github.erp.internal.service.autonomousReport.CSVDynamicConverterService;
import io.github.erp.internal.service.autonomousReport.reportListExport.ReportListExportService;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.dto.ApplicationUserDTO;
import io.github.erp.service.dto.AutonomousReportDTO;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public abstract class AbstractReportListCSVExportService<T> implements ReportListExportService<T> {

    private final ReportsProperties reportsProperties;
    private final FileStorageService fileStorageService;
    private final AutonomousReportService autonomousReportService;

    public AbstractReportListCSVExportService(ReportsProperties reportsProperties, FileStorageService fileStorageService, AutonomousReportService autonomousReportService) {
        this.reportsProperties = reportsProperties;
        this.fileStorageService = fileStorageService;
        this.autonomousReportService = autonomousReportService;
    }

    /**
     * @param reportList List items to be exported
     * @param reportDate report's date (this is the parameter for the report)
     * @param fileName   filename to be used on the file system
     * @param reportName name of the report as is to be saved on the DB
     * @throws IOException can happen
     */
    @Override
    public void executeReport(List<T> reportList, LocalDate reportDate, String fileName, String reportName) throws IOException {

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

        // todo CHECK report for tamper
        autoReport.setReportTampered(false);
        // Save report
        autonomousReportService.save(autoReport);
    }

    protected abstract ApplicationUserDTO getCreatedBy();
}
