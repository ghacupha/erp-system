package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import io.github.erp.repository.BusinessDocumentRepository;
import io.github.erp.service.BusinessDocumentQueryService;
import io.github.erp.service.BusinessDocumentService;
import io.github.erp.service.criteria.BusinessDocumentCriteria;
import io.github.erp.service.dto.BusinessDocumentDTO;
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
 * REST controller for managing {@link io.github.erp.domain.BusinessDocument}.
 */
@RestController
@RequestMapping("/api")
public class BusinessDocumentResource {

    private final Logger log = LoggerFactory.getLogger(BusinessDocumentResource.class);

    private static final String ENTITY_NAME = "businessDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessDocumentService businessDocumentService;

    private final BusinessDocumentRepository businessDocumentRepository;

    private final BusinessDocumentQueryService businessDocumentQueryService;

    public BusinessDocumentResource(
        BusinessDocumentService businessDocumentService,
        BusinessDocumentRepository businessDocumentRepository,
        BusinessDocumentQueryService businessDocumentQueryService
    ) {
        this.businessDocumentService = businessDocumentService;
        this.businessDocumentRepository = businessDocumentRepository;
        this.businessDocumentQueryService = businessDocumentQueryService;
    }

    /**
     * {@code POST  /business-documents} : Create a new businessDocument.
     *
     * @param businessDocumentDTO the businessDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessDocumentDTO, or with status {@code 400 (Bad Request)} if the businessDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-documents")
    public ResponseEntity<BusinessDocumentDTO> createBusinessDocument(@Valid @RequestBody BusinessDocumentDTO businessDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save BusinessDocument : {}", businessDocumentDTO);
        if (businessDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessDocumentDTO result = businessDocumentService.save(businessDocumentDTO);
        return ResponseEntity
            .created(new URI("/api/business-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-documents/:id} : Updates an existing businessDocument.
     *
     * @param id the id of the businessDocumentDTO to save.
     * @param businessDocumentDTO the businessDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the businessDocumentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-documents/{id}")
    public ResponseEntity<BusinessDocumentDTO> updateBusinessDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessDocumentDTO businessDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessDocument : {}, {}", id, businessDocumentDTO);
        if (businessDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessDocumentDTO result = businessDocumentService.save(businessDocumentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessDocumentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-documents/:id} : Partial updates given fields of an existing businessDocument, field will ignore if it is null
     *
     * @param id the id of the businessDocumentDTO to save.
     * @param businessDocumentDTO the businessDocumentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessDocumentDTO,
     * or with status {@code 400 (Bad Request)} if the businessDocumentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the businessDocumentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessDocumentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/business-documents/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessDocumentDTO> partialUpdateBusinessDocument(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessDocumentDTO businessDocumentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessDocument partially : {}, {}", id, businessDocumentDTO);
        if (businessDocumentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessDocumentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessDocumentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessDocumentDTO> result = businessDocumentService.partialUpdate(businessDocumentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessDocumentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /business-documents} : get all the businessDocuments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessDocuments in body.
     */
    @GetMapping("/business-documents")
    public ResponseEntity<List<BusinessDocumentDTO>> getAllBusinessDocuments(BusinessDocumentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BusinessDocuments by criteria: {}", criteria);
        Page<BusinessDocumentDTO> page = businessDocumentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-documents/count} : count all the businessDocuments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/business-documents/count")
    public ResponseEntity<Long> countBusinessDocuments(BusinessDocumentCriteria criteria) {
        log.debug("REST request to count BusinessDocuments by criteria: {}", criteria);
        return ResponseEntity.ok().body(businessDocumentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /business-documents/:id} : get the "id" businessDocument.
     *
     * @param id the id of the businessDocumentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessDocumentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-documents/{id}")
    public ResponseEntity<BusinessDocumentDTO> getBusinessDocument(@PathVariable Long id) {
        log.debug("REST request to get BusinessDocument : {}", id);
        Optional<BusinessDocumentDTO> businessDocumentDTO = businessDocumentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessDocumentDTO);
    }

    /**
     * {@code DELETE  /business-documents/:id} : delete the "id" businessDocument.
     *
     * @param id the id of the businessDocumentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-documents/{id}")
    public ResponseEntity<Void> deleteBusinessDocument(@PathVariable Long id) {
        log.debug("REST request to delete BusinessDocument : {}", id);
        businessDocumentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/business-documents?query=:query} : search for the businessDocument corresponding
     * to the query.
     *
     * @param query the query of the businessDocument search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/business-documents")
    public ResponseEntity<List<BusinessDocumentDTO>> searchBusinessDocuments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BusinessDocuments for query {}", query);
        Page<BusinessDocumentDTO> page = businessDocumentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
