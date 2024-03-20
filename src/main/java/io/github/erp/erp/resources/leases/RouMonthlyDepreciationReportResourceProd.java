package io.github.erp.erp.resources.leases;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.repository.RouMonthlyDepreciationReportRepository;
import io.github.erp.service.RouMonthlyDepreciationReportQueryService;
import io.github.erp.service.RouMonthlyDepreciationReportService;
import io.github.erp.service.criteria.RouMonthlyDepreciationReportCriteria;
import io.github.erp.service.dto.RouMonthlyDepreciationReportDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.RouMonthlyDepreciationReport}.
 */
@RestController
@RequestMapping("/api/leases")
public class RouMonthlyDepreciationReportResourceProd {

    private final Logger log = LoggerFactory.getLogger(RouMonthlyDepreciationReportResourceProd.class);

    private static final String ENTITY_NAME = "rouMonthlyDepreciationReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouMonthlyDepreciationReportService rouMonthlyDepreciationReportService;

    private final RouMonthlyDepreciationReportRepository rouMonthlyDepreciationReportRepository;

    private final RouMonthlyDepreciationReportQueryService rouMonthlyDepreciationReportQueryService;

    public RouMonthlyDepreciationReportResourceProd(
        RouMonthlyDepreciationReportService rouMonthlyDepreciationReportService,
        RouMonthlyDepreciationReportRepository rouMonthlyDepreciationReportRepository,
        RouMonthlyDepreciationReportQueryService rouMonthlyDepreciationReportQueryService
    ) {
        this.rouMonthlyDepreciationReportService = rouMonthlyDepreciationReportService;
        this.rouMonthlyDepreciationReportRepository = rouMonthlyDepreciationReportRepository;
        this.rouMonthlyDepreciationReportQueryService = rouMonthlyDepreciationReportQueryService;
    }

    /**
     * {@code POST  /rou-monthly-depreciation-reports} : Create a new rouMonthlyDepreciationReport.
     *
     * @param rouMonthlyDepreciationReportDTO the rouMonthlyDepreciationReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouMonthlyDepreciationReportDTO, or with status {@code 400 (Bad Request)} if the rouMonthlyDepreciationReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-monthly-depreciation-reports")
    public ResponseEntity<RouMonthlyDepreciationReportDTO> createRouMonthlyDepreciationReport(
        @Valid @RequestBody RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RouMonthlyDepreciationReport : {}", rouMonthlyDepreciationReportDTO);
        if (rouMonthlyDepreciationReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouMonthlyDepreciationReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouMonthlyDepreciationReportDTO result = rouMonthlyDepreciationReportService.save(rouMonthlyDepreciationReportDTO);
        return ResponseEntity
            .created(new URI("/api/rou-monthly-depreciation-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-monthly-depreciation-reports/:id} : Updates an existing rouMonthlyDepreciationReport.
     *
     * @param id the id of the rouMonthlyDepreciationReportDTO to save.
     * @param rouMonthlyDepreciationReportDTO the rouMonthlyDepreciationReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouMonthlyDepreciationReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouMonthlyDepreciationReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouMonthlyDepreciationReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-monthly-depreciation-reports/{id}")
    public ResponseEntity<RouMonthlyDepreciationReportDTO> updateRouMonthlyDepreciationReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouMonthlyDepreciationReport : {}, {}", id, rouMonthlyDepreciationReportDTO);
        if (rouMonthlyDepreciationReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouMonthlyDepreciationReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouMonthlyDepreciationReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouMonthlyDepreciationReportDTO result = rouMonthlyDepreciationReportService.save(rouMonthlyDepreciationReportDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouMonthlyDepreciationReportDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /rou-monthly-depreciation-reports/:id} : Partial updates given fields of an existing rouMonthlyDepreciationReport, field will ignore if it is null
     *
     * @param id the id of the rouMonthlyDepreciationReportDTO to save.
     * @param rouMonthlyDepreciationReportDTO the rouMonthlyDepreciationReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouMonthlyDepreciationReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouMonthlyDepreciationReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouMonthlyDepreciationReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouMonthlyDepreciationReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-monthly-depreciation-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouMonthlyDepreciationReportDTO> partialUpdateRouMonthlyDepreciationReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RouMonthlyDepreciationReportDTO rouMonthlyDepreciationReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RouMonthlyDepreciationReport partially : {}, {}", id, rouMonthlyDepreciationReportDTO);
        if (rouMonthlyDepreciationReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouMonthlyDepreciationReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouMonthlyDepreciationReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouMonthlyDepreciationReportDTO> result = rouMonthlyDepreciationReportService.partialUpdate(
            rouMonthlyDepreciationReportDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouMonthlyDepreciationReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-monthly-depreciation-reports} : get all the rouMonthlyDepreciationReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouMonthlyDepreciationReports in body.
     */
    @GetMapping("/rou-monthly-depreciation-reports")
    public ResponseEntity<List<RouMonthlyDepreciationReportDTO>> getAllRouMonthlyDepreciationReports(
        RouMonthlyDepreciationReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouMonthlyDepreciationReports by criteria: {}", criteria);
        Page<RouMonthlyDepreciationReportDTO> page = rouMonthlyDepreciationReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-monthly-depreciation-reports/count} : count all the rouMonthlyDepreciationReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-monthly-depreciation-reports/count")
    public ResponseEntity<Long> countRouMonthlyDepreciationReports(RouMonthlyDepreciationReportCriteria criteria) {
        log.debug("REST request to count RouMonthlyDepreciationReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouMonthlyDepreciationReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-monthly-depreciation-reports/:id} : get the "id" rouMonthlyDepreciationReport.
     *
     * @param id the id of the rouMonthlyDepreciationReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouMonthlyDepreciationReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-monthly-depreciation-reports/{id}")
    public ResponseEntity<RouMonthlyDepreciationReportDTO> getRouMonthlyDepreciationReport(@PathVariable Long id) {
        log.debug("REST request to get RouMonthlyDepreciationReport : {}", id);
        Optional<RouMonthlyDepreciationReportDTO> rouMonthlyDepreciationReportDTO = rouMonthlyDepreciationReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouMonthlyDepreciationReportDTO);
    }

    /**
     * {@code DELETE  /rou-monthly-depreciation-reports/:id} : delete the "id" rouMonthlyDepreciationReport.
     *
     * @param id the id of the rouMonthlyDepreciationReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-monthly-depreciation-reports/{id}")
    public ResponseEntity<Void> deleteRouMonthlyDepreciationReport(@PathVariable Long id) {
        log.debug("REST request to delete RouMonthlyDepreciationReport : {}", id);
        rouMonthlyDepreciationReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-monthly-depreciation-reports?query=:query} : search for the rouMonthlyDepreciationReport corresponding
     * to the query.
     *
     * @param query the query of the rouMonthlyDepreciationReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-monthly-depreciation-reports")
    public ResponseEntity<List<RouMonthlyDepreciationReportDTO>> searchRouMonthlyDepreciationReports(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of RouMonthlyDepreciationReports for query {}", query);
        Page<RouMonthlyDepreciationReportDTO> page = rouMonthlyDepreciationReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
