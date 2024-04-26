package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.CountySubCountyCodeRepository;
import io.github.erp.service.CountySubCountyCodeQueryService;
import io.github.erp.service.CountySubCountyCodeService;
import io.github.erp.service.criteria.CountySubCountyCodeCriteria;
import io.github.erp.service.dto.CountySubCountyCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CountySubCountyCode}.
 */
@RestController
@RequestMapping("/api")
public class CountySubCountyCodeResource {

    private final Logger log = LoggerFactory.getLogger(CountySubCountyCodeResource.class);

    private static final String ENTITY_NAME = "gdiDataCountySubCountyCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountySubCountyCodeService countySubCountyCodeService;

    private final CountySubCountyCodeRepository countySubCountyCodeRepository;

    private final CountySubCountyCodeQueryService countySubCountyCodeQueryService;

    public CountySubCountyCodeResource(
        CountySubCountyCodeService countySubCountyCodeService,
        CountySubCountyCodeRepository countySubCountyCodeRepository,
        CountySubCountyCodeQueryService countySubCountyCodeQueryService
    ) {
        this.countySubCountyCodeService = countySubCountyCodeService;
        this.countySubCountyCodeRepository = countySubCountyCodeRepository;
        this.countySubCountyCodeQueryService = countySubCountyCodeQueryService;
    }

    /**
     * {@code POST  /county-sub-county-codes} : Create a new countySubCountyCode.
     *
     * @param countySubCountyCodeDTO the countySubCountyCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countySubCountyCodeDTO, or with status {@code 400 (Bad Request)} if the countySubCountyCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/county-sub-county-codes")
    public ResponseEntity<CountySubCountyCodeDTO> createCountySubCountyCode(
        @Valid @RequestBody CountySubCountyCodeDTO countySubCountyCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CountySubCountyCode : {}", countySubCountyCodeDTO);
        if (countySubCountyCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new countySubCountyCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountySubCountyCodeDTO result = countySubCountyCodeService.save(countySubCountyCodeDTO);
        return ResponseEntity
            .created(new URI("/api/county-sub-county-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /county-sub-county-codes/:id} : Updates an existing countySubCountyCode.
     *
     * @param id the id of the countySubCountyCodeDTO to save.
     * @param countySubCountyCodeDTO the countySubCountyCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countySubCountyCodeDTO,
     * or with status {@code 400 (Bad Request)} if the countySubCountyCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countySubCountyCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/county-sub-county-codes/{id}")
    public ResponseEntity<CountySubCountyCodeDTO> updateCountySubCountyCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountySubCountyCodeDTO countySubCountyCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CountySubCountyCode : {}, {}", id, countySubCountyCodeDTO);
        if (countySubCountyCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countySubCountyCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countySubCountyCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CountySubCountyCodeDTO result = countySubCountyCodeService.save(countySubCountyCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countySubCountyCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /county-sub-county-codes/:id} : Partial updates given fields of an existing countySubCountyCode, field will ignore if it is null
     *
     * @param id the id of the countySubCountyCodeDTO to save.
     * @param countySubCountyCodeDTO the countySubCountyCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countySubCountyCodeDTO,
     * or with status {@code 400 (Bad Request)} if the countySubCountyCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the countySubCountyCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the countySubCountyCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/county-sub-county-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountySubCountyCodeDTO> partialUpdateCountySubCountyCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountySubCountyCodeDTO countySubCountyCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CountySubCountyCode partially : {}, {}", id, countySubCountyCodeDTO);
        if (countySubCountyCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countySubCountyCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countySubCountyCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountySubCountyCodeDTO> result = countySubCountyCodeService.partialUpdate(countySubCountyCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, countySubCountyCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /county-sub-county-codes} : get all the countySubCountyCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countySubCountyCodes in body.
     */
    @GetMapping("/county-sub-county-codes")
    public ResponseEntity<List<CountySubCountyCodeDTO>> getAllCountySubCountyCodes(
        CountySubCountyCodeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CountySubCountyCodes by criteria: {}", criteria);
        Page<CountySubCountyCodeDTO> page = countySubCountyCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /county-sub-county-codes/count} : count all the countySubCountyCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/county-sub-county-codes/count")
    public ResponseEntity<Long> countCountySubCountyCodes(CountySubCountyCodeCriteria criteria) {
        log.debug("REST request to count CountySubCountyCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(countySubCountyCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /county-sub-county-codes/:id} : get the "id" countySubCountyCode.
     *
     * @param id the id of the countySubCountyCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countySubCountyCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/county-sub-county-codes/{id}")
    public ResponseEntity<CountySubCountyCodeDTO> getCountySubCountyCode(@PathVariable Long id) {
        log.debug("REST request to get CountySubCountyCode : {}", id);
        Optional<CountySubCountyCodeDTO> countySubCountyCodeDTO = countySubCountyCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countySubCountyCodeDTO);
    }

    /**
     * {@code DELETE  /county-sub-county-codes/:id} : delete the "id" countySubCountyCode.
     *
     * @param id the id of the countySubCountyCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/county-sub-county-codes/{id}")
    public ResponseEntity<Void> deleteCountySubCountyCode(@PathVariable Long id) {
        log.debug("REST request to delete CountySubCountyCode : {}", id);
        countySubCountyCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/county-sub-county-codes?query=:query} : search for the countySubCountyCode corresponding
     * to the query.
     *
     * @param query the query of the countySubCountyCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/county-sub-county-codes")
    public ResponseEntity<List<CountySubCountyCodeDTO>> searchCountySubCountyCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CountySubCountyCodes for query {}", query);
        Page<CountySubCountyCodeDTO> page = countySubCountyCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
