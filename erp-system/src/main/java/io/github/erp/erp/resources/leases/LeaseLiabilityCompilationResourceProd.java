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
import io.github.erp.internal.service.leases.InternalLeaseLiabilityCompilationService;
import io.github.erp.repository.LeaseLiabilityCompilationRepository;
import io.github.erp.service.LeaseLiabilityCompilationQueryService;
import io.github.erp.service.LeaseLiabilityCompilationService;
import io.github.erp.service.criteria.LeaseLiabilityCompilationCriteria;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiabilityCompilation}.
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseLiabilityCompilationResourceProd {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityCompilationResourceProd.class);

    private static final String ENTITY_NAME = "leaseLiabilityCompilation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalLeaseLiabilityCompilationService leaseLiabilityCompilationService;

    private final LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository;

    private final LeaseLiabilityCompilationQueryService leaseLiabilityCompilationQueryService;

    public LeaseLiabilityCompilationResourceProd(
        InternalLeaseLiabilityCompilationService leaseLiabilityCompilationService,
        LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository,
        LeaseLiabilityCompilationQueryService leaseLiabilityCompilationQueryService
    ) {
        this.leaseLiabilityCompilationService = leaseLiabilityCompilationService;
        this.leaseLiabilityCompilationRepository = leaseLiabilityCompilationRepository;
        this.leaseLiabilityCompilationQueryService = leaseLiabilityCompilationQueryService;
    }

    /**
     * {@code POST  /lease-liability-compilations} : Create a new leaseLiabilityCompilation.
     *
     * @param leaseLiabilityCompilationDTO the leaseLiabilityCompilationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseLiabilityCompilationDTO, or with status {@code 400 (Bad Request)} if the leaseLiabilityCompilation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-liability-compilations")
    public ResponseEntity<LeaseLiabilityCompilationDTO> createLeaseLiabilityCompilation(
        @Valid @RequestBody LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LeaseLiabilityCompilation : {}", leaseLiabilityCompilationDTO);
        if (leaseLiabilityCompilationDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseLiabilityCompilation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseLiabilityCompilationDTO result = leaseLiabilityCompilationService.save(leaseLiabilityCompilationDTO);
        return ResponseEntity
            .created(new URI("/api/lease-liability-compilations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-liability-compilations/:id} : Updates an existing leaseLiabilityCompilation.
     *
     * @param id the id of the leaseLiabilityCompilationDTO to save.
     * @param leaseLiabilityCompilationDTO the leaseLiabilityCompilationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityCompilationDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityCompilationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityCompilationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-liability-compilations/{id}")
    public ResponseEntity<LeaseLiabilityCompilationDTO> updateLeaseLiabilityCompilation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseLiabilityCompilation : {}, {}", id, leaseLiabilityCompilationDTO);
        if (leaseLiabilityCompilationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityCompilationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityCompilationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseLiabilityCompilationDTO result = leaseLiabilityCompilationService.save(leaseLiabilityCompilationDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityCompilationDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /lease-liability-compilations/:id} : Partial updates given fields of an existing leaseLiabilityCompilation, field will ignore if it is null
     *
     * @param id the id of the leaseLiabilityCompilationDTO to save.
     * @param leaseLiabilityCompilationDTO the leaseLiabilityCompilationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityCompilationDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityCompilationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseLiabilityCompilationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityCompilationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-liability-compilations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseLiabilityCompilationDTO> partialUpdateLeaseLiabilityCompilation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseLiabilityCompilationDTO leaseLiabilityCompilationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseLiabilityCompilation partially : {}, {}", id, leaseLiabilityCompilationDTO);
        if (leaseLiabilityCompilationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityCompilationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityCompilationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseLiabilityCompilationDTO> result = leaseLiabilityCompilationService.partialUpdate(leaseLiabilityCompilationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityCompilationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-liability-compilations} : get all the leaseLiabilityCompilations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilityCompilations in body.
     */
    @GetMapping("/lease-liability-compilations")
    public ResponseEntity<List<LeaseLiabilityCompilationDTO>> getAllLeaseLiabilityCompilations(
        LeaseLiabilityCompilationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityCompilations by criteria: {}", criteria);
        Page<LeaseLiabilityCompilationDTO> page = leaseLiabilityCompilationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liability-compilations/count} : count all the leaseLiabilityCompilations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liability-compilations/count")
    public ResponseEntity<Long> countLeaseLiabilityCompilations(LeaseLiabilityCompilationCriteria criteria) {
        log.debug("REST request to count LeaseLiabilityCompilations by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityCompilationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liability-compilations/:id} : get the "id" leaseLiabilityCompilation.
     *
     * @param id the id of the leaseLiabilityCompilationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityCompilationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liability-compilations/{id}")
    public ResponseEntity<LeaseLiabilityCompilationDTO> getLeaseLiabilityCompilation(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityCompilation : {}", id);
        Optional<LeaseLiabilityCompilationDTO> leaseLiabilityCompilationDTO = leaseLiabilityCompilationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseLiabilityCompilationDTO);
    }

    @PostMapping("/lease-liability-compilations/{id}/activate")
    public ResponseEntity<Void> activateLeaseLiabilityCompilation(@PathVariable Long id) {
        log.debug("REST request to activate LeaseLiabilityCompilation schedules : {}", id);
        if (!leaseLiabilityCompilationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        int affected = leaseLiabilityCompilationService.updateScheduleItemActivation(id, true);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "leaseLiabilityCompilation.activated", id + ":" + affected))
            .build();
    }

    @PostMapping("/lease-liability-compilations/{id}/deactivate")
    public ResponseEntity<Void> deactivateLeaseLiabilityCompilation(@PathVariable Long id) {
        log.debug("REST request to deactivate LeaseLiabilityCompilation schedules : {}", id);
        if (!leaseLiabilityCompilationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }
        int affected = leaseLiabilityCompilationService.updateScheduleItemActivation(id, false);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createAlert(applicationName, "leaseLiabilityCompilation.deactivated", id + ":" + affected))
            .build();
    }

    /**
     * {@code DELETE  /lease-liability-compilations/:id} : delete the "id" leaseLiabilityCompilation.
     *
     * @param id the id of the leaseLiabilityCompilationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-liability-compilations/{id}")
    public ResponseEntity<Void> deleteLeaseLiabilityCompilation(@PathVariable Long id) {
        log.debug("REST request to delete LeaseLiabilityCompilation : {}", id);
        leaseLiabilityCompilationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-liability-compilations?query=:query} : search for the leaseLiabilityCompilation corresponding
     * to the query.
     *
     * @param query the query of the leaseLiabilityCompilation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liability-compilations")
    public ResponseEntity<List<LeaseLiabilityCompilationDTO>> searchLeaseLiabilityCompilations(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseLiabilityCompilations for query {}", query);
        Page<LeaseLiabilityCompilationDTO> page = leaseLiabilityCompilationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
