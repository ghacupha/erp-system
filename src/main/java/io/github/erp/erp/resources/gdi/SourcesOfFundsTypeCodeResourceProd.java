package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import io.github.erp.repository.SourcesOfFundsTypeCodeRepository;
import io.github.erp.service.SourcesOfFundsTypeCodeQueryService;
import io.github.erp.service.SourcesOfFundsTypeCodeService;
import io.github.erp.service.criteria.SourcesOfFundsTypeCodeCriteria;
import io.github.erp.service.dto.SourcesOfFundsTypeCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SourcesOfFundsTypeCode}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class SourcesOfFundsTypeCodeResourceProd {

    private final Logger log = LoggerFactory.getLogger(SourcesOfFundsTypeCodeResourceProd.class);

    private static final String ENTITY_NAME = "sourcesOfFundsTypeCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourcesOfFundsTypeCodeService sourcesOfFundsTypeCodeService;

    private final SourcesOfFundsTypeCodeRepository sourcesOfFundsTypeCodeRepository;

    private final SourcesOfFundsTypeCodeQueryService sourcesOfFundsTypeCodeQueryService;

    public SourcesOfFundsTypeCodeResourceProd(
        SourcesOfFundsTypeCodeService sourcesOfFundsTypeCodeService,
        SourcesOfFundsTypeCodeRepository sourcesOfFundsTypeCodeRepository,
        SourcesOfFundsTypeCodeQueryService sourcesOfFundsTypeCodeQueryService
    ) {
        this.sourcesOfFundsTypeCodeService = sourcesOfFundsTypeCodeService;
        this.sourcesOfFundsTypeCodeRepository = sourcesOfFundsTypeCodeRepository;
        this.sourcesOfFundsTypeCodeQueryService = sourcesOfFundsTypeCodeQueryService;
    }

    /**
     * {@code POST  /sources-of-funds-type-codes} : Create a new sourcesOfFundsTypeCode.
     *
     * @param sourcesOfFundsTypeCodeDTO the sourcesOfFundsTypeCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourcesOfFundsTypeCodeDTO, or with status {@code 400 (Bad Request)} if the sourcesOfFundsTypeCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sources-of-funds-type-codes")
    public ResponseEntity<SourcesOfFundsTypeCodeDTO> createSourcesOfFundsTypeCode(
        @Valid @RequestBody SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SourcesOfFundsTypeCode : {}", sourcesOfFundsTypeCodeDTO);
        if (sourcesOfFundsTypeCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new sourcesOfFundsTypeCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SourcesOfFundsTypeCodeDTO result = sourcesOfFundsTypeCodeService.save(sourcesOfFundsTypeCodeDTO);
        return ResponseEntity
            .created(new URI("/api/sources-of-funds-type-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sources-of-funds-type-codes/:id} : Updates an existing sourcesOfFundsTypeCode.
     *
     * @param id the id of the sourcesOfFundsTypeCodeDTO to save.
     * @param sourcesOfFundsTypeCodeDTO the sourcesOfFundsTypeCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourcesOfFundsTypeCodeDTO,
     * or with status {@code 400 (Bad Request)} if the sourcesOfFundsTypeCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourcesOfFundsTypeCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sources-of-funds-type-codes/{id}")
    public ResponseEntity<SourcesOfFundsTypeCodeDTO> updateSourcesOfFundsTypeCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SourcesOfFundsTypeCode : {}, {}", id, sourcesOfFundsTypeCodeDTO);
        if (sourcesOfFundsTypeCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourcesOfFundsTypeCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourcesOfFundsTypeCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SourcesOfFundsTypeCodeDTO result = sourcesOfFundsTypeCodeService.save(sourcesOfFundsTypeCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourcesOfFundsTypeCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sources-of-funds-type-codes/:id} : Partial updates given fields of an existing sourcesOfFundsTypeCode, field will ignore if it is null
     *
     * @param id the id of the sourcesOfFundsTypeCodeDTO to save.
     * @param sourcesOfFundsTypeCodeDTO the sourcesOfFundsTypeCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourcesOfFundsTypeCodeDTO,
     * or with status {@code 400 (Bad Request)} if the sourcesOfFundsTypeCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sourcesOfFundsTypeCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sourcesOfFundsTypeCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sources-of-funds-type-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SourcesOfFundsTypeCodeDTO> partialUpdateSourcesOfFundsTypeCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SourcesOfFundsTypeCodeDTO sourcesOfFundsTypeCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SourcesOfFundsTypeCode partially : {}, {}", id, sourcesOfFundsTypeCodeDTO);
        if (sourcesOfFundsTypeCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourcesOfFundsTypeCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourcesOfFundsTypeCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SourcesOfFundsTypeCodeDTO> result = sourcesOfFundsTypeCodeService.partialUpdate(sourcesOfFundsTypeCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourcesOfFundsTypeCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sources-of-funds-type-codes} : get all the sourcesOfFundsTypeCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourcesOfFundsTypeCodes in body.
     */
    @GetMapping("/sources-of-funds-type-codes")
    public ResponseEntity<List<SourcesOfFundsTypeCodeDTO>> getAllSourcesOfFundsTypeCodes(
        SourcesOfFundsTypeCodeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get SourcesOfFundsTypeCodes by criteria: {}", criteria);
        Page<SourcesOfFundsTypeCodeDTO> page = sourcesOfFundsTypeCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sources-of-funds-type-codes/count} : count all the sourcesOfFundsTypeCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sources-of-funds-type-codes/count")
    public ResponseEntity<Long> countSourcesOfFundsTypeCodes(SourcesOfFundsTypeCodeCriteria criteria) {
        log.debug("REST request to count SourcesOfFundsTypeCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(sourcesOfFundsTypeCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sources-of-funds-type-codes/:id} : get the "id" sourcesOfFundsTypeCode.
     *
     * @param id the id of the sourcesOfFundsTypeCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourcesOfFundsTypeCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sources-of-funds-type-codes/{id}")
    public ResponseEntity<SourcesOfFundsTypeCodeDTO> getSourcesOfFundsTypeCode(@PathVariable Long id) {
        log.debug("REST request to get SourcesOfFundsTypeCode : {}", id);
        Optional<SourcesOfFundsTypeCodeDTO> sourcesOfFundsTypeCodeDTO = sourcesOfFundsTypeCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourcesOfFundsTypeCodeDTO);
    }

    /**
     * {@code DELETE  /sources-of-funds-type-codes/:id} : delete the "id" sourcesOfFundsTypeCode.
     *
     * @param id the id of the sourcesOfFundsTypeCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sources-of-funds-type-codes/{id}")
    public ResponseEntity<Void> deleteSourcesOfFundsTypeCode(@PathVariable Long id) {
        log.debug("REST request to delete SourcesOfFundsTypeCode : {}", id);
        sourcesOfFundsTypeCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/sources-of-funds-type-codes?query=:query} : search for the sourcesOfFundsTypeCode corresponding
     * to the query.
     *
     * @param query the query of the sourcesOfFundsTypeCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/sources-of-funds-type-codes")
    public ResponseEntity<List<SourcesOfFundsTypeCodeDTO>> searchSourcesOfFundsTypeCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SourcesOfFundsTypeCodes for query {}", query);
        Page<SourcesOfFundsTypeCodeDTO> page = sourcesOfFundsTypeCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
