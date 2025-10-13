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

import io.github.erp.internal.repository.InternalTransactionAccountCategoryRepository;
import io.github.erp.internal.service.ledgers.InternalTransactionAccountCategoryService;
import io.github.erp.service.TransactionAccountCategoryQueryService;
import io.github.erp.service.criteria.TransactionAccountCategoryCriteria;
import io.github.erp.service.dto.TransactionAccountCategoryDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TransactionAccountCategory}.
 */
@RestController
@RequestMapping("/api/accounts")
public class TransactionAccountCategoryResourceProd {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountCategoryResourceProd.class);

    private static final String ENTITY_NAME = "transactionAccountCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalTransactionAccountCategoryService transactionAccountCategoryService;

    private final InternalTransactionAccountCategoryRepository transactionAccountCategoryRepository;

    private final TransactionAccountCategoryQueryService transactionAccountCategoryQueryService;

    public TransactionAccountCategoryResourceProd(
        InternalTransactionAccountCategoryService transactionAccountCategoryService,
        InternalTransactionAccountCategoryRepository transactionAccountCategoryRepository,
        TransactionAccountCategoryQueryService transactionAccountCategoryQueryService
    ) {
        this.transactionAccountCategoryService = transactionAccountCategoryService;
        this.transactionAccountCategoryRepository = transactionAccountCategoryRepository;
        this.transactionAccountCategoryQueryService = transactionAccountCategoryQueryService;
    }

    /**
     * {@code POST  /transaction-account-categories} : Create a new transactionAccountCategory.
     *
     * @param transactionAccountCategoryDTO the transactionAccountCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionAccountCategoryDTO, or with status {@code 400 (Bad Request)} if the transactionAccountCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-account-categories")
    public ResponseEntity<TransactionAccountCategoryDTO> createTransactionAccountCategory(
        @Valid @RequestBody TransactionAccountCategoryDTO transactionAccountCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TransactionAccountCategory : {}", transactionAccountCategoryDTO);
        if (transactionAccountCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new transactionAccountCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TransactionAccountCategoryDTO result = transactionAccountCategoryService.save(transactionAccountCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/transaction-account-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-account-categories/:id} : Updates an existing transactionAccountCategory.
     *
     * @param id the id of the transactionAccountCategoryDTO to save.
     * @param transactionAccountCategoryDTO the transactionAccountCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-account-categories/{id}")
    public ResponseEntity<TransactionAccountCategoryDTO> updateTransactionAccountCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionAccountCategoryDTO transactionAccountCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionAccountCategory : {}, {}", id, transactionAccountCategoryDTO);
        if (transactionAccountCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionAccountCategoryDTO result = transactionAccountCategoryService.save(transactionAccountCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionAccountCategoryDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-account-categories/:id} : Partial updates given fields of an existing transactionAccountCategory, field will ignore if it is null
     *
     * @param id the id of the transactionAccountCategoryDTO to save.
     * @param transactionAccountCategoryDTO the transactionAccountCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionAccountCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/transaction-account-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TransactionAccountCategoryDTO> partialUpdateTransactionAccountCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionAccountCategoryDTO transactionAccountCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TransactionAccountCategory partially : {}, {}", id, transactionAccountCategoryDTO);
        if (transactionAccountCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionAccountCategoryDTO> result = transactionAccountCategoryService.partialUpdate(transactionAccountCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, transactionAccountCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /transaction-account-categories} : get all the transactionAccountCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionAccountCategories in body.
     */
    @GetMapping("/transaction-account-categories")
    public ResponseEntity<List<TransactionAccountCategoryDTO>> getAllTransactionAccountCategories(
        TransactionAccountCategoryCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TransactionAccountCategories by criteria: {}", criteria);
        Page<TransactionAccountCategoryDTO> page = transactionAccountCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-account-categories/count} : count all the transactionAccountCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transaction-account-categories/count")
    public ResponseEntity<Long> countTransactionAccountCategories(TransactionAccountCategoryCriteria criteria) {
        log.debug("REST request to count TransactionAccountCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionAccountCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-account-categories/:id} : get the "id" transactionAccountCategory.
     *
     * @param id the id of the transactionAccountCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionAccountCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-account-categories/{id}")
    public ResponseEntity<TransactionAccountCategoryDTO> getTransactionAccountCategory(@PathVariable Long id) {
        log.debug("REST request to get TransactionAccountCategory : {}", id);
        Optional<TransactionAccountCategoryDTO> transactionAccountCategoryDTO = transactionAccountCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionAccountCategoryDTO);
    }

    /**
     * {@code DELETE  /transaction-account-categories/:id} : delete the "id" transactionAccountCategory.
     *
     * @param id the id of the transactionAccountCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-account-categories/{id}")
    public ResponseEntity<Void> deleteTransactionAccountCategory(@PathVariable Long id) {
        log.debug("REST request to delete TransactionAccountCategory : {}", id);
        transactionAccountCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/transaction-account-categories?query=:query} : search for the transactionAccountCategory corresponding
     * to the query.
     *
     * @param query the query of the transactionAccountCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/transaction-account-categories")
    public ResponseEntity<List<TransactionAccountCategoryDTO>> searchTransactionAccountCategories(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of TransactionAccountCategories for query {}", query);
        Page<TransactionAccountCategoryDTO> page = transactionAccountCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
