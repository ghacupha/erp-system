package io.github.erp.erp.resources.leases;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.repository.RouDepreciationPostingReportItemRepository;
import io.github.erp.service.RouDepreciationPostingReportItemQueryService;
import io.github.erp.service.RouDepreciationPostingReportItemService;
import io.github.erp.service.criteria.RouDepreciationPostingReportItemCriteria;
import io.github.erp.service.dto.RouDepreciationPostingReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouDepreciationPostingReportItem}.
 */
@RestController
@RequestMapping("/api/leases")
public class RouDepreciationPostingReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationPostingReportItemResourceProd.class);

    private final RouDepreciationPostingReportItemService rouDepreciationPostingReportItemService;

    private final RouDepreciationPostingReportItemRepository rouDepreciationPostingReportItemRepository;

    private final RouDepreciationPostingReportItemQueryService rouDepreciationPostingReportItemQueryService;

    public RouDepreciationPostingReportItemResourceProd(
        RouDepreciationPostingReportItemService rouDepreciationPostingReportItemService,
        RouDepreciationPostingReportItemRepository rouDepreciationPostingReportItemRepository,
        RouDepreciationPostingReportItemQueryService rouDepreciationPostingReportItemQueryService
    ) {
        this.rouDepreciationPostingReportItemService = rouDepreciationPostingReportItemService;
        this.rouDepreciationPostingReportItemRepository = rouDepreciationPostingReportItemRepository;
        this.rouDepreciationPostingReportItemQueryService = rouDepreciationPostingReportItemQueryService;
    }

    /**
     * {@code GET  /rou-depreciation-posting-report-items} : get all the rouDepreciationPostingReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouDepreciationPostingReportItems in body.
     */
    @GetMapping("/rou-depreciation-posting-report-items")
    public ResponseEntity<List<RouDepreciationPostingReportItemDTO>> getAllRouDepreciationPostingReportItems(
        RouDepreciationPostingReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouDepreciationPostingReportItems by criteria: {}", criteria);
        Page<RouDepreciationPostingReportItemDTO> page = rouDepreciationPostingReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-depreciation-posting-report-items/count} : count all the rouDepreciationPostingReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-depreciation-posting-report-items/count")
    public ResponseEntity<Long> countRouDepreciationPostingReportItems(RouDepreciationPostingReportItemCriteria criteria) {
        log.debug("REST request to count RouDepreciationPostingReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouDepreciationPostingReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-depreciation-posting-report-items/:id} : get the "id" rouDepreciationPostingReportItem.
     *
     * @param id the id of the rouDepreciationPostingReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouDepreciationPostingReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-depreciation-posting-report-items/{id}")
    public ResponseEntity<RouDepreciationPostingReportItemDTO> getRouDepreciationPostingReportItem(@PathVariable Long id) {
        log.debug("REST request to get RouDepreciationPostingReportItem : {}", id);
        Optional<RouDepreciationPostingReportItemDTO> rouDepreciationPostingReportItemDTO = rouDepreciationPostingReportItemService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(rouDepreciationPostingReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/rou-depreciation-posting-report-items?query=:query} : search for the rouDepreciationPostingReportItem corresponding
     * to the query.
     *
     * @param query the query of the rouDepreciationPostingReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-depreciation-posting-report-items")
    public ResponseEntity<List<RouDepreciationPostingReportItemDTO>> searchRouDepreciationPostingReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of RouDepreciationPostingReportItems for query {}", query);
        Page<RouDepreciationPostingReportItemDTO> page = rouDepreciationPostingReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
