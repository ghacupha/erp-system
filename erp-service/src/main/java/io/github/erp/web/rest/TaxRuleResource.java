package io.github.erp.web.rest;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import io.github.erp.service.TaxRuleService;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import io.github.erp.service.dto.TaxRuleDTO;
import io.github.erp.service.dto.TaxRuleCriteria;
import io.github.erp.service.TaxRuleQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.github.erp.domain.TaxRule}.
 */
@RestController
@RequestMapping("/api")
public class TaxRuleResource {

    private final Logger log = LoggerFactory.getLogger(TaxRuleResource.class);

    private static final String ENTITY_NAME = "erpServiceTaxRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TaxRuleService taxRuleService;

    private final TaxRuleQueryService taxRuleQueryService;

    public TaxRuleResource(TaxRuleService taxRuleService, TaxRuleQueryService taxRuleQueryService) {
        this.taxRuleService = taxRuleService;
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
    public ResponseEntity<TaxRuleDTO> createTaxRule(@Valid @RequestBody TaxRuleDTO taxRuleDTO) throws URISyntaxException {
        log.debug("REST request to save TaxRule : {}", taxRuleDTO);
        if (taxRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new taxRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TaxRuleDTO result = taxRuleService.save(taxRuleDTO);
        return ResponseEntity.created(new URI("/api/tax-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tax-rules} : Updates an existing taxRule.
     *
     * @param taxRuleDTO the taxRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated taxRuleDTO,
     * or with status {@code 400 (Bad Request)} if the taxRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the taxRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tax-rules")
    public ResponseEntity<TaxRuleDTO> updateTaxRule(@Valid @RequestBody TaxRuleDTO taxRuleDTO) throws URISyntaxException {
        log.debug("REST request to update TaxRule : {}", taxRuleDTO);
        if (taxRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        TaxRuleDTO result = taxRuleService.save(taxRuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, taxRuleDTO.getId().toString()))
            .body(result);
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
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
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
