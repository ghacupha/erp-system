package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.repository.RouDepreciationEntryReportRepository;
import io.github.erp.service.RouDepreciationEntryReportQueryService;
import io.github.erp.service.RouDepreciationEntryReportService;
import io.github.erp.service.criteria.RouDepreciationEntryReportCriteria;
import io.github.erp.service.dto.RouDepreciationEntryReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouDepreciationEntryReport}.
 */
@RestController
@RequestMapping("/api")
public class RouDepreciationEntryReportResource {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationEntryReportResource.class);

    private static final String ENTITY_NAME = "rouDepreciationEntryReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouDepreciationEntryReportService rouDepreciationEntryReportService;

    private final RouDepreciationEntryReportRepository rouDepreciationEntryReportRepository;

    private final RouDepreciationEntryReportQueryService rouDepreciationEntryReportQueryService;

    public RouDepreciationEntryReportResource(
        RouDepreciationEntryReportService rouDepreciationEntryReportService,
        RouDepreciationEntryReportRepository rouDepreciationEntryReportRepository,
        RouDepreciationEntryReportQueryService rouDepreciationEntryReportQueryService
    ) {
        this.rouDepreciationEntryReportService = rouDepreciationEntryReportService;
        this.rouDepreciationEntryReportRepository = rouDepreciationEntryReportRepository;
        this.rouDepreciationEntryReportQueryService = rouDepreciationEntryReportQueryService;
    }

    /**
     * {@code POST  /rou-depreciation-entry-reports} : Create a new rouDepreciationEntryReport.
     *
     * @param rouDepreciationEntryReportDTO the rouDepreciationEntryReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouDepreciationEntryReportDTO, or with status {@code 400 (Bad Request)} if the rouDepreciationEntryReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-depreciation-entry-reports")
    public ResponseEntity<RouDepreciationEntryReportDTO> createRouDepreciationEntryReport(
        @Valid @RequestBody RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RouDepreciationEntryReport : {}", rouDepreciationEntryReportDTO);
        if (rouDepreciationEntryReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouDepreciationEntryReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouDepreciationEntryReportDTO result = rouDepreciationEntryReportService.save(rouDepreciationEntryReportDTO);
        return ResponseEntity
            .created(new URI("/api/rou-depreciation-entry-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-depreciation-entry-reports/:id} : Updates an existing rouDepreciationEntryReport.
     *
     * @param id the id of the rouDepreciationEntryReportDTO to save.
     * @param rouDepreciationEntryReportDTO the rouDepreciationEntryReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouDepreciationEntryReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouDepreciationEntryReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouDepreciationEntryReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-depreciation-entry-reports/{id}")
    public ResponseEntity<RouDepreciationEntryReportDTO> updateRouDepreciationEntryReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouDepreciationEntryReport : {}, {}", id, rouDepreciationEntryReportDTO);
        if (rouDepreciationEntryReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouDepreciationEntryReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouDepreciationEntryReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouDepreciationEntryReportDTO result = rouDepreciationEntryReportService.save(rouDepreciationEntryReportDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouDepreciationEntryReportDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /rou-depreciation-entry-reports/:id} : Partial updates given fields of an existing rouDepreciationEntryReport, field will ignore if it is null
     *
     * @param id the id of the rouDepreciationEntryReportDTO to save.
     * @param rouDepreciationEntryReportDTO the rouDepreciationEntryReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouDepreciationEntryReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouDepreciationEntryReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouDepreciationEntryReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouDepreciationEntryReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-depreciation-entry-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouDepreciationEntryReportDTO> partialUpdateRouDepreciationEntryReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RouDepreciationEntryReportDTO rouDepreciationEntryReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RouDepreciationEntryReport partially : {}, {}", id, rouDepreciationEntryReportDTO);
        if (rouDepreciationEntryReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouDepreciationEntryReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouDepreciationEntryReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouDepreciationEntryReportDTO> result = rouDepreciationEntryReportService.partialUpdate(rouDepreciationEntryReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouDepreciationEntryReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-depreciation-entry-reports} : get all the rouDepreciationEntryReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouDepreciationEntryReports in body.
     */
    @GetMapping("/rou-depreciation-entry-reports")
    public ResponseEntity<List<RouDepreciationEntryReportDTO>> getAllRouDepreciationEntryReports(
        RouDepreciationEntryReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouDepreciationEntryReports by criteria: {}", criteria);
        Page<RouDepreciationEntryReportDTO> page = rouDepreciationEntryReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-depreciation-entry-reports/count} : count all the rouDepreciationEntryReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-depreciation-entry-reports/count")
    public ResponseEntity<Long> countRouDepreciationEntryReports(RouDepreciationEntryReportCriteria criteria) {
        log.debug("REST request to count RouDepreciationEntryReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouDepreciationEntryReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-depreciation-entry-reports/:id} : get the "id" rouDepreciationEntryReport.
     *
     * @param id the id of the rouDepreciationEntryReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouDepreciationEntryReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-depreciation-entry-reports/{id}")
    public ResponseEntity<RouDepreciationEntryReportDTO> getRouDepreciationEntryReport(@PathVariable Long id) {
        log.debug("REST request to get RouDepreciationEntryReport : {}", id);
        Optional<RouDepreciationEntryReportDTO> rouDepreciationEntryReportDTO = rouDepreciationEntryReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouDepreciationEntryReportDTO);
    }

    /**
     * {@code DELETE  /rou-depreciation-entry-reports/:id} : delete the "id" rouDepreciationEntryReport.
     *
     * @param id the id of the rouDepreciationEntryReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-depreciation-entry-reports/{id}")
    public ResponseEntity<Void> deleteRouDepreciationEntryReport(@PathVariable Long id) {
        log.debug("REST request to delete RouDepreciationEntryReport : {}", id);
        rouDepreciationEntryReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-depreciation-entry-reports?query=:query} : search for the rouDepreciationEntryReport corresponding
     * to the query.
     *
     * @param query the query of the rouDepreciationEntryReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-depreciation-entry-reports")
    public ResponseEntity<List<RouDepreciationEntryReportDTO>> searchRouDepreciationEntryReports(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of RouDepreciationEntryReports for query {}", query);
        Page<RouDepreciationEntryReportDTO> page = rouDepreciationEntryReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
