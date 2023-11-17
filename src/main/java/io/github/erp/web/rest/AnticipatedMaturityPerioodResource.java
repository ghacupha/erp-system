package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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

import io.github.erp.repository.AnticipatedMaturityPerioodRepository;
import io.github.erp.service.AnticipatedMaturityPerioodQueryService;
import io.github.erp.service.AnticipatedMaturityPerioodService;
import io.github.erp.service.criteria.AnticipatedMaturityPerioodCriteria;
import io.github.erp.service.dto.AnticipatedMaturityPerioodDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AnticipatedMaturityPeriood}.
 */
@RestController
@RequestMapping("/api")
public class AnticipatedMaturityPerioodResource {

    private final Logger log = LoggerFactory.getLogger(AnticipatedMaturityPerioodResource.class);

    private static final String ENTITY_NAME = "anticipatedMaturityPeriood";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnticipatedMaturityPerioodService anticipatedMaturityPerioodService;

    private final AnticipatedMaturityPerioodRepository anticipatedMaturityPerioodRepository;

    private final AnticipatedMaturityPerioodQueryService anticipatedMaturityPerioodQueryService;

    public AnticipatedMaturityPerioodResource(
        AnticipatedMaturityPerioodService anticipatedMaturityPerioodService,
        AnticipatedMaturityPerioodRepository anticipatedMaturityPerioodRepository,
        AnticipatedMaturityPerioodQueryService anticipatedMaturityPerioodQueryService
    ) {
        this.anticipatedMaturityPerioodService = anticipatedMaturityPerioodService;
        this.anticipatedMaturityPerioodRepository = anticipatedMaturityPerioodRepository;
        this.anticipatedMaturityPerioodQueryService = anticipatedMaturityPerioodQueryService;
    }

    /**
     * {@code POST  /anticipated-maturity-perioods} : Create a new anticipatedMaturityPeriood.
     *
     * @param anticipatedMaturityPerioodDTO the anticipatedMaturityPerioodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new anticipatedMaturityPerioodDTO, or with status {@code 400 (Bad Request)} if the anticipatedMaturityPeriood has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/anticipated-maturity-perioods")
    public ResponseEntity<AnticipatedMaturityPerioodDTO> createAnticipatedMaturityPeriood(
        @Valid @RequestBody AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AnticipatedMaturityPeriood : {}", anticipatedMaturityPerioodDTO);
        if (anticipatedMaturityPerioodDTO.getId() != null) {
            throw new BadRequestAlertException("A new anticipatedMaturityPeriood cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AnticipatedMaturityPerioodDTO result = anticipatedMaturityPerioodService.save(anticipatedMaturityPerioodDTO);
        return ResponseEntity
            .created(new URI("/api/anticipated-maturity-perioods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /anticipated-maturity-perioods/:id} : Updates an existing anticipatedMaturityPeriood.
     *
     * @param id the id of the anticipatedMaturityPerioodDTO to save.
     * @param anticipatedMaturityPerioodDTO the anticipatedMaturityPerioodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anticipatedMaturityPerioodDTO,
     * or with status {@code 400 (Bad Request)} if the anticipatedMaturityPerioodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the anticipatedMaturityPerioodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/anticipated-maturity-perioods/{id}")
    public ResponseEntity<AnticipatedMaturityPerioodDTO> updateAnticipatedMaturityPeriood(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AnticipatedMaturityPeriood : {}, {}", id, anticipatedMaturityPerioodDTO);
        if (anticipatedMaturityPerioodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anticipatedMaturityPerioodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anticipatedMaturityPerioodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AnticipatedMaturityPerioodDTO result = anticipatedMaturityPerioodService.save(anticipatedMaturityPerioodDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anticipatedMaturityPerioodDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /anticipated-maturity-perioods/:id} : Partial updates given fields of an existing anticipatedMaturityPeriood, field will ignore if it is null
     *
     * @param id the id of the anticipatedMaturityPerioodDTO to save.
     * @param anticipatedMaturityPerioodDTO the anticipatedMaturityPerioodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated anticipatedMaturityPerioodDTO,
     * or with status {@code 400 (Bad Request)} if the anticipatedMaturityPerioodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the anticipatedMaturityPerioodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the anticipatedMaturityPerioodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/anticipated-maturity-perioods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AnticipatedMaturityPerioodDTO> partialUpdateAnticipatedMaturityPeriood(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnticipatedMaturityPerioodDTO anticipatedMaturityPerioodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AnticipatedMaturityPeriood partially : {}, {}", id, anticipatedMaturityPerioodDTO);
        if (anticipatedMaturityPerioodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, anticipatedMaturityPerioodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!anticipatedMaturityPerioodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnticipatedMaturityPerioodDTO> result = anticipatedMaturityPerioodService.partialUpdate(anticipatedMaturityPerioodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, anticipatedMaturityPerioodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /anticipated-maturity-perioods} : get all the anticipatedMaturityPerioods.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of anticipatedMaturityPerioods in body.
     */
    @GetMapping("/anticipated-maturity-perioods")
    public ResponseEntity<List<AnticipatedMaturityPerioodDTO>> getAllAnticipatedMaturityPerioods(
        AnticipatedMaturityPerioodCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AnticipatedMaturityPerioods by criteria: {}", criteria);
        Page<AnticipatedMaturityPerioodDTO> page = anticipatedMaturityPerioodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /anticipated-maturity-perioods/count} : count all the anticipatedMaturityPerioods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/anticipated-maturity-perioods/count")
    public ResponseEntity<Long> countAnticipatedMaturityPerioods(AnticipatedMaturityPerioodCriteria criteria) {
        log.debug("REST request to count AnticipatedMaturityPerioods by criteria: {}", criteria);
        return ResponseEntity.ok().body(anticipatedMaturityPerioodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /anticipated-maturity-perioods/:id} : get the "id" anticipatedMaturityPeriood.
     *
     * @param id the id of the anticipatedMaturityPerioodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the anticipatedMaturityPerioodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/anticipated-maturity-perioods/{id}")
    public ResponseEntity<AnticipatedMaturityPerioodDTO> getAnticipatedMaturityPeriood(@PathVariable Long id) {
        log.debug("REST request to get AnticipatedMaturityPeriood : {}", id);
        Optional<AnticipatedMaturityPerioodDTO> anticipatedMaturityPerioodDTO = anticipatedMaturityPerioodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(anticipatedMaturityPerioodDTO);
    }

    /**
     * {@code DELETE  /anticipated-maturity-perioods/:id} : delete the "id" anticipatedMaturityPeriood.
     *
     * @param id the id of the anticipatedMaturityPerioodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/anticipated-maturity-perioods/{id}")
    public ResponseEntity<Void> deleteAnticipatedMaturityPeriood(@PathVariable Long id) {
        log.debug("REST request to delete AnticipatedMaturityPeriood : {}", id);
        anticipatedMaturityPerioodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/anticipated-maturity-perioods?query=:query} : search for the anticipatedMaturityPeriood corresponding
     * to the query.
     *
     * @param query the query of the anticipatedMaturityPeriood search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/anticipated-maturity-perioods")
    public ResponseEntity<List<AnticipatedMaturityPerioodDTO>> searchAnticipatedMaturityPerioods(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of AnticipatedMaturityPerioods for query {}", query);
        Page<AnticipatedMaturityPerioodDTO> page = anticipatedMaturityPerioodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
