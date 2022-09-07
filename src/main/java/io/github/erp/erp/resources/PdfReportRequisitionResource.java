package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 28 (Baruch Series) Server ver 0.0.9-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.internal.model.AttachedPdfReportRequisitionDTO;
import io.github.erp.internal.model.mapping.AttachedPdfReportRequisitionDTOMapping;
import io.github.erp.internal.report.attachment.ReportAttachmentService;
import io.github.erp.repository.PdfReportRequisitionRepository;
import io.github.erp.service.PdfReportRequisitionQueryService;
import io.github.erp.service.PdfReportRequisitionService;
import io.github.erp.service.criteria.PdfReportRequisitionCriteria;
import io.github.erp.service.dto.PdfReportRequisitionDTO;
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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.erp.domain.PdfReportRequisition}.
 */
@RestController
@RequestMapping("/api")
@RolesAllowed({"ROLE_REPORT_ACCESSOR", "ROLE_REPORT_DESIGNER", "ROLE_DEV"})
public class PdfReportRequisitionResource {

    private final Logger log = LoggerFactory.getLogger(PdfReportRequisitionResource.class);

    private static final String ENTITY_NAME = "pdfReportRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PdfReportRequisitionService pdfReportRequisitionService;

    private final PdfReportRequisitionRepository pdfReportRequisitionRepository;

    private final PdfReportRequisitionQueryService pdfReportRequisitionQueryService;


    private final AttachedPdfReportRequisitionDTOMapping reportRequisitionDTOMapping;
    private final ReportAttachmentService<AttachedPdfReportRequisitionDTO> reportAttachmentService;

    public PdfReportRequisitionResource(
        PdfReportRequisitionService pdfReportRequisitionService,
        PdfReportRequisitionRepository pdfReportRequisitionRepository,
        PdfReportRequisitionQueryService pdfReportRequisitionQueryService,
        AttachedPdfReportRequisitionDTOMapping reportRequisitionDTOMapping,
        ReportAttachmentService<AttachedPdfReportRequisitionDTO> reportAttachmentService) {
        this.pdfReportRequisitionService = pdfReportRequisitionService;
        this.pdfReportRequisitionRepository = pdfReportRequisitionRepository;
        this.pdfReportRequisitionQueryService = pdfReportRequisitionQueryService;
        this.reportRequisitionDTOMapping = reportRequisitionDTOMapping;
        this.reportAttachmentService = reportAttachmentService;
    }

    /**
     * {@code POST  /pdf-report-requisitions} : Create a new pdfReportRequisition.
     *
     * @param pdfReportRequisitionDTO the pdfReportRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new pdfReportRequisitionDTO, or with status {@code 400 (Bad Request)} if the pdfReportRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/pdf-report-requisitions")
    public ResponseEntity<PdfReportRequisitionDTO> createPdfReportRequisition(
        @Valid @RequestBody PdfReportRequisitionDTO pdfReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PdfReportRequisition : {}", pdfReportRequisitionDTO);
        if (pdfReportRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new pdfReportRequisition cannot already have an ID", ENTITY_NAME, "idexists");
        }

        PdfReportRequisitionDTO result = pdfReportRequisitionService.save(pdfReportRequisitionDTO);

        return ResponseEntity
            .created(new URI("/api/pdf-report-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /pdf-report-requisitions/:id} : Updates an existing pdfReportRequisition.
     *
     * @param id the id of the pdfReportRequisitionDTO to save.
     * @param pdfReportRequisitionDTO the pdfReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pdfReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the pdfReportRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the pdfReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/pdf-report-requisitions/{id}")
    public ResponseEntity<PdfReportRequisitionDTO> updatePdfReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PdfReportRequisitionDTO pdfReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PdfReportRequisition : {}, {}", id, pdfReportRequisitionDTO);
        if (pdfReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pdfReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pdfReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PdfReportRequisitionDTO result = pdfReportRequisitionService.save(pdfReportRequisitionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pdfReportRequisitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /pdf-report-requisitions/:id} : Partial updates given fields of an existing pdfReportRequisition, field will ignore if it is null
     *
     * @param id the id of the pdfReportRequisitionDTO to save.
     * @param pdfReportRequisitionDTO the pdfReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated pdfReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the pdfReportRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the pdfReportRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the pdfReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/pdf-report-requisitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PdfReportRequisitionDTO> partialUpdatePdfReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PdfReportRequisitionDTO pdfReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PdfReportRequisition partially : {}, {}", id, pdfReportRequisitionDTO);
        if (pdfReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, pdfReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!pdfReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PdfReportRequisitionDTO> result = pdfReportRequisitionService.partialUpdate(pdfReportRequisitionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, pdfReportRequisitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /pdf-report-requisitions} : get all the pdfReportRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of pdfReportRequisitions in body.
     */
    @GetMapping("/pdf-report-requisitions")
    public ResponseEntity<List<PdfReportRequisitionDTO>> getAllPdfReportRequisitions(
        PdfReportRequisitionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get PdfReportRequisitions by criteria: {}", criteria);

        // TODO Implement attachment for list
        Page<PdfReportRequisitionDTO> page = pdfReportRequisitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /pdf-report-requisitions/count} : count all the pdfReportRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/pdf-report-requisitions/count")
    public ResponseEntity<Long> countPdfReportRequisitions(PdfReportRequisitionCriteria criteria) {
        log.debug("REST request to count PdfReportRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(pdfReportRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /pdf-report-requisitions/:id} : get the "id" pdfReportRequisition.
     *
     * @param id the id of the pdfReportRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the pdfReportRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/pdf-report-requisitions/{id}")
    public ResponseEntity<AttachedPdfReportRequisitionDTO> getPdfReportRequisition(@PathVariable Long id) {
        log.debug("REST request to get PdfReportRequisition : {}", id);

        AtomicReference<AttachedPdfReportRequisitionDTO> attachedReport = new AtomicReference<>();

        pdfReportRequisitionService.findOne(id).ifPresentOrElse(theOne -> {
            attachedReport.set(reportAttachmentService.attachReport(reportRequisitionDTOMapping.toValue2(theOne)));
        }, () -> {throw new RuntimeException("We have failed to retrieve report id: " + id);});

        return  ResponseUtil.wrapOrNotFound(Optional.of(attachedReport.getAcquire()));
    }

    /**
     * {@code DELETE  /pdf-report-requisitions/:id} : delete the "id" pdfReportRequisition.
     *
     * @param id the id of the pdfReportRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/pdf-report-requisitions/{id}")
    public ResponseEntity<Void> deletePdfReportRequisition(@PathVariable Long id) {
        log.debug("REST request to delete PdfReportRequisition : {}", id);
        pdfReportRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/pdf-report-requisitions?query=:query} : search for the pdfReportRequisition corresponding
     * to the query.
     *
     * @param query the query of the pdfReportRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/pdf-report-requisitions")
    public ResponseEntity<List<PdfReportRequisitionDTO>> searchPdfReportRequisitions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PdfReportRequisitions for query {}", query);
        Page<PdfReportRequisitionDTO> page = pdfReportRequisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
