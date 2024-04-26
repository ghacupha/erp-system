package io.github.erp.erp.resources.leases;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.repository.RouAssetListReportItemRepository;
import io.github.erp.service.RouAssetListReportItemQueryService;
import io.github.erp.service.RouAssetListReportItemService;
import io.github.erp.service.criteria.RouAssetListReportItemCriteria;
import io.github.erp.service.dto.RouAssetListReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouAssetListReportItem}.
 */
@RestController
@RequestMapping("/api/leases")
public class RouAssetListReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(RouAssetListReportItemResourceProd.class);

    private final RouAssetListReportItemService rouAssetListReportItemService;

    private final RouAssetListReportItemRepository rouAssetListReportItemRepository;

    private final RouAssetListReportItemQueryService rouAssetListReportItemQueryService;

    public RouAssetListReportItemResourceProd(
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
