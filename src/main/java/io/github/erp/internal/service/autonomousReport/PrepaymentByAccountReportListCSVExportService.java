package io.github.erp.internal.service.autonomousReport;

import com.hazelcast.map.IMap;
import io.github.erp.domain.PrepaymentAccountReportTuple;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalPrepaymentAccountReportRepository;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Transactional
@Service("prepaymentByAccountReportListCSVExportService")
public class PrepaymentByAccountReportListCSVExportService implements DatedReportExportService {

    private final InternalPrepaymentAccountReportRepository internalPrepaymentAccountReportRepository;

    public final IMap<String, String> prepaymentsReportCache;

    private final Mapping<PrepaymentAccountReportTuple, PrepaymentAccountReportDTO> prepaymentAccountReportTupleMapper;

    private final ReportListExportService<PrepaymentAccountReportDTO> reportListExportService;

    public PrepaymentByAccountReportListCSVExportService(
        InternalPrepaymentAccountReportRepository internalPrepaymentAccountReportRepository,
        IMap<String, String> prepaymentsReportCache,
        Mapping<PrepaymentAccountReportTuple, PrepaymentAccountReportDTO> prepaymentAccountReportTupleMapper, @Qualifier("prepaymentAccountReportListCSVExportService") ReportListExportService<PrepaymentAccountReportDTO> reportListExportService) {
        this.internalPrepaymentAccountReportRepository = internalPrepaymentAccountReportRepository;
        this.prepaymentsReportCache = prepaymentsReportCache;
        this.prepaymentAccountReportTupleMapper = prepaymentAccountReportTupleMapper;
        this.reportListExportService = reportListExportService;
    }

    @Override
    public void exportReportByDate(LocalDate reportDate, String reportName) throws IOException {

        String cacheKey = reportDate.format(DateTimeFormatter.ISO_DATE) + "-" + reportName;

        String cachedReport = prepaymentsReportCache.get(cacheKey);

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

        Page<PrepaymentAccountReportDTO> result = internalPrepaymentAccountReportRepository.findAllByReportDate(reportDate, PageRequest.of(0, Integer.MAX_VALUE))
            .map(prepaymentAccountReportTupleMapper::toValue2);

        reportListExportService.executeReport(result.getContent(), reportDate, java.util.UUID.randomUUID().toString(), reportName);
    }

    protected void cacheReport(LocalDate reportDate, String reportMetadata) {
        String cacheKey = reportDate.format(DateTimeFormatter.ISO_DATE) + "-" + reportMetadata;
        this.prepaymentsReportCache.put(cacheKey, reportMetadata);
    }
}
