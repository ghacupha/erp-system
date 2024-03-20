package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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

import io.github.erp.repository.RouAssetListReportItemRepository;
import io.github.erp.service.RouAssetListReportItemQueryService;
import io.github.erp.service.RouAssetListReportItemService;
import io.github.erp.service.criteria.RouAssetListReportItemCriteria;
import io.github.erp.service.dto.RouAssetListReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouAssetListReportItem}.
 */
@RestController
@RequestMapping("/api")
public class RouAssetListReportItemResource {

    private final Logger log = LoggerFactory.getLogger(RouAssetListReportItemResource.class);

    private final RouAssetListReportItemService rouAssetListReportItemService;

    private final RouAssetListReportItemRepository rouAssetListReportItemRepository;

    private final RouAssetListReportItemQueryService rouAssetListReportItemQueryService;

    public RouAssetListReportItemResource(
        RouAssetListReportItemService rouAssetListReportItemService,
        RouAssetListReportItemRepository rouAssetListReportItemRepository,
        RouAssetListReportItemQueryService rouAssetListReportItemQueryService
    ) {
        this.rouAssetListReportItemService = rouAssetListReportItemService;
        this.rouAssetListReportItemRepository = rouAssetListReportItemRepository;
        this.rouAssetListReportItemQueryService = rouAssetListReportItemQueryService;
    }

    /**
     * {@code GET  /rou-asset-list-report-items} : get all the rouAssetListReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouAssetListReportItems in body.
     */
    @GetMapping("/rou-asset-list-report-items")
    public ResponseEntity<List<RouAssetListReportItemDTO>> getAllRouAssetListReportItems(
        RouAssetListReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouAssetListReportItems by criteria: {}", criteria);
        Page<RouAssetListReportItemDTO> page = rouAssetListReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-asset-list-report-items/count} : count all the rouAssetListReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-asset-list-report-items/count")
    public ResponseEntity<Long> countRouAssetListReportItems(RouAssetListReportItemCriteria criteria) {
        log.debug("REST request to count RouAssetListReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouAssetListReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-asset-list-report-items/:id} : get the "id" rouAssetListReportItem.
     *
     * @param id the id of the rouAssetListReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouAssetListReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-asset-list-report-items/{id}")
    public ResponseEntity<RouAssetListReportItemDTO> getRouAssetListReportItem(@PathVariable Long id) {
        log.debug("REST request to get RouAssetListReportItem : {}", id);
        Optional<RouAssetListReportItemDTO> rouAssetListReportItemDTO = rouAssetListReportItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouAssetListReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/rou-asset-list-report-items?query=:query} : search for the rouAssetListReportItem corresponding
     * to the query.
     *
     * @param query the query of the rouAssetListReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-asset-list-report-items")
    public ResponseEntity<List<RouAssetListReportItemDTO>> searchRouAssetListReportItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RouAssetListReportItems for query {}", query);
        Page<RouAssetListReportItemDTO> page = rouAssetListReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
