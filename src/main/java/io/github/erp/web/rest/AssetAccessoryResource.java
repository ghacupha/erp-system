package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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

import io.github.erp.repository.AssetAccessoryRepository;
import io.github.erp.service.AssetAccessoryQueryService;
import io.github.erp.service.AssetAccessoryService;
import io.github.erp.service.criteria.AssetAccessoryCriteria;
import io.github.erp.service.dto.AssetAccessoryDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetAccessory}.
 */
@RestController
@RequestMapping("/api")
public class AssetAccessoryResource {

    private final Logger log = LoggerFactory.getLogger(AssetAccessoryResource.class);

    private static final String ENTITY_NAME = "assetAccessory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetAccessoryService assetAccessoryService;

    private final AssetAccessoryRepository assetAccessoryRepository;

    private final AssetAccessoryQueryService assetAccessoryQueryService;

    public AssetAccessoryResource(
        AssetAccessoryService assetAccessoryService,
        AssetAccessoryRepository assetAccessoryRepository,
        AssetAccessoryQueryService assetAccessoryQueryService
    ) {
        this.assetAccessoryService = assetAccessoryService;
        this.assetAccessoryRepository = assetAccessoryRepository;
        this.assetAccessoryQueryService = assetAccessoryQueryService;
    }

    /**
     * {@code POST  /asset-accessories} : Create a new assetAccessory.
     *
     * @param assetAccessoryDTO the assetAccessoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetAccessoryDTO, or with status {@code 400 (Bad Request)} if the assetAccessory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-accessories")
    public ResponseEntity<AssetAccessoryDTO> createAssetAccessory(@Valid @RequestBody AssetAccessoryDTO assetAccessoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetAccessory : {}", assetAccessoryDTO);
        if (assetAccessoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetAccessory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetAccessoryDTO result = assetAccessoryService.save(assetAccessoryDTO);
        return ResponseEntity
            .created(new URI("/api/asset-accessories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-accessories/:id} : Updates an existing assetAccessory.
     *
     * @param id the id of the assetAccessoryDTO to save.
     * @param assetAccessoryDTO the assetAccessoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetAccessoryDTO,
     * or with status {@code 400 (Bad Request)} if the assetAccessoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetAccessoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-accessories/{id}")
    public ResponseEntity<AssetAccessoryDTO> updateAssetAccessory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetAccessoryDTO assetAccessoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetAccessory : {}, {}", id, assetAccessoryDTO);
        if (assetAccessoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetAccessoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetAccessoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetAccessoryDTO result = assetAccessoryService.save(assetAccessoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetAccessoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-accessories/:id} : Partial updates given fields of an existing assetAccessory, field will ignore if it is null
     *
     * @param id the id of the assetAccessoryDTO to save.
     * @param assetAccessoryDTO the assetAccessoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetAccessoryDTO,
     * or with status {@code 400 (Bad Request)} if the assetAccessoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetAccessoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetAccessoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-accessories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetAccessoryDTO> partialUpdateAssetAccessory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetAccessoryDTO assetAccessoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetAccessory partially : {}, {}", id, assetAccessoryDTO);
        if (assetAccessoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetAccessoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetAccessoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetAccessoryDTO> result = assetAccessoryService.partialUpdate(assetAccessoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetAccessoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-accessories} : get all the assetAccessories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetAccessories in body.
     */
    @GetMapping("/asset-accessories")
    public ResponseEntity<List<AssetAccessoryDTO>> getAllAssetAccessories(AssetAccessoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssetAccessories by criteria: {}", criteria);
        Page<AssetAccessoryDTO> page = assetAccessoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-accessories/count} : count all the assetAccessories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-accessories/count")
    public ResponseEntity<Long> countAssetAccessories(AssetAccessoryCriteria criteria) {
        log.debug("REST request to count AssetAccessories by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetAccessoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-accessories/:id} : get the "id" assetAccessory.
     *
     * @param id the id of the assetAccessoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetAccessoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-accessories/{id}")
    public ResponseEntity<AssetAccessoryDTO> getAssetAccessory(@PathVariable Long id) {
        log.debug("REST request to get AssetAccessory : {}", id);
        Optional<AssetAccessoryDTO> assetAccessoryDTO = assetAccessoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetAccessoryDTO);
    }

    /**
     * {@code DELETE  /asset-accessories/:id} : delete the "id" assetAccessory.
     *
     * @param id the id of the assetAccessoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-accessories/{id}")
    public ResponseEntity<Void> deleteAssetAccessory(@PathVariable Long id) {
        log.debug("REST request to delete AssetAccessory : {}", id);
        assetAccessoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/asset-accessories?query=:query} : search for the assetAccessory corresponding
     * to the query.
     *
     * @param query the query of the assetAccessory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-accessories")
    public ResponseEntity<List<AssetAccessoryDTO>> searchAssetAccessories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AssetAccessories for query {}", query);
        Page<AssetAccessoryDTO> page = assetAccessoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
