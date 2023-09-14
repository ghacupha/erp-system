package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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

import io.github.erp.repository.AcquiringIssuingFlagRepository;
import io.github.erp.service.AcquiringIssuingFlagQueryService;
import io.github.erp.service.AcquiringIssuingFlagService;
import io.github.erp.service.criteria.AcquiringIssuingFlagCriteria;
import io.github.erp.service.dto.AcquiringIssuingFlagDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AcquiringIssuingFlag}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class AcquiringIssuingFlagResourceProd {

    private final Logger log = LoggerFactory.getLogger(AcquiringIssuingFlagResourceProd.class);

    private static final String ENTITY_NAME = "acquiringIssuingFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcquiringIssuingFlagService acquiringIssuingFlagService;

    private final AcquiringIssuingFlagRepository acquiringIssuingFlagRepository;

    private final AcquiringIssuingFlagQueryService acquiringIssuingFlagQueryService;

    public AcquiringIssuingFlagResourceProd(
        AcquiringIssuingFlagService acquiringIssuingFlagService,
        AcquiringIssuingFlagRepository acquiringIssuingFlagRepository,
        AcquiringIssuingFlagQueryService acquiringIssuingFlagQueryService
    ) {
        this.acquiringIssuingFlagService = acquiringIssuingFlagService;
        this.acquiringIssuingFlagRepository = acquiringIssuingFlagRepository;
        this.acquiringIssuingFlagQueryService = acquiringIssuingFlagQueryService;
    }

    /**
     * {@code POST  /acquiring-issuing-flags} : Create a new acquiringIssuingFlag.
     *
     * @param acquiringIssuingFlagDTO the acquiringIssuingFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new acquiringIssuingFlagDTO, or with status {@code 400 (Bad Request)} if the acquiringIssuingFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/acquiring-issuing-flags")
    public ResponseEntity<AcquiringIssuingFlagDTO> createAcquiringIssuingFlag(
        @Valid @RequestBody AcquiringIssuingFlagDTO acquiringIssuingFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AcquiringIssuingFlag : {}", acquiringIssuingFlagDTO);
        if (acquiringIssuingFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new acquiringIssuingFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcquiringIssuingFlagDTO result = acquiringIssuingFlagService.save(acquiringIssuingFlagDTO);
        return ResponseEntity
            .created(new URI("/api/acquiring-issuing-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /acquiring-issuing-flags/:id} : Updates an existing acquiringIssuingFlag.
     *
     * @param id the id of the acquiringIssuingFlagDTO to save.
     * @param acquiringIssuingFlagDTO the acquiringIssuingFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acquiringIssuingFlagDTO,
     * or with status {@code 400 (Bad Request)} if the acquiringIssuingFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the acquiringIssuingFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/acquiring-issuing-flags/{id}")
    public ResponseEntity<AcquiringIssuingFlagDTO> updateAcquiringIssuingFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AcquiringIssuingFlagDTO acquiringIssuingFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AcquiringIssuingFlag : {}, {}", id, acquiringIssuingFlagDTO);
        if (acquiringIssuingFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acquiringIssuingFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acquiringIssuingFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AcquiringIssuingFlagDTO result = acquiringIssuingFlagService.save(acquiringIssuingFlagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acquiringIssuingFlagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /acquiring-issuing-flags/:id} : Partial updates given fields of an existing acquiringIssuingFlag, field will ignore if it is null
     *
     * @param id the id of the acquiringIssuingFlagDTO to save.
     * @param acquiringIssuingFlagDTO the acquiringIssuingFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated acquiringIssuingFlagDTO,
     * or with status {@code 400 (Bad Request)} if the acquiringIssuingFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the acquiringIssuingFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the acquiringIssuingFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/acquiring-issuing-flags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AcquiringIssuingFlagDTO> partialUpdateAcquiringIssuingFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AcquiringIssuingFlagDTO acquiringIssuingFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AcquiringIssuingFlag partially : {}, {}", id, acquiringIssuingFlagDTO);
        if (acquiringIssuingFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, acquiringIssuingFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!acquiringIssuingFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcquiringIssuingFlagDTO> result = acquiringIssuingFlagService.partialUpdate(acquiringIssuingFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, acquiringIssuingFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /acquiring-issuing-flags} : get all the acquiringIssuingFlags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of acquiringIssuingFlags in body.
     */
    @GetMapping("/acquiring-issuing-flags")
    public ResponseEntity<List<AcquiringIssuingFlagDTO>> getAllAcquiringIssuingFlags(
        AcquiringIssuingFlagCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AcquiringIssuingFlags by criteria: {}", criteria);
        Page<AcquiringIssuingFlagDTO> page = acquiringIssuingFlagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /acquiring-issuing-flags/count} : count all the acquiringIssuingFlags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/acquiring-issuing-flags/count")
    public ResponseEntity<Long> countAcquiringIssuingFlags(AcquiringIssuingFlagCriteria criteria) {
        log.debug("REST request to count AcquiringIssuingFlags by criteria: {}", criteria);
        return ResponseEntity.ok().body(acquiringIssuingFlagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /acquiring-issuing-flags/:id} : get the "id" acquiringIssuingFlag.
     *
     * @param id the id of the acquiringIssuingFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the acquiringIssuingFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/acquiring-issuing-flags/{id}")
    public ResponseEntity<AcquiringIssuingFlagDTO> getAcquiringIssuingFlag(@PathVariable Long id) {
        log.debug("REST request to get AcquiringIssuingFlag : {}", id);
        Optional<AcquiringIssuingFlagDTO> acquiringIssuingFlagDTO = acquiringIssuingFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(acquiringIssuingFlagDTO);
    }

    /**
     * {@code DELETE  /acquiring-issuing-flags/:id} : delete the "id" acquiringIssuingFlag.
     *
     * @param id the id of the acquiringIssuingFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/acquiring-issuing-flags/{id}")
    public ResponseEntity<Void> deleteAcquiringIssuingFlag(@PathVariable Long id) {
        log.debug("REST request to delete AcquiringIssuingFlag : {}", id);
        acquiringIssuingFlagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/acquiring-issuing-flags?query=:query} : search for the acquiringIssuingFlag corresponding
     * to the query.
     *
     * @param query the query of the acquiringIssuingFlag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/acquiring-issuing-flags")
    public ResponseEntity<List<AcquiringIssuingFlagDTO>> searchAcquiringIssuingFlags(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AcquiringIssuingFlags for query {}", query);
        Page<AcquiringIssuingFlagDTO> page = acquiringIssuingFlagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
