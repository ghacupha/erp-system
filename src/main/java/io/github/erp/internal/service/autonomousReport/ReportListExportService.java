package io.github.erp.internal.service.autonomousReport;


import org.springframework.scheduling.annotation.Async;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

/**
 * Executes finer details of report file implementation essentially creating and exporting
 * the file to the file system and also calculating the file checksums in SHA512. That's it
 * for now, we don't make it a choice for the user to make so we can be sure we have the
 * same algorithm for the initial calculation and the attachment protocol
 *
 * @param <T>
 */
public interface ReportListExportService<T> {

    /**
     *
     * @param reportList List items to be exported
     * @param reportDate report's date (this is the parameter for the report)
     * @param fileName filename to be used on the file system
     * @param reportName name of the report as is to be saved on the DB
     * @throws IOException can happen
     */
    void executeReport(List<T> reportList, LocalDate reportDate, String fileName, String reportName) throws IOException;
}
