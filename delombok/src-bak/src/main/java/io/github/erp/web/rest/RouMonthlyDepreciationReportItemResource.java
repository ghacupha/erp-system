package io.github.erp.web.rest;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.RouMonthlyDepreciationReportItemRepository;
import io.github.erp.service.RouMonthlyDepreciationReportItemQueryService;
import io.github.erp.service.RouMonthlyDepreciationReportItemService;
import io.github.erp.service.criteria.RouMonthlyDepreciationReportItemCriteria;
import io.github.erp.service.dto.RouMonthlyDepreciationReportItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouMonthlyDepreciationReportItem}.
 */
@RestController
@RequestMapping("/api")
public class RouMonthlyDepreciationReportItemResource {

    private final Logger log = LoggerFactory.getLogger(RouMonthlyDepreciationReportItemResource.class);

    private static final String ENTITY_NAME = "rouMonthlyDepreciationReportItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouMonthlyDepreciationReportItemService rouMonthlyDepreciationReportItemService;

    private final RouMonthlyDepreciationReportItemRepository rouMonthlyDepreciationReportItemRepository;

    private final RouMonthlyDepreciationReportItemQueryService rouMonthlyDepreciationReportItemQueryService;

    public RouMonthlyDepreciationReportItemResource(
        RouMonthlyDepreciationReportItemService rouMonthlyDepreciationReportItemService,
        RouMonthlyDepreciationReportItemRepository rouMonthlyDepreciationReportItemRepository,
        RouMonthlyDepreciationReportItemQueryService rouMonthlyDepreciationReportItemQueryService
    ) {
        this.rouMonthlyDepreciationReportItemService = rouMonthlyDepreciationReportItemService;
        this.rouMonthlyDepreciationReportItemRepository = rouMonthlyDepreciationReportItemRepository;
        this.rouMonthlyDepreciationReportItemQueryService = rouMonthlyDepreciationReportItemQueryService;
    }

