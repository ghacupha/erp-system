package io.github.erp.internal.service.autonomousReport;

import com.hazelcast.map.IMap;
import io.github.erp.domain.WorkInProgressReportREPO;
import io.github.erp.internal.repository.InternalWIPProjectDealerSummaryReportRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Transactional
@Service("workInProgressReportDatedReportExportService")
public class WorkInProgressReportDatedReportExportService implements DatedReportExportService<WorkInProgressReportREPO> {

    private final ReportListExportService<WorkInProgressReportREPO> reportListExportService;

    private final InternalWIPProjectDealerSummaryReportRepository internalWIPOutstandingReportRepository;

    private final IMap<String, String> workInProgressReportCache;


    public WorkInProgressReportDatedReportExportService(
        InternalWIPProjectDealerSummaryReportRepository internalWIPOutstandingReportRepository,
        @Qualifier("workInProgressReportCache") IMap<String, String> workInProgressReportCache,
        @Qualifier("wipSummaryDealerProjectReportListCSVExportService") ReportListExportService<WorkInProgressReportREPO> reportListExportService
        ) {
        this.internalWIPOutstandingReportRepository = internalWIPOutstandingReportRepository;
        this.workInProgressReportCache = workInProgressReportCache;
        this.reportListExportService = reportListExportService;
    }

    @Override
    public void exportReportByDate(LocalDate reportDate, String reportName) throws IOException {

        String cacheKey = reportDate.format(DateTimeFormatter.ISO_DATE) + "-" + reportName;

        String cachedReport = workInProgressReportCache.get(cacheKey);

        if (cachedReport == null) {

            runAndCacheReport(reportDate, reportName);

            return;
        }

        if (!cachedReport.equalsIgnoreCase(reportName)) {

            runAndCacheReport(reportDate, reportName);
        }

    }

    private void runAndCacheReport(LocalDate reportDate, String reportName) throws IOException {
        cacheReport(reportDate, reportName);

        Page<WorkInProgressReportREPO> result = internalWIPOutstandingReportRepository.findAllByReportDate(reportDate, PageRequest.of(0, Integer.MAX_VALUE));

        reportListExportService.executeReport(result.getContent(), reportDate, java.util.UUID.randomUUID().toString(), reportName);
    }

    protected void cacheReport(LocalDate reportDate, String reportMetadata) {
        String cacheKey = reportDate.format(DateTimeFormatter.ISO_DATE) + "-" + reportMetadata;
        this.workInProgressReportCache.put(cacheKey, reportMetadata);
    }
}
