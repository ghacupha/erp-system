package io.github.erp.erp.resources.leases;

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
import io.github.erp.internal.repository.InternalRouAssetNBVReportItemRepository;
import io.github.erp.internal.service.rou.InternalRouAssetNBVReportItemService;
import io.github.erp.repository.RouAssetNBVReportItemRepository;
import io.github.erp.service.RouAssetNBVReportItemQueryService;
import io.github.erp.service.RouAssetNBVReportItemService;
import io.github.erp.service.criteria.RouAssetNBVReportItemCriteria;
import io.github.erp.service.dto.RouAssetNBVReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouAssetNBVReportItem}.
 */
@RestController
@RequestMapping("/api/leases")
public class RouAssetNBVReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(RouAssetNBVReportItemResourceProd.class);

    private final InternalRouAssetNBVReportItemService rouAssetNBVReportItemService;

    private final InternalRouAssetNBVReportItemRepository rouAssetNBVReportItemRepository;

    private final RouAssetNBVReportItemQueryService rouAssetNBVReportItemQueryService;

    public RouAssetNBVReportItemResourceProd(
        InternalRouAssetNBVReportItemService rouAssetNBVReportItemService,
        InternalRouAssetNBVReportItemRepository rouAssetNBVReportItemRepository,
        RouAssetNBVReportItemQueryService rouAssetNBVReportItemQueryService
    ) {
        this.rouAssetNBVReportItemService = rouAssetNBVReportItemService;
        this.rouAssetNBVReportItemRepository = rouAssetNBVReportItemRepository;
        this.rouAssetNBVReportItemQueryService = rouAssetNBVReportItemQueryService;
    }

    /**
     * {@code GET  /rou-asset-nbv-report-items} : get all the rouAssetNBVReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouAssetNBVReportItems in body.
     */
    @GetMapping("/rou-asset-nbv-report-items")
    public ResponseEntity<List<RouAssetNBVReportItemDTO>> getAllRouAssetNBVReportItems(
        RouAssetNBVReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouAssetNBVReportItems by criteria: {}", criteria);
        Page<RouAssetNBVReportItemDTO> page = rouAssetNBVReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-asset-nbv-report-items/reports/{leasePeriodId}} : get all the rouAssetNBVReportItems for specified lease-period.
     *
     * @param pageable the pagination information.
     * @param leasePeriodId id of the lease period at the end of which we assess the NBV.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouAssetNBVReportItems in body.
     */
    @GetMapping("/rou-asset-nbv-report-items/reports/{leasePeriodId}")
    public ResponseEntity<List<RouAssetNBVReportItemDTO>> getAllRouAssetNBVReportItemsForSpecifiedLeasePeriod(
        Pageable pageable,
        @PathVariable long leasePeriodId
    ) {
        log.debug("REST request to get RouAssetNBVReportItems by for the lease-period with id: {}", leasePeriodId);
        Page<RouAssetNBVReportItemDTO> page = rouAssetNBVReportItemService.findAllAsAtPeriod(pageable, leasePeriodId);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-asset-nbv-report-items/count} : count all the rouAssetNBVReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-asset-nbv-report-items/count")
    public ResponseEntity<Long> countRouAssetNBVReportItems(RouAssetNBVReportItemCriteria criteria) {
        log.debug("REST request to count RouAssetNBVReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouAssetNBVReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-asset-nbv-report-items/:id} : get the "id" rouAssetNBVReportItem.
     *
     * @param id the id of the rouAssetNBVReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouAssetNBVReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-asset-nbv-report-items/{id}")
    public ResponseEntity<RouAssetNBVReportItemDTO> getRouAssetNBVReportItem(@PathVariable Long id) {
        log.debug("REST request to get RouAssetNBVReportItem : {}", id);
        Optional<RouAssetNBVReportItemDTO> rouAssetNBVReportItemDTO = rouAssetNBVReportItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouAssetNBVReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/rou-asset-nbv-report-items?query=:query} : search for the rouAssetNBVReportItem corresponding
     * to the query.
     *
     * @param query the query of the rouAssetNBVReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-asset-nbv-report-items")
    public ResponseEntity<List<RouAssetNBVReportItemDTO>> searchRouAssetNBVReportItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RouAssetNBVReportItems for query {}", query);
        Page<RouAssetNBVReportItemDTO> page = rouAssetNBVReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
