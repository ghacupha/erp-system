package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

import io.github.erp.repository.InsiderCategoryTypesRepository;
import io.github.erp.service.InsiderCategoryTypesQueryService;
import io.github.erp.service.InsiderCategoryTypesService;
import io.github.erp.service.criteria.InsiderCategoryTypesCriteria;
import io.github.erp.service.dto.InsiderCategoryTypesDTO;
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
 * REST controller for managing {@link io.github.erp.domain.InsiderCategoryTypes}.
 */
@RestController
@RequestMapping("/api")
public class InsiderCategoryTypesResource {

    private final Logger log = LoggerFactory.getLogger(InsiderCategoryTypesResource.class);

    private static final String ENTITY_NAME = "insiderCategoryTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InsiderCategoryTypesService insiderCategoryTypesService;

    private final InsiderCategoryTypesRepository insiderCategoryTypesRepository;

    private final InsiderCategoryTypesQueryService insiderCategoryTypesQueryService;

    public InsiderCategoryTypesResource(
        InsiderCategoryTypesService insiderCategoryTypesService,
        InsiderCategoryTypesRepository insiderCategoryTypesRepository,
        InsiderCategoryTypesQueryService insiderCategoryTypesQueryService
    ) {
        this.insiderCategoryTypesService = insiderCategoryTypesService;
        this.insiderCategoryTypesRepository = insiderCategoryTypesRepository;
        this.insiderCategoryTypesQueryService = insiderCategoryTypesQueryService;
    }

    /**
     * {@code POST  /insider-category-types} : Create a new insiderCategoryTypes.
     *
     * @param insiderCategoryTypesDTO the insiderCategoryTypesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new insiderCategoryTypesDTO, or with status {@code 400 (Bad Request)} if the insiderCategoryTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/insider-category-types")
    public ResponseEntity<InsiderCategoryTypesDTO> createInsiderCategoryTypes(
        @Valid @RequestBody InsiderCategoryTypesDTO insiderCategoryTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InsiderCategoryTypes : {}", insiderCategoryTypesDTO);
        if (insiderCategoryTypesDTO.getId() != null) {
            throw new BadRequestAlertException("A new insiderCategoryTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InsiderCategoryTypesDTO result = insiderCategoryTypesService.save(insiderCategoryTypesDTO);
        return ResponseEntity
            .created(new URI("/api/insider-category-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /insider-category-types/:id} : Updates an existing insiderCategoryTypes.
     *
     * @param id the id of the insiderCategoryTypesDTO to save.
     * @param insiderCategoryTypesDTO the insiderCategoryTypesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insiderCategoryTypesDTO,
     * or with status {@code 400 (Bad Request)} if the insiderCategoryTypesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the insiderCategoryTypesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/insider-category-types/{id}")
    public ResponseEntity<InsiderCategoryTypesDTO> updateInsiderCategoryTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InsiderCategoryTypesDTO insiderCategoryTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InsiderCategoryTypes : {}, {}", id, insiderCategoryTypesDTO);
        if (insiderCategoryTypesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insiderCategoryTypesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insiderCategoryTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InsiderCategoryTypesDTO result = insiderCategoryTypesService.save(insiderCategoryTypesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insiderCategoryTypesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /insider-category-types/:id} : Partial updates given fields of an existing insiderCategoryTypes, field will ignore if it is null
     *
     * @param id the id of the insiderCategoryTypesDTO to save.
     * @param insiderCategoryTypesDTO the insiderCategoryTypesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated insiderCategoryTypesDTO,
     * or with status {@code 400 (Bad Request)} if the insiderCategoryTypesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the insiderCategoryTypesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the insiderCategoryTypesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/insider-category-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InsiderCategoryTypesDTO> partialUpdateInsiderCategoryTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InsiderCategoryTypesDTO insiderCategoryTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InsiderCategoryTypes partially : {}, {}", id, insiderCategoryTypesDTO);
        if (insiderCategoryTypesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, insiderCategoryTypesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!insiderCategoryTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InsiderCategoryTypesDTO> result = insiderCategoryTypesService.partialUpdate(insiderCategoryTypesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, insiderCategoryTypesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /insider-category-types} : get all the insiderCategoryTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of insiderCategoryTypes in body.
     */
    @GetMapping("/insider-category-types")
    public ResponseEntity<List<InsiderCategoryTypesDTO>> getAllInsiderCategoryTypes(
        InsiderCategoryTypesCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get InsiderCategoryTypes by criteria: {}", criteria);
        Page<InsiderCategoryTypesDTO> page = insiderCategoryTypesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /insider-category-types/count} : count all the insiderCategoryTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/insider-category-types/count")
    public ResponseEntity<Long> countInsiderCategoryTypes(InsiderCategoryTypesCriteria criteria) {
        log.debug("REST request to count InsiderCategoryTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(insiderCategoryTypesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /insider-category-types/:id} : get the "id" insiderCategoryTypes.
     *
     * @param id the id of the insiderCategoryTypesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the insiderCategoryTypesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/insider-category-types/{id}")
    public ResponseEntity<InsiderCategoryTypesDTO> getInsiderCategoryTypes(@PathVariable Long id) {
        log.debug("REST request to get InsiderCategoryTypes : {}", id);
        Optional<InsiderCategoryTypesDTO> insiderCategoryTypesDTO = insiderCategoryTypesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(insiderCategoryTypesDTO);
    }

    /**
     * {@code DELETE  /insider-category-types/:id} : delete the "id" insiderCategoryTypes.
     *
     * @param id the id of the insiderCategoryTypesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/insider-category-types/{id}")
    public ResponseEntity<Void> deleteInsiderCategoryTypes(@PathVariable Long id) {
        log.debug("REST request to delete InsiderCategoryTypes : {}", id);
        insiderCategoryTypesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/insider-category-types?query=:query} : search for the insiderCategoryTypes corresponding
     * to the query.
     *
     * @param query the query of the insiderCategoryTypes search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/insider-category-types")
    public ResponseEntity<List<InsiderCategoryTypesDTO>> searchInsiderCategoryTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InsiderCategoryTypes for query {}", query);
        Page<InsiderCategoryTypesDTO> page = insiderCategoryTypesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
