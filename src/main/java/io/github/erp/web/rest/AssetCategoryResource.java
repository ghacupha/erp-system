package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.AssetCategoryRepository;
import io.github.erp.service.AssetCategoryQueryService;
import io.github.erp.service.AssetCategoryService;
import io.github.erp.service.criteria.AssetCategoryCriteria;
import io.github.erp.service.dto.AssetCategoryDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetCategory}.
 */
@RestController
@RequestMapping("/api")
public class AssetCategoryResource {

    private final Logger log = LoggerFactory.getLogger(AssetCategoryResource.class);

    private static final String ENTITY_NAME = "assetCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetCategoryService assetCategoryService;

    private final AssetCategoryRepository assetCategoryRepository;

    private final AssetCategoryQueryService assetCategoryQueryService;

    public AssetCategoryResource(
        AssetCategoryService assetCategoryService,
        AssetCategoryRepository assetCategoryRepository,
        AssetCategoryQueryService assetCategoryQueryService
    ) {
        this.assetCategoryService = assetCategoryService;
        this.assetCategoryRepository = assetCategoryRepository;
        this.assetCategoryQueryService = assetCategoryQueryService;
    }

    /**
     * {@code POST  /asset-categories} : Create a new assetCategory.
     *
     * @param assetCategoryDTO the assetCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetCategoryDTO, or with status {@code 400 (Bad Request)} if the assetCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-categories")
    public ResponseEntity<AssetCategoryDTO> createAssetCategory(@Valid @RequestBody AssetCategoryDTO assetCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetCategory : {}", assetCategoryDTO);
        if (assetCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetCategoryDTO result = assetCategoryService.save(assetCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/asset-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-categories/:id} : Updates an existing assetCategory.
     *
     * @param id the id of the assetCategoryDTO to save.
     * @param assetCategoryDTO the assetCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the assetCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-categories/{id}")
    public ResponseEntity<AssetCategoryDTO> updateAssetCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetCategoryDTO assetCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetCategory : {}, {}", id, assetCategoryDTO);
        if (assetCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetCategoryDTO result = assetCategoryService.save(assetCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-categories/:id} : Partial updates given fields of an existing assetCategory, field will ignore if it is null
     *
     * @param id the id of the assetCategoryDTO to save.
     * @param assetCategoryDTO the assetCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the assetCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetCategoryDTO> partialUpdateAssetCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetCategoryDTO assetCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetCategory partially : {}, {}", id, assetCategoryDTO);
        if (assetCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetCategoryDTO> result = assetCategoryService.partialUpdate(assetCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-categories} : get all the assetCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetCategories in body.
     */
    @GetMapping("/asset-categories")
    public ResponseEntity<List<AssetCategoryDTO>> getAllAssetCategories(AssetCategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssetCategories by criteria: {}", criteria);
        Page<AssetCategoryDTO> page = assetCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-categories/count} : count all the assetCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-categories/count")
    public ResponseEntity<Long> countAssetCategories(AssetCategoryCriteria criteria) {
        log.debug("REST request to count AssetCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-categories/:id} : get the "id" assetCategory.
     *
     * @param id the id of the assetCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-categories/{id}")
    public ResponseEntity<AssetCategoryDTO> getAssetCategory(@PathVariable Long id) {
        log.debug("REST request to get AssetCategory : {}", id);
        Optional<AssetCategoryDTO> assetCategoryDTO = assetCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetCategoryDTO);
    }

    /**
     * {@code DELETE  /asset-categories/:id} : delete the "id" assetCategory.
     *
     * @param id the id of the assetCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-categories/{id}")
    public ResponseEntity<Void> deleteAssetCategory(@PathVariable Long id) {
        log.debug("REST request to delete AssetCategory : {}", id);
        assetCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/asset-categories?query=:query} : search for the assetCategory corresponding
     * to the query.
     *
     * @param query the query of the assetCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-categories")
    public ResponseEntity<List<AssetCategoryDTO>> searchAssetCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AssetCategories for query {}", query);
        Page<AssetCategoryDTO> page = assetCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
