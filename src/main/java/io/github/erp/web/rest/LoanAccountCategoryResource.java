package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import io.github.erp.repository.LoanAccountCategoryRepository;
import io.github.erp.service.LoanAccountCategoryQueryService;
import io.github.erp.service.LoanAccountCategoryService;
import io.github.erp.service.criteria.LoanAccountCategoryCriteria;
import io.github.erp.service.dto.LoanAccountCategoryDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LoanAccountCategory}.
 */
@RestController
@RequestMapping("/api")
public class LoanAccountCategoryResource {

    private final Logger log = LoggerFactory.getLogger(LoanAccountCategoryResource.class);

    private static final String ENTITY_NAME = "loanAccountCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanAccountCategoryService loanAccountCategoryService;

    private final LoanAccountCategoryRepository loanAccountCategoryRepository;

    private final LoanAccountCategoryQueryService loanAccountCategoryQueryService;

    public LoanAccountCategoryResource(
        LoanAccountCategoryService loanAccountCategoryService,
        LoanAccountCategoryRepository loanAccountCategoryRepository,
        LoanAccountCategoryQueryService loanAccountCategoryQueryService
    ) {
        this.loanAccountCategoryService = loanAccountCategoryService;
        this.loanAccountCategoryRepository = loanAccountCategoryRepository;
        this.loanAccountCategoryQueryService = loanAccountCategoryQueryService;
    }

    /**
     * {@code POST  /loan-account-categories} : Create a new loanAccountCategory.
     *
     * @param loanAccountCategoryDTO the loanAccountCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanAccountCategoryDTO, or with status {@code 400 (Bad Request)} if the loanAccountCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loan-account-categories")
    public ResponseEntity<LoanAccountCategoryDTO> createLoanAccountCategory(
        @Valid @RequestBody LoanAccountCategoryDTO loanAccountCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LoanAccountCategory : {}", loanAccountCategoryDTO);
        if (loanAccountCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanAccountCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanAccountCategoryDTO result = loanAccountCategoryService.save(loanAccountCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/loan-account-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loan-account-categories/:id} : Updates an existing loanAccountCategory.
     *
     * @param id the id of the loanAccountCategoryDTO to save.
     * @param loanAccountCategoryDTO the loanAccountCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanAccountCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the loanAccountCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanAccountCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loan-account-categories/{id}")
    public ResponseEntity<LoanAccountCategoryDTO> updateLoanAccountCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoanAccountCategoryDTO loanAccountCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoanAccountCategory : {}, {}", id, loanAccountCategoryDTO);
        if (loanAccountCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanAccountCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanAccountCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoanAccountCategoryDTO result = loanAccountCategoryService.save(loanAccountCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanAccountCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loan-account-categories/:id} : Partial updates given fields of an existing loanAccountCategory, field will ignore if it is null
     *
     * @param id the id of the loanAccountCategoryDTO to save.
     * @param loanAccountCategoryDTO the loanAccountCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanAccountCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the loanAccountCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanAccountCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanAccountCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loan-account-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanAccountCategoryDTO> partialUpdateLoanAccountCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoanAccountCategoryDTO loanAccountCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanAccountCategory partially : {}, {}", id, loanAccountCategoryDTO);
        if (loanAccountCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanAccountCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanAccountCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanAccountCategoryDTO> result = loanAccountCategoryService.partialUpdate(loanAccountCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanAccountCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loan-account-categories} : get all the loanAccountCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanAccountCategories in body.
     */
    @GetMapping("/loan-account-categories")
    public ResponseEntity<List<LoanAccountCategoryDTO>> getAllLoanAccountCategories(
        LoanAccountCategoryCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LoanAccountCategories by criteria: {}", criteria);
        Page<LoanAccountCategoryDTO> page = loanAccountCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loan-account-categories/count} : count all the loanAccountCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/loan-account-categories/count")
    public ResponseEntity<Long> countLoanAccountCategories(LoanAccountCategoryCriteria criteria) {
        log.debug("REST request to count LoanAccountCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(loanAccountCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /loan-account-categories/:id} : get the "id" loanAccountCategory.
     *
     * @param id the id of the loanAccountCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanAccountCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loan-account-categories/{id}")
    public ResponseEntity<LoanAccountCategoryDTO> getLoanAccountCategory(@PathVariable Long id) {
        log.debug("REST request to get LoanAccountCategory : {}", id);
        Optional<LoanAccountCategoryDTO> loanAccountCategoryDTO = loanAccountCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanAccountCategoryDTO);
    }

    /**
     * {@code DELETE  /loan-account-categories/:id} : delete the "id" loanAccountCategory.
     *
     * @param id the id of the loanAccountCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loan-account-categories/{id}")
    public ResponseEntity<Void> deleteLoanAccountCategory(@PathVariable Long id) {
        log.debug("REST request to delete LoanAccountCategory : {}", id);
        loanAccountCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loan-account-categories?query=:query} : search for the loanAccountCategory corresponding
     * to the query.
     *
     * @param query the query of the loanAccountCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loan-account-categories")
    public ResponseEntity<List<LoanAccountCategoryDTO>> searchLoanAccountCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoanAccountCategories for query {}", query);
        Page<LoanAccountCategoryDTO> page = loanAccountCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
