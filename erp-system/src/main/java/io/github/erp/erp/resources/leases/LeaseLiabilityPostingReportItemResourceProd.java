package io.github.erp.erp.resources.leases;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.internal.service.leases.InternalLeaseLiabilityPostingReportItemService;
import io.github.erp.repository.LeaseLiabilityPostingReportItemRepository;
import io.github.erp.service.LeaseLiabilityPostingReportItemQueryService;
import io.github.erp.service.LeaseLiabilityPostingReportItemService;
import io.github.erp.service.criteria.LeaseLiabilityPostingReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityPostingReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiabilityPostingReportItem}.
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseLiabilityPostingReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityPostingReportItemResourceProd.class);

    private final InternalLeaseLiabilityPostingReportItemService leaseLiabilityPostingReportItemService;

    private final LeaseLiabilityPostingReportItemRepository leaseLiabilityPostingReportItemRepository;

    private final LeaseLiabilityPostingReportItemQueryService leaseLiabilityPostingReportItemQueryService;

    public LeaseLiabilityPostingReportItemResourceProd(
        InternalLeaseLiabilityPostingReportItemService leaseLiabilityPostingReportItemService,
        LeaseLiabilityPostingReportItemRepository leaseLiabilityPostingReportItemRepository,
        LeaseLiabilityPostingReportItemQueryService leaseLiabilityPostingReportItemQueryService
    ) {
        this.leaseLiabilityPostingReportItemService = leaseLiabilityPostingReportItemService;
        this.leaseLiabilityPostingReportItemRepository = leaseLiabilityPostingReportItemRepository;
        this.leaseLiabilityPostingReportItemQueryService = leaseLiabilityPostingReportItemQueryService;
    }

    /**
     * {@code GET  /lease-liability-posting-report-items} : get all the leaseLiabilityPostingReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilityPostingReportItems in body.
     */
    @GetMapping("/lease-liability-posting-report-items")
    public ResponseEntity<List<LeaseLiabilityPostingReportItemDTO>> getAllLeaseLiabilityPostingReportItems(
        LeaseLiabilityPostingReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityPostingReportItems by criteria: {}", criteria);
        Page<LeaseLiabilityPostingReportItemDTO> page = leaseLiabilityPostingReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liability-posting-report-items/count} : count all the leaseLiabilityPostingReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liability-posting-report-items/count")
    public ResponseEntity<Long> countLeaseLiabilityPostingReportItems(LeaseLiabilityPostingReportItemCriteria criteria) {
        log.debug("REST request to count LeaseLiabilityPostingReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityPostingReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liability-posting-report-items/:id} : get the "id" leaseLiabilityPostingReportItem.
     *
     * @param id the id of the leaseLiabilityPostingReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityPostingReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liability-posting-report-items/{id}")
    public ResponseEntity<LeaseLiabilityPostingReportItemDTO> getLeaseLiabilityPostingReportItem(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityPostingReportItem : {}", id);
        Optional<LeaseLiabilityPostingReportItemDTO> leaseLiabilityPostingReportItemDTO = leaseLiabilityPostingReportItemService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(leaseLiabilityPostingReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/lease-liability-posting-report-items?query=:query} : search for the leaseLiabilityPostingReportItem corresponding
     * to the query.
     *
     * @param query the query of the leaseLiabilityPostingReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liability-posting-report-items")
    public ResponseEntity<List<LeaseLiabilityPostingReportItemDTO>> searchLeaseLiabilityPostingReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseLiabilityPostingReportItems for query {}", query);
        Page<LeaseLiabilityPostingReportItemDTO> page = leaseLiabilityPostingReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
