package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

import io.github.erp.repository.AssetAdditionsReportItemRepository;
import io.github.erp.service.AssetAdditionsReportItemQueryService;
import io.github.erp.service.AssetAdditionsReportItemService;
import io.github.erp.service.criteria.AssetAdditionsReportItemCriteria;
import io.github.erp.service.dto.AssetAdditionsReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetAdditionsReportItem}.
 */
@RestController
@RequestMapping("/api")
public class AssetAdditionsReportItemResource {

    private final Logger log = LoggerFactory.getLogger(AssetAdditionsReportItemResource.class);

    private final AssetAdditionsReportItemService assetAdditionsReportItemService;

    private final AssetAdditionsReportItemRepository assetAdditionsReportItemRepository;

    private final AssetAdditionsReportItemQueryService assetAdditionsReportItemQueryService;

    public AssetAdditionsReportItemResource(
        AssetAdditionsReportItemService assetAdditionsReportItemService,
        AssetAdditionsReportItemRepository assetAdditionsReportItemRepository,
        AssetAdditionsReportItemQueryService assetAdditionsReportItemQueryService
    ) {
        this.assetAdditionsReportItemService = assetAdditionsReportItemService;
        this.assetAdditionsReportItemRepository = assetAdditionsReportItemRepository;
        this.assetAdditionsReportItemQueryService = assetAdditionsReportItemQueryService;
    }

    /**
     * {@code GET  /asset-additions-report-items} : get all the assetAdditionsReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of assetAdditionsReportItems in body.
     */
    @GetMapping("/asset-additions-report-items")
    public ResponseEntity<List<AssetAdditionsReportItemDTO>> getAllAssetAdditionsReportItems(
        AssetAdditionsReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AssetAdditionsReportItems by criteria: {}", criteria);
        Page<AssetAdditionsReportItemDTO> page = assetAdditionsReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /asset-additions-report-items/count} : count all the assetAdditionsReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/asset-additions-report-items/count")
    public ResponseEntity<Long> countAssetAdditionsReportItems(AssetAdditionsReportItemCriteria criteria) {
        log.debug("REST request to count AssetAdditionsReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(assetAdditionsReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /asset-additions-report-items/:id} : get the "id" assetAdditionsReportItem.
     *
     * @param id the id of the assetAdditionsReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the assetAdditionsReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/asset-additions-report-items/{id}")
    public ResponseEntity<AssetAdditionsReportItemDTO> getAssetAdditionsReportItem(@PathVariable Long id) {
        log.debug("REST request to get AssetAdditionsReportItem : {}", id);
        Optional<AssetAdditionsReportItemDTO> assetAdditionsReportItemDTO = assetAdditionsReportItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(assetAdditionsReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/asset-additions-report-items?query=:query} : search for the assetAdditionsReportItem corresponding
     * to the query.
     *
     * @param query the query of the assetAdditionsReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/asset-additions-report-items")
    public ResponseEntity<List<AssetAdditionsReportItemDTO>> searchAssetAdditionsReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of AssetAdditionsReportItems for query {}", query);
        Page<AssetAdditionsReportItemDTO> page = assetAdditionsReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
