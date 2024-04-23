package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.LoanRestructureItemRepository;
import io.github.erp.service.LoanRestructureItemQueryService;
import io.github.erp.service.LoanRestructureItemService;
import io.github.erp.service.criteria.LoanRestructureItemCriteria;
import io.github.erp.service.dto.LoanRestructureItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LoanRestructureItem}.
 */
@RestController
@RequestMapping("/api")
public class LoanRestructureItemResource {

    private final Logger log = LoggerFactory.getLogger(LoanRestructureItemResource.class);

    private static final String ENTITY_NAME = "loanRestructureItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanRestructureItemService loanRestructureItemService;

    private final LoanRestructureItemRepository loanRestructureItemRepository;

    private final LoanRestructureItemQueryService loanRestructureItemQueryService;

    public LoanRestructureItemResource(
        LoanRestructureItemService loanRestructureItemService,
        LoanRestructureItemRepository loanRestructureItemRepository,
        LoanRestructureItemQueryService loanRestructureItemQueryService
    ) {
        this.loanRestructureItemService = loanRestructureItemService;
        this.loanRestructureItemRepository = loanRestructureItemRepository;
        this.loanRestructureItemQueryService = loanRestructureItemQueryService;
    }

    /**
     * {@code POST  /loan-restructure-items} : Create a new loanRestructureItem.
     *
     * @param loanRestructureItemDTO the loanRestructureItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanRestructureItemDTO, or with status {@code 400 (Bad Request)} if the loanRestructureItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loan-restructure-items")
    public ResponseEntity<LoanRestructureItemDTO> createLoanRestructureItem(
        @Valid @RequestBody LoanRestructureItemDTO loanRestructureItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LoanRestructureItem : {}", loanRestructureItemDTO);
        if (loanRestructureItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanRestructureItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanRestructureItemDTO result = loanRestructureItemService.save(loanRestructureItemDTO);
        return ResponseEntity
            .created(new URI("/api/loan-restructure-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loan-restructure-items/:id} : Updates an existing loanRestructureItem.
     *
     * @param id the id of the loanRestructureItemDTO to save.
     * @param loanRestructureItemDTO the loanRestructureItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanRestructureItemDTO,
     * or with status {@code 400 (Bad Request)} if the loanRestructureItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanRestructureItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loan-restructure-items/{id}")
    public ResponseEntity<LoanRestructureItemDTO> updateLoanRestructureItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoanRestructureItemDTO loanRestructureItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoanRestructureItem : {}, {}", id, loanRestructureItemDTO);
        if (loanRestructureItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanRestructureItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanRestructureItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoanRestructureItemDTO result = loanRestructureItemService.save(loanRestructureItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanRestructureItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loan-restructure-items/:id} : Partial updates given fields of an existing loanRestructureItem, field will ignore if it is null
     *
     * @param id the id of the loanRestructureItemDTO to save.
     * @param loanRestructureItemDTO the loanRestructureItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanRestructureItemDTO,
     * or with status {@code 400 (Bad Request)} if the loanRestructureItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanRestructureItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanRestructureItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loan-restructure-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanRestructureItemDTO> partialUpdateLoanRestructureItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoanRestructureItemDTO loanRestructureItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanRestructureItem partially : {}, {}", id, loanRestructureItemDTO);
        if (loanRestructureItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanRestructureItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanRestructureItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanRestructureItemDTO> result = loanRestructureItemService.partialUpdate(loanRestructureItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanRestructureItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loan-restructure-items} : get all the loanRestructureItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanRestructureItems in body.
     */
    @GetMapping("/loan-restructure-items")
    public ResponseEntity<List<LoanRestructureItemDTO>> getAllLoanRestructureItems(
        LoanRestructureItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LoanRestructureItems by criteria: {}", criteria);
        Page<LoanRestructureItemDTO> page = loanRestructureItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loan-restructure-items/count} : count all the loanRestructureItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/loan-restructure-items/count")
    public ResponseEntity<Long> countLoanRestructureItems(LoanRestructureItemCriteria criteria) {
        log.debug("REST request to count LoanRestructureItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(loanRestructureItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /loan-restructure-items/:id} : get the "id" loanRestructureItem.
     *
     * @param id the id of the loanRestructureItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanRestructureItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loan-restructure-items/{id}")
    public ResponseEntity<LoanRestructureItemDTO> getLoanRestructureItem(@PathVariable Long id) {
        log.debug("REST request to get LoanRestructureItem : {}", id);
        Optional<LoanRestructureItemDTO> loanRestructureItemDTO = loanRestructureItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanRestructureItemDTO);
    }

    /**
     * {@code DELETE  /loan-restructure-items/:id} : delete the "id" loanRestructureItem.
     *
     * @param id the id of the loanRestructureItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loan-restructure-items/{id}")
    public ResponseEntity<Void> deleteLoanRestructureItem(@PathVariable Long id) {
        log.debug("REST request to delete LoanRestructureItem : {}", id);
        loanRestructureItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loan-restructure-items?query=:query} : search for the loanRestructureItem corresponding
     * to the query.
     *
     * @param query the query of the loanRestructureItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loan-restructure-items")
    public ResponseEntity<List<LoanRestructureItemDTO>> searchLoanRestructureItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoanRestructureItems for query {}", query);
        Page<LoanRestructureItemDTO> page = loanRestructureItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
