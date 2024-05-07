package io.github.erp.erp.resources.assets;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.repository.AssetRevaluationRepository;
import io.github.erp.service.AssetRevaluationQueryService;
import io.github.erp.service.AssetRevaluationService;
import io.github.erp.service.criteria.AssetRevaluationCriteria;
import io.github.erp.service.dto.AssetRevaluationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetRevaluation}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
public class AssetRevaluationResourceProd {

    private final Logger log = LoggerFactory.getLogger(AssetRevaluationResourceProd.class);

    private static final String ENTITY_NAME = "assetRevaluation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetRevaluationService assetRevaluationService;

    private final AssetRevaluationRepository assetRevaluationRepository;

    private final AssetRevaluationQueryService assetRevaluationQueryService;

    public AssetRevaluationResourceProd(
        AssetRevaluationService assetRevaluationService,
        AssetRevaluationRepository assetRevaluationRepository,
        AssetRevaluationQueryService assetRevaluationQueryService
    ) {
        this.assetRevaluationService = assetRevaluationService;
        this.assetRevaluationRepository = assetRevaluationRepository;
        this.assetRevaluationQueryService = assetRevaluationQueryService;
    }

    /**
     * {@code POST  /asset-revaluations} : Create a new assetRevaluation.
     *
     * @param assetRevaluationDTO the assetRevaluationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetRevaluationDTO, or with status {@code 400 (Bad Request)} if the assetRevaluation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-revaluations")
    public ResponseEntity<AssetRevaluationDTO> createAssetRevaluation(@Valid @RequestBody AssetRevaluationDTO assetRevaluationDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetRevaluation : {}", assetRevaluationDTO);
        if (assetRevaluationDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetRevaluation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetRevaluationDTO result = assetRevaluationService.save(assetRevaluationDTO);
        return ResponseEntity
            .created(new URI("/api/asset-revaluations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-revaluations/:id} : Updates an existing assetRevaluation.
     *
     * @param id the id of the assetRevaluationDTO to save.
     * @param assetRevaluationDTO the assetRevaluationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetRevaluationDTO,
     * or with status {@code 400 (Bad Request)} if the assetRevaluationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetRevaluationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-revaluations/{id}")
    public ResponseEntity<AssetRevaluationDTO> updateAssetRevaluation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetRevaluationDTO assetRevaluationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetRevaluation : {}, {}", id, assetRevaluationDTO);
        if (assetRevaluationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetRevaluationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetRevaluationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetRevaluationDTO result = assetRevaluationService.save(assetRevaluationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetRevaluationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-revaluations/:id} : Partial updates given fields of an existing assetRevaluation, field will ignore if it is null
     *
     * @param id the id of the assetRevaluationDTO to save.
     * @param assetRevaluationDTO the assetRevaluationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetRevaluationDTO,
     * or with status {@code 400 (Bad Request)} if the assetRevaluationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetRevaluationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetRevaluationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-revaluations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetRevaluationDTO> partialUpdateAssetRevaluation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetRevaluationDTO assetRevaluationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetRevaluation partially : {}, {}", id, assetRevaluationDTO);
        if (assetRevaluationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetRevaluationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetRevaluationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetRevaluationDTO> result = assetRevaluationService.partialUpdate(assetRevaluationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetRevaluationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-revaluations} : get all the assetRevaluations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetRevaluations in body.
     */
    @GetMapping("/asset-revaluations")
    public ResponseEntity<List<AssetRevaluationDTO>> getAllAssetRevaluations(AssetRevaluationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssetRevaluations by criteria: {}", criteria);
        Page<AssetRevaluationDTO> page = assetRevaluationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-revaluations/count} : count all the assetRevaluations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-revaluations/count")
    public ResponseEntity<Long> countAssetRevaluations(AssetRevaluationCriteria criteria) {
        log.debug("REST request to count AssetRevaluations by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetRevaluationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-revaluations/:id} : get the "id" assetRevaluation.
     *
     * @param id the id of the assetRevaluationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetRevaluationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-revaluations/{id}")
    public ResponseEntity<AssetRevaluationDTO> getAssetRevaluation(@PathVariable Long id) {
        log.debug("REST request to get AssetRevaluation : {}", id);
        Optional<AssetRevaluationDTO> assetRevaluationDTO = assetRevaluationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetRevaluationDTO);
    }

    /**
     * {@code DELETE  /asset-revaluations/:id} : delete the "id" assetRevaluation.
     *
     * @param id the id of the assetRevaluationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-revaluations/{id}")
    public ResponseEntity<Void> deleteAssetRevaluation(@PathVariable Long id) {
        log.debug("REST request to delete AssetRevaluation : {}", id);
        assetRevaluationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/asset-revaluations?query=:query} : search for the assetRevaluation corresponding
     * to the query.
     *
     * @param query the query of the assetRevaluation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-revaluations")
    public ResponseEntity<List<AssetRevaluationDTO>> searchAssetRevaluations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AssetRevaluations for query {}", query);
        Page<AssetRevaluationDTO> page = assetRevaluationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
