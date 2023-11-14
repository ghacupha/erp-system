package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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

import io.github.erp.repository.DepreciationEntryRepository;
import io.github.erp.service.DepreciationEntryQueryService;
import io.github.erp.service.DepreciationEntryService;
import io.github.erp.service.criteria.DepreciationEntryCriteria;
import io.github.erp.service.dto.DepreciationEntryDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link io.github.erp.domain.DepreciationEntry}.
 */
@RestController
@RequestMapping("/api")
public class DepreciationEntryResource {

    private final Logger log = LoggerFactory.getLogger(DepreciationEntryResource.class);

    private static final String ENTITY_NAME = "depreciationEntry";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepreciationEntryService depreciationEntryService;

    private final DepreciationEntryRepository depreciationEntryRepository;

    private final DepreciationEntryQueryService depreciationEntryQueryService;

    public DepreciationEntryResource(
        DepreciationEntryService depreciationEntryService,
        DepreciationEntryRepository depreciationEntryRepository,
        DepreciationEntryQueryService depreciationEntryQueryService
    ) {
        this.depreciationEntryService = depreciationEntryService;
        this.depreciationEntryRepository = depreciationEntryRepository;
        this.depreciationEntryQueryService = depreciationEntryQueryService;
    }

    /**
     * {@code POST  /depreciation-entries} : Create a new depreciationEntry.
     *
     * @param depreciationEntryDTO the depreciationEntryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depreciationEntryDTO, or with status {@code 400 (Bad Request)} if the depreciationEntry has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depreciation-entries")
    public ResponseEntity<DepreciationEntryDTO> createDepreciationEntry(@RequestBody DepreciationEntryDTO depreciationEntryDTO)
        throws URISyntaxException {
        log.debug("REST request to save DepreciationEntry : {}", depreciationEntryDTO);
        if (depreciationEntryDTO.getId() != null) {
            throw new BadRequestAlertException("A new depreciationEntry cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepreciationEntryDTO result = depreciationEntryService.save(depreciationEntryDTO);
        return ResponseEntity
            .created(new URI("/api/depreciation-entries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depreciation-entries/:id} : Updates an existing depreciationEntry.
     *
     * @param id the id of the depreciationEntryDTO to save.
     * @param depreciationEntryDTO the depreciationEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationEntryDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationEntryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depreciationEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depreciation-entries/{id}")
    public ResponseEntity<DepreciationEntryDTO> updateDepreciationEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepreciationEntryDTO depreciationEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DepreciationEntry : {}, {}", id, depreciationEntryDTO);
        if (depreciationEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepreciationEntryDTO result = depreciationEntryService.save(depreciationEntryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depreciationEntryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depreciation-entries/:id} : Partial updates given fields of an existing depreciationEntry, field will ignore if it is null
     *
     * @param id the id of the depreciationEntryDTO to save.
     * @param depreciationEntryDTO the depreciationEntryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationEntryDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationEntryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depreciationEntryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depreciationEntryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depreciation-entries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepreciationEntryDTO> partialUpdateDepreciationEntry(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepreciationEntryDTO depreciationEntryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DepreciationEntry partially : {}, {}", id, depreciationEntryDTO);
        if (depreciationEntryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationEntryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationEntryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepreciationEntryDTO> result = depreciationEntryService.partialUpdate(depreciationEntryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depreciationEntryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depreciation-entries} : get all the depreciationEntries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depreciationEntries in body.
     */
    @GetMapping("/depreciation-entries")
    public ResponseEntity<List<DepreciationEntryDTO>> getAllDepreciationEntries(DepreciationEntryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DepreciationEntries by criteria: {}", criteria);
        Page<DepreciationEntryDTO> page = depreciationEntryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depreciation-entries/count} : count all the depreciationEntries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/depreciation-entries/count")
    public ResponseEntity<Long> countDepreciationEntries(DepreciationEntryCriteria criteria) {
        log.debug("REST request to count DepreciationEntries by criteria: {}", criteria);
        return ResponseEntity.ok().body(depreciationEntryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /depreciation-entries/:id} : get the "id" depreciationEntry.
     *
     * @param id the id of the depreciationEntryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depreciationEntryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depreciation-entries/{id}")
    public ResponseEntity<DepreciationEntryDTO> getDepreciationEntry(@PathVariable Long id) {
        log.debug("REST request to get DepreciationEntry : {}", id);
        Optional<DepreciationEntryDTO> depreciationEntryDTO = depreciationEntryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depreciationEntryDTO);
    }

    /**
     * {@code DELETE  /depreciation-entries/:id} : delete the "id" depreciationEntry.
     *
     * @param id the id of the depreciationEntryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depreciation-entries/{id}")
    public ResponseEntity<Void> deleteDepreciationEntry(@PathVariable Long id) {
        log.debug("REST request to delete DepreciationEntry : {}", id);
        depreciationEntryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/depreciation-entries?query=:query} : search for the depreciationEntry corresponding
     * to the query.
     *
     * @param query the query of the depreciationEntry search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/depreciation-entries")
    public ResponseEntity<List<DepreciationEntryDTO>> searchDepreciationEntries(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DepreciationEntries for query {}", query);
        Page<DepreciationEntryDTO> page = depreciationEntryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
