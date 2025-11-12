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

import io.github.erp.repository.ReportMetadataRepository;
import io.github.erp.service.ReportMetadataQueryService;
import io.github.erp.service.ReportMetadataService;
import io.github.erp.service.criteria.ReportMetadataCriteria;
import io.github.erp.service.dto.ReportMetadataDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ReportMetadata}.
 */
@RestController
@RequestMapping("/api")
public class ReportMetadataResource {

    private final Logger log = LoggerFactory.getLogger(ReportMetadataResource.class);

    private static final String ENTITY_NAME = "reportMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportMetadataService reportMetadataService;

    private final ReportMetadataRepository reportMetadataRepository;

    private final ReportMetadataQueryService reportMetadataQueryService;

    public ReportMetadataResource(
        ReportMetadataService reportMetadataService,
        ReportMetadataRepository reportMetadataRepository,
        ReportMetadataQueryService reportMetadataQueryService
    ) {
        this.reportMetadataService = reportMetadataService;
        this.reportMetadataRepository = reportMetadataRepository;
        this.reportMetadataQueryService = reportMetadataQueryService;
    }

    /**
     * {@code POST  /report-metadata} : Create a new reportMetadata.
     *
     * @param reportMetadataDTO the reportMetadataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportMetadataDTO, or with status {@code 400 (Bad Request)} if the reportMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/report-metadata")
    public ResponseEntity<ReportMetadataDTO> createReportMetadata(@Valid @RequestBody ReportMetadataDTO reportMetadataDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportMetadata : {}", reportMetadataDTO);
        if (reportMetadataDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportMetadataDTO result = reportMetadataService.save(reportMetadataDTO);
        return ResponseEntity
            .created(new URI("/api/report-metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /report-metadata/:id} : Updates an existing reportMetadata.
     *
     * @param id the id of the reportMetadataDTO to save.
     * @param reportMetadataDTO the reportMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the reportMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/report-metadata/{id}")
    public ResponseEntity<ReportMetadataDTO> updateReportMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportMetadataDTO reportMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportMetadata : {}, {}", id, reportMetadataDTO);
        if (reportMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportMetadataDTO result = reportMetadataService.save(reportMetadataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportMetadataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /report-metadata/:id} : Partial updates given fields of an existing reportMetadata, field will ignore if it is null
     *
     * @param id the id of the reportMetadataDTO to save.
     * @param reportMetadataDTO the reportMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the reportMetadataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportMetadataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/report-metadata/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportMetadataDTO> partialUpdateReportMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportMetadataDTO reportMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportMetadata partially : {}, {}", id, reportMetadataDTO);
        if (reportMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportMetadataDTO> result = reportMetadataService.partialUpdate(reportMetadataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportMetadataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-metadata} : get all the reportMetadata.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportMetadata in body.
     */
    @GetMapping("/report-metadata")
    public ResponseEntity<List<ReportMetadataDTO>> getAllReportMetadata(ReportMetadataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReportMetadata by criteria: {}", criteria);
        Page<ReportMetadataDTO> page = reportMetadataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-metadata/count} : count all the reportMetadata.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/report-metadata/count")
    public ResponseEntity<Long> countReportMetadata(ReportMetadataCriteria criteria) {
        log.debug("REST request to count ReportMetadata by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportMetadataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-metadata/:id} : get the "id" reportMetadata.
     *
     * @param id the id of the reportMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/report-metadata/{id}")
    public ResponseEntity<ReportMetadataDTO> getReportMetadata(@PathVariable Long id) {
        log.debug("REST request to get ReportMetadata : {}", id);
        Optional<ReportMetadataDTO> reportMetadataDTO = reportMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportMetadataDTO);
    }

    /**
     * {@code DELETE  /report-metadata/:id} : delete the "id" reportMetadata.
     *
     * @param id the id of the reportMetadataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO CONTENT)}.
     */
    @DeleteMapping("/report-metadata/{id}")
    public ResponseEntity<Void> deleteReportMetadata(@PathVariable Long id) {
        log.debug("REST request to delete ReportMetadata : {}", id);
        reportMetadataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/report-metadata?query=:query} : search for the reportMetadata corresponding to the query.
     *
     * @param query the query of the reportMetadata search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/report-metadata")
    public ResponseEntity<List<ReportMetadataDTO>> searchReportMetadata(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReportMetadata for query {}", query);
        Page<ReportMetadataDTO> page = reportMetadataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
