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

import io.github.erp.repository.TAInterestPaidTransferRuleRepository;
import io.github.erp.service.TAInterestPaidTransferRuleQueryService;
import io.github.erp.service.TAInterestPaidTransferRuleService;
import io.github.erp.service.criteria.TAInterestPaidTransferRuleCriteria;
import io.github.erp.service.dto.TAInterestPaidTransferRuleDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TAInterestPaidTransferRule}.
 */
@RestController
@RequestMapping("/api")
public class TAInterestPaidTransferRuleResource {

    private final Logger log = LoggerFactory.getLogger(TAInterestPaidTransferRuleResource.class);

    private static final String ENTITY_NAME = "accountingTaInterestPaidTransferRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TAInterestPaidTransferRuleService tAInterestPaidTransferRuleService;

    private final TAInterestPaidTransferRuleRepository tAInterestPaidTransferRuleRepository;

    private final TAInterestPaidTransferRuleQueryService tAInterestPaidTransferRuleQueryService;

    public TAInterestPaidTransferRuleResource(
        TAInterestPaidTransferRuleService tAInterestPaidTransferRuleService,
        TAInterestPaidTransferRuleRepository tAInterestPaidTransferRuleRepository,
        TAInterestPaidTransferRuleQueryService tAInterestPaidTransferRuleQueryService
    ) {
        this.tAInterestPaidTransferRuleService = tAInterestPaidTransferRuleService;
        this.tAInterestPaidTransferRuleRepository = tAInterestPaidTransferRuleRepository;
        this.tAInterestPaidTransferRuleQueryService = tAInterestPaidTransferRuleQueryService;
    }

    /**
     * {@code POST  /ta-interest-paid-transfer-rules} : Create a new tAInterestPaidTransferRule.
     *
     * @param tAInterestPaidTransferRuleDTO the tAInterestPaidTransferRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tAInterestPaidTransferRuleDTO, or with status {@code 400 (Bad Request)} if the tAInterestPaidTransferRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ta-interest-paid-transfer-rules")
    public ResponseEntity<TAInterestPaidTransferRuleDTO> createTAInterestPaidTransferRule(
        @Valid @RequestBody TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TAInterestPaidTransferRule : {}", tAInterestPaidTransferRuleDTO);
        if (tAInterestPaidTransferRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new tAInterestPaidTransferRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TAInterestPaidTransferRuleDTO result = tAInterestPaidTransferRuleService.save(tAInterestPaidTransferRuleDTO);
        return ResponseEntity
            .created(new URI("/api/ta-interest-paid-transfer-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ta-interest-paid-transfer-rules/:id} : Updates an existing tAInterestPaidTransferRule.
     *
     * @param id the id of the tAInterestPaidTransferRuleDTO to save.
     * @param tAInterestPaidTransferRuleDTO the tAInterestPaidTransferRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tAInterestPaidTransferRuleDTO,
     * or with status {@code 400 (Bad Request)} if the tAInterestPaidTransferRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tAInterestPaidTransferRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ta-interest-paid-transfer-rules/{id}")
    public ResponseEntity<TAInterestPaidTransferRuleDTO> updateTAInterestPaidTransferRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TAInterestPaidTransferRule : {}, {}", id, tAInterestPaidTransferRuleDTO);
        if (tAInterestPaidTransferRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tAInterestPaidTransferRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tAInterestPaidTransferRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TAInterestPaidTransferRuleDTO result = tAInterestPaidTransferRuleService.save(tAInterestPaidTransferRuleDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tAInterestPaidTransferRuleDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /ta-interest-paid-transfer-rules/:id} : Partial updates given fields of an existing tAInterestPaidTransferRule, field will ignore if it is null
     *
     * @param id the id of the tAInterestPaidTransferRuleDTO to save.
     * @param tAInterestPaidTransferRuleDTO the tAInterestPaidTransferRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tAInterestPaidTransferRuleDTO,
     * or with status {@code 400 (Bad Request)} if the tAInterestPaidTransferRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tAInterestPaidTransferRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tAInterestPaidTransferRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ta-interest-paid-transfer-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TAInterestPaidTransferRuleDTO> partialUpdateTAInterestPaidTransferRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TAInterestPaidTransferRuleDTO tAInterestPaidTransferRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TAInterestPaidTransferRule partially : {}, {}", id, tAInterestPaidTransferRuleDTO);
        if (tAInterestPaidTransferRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tAInterestPaidTransferRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tAInterestPaidTransferRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TAInterestPaidTransferRuleDTO> result = tAInterestPaidTransferRuleService.partialUpdate(tAInterestPaidTransferRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tAInterestPaidTransferRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ta-interest-paid-transfer-rules} : get all the tAInterestPaidTransferRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tAInterestPaidTransferRules in body.
     */
    @GetMapping("/ta-interest-paid-transfer-rules")
    public ResponseEntity<List<TAInterestPaidTransferRuleDTO>> getAllTAInterestPaidTransferRules(
        TAInterestPaidTransferRuleCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TAInterestPaidTransferRules by criteria: {}", criteria);
        Page<TAInterestPaidTransferRuleDTO> page = tAInterestPaidTransferRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ta-interest-paid-transfer-rules/count} : count all the tAInterestPaidTransferRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ta-interest-paid-transfer-rules/count")
    public ResponseEntity<Long> countTAInterestPaidTransferRules(TAInterestPaidTransferRuleCriteria criteria) {
        log.debug("REST request to count TAInterestPaidTransferRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(tAInterestPaidTransferRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ta-interest-paid-transfer-rules/:id} : get the "id" tAInterestPaidTransferRule.
     *
     * @param id the id of the tAInterestPaidTransferRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tAInterestPaidTransferRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ta-interest-paid-transfer-rules/{id}")
    public ResponseEntity<TAInterestPaidTransferRuleDTO> getTAInterestPaidTransferRule(@PathVariable Long id) {
        log.debug("REST request to get TAInterestPaidTransferRule : {}", id);
        Optional<TAInterestPaidTransferRuleDTO> tAInterestPaidTransferRuleDTO = tAInterestPaidTransferRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tAInterestPaidTransferRuleDTO);
    }

    /**
     * {@code DELETE  /ta-interest-paid-transfer-rules/:id} : delete the "id" tAInterestPaidTransferRule.
     *
     * @param id the id of the tAInterestPaidTransferRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ta-interest-paid-transfer-rules/{id}")
    public ResponseEntity<Void> deleteTAInterestPaidTransferRule(@PathVariable Long id) {
        log.debug("REST request to delete TAInterestPaidTransferRule : {}", id);
        tAInterestPaidTransferRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ta-interest-paid-transfer-rules?query=:query} : search for the tAInterestPaidTransferRule corresponding
     * to the query.
     *
     * @param query the query of the tAInterestPaidTransferRule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ta-interest-paid-transfer-rules")
    public ResponseEntity<List<TAInterestPaidTransferRuleDTO>> searchTAInterestPaidTransferRules(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of TAInterestPaidTransferRules for query {}", query);
        Page<TAInterestPaidTransferRuleDTO> page = tAInterestPaidTransferRuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
