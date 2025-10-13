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

import io.github.erp.repository.DepreciationReportRepository;
import io.github.erp.service.DepreciationReportQueryService;
import io.github.erp.service.DepreciationReportService;
import io.github.erp.service.criteria.DepreciationReportCriteria;
import io.github.erp.service.dto.DepreciationReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DepreciationReport}.
 */
@RestController
@RequestMapping("/api")
public class DepreciationReportResource {

    private final Logger log = LoggerFactory.getLogger(DepreciationReportResource.class);

    private static final String ENTITY_NAME = "depreciationReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepreciationReportService depreciationReportService;

    private final DepreciationReportRepository depreciationReportRepository;

    private final DepreciationReportQueryService depreciationReportQueryService;

    public DepreciationReportResource(
        DepreciationReportService depreciationReportService,
        DepreciationReportRepository depreciationReportRepository,
        DepreciationReportQueryService depreciationReportQueryService
    ) {
        this.depreciationReportService = depreciationReportService;
        this.depreciationReportRepository = depreciationReportRepository;
        this.depreciationReportQueryService = depreciationReportQueryService;
    }

    /**
     * {@code POST  /depreciation-reports} : Create a new depreciationReport.
     *
     * @param depreciationReportDTO the depreciationReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depreciationReportDTO, or with status {@code 400 (Bad Request)} if the depreciationReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depreciation-reports")
    public ResponseEntity<DepreciationReportDTO> createDepreciationReport(@Valid @RequestBody DepreciationReportDTO depreciationReportDTO)
        throws URISyntaxException {
        log.debug("REST request to save DepreciationReport : {}", depreciationReportDTO);
        if (depreciationReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new depreciationReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepreciationReportDTO result = depreciationReportService.save(depreciationReportDTO);
        return ResponseEntity
            .created(new URI("/api/depreciation-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depreciation-reports/:id} : Updates an existing depreciationReport.
     *
     * @param id the id of the depreciationReportDTO to save.
     * @param depreciationReportDTO the depreciationReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationReportDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depreciationReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depreciation-reports/{id}")
    public ResponseEntity<DepreciationReportDTO> updateDepreciationReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepreciationReportDTO depreciationReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DepreciationReport : {}, {}", id, depreciationReportDTO);
        if (depreciationReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepreciationReportDTO result = depreciationReportService.save(depreciationReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depreciationReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depreciation-reports/:id} : Partial updates given fields of an existing depreciationReport, field will ignore if it is null
     *
     * @param id the id of the depreciationReportDTO to save.
     * @param depreciationReportDTO the depreciationReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationReportDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depreciationReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depreciationReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depreciation-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepreciationReportDTO> partialUpdateDepreciationReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepreciationReportDTO depreciationReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DepreciationReport partially : {}, {}", id, depreciationReportDTO);
        if (depreciationReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepreciationReportDTO> result = depreciationReportService.partialUpdate(depreciationReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depreciationReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depreciation-reports} : get all the depreciationReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depreciationReports in body.
     */
    @GetMapping("/depreciation-reports")
    public ResponseEntity<List<DepreciationReportDTO>> getAllDepreciationReports(DepreciationReportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DepreciationReports by criteria: {}", criteria);
        Page<DepreciationReportDTO> page = depreciationReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depreciation-reports/count} : count all the depreciationReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/depreciation-reports/count")
    public ResponseEntity<Long> countDepreciationReports(DepreciationReportCriteria criteria) {
        log.debug("REST request to count DepreciationReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(depreciationReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /depreciation-reports/:id} : get the "id" depreciationReport.
     *
     * @param id the id of the depreciationReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depreciationReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depreciation-reports/{id}")
    public ResponseEntity<DepreciationReportDTO> getDepreciationReport(@PathVariable Long id) {
        log.debug("REST request to get DepreciationReport : {}", id);
        Optional<DepreciationReportDTO> depreciationReportDTO = depreciationReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depreciationReportDTO);
    }

    /**
     * {@code DELETE  /depreciation-reports/:id} : delete the "id" depreciationReport.
     *
     * @param id the id of the depreciationReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depreciation-reports/{id}")
    public ResponseEntity<Void> deleteDepreciationReport(@PathVariable Long id) {
        log.debug("REST request to delete DepreciationReport : {}", id);
        depreciationReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/depreciation-reports?query=:query} : search for the depreciationReport corresponding
     * to the query.
     *
     * @param query the query of the depreciationReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/depreciation-reports")
    public ResponseEntity<List<DepreciationReportDTO>> searchDepreciationReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DepreciationReports for query {}", query);
        Page<DepreciationReportDTO> page = depreciationReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
