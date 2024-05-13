package io.github.erp.erp.resources.wip;

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

import io.github.erp.internal.repository.InternalWorkInProgressOutstandingReportRequisitionRepository;
import io.github.erp.internal.service.assets.InternalWorkInProgressOutstandingReportRequisitionService;
import io.github.erp.service.WorkInProgressOutstandingReportRequisitionQueryService;
import io.github.erp.service.criteria.WorkInProgressOutstandingReportRequisitionCriteria;
import io.github.erp.service.dto.WorkInProgressOutstandingReportRequisitionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.WorkInProgressOutstandingReportRequisition}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
public class WorkInProgressOutstandingReportRequisitionResourceProd {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressOutstandingReportRequisitionResourceProd.class);

    private static final String ENTITY_NAME = "workInProgressOutstandingReportRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalWorkInProgressOutstandingReportRequisitionService workInProgressOutstandingReportRequisitionService;

    private final InternalWorkInProgressOutstandingReportRequisitionRepository workInProgressOutstandingReportRequisitionRepository;

    private final WorkInProgressOutstandingReportRequisitionQueryService workInProgressOutstandingReportRequisitionQueryService;

    public WorkInProgressOutstandingReportRequisitionResourceProd(
        InternalWorkInProgressOutstandingReportRequisitionService workInProgressOutstandingReportRequisitionService,
        InternalWorkInProgressOutstandingReportRequisitionRepository workInProgressOutstandingReportRequisitionRepository,
        WorkInProgressOutstandingReportRequisitionQueryService workInProgressOutstandingReportRequisitionQueryService
    ) {
        this.workInProgressOutstandingReportRequisitionService = workInProgressOutstandingReportRequisitionService;
        this.workInProgressOutstandingReportRequisitionRepository = workInProgressOutstandingReportRequisitionRepository;
        this.workInProgressOutstandingReportRequisitionQueryService = workInProgressOutstandingReportRequisitionQueryService;
    }

    /**
     * {@code POST  /work-in-progress-outstanding-report-requisitions} : Create a new workInProgressOutstandingReportRequisition.
     *
     * @param workInProgressOutstandingReportRequisitionDTO the workInProgressOutstandingReportRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workInProgressOutstandingReportRequisitionDTO, or with status {@code 400 (Bad Request)} if the workInProgressOutstandingReportRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-in-progress-outstanding-report-requisitions")
    public ResponseEntity<WorkInProgressOutstandingReportRequisitionDTO> createWorkInProgressOutstandingReportRequisition(
        @Valid @RequestBody WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save WorkInProgressOutstandingReportRequisition : {}", workInProgressOutstandingReportRequisitionDTO);
        if (workInProgressOutstandingReportRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new workInProgressOutstandingReportRequisition cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        WorkInProgressOutstandingReportRequisitionDTO result = workInProgressOutstandingReportRequisitionService.save(
            workInProgressOutstandingReportRequisitionDTO
        );
        return ResponseEntity
            .created(new URI("/api/work-in-progress-outstanding-report-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-in-progress-outstanding-report-requisitions/:id} : Updates an existing workInProgressOutstandingReportRequisition.
     *
     * @param id the id of the workInProgressOutstandingReportRequisitionDTO to save.
     * @param workInProgressOutstandingReportRequisitionDTO the workInProgressOutstandingReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workInProgressOutstandingReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the workInProgressOutstandingReportRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workInProgressOutstandingReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-in-progress-outstanding-report-requisitions/{id}")
    public ResponseEntity<WorkInProgressOutstandingReportRequisitionDTO> updateWorkInProgressOutstandingReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to update WorkInProgressOutstandingReportRequisition : {}, {}",
            id,
            workInProgressOutstandingReportRequisitionDTO
        );
        if (workInProgressOutstandingReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workInProgressOutstandingReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workInProgressOutstandingReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkInProgressOutstandingReportRequisitionDTO result = workInProgressOutstandingReportRequisitionService.save(
            workInProgressOutstandingReportRequisitionDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    workInProgressOutstandingReportRequisitionDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /work-in-progress-outstanding-report-requisitions/:id} : Partial updates given fields of an existing workInProgressOutstandingReportRequisition, field will ignore if it is null
     *
     * @param id the id of the workInProgressOutstandingReportRequisitionDTO to save.
     * @param workInProgressOutstandingReportRequisitionDTO the workInProgressOutstandingReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workInProgressOutstandingReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the workInProgressOutstandingReportRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workInProgressOutstandingReportRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workInProgressOutstandingReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(
        value = "/work-in-progress-outstanding-report-requisitions/{id}",
        consumes = { "application/json", "application/merge-patch+json" }
    )
    public ResponseEntity<WorkInProgressOutstandingReportRequisitionDTO> partialUpdateWorkInProgressOutstandingReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkInProgressOutstandingReportRequisitionDTO workInProgressOutstandingReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update WorkInProgressOutstandingReportRequisition partially : {}, {}",
            id,
            workInProgressOutstandingReportRequisitionDTO
        );
        if (workInProgressOutstandingReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workInProgressOutstandingReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workInProgressOutstandingReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkInProgressOutstandingReportRequisitionDTO> result = workInProgressOutstandingReportRequisitionService.partialUpdate(
            workInProgressOutstandingReportRequisitionDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                true,
                ENTITY_NAME,
                workInProgressOutstandingReportRequisitionDTO.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /work-in-progress-outstanding-report-requisitions} : get all the workInProgressOutstandingReportRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workInProgressOutstandingReportRequisitions in body.
     */
    @GetMapping("/work-in-progress-outstanding-report-requisitions")
    public ResponseEntity<List<WorkInProgressOutstandingReportRequisitionDTO>> getAllWorkInProgressOutstandingReportRequisitions(
        WorkInProgressOutstandingReportRequisitionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get WorkInProgressOutstandingReportRequisitions by criteria: {}", criteria);
        Page<WorkInProgressOutstandingReportRequisitionDTO> page = workInProgressOutstandingReportRequisitionQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-in-progress-outstanding-report-requisitions/count} : count all the workInProgressOutstandingReportRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/work-in-progress-outstanding-report-requisitions/count")
    public ResponseEntity<Long> countWorkInProgressOutstandingReportRequisitions(
        WorkInProgressOutstandingReportRequisitionCriteria criteria
    ) {
        log.debug("REST request to count WorkInProgressOutstandingReportRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(workInProgressOutstandingReportRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /work-in-progress-outstanding-report-requisitions/:id} : get the "id" workInProgressOutstandingReportRequisition.
     *
     * @param id the id of the workInProgressOutstandingReportRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workInProgressOutstandingReportRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-in-progress-outstanding-report-requisitions/{id}")
    public ResponseEntity<WorkInProgressOutstandingReportRequisitionDTO> getWorkInProgressOutstandingReportRequisition(
        @PathVariable Long id
    ) {
        log.debug("REST request to get WorkInProgressOutstandingReportRequisition : {}", id);
        Optional<WorkInProgressOutstandingReportRequisitionDTO> workInProgressOutstandingReportRequisitionDTO = workInProgressOutstandingReportRequisitionService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(workInProgressOutstandingReportRequisitionDTO);
    }

    /**
     * {@code DELETE  /work-in-progress-outstanding-report-requisitions/:id} : delete the "id" workInProgressOutstandingReportRequisition.
     *
     * @param id the id of the workInProgressOutstandingReportRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-in-progress-outstanding-report-requisitions/{id}")
    public ResponseEntity<Void> deleteWorkInProgressOutstandingReportRequisition(@PathVariable Long id) {
        log.debug("REST request to delete WorkInProgressOutstandingReportRequisition : {}", id);
        workInProgressOutstandingReportRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/work-in-progress-outstanding-report-requisitions?query=:query} : search for the workInProgressOutstandingReportRequisition corresponding
     * to the query.
     *
     * @param query the query of the workInProgressOutstandingReportRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/work-in-progress-outstanding-report-requisitions")
    public ResponseEntity<List<WorkInProgressOutstandingReportRequisitionDTO>> searchWorkInProgressOutstandingReportRequisitions(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of WorkInProgressOutstandingReportRequisitions for query {}", query);
        Page<WorkInProgressOutstandingReportRequisitionDTO> page = workInProgressOutstandingReportRequisitionService.search(
            query,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
