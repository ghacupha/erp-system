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

import io.github.erp.repository.RouDepreciationPostingReportRepository;
import io.github.erp.service.RouDepreciationPostingReportQueryService;
import io.github.erp.service.RouDepreciationPostingReportService;
import io.github.erp.service.criteria.RouDepreciationPostingReportCriteria;
import io.github.erp.service.dto.RouDepreciationPostingReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouDepreciationPostingReport}.
 */
@RestController
@RequestMapping("/api")
public class RouDepreciationPostingReportResource {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationPostingReportResource.class);

    private static final String ENTITY_NAME = "rouDepreciationPostingReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouDepreciationPostingReportService rouDepreciationPostingReportService;

    private final RouDepreciationPostingReportRepository rouDepreciationPostingReportRepository;

    private final RouDepreciationPostingReportQueryService rouDepreciationPostingReportQueryService;

    public RouDepreciationPostingReportResource(
        RouDepreciationPostingReportService rouDepreciationPostingReportService,
        RouDepreciationPostingReportRepository rouDepreciationPostingReportRepository,
        RouDepreciationPostingReportQueryService rouDepreciationPostingReportQueryService
    ) {
        this.rouDepreciationPostingReportService = rouDepreciationPostingReportService;
        this.rouDepreciationPostingReportRepository = rouDepreciationPostingReportRepository;
        this.rouDepreciationPostingReportQueryService = rouDepreciationPostingReportQueryService;
    }

    /**
     * {@code POST  /rou-depreciation-posting-reports} : Create a new rouDepreciationPostingReport.
     *
     * @param rouDepreciationPostingReportDTO the rouDepreciationPostingReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouDepreciationPostingReportDTO, or with status {@code 400 (Bad Request)} if the rouDepreciationPostingReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-depreciation-posting-reports")
    public ResponseEntity<RouDepreciationPostingReportDTO> createRouDepreciationPostingReport(
        @Valid @RequestBody RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RouDepreciationPostingReport : {}", rouDepreciationPostingReportDTO);
        if (rouDepreciationPostingReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouDepreciationPostingReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouDepreciationPostingReportDTO result = rouDepreciationPostingReportService.save(rouDepreciationPostingReportDTO);
        return ResponseEntity
            .created(new URI("/api/rou-depreciation-posting-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-depreciation-posting-reports/:id} : Updates an existing rouDepreciationPostingReport.
     *
     * @param id the id of the rouDepreciationPostingReportDTO to save.
     * @param rouDepreciationPostingReportDTO the rouDepreciationPostingReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouDepreciationPostingReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouDepreciationPostingReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouDepreciationPostingReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-depreciation-posting-reports/{id}")
    public ResponseEntity<RouDepreciationPostingReportDTO> updateRouDepreciationPostingReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouDepreciationPostingReport : {}, {}", id, rouDepreciationPostingReportDTO);
        if (rouDepreciationPostingReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouDepreciationPostingReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouDepreciationPostingReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouDepreciationPostingReportDTO result = rouDepreciationPostingReportService.save(rouDepreciationPostingReportDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouDepreciationPostingReportDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /rou-depreciation-posting-reports/:id} : Partial updates given fields of an existing rouDepreciationPostingReport, field will ignore if it is null
     *
     * @param id the id of the rouDepreciationPostingReportDTO to save.
     * @param rouDepreciationPostingReportDTO the rouDepreciationPostingReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouDepreciationPostingReportDTO,
     * or with status {@code 400 (Bad Request)} if the rouDepreciationPostingReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouDepreciationPostingReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouDepreciationPostingReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-depreciation-posting-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouDepreciationPostingReportDTO> partialUpdateRouDepreciationPostingReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RouDepreciationPostingReportDTO rouDepreciationPostingReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RouDepreciationPostingReport partially : {}, {}", id, rouDepreciationPostingReportDTO);
        if (rouDepreciationPostingReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouDepreciationPostingReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouDepreciationPostingReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouDepreciationPostingReportDTO> result = rouDepreciationPostingReportService.partialUpdate(
            rouDepreciationPostingReportDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouDepreciationPostingReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-depreciation-posting-reports} : get all the rouDepreciationPostingReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouDepreciationPostingReports in body.
     */
    @GetMapping("/rou-depreciation-posting-reports")
    public ResponseEntity<List<RouDepreciationPostingReportDTO>> getAllRouDepreciationPostingReports(
        RouDepreciationPostingReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouDepreciationPostingReports by criteria: {}", criteria);
        Page<RouDepreciationPostingReportDTO> page = rouDepreciationPostingReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-depreciation-posting-reports/count} : count all the rouDepreciationPostingReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-depreciation-posting-reports/count")
    public ResponseEntity<Long> countRouDepreciationPostingReports(RouDepreciationPostingReportCriteria criteria) {
        log.debug("REST request to count RouDepreciationPostingReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouDepreciationPostingReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-depreciation-posting-reports/:id} : get the "id" rouDepreciationPostingReport.
     *
     * @param id the id of the rouDepreciationPostingReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouDepreciationPostingReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-depreciation-posting-reports/{id}")
    public ResponseEntity<RouDepreciationPostingReportDTO> getRouDepreciationPostingReport(@PathVariable Long id) {
        log.debug("REST request to get RouDepreciationPostingReport : {}", id);
        Optional<RouDepreciationPostingReportDTO> rouDepreciationPostingReportDTO = rouDepreciationPostingReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouDepreciationPostingReportDTO);
    }

    /**
     * {@code DELETE  /rou-depreciation-posting-reports/:id} : delete the "id" rouDepreciationPostingReport.
     *
     * @param id the id of the rouDepreciationPostingReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-depreciation-posting-reports/{id}")
    public ResponseEntity<Void> deleteRouDepreciationPostingReport(@PathVariable Long id) {
        log.debug("REST request to delete RouDepreciationPostingReport : {}", id);
        rouDepreciationPostingReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-depreciation-posting-reports?query=:query} : search for the rouDepreciationPostingReport corresponding
     * to the query.
     *
     * @param query the query of the rouDepreciationPostingReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-depreciation-posting-reports")
    public ResponseEntity<List<RouDepreciationPostingReportDTO>> searchRouDepreciationPostingReports(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of RouDepreciationPostingReports for query {}", query);
        Page<RouDepreciationPostingReportDTO> page = rouDepreciationPostingReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
