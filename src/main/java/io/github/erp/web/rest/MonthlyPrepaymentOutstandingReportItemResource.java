package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.repository.MonthlyPrepaymentOutstandingReportItemRepository;
import io.github.erp.service.MonthlyPrepaymentOutstandingReportItemQueryService;
import io.github.erp.service.MonthlyPrepaymentOutstandingReportItemService;
import io.github.erp.service.criteria.MonthlyPrepaymentOutstandingReportItemCriteria;
import io.github.erp.service.dto.MonthlyPrepaymentOutstandingReportItemDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link io.github.erp.domain.MonthlyPrepaymentOutstandingReportItem}.
 */
@RestController
@RequestMapping("/api")
public class MonthlyPrepaymentOutstandingReportItemResource {

    private final Logger log = LoggerFactory.getLogger(MonthlyPrepaymentOutstandingReportItemResource.class);

    private final MonthlyPrepaymentOutstandingReportItemService monthlyPrepaymentOutstandingReportItemService;

    private final MonthlyPrepaymentOutstandingReportItemRepository monthlyPrepaymentOutstandingReportItemRepository;

    private final MonthlyPrepaymentOutstandingReportItemQueryService monthlyPrepaymentOutstandingReportItemQueryService;

    public MonthlyPrepaymentOutstandingReportItemResource(
        MonthlyPrepaymentOutstandingReportItemService monthlyPrepaymentOutstandingReportItemService,
        MonthlyPrepaymentOutstandingReportItemRepository monthlyPrepaymentOutstandingReportItemRepository,
        MonthlyPrepaymentOutstandingReportItemQueryService monthlyPrepaymentOutstandingReportItemQueryService
    ) {
        this.monthlyPrepaymentOutstandingReportItemService = monthlyPrepaymentOutstandingReportItemService;
        this.monthlyPrepaymentOutstandingReportItemRepository = monthlyPrepaymentOutstandingReportItemRepository;
        this.monthlyPrepaymentOutstandingReportItemQueryService = monthlyPrepaymentOutstandingReportItemQueryService;
    }

    /**
     * {@code GET  /monthly-prepayment-outstanding-report-items} : get all the monthlyPrepaymentOutstandingReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monthlyPrepaymentOutstandingReportItems in body.
     */
    @GetMapping("/monthly-prepayment-outstanding-report-items")
    public ResponseEntity<List<MonthlyPrepaymentOutstandingReportItemDTO>> getAllMonthlyPrepaymentOutstandingReportItems(
        MonthlyPrepaymentOutstandingReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get MonthlyPrepaymentOutstandingReportItems by criteria: {}", criteria);
        Page<MonthlyPrepaymentOutstandingReportItemDTO> page = monthlyPrepaymentOutstandingReportItemQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monthly-prepayment-outstanding-report-items/count} : count all the monthlyPrepaymentOutstandingReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/monthly-prepayment-outstanding-report-items/count")
    public ResponseEntity<Long> countMonthlyPrepaymentOutstandingReportItems(MonthlyPrepaymentOutstandingReportItemCriteria criteria) {
        log.debug("REST request to count MonthlyPrepaymentOutstandingReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(monthlyPrepaymentOutstandingReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monthly-prepayment-outstanding-report-items/:id} : get the "id" monthlyPrepaymentOutstandingReportItem.
     *
     * @param id the id of the monthlyPrepaymentOutstandingReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monthlyPrepaymentOutstandingReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monthly-prepayment-outstanding-report-items/{id}")
    public ResponseEntity<MonthlyPrepaymentOutstandingReportItemDTO> getMonthlyPrepaymentOutstandingReportItem(@PathVariable Long id) {
        log.debug("REST request to get MonthlyPrepaymentOutstandingReportItem : {}", id);
        Optional<MonthlyPrepaymentOutstandingReportItemDTO> monthlyPrepaymentOutstandingReportItemDTO = monthlyPrepaymentOutstandingReportItemService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(monthlyPrepaymentOutstandingReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/monthly-prepayment-outstanding-report-items?query=:query} : search for the monthlyPrepaymentOutstandingReportItem corresponding
     * to the query.
     *
     * @param query the query of the monthlyPrepaymentOutstandingReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/monthly-prepayment-outstanding-report-items")
    public ResponseEntity<List<MonthlyPrepaymentOutstandingReportItemDTO>> searchMonthlyPrepaymentOutstandingReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of MonthlyPrepaymentOutstandingReportItems for query {}", query);
        Page<MonthlyPrepaymentOutstandingReportItemDTO> page = monthlyPrepaymentOutstandingReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
