package io.github.erp.erp.resources.ledgers;

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
import io.github.erp.internal.repository.InternalTACompilationRequestRepository;
import io.github.erp.internal.service.leases.InternalTACompilationRequestService;
import io.github.erp.service.TACompilationRequestQueryService;
import io.github.erp.service.criteria.TACompilationRequestCriteria;
import io.github.erp.service.dto.TACompilationRequestDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
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
 * REST controller for managing {@link io.github.erp.domain.TACompilationRequest}.
 */
@RestController
@RequestMapping("/api/accounts")
public class TACompilationRequestResourceProd {

    private final Logger log = LoggerFactory.getLogger(TACompilationRequestResourceProd.class);

    private static final String ENTITY_NAME = "tACompilationRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalTACompilationRequestService tACompilationRequestService;

    private final InternalTACompilationRequestRepository tACompilationRequestRepository;

    private final TACompilationRequestQueryService tACompilationRequestQueryService;

    public TACompilationRequestResourceProd(
        InternalTACompilationRequestService tACompilationRequestService,
        InternalTACompilationRequestRepository tACompilationRequestRepository,
        TACompilationRequestQueryService tACompilationRequestQueryService
    ) {
        this.tACompilationRequestService = tACompilationRequestService;
        this.tACompilationRequestRepository = tACompilationRequestRepository;
        this.tACompilationRequestQueryService = tACompilationRequestQueryService;
    }

    /**
     * {@code POST  /ta-compilation-requests} : Create a new tACompilationRequest.
     *
     * @param tACompilationRequestDTO the tACompilationRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tACompilationRequestDTO, or with status {@code 400 (Bad Request)} if the tACompilationRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ta-compilation-requests")
    public ResponseEntity<TACompilationRequestDTO> createTACompilationRequest(
        @Valid @RequestBody TACompilationRequestDTO tACompilationRequestDTO
    ) throws URISyntaxException, JobInstanceAlreadyCompleteException, JobExecutionAlreadyRunningException, JobParametersInvalidException, JobRestartException {
        log.debug("REST request to save TACompilationRequest : {}", tACompilationRequestDTO);
        if (tACompilationRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new tACompilationRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }

        TACompilationRequestDTO result = tACompilationRequestService.save(tACompilationRequestDTO);

        tACompilationRequestService.launchTACompilationBatch(tACompilationRequestDTO);

        return ResponseEntity
            .created(new URI("/api/accounts/ta-compilation-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ta-compilation-requests/:id} : Updates an existing tACompilationRequest.
     *
     * @param id the id of the tACompilationRequestDTO to save.
     * @param tACompilationRequestDTO the tACompilationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tACompilationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the tACompilationRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tACompilationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ta-compilation-requests/{id}")
    public ResponseEntity<TACompilationRequestDTO> updateTACompilationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TACompilationRequestDTO tACompilationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TACompilationRequest : {}, {}", id, tACompilationRequestDTO);
        if (tACompilationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tACompilationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tACompilationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TACompilationRequestDTO result = tACompilationRequestService.save(tACompilationRequestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tACompilationRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ta-compilation-requests/:id} : Partial updates given fields of an existing tACompilationRequest, field will ignore if it is null
     *
     * @param id the id of the tACompilationRequestDTO to save.
     * @param tACompilationRequestDTO the tACompilationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tACompilationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the tACompilationRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tACompilationRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tACompilationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ta-compilation-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TACompilationRequestDTO> partialUpdateTACompilationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TACompilationRequestDTO tACompilationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TACompilationRequest partially : {}, {}", id, tACompilationRequestDTO);
        if (tACompilationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tACompilationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tACompilationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TACompilationRequestDTO> result = tACompilationRequestService.partialUpdate(tACompilationRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tACompilationRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ta-compilation-requests} : get all the tACompilationRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tACompilationRequests in body.
     */
    @GetMapping("/ta-compilation-requests")
    public ResponseEntity<List<TACompilationRequestDTO>> getAllTACompilationRequests(
        TACompilationRequestCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TACompilationRequests by criteria: {}", criteria);
        Page<TACompilationRequestDTO> page = tACompilationRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ta-compilation-requests/count} : count all the tACompilationRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ta-compilation-requests/count")
    public ResponseEntity<Long> countTACompilationRequests(TACompilationRequestCriteria criteria) {
        log.debug("REST request to count TACompilationRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(tACompilationRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ta-compilation-requests/:id} : get the "id" tACompilationRequest.
     *
     * @param id the id of the tACompilationRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tACompilationRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ta-compilation-requests/{id}")
    public ResponseEntity<TACompilationRequestDTO> getTACompilationRequest(@PathVariable Long id) {
        log.debug("REST request to get TACompilationRequest : {}", id);
        Optional<TACompilationRequestDTO> tACompilationRequestDTO = tACompilationRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tACompilationRequestDTO);
    }

    /**
     * {@code DELETE  /ta-compilation-requests/:id} : delete the "id" tACompilationRequest.
     *
     * @param id the id of the tACompilationRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ta-compilation-requests/{id}")
    public ResponseEntity<Void> deleteTACompilationRequest(@PathVariable Long id) {
        log.debug("REST request to delete TACompilationRequest : {}", id);
        tACompilationRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ta-compilation-requests?query=:query} : search for the tACompilationRequest corresponding
     * to the query.
     *
     * @param query the query of the tACompilationRequest search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ta-compilation-requests")
    public ResponseEntity<List<TACompilationRequestDTO>> searchTACompilationRequests(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TACompilationRequests for query {}", query);
        Page<TACompilationRequestDTO> page = tACompilationRequestService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
