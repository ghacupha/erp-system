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

import io.github.erp.repository.SettlementCurrencyRepository;
import io.github.erp.service.SettlementCurrencyQueryService;
import io.github.erp.service.SettlementCurrencyService;
import io.github.erp.service.criteria.SettlementCurrencyCriteria;
import io.github.erp.service.dto.SettlementCurrencyDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SettlementCurrency}.
 */
@RestController
@RequestMapping("/api")
public class SettlementCurrencyResource {

    private final Logger log = LoggerFactory.getLogger(SettlementCurrencyResource.class);

    private static final String ENTITY_NAME = "settlementCurrency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettlementCurrencyService settlementCurrencyService;

    private final SettlementCurrencyRepository settlementCurrencyRepository;

    private final SettlementCurrencyQueryService settlementCurrencyQueryService;

    public SettlementCurrencyResource(
        SettlementCurrencyService settlementCurrencyService,
        SettlementCurrencyRepository settlementCurrencyRepository,
        SettlementCurrencyQueryService settlementCurrencyQueryService
    ) {
        this.settlementCurrencyService = settlementCurrencyService;
        this.settlementCurrencyRepository = settlementCurrencyRepository;
        this.settlementCurrencyQueryService = settlementCurrencyQueryService;
    }

    /**
     * {@code POST  /settlement-currencies} : Create a new settlementCurrency.
     *
     * @param settlementCurrencyDTO the settlementCurrencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new settlementCurrencyDTO, or with status {@code 400 (Bad Request)} if the settlementCurrency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/settlement-currencies")
    public ResponseEntity<SettlementCurrencyDTO> createSettlementCurrency(@Valid @RequestBody SettlementCurrencyDTO settlementCurrencyDTO)
        throws URISyntaxException {
        log.debug("REST request to save SettlementCurrency : {}", settlementCurrencyDTO);
        if (settlementCurrencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new settlementCurrency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SettlementCurrencyDTO result = settlementCurrencyService.save(settlementCurrencyDTO);
        return ResponseEntity
            .created(new URI("/api/settlement-currencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /settlement-currencies/:id} : Updates an existing settlementCurrency.
     *
     * @param id the id of the settlementCurrencyDTO to save.
     * @param settlementCurrencyDTO the settlementCurrencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settlementCurrencyDTO,
     * or with status {@code 400 (Bad Request)} if the settlementCurrencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the settlementCurrencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/settlement-currencies/{id}")
    public ResponseEntity<SettlementCurrencyDTO> updateSettlementCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SettlementCurrencyDTO settlementCurrencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SettlementCurrency : {}, {}", id, settlementCurrencyDTO);
        if (settlementCurrencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settlementCurrencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settlementCurrencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SettlementCurrencyDTO result = settlementCurrencyService.save(settlementCurrencyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, settlementCurrencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /settlement-currencies/:id} : Partial updates given fields of an existing settlementCurrency, field will ignore if it is null
     *
     * @param id the id of the settlementCurrencyDTO to save.
     * @param settlementCurrencyDTO the settlementCurrencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settlementCurrencyDTO,
     * or with status {@code 400 (Bad Request)} if the settlementCurrencyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the settlementCurrencyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the settlementCurrencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/settlement-currencies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SettlementCurrencyDTO> partialUpdateSettlementCurrency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SettlementCurrencyDTO settlementCurrencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SettlementCurrency partially : {}, {}", id, settlementCurrencyDTO);
        if (settlementCurrencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settlementCurrencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settlementCurrencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SettlementCurrencyDTO> result = settlementCurrencyService.partialUpdate(settlementCurrencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, settlementCurrencyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /settlement-currencies} : get all the settlementCurrencies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of settlementCurrencies in body.
     */
    @GetMapping("/settlement-currencies")
    public ResponseEntity<List<SettlementCurrencyDTO>> getAllSettlementCurrencies(SettlementCurrencyCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SettlementCurrencies by criteria: {}", criteria);
        Page<SettlementCurrencyDTO> page = settlementCurrencyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /settlement-currencies/count} : count all the settlementCurrencies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/settlement-currencies/count")
    public ResponseEntity<Long> countSettlementCurrencies(SettlementCurrencyCriteria criteria) {
        log.debug("REST request to count SettlementCurrencies by criteria: {}", criteria);
        return ResponseEntity.ok().body(settlementCurrencyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /settlement-currencies/:id} : get the "id" settlementCurrency.
     *
     * @param id the id of the settlementCurrencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the settlementCurrencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/settlement-currencies/{id}")
    public ResponseEntity<SettlementCurrencyDTO> getSettlementCurrency(@PathVariable Long id) {
        log.debug("REST request to get SettlementCurrency : {}", id);
        Optional<SettlementCurrencyDTO> settlementCurrencyDTO = settlementCurrencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(settlementCurrencyDTO);
    }

    /**
     * {@code DELETE  /settlement-currencies/:id} : delete the "id" settlementCurrency.
     *
     * @param id the id of the settlementCurrencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/settlement-currencies/{id}")
    public ResponseEntity<Void> deleteSettlementCurrency(@PathVariable Long id) {
        log.debug("REST request to delete SettlementCurrency : {}", id);
        settlementCurrencyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/settlement-currencies?query=:query} : search for the settlementCurrency corresponding
     * to the query.
     *
     * @param query the query of the settlementCurrency search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/settlement-currencies")
    public ResponseEntity<List<SettlementCurrencyDTO>> searchSettlementCurrencies(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SettlementCurrencies for query {}", query);
        Page<SettlementCurrencyDTO> page = settlementCurrencyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
