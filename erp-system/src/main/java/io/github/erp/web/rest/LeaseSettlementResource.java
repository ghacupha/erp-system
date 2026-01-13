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
import io.github.erp.repository.LeaseSettlementRepository;
import io.github.erp.service.LeaseSettlementQueryService;
import io.github.erp.service.LeaseSettlementService;
import io.github.erp.service.criteria.LeaseSettlementCriteria;
import io.github.erp.service.dto.LeaseSettlementDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link io.github.erp.domain.LeaseSettlement}.
 */
@RestController
@RequestMapping("/api")
public class LeaseSettlementResource {

    private final Logger log = LoggerFactory.getLogger(LeaseSettlementResource.class);

    private static final String ENTITY_NAME = "leaseSettlement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseSettlementService leaseSettlementService;

    private final LeaseSettlementRepository leaseSettlementRepository;

    private final LeaseSettlementQueryService leaseSettlementQueryService;

    public LeaseSettlementResource(
        LeaseSettlementService leaseSettlementService,
        LeaseSettlementRepository leaseSettlementRepository,
        LeaseSettlementQueryService leaseSettlementQueryService
    ) {
        this.leaseSettlementService = leaseSettlementService;
        this.leaseSettlementRepository = leaseSettlementRepository;
        this.leaseSettlementQueryService = leaseSettlementQueryService;
    }

    /**
     * {@code POST  /lease-settlements} : Create a new leaseSettlement.
     *
     * @param leaseSettlementDTO the leaseSettlementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseSettlementDTO, or with status {@code 400 (Bad Request)} if the leaseSettlement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-settlements")
    public ResponseEntity<LeaseSettlementDTO> createLeaseSettlement(@Valid @RequestBody LeaseSettlementDTO leaseSettlementDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaseSettlement : {}", leaseSettlementDTO);
        if (leaseSettlementDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseSettlement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseSettlementDTO result = leaseSettlementService.save(leaseSettlementDTO);
        return ResponseEntity
            .created(new URI("/api/lease-settlements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-settlements/:id} : Updates an existing leaseSettlement.
     *
     * @param id the id of the leaseSettlementDTO to save.
     * @param leaseSettlementDTO the leaseSettlementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseSettlementDTO,
     * or with status {@code 400 (Bad Request)} if the leaseSettlementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseSettlementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-settlements/{id}")
    public ResponseEntity<LeaseSettlementDTO> updateLeaseSettlement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseSettlementDTO leaseSettlementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseSettlement : {}, {}", id, leaseSettlementDTO);
        if (leaseSettlementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseSettlementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseSettlementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseSettlementDTO result = leaseSettlementService.save(leaseSettlementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseSettlementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lease-settlements/:id} : Partial updates given fields of an existing leaseSettlement, field will ignore if it is null
     *
     * @param id the id of the leaseSettlementDTO to save.
     * @param leaseSettlementDTO the leaseSettlementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseSettlementDTO,
     * or with status {@code 400 (Bad Request)} if the leaseSettlementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseSettlementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseSettlementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-settlements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseSettlementDTO> partialUpdateLeaseSettlement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseSettlementDTO leaseSettlementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseSettlement partially : {}, {}", id, leaseSettlementDTO);
        if (leaseSettlementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseSettlementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseSettlementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseSettlementDTO> result = leaseSettlementService.partialUpdate(leaseSettlementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseSettlementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-settlements} : get all the leaseSettlements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseSettlements in body.
     */
    @GetMapping("/lease-settlements")
    public ResponseEntity<List<LeaseSettlementDTO>> getAllLeaseSettlements(LeaseSettlementCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaseSettlements by criteria: {}", criteria);
        Page<LeaseSettlementDTO> page = leaseSettlementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-settlements/count} : count all the leaseSettlements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-settlements/count")
    public ResponseEntity<Long> countLeaseSettlements(LeaseSettlementCriteria criteria) {
        log.debug("REST request to count LeaseSettlements by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseSettlementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-settlements/:id} : get the "id" leaseSettlement.
     *
     * @param id the id of the leaseSettlementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseSettlementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-settlements/{id}")
    public ResponseEntity<LeaseSettlementDTO> getLeaseSettlement(@PathVariable Long id) {
        log.debug("REST request to get LeaseSettlement : {}", id);
        Optional<LeaseSettlementDTO> leaseSettlementDTO = leaseSettlementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseSettlementDTO);
    }

    /**
     * {@code DELETE  /lease-settlements/:id} : delete the "id" leaseSettlement.
     *
     * @param id the id of the leaseSettlementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-settlements/{id}")
    public ResponseEntity<Void> deleteLeaseSettlement(@PathVariable Long id) {
        log.debug("REST request to delete LeaseSettlement : {}", id);
        leaseSettlementService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/lease-settlements?query=:query} : search for the leaseSettlement corresponding
     * to the query.
     *
     * @param query the query of the leaseSettlement search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-settlements")
    public ResponseEntity<List<LeaseSettlementDTO>> searchLeaseSettlements(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeaseSettlements for query {}", query);
        Page<LeaseSettlementDTO> page = leaseSettlementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
