package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.5
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

import io.github.erp.repository.DeliveryNoteRepository;
import io.github.erp.service.DeliveryNoteQueryService;
import io.github.erp.service.DeliveryNoteService;
import io.github.erp.service.criteria.DeliveryNoteCriteria;
import io.github.erp.service.dto.DeliveryNoteDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DeliveryNote}.
 */
@RestController
@RequestMapping("/api")
public class DeliveryNoteResource {

    private final Logger log = LoggerFactory.getLogger(DeliveryNoteResource.class);

    private static final String ENTITY_NAME = "deliveryNote";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeliveryNoteService deliveryNoteService;

    private final DeliveryNoteRepository deliveryNoteRepository;

    private final DeliveryNoteQueryService deliveryNoteQueryService;

    public DeliveryNoteResource(
        DeliveryNoteService deliveryNoteService,
        DeliveryNoteRepository deliveryNoteRepository,
        DeliveryNoteQueryService deliveryNoteQueryService
    ) {
        this.deliveryNoteService = deliveryNoteService;
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.deliveryNoteQueryService = deliveryNoteQueryService;
    }

    /**
     * {@code POST  /delivery-notes} : Create a new deliveryNote.
     *
     * @param deliveryNoteDTO the deliveryNoteDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deliveryNoteDTO, or with status {@code 400 (Bad Request)} if the deliveryNote has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/delivery-notes")
    public ResponseEntity<DeliveryNoteDTO> createDeliveryNote(@Valid @RequestBody DeliveryNoteDTO deliveryNoteDTO)
        throws URISyntaxException {
        log.debug("REST request to save DeliveryNote : {}", deliveryNoteDTO);
        if (deliveryNoteDTO.getId() != null) {
            throw new BadRequestAlertException("A new deliveryNote cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeliveryNoteDTO result = deliveryNoteService.save(deliveryNoteDTO);
        return ResponseEntity
            .created(new URI("/api/delivery-notes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /delivery-notes/:id} : Updates an existing deliveryNote.
     *
     * @param id the id of the deliveryNoteDTO to save.
     * @param deliveryNoteDTO the deliveryNoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryNoteDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryNoteDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deliveryNoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/delivery-notes/{id}")
    public ResponseEntity<DeliveryNoteDTO> updateDeliveryNote(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DeliveryNoteDTO deliveryNoteDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DeliveryNote : {}, {}", id, deliveryNoteDTO);
        if (deliveryNoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryNoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DeliveryNoteDTO result = deliveryNoteService.save(deliveryNoteDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deliveryNoteDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /delivery-notes/:id} : Partial updates given fields of an existing deliveryNote, field will ignore if it is null
     *
     * @param id the id of the deliveryNoteDTO to save.
     * @param deliveryNoteDTO the deliveryNoteDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deliveryNoteDTO,
     * or with status {@code 400 (Bad Request)} if the deliveryNoteDTO is not valid,
     * or with status {@code 404 (Not Found)} if the deliveryNoteDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the deliveryNoteDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/delivery-notes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DeliveryNoteDTO> partialUpdateDeliveryNote(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DeliveryNoteDTO deliveryNoteDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DeliveryNote partially : {}, {}", id, deliveryNoteDTO);
        if (deliveryNoteDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, deliveryNoteDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!deliveryNoteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DeliveryNoteDTO> result = deliveryNoteService.partialUpdate(deliveryNoteDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, deliveryNoteDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /delivery-notes} : get all the deliveryNotes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deliveryNotes in body.
     */
    @GetMapping("/delivery-notes")
    public ResponseEntity<List<DeliveryNoteDTO>> getAllDeliveryNotes(DeliveryNoteCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DeliveryNotes by criteria: {}", criteria);
        Page<DeliveryNoteDTO> page = deliveryNoteQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /delivery-notes/count} : count all the deliveryNotes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/delivery-notes/count")
    public ResponseEntity<Long> countDeliveryNotes(DeliveryNoteCriteria criteria) {
        log.debug("REST request to count DeliveryNotes by criteria: {}", criteria);
        return ResponseEntity.ok().body(deliveryNoteQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /delivery-notes/:id} : get the "id" deliveryNote.
     *
     * @param id the id of the deliveryNoteDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deliveryNoteDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/delivery-notes/{id}")
    public ResponseEntity<DeliveryNoteDTO> getDeliveryNote(@PathVariable Long id) {
        log.debug("REST request to get DeliveryNote : {}", id);
        Optional<DeliveryNoteDTO> deliveryNoteDTO = deliveryNoteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(deliveryNoteDTO);
    }

    /**
     * {@code DELETE  /delivery-notes/:id} : delete the "id" deliveryNote.
     *
     * @param id the id of the deliveryNoteDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/delivery-notes/{id}")
    public ResponseEntity<Void> deleteDeliveryNote(@PathVariable Long id) {
        log.debug("REST request to delete DeliveryNote : {}", id);
        deliveryNoteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/delivery-notes?query=:query} : search for the deliveryNote corresponding
     * to the query.
     *
     * @param query the query of the deliveryNote search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/delivery-notes")
    public ResponseEntity<List<DeliveryNoteDTO>> searchDeliveryNotes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DeliveryNotes for query {}", query);
        Page<DeliveryNoteDTO> page = deliveryNoteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
