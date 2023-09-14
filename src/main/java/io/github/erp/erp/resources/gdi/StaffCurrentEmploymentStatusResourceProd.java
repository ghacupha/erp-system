package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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

import io.github.erp.repository.StaffCurrentEmploymentStatusRepository;
import io.github.erp.service.StaffCurrentEmploymentStatusQueryService;
import io.github.erp.service.StaffCurrentEmploymentStatusService;
import io.github.erp.service.criteria.StaffCurrentEmploymentStatusCriteria;
import io.github.erp.service.dto.StaffCurrentEmploymentStatusDTO;
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
 * REST controller for managing {@link io.github.erp.domain.StaffCurrentEmploymentStatus}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class StaffCurrentEmploymentStatusResourceProd {

    private final Logger log = LoggerFactory.getLogger(StaffCurrentEmploymentStatusResourceProd.class);

    private static final String ENTITY_NAME = "staffCurrentEmploymentStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StaffCurrentEmploymentStatusService staffCurrentEmploymentStatusService;

    private final StaffCurrentEmploymentStatusRepository staffCurrentEmploymentStatusRepository;

    private final StaffCurrentEmploymentStatusQueryService staffCurrentEmploymentStatusQueryService;

    public StaffCurrentEmploymentStatusResourceProd(
        StaffCurrentEmploymentStatusService staffCurrentEmploymentStatusService,
        StaffCurrentEmploymentStatusRepository staffCurrentEmploymentStatusRepository,
        StaffCurrentEmploymentStatusQueryService staffCurrentEmploymentStatusQueryService
    ) {
        this.staffCurrentEmploymentStatusService = staffCurrentEmploymentStatusService;
        this.staffCurrentEmploymentStatusRepository = staffCurrentEmploymentStatusRepository;
        this.staffCurrentEmploymentStatusQueryService = staffCurrentEmploymentStatusQueryService;
    }

    /**
     * {@code POST  /staff-current-employment-statuses} : Create a new staffCurrentEmploymentStatus.
     *
     * @param staffCurrentEmploymentStatusDTO the staffCurrentEmploymentStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staffCurrentEmploymentStatusDTO, or with status {@code 400 (Bad Request)} if the staffCurrentEmploymentStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/staff-current-employment-statuses")
    public ResponseEntity<StaffCurrentEmploymentStatusDTO> createStaffCurrentEmploymentStatus(
        @Valid @RequestBody StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to save StaffCurrentEmploymentStatus : {}", staffCurrentEmploymentStatusDTO);
        if (staffCurrentEmploymentStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new staffCurrentEmploymentStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StaffCurrentEmploymentStatusDTO result = staffCurrentEmploymentStatusService.save(staffCurrentEmploymentStatusDTO);
        return ResponseEntity
            .created(new URI("/api/staff-current-employment-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /staff-current-employment-statuses/:id} : Updates an existing staffCurrentEmploymentStatus.
     *
     * @param id the id of the staffCurrentEmploymentStatusDTO to save.
     * @param staffCurrentEmploymentStatusDTO the staffCurrentEmploymentStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staffCurrentEmploymentStatusDTO,
     * or with status {@code 400 (Bad Request)} if the staffCurrentEmploymentStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staffCurrentEmploymentStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/staff-current-employment-statuses/{id}")
    public ResponseEntity<StaffCurrentEmploymentStatusDTO> updateStaffCurrentEmploymentStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update StaffCurrentEmploymentStatus : {}, {}", id, staffCurrentEmploymentStatusDTO);
        if (staffCurrentEmploymentStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staffCurrentEmploymentStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffCurrentEmploymentStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StaffCurrentEmploymentStatusDTO result = staffCurrentEmploymentStatusService.save(staffCurrentEmploymentStatusDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staffCurrentEmploymentStatusDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /staff-current-employment-statuses/:id} : Partial updates given fields of an existing staffCurrentEmploymentStatus, field will ignore if it is null
     *
     * @param id the id of the staffCurrentEmploymentStatusDTO to save.
     * @param staffCurrentEmploymentStatusDTO the staffCurrentEmploymentStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staffCurrentEmploymentStatusDTO,
     * or with status {@code 400 (Bad Request)} if the staffCurrentEmploymentStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the staffCurrentEmploymentStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the staffCurrentEmploymentStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/staff-current-employment-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StaffCurrentEmploymentStatusDTO> partialUpdateStaffCurrentEmploymentStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StaffCurrentEmploymentStatusDTO staffCurrentEmploymentStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update StaffCurrentEmploymentStatus partially : {}, {}", id, staffCurrentEmploymentStatusDTO);
        if (staffCurrentEmploymentStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staffCurrentEmploymentStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staffCurrentEmploymentStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StaffCurrentEmploymentStatusDTO> result = staffCurrentEmploymentStatusService.partialUpdate(
            staffCurrentEmploymentStatusDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staffCurrentEmploymentStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /staff-current-employment-statuses} : get all the staffCurrentEmploymentStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staffCurrentEmploymentStatuses in body.
     */
    @GetMapping("/staff-current-employment-statuses")
    public ResponseEntity<List<StaffCurrentEmploymentStatusDTO>> getAllStaffCurrentEmploymentStatuses(
        StaffCurrentEmploymentStatusCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get StaffCurrentEmploymentStatuses by criteria: {}", criteria);
        Page<StaffCurrentEmploymentStatusDTO> page = staffCurrentEmploymentStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /staff-current-employment-statuses/count} : count all the staffCurrentEmploymentStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/staff-current-employment-statuses/count")
    public ResponseEntity<Long> countStaffCurrentEmploymentStatuses(StaffCurrentEmploymentStatusCriteria criteria) {
        log.debug("REST request to count StaffCurrentEmploymentStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(staffCurrentEmploymentStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /staff-current-employment-statuses/:id} : get the "id" staffCurrentEmploymentStatus.
     *
     * @param id the id of the staffCurrentEmploymentStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staffCurrentEmploymentStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/staff-current-employment-statuses/{id}")
    public ResponseEntity<StaffCurrentEmploymentStatusDTO> getStaffCurrentEmploymentStatus(@PathVariable Long id) {
        log.debug("REST request to get StaffCurrentEmploymentStatus : {}", id);
        Optional<StaffCurrentEmploymentStatusDTO> staffCurrentEmploymentStatusDTO = staffCurrentEmploymentStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(staffCurrentEmploymentStatusDTO);
    }

    /**
     * {@code DELETE  /staff-current-employment-statuses/:id} : delete the "id" staffCurrentEmploymentStatus.
     *
     * @param id the id of the staffCurrentEmploymentStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/staff-current-employment-statuses/{id}")
    public ResponseEntity<Void> deleteStaffCurrentEmploymentStatus(@PathVariable Long id) {
        log.debug("REST request to delete StaffCurrentEmploymentStatus : {}", id);
        staffCurrentEmploymentStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/staff-current-employment-statuses?query=:query} : search for the staffCurrentEmploymentStatus corresponding
     * to the query.
     *
     * @param query the query of the staffCurrentEmploymentStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/staff-current-employment-statuses")
    public ResponseEntity<List<StaffCurrentEmploymentStatusDTO>> searchStaffCurrentEmploymentStatuses(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of StaffCurrentEmploymentStatuses for query {}", query);
        Page<StaffCurrentEmploymentStatusDTO> page = staffCurrentEmploymentStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
