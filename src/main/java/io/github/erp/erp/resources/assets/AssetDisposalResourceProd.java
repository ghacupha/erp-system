package io.github.erp.erp.resources.assets;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.repository.AssetDisposalRepository;
import io.github.erp.service.AssetDisposalQueryService;
import io.github.erp.service.AssetDisposalService;
import io.github.erp.service.criteria.AssetDisposalCriteria;
import io.github.erp.service.dto.AssetDisposalDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetDisposal}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
public class AssetDisposalResourceProd {

    private final Logger log = LoggerFactory.getLogger(AssetDisposalResourceProd.class);

    private static final String ENTITY_NAME = "assetDisposal";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetDisposalService assetDisposalService;

    private final AssetDisposalRepository assetDisposalRepository;

    private final AssetDisposalQueryService assetDisposalQueryService;

    public AssetDisposalResourceProd(
        AssetDisposalService assetDisposalService,
        AssetDisposalRepository assetDisposalRepository,
        AssetDisposalQueryService assetDisposalQueryService
    ) {
        this.assetDisposalService = assetDisposalService;
        this.assetDisposalRepository = assetDisposalRepository;
        this.assetDisposalQueryService = assetDisposalQueryService;
    }

    /**
     * {@code POST  /asset-disposals} : Create a new assetDisposal.
     *
     * @param assetDisposalDTO the assetDisposalDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetDisposalDTO, or with status {@code 400 (Bad Request)} if the assetDisposal has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-disposals")
    public ResponseEntity<AssetDisposalDTO> createAssetDisposal(@Valid @RequestBody AssetDisposalDTO assetDisposalDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetDisposal : {}", assetDisposalDTO);
        if (assetDisposalDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetDisposal cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetDisposalDTO result = assetDisposalService.save(assetDisposalDTO);
        return ResponseEntity
            .created(new URI("/api/asset-disposals/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-disposals/:id} : Updates an existing assetDisposal.
     *
     * @param id the id of the assetDisposalDTO to save.
     * @param assetDisposalDTO the assetDisposalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDisposalDTO,
     * or with status {@code 400 (Bad Request)} if the assetDisposalDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetDisposalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-disposals/{id}")
    public ResponseEntity<AssetDisposalDTO> updateAssetDisposal(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetDisposalDTO assetDisposalDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetDisposal : {}, {}", id, assetDisposalDTO);
        if (assetDisposalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDisposalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetDisposalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetDisposalDTO result = assetDisposalService.save(assetDisposalDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetDisposalDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-disposals/:id} : Partial updates given fields of an existing assetDisposal, field will ignore if it is null
     *
     * @param id the id of the assetDisposalDTO to save.
     * @param assetDisposalDTO the assetDisposalDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetDisposalDTO,
     * or with status {@code 400 (Bad Request)} if the assetDisposalDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetDisposalDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetDisposalDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-disposals/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetDisposalDTO> partialUpdateAssetDisposal(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetDisposalDTO assetDisposalDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetDisposal partially : {}, {}", id, assetDisposalDTO);
        if (assetDisposalDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetDisposalDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetDisposalRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetDisposalDTO> result = assetDisposalService.partialUpdate(assetDisposalDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetDisposalDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-disposals} : get all the assetDisposals.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetDisposals in body.
     */
    @GetMapping("/asset-disposals")
    public ResponseEntity<List<AssetDisposalDTO>> getAllAssetDisposals(AssetDisposalCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssetDisposals by criteria: {}", criteria);
        Page<AssetDisposalDTO> page = assetDisposalQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-disposals/count} : count all the assetDisposals.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-disposals/count")
    public ResponseEntity<Long> countAssetDisposals(AssetDisposalCriteria criteria) {
        log.debug("REST request to count AssetDisposals by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetDisposalQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-disposals/:id} : get the "id" assetDisposal.
     *
     * @param id the id of the assetDisposalDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetDisposalDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-disposals/{id}")
    public ResponseEntity<AssetDisposalDTO> getAssetDisposal(@PathVariable Long id) {
        log.debug("REST request to get AssetDisposal : {}", id);
        Optional<AssetDisposalDTO> assetDisposalDTO = assetDisposalService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetDisposalDTO);
    }

    /**
     * {@code DELETE  /asset-disposals/:id} : delete the "id" assetDisposal.
     *
     * @param id the id of the assetDisposalDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-disposals/{id}")
    public ResponseEntity<Void> deleteAssetDisposal(@PathVariable Long id) {
        log.debug("REST request to delete AssetDisposal : {}", id);
        assetDisposalService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/asset-disposals?query=:query} : search for the assetDisposal corresponding
     * to the query.
     *
     * @param query the query of the assetDisposal search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-disposals")
    public ResponseEntity<List<AssetDisposalDTO>> searchAssetDisposals(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AssetDisposals for query {}", query);
        Page<AssetDisposalDTO> page = assetDisposalService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
