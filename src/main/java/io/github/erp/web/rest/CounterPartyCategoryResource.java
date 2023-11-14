package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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

import io.github.erp.repository.CounterPartyCategoryRepository;
import io.github.erp.service.CounterPartyCategoryQueryService;
import io.github.erp.service.CounterPartyCategoryService;
import io.github.erp.service.criteria.CounterPartyCategoryCriteria;
import io.github.erp.service.dto.CounterPartyCategoryDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CounterPartyCategory}.
 */
@RestController
@RequestMapping("/api")
public class CounterPartyCategoryResource {

    private final Logger log = LoggerFactory.getLogger(CounterPartyCategoryResource.class);

    private static final String ENTITY_NAME = "counterPartyCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CounterPartyCategoryService counterPartyCategoryService;

    private final CounterPartyCategoryRepository counterPartyCategoryRepository;

    private final CounterPartyCategoryQueryService counterPartyCategoryQueryService;

    public CounterPartyCategoryResource(
        CounterPartyCategoryService counterPartyCategoryService,
        CounterPartyCategoryRepository counterPartyCategoryRepository,
        CounterPartyCategoryQueryService counterPartyCategoryQueryService
    ) {
        this.counterPartyCategoryService = counterPartyCategoryService;
        this.counterPartyCategoryRepository = counterPartyCategoryRepository;
        this.counterPartyCategoryQueryService = counterPartyCategoryQueryService;
    }

    /**
     * {@code POST  /counter-party-categories} : Create a new counterPartyCategory.
     *
     * @param counterPartyCategoryDTO the counterPartyCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new counterPartyCategoryDTO, or with status {@code 400 (Bad Request)} if the counterPartyCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/counter-party-categories")
    public ResponseEntity<CounterPartyCategoryDTO> createCounterPartyCategory(
        @Valid @RequestBody CounterPartyCategoryDTO counterPartyCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CounterPartyCategory : {}", counterPartyCategoryDTO);
        if (counterPartyCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new counterPartyCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CounterPartyCategoryDTO result = counterPartyCategoryService.save(counterPartyCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/counter-party-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /counter-party-categories/:id} : Updates an existing counterPartyCategory.
     *
     * @param id the id of the counterPartyCategoryDTO to save.
     * @param counterPartyCategoryDTO the counterPartyCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterPartyCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the counterPartyCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the counterPartyCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/counter-party-categories/{id}")
    public ResponseEntity<CounterPartyCategoryDTO> updateCounterPartyCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CounterPartyCategoryDTO counterPartyCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CounterPartyCategory : {}, {}", id, counterPartyCategoryDTO);
        if (counterPartyCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counterPartyCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterPartyCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CounterPartyCategoryDTO result = counterPartyCategoryService.save(counterPartyCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterPartyCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /counter-party-categories/:id} : Partial updates given fields of an existing counterPartyCategory, field will ignore if it is null
     *
     * @param id the id of the counterPartyCategoryDTO to save.
     * @param counterPartyCategoryDTO the counterPartyCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterPartyCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the counterPartyCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the counterPartyCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the counterPartyCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/counter-party-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CounterPartyCategoryDTO> partialUpdateCounterPartyCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CounterPartyCategoryDTO counterPartyCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CounterPartyCategory partially : {}, {}", id, counterPartyCategoryDTO);
        if (counterPartyCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counterPartyCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterPartyCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CounterPartyCategoryDTO> result = counterPartyCategoryService.partialUpdate(counterPartyCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterPartyCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /counter-party-categories} : get all the counterPartyCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of counterPartyCategories in body.
     */
    @GetMapping("/counter-party-categories")
    public ResponseEntity<List<CounterPartyCategoryDTO>> getAllCounterPartyCategories(
        CounterPartyCategoryCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CounterPartyCategories by criteria: {}", criteria);
        Page<CounterPartyCategoryDTO> page = counterPartyCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /counter-party-categories/count} : count all the counterPartyCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/counter-party-categories/count")
    public ResponseEntity<Long> countCounterPartyCategories(CounterPartyCategoryCriteria criteria) {
        log.debug("REST request to count CounterPartyCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(counterPartyCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /counter-party-categories/:id} : get the "id" counterPartyCategory.
     *
     * @param id the id of the counterPartyCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the counterPartyCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/counter-party-categories/{id}")
    public ResponseEntity<CounterPartyCategoryDTO> getCounterPartyCategory(@PathVariable Long id) {
        log.debug("REST request to get CounterPartyCategory : {}", id);
        Optional<CounterPartyCategoryDTO> counterPartyCategoryDTO = counterPartyCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(counterPartyCategoryDTO);
    }

    /**
     * {@code DELETE  /counter-party-categories/:id} : delete the "id" counterPartyCategory.
     *
     * @param id the id of the counterPartyCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/counter-party-categories/{id}")
    public ResponseEntity<Void> deleteCounterPartyCategory(@PathVariable Long id) {
        log.debug("REST request to delete CounterPartyCategory : {}", id);
        counterPartyCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/counter-party-categories?query=:query} : search for the counterPartyCategory corresponding
     * to the query.
     *
     * @param query the query of the counterPartyCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/counter-party-categories")
    public ResponseEntity<List<CounterPartyCategoryDTO>> searchCounterPartyCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CounterPartyCategories for query {}", query);
        Page<CounterPartyCategoryDTO> page = counterPartyCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
