package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.repository.RouInitialDirectCostRepository;
import io.github.erp.service.RouInitialDirectCostQueryService;
import io.github.erp.service.RouInitialDirectCostService;
import io.github.erp.service.criteria.RouInitialDirectCostCriteria;
import io.github.erp.service.dto.RouInitialDirectCostDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouInitialDirectCost}.
 */
@RestController
@RequestMapping("/api")
public class RouInitialDirectCostResource {

    private final Logger log = LoggerFactory.getLogger(RouInitialDirectCostResource.class);

    private static final String ENTITY_NAME = "rouInitialDirectCost";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouInitialDirectCostService rouInitialDirectCostService;

    private final RouInitialDirectCostRepository rouInitialDirectCostRepository;

    private final RouInitialDirectCostQueryService rouInitialDirectCostQueryService;

    public RouInitialDirectCostResource(
        RouInitialDirectCostService rouInitialDirectCostService,
        RouInitialDirectCostRepository rouInitialDirectCostRepository,
        RouInitialDirectCostQueryService rouInitialDirectCostQueryService
    ) {
        this.rouInitialDirectCostService = rouInitialDirectCostService;
        this.rouInitialDirectCostRepository = rouInitialDirectCostRepository;
        this.rouInitialDirectCostQueryService = rouInitialDirectCostQueryService;
    }

    /**
     * {@code POST  /rou-initial-direct-costs} : Create a new rouInitialDirectCost.
     *
     * @param rouInitialDirectCostDTO the rouInitialDirectCostDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouInitialDirectCostDTO, or with status {@code 400 (Bad Request)} if the rouInitialDirectCost has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-initial-direct-costs")
    public ResponseEntity<RouInitialDirectCostDTO> createRouInitialDirectCost(
        @Valid @RequestBody RouInitialDirectCostDTO rouInitialDirectCostDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RouInitialDirectCost : {}", rouInitialDirectCostDTO);
        if (rouInitialDirectCostDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouInitialDirectCost cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouInitialDirectCostDTO result = rouInitialDirectCostService.save(rouInitialDirectCostDTO);
        return ResponseEntity
            .created(new URI("/api/rou-initial-direct-costs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-initial-direct-costs/:id} : Updates an existing rouInitialDirectCost.
     *
     * @param id the id of the rouInitialDirectCostDTO to save.
     * @param rouInitialDirectCostDTO the rouInitialDirectCostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouInitialDirectCostDTO,
     * or with status {@code 400 (Bad Request)} if the rouInitialDirectCostDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouInitialDirectCostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-initial-direct-costs/{id}")
    public ResponseEntity<RouInitialDirectCostDTO> updateRouInitialDirectCost(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RouInitialDirectCostDTO rouInitialDirectCostDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouInitialDirectCost : {}, {}", id, rouInitialDirectCostDTO);
        if (rouInitialDirectCostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouInitialDirectCostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouInitialDirectCostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouInitialDirectCostDTO result = rouInitialDirectCostService.save(rouInitialDirectCostDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouInitialDirectCostDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rou-initial-direct-costs/:id} : Partial updates given fields of an existing rouInitialDirectCost, field will ignore if it is null
     *
     * @param id the id of the rouInitialDirectCostDTO to save.
     * @param rouInitialDirectCostDTO the rouInitialDirectCostDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouInitialDirectCostDTO,
     * or with status {@code 400 (Bad Request)} if the rouInitialDirectCostDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouInitialDirectCostDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouInitialDirectCostDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-initial-direct-costs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouInitialDirectCostDTO> partialUpdateRouInitialDirectCost(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RouInitialDirectCostDTO rouInitialDirectCostDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RouInitialDirectCost partially : {}, {}", id, rouInitialDirectCostDTO);
        if (rouInitialDirectCostDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouInitialDirectCostDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouInitialDirectCostRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouInitialDirectCostDTO> result = rouInitialDirectCostService.partialUpdate(rouInitialDirectCostDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouInitialDirectCostDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-initial-direct-costs} : get all the rouInitialDirectCosts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouInitialDirectCosts in body.
     */
    @GetMapping("/rou-initial-direct-costs")
    public ResponseEntity<List<RouInitialDirectCostDTO>> getAllRouInitialDirectCosts(
        RouInitialDirectCostCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouInitialDirectCosts by criteria: {}", criteria);
        Page<RouInitialDirectCostDTO> page = rouInitialDirectCostQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-initial-direct-costs/count} : count all the rouInitialDirectCosts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-initial-direct-costs/count")
    public ResponseEntity<Long> countRouInitialDirectCosts(RouInitialDirectCostCriteria criteria) {
        log.debug("REST request to count RouInitialDirectCosts by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouInitialDirectCostQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-initial-direct-costs/:id} : get the "id" rouInitialDirectCost.
     *
     * @param id the id of the rouInitialDirectCostDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouInitialDirectCostDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-initial-direct-costs/{id}")
    public ResponseEntity<RouInitialDirectCostDTO> getRouInitialDirectCost(@PathVariable Long id) {
        log.debug("REST request to get RouInitialDirectCost : {}", id);
        Optional<RouInitialDirectCostDTO> rouInitialDirectCostDTO = rouInitialDirectCostService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouInitialDirectCostDTO);
    }

    /**
     * {@code DELETE  /rou-initial-direct-costs/:id} : delete the "id" rouInitialDirectCost.
     *
     * @param id the id of the rouInitialDirectCostDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-initial-direct-costs/{id}")
    public ResponseEntity<Void> deleteRouInitialDirectCost(@PathVariable Long id) {
        log.debug("REST request to delete RouInitialDirectCost : {}", id);
        rouInitialDirectCostService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-initial-direct-costs?query=:query} : search for the rouInitialDirectCost corresponding
     * to the query.
     *
     * @param query the query of the rouInitialDirectCost search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-initial-direct-costs")
    public ResponseEntity<List<RouInitialDirectCostDTO>> searchRouInitialDirectCosts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RouInitialDirectCosts for query {}", query);
        Page<RouInitialDirectCostDTO> page = rouInitialDirectCostService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
