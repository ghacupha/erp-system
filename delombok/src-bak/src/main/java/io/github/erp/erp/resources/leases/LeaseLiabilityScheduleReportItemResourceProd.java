package io.github.erp.erp.resources.leases;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.internal.service.leases.InternalLeaseLiabilityScheduleReportItemService;
import io.github.erp.repository.LeaseLiabilityScheduleReportItemRepository;
import io.github.erp.service.LeaseLiabilityScheduleReportItemQueryService;
import io.github.erp.service.LeaseLiabilityScheduleReportItemService;
import io.github.erp.service.criteria.LeaseLiabilityScheduleReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityScheduleReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiabilityScheduleReportItem}.
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseLiabilityScheduleReportItemResourceProd {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleReportItemResourceProd.class);

    private final InternalLeaseLiabilityScheduleReportItemService leaseLiabilityScheduleReportItemService;

    private final LeaseLiabilityScheduleReportItemRepository leaseLiabilityScheduleReportItemRepository;

    private final LeaseLiabilityScheduleReportItemQueryService leaseLiabilityScheduleReportItemQueryService;

    public LeaseLiabilityScheduleReportItemResourceProd(
        InternalLeaseLiabilityScheduleReportItemService leaseLiabilityScheduleReportItemService,
        LeaseLiabilityScheduleReportItemRepository leaseLiabilityScheduleReportItemRepository,
        LeaseLiabilityScheduleReportItemQueryService leaseLiabilityScheduleReportItemQueryService
    ) {
        this.leaseLiabilityScheduleReportItemService = leaseLiabilityScheduleReportItemService;
        this.leaseLiabilityScheduleReportItemRepository = leaseLiabilityScheduleReportItemRepository;
        this.leaseLiabilityScheduleReportItemQueryService = leaseLiabilityScheduleReportItemQueryService;
    }

    /**
     * {@code GET  /lease-liability-schedule-report-items} : get all the leaseLiabilityScheduleReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilityScheduleReportItems in body.
     */
    @GetMapping("/lease-liability-schedule-report-items")
    public ResponseEntity<List<LeaseLiabilityScheduleReportItemDTO>> getAllLeaseLiabilityScheduleReportItems(
        LeaseLiabilityScheduleReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityScheduleReportItems by criteria: {}", criteria);
        Page<LeaseLiabilityScheduleReportItemDTO> page = leaseLiabilityScheduleReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liability-schedule-report-items/count} : count all the leaseLiabilityScheduleReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liability-schedule-report-items/count")
    public ResponseEntity<Long> countLeaseLiabilityScheduleReportItems(LeaseLiabilityScheduleReportItemCriteria criteria) {
        log.debug("REST request to count LeaseLiabilityScheduleReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityScheduleReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liability-schedule-report-items/:id} : get the "id" leaseLiabilityScheduleReportItem.
     *
     * @param id the id of the leaseLiabilityScheduleReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityScheduleReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liability-schedule-report-items/{id}")
    public ResponseEntity<LeaseLiabilityScheduleReportItemDTO> getLeaseLiabilityScheduleReportItem(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityScheduleReportItem : {}", id);
        Optional<LeaseLiabilityScheduleReportItemDTO> leaseLiabilityScheduleReportItemDTO = leaseLiabilityScheduleReportItemService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(leaseLiabilityScheduleReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/lease-liability-schedule-report-items?query=:query} : search for the leaseLiabilityScheduleReportItem corresponding
     * to the query.
     *
     * @param query the query of the leaseLiabilityScheduleReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liability-schedule-report-items")
    public ResponseEntity<List<LeaseLiabilityScheduleReportItemDTO>> searchLeaseLiabilityScheduleReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseLiabilityScheduleReportItems for query {}", query);
        Page<LeaseLiabilityScheduleReportItemDTO> page = leaseLiabilityScheduleReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
