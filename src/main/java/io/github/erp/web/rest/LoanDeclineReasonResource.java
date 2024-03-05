package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

import io.github.erp.repository.LoanDeclineReasonRepository;
import io.github.erp.service.LoanDeclineReasonQueryService;
import io.github.erp.service.LoanDeclineReasonService;
import io.github.erp.service.criteria.LoanDeclineReasonCriteria;
import io.github.erp.service.dto.LoanDeclineReasonDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LoanDeclineReason}.
 */
@RestController
@RequestMapping("/api")
public class LoanDeclineReasonResource {

    private final Logger log = LoggerFactory.getLogger(LoanDeclineReasonResource.class);

    private static final String ENTITY_NAME = "loanDeclineReason";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanDeclineReasonService loanDeclineReasonService;

    private final LoanDeclineReasonRepository loanDeclineReasonRepository;

    private final LoanDeclineReasonQueryService loanDeclineReasonQueryService;

    public LoanDeclineReasonResource(
        LoanDeclineReasonService loanDeclineReasonService,
        LoanDeclineReasonRepository loanDeclineReasonRepository,
        LoanDeclineReasonQueryService loanDeclineReasonQueryService
    ) {
        this.loanDeclineReasonService = loanDeclineReasonService;
        this.loanDeclineReasonRepository = loanDeclineReasonRepository;
        this.loanDeclineReasonQueryService = loanDeclineReasonQueryService;
    }

    /**
     * {@code POST  /loan-decline-reasons} : Create a new loanDeclineReason.
     *
     * @param loanDeclineReasonDTO the loanDeclineReasonDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanDeclineReasonDTO, or with status {@code 400 (Bad Request)} if the loanDeclineReason has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loan-decline-reasons")
    public ResponseEntity<LoanDeclineReasonDTO> createLoanDeclineReason(@Valid @RequestBody LoanDeclineReasonDTO loanDeclineReasonDTO)
        throws URISyntaxException {
        log.debug("REST request to save LoanDeclineReason : {}", loanDeclineReasonDTO);
        if (loanDeclineReasonDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanDeclineReason cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanDeclineReasonDTO result = loanDeclineReasonService.save(loanDeclineReasonDTO);
        return ResponseEntity
            .created(new URI("/api/loan-decline-reasons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loan-decline-reasons/:id} : Updates an existing loanDeclineReason.
     *
     * @param id the id of the loanDeclineReasonDTO to save.
     * @param loanDeclineReasonDTO the loanDeclineReasonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanDeclineReasonDTO,
     * or with status {@code 400 (Bad Request)} if the loanDeclineReasonDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanDeclineReasonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loan-decline-reasons/{id}")
    public ResponseEntity<LoanDeclineReasonDTO> updateLoanDeclineReason(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoanDeclineReasonDTO loanDeclineReasonDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoanDeclineReason : {}, {}", id, loanDeclineReasonDTO);
        if (loanDeclineReasonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanDeclineReasonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanDeclineReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoanDeclineReasonDTO result = loanDeclineReasonService.save(loanDeclineReasonDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanDeclineReasonDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loan-decline-reasons/:id} : Partial updates given fields of an existing loanDeclineReason, field will ignore if it is null
     *
     * @param id the id of the loanDeclineReasonDTO to save.
     * @param loanDeclineReasonDTO the loanDeclineReasonDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanDeclineReasonDTO,
     * or with status {@code 400 (Bad Request)} if the loanDeclineReasonDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanDeclineReasonDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanDeclineReasonDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loan-decline-reasons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanDeclineReasonDTO> partialUpdateLoanDeclineReason(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoanDeclineReasonDTO loanDeclineReasonDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanDeclineReason partially : {}, {}", id, loanDeclineReasonDTO);
        if (loanDeclineReasonDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanDeclineReasonDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanDeclineReasonRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanDeclineReasonDTO> result = loanDeclineReasonService.partialUpdate(loanDeclineReasonDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanDeclineReasonDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loan-decline-reasons} : get all the loanDeclineReasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanDeclineReasons in body.
     */
    @GetMapping("/loan-decline-reasons")
    public ResponseEntity<List<LoanDeclineReasonDTO>> getAllLoanDeclineReasons(LoanDeclineReasonCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LoanDeclineReasons by criteria: {}", criteria);
        Page<LoanDeclineReasonDTO> page = loanDeclineReasonQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loan-decline-reasons/count} : count all the loanDeclineReasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/loan-decline-reasons/count")
    public ResponseEntity<Long> countLoanDeclineReasons(LoanDeclineReasonCriteria criteria) {
        log.debug("REST request to count LoanDeclineReasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(loanDeclineReasonQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /loan-decline-reasons/:id} : get the "id" loanDeclineReason.
     *
     * @param id the id of the loanDeclineReasonDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanDeclineReasonDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loan-decline-reasons/{id}")
    public ResponseEntity<LoanDeclineReasonDTO> getLoanDeclineReason(@PathVariable Long id) {
        log.debug("REST request to get LoanDeclineReason : {}", id);
        Optional<LoanDeclineReasonDTO> loanDeclineReasonDTO = loanDeclineReasonService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanDeclineReasonDTO);
    }

    /**
     * {@code DELETE  /loan-decline-reasons/:id} : delete the "id" loanDeclineReason.
     *
     * @param id the id of the loanDeclineReasonDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loan-decline-reasons/{id}")
    public ResponseEntity<Void> deleteLoanDeclineReason(@PathVariable Long id) {
        log.debug("REST request to delete LoanDeclineReason : {}", id);
        loanDeclineReasonService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loan-decline-reasons?query=:query} : search for the loanDeclineReason corresponding
     * to the query.
     *
     * @param query the query of the loanDeclineReason search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loan-decline-reasons")
    public ResponseEntity<List<LoanDeclineReasonDTO>> searchLoanDeclineReasons(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoanDeclineReasons for query {}", query);
        Page<LoanDeclineReasonDTO> page = loanDeclineReasonService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
