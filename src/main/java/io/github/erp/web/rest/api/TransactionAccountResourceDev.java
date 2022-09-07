package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.0.9-SNAPSHOT
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
import io.github.erp.repository.TransactionAccountRepository;
import io.github.erp.service.TransactionAccountQueryService;
import io.github.erp.service.TransactionAccountService;
import io.github.erp.service.criteria.TransactionAccountCriteria;
import io.github.erp.service.dto.TransactionAccountDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TransactionAccount}.
 */
@RestController
@RequestMapping("/api/dev")
public class TransactionAccountResourceDev {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountResourceDev.class);

    private static final String ENTITY_NAME = "transactionAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionAccountService transactionAccountService;

    private final TransactionAccountRepository transactionAccountRepository;

    private final TransactionAccountQueryService transactionAccountQueryService;

    public TransactionAccountResourceDev(
        TransactionAccountService transactionAccountService,
        TransactionAccountRepository transactionAccountRepository,
        TransactionAccountQueryService transactionAccountQueryService
    ) {
        this.transactionAccountService = transactionAccountService;
        this.transactionAccountRepository = transactionAccountRepository;
        this.transactionAccountQueryService = transactionAccountQueryService;
    }

    /**
     * {@code POST  /transaction-accounts} : Create a new transactionAccount.
     *
     * @param transactionAccountDTO the transactionAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionAccountDTO, or with status {@code 400 (Bad Request)} if the transactionAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-accounts")
    public ResponseEntity<TransactionAccountDTO> createTransactionAccount(@Valid @RequestBody TransactionAccountDTO transactionAccountDTO)
        throws URISyntaxException {
        log.debug("REST request to save TransactionAccount : {}", transactionAccountDTO);
        if (transactionAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionAccountDTO result = transactionAccountService.save(transactionAccountDTO);
        return ResponseEntity
            .created(new URI("/api/transaction-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-accounts/:id} : Updates an existing transactionAccount.
     *
     * @param id the id of the transactionAccountDTO to save.
     * @param transactionAccountDTO the transactionAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-accounts/{id}")
    public ResponseEntity<TransactionAccountDTO> updateTransactionAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionAccountDTO transactionAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionAccount : {}, {}", id, transactionAccountDTO);
        if (transactionAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionAccountDTO result = transactionAccountService.save(transactionAccountDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-accounts/:id} : Partial updates given fields of an existing transactionAccount, field will ignore if it is null
     *
     * @param id the id of the transactionAccountDTO to save.
     * @param transactionAccountDTO the transactionAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionAccountDTO> partialUpdateTransactionAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionAccountDTO transactionAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionAccount partially : {}, {}", id, transactionAccountDTO);
        if (transactionAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionAccountDTO> result = transactionAccountService.partialUpdate(transactionAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, transactionAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-accounts} : get all the transactionAccounts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionAccounts in body.
     */
    @GetMapping("/transaction-accounts")
    public ResponseEntity<List<TransactionAccountDTO>> getAllTransactionAccounts(TransactionAccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TransactionAccounts by criteria: {}", criteria);
        Page<TransactionAccountDTO> page = transactionAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-accounts/count} : count all the transactionAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transaction-accounts/count")
    public ResponseEntity<Long> countTransactionAccounts(TransactionAccountCriteria criteria) {
        log.debug("REST request to count TransactionAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-accounts/:id} : get the "id" transactionAccount.
     *
     * @param id the id of the transactionAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-accounts/{id}")
    public ResponseEntity<TransactionAccountDTO> getTransactionAccount(@PathVariable Long id) {
        log.debug("REST request to get TransactionAccount : {}", id);
        Optional<TransactionAccountDTO> transactionAccountDTO = transactionAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionAccountDTO);
    }

    /**
     * {@code DELETE  /transaction-accounts/:id} : delete the "id" transactionAccount.
     *
     * @param id the id of the transactionAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-accounts/{id}")
    public ResponseEntity<Void> deleteTransactionAccount(@PathVariable Long id) {
        log.debug("REST request to delete TransactionAccount : {}", id);
        transactionAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/transaction-accounts?query=:query} : search for the transactionAccount corresponding
     * to the query.
     *
     * @param query the query of the transactionAccount search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/transaction-accounts")
    public ResponseEntity<List<TransactionAccountDTO>> searchTransactionAccounts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TransactionAccounts for query {}", query);
        Page<TransactionAccountDTO> page = transactionAccountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
