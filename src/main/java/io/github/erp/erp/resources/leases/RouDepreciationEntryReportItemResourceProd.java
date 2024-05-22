package io.github.erp.erp.resources.leases;

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
import io.github.erp.internal.repository.InternalRouDepreciationEntryReportItemRepository;
import io.github.erp.internal.service.rou.InternalRouDepreciationEntryReportItemQueryService;
import io.github.erp.internal.service.rou.InternalRouDepreciationEntryReportItemService;
import io.github.erp.repository.RouDepreciationEntryReportItemRepository;
import io.github.erp.service.RouDepreciationEntryReportItemQueryService;
import io.github.erp.service.RouDepreciationEntryReportItemService;
import io.github.erp.service.criteria.RouDepreciationEntryReportItemCriteria;
import io.github.erp.service.dto.RouDepreciationEntryReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouDepreciationEntryReportItem}.
 */
@RestController
@RequestMapping("/api/leases")
public class RouDepreciationEntryReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationEntryReportItemResourceProd.class);

    private final InternalRouDepreciationEntryReportItemService rouDepreciationEntryReportItemService;

    private final InternalRouDepreciationEntryReportItemRepository rouDepreciationEntryReportItemRepository;

    private final InternalRouDepreciationEntryReportItemQueryService rouDepreciationEntryReportItemQueryService;

    public RouDepreciationEntryReportItemResourceProd(
        InternalRouDepreciationEntryReportItemService rouDepreciationEntryReportItemService,
        InternalRouDepreciationEntryReportItemRepository rouDepreciationEntryReportItemRepository,
        InternalRouDepreciationEntryReportItemQueryService rouDepreciationEntryReportItemQueryService
    ) {
        this.rouDepreciationEntryReportItemService = rouDepreciationEntryReportItemService;
        this.rouDepreciationEntryReportItemRepository = rouDepreciationEntryReportItemRepository;
        this.rouDepreciationEntryReportItemQueryService = rouDepreciationEntryReportItemQueryService;
    }

    /**
     * {@code GET  /rou-depreciation-entry-report-items} : get all the rouDepreciationEntryReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouDepreciationEntryReportItems in body.
     */
    @GetMapping("/rou-depreciation-entry-report-items")
    public ResponseEntity<List<RouDepreciationEntryReportItemDTO>> getAllRouDepreciationEntryReportItems(
        RouDepreciationEntryReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouDepreciationEntryReportItems by criteria: {}", criteria);
        Page<RouDepreciationEntryReportItemDTO> page = rouDepreciationEntryReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-depreciation-entry-report-items/by-query} : get all the rouDepreciationEntryReportItems.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouDepreciationEntryReportItems in body.
     */
    @GetMapping("/rou-depreciation-entry-report-items/by-query")
    public ResponseEntity<List<RouDepreciationEntryReportItemDTO>> getAllReportItemsByQuery(Pageable pageable) {
        log.debug("REST request to get RouDepreciationEntryReportItems by query");
        Page<RouDepreciationEntryReportItemDTO> page = rouDepreciationEntryReportItemService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-depreciation-entry-report-items/count} : count all the rouDepreciationEntryReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-depreciation-entry-report-items/count")
    public ResponseEntity<Long> countRouDepreciationEntryReportItems(RouDepreciationEntryReportItemCriteria criteria) {
        log.debug("REST request to count RouDepreciationEntryReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouDepreciationEntryReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-depreciation-entry-report-items/:id} : get the "id" rouDepreciationEntryReportItem.
     *
     * @param id the id of the rouDepreciationEntryReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouDepreciationEntryReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-depreciation-entry-report-items/{id}")
    public ResponseEntity<RouDepreciationEntryReportItemDTO> getRouDepreciationEntryReportItem(@PathVariable Long id) {
        log.debug("REST request to get RouDepreciationEntryReportItem : {}", id);
        Optional<RouDepreciationEntryReportItemDTO> rouDepreciationEntryReportItemDTO = rouDepreciationEntryReportItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouDepreciationEntryReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/rou-depreciation-entry-report-items?query=:query} : search for the rouDepreciationEntryReportItem corresponding
     * to the query.
     *
     * @param query the query of the rouDepreciationEntryReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-depreciation-entry-report-items")
    public ResponseEntity<List<RouDepreciationEntryReportItemDTO>> searchRouDepreciationEntryReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of RouDepreciationEntryReportItems for query {}", query);
        Page<RouDepreciationEntryReportItemDTO> page = rouDepreciationEntryReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
