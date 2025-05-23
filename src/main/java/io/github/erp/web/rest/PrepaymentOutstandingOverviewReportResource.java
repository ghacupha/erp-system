package io.github.erp.web.rest;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.PrepaymentOutstandingOverviewReport;
import io.github.erp.repository.PrepaymentOutstandingOverviewReportRepository;
import io.github.erp.repository.search.PrepaymentOutstandingOverviewReportSearchRepository;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.erp.domain.PrepaymentOutstandingOverviewReport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PrepaymentOutstandingOverviewReportResource {

    private final Logger log = LoggerFactory.getLogger(PrepaymentOutstandingOverviewReportResource.class);

    private final PrepaymentOutstandingOverviewReportRepository prepaymentOutstandingOverviewReportRepository;

    private final PrepaymentOutstandingOverviewReportSearchRepository prepaymentOutstandingOverviewReportSearchRepository;

    public PrepaymentOutstandingOverviewReportResource(
        PrepaymentOutstandingOverviewReportRepository prepaymentOutstandingOverviewReportRepository,
        PrepaymentOutstandingOverviewReportSearchRepository prepaymentOutstandingOverviewReportSearchRepository
    ) {
        this.prepaymentOutstandingOverviewReportRepository = prepaymentOutstandingOverviewReportRepository;
        this.prepaymentOutstandingOverviewReportSearchRepository = prepaymentOutstandingOverviewReportSearchRepository;
    }

    /**
     * {@code GET  /prepayment-outstanding-overview-reports} : get all the prepaymentOutstandingOverviewReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentOutstandingOverviewReports in body.
     */
    @GetMapping("/prepayment-outstanding-overview-reports")
    public ResponseEntity<List<PrepaymentOutstandingOverviewReport>> getAllPrepaymentOutstandingOverviewReports(Pageable pageable) {
        log.debug("REST request to get a page of PrepaymentOutstandingOverviewReports");
        Page<PrepaymentOutstandingOverviewReport> page = prepaymentOutstandingOverviewReportRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-outstanding-overview-reports/:id} : get the "id" prepaymentOutstandingOverviewReport.
     *
     * @param id the id of the prepaymentOutstandingOverviewReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentOutstandingOverviewReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-outstanding-overview-reports/{id}")
    public ResponseEntity<PrepaymentOutstandingOverviewReport> getPrepaymentOutstandingOverviewReport(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentOutstandingOverviewReport : {}", id);
        Optional<PrepaymentOutstandingOverviewReport> prepaymentOutstandingOverviewReport = prepaymentOutstandingOverviewReportRepository.findById(
            id
        );
        return ResponseUtil.wrapOrNotFound(prepaymentOutstandingOverviewReport);
    }

    /**
     * {@code SEARCH  /_search/prepayment-outstanding-overview-reports?query=:query} : search for the prepaymentOutstandingOverviewReport corresponding
     * to the query.
     *
     * @param query the query of the prepaymentOutstandingOverviewReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-outstanding-overview-reports")
    public ResponseEntity<List<PrepaymentOutstandingOverviewReport>> searchPrepaymentOutstandingOverviewReports(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of PrepaymentOutstandingOverviewReports for query {}", query);
        Page<PrepaymentOutstandingOverviewReport> page = prepaymentOutstandingOverviewReportSearchRepository.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
