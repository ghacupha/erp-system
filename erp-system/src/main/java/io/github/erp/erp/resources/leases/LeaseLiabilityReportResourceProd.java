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
import io.github.erp.repository.LeaseLiabilityReportRepository;
import io.github.erp.service.LeaseLiabilityReportQueryService;
import io.github.erp.service.LeaseLiabilityReportService;
import io.github.erp.service.criteria.LeaseLiabilityReportCriteria;
import io.github.erp.service.dto.LeaseLiabilityReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiabilityReport}.
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseLiabilityReportResourceProd {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityReportResourceProd.class);

    private static final String ENTITY_NAME = "leaseLiabilityReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseLiabilityReportService leaseLiabilityReportService;

    private final LeaseLiabilityReportRepository leaseLiabilityReportRepository;

    private final LeaseLiabilityReportQueryService leaseLiabilityReportQueryService;

    public LeaseLiabilityReportResourceProd(
        LeaseLiabilityReportService leaseLiabilityReportService,
        LeaseLiabilityReportRepository leaseLiabilityReportRepository,
        LeaseLiabilityReportQueryService leaseLiabilityReportQueryService
    ) {
        this.leaseLiabilityReportService = leaseLiabilityReportService;
        this.leaseLiabilityReportRepository = leaseLiabilityReportRepository;
        this.leaseLiabilityReportQueryService = leaseLiabilityReportQueryService;
    }

    /**
     * {@code POST  /lease-liability-reports} : Create a new leaseLiabilityReport.
     *
     * @param leaseLiabilityReportDTO the leaseLiabilityReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseLiabilityReportDTO, or with status {@code 400 (Bad Request)} if the leaseLiabilityReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-liability-reports")
    public ResponseEntity<LeaseLiabilityReportDTO> createLeaseLiabilityReport(
        @Valid @RequestBody LeaseLiabilityReportDTO leaseLiabilityReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LeaseLiabilityReport : {}", leaseLiabilityReportDTO);
        if (leaseLiabilityReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseLiabilityReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseLiabilityReportDTO result = leaseLiabilityReportService.save(leaseLiabilityReportDTO);
        return ResponseEntity
            .created(new URI("/api/lease-liability-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-liability-reports/:id} : Updates an existing leaseLiabilityReport.
     *
     * @param id the id of the leaseLiabilityReportDTO to save.
     * @param leaseLiabilityReportDTO the leaseLiabilityReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityReportDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-liability-reports/{id}")
    public ResponseEntity<LeaseLiabilityReportDTO> updateLeaseLiabilityReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseLiabilityReportDTO leaseLiabilityReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseLiabilityReport : {}, {}", id, leaseLiabilityReportDTO);
        if (leaseLiabilityReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseLiabilityReportDTO result = leaseLiabilityReportService.save(leaseLiabilityReportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityReportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lease-liability-reports/:id} : Partial updates given fields of an existing leaseLiabilityReport, field will ignore if it is null
     *
     * @param id the id of the leaseLiabilityReportDTO to save.
     * @param leaseLiabilityReportDTO the leaseLiabilityReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityReportDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseLiabilityReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-liability-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseLiabilityReportDTO> partialUpdateLeaseLiabilityReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseLiabilityReportDTO leaseLiabilityReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseLiabilityReport partially : {}, {}", id, leaseLiabilityReportDTO);
        if (leaseLiabilityReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseLiabilityReportDTO> result = leaseLiabilityReportService.partialUpdate(leaseLiabilityReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-liability-reports} : get all the leaseLiabilityReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilityReports in body.
     */
    @GetMapping("/lease-liability-reports")
    public ResponseEntity<List<LeaseLiabilityReportDTO>> getAllLeaseLiabilityReports(
        LeaseLiabilityReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityReports by criteria: {}", criteria);
        Page<LeaseLiabilityReportDTO> page = leaseLiabilityReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liability-reports/count} : count all the leaseLiabilityReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liability-reports/count")
    public ResponseEntity<Long> countLeaseLiabilityReports(LeaseLiabilityReportCriteria criteria) {
        log.debug("REST request to count LeaseLiabilityReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liability-reports/:id} : get the "id" leaseLiabilityReport.
     *
     * @param id the id of the leaseLiabilityReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liability-reports/{id}")
    public ResponseEntity<LeaseLiabilityReportDTO> getLeaseLiabilityReport(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityReport : {}", id);
        Optional<LeaseLiabilityReportDTO> leaseLiabilityReportDTO = leaseLiabilityReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseLiabilityReportDTO);
    }

    /**
     * {@code DELETE  /lease-liability-reports/:id} : delete the "id" leaseLiabilityReport.
     *
     * @param id the id of the leaseLiabilityReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-liability-reports/{id}")
    public ResponseEntity<Void> deleteLeaseLiabilityReport(@PathVariable Long id) {
        log.debug("REST request to delete LeaseLiabilityReport : {}", id);
        leaseLiabilityReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-liability-reports?query=:query} : search for the leaseLiabilityReport corresponding
     * to the query.
     *
     * @param query the query of the leaseLiabilityReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liability-reports")
    public ResponseEntity<List<LeaseLiabilityReportDTO>> searchLeaseLiabilityReports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeaseLiabilityReports for query {}", query);
        Page<LeaseLiabilityReportDTO> page = leaseLiabilityReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