    /**
     * {@code POST  /rou-monthly-depreciation-report-items} : Create a new rouMonthlyDepreciationReportItem.
     *
     * @param rouMonthlyDepreciationReportItemDTO the rouMonthlyDepreciationReportItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouMonthlyDepreciationReportItemDTO, or with status {@code 400 (Bad Request)} if the rouMonthlyDepreciationReportItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-monthly-depreciation-report-items")
    public ResponseEntity<RouMonthlyDepreciationReportItemDTO> createRouMonthlyDepreciationReportItem(
        @RequestBody RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RouMonthlyDepreciationReportItem : {}", rouMonthlyDepreciationReportItemDTO);
        if (rouMonthlyDepreciationReportItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouMonthlyDepreciationReportItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouMonthlyDepreciationReportItemDTO result = rouMonthlyDepreciationReportItemService.save(rouMonthlyDepreciationReportItemDTO);
        return ResponseEntity
            .created(new URI("/api/rou-monthly-depreciation-report-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-monthly-depreciation-report-items/:id} : Updates an existing rouMonthlyDepreciationReportItem.
     *
     * @param id the id of the rouMonthlyDepreciationReportItemDTO to save.
     * @param rouMonthlyDepreciationReportItemDTO the rouMonthlyDepreciationReportItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouMonthlyDepreciationReportItemDTO,
     * or with status {@code 400 (Bad Request)} if the rouMonthlyDepreciationReportItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouMonthlyDepreciationReportItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-monthly-depreciation-report-items/{id}")
    public ResponseEntity<RouMonthlyDepreciationReportItemDTO> updateRouMonthlyDepreciationReportItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouMonthlyDepreciationReportItem : {}, {}", id, rouMonthlyDepreciationReportItemDTO);
        if (rouMonthlyDepreciationReportItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouMonthlyDepreciationReportItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouMonthlyDepreciationReportItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouMonthlyDepreciationReportItemDTO result = rouMonthlyDepreciationReportItemService.save(rouMonthlyDepreciationReportItemDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    rouMonthlyDepreciationReportItemDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /rou-monthly-depreciation-report-items/:id} : Partial updates given fields of an existing rouMonthlyDepreciationReportItem, field will ignore if it is null
     *
     * @param id the id of the rouMonthlyDepreciationReportItemDTO to save.
     * @param rouMonthlyDepreciationReportItemDTO the rouMonthlyDepreciationReportItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouMonthlyDepreciationReportItemDTO,
     * or with status {@code 400 (Bad Request)} if the rouMonthlyDepreciationReportItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouMonthlyDepreciationReportItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouMonthlyDepreciationReportItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-monthly-depreciation-report-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouMonthlyDepreciationReportItemDTO> partialUpdateRouMonthlyDepreciationReportItem(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RouMonthlyDepreciationReportItemDTO rouMonthlyDepreciationReportItemDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update RouMonthlyDepreciationReportItem partially : {}, {}",
            id,
            rouMonthlyDepreciationReportItemDTO
        );
        if (rouMonthlyDepreciationReportItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouMonthlyDepreciationReportItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouMonthlyDepreciationReportItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouMonthlyDepreciationReportItemDTO> result = rouMonthlyDepreciationReportItemService.partialUpdate(
            rouMonthlyDepreciationReportItemDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouMonthlyDepreciationReportItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-monthly-depreciation-report-items} : get all the rouMonthlyDepreciationReportItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouMonthlyDepreciationReportItems in body.
     */
    @GetMapping("/rou-monthly-depreciation-report-items")
    public ResponseEntity<List<RouMonthlyDepreciationReportItemDTO>> getAllRouMonthlyDepreciationReportItems(
        RouMonthlyDepreciationReportItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RouMonthlyDepreciationReportItems by criteria: {}", criteria);
        Page<RouMonthlyDepreciationReportItemDTO> page = rouMonthlyDepreciationReportItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-monthly-depreciation-report-items/count} : count all the rouMonthlyDepreciationReportItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-monthly-depreciation-report-items/count")
    public ResponseEntity<Long> countRouMonthlyDepreciationReportItems(RouMonthlyDepreciationReportItemCriteria criteria) {
        log.debug("REST request to count RouMonthlyDepreciationReportItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouMonthlyDepreciationReportItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-monthly-depreciation-report-items/:id} : get the "id" rouMonthlyDepreciationReportItem.
     *
     * @param id the id of the rouMonthlyDepreciationReportItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouMonthlyDepreciationReportItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-monthly-depreciation-report-items/{id}")
    public ResponseEntity<RouMonthlyDepreciationReportItemDTO> getRouMonthlyDepreciationReportItem(@PathVariable Long id) {
        log.debug("REST request to get RouMonthlyDepreciationReportItem : {}", id);
        Optional<RouMonthlyDepreciationReportItemDTO> rouMonthlyDepreciationReportItemDTO = rouMonthlyDepreciationReportItemService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(rouMonthlyDepreciationReportItemDTO);
    }

    /**
     * {@code DELETE  /rou-monthly-depreciation-report-items/:id} : delete the "id" rouMonthlyDepreciationReportItem.
     *
     * @param id the id of the rouMonthlyDepreciationReportItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-monthly-depreciation-report-items/{id}")
    public ResponseEntity<Void> deleteRouMonthlyDepreciationReportItem(@PathVariable Long id) {
        log.debug("REST request to delete RouMonthlyDepreciationReportItem : {}", id);
        rouMonthlyDepreciationReportItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-monthly-depreciation-report-items?query=:query} : search for the rouMonthlyDepreciationReportItem corresponding
     * to the query.
     *
     * @param query the query of the rouMonthlyDepreciationReportItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-monthly-depreciation-report-items")
    public ResponseEntity<List<RouMonthlyDepreciationReportItemDTO>> searchRouMonthlyDepreciationReportItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of RouMonthlyDepreciationReportItems for query {}", query);
        Page<RouMonthlyDepreciationReportItemDTO> page = rouMonthlyDepreciationReportItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
