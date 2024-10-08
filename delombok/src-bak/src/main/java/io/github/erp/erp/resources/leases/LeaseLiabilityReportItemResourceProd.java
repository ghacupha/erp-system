package io.github.erp.erp.resources.leases;

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
import io.github.erp.internal.service.leases.InternalLeaseLiabilityReportItemService;
import io.github.erp.repository.LeaseLiabilityReportItemRepository;
import io.github.erp.service.LeaseLiabilityReportItemQueryService;
import io.github.erp.service.LeaseLiabilityReportItemService;
import io.github.erp.service.criteria.LeaseLiabilityReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiabilityReportItem}.
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseLiabilityReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityReportItemResourceProd.class);

    private final InternalLeaseLiabilityReportItemService leaseLiabilityReportItemService;

    private final LeaseLiabilityReportItemRepository leaseLiabilityReportItemRepository;

    private final LeaseLiabilityReportItemQueryService leaseLiabilityReportItemQueryService;

    public LeaseLiabilityReportItemResourceProd(
        InternalLeaseLiabilityReportItemService leaseLiabilityReportItemService,
        LeaseLiabilityReportItemRepository leaseLiabilityReportItemRepository,
        LeaseLiabilityReportItemQueryService leaseLiabilityReportItemQueryService
    ) {
        this.leaseLiabilityReportItemService = leaseLiabilityReportItemService;
        this.leaseLiabilityReportItemRepository = leaseLiabilityReportItemRepository;
        this.leaseLiabilityReportItemQueryService = leaseLiabilityReportItemQueryService;
    }

    /**
     * {@code GET  /lease-liability-report-items} : get all the leaseLiabilityReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilityReportItems in body.
     */
    @GetMapping("/lease-liability-report-items")
    public ResponseEntity<List<LeaseLiabilityReportItemDTO>> getAllLeaseLiabilityReportItems(
        LeaseLiabilityReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityReportItems by criteria: {}", criteria);
        Page<LeaseLiabilityReportItemDTO> page = leaseLiabilityReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liability-report-items/count} : count all the leaseLiabilityReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liability-report-items/count")
    public ResponseEntity<Long> countLeaseLiabilityReportItems(LeaseLiabilityReportItemCriteria criteria) {
        log.debug("REST request to count LeaseLiabilityReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liability-report-items/:id} : get the "id" leaseLiabilityReportItem.
     *
     * @param id the id of the leaseLiabilityReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liability-report-items/{id}")
    public ResponseEntity<LeaseLiabilityReportItemDTO> getLeaseLiabilityReportItem(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityReportItem : {}", id);
        Optional<LeaseLiabilityReportItemDTO> leaseLiabilityReportItemDTO = leaseLiabilityReportItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseLiabilityReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/lease-liability-report-items?query=:query} : search for the leaseLiabilityReportItem corresponding
     * to the query.
     *
     * @param query the query of the leaseLiabilityReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liability-report-items")
    public ResponseEntity<List<LeaseLiabilityReportItemDTO>> searchLeaseLiabilityReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseLiabilityReportItems for query {}", query);
        Page<LeaseLiabilityReportItemDTO> page = leaseLiabilityReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
