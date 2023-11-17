package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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

import io.github.erp.repository.ExchangeRateRepository;
import io.github.erp.service.ExchangeRateQueryService;
import io.github.erp.service.ExchangeRateService;
import io.github.erp.service.criteria.ExchangeRateCriteria;
import io.github.erp.service.dto.ExchangeRateDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ExchangeRate}.
 */
@RestController
@RequestMapping("/api")
public class ExchangeRateResource {

    private final Logger log = LoggerFactory.getLogger(ExchangeRateResource.class);

    private static final String ENTITY_NAME = "gdiDataExchangeRate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExchangeRateService exchangeRateService;

    private final ExchangeRateRepository exchangeRateRepository;

    private final ExchangeRateQueryService exchangeRateQueryService;

    public ExchangeRateResource(
        ExchangeRateService exchangeRateService,
        ExchangeRateRepository exchangeRateRepository,
        ExchangeRateQueryService exchangeRateQueryService
    ) {
        this.exchangeRateService = exchangeRateService;
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateQueryService = exchangeRateQueryService;
    }

    /**
     * {@code POST  /exchange-rates} : Create a new exchangeRate.
     *
     * @param exchangeRateDTO the exchangeRateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new exchangeRateDTO, or with status {@code 400 (Bad Request)} if the exchangeRate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/exchange-rates")
    public ResponseEntity<ExchangeRateDTO> createExchangeRate(@Valid @RequestBody ExchangeRateDTO exchangeRateDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExchangeRate : {}", exchangeRateDTO);
        if (exchangeRateDTO.getId() != null) {
            throw new BadRequestAlertException("A new exchangeRate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExchangeRateDTO result = exchangeRateService.save(exchangeRateDTO);
        return ResponseEntity
            .created(new URI("/api/exchange-rates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /exchange-rates/:id} : Updates an existing exchangeRate.
     *
     * @param id the id of the exchangeRateDTO to save.
     * @param exchangeRateDTO the exchangeRateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exchangeRateDTO,
     * or with status {@code 400 (Bad Request)} if the exchangeRateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the exchangeRateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/exchange-rates/{id}")
    public ResponseEntity<ExchangeRateDTO> updateExchangeRate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExchangeRateDTO exchangeRateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExchangeRate : {}, {}", id, exchangeRateDTO);
        if (exchangeRateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exchangeRateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exchangeRateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExchangeRateDTO result = exchangeRateService.save(exchangeRateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exchangeRateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /exchange-rates/:id} : Partial updates given fields of an existing exchangeRate, field will ignore if it is null
     *
     * @param id the id of the exchangeRateDTO to save.
     * @param exchangeRateDTO the exchangeRateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated exchangeRateDTO,
     * or with status {@code 400 (Bad Request)} if the exchangeRateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the exchangeRateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the exchangeRateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/exchange-rates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExchangeRateDTO> partialUpdateExchangeRate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExchangeRateDTO exchangeRateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExchangeRate partially : {}, {}", id, exchangeRateDTO);
        if (exchangeRateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, exchangeRateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!exchangeRateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExchangeRateDTO> result = exchangeRateService.partialUpdate(exchangeRateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, exchangeRateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /exchange-rates} : get all the exchangeRates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of exchangeRates in body.
     */
    @GetMapping("/exchange-rates")
    public ResponseEntity<List<ExchangeRateDTO>> getAllExchangeRates(ExchangeRateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExchangeRates by criteria: {}", criteria);
        Page<ExchangeRateDTO> page = exchangeRateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /exchange-rates/count} : count all the exchangeRates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/exchange-rates/count")
    public ResponseEntity<Long> countExchangeRates(ExchangeRateCriteria criteria) {
        log.debug("REST request to count ExchangeRates by criteria: {}", criteria);
        return ResponseEntity.ok().body(exchangeRateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /exchange-rates/:id} : get the "id" exchangeRate.
     *
     * @param id the id of the exchangeRateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the exchangeRateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/exchange-rates/{id}")
    public ResponseEntity<ExchangeRateDTO> getExchangeRate(@PathVariable Long id) {
        log.debug("REST request to get ExchangeRate : {}", id);
        Optional<ExchangeRateDTO> exchangeRateDTO = exchangeRateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(exchangeRateDTO);
    }

    /**
     * {@code DELETE  /exchange-rates/:id} : delete the "id" exchangeRate.
     *
     * @param id the id of the exchangeRateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/exchange-rates/{id}")
    public ResponseEntity<Void> deleteExchangeRate(@PathVariable Long id) {
        log.debug("REST request to delete ExchangeRate : {}", id);
        exchangeRateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/exchange-rates?query=:query} : search for the exchangeRate corresponding
     * to the query.
     *
     * @param query the query of the exchangeRate search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/exchange-rates")
    public ResponseEntity<List<ExchangeRateDTO>> searchExchangeRates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ExchangeRates for query {}", query);
        Page<ExchangeRateDTO> page = exchangeRateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
