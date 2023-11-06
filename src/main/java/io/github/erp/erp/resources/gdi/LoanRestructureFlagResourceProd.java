package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.repository.LoanRestructureFlagRepository;
import io.github.erp.service.LoanRestructureFlagQueryService;
import io.github.erp.service.LoanRestructureFlagService;
import io.github.erp.service.criteria.LoanRestructureFlagCriteria;
import io.github.erp.service.dto.LoanRestructureFlagDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LoanRestructureFlag}.
 */
@RestController("LoanRestructureFlagResourceProd")
@RequestMapping("/api/granular-data")
public class LoanRestructureFlagResourceProd {

    private final Logger log = LoggerFactory.getLogger(LoanRestructureFlagResourceProd.class);

    private static final String ENTITY_NAME = "loanRestructureFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanRestructureFlagService loanRestructureFlagService;

    private final LoanRestructureFlagRepository loanRestructureFlagRepository;

    private final LoanRestructureFlagQueryService loanRestructureFlagQueryService;

    public LoanRestructureFlagResourceProd(
        LoanRestructureFlagService loanRestructureFlagService,
        LoanRestructureFlagRepository loanRestructureFlagRepository,
        LoanRestructureFlagQueryService loanRestructureFlagQueryService
    ) {
        this.loanRestructureFlagService = loanRestructureFlagService;
        this.loanRestructureFlagRepository = loanRestructureFlagRepository;
        this.loanRestructureFlagQueryService = loanRestructureFlagQueryService;
    }

    /**
     * {@code POST  /loan-restructure-flags} : Create a new loanRestructureFlag.
     *
     * @param loanRestructureFlagDTO the loanRestructureFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanRestructureFlagDTO, or with status {@code 400 (Bad Request)} if the loanRestructureFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loan-restructure-flags")
    public ResponseEntity<LoanRestructureFlagDTO> createLoanRestructureFlag(
        @Valid @RequestBody LoanRestructureFlagDTO loanRestructureFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LoanRestructureFlag : {}", loanRestructureFlagDTO);
        if (loanRestructureFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanRestructureFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanRestructureFlagDTO result = loanRestructureFlagService.save(loanRestructureFlagDTO);
        return ResponseEntity
            .created(new URI("/api/loan-restructure-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loan-restructure-flags/:id} : Updates an existing loanRestructureFlag.
     *
     * @param id the id of the loanRestructureFlagDTO to save.
     * @param loanRestructureFlagDTO the loanRestructureFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanRestructureFlagDTO,
     * or with status {@code 400 (Bad Request)} if the loanRestructureFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanRestructureFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loan-restructure-flags/{id}")
    public ResponseEntity<LoanRestructureFlagDTO> updateLoanRestructureFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoanRestructureFlagDTO loanRestructureFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoanRestructureFlag : {}, {}", id, loanRestructureFlagDTO);
        if (loanRestructureFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanRestructureFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanRestructureFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoanRestructureFlagDTO result = loanRestructureFlagService.save(loanRestructureFlagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanRestructureFlagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loan-restructure-flags/:id} : Partial updates given fields of an existing loanRestructureFlag, field will ignore if it is null
     *
     * @param id the id of the loanRestructureFlagDTO to save.
     * @param loanRestructureFlagDTO the loanRestructureFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanRestructureFlagDTO,
     * or with status {@code 400 (Bad Request)} if the loanRestructureFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanRestructureFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanRestructureFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loan-restructure-flags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanRestructureFlagDTO> partialUpdateLoanRestructureFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoanRestructureFlagDTO loanRestructureFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanRestructureFlag partially : {}, {}", id, loanRestructureFlagDTO);
        if (loanRestructureFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanRestructureFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanRestructureFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanRestructureFlagDTO> result = loanRestructureFlagService.partialUpdate(loanRestructureFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanRestructureFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loan-restructure-flags} : get all the loanRestructureFlags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanRestructureFlags in body.
     */
    @GetMapping("/loan-restructure-flags")
    public ResponseEntity<List<LoanRestructureFlagDTO>> getAllLoanRestructureFlags(
        LoanRestructureFlagCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LoanRestructureFlags by criteria: {}", criteria);
        Page<LoanRestructureFlagDTO> page = loanRestructureFlagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loan-restructure-flags/count} : count all the loanRestructureFlags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/loan-restructure-flags/count")
    public ResponseEntity<Long> countLoanRestructureFlags(LoanRestructureFlagCriteria criteria) {
        log.debug("REST request to count LoanRestructureFlags by criteria: {}", criteria);
        return ResponseEntity.ok().body(loanRestructureFlagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /loan-restructure-flags/:id} : get the "id" loanRestructureFlag.
     *
     * @param id the id of the loanRestructureFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanRestructureFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loan-restructure-flags/{id}")
    public ResponseEntity<LoanRestructureFlagDTO> getLoanRestructureFlag(@PathVariable Long id) {
        log.debug("REST request to get LoanRestructureFlag : {}", id);
        Optional<LoanRestructureFlagDTO> loanRestructureFlagDTO = loanRestructureFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanRestructureFlagDTO);
    }

    /**
     * {@code DELETE  /loan-restructure-flags/:id} : delete the "id" loanRestructureFlag.
     *
     * @param id the id of the loanRestructureFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loan-restructure-flags/{id}")
    public ResponseEntity<Void> deleteLoanRestructureFlag(@PathVariable Long id) {
        log.debug("REST request to delete LoanRestructureFlag : {}", id);
        loanRestructureFlagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loan-restructure-flags?query=:query} : search for the loanRestructureFlag corresponding
     * to the query.
     *
     * @param query the query of the loanRestructureFlag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loan-restructure-flags")
    public ResponseEntity<List<LoanRestructureFlagDTO>> searchLoanRestructureFlags(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoanRestructureFlags for query {}", query);
        Page<LoanRestructureFlagDTO> page = loanRestructureFlagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
