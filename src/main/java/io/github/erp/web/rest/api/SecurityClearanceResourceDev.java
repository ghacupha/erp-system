package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0-SNAPSHOT
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
import io.github.erp.repository.SecurityClearanceRepository;
import io.github.erp.service.SecurityClearanceQueryService;
import io.github.erp.service.SecurityClearanceService;
import io.github.erp.service.criteria.SecurityClearanceCriteria;
import io.github.erp.service.dto.SecurityClearanceDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SecurityClearance}.
 */
@RestController
@RequestMapping("/api/dev")
public class SecurityClearanceResourceDev {

    private final Logger log = LoggerFactory.getLogger(SecurityClearanceResourceDev.class);

    private static final String ENTITY_NAME = "securityClearance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SecurityClearanceService securityClearanceService;

    private final SecurityClearanceRepository securityClearanceRepository;

    private final SecurityClearanceQueryService securityClearanceQueryService;

    public SecurityClearanceResourceDev(
        SecurityClearanceService securityClearanceService,
        SecurityClearanceRepository securityClearanceRepository,
        SecurityClearanceQueryService securityClearanceQueryService
    ) {
        this.securityClearanceService = securityClearanceService;
        this.securityClearanceRepository = securityClearanceRepository;
        this.securityClearanceQueryService = securityClearanceQueryService;
    }

    /**
     * {@code POST  /security-clearances} : Create a new securityClearance.
     *
     * @param securityClearanceDTO the securityClearanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new securityClearanceDTO, or with status {@code 400 (Bad Request)} if the securityClearance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/security-clearances")
    public ResponseEntity<SecurityClearanceDTO> createSecurityClearance(@Valid @RequestBody SecurityClearanceDTO securityClearanceDTO)
        throws URISyntaxException {
        log.debug("REST request to save SecurityClearance : {}", securityClearanceDTO);
        if (securityClearanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new securityClearance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SecurityClearanceDTO result = securityClearanceService.save(securityClearanceDTO);
        return ResponseEntity
            .created(new URI("/api/security-clearances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /security-clearances/:id} : Updates an existing securityClearance.
     *
     * @param id the id of the securityClearanceDTO to save.
     * @param securityClearanceDTO the securityClearanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityClearanceDTO,
     * or with status {@code 400 (Bad Request)} if the securityClearanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the securityClearanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/security-clearances/{id}")
    public ResponseEntity<SecurityClearanceDTO> updateSecurityClearance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SecurityClearanceDTO securityClearanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SecurityClearance : {}, {}", id, securityClearanceDTO);
        if (securityClearanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityClearanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityClearanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SecurityClearanceDTO result = securityClearanceService.save(securityClearanceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, securityClearanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /security-clearances/:id} : Partial updates given fields of an existing securityClearance, field will ignore if it is null
     *
     * @param id the id of the securityClearanceDTO to save.
     * @param securityClearanceDTO the securityClearanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityClearanceDTO,
     * or with status {@code 400 (Bad Request)} if the securityClearanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the securityClearanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the securityClearanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/security-clearances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SecurityClearanceDTO> partialUpdateSecurityClearance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SecurityClearanceDTO securityClearanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SecurityClearance partially : {}, {}", id, securityClearanceDTO);
        if (securityClearanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityClearanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityClearanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SecurityClearanceDTO> result = securityClearanceService.partialUpdate(securityClearanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, securityClearanceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /security-clearances} : get all the securityClearances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of securityClearances in body.
     */
    @GetMapping("/security-clearances")
    public ResponseEntity<List<SecurityClearanceDTO>> getAllSecurityClearances(SecurityClearanceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SecurityClearances by criteria: {}", criteria);
        Page<SecurityClearanceDTO> page = securityClearanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /security-clearances/count} : count all the securityClearances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/security-clearances/count")
    public ResponseEntity<Long> countSecurityClearances(SecurityClearanceCriteria criteria) {
        log.debug("REST request to count SecurityClearances by criteria: {}", criteria);
        return ResponseEntity.ok().body(securityClearanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /security-clearances/:id} : get the "id" securityClearance.
     *
     * @param id the id of the securityClearanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the securityClearanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/security-clearances/{id}")
    public ResponseEntity<SecurityClearanceDTO> getSecurityClearance(@PathVariable Long id) {
        log.debug("REST request to get SecurityClearance : {}", id);
        Optional<SecurityClearanceDTO> securityClearanceDTO = securityClearanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(securityClearanceDTO);
    }

    /**
     * {@code DELETE  /security-clearances/:id} : delete the "id" securityClearance.
     *
     * @param id the id of the securityClearanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/security-clearances/{id}")
    public ResponseEntity<Void> deleteSecurityClearance(@PathVariable Long id) {
        log.debug("REST request to delete SecurityClearance : {}", id);
        securityClearanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/security-clearances?query=:query} : search for the securityClearance corresponding
     * to the query.
     *
     * @param query the query of the securityClearance search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/security-clearances")
    public ResponseEntity<List<SecurityClearanceDTO>> searchSecurityClearances(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SecurityClearances for query {}", query);
        Page<SecurityClearanceDTO> page = securityClearanceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
