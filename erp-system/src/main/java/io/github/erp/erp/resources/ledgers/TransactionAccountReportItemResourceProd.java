package io.github.erp.erp.resources.ledgers;

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
import io.github.erp.internal.repository.InternalTransactionAccountReportItemRepository;
import io.github.erp.internal.service.ledgers.InternalTransactionAccountReportItemService;
import io.github.erp.repository.TransactionAccountReportItemRepository;
import io.github.erp.service.TransactionAccountReportItemQueryService;
import io.github.erp.service.TransactionAccountReportItemService;
import io.github.erp.service.criteria.TransactionAccountReportItemCriteria;
import io.github.erp.service.dto.TransactionAccountReportItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.TransactionAccountReportItem}.
 */
@RestController
@RequestMapping("/api/accounts")
public class TransactionAccountReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountReportItemResourceProd.class);

    private final InternalTransactionAccountReportItemService transactionAccountReportItemService;

    private final InternalTransactionAccountReportItemRepository transactionAccountReportItemRepository;

    private final TransactionAccountReportItemQueryService transactionAccountReportItemQueryService;

    public TransactionAccountReportItemResourceProd(
        InternalTransactionAccountReportItemService transactionAccountReportItemService,
        InternalTransactionAccountReportItemRepository transactionAccountReportItemRepository,
        TransactionAccountReportItemQueryService transactionAccountReportItemQueryService
    ) {
        this.transactionAccountReportItemService = transactionAccountReportItemService;
        this.transactionAccountReportItemRepository = transactionAccountReportItemRepository;
        this.transactionAccountReportItemQueryService = transactionAccountReportItemQueryService;
    }

    /**
     * {@code GET  /transaction-account-report-items} : get all the transactionAccountReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionAccountReportItems in body.
     */
    @GetMapping("/transaction-account-report-items")
    public ResponseEntity<List<TransactionAccountReportItemDTO>> getAllTransactionAccountReportItems(
        TransactionAccountReportItemCriteria criteria,
        @RequestParam(value = "reportDate", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate reportDate,
        Pageable pageable
    ) {
        log.debug("REST request to get TransactionAccountReportItems by criteria: {}", criteria);
        Page<TransactionAccountReportItemDTO> page = transactionAccountReportItemService.findAll(reportDate, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-account-report-items/count} : count all the transactionAccountReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transaction-account-report-items/count")
    public ResponseEntity<Long> countTransactionAccountReportItems(TransactionAccountReportItemCriteria criteria) {
        log.debug("REST request to count TransactionAccountReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionAccountReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-account-report-items/:id} : get the "id" transactionAccountReportItem.
     *
     * @param id the id of the transactionAccountReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionAccountReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-account-report-items/{id}")
    public ResponseEntity<TransactionAccountReportItemDTO> getTransactionAccountReportItem(@PathVariable Long id) {
        log.debug("REST request to get TransactionAccountReportItem : {}", id);
        Optional<TransactionAccountReportItemDTO> transactionAccountReportItemDTO = transactionAccountReportItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(transactionAccountReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/transaction-account-report-items?query=:query} : search for the transactionAccountReportItem corresponding
     * to the query.
     *
     * @param query the query of the transactionAccountReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/transaction-account-report-items")
    public ResponseEntity<List<TransactionAccountReportItemDTO>> searchTransactionAccountReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of TransactionAccountReportItems for query {}", query);
        Page<TransactionAccountReportItemDTO> page = transactionAccountReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
