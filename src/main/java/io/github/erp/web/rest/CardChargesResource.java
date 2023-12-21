package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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

import io.github.erp.repository.CardChargesRepository;
import io.github.erp.service.CardChargesQueryService;
import io.github.erp.service.CardChargesService;
import io.github.erp.service.criteria.CardChargesCriteria;
import io.github.erp.service.dto.CardChargesDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardCharges}.
 */
@RestController
@RequestMapping("/api")
public class CardChargesResource {

    private final Logger log = LoggerFactory.getLogger(CardChargesResource.class);

    private static final String ENTITY_NAME = "cardCharges";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardChargesService cardChargesService;

    private final CardChargesRepository cardChargesRepository;

    private final CardChargesQueryService cardChargesQueryService;

    public CardChargesResource(
        CardChargesService cardChargesService,
        CardChargesRepository cardChargesRepository,
        CardChargesQueryService cardChargesQueryService
    ) {
        this.cardChargesService = cardChargesService;
        this.cardChargesRepository = cardChargesRepository;
        this.cardChargesQueryService = cardChargesQueryService;
    }

    /**
     * {@code POST  /card-charges} : Create a new cardCharges.
     *
     * @param cardChargesDTO the cardChargesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardChargesDTO, or with status {@code 400 (Bad Request)} if the cardCharges has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-charges")
    public ResponseEntity<CardChargesDTO> createCardCharges(@Valid @RequestBody CardChargesDTO cardChargesDTO) throws URISyntaxException {
        log.debug("REST request to save CardCharges : {}", cardChargesDTO);
        if (cardChargesDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardCharges cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardChargesDTO result = cardChargesService.save(cardChargesDTO);
        return ResponseEntity
            .created(new URI("/api/card-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-charges/:id} : Updates an existing cardCharges.
     *
     * @param id the id of the cardChargesDTO to save.
     * @param cardChargesDTO the cardChargesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardChargesDTO,
     * or with status {@code 400 (Bad Request)} if the cardChargesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardChargesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-charges/{id}")
    public ResponseEntity<CardChargesDTO> updateCardCharges(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardChargesDTO cardChargesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardCharges : {}, {}", id, cardChargesDTO);
        if (cardChargesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardChargesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardChargesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardChargesDTO result = cardChargesService.save(cardChargesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardChargesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-charges/:id} : Partial updates given fields of an existing cardCharges, field will ignore if it is null
     *
     * @param id the id of the cardChargesDTO to save.
     * @param cardChargesDTO the cardChargesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardChargesDTO,
     * or with status {@code 400 (Bad Request)} if the cardChargesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardChargesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardChargesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-charges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardChargesDTO> partialUpdateCardCharges(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardChargesDTO cardChargesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardCharges partially : {}, {}", id, cardChargesDTO);
        if (cardChargesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardChargesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardChargesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardChargesDTO> result = cardChargesService.partialUpdate(cardChargesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardChargesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-charges} : get all the cardCharges.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardCharges in body.
     */
    @GetMapping("/card-charges")
    public ResponseEntity<List<CardChargesDTO>> getAllCardCharges(CardChargesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CardCharges by criteria: {}", criteria);
        Page<CardChargesDTO> page = cardChargesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-charges/count} : count all the cardCharges.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-charges/count")
    public ResponseEntity<Long> countCardCharges(CardChargesCriteria criteria) {
        log.debug("REST request to count CardCharges by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardChargesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-charges/:id} : get the "id" cardCharges.
     *
     * @param id the id of the cardChargesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardChargesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-charges/{id}")
    public ResponseEntity<CardChargesDTO> getCardCharges(@PathVariable Long id) {
        log.debug("REST request to get CardCharges : {}", id);
        Optional<CardChargesDTO> cardChargesDTO = cardChargesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardChargesDTO);
    }

    /**
     * {@code DELETE  /card-charges/:id} : delete the "id" cardCharges.
     *
     * @param id the id of the cardChargesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-charges/{id}")
    public ResponseEntity<Void> deleteCardCharges(@PathVariable Long id) {
        log.debug("REST request to delete CardCharges : {}", id);
        cardChargesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-charges?query=:query} : search for the cardCharges corresponding
     * to the query.
     *
     * @param query the query of the cardCharges search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-charges")
    public ResponseEntity<List<CardChargesDTO>> searchCardCharges(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardCharges for query {}", query);
        Page<CardChargesDTO> page = cardChargesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
