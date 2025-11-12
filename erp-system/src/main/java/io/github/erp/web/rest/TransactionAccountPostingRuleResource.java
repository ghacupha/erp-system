package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.repository.TransactionAccountPostingRuleRepository;
import io.github.erp.service.TransactionAccountPostingRuleQueryService;
import io.github.erp.service.TransactionAccountPostingRuleService;
import io.github.erp.service.criteria.TransactionAccountPostingRuleCriteria;
import io.github.erp.service.dto.TransactionAccountPostingRuleDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TransactionAccountPostingRule}.
 */
@RestController
@RequestMapping("/api")
public class TransactionAccountPostingRuleResource {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountPostingRuleResource.class);

    private static final String ENTITY_NAME = "transactionAccountPostingRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionAccountPostingRuleService transactionAccountPostingRuleService;

    private final TransactionAccountPostingRuleRepository transactionAccountPostingRuleRepository;

    private final TransactionAccountPostingRuleQueryService transactionAccountPostingRuleQueryService;

    public TransactionAccountPostingRuleResource(
        TransactionAccountPostingRuleService transactionAccountPostingRuleService,
        TransactionAccountPostingRuleRepository transactionAccountPostingRuleRepository,
        TransactionAccountPostingRuleQueryService transactionAccountPostingRuleQueryService
    ) {
        this.transactionAccountPostingRuleService = transactionAccountPostingRuleService;
        this.transactionAccountPostingRuleRepository = transactionAccountPostingRuleRepository;
        this.transactionAccountPostingRuleQueryService = transactionAccountPostingRuleQueryService;
    }

    /**
     * {@code POST  /transaction-account-posting-rules} : Create a new transactionAccountPostingRule.
     *
     * @param transactionAccountPostingRuleDTO the transactionAccountPostingRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionAccountPostingRuleDTO, or with status {@code 400 (Bad Request)} if the transactionAccountPostingRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-account-posting-rules")
    public ResponseEntity<TransactionAccountPostingRuleDTO> createTransactionAccountPostingRule(
        @Valid @RequestBody TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TransactionAccountPostingRule : {}", transactionAccountPostingRuleDTO);
        if (transactionAccountPostingRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionAccountPostingRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionAccountPostingRuleDTO result = transactionAccountPostingRuleService.save(transactionAccountPostingRuleDTO);
        return ResponseEntity
            .created(new URI("/api/transaction-account-posting-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-account-posting-rules/:id} : Updates an existing transactionAccountPostingRule.
     *
     * @param id the id of the transactionAccountPostingRuleDTO to save.
     * @param transactionAccountPostingRuleDTO the transactionAccountPostingRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountPostingRuleDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountPostingRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountPostingRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-account-posting-rules/{id}")
    public ResponseEntity<TransactionAccountPostingRuleDTO> updateTransactionAccountPostingRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionAccountPostingRule : {}, {}", id, transactionAccountPostingRuleDTO);
        if (transactionAccountPostingRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountPostingRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountPostingRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionAccountPostingRuleDTO result = transactionAccountPostingRuleService.save(transactionAccountPostingRuleDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionAccountPostingRuleDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-account-posting-rules/:id} : Partial updates given fields of an existing transactionAccountPostingRule, field will ignore if it is null
     *
     * @param id the id of the transactionAccountPostingRuleDTO to save.
     * @param transactionAccountPostingRuleDTO the transactionAccountPostingRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountPostingRuleDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountPostingRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionAccountPostingRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountPostingRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-account-posting-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionAccountPostingRuleDTO> partialUpdateTransactionAccountPostingRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionAccountPostingRuleDTO transactionAccountPostingRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionAccountPostingRule partially : {}, {}", id, transactionAccountPostingRuleDTO);
        if (transactionAccountPostingRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountPostingRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountPostingRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionAccountPostingRuleDTO> result = transactionAccountPostingRuleService.partialUpdate(
            transactionAccountPostingRuleDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionAccountPostingRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-account-posting-rules} : get all the transactionAccountPostingRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionAccountPostingRules in body.
     */
    @GetMapping("/transaction-account-posting-rules")
    public ResponseEntity<List<TransactionAccountPostingRuleDTO>> getAllTransactionAccountPostingRules(
        TransactionAccountPostingRuleCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TransactionAccountPostingRules by criteria: {}", criteria);
        Page<TransactionAccountPostingRuleDTO> page = transactionAccountPostingRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-account-posting-rules/count} : count all the transactionAccountPostingRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transaction-account-posting-rules/count")
    public ResponseEntity<Long> countTransactionAccountPostingRules(TransactionAccountPostingRuleCriteria criteria) {
        log.debug("REST request to count TransactionAccountPostingRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionAccountPostingRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-account-posting-rules/:id} : get the "id" transactionAccountPostingRule.
     *
     * @param id the id of the transactionAccountPostingRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionAccountPostingRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-account-posting-rules/{id}")
    public ResponseEntity<TransactionAccountPostingRuleDTO> getTransactionAccountPostingRule(@PathVariable Long id) {
        log.debug("REST request to get TransactionAccountPostingRule : {}", id);
        Optional<TransactionAccountPostingRuleDTO> transactionAccountPostingRuleDTO = transactionAccountPostingRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionAccountPostingRuleDTO);
    }

    /**
     * {@code DELETE  /transaction-account-posting-rules/:id} : delete the "id" transactionAccountPostingRule.
     *
     * @param id the id of the transactionAccountPostingRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-account-posting-rules/{id}")
    public ResponseEntity<Void> deleteTransactionAccountPostingRule(@PathVariable Long id) {
        log.debug("REST request to delete TransactionAccountPostingRule : {}", id);
        transactionAccountPostingRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/transaction-account-posting-rules?query=:query} : search for the transactionAccountPostingRule corresponding
     * to the query.
     *
     * @param query the query of the transactionAccountPostingRule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/transaction-account-posting-rules")
    public ResponseEntity<List<TransactionAccountPostingRuleDTO>> searchTransactionAccountPostingRules(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of TransactionAccountPostingRules for query {}", query);
        Page<TransactionAccountPostingRuleDTO> page = transactionAccountPostingRuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
