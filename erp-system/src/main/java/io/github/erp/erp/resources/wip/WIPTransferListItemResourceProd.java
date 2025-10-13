package io.github.erp.erp.resources.wip;

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

import io.github.erp.domain.WIPTransferListItem;
import io.github.erp.internal.service.wip.InternalWIPTransferListItemService;
import io.github.erp.repository.WIPTransferListItemRepository;
import io.github.erp.service.criteria.WIPTransferListItemCriteria;
import io.github.erp.service.dto.WIPTransferListItemDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
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
 * REST controller for managing {@link WIPTransferListItem}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
public class WIPTransferListItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(WIPTransferListItemResourceProd.class);

    private final InternalWIPTransferListItemService wIPTransferListItemService;

    public WIPTransferListItemResourceProd(
        @Qualifier("internalWIPTransferListItemServiceImpl") InternalWIPTransferListItemService wIPTransferListItemService
    ) {
        this.wIPTransferListItemService = wIPTransferListItemService;
    }

    /**
     * {@code GET  /wip-transfer-list-items} : get all the wIPTransferListItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wIPTransferListItems in body.
     */
    @GetMapping("/wip-transfer-list-items")
    public ResponseEntity<List<WIPTransferListItemDTO>> getAllWIPTransferListItems(
        WIPTransferListItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get WIPTransferListItems by criteria: {}", criteria);
        Page<WIPTransferListItemDTO> page = wIPTransferListItemService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wip-transfer-list-items/count} : count all the wIPTransferListItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/wip-transfer-list-items/count")
    public ResponseEntity<Long> countWIPTransferListItems(WIPTransferListItemCriteria criteria) {
        log.debug("REST request to count WIPTransferListItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(wIPTransferListItemService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /wip-transfer-list-items/:id} : get the "id" wIPTransferListItem.
     *
     * @param id the id of the wIPTransferListItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wIPTransferListItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wip-transfer-list-items/{id}")
    public ResponseEntity<WIPTransferListItemDTO> getWIPTransferListItem(@PathVariable Long id) {
        log.debug("REST request to get WIPTransferListItem : {}", id);
        Optional<WIPTransferListItemDTO> wIPTransferListItemDTO = wIPTransferListItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wIPTransferListItemDTO);
    }

    /**
     * {@code SEARCH  /_search/wip-transfer-list-items?query=:query} : search for the wIPTransferListItem corresponding
     * to the query.
     *
     * @param query the query of the wIPTransferListItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/wip-transfer-list-items")
    public ResponseEntity<List<WIPTransferListItemDTO>> searchWIPTransferListItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WIPTransferListItems for query {}", query);
        Page<WIPTransferListItemDTO> page = wIPTransferListItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
