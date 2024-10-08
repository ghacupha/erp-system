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

import io.github.erp.repository.RouAccountBalanceReportRepository;
import io.github.erp.service.RouAccountBalanceReportQueryService;
import io.github.erp.service.RouAccountBalanceReportService;
import io.github.erp.service.criteria.RouAccountBalanceReportCriteria;
import io.github.erp.service.dto.RouAccountBalanceReportDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link io.github.erp.domain.RouAccountBalanceReport}.
 */
@RestController
@RequestMapping("/api")
public class RouAccountBalanceReportResource {

    private final Logger log = LoggerFactory.getLogger(RouAccountBalanceReportResource.class);

    private static final String ENTITY_NAME = "rouAccountBalanceReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouAccountBalanceReportService rouAccountBalanceReportService;

    private final RouAccountBalanceReportRepository rouAccountBalanceReportRepository;

    private final RouAccountBalanceReportQueryService rouAccountBalanceReportQueryService;

    public RouAccountBalanceReportResource(
        RouAccountBalanceReportService rouAccountBalanceReportService,
        RouAccountBalanceReportRepository rouAccountBalanceReportRepository,
        RouAccountBalanceReportQueryService rouAccountBalanceReportQueryService
    ) {
        this.rouAccountBalanceReportService = rouAccountBalanceReportService;
        this.rouAccountBalanceReportRepository = rouAccountBalanceReportRepository;
        this.rouAccountBalanceReportQueryService = rouAccountBalanceReportQueryService;
    }

    /**
     * {@code POST  /rou-account-balance-reports} : Create a new rouAccountBalanceReport.
     *
     * @param rouAccountBalanceReportDTO the rouAccountBalanceReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouAccountBalanceReportDTO, or with status {@code 400 (Bad Request)} if the rouAccountBalanceReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-account-balance-reports")
    public ResponseEntity<RouAccountBalanceReportDTO> createRouAccountBalanceReport(
        @Valid @RequestBody RouAccountBalanceReportDTO rouAccountBalanceReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RouAccountBalanceReport : {}", rouAccountBalanceReportDTO);
        if (rouAccountBalanceReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouAccountBalanceReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouAccountBalanceReportDTO result = rouAccountBalanceReportService.save(rouAccountBalanceReportDTO);
        return ResponseEntity
            .created(new URI("/api/rou-account-balance-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-account-balance-reports/:id} : Updates an existing rouAccountBalanceReport.
     *
     * @param id the id of the rouAccountBalanceReportDTO to save.
     * @param rouAccountBalanceReportDTO the rouAccountBalanceReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouAccountBalanceReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouAccountBalanceReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouAccountBalanceReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-account-balance-reports/{id}")
    public ResponseEntity<RouAccountBalanceReportDTO> updateRouAccountBalanceReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RouAccountBalanceReportDTO rouAccountBalanceReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouAccountBalanceReport : {}, {}", id, rouAccountBalanceReportDTO);
        if (rouAccountBalanceReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouAccountBalanceReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouAccountBalanceReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouAccountBalanceReportDTO result = rouAccountBalanceReportService.save(rouAccountBalanceReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouAccountBalanceReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rou-account-balance-reports/:id} : Partial updates given fields of an existing rouAccountBalanceReport, field will ignore if it is null
     *
     * @param id the id of the rouAccountBalanceReportDTO to save.
     * @param rouAccountBalanceReportDTO the rouAccountBalanceReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouAccountBalanceReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouAccountBalanceReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouAccountBalanceReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouAccountBalanceReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-account-balance-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouAccountBalanceReportDTO> partialUpdateRouAccountBalanceReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RouAccountBalanceReportDTO rouAccountBalanceReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RouAccountBalanceReport partially : {}, {}", id, rouAccountBalanceReportDTO);
        if (rouAccountBalanceReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouAccountBalanceReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouAccountBalanceReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouAccountBalanceReportDTO> result = rouAccountBalanceReportService.partialUpdate(rouAccountBalanceReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouAccountBalanceReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-account-balance-reports} : get all the rouAccountBalanceReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouAccountBalanceReports in body.
     */
    @GetMapping("/rou-account-balance-reports")
    public ResponseEntity<List<RouAccountBalanceReportDTO>> getAllRouAccountBalanceReports(
        RouAccountBalanceReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouAccountBalanceReports by criteria: {}", criteria);
        Page<RouAccountBalanceReportDTO> page = rouAccountBalanceReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-account-balance-reports/count} : count all the rouAccountBalanceReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-account-balance-reports/count")
    public ResponseEntity<Long> countRouAccountBalanceReports(RouAccountBalanceReportCriteria criteria) {
        log.debug("REST request to count RouAccountBalanceReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouAccountBalanceReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-account-balance-reports/:id} : get the "id" rouAccountBalanceReport.
     *
     * @param id the id of the rouAccountBalanceReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouAccountBalanceReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-account-balance-reports/{id}")
    public ResponseEntity<RouAccountBalanceReportDTO> getRouAccountBalanceReport(@PathVariable Long id) {
        log.debug("REST request to get RouAccountBalanceReport : {}", id);
        Optional<RouAccountBalanceReportDTO> rouAccountBalanceReportDTO = rouAccountBalanceReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouAccountBalanceReportDTO);
    }

    /**
     * {@code DELETE  /rou-account-balance-reports/:id} : delete the "id" rouAccountBalanceReport.
     *
     * @param id the id of the rouAccountBalanceReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-account-balance-reports/{id}")
    public ResponseEntity<Void> deleteRouAccountBalanceReport(@PathVariable Long id) {
        log.debug("REST request to delete RouAccountBalanceReport : {}", id);
        rouAccountBalanceReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-account-balance-reports?query=:query} : search for the rouAccountBalanceReport corresponding
     * to the query.
     *
     * @param query the query of the rouAccountBalanceReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-account-balance-reports")
    public ResponseEntity<List<RouAccountBalanceReportDTO>> searchRouAccountBalanceReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RouAccountBalanceReports for query {}", query);
        Page<RouAccountBalanceReportDTO> page = rouAccountBalanceReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
