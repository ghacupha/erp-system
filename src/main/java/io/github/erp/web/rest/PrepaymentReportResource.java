package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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

import io.github.erp.repository.PrepaymentReportRepository;
import io.github.erp.service.PrepaymentReportQueryService;
import io.github.erp.service.PrepaymentReportService;
import io.github.erp.service.criteria.PrepaymentReportCriteria;
import io.github.erp.service.dto.PrepaymentReportDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.erp.domain.PrepaymentReport}.
 */
@RestController
@RequestMapping("/api")
public class PrepaymentReportResource {

    private final Logger log = LoggerFactory.getLogger(PrepaymentReportResource.class);

    private final PrepaymentReportService prepaymentReportService;

    private final PrepaymentReportRepository prepaymentReportRepository;

    private final PrepaymentReportQueryService prepaymentReportQueryService;

    public PrepaymentReportResource(
        PrepaymentReportService prepaymentReportService,
        PrepaymentReportRepository prepaymentReportRepository,
        PrepaymentReportQueryService prepaymentReportQueryService
    ) {
        this.prepaymentReportService = prepaymentReportService;
        this.prepaymentReportRepository = prepaymentReportRepository;
        this.prepaymentReportQueryService = prepaymentReportQueryService;
    }

    /**
     * {@code GET  /prepayment-reports} : get all the prepaymentReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentReports in body.
     */
    @GetMapping("/prepayment-reports")
    public ResponseEntity<List<PrepaymentReportDTO>> getAllPrepaymentReports(PrepaymentReportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PrepaymentReports by criteria: {}", criteria);
        Page<PrepaymentReportDTO> page = prepaymentReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-reports/count} : count all the prepaymentReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-reports/count")
    public ResponseEntity<Long> countPrepaymentReports(PrepaymentReportCriteria criteria) {
        log.debug("REST request to count PrepaymentReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-reports/:id} : get the "id" prepaymentReport.
     *
     * @param id the id of the prepaymentReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-reports/{id}")
    public ResponseEntity<PrepaymentReportDTO> getPrepaymentReport(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentReport : {}", id);
        Optional<PrepaymentReportDTO> prepaymentReportDTO = prepaymentReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaymentReportDTO);
    }

    /**
     * {@code SEARCH  /_search/prepayment-reports?query=:query} : search for the prepaymentReport corresponding
     * to the query.
     *
     * @param query the query of the prepaymentReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-reports")
    public ResponseEntity<List<PrepaymentReportDTO>> searchPrepaymentReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PrepaymentReports for query {}", query);
        Page<PrepaymentReportDTO> page = prepaymentReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
