package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.LeaseLiabilityByAccountReportItemRepository;
import io.github.erp.service.LeaseLiabilityByAccountReportItemQueryService;
import io.github.erp.service.LeaseLiabilityByAccountReportItemService;
import io.github.erp.service.criteria.LeaseLiabilityByAccountReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityByAccountReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiabilityByAccountReportItem}.
 */
@RestController
@RequestMapping("/api")
public class LeaseLiabilityByAccountReportItemResource {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityByAccountReportItemResource.class);

    private final LeaseLiabilityByAccountReportItemService leaseLiabilityByAccountReportItemService;

    private final LeaseLiabilityByAccountReportItemRepository leaseLiabilityByAccountReportItemRepository;

    private final LeaseLiabilityByAccountReportItemQueryService leaseLiabilityByAccountReportItemQueryService;

    public LeaseLiabilityByAccountReportItemResource(
        LeaseLiabilityByAccountReportItemService leaseLiabilityByAccountReportItemService,
        LeaseLiabilityByAccountReportItemRepository leaseLiabilityByAccountReportItemRepository,
        LeaseLiabilityByAccountReportItemQueryService leaseLiabilityByAccountReportItemQueryService
    ) {
        this.leaseLiabilityByAccountReportItemService = leaseLiabilityByAccountReportItemService;
        this.leaseLiabilityByAccountReportItemRepository = leaseLiabilityByAccountReportItemRepository;
        this.leaseLiabilityByAccountReportItemQueryService = leaseLiabilityByAccountReportItemQueryService;
    }

    /**
     * {@code GET  /lease-liability-by-account-report-items} : get all the leaseLiabilityByAccountReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilityByAccountReportItems in body.
     */
    @GetMapping("/lease-liability-by-account-report-items")
    public ResponseEntity<List<LeaseLiabilityByAccountReportItemDTO>> getAllLeaseLiabilityByAccountReportItems(
        LeaseLiabilityByAccountReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityByAccountReportItems by criteria: {}", criteria);
        Page<LeaseLiabilityByAccountReportItemDTO> page = leaseLiabilityByAccountReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liability-by-account-report-items/count} : count all the leaseLiabilityByAccountReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liability-by-account-report-items/count")
    public ResponseEntity<Long> countLeaseLiabilityByAccountReportItems(LeaseLiabilityByAccountReportItemCriteria criteria) {
        log.debug("REST request to count LeaseLiabilityByAccountReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityByAccountReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liability-by-account-report-items/:id} : get the "id" leaseLiabilityByAccountReportItem.
     *
     * @param id the id of the leaseLiabilityByAccountReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityByAccountReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liability-by-account-report-items/{id}")
    public ResponseEntity<LeaseLiabilityByAccountReportItemDTO> getLeaseLiabilityByAccountReportItem(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityByAccountReportItem : {}", id);
        Optional<LeaseLiabilityByAccountReportItemDTO> leaseLiabilityByAccountReportItemDTO = leaseLiabilityByAccountReportItemService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(leaseLiabilityByAccountReportItemDTO);
    }

    /**
     * {@code SEARCH  /_search/lease-liability-by-account-report-items?query=:query} : search for the leaseLiabilityByAccountReportItem corresponding
     * to the query.
     *
     * @param query the query of the leaseLiabilityByAccountReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liability-by-account-report-items")
    public ResponseEntity<List<LeaseLiabilityByAccountReportItemDTO>> searchLeaseLiabilityByAccountReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseLiabilityByAccountReportItems for query {}", query);
        Page<LeaseLiabilityByAccountReportItemDTO> page = leaseLiabilityByAccountReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
