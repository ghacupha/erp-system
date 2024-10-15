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

import io.github.erp.repository.TransactionDetailsRepository;
import io.github.erp.service.TransactionDetailsQueryService;
import io.github.erp.service.TransactionDetailsService;
import io.github.erp.service.criteria.TransactionDetailsCriteria;
import io.github.erp.service.dto.TransactionDetailsDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TransactionDetails}.
 */
@RestController
@RequestMapping("/api")
public class TransactionDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TransactionDetailsResource.class);

    private static final String ENTITY_NAME = "transactionDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionDetailsService transactionDetailsService;

    private final TransactionDetailsRepository transactionDetailsRepository;

    private final TransactionDetailsQueryService transactionDetailsQueryService;

    public TransactionDetailsResource(
        TransactionDetailsService transactionDetailsService,
        TransactionDetailsRepository transactionDetailsRepository,
        TransactionDetailsQueryService transactionDetailsQueryService
    ) {
        this.transactionDetailsService = transactionDetailsService;
        this.transactionDetailsRepository = transactionDetailsRepository;
        this.transactionDetailsQueryService = transactionDetailsQueryService;
    }

    /**
     * {@code POST  /transaction-details} : Create a new transactionDetails.
     *
     * @param transactionDetailsDTO the transactionDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionDetailsDTO, or with status {@code 400 (Bad Request)} if the transactionDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-details")
    public ResponseEntity<TransactionDetailsDTO> createTransactionDetails(@Valid @RequestBody TransactionDetailsDTO transactionDetailsDTO)
        throws URISyntaxException {
        log.debug("REST request to save TransactionDetails : {}", transactionDetailsDTO);
        if (transactionDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionDetailsDTO result = transactionDetailsService.save(transactionDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/transaction-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-details/:id} : Updates an existing transactionDetails.
     *
     * @param id the id of the transactionDetailsDTO to save.
     * @param transactionDetailsDTO the transactionDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the transactionDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-details/{id}")
    public ResponseEntity<TransactionDetailsDTO> updateTransactionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionDetailsDTO transactionDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionDetails : {}, {}", id, transactionDetailsDTO);
        if (transactionDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionDetailsDTO result = transactionDetailsService.save(transactionDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionDetailsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-details/:id} : Partial updates given fields of an existing transactionDetails, field will ignore if it is null
     *
     * @param id the id of the transactionDetailsDTO to save.
     * @param transactionDetailsDTO the transactionDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the transactionDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionDetailsDTO> partialUpdateTransactionDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionDetailsDTO transactionDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionDetails partially : {}, {}", id, transactionDetailsDTO);
        if (transactionDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionDetailsDTO> result = transactionDetailsService.partialUpdate(transactionDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-details} : get all the transactionDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionDetails in body.
     */
    @GetMapping("/transaction-details")
    public ResponseEntity<List<TransactionDetailsDTO>> getAllTransactionDetails(TransactionDetailsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TransactionDetails by criteria: {}", criteria);
        Page<TransactionDetailsDTO> page = transactionDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-details/count} : count all the transactionDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transaction-details/count")
    public ResponseEntity<Long> countTransactionDetails(TransactionDetailsCriteria criteria) {
        log.debug("REST request to count TransactionDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-details/:id} : get the "id" transactionDetails.
     *
     * @param id the id of the transactionDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-details/{id}")
    public ResponseEntity<TransactionDetailsDTO> getTransactionDetails(@PathVariable Long id) {
        log.debug("REST request to get TransactionDetails : {}", id);
        Optional<TransactionDetailsDTO> transactionDetailsDTO = transactionDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionDetailsDTO);
    }

    /**
     * {@code DELETE  /transaction-details/:id} : delete the "id" transactionDetails.
     *
     * @param id the id of the transactionDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-details/{id}")
    public ResponseEntity<Void> deleteTransactionDetails(@PathVariable Long id) {
        log.debug("REST request to delete TransactionDetails : {}", id);
        transactionDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/transaction-details?query=:query} : search for the transactionDetails corresponding
     * to the query.
     *
     * @param query the query of the transactionDetails search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/transaction-details")
    public ResponseEntity<List<TransactionDetailsDTO>> searchTransactionDetails(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TransactionDetails for query {}", query);
        Page<TransactionDetailsDTO> page = transactionDetailsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
