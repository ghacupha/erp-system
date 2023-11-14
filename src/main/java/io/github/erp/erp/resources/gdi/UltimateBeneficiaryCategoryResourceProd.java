package io.github.erp.erp.resources.gdi;

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
import io.github.erp.repository.UltimateBeneficiaryCategoryRepository;
import io.github.erp.service.UltimateBeneficiaryCategoryQueryService;
import io.github.erp.service.UltimateBeneficiaryCategoryService;
import io.github.erp.service.criteria.UltimateBeneficiaryCategoryCriteria;
import io.github.erp.service.dto.UltimateBeneficiaryCategoryDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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
 * REST controller for managing {@link io.github.erp.domain.UltimateBeneficiaryCategory}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class UltimateBeneficiaryCategoryResourceProd {

    private final Logger log = LoggerFactory.getLogger(UltimateBeneficiaryCategoryResourceProd.class);

    private static final String ENTITY_NAME = "ultimateBeneficiaryCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UltimateBeneficiaryCategoryService ultimateBeneficiaryCategoryService;

    private final UltimateBeneficiaryCategoryRepository ultimateBeneficiaryCategoryRepository;

    private final UltimateBeneficiaryCategoryQueryService ultimateBeneficiaryCategoryQueryService;

    public UltimateBeneficiaryCategoryResourceProd(
        UltimateBeneficiaryCategoryService ultimateBeneficiaryCategoryService,
        UltimateBeneficiaryCategoryRepository ultimateBeneficiaryCategoryRepository,
        UltimateBeneficiaryCategoryQueryService ultimateBeneficiaryCategoryQueryService
    ) {
        this.ultimateBeneficiaryCategoryService = ultimateBeneficiaryCategoryService;
        this.ultimateBeneficiaryCategoryRepository = ultimateBeneficiaryCategoryRepository;
        this.ultimateBeneficiaryCategoryQueryService = ultimateBeneficiaryCategoryQueryService;
    }

    /**
     * {@code POST  /ultimate-beneficiary-categories} : Create a new ultimateBeneficiaryCategory.
     *
     * @param ultimateBeneficiaryCategoryDTO the ultimateBeneficiaryCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ultimateBeneficiaryCategoryDTO, or with status {@code 400 (Bad Request)} if the ultimateBeneficiaryCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ultimate-beneficiary-categories")
    public ResponseEntity<UltimateBeneficiaryCategoryDTO> createUltimateBeneficiaryCategory(
        @Valid @RequestBody UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UltimateBeneficiaryCategory : {}", ultimateBeneficiaryCategoryDTO);
        if (ultimateBeneficiaryCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new ultimateBeneficiaryCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UltimateBeneficiaryCategoryDTO result = ultimateBeneficiaryCategoryService.save(ultimateBeneficiaryCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/ultimate-beneficiary-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ultimate-beneficiary-categories/:id} : Updates an existing ultimateBeneficiaryCategory.
     *
     * @param id the id of the ultimateBeneficiaryCategoryDTO to save.
     * @param ultimateBeneficiaryCategoryDTO the ultimateBeneficiaryCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ultimateBeneficiaryCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the ultimateBeneficiaryCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ultimateBeneficiaryCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ultimate-beneficiary-categories/{id}")
    public ResponseEntity<UltimateBeneficiaryCategoryDTO> updateUltimateBeneficiaryCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UltimateBeneficiaryCategory : {}, {}", id, ultimateBeneficiaryCategoryDTO);
        if (ultimateBeneficiaryCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ultimateBeneficiaryCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ultimateBeneficiaryCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UltimateBeneficiaryCategoryDTO result = ultimateBeneficiaryCategoryService.save(ultimateBeneficiaryCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ultimateBeneficiaryCategoryDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /ultimate-beneficiary-categories/:id} : Partial updates given fields of an existing ultimateBeneficiaryCategory, field will ignore if it is null
     *
     * @param id the id of the ultimateBeneficiaryCategoryDTO to save.
     * @param ultimateBeneficiaryCategoryDTO the ultimateBeneficiaryCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ultimateBeneficiaryCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the ultimateBeneficiaryCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ultimateBeneficiaryCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ultimateBeneficiaryCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ultimate-beneficiary-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UltimateBeneficiaryCategoryDTO> partialUpdateUltimateBeneficiaryCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UltimateBeneficiaryCategoryDTO ultimateBeneficiaryCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UltimateBeneficiaryCategory partially : {}, {}", id, ultimateBeneficiaryCategoryDTO);
        if (ultimateBeneficiaryCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ultimateBeneficiaryCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ultimateBeneficiaryCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UltimateBeneficiaryCategoryDTO> result = ultimateBeneficiaryCategoryService.partialUpdate(ultimateBeneficiaryCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ultimateBeneficiaryCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ultimate-beneficiary-categories} : get all the ultimateBeneficiaryCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ultimateBeneficiaryCategories in body.
     */
    @GetMapping("/ultimate-beneficiary-categories")
    public ResponseEntity<List<UltimateBeneficiaryCategoryDTO>> getAllUltimateBeneficiaryCategories(
        UltimateBeneficiaryCategoryCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get UltimateBeneficiaryCategories by criteria: {}", criteria);
        Page<UltimateBeneficiaryCategoryDTO> page = ultimateBeneficiaryCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ultimate-beneficiary-categories/count} : count all the ultimateBeneficiaryCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ultimate-beneficiary-categories/count")
    public ResponseEntity<Long> countUltimateBeneficiaryCategories(UltimateBeneficiaryCategoryCriteria criteria) {
        log.debug("REST request to count UltimateBeneficiaryCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(ultimateBeneficiaryCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ultimate-beneficiary-categories/:id} : get the "id" ultimateBeneficiaryCategory.
     *
     * @param id the id of the ultimateBeneficiaryCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ultimateBeneficiaryCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ultimate-beneficiary-categories/{id}")
    public ResponseEntity<UltimateBeneficiaryCategoryDTO> getUltimateBeneficiaryCategory(@PathVariable Long id) {
        log.debug("REST request to get UltimateBeneficiaryCategory : {}", id);
        Optional<UltimateBeneficiaryCategoryDTO> ultimateBeneficiaryCategoryDTO = ultimateBeneficiaryCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ultimateBeneficiaryCategoryDTO);
    }

    /**
     * {@code DELETE  /ultimate-beneficiary-categories/:id} : delete the "id" ultimateBeneficiaryCategory.
     *
     * @param id the id of the ultimateBeneficiaryCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ultimate-beneficiary-categories/{id}")
    public ResponseEntity<Void> deleteUltimateBeneficiaryCategory(@PathVariable Long id) {
        log.debug("REST request to delete UltimateBeneficiaryCategory : {}", id);
        ultimateBeneficiaryCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ultimate-beneficiary-categories?query=:query} : search for the ultimateBeneficiaryCategory corresponding
     * to the query.
     *
     * @param query the query of the ultimateBeneficiaryCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ultimate-beneficiary-categories")
    public ResponseEntity<List<UltimateBeneficiaryCategoryDTO>> searchUltimateBeneficiaryCategories(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of UltimateBeneficiaryCategories for query {}", query);
        Page<UltimateBeneficiaryCategoryDTO> page = ultimateBeneficiaryCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
