package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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

import io.github.erp.repository.DerivativeUnderlyingAssetRepository;
import io.github.erp.service.DerivativeUnderlyingAssetQueryService;
import io.github.erp.service.DerivativeUnderlyingAssetService;
import io.github.erp.service.criteria.DerivativeUnderlyingAssetCriteria;
import io.github.erp.service.dto.DerivativeUnderlyingAssetDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DerivativeUnderlyingAsset}.
 */
@RestController
@RequestMapping("/api")
public class DerivativeUnderlyingAssetResource {

    private final Logger log = LoggerFactory.getLogger(DerivativeUnderlyingAssetResource.class);

    private static final String ENTITY_NAME = "derivativeUnderlyingAsset";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DerivativeUnderlyingAssetService derivativeUnderlyingAssetService;

    private final DerivativeUnderlyingAssetRepository derivativeUnderlyingAssetRepository;

    private final DerivativeUnderlyingAssetQueryService derivativeUnderlyingAssetQueryService;

    public DerivativeUnderlyingAssetResource(
        DerivativeUnderlyingAssetService derivativeUnderlyingAssetService,
        DerivativeUnderlyingAssetRepository derivativeUnderlyingAssetRepository,
        DerivativeUnderlyingAssetQueryService derivativeUnderlyingAssetQueryService
    ) {
        this.derivativeUnderlyingAssetService = derivativeUnderlyingAssetService;
        this.derivativeUnderlyingAssetRepository = derivativeUnderlyingAssetRepository;
        this.derivativeUnderlyingAssetQueryService = derivativeUnderlyingAssetQueryService;
    }

    /**
     * {@code POST  /derivative-underlying-assets} : Create a new derivativeUnderlyingAsset.
     *
     * @param derivativeUnderlyingAssetDTO the derivativeUnderlyingAssetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new derivativeUnderlyingAssetDTO, or with status {@code 400 (Bad Request)} if the derivativeUnderlyingAsset has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/derivative-underlying-assets")
    public ResponseEntity<DerivativeUnderlyingAssetDTO> createDerivativeUnderlyingAsset(
        @Valid @RequestBody DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DerivativeUnderlyingAsset : {}", derivativeUnderlyingAssetDTO);
        if (derivativeUnderlyingAssetDTO.getId() != null) {
            throw new BadRequestAlertException("A new derivativeUnderlyingAsset cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DerivativeUnderlyingAssetDTO result = derivativeUnderlyingAssetService.save(derivativeUnderlyingAssetDTO);
        return ResponseEntity
            .created(new URI("/api/derivative-underlying-assets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /derivative-underlying-assets/:id} : Updates an existing derivativeUnderlyingAsset.
     *
     * @param id the id of the derivativeUnderlyingAssetDTO to save.
     * @param derivativeUnderlyingAssetDTO the derivativeUnderlyingAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated derivativeUnderlyingAssetDTO,
     * or with status {@code 400 (Bad Request)} if the derivativeUnderlyingAssetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the derivativeUnderlyingAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/derivative-underlying-assets/{id}")
    public ResponseEntity<DerivativeUnderlyingAssetDTO> updateDerivativeUnderlyingAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DerivativeUnderlyingAsset : {}, {}", id, derivativeUnderlyingAssetDTO);
        if (derivativeUnderlyingAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, derivativeUnderlyingAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!derivativeUnderlyingAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DerivativeUnderlyingAssetDTO result = derivativeUnderlyingAssetService.save(derivativeUnderlyingAssetDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, derivativeUnderlyingAssetDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /derivative-underlying-assets/:id} : Partial updates given fields of an existing derivativeUnderlyingAsset, field will ignore if it is null
     *
     * @param id the id of the derivativeUnderlyingAssetDTO to save.
     * @param derivativeUnderlyingAssetDTO the derivativeUnderlyingAssetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated derivativeUnderlyingAssetDTO,
     * or with status {@code 400 (Bad Request)} if the derivativeUnderlyingAssetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the derivativeUnderlyingAssetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the derivativeUnderlyingAssetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/derivative-underlying-assets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DerivativeUnderlyingAssetDTO> partialUpdateDerivativeUnderlyingAsset(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DerivativeUnderlyingAssetDTO derivativeUnderlyingAssetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DerivativeUnderlyingAsset partially : {}, {}", id, derivativeUnderlyingAssetDTO);
        if (derivativeUnderlyingAssetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, derivativeUnderlyingAssetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!derivativeUnderlyingAssetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DerivativeUnderlyingAssetDTO> result = derivativeUnderlyingAssetService.partialUpdate(derivativeUnderlyingAssetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, derivativeUnderlyingAssetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /derivative-underlying-assets} : get all the derivativeUnderlyingAssets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of derivativeUnderlyingAssets in body.
     */
    @GetMapping("/derivative-underlying-assets")
    public ResponseEntity<List<DerivativeUnderlyingAssetDTO>> getAllDerivativeUnderlyingAssets(
        DerivativeUnderlyingAssetCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get DerivativeUnderlyingAssets by criteria: {}", criteria);
        Page<DerivativeUnderlyingAssetDTO> page = derivativeUnderlyingAssetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /derivative-underlying-assets/count} : count all the derivativeUnderlyingAssets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/derivative-underlying-assets/count")
    public ResponseEntity<Long> countDerivativeUnderlyingAssets(DerivativeUnderlyingAssetCriteria criteria) {
        log.debug("REST request to count DerivativeUnderlyingAssets by criteria: {}", criteria);
        return ResponseEntity.ok().body(derivativeUnderlyingAssetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /derivative-underlying-assets/:id} : get the "id" derivativeUnderlyingAsset.
     *
     * @param id the id of the derivativeUnderlyingAssetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the derivativeUnderlyingAssetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/derivative-underlying-assets/{id}")
    public ResponseEntity<DerivativeUnderlyingAssetDTO> getDerivativeUnderlyingAsset(@PathVariable Long id) {
        log.debug("REST request to get DerivativeUnderlyingAsset : {}", id);
        Optional<DerivativeUnderlyingAssetDTO> derivativeUnderlyingAssetDTO = derivativeUnderlyingAssetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(derivativeUnderlyingAssetDTO);
    }

    /**
     * {@code DELETE  /derivative-underlying-assets/:id} : delete the "id" derivativeUnderlyingAsset.
     *
     * @param id the id of the derivativeUnderlyingAssetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/derivative-underlying-assets/{id}")
    public ResponseEntity<Void> deleteDerivativeUnderlyingAsset(@PathVariable Long id) {
        log.debug("REST request to delete DerivativeUnderlyingAsset : {}", id);
        derivativeUnderlyingAssetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/derivative-underlying-assets?query=:query} : search for the derivativeUnderlyingAsset corresponding
     * to the query.
     *
     * @param query the query of the derivativeUnderlyingAsset search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/derivative-underlying-assets")
    public ResponseEntity<List<DerivativeUnderlyingAssetDTO>> searchDerivativeUnderlyingAssets(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of DerivativeUnderlyingAssets for query {}", query);
        Page<DerivativeUnderlyingAssetDTO> page = derivativeUnderlyingAssetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
