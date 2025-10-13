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

import io.github.erp.repository.LeaseLiabilityPostingReportRepository;
import io.github.erp.service.LeaseLiabilityPostingReportQueryService;
import io.github.erp.service.LeaseLiabilityPostingReportService;
import io.github.erp.service.criteria.LeaseLiabilityPostingReportCriteria;
import io.github.erp.service.dto.LeaseLiabilityPostingReportDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiabilityPostingReport}.
 */
@RestController
@RequestMapping("/api")
public class LeaseLiabilityPostingReportResource {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityPostingReportResource.class);

    private static final String ENTITY_NAME = "leaseLiabilityPostingReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseLiabilityPostingReportService leaseLiabilityPostingReportService;

    private final LeaseLiabilityPostingReportRepository leaseLiabilityPostingReportRepository;

    private final LeaseLiabilityPostingReportQueryService leaseLiabilityPostingReportQueryService;

    public LeaseLiabilityPostingReportResource(
        LeaseLiabilityPostingReportService leaseLiabilityPostingReportService,
        LeaseLiabilityPostingReportRepository leaseLiabilityPostingReportRepository,
        LeaseLiabilityPostingReportQueryService leaseLiabilityPostingReportQueryService
    ) {
        this.leaseLiabilityPostingReportService = leaseLiabilityPostingReportService;
        this.leaseLiabilityPostingReportRepository = leaseLiabilityPostingReportRepository;
        this.leaseLiabilityPostingReportQueryService = leaseLiabilityPostingReportQueryService;
    }

    /**
     * {@code POST  /lease-liability-posting-reports} : Create a new leaseLiabilityPostingReport.
     *
     * @param leaseLiabilityPostingReportDTO the leaseLiabilityPostingReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseLiabilityPostingReportDTO, or with status {@code 400 (Bad Request)} if the leaseLiabilityPostingReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-liability-posting-reports")
    public ResponseEntity<LeaseLiabilityPostingReportDTO> createLeaseLiabilityPostingReport(
        @Valid @RequestBody LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LeaseLiabilityPostingReport : {}", leaseLiabilityPostingReportDTO);
        if (leaseLiabilityPostingReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseLiabilityPostingReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseLiabilityPostingReportDTO result = leaseLiabilityPostingReportService.save(leaseLiabilityPostingReportDTO);
        return ResponseEntity
            .created(new URI("/api/lease-liability-posting-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-liability-posting-reports/:id} : Updates an existing leaseLiabilityPostingReport.
     *
     * @param id the id of the leaseLiabilityPostingReportDTO to save.
     * @param leaseLiabilityPostingReportDTO the leaseLiabilityPostingReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityPostingReportDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityPostingReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityPostingReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-liability-posting-reports/{id}")
    public ResponseEntity<LeaseLiabilityPostingReportDTO> updateLeaseLiabilityPostingReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseLiabilityPostingReport : {}, {}", id, leaseLiabilityPostingReportDTO);
        if (leaseLiabilityPostingReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityPostingReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityPostingReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseLiabilityPostingReportDTO result = leaseLiabilityPostingReportService.save(leaseLiabilityPostingReportDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityPostingReportDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /lease-liability-posting-reports/:id} : Partial updates given fields of an existing leaseLiabilityPostingReport, field will ignore if it is null
     *
     * @param id the id of the leaseLiabilityPostingReportDTO to save.
     * @param leaseLiabilityPostingReportDTO the leaseLiabilityPostingReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityPostingReportDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityPostingReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseLiabilityPostingReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityPostingReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-liability-posting-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseLiabilityPostingReportDTO> partialUpdateLeaseLiabilityPostingReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseLiabilityPostingReportDTO leaseLiabilityPostingReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseLiabilityPostingReport partially : {}, {}", id, leaseLiabilityPostingReportDTO);
        if (leaseLiabilityPostingReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityPostingReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityPostingReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseLiabilityPostingReportDTO> result = leaseLiabilityPostingReportService.partialUpdate(leaseLiabilityPostingReportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityPostingReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-liability-posting-reports} : get all the leaseLiabilityPostingReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilityPostingReports in body.
     */
    @GetMapping("/lease-liability-posting-reports")
    public ResponseEntity<List<LeaseLiabilityPostingReportDTO>> getAllLeaseLiabilityPostingReports(
        LeaseLiabilityPostingReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityPostingReports by criteria: {}", criteria);
        Page<LeaseLiabilityPostingReportDTO> page = leaseLiabilityPostingReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liability-posting-reports/count} : count all the leaseLiabilityPostingReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liability-posting-reports/count")
    public ResponseEntity<Long> countLeaseLiabilityPostingReports(LeaseLiabilityPostingReportCriteria criteria) {
        log.debug("REST request to count LeaseLiabilityPostingReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityPostingReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liability-posting-reports/:id} : get the "id" leaseLiabilityPostingReport.
     *
     * @param id the id of the leaseLiabilityPostingReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityPostingReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liability-posting-reports/{id}")
    public ResponseEntity<LeaseLiabilityPostingReportDTO> getLeaseLiabilityPostingReport(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityPostingReport : {}", id);
        Optional<LeaseLiabilityPostingReportDTO> leaseLiabilityPostingReportDTO = leaseLiabilityPostingReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseLiabilityPostingReportDTO);
    }

    /**
     * {@code DELETE  /lease-liability-posting-reports/:id} : delete the "id" leaseLiabilityPostingReport.
     *
     * @param id the id of the leaseLiabilityPostingReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-liability-posting-reports/{id}")
    public ResponseEntity<Void> deleteLeaseLiabilityPostingReport(@PathVariable Long id) {
        log.debug("REST request to delete LeaseLiabilityPostingReport : {}", id);
        leaseLiabilityPostingReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-liability-posting-reports?query=:query} : search for the leaseLiabilityPostingReport corresponding
     * to the query.
     *
     * @param query the query of the leaseLiabilityPostingReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liability-posting-reports")
    public ResponseEntity<List<LeaseLiabilityPostingReportDTO>> searchLeaseLiabilityPostingReports(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseLiabilityPostingReports for query {}", query);
        Page<LeaseLiabilityPostingReportDTO> page = leaseLiabilityPostingReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
