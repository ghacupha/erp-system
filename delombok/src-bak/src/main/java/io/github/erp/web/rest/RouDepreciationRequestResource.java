package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.repository.RouDepreciationRequestRepository;
import io.github.erp.service.RouDepreciationRequestQueryService;
import io.github.erp.service.RouDepreciationRequestService;
import io.github.erp.service.criteria.RouDepreciationRequestCriteria;
import io.github.erp.service.dto.RouDepreciationRequestDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouDepreciationRequest}.
 */
@RestController
@RequestMapping("/api")
public class RouDepreciationRequestResource {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationRequestResource.class);

    private static final String ENTITY_NAME = "rouDepreciationRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouDepreciationRequestService rouDepreciationRequestService;

    private final RouDepreciationRequestRepository rouDepreciationRequestRepository;

    private final RouDepreciationRequestQueryService rouDepreciationRequestQueryService;

    public RouDepreciationRequestResource(
        RouDepreciationRequestService rouDepreciationRequestService,
        RouDepreciationRequestRepository rouDepreciationRequestRepository,
        RouDepreciationRequestQueryService rouDepreciationRequestQueryService
    ) {
        this.rouDepreciationRequestService = rouDepreciationRequestService;
        this.rouDepreciationRequestRepository = rouDepreciationRequestRepository;
        this.rouDepreciationRequestQueryService = rouDepreciationRequestQueryService;
    }

    /**
     * {@code POST  /rou-depreciation-requests} : Create a new rouDepreciationRequest.
     *
     * @param rouDepreciationRequestDTO the rouDepreciationRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouDepreciationRequestDTO, or with status {@code 400 (Bad Request)} if the rouDepreciationRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-depreciation-requests")
    public ResponseEntity<RouDepreciationRequestDTO> createRouDepreciationRequest(
        @Valid @RequestBody RouDepreciationRequestDTO rouDepreciationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RouDepreciationRequest : {}", rouDepreciationRequestDTO);
        if (rouDepreciationRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouDepreciationRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouDepreciationRequestDTO result = rouDepreciationRequestService.save(rouDepreciationRequestDTO);
        return ResponseEntity
            .created(new URI("/api/rou-depreciation-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-depreciation-requests/:id} : Updates an existing rouDepreciationRequest.
     *
     * @param id the id of the rouDepreciationRequestDTO to save.
     * @param rouDepreciationRequestDTO the rouDepreciationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouDepreciationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the rouDepreciationRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouDepreciationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-depreciation-requests/{id}")
    public ResponseEntity<RouDepreciationRequestDTO> updateRouDepreciationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RouDepreciationRequestDTO rouDepreciationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouDepreciationRequest : {}, {}", id, rouDepreciationRequestDTO);
        if (rouDepreciationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouDepreciationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouDepreciationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouDepreciationRequestDTO result = rouDepreciationRequestService.save(rouDepreciationRequestDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouDepreciationRequestDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rou-depreciation-requests/:id} : Partial updates given fields of an existing rouDepreciationRequest, field will ignore if it is null
     *
     * @param id the id of the rouDepreciationRequestDTO to save.
     * @param rouDepreciationRequestDTO the rouDepreciationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouDepreciationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the rouDepreciationRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouDepreciationRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouDepreciationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-depreciation-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouDepreciationRequestDTO> partialUpdateRouDepreciationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RouDepreciationRequestDTO rouDepreciationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RouDepreciationRequest partially : {}, {}", id, rouDepreciationRequestDTO);
        if (rouDepreciationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouDepreciationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouDepreciationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouDepreciationRequestDTO> result = rouDepreciationRequestService.partialUpdate(rouDepreciationRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouDepreciationRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-depreciation-requests} : get all the rouDepreciationRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouDepreciationRequests in body.
     */
    @GetMapping("/rou-depreciation-requests")
    public ResponseEntity<List<RouDepreciationRequestDTO>> getAllRouDepreciationRequests(
        RouDepreciationRequestCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouDepreciationRequests by criteria: {}", criteria);
        Page<RouDepreciationRequestDTO> page = rouDepreciationRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-depreciation-requests/count} : count all the rouDepreciationRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-depreciation-requests/count")
    public ResponseEntity<Long> countRouDepreciationRequests(RouDepreciationRequestCriteria criteria) {
        log.debug("REST request to count RouDepreciationRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouDepreciationRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-depreciation-requests/:id} : get the "id" rouDepreciationRequest.
     *
     * @param id the id of the rouDepreciationRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouDepreciationRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-depreciation-requests/{id}")
    public ResponseEntity<RouDepreciationRequestDTO> getRouDepreciationRequest(@PathVariable Long id) {
        log.debug("REST request to get RouDepreciationRequest : {}", id);
        Optional<RouDepreciationRequestDTO> rouDepreciationRequestDTO = rouDepreciationRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouDepreciationRequestDTO);
    }

    /**
     * {@code DELETE  /rou-depreciation-requests/:id} : delete the "id" rouDepreciationRequest.
     *
     * @param id the id of the rouDepreciationRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-depreciation-requests/{id}")
    public ResponseEntity<Void> deleteRouDepreciationRequest(@PathVariable Long id) {
        log.debug("REST request to delete RouDepreciationRequest : {}", id);
        rouDepreciationRequestService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-depreciation-requests?query=:query} : search for the rouDepreciationRequest corresponding
     * to the query.
     *
     * @param query the query of the rouDepreciationRequest search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-depreciation-requests")
    public ResponseEntity<List<RouDepreciationRequestDTO>> searchRouDepreciationRequests(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RouDepreciationRequests for query {}", query);
        Page<RouDepreciationRequestDTO> page = rouDepreciationRequestService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
