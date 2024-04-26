package io.github.erp.erp.resources.assets;

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
import io.github.erp.repository.AssetAdditionsReportItemRepository;
import io.github.erp.service.AssetAdditionsReportItemQueryService;
import io.github.erp.service.AssetAdditionsReportItemService;
import io.github.erp.service.criteria.AssetAdditionsReportItemCriteria;
import io.github.erp.service.dto.AssetAdditionsReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AssetAdditionsReportItem}.
 */
@RestController
@RequestMapping("/api/fixed-asset/report")
public class AssetAdditionsReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(AssetAdditionsReportItemResourceProd.class);

    private final AssetAdditionsReportItemService assetAdditionsReportItemService;

    private final AssetAdditionsReportItemRepository assetAdditionsReportItemRepository;

    private final AssetAdditionsReportItemQueryService assetAdditionsReportItemQueryService;

    public AssetAdditionsReportItemResourceProd(
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
