package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.repository.CrbSubmittingInstitutionCategoryRepository;
import io.github.erp.service.CrbSubmittingInstitutionCategoryQueryService;
import io.github.erp.service.CrbSubmittingInstitutionCategoryService;
import io.github.erp.service.criteria.CrbSubmittingInstitutionCategoryCriteria;
import io.github.erp.service.dto.CrbSubmittingInstitutionCategoryDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbSubmittingInstitutionCategory}.
 */
@RestController
@RequestMapping("/api")
public class CrbSubmittingInstitutionCategoryResource {

    private final Logger log = LoggerFactory.getLogger(CrbSubmittingInstitutionCategoryResource.class);

    private static final String ENTITY_NAME = "crbSubmittingInstitutionCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbSubmittingInstitutionCategoryService crbSubmittingInstitutionCategoryService;

    private final CrbSubmittingInstitutionCategoryRepository crbSubmittingInstitutionCategoryRepository;

    private final CrbSubmittingInstitutionCategoryQueryService crbSubmittingInstitutionCategoryQueryService;

    public CrbSubmittingInstitutionCategoryResource(
        CrbSubmittingInstitutionCategoryService crbSubmittingInstitutionCategoryService,
        CrbSubmittingInstitutionCategoryRepository crbSubmittingInstitutionCategoryRepository,
        CrbSubmittingInstitutionCategoryQueryService crbSubmittingInstitutionCategoryQueryService
    ) {
        this.crbSubmittingInstitutionCategoryService = crbSubmittingInstitutionCategoryService;
        this.crbSubmittingInstitutionCategoryRepository = crbSubmittingInstitutionCategoryRepository;
        this.crbSubmittingInstitutionCategoryQueryService = crbSubmittingInstitutionCategoryQueryService;
    }

    /**
     * {@code POST  /crb-submitting-institution-categories} : Create a new crbSubmittingInstitutionCategory.
     *
     * @param crbSubmittingInstitutionCategoryDTO the crbSubmittingInstitutionCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbSubmittingInstitutionCategoryDTO, or with status {@code 400 (Bad Request)} if the crbSubmittingInstitutionCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-submitting-institution-categories")
    public ResponseEntity<CrbSubmittingInstitutionCategoryDTO> createCrbSubmittingInstitutionCategory(
        @Valid @RequestBody CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbSubmittingInstitutionCategory : {}", crbSubmittingInstitutionCategoryDTO);
        if (crbSubmittingInstitutionCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbSubmittingInstitutionCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbSubmittingInstitutionCategoryDTO result = crbSubmittingInstitutionCategoryService.save(crbSubmittingInstitutionCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/crb-submitting-institution-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-submitting-institution-categories/:id} : Updates an existing crbSubmittingInstitutionCategory.
     *
     * @param id the id of the crbSubmittingInstitutionCategoryDTO to save.
     * @param crbSubmittingInstitutionCategoryDTO the crbSubmittingInstitutionCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbSubmittingInstitutionCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the crbSubmittingInstitutionCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbSubmittingInstitutionCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-submitting-institution-categories/{id}")
    public ResponseEntity<CrbSubmittingInstitutionCategoryDTO> updateCrbSubmittingInstitutionCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbSubmittingInstitutionCategory : {}, {}", id, crbSubmittingInstitutionCategoryDTO);
        if (crbSubmittingInstitutionCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbSubmittingInstitutionCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbSubmittingInstitutionCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbSubmittingInstitutionCategoryDTO result = crbSubmittingInstitutionCategoryService.save(crbSubmittingInstitutionCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    crbSubmittingInstitutionCategoryDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /crb-submitting-institution-categories/:id} : Partial updates given fields of an existing crbSubmittingInstitutionCategory, field will ignore if it is null
     *
     * @param id the id of the crbSubmittingInstitutionCategoryDTO to save.
     * @param crbSubmittingInstitutionCategoryDTO the crbSubmittingInstitutionCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbSubmittingInstitutionCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the crbSubmittingInstitutionCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbSubmittingInstitutionCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbSubmittingInstitutionCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-submitting-institution-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbSubmittingInstitutionCategoryDTO> partialUpdateCrbSubmittingInstitutionCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbSubmittingInstitutionCategoryDTO crbSubmittingInstitutionCategoryDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update CrbSubmittingInstitutionCategory partially : {}, {}",
            id,
            crbSubmittingInstitutionCategoryDTO
        );
        if (crbSubmittingInstitutionCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbSubmittingInstitutionCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbSubmittingInstitutionCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbSubmittingInstitutionCategoryDTO> result = crbSubmittingInstitutionCategoryService.partialUpdate(
            crbSubmittingInstitutionCategoryDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbSubmittingInstitutionCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-submitting-institution-categories} : get all the crbSubmittingInstitutionCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbSubmittingInstitutionCategories in body.
     */
    @GetMapping("/crb-submitting-institution-categories")
    public ResponseEntity<List<CrbSubmittingInstitutionCategoryDTO>> getAllCrbSubmittingInstitutionCategories(
        CrbSubmittingInstitutionCategoryCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbSubmittingInstitutionCategories by criteria: {}", criteria);
        Page<CrbSubmittingInstitutionCategoryDTO> page = crbSubmittingInstitutionCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-submitting-institution-categories/count} : count all the crbSubmittingInstitutionCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-submitting-institution-categories/count")
    public ResponseEntity<Long> countCrbSubmittingInstitutionCategories(CrbSubmittingInstitutionCategoryCriteria criteria) {
        log.debug("REST request to count CrbSubmittingInstitutionCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbSubmittingInstitutionCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-submitting-institution-categories/:id} : get the "id" crbSubmittingInstitutionCategory.
     *
     * @param id the id of the crbSubmittingInstitutionCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbSubmittingInstitutionCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-submitting-institution-categories/{id}")
    public ResponseEntity<CrbSubmittingInstitutionCategoryDTO> getCrbSubmittingInstitutionCategory(@PathVariable Long id) {
        log.debug("REST request to get CrbSubmittingInstitutionCategory : {}", id);
        Optional<CrbSubmittingInstitutionCategoryDTO> crbSubmittingInstitutionCategoryDTO = crbSubmittingInstitutionCategoryService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(crbSubmittingInstitutionCategoryDTO);
    }

    /**
     * {@code DELETE  /crb-submitting-institution-categories/:id} : delete the "id" crbSubmittingInstitutionCategory.
     *
     * @param id the id of the crbSubmittingInstitutionCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-submitting-institution-categories/{id}")
    public ResponseEntity<Void> deleteCrbSubmittingInstitutionCategory(@PathVariable Long id) {
        log.debug("REST request to delete CrbSubmittingInstitutionCategory : {}", id);
        crbSubmittingInstitutionCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-submitting-institution-categories?query=:query} : search for the crbSubmittingInstitutionCategory corresponding
     * to the query.
     *
     * @param query the query of the crbSubmittingInstitutionCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-submitting-institution-categories")
    public ResponseEntity<List<CrbSubmittingInstitutionCategoryDTO>> searchCrbSubmittingInstitutionCategories(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CrbSubmittingInstitutionCategories for query {}", query);
        Page<CrbSubmittingInstitutionCategoryDTO> page = crbSubmittingInstitutionCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
