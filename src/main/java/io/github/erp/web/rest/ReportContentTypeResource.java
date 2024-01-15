package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.repository.ReportContentTypeRepository;
import io.github.erp.service.ReportContentTypeQueryService;
import io.github.erp.service.ReportContentTypeService;
import io.github.erp.service.criteria.ReportContentTypeCriteria;
import io.github.erp.service.dto.ReportContentTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ReportContentType}.
 */
@RestController
@RequestMapping("/api")
public class ReportContentTypeResource {

    private final Logger log = LoggerFactory.getLogger(ReportContentTypeResource.class);

    private static final String ENTITY_NAME = "reportContentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportContentTypeService reportContentTypeService;

    private final ReportContentTypeRepository reportContentTypeRepository;

    private final ReportContentTypeQueryService reportContentTypeQueryService;

    public ReportContentTypeResource(
        ReportContentTypeService reportContentTypeService,
        ReportContentTypeRepository reportContentTypeRepository,
        ReportContentTypeQueryService reportContentTypeQueryService
    ) {
        this.reportContentTypeService = reportContentTypeService;
        this.reportContentTypeRepository = reportContentTypeRepository;
        this.reportContentTypeQueryService = reportContentTypeQueryService;
    }

    /**
     * {@code POST  /report-content-types} : Create a new reportContentType.
     *
     * @param reportContentTypeDTO the reportContentTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportContentTypeDTO, or with status {@code 400 (Bad Request)} if the reportContentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/report-content-types")
    public ResponseEntity<ReportContentTypeDTO> createReportContentType(@Valid @RequestBody ReportContentTypeDTO reportContentTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportContentType : {}", reportContentTypeDTO);
        if (reportContentTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportContentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportContentTypeDTO result = reportContentTypeService.save(reportContentTypeDTO);
        return ResponseEntity
            .created(new URI("/api/report-content-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /report-content-types/:id} : Updates an existing reportContentType.
     *
     * @param id the id of the reportContentTypeDTO to save.
     * @param reportContentTypeDTO the reportContentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportContentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the reportContentTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportContentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/report-content-types/{id}")
    public ResponseEntity<ReportContentTypeDTO> updateReportContentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportContentTypeDTO reportContentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportContentType : {}, {}", id, reportContentTypeDTO);
        if (reportContentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportContentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportContentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportContentTypeDTO result = reportContentTypeService.save(reportContentTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportContentTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /report-content-types/:id} : Partial updates given fields of an existing reportContentType, field will ignore if it is null
     *
     * @param id the id of the reportContentTypeDTO to save.
     * @param reportContentTypeDTO the reportContentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportContentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the reportContentTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportContentTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportContentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/report-content-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportContentTypeDTO> partialUpdateReportContentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportContentTypeDTO reportContentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportContentType partially : {}, {}", id, reportContentTypeDTO);
        if (reportContentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportContentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportContentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportContentTypeDTO> result = reportContentTypeService.partialUpdate(reportContentTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reportContentTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-content-types} : get all the reportContentTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportContentTypes in body.
     */
    @GetMapping("/report-content-types")
    public ResponseEntity<List<ReportContentTypeDTO>> getAllReportContentTypes(ReportContentTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReportContentTypes by criteria: {}", criteria);
        Page<ReportContentTypeDTO> page = reportContentTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-content-types/count} : count all the reportContentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/report-content-types/count")
    public ResponseEntity<Long> countReportContentTypes(ReportContentTypeCriteria criteria) {
        log.debug("REST request to count ReportContentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportContentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-content-types/:id} : get the "id" reportContentType.
     *
     * @param id the id of the reportContentTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportContentTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/report-content-types/{id}")
    public ResponseEntity<ReportContentTypeDTO> getReportContentType(@PathVariable Long id) {
        log.debug("REST request to get ReportContentType : {}", id);
        Optional<ReportContentTypeDTO> reportContentTypeDTO = reportContentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportContentTypeDTO);
    }

    /**
     * {@code DELETE  /report-content-types/:id} : delete the "id" reportContentType.
     *
     * @param id the id of the reportContentTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/report-content-types/{id}")
    public ResponseEntity<Void> deleteReportContentType(@PathVariable Long id) {
        log.debug("REST request to delete ReportContentType : {}", id);
        reportContentTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/report-content-types?query=:query} : search for the reportContentType corresponding
     * to the query.
     *
     * @param query the query of the reportContentType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/report-content-types")
    public ResponseEntity<List<ReportContentTypeDTO>> searchReportContentTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReportContentTypes for query {}", query);
        Page<ReportContentTypeDTO> page = reportContentTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
