package io.github.erp.erp.resources.prepayments;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.internal.report.autonomousReport.DatedReportExportService;
import io.github.erp.internal.service.prepayments.InternalPrepaymentAccountReportService;
import io.github.erp.service.PrepaymentAccountReportQueryService;
import io.github.erp.service.criteria.PrepaymentAccountReportCriteria;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.PrepaymentAccountReport}.
 */
@RestController("PrepaymentAccountReportResourceProd")
@RequestMapping("/api/prepayments")
public class PrepaymentAccountReportResourceProd {

    private final static String REPORT_NAME = "prepayments-by-accounts-outstanding-report";

    private final Logger log = LoggerFactory.getLogger(PrepaymentAccountReportResourceProd.class);

    private final InternalPrepaymentAccountReportService prepaymentAccountReportService;

    private final PrepaymentAccountReportQueryService prepaymentAccountReportQueryService;

    private final DatedReportExportService<PrepaymentAccountReportDTO> prepaymentReportExportService;


    public PrepaymentAccountReportResourceProd(
        DatedReportExportService<PrepaymentAccountReportDTO> prepaymentReportExportService,
        InternalPrepaymentAccountReportService prepaymentAccountReportService,
        PrepaymentAccountReportQueryService prepaymentAccountReportQueryService) {
        this.prepaymentAccountReportService = prepaymentAccountReportService;
        this.prepaymentAccountReportQueryService = prepaymentAccountReportQueryService;

        this.prepaymentReportExportService = prepaymentReportExportService;
    }

    /**
     * {@code GET  /prepayment-account-reports} : get all the prepaymentAccountReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentAccountReports in body.
     */
    @GetMapping("/prepayment-account-reports")
    public ResponseEntity<List<PrepaymentAccountReportDTO>> getAllPrepaymentAccountReports(
        PrepaymentAccountReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get PrepaymentAccountReports by criteria: {}", criteria);
        Page<PrepaymentAccountReportDTO> page = prepaymentAccountReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-account-reports} : get all the prepaymentAccountReports.
     *
     * @param pageable the pagination information.
     * @param reportDate the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentAccountReports in body.
     */
    @GetMapping("/prepayment-account-reports/reported")
    public ResponseEntity<List<PrepaymentAccountReportDTO>> getAllPrepaymentAccountReportsByReportDate (
        @RequestParam("reportDate") String reportDate,
        Pageable pageable
    ) {
        log.debug("REST request to get PrepaymentAccountReports by criteria, report-date: {}", reportDate);

        Page<PrepaymentAccountReportDTO> page =
            prepaymentAccountReportService.findAllByReportDate(LocalDate.parse(reportDate), pageable);

        exportCSVReport(LocalDate.parse(reportDate));

        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @Async
    void exportCSVReport(LocalDate reportDate) {

        try {
            prepaymentReportExportService.exportReportByDate(reportDate, REPORT_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * {@code GET  /prepayment-account-reports/count} : count all the prepaymentAccountReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-account-reports/count")
    public ResponseEntity<Long> countPrepaymentAccountReports(PrepaymentAccountReportCriteria criteria) {
        log.debug("REST request to count PrepaymentAccountReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentAccountReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-account-reports/:id} : get the "id" prepaymentAccountReport.
     *
     * @param id the id of the prepaymentAccountReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentAccountReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-account-reports/{id}")
    public ResponseEntity<PrepaymentAccountReportDTO> getPrepaymentAccountReport(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentAccountReport : {}", id);
        Optional<PrepaymentAccountReportDTO> prepaymentAccountReportDTO = prepaymentAccountReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaymentAccountReportDTO);
    }

    /**
     * {@code GET  /prepayment-account-reports/:id} : get the "id" prepaymentAccountReport.
     *
     * @param id the id of the prepaymentAccountReportDTO to retrieve.
     * @param reportDate the id of the prepaymentAccountReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentAccountReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-account-reports/reported/{id}")
    public ResponseEntity<PrepaymentAccountReportDTO> getPrepaymentAccountReportByDate(@RequestParam("reportDate") String reportDate, @PathVariable Long id) {
        log.debug("REST request to get PrepaymentAccountReport : {}", id);
        return ResponseUtil.wrapOrNotFound(prepaymentAccountReportService.findOneByReportDate(LocalDate.parse(reportDate), id));
    }

    /**
     * {@code SEARCH  /_search/prepayment-account-reports?query=:query} : search for the prepaymentAccountReport corresponding
     * to the query.
     *
     * @param query the query of the prepaymentAccountReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-account-reports")
    public ResponseEntity<List<PrepaymentAccountReportDTO>> searchPrepaymentAccountReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PrepaymentAccountReports for query {}", query);
        Page<PrepaymentAccountReportDTO> page = prepaymentAccountReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
