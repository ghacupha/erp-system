package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.repository.FixedAssetNetBookValueRepository;
import io.github.erp.service.FixedAssetNetBookValueQueryService;
import io.github.erp.service.FixedAssetNetBookValueService;
import io.github.erp.service.criteria.FixedAssetNetBookValueCriteria;
import io.github.erp.service.dto.FixedAssetNetBookValueDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link io.github.erp.domain.FixedAssetNetBookValue}.
 */
@RestController
@RequestMapping("/api")
public class FixedAssetNetBookValueResource {

    private final Logger log = LoggerFactory.getLogger(FixedAssetNetBookValueResource.class);

    private static final String ENTITY_NAME = "assetsFixedAssetNetBookValue";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FixedAssetNetBookValueService fixedAssetNetBookValueService;

    private final FixedAssetNetBookValueRepository fixedAssetNetBookValueRepository;

    private final FixedAssetNetBookValueQueryService fixedAssetNetBookValueQueryService;

    public FixedAssetNetBookValueResource(
        FixedAssetNetBookValueService fixedAssetNetBookValueService,
        FixedAssetNetBookValueRepository fixedAssetNetBookValueRepository,
        FixedAssetNetBookValueQueryService fixedAssetNetBookValueQueryService
    ) {
        this.fixedAssetNetBookValueService = fixedAssetNetBookValueService;
        this.fixedAssetNetBookValueRepository = fixedAssetNetBookValueRepository;
        this.fixedAssetNetBookValueQueryService = fixedAssetNetBookValueQueryService;
    }

    /**
     * {@code POST  /fixed-asset-net-book-values} : Create a new fixedAssetNetBookValue.
     *
     * @param fixedAssetNetBookValueDTO the fixedAssetNetBookValueDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fixedAssetNetBookValueDTO, or with status {@code 400 (Bad Request)} if the fixedAssetNetBookValue has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fixed-asset-net-book-values")
    public ResponseEntity<FixedAssetNetBookValueDTO> createFixedAssetNetBookValue(
        @RequestBody FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FixedAssetNetBookValue : {}", fixedAssetNetBookValueDTO);
        if (fixedAssetNetBookValueDTO.getId() != null) {
            throw new BadRequestAlertException("A new fixedAssetNetBookValue cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FixedAssetNetBookValueDTO result = fixedAssetNetBookValueService.save(fixedAssetNetBookValueDTO);
        return ResponseEntity
            .created(new URI("/api/fixed-asset-net-book-values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fixed-asset-net-book-values/:id} : Updates an existing fixedAssetNetBookValue.
     *
     * @param id the id of the fixedAssetNetBookValueDTO to save.
     * @param fixedAssetNetBookValueDTO the fixedAssetNetBookValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fixedAssetNetBookValueDTO,
     * or with status {@code 400 (Bad Request)} if the fixedAssetNetBookValueDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fixedAssetNetBookValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fixed-asset-net-book-values/{id}")
    public ResponseEntity<FixedAssetNetBookValueDTO> updateFixedAssetNetBookValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FixedAssetNetBookValue : {}, {}", id, fixedAssetNetBookValueDTO);
        if (fixedAssetNetBookValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fixedAssetNetBookValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fixedAssetNetBookValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FixedAssetNetBookValueDTO result = fixedAssetNetBookValueService.save(fixedAssetNetBookValueDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fixedAssetNetBookValueDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fixed-asset-net-book-values/:id} : Partial updates given fields of an existing fixedAssetNetBookValue, field will ignore if it is null
     *
     * @param id the id of the fixedAssetNetBookValueDTO to save.
     * @param fixedAssetNetBookValueDTO the fixedAssetNetBookValueDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fixedAssetNetBookValueDTO,
     * or with status {@code 400 (Bad Request)} if the fixedAssetNetBookValueDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fixedAssetNetBookValueDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fixedAssetNetBookValueDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fixed-asset-net-book-values/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FixedAssetNetBookValueDTO> partialUpdateFixedAssetNetBookValue(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FixedAssetNetBookValueDTO fixedAssetNetBookValueDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FixedAssetNetBookValue partially : {}, {}", id, fixedAssetNetBookValueDTO);
        if (fixedAssetNetBookValueDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fixedAssetNetBookValueDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fixedAssetNetBookValueRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FixedAssetNetBookValueDTO> result = fixedAssetNetBookValueService.partialUpdate(fixedAssetNetBookValueDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fixedAssetNetBookValueDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fixed-asset-net-book-values} : get all the fixedAssetNetBookValues.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fixedAssetNetBookValues in body.
     */
    @GetMapping("/fixed-asset-net-book-values")
    public ResponseEntity<List<FixedAssetNetBookValueDTO>> getAllFixedAssetNetBookValues(
        FixedAssetNetBookValueCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get FixedAssetNetBookValues by criteria: {}", criteria);
        Page<FixedAssetNetBookValueDTO> page = fixedAssetNetBookValueQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fixed-asset-net-book-values/count} : count all the fixedAssetNetBookValues.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fixed-asset-net-book-values/count")
    public ResponseEntity<Long> countFixedAssetNetBookValues(FixedAssetNetBookValueCriteria criteria) {
        log.debug("REST request to count FixedAssetNetBookValues by criteria: {}", criteria);
        return ResponseEntity.ok().body(fixedAssetNetBookValueQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fixed-asset-net-book-values/:id} : get the "id" fixedAssetNetBookValue.
     *
     * @param id the id of the fixedAssetNetBookValueDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fixedAssetNetBookValueDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fixed-asset-net-book-values/{id}")
    public ResponseEntity<FixedAssetNetBookValueDTO> getFixedAssetNetBookValue(@PathVariable Long id) {
        log.debug("REST request to get FixedAssetNetBookValue : {}", id);
        Optional<FixedAssetNetBookValueDTO> fixedAssetNetBookValueDTO = fixedAssetNetBookValueService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fixedAssetNetBookValueDTO);
    }

    /**
     * {@code DELETE  /fixed-asset-net-book-values/:id} : delete the "id" fixedAssetNetBookValue.
     *
     * @param id the id of the fixedAssetNetBookValueDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fixed-asset-net-book-values/{id}")
    public ResponseEntity<Void> deleteFixedAssetNetBookValue(@PathVariable Long id) {
        log.debug("REST request to delete FixedAssetNetBookValue : {}", id);
        fixedAssetNetBookValueService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fixed-asset-net-book-values?query=:query} : search for the fixedAssetNetBookValue corresponding
     * to the query.
     *
     * @param query the query of the fixedAssetNetBookValue search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fixed-asset-net-book-values")
    public ResponseEntity<List<FixedAssetNetBookValueDTO>> searchFixedAssetNetBookValues(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FixedAssetNetBookValues for query {}", query);
        Page<FixedAssetNetBookValueDTO> page = fixedAssetNetBookValueService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
