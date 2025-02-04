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

import io.github.erp.internal.repository.InternalWIPListItemRepository;
import io.github.erp.internal.service.wip.InternalWIPListItemService;
import io.github.erp.repository.WIPListItemRepository;
import io.github.erp.service.WIPListItemQueryService;
import io.github.erp.service.WIPListItemService;
import io.github.erp.service.criteria.WIPListItemCriteria;
import io.github.erp.service.dto.WIPListItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.WIPListItem}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
public class WIPListItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(WIPListItemResourceProd.class);

    private final InternalWIPListItemService wIPListItemService;

    private final InternalWIPListItemRepository wIPListItemRepository;

    private final WIPListItemQueryService wIPListItemQueryService;

    public WIPListItemResourceProd(
        InternalWIPListItemService wIPListItemService,
        InternalWIPListItemRepository wIPListItemRepository,
        WIPListItemQueryService wIPListItemQueryService
    ) {
        this.wIPListItemService = wIPListItemService;
        this.wIPListItemRepository = wIPListItemRepository;
        this.wIPListItemQueryService = wIPListItemQueryService;
    }

    /**
     * {@code GET  /wip-list-items} : get all the wIPListItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wIPListItems in body.
     */
    @GetMapping("/wip-list-items")
    public ResponseEntity<List<WIPListItemDTO>> getAllWIPListItems(WIPListItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WIPListItems by criteria: {}", criteria);
        Page<WIPListItemDTO> page = wIPListItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wip-list-items/count} : count all the wIPListItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/wip-list-items/count")
    public ResponseEntity<Long> countWIPListItems(WIPListItemCriteria criteria) {
        log.debug("REST request to count WIPListItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(wIPListItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /wip-list-items/:id} : get the "id" wIPListItem.
     *
     * @param id the id of the wIPListItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wIPListItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wip-list-items/{id}")
    public ResponseEntity<WIPListItemDTO> getWIPListItem(@PathVariable Long id) {
        log.debug("REST request to get WIPListItem : {}", id);
        Optional<WIPListItemDTO> wIPListItemDTO = wIPListItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wIPListItemDTO);
    }

    /**
     * {@code SEARCH  /_search/wip-list-items?query=:query} : search for the wIPListItem corresponding
     * to the query.
     *
     * @param query the query of the wIPListItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/wip-list-items")
    public ResponseEntity<List<WIPListItemDTO>> searchWIPListItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WIPListItems for query {}", query);
        Page<WIPListItemDTO> page = wIPListItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
