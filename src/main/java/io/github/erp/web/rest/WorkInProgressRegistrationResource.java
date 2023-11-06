package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

import io.github.erp.repository.WorkInProgressRegistrationRepository;
import io.github.erp.service.WorkInProgressRegistrationQueryService;
import io.github.erp.service.WorkInProgressRegistrationService;
import io.github.erp.service.criteria.WorkInProgressRegistrationCriteria;
import io.github.erp.service.dto.WorkInProgressRegistrationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.WorkInProgressRegistration}.
 */
@RestController
@RequestMapping("/api")
public class WorkInProgressRegistrationResource {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressRegistrationResource.class);

    private static final String ENTITY_NAME = "workInProgressRegistration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkInProgressRegistrationService workInProgressRegistrationService;

    private final WorkInProgressRegistrationRepository workInProgressRegistrationRepository;

    private final WorkInProgressRegistrationQueryService workInProgressRegistrationQueryService;

    public WorkInProgressRegistrationResource(
        WorkInProgressRegistrationService workInProgressRegistrationService,
        WorkInProgressRegistrationRepository workInProgressRegistrationRepository,
        WorkInProgressRegistrationQueryService workInProgressRegistrationQueryService
    ) {
        this.workInProgressRegistrationService = workInProgressRegistrationService;
        this.workInProgressRegistrationRepository = workInProgressRegistrationRepository;
        this.workInProgressRegistrationQueryService = workInProgressRegistrationQueryService;
    }

    /**
     * {@code POST  /work-in-progress-registrations} : Create a new workInProgressRegistration.
     *
     * @param workInProgressRegistrationDTO the workInProgressRegistrationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workInProgressRegistrationDTO, or with status {@code 400 (Bad Request)} if the workInProgressRegistration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-in-progress-registrations")
    public ResponseEntity<WorkInProgressRegistrationDTO> createWorkInProgressRegistration(
        @Valid @RequestBody WorkInProgressRegistrationDTO workInProgressRegistrationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save WorkInProgressRegistration : {}", workInProgressRegistrationDTO);
        if (workInProgressRegistrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new workInProgressRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkInProgressRegistrationDTO result = workInProgressRegistrationService.save(workInProgressRegistrationDTO);
        return ResponseEntity
            .created(new URI("/api/work-in-progress-registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-in-progress-registrations/:id} : Updates an existing workInProgressRegistration.
     *
     * @param id the id of the workInProgressRegistrationDTO to save.
     * @param workInProgressRegistrationDTO the workInProgressRegistrationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workInProgressRegistrationDTO,
     * or with status {@code 400 (Bad Request)} if the workInProgressRegistrationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workInProgressRegistrationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-in-progress-registrations/{id}")
    public ResponseEntity<WorkInProgressRegistrationDTO> updateWorkInProgressRegistration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkInProgressRegistrationDTO workInProgressRegistrationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkInProgressRegistration : {}, {}", id, workInProgressRegistrationDTO);
        if (workInProgressRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workInProgressRegistrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workInProgressRegistrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkInProgressRegistrationDTO result = workInProgressRegistrationService.save(workInProgressRegistrationDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workInProgressRegistrationDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /work-in-progress-registrations/:id} : Partial updates given fields of an existing workInProgressRegistration, field will ignore if it is null
     *
     * @param id the id of the workInProgressRegistrationDTO to save.
     * @param workInProgressRegistrationDTO the workInProgressRegistrationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workInProgressRegistrationDTO,
     * or with status {@code 400 (Bad Request)} if the workInProgressRegistrationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workInProgressRegistrationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workInProgressRegistrationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-in-progress-registrations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkInProgressRegistrationDTO> partialUpdateWorkInProgressRegistration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkInProgressRegistrationDTO workInProgressRegistrationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkInProgressRegistration partially : {}, {}", id, workInProgressRegistrationDTO);
        if (workInProgressRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workInProgressRegistrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workInProgressRegistrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkInProgressRegistrationDTO> result = workInProgressRegistrationService.partialUpdate(workInProgressRegistrationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, workInProgressRegistrationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /work-in-progress-registrations} : get all the workInProgressRegistrations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workInProgressRegistrations in body.
     */
    @GetMapping("/work-in-progress-registrations")
    public ResponseEntity<List<WorkInProgressRegistrationDTO>> getAllWorkInProgressRegistrations(
        WorkInProgressRegistrationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get WorkInProgressRegistrations by criteria: {}", criteria);
        Page<WorkInProgressRegistrationDTO> page = workInProgressRegistrationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-in-progress-registrations/count} : count all the workInProgressRegistrations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/work-in-progress-registrations/count")
    public ResponseEntity<Long> countWorkInProgressRegistrations(WorkInProgressRegistrationCriteria criteria) {
        log.debug("REST request to count WorkInProgressRegistrations by criteria: {}", criteria);
        return ResponseEntity.ok().body(workInProgressRegistrationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /work-in-progress-registrations/:id} : get the "id" workInProgressRegistration.
     *
     * @param id the id of the workInProgressRegistrationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workInProgressRegistrationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-in-progress-registrations/{id}")
    public ResponseEntity<WorkInProgressRegistrationDTO> getWorkInProgressRegistration(@PathVariable Long id) {
        log.debug("REST request to get WorkInProgressRegistration : {}", id);
        Optional<WorkInProgressRegistrationDTO> workInProgressRegistrationDTO = workInProgressRegistrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workInProgressRegistrationDTO);
    }

    /**
     * {@code DELETE  /work-in-progress-registrations/:id} : delete the "id" workInProgressRegistration.
     *
     * @param id the id of the workInProgressRegistrationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-in-progress-registrations/{id}")
    public ResponseEntity<Void> deleteWorkInProgressRegistration(@PathVariable Long id) {
        log.debug("REST request to delete WorkInProgressRegistration : {}", id);
        workInProgressRegistrationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/work-in-progress-registrations?query=:query} : search for the workInProgressRegistration corresponding
     * to the query.
     *
     * @param query the query of the workInProgressRegistration search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/work-in-progress-registrations")
    public ResponseEntity<List<WorkInProgressRegistrationDTO>> searchWorkInProgressRegistrations(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of WorkInProgressRegistrations for query {}", query);
        Page<WorkInProgressRegistrationDTO> page = workInProgressRegistrationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
