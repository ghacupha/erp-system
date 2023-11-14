package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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
import io.github.erp.repository.AssetWarrantyRepository;
import io.github.erp.service.AssetWarrantyQueryService;
import io.github.erp.service.AssetWarrantyService;
import io.github.erp.service.criteria.AssetWarrantyCriteria;
import io.github.erp.service.dto.AssetWarrantyDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetWarranty}.
 */
@RestController("assetWarrantyResourceProd")
@RequestMapping("/api/fixed-asset")
public class AssetWarrantyResourceProd {

    private final Logger log = LoggerFactory.getLogger(AssetWarrantyResourceProd.class);

    private static final String ENTITY_NAME = "assetWarranty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetWarrantyService assetWarrantyService;

    private final AssetWarrantyRepository assetWarrantyRepository;

    private final AssetWarrantyQueryService assetWarrantyQueryService;

    public AssetWarrantyResourceProd(
        AssetWarrantyService assetWarrantyService,
        AssetWarrantyRepository assetWarrantyRepository,
        AssetWarrantyQueryService assetWarrantyQueryService
    ) {
        this.assetWarrantyService = assetWarrantyService;
        this.assetWarrantyRepository = assetWarrantyRepository;
        this.assetWarrantyQueryService = assetWarrantyQueryService;
    }

    /**
     * {@code POST  /asset-warranties} : Create a new assetWarranty.
     *
     * @param assetWarrantyDTO the assetWarrantyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetWarrantyDTO, or with status {@code 400 (Bad Request)} if the assetWarranty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-warranties")
    public ResponseEntity<AssetWarrantyDTO> createAssetWarranty(@Valid @RequestBody AssetWarrantyDTO assetWarrantyDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetWarranty : {}", assetWarrantyDTO);
        if (assetWarrantyDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetWarranty cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetWarrantyDTO result = assetWarrantyService.save(assetWarrantyDTO);
        return ResponseEntity
            .created(new URI("/api/asset-warranties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-warranties/:id} : Updates an existing assetWarranty.
     *
     * @param id the id of the assetWarrantyDTO to save.
     * @param assetWarrantyDTO the assetWarrantyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetWarrantyDTO,
     * or with status {@code 400 (Bad Request)} if the assetWarrantyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetWarrantyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-warranties/{id}")
    public ResponseEntity<AssetWarrantyDTO> updateAssetWarranty(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetWarrantyDTO assetWarrantyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetWarranty : {}, {}", id, assetWarrantyDTO);
        if (assetWarrantyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetWarrantyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetWarrantyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetWarrantyDTO result = assetWarrantyService.save(assetWarrantyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetWarrantyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-warranties/:id} : Partial updates given fields of an existing assetWarranty, field will ignore if it is null
     *
     * @param id the id of the assetWarrantyDTO to save.
     * @param assetWarrantyDTO the assetWarrantyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetWarrantyDTO,
     * or with status {@code 400 (Bad Request)} if the assetWarrantyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetWarrantyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetWarrantyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-warranties/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetWarrantyDTO> partialUpdateAssetWarranty(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetWarrantyDTO assetWarrantyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetWarranty partially : {}, {}", id, assetWarrantyDTO);
        if (assetWarrantyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetWarrantyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetWarrantyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetWarrantyDTO> result = assetWarrantyService.partialUpdate(assetWarrantyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, assetWarrantyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-warranties} : get all the assetWarranties.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetWarranties in body.
     */
    @GetMapping("/asset-warranties")
    public ResponseEntity<List<AssetWarrantyDTO>> getAllAssetWarranties(AssetWarrantyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssetWarranties by criteria: {}", criteria);
        Page<AssetWarrantyDTO> page = assetWarrantyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-warranties/count} : count all the assetWarranties.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-warranties/count")
    public ResponseEntity<Long> countAssetWarranties(AssetWarrantyCriteria criteria) {
        log.debug("REST request to count AssetWarranties by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetWarrantyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-warranties/:id} : get the "id" assetWarranty.
     *
     * @param id the id of the assetWarrantyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetWarrantyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-warranties/{id}")
    public ResponseEntity<AssetWarrantyDTO> getAssetWarranty(@PathVariable Long id) {
        log.debug("REST request to get AssetWarranty : {}", id);
        Optional<AssetWarrantyDTO> assetWarrantyDTO = assetWarrantyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetWarrantyDTO);
    }

    /**
     * {@code DELETE  /asset-warranties/:id} : delete the "id" assetWarranty.
     *
     * @param id the id of the assetWarrantyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-warranties/{id}")
    public ResponseEntity<Void> deleteAssetWarranty(@PathVariable Long id) {
        log.debug("REST request to delete AssetWarranty : {}", id);
        assetWarrantyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/asset-warranties?query=:query} : search for the assetWarranty corresponding
     * to the query.
     *
     * @param query the query of the assetWarranty search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-warranties")
    public ResponseEntity<List<AssetWarrantyDTO>> searchAssetWarranties(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AssetWarranties for query {}", query);
        Page<AssetWarrantyDTO> page = assetWarrantyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
