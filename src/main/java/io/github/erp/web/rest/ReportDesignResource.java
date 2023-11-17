package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.repository.ReportDesignRepository;
import io.github.erp.service.ReportDesignQueryService;
import io.github.erp.service.ReportDesignService;
import io.github.erp.service.criteria.ReportDesignCriteria;
import io.github.erp.service.dto.ReportDesignDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ReportDesign}.
 */
@RestController
@RequestMapping("/api")
public class ReportDesignResource {

    private final Logger log = LoggerFactory.getLogger(ReportDesignResource.class);

    private static final String ENTITY_NAME = "reportDesign";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportDesignService reportDesignService;

    private final ReportDesignRepository reportDesignRepository;

    private final ReportDesignQueryService reportDesignQueryService;

    public ReportDesignResource(
        ReportDesignService reportDesignService,
        ReportDesignRepository reportDesignRepository,
        ReportDesignQueryService reportDesignQueryService
    ) {
        this.reportDesignService = reportDesignService;
        this.reportDesignRepository = reportDesignRepository;
        this.reportDesignQueryService = reportDesignQueryService;
    }

    /**
     * {@code POST  /report-designs} : Create a new reportDesign.
     *
     * @param reportDesignDTO the reportDesignDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportDesignDTO, or with status {@code 400 (Bad Request)} if the reportDesign has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/report-designs")
    public ResponseEntity<ReportDesignDTO> createReportDesign(@Valid @RequestBody ReportDesignDTO reportDesignDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportDesign : {}", reportDesignDTO);
        if (reportDesignDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportDesign cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportDesignDTO result = reportDesignService.save(reportDesignDTO);
        return ResponseEntity
            .created(new URI("/api/report-designs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /report-designs/:id} : Updates an existing reportDesign.
     *
     * @param id the id of the reportDesignDTO to save.
     * @param reportDesignDTO the reportDesignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDesignDTO,
     * or with status {@code 400 (Bad Request)} if the reportDesignDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportDesignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/report-designs/{id}")
    public ResponseEntity<ReportDesignDTO> updateReportDesign(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportDesignDTO reportDesignDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportDesign : {}, {}", id, reportDesignDTO);
        if (reportDesignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportDesignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportDesignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportDesignDTO result = reportDesignService.save(reportDesignDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportDesignDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /report-designs/:id} : Partial updates given fields of an existing reportDesign, field will ignore if it is null
     *
     * @param id the id of the reportDesignDTO to save.
     * @param reportDesignDTO the reportDesignDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportDesignDTO,
     * or with status {@code 400 (Bad Request)} if the reportDesignDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportDesignDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportDesignDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/report-designs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportDesignDTO> partialUpdateReportDesign(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportDesignDTO reportDesignDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportDesign partially : {}, {}", id, reportDesignDTO);
        if (reportDesignDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportDesignDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportDesignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportDesignDTO> result = reportDesignService.partialUpdate(reportDesignDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportDesignDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-designs} : get all the reportDesigns.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportDesigns in body.
     */
    @GetMapping("/report-designs")
    public ResponseEntity<List<ReportDesignDTO>> getAllReportDesigns(ReportDesignCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReportDesigns by criteria: {}", criteria);
        Page<ReportDesignDTO> page = reportDesignQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-designs/count} : count all the reportDesigns.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/report-designs/count")
    public ResponseEntity<Long> countReportDesigns(ReportDesignCriteria criteria) {
        log.debug("REST request to count ReportDesigns by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportDesignQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-designs/:id} : get the "id" reportDesign.
     *
     * @param id the id of the reportDesignDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportDesignDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/report-designs/{id}")
    public ResponseEntity<ReportDesignDTO> getReportDesign(@PathVariable Long id) {
        log.debug("REST request to get ReportDesign : {}", id);
        Optional<ReportDesignDTO> reportDesignDTO = reportDesignService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportDesignDTO);
    }

    /**
     * {@code DELETE  /report-designs/:id} : delete the "id" reportDesign.
     *
     * @param id the id of the reportDesignDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/report-designs/{id}")
    public ResponseEntity<Void> deleteReportDesign(@PathVariable Long id) {
        log.debug("REST request to delete ReportDesign : {}", id);
        reportDesignService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/report-designs?query=:query} : search for the reportDesign corresponding
     * to the query.
     *
     * @param query the query of the reportDesign search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/report-designs")
    public ResponseEntity<List<ReportDesignDTO>> searchReportDesigns(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReportDesigns for query {}", query);
        Page<ReportDesignDTO> page = reportDesignService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
