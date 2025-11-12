package io.github.erp.erp.resources.leases;

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
import io.github.erp.repository.LeaseRepaymentPeriodRepository;
import io.github.erp.service.LeaseRepaymentPeriodQueryService;
import io.github.erp.service.LeaseRepaymentPeriodService;
import io.github.erp.service.criteria.LeaseRepaymentPeriodCriteria;
import io.github.erp.service.dto.LeaseRepaymentPeriodDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseRepaymentPeriod}.
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseRepaymentPeriodResourceProd {

    private final Logger log = LoggerFactory.getLogger(LeaseRepaymentPeriodResourceProd.class);

    private static final String ENTITY_NAME = "leaseRepaymentPeriod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseRepaymentPeriodService leaseRepaymentPeriodService;

    private final LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository;

    private final LeaseRepaymentPeriodQueryService leaseRepaymentPeriodQueryService;

    public LeaseRepaymentPeriodResourceProd(
        LeaseRepaymentPeriodService leaseRepaymentPeriodService,
        LeaseRepaymentPeriodRepository leaseRepaymentPeriodRepository,
        LeaseRepaymentPeriodQueryService leaseRepaymentPeriodQueryService
    ) {
        this.leaseRepaymentPeriodService = leaseRepaymentPeriodService;
        this.leaseRepaymentPeriodRepository = leaseRepaymentPeriodRepository;
        this.leaseRepaymentPeriodQueryService = leaseRepaymentPeriodQueryService;
    }

    /**
     * {@code POST  /lease-repayment-periods} : Create a new leaseRepaymentPeriod.
     *
     * @param leaseRepaymentPeriodDTO the leaseRepaymentPeriodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseRepaymentPeriodDTO, or with status {@code 400 (Bad Request)} if the leaseRepaymentPeriod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-repayment-periods")
    public ResponseEntity<LeaseRepaymentPeriodDTO> createLeaseRepaymentPeriod(
        @Valid @RequestBody LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LeaseRepaymentPeriod : {}", leaseRepaymentPeriodDTO);
        if (leaseRepaymentPeriodDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseRepaymentPeriod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseRepaymentPeriodDTO result = leaseRepaymentPeriodService.save(leaseRepaymentPeriodDTO);
        return ResponseEntity
            .created(new URI("/api/lease-repayment-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-repayment-periods/:id} : Updates an existing leaseRepaymentPeriod.
     *
     * @param id the id of the leaseRepaymentPeriodDTO to save.
     * @param leaseRepaymentPeriodDTO the leaseRepaymentPeriodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseRepaymentPeriodDTO,
     * or with status {@code 400 (Bad Request)} if the leaseRepaymentPeriodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseRepaymentPeriodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-repayment-periods/{id}")
    public ResponseEntity<LeaseRepaymentPeriodDTO> updateLeaseRepaymentPeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseRepaymentPeriod : {}, {}", id, leaseRepaymentPeriodDTO);
        if (leaseRepaymentPeriodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseRepaymentPeriodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseRepaymentPeriodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseRepaymentPeriodDTO result = leaseRepaymentPeriodService.save(leaseRepaymentPeriodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseRepaymentPeriodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lease-repayment-periods/:id} : Partial updates given fields of an existing leaseRepaymentPeriod, field will ignore if it is null
     *
     * @param id the id of the leaseRepaymentPeriodDTO to save.
     * @param leaseRepaymentPeriodDTO the leaseRepaymentPeriodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseRepaymentPeriodDTO,
     * or with status {@code 400 (Bad Request)} if the leaseRepaymentPeriodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseRepaymentPeriodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseRepaymentPeriodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-repayment-periods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseRepaymentPeriodDTO> partialUpdateLeaseRepaymentPeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseRepaymentPeriodDTO leaseRepaymentPeriodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseRepaymentPeriod partially : {}, {}", id, leaseRepaymentPeriodDTO);
        if (leaseRepaymentPeriodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseRepaymentPeriodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseRepaymentPeriodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseRepaymentPeriodDTO> result = leaseRepaymentPeriodService.partialUpdate(leaseRepaymentPeriodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseRepaymentPeriodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-repayment-periods} : get all the leaseRepaymentPeriods.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseRepaymentPeriods in body.
     */
    @GetMapping("/lease-repayment-periods")
    public ResponseEntity<List<LeaseRepaymentPeriodDTO>> getAllLeaseRepaymentPeriods(
        LeaseRepaymentPeriodCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseRepaymentPeriods by criteria: {}", criteria);
        Page<LeaseRepaymentPeriodDTO> page = leaseRepaymentPeriodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-repayment-periods/count} : count all the leaseRepaymentPeriods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-repayment-periods/count")
    public ResponseEntity<Long> countLeaseRepaymentPeriods(LeaseRepaymentPeriodCriteria criteria) {
        log.debug("REST request to count LeaseRepaymentPeriods by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseRepaymentPeriodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-repayment-periods/:id} : get the "id" leaseRepaymentPeriod.
     *
     * @param id the id of the leaseRepaymentPeriodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseRepaymentPeriodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-repayment-periods/{id}")
    public ResponseEntity<LeaseRepaymentPeriodDTO> getLeaseRepaymentPeriod(@PathVariable Long id) {
        log.debug("REST request to get LeaseRepaymentPeriod : {}", id);
        Optional<LeaseRepaymentPeriodDTO> leaseRepaymentPeriodDTO = leaseRepaymentPeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseRepaymentPeriodDTO);
    }

    /**
     * {@code DELETE  /lease-repayment-periods/:id} : delete the "id" leaseRepaymentPeriod.
     *
     * @param id the id of the leaseRepaymentPeriodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-repayment-periods/{id}")
    public ResponseEntity<Void> deleteLeaseRepaymentPeriod(@PathVariable Long id) {
        log.debug("REST request to delete LeaseRepaymentPeriod : {}", id);
        leaseRepaymentPeriodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-repayment-periods?query=:query} : search for the leaseRepaymentPeriod corresponding
     * to the query.
     *
     * @param query the query of the leaseRepaymentPeriod search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-repayment-periods")
    public ResponseEntity<List<LeaseRepaymentPeriodDTO>> searchLeaseRepaymentPeriods(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeaseRepaymentPeriods for query {}", query);
        Page<LeaseRepaymentPeriodDTO> page = leaseRepaymentPeriodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
