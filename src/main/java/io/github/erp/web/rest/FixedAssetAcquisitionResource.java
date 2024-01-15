package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.repository.FixedAssetAcquisitionRepository;
import io.github.erp.service.FixedAssetAcquisitionQueryService;
import io.github.erp.service.FixedAssetAcquisitionService;
import io.github.erp.service.criteria.FixedAssetAcquisitionCriteria;
import io.github.erp.service.dto.FixedAssetAcquisitionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FixedAssetAcquisition}.
 */
@RestController
@RequestMapping("/api")
public class FixedAssetAcquisitionResource {

    private final Logger log = LoggerFactory.getLogger(FixedAssetAcquisitionResource.class);

    private static final String ENTITY_NAME = "assetsFixedAssetAcquisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FixedAssetAcquisitionService fixedAssetAcquisitionService;

    private final FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository;

    private final FixedAssetAcquisitionQueryService fixedAssetAcquisitionQueryService;

    public FixedAssetAcquisitionResource(
        FixedAssetAcquisitionService fixedAssetAcquisitionService,
        FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository,
        FixedAssetAcquisitionQueryService fixedAssetAcquisitionQueryService
    ) {
        this.fixedAssetAcquisitionService = fixedAssetAcquisitionService;
        this.fixedAssetAcquisitionRepository = fixedAssetAcquisitionRepository;
        this.fixedAssetAcquisitionQueryService = fixedAssetAcquisitionQueryService;
    }

    /**
     * {@code POST  /fixed-asset-acquisitions} : Create a new fixedAssetAcquisition.
     *
     * @param fixedAssetAcquisitionDTO the fixedAssetAcquisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fixedAssetAcquisitionDTO, or with status {@code 400 (Bad Request)} if the fixedAssetAcquisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fixed-asset-acquisitions")
    public ResponseEntity<FixedAssetAcquisitionDTO> createFixedAssetAcquisition(
        @Valid @RequestBody FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FixedAssetAcquisition : {}", fixedAssetAcquisitionDTO);
        if (fixedAssetAcquisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new fixedAssetAcquisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FixedAssetAcquisitionDTO result = fixedAssetAcquisitionService.save(fixedAssetAcquisitionDTO);
        return ResponseEntity
            .created(new URI("/api/fixed-asset-acquisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fixed-asset-acquisitions/:id} : Updates an existing fixedAssetAcquisition.
     *
     * @param id the id of the fixedAssetAcquisitionDTO to save.
     * @param fixedAssetAcquisitionDTO the fixedAssetAcquisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fixedAssetAcquisitionDTO,
     * or with status {@code 400 (Bad Request)} if the fixedAssetAcquisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fixedAssetAcquisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fixed-asset-acquisitions/{id}")
    public ResponseEntity<FixedAssetAcquisitionDTO> updateFixedAssetAcquisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FixedAssetAcquisition : {}, {}", id, fixedAssetAcquisitionDTO);
        if (fixedAssetAcquisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fixedAssetAcquisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fixedAssetAcquisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FixedAssetAcquisitionDTO result = fixedAssetAcquisitionService.save(fixedAssetAcquisitionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fixedAssetAcquisitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fixed-asset-acquisitions/:id} : Partial updates given fields of an existing fixedAssetAcquisition, field will ignore if it is null
     *
     * @param id the id of the fixedAssetAcquisitionDTO to save.
     * @param fixedAssetAcquisitionDTO the fixedAssetAcquisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fixedAssetAcquisitionDTO,
     * or with status {@code 400 (Bad Request)} if the fixedAssetAcquisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fixedAssetAcquisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fixedAssetAcquisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fixed-asset-acquisitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FixedAssetAcquisitionDTO> partialUpdateFixedAssetAcquisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FixedAssetAcquisitionDTO fixedAssetAcquisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FixedAssetAcquisition partially : {}, {}", id, fixedAssetAcquisitionDTO);
        if (fixedAssetAcquisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fixedAssetAcquisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fixedAssetAcquisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FixedAssetAcquisitionDTO> result = fixedAssetAcquisitionService.partialUpdate(fixedAssetAcquisitionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fixedAssetAcquisitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fixed-asset-acquisitions} : get all the fixedAssetAcquisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fixedAssetAcquisitions in body.
     */
    @GetMapping("/fixed-asset-acquisitions")
    public ResponseEntity<List<FixedAssetAcquisitionDTO>> getAllFixedAssetAcquisitions(
        FixedAssetAcquisitionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get FixedAssetAcquisitions by criteria: {}", criteria);
        Page<FixedAssetAcquisitionDTO> page = fixedAssetAcquisitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fixed-asset-acquisitions/count} : count all the fixedAssetAcquisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fixed-asset-acquisitions/count")
    public ResponseEntity<Long> countFixedAssetAcquisitions(FixedAssetAcquisitionCriteria criteria) {
        log.debug("REST request to count FixedAssetAcquisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(fixedAssetAcquisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fixed-asset-acquisitions/:id} : get the "id" fixedAssetAcquisition.
     *
     * @param id the id of the fixedAssetAcquisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fixedAssetAcquisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fixed-asset-acquisitions/{id}")
    public ResponseEntity<FixedAssetAcquisitionDTO> getFixedAssetAcquisition(@PathVariable Long id) {
        log.debug("REST request to get FixedAssetAcquisition : {}", id);
        Optional<FixedAssetAcquisitionDTO> fixedAssetAcquisitionDTO = fixedAssetAcquisitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fixedAssetAcquisitionDTO);
    }

    /**
     * {@code DELETE  /fixed-asset-acquisitions/:id} : delete the "id" fixedAssetAcquisition.
     *
     * @param id the id of the fixedAssetAcquisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fixed-asset-acquisitions/{id}")
    public ResponseEntity<Void> deleteFixedAssetAcquisition(@PathVariable Long id) {
        log.debug("REST request to delete FixedAssetAcquisition : {}", id);
        fixedAssetAcquisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fixed-asset-acquisitions?query=:query} : search for the fixedAssetAcquisition corresponding
     * to the query.
     *
     * @param query the query of the fixedAssetAcquisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fixed-asset-acquisitions")
    public ResponseEntity<List<FixedAssetAcquisitionDTO>> searchFixedAssetAcquisitions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FixedAssetAcquisitions for query {}", query);
        Page<FixedAssetAcquisitionDTO> page = fixedAssetAcquisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
