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

import io.github.erp.repository.ExecutiveCategoryTypeRepository;
import io.github.erp.service.ExecutiveCategoryTypeQueryService;
import io.github.erp.service.ExecutiveCategoryTypeService;
import io.github.erp.service.criteria.ExecutiveCategoryTypeCriteria;
import io.github.erp.service.dto.ExecutiveCategoryTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ExecutiveCategoryType}.
 */
@RestController
@RequestMapping("/api")
public class ExecutiveCategoryTypeResource {

    private final Logger log = LoggerFactory.getLogger(ExecutiveCategoryTypeResource.class);

    private static final String ENTITY_NAME = "executiveCategoryType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExecutiveCategoryTypeService executiveCategoryTypeService;

    private final ExecutiveCategoryTypeRepository executiveCategoryTypeRepository;

    private final ExecutiveCategoryTypeQueryService executiveCategoryTypeQueryService;

    public ExecutiveCategoryTypeResource(
        ExecutiveCategoryTypeService executiveCategoryTypeService,
        ExecutiveCategoryTypeRepository executiveCategoryTypeRepository,
        ExecutiveCategoryTypeQueryService executiveCategoryTypeQueryService
    ) {
        this.executiveCategoryTypeService = executiveCategoryTypeService;
        this.executiveCategoryTypeRepository = executiveCategoryTypeRepository;
        this.executiveCategoryTypeQueryService = executiveCategoryTypeQueryService;
    }

    /**
     * {@code POST  /executive-category-types} : Create a new executiveCategoryType.
     *
     * @param executiveCategoryTypeDTO the executiveCategoryTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new executiveCategoryTypeDTO, or with status {@code 400 (Bad Request)} if the executiveCategoryType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/executive-category-types")
    public ResponseEntity<ExecutiveCategoryTypeDTO> createExecutiveCategoryType(
        @Valid @RequestBody ExecutiveCategoryTypeDTO executiveCategoryTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ExecutiveCategoryType : {}", executiveCategoryTypeDTO);
        if (executiveCategoryTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new executiveCategoryType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExecutiveCategoryTypeDTO result = executiveCategoryTypeService.save(executiveCategoryTypeDTO);
        return ResponseEntity
            .created(new URI("/api/executive-category-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /executive-category-types/:id} : Updates an existing executiveCategoryType.
     *
     * @param id the id of the executiveCategoryTypeDTO to save.
     * @param executiveCategoryTypeDTO the executiveCategoryTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated executiveCategoryTypeDTO,
     * or with status {@code 400 (Bad Request)} if the executiveCategoryTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the executiveCategoryTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/executive-category-types/{id}")
    public ResponseEntity<ExecutiveCategoryTypeDTO> updateExecutiveCategoryType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExecutiveCategoryTypeDTO executiveCategoryTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExecutiveCategoryType : {}, {}", id, executiveCategoryTypeDTO);
        if (executiveCategoryTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, executiveCategoryTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!executiveCategoryTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExecutiveCategoryTypeDTO result = executiveCategoryTypeService.save(executiveCategoryTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, executiveCategoryTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /executive-category-types/:id} : Partial updates given fields of an existing executiveCategoryType, field will ignore if it is null
     *
     * @param id the id of the executiveCategoryTypeDTO to save.
     * @param executiveCategoryTypeDTO the executiveCategoryTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated executiveCategoryTypeDTO,
     * or with status {@code 400 (Bad Request)} if the executiveCategoryTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the executiveCategoryTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the executiveCategoryTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/executive-category-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExecutiveCategoryTypeDTO> partialUpdateExecutiveCategoryType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExecutiveCategoryTypeDTO executiveCategoryTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExecutiveCategoryType partially : {}, {}", id, executiveCategoryTypeDTO);
        if (executiveCategoryTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, executiveCategoryTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!executiveCategoryTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExecutiveCategoryTypeDTO> result = executiveCategoryTypeService.partialUpdate(executiveCategoryTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, executiveCategoryTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /executive-category-types} : get all the executiveCategoryTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of executiveCategoryTypes in body.
     */
    @GetMapping("/executive-category-types")
    public ResponseEntity<List<ExecutiveCategoryTypeDTO>> getAllExecutiveCategoryTypes(
        ExecutiveCategoryTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ExecutiveCategoryTypes by criteria: {}", criteria);
        Page<ExecutiveCategoryTypeDTO> page = executiveCategoryTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /executive-category-types/count} : count all the executiveCategoryTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/executive-category-types/count")
    public ResponseEntity<Long> countExecutiveCategoryTypes(ExecutiveCategoryTypeCriteria criteria) {
        log.debug("REST request to count ExecutiveCategoryTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(executiveCategoryTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /executive-category-types/:id} : get the "id" executiveCategoryType.
     *
     * @param id the id of the executiveCategoryTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the executiveCategoryTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/executive-category-types/{id}")
    public ResponseEntity<ExecutiveCategoryTypeDTO> getExecutiveCategoryType(@PathVariable Long id) {
        log.debug("REST request to get ExecutiveCategoryType : {}", id);
        Optional<ExecutiveCategoryTypeDTO> executiveCategoryTypeDTO = executiveCategoryTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(executiveCategoryTypeDTO);
    }

    /**
     * {@code DELETE  /executive-category-types/:id} : delete the "id" executiveCategoryType.
     *
     * @param id the id of the executiveCategoryTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/executive-category-types/{id}")
    public ResponseEntity<Void> deleteExecutiveCategoryType(@PathVariable Long id) {
        log.debug("REST request to delete ExecutiveCategoryType : {}", id);
        executiveCategoryTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/executive-category-types?query=:query} : search for the executiveCategoryType corresponding
     * to the query.
     *
     * @param query the query of the executiveCategoryType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/executive-category-types")
    public ResponseEntity<List<ExecutiveCategoryTypeDTO>> searchExecutiveCategoryTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ExecutiveCategoryTypes for query {}", query);
        Page<ExecutiveCategoryTypeDTO> page = executiveCategoryTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
