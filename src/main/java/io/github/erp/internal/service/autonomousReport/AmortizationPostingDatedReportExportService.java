package io.github.erp.internal.service.autonomousReport;

import com.hazelcast.map.IMap;
import io.github.erp.domain.AmortizationPostingReportInternal;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalAmortizationPostingRepository;
import io.github.erp.internal.service.autonomousReport.reportListExport.ReportListExportService;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class AmortizationPostingDatedReportExportService extends AbstractDatedReportExportService<AmortizationPostingReportDTO> implements DatedReportExportService<AmortizationPostingReportDTO> {


    private final InternalAmortizationPostingRepository internalAmortizationPostingRepository;
    private final Mapping<AmortizationPostingReportInternal, AmortizationPostingReportDTO> dtoMapping;

    public AmortizationPostingDatedReportExportService(
        InternalAmortizationPostingRepository internalAmortizationPostingRepository,
        IMap<String, String> prepaymentsReportCache,
        ReportListExportService<AmortizationPostingReportDTO> reportListExportService,
        Mapping<AmortizationPostingReportInternal,
            AmortizationPostingReportDTO> dtoMapping ) {

        super(prepaymentsReportCache, reportListExportService);

        this.internalAmortizationPostingRepository = internalAmortizationPostingRepository;
        this.dtoMapping = dtoMapping;
    }

    @Override
    public void exportReportByDate(LocalDate reportDate, String reportName) throws IOException {
        super.exportReportByDate(reportDate, reportName);
    }

    @Override
    protected List<AmortizationPostingReportDTO> getReportList(LocalDate reportDate) {
        return internalAmortizationPostingRepository.findByReportDate(reportDate, PageRequest.of(0, Integer.MAX_VALUE))
            .map(dtoMapping::toValue2)
            .getContent();
    }

    @Override
    protected String getCacheKey(LocalDate reportDate, String reportName) {
        return reportDate.format(DateTimeFormatter.ISO_DATE) + "-" + reportName;
    }

}
