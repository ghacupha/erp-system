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

import io.github.erp.repository.InstitutionStatusTypeRepository;
import io.github.erp.service.InstitutionStatusTypeQueryService;
import io.github.erp.service.InstitutionStatusTypeService;
import io.github.erp.service.criteria.InstitutionStatusTypeCriteria;
import io.github.erp.service.dto.InstitutionStatusTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.InstitutionStatusType}.
 */
@RestController
@RequestMapping("/api")
public class InstitutionStatusTypeResource {

    private final Logger log = LoggerFactory.getLogger(InstitutionStatusTypeResource.class);

    private static final String ENTITY_NAME = "institutionStatusType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstitutionStatusTypeService institutionStatusTypeService;

    private final InstitutionStatusTypeRepository institutionStatusTypeRepository;

    private final InstitutionStatusTypeQueryService institutionStatusTypeQueryService;

    public InstitutionStatusTypeResource(
        InstitutionStatusTypeService institutionStatusTypeService,
        InstitutionStatusTypeRepository institutionStatusTypeRepository,
        InstitutionStatusTypeQueryService institutionStatusTypeQueryService
    ) {
        this.institutionStatusTypeService = institutionStatusTypeService;
        this.institutionStatusTypeRepository = institutionStatusTypeRepository;
        this.institutionStatusTypeQueryService = institutionStatusTypeQueryService;
    }

    /**
     * {@code POST  /institution-status-types} : Create a new institutionStatusType.
     *
     * @param institutionStatusTypeDTO the institutionStatusTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new institutionStatusTypeDTO, or with status {@code 400 (Bad Request)} if the institutionStatusType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/institution-status-types")
    public ResponseEntity<InstitutionStatusTypeDTO> createInstitutionStatusType(
        @Valid @RequestBody InstitutionStatusTypeDTO institutionStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InstitutionStatusType : {}", institutionStatusTypeDTO);
        if (institutionStatusTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new institutionStatusType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstitutionStatusTypeDTO result = institutionStatusTypeService.save(institutionStatusTypeDTO);
        return ResponseEntity
            .created(new URI("/api/institution-status-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /institution-status-types/:id} : Updates an existing institutionStatusType.
     *
     * @param id the id of the institutionStatusTypeDTO to save.
     * @param institutionStatusTypeDTO the institutionStatusTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institutionStatusTypeDTO,
     * or with status {@code 400 (Bad Request)} if the institutionStatusTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the institutionStatusTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/institution-status-types/{id}")
    public ResponseEntity<InstitutionStatusTypeDTO> updateInstitutionStatusType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InstitutionStatusTypeDTO institutionStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InstitutionStatusType : {}, {}", id, institutionStatusTypeDTO);
        if (institutionStatusTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institutionStatusTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!institutionStatusTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InstitutionStatusTypeDTO result = institutionStatusTypeService.save(institutionStatusTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, institutionStatusTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /institution-status-types/:id} : Partial updates given fields of an existing institutionStatusType, field will ignore if it is null
     *
     * @param id the id of the institutionStatusTypeDTO to save.
     * @param institutionStatusTypeDTO the institutionStatusTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institutionStatusTypeDTO,
     * or with status {@code 400 (Bad Request)} if the institutionStatusTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the institutionStatusTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the institutionStatusTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/institution-status-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InstitutionStatusTypeDTO> partialUpdateInstitutionStatusType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InstitutionStatusTypeDTO institutionStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InstitutionStatusType partially : {}, {}", id, institutionStatusTypeDTO);
        if (institutionStatusTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institutionStatusTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!institutionStatusTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InstitutionStatusTypeDTO> result = institutionStatusTypeService.partialUpdate(institutionStatusTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, institutionStatusTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /institution-status-types} : get all the institutionStatusTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of institutionStatusTypes in body.
     */
    @GetMapping("/institution-status-types")
    public ResponseEntity<List<InstitutionStatusTypeDTO>> getAllInstitutionStatusTypes(
        InstitutionStatusTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get InstitutionStatusTypes by criteria: {}", criteria);
        Page<InstitutionStatusTypeDTO> page = institutionStatusTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /institution-status-types/count} : count all the institutionStatusTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/institution-status-types/count")
    public ResponseEntity<Long> countInstitutionStatusTypes(InstitutionStatusTypeCriteria criteria) {
        log.debug("REST request to count InstitutionStatusTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(institutionStatusTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /institution-status-types/:id} : get the "id" institutionStatusType.
     *
     * @param id the id of the institutionStatusTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the institutionStatusTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/institution-status-types/{id}")
    public ResponseEntity<InstitutionStatusTypeDTO> getInstitutionStatusType(@PathVariable Long id) {
        log.debug("REST request to get InstitutionStatusType : {}", id);
        Optional<InstitutionStatusTypeDTO> institutionStatusTypeDTO = institutionStatusTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(institutionStatusTypeDTO);
    }

    /**
     * {@code DELETE  /institution-status-types/:id} : delete the "id" institutionStatusType.
     *
     * @param id the id of the institutionStatusTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/institution-status-types/{id}")
    public ResponseEntity<Void> deleteInstitutionStatusType(@PathVariable Long id) {
        log.debug("REST request to delete InstitutionStatusType : {}", id);
        institutionStatusTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/institution-status-types?query=:query} : search for the institutionStatusType corresponding
     * to the query.
     *
     * @param query the query of the institutionStatusType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/institution-status-types")
    public ResponseEntity<List<InstitutionStatusTypeDTO>> searchInstitutionStatusTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InstitutionStatusTypes for query {}", query);
        Page<InstitutionStatusTypeDTO> page = institutionStatusTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
