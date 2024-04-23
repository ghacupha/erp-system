package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.repository.LoanPerformanceClassificationRepository;
import io.github.erp.service.LoanPerformanceClassificationQueryService;
import io.github.erp.service.LoanPerformanceClassificationService;
import io.github.erp.service.criteria.LoanPerformanceClassificationCriteria;
import io.github.erp.service.dto.LoanPerformanceClassificationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LoanPerformanceClassification}.
 */
@RestController("LoanPerformanceClassificationResourceProd")
@RequestMapping("/api/granular-data")
public class LoanPerformanceClassificationResourceProd {

    private final Logger log = LoggerFactory.getLogger(LoanPerformanceClassificationResourceProd.class);

    private static final String ENTITY_NAME = "loanPerformanceClassification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanPerformanceClassificationService loanPerformanceClassificationService;

    private final LoanPerformanceClassificationRepository loanPerformanceClassificationRepository;

    private final LoanPerformanceClassificationQueryService loanPerformanceClassificationQueryService;

    public LoanPerformanceClassificationResourceProd(
        LoanPerformanceClassificationService loanPerformanceClassificationService,
        LoanPerformanceClassificationRepository loanPerformanceClassificationRepository,
        LoanPerformanceClassificationQueryService loanPerformanceClassificationQueryService
    ) {
        this.loanPerformanceClassificationService = loanPerformanceClassificationService;
        this.loanPerformanceClassificationRepository = loanPerformanceClassificationRepository;
        this.loanPerformanceClassificationQueryService = loanPerformanceClassificationQueryService;
    }

    /**
     * {@code POST  /loan-performance-classifications} : Create a new loanPerformanceClassification.
     *
     * @param loanPerformanceClassificationDTO the loanPerformanceClassificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanPerformanceClassificationDTO, or with status {@code 400 (Bad Request)} if the loanPerformanceClassification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loan-performance-classifications")
    public ResponseEntity<LoanPerformanceClassificationDTO> createLoanPerformanceClassification(
        @Valid @RequestBody LoanPerformanceClassificationDTO loanPerformanceClassificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LoanPerformanceClassification : {}", loanPerformanceClassificationDTO);
        if (loanPerformanceClassificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanPerformanceClassification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanPerformanceClassificationDTO result = loanPerformanceClassificationService.save(loanPerformanceClassificationDTO);
        return ResponseEntity
            .created(new URI("/api/loan-performance-classifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loan-performance-classifications/:id} : Updates an existing loanPerformanceClassification.
     *
     * @param id the id of the loanPerformanceClassificationDTO to save.
     * @param loanPerformanceClassificationDTO the loanPerformanceClassificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanPerformanceClassificationDTO,
     * or with status {@code 400 (Bad Request)} if the loanPerformanceClassificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanPerformanceClassificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loan-performance-classifications/{id}")
    public ResponseEntity<LoanPerformanceClassificationDTO> updateLoanPerformanceClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoanPerformanceClassificationDTO loanPerformanceClassificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoanPerformanceClassification : {}, {}", id, loanPerformanceClassificationDTO);
        if (loanPerformanceClassificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanPerformanceClassificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanPerformanceClassificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoanPerformanceClassificationDTO result = loanPerformanceClassificationService.save(loanPerformanceClassificationDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanPerformanceClassificationDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /loan-performance-classifications/:id} : Partial updates given fields of an existing loanPerformanceClassification, field will ignore if it is null
     *
     * @param id the id of the loanPerformanceClassificationDTO to save.
     * @param loanPerformanceClassificationDTO the loanPerformanceClassificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanPerformanceClassificationDTO,
     * or with status {@code 400 (Bad Request)} if the loanPerformanceClassificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanPerformanceClassificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanPerformanceClassificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loan-performance-classifications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanPerformanceClassificationDTO> partialUpdateLoanPerformanceClassification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoanPerformanceClassificationDTO loanPerformanceClassificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanPerformanceClassification partially : {}, {}", id, loanPerformanceClassificationDTO);
        if (loanPerformanceClassificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanPerformanceClassificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanPerformanceClassificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanPerformanceClassificationDTO> result = loanPerformanceClassificationService.partialUpdate(
            loanPerformanceClassificationDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanPerformanceClassificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loan-performance-classifications} : get all the loanPerformanceClassifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanPerformanceClassifications in body.
     */
    @GetMapping("/loan-performance-classifications")
    public ResponseEntity<List<LoanPerformanceClassificationDTO>> getAllLoanPerformanceClassifications(
        LoanPerformanceClassificationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LoanPerformanceClassifications by criteria: {}", criteria);
        Page<LoanPerformanceClassificationDTO> page = loanPerformanceClassificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loan-performance-classifications/count} : count all the loanPerformanceClassifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/loan-performance-classifications/count")
    public ResponseEntity<Long> countLoanPerformanceClassifications(LoanPerformanceClassificationCriteria criteria) {
        log.debug("REST request to count LoanPerformanceClassifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(loanPerformanceClassificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /loan-performance-classifications/:id} : get the "id" loanPerformanceClassification.
     *
     * @param id the id of the loanPerformanceClassificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanPerformanceClassificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loan-performance-classifications/{id}")
    public ResponseEntity<LoanPerformanceClassificationDTO> getLoanPerformanceClassification(@PathVariable Long id) {
        log.debug("REST request to get LoanPerformanceClassification : {}", id);
        Optional<LoanPerformanceClassificationDTO> loanPerformanceClassificationDTO = loanPerformanceClassificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanPerformanceClassificationDTO);
    }

    /**
     * {@code DELETE  /loan-performance-classifications/:id} : delete the "id" loanPerformanceClassification.
     *
     * @param id the id of the loanPerformanceClassificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loan-performance-classifications/{id}")
    public ResponseEntity<Void> deleteLoanPerformanceClassification(@PathVariable Long id) {
        log.debug("REST request to delete LoanPerformanceClassification : {}", id);
        loanPerformanceClassificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loan-performance-classifications?query=:query} : search for the loanPerformanceClassification corresponding
     * to the query.
     *
     * @param query the query of the loanPerformanceClassification search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loan-performance-classifications")
    public ResponseEntity<List<LoanPerformanceClassificationDTO>> searchLoanPerformanceClassifications(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LoanPerformanceClassifications for query {}", query);
        Page<LoanPerformanceClassificationDTO> page = loanPerformanceClassificationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
