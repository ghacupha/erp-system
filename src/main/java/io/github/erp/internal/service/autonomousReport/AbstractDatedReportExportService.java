package io.github.erp.internal.service.autonomousReport;

import com.hazelcast.map.IMap;
import io.github.erp.internal.service.autonomousReport.reportListExport.ReportListExportService;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class AbstractDatedReportExportService<T> implements DatedReportExportService<T>{

    public final IMap<String, String> reportCache;
    private final ReportListExportService<T> reportListExportService;

    public AbstractDatedReportExportService(IMap<String, String> reportCache, ReportListExportService<T> reportListExportService) {
        this.reportCache = reportCache;
        this.reportListExportService = reportListExportService;
    }

    @Override
    public void exportReportByDate(LocalDate reportDate, String reportName) throws IOException {

        // cacheReport(reportDate, reportName);

        String cachedReportId = reportCache.get(getCacheKey(reportDate, reportName));

        // TODO review logic
        if (cachedReportId == null) {

            runAndCacheReport(reportDate, reportName);

            return;
        }

        if (!cachedReportId.equalsIgnoreCase(reportName)) {

            runAndCacheReport(reportDate, reportName);
        }
    }

    private void runAndCacheReport(LocalDate reportDate, String reportName) throws IOException {
        cacheReport(reportDate, reportName);

        reportListExportService.executeReport(getReportList(reportDate), reportDate, java.util.UUID.randomUUID().toString(), reportName);
    }

    /**
     * Cache report and/or return key
     *
     * @param reportDate
     * @param reportMetadata
     * @return
     */
    protected void cacheReport(LocalDate reportDate, String reportMetadata) {

        this.reportCache.put(getCacheKey(reportDate, reportMetadata), reportMetadata);

    }

    protected abstract String getCacheKey(LocalDate reportDate, String reportName);

    protected abstract List<T> getReportList(LocalDate reportDate);

}
