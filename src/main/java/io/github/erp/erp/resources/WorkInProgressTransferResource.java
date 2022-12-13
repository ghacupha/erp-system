package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.6-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.repository.WorkInProgressTransferRepository;
import io.github.erp.service.WorkInProgressTransferQueryService;
import io.github.erp.service.WorkInProgressTransferService;
import io.github.erp.service.criteria.WorkInProgressTransferCriteria;
import io.github.erp.service.dto.WorkInProgressTransferDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

/**
 * REST controller for managing {@link io.github.erp.domain.WorkInProgressTransfer}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
public class WorkInProgressTransferResource {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressTransferResource.class);

    private static final String ENTITY_NAME = "workInProgressTransfer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkInProgressTransferService workInProgressTransferService;

    private final WorkInProgressTransferRepository workInProgressTransferRepository;

    private final WorkInProgressTransferQueryService workInProgressTransferQueryService;

    public WorkInProgressTransferResource(
        WorkInProgressTransferService workInProgressTransferService,
        WorkInProgressTransferRepository workInProgressTransferRepository,
        WorkInProgressTransferQueryService workInProgressTransferQueryService
    ) {
        this.workInProgressTransferService = workInProgressTransferService;
        this.workInProgressTransferRepository = workInProgressTransferRepository;
        this.workInProgressTransferQueryService = workInProgressTransferQueryService;
    }

    /**
     * {@code POST  /work-in-progress-transfers} : Create a new workInProgressTransfer.
     *
     * @param workInProgressTransferDTO the workInProgressTransferDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workInProgressTransferDTO, or with status {@code 400 (Bad Request)} if the workInProgressTransfer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-in-progress-transfers")
    public ResponseEntity<WorkInProgressTransferDTO> createWorkInProgressTransfer(
        @RequestBody WorkInProgressTransferDTO workInProgressTransferDTO
    ) throws URISyntaxException {
        log.debug("REST request to save WorkInProgressTransfer : {}", workInProgressTransferDTO);
        if (workInProgressTransferDTO.getId() != null) {
            throw new BadRequestAlertException("A new workInProgressTransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkInProgressTransferDTO result = workInProgressTransferService.save(workInProgressTransferDTO);
        return ResponseEntity
            .created(new URI("/api/work-in-progress-transfers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-in-progress-transfers/:id} : Updates an existing workInProgressTransfer.
     *
     * @param id the id of the workInProgressTransferDTO to save.
     * @param workInProgressTransferDTO the workInProgressTransferDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workInProgressTransferDTO,
     * or with status {@code 400 (Bad Request)} if the workInProgressTransferDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workInProgressTransferDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-in-progress-transfers/{id}")
    public ResponseEntity<WorkInProgressTransferDTO> updateWorkInProgressTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkInProgressTransferDTO workInProgressTransferDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkInProgressTransfer : {}, {}", id, workInProgressTransferDTO);
        if (workInProgressTransferDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workInProgressTransferDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workInProgressTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkInProgressTransferDTO result = workInProgressTransferService.save(workInProgressTransferDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workInProgressTransferDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-in-progress-transfers/:id} : Partial updates given fields of an existing workInProgressTransfer, field will ignore if it is null
     *
     * @param id the id of the workInProgressTransferDTO to save.
     * @param workInProgressTransferDTO the workInProgressTransferDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workInProgressTransferDTO,
     * or with status {@code 400 (Bad Request)} if the workInProgressTransferDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workInProgressTransferDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workInProgressTransferDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-in-progress-transfers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkInProgressTransferDTO> partialUpdateWorkInProgressTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody WorkInProgressTransferDTO workInProgressTransferDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkInProgressTransfer partially : {}, {}", id, workInProgressTransferDTO);
        if (workInProgressTransferDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workInProgressTransferDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workInProgressTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkInProgressTransferDTO> result = workInProgressTransferService.partialUpdate(workInProgressTransferDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workInProgressTransferDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /work-in-progress-transfers} : get all the workInProgressTransfers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workInProgressTransfers in body.
     */
    @GetMapping("/work-in-progress-transfers")
    public ResponseEntity<List<WorkInProgressTransferDTO>> getAllWorkInProgressTransfers(
        WorkInProgressTransferCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get WorkInProgressTransfers by criteria: {}", criteria);
        Page<WorkInProgressTransferDTO> page = workInProgressTransferQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-in-progress-transfers/count} : count all the workInProgressTransfers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/work-in-progress-transfers/count")
    public ResponseEntity<Long> countWorkInProgressTransfers(WorkInProgressTransferCriteria criteria) {
        log.debug("REST request to count WorkInProgressTransfers by criteria: {}", criteria);
        return ResponseEntity.ok().body(workInProgressTransferQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /work-in-progress-transfers/:id} : get the "id" workInProgressTransfer.
     *
     * @param id the id of the workInProgressTransferDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workInProgressTransferDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-in-progress-transfers/{id}")
    public ResponseEntity<WorkInProgressTransferDTO> getWorkInProgressTransfer(@PathVariable Long id) {
        log.debug("REST request to get WorkInProgressTransfer : {}", id);
        Optional<WorkInProgressTransferDTO> workInProgressTransferDTO = workInProgressTransferService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workInProgressTransferDTO);
    }

    /**
     * {@code DELETE  /work-in-progress-transfers/:id} : delete the "id" workInProgressTransfer.
     *
     * @param id the id of the workInProgressTransferDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-in-progress-transfers/{id}")
    public ResponseEntity<Void> deleteWorkInProgressTransfer(@PathVariable Long id) {
        log.debug("REST request to delete WorkInProgressTransfer : {}", id);
        workInProgressTransferService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/work-in-progress-transfers?query=:query} : search for the workInProgressTransfer corresponding
     * to the query.
     *
     * @param query the query of the workInProgressTransfer search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/work-in-progress-transfers")
    public ResponseEntity<List<WorkInProgressTransferDTO>> searchWorkInProgressTransfers(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WorkInProgressTransfers for query {}", query);
        Page<WorkInProgressTransferDTO> page = workInProgressTransferService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
