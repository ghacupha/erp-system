package io.github.erp.erp.resources.depreciation;

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
import io.github.erp.repository.DepreciationEntryReportItemRepository;
import io.github.erp.service.DepreciationEntryReportItemQueryService;
import io.github.erp.service.DepreciationEntryReportItemService;
import io.github.erp.service.criteria.DepreciationEntryReportItemCriteria;
import io.github.erp.service.dto.DepreciationEntryReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DepreciationEntryReportItem}.
 */
@RestController
@RequestMapping("/api/fixed-asset/report")
public class DepreciationEntryReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(DepreciationEntryReportItemResourceProd.class);

    private final DepreciationEntryReportItemService depreciationEntryReportItemService;

    private final DepreciationEntryReportItemRepository depreciationEntryReportItemRepository;

    private final DepreciationEntryReportItemQueryService depreciationEntryReportItemQueryService;

    public DepreciationEntryReportItemResourceProd(
        DepreciationEntryReportItemService depreciationEntryReportItemService,
        DepreciationEntryReportItemRepository depreciationEntryReportItemRepository,
        DepreciationEntryReportItemQueryService depreciationEntryReportItemQueryService
    ) {
        this.depreciationEntryReportItemService = depreciationEntryReportItemService;
        this.depreciationEntryReportItemRepository = depreciationEntryReportItemRepository;
        this.depreciationEntryReportItemQueryService = depreciationEntryReportItemQueryService;
    }

    /**
     * {@code GET  /depreciation-entry-report-items} : get all the depreciationEntryReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depreciationEntryReportItems in body.
     */
    @GetMapping("/depreciation-entry-report-items")
    public ResponseEntity<List<DepreciationEntryReportItemDTO>> getAllDepreciationEntryReportItems(
        DepreciationEntryReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get DepreciationEntryReportItems by criteria: {}", criteria);
        Page<DepreciationEntryReportItemDTO> page = depreciationEntryReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depreciation-entry-report-items/count} : count all the depreciationEntryReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/depreciation-entry-report-items/count")
    public ResponseEntity<Long> countDepreciationEntryReportItems(DepreciationEntryReportItemCriteria criteria) {
        log.debug("REST request to count DepreciationEntryReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(depreciationEntryReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /depreciation-entry-report-items/:id} : get the "id" depreciationEntryReportItem.
     *
     * @param id the id of the depreciationEntryReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depreciationEntryReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depreciation-entry-report-items/{id}")
    public ResponseEntity<DepreciationEntryReportItemDTO> getDepreciationEntryReportItem(@PathVariable Long id) {
        log.debug("REST request to get DepreciationEntryReportItem : {}", id);
        Optional<DepreciationEntryReportItemDTO> depreciationEntryReportItemDTO = depreciationEntryReportItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depreciationEntryReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/depreciation-entry-report-items?query=:query} : search for the depreciationEntryReportItem corresponding
     * to the query.
     *
     * @param query the query of the depreciationEntryReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/depreciation-entry-report-items")
    public ResponseEntity<List<DepreciationEntryReportItemDTO>> searchDepreciationEntryReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of DepreciationEntryReportItems for query {}", query);
        Page<DepreciationEntryReportItemDTO> page = depreciationEntryReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
