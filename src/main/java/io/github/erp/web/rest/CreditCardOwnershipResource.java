package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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

import io.github.erp.repository.CreditCardOwnershipRepository;
import io.github.erp.service.CreditCardOwnershipQueryService;
import io.github.erp.service.CreditCardOwnershipService;
import io.github.erp.service.criteria.CreditCardOwnershipCriteria;
import io.github.erp.service.dto.CreditCardOwnershipDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CreditCardOwnership}.
 */
@RestController
@RequestMapping("/api")
public class CreditCardOwnershipResource {

    private final Logger log = LoggerFactory.getLogger(CreditCardOwnershipResource.class);

    private static final String ENTITY_NAME = "creditCardOwnership";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CreditCardOwnershipService creditCardOwnershipService;

    private final CreditCardOwnershipRepository creditCardOwnershipRepository;

    private final CreditCardOwnershipQueryService creditCardOwnershipQueryService;

    public CreditCardOwnershipResource(
        CreditCardOwnershipService creditCardOwnershipService,
        CreditCardOwnershipRepository creditCardOwnershipRepository,
        CreditCardOwnershipQueryService creditCardOwnershipQueryService
    ) {
        this.creditCardOwnershipService = creditCardOwnershipService;
        this.creditCardOwnershipRepository = creditCardOwnershipRepository;
        this.creditCardOwnershipQueryService = creditCardOwnershipQueryService;
    }

    /**
     * {@code POST  /credit-card-ownerships} : Create a new creditCardOwnership.
     *
     * @param creditCardOwnershipDTO the creditCardOwnershipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new creditCardOwnershipDTO, or with status {@code 400 (Bad Request)} if the creditCardOwnership has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/credit-card-ownerships")
    public ResponseEntity<CreditCardOwnershipDTO> createCreditCardOwnership(
        @Valid @RequestBody CreditCardOwnershipDTO creditCardOwnershipDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CreditCardOwnership : {}", creditCardOwnershipDTO);
        if (creditCardOwnershipDTO.getId() != null) {
            throw new BadRequestAlertException("A new creditCardOwnership cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CreditCardOwnershipDTO result = creditCardOwnershipService.save(creditCardOwnershipDTO);
        return ResponseEntity
            .created(new URI("/api/credit-card-ownerships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /credit-card-ownerships/:id} : Updates an existing creditCardOwnership.
     *
     * @param id the id of the creditCardOwnershipDTO to save.
     * @param creditCardOwnershipDTO the creditCardOwnershipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditCardOwnershipDTO,
     * or with status {@code 400 (Bad Request)} if the creditCardOwnershipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the creditCardOwnershipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/credit-card-ownerships/{id}")
    public ResponseEntity<CreditCardOwnershipDTO> updateCreditCardOwnership(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CreditCardOwnershipDTO creditCardOwnershipDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CreditCardOwnership : {}, {}", id, creditCardOwnershipDTO);
        if (creditCardOwnershipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditCardOwnershipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditCardOwnershipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CreditCardOwnershipDTO result = creditCardOwnershipService.save(creditCardOwnershipDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, creditCardOwnershipDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /credit-card-ownerships/:id} : Partial updates given fields of an existing creditCardOwnership, field will ignore if it is null
     *
     * @param id the id of the creditCardOwnershipDTO to save.
     * @param creditCardOwnershipDTO the creditCardOwnershipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditCardOwnershipDTO,
     * or with status {@code 400 (Bad Request)} if the creditCardOwnershipDTO is not valid,
     * or with status {@code 404 (Not Found)} if the creditCardOwnershipDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the creditCardOwnershipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/credit-card-ownerships/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CreditCardOwnershipDTO> partialUpdateCreditCardOwnership(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CreditCardOwnershipDTO creditCardOwnershipDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CreditCardOwnership partially : {}, {}", id, creditCardOwnershipDTO);
        if (creditCardOwnershipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditCardOwnershipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditCardOwnershipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CreditCardOwnershipDTO> result = creditCardOwnershipService.partialUpdate(creditCardOwnershipDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, creditCardOwnershipDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /credit-card-ownerships} : get all the creditCardOwnerships.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of creditCardOwnerships in body.
     */
    @GetMapping("/credit-card-ownerships")
    public ResponseEntity<List<CreditCardOwnershipDTO>> getAllCreditCardOwnerships(
        CreditCardOwnershipCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CreditCardOwnerships by criteria: {}", criteria);
        Page<CreditCardOwnershipDTO> page = creditCardOwnershipQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /credit-card-ownerships/count} : count all the creditCardOwnerships.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/credit-card-ownerships/count")
    public ResponseEntity<Long> countCreditCardOwnerships(CreditCardOwnershipCriteria criteria) {
        log.debug("REST request to count CreditCardOwnerships by criteria: {}", criteria);
        return ResponseEntity.ok().body(creditCardOwnershipQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /credit-card-ownerships/:id} : get the "id" creditCardOwnership.
     *
     * @param id the id of the creditCardOwnershipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the creditCardOwnershipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/credit-card-ownerships/{id}")
    public ResponseEntity<CreditCardOwnershipDTO> getCreditCardOwnership(@PathVariable Long id) {
        log.debug("REST request to get CreditCardOwnership : {}", id);
        Optional<CreditCardOwnershipDTO> creditCardOwnershipDTO = creditCardOwnershipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(creditCardOwnershipDTO);
    }

    /**
     * {@code DELETE  /credit-card-ownerships/:id} : delete the "id" creditCardOwnership.
     *
     * @param id the id of the creditCardOwnershipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/credit-card-ownerships/{id}")
    public ResponseEntity<Void> deleteCreditCardOwnership(@PathVariable Long id) {
        log.debug("REST request to delete CreditCardOwnership : {}", id);
        creditCardOwnershipService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/credit-card-ownerships?query=:query} : search for the creditCardOwnership corresponding
     * to the query.
     *
     * @param query the query of the creditCardOwnership search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/credit-card-ownerships")
    public ResponseEntity<List<CreditCardOwnershipDTO>> searchCreditCardOwnerships(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CreditCardOwnerships for query {}", query);
        Page<CreditCardOwnershipDTO> page = creditCardOwnershipService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
