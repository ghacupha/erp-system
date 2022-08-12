package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 26 (Baruch Series)
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
import io.github.erp.domain.UniversallyUniqueMapping;
import io.github.erp.internal.service.InternalUniversallyUniqueMappingService;
import io.github.erp.repository.UniversallyUniqueMappingRepository;
import io.github.erp.repository.search.UniversallyUniqueMappingSearchRepository;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
 * REST controller for managing {@link UniversallyUniqueMapping}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class UniversallyUniqueMappingResource {

    private final Logger log = LoggerFactory.getLogger(UniversallyUniqueMappingResource.class);

    private static final String ENTITY_NAME = "universallyUniqueMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UniversallyUniqueMappingRepository universallyUniqueMappingRepository;

    private final UniversallyUniqueMappingSearchRepository universallyUniqueMappingSearchRepository;

    public UniversallyUniqueMappingResource(
        UniversallyUniqueMappingRepository universallyUniqueMappingRepository,
        UniversallyUniqueMappingSearchRepository universallyUniqueMappingSearchRepository
    ) {
        this.universallyUniqueMappingRepository = universallyUniqueMappingRepository;
        this.universallyUniqueMappingSearchRepository = universallyUniqueMappingSearchRepository;
    }

    /**
     * {@code POST  /universally-unique-mappings} : Create a new universallyUniqueMapping.
     *
     * @param universallyUniqueMapping the universallyUniqueMapping to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new universallyUniqueMapping, or with status {@code 400 (Bad Request)} if the universallyUniqueMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/universally-unique-mappings")
    public ResponseEntity<UniversallyUniqueMapping> createUniversallyUniqueMapping(
        @Valid @RequestBody UniversallyUniqueMapping universallyUniqueMapping
    ) throws URISyntaxException {
        log.debug("REST request to save UniversallyUniqueMapping : {}", universallyUniqueMapping);
        if (universallyUniqueMapping.getId() != null) {
            throw new BadRequestAlertException("A new universallyUniqueMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UniversallyUniqueMapping result = universallyUniqueMappingRepository.save(universallyUniqueMapping);
        universallyUniqueMappingSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/universally-unique-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /universally-unique-mappings/:id} : Updates an existing universallyUniqueMapping.
     *
     * @param id the id of the universallyUniqueMapping to save.
     * @param universallyUniqueMapping the universallyUniqueMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated universallyUniqueMapping,
     * or with status {@code 400 (Bad Request)} if the universallyUniqueMapping is not valid,
     * or with status {@code 500 (Internal Server Error)} if the universallyUniqueMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/universally-unique-mappings/{id}")
    public ResponseEntity<UniversallyUniqueMapping> updateUniversallyUniqueMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UniversallyUniqueMapping universallyUniqueMapping
    ) throws URISyntaxException {
        log.debug("REST request to update UniversallyUniqueMapping : {}, {}", id, universallyUniqueMapping);
        if (universallyUniqueMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, universallyUniqueMapping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!universallyUniqueMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UniversallyUniqueMapping result = universallyUniqueMappingRepository.save(universallyUniqueMapping);
        universallyUniqueMappingSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, universallyUniqueMapping.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /universally-unique-mappings/:id} : Partial updates given fields of an existing universallyUniqueMapping, field will ignore if it is null
     *
     * @param id the id of the universallyUniqueMapping to save.
     * @param universallyUniqueMapping the universallyUniqueMapping to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated universallyUniqueMapping,
     * or with status {@code 400 (Bad Request)} if the universallyUniqueMapping is not valid,
     * or with status {@code 404 (Not Found)} if the universallyUniqueMapping is not found,
     * or with status {@code 500 (Internal Server Error)} if the universallyUniqueMapping couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/universally-unique-mappings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UniversallyUniqueMapping> partialUpdateUniversallyUniqueMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UniversallyUniqueMapping universallyUniqueMapping
    ) throws URISyntaxException {
        log.debug("REST request to partial update UniversallyUniqueMapping partially : {}, {}", id, universallyUniqueMapping);
        if (universallyUniqueMapping.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, universallyUniqueMapping.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!universallyUniqueMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UniversallyUniqueMapping> result = universallyUniqueMappingRepository
            .findById(universallyUniqueMapping.getId())
            .map(existingUniversallyUniqueMapping -> {
                if (universallyUniqueMapping.getUniversalKey() != null) {
                    existingUniversallyUniqueMapping.setUniversalKey(universallyUniqueMapping.getUniversalKey());
                }
                if (universallyUniqueMapping.getMappedValue() != null) {
                    existingUniversallyUniqueMapping.setMappedValue(universallyUniqueMapping.getMappedValue());
                }

                return existingUniversallyUniqueMapping;
            })
            .map(universallyUniqueMappingRepository::save)
            .map(savedUniversallyUniqueMapping -> {
                universallyUniqueMappingSearchRepository.save(savedUniversallyUniqueMapping);

                return savedUniversallyUniqueMapping;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, universallyUniqueMapping.getId().toString())
        );
    }

    /**
     * {@code GET  /universally-unique-mappings} : get all the universallyUniqueMappings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of universallyUniqueMappings in body.
     */
    @GetMapping("/universally-unique-mappings")
    public ResponseEntity<List<UniversallyUniqueMapping>> getAllUniversallyUniqueMappings(Pageable pageable) {
        log.debug("REST request to get a page of UniversallyUniqueMappings");
        Page<UniversallyUniqueMapping> page = universallyUniqueMappingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /universally-unique-mappings/:id} : get the "id" universallyUniqueMapping.
     *
     * @param id the id of the universallyUniqueMapping to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the universallyUniqueMapping, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/universally-unique-mappings/{id}")
    public ResponseEntity<UniversallyUniqueMapping> getUniversallyUniqueMapping(@PathVariable Long id) {
        log.debug("REST request to get UniversallyUniqueMapping : {}", id);
        Optional<UniversallyUniqueMapping> universallyUniqueMapping = universallyUniqueMappingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(universallyUniqueMapping);
    }

    /**
     * {@code DELETE  /universally-unique-mappings/:id} : delete the "id" universallyUniqueMapping.
     *
     * @param id the id of the universallyUniqueMapping to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/universally-unique-mappings/{id}")
    public ResponseEntity<Void> deleteUniversallyUniqueMapping(@PathVariable Long id) {
        log.debug("REST request to delete UniversallyUniqueMapping : {}", id);
        universallyUniqueMappingRepository.deleteById(id);
        universallyUniqueMappingSearchRepository.deleteById(id);
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
    public ResponseEntity<List<UniversallyUniqueMapping>> searchUniversallyUniqueMappings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UniversallyUniqueMappings for query {}", query);
        Page<UniversallyUniqueMapping> page = universallyUniqueMappingSearchRepository.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
