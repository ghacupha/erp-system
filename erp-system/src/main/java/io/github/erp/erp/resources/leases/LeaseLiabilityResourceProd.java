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
import io.github.erp.internal.service.leases.InternalLeaseLiabilityService;
import io.github.erp.repository.LeaseLiabilityRepository;
import io.github.erp.service.LeaseLiabilityQueryService;
import io.github.erp.service.LeaseLiabilityService;
import io.github.erp.service.criteria.LeaseLiabilityCriteria;
import io.github.erp.service.dto.LeaseLiabilityDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiability}.
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseLiabilityResourceProd {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityResourceProd.class);

    private static final String ENTITY_NAME = "leaseLiability";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalLeaseLiabilityService leaseLiabilityService;

    private final LeaseLiabilityRepository leaseLiabilityRepository;

    private final LeaseLiabilityQueryService leaseLiabilityQueryService;

    public LeaseLiabilityResourceProd(
        InternalLeaseLiabilityService leaseLiabilityService,
        LeaseLiabilityRepository leaseLiabilityRepository,
        LeaseLiabilityQueryService leaseLiabilityQueryService
    ) {
        this.leaseLiabilityService = leaseLiabilityService;
        this.leaseLiabilityRepository = leaseLiabilityRepository;
        this.leaseLiabilityQueryService = leaseLiabilityQueryService;
    }

    /**
     * {@code POST  /lease-liabilities} : Create a new leaseLiability.
     *
     * @param leaseLiabilityDTO the leaseLiabilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseLiabilityDTO, or with status {@code 400 (Bad Request)} if the leaseLiability has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-liabilities")
    public ResponseEntity<LeaseLiabilityDTO> createLeaseLiability(@Valid @RequestBody LeaseLiabilityDTO leaseLiabilityDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaseLiability : {}", leaseLiabilityDTO);
        if (leaseLiabilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseLiability cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseLiabilityDTO result = leaseLiabilityService.save(leaseLiabilityDTO);
        return ResponseEntity
            .created(new URI("/api/leases/lease-liabilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-liabilities/:id} : Updates an existing leaseLiability.
     *
     * @param id the id of the leaseLiabilityDTO to save.
     * @param leaseLiabilityDTO the leaseLiabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-liabilities/{id}")
    public ResponseEntity<LeaseLiabilityDTO> updateLeaseLiability(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseLiabilityDTO leaseLiabilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseLiability : {}, {}", id, leaseLiabilityDTO);
        if (leaseLiabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseLiabilityDTO result = leaseLiabilityService.update(leaseLiabilityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lease-liabilities/:id} : Partial updates given fields of an existing leaseLiability, field will ignore if it is null
     *
     * @param id the id of the leaseLiabilityDTO to save.
     * @param leaseLiabilityDTO the leaseLiabilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseLiabilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-liabilities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseLiabilityDTO> partialUpdateLeaseLiability(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseLiabilityDTO leaseLiabilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseLiability partially : {}, {}", id, leaseLiabilityDTO);
        if (leaseLiabilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseLiabilityDTO> result = leaseLiabilityService.partialUpdate(leaseLiabilityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-liabilities} : get all the leaseLiabilities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilities in body.
     */
    @GetMapping("/lease-liabilities")
    public ResponseEntity<List<LeaseLiabilityDTO>> getAllLeaseLiabilities(LeaseLiabilityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaseLiabilities by criteria: {}", criteria);
        Page<LeaseLiabilityDTO> page = leaseLiabilityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liabilities/count} : count all the leaseLiabilities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liabilities/count")
    public ResponseEntity<Long> countLeaseLiabilities(LeaseLiabilityCriteria criteria) {
        log.debug("REST request to count LeaseLiabilities by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liabilities/:id} : get the "id" leaseLiability.
     *
     * @param id the id of the leaseLiabilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liabilities/{id}")
    public ResponseEntity<LeaseLiabilityDTO> getLeaseLiability(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiability : {}", id);
        Optional<LeaseLiabilityDTO> leaseLiabilityDTO = leaseLiabilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseLiabilityDTO);
    }

    /**
     * {@code DELETE  /lease-liabilities/:id} : delete the "id" leaseLiability.
     *
     * @param id the id of the leaseLiabilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-liabilities/{id}")
    public ResponseEntity<Void> deleteLeaseLiability(@PathVariable Long id) {
        log.debug("REST request to delete LeaseLiability : {}", id);
        leaseLiabilityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-liabilities?query=:query} : search for the leaseLiability corresponding
     * to the query.
     *
     * @param query the query of the leaseLiability search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liabilities")
    public ResponseEntity<List<LeaseLiabilityDTO>> searchLeaseLiabilities(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeaseLiabilities for query {}", query);
        Page<LeaseLiabilityDTO> page = leaseLiabilityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
