package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.BusinessSegmentTypesRepository;
import io.github.erp.service.BusinessSegmentTypesQueryService;
import io.github.erp.service.BusinessSegmentTypesService;
import io.github.erp.service.criteria.BusinessSegmentTypesCriteria;
import io.github.erp.service.dto.BusinessSegmentTypesDTO;
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
 * REST controller for managing {@link io.github.erp.domain.BusinessSegmentTypes}.
 */
@RestController
@RequestMapping("/api")
public class BusinessSegmentTypesResource {

    private final Logger log = LoggerFactory.getLogger(BusinessSegmentTypesResource.class);

    private static final String ENTITY_NAME = "businessSegmentTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessSegmentTypesService businessSegmentTypesService;

    private final BusinessSegmentTypesRepository businessSegmentTypesRepository;

    private final BusinessSegmentTypesQueryService businessSegmentTypesQueryService;

    public BusinessSegmentTypesResource(
        BusinessSegmentTypesService businessSegmentTypesService,
        BusinessSegmentTypesRepository businessSegmentTypesRepository,
        BusinessSegmentTypesQueryService businessSegmentTypesQueryService
    ) {
        this.businessSegmentTypesService = businessSegmentTypesService;
        this.businessSegmentTypesRepository = businessSegmentTypesRepository;
        this.businessSegmentTypesQueryService = businessSegmentTypesQueryService;
    }

    /**
     * {@code POST  /business-segment-types} : Create a new businessSegmentTypes.
     *
     * @param businessSegmentTypesDTO the businessSegmentTypesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessSegmentTypesDTO, or with status {@code 400 (Bad Request)} if the businessSegmentTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-segment-types")
    public ResponseEntity<BusinessSegmentTypesDTO> createBusinessSegmentTypes(
        @Valid @RequestBody BusinessSegmentTypesDTO businessSegmentTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to save BusinessSegmentTypes : {}", businessSegmentTypesDTO);
        if (businessSegmentTypesDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessSegmentTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessSegmentTypesDTO result = businessSegmentTypesService.save(businessSegmentTypesDTO);
        return ResponseEntity
            .created(new URI("/api/business-segment-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-segment-types/:id} : Updates an existing businessSegmentTypes.
     *
     * @param id the id of the businessSegmentTypesDTO to save.
     * @param businessSegmentTypesDTO the businessSegmentTypesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessSegmentTypesDTO,
     * or with status {@code 400 (Bad Request)} if the businessSegmentTypesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessSegmentTypesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-segment-types/{id}")
    public ResponseEntity<BusinessSegmentTypesDTO> updateBusinessSegmentTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessSegmentTypesDTO businessSegmentTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessSegmentTypes : {}, {}", id, businessSegmentTypesDTO);
        if (businessSegmentTypesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessSegmentTypesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessSegmentTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessSegmentTypesDTO result = businessSegmentTypesService.save(businessSegmentTypesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessSegmentTypesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-segment-types/:id} : Partial updates given fields of an existing businessSegmentTypes, field will ignore if it is null
     *
     * @param id the id of the businessSegmentTypesDTO to save.
     * @param businessSegmentTypesDTO the businessSegmentTypesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessSegmentTypesDTO,
     * or with status {@code 400 (Bad Request)} if the businessSegmentTypesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the businessSegmentTypesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessSegmentTypesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/business-segment-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessSegmentTypesDTO> partialUpdateBusinessSegmentTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessSegmentTypesDTO businessSegmentTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessSegmentTypes partially : {}, {}", id, businessSegmentTypesDTO);
        if (businessSegmentTypesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessSegmentTypesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessSegmentTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessSegmentTypesDTO> result = businessSegmentTypesService.partialUpdate(businessSegmentTypesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessSegmentTypesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /business-segment-types} : get all the businessSegmentTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessSegmentTypes in body.
     */
    @GetMapping("/business-segment-types")
    public ResponseEntity<List<BusinessSegmentTypesDTO>> getAllBusinessSegmentTypes(
        BusinessSegmentTypesCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get BusinessSegmentTypes by criteria: {}", criteria);
        Page<BusinessSegmentTypesDTO> page = businessSegmentTypesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-segment-types/count} : count all the businessSegmentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/business-segment-types/count")
    public ResponseEntity<Long> countBusinessSegmentTypes(BusinessSegmentTypesCriteria criteria) {
        log.debug("REST request to count BusinessSegmentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(businessSegmentTypesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /business-segment-types/:id} : get the "id" businessSegmentTypes.
     *
     * @param id the id of the businessSegmentTypesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessSegmentTypesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-segment-types/{id}")
    public ResponseEntity<BusinessSegmentTypesDTO> getBusinessSegmentTypes(@PathVariable Long id) {
        log.debug("REST request to get BusinessSegmentTypes : {}", id);
        Optional<BusinessSegmentTypesDTO> businessSegmentTypesDTO = businessSegmentTypesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessSegmentTypesDTO);
    }

    /**
     * {@code DELETE  /business-segment-types/:id} : delete the "id" businessSegmentTypes.
     *
     * @param id the id of the businessSegmentTypesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-segment-types/{id}")
    public ResponseEntity<Void> deleteBusinessSegmentTypes(@PathVariable Long id) {
        log.debug("REST request to delete BusinessSegmentTypes : {}", id);
        businessSegmentTypesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/business-segment-types?query=:query} : search for the businessSegmentTypes corresponding
     * to the query.
     *
     * @param query the query of the businessSegmentTypes search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/business-segment-types")
    public ResponseEntity<List<BusinessSegmentTypesDTO>> searchBusinessSegmentTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BusinessSegmentTypes for query {}", query);
        Page<BusinessSegmentTypesDTO> page = businessSegmentTypesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
