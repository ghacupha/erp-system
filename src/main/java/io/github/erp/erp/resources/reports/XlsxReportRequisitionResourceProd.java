package io.github.erp.erp.resources.reports;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.internal.model.AttachedXlsxReportRequisitionDTO;
import io.github.erp.internal.model.mapping.AttachedXlsxReportRequisitionDTOMapping;
import io.github.erp.internal.report.attachment.ReportAttachmentService;
import io.github.erp.repository.XlsxReportRequisitionRepository;
import io.github.erp.service.XlsxReportRequisitionQueryService;
import io.github.erp.service.XlsxReportRequisitionService;
import io.github.erp.service.criteria.XlsxReportRequisitionCriteria;
import io.github.erp.service.dto.XlsxReportRequisitionDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
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
 * REST controller for managing {@link io.github.erp.domain.XlsxReportRequisition}.
 */
@RestController("xlsxReportRequisitionResourceProd")
@RequestMapping("/api/app")
@RolesAllowed({"ROLE_REPORT_ACCESSOR", "ROLE_REPORT_DESIGNER", "ROLE_DEV"})
public class XlsxReportRequisitionResourceProd {

    private final Logger log = LoggerFactory.getLogger(XlsxReportRequisitionResourceProd.class);

    private static final String ENTITY_NAME = "xlsxReportRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final XlsxReportRequisitionService xlsxReportRequisitionService;

    private final XlsxReportRequisitionRepository xlsxReportRequisitionRepository;

    private final XlsxReportRequisitionQueryService xlsxReportRequisitionQueryService;

    private final AttachedXlsxReportRequisitionDTOMapping reportRequisitionDTOMapping;
    private final ReportAttachmentService<AttachedXlsxReportRequisitionDTO> reportAttachmentService;

    public XlsxReportRequisitionResourceProd(
        XlsxReportRequisitionService xlsxReportRequisitionService,
        XlsxReportRequisitionRepository xlsxReportRequisitionRepository,
        XlsxReportRequisitionQueryService xlsxReportRequisitionQueryService,
        AttachedXlsxReportRequisitionDTOMapping reportRequisitionDTOMapping,
        ReportAttachmentService<AttachedXlsxReportRequisitionDTO> reportAttachmentService) {
        this.xlsxReportRequisitionService = xlsxReportRequisitionService;
        this.xlsxReportRequisitionRepository = xlsxReportRequisitionRepository;
        this.xlsxReportRequisitionQueryService = xlsxReportRequisitionQueryService;
        this.reportRequisitionDTOMapping = reportRequisitionDTOMapping;
        this.reportAttachmentService = reportAttachmentService;
    }

