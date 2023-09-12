package io.github.erp.web.rest;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.LoanRepaymentFrequencyRepository;
import io.github.erp.service.LoanRepaymentFrequencyQueryService;
import io.github.erp.service.LoanRepaymentFrequencyService;
import io.github.erp.service.criteria.LoanRepaymentFrequencyCriteria;
import io.github.erp.service.dto.LoanRepaymentFrequencyDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LoanRepaymentFrequency}.
 */
@RestController
@RequestMapping("/api")
public class LoanRepaymentFrequencyResource {

    private final Logger log = LoggerFactory.getLogger(LoanRepaymentFrequencyResource.class);

    private static final String ENTITY_NAME = "loanRepaymentFrequency";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanRepaymentFrequencyService loanRepaymentFrequencyService;

    private final LoanRepaymentFrequencyRepository loanRepaymentFrequencyRepository;

    private final LoanRepaymentFrequencyQueryService loanRepaymentFrequencyQueryService;

    public LoanRepaymentFrequencyResource(
        LoanRepaymentFrequencyService loanRepaymentFrequencyService,
        LoanRepaymentFrequencyRepository loanRepaymentFrequencyRepository,
        LoanRepaymentFrequencyQueryService loanRepaymentFrequencyQueryService
    ) {
        this.loanRepaymentFrequencyService = loanRepaymentFrequencyService;
        this.loanRepaymentFrequencyRepository = loanRepaymentFrequencyRepository;
        this.loanRepaymentFrequencyQueryService = loanRepaymentFrequencyQueryService;
    }

    /**
     * {@code POST  /loan-repayment-frequencies} : Create a new loanRepaymentFrequency.
     *
     * @param loanRepaymentFrequencyDTO the loanRepaymentFrequencyDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanRepaymentFrequencyDTO, or with status {@code 400 (Bad Request)} if the loanRepaymentFrequency has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loan-repayment-frequencies")
    public ResponseEntity<LoanRepaymentFrequencyDTO> createLoanRepaymentFrequency(
        @Valid @RequestBody LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LoanRepaymentFrequency : {}", loanRepaymentFrequencyDTO);
        if (loanRepaymentFrequencyDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanRepaymentFrequency cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanRepaymentFrequencyDTO result = loanRepaymentFrequencyService.save(loanRepaymentFrequencyDTO);
        return ResponseEntity
            .created(new URI("/api/loan-repayment-frequencies/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loan-repayment-frequencies/:id} : Updates an existing loanRepaymentFrequency.
     *
     * @param id the id of the loanRepaymentFrequencyDTO to save.
     * @param loanRepaymentFrequencyDTO the loanRepaymentFrequencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanRepaymentFrequencyDTO,
     * or with status {@code 400 (Bad Request)} if the loanRepaymentFrequencyDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanRepaymentFrequencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loan-repayment-frequencies/{id}")
    public ResponseEntity<LoanRepaymentFrequencyDTO> updateLoanRepaymentFrequency(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoanRepaymentFrequency : {}, {}", id, loanRepaymentFrequencyDTO);
        if (loanRepaymentFrequencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanRepaymentFrequencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanRepaymentFrequencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoanRepaymentFrequencyDTO result = loanRepaymentFrequencyService.save(loanRepaymentFrequencyDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanRepaymentFrequencyDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loan-repayment-frequencies/:id} : Partial updates given fields of an existing loanRepaymentFrequency, field will ignore if it is null
     *
     * @param id the id of the loanRepaymentFrequencyDTO to save.
     * @param loanRepaymentFrequencyDTO the loanRepaymentFrequencyDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanRepaymentFrequencyDTO,
     * or with status {@code 400 (Bad Request)} if the loanRepaymentFrequencyDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanRepaymentFrequencyDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanRepaymentFrequencyDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loan-repayment-frequencies/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanRepaymentFrequencyDTO> partialUpdateLoanRepaymentFrequency(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoanRepaymentFrequencyDTO loanRepaymentFrequencyDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanRepaymentFrequency partially : {}, {}", id, loanRepaymentFrequencyDTO);
        if (loanRepaymentFrequencyDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanRepaymentFrequencyDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanRepaymentFrequencyRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanRepaymentFrequencyDTO> result = loanRepaymentFrequencyService.partialUpdate(loanRepaymentFrequencyDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanRepaymentFrequencyDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loan-repayment-frequencies} : get all the loanRepaymentFrequencies.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanRepaymentFrequencies in body.
     */
    @GetMapping("/loan-repayment-frequencies")
    public ResponseEntity<List<LoanRepaymentFrequencyDTO>> getAllLoanRepaymentFrequencies(
        LoanRepaymentFrequencyCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LoanRepaymentFrequencies by criteria: {}", criteria);
        Page<LoanRepaymentFrequencyDTO> page = loanRepaymentFrequencyQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loan-repayment-frequencies/count} : count all the loanRepaymentFrequencies.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/loan-repayment-frequencies/count")
    public ResponseEntity<Long> countLoanRepaymentFrequencies(LoanRepaymentFrequencyCriteria criteria) {
        log.debug("REST request to count LoanRepaymentFrequencies by criteria: {}", criteria);
        return ResponseEntity.ok().body(loanRepaymentFrequencyQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /loan-repayment-frequencies/:id} : get the "id" loanRepaymentFrequency.
     *
     * @param id the id of the loanRepaymentFrequencyDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanRepaymentFrequencyDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loan-repayment-frequencies/{id}")
    public ResponseEntity<LoanRepaymentFrequencyDTO> getLoanRepaymentFrequency(@PathVariable Long id) {
        log.debug("REST request to get LoanRepaymentFrequency : {}", id);
        Optional<LoanRepaymentFrequencyDTO> loanRepaymentFrequencyDTO = loanRepaymentFrequencyService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanRepaymentFrequencyDTO);
    }

    /**
     * {@code DELETE  /loan-repayment-frequencies/:id} : delete the "id" loanRepaymentFrequency.
     *
     * @param id the id of the loanRepaymentFrequencyDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loan-repayment-frequencies/{id}")
    public ResponseEntity<Void> deleteLoanRepaymentFrequency(@PathVariable Long id) {
        log.debug("REST request to delete LoanRepaymentFrequency : {}", id);
        loanRepaymentFrequencyService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loan-repayment-frequencies?query=:query} : search for the loanRepaymentFrequency corresponding
     * to the query.
     *
     * @param query the query of the loanRepaymentFrequency search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loan-repayment-frequencies")
    public ResponseEntity<List<LoanRepaymentFrequencyDTO>> searchLoanRepaymentFrequencies(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoanRepaymentFrequencies for query {}", query);
        Page<LoanRepaymentFrequencyDTO> page = loanRepaymentFrequencyService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
