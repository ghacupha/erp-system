package io.github.erp.erp.resources.reports;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.repository.ReportTemplateRepository;
import io.github.erp.service.ReportTemplateQueryService;
import io.github.erp.service.ReportTemplateService;
import io.github.erp.service.criteria.ReportTemplateCriteria;
import io.github.erp.service.dto.ReportTemplateDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link io.github.erp.domain.ReportTemplate}.
 */
@RestController("reportTemplateResourceProd")
@RequestMapping("/api/app")
public class ReportTemplateResourceProd {

    private final Logger log = LoggerFactory.getLogger(ReportTemplateResourceProd.class);

    private static final String ENTITY_NAME = "reportTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReportTemplateService reportTemplateService;

    private final ReportTemplateRepository reportTemplateRepository;

    private final ReportTemplateQueryService reportTemplateQueryService;

    public ReportTemplateResourceProd(
        ReportTemplateService reportTemplateService,
        ReportTemplateRepository reportTemplateRepository,
        ReportTemplateQueryService reportTemplateQueryService
    ) {
        this.reportTemplateService = reportTemplateService;
        this.reportTemplateRepository = reportTemplateRepository;
        this.reportTemplateQueryService = reportTemplateQueryService;
    }

    /**
     * {@code POST  /report-templates} : Create a new reportTemplate.
     *
     * @param reportTemplateDTO the reportTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reportTemplateDTO, or with status {@code 400 (Bad Request)} if the reportTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @RolesAllowed({"ROLE_REPORT_DESIGNER", "ROLE_DEV"})
    @PostMapping("/report-templates")
    public ResponseEntity<ReportTemplateDTO> createReportTemplate(@Valid @RequestBody ReportTemplateDTO reportTemplateDTO)
        throws URISyntaxException {
        log.debug("REST request to save ReportTemplate : {}", reportTemplateDTO);
        if (reportTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new reportTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ReportTemplateDTO result = reportTemplateService.save(reportTemplateDTO);
        return ResponseEntity
            .created(new URI("/api/report-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /report-templates/:id} : Updates an existing reportTemplate.
     *
     * @param id the id of the reportTemplateDTO to save.
     * @param reportTemplateDTO the reportTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the reportTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reportTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @RolesAllowed({"ROLE_REPORT_DESIGNER", "ROLE_DEV"})
    @PutMapping("/report-templates/{id}")
    public ResponseEntity<ReportTemplateDTO> updateReportTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ReportTemplateDTO reportTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ReportTemplate : {}, {}", id, reportTemplateDTO);
        if (reportTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ReportTemplateDTO result = reportTemplateService.save(reportTemplateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reportTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /report-templates/:id} : Partial updates given fields of an existing reportTemplate, field will ignore if it is null
     *
     * @param id the id of the reportTemplateDTO to save.
     * @param reportTemplateDTO the reportTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reportTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the reportTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the reportTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the reportTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @RolesAllowed({"ROLE_REPORT_DESIGNER", "ROLE_DEV"})
    @PatchMapping(value = "/report-templates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ReportTemplateDTO> partialUpdateReportTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ReportTemplateDTO reportTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ReportTemplate partially : {}, {}", id, reportTemplateDTO);
        if (reportTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, reportTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!reportTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ReportTemplateDTO> result = reportTemplateService.partialUpdate(reportTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, reportTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /report-templates} : get all the reportTemplates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reportTemplates in body.
     */
    @RolesAllowed({"ROLE_REPORT_ACCESSOR", "ROLE_REPORT_DESIGNER", "ROLE_DEV"})
    @GetMapping("/report-templates")
    public ResponseEntity<List<ReportTemplateDTO>> getAllReportTemplates(ReportTemplateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ReportTemplates by criteria: {}", criteria);
        Page<ReportTemplateDTO> page = reportTemplateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /report-templates/count} : count all the reportTemplates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/report-templates/count")
    public ResponseEntity<Long> countReportTemplates(ReportTemplateCriteria criteria) {
        log.debug("REST request to count ReportTemplates by criteria: {}", criteria);
        return ResponseEntity.ok().body(reportTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /report-templates/:id} : get the "id" reportTemplate.
     *
     * @param id the id of the reportTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reportTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @RolesAllowed({"ROLE_REPORT_ACCESSOR", "ROLE_REPORT_DESIGNER", "ROLE_DEV"})
    @GetMapping("/report-templates/{id}")
    public ResponseEntity<ReportTemplateDTO> getReportTemplate(@PathVariable Long id) {
        log.debug("REST request to get ReportTemplate : {}", id);
        Optional<ReportTemplateDTO> reportTemplateDTO = reportTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(reportTemplateDTO);
    }

    /**
     * {@code DELETE  /report-templates/:id} : delete the "id" reportTemplate.
     *
     * @param id the id of the reportTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @RolesAllowed({"ROLE_REPORT_DESIGNER", "ROLE_DEV"})
    @DeleteMapping("/report-templates/{id}")
    public ResponseEntity<Void> deleteReportTemplate(@PathVariable Long id) {
        log.debug("REST request to delete ReportTemplate : {}", id);
        reportTemplateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/report-templates?query=:query} : search for the reportTemplate corresponding
     * to the query.
     *
     * @param query the query of the reportTemplate search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @RolesAllowed({"ROLE_REPORT_DESIGNER", "ROLE_DEV"})
    @GetMapping("/_search/report-templates")
    public ResponseEntity<List<ReportTemplateDTO>> searchReportTemplates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ReportTemplates for query {}", query);
        Page<ReportTemplateDTO> page = reportTemplateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
