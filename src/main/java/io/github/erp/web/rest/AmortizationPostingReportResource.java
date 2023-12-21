package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.domain.AmortizationPostingReport;
import io.github.erp.repository.AmortizationPostingReportRepository;
import io.github.erp.repository.search.AmortizationPostingReportSearchRepository;
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
 * REST controller for managing {@link io.github.erp.domain.AmortizationPostingReport}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AmortizationPostingReportResource {

    private final Logger log = LoggerFactory.getLogger(AmortizationPostingReportResource.class);

    private final AmortizationPostingReportRepository amortizationPostingReportRepository;

    private final AmortizationPostingReportSearchRepository amortizationPostingReportSearchRepository;

    public AmortizationPostingReportResource(
        AmortizationPostingReportRepository amortizationPostingReportRepository,
        AmortizationPostingReportSearchRepository amortizationPostingReportSearchRepository
    ) {
        this.amortizationPostingReportRepository = amortizationPostingReportRepository;
        this.amortizationPostingReportSearchRepository = amortizationPostingReportSearchRepository;
    }

    /**
     * {@code GET  /amortization-posting-reports} : get all the amortizationPostingReports.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amortizationPostingReports in body.
     */
    @GetMapping("/amortization-posting-reports")
    public ResponseEntity<List<AmortizationPostingReport>> getAllAmortizationPostingReports(Pageable pageable) {
        log.debug("REST request to get a page of AmortizationPostingReports");
        Page<AmortizationPostingReport> page = amortizationPostingReportRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /amortization-posting-reports/:id} : get the "id" amortizationPostingReport.
     *
     * @param id the id of the amortizationPostingReport to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amortizationPostingReport, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amortization-posting-reports/{id}")
    public ResponseEntity<AmortizationPostingReport> getAmortizationPostingReport(@PathVariable Long id) {
        log.debug("REST request to get AmortizationPostingReport : {}", id);
        Optional<AmortizationPostingReport> amortizationPostingReport = amortizationPostingReportRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(amortizationPostingReport);
    }

    /**
     * {@code SEARCH  /_search/amortization-posting-reports?query=:query} : search for the amortizationPostingReport corresponding
     * to the query.
     *
     * @param query the query of the amortizationPostingReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/amortization-posting-reports")
    public ResponseEntity<List<AmortizationPostingReport>> searchAmortizationPostingReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AmortizationPostingReports for query {}", query);
        Page<AmortizationPostingReport> page = amortizationPostingReportSearchRepository.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
