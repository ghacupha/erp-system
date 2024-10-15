package io.github.erp.web.rest;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.TALeaseRepaymentRuleRepository;
import io.github.erp.service.TALeaseRepaymentRuleQueryService;
import io.github.erp.service.TALeaseRepaymentRuleService;
import io.github.erp.service.criteria.TALeaseRepaymentRuleCriteria;
import io.github.erp.service.dto.TALeaseRepaymentRuleDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TALeaseRepaymentRule}.
 */
@RestController
@RequestMapping("/api")
public class TALeaseRepaymentRuleResource {

    private final Logger log = LoggerFactory.getLogger(TALeaseRepaymentRuleResource.class);

    private static final String ENTITY_NAME = "accountingTaLeaseRepaymentRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TALeaseRepaymentRuleService tALeaseRepaymentRuleService;

    private final TALeaseRepaymentRuleRepository tALeaseRepaymentRuleRepository;

    private final TALeaseRepaymentRuleQueryService tALeaseRepaymentRuleQueryService;

    public TALeaseRepaymentRuleResource(
        TALeaseRepaymentRuleService tALeaseRepaymentRuleService,
        TALeaseRepaymentRuleRepository tALeaseRepaymentRuleRepository,
        TALeaseRepaymentRuleQueryService tALeaseRepaymentRuleQueryService
    ) {
        this.tALeaseRepaymentRuleService = tALeaseRepaymentRuleService;
        this.tALeaseRepaymentRuleRepository = tALeaseRepaymentRuleRepository;
        this.tALeaseRepaymentRuleQueryService = tALeaseRepaymentRuleQueryService;
    }

    /**
     * {@code POST  /ta-lease-repayment-rules} : Create a new tALeaseRepaymentRule.
     *
     * @param tALeaseRepaymentRuleDTO the tALeaseRepaymentRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tALeaseRepaymentRuleDTO, or with status {@code 400 (Bad Request)} if the tALeaseRepaymentRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ta-lease-repayment-rules")
    public ResponseEntity<TALeaseRepaymentRuleDTO> createTALeaseRepaymentRule(
        @Valid @RequestBody TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TALeaseRepaymentRule : {}", tALeaseRepaymentRuleDTO);
        if (tALeaseRepaymentRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new tALeaseRepaymentRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TALeaseRepaymentRuleDTO result = tALeaseRepaymentRuleService.save(tALeaseRepaymentRuleDTO);
        return ResponseEntity
            .created(new URI("/api/ta-lease-repayment-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ta-lease-repayment-rules/:id} : Updates an existing tALeaseRepaymentRule.
     *
     * @param id the id of the tALeaseRepaymentRuleDTO to save.
     * @param tALeaseRepaymentRuleDTO the tALeaseRepaymentRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tALeaseRepaymentRuleDTO,
     * or with status {@code 400 (Bad Request)} if the tALeaseRepaymentRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tALeaseRepaymentRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ta-lease-repayment-rules/{id}")
    public ResponseEntity<TALeaseRepaymentRuleDTO> updateTALeaseRepaymentRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TALeaseRepaymentRule : {}, {}", id, tALeaseRepaymentRuleDTO);
        if (tALeaseRepaymentRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tALeaseRepaymentRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tALeaseRepaymentRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TALeaseRepaymentRuleDTO result = tALeaseRepaymentRuleService.save(tALeaseRepaymentRuleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tALeaseRepaymentRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ta-lease-repayment-rules/:id} : Partial updates given fields of an existing tALeaseRepaymentRule, field will ignore if it is null
     *
     * @param id the id of the tALeaseRepaymentRuleDTO to save.
     * @param tALeaseRepaymentRuleDTO the tALeaseRepaymentRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tALeaseRepaymentRuleDTO,
     * or with status {@code 400 (Bad Request)} if the tALeaseRepaymentRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tALeaseRepaymentRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tALeaseRepaymentRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ta-lease-repayment-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TALeaseRepaymentRuleDTO> partialUpdateTALeaseRepaymentRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TALeaseRepaymentRuleDTO tALeaseRepaymentRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TALeaseRepaymentRule partially : {}, {}", id, tALeaseRepaymentRuleDTO);
        if (tALeaseRepaymentRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tALeaseRepaymentRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tALeaseRepaymentRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TALeaseRepaymentRuleDTO> result = tALeaseRepaymentRuleService.partialUpdate(tALeaseRepaymentRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tALeaseRepaymentRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ta-lease-repayment-rules} : get all the tALeaseRepaymentRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tALeaseRepaymentRules in body.
     */
    @GetMapping("/ta-lease-repayment-rules")
    public ResponseEntity<List<TALeaseRepaymentRuleDTO>> getAllTALeaseRepaymentRules(
        TALeaseRepaymentRuleCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TALeaseRepaymentRules by criteria: {}", criteria);
        Page<TALeaseRepaymentRuleDTO> page = tALeaseRepaymentRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ta-lease-repayment-rules/count} : count all the tALeaseRepaymentRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ta-lease-repayment-rules/count")
    public ResponseEntity<Long> countTALeaseRepaymentRules(TALeaseRepaymentRuleCriteria criteria) {
        log.debug("REST request to count TALeaseRepaymentRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(tALeaseRepaymentRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ta-lease-repayment-rules/:id} : get the "id" tALeaseRepaymentRule.
     *
     * @param id the id of the tALeaseRepaymentRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tALeaseRepaymentRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ta-lease-repayment-rules/{id}")
    public ResponseEntity<TALeaseRepaymentRuleDTO> getTALeaseRepaymentRule(@PathVariable Long id) {
        log.debug("REST request to get TALeaseRepaymentRule : {}", id);
        Optional<TALeaseRepaymentRuleDTO> tALeaseRepaymentRuleDTO = tALeaseRepaymentRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tALeaseRepaymentRuleDTO);
    }

    /**
     * {@code DELETE  /ta-lease-repayment-rules/:id} : delete the "id" tALeaseRepaymentRule.
     *
     * @param id the id of the tALeaseRepaymentRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ta-lease-repayment-rules/{id}")
    public ResponseEntity<Void> deleteTALeaseRepaymentRule(@PathVariable Long id) {
        log.debug("REST request to delete TALeaseRepaymentRule : {}", id);
        tALeaseRepaymentRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ta-lease-repayment-rules?query=:query} : search for the tALeaseRepaymentRule corresponding
     * to the query.
     *
     * @param query the query of the tALeaseRepaymentRule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ta-lease-repayment-rules")
    public ResponseEntity<List<TALeaseRepaymentRuleDTO>> searchTALeaseRepaymentRules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TALeaseRepaymentRules for query {}", query);
        Page<TALeaseRepaymentRuleDTO> page = tALeaseRepaymentRuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
