package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.repository.CurrencyServiceabilityFlagRepository;
import io.github.erp.service.CurrencyServiceabilityFlagQueryService;
import io.github.erp.service.CurrencyServiceabilityFlagService;
import io.github.erp.service.criteria.CurrencyServiceabilityFlagCriteria;
import io.github.erp.service.dto.CurrencyServiceabilityFlagDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CurrencyServiceabilityFlag}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CurrencyServiceabilityFlagResourceProd {

    private final Logger log = LoggerFactory.getLogger(CurrencyServiceabilityFlagResourceProd.class);

    private static final String ENTITY_NAME = "currencyServiceabilityFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CurrencyServiceabilityFlagService currencyServiceabilityFlagService;

    private final CurrencyServiceabilityFlagRepository currencyServiceabilityFlagRepository;

    private final CurrencyServiceabilityFlagQueryService currencyServiceabilityFlagQueryService;

    public CurrencyServiceabilityFlagResourceProd(
        CurrencyServiceabilityFlagService currencyServiceabilityFlagService,
        CurrencyServiceabilityFlagRepository currencyServiceabilityFlagRepository,
        CurrencyServiceabilityFlagQueryService currencyServiceabilityFlagQueryService
    ) {
        this.currencyServiceabilityFlagService = currencyServiceabilityFlagService;
        this.currencyServiceabilityFlagRepository = currencyServiceabilityFlagRepository;
        this.currencyServiceabilityFlagQueryService = currencyServiceabilityFlagQueryService;
    }

    /**
     * {@code POST  /currency-serviceability-flags} : Create a new currencyServiceabilityFlag.
     *
     * @param currencyServiceabilityFlagDTO the currencyServiceabilityFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new currencyServiceabilityFlagDTO, or with status {@code 400 (Bad Request)} if the currencyServiceabilityFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/currency-serviceability-flags")
    public ResponseEntity<CurrencyServiceabilityFlagDTO> createCurrencyServiceabilityFlag(
        @Valid @RequestBody CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CurrencyServiceabilityFlag : {}", currencyServiceabilityFlagDTO);
        if (currencyServiceabilityFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new currencyServiceabilityFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CurrencyServiceabilityFlagDTO result = currencyServiceabilityFlagService.save(currencyServiceabilityFlagDTO);
        return ResponseEntity
            .created(new URI("/api/currency-serviceability-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /currency-serviceability-flags/:id} : Updates an existing currencyServiceabilityFlag.
     *
     * @param id the id of the currencyServiceabilityFlagDTO to save.
     * @param currencyServiceabilityFlagDTO the currencyServiceabilityFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyServiceabilityFlagDTO,
     * or with status {@code 400 (Bad Request)} if the currencyServiceabilityFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the currencyServiceabilityFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/currency-serviceability-flags/{id}")
    public ResponseEntity<CurrencyServiceabilityFlagDTO> updateCurrencyServiceabilityFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CurrencyServiceabilityFlag : {}, {}", id, currencyServiceabilityFlagDTO);
        if (currencyServiceabilityFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyServiceabilityFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyServiceabilityFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CurrencyServiceabilityFlagDTO result = currencyServiceabilityFlagService.save(currencyServiceabilityFlagDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyServiceabilityFlagDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /currency-serviceability-flags/:id} : Partial updates given fields of an existing currencyServiceabilityFlag, field will ignore if it is null
     *
     * @param id the id of the currencyServiceabilityFlagDTO to save.
     * @param currencyServiceabilityFlagDTO the currencyServiceabilityFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated currencyServiceabilityFlagDTO,
     * or with status {@code 400 (Bad Request)} if the currencyServiceabilityFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the currencyServiceabilityFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the currencyServiceabilityFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/currency-serviceability-flags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CurrencyServiceabilityFlagDTO> partialUpdateCurrencyServiceabilityFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CurrencyServiceabilityFlagDTO currencyServiceabilityFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CurrencyServiceabilityFlag partially : {}, {}", id, currencyServiceabilityFlagDTO);
        if (currencyServiceabilityFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, currencyServiceabilityFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!currencyServiceabilityFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CurrencyServiceabilityFlagDTO> result = currencyServiceabilityFlagService.partialUpdate(currencyServiceabilityFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, currencyServiceabilityFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /currency-serviceability-flags} : get all the currencyServiceabilityFlags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of currencyServiceabilityFlags in body.
     */
    @GetMapping("/currency-serviceability-flags")
    public ResponseEntity<List<CurrencyServiceabilityFlagDTO>> getAllCurrencyServiceabilityFlags(
        CurrencyServiceabilityFlagCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CurrencyServiceabilityFlags by criteria: {}", criteria);
        Page<CurrencyServiceabilityFlagDTO> page = currencyServiceabilityFlagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /currency-serviceability-flags/count} : count all the currencyServiceabilityFlags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/currency-serviceability-flags/count")
    public ResponseEntity<Long> countCurrencyServiceabilityFlags(CurrencyServiceabilityFlagCriteria criteria) {
        log.debug("REST request to count CurrencyServiceabilityFlags by criteria: {}", criteria);
        return ResponseEntity.ok().body(currencyServiceabilityFlagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /currency-serviceability-flags/:id} : get the "id" currencyServiceabilityFlag.
     *
     * @param id the id of the currencyServiceabilityFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the currencyServiceabilityFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/currency-serviceability-flags/{id}")
    public ResponseEntity<CurrencyServiceabilityFlagDTO> getCurrencyServiceabilityFlag(@PathVariable Long id) {
        log.debug("REST request to get CurrencyServiceabilityFlag : {}", id);
        Optional<CurrencyServiceabilityFlagDTO> currencyServiceabilityFlagDTO = currencyServiceabilityFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(currencyServiceabilityFlagDTO);
    }

    /**
     * {@code DELETE  /currency-serviceability-flags/:id} : delete the "id" currencyServiceabilityFlag.
     *
     * @param id the id of the currencyServiceabilityFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/currency-serviceability-flags/{id}")
    public ResponseEntity<Void> deleteCurrencyServiceabilityFlag(@PathVariable Long id) {
        log.debug("REST request to delete CurrencyServiceabilityFlag : {}", id);
        currencyServiceabilityFlagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/currency-serviceability-flags?query=:query} : search for the currencyServiceabilityFlag corresponding
     * to the query.
     *
     * @param query the query of the currencyServiceabilityFlag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/currency-serviceability-flags")
    public ResponseEntity<List<CurrencyServiceabilityFlagDTO>> searchCurrencyServiceabilityFlags(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CurrencyServiceabilityFlags for query {}", query);
        Page<CurrencyServiceabilityFlagDTO> page = currencyServiceabilityFlagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
