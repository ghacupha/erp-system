package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.LeaseAmortizationScheduleRepository;
import io.github.erp.service.LeaseAmortizationScheduleQueryService;
import io.github.erp.service.LeaseAmortizationScheduleService;
import io.github.erp.service.criteria.LeaseAmortizationScheduleCriteria;
import io.github.erp.service.dto.LeaseAmortizationScheduleDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseAmortizationSchedule}.
 */
@RestController
@RequestMapping("/api")
public class LeaseAmortizationScheduleResource {

    private final Logger log = LoggerFactory.getLogger(LeaseAmortizationScheduleResource.class);

    private static final String ENTITY_NAME = "leaseAmortizationSchedule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseAmortizationScheduleService leaseAmortizationScheduleService;

    private final LeaseAmortizationScheduleRepository leaseAmortizationScheduleRepository;

    private final LeaseAmortizationScheduleQueryService leaseAmortizationScheduleQueryService;

    public LeaseAmortizationScheduleResource(
        LeaseAmortizationScheduleService leaseAmortizationScheduleService,
        LeaseAmortizationScheduleRepository leaseAmortizationScheduleRepository,
        LeaseAmortizationScheduleQueryService leaseAmortizationScheduleQueryService
    ) {
        this.leaseAmortizationScheduleService = leaseAmortizationScheduleService;
        this.leaseAmortizationScheduleRepository = leaseAmortizationScheduleRepository;
        this.leaseAmortizationScheduleQueryService = leaseAmortizationScheduleQueryService;
    }

    /**
     * {@code POST  /lease-amortization-schedules} : Create a new leaseAmortizationSchedule.
     *
     * @param leaseAmortizationScheduleDTO the leaseAmortizationScheduleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseAmortizationScheduleDTO, or with status {@code 400 (Bad Request)} if the leaseAmortizationSchedule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-amortization-schedules")
    public ResponseEntity<LeaseAmortizationScheduleDTO> createLeaseAmortizationSchedule(
        @Valid @RequestBody LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LeaseAmortizationSchedule : {}", leaseAmortizationScheduleDTO);
        if (leaseAmortizationScheduleDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseAmortizationSchedule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseAmortizationScheduleDTO result = leaseAmortizationScheduleService.save(leaseAmortizationScheduleDTO);
        return ResponseEntity
            .created(new URI("/api/lease-amortization-schedules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-amortization-schedules/:id} : Updates an existing leaseAmortizationSchedule.
     *
     * @param id the id of the leaseAmortizationScheduleDTO to save.
     * @param leaseAmortizationScheduleDTO the leaseAmortizationScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseAmortizationScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the leaseAmortizationScheduleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseAmortizationScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-amortization-schedules/{id}")
    public ResponseEntity<LeaseAmortizationScheduleDTO> updateLeaseAmortizationSchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseAmortizationSchedule : {}, {}", id, leaseAmortizationScheduleDTO);
        if (leaseAmortizationScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseAmortizationScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseAmortizationScheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseAmortizationScheduleDTO result = leaseAmortizationScheduleService.save(leaseAmortizationScheduleDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseAmortizationScheduleDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /lease-amortization-schedules/:id} : Partial updates given fields of an existing leaseAmortizationSchedule, field will ignore if it is null
     *
     * @param id the id of the leaseAmortizationScheduleDTO to save.
     * @param leaseAmortizationScheduleDTO the leaseAmortizationScheduleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseAmortizationScheduleDTO,
     * or with status {@code 400 (Bad Request)} if the leaseAmortizationScheduleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseAmortizationScheduleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseAmortizationScheduleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-amortization-schedules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseAmortizationScheduleDTO> partialUpdateLeaseAmortizationSchedule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseAmortizationScheduleDTO leaseAmortizationScheduleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseAmortizationSchedule partially : {}, {}", id, leaseAmortizationScheduleDTO);
        if (leaseAmortizationScheduleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseAmortizationScheduleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseAmortizationScheduleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseAmortizationScheduleDTO> result = leaseAmortizationScheduleService.partialUpdate(leaseAmortizationScheduleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseAmortizationScheduleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-amortization-schedules} : get all the leaseAmortizationSchedules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseAmortizationSchedules in body.
     */
    @GetMapping("/lease-amortization-schedules")
    public ResponseEntity<List<LeaseAmortizationScheduleDTO>> getAllLeaseAmortizationSchedules(
        LeaseAmortizationScheduleCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseAmortizationSchedules by criteria: {}", criteria);
        Page<LeaseAmortizationScheduleDTO> page = leaseAmortizationScheduleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-amortization-schedules/count} : count all the leaseAmortizationSchedules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-amortization-schedules/count")
    public ResponseEntity<Long> countLeaseAmortizationSchedules(LeaseAmortizationScheduleCriteria criteria) {
        log.debug("REST request to count LeaseAmortizationSchedules by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseAmortizationScheduleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-amortization-schedules/:id} : get the "id" leaseAmortizationSchedule.
     *
     * @param id the id of the leaseAmortizationScheduleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseAmortizationScheduleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-amortization-schedules/{id}")
    public ResponseEntity<LeaseAmortizationScheduleDTO> getLeaseAmortizationSchedule(@PathVariable Long id) {
        log.debug("REST request to get LeaseAmortizationSchedule : {}", id);
        Optional<LeaseAmortizationScheduleDTO> leaseAmortizationScheduleDTO = leaseAmortizationScheduleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseAmortizationScheduleDTO);
    }

    /**
     * {@code DELETE  /lease-amortization-schedules/:id} : delete the "id" leaseAmortizationSchedule.
     *
     * @param id the id of the leaseAmortizationScheduleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-amortization-schedules/{id}")
    public ResponseEntity<Void> deleteLeaseAmortizationSchedule(@PathVariable Long id) {
        log.debug("REST request to delete LeaseAmortizationSchedule : {}", id);
        leaseAmortizationScheduleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-amortization-schedules?query=:query} : search for the leaseAmortizationSchedule corresponding
     * to the query.
     *
     * @param query the query of the leaseAmortizationSchedule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-amortization-schedules")
    public ResponseEntity<List<LeaseAmortizationScheduleDTO>> searchLeaseAmortizationSchedules(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseAmortizationSchedules for query {}", query);
        Page<LeaseAmortizationScheduleDTO> page = leaseAmortizationScheduleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
