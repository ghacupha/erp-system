package io.github.erp.web.rest;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
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

import io.github.erp.repository.ProcessStatusRepository;
import io.github.erp.service.ProcessStatusQueryService;
import io.github.erp.service.ProcessStatusService;
import io.github.erp.service.criteria.ProcessStatusCriteria;
import io.github.erp.service.dto.ProcessStatusDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ProcessStatus}.
 */
@RestController
@RequestMapping("/api")
public class ProcessStatusResource {

    private final Logger log = LoggerFactory.getLogger(ProcessStatusResource.class);

    private static final String ENTITY_NAME = "processStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProcessStatusService processStatusService;

    private final ProcessStatusRepository processStatusRepository;

    private final ProcessStatusQueryService processStatusQueryService;

    public ProcessStatusResource(
        ProcessStatusService processStatusService,
        ProcessStatusRepository processStatusRepository,
        ProcessStatusQueryService processStatusQueryService
    ) {
        this.processStatusService = processStatusService;
        this.processStatusRepository = processStatusRepository;
        this.processStatusQueryService = processStatusQueryService;
    }

    /**
     * {@code POST  /process-statuses} : Create a new processStatus.
     *
     * @param processStatusDTO the processStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new processStatusDTO, or with status {@code 400 (Bad Request)} if the processStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/process-statuses")
    public ResponseEntity<ProcessStatusDTO> createProcessStatus(@Valid @RequestBody ProcessStatusDTO processStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to save ProcessStatus : {}", processStatusDTO);
        if (processStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new processStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProcessStatusDTO result = processStatusService.save(processStatusDTO);
        return ResponseEntity
            .created(new URI("/api/process-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /process-statuses/:id} : Updates an existing processStatus.
     *
     * @param id the id of the processStatusDTO to save.
     * @param processStatusDTO the processStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processStatusDTO,
     * or with status {@code 400 (Bad Request)} if the processStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the processStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/process-statuses/{id}")
    public ResponseEntity<ProcessStatusDTO> updateProcessStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProcessStatusDTO processStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProcessStatus : {}, {}", id, processStatusDTO);
        if (processStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProcessStatusDTO result = processStatusService.save(processStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /process-statuses/:id} : Partial updates given fields of an existing processStatus, field will ignore if it is null
     *
     * @param id the id of the processStatusDTO to save.
     * @param processStatusDTO the processStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated processStatusDTO,
     * or with status {@code 400 (Bad Request)} if the processStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the processStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the processStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/process-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProcessStatusDTO> partialUpdateProcessStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProcessStatusDTO processStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProcessStatus partially : {}, {}", id, processStatusDTO);
        if (processStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, processStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!processStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProcessStatusDTO> result = processStatusService.partialUpdate(processStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, processStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /process-statuses} : get all the processStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of processStatuses in body.
     */
    @GetMapping("/process-statuses")
    public ResponseEntity<List<ProcessStatusDTO>> getAllProcessStatuses(ProcessStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ProcessStatuses by criteria: {}", criteria);
        Page<ProcessStatusDTO> page = processStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /process-statuses/count} : count all the processStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/process-statuses/count")
    public ResponseEntity<Long> countProcessStatuses(ProcessStatusCriteria criteria) {
        log.debug("REST request to count ProcessStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(processStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /process-statuses/:id} : get the "id" processStatus.
     *
     * @param id the id of the processStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the processStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/process-statuses/{id}")
    public ResponseEntity<ProcessStatusDTO> getProcessStatus(@PathVariable Long id) {
        log.debug("REST request to get ProcessStatus : {}", id);
        Optional<ProcessStatusDTO> processStatusDTO = processStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(processStatusDTO);
    }

    /**
     * {@code DELETE  /process-statuses/:id} : delete the "id" processStatus.
     *
     * @param id the id of the processStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/process-statuses/{id}")
    public ResponseEntity<Void> deleteProcessStatus(@PathVariable Long id) {
        log.debug("REST request to delete ProcessStatus : {}", id);
        processStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/process-statuses?query=:query} : search for the processStatus corresponding
     * to the query.
     *
     * @param query the query of the processStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/process-statuses")
    public ResponseEntity<List<ProcessStatusDTO>> searchProcessStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ProcessStatuses for query {}", query);
        Page<ProcessStatusDTO> page = processStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
