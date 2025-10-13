package io.github.erp.erp.resources;

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
import io.github.erp.internal.model.BusinessDocumentFSO;
import io.github.erp.internal.model.mapping.BusinessDocumentFSOMapping;
import io.github.erp.internal.service.applicationUser.InternalApplicationUserDetailService;
import io.github.erp.repository.BusinessDocumentRepository;
import io.github.erp.service.BusinessDocumentQueryService;
import io.github.erp.service.BusinessDocumentService;
import io.github.erp.service.criteria.BusinessDocumentCriteria;
import io.github.erp.service.dto.BusinessDocumentDTO;
import io.github.erp.service.mapper.ApplicationUserMapper;
import io.github.erp.web.rest.errors.BadRequestAlertException;
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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.BusinessDocument}.
 */
@RestController("businessDocumentResourceProd")
@RequestMapping("/api/docs")
public class BusinessDocumentResourceProd {

    private final Logger log = LoggerFactory.getLogger(BusinessDocumentResourceProd.class);

    private static final String ENTITY_NAME = "businessDocument";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessDocumentService businessDocumentService;

    private final BusinessDocumentRepository businessDocumentRepository;

    private final BusinessDocumentQueryService businessDocumentQueryService;

    private final BusinessDocumentFSOMapping businessDocumentFSOMapping;

    private final InternalApplicationUserDetailService userDetailService;

    private final ApplicationUserMapper applicationUserMapper;

    public BusinessDocumentResourceProd(
        BusinessDocumentService businessDocumentService,
        BusinessDocumentRepository businessDocumentRepository,
        BusinessDocumentQueryService businessDocumentQueryService,
        BusinessDocumentFSOMapping businessDocumentFSOMapping,
        InternalApplicationUserDetailService userDetailService,
        ApplicationUserMapper applicationUserMapper) {
        this.businessDocumentService = businessDocumentService;
        this.businessDocumentRepository = businessDocumentRepository;
        this.businessDocumentQueryService = businessDocumentQueryService;
        this.businessDocumentFSOMapping = businessDocumentFSOMapping;
        this.userDetailService = userDetailService;
        this.applicationUserMapper = applicationUserMapper;
    }

    /**
     * {@code POST  /business-documents} : Create a new businessDocument.
     *
     * @param businessDocumentDTO the businessDocumentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessDocumentDTO, or with status {@code 400 (Bad Request)} if the businessDocument has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-documents")
    public ResponseEntity<BusinessDocumentFSO> createBusinessDocument(@Valid @RequestBody BusinessDocumentFSO businessDocumentDTO)
        throws URISyntaxException {
        log.debug("REST request to save BusinessDocument : {}", businessDocumentDTO);
        if (businessDocumentDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessDocument cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessDocumentDTO result = businessDocumentService.save(businessDocumentFSOMapping.toValue2(businessDocumentDTO));

        return ResponseEntity
            .created(new URI("/api/docs/business-documents/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(businessDocumentFSOMapping.toValue1(result));
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

        BusinessDocumentDTO result = null;

        if (userDetailService.getCurrentApplicationUser().isPresent()) {

            businessDocumentDTO.setLastModified(ZonedDateTime.now());
            businessDocumentDTO.setLastModifiedBy(userDetailService.getCurrentApplicationUser().get());

             result = businessDocumentService.save(businessDocumentDTO);

        } else {

            throw new BadRequestAlertException("ApplicationUser not found", ENTITY_NAME, "idnotfound");
        }

        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, businessDocumentDTO.getId().toString()))
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

        // Optional<BusinessDocumentDTO> result = businessDocumentService.partialUpdate(businessDocumentDTO);

        Optional<BusinessDocumentDTO> result = null;

        if (userDetailService.getCurrentApplicationUser().isPresent()) {

            businessDocumentDTO.setLastModified(ZonedDateTime.now());
            businessDocumentDTO.setLastModifiedBy(userDetailService.getCurrentApplicationUser().get());

            result = businessDocumentService.partialUpdate(businessDocumentDTO);

        } else {

            throw new BadRequestAlertException("ApplicationUser not found", ENTITY_NAME, "idnotfound");
        }


        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, businessDocumentDTO.getId().toString())
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
    public ResponseEntity<BusinessDocumentFSO> getBusinessDocument(@PathVariable Long id) {
        log.debug("REST request to get BusinessDocument : {}", id);
        final Optional[] businessDocument = new Optional[]{Optional.empty()};
        businessDocumentService.findOne(id).ifPresent(doc -> {
            businessDocument[0] = Optional.ofNullable(businessDocumentFSOMapping.toValue1(doc));
        });
        return ResponseUtil.wrapOrNotFound(businessDocument[0]);
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
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
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
