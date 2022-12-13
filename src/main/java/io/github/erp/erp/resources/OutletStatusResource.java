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
import io.github.erp.repository.OutletStatusRepository;
import io.github.erp.service.OutletStatusQueryService;
import io.github.erp.service.OutletStatusService;
import io.github.erp.service.criteria.OutletStatusCriteria;
import io.github.erp.service.dto.OutletStatusDTO;
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
 * REST controller for managing {@link io.github.erp.domain.OutletStatus}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class OutletStatusResource {

    private final Logger log = LoggerFactory.getLogger(OutletStatusResource.class);

    private static final String ENTITY_NAME = "outletStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutletStatusService outletStatusService;

    private final OutletStatusRepository outletStatusRepository;

    private final OutletStatusQueryService outletStatusQueryService;

    public OutletStatusResource(
        OutletStatusService outletStatusService,
        OutletStatusRepository outletStatusRepository,
        OutletStatusQueryService outletStatusQueryService
    ) {
        this.outletStatusService = outletStatusService;
        this.outletStatusRepository = outletStatusRepository;
        this.outletStatusQueryService = outletStatusQueryService;
    }

    /**
     * {@code POST  /outlet-statuses} : Create a new outletStatus.
     *
     * @param outletStatusDTO the outletStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outletStatusDTO, or with status {@code 400 (Bad Request)} if the outletStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/outlet-statuses")
    public ResponseEntity<OutletStatusDTO> createOutletStatus(@Valid @RequestBody OutletStatusDTO outletStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to save OutletStatus : {}", outletStatusDTO);
        if (outletStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new outletStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutletStatusDTO result = outletStatusService.save(outletStatusDTO);
        return ResponseEntity
            .created(new URI("/api/outlet-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /outlet-statuses/:id} : Updates an existing outletStatus.
     *
     * @param id the id of the outletStatusDTO to save.
     * @param outletStatusDTO the outletStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outletStatusDTO,
     * or with status {@code 400 (Bad Request)} if the outletStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outletStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/outlet-statuses/{id}")
    public ResponseEntity<OutletStatusDTO> updateOutletStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OutletStatusDTO outletStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OutletStatus : {}, {}", id, outletStatusDTO);
        if (outletStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outletStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outletStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OutletStatusDTO result = outletStatusService.save(outletStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outletStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /outlet-statuses/:id} : Partial updates given fields of an existing outletStatus, field will ignore if it is null
     *
     * @param id the id of the outletStatusDTO to save.
     * @param outletStatusDTO the outletStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outletStatusDTO,
     * or with status {@code 400 (Bad Request)} if the outletStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the outletStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the outletStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/outlet-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OutletStatusDTO> partialUpdateOutletStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OutletStatusDTO outletStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OutletStatus partially : {}, {}", id, outletStatusDTO);
        if (outletStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outletStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outletStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OutletStatusDTO> result = outletStatusService.partialUpdate(outletStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outletStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /outlet-statuses} : get all the outletStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outletStatuses in body.
     */
    @GetMapping("/outlet-statuses")
    public ResponseEntity<List<OutletStatusDTO>> getAllOutletStatuses(OutletStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OutletStatuses by criteria: {}", criteria);
        Page<OutletStatusDTO> page = outletStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /outlet-statuses/count} : count all the outletStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/outlet-statuses/count")
    public ResponseEntity<Long> countOutletStatuses(OutletStatusCriteria criteria) {
        log.debug("REST request to count OutletStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(outletStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /outlet-statuses/:id} : get the "id" outletStatus.
     *
     * @param id the id of the outletStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outletStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/outlet-statuses/{id}")
    public ResponseEntity<OutletStatusDTO> getOutletStatus(@PathVariable Long id) {
        log.debug("REST request to get OutletStatus : {}", id);
        Optional<OutletStatusDTO> outletStatusDTO = outletStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outletStatusDTO);
    }

    /**
     * {@code DELETE  /outlet-statuses/:id} : delete the "id" outletStatus.
     *
     * @param id the id of the outletStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/outlet-statuses/{id}")
    public ResponseEntity<Void> deleteOutletStatus(@PathVariable Long id) {
        log.debug("REST request to delete OutletStatus : {}", id);
        outletStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/outlet-statuses?query=:query} : search for the outletStatus corresponding
     * to the query.
     *
     * @param query the query of the outletStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/outlet-statuses")
    public ResponseEntity<List<OutletStatusDTO>> searchOutletStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OutletStatuses for query {}", query);
        Page<OutletStatusDTO> page = outletStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
