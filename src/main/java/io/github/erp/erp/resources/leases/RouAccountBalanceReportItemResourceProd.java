package io.github.erp.erp.resources.leases;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.internal.repository.InternalRouAccountBalanceReportItemRepository;
import io.github.erp.internal.service.rou.InternalRouAccountBalanceReportItemService;
import io.github.erp.service.RouAccountBalanceReportItemQueryService;
import io.github.erp.service.criteria.RouAccountBalanceReportItemCriteria;
import io.github.erp.service.dto.RouAccountBalanceReportItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.RouAccountBalanceReportItem}.
 */
@RestController
@RequestMapping("/api/leases")
public class RouAccountBalanceReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(RouAccountBalanceReportItemResourceProd.class);

    private final InternalRouAccountBalanceReportItemService rouAccountBalanceReportItemService;

    private final RouAccountBalanceReportItemQueryService rouAccountBalanceReportItemQueryService;

    public RouAccountBalanceReportItemResourceProd(
        InternalRouAccountBalanceReportItemService rouAccountBalanceReportItemService,
        RouAccountBalanceReportItemQueryService rouAccountBalanceReportItemQueryService
    ) {
        this.rouAccountBalanceReportItemService = rouAccountBalanceReportItemService;
        this.rouAccountBalanceReportItemQueryService = rouAccountBalanceReportItemQueryService;
    }

    /**
     * {@code GET  /rou-account-balance-report-items} : get all the rouAccountBalanceReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouAccountBalanceReportItems in body.
     */
    @GetMapping("/rou-account-balance-report-items")
    public ResponseEntity<List<RouAccountBalanceReportItemDTO>> getAllRouAccountBalanceReportItems(
        RouAccountBalanceReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouAccountBalanceReportItems by criteria: {}", criteria);
        Page<RouAccountBalanceReportItemDTO> page = rouAccountBalanceReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-account-balance-report-items/reports/:leasePeriodId} : get the rouAccountBalanceReportItems for the given lease-period-id.
     *
     * @param leasePeriodId the id of the lease-period to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouAccountBalanceReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-account-balance-report-items/reports/{leasePeriodId}")
    public ResponseEntity<List<RouAccountBalanceReportItemDTO>> getRouAccountBalanceReportItems(@PathVariable Long leasePeriodId, Pageable pageable) {
        log.debug("REST request to get RouAccountBalanceReportItems for lease-period-id : {}", leasePeriodId);
        Page<RouAccountBalanceReportItemDTO> page = rouAccountBalanceReportItemService.findAllForGivenLeasePeriod(pageable, leasePeriodId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-account-balance-report-items/count} : count all the rouAccountBalanceReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-account-balance-report-items/count")
    public ResponseEntity<Long> countRouAccountBalanceReportItems(RouAccountBalanceReportItemCriteria criteria) {
        log.debug("REST request to count RouAccountBalanceReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouAccountBalanceReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-account-balance-report-items/:id} : get the "id" rouAccountBalanceReportItem.
     *
     * @param id the id of the rouAccountBalanceReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouAccountBalanceReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-account-balance-report-items/{id}")
    public ResponseEntity<RouAccountBalanceReportItemDTO> getRouAccountBalanceReportItem(@PathVariable Long id) {
        log.debug("REST request to get RouAccountBalanceReportItem : {}", id);
        Optional<RouAccountBalanceReportItemDTO> rouAccountBalanceReportItemDTO = rouAccountBalanceReportItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouAccountBalanceReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/rou-account-balance-report-items?query=:query} : search for the rouAccountBalanceReportItem corresponding
     * to the query.
     *
     * @param query the query of the rouAccountBalanceReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-account-balance-report-items")
    public ResponseEntity<List<RouAccountBalanceReportItemDTO>> searchRouAccountBalanceReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of RouAccountBalanceReportItems for query {}", query);
        Page<RouAccountBalanceReportItemDTO> page = rouAccountBalanceReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
