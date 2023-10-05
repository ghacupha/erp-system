package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.repository.ReportRequisitionRepository;
import io.github.erp.service.ReportRequisitionQueryService;
import io.github.erp.service.ReportRequisitionService;
import io.github.erp.service.criteria.ReportRequisitionCriteria;
import io.github.erp.service.dto.ReportRequisitionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ReportRequisition}.
 */
@RestController
@RequestMapping("/api")
public class ReportRequisitionResource {

    private final Logger log = LoggerFactory.getLogger(ReportRequisitionResource.class);

    private static final String ENTITY_NAME = "gdiDataReportRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportRequisitionService reportRequisitionService;

    private final ReportRequisitionRepository reportRequisitionRepository;

    private final ReportRequisitionQueryService reportRequisitionQueryService;

    public ReportRequisitionResource(
        ReportRequisitionService reportRequisitionService,
        ReportRequisitionRepository reportRequisitionRepository,
        ReportRequisitionQueryService reportRequisitionQueryService
    ) {
        this.reportRequisitionService = reportRequisitionService;
        this.reportRequisitionRepository = reportRequisitionRepository;
        this.reportRequisitionQueryService = reportRequisitionQueryService;
    }

    /**
     * {@code POST  /report-requisitions} : Create a new reportRequisition.
     *
     * @param reportRequisitionDTO the reportRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportRequisitionDTO, or with status {@code 400 (Bad Request)} if the reportRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/report-requisitions")
    public ResponseEntity<ReportRequisitionDTO> createReportRequisition(@Valid @RequestBody ReportRequisitionDTO reportRequisitionDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportRequisition : {}", reportRequisitionDTO);
        if (reportRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportRequisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportRequisitionDTO result = reportRequisitionService.save(reportRequisitionDTO);
        return ResponseEntity
            .created(new URI("/api/report-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /report-requisitions/:id} : Updates an existing reportRequisition.
     *
     * @param id the id of the reportRequisitionDTO to save.
     * @param reportRequisitionDTO the reportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the reportRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/report-requisitions/{id}")
    public ResponseEntity<ReportRequisitionDTO> updateReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportRequisitionDTO reportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportRequisition : {}, {}", id, reportRequisitionDTO);
        if (reportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportRequisitionDTO result = reportRequisitionService.save(reportRequisitionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportRequisitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /report-requisitions/:id} : Partial updates given fields of an existing reportRequisition, field will ignore if it is null
     *
     * @param id the id of the reportRequisitionDTO to save.
     * @param reportRequisitionDTO the reportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the reportRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/report-requisitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportRequisitionDTO> partialUpdateReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportRequisitionDTO reportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportRequisition partially : {}, {}", id, reportRequisitionDTO);
        if (reportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportRequisitionDTO> result = reportRequisitionService.partialUpdate(reportRequisitionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportRequisitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-requisitions} : get all the reportRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportRequisitions in body.
     */
    @GetMapping("/report-requisitions")
    public ResponseEntity<List<ReportRequisitionDTO>> getAllReportRequisitions(ReportRequisitionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReportRequisitions by criteria: {}", criteria);
        Page<ReportRequisitionDTO> page = reportRequisitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-requisitions/count} : count all the reportRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/report-requisitions/count")
    public ResponseEntity<Long> countReportRequisitions(ReportRequisitionCriteria criteria) {
        log.debug("REST request to count ReportRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-requisitions/:id} : get the "id" reportRequisition.
     *
     * @param id the id of the reportRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/report-requisitions/{id}")
    public ResponseEntity<ReportRequisitionDTO> getReportRequisition(@PathVariable Long id) {
        log.debug("REST request to get ReportRequisition : {}", id);
        Optional<ReportRequisitionDTO> reportRequisitionDTO = reportRequisitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportRequisitionDTO);
    }

    /**
     * {@code DELETE  /report-requisitions/:id} : delete the "id" reportRequisition.
     *
     * @param id the id of the reportRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/report-requisitions/{id}")
    public ResponseEntity<Void> deleteReportRequisition(@PathVariable Long id) {
        log.debug("REST request to delete ReportRequisition : {}", id);
        reportRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/report-requisitions?query=:query} : search for the reportRequisition corresponding
     * to the query.
     *
     * @param query the query of the reportRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/report-requisitions")
    public ResponseEntity<List<ReportRequisitionDTO>> searchReportRequisitions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReportRequisitions for query {}", query);
        Page<ReportRequisitionDTO> page = reportRequisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
