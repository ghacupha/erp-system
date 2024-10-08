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

import io.github.erp.repository.AssetWriteOffRepository;
import io.github.erp.service.AssetWriteOffQueryService;
import io.github.erp.service.AssetWriteOffService;
import io.github.erp.service.criteria.AssetWriteOffCriteria;
import io.github.erp.service.dto.AssetWriteOffDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetWriteOff}.
 */
@RestController
@RequestMapping("/api")
public class AssetWriteOffResource {

    private final Logger log = LoggerFactory.getLogger(AssetWriteOffResource.class);

    private static final String ENTITY_NAME = "assetWriteOff";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetWriteOffService assetWriteOffService;

    private final AssetWriteOffRepository assetWriteOffRepository;

    private final AssetWriteOffQueryService assetWriteOffQueryService;

    public AssetWriteOffResource(
        AssetWriteOffService assetWriteOffService,
        AssetWriteOffRepository assetWriteOffRepository,
        AssetWriteOffQueryService assetWriteOffQueryService
    ) {
        this.assetWriteOffService = assetWriteOffService;
        this.assetWriteOffRepository = assetWriteOffRepository;
        this.assetWriteOffQueryService = assetWriteOffQueryService;
    }

    /**
     * {@code POST  /asset-write-offs} : Create a new assetWriteOff.
     *
     * @param assetWriteOffDTO the assetWriteOffDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetWriteOffDTO, or with status {@code 400 (Bad Request)} if the assetWriteOff has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-write-offs")
    public ResponseEntity<AssetWriteOffDTO> createAssetWriteOff(@Valid @RequestBody AssetWriteOffDTO assetWriteOffDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetWriteOff : {}", assetWriteOffDTO);
        if (assetWriteOffDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetWriteOff cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetWriteOffDTO result = assetWriteOffService.save(assetWriteOffDTO);
        return ResponseEntity
            .created(new URI("/api/asset-write-offs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-write-offs/:id} : Updates an existing assetWriteOff.
     *
     * @param id the id of the assetWriteOffDTO to save.
     * @param assetWriteOffDTO the assetWriteOffDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetWriteOffDTO,
     * or with status {@code 400 (Bad Request)} if the assetWriteOffDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetWriteOffDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-write-offs/{id}")
    public ResponseEntity<AssetWriteOffDTO> updateAssetWriteOff(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetWriteOffDTO assetWriteOffDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetWriteOff : {}, {}", id, assetWriteOffDTO);
        if (assetWriteOffDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetWriteOffDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetWriteOffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetWriteOffDTO result = assetWriteOffService.save(assetWriteOffDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetWriteOffDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-write-offs/:id} : Partial updates given fields of an existing assetWriteOff, field will ignore if it is null
     *
     * @param id the id of the assetWriteOffDTO to save.
     * @param assetWriteOffDTO the assetWriteOffDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetWriteOffDTO,
     * or with status {@code 400 (Bad Request)} if the assetWriteOffDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetWriteOffDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetWriteOffDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-write-offs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetWriteOffDTO> partialUpdateAssetWriteOff(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetWriteOffDTO assetWriteOffDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetWriteOff partially : {}, {}", id, assetWriteOffDTO);
        if (assetWriteOffDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetWriteOffDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetWriteOffRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetWriteOffDTO> result = assetWriteOffService.partialUpdate(assetWriteOffDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetWriteOffDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-write-offs} : get all the assetWriteOffs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetWriteOffs in body.
     */
    @GetMapping("/asset-write-offs")
    public ResponseEntity<List<AssetWriteOffDTO>> getAllAssetWriteOffs(AssetWriteOffCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssetWriteOffs by criteria: {}", criteria);
        Page<AssetWriteOffDTO> page = assetWriteOffQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-write-offs/count} : count all the assetWriteOffs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-write-offs/count")
    public ResponseEntity<Long> countAssetWriteOffs(AssetWriteOffCriteria criteria) {
        log.debug("REST request to count AssetWriteOffs by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetWriteOffQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-write-offs/:id} : get the "id" assetWriteOff.
     *
     * @param id the id of the assetWriteOffDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetWriteOffDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-write-offs/{id}")
    public ResponseEntity<AssetWriteOffDTO> getAssetWriteOff(@PathVariable Long id) {
        log.debug("REST request to get AssetWriteOff : {}", id);
        Optional<AssetWriteOffDTO> assetWriteOffDTO = assetWriteOffService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetWriteOffDTO);
    }

    /**
     * {@code DELETE  /asset-write-offs/:id} : delete the "id" assetWriteOff.
     *
     * @param id the id of the assetWriteOffDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-write-offs/{id}")
    public ResponseEntity<Void> deleteAssetWriteOff(@PathVariable Long id) {
        log.debug("REST request to delete AssetWriteOff : {}", id);
        assetWriteOffService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/asset-write-offs?query=:query} : search for the assetWriteOff corresponding
     * to the query.
     *
     * @param query the query of the assetWriteOff search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-write-offs")
    public ResponseEntity<List<AssetWriteOffDTO>> searchAssetWriteOffs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AssetWriteOffs for query {}", query);
        Page<AssetWriteOffDTO> page = assetWriteOffService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
