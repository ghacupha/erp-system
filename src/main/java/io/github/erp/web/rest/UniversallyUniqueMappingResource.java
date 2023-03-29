package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.1-SNAPSHOT
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

import io.github.erp.repository.UniversallyUniqueMappingRepository;
import io.github.erp.service.UniversallyUniqueMappingQueryService;
import io.github.erp.service.UniversallyUniqueMappingService;
import io.github.erp.service.criteria.UniversallyUniqueMappingCriteria;
import io.github.erp.service.dto.UniversallyUniqueMappingDTO;
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
 * REST controller for managing {@link io.github.erp.domain.UniversallyUniqueMapping}.
 */
@RestController
@RequestMapping("/api")
public class UniversallyUniqueMappingResource {

    private final Logger log = LoggerFactory.getLogger(UniversallyUniqueMappingResource.class);

    private static final String ENTITY_NAME = "universallyUniqueMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniversallyUniqueMappingService universallyUniqueMappingService;

    private final UniversallyUniqueMappingRepository universallyUniqueMappingRepository;

    private final UniversallyUniqueMappingQueryService universallyUniqueMappingQueryService;

    public UniversallyUniqueMappingResource(
        UniversallyUniqueMappingService universallyUniqueMappingService,
        UniversallyUniqueMappingRepository universallyUniqueMappingRepository,
        UniversallyUniqueMappingQueryService universallyUniqueMappingQueryService
    ) {
        this.universallyUniqueMappingService = universallyUniqueMappingService;
        this.universallyUniqueMappingRepository = universallyUniqueMappingRepository;
        this.universallyUniqueMappingQueryService = universallyUniqueMappingQueryService;
    }

    /**
     * {@code POST  /universally-unique-mappings} : Create a new universallyUniqueMapping.
     *
     * @param universallyUniqueMappingDTO the universallyUniqueMappingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new universallyUniqueMappingDTO, or with status {@code 400 (Bad Request)} if the universallyUniqueMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/universally-unique-mappings")
    public ResponseEntity<UniversallyUniqueMappingDTO> createUniversallyUniqueMapping(
        @Valid @RequestBody UniversallyUniqueMappingDTO universallyUniqueMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UniversallyUniqueMapping : {}", universallyUniqueMappingDTO);
        if (universallyUniqueMappingDTO.getId() != null) {
            throw new BadRequestAlertException("A new universallyUniqueMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UniversallyUniqueMappingDTO result = universallyUniqueMappingService.save(universallyUniqueMappingDTO);
        return ResponseEntity
            .created(new URI("/api/universally-unique-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /universally-unique-mappings/:id} : Updates an existing universallyUniqueMapping.
     *
     * @param id the id of the universallyUniqueMappingDTO to save.
     * @param universallyUniqueMappingDTO the universallyUniqueMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated universallyUniqueMappingDTO,
     * or with status {@code 400 (Bad Request)} if the universallyUniqueMappingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the universallyUniqueMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/universally-unique-mappings/{id}")
    public ResponseEntity<UniversallyUniqueMappingDTO> updateUniversallyUniqueMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UniversallyUniqueMappingDTO universallyUniqueMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UniversallyUniqueMapping : {}, {}", id, universallyUniqueMappingDTO);
        if (universallyUniqueMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, universallyUniqueMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!universallyUniqueMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UniversallyUniqueMappingDTO result = universallyUniqueMappingService.save(universallyUniqueMappingDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, universallyUniqueMappingDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /universally-unique-mappings/:id} : Partial updates given fields of an existing universallyUniqueMapping, field will ignore if it is null
     *
     * @param id the id of the universallyUniqueMappingDTO to save.
     * @param universallyUniqueMappingDTO the universallyUniqueMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated universallyUniqueMappingDTO,
     * or with status {@code 400 (Bad Request)} if the universallyUniqueMappingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the universallyUniqueMappingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the universallyUniqueMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/universally-unique-mappings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UniversallyUniqueMappingDTO> partialUpdateUniversallyUniqueMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UniversallyUniqueMappingDTO universallyUniqueMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UniversallyUniqueMapping partially : {}, {}", id, universallyUniqueMappingDTO);
        if (universallyUniqueMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, universallyUniqueMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!universallyUniqueMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UniversallyUniqueMappingDTO> result = universallyUniqueMappingService.partialUpdate(universallyUniqueMappingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, universallyUniqueMappingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /universally-unique-mappings} : get all the universallyUniqueMappings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of universallyUniqueMappings in body.
     */
    @GetMapping("/universally-unique-mappings")
    public ResponseEntity<List<UniversallyUniqueMappingDTO>> getAllUniversallyUniqueMappings(
        UniversallyUniqueMappingCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get UniversallyUniqueMappings by criteria: {}", criteria);
        Page<UniversallyUniqueMappingDTO> page = universallyUniqueMappingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /universally-unique-mappings/count} : count all the universallyUniqueMappings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/universally-unique-mappings/count")
    public ResponseEntity<Long> countUniversallyUniqueMappings(UniversallyUniqueMappingCriteria criteria) {
        log.debug("REST request to count UniversallyUniqueMappings by criteria: {}", criteria);
        return ResponseEntity.ok().body(universallyUniqueMappingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /universally-unique-mappings/:id} : get the "id" universallyUniqueMapping.
     *
     * @param id the id of the universallyUniqueMappingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the universallyUniqueMappingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/universally-unique-mappings/{id}")
    public ResponseEntity<UniversallyUniqueMappingDTO> getUniversallyUniqueMapping(@PathVariable Long id) {
        log.debug("REST request to get UniversallyUniqueMapping : {}", id);
        Optional<UniversallyUniqueMappingDTO> universallyUniqueMappingDTO = universallyUniqueMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(universallyUniqueMappingDTO);
    }

    /**
     * {@code DELETE  /universally-unique-mappings/:id} : delete the "id" universallyUniqueMapping.
     *
     * @param id the id of the universallyUniqueMappingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/universally-unique-mappings/{id}")
    public ResponseEntity<Void> deleteUniversallyUniqueMapping(@PathVariable Long id) {
        log.debug("REST request to delete UniversallyUniqueMapping : {}", id);
        universallyUniqueMappingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/universally-unique-mappings?query=:query} : search for the universallyUniqueMapping corresponding
     * to the query.
     *
     * @param query the query of the universallyUniqueMapping search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/universally-unique-mappings")
    public ResponseEntity<List<UniversallyUniqueMappingDTO>> searchUniversallyUniqueMappings(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of UniversallyUniqueMappings for query {}", query);
        Page<UniversallyUniqueMappingDTO> page = universallyUniqueMappingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
