package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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

import io.github.erp.repository.LeasePaymentRepository;
import io.github.erp.service.LeasePaymentQueryService;
import io.github.erp.service.LeasePaymentService;
import io.github.erp.service.criteria.LeasePaymentCriteria;
import io.github.erp.service.dto.LeasePaymentDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeasePayment}.
 */
@RestController
@RequestMapping("/api")
public class LeasePaymentResource {

    private final Logger log = LoggerFactory.getLogger(LeasePaymentResource.class);

    private static final String ENTITY_NAME = "leasePayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeasePaymentService leasePaymentService;

    private final LeasePaymentRepository leasePaymentRepository;

    private final LeasePaymentQueryService leasePaymentQueryService;

    public LeasePaymentResource(
        LeasePaymentService leasePaymentService,
        LeasePaymentRepository leasePaymentRepository,
        LeasePaymentQueryService leasePaymentQueryService
    ) {
        this.leasePaymentService = leasePaymentService;
        this.leasePaymentRepository = leasePaymentRepository;
        this.leasePaymentQueryService = leasePaymentQueryService;
    }

    /**
     * {@code POST  /lease-payments} : Create a new leasePayment.
     *
     * @param leasePaymentDTO the leasePaymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leasePaymentDTO, or with status {@code 400 (Bad Request)} if the leasePayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-payments")
    public ResponseEntity<LeasePaymentDTO> createLeasePayment(@Valid @RequestBody LeasePaymentDTO leasePaymentDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeasePayment : {}", leasePaymentDTO);
        if (leasePaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new leasePayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeasePaymentDTO result = leasePaymentService.save(leasePaymentDTO);
        return ResponseEntity
            .created(new URI("/api/lease-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-payments/:id} : Updates an existing leasePayment.
     *
     * @param id the id of the leasePaymentDTO to save.
     * @param leasePaymentDTO the leasePaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leasePaymentDTO,
     * or with status {@code 400 (Bad Request)} if the leasePaymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leasePaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-payments/{id}")
    public ResponseEntity<LeasePaymentDTO> updateLeasePayment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeasePaymentDTO leasePaymentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeasePayment : {}, {}", id, leasePaymentDTO);
        if (leasePaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leasePaymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leasePaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeasePaymentDTO result = leasePaymentService.save(leasePaymentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leasePaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lease-payments/:id} : Partial updates given fields of an existing leasePayment, field will ignore if it is null
     *
     * @param id the id of the leasePaymentDTO to save.
     * @param leasePaymentDTO the leasePaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leasePaymentDTO,
     * or with status {@code 400 (Bad Request)} if the leasePaymentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leasePaymentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leasePaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeasePaymentDTO> partialUpdateLeasePayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeasePaymentDTO leasePaymentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeasePayment partially : {}, {}", id, leasePaymentDTO);
        if (leasePaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leasePaymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leasePaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeasePaymentDTO> result = leasePaymentService.partialUpdate(leasePaymentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leasePaymentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-payments} : get all the leasePayments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leasePayments in body.
     */
    @GetMapping("/lease-payments")
    public ResponseEntity<List<LeasePaymentDTO>> getAllLeasePayments(LeasePaymentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeasePayments by criteria: {}", criteria);
        Page<LeasePaymentDTO> page = leasePaymentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-payments/count} : count all the leasePayments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-payments/count")
    public ResponseEntity<Long> countLeasePayments(LeasePaymentCriteria criteria) {
        log.debug("REST request to count LeasePayments by criteria: {}", criteria);
        return ResponseEntity.ok().body(leasePaymentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-payments/:id} : get the "id" leasePayment.
     *
     * @param id the id of the leasePaymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leasePaymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-payments/{id}")
    public ResponseEntity<LeasePaymentDTO> getLeasePayment(@PathVariable Long id) {
        log.debug("REST request to get LeasePayment : {}", id);
        Optional<LeasePaymentDTO> leasePaymentDTO = leasePaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leasePaymentDTO);
    }

    /**
     * {@code DELETE  /lease-payments/:id} : delete the "id" leasePayment.
     *
     * @param id the id of the leasePaymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-payments/{id}")
    public ResponseEntity<Void> deleteLeasePayment(@PathVariable Long id) {
        log.debug("REST request to delete LeasePayment : {}", id);
        leasePaymentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-payments?query=:query} : search for the leasePayment corresponding
     * to the query.
     *
     * @param query the query of the leasePayment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-payments")
    public ResponseEntity<List<LeasePaymentDTO>> searchLeasePayments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeasePayments for query {}", query);
        Page<LeasePaymentDTO> page = leasePaymentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
