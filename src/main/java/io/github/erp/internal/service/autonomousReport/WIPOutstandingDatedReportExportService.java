package io.github.erp.internal.service.autonomousReport;

import com.hazelcast.map.IMap;
import io.github.erp.domain.WorkInProgressOutstandingReportREPO;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalWIPOutstandingReportRepository;
import io.github.erp.internal.service.autonomousReport.reportListExport.ReportListExportService;
import io.github.erp.service.dto.WorkInProgressOutstandingReportDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Transactional
@Service
public class WIPOutstandingDatedReportExportService extends AbstractDatedReportExportService<WorkInProgressOutstandingReportDTO> implements DatedReportExportService<WorkInProgressOutstandingReportDTO> {

    private final InternalWIPOutstandingReportRepository internalWIPOutstandingReportRepository;


    public final IMap<String, String> workInProgressReportCache;
    private final Mapping<WorkInProgressOutstandingReportREPO, WorkInProgressOutstandingReportDTO> workInProgressOutstandingReportREPOMapper;

    public WIPOutstandingDatedReportExportService(
        InternalWIPOutstandingReportRepository internalWIPOutstandingReportRepository,
        IMap<String, String> workInProgressReportCache,
        Mapping<WorkInProgressOutstandingReportREPO, WorkInProgressOutstandingReportDTO> workInProgressOutstandingReportREPOMapper,
        ReportListExportService<WorkInProgressOutstandingReportDTO> reportListExportService) {

        super(workInProgressReportCache, reportListExportService);

        this.internalWIPOutstandingReportRepository = internalWIPOutstandingReportRepository;
        this.workInProgressReportCache = workInProgressReportCache;
        this.workInProgressOutstandingReportREPOMapper = workInProgressOutstandingReportREPOMapper;
    }

    @Override
    public void exportReportByDate(LocalDate reportDate, String reportName) throws IOException {

        super.exportReportByDate(reportDate, reportName);
    }

    @Override
    protected List<WorkInProgressOutstandingReportDTO> getReportList(LocalDate reportDate) {
        return internalWIPOutstandingReportRepository.findByReportDate(reportDate, PageRequest.of(0, Integer.MAX_VALUE))
            .map(workInProgressOutstandingReportREPOMapper::toValue2).getContent();
    }

    @Override
    protected String getCacheKey(LocalDate reportDate, String reportName) {

        return reportDate.format(DateTimeFormatter.ISO_DATE) + "-" + reportName;
    }
}
