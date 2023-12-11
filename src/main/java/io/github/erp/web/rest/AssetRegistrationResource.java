package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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

import io.github.erp.repository.AssetRegistrationRepository;
import io.github.erp.service.AssetRegistrationQueryService;
import io.github.erp.service.AssetRegistrationService;
import io.github.erp.service.criteria.AssetRegistrationCriteria;
import io.github.erp.service.dto.AssetRegistrationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetRegistration}.
 */
@RestController
@RequestMapping("/api")
public class AssetRegistrationResource {

    private final Logger log = LoggerFactory.getLogger(AssetRegistrationResource.class);

    private static final String ENTITY_NAME = "assetRegistration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AssetRegistrationService assetRegistrationService;

    private final AssetRegistrationRepository assetRegistrationRepository;

    private final AssetRegistrationQueryService assetRegistrationQueryService;

    public AssetRegistrationResource(
        AssetRegistrationService assetRegistrationService,
        AssetRegistrationRepository assetRegistrationRepository,
        AssetRegistrationQueryService assetRegistrationQueryService
    ) {
        this.assetRegistrationService = assetRegistrationService;
        this.assetRegistrationRepository = assetRegistrationRepository;
        this.assetRegistrationQueryService = assetRegistrationQueryService;
    }

    /**
     * {@code POST  /asset-registrations} : Create a new assetRegistration.
     *
     * @param assetRegistrationDTO the assetRegistrationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new assetRegistrationDTO, or with status {@code 400 (Bad Request)} if the assetRegistration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/asset-registrations")
    public ResponseEntity<AssetRegistrationDTO> createAssetRegistration(@Valid @RequestBody AssetRegistrationDTO assetRegistrationDTO)
        throws URISyntaxException {
        log.debug("REST request to save AssetRegistration : {}", assetRegistrationDTO);
        if (assetRegistrationDTO.getId() != null) {
            throw new BadRequestAlertException("A new assetRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AssetRegistrationDTO result = assetRegistrationService.save(assetRegistrationDTO);
        return ResponseEntity
            .created(new URI("/api/asset-registrations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /asset-registrations/:id} : Updates an existing assetRegistration.
     *
     * @param id the id of the assetRegistrationDTO to save.
     * @param assetRegistrationDTO the assetRegistrationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetRegistrationDTO,
     * or with status {@code 400 (Bad Request)} if the assetRegistrationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the assetRegistrationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/asset-registrations/{id}")
    public ResponseEntity<AssetRegistrationDTO> updateAssetRegistration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AssetRegistrationDTO assetRegistrationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AssetRegistration : {}, {}", id, assetRegistrationDTO);
        if (assetRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetRegistrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetRegistrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AssetRegistrationDTO result = assetRegistrationService.save(assetRegistrationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetRegistrationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /asset-registrations/:id} : Partial updates given fields of an existing assetRegistration, field will ignore if it is null
     *
     * @param id the id of the assetRegistrationDTO to save.
     * @param assetRegistrationDTO the assetRegistrationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated assetRegistrationDTO,
     * or with status {@code 400 (Bad Request)} if the assetRegistrationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the assetRegistrationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the assetRegistrationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/asset-registrations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AssetRegistrationDTO> partialUpdateAssetRegistration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AssetRegistrationDTO assetRegistrationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AssetRegistration partially : {}, {}", id, assetRegistrationDTO);
        if (assetRegistrationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, assetRegistrationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!assetRegistrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AssetRegistrationDTO> result = assetRegistrationService.partialUpdate(assetRegistrationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, assetRegistrationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /asset-registrations} : get all the assetRegistrations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetRegistrations in body.
     */
    @GetMapping("/asset-registrations")
    public ResponseEntity<List<AssetRegistrationDTO>> getAllAssetRegistrations(AssetRegistrationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AssetRegistrations by criteria: {}", criteria);
        Page<AssetRegistrationDTO> page = assetRegistrationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-registrations/count} : count all the assetRegistrations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-registrations/count")
    public ResponseEntity<Long> countAssetRegistrations(AssetRegistrationCriteria criteria) {
        log.debug("REST request to count AssetRegistrations by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetRegistrationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-registrations/:id} : get the "id" assetRegistration.
     *
     * @param id the id of the assetRegistrationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetRegistrationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-registrations/{id}")
    public ResponseEntity<AssetRegistrationDTO> getAssetRegistration(@PathVariable Long id) {
        log.debug("REST request to get AssetRegistration : {}", id);
        Optional<AssetRegistrationDTO> assetRegistrationDTO = assetRegistrationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetRegistrationDTO);
    }

    /**
     * {@code DELETE  /asset-registrations/:id} : delete the "id" assetRegistration.
     *
     * @param id the id of the assetRegistrationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/asset-registrations/{id}")
    public ResponseEntity<Void> deleteAssetRegistration(@PathVariable Long id) {
        log.debug("REST request to delete AssetRegistration : {}", id);
        assetRegistrationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/asset-registrations?query=:query} : search for the assetRegistration corresponding
     * to the query.
     *
     * @param query the query of the assetRegistration search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-registrations")
    public ResponseEntity<List<AssetRegistrationDTO>> searchAssetRegistrations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AssetRegistrations for query {}", query);
        Page<AssetRegistrationDTO> page = assetRegistrationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
