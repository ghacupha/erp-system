package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.repository.CrbDataSubmittingInstitutionsRepository;
import io.github.erp.service.CrbDataSubmittingInstitutionsQueryService;
import io.github.erp.service.CrbDataSubmittingInstitutionsService;
import io.github.erp.service.criteria.CrbDataSubmittingInstitutionsCriteria;
import io.github.erp.service.dto.CrbDataSubmittingInstitutionsDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbDataSubmittingInstitutions}.
 */
@RestController
@RequestMapping("/api")
public class CrbDataSubmittingInstitutionsResource {

    private final Logger log = LoggerFactory.getLogger(CrbDataSubmittingInstitutionsResource.class);

    private static final String ENTITY_NAME = "crbDataSubmittingInstitutions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbDataSubmittingInstitutionsService crbDataSubmittingInstitutionsService;

    private final CrbDataSubmittingInstitutionsRepository crbDataSubmittingInstitutionsRepository;

    private final CrbDataSubmittingInstitutionsQueryService crbDataSubmittingInstitutionsQueryService;

    public CrbDataSubmittingInstitutionsResource(
        CrbDataSubmittingInstitutionsService crbDataSubmittingInstitutionsService,
        CrbDataSubmittingInstitutionsRepository crbDataSubmittingInstitutionsRepository,
        CrbDataSubmittingInstitutionsQueryService crbDataSubmittingInstitutionsQueryService
    ) {
        this.crbDataSubmittingInstitutionsService = crbDataSubmittingInstitutionsService;
        this.crbDataSubmittingInstitutionsRepository = crbDataSubmittingInstitutionsRepository;
        this.crbDataSubmittingInstitutionsQueryService = crbDataSubmittingInstitutionsQueryService;
    }

    /**
     * {@code POST  /crb-data-submitting-institutions} : Create a new crbDataSubmittingInstitutions.
     *
     * @param crbDataSubmittingInstitutionsDTO the crbDataSubmittingInstitutionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbDataSubmittingInstitutionsDTO, or with status {@code 400 (Bad Request)} if the crbDataSubmittingInstitutions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-data-submitting-institutions")
    public ResponseEntity<CrbDataSubmittingInstitutionsDTO> createCrbDataSubmittingInstitutions(
        @Valid @RequestBody CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbDataSubmittingInstitutions : {}", crbDataSubmittingInstitutionsDTO);
        if (crbDataSubmittingInstitutionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbDataSubmittingInstitutions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbDataSubmittingInstitutionsDTO result = crbDataSubmittingInstitutionsService.save(crbDataSubmittingInstitutionsDTO);
        return ResponseEntity
            .created(new URI("/api/crb-data-submitting-institutions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-data-submitting-institutions/:id} : Updates an existing crbDataSubmittingInstitutions.
     *
     * @param id the id of the crbDataSubmittingInstitutionsDTO to save.
     * @param crbDataSubmittingInstitutionsDTO the crbDataSubmittingInstitutionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbDataSubmittingInstitutionsDTO,
     * or with status {@code 400 (Bad Request)} if the crbDataSubmittingInstitutionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbDataSubmittingInstitutionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-data-submitting-institutions/{id}")
    public ResponseEntity<CrbDataSubmittingInstitutionsDTO> updateCrbDataSubmittingInstitutions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbDataSubmittingInstitutions : {}, {}", id, crbDataSubmittingInstitutionsDTO);
        if (crbDataSubmittingInstitutionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbDataSubmittingInstitutionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbDataSubmittingInstitutionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbDataSubmittingInstitutionsDTO result = crbDataSubmittingInstitutionsService.save(crbDataSubmittingInstitutionsDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbDataSubmittingInstitutionsDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /crb-data-submitting-institutions/:id} : Partial updates given fields of an existing crbDataSubmittingInstitutions, field will ignore if it is null
     *
     * @param id the id of the crbDataSubmittingInstitutionsDTO to save.
     * @param crbDataSubmittingInstitutionsDTO the crbDataSubmittingInstitutionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbDataSubmittingInstitutionsDTO,
     * or with status {@code 400 (Bad Request)} if the crbDataSubmittingInstitutionsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbDataSubmittingInstitutionsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbDataSubmittingInstitutionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-data-submitting-institutions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbDataSubmittingInstitutionsDTO> partialUpdateCrbDataSubmittingInstitutions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbDataSubmittingInstitutionsDTO crbDataSubmittingInstitutionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbDataSubmittingInstitutions partially : {}, {}", id, crbDataSubmittingInstitutionsDTO);
        if (crbDataSubmittingInstitutionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbDataSubmittingInstitutionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbDataSubmittingInstitutionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbDataSubmittingInstitutionsDTO> result = crbDataSubmittingInstitutionsService.partialUpdate(
            crbDataSubmittingInstitutionsDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbDataSubmittingInstitutionsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-data-submitting-institutions} : get all the crbDataSubmittingInstitutions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbDataSubmittingInstitutions in body.
     */
    @GetMapping("/crb-data-submitting-institutions")
    public ResponseEntity<List<CrbDataSubmittingInstitutionsDTO>> getAllCrbDataSubmittingInstitutions(
        CrbDataSubmittingInstitutionsCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbDataSubmittingInstitutions by criteria: {}", criteria);
        Page<CrbDataSubmittingInstitutionsDTO> page = crbDataSubmittingInstitutionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-data-submitting-institutions/count} : count all the crbDataSubmittingInstitutions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-data-submitting-institutions/count")
    public ResponseEntity<Long> countCrbDataSubmittingInstitutions(CrbDataSubmittingInstitutionsCriteria criteria) {
        log.debug("REST request to count CrbDataSubmittingInstitutions by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbDataSubmittingInstitutionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-data-submitting-institutions/:id} : get the "id" crbDataSubmittingInstitutions.
     *
     * @param id the id of the crbDataSubmittingInstitutionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbDataSubmittingInstitutionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-data-submitting-institutions/{id}")
    public ResponseEntity<CrbDataSubmittingInstitutionsDTO> getCrbDataSubmittingInstitutions(@PathVariable Long id) {
        log.debug("REST request to get CrbDataSubmittingInstitutions : {}", id);
        Optional<CrbDataSubmittingInstitutionsDTO> crbDataSubmittingInstitutionsDTO = crbDataSubmittingInstitutionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbDataSubmittingInstitutionsDTO);
    }

    /**
     * {@code DELETE  /crb-data-submitting-institutions/:id} : delete the "id" crbDataSubmittingInstitutions.
     *
     * @param id the id of the crbDataSubmittingInstitutionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-data-submitting-institutions/{id}")
    public ResponseEntity<Void> deleteCrbDataSubmittingInstitutions(@PathVariable Long id) {
        log.debug("REST request to delete CrbDataSubmittingInstitutions : {}", id);
        crbDataSubmittingInstitutionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-data-submitting-institutions?query=:query} : search for the crbDataSubmittingInstitutions corresponding
     * to the query.
     *
     * @param query the query of the crbDataSubmittingInstitutions search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-data-submitting-institutions")
    public ResponseEntity<List<CrbDataSubmittingInstitutionsDTO>> searchCrbDataSubmittingInstitutions(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CrbDataSubmittingInstitutions for query {}", query);
        Page<CrbDataSubmittingInstitutionsDTO> page = crbDataSubmittingInstitutionsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
