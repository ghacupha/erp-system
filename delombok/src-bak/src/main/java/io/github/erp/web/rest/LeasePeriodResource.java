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

import io.github.erp.repository.LeasePeriodRepository;
import io.github.erp.service.LeasePeriodQueryService;
import io.github.erp.service.LeasePeriodService;
import io.github.erp.service.criteria.LeasePeriodCriteria;
import io.github.erp.service.dto.LeasePeriodDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeasePeriod}.
 */
@RestController
@RequestMapping("/api")
public class LeasePeriodResource {

    private final Logger log = LoggerFactory.getLogger(LeasePeriodResource.class);

    private static final String ENTITY_NAME = "leasePeriod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeasePeriodService leasePeriodService;

    private final LeasePeriodRepository leasePeriodRepository;

    private final LeasePeriodQueryService leasePeriodQueryService;

    public LeasePeriodResource(
        LeasePeriodService leasePeriodService,
        LeasePeriodRepository leasePeriodRepository,
        LeasePeriodQueryService leasePeriodQueryService
    ) {
        this.leasePeriodService = leasePeriodService;
        this.leasePeriodRepository = leasePeriodRepository;
        this.leasePeriodQueryService = leasePeriodQueryService;
    }

    /**
     * {@code POST  /lease-periods} : Create a new leasePeriod.
     *
     * @param leasePeriodDTO the leasePeriodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leasePeriodDTO, or with status {@code 400 (Bad Request)} if the leasePeriod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-periods")
    public ResponseEntity<LeasePeriodDTO> createLeasePeriod(@Valid @RequestBody LeasePeriodDTO leasePeriodDTO) throws URISyntaxException {
        log.debug("REST request to save LeasePeriod : {}", leasePeriodDTO);
        if (leasePeriodDTO.getId() != null) {
            throw new BadRequestAlertException("A new leasePeriod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeasePeriodDTO result = leasePeriodService.save(leasePeriodDTO);
        return ResponseEntity
            .created(new URI("/api/lease-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-periods/:id} : Updates an existing leasePeriod.
     *
     * @param id the id of the leasePeriodDTO to save.
     * @param leasePeriodDTO the leasePeriodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leasePeriodDTO,
     * or with status {@code 400 (Bad Request)} if the leasePeriodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leasePeriodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-periods/{id}")
    public ResponseEntity<LeasePeriodDTO> updateLeasePeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeasePeriodDTO leasePeriodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeasePeriod : {}, {}", id, leasePeriodDTO);
        if (leasePeriodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leasePeriodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leasePeriodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeasePeriodDTO result = leasePeriodService.save(leasePeriodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leasePeriodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lease-periods/:id} : Partial updates given fields of an existing leasePeriod, field will ignore if it is null
     *
     * @param id the id of the leasePeriodDTO to save.
     * @param leasePeriodDTO the leasePeriodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leasePeriodDTO,
     * or with status {@code 400 (Bad Request)} if the leasePeriodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leasePeriodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leasePeriodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-periods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeasePeriodDTO> partialUpdateLeasePeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeasePeriodDTO leasePeriodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeasePeriod partially : {}, {}", id, leasePeriodDTO);
        if (leasePeriodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leasePeriodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leasePeriodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeasePeriodDTO> result = leasePeriodService.partialUpdate(leasePeriodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leasePeriodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-periods} : get all the leasePeriods.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leasePeriods in body.
     */
    @GetMapping("/lease-periods")
    public ResponseEntity<List<LeasePeriodDTO>> getAllLeasePeriods(LeasePeriodCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeasePeriods by criteria: {}", criteria);
        Page<LeasePeriodDTO> page = leasePeriodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-periods/count} : count all the leasePeriods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-periods/count")
    public ResponseEntity<Long> countLeasePeriods(LeasePeriodCriteria criteria) {
        log.debug("REST request to count LeasePeriods by criteria: {}", criteria);
        return ResponseEntity.ok().body(leasePeriodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-periods/:id} : get the "id" leasePeriod.
     *
     * @param id the id of the leasePeriodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leasePeriodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-periods/{id}")
    public ResponseEntity<LeasePeriodDTO> getLeasePeriod(@PathVariable Long id) {
        log.debug("REST request to get LeasePeriod : {}", id);
        Optional<LeasePeriodDTO> leasePeriodDTO = leasePeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leasePeriodDTO);
    }

    /**
     * {@code DELETE  /lease-periods/:id} : delete the "id" leasePeriod.
     *
     * @param id the id of the leasePeriodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-periods/{id}")
    public ResponseEntity<Void> deleteLeasePeriod(@PathVariable Long id) {
        log.debug("REST request to delete LeasePeriod : {}", id);
        leasePeriodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-periods?query=:query} : search for the leasePeriod corresponding
     * to the query.
     *
     * @param query the query of the leasePeriod search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-periods")
    public ResponseEntity<List<LeasePeriodDTO>> searchLeasePeriods(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeasePeriods for query {}", query);
        Page<LeasePeriodDTO> page = leasePeriodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
