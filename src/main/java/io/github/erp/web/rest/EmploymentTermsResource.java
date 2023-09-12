package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.EmploymentTermsRepository;
import io.github.erp.service.EmploymentTermsQueryService;
import io.github.erp.service.EmploymentTermsService;
import io.github.erp.service.criteria.EmploymentTermsCriteria;
import io.github.erp.service.dto.EmploymentTermsDTO;
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
 * REST controller for managing {@link io.github.erp.domain.EmploymentTerms}.
 */
@RestController
@RequestMapping("/api")
public class EmploymentTermsResource {

    private final Logger log = LoggerFactory.getLogger(EmploymentTermsResource.class);

    private static final String ENTITY_NAME = "employmentTerms";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final EmploymentTermsService employmentTermsService;

    private final EmploymentTermsRepository employmentTermsRepository;

    private final EmploymentTermsQueryService employmentTermsQueryService;

    public EmploymentTermsResource(
        EmploymentTermsService employmentTermsService,
        EmploymentTermsRepository employmentTermsRepository,
        EmploymentTermsQueryService employmentTermsQueryService
    ) {
        this.employmentTermsService = employmentTermsService;
        this.employmentTermsRepository = employmentTermsRepository;
        this.employmentTermsQueryService = employmentTermsQueryService;
    }

    /**
     * {@code POST  /employment-terms} : Create a new employmentTerms.
     *
     * @param employmentTermsDTO the employmentTermsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new employmentTermsDTO, or with status {@code 400 (Bad Request)} if the employmentTerms has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/employment-terms")
    public ResponseEntity<EmploymentTermsDTO> createEmploymentTerms(@Valid @RequestBody EmploymentTermsDTO employmentTermsDTO)
        throws URISyntaxException {
        log.debug("REST request to save EmploymentTerms : {}", employmentTermsDTO);
        if (employmentTermsDTO.getId() != null) {
            throw new BadRequestAlertException("A new employmentTerms cannot already have an ID", ENTITY_NAME, "idexists");
        }
        EmploymentTermsDTO result = employmentTermsService.save(employmentTermsDTO);
        return ResponseEntity
            .created(new URI("/api/employment-terms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /employment-terms/:id} : Updates an existing employmentTerms.
     *
     * @param id the id of the employmentTermsDTO to save.
     * @param employmentTermsDTO the employmentTermsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentTermsDTO,
     * or with status {@code 400 (Bad Request)} if the employmentTermsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the employmentTermsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/employment-terms/{id}")
    public ResponseEntity<EmploymentTermsDTO> updateEmploymentTerms(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody EmploymentTermsDTO employmentTermsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update EmploymentTerms : {}, {}", id, employmentTermsDTO);
        if (employmentTermsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employmentTermsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentTermsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        EmploymentTermsDTO result = employmentTermsService.save(employmentTermsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employmentTermsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /employment-terms/:id} : Partial updates given fields of an existing employmentTerms, field will ignore if it is null
     *
     * @param id the id of the employmentTermsDTO to save.
     * @param employmentTermsDTO the employmentTermsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated employmentTermsDTO,
     * or with status {@code 400 (Bad Request)} if the employmentTermsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the employmentTermsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the employmentTermsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/employment-terms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<EmploymentTermsDTO> partialUpdateEmploymentTerms(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody EmploymentTermsDTO employmentTermsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update EmploymentTerms partially : {}, {}", id, employmentTermsDTO);
        if (employmentTermsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, employmentTermsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!employmentTermsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<EmploymentTermsDTO> result = employmentTermsService.partialUpdate(employmentTermsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, employmentTermsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /employment-terms} : get all the employmentTerms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of employmentTerms in body.
     */
    @GetMapping("/employment-terms")
    public ResponseEntity<List<EmploymentTermsDTO>> getAllEmploymentTerms(EmploymentTermsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get EmploymentTerms by criteria: {}", criteria);
        Page<EmploymentTermsDTO> page = employmentTermsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /employment-terms/count} : count all the employmentTerms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/employment-terms/count")
    public ResponseEntity<Long> countEmploymentTerms(EmploymentTermsCriteria criteria) {
        log.debug("REST request to count EmploymentTerms by criteria: {}", criteria);
        return ResponseEntity.ok().body(employmentTermsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /employment-terms/:id} : get the "id" employmentTerms.
     *
     * @param id the id of the employmentTermsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the employmentTermsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/employment-terms/{id}")
    public ResponseEntity<EmploymentTermsDTO> getEmploymentTerms(@PathVariable Long id) {
        log.debug("REST request to get EmploymentTerms : {}", id);
        Optional<EmploymentTermsDTO> employmentTermsDTO = employmentTermsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(employmentTermsDTO);
    }

    /**
     * {@code DELETE  /employment-terms/:id} : delete the "id" employmentTerms.
     *
     * @param id the id of the employmentTermsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/employment-terms/{id}")
    public ResponseEntity<Void> deleteEmploymentTerms(@PathVariable Long id) {
        log.debug("REST request to delete EmploymentTerms : {}", id);
        employmentTermsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/employment-terms?query=:query} : search for the employmentTerms corresponding
     * to the query.
     *
     * @param query the query of the employmentTerms search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/employment-terms")
    public ResponseEntity<List<EmploymentTermsDTO>> searchEmploymentTerms(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of EmploymentTerms for query {}", query);
        Page<EmploymentTermsDTO> page = employmentTermsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
