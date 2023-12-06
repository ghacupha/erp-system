package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import io.github.erp.repository.ManagementMemberTypeRepository;
import io.github.erp.service.ManagementMemberTypeQueryService;
import io.github.erp.service.ManagementMemberTypeService;
import io.github.erp.service.criteria.ManagementMemberTypeCriteria;
import io.github.erp.service.dto.ManagementMemberTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ManagementMemberType}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class ManagementMemberTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(ManagementMemberTypeResourceProd.class);

    private static final String ENTITY_NAME = "managementMemberType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ManagementMemberTypeService managementMemberTypeService;

    private final ManagementMemberTypeRepository managementMemberTypeRepository;

    private final ManagementMemberTypeQueryService managementMemberTypeQueryService;

    public ManagementMemberTypeResourceProd(
        ManagementMemberTypeService managementMemberTypeService,
        ManagementMemberTypeRepository managementMemberTypeRepository,
        ManagementMemberTypeQueryService managementMemberTypeQueryService
    ) {
        this.managementMemberTypeService = managementMemberTypeService;
        this.managementMemberTypeRepository = managementMemberTypeRepository;
        this.managementMemberTypeQueryService = managementMemberTypeQueryService;
    }

    /**
     * {@code POST  /management-member-types} : Create a new managementMemberType.
     *
     * @param managementMemberTypeDTO the managementMemberTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new managementMemberTypeDTO, or with status {@code 400 (Bad Request)} if the managementMemberType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/management-member-types")
    public ResponseEntity<ManagementMemberTypeDTO> createManagementMemberType(
        @Valid @RequestBody ManagementMemberTypeDTO managementMemberTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ManagementMemberType : {}", managementMemberTypeDTO);
        if (managementMemberTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new managementMemberType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ManagementMemberTypeDTO result = managementMemberTypeService.save(managementMemberTypeDTO);
        return ResponseEntity
            .created(new URI("/api/management-member-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /management-member-types/:id} : Updates an existing managementMemberType.
     *
     * @param id the id of the managementMemberTypeDTO to save.
     * @param managementMemberTypeDTO the managementMemberTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated managementMemberTypeDTO,
     * or with status {@code 400 (Bad Request)} if the managementMemberTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the managementMemberTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/management-member-types/{id}")
    public ResponseEntity<ManagementMemberTypeDTO> updateManagementMemberType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ManagementMemberTypeDTO managementMemberTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ManagementMemberType : {}, {}", id, managementMemberTypeDTO);
        if (managementMemberTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, managementMemberTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!managementMemberTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ManagementMemberTypeDTO result = managementMemberTypeService.save(managementMemberTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, managementMemberTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /management-member-types/:id} : Partial updates given fields of an existing managementMemberType, field will ignore if it is null
     *
     * @param id the id of the managementMemberTypeDTO to save.
     * @param managementMemberTypeDTO the managementMemberTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated managementMemberTypeDTO,
     * or with status {@code 400 (Bad Request)} if the managementMemberTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the managementMemberTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the managementMemberTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/management-member-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ManagementMemberTypeDTO> partialUpdateManagementMemberType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ManagementMemberTypeDTO managementMemberTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ManagementMemberType partially : {}, {}", id, managementMemberTypeDTO);
        if (managementMemberTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, managementMemberTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!managementMemberTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ManagementMemberTypeDTO> result = managementMemberTypeService.partialUpdate(managementMemberTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, managementMemberTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /management-member-types} : get all the managementMemberTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of managementMemberTypes in body.
     */
    @GetMapping("/management-member-types")
    public ResponseEntity<List<ManagementMemberTypeDTO>> getAllManagementMemberTypes(
        ManagementMemberTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ManagementMemberTypes by criteria: {}", criteria);
        Page<ManagementMemberTypeDTO> page = managementMemberTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /management-member-types/count} : count all the managementMemberTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/management-member-types/count")
    public ResponseEntity<Long> countManagementMemberTypes(ManagementMemberTypeCriteria criteria) {
        log.debug("REST request to count ManagementMemberTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(managementMemberTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /management-member-types/:id} : get the "id" managementMemberType.
     *
     * @param id the id of the managementMemberTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the managementMemberTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/management-member-types/{id}")
    public ResponseEntity<ManagementMemberTypeDTO> getManagementMemberType(@PathVariable Long id) {
        log.debug("REST request to get ManagementMemberType : {}", id);
        Optional<ManagementMemberTypeDTO> managementMemberTypeDTO = managementMemberTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(managementMemberTypeDTO);
    }

    /**
     * {@code DELETE  /management-member-types/:id} : delete the "id" managementMemberType.
     *
     * @param id the id of the managementMemberTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/management-member-types/{id}")
    public ResponseEntity<Void> deleteManagementMemberType(@PathVariable Long id) {
        log.debug("REST request to delete ManagementMemberType : {}", id);
        managementMemberTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/management-member-types?query=:query} : search for the managementMemberType corresponding
     * to the query.
     *
     * @param query the query of the managementMemberType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/management-member-types")
    public ResponseEntity<List<ManagementMemberTypeDTO>> searchManagementMemberTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ManagementMemberTypes for query {}", query);
        Page<ManagementMemberTypeDTO> page = managementMemberTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
