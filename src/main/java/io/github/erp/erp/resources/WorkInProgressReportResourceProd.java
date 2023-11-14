package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
import io.github.erp.domain.WorkInProgressReport;
import io.github.erp.internal.repository.InternalWIPProjectDealerSummaryReportRepository;
import io.github.erp.repository.WorkInProgressReportRepository;
import io.github.erp.repository.search.WorkInProgressReportSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

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

    private final WorkInProgressReportRepository workInProgressReportRepository;
    private final InternalWIPProjectDealerSummaryReportRepository internalWIPProjectDealerSummaryReportRepository;
    private final WorkInProgressReportSearchRepository workInProgressReportSearchRepository;

    public WorkInProgressReportResourceProd(
        WorkInProgressReportRepository workInProgressReportRepository,
        InternalWIPProjectDealerSummaryReportRepository internalWIPProjectDealerSummaryReportRepository,
        WorkInProgressReportSearchRepository workInProgressReportSearchRepository
    ) {
        this.workInProgressReportRepository = workInProgressReportRepository;
        this.internalWIPProjectDealerSummaryReportRepository = internalWIPProjectDealerSummaryReportRepository;
        this.workInProgressReportSearchRepository = workInProgressReportSearchRepository;
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
