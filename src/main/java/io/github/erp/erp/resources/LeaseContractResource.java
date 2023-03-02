package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 11 (Caleb Series) Server ver 0.7.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.repository.LeaseContractRepository;
import io.github.erp.service.LeaseContractQueryService;
import io.github.erp.service.LeaseContractService;
import io.github.erp.service.criteria.LeaseContractCriteria;
import io.github.erp.service.dto.LeaseContractDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseContract}.
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseContractResource {

    private final Logger log = LoggerFactory.getLogger(LeaseContractResource.class);

    private static final String ENTITY_NAME = "leaseContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseContractService leaseContractService;

    private final LeaseContractRepository leaseContractRepository;

    private final LeaseContractQueryService leaseContractQueryService;

    public LeaseContractResource(
        LeaseContractService leaseContractService,
        LeaseContractRepository leaseContractRepository,
        LeaseContractQueryService leaseContractQueryService
    ) {
        this.leaseContractService = leaseContractService;
        this.leaseContractRepository = leaseContractRepository;
        this.leaseContractQueryService = leaseContractQueryService;
    }

    /**
     * {@code POST  /lease-contracts} : Create a new leaseContract.
     *
     * @param leaseContractDTO the leaseContractDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseContractDTO, or with status {@code 400 (Bad Request)} if the leaseContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-contracts")
    public ResponseEntity<LeaseContractDTO> createLeaseContract(@Valid @RequestBody LeaseContractDTO leaseContractDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaseContract : {}", leaseContractDTO);
        if (leaseContractDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseContractDTO result = leaseContractService.save(leaseContractDTO);
        return ResponseEntity
            .created(new URI("/api/lease-contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-contracts/:id} : Updates an existing leaseContract.
     *
     * @param id the id of the leaseContractDTO to save.
     * @param leaseContractDTO the leaseContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseContractDTO,
     * or with status {@code 400 (Bad Request)} if the leaseContractDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-contracts/{id}")
    public ResponseEntity<LeaseContractDTO> updateLeaseContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseContractDTO leaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseContract : {}, {}", id, leaseContractDTO);
        if (leaseContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseContractDTO result = leaseContractService.save(leaseContractDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leaseContractDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lease-contracts/:id} : Partial updates given fields of an existing leaseContract, field will ignore if it is null
     *
     * @param id the id of the leaseContractDTO to save.
     * @param leaseContractDTO the leaseContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseContractDTO,
     * or with status {@code 400 (Bad Request)} if the leaseContractDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseContractDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-contracts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseContractDTO> partialUpdateLeaseContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseContractDTO leaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseContract partially : {}, {}", id, leaseContractDTO);
        if (leaseContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseContractDTO> result = leaseContractService.partialUpdate(leaseContractDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leaseContractDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-contracts} : get all the leaseContracts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseContracts in body.
     */
    @GetMapping("/lease-contracts")
    public ResponseEntity<List<LeaseContractDTO>> getAllLeaseContracts(LeaseContractCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaseContracts by criteria: {}", criteria);
        Page<LeaseContractDTO> page = leaseContractQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-contracts/count} : count all the leaseContracts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-contracts/count")
    public ResponseEntity<Long> countLeaseContracts(LeaseContractCriteria criteria) {
        log.debug("REST request to count LeaseContracts by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseContractQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-contracts/:id} : get the "id" leaseContract.
     *
     * @param id the id of the leaseContractDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseContractDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-contracts/{id}")
    public ResponseEntity<LeaseContractDTO> getLeaseContract(@PathVariable Long id) {
        log.debug("REST request to get LeaseContract : {}", id);
        Optional<LeaseContractDTO> leaseContractDTO = leaseContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseContractDTO);
    }

    /**
     * {@code DELETE  /lease-contracts/:id} : delete the "id" leaseContract.
     *
     * @param id the id of the leaseContractDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-contracts/{id}")
    public ResponseEntity<Void> deleteLeaseContract(@PathVariable Long id) {
        log.debug("REST request to delete LeaseContract : {}", id);
        leaseContractService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-contracts?query=:query} : search for the leaseContract corresponding
     * to the query.
     *
     * @param query the query of the leaseContract search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-contracts")
    public ResponseEntity<List<LeaseContractDTO>> searchLeaseContracts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeaseContracts for query {}", query);
        Page<LeaseContractDTO> page = leaseContractService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
