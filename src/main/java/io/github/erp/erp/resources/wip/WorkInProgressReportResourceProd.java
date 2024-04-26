package io.github.erp.erp.resources.wip;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.WorkInProgressReport;
import io.github.erp.domain.WorkInProgressReportREPO;
import io.github.erp.internal.repository.InternalWIPProjectDealerSummaryReportRepository;
import io.github.erp.internal.report.autonomousReport.DatedReportExportService;
import io.github.erp.repository.WorkInProgressReportRepository;
import io.github.erp.repository.search.WorkInProgressReportSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link WorkInProgressReport}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
@Transactional
public class WorkInProgressReportResourceProd {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressReportResourceProd.class);

    private final static String WIP_OUTSTANDING_REPORT = "work-in-progress-outstanding-report";
    private final static String WIP_OUTSTANDING_BY_DEALER_PROJECT = "WIP-outstanding-by_dealer_project_report";

    private final InternalWIPProjectDealerSummaryReportRepository internalWIPOutstandingReportRepository;
    private final WorkInProgressReportRepository workInProgressReportRepository;
    private final InternalWIPProjectDealerSummaryReportRepository internalWIPProjectDealerSummaryReportRepository;
    private final WorkInProgressReportSearchRepository workInProgressReportSearchRepository;

    private final DatedReportExportService datedReportExportService;

    public WorkInProgressReportResourceProd(
        InternalWIPProjectDealerSummaryReportRepository internalWIPOutstandingReportRepository, WorkInProgressReportRepository workInProgressReportRepository,
        InternalWIPProjectDealerSummaryReportRepository internalWIPProjectDealerSummaryReportRepository,
        WorkInProgressReportSearchRepository workInProgressReportSearchRepository,
        DatedReportExportService<WorkInProgressReportREPO> datedReportExportService) {
        this.internalWIPOutstandingReportRepository = internalWIPOutstandingReportRepository;
        this.workInProgressReportRepository = workInProgressReportRepository;
        this.internalWIPProjectDealerSummaryReportRepository = internalWIPProjectDealerSummaryReportRepository;
        this.workInProgressReportSearchRepository = workInProgressReportSearchRepository;
        this.datedReportExportService = datedReportExportService;
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
    ) throws IOException {
        log.debug("REST request to get WorkInProgressOutstandingReports by criteria, report-date: {}", reportDate);

        // todo implement autonomous report here
        Page<WorkInProgressReportREPO> page =
            internalWIPOutstandingReportRepository.findAllByReportDate(LocalDate.parse(reportDate), pageable);

        exportCSVReport(LocalDate.parse(reportDate));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    // TODO mapper <WorkInProgressReportREPO>
    @Async void exportCSVReport(LocalDate reportDate) throws IOException {
        datedReportExportService.exportReportByDate(reportDate, WIP_OUTSTANDING_BY_DEALER_PROJECT);
    }

    /**
     * {@code GET  /work-in-progress-reports} : get all the workInProgressReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workInProgressReports in body.
     */
    @GetMapping("/work-in-progress-reports/reported")
    public ResponseEntity<List<WorkInProgressReport>> getAllWorkInProgressReportsByDate(@RequestParam("reportDate") String reportDate, Pageable pageable) {
        log.debug("REST request to get a page of WorkInProgressReports");
        Page<WorkInProgressReport> page =
            internalWIPProjectDealerSummaryReportRepository.findAllByReportDate(LocalDate.parse(reportDate), pageable)
            .map(repo -> new WorkInProgressReport()
                .id(repo.getId())
                .projectTitle(repo.getProjectTitle())
                .dealerName(repo.getDealerName())
                .numberOfItems(Math.toIntExact(repo.getNumberOfItems()))
                .instalmentAmount(repo.getInstalmentAmount())
                .transferAmount(repo.getTransferAmount())
                .outstandingAmount(repo.getOutstandingAmount()));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-in-progress-reports} : get all the workInProgressReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workInProgressReports in body.
     */
    @GetMapping("/work-in-progress-reports")
    public ResponseEntity<List<WorkInProgressReport>> getAllWorkInProgressReports(Pageable pageable) {
        log.debug("REST request to get a page of WorkInProgressReports");
        Page<WorkInProgressReport> page = workInProgressReportRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-in-progress-reports/:id} : get the "id" workInProgressReport.
     *
     * @param id the id of the workInProgressReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workInProgressReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-in-progress-reports/{id}")
    public ResponseEntity<WorkInProgressReport> getWorkInProgressReport(@PathVariable Long id) {
        log.debug("REST request to get WorkInProgressReport : {}", id);
        Optional<WorkInProgressReport> workInProgressReport = workInProgressReportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(workInProgressReport);
    }

    /**
     * {@code SEARCH  /_search/work-in-progress-reports?query=:query} : search for the workInProgressReport corresponding
     * to the query.
     *
     * @param query the query of the workInProgressReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/work-in-progress-reports")
    public ResponseEntity<List<WorkInProgressReport>> searchWorkInProgressReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WorkInProgressReports for query {}", query);
        Page<WorkInProgressReport> page = workInProgressReportSearchRepository.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
