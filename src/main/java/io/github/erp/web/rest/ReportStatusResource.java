package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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

import io.github.erp.repository.ReportStatusRepository;
import io.github.erp.service.ReportStatusQueryService;
import io.github.erp.service.ReportStatusService;
import io.github.erp.service.criteria.ReportStatusCriteria;
import io.github.erp.service.dto.ReportStatusDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ReportStatus}.
 */
@RestController
@RequestMapping("/api")
public class ReportStatusResource {

    private final Logger log = LoggerFactory.getLogger(ReportStatusResource.class);

    private static final String ENTITY_NAME = "reportStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportStatusService reportStatusService;

    private final ReportStatusRepository reportStatusRepository;

    private final ReportStatusQueryService reportStatusQueryService;

    public ReportStatusResource(
        ReportStatusService reportStatusService,
        ReportStatusRepository reportStatusRepository,
        ReportStatusQueryService reportStatusQueryService
    ) {
        this.reportStatusService = reportStatusService;
        this.reportStatusRepository = reportStatusRepository;
        this.reportStatusQueryService = reportStatusQueryService;
    }

    /**
     * {@code POST  /report-statuses} : Create a new reportStatus.
     *
     * @param reportStatusDTO the reportStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportStatusDTO, or with status {@code 400 (Bad Request)} if the reportStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/report-statuses")
    public ResponseEntity<ReportStatusDTO> createReportStatus(@RequestBody ReportStatusDTO reportStatusDTO) throws URISyntaxException {
        log.debug("REST request to save ReportStatus : {}", reportStatusDTO);
        if (reportStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportStatusDTO result = reportStatusService.save(reportStatusDTO);
        return ResponseEntity
            .created(new URI("/api/report-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /report-statuses/:id} : Updates an existing reportStatus.
     *
     * @param id the id of the reportStatusDTO to save.
     * @param reportStatusDTO the reportStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportStatusDTO,
     * or with status {@code 400 (Bad Request)} if the reportStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/report-statuses/{id}")
    public ResponseEntity<ReportStatusDTO> updateReportStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportStatusDTO reportStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportStatus : {}, {}", id, reportStatusDTO);
        if (reportStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportStatusDTO result = reportStatusService.save(reportStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /report-statuses/:id} : Partial updates given fields of an existing reportStatus, field will ignore if it is null
     *
     * @param id the id of the reportStatusDTO to save.
     * @param reportStatusDTO the reportStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportStatusDTO,
     * or with status {@code 400 (Bad Request)} if the reportStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/report-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportStatusDTO> partialUpdateReportStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody ReportStatusDTO reportStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportStatus partially : {}, {}", id, reportStatusDTO);
        if (reportStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportStatusDTO> result = reportStatusService.partialUpdate(reportStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-statuses} : get all the reportStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportStatuses in body.
     */
    @GetMapping("/report-statuses")
    public ResponseEntity<List<ReportStatusDTO>> getAllReportStatuses(ReportStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReportStatuses by criteria: {}", criteria);
        Page<ReportStatusDTO> page = reportStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-statuses/count} : count all the reportStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/report-statuses/count")
    public ResponseEntity<Long> countReportStatuses(ReportStatusCriteria criteria) {
        log.debug("REST request to count ReportStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-statuses/:id} : get the "id" reportStatus.
     *
     * @param id the id of the reportStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/report-statuses/{id}")
    public ResponseEntity<ReportStatusDTO> getReportStatus(@PathVariable Long id) {
        log.debug("REST request to get ReportStatus : {}", id);
        Optional<ReportStatusDTO> reportStatusDTO = reportStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportStatusDTO);
    }

    /**
     * {@code DELETE  /report-statuses/:id} : delete the "id" reportStatus.
     *
     * @param id the id of the reportStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/report-statuses/{id}")
    public ResponseEntity<Void> deleteReportStatus(@PathVariable Long id) {
        log.debug("REST request to delete ReportStatus : {}", id);
        reportStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/report-statuses?query=:query} : search for the reportStatus corresponding
     * to the query.
     *
     * @param query the query of the reportStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/report-statuses")
    public ResponseEntity<List<ReportStatusDTO>> searchReportStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReportStatuses for query {}", query);
        Page<ReportStatusDTO> page = reportStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
