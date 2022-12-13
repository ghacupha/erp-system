package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.7-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.repository.TaxRuleRepository;
import io.github.erp.service.TaxRuleQueryService;
import io.github.erp.service.TaxRuleService;
import io.github.erp.service.criteria.TaxRuleCriteria;
import io.github.erp.service.dto.TaxRuleDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TaxRule}.
 */
@RestController
@RequestMapping("/api/taxes")
public class TaxRuleResource {

    private final Logger log = LoggerFactory.getLogger(TaxRuleResource.class);

    private static final String ENTITY_NAME = "paymentsTaxRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxRuleService taxRuleService;

    private final TaxRuleRepository taxRuleRepository;

    private final TaxRuleQueryService taxRuleQueryService;

    public TaxRuleResource(TaxRuleService taxRuleService, TaxRuleRepository taxRuleRepository, TaxRuleQueryService taxRuleQueryService) {
        this.taxRuleService = taxRuleService;
        this.taxRuleRepository = taxRuleRepository;
        this.taxRuleQueryService = taxRuleQueryService;
    }

    /**
     * {@code POST  /tax-rules} : Create a new taxRule.
     *
     * @param taxRuleDTO the taxRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new taxRuleDTO, or with status {@code 400 (Bad Request)} if the taxRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tax-rules")
    public ResponseEntity<TaxRuleDTO> createTaxRule(@RequestBody TaxRuleDTO taxRuleDTO) throws URISyntaxException {
        log.debug("REST request to save TaxRule : {}", taxRuleDTO);
        if (taxRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxRuleDTO result = taxRuleService.save(taxRuleDTO);
        return ResponseEntity
            .created(new URI("/api/tax-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tax-rules/:id} : Updates an existing taxRule.
     *
     * @param id the id of the taxRuleDTO to save.
     * @param taxRuleDTO the taxRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxRuleDTO,
     * or with status {@code 400 (Bad Request)} if the taxRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tax-rules/{id}")
    public ResponseEntity<TaxRuleDTO> updateTaxRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaxRuleDTO taxRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TaxRule : {}, {}", id, taxRuleDTO);
        if (taxRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TaxRuleDTO result = taxRuleService.save(taxRuleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taxRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tax-rules/:id} : Partial updates given fields of an existing taxRule, field will ignore if it is null
     *
     * @param id the id of the taxRuleDTO to save.
     * @param taxRuleDTO the taxRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxRuleDTO,
     * or with status {@code 400 (Bad Request)} if the taxRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the taxRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the taxRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tax-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TaxRuleDTO> partialUpdateTaxRule(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody TaxRuleDTO taxRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TaxRule partially : {}, {}", id, taxRuleDTO);
        if (taxRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, taxRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!taxRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TaxRuleDTO> result = taxRuleService.partialUpdate(taxRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taxRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tax-rules} : get all the taxRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of taxRules in body.
     */
    @GetMapping("/tax-rules")
    public ResponseEntity<List<TaxRuleDTO>> getAllTaxRules(TaxRuleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TaxRules by criteria: {}", criteria);
        Page<TaxRuleDTO> page = taxRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tax-rules/count} : count all the taxRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/tax-rules/count")
    public ResponseEntity<Long> countTaxRules(TaxRuleCriteria criteria) {
        log.debug("REST request to count TaxRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(taxRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tax-rules/:id} : get the "id" taxRule.
     *
     * @param id the id of the taxRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the taxRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tax-rules/{id}")
    public ResponseEntity<TaxRuleDTO> getTaxRule(@PathVariable Long id) {
        log.debug("REST request to get TaxRule : {}", id);
        Optional<TaxRuleDTO> taxRuleDTO = taxRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(taxRuleDTO);
    }

    /**
     * {@code DELETE  /tax-rules/:id} : delete the "id" taxRule.
     *
     * @param id the id of the taxRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tax-rules/{id}")
    public ResponseEntity<Void> deleteTaxRule(@PathVariable Long id) {
        log.debug("REST request to delete TaxRule : {}", id);
        taxRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/tax-rules?query=:query} : search for the taxRule corresponding
     * to the query.
     *
     * @param query the query of the taxRule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/tax-rules")
    public ResponseEntity<List<TaxRuleDTO>> searchTaxRules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TaxRules for query {}", query);
        Page<TaxRuleDTO> page = taxRuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
