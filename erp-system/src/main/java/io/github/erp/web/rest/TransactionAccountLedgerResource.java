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

import io.github.erp.repository.TransactionAccountLedgerRepository;
import io.github.erp.service.TransactionAccountLedgerQueryService;
import io.github.erp.service.TransactionAccountLedgerService;
import io.github.erp.service.criteria.TransactionAccountLedgerCriteria;
import io.github.erp.service.dto.TransactionAccountLedgerDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TransactionAccountLedger}.
 */
@RestController
@RequestMapping("/api")
public class TransactionAccountLedgerResource {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountLedgerResource.class);

    private static final String ENTITY_NAME = "transactionAccountLedger";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionAccountLedgerService transactionAccountLedgerService;

    private final TransactionAccountLedgerRepository transactionAccountLedgerRepository;

    private final TransactionAccountLedgerQueryService transactionAccountLedgerQueryService;

    public TransactionAccountLedgerResource(
        TransactionAccountLedgerService transactionAccountLedgerService,
        TransactionAccountLedgerRepository transactionAccountLedgerRepository,
        TransactionAccountLedgerQueryService transactionAccountLedgerQueryService
    ) {
        this.transactionAccountLedgerService = transactionAccountLedgerService;
        this.transactionAccountLedgerRepository = transactionAccountLedgerRepository;
        this.transactionAccountLedgerQueryService = transactionAccountLedgerQueryService;
    }

    /**
     * {@code POST  /transaction-account-ledgers} : Create a new transactionAccountLedger.
     *
     * @param transactionAccountLedgerDTO the transactionAccountLedgerDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionAccountLedgerDTO, or with status {@code 400 (Bad Request)} if the transactionAccountLedger has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-account-ledgers")
    public ResponseEntity<TransactionAccountLedgerDTO> createTransactionAccountLedger(
        @Valid @RequestBody TransactionAccountLedgerDTO transactionAccountLedgerDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TransactionAccountLedger : {}", transactionAccountLedgerDTO);
        if (transactionAccountLedgerDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionAccountLedger cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionAccountLedgerDTO result = transactionAccountLedgerService.save(transactionAccountLedgerDTO);
        return ResponseEntity
            .created(new URI("/api/transaction-account-ledgers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-account-ledgers/:id} : Updates an existing transactionAccountLedger.
     *
     * @param id the id of the transactionAccountLedgerDTO to save.
     * @param transactionAccountLedgerDTO the transactionAccountLedgerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountLedgerDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountLedgerDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountLedgerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-account-ledgers/{id}")
    public ResponseEntity<TransactionAccountLedgerDTO> updateTransactionAccountLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionAccountLedgerDTO transactionAccountLedgerDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionAccountLedger : {}, {}", id, transactionAccountLedgerDTO);
        if (transactionAccountLedgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountLedgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountLedgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionAccountLedgerDTO result = transactionAccountLedgerService.save(transactionAccountLedgerDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionAccountLedgerDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-account-ledgers/:id} : Partial updates given fields of an existing transactionAccountLedger, field will ignore if it is null
     *
     * @param id the id of the transactionAccountLedgerDTO to save.
     * @param transactionAccountLedgerDTO the transactionAccountLedgerDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountLedgerDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountLedgerDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionAccountLedgerDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountLedgerDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-account-ledgers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionAccountLedgerDTO> partialUpdateTransactionAccountLedger(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionAccountLedgerDTO transactionAccountLedgerDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionAccountLedger partially : {}, {}", id, transactionAccountLedgerDTO);
        if (transactionAccountLedgerDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountLedgerDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountLedgerRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionAccountLedgerDTO> result = transactionAccountLedgerService.partialUpdate(transactionAccountLedgerDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionAccountLedgerDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-account-ledgers} : get all the transactionAccountLedgers.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionAccountLedgers in body.
     */
    @GetMapping("/transaction-account-ledgers")
    public ResponseEntity<List<TransactionAccountLedgerDTO>> getAllTransactionAccountLedgers(
        TransactionAccountLedgerCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TransactionAccountLedgers by criteria: {}", criteria);
        Page<TransactionAccountLedgerDTO> page = transactionAccountLedgerQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-account-ledgers/count} : count all the transactionAccountLedgers.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transaction-account-ledgers/count")
    public ResponseEntity<Long> countTransactionAccountLedgers(TransactionAccountLedgerCriteria criteria) {
        log.debug("REST request to count TransactionAccountLedgers by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionAccountLedgerQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-account-ledgers/:id} : get the "id" transactionAccountLedger.
     *
     * @param id the id of the transactionAccountLedgerDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionAccountLedgerDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-account-ledgers/{id}")
    public ResponseEntity<TransactionAccountLedgerDTO> getTransactionAccountLedger(@PathVariable Long id) {
        log.debug("REST request to get TransactionAccountLedger : {}", id);
        Optional<TransactionAccountLedgerDTO> transactionAccountLedgerDTO = transactionAccountLedgerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionAccountLedgerDTO);
    }

    /**
     * {@code DELETE  /transaction-account-ledgers/:id} : delete the "id" transactionAccountLedger.
     *
     * @param id the id of the transactionAccountLedgerDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-account-ledgers/{id}")
    public ResponseEntity<Void> deleteTransactionAccountLedger(@PathVariable Long id) {
        log.debug("REST request to delete TransactionAccountLedger : {}", id);
        transactionAccountLedgerService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/transaction-account-ledgers?query=:query} : search for the transactionAccountLedger corresponding
     * to the query.
     *
     * @param query the query of the transactionAccountLedger search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/transaction-account-ledgers")
    public ResponseEntity<List<TransactionAccountLedgerDTO>> searchTransactionAccountLedgers(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of TransactionAccountLedgers for query {}", query);
        Page<TransactionAccountLedgerDTO> page = transactionAccountLedgerService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
