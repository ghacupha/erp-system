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

import io.github.erp.repository.InstitutionCodeRepository;
import io.github.erp.service.InstitutionCodeQueryService;
import io.github.erp.service.InstitutionCodeService;
import io.github.erp.service.criteria.InstitutionCodeCriteria;
import io.github.erp.service.dto.InstitutionCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.InstitutionCode}.
 */
@RestController
@RequestMapping("/api")
public class InstitutionCodeResource {

    private final Logger log = LoggerFactory.getLogger(InstitutionCodeResource.class);

    private static final String ENTITY_NAME = "institutionCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InstitutionCodeService institutionCodeService;

    private final InstitutionCodeRepository institutionCodeRepository;

    private final InstitutionCodeQueryService institutionCodeQueryService;

    public InstitutionCodeResource(
        InstitutionCodeService institutionCodeService,
        InstitutionCodeRepository institutionCodeRepository,
        InstitutionCodeQueryService institutionCodeQueryService
    ) {
        this.institutionCodeService = institutionCodeService;
        this.institutionCodeRepository = institutionCodeRepository;
        this.institutionCodeQueryService = institutionCodeQueryService;
    }

    /**
     * {@code POST  /institution-codes} : Create a new institutionCode.
     *
     * @param institutionCodeDTO the institutionCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new institutionCodeDTO, or with status {@code 400 (Bad Request)} if the institutionCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/institution-codes")
    public ResponseEntity<InstitutionCodeDTO> createInstitutionCode(@Valid @RequestBody InstitutionCodeDTO institutionCodeDTO)
        throws URISyntaxException {
        log.debug("REST request to save InstitutionCode : {}", institutionCodeDTO);
        if (institutionCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new institutionCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InstitutionCodeDTO result = institutionCodeService.save(institutionCodeDTO);
        return ResponseEntity
            .created(new URI("/api/institution-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /institution-codes/:id} : Updates an existing institutionCode.
     *
     * @param id the id of the institutionCodeDTO to save.
     * @param institutionCodeDTO the institutionCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institutionCodeDTO,
     * or with status {@code 400 (Bad Request)} if the institutionCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the institutionCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/institution-codes/{id}")
    public ResponseEntity<InstitutionCodeDTO> updateInstitutionCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InstitutionCodeDTO institutionCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InstitutionCode : {}, {}", id, institutionCodeDTO);
        if (institutionCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institutionCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!institutionCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InstitutionCodeDTO result = institutionCodeService.save(institutionCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, institutionCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /institution-codes/:id} : Partial updates given fields of an existing institutionCode, field will ignore if it is null
     *
     * @param id the id of the institutionCodeDTO to save.
     * @param institutionCodeDTO the institutionCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated institutionCodeDTO,
     * or with status {@code 400 (Bad Request)} if the institutionCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the institutionCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the institutionCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/institution-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InstitutionCodeDTO> partialUpdateInstitutionCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InstitutionCodeDTO institutionCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InstitutionCode partially : {}, {}", id, institutionCodeDTO);
        if (institutionCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, institutionCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!institutionCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InstitutionCodeDTO> result = institutionCodeService.partialUpdate(institutionCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, institutionCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /institution-codes} : get all the institutionCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of institutionCodes in body.
     */
    @GetMapping("/institution-codes")
    public ResponseEntity<List<InstitutionCodeDTO>> getAllInstitutionCodes(InstitutionCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InstitutionCodes by criteria: {}", criteria);
        Page<InstitutionCodeDTO> page = institutionCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /institution-codes/count} : count all the institutionCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/institution-codes/count")
    public ResponseEntity<Long> countInstitutionCodes(InstitutionCodeCriteria criteria) {
        log.debug("REST request to count InstitutionCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(institutionCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /institution-codes/:id} : get the "id" institutionCode.
     *
     * @param id the id of the institutionCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the institutionCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/institution-codes/{id}")
    public ResponseEntity<InstitutionCodeDTO> getInstitutionCode(@PathVariable Long id) {
        log.debug("REST request to get InstitutionCode : {}", id);
        Optional<InstitutionCodeDTO> institutionCodeDTO = institutionCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(institutionCodeDTO);
    }

    /**
     * {@code DELETE  /institution-codes/:id} : delete the "id" institutionCode.
     *
     * @param id the id of the institutionCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/institution-codes/{id}")
    public ResponseEntity<Void> deleteInstitutionCode(@PathVariable Long id) {
        log.debug("REST request to delete InstitutionCode : {}", id);
        institutionCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/institution-codes?query=:query} : search for the institutionCode corresponding
     * to the query.
     *
     * @param query the query of the institutionCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/institution-codes")
    public ResponseEntity<List<InstitutionCodeDTO>> searchInstitutionCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InstitutionCodes for query {}", query);
        Page<InstitutionCodeDTO> page = institutionCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
