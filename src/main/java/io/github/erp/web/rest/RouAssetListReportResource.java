package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.repository.RouAssetListReportRepository;
import io.github.erp.service.RouAssetListReportQueryService;
import io.github.erp.service.RouAssetListReportService;
import io.github.erp.service.criteria.RouAssetListReportCriteria;
import io.github.erp.service.dto.RouAssetListReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouAssetListReport}.
 */
@RestController
@RequestMapping("/api")
public class RouAssetListReportResource {

    private final Logger log = LoggerFactory.getLogger(RouAssetListReportResource.class);

    private static final String ENTITY_NAME = "rouAssetListReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouAssetListReportService rouAssetListReportService;

    private final RouAssetListReportRepository rouAssetListReportRepository;

    private final RouAssetListReportQueryService rouAssetListReportQueryService;

    public RouAssetListReportResource(
        RouAssetListReportService rouAssetListReportService,
        RouAssetListReportRepository rouAssetListReportRepository,
        RouAssetListReportQueryService rouAssetListReportQueryService
    ) {
        this.rouAssetListReportService = rouAssetListReportService;
        this.rouAssetListReportRepository = rouAssetListReportRepository;
        this.rouAssetListReportQueryService = rouAssetListReportQueryService;
    }

    /**
     * {@code POST  /rou-asset-list-reports} : Create a new rouAssetListReport.
     *
     * @param rouAssetListReportDTO the rouAssetListReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouAssetListReportDTO, or with status {@code 400 (Bad Request)} if the rouAssetListReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-asset-list-reports")
    public ResponseEntity<RouAssetListReportDTO> createRouAssetListReport(@Valid @RequestBody RouAssetListReportDTO rouAssetListReportDTO)
        throws URISyntaxException {
        log.debug("REST request to save RouAssetListReport : {}", rouAssetListReportDTO);
        if (rouAssetListReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouAssetListReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouAssetListReportDTO result = rouAssetListReportService.save(rouAssetListReportDTO);
        return ResponseEntity
            .created(new URI("/api/rou-asset-list-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-asset-list-reports/:id} : Updates an existing rouAssetListReport.
     *
     * @param id the id of the rouAssetListReportDTO to save.
     * @param rouAssetListReportDTO the rouAssetListReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouAssetListReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouAssetListReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouAssetListReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-asset-list-reports/{id}")
    public ResponseEntity<RouAssetListReportDTO> updateRouAssetListReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RouAssetListReportDTO rouAssetListReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouAssetListReport : {}, {}", id, rouAssetListReportDTO);
        if (rouAssetListReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouAssetListReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouAssetListReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouAssetListReportDTO result = rouAssetListReportService.save(rouAssetListReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouAssetListReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rou-asset-list-reports/:id} : Partial updates given fields of an existing rouAssetListReport, field will ignore if it is null
     *
     * @param id the id of the rouAssetListReportDTO to save.
     * @param rouAssetListReportDTO the rouAssetListReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouAssetListReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouAssetListReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouAssetListReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouAssetListReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-asset-list-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouAssetListReportDTO> partialUpdateRouAssetListReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RouAssetListReportDTO rouAssetListReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RouAssetListReport partially : {}, {}", id, rouAssetListReportDTO);
        if (rouAssetListReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouAssetListReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouAssetListReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouAssetListReportDTO> result = rouAssetListReportService.partialUpdate(rouAssetListReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouAssetListReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-asset-list-reports} : get all the rouAssetListReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouAssetListReports in body.
     */
    @GetMapping("/rou-asset-list-reports")
    public ResponseEntity<List<RouAssetListReportDTO>> getAllRouAssetListReports(RouAssetListReportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RouAssetListReports by criteria: {}", criteria);
        Page<RouAssetListReportDTO> page = rouAssetListReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-asset-list-reports/count} : count all the rouAssetListReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-asset-list-reports/count")
    public ResponseEntity<Long> countRouAssetListReports(RouAssetListReportCriteria criteria) {
        log.debug("REST request to count RouAssetListReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouAssetListReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-asset-list-reports/:id} : get the "id" rouAssetListReport.
     *
     * @param id the id of the rouAssetListReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouAssetListReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-asset-list-reports/{id}")
    public ResponseEntity<RouAssetListReportDTO> getRouAssetListReport(@PathVariable Long id) {
        log.debug("REST request to get RouAssetListReport : {}", id);
        Optional<RouAssetListReportDTO> rouAssetListReportDTO = rouAssetListReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouAssetListReportDTO);
    }

    /**
     * {@code DELETE  /rou-asset-list-reports/:id} : delete the "id" rouAssetListReport.
     *
     * @param id the id of the rouAssetListReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-asset-list-reports/{id}")
    public ResponseEntity<Void> deleteRouAssetListReport(@PathVariable Long id) {
        log.debug("REST request to delete RouAssetListReport : {}", id);
        rouAssetListReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-asset-list-reports?query=:query} : search for the rouAssetListReport corresponding
     * to the query.
     *
     * @param query the query of the rouAssetListReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-asset-list-reports")
    public ResponseEntity<List<RouAssetListReportDTO>> searchRouAssetListReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RouAssetListReports for query {}", query);
        Page<RouAssetListReportDTO> page = rouAssetListReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
