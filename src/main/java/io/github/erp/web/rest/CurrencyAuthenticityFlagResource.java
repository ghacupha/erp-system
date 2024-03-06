package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

import io.github.erp.repository.CurrencyAuthenticityFlagRepository;
import io.github.erp.service.CurrencyAuthenticityFlagQueryService;
import io.github.erp.service.CurrencyAuthenticityFlagService;
import io.github.erp.service.criteria.CurrencyAuthenticityFlagCriteria;
import io.github.erp.service.dto.CurrencyAuthenticityFlagDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CurrencyAuthenticityFlag}.
 */
@RestController
@RequestMapping("/api")
public class CurrencyAuthenticityFlagResource {

    private final Logger log = LoggerFactory.getLogger(CurrencyAuthenticityFlagResource.class);

    private static final String ENTITY_NAME = "currencyAuthenticityFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyAuthenticityFlagService currencyAuthenticityFlagService;

    private final CurrencyAuthenticityFlagRepository currencyAuthenticityFlagRepository;

    private final CurrencyAuthenticityFlagQueryService currencyAuthenticityFlagQueryService;

    public CurrencyAuthenticityFlagResource(
        CurrencyAuthenticityFlagService currencyAuthenticityFlagService,
        CurrencyAuthenticityFlagRepository currencyAuthenticityFlagRepository,
        CurrencyAuthenticityFlagQueryService currencyAuthenticityFlagQueryService
    ) {
        this.currencyAuthenticityFlagService = currencyAuthenticityFlagService;
        this.currencyAuthenticityFlagRepository = currencyAuthenticityFlagRepository;
        this.currencyAuthenticityFlagQueryService = currencyAuthenticityFlagQueryService;
    }

    /**
     * {@code POST  /currency-authenticity-flags} : Create a new currencyAuthenticityFlag.
     *
     * @param currencyAuthenticityFlagDTO the currencyAuthenticityFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyAuthenticityFlagDTO, or with status {@code 400 (Bad Request)} if the currencyAuthenticityFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currency-authenticity-flags")
    public ResponseEntity<CurrencyAuthenticityFlagDTO> createCurrencyAuthenticityFlag(
        @Valid @RequestBody CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CurrencyAuthenticityFlag : {}", currencyAuthenticityFlagDTO);
        if (currencyAuthenticityFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new currencyAuthenticityFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyAuthenticityFlagDTO result = currencyAuthenticityFlagService.save(currencyAuthenticityFlagDTO);
        return ResponseEntity
            .created(new URI("/api/currency-authenticity-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /currency-authenticity-flags/:id} : Updates an existing currencyAuthenticityFlag.
     *
     * @param id the id of the currencyAuthenticityFlagDTO to save.
     * @param currencyAuthenticityFlagDTO the currencyAuthenticityFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyAuthenticityFlagDTO,
     * or with status {@code 400 (Bad Request)} if the currencyAuthenticityFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyAuthenticityFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currency-authenticity-flags/{id}")
    public ResponseEntity<CurrencyAuthenticityFlagDTO> updateCurrencyAuthenticityFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CurrencyAuthenticityFlag : {}, {}", id, currencyAuthenticityFlagDTO);
        if (currencyAuthenticityFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyAuthenticityFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyAuthenticityFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CurrencyAuthenticityFlagDTO result = currencyAuthenticityFlagService.save(currencyAuthenticityFlagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyAuthenticityFlagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /currency-authenticity-flags/:id} : Partial updates given fields of an existing currencyAuthenticityFlag, field will ignore if it is null
     *
     * @param id the id of the currencyAuthenticityFlagDTO to save.
     * @param currencyAuthenticityFlagDTO the currencyAuthenticityFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyAuthenticityFlagDTO,
     * or with status {@code 400 (Bad Request)} if the currencyAuthenticityFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the currencyAuthenticityFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the currencyAuthenticityFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/currency-authenticity-flags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurrencyAuthenticityFlagDTO> partialUpdateCurrencyAuthenticityFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurrencyAuthenticityFlagDTO currencyAuthenticityFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CurrencyAuthenticityFlag partially : {}, {}", id, currencyAuthenticityFlagDTO);
        if (currencyAuthenticityFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyAuthenticityFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyAuthenticityFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CurrencyAuthenticityFlagDTO> result = currencyAuthenticityFlagService.partialUpdate(currencyAuthenticityFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyAuthenticityFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /currency-authenticity-flags} : get all the currencyAuthenticityFlags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencyAuthenticityFlags in body.
     */
    @GetMapping("/currency-authenticity-flags")
    public ResponseEntity<List<CurrencyAuthenticityFlagDTO>> getAllCurrencyAuthenticityFlags(
        CurrencyAuthenticityFlagCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CurrencyAuthenticityFlags by criteria: {}", criteria);
        Page<CurrencyAuthenticityFlagDTO> page = currencyAuthenticityFlagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /currency-authenticity-flags/count} : count all the currencyAuthenticityFlags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/currency-authenticity-flags/count")
    public ResponseEntity<Long> countCurrencyAuthenticityFlags(CurrencyAuthenticityFlagCriteria criteria) {
        log.debug("REST request to count CurrencyAuthenticityFlags by criteria: {}", criteria);
        return ResponseEntity.ok().body(currencyAuthenticityFlagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /currency-authenticity-flags/:id} : get the "id" currencyAuthenticityFlag.
     *
     * @param id the id of the currencyAuthenticityFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyAuthenticityFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currency-authenticity-flags/{id}")
    public ResponseEntity<CurrencyAuthenticityFlagDTO> getCurrencyAuthenticityFlag(@PathVariable Long id) {
        log.debug("REST request to get CurrencyAuthenticityFlag : {}", id);
        Optional<CurrencyAuthenticityFlagDTO> currencyAuthenticityFlagDTO = currencyAuthenticityFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyAuthenticityFlagDTO);
    }

    /**
     * {@code DELETE  /currency-authenticity-flags/:id} : delete the "id" currencyAuthenticityFlag.
     *
     * @param id the id of the currencyAuthenticityFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currency-authenticity-flags/{id}")
    public ResponseEntity<Void> deleteCurrencyAuthenticityFlag(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyAuthenticityFlag : {}", id);
        currencyAuthenticityFlagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/currency-authenticity-flags?query=:query} : search for the currencyAuthenticityFlag corresponding
     * to the query.
     *
     * @param query the query of the currencyAuthenticityFlag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/currency-authenticity-flags")
    public ResponseEntity<List<CurrencyAuthenticityFlagDTO>> searchCurrencyAuthenticityFlags(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CurrencyAuthenticityFlags for query {}", query);
        Page<CurrencyAuthenticityFlagDTO> page = currencyAuthenticityFlagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
