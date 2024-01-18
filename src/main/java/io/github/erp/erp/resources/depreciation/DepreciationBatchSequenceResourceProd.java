package io.github.erp.erp.resources.depreciation;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.repository.DepreciationBatchSequenceRepository;
import io.github.erp.service.DepreciationBatchSequenceQueryService;
import io.github.erp.service.DepreciationBatchSequenceService;
import io.github.erp.service.criteria.DepreciationBatchSequenceCriteria;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.DepreciationBatchSequence}.
 */
@RestController("depreciationBatchSequenceResourceProd")
@RequestMapping("/api/fixed-asset")
public class DepreciationBatchSequenceResourceProd {

    private final Logger log = LoggerFactory.getLogger(DepreciationBatchSequenceResourceProd.class);

    private static final String ENTITY_NAME = "depreciationBatchSequence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepreciationBatchSequenceService depreciationBatchSequenceService;

    private final DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;

    private final DepreciationBatchSequenceQueryService depreciationBatchSequenceQueryService;

    public DepreciationBatchSequenceResourceProd(
        DepreciationBatchSequenceService depreciationBatchSequenceService,
        DepreciationBatchSequenceRepository depreciationBatchSequenceRepository,
        DepreciationBatchSequenceQueryService depreciationBatchSequenceQueryService
    ) {
        this.depreciationBatchSequenceService = depreciationBatchSequenceService;
        this.depreciationBatchSequenceRepository = depreciationBatchSequenceRepository;
        this.depreciationBatchSequenceQueryService = depreciationBatchSequenceQueryService;
    }

    /**
     * {@code POST  /depreciation-batch-sequences} : Create a new depreciationBatchSequence.
     *
     * @param depreciationBatchSequenceDTO the depreciationBatchSequenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depreciationBatchSequenceDTO, or with status {@code 400 (Bad Request)} if the depreciationBatchSequence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depreciation-batch-sequences")
    public ResponseEntity<DepreciationBatchSequenceDTO> createDepreciationBatchSequence(
        @RequestBody DepreciationBatchSequenceDTO depreciationBatchSequenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to save DepreciationBatchSequence : {}", depreciationBatchSequenceDTO);
        if (depreciationBatchSequenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new depreciationBatchSequence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepreciationBatchSequenceDTO result = depreciationBatchSequenceService.save(depreciationBatchSequenceDTO);
        return ResponseEntity
            .created(new URI("/api/depreciation-batch-sequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depreciation-batch-sequences/:id} : Updates an existing depreciationBatchSequence.
     *
     * @param id the id of the depreciationBatchSequenceDTO to save.
     * @param depreciationBatchSequenceDTO the depreciationBatchSequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationBatchSequenceDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationBatchSequenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depreciationBatchSequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depreciation-batch-sequences/{id}")
    public ResponseEntity<DepreciationBatchSequenceDTO> updateDepreciationBatchSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepreciationBatchSequenceDTO depreciationBatchSequenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DepreciationBatchSequence : {}, {}", id, depreciationBatchSequenceDTO);
        if (depreciationBatchSequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationBatchSequenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationBatchSequenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepreciationBatchSequenceDTO result = depreciationBatchSequenceService.save(depreciationBatchSequenceDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, depreciationBatchSequenceDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /depreciation-batch-sequences/:id} : Partial updates given fields of an existing depreciationBatchSequence, field will ignore if it is null
     *
     * @param id the id of the depreciationBatchSequenceDTO to save.
     * @param depreciationBatchSequenceDTO the depreciationBatchSequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationBatchSequenceDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationBatchSequenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depreciationBatchSequenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depreciationBatchSequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depreciation-batch-sequences/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepreciationBatchSequenceDTO> partialUpdateDepreciationBatchSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody DepreciationBatchSequenceDTO depreciationBatchSequenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DepreciationBatchSequence partially : {}, {}", id, depreciationBatchSequenceDTO);
        if (depreciationBatchSequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationBatchSequenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationBatchSequenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepreciationBatchSequenceDTO> result = depreciationBatchSequenceService.partialUpdate(depreciationBatchSequenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, depreciationBatchSequenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depreciation-batch-sequences} : get all the depreciationBatchSequences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depreciationBatchSequences in body.
     */
    @GetMapping("/depreciation-batch-sequences")
    public ResponseEntity<List<DepreciationBatchSequenceDTO>> getAllDepreciationBatchSequences(
        DepreciationBatchSequenceCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get DepreciationBatchSequences by criteria: {}", criteria);
        Page<DepreciationBatchSequenceDTO> page = depreciationBatchSequenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depreciation-batch-sequences/count} : count all the depreciationBatchSequences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/depreciation-batch-sequences/count")
    public ResponseEntity<Long> countDepreciationBatchSequences(DepreciationBatchSequenceCriteria criteria) {
        log.debug("REST request to count DepreciationBatchSequences by criteria: {}", criteria);
        return ResponseEntity.ok().body(depreciationBatchSequenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /depreciation-batch-sequences/:id} : get the "id" depreciationBatchSequence.
     *
     * @param id the id of the depreciationBatchSequenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depreciationBatchSequenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depreciation-batch-sequences/{id}")
    public ResponseEntity<DepreciationBatchSequenceDTO> getDepreciationBatchSequence(@PathVariable Long id) {
        log.debug("REST request to get DepreciationBatchSequence : {}", id);
        Optional<DepreciationBatchSequenceDTO> depreciationBatchSequenceDTO = depreciationBatchSequenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depreciationBatchSequenceDTO);
    }

    /**
     * {@code DELETE  /depreciation-batch-sequences/:id} : delete the "id" depreciationBatchSequence.
     *
     * @param id the id of the depreciationBatchSequenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depreciation-batch-sequences/{id}")
    public ResponseEntity<Void> deleteDepreciationBatchSequence(@PathVariable Long id) {
        log.debug("REST request to delete DepreciationBatchSequence : {}", id);
        depreciationBatchSequenceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/depreciation-batch-sequences?query=:query} : search for the depreciationBatchSequence corresponding
     * to the query.
     *
     * @param query the query of the depreciationBatchSequence search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/depreciation-batch-sequences")
    public ResponseEntity<List<DepreciationBatchSequenceDTO>> searchDepreciationBatchSequences(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of DepreciationBatchSequences for query {}", query);
        Page<DepreciationBatchSequenceDTO> page = depreciationBatchSequenceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
