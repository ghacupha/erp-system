package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.repository.LegalStatusRepository;
import io.github.erp.service.LegalStatusQueryService;
import io.github.erp.service.LegalStatusService;
import io.github.erp.service.criteria.LegalStatusCriteria;
import io.github.erp.service.dto.LegalStatusDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LegalStatus}.
 */
@RestController
@RequestMapping("/api")
public class LegalStatusResource {

    private final Logger log = LoggerFactory.getLogger(LegalStatusResource.class);

    private static final String ENTITY_NAME = "legalStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LegalStatusService legalStatusService;

    private final LegalStatusRepository legalStatusRepository;

    private final LegalStatusQueryService legalStatusQueryService;

    public LegalStatusResource(
        LegalStatusService legalStatusService,
        LegalStatusRepository legalStatusRepository,
        LegalStatusQueryService legalStatusQueryService
    ) {
        this.legalStatusService = legalStatusService;
        this.legalStatusRepository = legalStatusRepository;
        this.legalStatusQueryService = legalStatusQueryService;
    }

    /**
     * {@code POST  /legal-statuses} : Create a new legalStatus.
     *
     * @param legalStatusDTO the legalStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new legalStatusDTO, or with status {@code 400 (Bad Request)} if the legalStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/legal-statuses")
    public ResponseEntity<LegalStatusDTO> createLegalStatus(@Valid @RequestBody LegalStatusDTO legalStatusDTO) throws URISyntaxException {
        log.debug("REST request to save LegalStatus : {}", legalStatusDTO);
        if (legalStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new legalStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LegalStatusDTO result = legalStatusService.save(legalStatusDTO);
        return ResponseEntity
            .created(new URI("/api/legal-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /legal-statuses/:id} : Updates an existing legalStatus.
     *
     * @param id the id of the legalStatusDTO to save.
     * @param legalStatusDTO the legalStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated legalStatusDTO,
     * or with status {@code 400 (Bad Request)} if the legalStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the legalStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/legal-statuses/{id}")
    public ResponseEntity<LegalStatusDTO> updateLegalStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LegalStatusDTO legalStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LegalStatus : {}, {}", id, legalStatusDTO);
        if (legalStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, legalStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!legalStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LegalStatusDTO result = legalStatusService.save(legalStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, legalStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /legal-statuses/:id} : Partial updates given fields of an existing legalStatus, field will ignore if it is null
     *
     * @param id the id of the legalStatusDTO to save.
     * @param legalStatusDTO the legalStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated legalStatusDTO,
     * or with status {@code 400 (Bad Request)} if the legalStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the legalStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the legalStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/legal-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LegalStatusDTO> partialUpdateLegalStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LegalStatusDTO legalStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LegalStatus partially : {}, {}", id, legalStatusDTO);
        if (legalStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, legalStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!legalStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LegalStatusDTO> result = legalStatusService.partialUpdate(legalStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, legalStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /legal-statuses} : get all the legalStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of legalStatuses in body.
     */
    @GetMapping("/legal-statuses")
    public ResponseEntity<List<LegalStatusDTO>> getAllLegalStatuses(LegalStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LegalStatuses by criteria: {}", criteria);
        Page<LegalStatusDTO> page = legalStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /legal-statuses/count} : count all the legalStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/legal-statuses/count")
    public ResponseEntity<Long> countLegalStatuses(LegalStatusCriteria criteria) {
        log.debug("REST request to count LegalStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(legalStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /legal-statuses/:id} : get the "id" legalStatus.
     *
     * @param id the id of the legalStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the legalStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/legal-statuses/{id}")
    public ResponseEntity<LegalStatusDTO> getLegalStatus(@PathVariable Long id) {
        log.debug("REST request to get LegalStatus : {}", id);
        Optional<LegalStatusDTO> legalStatusDTO = legalStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(legalStatusDTO);
    }

    /**
     * {@code DELETE  /legal-statuses/:id} : delete the "id" legalStatus.
     *
     * @param id the id of the legalStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/legal-statuses/{id}")
    public ResponseEntity<Void> deleteLegalStatus(@PathVariable Long id) {
        log.debug("REST request to delete LegalStatus : {}", id);
        legalStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/legal-statuses?query=:query} : search for the legalStatus corresponding
     * to the query.
     *
     * @param query the query of the legalStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/legal-statuses")
    public ResponseEntity<List<LegalStatusDTO>> searchLegalStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LegalStatuses for query {}", query);
        Page<LegalStatusDTO> page = legalStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
