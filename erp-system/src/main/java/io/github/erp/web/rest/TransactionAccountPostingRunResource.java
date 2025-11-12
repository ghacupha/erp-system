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

import io.github.erp.repository.TransactionAccountPostingRunRepository;
import io.github.erp.service.TransactionAccountPostingRunQueryService;
import io.github.erp.service.TransactionAccountPostingRunService;
import io.github.erp.service.criteria.TransactionAccountPostingRunCriteria;
import io.github.erp.service.dto.TransactionAccountPostingRunDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TransactionAccountPostingRun}.
 */
@RestController
@RequestMapping("/api")
public class TransactionAccountPostingRunResource {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountPostingRunResource.class);

    private static final String ENTITY_NAME = "transactionAccountPostingRun";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionAccountPostingRunService transactionAccountPostingRunService;

    private final TransactionAccountPostingRunRepository transactionAccountPostingRunRepository;

    private final TransactionAccountPostingRunQueryService transactionAccountPostingRunQueryService;

    public TransactionAccountPostingRunResource(
        TransactionAccountPostingRunService transactionAccountPostingRunService,
        TransactionAccountPostingRunRepository transactionAccountPostingRunRepository,
        TransactionAccountPostingRunQueryService transactionAccountPostingRunQueryService
    ) {
        this.transactionAccountPostingRunService = transactionAccountPostingRunService;
        this.transactionAccountPostingRunRepository = transactionAccountPostingRunRepository;
        this.transactionAccountPostingRunQueryService = transactionAccountPostingRunQueryService;
    }

    /**
     * {@code POST  /transaction-account-posting-runs} : Create a new transactionAccountPostingRun.
     *
     * @param transactionAccountPostingRunDTO the transactionAccountPostingRunDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionAccountPostingRunDTO, or with status {@code 400 (Bad Request)} if the transactionAccountPostingRun has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-account-posting-runs")
    public ResponseEntity<TransactionAccountPostingRunDTO> createTransactionAccountPostingRun(
        @Valid @RequestBody TransactionAccountPostingRunDTO transactionAccountPostingRunDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TransactionAccountPostingRun : {}", transactionAccountPostingRunDTO);
        if (transactionAccountPostingRunDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionAccountPostingRun cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionAccountPostingRunDTO result = transactionAccountPostingRunService.save(transactionAccountPostingRunDTO);
        return ResponseEntity
            .created(new URI("/api/transaction-account-posting-runs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-account-posting-runs/:id} : Updates an existing transactionAccountPostingRun.
     *
     * @param id the id of the transactionAccountPostingRunDTO to save.
     * @param transactionAccountPostingRunDTO the transactionAccountPostingRunDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountPostingRunDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountPostingRunDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountPostingRunDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-account-posting-runs/{id}")
    public ResponseEntity<TransactionAccountPostingRunDTO> updateTransactionAccountPostingRun(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionAccountPostingRunDTO transactionAccountPostingRunDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionAccountPostingRun : {}, {}", id, transactionAccountPostingRunDTO);
        if (transactionAccountPostingRunDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountPostingRunDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountPostingRunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionAccountPostingRunDTO result = transactionAccountPostingRunService.save(transactionAccountPostingRunDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionAccountPostingRunDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-account-posting-runs/:id} : Partial updates given fields of an existing transactionAccountPostingRun, field will ignore if it is null
     *
     * @param id the id of the transactionAccountPostingRunDTO to save.
     * @param transactionAccountPostingRunDTO the transactionAccountPostingRunDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountPostingRunDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountPostingRunDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionAccountPostingRunDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountPostingRunDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-account-posting-runs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionAccountPostingRunDTO> partialUpdateTransactionAccountPostingRun(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionAccountPostingRunDTO transactionAccountPostingRunDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionAccountPostingRun partially : {}, {}", id, transactionAccountPostingRunDTO);
        if (transactionAccountPostingRunDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountPostingRunDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountPostingRunRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionAccountPostingRunDTO> result = transactionAccountPostingRunService.partialUpdate(
            transactionAccountPostingRunDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionAccountPostingRunDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-account-posting-runs} : get all the transactionAccountPostingRuns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionAccountPostingRuns in body.
     */
    @GetMapping("/transaction-account-posting-runs")
    public ResponseEntity<List<TransactionAccountPostingRunDTO>> getAllTransactionAccountPostingRuns(
        TransactionAccountPostingRunCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TransactionAccountPostingRuns by criteria: {}", criteria);
        Page<TransactionAccountPostingRunDTO> page = transactionAccountPostingRunQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-account-posting-runs/count} : count all the transactionAccountPostingRuns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transaction-account-posting-runs/count")
    public ResponseEntity<Long> countTransactionAccountPostingRuns(TransactionAccountPostingRunCriteria criteria) {
        log.debug("REST request to count TransactionAccountPostingRuns by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionAccountPostingRunQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-account-posting-runs/:id} : get the "id" transactionAccountPostingRun.
     *
     * @param id the id of the transactionAccountPostingRunDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionAccountPostingRunDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-account-posting-runs/{id}")
    public ResponseEntity<TransactionAccountPostingRunDTO> getTransactionAccountPostingRun(@PathVariable Long id) {
        log.debug("REST request to get TransactionAccountPostingRun : {}", id);
        Optional<TransactionAccountPostingRunDTO> transactionAccountPostingRunDTO = transactionAccountPostingRunService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionAccountPostingRunDTO);
    }

    /**
     * {@code DELETE  /transaction-account-posting-runs/:id} : delete the "id" transactionAccountPostingRun.
     *
     * @param id the id of the transactionAccountPostingRunDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-account-posting-runs/{id}")
    public ResponseEntity<Void> deleteTransactionAccountPostingRun(@PathVariable Long id) {
        log.debug("REST request to delete TransactionAccountPostingRun : {}", id);
        transactionAccountPostingRunService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/transaction-account-posting-runs?query=:query} : search for the transactionAccountPostingRun corresponding
     * to the query.
     *
     * @param query the query of the transactionAccountPostingRun search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/transaction-account-posting-runs")
    public ResponseEntity<List<TransactionAccountPostingRunDTO>> searchTransactionAccountPostingRuns(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of TransactionAccountPostingRuns for query {}", query);
        Page<TransactionAccountPostingRunDTO> page = transactionAccountPostingRunService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