    /**
     * {@code POST  /xlsx-report-requisitions} : Create a new xlsxReportRequisition.
     *
     * @param xlsxReportRequisitionDTO the xlsxReportRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new xlsxReportRequisitionDTO, or with status {@code 400 (Bad Request)} if the xlsxReportRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/xlsx-report-requisitions")
    public ResponseEntity<XlsxReportRequisitionDTO> createXlsxReportRequisition(
        @Valid @RequestBody XlsxReportRequisitionDTO xlsxReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save XlsxReportRequisition : {}", xlsxReportRequisitionDTO);
        if (xlsxReportRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new xlsxReportRequisition cannot already have an ID", ENTITY_NAME, "idexists");
        }

        XlsxReportRequisitionDTO result = xlsxReportRequisitionService.save(xlsxReportRequisitionDTO);

        return ResponseEntity
            .created(new URI("/api/xlsx-report-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /xlsx-report-requisitions/:id} : Updates an existing xlsxReportRequisition.
     *
     * @param id the id of the xlsxReportRequisitionDTO to save.
     * @param xlsxReportRequisitionDTO the xlsxReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xlsxReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the xlsxReportRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the xlsxReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/xlsx-report-requisitions/{id}")
    public ResponseEntity<XlsxReportRequisitionDTO> updateXlsxReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody XlsxReportRequisitionDTO xlsxReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update XlsxReportRequisition : {}, {}", id, xlsxReportRequisitionDTO);
        if (xlsxReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xlsxReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xlsxReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        XlsxReportRequisitionDTO result = xlsxReportRequisitionService.save(xlsxReportRequisitionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, xlsxReportRequisitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /xlsx-report-requisitions/:id} : Partial updates given fields of an existing xlsxReportRequisition, field will ignore if it is null
     *
     * @param id the id of the xlsxReportRequisitionDTO to save.
     * @param xlsxReportRequisitionDTO the xlsxReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated xlsxReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the xlsxReportRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the xlsxReportRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the xlsxReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/xlsx-report-requisitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<XlsxReportRequisitionDTO> partialUpdateXlsxReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody XlsxReportRequisitionDTO xlsxReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update XlsxReportRequisition partially : {}, {}", id, xlsxReportRequisitionDTO);
        if (xlsxReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, xlsxReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!xlsxReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<XlsxReportRequisitionDTO> result = xlsxReportRequisitionService.partialUpdate(xlsxReportRequisitionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, xlsxReportRequisitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /xlsx-report-requisitions} : get all the xlsxReportRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of xlsxReportRequisitions in body.
     */
    @GetMapping("/xlsx-report-requisitions")
    public ResponseEntity<List<XlsxReportRequisitionDTO>> getAllXlsxReportRequisitions(
        XlsxReportRequisitionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get XlsxReportRequisitions by criteria: {}", criteria);
        Page<XlsxReportRequisitionDTO> page = xlsxReportRequisitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);

        // TODO Implement list requisitions
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /xlsx-report-requisitions/count} : count all the xlsxReportRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/xlsx-report-requisitions/count")
    public ResponseEntity<Long> countXlsxReportRequisitions(XlsxReportRequisitionCriteria criteria) {
        log.debug("REST request to count XlsxReportRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(xlsxReportRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /xlsx-report-requisitions/:id} : get the "id" xlsxReportRequisition.
     *
     * @param id the id of the xlsxReportRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the xlsxReportRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/xlsx-report-requisitions/{id}")
    public ResponseEntity<AttachedXlsxReportRequisitionDTO> getXlsxReportRequisition(@PathVariable Long id) {
        log.debug("REST request to get XlsxReportRequisition : {}", id);
        // Optional<XlsxReportRequisitionDTO> xlsxReportRequisitionDTO = xlsxReportRequisitionService.findOne(id);

        AtomicReference<AttachedXlsxReportRequisitionDTO> attachedReport = new AtomicReference<>();

        xlsxReportRequisitionService.findOne(id).ifPresentOrElse(theOne -> attachedReport.set(reportAttachmentService.attachReport(reportRequisitionDTOMapping.toValue2(theOne))),
            () -> { throw new RuntimeException("We have failed to retrieve report id: " + id); }
        );

        return ResponseUtil.wrapOrNotFound(Optional.of(attachedReport.getAcquire()));
    }

    /**
     * {@code DELETE  /xlsx-report-requisitions/:id} : delete the "id" xlsxReportRequisition.
     *
     * @param id the id of the xlsxReportRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/xlsx-report-requisitions/{id}")
    public ResponseEntity<Void> deleteXlsxReportRequisition(@PathVariable Long id) {
        log.debug("REST request to delete XlsxReportRequisition : {}", id);
        xlsxReportRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/xlsx-report-requisitions?query=:query} : search for the xlsxReportRequisition corresponding
     * to the query.
     *
     * @param query the query of the xlsxReportRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/xlsx-report-requisitions")
    public ResponseEntity<List<XlsxReportRequisitionDTO>> searchXlsxReportRequisitions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of XlsxReportRequisitions for query {}", query);
        Page<XlsxReportRequisitionDTO> page = xlsxReportRequisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
