package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.AmortizationSequenceRepository;
import io.github.erp.service.AmortizationSequenceQueryService;
import io.github.erp.service.AmortizationSequenceService;
import io.github.erp.service.criteria.AmortizationSequenceCriteria;
import io.github.erp.service.dto.AmortizationSequenceDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AmortizationSequence}.
 */
@RestController
@RequestMapping("/api")
public class AmortizationSequenceResource {

    private final Logger log = LoggerFactory.getLogger(AmortizationSequenceResource.class);

    private static final String ENTITY_NAME = "amortizationSequence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AmortizationSequenceService amortizationSequenceService;

    private final AmortizationSequenceRepository amortizationSequenceRepository;

    private final AmortizationSequenceQueryService amortizationSequenceQueryService;

    public AmortizationSequenceResource(
        AmortizationSequenceService amortizationSequenceService,
        AmortizationSequenceRepository amortizationSequenceRepository,
        AmortizationSequenceQueryService amortizationSequenceQueryService
    ) {
        this.amortizationSequenceService = amortizationSequenceService;
        this.amortizationSequenceRepository = amortizationSequenceRepository;
        this.amortizationSequenceQueryService = amortizationSequenceQueryService;
    }

    /**
     * {@code POST  /amortization-sequences} : Create a new amortizationSequence.
     *
     * @param amortizationSequenceDTO the amortizationSequenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new amortizationSequenceDTO, or with status {@code 400 (Bad Request)} if the amortizationSequence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/amortization-sequences")
    public ResponseEntity<AmortizationSequenceDTO> createAmortizationSequence(
        @Valid @RequestBody AmortizationSequenceDTO amortizationSequenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AmortizationSequence : {}", amortizationSequenceDTO);
        if (amortizationSequenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new amortizationSequence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AmortizationSequenceDTO result = amortizationSequenceService.save(amortizationSequenceDTO);
        return ResponseEntity
            .created(new URI("/api/amortization-sequences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /amortization-sequences/:id} : Updates an existing amortizationSequence.
     *
     * @param id the id of the amortizationSequenceDTO to save.
     * @param amortizationSequenceDTO the amortizationSequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amortizationSequenceDTO,
     * or with status {@code 400 (Bad Request)} if the amortizationSequenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the amortizationSequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/amortization-sequences/{id}")
    public ResponseEntity<AmortizationSequenceDTO> updateAmortizationSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AmortizationSequenceDTO amortizationSequenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AmortizationSequence : {}, {}", id, amortizationSequenceDTO);
        if (amortizationSequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amortizationSequenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amortizationSequenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AmortizationSequenceDTO result = amortizationSequenceService.save(amortizationSequenceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amortizationSequenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /amortization-sequences/:id} : Partial updates given fields of an existing amortizationSequence, field will ignore if it is null
     *
     * @param id the id of the amortizationSequenceDTO to save.
     * @param amortizationSequenceDTO the amortizationSequenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amortizationSequenceDTO,
     * or with status {@code 400 (Bad Request)} if the amortizationSequenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the amortizationSequenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the amortizationSequenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/amortization-sequences/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AmortizationSequenceDTO> partialUpdateAmortizationSequence(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AmortizationSequenceDTO amortizationSequenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AmortizationSequence partially : {}, {}", id, amortizationSequenceDTO);
        if (amortizationSequenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amortizationSequenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amortizationSequenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AmortizationSequenceDTO> result = amortizationSequenceService.partialUpdate(amortizationSequenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amortizationSequenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /amortization-sequences} : get all the amortizationSequences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amortizationSequences in body.
     */
    @GetMapping("/amortization-sequences")
    public ResponseEntity<List<AmortizationSequenceDTO>> getAllAmortizationSequences(
        AmortizationSequenceCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AmortizationSequences by criteria: {}", criteria);
        Page<AmortizationSequenceDTO> page = amortizationSequenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /amortization-sequences/count} : count all the amortizationSequences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/amortization-sequences/count")
    public ResponseEntity<Long> countAmortizationSequences(AmortizationSequenceCriteria criteria) {
        log.debug("REST request to count AmortizationSequences by criteria: {}", criteria);
        return ResponseEntity.ok().body(amortizationSequenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /amortization-sequences/:id} : get the "id" amortizationSequence.
     *
     * @param id the id of the amortizationSequenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amortizationSequenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amortization-sequences/{id}")
    public ResponseEntity<AmortizationSequenceDTO> getAmortizationSequence(@PathVariable Long id) {
        log.debug("REST request to get AmortizationSequence : {}", id);
        Optional<AmortizationSequenceDTO> amortizationSequenceDTO = amortizationSequenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(amortizationSequenceDTO);
    }

    /**
     * {@code DELETE  /amortization-sequences/:id} : delete the "id" amortizationSequence.
     *
     * @param id the id of the amortizationSequenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/amortization-sequences/{id}")
    public ResponseEntity<Void> deleteAmortizationSequence(@PathVariable Long id) {
        log.debug("REST request to delete AmortizationSequence : {}", id);
        amortizationSequenceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/amortization-sequences?query=:query} : search for the amortizationSequence corresponding
     * to the query.
     *
     * @param query the query of the amortizationSequence search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/amortization-sequences")
    public ResponseEntity<List<AmortizationSequenceDTO>> searchAmortizationSequences(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AmortizationSequences for query {}", query);
        Page<AmortizationSequenceDTO> page = amortizationSequenceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
