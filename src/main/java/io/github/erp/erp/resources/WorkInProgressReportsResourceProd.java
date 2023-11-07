package io.github.erp.erp.resources;

import io.github.erp.domain.WorkInProgressReportREPO;
import io.github.erp.internal.repository.InternalWIPProjectDealerSummaryReportRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/fixed-asset")
public class WorkInProgressReportsResourceProd {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressReportsResourceProd.class);

    private final InternalWIPProjectDealerSummaryReportRepository internalWIPOutstandingReportRepository;

    public WorkInProgressReportsResourceProd(InternalWIPProjectDealerSummaryReportRepository internalWIPOutstandingReportRepository) {
        this.internalWIPOutstandingReportRepository = internalWIPOutstandingReportRepository;
    }

    /**
     * {@code GET  /work-in-progress-summary/reported} : get all the workInProgressReports.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of WorkInProgressReportREPO in body.
     */
    @GetMapping("/work-in-progress-summary/reported")
    public ResponseEntity<List<WorkInProgressReportREPO>> getAllWorkInProgressReportsByReportDate(
        @RequestParam("reportDate") String reportDate,
        Pageable pageable
    ) {
        log.debug("REST request to get WorkInProgressOutstandingReports by criteria, report-date: {}", reportDate);

        Page<WorkInProgressReportREPO> page =
            internalWIPOutstandingReportRepository.findSummaryByReportDate(LocalDate.parse(reportDate), pageable);

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
