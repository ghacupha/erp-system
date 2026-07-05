package io.github.erp.erp.resources.entity;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.repository.ReportingEntityRepository;
import io.github.erp.service.ReportingEntityQueryService;
import io.github.erp.service.ReportingEntityService;
import io.github.erp.service.criteria.ReportingEntityCriteria;
import io.github.erp.service.dto.ReportingEntityDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ReportingEntity}.
 */
@RestController
@RequestMapping("/api/app")
public class ReportingEntityResourceProd {

    private final Logger log = LoggerFactory.getLogger(ReportingEntityResourceProd.class);

    private static final String ENTITY_NAME = "reportingEntity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportingEntityService reportingEntityService;

    private final ReportingEntityRepository reportingEntityRepository;

    private final ReportingEntityQueryService reportingEntityQueryService;

    public ReportingEntityResourceProd(
        ReportingEntityService reportingEntityService,
        ReportingEntityRepository reportingEntityRepository,
        ReportingEntityQueryService reportingEntityQueryService
    ) {
        this.reportingEntityService = reportingEntityService;
        this.reportingEntityRepository = reportingEntityRepository;
        this.reportingEntityQueryService = reportingEntityQueryService;
    }

    /**
     * {@code POST  /reporting-entities} : Create a new reportingEntity.
     *
     * @param reportingEntityDTO the reportingEntityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportingEntityDTO, or with status {@code 400 (Bad Request)} if the reportingEntity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reporting-entities")
    public ResponseEntity<ReportingEntityDTO> createReportingEntity(@Valid @RequestBody ReportingEntityDTO reportingEntityDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportingEntity : {}", reportingEntityDTO);
        if (reportingEntityDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportingEntity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportingEntityDTO result = reportingEntityService.save(reportingEntityDTO);
        return ResponseEntity
            .created(new URI("/api/reporting-entities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reporting-entities/:id} : Updates an existing reportingEntity.
     *
     * @param id the id of the reportingEntityDTO to save.
     * @param reportingEntityDTO the reportingEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingEntityDTO,
     * or with status {@code 400 (Bad Request)} if the reportingEntityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportingEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reporting-entities/{id}")
    public ResponseEntity<ReportingEntityDTO> updateReportingEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportingEntityDTO reportingEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportingEntity : {}, {}", id, reportingEntityDTO);
        if (reportingEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportingEntityDTO result = reportingEntityService.save(reportingEntityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingEntityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /reporting-entities/:id} : Partial updates given fields of an existing reportingEntity, field will ignore if it is null
     *
     * @param id the id of the reportingEntityDTO to save.
     * @param reportingEntityDTO the reportingEntityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportingEntityDTO,
     * or with status {@code 400 (Bad Request)} if the reportingEntityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportingEntityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportingEntityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/reporting-entities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportingEntityDTO> partialUpdateReportingEntity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportingEntityDTO reportingEntityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportingEntity partially : {}, {}", id, reportingEntityDTO);
        if (reportingEntityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportingEntityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportingEntityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportingEntityDTO> result = reportingEntityService.partialUpdate(reportingEntityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportingEntityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /reporting-entities} : get all the reportingEntities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportingEntities in body.
     */
    @GetMapping("/reporting-entities")
    public ResponseEntity<List<ReportingEntityDTO>> getAllReportingEntities(ReportingEntityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReportingEntities by criteria: {}", criteria);
        Page<ReportingEntityDTO> page = reportingEntityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reporting-entities/count} : count all the reportingEntities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/reporting-entities/count")
    public ResponseEntity<Long> countReportingEntities(ReportingEntityCriteria criteria) {
        log.debug("REST request to count ReportingEntities by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportingEntityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /reporting-entities/:id} : get the "id" reportingEntity.
     *
     * @param id the id of the reportingEntityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportingEntityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reporting-entities/{id}")
    public ResponseEntity<ReportingEntityDTO> getReportingEntity(@PathVariable Long id) {
        log.debug("REST request to get ReportingEntity : {}", id);
        Optional<ReportingEntityDTO> reportingEntityDTO = reportingEntityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportingEntityDTO);
    }

    /**
     * {@code DELETE  /reporting-entities/:id} : delete the "id" reportingEntity.
     *
     * @param id the id of the reportingEntityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reporting-entities/{id}")
    public ResponseEntity<Void> deleteReportingEntity(@PathVariable Long id) {
        log.debug("REST request to delete ReportingEntity : {}", id);
        reportingEntityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/reporting-entities?query=:query} : search for the reportingEntity corresponding
     * to the query.
     *
     * @param query the query of the reportingEntity search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/reporting-entities")
    public ResponseEntity<List<ReportingEntityDTO>> searchReportingEntities(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReportingEntities for query {}", query);
        Page<ReportingEntityDTO> page = reportingEntityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
