package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.repository.CardAcquiringTransactionRepository;
import io.github.erp.service.CardAcquiringTransactionQueryService;
import io.github.erp.service.CardAcquiringTransactionService;
import io.github.erp.service.criteria.CardAcquiringTransactionCriteria;
import io.github.erp.service.dto.CardAcquiringTransactionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardAcquiringTransaction}.
 */
@RestController
@RequestMapping("/api")
public class CardAcquiringTransactionResource {

    private final Logger log = LoggerFactory.getLogger(CardAcquiringTransactionResource.class);

    private static final String ENTITY_NAME = "gdiDataCardAcquiringTransaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardAcquiringTransactionService cardAcquiringTransactionService;

    private final CardAcquiringTransactionRepository cardAcquiringTransactionRepository;

    private final CardAcquiringTransactionQueryService cardAcquiringTransactionQueryService;

    public CardAcquiringTransactionResource(
        CardAcquiringTransactionService cardAcquiringTransactionService,
        CardAcquiringTransactionRepository cardAcquiringTransactionRepository,
        CardAcquiringTransactionQueryService cardAcquiringTransactionQueryService
    ) {
        this.cardAcquiringTransactionService = cardAcquiringTransactionService;
        this.cardAcquiringTransactionRepository = cardAcquiringTransactionRepository;
        this.cardAcquiringTransactionQueryService = cardAcquiringTransactionQueryService;
    }

    /**
     * {@code POST  /card-acquiring-transactions} : Create a new cardAcquiringTransaction.
     *
     * @param cardAcquiringTransactionDTO the cardAcquiringTransactionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardAcquiringTransactionDTO, or with status {@code 400 (Bad Request)} if the cardAcquiringTransaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-acquiring-transactions")
    public ResponseEntity<CardAcquiringTransactionDTO> createCardAcquiringTransaction(
        @Valid @RequestBody CardAcquiringTransactionDTO cardAcquiringTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CardAcquiringTransaction : {}", cardAcquiringTransactionDTO);
        if (cardAcquiringTransactionDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardAcquiringTransaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardAcquiringTransactionDTO result = cardAcquiringTransactionService.save(cardAcquiringTransactionDTO);
        return ResponseEntity
            .created(new URI("/api/card-acquiring-transactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-acquiring-transactions/:id} : Updates an existing cardAcquiringTransaction.
     *
     * @param id the id of the cardAcquiringTransactionDTO to save.
     * @param cardAcquiringTransactionDTO the cardAcquiringTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardAcquiringTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the cardAcquiringTransactionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardAcquiringTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-acquiring-transactions/{id}")
    public ResponseEntity<CardAcquiringTransactionDTO> updateCardAcquiringTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardAcquiringTransactionDTO cardAcquiringTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardAcquiringTransaction : {}, {}", id, cardAcquiringTransactionDTO);
        if (cardAcquiringTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardAcquiringTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardAcquiringTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardAcquiringTransactionDTO result = cardAcquiringTransactionService.save(cardAcquiringTransactionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardAcquiringTransactionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-acquiring-transactions/:id} : Partial updates given fields of an existing cardAcquiringTransaction, field will ignore if it is null
     *
     * @param id the id of the cardAcquiringTransactionDTO to save.
     * @param cardAcquiringTransactionDTO the cardAcquiringTransactionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardAcquiringTransactionDTO,
     * or with status {@code 400 (Bad Request)} if the cardAcquiringTransactionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardAcquiringTransactionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardAcquiringTransactionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-acquiring-transactions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardAcquiringTransactionDTO> partialUpdateCardAcquiringTransaction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardAcquiringTransactionDTO cardAcquiringTransactionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardAcquiringTransaction partially : {}, {}", id, cardAcquiringTransactionDTO);
        if (cardAcquiringTransactionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardAcquiringTransactionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardAcquiringTransactionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardAcquiringTransactionDTO> result = cardAcquiringTransactionService.partialUpdate(cardAcquiringTransactionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardAcquiringTransactionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-acquiring-transactions} : get all the cardAcquiringTransactions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardAcquiringTransactions in body.
     */
    @GetMapping("/card-acquiring-transactions")
    public ResponseEntity<List<CardAcquiringTransactionDTO>> getAllCardAcquiringTransactions(
        CardAcquiringTransactionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CardAcquiringTransactions by criteria: {}", criteria);
        Page<CardAcquiringTransactionDTO> page = cardAcquiringTransactionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-acquiring-transactions/count} : count all the cardAcquiringTransactions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-acquiring-transactions/count")
    public ResponseEntity<Long> countCardAcquiringTransactions(CardAcquiringTransactionCriteria criteria) {
        log.debug("REST request to count CardAcquiringTransactions by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardAcquiringTransactionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-acquiring-transactions/:id} : get the "id" cardAcquiringTransaction.
     *
     * @param id the id of the cardAcquiringTransactionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardAcquiringTransactionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-acquiring-transactions/{id}")
    public ResponseEntity<CardAcquiringTransactionDTO> getCardAcquiringTransaction(@PathVariable Long id) {
        log.debug("REST request to get CardAcquiringTransaction : {}", id);
        Optional<CardAcquiringTransactionDTO> cardAcquiringTransactionDTO = cardAcquiringTransactionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardAcquiringTransactionDTO);
    }

    /**
     * {@code DELETE  /card-acquiring-transactions/:id} : delete the "id" cardAcquiringTransaction.
     *
     * @param id the id of the cardAcquiringTransactionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-acquiring-transactions/{id}")
    public ResponseEntity<Void> deleteCardAcquiringTransaction(@PathVariable Long id) {
        log.debug("REST request to delete CardAcquiringTransaction : {}", id);
        cardAcquiringTransactionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-acquiring-transactions?query=:query} : search for the cardAcquiringTransaction corresponding
     * to the query.
     *
     * @param query the query of the cardAcquiringTransaction search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-acquiring-transactions")
    public ResponseEntity<List<CardAcquiringTransactionDTO>> searchCardAcquiringTransactions(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CardAcquiringTransactions for query {}", query);
        Page<CardAcquiringTransactionDTO> page = cardAcquiringTransactionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
