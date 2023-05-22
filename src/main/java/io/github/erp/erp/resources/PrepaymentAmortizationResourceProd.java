package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.2
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
import io.github.erp.repository.PrepaymentAmortizationRepository;
import io.github.erp.service.PrepaymentAmortizationQueryService;
import io.github.erp.service.PrepaymentAmortizationService;
import io.github.erp.service.criteria.PrepaymentAmortizationCriteria;
import io.github.erp.service.dto.PrepaymentAmortizationDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

/**
 * REST controller for managing {@link io.github.erp.domain.PrepaymentAmortization}.
 */
@RestController("PrepaymentAmortizationResourceProd")
@RequestMapping("/api/prepayments")
public class PrepaymentAmortizationResourceProd {

    private final Logger log = LoggerFactory.getLogger(PrepaymentAmortizationResourceProd.class);

    private static final String ENTITY_NAME = "prepaymentAmortization";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrepaymentAmortizationService prepaymentAmortizationService;

    private final PrepaymentAmortizationRepository prepaymentAmortizationRepository;

    private final PrepaymentAmortizationQueryService prepaymentAmortizationQueryService;

    public PrepaymentAmortizationResourceProd(
        PrepaymentAmortizationService prepaymentAmortizationService,
        PrepaymentAmortizationRepository prepaymentAmortizationRepository,
        PrepaymentAmortizationQueryService prepaymentAmortizationQueryService
    ) {
        this.prepaymentAmortizationService = prepaymentAmortizationService;
        this.prepaymentAmortizationRepository = prepaymentAmortizationRepository;
        this.prepaymentAmortizationQueryService = prepaymentAmortizationQueryService;
    }

    /**
     * {@code POST  /prepayment-amortizations} : Create a new prepaymentAmortization.
     *
     * @param prepaymentAmortizationDTO the prepaymentAmortizationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prepaymentAmortizationDTO, or with status {@code 400 (Bad Request)} if the prepaymentAmortization has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prepayment-amortizations")
    public ResponseEntity<PrepaymentAmortizationDTO> createPrepaymentAmortization(
        @RequestBody PrepaymentAmortizationDTO prepaymentAmortizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PrepaymentAmortization : {}", prepaymentAmortizationDTO);
        if (prepaymentAmortizationDTO.getId() != null) {
            throw new BadRequestAlertException("A new prepaymentAmortization cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrepaymentAmortizationDTO result = prepaymentAmortizationService.save(prepaymentAmortizationDTO);
        return ResponseEntity
            .created(new URI("/api/prepayment-amortizations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prepayment-amortizations/:id} : Updates an existing prepaymentAmortization.
     *
     * @param id the id of the prepaymentAmortizationDTO to save.
     * @param prepaymentAmortizationDTO the prepaymentAmortizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentAmortizationDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentAmortizationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentAmortizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prepayment-amortizations/{id}")
    public ResponseEntity<PrepaymentAmortizationDTO> updatePrepaymentAmortization(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PrepaymentAmortizationDTO prepaymentAmortizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrepaymentAmortization : {}, {}", id, prepaymentAmortizationDTO);
        if (prepaymentAmortizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentAmortizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentAmortizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrepaymentAmortizationDTO result = prepaymentAmortizationService.save(prepaymentAmortizationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prepaymentAmortizationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prepayment-amortizations/:id} : Partial updates given fields of an existing prepaymentAmortization, field will ignore if it is null
     *
     * @param id the id of the prepaymentAmortizationDTO to save.
     * @param prepaymentAmortizationDTO the prepaymentAmortizationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentAmortizationDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentAmortizationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prepaymentAmortizationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentAmortizationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prepayment-amortizations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrepaymentAmortizationDTO> partialUpdatePrepaymentAmortization(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PrepaymentAmortizationDTO prepaymentAmortizationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrepaymentAmortization partially : {}, {}", id, prepaymentAmortizationDTO);
        if (prepaymentAmortizationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentAmortizationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentAmortizationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrepaymentAmortizationDTO> result = prepaymentAmortizationService.partialUpdate(prepaymentAmortizationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prepaymentAmortizationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prepayment-amortizations} : get all the prepaymentAmortizations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentAmortizations in body.
     */
    @GetMapping("/prepayment-amortizations")
    public ResponseEntity<List<PrepaymentAmortizationDTO>> getAllPrepaymentAmortizations(
        PrepaymentAmortizationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get PrepaymentAmortizations by criteria: {}", criteria);
        Page<PrepaymentAmortizationDTO> page = prepaymentAmortizationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-amortizations/count} : count all the prepaymentAmortizations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-amortizations/count")
    public ResponseEntity<Long> countPrepaymentAmortizations(PrepaymentAmortizationCriteria criteria) {
        log.debug("REST request to count PrepaymentAmortizations by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentAmortizationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-amortizations/:id} : get the "id" prepaymentAmortization.
     *
     * @param id the id of the prepaymentAmortizationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentAmortizationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-amortizations/{id}")
    public ResponseEntity<PrepaymentAmortizationDTO> getPrepaymentAmortization(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentAmortization : {}", id);
        Optional<PrepaymentAmortizationDTO> prepaymentAmortizationDTO = prepaymentAmortizationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaymentAmortizationDTO);
    }

    /**
     * {@code DELETE  /prepayment-amortizations/:id} : delete the "id" prepaymentAmortization.
     *
     * @param id the id of the prepaymentAmortizationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prepayment-amortizations/{id}")
    public ResponseEntity<Void> deletePrepaymentAmortization(@PathVariable Long id) {
        log.debug("REST request to delete PrepaymentAmortization : {}", id);
        prepaymentAmortizationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/prepayment-amortizations?query=:query} : search for the prepaymentAmortization corresponding
     * to the query.
     *
     * @param query the query of the prepaymentAmortization search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-amortizations")
    public ResponseEntity<List<PrepaymentAmortizationDTO>> searchPrepaymentAmortizations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PrepaymentAmortizations for query {}", query);
        Page<PrepaymentAmortizationDTO> page = prepaymentAmortizationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
