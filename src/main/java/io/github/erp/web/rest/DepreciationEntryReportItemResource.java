package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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

import io.github.erp.repository.DepreciationEntryReportItemRepository;
import io.github.erp.service.DepreciationEntryReportItemQueryService;
import io.github.erp.service.DepreciationEntryReportItemService;
import io.github.erp.service.criteria.DepreciationEntryReportItemCriteria;
import io.github.erp.service.dto.DepreciationEntryReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DepreciationEntryReportItem}.
 */
@RestController
@RequestMapping("/api")
public class DepreciationEntryReportItemResource {

    private final Logger log = LoggerFactory.getLogger(DepreciationEntryReportItemResource.class);

    private final DepreciationEntryReportItemService depreciationEntryReportItemService;

    private final DepreciationEntryReportItemRepository depreciationEntryReportItemRepository;

    private final DepreciationEntryReportItemQueryService depreciationEntryReportItemQueryService;

    public DepreciationEntryReportItemResource(
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
