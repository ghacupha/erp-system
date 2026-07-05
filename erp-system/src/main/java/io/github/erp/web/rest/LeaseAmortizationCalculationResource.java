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

import io.github.erp.repository.LeaseAmortizationCalculationRepository;
import io.github.erp.service.LeaseAmortizationCalculationQueryService;
import io.github.erp.service.LeaseAmortizationCalculationService;
import io.github.erp.service.criteria.LeaseAmortizationCalculationCriteria;
import io.github.erp.service.dto.LeaseAmortizationCalculationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseAmortizationCalculation}.
 */
@RestController
@RequestMapping("/api")
public class LeaseAmortizationCalculationResource {

    private final Logger log = LoggerFactory.getLogger(LeaseAmortizationCalculationResource.class);

    private static final String ENTITY_NAME = "leaseAmortizationCalculation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseAmortizationCalculationService leaseAmortizationCalculationService;

    private final LeaseAmortizationCalculationRepository leaseAmortizationCalculationRepository;

    private final LeaseAmortizationCalculationQueryService leaseAmortizationCalculationQueryService;

    public LeaseAmortizationCalculationResource(
        LeaseAmortizationCalculationService leaseAmortizationCalculationService,
        LeaseAmortizationCalculationRepository leaseAmortizationCalculationRepository,
        LeaseAmortizationCalculationQueryService leaseAmortizationCalculationQueryService
    ) {
        this.leaseAmortizationCalculationService = leaseAmortizationCalculationService;
        this.leaseAmortizationCalculationRepository = leaseAmortizationCalculationRepository;
        this.leaseAmortizationCalculationQueryService = leaseAmortizationCalculationQueryService;
    }

    /**
     * {@code POST  /lease-amortization-calculations} : Create a new leaseAmortizationCalculation.
     *
     * @param leaseAmortizationCalculationDTO the leaseAmortizationCalculationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseAmortizationCalculationDTO, or with status {@code 400 (Bad Request)} if the leaseAmortizationCalculation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-amortization-calculations")
    public ResponseEntity<LeaseAmortizationCalculationDTO> createLeaseAmortizationCalculation(
        @Valid @RequestBody LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LeaseAmortizationCalculation : {}", leaseAmortizationCalculationDTO);
        if (leaseAmortizationCalculationDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseAmortizationCalculation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseAmortizationCalculationDTO result = leaseAmortizationCalculationService.save(leaseAmortizationCalculationDTO);
        return ResponseEntity
            .created(new URI("/api/lease-amortization-calculations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-amortization-calculations/:id} : Updates an existing leaseAmortizationCalculation.
     *
     * @param id the id of the leaseAmortizationCalculationDTO to save.
     * @param leaseAmortizationCalculationDTO the leaseAmortizationCalculationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseAmortizationCalculationDTO,
     * or with status {@code 400 (Bad Request)} if the leaseAmortizationCalculationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseAmortizationCalculationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-amortization-calculations/{id}")
    public ResponseEntity<LeaseAmortizationCalculationDTO> updateLeaseAmortizationCalculation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseAmortizationCalculation : {}, {}", id, leaseAmortizationCalculationDTO);
        if (leaseAmortizationCalculationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseAmortizationCalculationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseAmortizationCalculationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseAmortizationCalculationDTO result = leaseAmortizationCalculationService.save(leaseAmortizationCalculationDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseAmortizationCalculationDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /lease-amortization-calculations/:id} : Partial updates given fields of an existing leaseAmortizationCalculation, field will ignore if it is null
     *
     * @param id the id of the leaseAmortizationCalculationDTO to save.
     * @param leaseAmortizationCalculationDTO the leaseAmortizationCalculationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseAmortizationCalculationDTO,
     * or with status {@code 400 (Bad Request)} if the leaseAmortizationCalculationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseAmortizationCalculationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseAmortizationCalculationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-amortization-calculations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseAmortizationCalculationDTO> partialUpdateLeaseAmortizationCalculation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseAmortizationCalculationDTO leaseAmortizationCalculationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseAmortizationCalculation partially : {}, {}", id, leaseAmortizationCalculationDTO);
        if (leaseAmortizationCalculationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseAmortizationCalculationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseAmortizationCalculationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseAmortizationCalculationDTO> result = leaseAmortizationCalculationService.partialUpdate(
            leaseAmortizationCalculationDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseAmortizationCalculationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-amortization-calculations} : get all the leaseAmortizationCalculations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseAmortizationCalculations in body.
     */
    @GetMapping("/lease-amortization-calculations")
    public ResponseEntity<List<LeaseAmortizationCalculationDTO>> getAllLeaseAmortizationCalculations(
        LeaseAmortizationCalculationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseAmortizationCalculations by criteria: {}", criteria);
        Page<LeaseAmortizationCalculationDTO> page = leaseAmortizationCalculationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-amortization-calculations/count} : count all the leaseAmortizationCalculations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-amortization-calculations/count")
    public ResponseEntity<Long> countLeaseAmortizationCalculations(LeaseAmortizationCalculationCriteria criteria) {
        log.debug("REST request to count LeaseAmortizationCalculations by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseAmortizationCalculationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-amortization-calculations/:id} : get the "id" leaseAmortizationCalculation.
     *
     * @param id the id of the leaseAmortizationCalculationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseAmortizationCalculationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-amortization-calculations/{id}")
    public ResponseEntity<LeaseAmortizationCalculationDTO> getLeaseAmortizationCalculation(@PathVariable Long id) {
        log.debug("REST request to get LeaseAmortizationCalculation : {}", id);
        Optional<LeaseAmortizationCalculationDTO> leaseAmortizationCalculationDTO = leaseAmortizationCalculationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseAmortizationCalculationDTO);
    }

    /**
     * {@code DELETE  /lease-amortization-calculations/:id} : delete the "id" leaseAmortizationCalculation.
     *
     * @param id the id of the leaseAmortizationCalculationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-amortization-calculations/{id}")
    public ResponseEntity<Void> deleteLeaseAmortizationCalculation(@PathVariable Long id) {
        log.debug("REST request to delete LeaseAmortizationCalculation : {}", id);
        leaseAmortizationCalculationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-amortization-calculations?query=:query} : search for the leaseAmortizationCalculation corresponding
     * to the query.
     *
     * @param query the query of the leaseAmortizationCalculation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-amortization-calculations")
    public ResponseEntity<List<LeaseAmortizationCalculationDTO>> searchLeaseAmortizationCalculations(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseAmortizationCalculations for query {}", query);
        Page<LeaseAmortizationCalculationDTO> page = leaseAmortizationCalculationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
