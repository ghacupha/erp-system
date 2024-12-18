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
import io.github.erp.repository.LeaseLiabilityScheduleReportRepository;
import io.github.erp.service.LeaseLiabilityScheduleReportQueryService;
import io.github.erp.service.LeaseLiabilityScheduleReportService;
import io.github.erp.service.criteria.LeaseLiabilityScheduleReportCriteria;
import io.github.erp.service.dto.LeaseLiabilityScheduleReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiabilityScheduleReport}.
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseLiabilityScheduleReportResourceProd {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleReportResourceProd.class);

    private static final String ENTITY_NAME = "leaseLiabilityScheduleReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseLiabilityScheduleReportService leaseLiabilityScheduleReportService;

    private final LeaseLiabilityScheduleReportRepository leaseLiabilityScheduleReportRepository;

    private final LeaseLiabilityScheduleReportQueryService leaseLiabilityScheduleReportQueryService;

    public LeaseLiabilityScheduleReportResourceProd(
        LeaseLiabilityScheduleReportService leaseLiabilityScheduleReportService,
        LeaseLiabilityScheduleReportRepository leaseLiabilityScheduleReportRepository,
        LeaseLiabilityScheduleReportQueryService leaseLiabilityScheduleReportQueryService
    ) {
        this.leaseLiabilityScheduleReportService = leaseLiabilityScheduleReportService;
        this.leaseLiabilityScheduleReportRepository = leaseLiabilityScheduleReportRepository;
        this.leaseLiabilityScheduleReportQueryService = leaseLiabilityScheduleReportQueryService;
    }

    /**
     * {@code POST  /lease-liability-schedule-reports} : Create a new leaseLiabilityScheduleReport.
     *
     * @param leaseLiabilityScheduleReportDTO the leaseLiabilityScheduleReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseLiabilityScheduleReportDTO, or with status {@code 400 (Bad Request)} if the leaseLiabilityScheduleReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-liability-schedule-reports")
    public ResponseEntity<LeaseLiabilityScheduleReportDTO> createLeaseLiabilityScheduleReport(
        @Valid @RequestBody LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LeaseLiabilityScheduleReport : {}", leaseLiabilityScheduleReportDTO);
        if (leaseLiabilityScheduleReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseLiabilityScheduleReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseLiabilityScheduleReportDTO result = leaseLiabilityScheduleReportService.save(leaseLiabilityScheduleReportDTO);
        return ResponseEntity
            .created(new URI("/api/lease-liability-schedule-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-liability-schedule-reports/:id} : Updates an existing leaseLiabilityScheduleReport.
     *
     * @param id the id of the leaseLiabilityScheduleReportDTO to save.
     * @param leaseLiabilityScheduleReportDTO the leaseLiabilityScheduleReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityScheduleReportDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityScheduleReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityScheduleReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-liability-schedule-reports/{id}")
    public ResponseEntity<LeaseLiabilityScheduleReportDTO> updateLeaseLiabilityScheduleReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseLiabilityScheduleReport : {}, {}", id, leaseLiabilityScheduleReportDTO);
        if (leaseLiabilityScheduleReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityScheduleReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityScheduleReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseLiabilityScheduleReportDTO result = leaseLiabilityScheduleReportService.save(leaseLiabilityScheduleReportDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityScheduleReportDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /lease-liability-schedule-reports/:id} : Partial updates given fields of an existing leaseLiabilityScheduleReport, field will ignore if it is null
     *
     * @param id the id of the leaseLiabilityScheduleReportDTO to save.
     * @param leaseLiabilityScheduleReportDTO the leaseLiabilityScheduleReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityScheduleReportDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityScheduleReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseLiabilityScheduleReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityScheduleReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-liability-schedule-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseLiabilityScheduleReportDTO> partialUpdateLeaseLiabilityScheduleReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseLiabilityScheduleReportDTO leaseLiabilityScheduleReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseLiabilityScheduleReport partially : {}, {}", id, leaseLiabilityScheduleReportDTO);
        if (leaseLiabilityScheduleReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityScheduleReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityScheduleReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseLiabilityScheduleReportDTO> result = leaseLiabilityScheduleReportService.partialUpdate(
            leaseLiabilityScheduleReportDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityScheduleReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-liability-schedule-reports} : get all the leaseLiabilityScheduleReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilityScheduleReports in body.
     */
    @GetMapping("/lease-liability-schedule-reports")
    public ResponseEntity<List<LeaseLiabilityScheduleReportDTO>> getAllLeaseLiabilityScheduleReports(
        LeaseLiabilityScheduleReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityScheduleReports by criteria: {}", criteria);
        Page<LeaseLiabilityScheduleReportDTO> page = leaseLiabilityScheduleReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liability-schedule-reports/count} : count all the leaseLiabilityScheduleReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liability-schedule-reports/count")
    public ResponseEntity<Long> countLeaseLiabilityScheduleReports(LeaseLiabilityScheduleReportCriteria criteria) {
        log.debug("REST request to count LeaseLiabilityScheduleReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityScheduleReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liability-schedule-reports/:id} : get the "id" leaseLiabilityScheduleReport.
     *
     * @param id the id of the leaseLiabilityScheduleReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityScheduleReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liability-schedule-reports/{id}")
    public ResponseEntity<LeaseLiabilityScheduleReportDTO> getLeaseLiabilityScheduleReport(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityScheduleReport : {}", id);
        Optional<LeaseLiabilityScheduleReportDTO> leaseLiabilityScheduleReportDTO = leaseLiabilityScheduleReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseLiabilityScheduleReportDTO);
    }

    /**
     * {@code DELETE  /lease-liability-schedule-reports/:id} : delete the "id" leaseLiabilityScheduleReport.
     *
     * @param id the id of the leaseLiabilityScheduleReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-liability-schedule-reports/{id}")
    public ResponseEntity<Void> deleteLeaseLiabilityScheduleReport(@PathVariable Long id) {
        log.debug("REST request to delete LeaseLiabilityScheduleReport : {}", id);
        leaseLiabilityScheduleReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-liability-schedule-reports?query=:query} : search for the leaseLiabilityScheduleReport corresponding
     * to the query.
     *
     * @param query the query of the leaseLiabilityScheduleReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liability-schedule-reports")
    public ResponseEntity<List<LeaseLiabilityScheduleReportDTO>> searchLeaseLiabilityScheduleReports(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseLiabilityScheduleReports for query {}", query);
        Page<LeaseLiabilityScheduleReportDTO> page = leaseLiabilityScheduleReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
