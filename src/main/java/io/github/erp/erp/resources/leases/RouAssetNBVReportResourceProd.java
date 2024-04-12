package io.github.erp.erp.resources.leases;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.repository.RouAssetNBVReportRepository;
import io.github.erp.service.RouAssetNBVReportQueryService;
import io.github.erp.service.RouAssetNBVReportService;
import io.github.erp.service.criteria.RouAssetNBVReportCriteria;
import io.github.erp.service.dto.RouAssetNBVReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouAssetNBVReport}.
 */
@RestController
@RequestMapping("/api/leases")
public class RouAssetNBVReportResourceProd {

    private final Logger log = LoggerFactory.getLogger(RouAssetNBVReportResourceProd.class);

    private static final String ENTITY_NAME = "rouAssetNBVReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouAssetNBVReportService rouAssetNBVReportService;

    private final RouAssetNBVReportRepository rouAssetNBVReportRepository;

    private final RouAssetNBVReportQueryService rouAssetNBVReportQueryService;

    public RouAssetNBVReportResourceProd(
        RouAssetNBVReportService rouAssetNBVReportService,
        RouAssetNBVReportRepository rouAssetNBVReportRepository,
        RouAssetNBVReportQueryService rouAssetNBVReportQueryService
    ) {
        this.rouAssetNBVReportService = rouAssetNBVReportService;
        this.rouAssetNBVReportRepository = rouAssetNBVReportRepository;
        this.rouAssetNBVReportQueryService = rouAssetNBVReportQueryService;
    }

    /**
     * {@code POST  /rou-asset-nbv-reports} : Create a new rouAssetNBVReport.
     *
     * @param rouAssetNBVReportDTO the rouAssetNBVReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouAssetNBVReportDTO, or with status {@code 400 (Bad Request)} if the rouAssetNBVReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-asset-nbv-reports")
    public ResponseEntity<RouAssetNBVReportDTO> createRouAssetNBVReport(@Valid @RequestBody RouAssetNBVReportDTO rouAssetNBVReportDTO)
        throws URISyntaxException {
        log.debug("REST request to save RouAssetNBVReport : {}", rouAssetNBVReportDTO);
        if (rouAssetNBVReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouAssetNBVReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouAssetNBVReportDTO result = rouAssetNBVReportService.save(rouAssetNBVReportDTO);
        return ResponseEntity
            .created(new URI("/api/rou-asset-nbv-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-asset-nbv-reports/:id} : Updates an existing rouAssetNBVReport.
     *
     * @param id the id of the rouAssetNBVReportDTO to save.
     * @param rouAssetNBVReportDTO the rouAssetNBVReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouAssetNBVReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouAssetNBVReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouAssetNBVReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-asset-nbv-reports/{id}")
    public ResponseEntity<RouAssetNBVReportDTO> updateRouAssetNBVReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RouAssetNBVReportDTO rouAssetNBVReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouAssetNBVReport : {}, {}", id, rouAssetNBVReportDTO);
        if (rouAssetNBVReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouAssetNBVReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouAssetNBVReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouAssetNBVReportDTO result = rouAssetNBVReportService.save(rouAssetNBVReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouAssetNBVReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rou-asset-nbv-reports/:id} : Partial updates given fields of an existing rouAssetNBVReport, field will ignore if it is null
     *
     * @param id the id of the rouAssetNBVReportDTO to save.
     * @param rouAssetNBVReportDTO the rouAssetNBVReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouAssetNBVReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouAssetNBVReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouAssetNBVReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouAssetNBVReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-asset-nbv-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouAssetNBVReportDTO> partialUpdateRouAssetNBVReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RouAssetNBVReportDTO rouAssetNBVReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RouAssetNBVReport partially : {}, {}", id, rouAssetNBVReportDTO);
        if (rouAssetNBVReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouAssetNBVReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouAssetNBVReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouAssetNBVReportDTO> result = rouAssetNBVReportService.partialUpdate(rouAssetNBVReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouAssetNBVReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-asset-nbv-reports} : get all the rouAssetNBVReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouAssetNBVReports in body.
     */
    @GetMapping("/rou-asset-nbv-reports")
    public ResponseEntity<List<RouAssetNBVReportDTO>> getAllRouAssetNBVReports(RouAssetNBVReportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RouAssetNBVReports by criteria: {}", criteria);
        Page<RouAssetNBVReportDTO> page = rouAssetNBVReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-asset-nbv-reports/count} : count all the rouAssetNBVReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-asset-nbv-reports/count")
    public ResponseEntity<Long> countRouAssetNBVReports(RouAssetNBVReportCriteria criteria) {
        log.debug("REST request to count RouAssetNBVReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouAssetNBVReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-asset-nbv-reports/:id} : get the "id" rouAssetNBVReport.
     *
     * @param id the id of the rouAssetNBVReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouAssetNBVReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-asset-nbv-reports/{id}")
    public ResponseEntity<RouAssetNBVReportDTO> getRouAssetNBVReport(@PathVariable Long id) {
        log.debug("REST request to get RouAssetNBVReport : {}", id);
        Optional<RouAssetNBVReportDTO> rouAssetNBVReportDTO = rouAssetNBVReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouAssetNBVReportDTO);
    }

    /**
     * {@code DELETE  /rou-asset-nbv-reports/:id} : delete the "id" rouAssetNBVReport.
     *
     * @param id the id of the rouAssetNBVReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-asset-nbv-reports/{id}")
    public ResponseEntity<Void> deleteRouAssetNBVReport(@PathVariable Long id) {
        log.debug("REST request to delete RouAssetNBVReport : {}", id);
        rouAssetNBVReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-asset-nbv-reports?query=:query} : search for the rouAssetNBVReport corresponding
     * to the query.
     *
     * @param query the query of the rouAssetNBVReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-asset-nbv-reports")
    public ResponseEntity<List<RouAssetNBVReportDTO>> searchRouAssetNBVReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RouAssetNBVReports for query {}", query);
        Page<RouAssetNBVReportDTO> page = rouAssetNBVReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
