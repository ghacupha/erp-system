package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import io.github.erp.repository.AutonomousReportRepository;
import io.github.erp.service.AutonomousReportQueryService;
import io.github.erp.service.AutonomousReportService;
import io.github.erp.service.criteria.AutonomousReportCriteria;
import io.github.erp.service.dto.AutonomousReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AutonomousReport}.
 */
@RestController
@RequestMapping("/api")
public class AutonomousReportResource {

    private final Logger log = LoggerFactory.getLogger(AutonomousReportResource.class);

    private static final String ENTITY_NAME = "autonomousReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AutonomousReportService autonomousReportService;

    private final AutonomousReportRepository autonomousReportRepository;

    private final AutonomousReportQueryService autonomousReportQueryService;

    public AutonomousReportResource(
        AutonomousReportService autonomousReportService,
        AutonomousReportRepository autonomousReportRepository,
        AutonomousReportQueryService autonomousReportQueryService
    ) {
        this.autonomousReportService = autonomousReportService;
        this.autonomousReportRepository = autonomousReportRepository;
        this.autonomousReportQueryService = autonomousReportQueryService;
    }

    /**
     * {@code POST  /autonomous-reports} : Create a new autonomousReport.
     *
     * @param autonomousReportDTO the autonomousReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new autonomousReportDTO, or with status {@code 400 (Bad Request)} if the autonomousReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/autonomous-reports")
    public ResponseEntity<AutonomousReportDTO> createAutonomousReport(@Valid @RequestBody AutonomousReportDTO autonomousReportDTO)
        throws URISyntaxException {
        log.debug("REST request to save AutonomousReport : {}", autonomousReportDTO);
        if (autonomousReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new autonomousReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AutonomousReportDTO result = autonomousReportService.save(autonomousReportDTO);
        return ResponseEntity
            .created(new URI("/api/autonomous-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /autonomous-reports/:id} : Updates an existing autonomousReport.
     *
     * @param id the id of the autonomousReportDTO to save.
     * @param autonomousReportDTO the autonomousReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autonomousReportDTO,
     * or with status {@code 400 (Bad Request)} if the autonomousReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the autonomousReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/autonomous-reports/{id}")
    public ResponseEntity<AutonomousReportDTO> updateAutonomousReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AutonomousReportDTO autonomousReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AutonomousReport : {}, {}", id, autonomousReportDTO);
        if (autonomousReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autonomousReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autonomousReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AutonomousReportDTO result = autonomousReportService.save(autonomousReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autonomousReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /autonomous-reports/:id} : Partial updates given fields of an existing autonomousReport, field will ignore if it is null
     *
     * @param id the id of the autonomousReportDTO to save.
     * @param autonomousReportDTO the autonomousReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated autonomousReportDTO,
     * or with status {@code 400 (Bad Request)} if the autonomousReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the autonomousReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the autonomousReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/autonomous-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AutonomousReportDTO> partialUpdateAutonomousReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AutonomousReportDTO autonomousReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AutonomousReport partially : {}, {}", id, autonomousReportDTO);
        if (autonomousReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, autonomousReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!autonomousReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AutonomousReportDTO> result = autonomousReportService.partialUpdate(autonomousReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, autonomousReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /autonomous-reports} : get all the autonomousReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of autonomousReports in body.
     */
    @GetMapping("/autonomous-reports")
    public ResponseEntity<List<AutonomousReportDTO>> getAllAutonomousReports(AutonomousReportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AutonomousReports by criteria: {}", criteria);
        Page<AutonomousReportDTO> page = autonomousReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /autonomous-reports/count} : count all the autonomousReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/autonomous-reports/count")
    public ResponseEntity<Long> countAutonomousReports(AutonomousReportCriteria criteria) {
        log.debug("REST request to count AutonomousReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(autonomousReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /autonomous-reports/:id} : get the "id" autonomousReport.
     *
     * @param id the id of the autonomousReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the autonomousReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/autonomous-reports/{id}")
    public ResponseEntity<AutonomousReportDTO> getAutonomousReport(@PathVariable Long id) {
        log.debug("REST request to get AutonomousReport : {}", id);
        Optional<AutonomousReportDTO> autonomousReportDTO = autonomousReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(autonomousReportDTO);
    }

    /**
     * {@code DELETE  /autonomous-reports/:id} : delete the "id" autonomousReport.
     *
     * @param id the id of the autonomousReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/autonomous-reports/{id}")
    public ResponseEntity<Void> deleteAutonomousReport(@PathVariable Long id) {
        log.debug("REST request to delete AutonomousReport : {}", id);
        autonomousReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/autonomous-reports?query=:query} : search for the autonomousReport corresponding
     * to the query.
     *
     * @param query the query of the autonomousReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/autonomous-reports")
    public ResponseEntity<List<AutonomousReportDTO>> searchAutonomousReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AutonomousReports for query {}", query);
        Page<AutonomousReportDTO> page = autonomousReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
