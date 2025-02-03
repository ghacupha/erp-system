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

import io.github.erp.internal.service.wip.InternalWIPTransferListReportService;
import io.github.erp.repository.WIPTransferListReportRepository;
import io.github.erp.service.WIPTransferListReportQueryService;
import io.github.erp.service.WIPTransferListReportService;
import io.github.erp.service.criteria.WIPTransferListReportCriteria;
import io.github.erp.service.dto.WIPTransferListReportDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.WIPTransferListReport}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
public class WIPTransferListReportResourceProd {

    private final Logger log = LoggerFactory.getLogger(WIPTransferListReportResourceProd.class);

    private static final String ENTITY_NAME = "wIPTransferListReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalWIPTransferListReportService wIPTransferListReportService;

    private final WIPTransferListReportRepository wIPTransferListReportRepository;

    private final WIPTransferListReportQueryService wIPTransferListReportQueryService;

    public WIPTransferListReportResourceProd(
        InternalWIPTransferListReportService wIPTransferListReportService,
        WIPTransferListReportRepository wIPTransferListReportRepository,
        WIPTransferListReportQueryService wIPTransferListReportQueryService
    ) {
        this.wIPTransferListReportService = wIPTransferListReportService;
        this.wIPTransferListReportRepository = wIPTransferListReportRepository;
        this.wIPTransferListReportQueryService = wIPTransferListReportQueryService;
    }

    /**
     * {@code POST  /wip-transfer-list-reports} : Create a new wIPTransferListReport.
     *
     * @param wIPTransferListReportDTO the wIPTransferListReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wIPTransferListReportDTO, or with status {@code 400 (Bad Request)} if the wIPTransferListReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wip-transfer-list-reports")
    public ResponseEntity<WIPTransferListReportDTO> createWIPTransferListReport(
        @Valid @RequestBody WIPTransferListReportDTO wIPTransferListReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to save WIPTransferListReport : {}", wIPTransferListReportDTO);
        if (wIPTransferListReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new wIPTransferListReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WIPTransferListReportDTO result = wIPTransferListReportService.save(wIPTransferListReportDTO);
        return ResponseEntity
            .created(new URI("/api/wip-transfer-list-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wip-transfer-list-reports/:id} : Updates an existing wIPTransferListReport.
     *
     * @param id the id of the wIPTransferListReportDTO to save.
     * @param wIPTransferListReportDTO the wIPTransferListReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wIPTransferListReportDTO,
     * or with status {@code 400 (Bad Request)} if the wIPTransferListReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wIPTransferListReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wip-transfer-list-reports/{id}")
    public ResponseEntity<WIPTransferListReportDTO> updateWIPTransferListReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WIPTransferListReportDTO wIPTransferListReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WIPTransferListReport : {}, {}", id, wIPTransferListReportDTO);
        if (wIPTransferListReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wIPTransferListReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wIPTransferListReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WIPTransferListReportDTO result = wIPTransferListReportService.save(wIPTransferListReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wIPTransferListReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wip-transfer-list-reports/:id} : Partial updates given fields of an existing wIPTransferListReport, field will ignore if it is null
     *
     * @param id the id of the wIPTransferListReportDTO to save.
     * @param wIPTransferListReportDTO the wIPTransferListReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wIPTransferListReportDTO,
     * or with status {@code 400 (Bad Request)} if the wIPTransferListReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the wIPTransferListReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the wIPTransferListReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wip-transfer-list-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WIPTransferListReportDTO> partialUpdateWIPTransferListReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WIPTransferListReportDTO wIPTransferListReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WIPTransferListReport partially : {}, {}", id, wIPTransferListReportDTO);
        if (wIPTransferListReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wIPTransferListReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wIPTransferListReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WIPTransferListReportDTO> result = wIPTransferListReportService.partialUpdate(wIPTransferListReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wIPTransferListReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wip-transfer-list-reports} : get all the wIPTransferListReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wIPTransferListReports in body.
     */
    @GetMapping("/wip-transfer-list-reports")
    public ResponseEntity<List<WIPTransferListReportDTO>> getAllWIPTransferListReports(
        WIPTransferListReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get WIPTransferListReports by criteria: {}", criteria);
        Page<WIPTransferListReportDTO> page = wIPTransferListReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wip-transfer-list-reports/count} : count all the wIPTransferListReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/wip-transfer-list-reports/count")
    public ResponseEntity<Long> countWIPTransferListReports(WIPTransferListReportCriteria criteria) {
        log.debug("REST request to count WIPTransferListReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(wIPTransferListReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /wip-transfer-list-reports/:id} : get the "id" wIPTransferListReport.
     *
     * @param id the id of the wIPTransferListReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wIPTransferListReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wip-transfer-list-reports/{id}")
    public ResponseEntity<WIPTransferListReportDTO> getWIPTransferListReport(@PathVariable Long id) {
        log.debug("REST request to get WIPTransferListReport : {}", id);
        Optional<WIPTransferListReportDTO> wIPTransferListReportDTO = wIPTransferListReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wIPTransferListReportDTO);
    }

    /**
     * {@code DELETE  /wip-transfer-list-reports/:id} : delete the "id" wIPTransferListReport.
     *
     * @param id the id of the wIPTransferListReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wip-transfer-list-reports/{id}")
    public ResponseEntity<Void> deleteWIPTransferListReport(@PathVariable Long id) {
        log.debug("REST request to delete WIPTransferListReport : {}", id);
        wIPTransferListReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/wip-transfer-list-reports?query=:query} : search for the wIPTransferListReport corresponding
     * to the query.
     *
     * @param query the query of the wIPTransferListReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/wip-transfer-list-reports")
    public ResponseEntity<List<WIPTransferListReportDTO>> searchWIPTransferListReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WIPTransferListReports for query {}", query);
        Page<WIPTransferListReportDTO> page = wIPTransferListReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
