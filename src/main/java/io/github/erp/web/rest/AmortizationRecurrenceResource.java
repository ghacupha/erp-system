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

import io.github.erp.repository.AmortizationRecurrenceRepository;
import io.github.erp.service.AmortizationRecurrenceQueryService;
import io.github.erp.service.AmortizationRecurrenceService;
import io.github.erp.service.criteria.AmortizationRecurrenceCriteria;
import io.github.erp.service.dto.AmortizationRecurrenceDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AmortizationRecurrence}.
 */
@RestController
@RequestMapping("/api")
public class AmortizationRecurrenceResource {

    private final Logger log = LoggerFactory.getLogger(AmortizationRecurrenceResource.class);

    private static final String ENTITY_NAME = "amortizationRecurrence";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AmortizationRecurrenceService amortizationRecurrenceService;

    private final AmortizationRecurrenceRepository amortizationRecurrenceRepository;

    private final AmortizationRecurrenceQueryService amortizationRecurrenceQueryService;

    public AmortizationRecurrenceResource(
        AmortizationRecurrenceService amortizationRecurrenceService,
        AmortizationRecurrenceRepository amortizationRecurrenceRepository,
        AmortizationRecurrenceQueryService amortizationRecurrenceQueryService
    ) {
        this.amortizationRecurrenceService = amortizationRecurrenceService;
        this.amortizationRecurrenceRepository = amortizationRecurrenceRepository;
        this.amortizationRecurrenceQueryService = amortizationRecurrenceQueryService;
    }

    /**
     * {@code POST  /amortization-recurrences} : Create a new amortizationRecurrence.
     *
     * @param amortizationRecurrenceDTO the amortizationRecurrenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new amortizationRecurrenceDTO, or with status {@code 400 (Bad Request)} if the amortizationRecurrence has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/amortization-recurrences")
    public ResponseEntity<AmortizationRecurrenceDTO> createAmortizationRecurrence(
        @Valid @RequestBody AmortizationRecurrenceDTO amortizationRecurrenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AmortizationRecurrence : {}", amortizationRecurrenceDTO);
        if (amortizationRecurrenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new amortizationRecurrence cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AmortizationRecurrenceDTO result = amortizationRecurrenceService.save(amortizationRecurrenceDTO);
        return ResponseEntity
            .created(new URI("/api/amortization-recurrences/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /amortization-recurrences/:id} : Updates an existing amortizationRecurrence.
     *
     * @param id the id of the amortizationRecurrenceDTO to save.
     * @param amortizationRecurrenceDTO the amortizationRecurrenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amortizationRecurrenceDTO,
     * or with status {@code 400 (Bad Request)} if the amortizationRecurrenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the amortizationRecurrenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/amortization-recurrences/{id}")
    public ResponseEntity<AmortizationRecurrenceDTO> updateAmortizationRecurrence(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AmortizationRecurrenceDTO amortizationRecurrenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AmortizationRecurrence : {}, {}", id, amortizationRecurrenceDTO);
        if (amortizationRecurrenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amortizationRecurrenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amortizationRecurrenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AmortizationRecurrenceDTO result = amortizationRecurrenceService.save(amortizationRecurrenceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amortizationRecurrenceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /amortization-recurrences/:id} : Partial updates given fields of an existing amortizationRecurrence, field will ignore if it is null
     *
     * @param id the id of the amortizationRecurrenceDTO to save.
     * @param amortizationRecurrenceDTO the amortizationRecurrenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amortizationRecurrenceDTO,
     * or with status {@code 400 (Bad Request)} if the amortizationRecurrenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the amortizationRecurrenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the amortizationRecurrenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/amortization-recurrences/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AmortizationRecurrenceDTO> partialUpdateAmortizationRecurrence(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AmortizationRecurrenceDTO amortizationRecurrenceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AmortizationRecurrence partially : {}, {}", id, amortizationRecurrenceDTO);
        if (amortizationRecurrenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amortizationRecurrenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amortizationRecurrenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AmortizationRecurrenceDTO> result = amortizationRecurrenceService.partialUpdate(amortizationRecurrenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amortizationRecurrenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /amortization-recurrences} : get all the amortizationRecurrences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amortizationRecurrences in body.
     */
    @GetMapping("/amortization-recurrences")
    public ResponseEntity<List<AmortizationRecurrenceDTO>> getAllAmortizationRecurrences(
        AmortizationRecurrenceCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AmortizationRecurrences by criteria: {}", criteria);
        Page<AmortizationRecurrenceDTO> page = amortizationRecurrenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /amortization-recurrences/count} : count all the amortizationRecurrences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/amortization-recurrences/count")
    public ResponseEntity<Long> countAmortizationRecurrences(AmortizationRecurrenceCriteria criteria) {
        log.debug("REST request to count AmortizationRecurrences by criteria: {}", criteria);
        return ResponseEntity.ok().body(amortizationRecurrenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /amortization-recurrences/:id} : get the "id" amortizationRecurrence.
     *
     * @param id the id of the amortizationRecurrenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amortizationRecurrenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amortization-recurrences/{id}")
    public ResponseEntity<AmortizationRecurrenceDTO> getAmortizationRecurrence(@PathVariable Long id) {
        log.debug("REST request to get AmortizationRecurrence : {}", id);
        Optional<AmortizationRecurrenceDTO> amortizationRecurrenceDTO = amortizationRecurrenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(amortizationRecurrenceDTO);
    }

    /**
     * {@code DELETE  /amortization-recurrences/:id} : delete the "id" amortizationRecurrence.
     *
     * @param id the id of the amortizationRecurrenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/amortization-recurrences/{id}")
    public ResponseEntity<Void> deleteAmortizationRecurrence(@PathVariable Long id) {
        log.debug("REST request to delete AmortizationRecurrence : {}", id);
        amortizationRecurrenceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/amortization-recurrences?query=:query} : search for the amortizationRecurrence corresponding
     * to the query.
     *
     * @param query the query of the amortizationRecurrence search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/amortization-recurrences")
    public ResponseEntity<List<AmortizationRecurrenceDTO>> searchAmortizationRecurrences(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AmortizationRecurrences for query {}", query);
        Page<AmortizationRecurrenceDTO> page = amortizationRecurrenceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
