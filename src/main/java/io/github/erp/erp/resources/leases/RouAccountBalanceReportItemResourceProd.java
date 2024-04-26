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
import io.github.erp.repository.RouAccountBalanceReportItemRepository;
import io.github.erp.service.RouAccountBalanceReportItemQueryService;
import io.github.erp.service.RouAccountBalanceReportItemService;
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

    private final RouAccountBalanceReportItemService rouAccountBalanceReportItemService;

    private final RouAccountBalanceReportItemRepository rouAccountBalanceReportItemRepository;

    private final RouAccountBalanceReportItemQueryService rouAccountBalanceReportItemQueryService;

    public RouAccountBalanceReportItemResourceProd(
        RouAccountBalanceReportItemService rouAccountBalanceReportItemService,
        RouAccountBalanceReportItemRepository rouAccountBalanceReportItemRepository,
        RouAccountBalanceReportItemQueryService rouAccountBalanceReportItemQueryService
    ) {
        this.rouAccountBalanceReportItemService = rouAccountBalanceReportItemService;
        this.rouAccountBalanceReportItemRepository = rouAccountBalanceReportItemRepository;
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
