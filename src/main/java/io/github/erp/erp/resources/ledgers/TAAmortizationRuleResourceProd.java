package io.github.erp.erp.resources.ledgers;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.repository.TAAmortizationRuleRepository;
import io.github.erp.service.TAAmortizationRuleQueryService;
import io.github.erp.service.TAAmortizationRuleService;
import io.github.erp.service.criteria.TAAmortizationRuleCriteria;
import io.github.erp.service.dto.TAAmortizationRuleDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TAAmortizationRule}.
 */
@RestController
@RequestMapping("/api/accounts")
public class TAAmortizationRuleResourceProd {

    private final Logger log = LoggerFactory.getLogger(TAAmortizationRuleResourceProd.class);

    private static final String ENTITY_NAME = "tAAmortizationRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TAAmortizationRuleService tAAmortizationRuleService;

    private final TAAmortizationRuleRepository tAAmortizationRuleRepository;

    private final TAAmortizationRuleQueryService tAAmortizationRuleQueryService;

    public TAAmortizationRuleResourceProd(
        TAAmortizationRuleService tAAmortizationRuleService,
        TAAmortizationRuleRepository tAAmortizationRuleRepository,
        TAAmortizationRuleQueryService tAAmortizationRuleQueryService
    ) {
        this.tAAmortizationRuleService = tAAmortizationRuleService;
        this.tAAmortizationRuleRepository = tAAmortizationRuleRepository;
        this.tAAmortizationRuleQueryService = tAAmortizationRuleQueryService;
    }

    /**
     * {@code POST  /ta-amortization-rules} : Create a new tAAmortizationRule.
     *
     * @param tAAmortizationRuleDTO the tAAmortizationRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tAAmortizationRuleDTO, or with status {@code 400 (Bad Request)} if the tAAmortizationRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ta-amortization-rules")
    public ResponseEntity<TAAmortizationRuleDTO> createTAAmortizationRule(@Valid @RequestBody TAAmortizationRuleDTO tAAmortizationRuleDTO)
        throws URISyntaxException {
        log.debug("REST request to save TAAmortizationRule : {}", tAAmortizationRuleDTO);
        if (tAAmortizationRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new tAAmortizationRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TAAmortizationRuleDTO result = tAAmortizationRuleService.save(tAAmortizationRuleDTO);
        return ResponseEntity
            .created(new URI("/api/ta-amortization-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ta-amortization-rules/:id} : Updates an existing tAAmortizationRule.
     *
     * @param id the id of the tAAmortizationRuleDTO to save.
     * @param tAAmortizationRuleDTO the tAAmortizationRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tAAmortizationRuleDTO,
     * or with status {@code 400 (Bad Request)} if the tAAmortizationRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tAAmortizationRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ta-amortization-rules/{id}")
    public ResponseEntity<TAAmortizationRuleDTO> updateTAAmortizationRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TAAmortizationRuleDTO tAAmortizationRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TAAmortizationRule : {}, {}", id, tAAmortizationRuleDTO);
        if (tAAmortizationRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tAAmortizationRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tAAmortizationRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TAAmortizationRuleDTO result = tAAmortizationRuleService.save(tAAmortizationRuleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tAAmortizationRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ta-amortization-rules/:id} : Partial updates given fields of an existing tAAmortizationRule, field will ignore if it is null
     *
     * @param id the id of the tAAmortizationRuleDTO to save.
     * @param tAAmortizationRuleDTO the tAAmortizationRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tAAmortizationRuleDTO,
     * or with status {@code 400 (Bad Request)} if the tAAmortizationRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tAAmortizationRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tAAmortizationRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ta-amortization-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TAAmortizationRuleDTO> partialUpdateTAAmortizationRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TAAmortizationRuleDTO tAAmortizationRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TAAmortizationRule partially : {}, {}", id, tAAmortizationRuleDTO);
        if (tAAmortizationRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tAAmortizationRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tAAmortizationRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TAAmortizationRuleDTO> result = tAAmortizationRuleService.partialUpdate(tAAmortizationRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tAAmortizationRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ta-amortization-rules} : get all the tAAmortizationRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tAAmortizationRules in body.
     */
    @GetMapping("/ta-amortization-rules")
    public ResponseEntity<List<TAAmortizationRuleDTO>> getAllTAAmortizationRules(TAAmortizationRuleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TAAmortizationRules by criteria: {}", criteria);
        Page<TAAmortizationRuleDTO> page = tAAmortizationRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ta-amortization-rules/count} : count all the tAAmortizationRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ta-amortization-rules/count")
    public ResponseEntity<Long> countTAAmortizationRules(TAAmortizationRuleCriteria criteria) {
        log.debug("REST request to count TAAmortizationRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(tAAmortizationRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ta-amortization-rules/:id} : get the "id" tAAmortizationRule.
     *
     * @param id the id of the tAAmortizationRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tAAmortizationRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ta-amortization-rules/{id}")
    public ResponseEntity<TAAmortizationRuleDTO> getTAAmortizationRule(@PathVariable Long id) {
        log.debug("REST request to get TAAmortizationRule : {}", id);
        Optional<TAAmortizationRuleDTO> tAAmortizationRuleDTO = tAAmortizationRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tAAmortizationRuleDTO);
    }

    /**
     * {@code DELETE  /ta-amortization-rules/:id} : delete the "id" tAAmortizationRule.
     *
     * @param id the id of the tAAmortizationRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ta-amortization-rules/{id}")
    public ResponseEntity<Void> deleteTAAmortizationRule(@PathVariable Long id) {
        log.debug("REST request to delete TAAmortizationRule : {}", id);
        tAAmortizationRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ta-amortization-rules?query=:query} : search for the tAAmortizationRule corresponding
     * to the query.
     *
     * @param query the query of the tAAmortizationRule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ta-amortization-rules")
    public ResponseEntity<List<TAAmortizationRuleDTO>> searchTAAmortizationRules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TAAmortizationRules for query {}", query);
        Page<TAAmortizationRuleDTO> page = tAAmortizationRuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
