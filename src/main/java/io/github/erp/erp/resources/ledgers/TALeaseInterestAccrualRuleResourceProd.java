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

import io.github.erp.internal.repository.InternalTALeaseInterestAccrualRuleRepository;
import io.github.erp.internal.service.ledgers.InternalTALeaseInterestAccrualRuleService;
import io.github.erp.repository.TALeaseInterestAccrualRuleRepository;
import io.github.erp.service.TALeaseInterestAccrualRuleQueryService;
import io.github.erp.service.TALeaseInterestAccrualRuleService;
import io.github.erp.service.criteria.TALeaseInterestAccrualRuleCriteria;
import io.github.erp.service.dto.TALeaseInterestAccrualRuleDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TALeaseInterestAccrualRule}.
 */
@RestController
@RequestMapping("/api/accounts")
public class TALeaseInterestAccrualRuleResourceProd {

    private final Logger log = LoggerFactory.getLogger(TALeaseInterestAccrualRuleResourceProd.class);

    private static final String ENTITY_NAME = "tALeaseInterestAccrualRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalTALeaseInterestAccrualRuleService tALeaseInterestAccrualRuleService;

    private final InternalTALeaseInterestAccrualRuleRepository tALeaseInterestAccrualRuleRepository;

    private final TALeaseInterestAccrualRuleQueryService tALeaseInterestAccrualRuleQueryService;

    public TALeaseInterestAccrualRuleResourceProd(
        InternalTALeaseInterestAccrualRuleService tALeaseInterestAccrualRuleService,
        InternalTALeaseInterestAccrualRuleRepository tALeaseInterestAccrualRuleRepository,
        TALeaseInterestAccrualRuleQueryService tALeaseInterestAccrualRuleQueryService
    ) {
        this.tALeaseInterestAccrualRuleService = tALeaseInterestAccrualRuleService;
        this.tALeaseInterestAccrualRuleRepository = tALeaseInterestAccrualRuleRepository;
        this.tALeaseInterestAccrualRuleQueryService = tALeaseInterestAccrualRuleQueryService;
    }

