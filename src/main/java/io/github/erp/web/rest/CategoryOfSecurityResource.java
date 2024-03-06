package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

import io.github.erp.repository.CategoryOfSecurityRepository;
import io.github.erp.service.CategoryOfSecurityQueryService;
import io.github.erp.service.CategoryOfSecurityService;
import io.github.erp.service.criteria.CategoryOfSecurityCriteria;
import io.github.erp.service.dto.CategoryOfSecurityDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CategoryOfSecurity}.
 */
@RestController
@RequestMapping("/api")
public class CategoryOfSecurityResource {

    private final Logger log = LoggerFactory.getLogger(CategoryOfSecurityResource.class);

    private static final String ENTITY_NAME = "categoryOfSecurity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryOfSecurityService categoryOfSecurityService;

    private final CategoryOfSecurityRepository categoryOfSecurityRepository;

    private final CategoryOfSecurityQueryService categoryOfSecurityQueryService;

    public CategoryOfSecurityResource(
        CategoryOfSecurityService categoryOfSecurityService,
        CategoryOfSecurityRepository categoryOfSecurityRepository,
        CategoryOfSecurityQueryService categoryOfSecurityQueryService
    ) {
        this.categoryOfSecurityService = categoryOfSecurityService;
        this.categoryOfSecurityRepository = categoryOfSecurityRepository;
        this.categoryOfSecurityQueryService = categoryOfSecurityQueryService;
    }

    /**
     * {@code POST  /category-of-securities} : Create a new categoryOfSecurity.
     *
     * @param categoryOfSecurityDTO the categoryOfSecurityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryOfSecurityDTO, or with status {@code 400 (Bad Request)} if the categoryOfSecurity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/category-of-securities")
    public ResponseEntity<CategoryOfSecurityDTO> createCategoryOfSecurity(@Valid @RequestBody CategoryOfSecurityDTO categoryOfSecurityDTO)
        throws URISyntaxException {
        log.debug("REST request to save CategoryOfSecurity : {}", categoryOfSecurityDTO);
        if (categoryOfSecurityDTO.getId() != null) {
            throw new BadRequestAlertException("A new categoryOfSecurity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoryOfSecurityDTO result = categoryOfSecurityService.save(categoryOfSecurityDTO);
        return ResponseEntity
            .created(new URI("/api/category-of-securities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /category-of-securities/:id} : Updates an existing categoryOfSecurity.
     *
     * @param id the id of the categoryOfSecurityDTO to save.
     * @param categoryOfSecurityDTO the categoryOfSecurityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryOfSecurityDTO,
     * or with status {@code 400 (Bad Request)} if the categoryOfSecurityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryOfSecurityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/category-of-securities/{id}")
    public ResponseEntity<CategoryOfSecurityDTO> updateCategoryOfSecurity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryOfSecurityDTO categoryOfSecurityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CategoryOfSecurity : {}, {}", id, categoryOfSecurityDTO);
        if (categoryOfSecurityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryOfSecurityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryOfSecurityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategoryOfSecurityDTO result = categoryOfSecurityService.save(categoryOfSecurityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryOfSecurityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /category-of-securities/:id} : Partial updates given fields of an existing categoryOfSecurity, field will ignore if it is null
     *
     * @param id the id of the categoryOfSecurityDTO to save.
     * @param categoryOfSecurityDTO the categoryOfSecurityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryOfSecurityDTO,
     * or with status {@code 400 (Bad Request)} if the categoryOfSecurityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the categoryOfSecurityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryOfSecurityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/category-of-securities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CategoryOfSecurityDTO> partialUpdateCategoryOfSecurity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryOfSecurityDTO categoryOfSecurityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategoryOfSecurity partially : {}, {}", id, categoryOfSecurityDTO);
        if (categoryOfSecurityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryOfSecurityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryOfSecurityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryOfSecurityDTO> result = categoryOfSecurityService.partialUpdate(categoryOfSecurityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, categoryOfSecurityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /category-of-securities} : get all the categoryOfSecurities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryOfSecurities in body.
     */
    @GetMapping("/category-of-securities")
    public ResponseEntity<List<CategoryOfSecurityDTO>> getAllCategoryOfSecurities(CategoryOfSecurityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CategoryOfSecurities by criteria: {}", criteria);
        Page<CategoryOfSecurityDTO> page = categoryOfSecurityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /category-of-securities/count} : count all the categoryOfSecurities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/category-of-securities/count")
    public ResponseEntity<Long> countCategoryOfSecurities(CategoryOfSecurityCriteria criteria) {
        log.debug("REST request to count CategoryOfSecurities by criteria: {}", criteria);
        return ResponseEntity.ok().body(categoryOfSecurityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /category-of-securities/:id} : get the "id" categoryOfSecurity.
     *
     * @param id the id of the categoryOfSecurityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryOfSecurityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/category-of-securities/{id}")
    public ResponseEntity<CategoryOfSecurityDTO> getCategoryOfSecurity(@PathVariable Long id) {
        log.debug("REST request to get CategoryOfSecurity : {}", id);
        Optional<CategoryOfSecurityDTO> categoryOfSecurityDTO = categoryOfSecurityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(categoryOfSecurityDTO);
    }

    /**
     * {@code DELETE  /category-of-securities/:id} : delete the "id" categoryOfSecurity.
     *
     * @param id the id of the categoryOfSecurityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/category-of-securities/{id}")
    public ResponseEntity<Void> deleteCategoryOfSecurity(@PathVariable Long id) {
        log.debug("REST request to delete CategoryOfSecurity : {}", id);
        categoryOfSecurityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/category-of-securities?query=:query} : search for the categoryOfSecurity corresponding
     * to the query.
     *
     * @param query the query of the categoryOfSecurity search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/category-of-securities")
    public ResponseEntity<List<CategoryOfSecurityDTO>> searchCategoryOfSecurities(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CategoryOfSecurities for query {}", query);
        Page<CategoryOfSecurityDTO> page = categoryOfSecurityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
