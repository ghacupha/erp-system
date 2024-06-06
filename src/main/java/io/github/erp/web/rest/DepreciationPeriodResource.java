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

import io.github.erp.repository.DepreciationPeriodRepository;
import io.github.erp.service.DepreciationPeriodQueryService;
import io.github.erp.service.DepreciationPeriodService;
import io.github.erp.service.criteria.DepreciationPeriodCriteria;
import io.github.erp.service.dto.DepreciationPeriodDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DepreciationPeriod}.
 */
@RestController
@RequestMapping("/api")
public class DepreciationPeriodResource {

    private final Logger log = LoggerFactory.getLogger(DepreciationPeriodResource.class);

    private static final String ENTITY_NAME = "depreciationPeriod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepreciationPeriodService depreciationPeriodService;

    private final DepreciationPeriodRepository depreciationPeriodRepository;

    private final DepreciationPeriodQueryService depreciationPeriodQueryService;

    public DepreciationPeriodResource(
        DepreciationPeriodService depreciationPeriodService,
        DepreciationPeriodRepository depreciationPeriodRepository,
        DepreciationPeriodQueryService depreciationPeriodQueryService
    ) {
        this.depreciationPeriodService = depreciationPeriodService;
        this.depreciationPeriodRepository = depreciationPeriodRepository;
        this.depreciationPeriodQueryService = depreciationPeriodQueryService;
    }

    /**
     * {@code POST  /depreciation-periods} : Create a new depreciationPeriod.
     *
     * @param depreciationPeriodDTO the depreciationPeriodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depreciationPeriodDTO, or with status {@code 400 (Bad Request)} if the depreciationPeriod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depreciation-periods")
    public ResponseEntity<DepreciationPeriodDTO> createDepreciationPeriod(@Valid @RequestBody DepreciationPeriodDTO depreciationPeriodDTO)
        throws URISyntaxException {
        log.debug("REST request to save DepreciationPeriod : {}", depreciationPeriodDTO);
        if (depreciationPeriodDTO.getId() != null) {
            throw new BadRequestAlertException("A new depreciationPeriod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepreciationPeriodDTO result = depreciationPeriodService.save(depreciationPeriodDTO);
        return ResponseEntity
            .created(new URI("/api/depreciation-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depreciation-periods/:id} : Updates an existing depreciationPeriod.
     *
     * @param id the id of the depreciationPeriodDTO to save.
     * @param depreciationPeriodDTO the depreciationPeriodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationPeriodDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationPeriodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depreciationPeriodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depreciation-periods/{id}")
    public ResponseEntity<DepreciationPeriodDTO> updateDepreciationPeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepreciationPeriodDTO depreciationPeriodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DepreciationPeriod : {}, {}", id, depreciationPeriodDTO);
        if (depreciationPeriodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationPeriodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationPeriodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepreciationPeriodDTO result = depreciationPeriodService.save(depreciationPeriodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depreciationPeriodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depreciation-periods/:id} : Partial updates given fields of an existing depreciationPeriod, field will ignore if it is null
     *
     * @param id the id of the depreciationPeriodDTO to save.
     * @param depreciationPeriodDTO the depreciationPeriodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationPeriodDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationPeriodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depreciationPeriodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depreciationPeriodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depreciation-periods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepreciationPeriodDTO> partialUpdateDepreciationPeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepreciationPeriodDTO depreciationPeriodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DepreciationPeriod partially : {}, {}", id, depreciationPeriodDTO);
        if (depreciationPeriodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationPeriodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationPeriodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepreciationPeriodDTO> result = depreciationPeriodService.partialUpdate(depreciationPeriodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depreciationPeriodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depreciation-periods} : get all the depreciationPeriods.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depreciationPeriods in body.
     */
    @GetMapping("/depreciation-periods")
    public ResponseEntity<List<DepreciationPeriodDTO>> getAllDepreciationPeriods(DepreciationPeriodCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DepreciationPeriods by criteria: {}", criteria);
        Page<DepreciationPeriodDTO> page = depreciationPeriodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depreciation-periods/count} : count all the depreciationPeriods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/depreciation-periods/count")
    public ResponseEntity<Long> countDepreciationPeriods(DepreciationPeriodCriteria criteria) {
        log.debug("REST request to count DepreciationPeriods by criteria: {}", criteria);
        return ResponseEntity.ok().body(depreciationPeriodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /depreciation-periods/:id} : get the "id" depreciationPeriod.
     *
     * @param id the id of the depreciationPeriodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depreciationPeriodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depreciation-periods/{id}")
    public ResponseEntity<DepreciationPeriodDTO> getDepreciationPeriod(@PathVariable Long id) {
        log.debug("REST request to get DepreciationPeriod : {}", id);
        Optional<DepreciationPeriodDTO> depreciationPeriodDTO = depreciationPeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depreciationPeriodDTO);
    }

    /**
     * {@code DELETE  /depreciation-periods/:id} : delete the "id" depreciationPeriod.
     *
     * @param id the id of the depreciationPeriodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depreciation-periods/{id}")
    public ResponseEntity<Void> deleteDepreciationPeriod(@PathVariable Long id) {
        log.debug("REST request to delete DepreciationPeriod : {}", id);
        depreciationPeriodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/depreciation-periods?query=:query} : search for the depreciationPeriod corresponding
     * to the query.
     *
     * @param query the query of the depreciationPeriod search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/depreciation-periods")
    public ResponseEntity<List<DepreciationPeriodDTO>> searchDepreciationPeriods(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DepreciationPeriods for query {}", query);
        Page<DepreciationPeriodDTO> page = depreciationPeriodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
