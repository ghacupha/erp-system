package io.github.erp.erp.resources.wip;

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
import io.github.erp.internal.service.wip.InternalWIPListReportService;
import io.github.erp.repository.WIPListReportRepository;
import io.github.erp.service.criteria.WIPListReportCriteria;
import io.github.erp.service.dto.WIPListReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.WIPListReport}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
public class WIPListReportResourceProd {

    private final Logger log = LoggerFactory.getLogger(WIPListReportResourceProd.class);

    private static final String ENTITY_NAME = "wIPListReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalWIPListReportService wIPListReportService;

    private final WIPListReportRepository wIPListReportRepository;

    public WIPListReportResourceProd(
        InternalWIPListReportService wIPListReportService,
        WIPListReportRepository wIPListReportRepository
    ) {
        this.wIPListReportService = wIPListReportService;
        this.wIPListReportRepository = wIPListReportRepository;
    }

    /**
     * {@code POST  /wip-list-reports} : Create a new wIPListReport.
     *
     * @param wIPListReportDTO the wIPListReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new wIPListReportDTO, or with status {@code 400 (Bad Request)} if the wIPListReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/wip-list-reports")
    public ResponseEntity<WIPListReportDTO> createWIPListReport(@Valid @RequestBody WIPListReportDTO wIPListReportDTO)
        throws URISyntaxException {
        log.debug("REST request to save WIPListReport : {}", wIPListReportDTO);
        if (wIPListReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new wIPListReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WIPListReportDTO result = wIPListReportService.save(wIPListReportDTO);
        return ResponseEntity
            .created(new URI("/api/wip-list-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /wip-list-reports/:id} : Updates an existing wIPListReport.
     *
     * @param id the id of the wIPListReportDTO to save.
     * @param wIPListReportDTO the wIPListReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wIPListReportDTO,
     * or with status {@code 400 (Bad Request)} if the wIPListReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the wIPListReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/wip-list-reports/{id}")
    public ResponseEntity<WIPListReportDTO> updateWIPListReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WIPListReportDTO wIPListReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WIPListReport : {}, {}", id, wIPListReportDTO);
        if (wIPListReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wIPListReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wIPListReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WIPListReportDTO result = wIPListReportService.save(wIPListReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wIPListReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /wip-list-reports/:id} : Partial updates given fields of an existing wIPListReport, field will ignore if it is null
     *
     * @param id the id of the wIPListReportDTO to save.
     * @param wIPListReportDTO the wIPListReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated wIPListReportDTO,
     * or with status {@code 400 (Bad Request)} if the wIPListReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the wIPListReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the wIPListReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/wip-list-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WIPListReportDTO> partialUpdateWIPListReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WIPListReportDTO wIPListReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WIPListReport partially : {}, {}", id, wIPListReportDTO);
        if (wIPListReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, wIPListReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!wIPListReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WIPListReportDTO> result = wIPListReportService.partialUpdate(wIPListReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, wIPListReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /wip-list-reports} : get all the wIPListReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of wIPListReports in body.
     */
    @GetMapping("/wip-list-reports")
    public ResponseEntity<List<WIPListReportDTO>> getAllWIPListReports(WIPListReportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WIPListReports by criteria: {}", criteria);
        Page<WIPListReportDTO> page = wIPListReportService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /wip-list-reports/count} : count all the wIPListReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/wip-list-reports/count")
    public ResponseEntity<Long> countWIPListReports(WIPListReportCriteria criteria) {
        log.debug("REST request to count WIPListReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(wIPListReportService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /wip-list-reports/:id} : get the "id" wIPListReport.
     *
     * @param id the id of the wIPListReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the wIPListReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/wip-list-reports/{id}")
    public ResponseEntity<WIPListReportDTO> getWIPListReport(@PathVariable Long id) {
        log.debug("REST request to get WIPListReport : {}", id);
        Optional<WIPListReportDTO> wIPListReportDTO = wIPListReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(wIPListReportDTO);
    }

    /**
     * {@code DELETE  /wip-list-reports/:id} : delete the "id" wIPListReport.
     *
     * @param id the id of the wIPListReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/wip-list-reports/{id}")
    public ResponseEntity<Void> deleteWIPListReport(@PathVariable Long id) {
        log.debug("REST request to delete WIPListReport : {}", id);
        wIPListReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/wip-list-reports?query=:query} : search for the wIPListReport corresponding
     * to the query.
     *
     * @param query the query of the wIPListReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/wip-list-reports")
    public ResponseEntity<List<WIPListReportDTO>> searchWIPListReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WIPListReports for query {}", query);
        Page<WIPListReportDTO> page = wIPListReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
