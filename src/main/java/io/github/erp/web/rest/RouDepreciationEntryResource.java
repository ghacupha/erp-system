package io.github.erp.web.rest;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.RouDepreciationEntryRepository;
import io.github.erp.service.RouDepreciationEntryQueryService;
import io.github.erp.service.RouDepreciationEntryService;
import io.github.erp.service.criteria.RouDepreciationEntryCriteria;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouDepreciationEntry}.
 */
@RestController
@RequestMapping("/api")
public class RouDepreciationEntryResource {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationEntryResource.class);

    private static final String ENTITY_NAME = "rouDepreciationEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouDepreciationEntryService rouDepreciationEntryService;

    private final RouDepreciationEntryRepository rouDepreciationEntryRepository;

    private final RouDepreciationEntryQueryService rouDepreciationEntryQueryService;

    public RouDepreciationEntryResource(
        RouDepreciationEntryService rouDepreciationEntryService,
        RouDepreciationEntryRepository rouDepreciationEntryRepository,
        RouDepreciationEntryQueryService rouDepreciationEntryQueryService
    ) {
        this.rouDepreciationEntryService = rouDepreciationEntryService;
        this.rouDepreciationEntryRepository = rouDepreciationEntryRepository;
        this.rouDepreciationEntryQueryService = rouDepreciationEntryQueryService;
    }

    /**
     * {@code POST  /rou-depreciation-entries} : Create a new rouDepreciationEntry.
     *
     * @param rouDepreciationEntryDTO the rouDepreciationEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouDepreciationEntryDTO, or with status {@code 400 (Bad Request)} if the rouDepreciationEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-depreciation-entries")
    public ResponseEntity<RouDepreciationEntryDTO> createRouDepreciationEntry(
        @Valid @RequestBody RouDepreciationEntryDTO rouDepreciationEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RouDepreciationEntry : {}", rouDepreciationEntryDTO);
        if (rouDepreciationEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouDepreciationEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouDepreciationEntryDTO result = rouDepreciationEntryService.save(rouDepreciationEntryDTO);
        return ResponseEntity
            .created(new URI("/api/rou-depreciation-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-depreciation-entries/:id} : Updates an existing rouDepreciationEntry.
     *
     * @param id the id of the rouDepreciationEntryDTO to save.
     * @param rouDepreciationEntryDTO the rouDepreciationEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouDepreciationEntryDTO,
     * or with status {@code 400 (Bad Request)} if the rouDepreciationEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouDepreciationEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-depreciation-entries/{id}")
    public ResponseEntity<RouDepreciationEntryDTO> updateRouDepreciationEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RouDepreciationEntryDTO rouDepreciationEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouDepreciationEntry : {}, {}", id, rouDepreciationEntryDTO);
        if (rouDepreciationEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouDepreciationEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouDepreciationEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouDepreciationEntryDTO result = rouDepreciationEntryService.save(rouDepreciationEntryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouDepreciationEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rou-depreciation-entries/:id} : Partial updates given fields of an existing rouDepreciationEntry, field will ignore if it is null
     *
     * @param id the id of the rouDepreciationEntryDTO to save.
     * @param rouDepreciationEntryDTO the rouDepreciationEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouDepreciationEntryDTO,
     * or with status {@code 400 (Bad Request)} if the rouDepreciationEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouDepreciationEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouDepreciationEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-depreciation-entries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouDepreciationEntryDTO> partialUpdateRouDepreciationEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RouDepreciationEntryDTO rouDepreciationEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RouDepreciationEntry partially : {}, {}", id, rouDepreciationEntryDTO);
        if (rouDepreciationEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouDepreciationEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouDepreciationEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouDepreciationEntryDTO> result = rouDepreciationEntryService.partialUpdate(rouDepreciationEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouDepreciationEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-depreciation-entries} : get all the rouDepreciationEntries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouDepreciationEntries in body.
     */
    @GetMapping("/rou-depreciation-entries")
    public ResponseEntity<List<RouDepreciationEntryDTO>> getAllRouDepreciationEntries(
        RouDepreciationEntryCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouDepreciationEntries by criteria: {}", criteria);
        Page<RouDepreciationEntryDTO> page = rouDepreciationEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-depreciation-entries/count} : count all the rouDepreciationEntries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-depreciation-entries/count")
    public ResponseEntity<Long> countRouDepreciationEntries(RouDepreciationEntryCriteria criteria) {
        log.debug("REST request to count RouDepreciationEntries by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouDepreciationEntryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-depreciation-entries/:id} : get the "id" rouDepreciationEntry.
     *
     * @param id the id of the rouDepreciationEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouDepreciationEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-depreciation-entries/{id}")
    public ResponseEntity<RouDepreciationEntryDTO> getRouDepreciationEntry(@PathVariable Long id) {
        log.debug("REST request to get RouDepreciationEntry : {}", id);
        Optional<RouDepreciationEntryDTO> rouDepreciationEntryDTO = rouDepreciationEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouDepreciationEntryDTO);
    }

    /**
     * {@code DELETE  /rou-depreciation-entries/:id} : delete the "id" rouDepreciationEntry.
     *
     * @param id the id of the rouDepreciationEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-depreciation-entries/{id}")
    public ResponseEntity<Void> deleteRouDepreciationEntry(@PathVariable Long id) {
        log.debug("REST request to delete RouDepreciationEntry : {}", id);
        rouDepreciationEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-depreciation-entries?query=:query} : search for the rouDepreciationEntry corresponding
     * to the query.
     *
     * @param query the query of the rouDepreciationEntry search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-depreciation-entries")
    public ResponseEntity<List<RouDepreciationEntryDTO>> searchRouDepreciationEntries(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RouDepreciationEntries for query {}", query);
        Page<RouDepreciationEntryDTO> page = rouDepreciationEntryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
