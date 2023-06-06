package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.5
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
import io.github.erp.repository.ExcelReportExportRepository;
import io.github.erp.service.ExcelReportExportQueryService;
import io.github.erp.service.ExcelReportExportService;
import io.github.erp.service.criteria.ExcelReportExportCriteria;
import io.github.erp.service.dto.ExcelReportExportDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ExcelReportExport}.
 */
@RestController("ExcelReportExportResourceProd")
@RequestMapping("/api/app")
public class ExcelReportExportResourceProd {

    private final Logger log = LoggerFactory.getLogger(ExcelReportExportResourceProd.class);

    private static final String ENTITY_NAME = "excelReportExport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExcelReportExportService excelReportExportService;

    private final ExcelReportExportRepository excelReportExportRepository;

    private final ExcelReportExportQueryService excelReportExportQueryService;

    public ExcelReportExportResourceProd(
        ExcelReportExportService excelReportExportService,
        ExcelReportExportRepository excelReportExportRepository,
        ExcelReportExportQueryService excelReportExportQueryService
    ) {
        this.excelReportExportService = excelReportExportService;
        this.excelReportExportRepository = excelReportExportRepository;
        this.excelReportExportQueryService = excelReportExportQueryService;
    }

    /**
     * {@code POST  /excel-report-exports} : Create a new excelReportExport.
     *
     * @param excelReportExportDTO the excelReportExportDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new excelReportExportDTO, or with status {@code 400 (Bad Request)} if the excelReportExport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/excel-report-exports")
    public ResponseEntity<ExcelReportExportDTO> createExcelReportExport(@Valid @RequestBody ExcelReportExportDTO excelReportExportDTO)
        throws URISyntaxException {
        log.debug("REST request to save ExcelReportExport : {}", excelReportExportDTO);
        if (excelReportExportDTO.getId() != null) {
            throw new BadRequestAlertException("A new excelReportExport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ExcelReportExportDTO result = excelReportExportService.save(excelReportExportDTO);
        return ResponseEntity
            .created(new URI("/api/excel-report-exports/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /excel-report-exports/:id} : Updates an existing excelReportExport.
     *
     * @param id the id of the excelReportExportDTO to save.
     * @param excelReportExportDTO the excelReportExportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated excelReportExportDTO,
     * or with status {@code 400 (Bad Request)} if the excelReportExportDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the excelReportExportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/excel-report-exports/{id}")
    public ResponseEntity<ExcelReportExportDTO> updateExcelReportExport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExcelReportExportDTO excelReportExportDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ExcelReportExport : {}, {}", id, excelReportExportDTO);
        if (excelReportExportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, excelReportExportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!excelReportExportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ExcelReportExportDTO result = excelReportExportService.save(excelReportExportDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, excelReportExportDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /excel-report-exports/:id} : Partial updates given fields of an existing excelReportExport, field will ignore if it is null
     *
     * @param id the id of the excelReportExportDTO to save.
     * @param excelReportExportDTO the excelReportExportDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated excelReportExportDTO,
     * or with status {@code 400 (Bad Request)} if the excelReportExportDTO is not valid,
     * or with status {@code 404 (Not Found)} if the excelReportExportDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the excelReportExportDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/excel-report-exports/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExcelReportExportDTO> partialUpdateExcelReportExport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExcelReportExportDTO excelReportExportDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ExcelReportExport partially : {}, {}", id, excelReportExportDTO);
        if (excelReportExportDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, excelReportExportDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!excelReportExportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExcelReportExportDTO> result = excelReportExportService.partialUpdate(excelReportExportDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, excelReportExportDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /excel-report-exports} : get all the excelReportExports.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of excelReportExports in body.
     */
    @GetMapping("/excel-report-exports")
    public ResponseEntity<List<ExcelReportExportDTO>> getAllExcelReportExports(ExcelReportExportCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ExcelReportExports by criteria: {}", criteria);
        Page<ExcelReportExportDTO> page = excelReportExportQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /excel-report-exports/count} : count all the excelReportExports.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/excel-report-exports/count")
    public ResponseEntity<Long> countExcelReportExports(ExcelReportExportCriteria criteria) {
        log.debug("REST request to count ExcelReportExports by criteria: {}", criteria);
        return ResponseEntity.ok().body(excelReportExportQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /excel-report-exports/:id} : get the "id" excelReportExport.
     *
     * @param id the id of the excelReportExportDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the excelReportExportDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/excel-report-exports/{id}")
    public ResponseEntity<ExcelReportExportDTO> getExcelReportExport(@PathVariable Long id) {
        log.debug("REST request to get ExcelReportExport : {}", id);
        Optional<ExcelReportExportDTO> excelReportExportDTO = excelReportExportService.findOne(id);
        return ResponseUtil.wrapOrNotFound(excelReportExportDTO);
    }

    /**
     * {@code DELETE  /excel-report-exports/:id} : delete the "id" excelReportExport.
     *
     * @param id the id of the excelReportExportDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/excel-report-exports/{id}")
    public ResponseEntity<Void> deleteExcelReportExport(@PathVariable Long id) {
        log.debug("REST request to delete ExcelReportExport : {}", id);
        excelReportExportService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/excel-report-exports?query=:query} : search for the excelReportExport corresponding
     * to the query.
     *
     * @param query the query of the excelReportExport search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/excel-report-exports")
    public ResponseEntity<List<ExcelReportExportDTO>> searchExcelReportExports(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ExcelReportExports for query {}", query);
        Page<ExcelReportExportDTO> page = excelReportExportService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
