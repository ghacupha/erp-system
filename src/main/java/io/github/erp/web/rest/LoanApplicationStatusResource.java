package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.LoanApplicationStatusRepository;
import io.github.erp.service.LoanApplicationStatusQueryService;
import io.github.erp.service.LoanApplicationStatusService;
import io.github.erp.service.criteria.LoanApplicationStatusCriteria;
import io.github.erp.service.dto.LoanApplicationStatusDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LoanApplicationStatus}.
 */
@RestController
@RequestMapping("/api")
public class LoanApplicationStatusResource {

    private final Logger log = LoggerFactory.getLogger(LoanApplicationStatusResource.class);

    private static final String ENTITY_NAME = "loanApplicationStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanApplicationStatusService loanApplicationStatusService;

    private final LoanApplicationStatusRepository loanApplicationStatusRepository;

    private final LoanApplicationStatusQueryService loanApplicationStatusQueryService;

    public LoanApplicationStatusResource(
        LoanApplicationStatusService loanApplicationStatusService,
        LoanApplicationStatusRepository loanApplicationStatusRepository,
        LoanApplicationStatusQueryService loanApplicationStatusQueryService
    ) {
        this.loanApplicationStatusService = loanApplicationStatusService;
        this.loanApplicationStatusRepository = loanApplicationStatusRepository;
        this.loanApplicationStatusQueryService = loanApplicationStatusQueryService;
    }

    /**
     * {@code POST  /loan-application-statuses} : Create a new loanApplicationStatus.
     *
     * @param loanApplicationStatusDTO the loanApplicationStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanApplicationStatusDTO, or with status {@code 400 (Bad Request)} if the loanApplicationStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loan-application-statuses")
    public ResponseEntity<LoanApplicationStatusDTO> createLoanApplicationStatus(
        @Valid @RequestBody LoanApplicationStatusDTO loanApplicationStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LoanApplicationStatus : {}", loanApplicationStatusDTO);
        if (loanApplicationStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanApplicationStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanApplicationStatusDTO result = loanApplicationStatusService.save(loanApplicationStatusDTO);
        return ResponseEntity
            .created(new URI("/api/loan-application-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loan-application-statuses/:id} : Updates an existing loanApplicationStatus.
     *
     * @param id the id of the loanApplicationStatusDTO to save.
     * @param loanApplicationStatusDTO the loanApplicationStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanApplicationStatusDTO,
     * or with status {@code 400 (Bad Request)} if the loanApplicationStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanApplicationStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loan-application-statuses/{id}")
    public ResponseEntity<LoanApplicationStatusDTO> updateLoanApplicationStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoanApplicationStatusDTO loanApplicationStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoanApplicationStatus : {}, {}", id, loanApplicationStatusDTO);
        if (loanApplicationStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanApplicationStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanApplicationStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoanApplicationStatusDTO result = loanApplicationStatusService.save(loanApplicationStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanApplicationStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loan-application-statuses/:id} : Partial updates given fields of an existing loanApplicationStatus, field will ignore if it is null
     *
     * @param id the id of the loanApplicationStatusDTO to save.
     * @param loanApplicationStatusDTO the loanApplicationStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanApplicationStatusDTO,
     * or with status {@code 400 (Bad Request)} if the loanApplicationStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanApplicationStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanApplicationStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loan-application-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanApplicationStatusDTO> partialUpdateLoanApplicationStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoanApplicationStatusDTO loanApplicationStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanApplicationStatus partially : {}, {}", id, loanApplicationStatusDTO);
        if (loanApplicationStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanApplicationStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanApplicationStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanApplicationStatusDTO> result = loanApplicationStatusService.partialUpdate(loanApplicationStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanApplicationStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loan-application-statuses} : get all the loanApplicationStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanApplicationStatuses in body.
     */
    @GetMapping("/loan-application-statuses")
    public ResponseEntity<List<LoanApplicationStatusDTO>> getAllLoanApplicationStatuses(
        LoanApplicationStatusCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LoanApplicationStatuses by criteria: {}", criteria);
        Page<LoanApplicationStatusDTO> page = loanApplicationStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loan-application-statuses/count} : count all the loanApplicationStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/loan-application-statuses/count")
    public ResponseEntity<Long> countLoanApplicationStatuses(LoanApplicationStatusCriteria criteria) {
        log.debug("REST request to count LoanApplicationStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(loanApplicationStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /loan-application-statuses/:id} : get the "id" loanApplicationStatus.
     *
     * @param id the id of the loanApplicationStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanApplicationStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loan-application-statuses/{id}")
    public ResponseEntity<LoanApplicationStatusDTO> getLoanApplicationStatus(@PathVariable Long id) {
        log.debug("REST request to get LoanApplicationStatus : {}", id);
        Optional<LoanApplicationStatusDTO> loanApplicationStatusDTO = loanApplicationStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanApplicationStatusDTO);
    }

    /**
     * {@code DELETE  /loan-application-statuses/:id} : delete the "id" loanApplicationStatus.
     *
     * @param id the id of the loanApplicationStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loan-application-statuses/{id}")
    public ResponseEntity<Void> deleteLoanApplicationStatus(@PathVariable Long id) {
        log.debug("REST request to delete LoanApplicationStatus : {}", id);
        loanApplicationStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loan-application-statuses?query=:query} : search for the loanApplicationStatus corresponding
     * to the query.
     *
     * @param query the query of the loanApplicationStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loan-application-statuses")
    public ResponseEntity<List<LoanApplicationStatusDTO>> searchLoanApplicationStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoanApplicationStatuses for query {}", query);
        Page<LoanApplicationStatusDTO> page = loanApplicationStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
