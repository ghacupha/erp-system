package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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

import io.github.erp.repository.InstitutionContactDetailsRepository;
import io.github.erp.service.InstitutionContactDetailsQueryService;
import io.github.erp.service.InstitutionContactDetailsService;
import io.github.erp.service.criteria.InstitutionContactDetailsCriteria;
import io.github.erp.service.dto.InstitutionContactDetailsDTO;
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
 * REST controller for managing {@link io.github.erp.domain.InstitutionContactDetails}.
 */
@RestController
@RequestMapping("/api")
public class InstitutionContactDetailsResource {

    private final Logger log = LoggerFactory.getLogger(InstitutionContactDetailsResource.class);

    private static final String ENTITY_NAME = "institutionContactDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstitutionContactDetailsService institutionContactDetailsService;

    private final InstitutionContactDetailsRepository institutionContactDetailsRepository;

    private final InstitutionContactDetailsQueryService institutionContactDetailsQueryService;

    public InstitutionContactDetailsResource(
        InstitutionContactDetailsService institutionContactDetailsService,
        InstitutionContactDetailsRepository institutionContactDetailsRepository,
        InstitutionContactDetailsQueryService institutionContactDetailsQueryService
    ) {
        this.institutionContactDetailsService = institutionContactDetailsService;
        this.institutionContactDetailsRepository = institutionContactDetailsRepository;
        this.institutionContactDetailsQueryService = institutionContactDetailsQueryService;
    }

    /**
     * {@code POST  /institution-contact-details} : Create a new institutionContactDetails.
     *
     * @param institutionContactDetailsDTO the institutionContactDetailsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new institutionContactDetailsDTO, or with status {@code 400 (Bad Request)} if the institutionContactDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/institution-contact-details")
    public ResponseEntity<InstitutionContactDetailsDTO> createInstitutionContactDetails(
        @Valid @RequestBody InstitutionContactDetailsDTO institutionContactDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InstitutionContactDetails : {}", institutionContactDetailsDTO);
        if (institutionContactDetailsDTO.getId() != null) {
            throw new BadRequestAlertException("A new institutionContactDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstitutionContactDetailsDTO result = institutionContactDetailsService.save(institutionContactDetailsDTO);
        return ResponseEntity
            .created(new URI("/api/institution-contact-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /institution-contact-details/:id} : Updates an existing institutionContactDetails.
     *
     * @param id the id of the institutionContactDetailsDTO to save.
     * @param institutionContactDetailsDTO the institutionContactDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institutionContactDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the institutionContactDetailsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the institutionContactDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/institution-contact-details/{id}")
    public ResponseEntity<InstitutionContactDetailsDTO> updateInstitutionContactDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InstitutionContactDetailsDTO institutionContactDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InstitutionContactDetails : {}, {}", id, institutionContactDetailsDTO);
        if (institutionContactDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institutionContactDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!institutionContactDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InstitutionContactDetailsDTO result = institutionContactDetailsService.save(institutionContactDetailsDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, institutionContactDetailsDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /institution-contact-details/:id} : Partial updates given fields of an existing institutionContactDetails, field will ignore if it is null
     *
     * @param id the id of the institutionContactDetailsDTO to save.
     * @param institutionContactDetailsDTO the institutionContactDetailsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institutionContactDetailsDTO,
     * or with status {@code 400 (Bad Request)} if the institutionContactDetailsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the institutionContactDetailsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the institutionContactDetailsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/institution-contact-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InstitutionContactDetailsDTO> partialUpdateInstitutionContactDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InstitutionContactDetailsDTO institutionContactDetailsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InstitutionContactDetails partially : {}, {}", id, institutionContactDetailsDTO);
        if (institutionContactDetailsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institutionContactDetailsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!institutionContactDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InstitutionContactDetailsDTO> result = institutionContactDetailsService.partialUpdate(institutionContactDetailsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, institutionContactDetailsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /institution-contact-details} : get all the institutionContactDetails.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of institutionContactDetails in body.
     */
    @GetMapping("/institution-contact-details")
    public ResponseEntity<List<InstitutionContactDetailsDTO>> getAllInstitutionContactDetails(
        InstitutionContactDetailsCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get InstitutionContactDetails by criteria: {}", criteria);
        Page<InstitutionContactDetailsDTO> page = institutionContactDetailsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /institution-contact-details/count} : count all the institutionContactDetails.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/institution-contact-details/count")
    public ResponseEntity<Long> countInstitutionContactDetails(InstitutionContactDetailsCriteria criteria) {
        log.debug("REST request to count InstitutionContactDetails by criteria: {}", criteria);
        return ResponseEntity.ok().body(institutionContactDetailsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /institution-contact-details/:id} : get the "id" institutionContactDetails.
     *
     * @param id the id of the institutionContactDetailsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the institutionContactDetailsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/institution-contact-details/{id}")
    public ResponseEntity<InstitutionContactDetailsDTO> getInstitutionContactDetails(@PathVariable Long id) {
        log.debug("REST request to get InstitutionContactDetails : {}", id);
        Optional<InstitutionContactDetailsDTO> institutionContactDetailsDTO = institutionContactDetailsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(institutionContactDetailsDTO);
    }

    /**
     * {@code DELETE  /institution-contact-details/:id} : delete the "id" institutionContactDetails.
     *
     * @param id the id of the institutionContactDetailsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/institution-contact-details/{id}")
    public ResponseEntity<Void> deleteInstitutionContactDetails(@PathVariable Long id) {
        log.debug("REST request to delete InstitutionContactDetails : {}", id);
        institutionContactDetailsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/institution-contact-details?query=:query} : search for the institutionContactDetails corresponding
     * to the query.
     *
     * @param query the query of the institutionContactDetails search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/institution-contact-details")
    public ResponseEntity<List<InstitutionContactDetailsDTO>> searchInstitutionContactDetails(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of InstitutionContactDetails for query {}", query);
        Page<InstitutionContactDetailsDTO> page = institutionContactDetailsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
