package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.LeaseLiabilityByAccountReportRepository;
import io.github.erp.service.LeaseLiabilityByAccountReportQueryService;
import io.github.erp.service.LeaseLiabilityByAccountReportService;
import io.github.erp.service.criteria.LeaseLiabilityByAccountReportCriteria;
import io.github.erp.service.dto.LeaseLiabilityByAccountReportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiabilityByAccountReport}.
 */
@RestController
@RequestMapping("/api")
public class LeaseLiabilityByAccountReportResource {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityByAccountReportResource.class);

    private static final String ENTITY_NAME = "leaseLiabilityByAccountReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseLiabilityByAccountReportService leaseLiabilityByAccountReportService;

    private final LeaseLiabilityByAccountReportRepository leaseLiabilityByAccountReportRepository;

    private final LeaseLiabilityByAccountReportQueryService leaseLiabilityByAccountReportQueryService;

    public LeaseLiabilityByAccountReportResource(
        LeaseLiabilityByAccountReportService leaseLiabilityByAccountReportService,
        LeaseLiabilityByAccountReportRepository leaseLiabilityByAccountReportRepository,
        LeaseLiabilityByAccountReportQueryService leaseLiabilityByAccountReportQueryService
    ) {
        this.leaseLiabilityByAccountReportService = leaseLiabilityByAccountReportService;
        this.leaseLiabilityByAccountReportRepository = leaseLiabilityByAccountReportRepository;
        this.leaseLiabilityByAccountReportQueryService = leaseLiabilityByAccountReportQueryService;
    }

    /**
     * {@code POST  /lease-liability-by-account-reports} : Create a new leaseLiabilityByAccountReport.
     *
     * @param leaseLiabilityByAccountReportDTO the leaseLiabilityByAccountReportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseLiabilityByAccountReportDTO, or with status {@code 400 (Bad Request)} if the leaseLiabilityByAccountReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-liability-by-account-reports")
    public ResponseEntity<LeaseLiabilityByAccountReportDTO> createLeaseLiabilityByAccountReport(
        @Valid @RequestBody LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LeaseLiabilityByAccountReport : {}", leaseLiabilityByAccountReportDTO);
        if (leaseLiabilityByAccountReportDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseLiabilityByAccountReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseLiabilityByAccountReportDTO result = leaseLiabilityByAccountReportService.save(leaseLiabilityByAccountReportDTO);
        return ResponseEntity
            .created(new URI("/api/lease-liability-by-account-reports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-liability-by-account-reports/:id} : Updates an existing leaseLiabilityByAccountReport.
     *
     * @param id the id of the leaseLiabilityByAccountReportDTO to save.
     * @param leaseLiabilityByAccountReportDTO the leaseLiabilityByAccountReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityByAccountReportDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityByAccountReportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityByAccountReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-liability-by-account-reports/{id}")
    public ResponseEntity<LeaseLiabilityByAccountReportDTO> updateLeaseLiabilityByAccountReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseLiabilityByAccountReport : {}, {}", id, leaseLiabilityByAccountReportDTO);
        if (leaseLiabilityByAccountReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityByAccountReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityByAccountReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseLiabilityByAccountReportDTO result = leaseLiabilityByAccountReportService.save(leaseLiabilityByAccountReportDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityByAccountReportDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /lease-liability-by-account-reports/:id} : Partial updates given fields of an existing leaseLiabilityByAccountReport, field will ignore if it is null
     *
     * @param id the id of the leaseLiabilityByAccountReportDTO to save.
     * @param leaseLiabilityByAccountReportDTO the leaseLiabilityByAccountReportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityByAccountReportDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityByAccountReportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseLiabilityByAccountReportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityByAccountReportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-liability-by-account-reports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseLiabilityByAccountReportDTO> partialUpdateLeaseLiabilityByAccountReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseLiabilityByAccountReportDTO leaseLiabilityByAccountReportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseLiabilityByAccountReport partially : {}, {}", id, leaseLiabilityByAccountReportDTO);
        if (leaseLiabilityByAccountReportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityByAccountReportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityByAccountReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseLiabilityByAccountReportDTO> result = leaseLiabilityByAccountReportService.partialUpdate(
            leaseLiabilityByAccountReportDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityByAccountReportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-liability-by-account-reports} : get all the leaseLiabilityByAccountReports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilityByAccountReports in body.
     */
    @GetMapping("/lease-liability-by-account-reports")
    public ResponseEntity<List<LeaseLiabilityByAccountReportDTO>> getAllLeaseLiabilityByAccountReports(
        LeaseLiabilityByAccountReportCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityByAccountReports by criteria: {}", criteria);
        Page<LeaseLiabilityByAccountReportDTO> page = leaseLiabilityByAccountReportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liability-by-account-reports/count} : count all the leaseLiabilityByAccountReports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liability-by-account-reports/count")
    public ResponseEntity<Long> countLeaseLiabilityByAccountReports(LeaseLiabilityByAccountReportCriteria criteria) {
        log.debug("REST request to count LeaseLiabilityByAccountReports by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityByAccountReportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liability-by-account-reports/:id} : get the "id" leaseLiabilityByAccountReport.
     *
     * @param id the id of the leaseLiabilityByAccountReportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityByAccountReportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liability-by-account-reports/{id}")
    public ResponseEntity<LeaseLiabilityByAccountReportDTO> getLeaseLiabilityByAccountReport(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityByAccountReport : {}", id);
        Optional<LeaseLiabilityByAccountReportDTO> leaseLiabilityByAccountReportDTO = leaseLiabilityByAccountReportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseLiabilityByAccountReportDTO);
    }

    /**
     * {@code DELETE  /lease-liability-by-account-reports/:id} : delete the "id" leaseLiabilityByAccountReport.
     *
     * @param id the id of the leaseLiabilityByAccountReportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-liability-by-account-reports/{id}")
    public ResponseEntity<Void> deleteLeaseLiabilityByAccountReport(@PathVariable Long id) {
        log.debug("REST request to delete LeaseLiabilityByAccountReport : {}", id);
        leaseLiabilityByAccountReportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-liability-by-account-reports?query=:query} : search for the leaseLiabilityByAccountReport corresponding
     * to the query.
     *
     * @param query the query of the leaseLiabilityByAccountReport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liability-by-account-reports")
    public ResponseEntity<List<LeaseLiabilityByAccountReportDTO>> searchLeaseLiabilityByAccountReports(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseLiabilityByAccountReports for query {}", query);
        Page<LeaseLiabilityByAccountReportDTO> page = leaseLiabilityByAccountReportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
