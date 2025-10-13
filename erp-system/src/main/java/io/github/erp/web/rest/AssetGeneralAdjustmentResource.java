package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.repository.AssetGeneralAdjustmentRepository;
import io.github.erp.service.AssetGeneralAdjustmentQueryService;
import io.github.erp.service.AssetGeneralAdjustmentService;
import io.github.erp.service.criteria.AssetGeneralAdjustmentCriteria;
import io.github.erp.service.dto.AssetGeneralAdjustmentDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetGeneralAdjustment}.
 */
@RestController
@RequestMapping("/api")
public class AssetGeneralAdjustmentResource {

    private final Logger log = LoggerFactory.getLogger(AssetGeneralAdjustmentResource.class);

    private static final String ENTITY_NAME = "assetGeneralAdjustment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetGeneralAdjustmentService assetGeneralAdjustmentService;

    private final AssetGeneralAdjustmentRepository assetGeneralAdjustmentRepository;

    private final AssetGeneralAdjustmentQueryService assetGeneralAdjustmentQueryService;

    public AssetGeneralAdjustmentResource(
        AssetGeneralAdjustmentService assetGeneralAdjustmentService,
        AssetGeneralAdjustmentRepository assetGeneralAdjustmentRepository,
        AssetGeneralAdjustmentQueryService assetGeneralAdjustmentQueryService
    ) {
        this.assetGeneralAdjustmentService = assetGeneralAdjustmentService;
        this.assetGeneralAdjustmentRepository = assetGeneralAdjustmentRepository;
        this.assetGeneralAdjustmentQueryService = assetGeneralAdjustmentQueryService;
    }

    /**
     * {@code POST  /asset-general-adjustments} : Create a new assetGeneralAdjustment.
     *
     * @param assetGeneralAdjustmentDTO the assetGeneralAdjustmentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetGeneralAdjustmentDTO, or with status {@code 400 (Bad Request)} if the assetGeneralAdjustment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-general-adjustments")
    public ResponseEntity<AssetGeneralAdjustmentDTO> createAssetGeneralAdjustment(
        @Valid @RequestBody AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AssetGeneralAdjustment : {}", assetGeneralAdjustmentDTO);
        if (assetGeneralAdjustmentDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetGeneralAdjustment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetGeneralAdjustmentDTO result = assetGeneralAdjustmentService.save(assetGeneralAdjustmentDTO);
        return ResponseEntity
            .created(new URI("/api/asset-general-adjustments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-general-adjustments/:id} : Updates an existing assetGeneralAdjustment.
     *
     * @param id the id of the assetGeneralAdjustmentDTO to save.
     * @param assetGeneralAdjustmentDTO the assetGeneralAdjustmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetGeneralAdjustmentDTO,
     * or with status {@code 400 (Bad Request)} if the assetGeneralAdjustmentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetGeneralAdjustmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-general-adjustments/{id}")
    public ResponseEntity<AssetGeneralAdjustmentDTO> updateAssetGeneralAdjustment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetGeneralAdjustment : {}, {}", id, assetGeneralAdjustmentDTO);
        if (assetGeneralAdjustmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetGeneralAdjustmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetGeneralAdjustmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetGeneralAdjustmentDTO result = assetGeneralAdjustmentService.save(assetGeneralAdjustmentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetGeneralAdjustmentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-general-adjustments/:id} : Partial updates given fields of an existing assetGeneralAdjustment, field will ignore if it is null
     *
     * @param id the id of the assetGeneralAdjustmentDTO to save.
     * @param assetGeneralAdjustmentDTO the assetGeneralAdjustmentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetGeneralAdjustmentDTO,
     * or with status {@code 400 (Bad Request)} if the assetGeneralAdjustmentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetGeneralAdjustmentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetGeneralAdjustmentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-general-adjustments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetGeneralAdjustmentDTO> partialUpdateAssetGeneralAdjustment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetGeneralAdjustmentDTO assetGeneralAdjustmentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetGeneralAdjustment partially : {}, {}", id, assetGeneralAdjustmentDTO);
        if (assetGeneralAdjustmentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetGeneralAdjustmentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetGeneralAdjustmentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetGeneralAdjustmentDTO> result = assetGeneralAdjustmentService.partialUpdate(assetGeneralAdjustmentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetGeneralAdjustmentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-general-adjustments} : get all the assetGeneralAdjustments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetGeneralAdjustments in body.
     */
    @GetMapping("/asset-general-adjustments")
    public ResponseEntity<List<AssetGeneralAdjustmentDTO>> getAllAssetGeneralAdjustments(
        AssetGeneralAdjustmentCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AssetGeneralAdjustments by criteria: {}", criteria);
        Page<AssetGeneralAdjustmentDTO> page = assetGeneralAdjustmentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-general-adjustments/count} : count all the assetGeneralAdjustments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-general-adjustments/count")
    public ResponseEntity<Long> countAssetGeneralAdjustments(AssetGeneralAdjustmentCriteria criteria) {
        log.debug("REST request to count AssetGeneralAdjustments by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetGeneralAdjustmentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-general-adjustments/:id} : get the "id" assetGeneralAdjustment.
     *
     * @param id the id of the assetGeneralAdjustmentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetGeneralAdjustmentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-general-adjustments/{id}")
    public ResponseEntity<AssetGeneralAdjustmentDTO> getAssetGeneralAdjustment(@PathVariable Long id) {
        log.debug("REST request to get AssetGeneralAdjustment : {}", id);
        Optional<AssetGeneralAdjustmentDTO> assetGeneralAdjustmentDTO = assetGeneralAdjustmentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetGeneralAdjustmentDTO);
    }

    /**
     * {@code DELETE  /asset-general-adjustments/:id} : delete the "id" assetGeneralAdjustment.
     *
     * @param id the id of the assetGeneralAdjustmentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-general-adjustments/{id}")
    public ResponseEntity<Void> deleteAssetGeneralAdjustment(@PathVariable Long id) {
        log.debug("REST request to delete AssetGeneralAdjustment : {}", id);
        assetGeneralAdjustmentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/asset-general-adjustments?query=:query} : search for the assetGeneralAdjustment corresponding
     * to the query.
     *
     * @param query the query of the assetGeneralAdjustment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-general-adjustments")
    public ResponseEntity<List<AssetGeneralAdjustmentDTO>> searchAssetGeneralAdjustments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AssetGeneralAdjustments for query {}", query);
        Page<AssetGeneralAdjustmentDTO> page = assetGeneralAdjustmentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