    /**
     * {@code POST  /ta-lease-interest-accrual-rules} : Create a new tALeaseInterestAccrualRule.
     *
     * @param tALeaseInterestAccrualRuleDTO the tALeaseInterestAccrualRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tALeaseInterestAccrualRuleDTO, or with status {@code 400 (Bad Request)} if the tALeaseInterestAccrualRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ta-lease-interest-accrual-rules")
    public ResponseEntity<TALeaseInterestAccrualRuleDTO> createTALeaseInterestAccrualRule(
        @Valid @RequestBody TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TALeaseInterestAccrualRule : {}", tALeaseInterestAccrualRuleDTO);
        if (tALeaseInterestAccrualRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new tALeaseInterestAccrualRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TALeaseInterestAccrualRuleDTO result = tALeaseInterestAccrualRuleService.save(tALeaseInterestAccrualRuleDTO);
        return ResponseEntity
            .created(new URI("/api/ta-lease-interest-accrual-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ta-lease-interest-accrual-rules/:id} : Updates an existing tALeaseInterestAccrualRule.
     *
     * @param id the id of the tALeaseInterestAccrualRuleDTO to save.
     * @param tALeaseInterestAccrualRuleDTO the tALeaseInterestAccrualRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tALeaseInterestAccrualRuleDTO,
     * or with status {@code 400 (Bad Request)} if the tALeaseInterestAccrualRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tALeaseInterestAccrualRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ta-lease-interest-accrual-rules/{id}")
    public ResponseEntity<TALeaseInterestAccrualRuleDTO> updateTALeaseInterestAccrualRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TALeaseInterestAccrualRule : {}, {}", id, tALeaseInterestAccrualRuleDTO);
        if (tALeaseInterestAccrualRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tALeaseInterestAccrualRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tALeaseInterestAccrualRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TALeaseInterestAccrualRuleDTO result = tALeaseInterestAccrualRuleService.save(tALeaseInterestAccrualRuleDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tALeaseInterestAccrualRuleDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /ta-lease-interest-accrual-rules/:id} : Partial updates given fields of an existing tALeaseInterestAccrualRule, field will ignore if it is null
     *
     * @param id the id of the tALeaseInterestAccrualRuleDTO to save.
     * @param tALeaseInterestAccrualRuleDTO the tALeaseInterestAccrualRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tALeaseInterestAccrualRuleDTO,
     * or with status {@code 400 (Bad Request)} if the tALeaseInterestAccrualRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tALeaseInterestAccrualRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tALeaseInterestAccrualRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ta-lease-interest-accrual-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TALeaseInterestAccrualRuleDTO> partialUpdateTALeaseInterestAccrualRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TALeaseInterestAccrualRuleDTO tALeaseInterestAccrualRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TALeaseInterestAccrualRule partially : {}, {}", id, tALeaseInterestAccrualRuleDTO);
        if (tALeaseInterestAccrualRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tALeaseInterestAccrualRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tALeaseInterestAccrualRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TALeaseInterestAccrualRuleDTO> result = tALeaseInterestAccrualRuleService.partialUpdate(tALeaseInterestAccrualRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tALeaseInterestAccrualRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ta-lease-interest-accrual-rules} : get all the tALeaseInterestAccrualRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tALeaseInterestAccrualRules in body.
     */
    @GetMapping("/ta-lease-interest-accrual-rules")
    public ResponseEntity<List<TALeaseInterestAccrualRuleDTO>> getAllTALeaseInterestAccrualRules(
        TALeaseInterestAccrualRuleCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TALeaseInterestAccrualRules by criteria: {}", criteria);
        Page<TALeaseInterestAccrualRuleDTO> page = tALeaseInterestAccrualRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ta-lease-interest-accrual-rules/count} : count all the tALeaseInterestAccrualRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ta-lease-interest-accrual-rules/count")
    public ResponseEntity<Long> countTALeaseInterestAccrualRules(TALeaseInterestAccrualRuleCriteria criteria) {
        log.debug("REST request to count TALeaseInterestAccrualRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(tALeaseInterestAccrualRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ta-lease-interest-accrual-rules/:id} : get the "id" tALeaseInterestAccrualRule.
     *
     * @param id the id of the tALeaseInterestAccrualRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tALeaseInterestAccrualRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ta-lease-interest-accrual-rules/{id}")
    public ResponseEntity<TALeaseInterestAccrualRuleDTO> getTALeaseInterestAccrualRule(@PathVariable Long id) {
        log.debug("REST request to get TALeaseInterestAccrualRule : {}", id);
        Optional<TALeaseInterestAccrualRuleDTO> tALeaseInterestAccrualRuleDTO = tALeaseInterestAccrualRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tALeaseInterestAccrualRuleDTO);
    }

    /**
     * {@code DELETE  /ta-lease-interest-accrual-rules/:id} : delete the "id" tALeaseInterestAccrualRule.
     *
     * @param id the id of the tALeaseInterestAccrualRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ta-lease-interest-accrual-rules/{id}")
    public ResponseEntity<Void> deleteTALeaseInterestAccrualRule(@PathVariable Long id) {
        log.debug("REST request to delete TALeaseInterestAccrualRule : {}", id);
        tALeaseInterestAccrualRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ta-lease-interest-accrual-rules?query=:query} : search for the tALeaseInterestAccrualRule corresponding
     * to the query.
     *
     * @param query the query of the tALeaseInterestAccrualRule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ta-lease-interest-accrual-rules")
    public ResponseEntity<List<TALeaseInterestAccrualRuleDTO>> searchTALeaseInterestAccrualRules(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of TALeaseInterestAccrualRules for query {}", query);
        Page<TALeaseInterestAccrualRuleDTO> page = tALeaseInterestAccrualRuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
