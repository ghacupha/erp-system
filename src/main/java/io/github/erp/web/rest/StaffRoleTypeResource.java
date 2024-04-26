package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.StaffRoleTypeRepository;
import io.github.erp.service.StaffRoleTypeQueryService;
import io.github.erp.service.StaffRoleTypeService;
import io.github.erp.service.criteria.StaffRoleTypeCriteria;
import io.github.erp.service.dto.StaffRoleTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.StaffRoleType}.
 */
@RestController
@RequestMapping("/api")
public class StaffRoleTypeResource {

    private final Logger log = LoggerFactory.getLogger(StaffRoleTypeResource.class);

    private static final String ENTITY_NAME = "staffRoleType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StaffRoleTypeService staffRoleTypeService;

    private final StaffRoleTypeRepository staffRoleTypeRepository;

    private final StaffRoleTypeQueryService staffRoleTypeQueryService;

    public StaffRoleTypeResource(
        StaffRoleTypeService staffRoleTypeService,
        StaffRoleTypeRepository staffRoleTypeRepository,
        StaffRoleTypeQueryService staffRoleTypeQueryService
    ) {
        this.staffRoleTypeService = staffRoleTypeService;
        this.staffRoleTypeRepository = staffRoleTypeRepository;
        this.staffRoleTypeQueryService = staffRoleTypeQueryService;
    }

    /**
     * {@code POST  /staff-role-types} : Create a new staffRoleType.
     *
     * @param staffRoleTypeDTO the staffRoleTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staffRoleTypeDTO, or with status {@code 400 (Bad Request)} if the staffRoleType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/staff-role-types")
    public ResponseEntity<StaffRoleTypeDTO> createStaffRoleType(@Valid @RequestBody StaffRoleTypeDTO staffRoleTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save StaffRoleType : {}", staffRoleTypeDTO);
        if (staffRoleTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new staffRoleType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StaffRoleTypeDTO result = staffRoleTypeService.save(staffRoleTypeDTO);
        return ResponseEntity
            .created(new URI("/api/staff-role-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /staff-role-types/:id} : Updates an existing staffRoleType.
     *
     * @param id the id of the staffRoleTypeDTO to save.
     * @param staffRoleTypeDTO the staffRoleTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staffRoleTypeDTO,
     * or with status {@code 400 (Bad Request)} if the staffRoleTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staffRoleTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/staff-role-types/{id}")
    public ResponseEntity<StaffRoleTypeDTO> updateStaffRoleType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StaffRoleTypeDTO staffRoleTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StaffRoleType : {}, {}", id, staffRoleTypeDTO);
        if (staffRoleTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staffRoleTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffRoleTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StaffRoleTypeDTO result = staffRoleTypeService.save(staffRoleTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staffRoleTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /staff-role-types/:id} : Partial updates given fields of an existing staffRoleType, field will ignore if it is null
     *
     * @param id the id of the staffRoleTypeDTO to save.
     * @param staffRoleTypeDTO the staffRoleTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staffRoleTypeDTO,
     * or with status {@code 400 (Bad Request)} if the staffRoleTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the staffRoleTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the staffRoleTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/staff-role-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StaffRoleTypeDTO> partialUpdateStaffRoleType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StaffRoleTypeDTO staffRoleTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StaffRoleType partially : {}, {}", id, staffRoleTypeDTO);
        if (staffRoleTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staffRoleTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffRoleTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StaffRoleTypeDTO> result = staffRoleTypeService.partialUpdate(staffRoleTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staffRoleTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /staff-role-types} : get all the staffRoleTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staffRoleTypes in body.
     */
    @GetMapping("/staff-role-types")
    public ResponseEntity<List<StaffRoleTypeDTO>> getAllStaffRoleTypes(StaffRoleTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get StaffRoleTypes by criteria: {}", criteria);
        Page<StaffRoleTypeDTO> page = staffRoleTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /staff-role-types/count} : count all the staffRoleTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/staff-role-types/count")
    public ResponseEntity<Long> countStaffRoleTypes(StaffRoleTypeCriteria criteria) {
        log.debug("REST request to count StaffRoleTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(staffRoleTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /staff-role-types/:id} : get the "id" staffRoleType.
     *
     * @param id the id of the staffRoleTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staffRoleTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/staff-role-types/{id}")
    public ResponseEntity<StaffRoleTypeDTO> getStaffRoleType(@PathVariable Long id) {
        log.debug("REST request to get StaffRoleType : {}", id);
        Optional<StaffRoleTypeDTO> staffRoleTypeDTO = staffRoleTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(staffRoleTypeDTO);
    }

    /**
     * {@code DELETE  /staff-role-types/:id} : delete the "id" staffRoleType.
     *
     * @param id the id of the staffRoleTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/staff-role-types/{id}")
    public ResponseEntity<Void> deleteStaffRoleType(@PathVariable Long id) {
        log.debug("REST request to delete StaffRoleType : {}", id);
        staffRoleTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/staff-role-types?query=:query} : search for the staffRoleType corresponding
     * to the query.
     *
     * @param query the query of the staffRoleType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/staff-role-types")
    public ResponseEntity<List<StaffRoleTypeDTO>> searchStaffRoleTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of StaffRoleTypes for query {}", query);
        Page<StaffRoleTypeDTO> page = staffRoleTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
