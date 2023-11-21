package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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

import io.github.erp.repository.CardIssuerChargesRepository;
import io.github.erp.service.CardIssuerChargesQueryService;
import io.github.erp.service.CardIssuerChargesService;
import io.github.erp.service.criteria.CardIssuerChargesCriteria;
import io.github.erp.service.dto.CardIssuerChargesDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardIssuerCharges}.
 */
@RestController
@RequestMapping("/api")
public class CardIssuerChargesResource {

    private final Logger log = LoggerFactory.getLogger(CardIssuerChargesResource.class);

    private static final String ENTITY_NAME = "gdiDataCardIssuerCharges";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardIssuerChargesService cardIssuerChargesService;

    private final CardIssuerChargesRepository cardIssuerChargesRepository;

    private final CardIssuerChargesQueryService cardIssuerChargesQueryService;

    public CardIssuerChargesResource(
        CardIssuerChargesService cardIssuerChargesService,
        CardIssuerChargesRepository cardIssuerChargesRepository,
        CardIssuerChargesQueryService cardIssuerChargesQueryService
    ) {
        this.cardIssuerChargesService = cardIssuerChargesService;
        this.cardIssuerChargesRepository = cardIssuerChargesRepository;
        this.cardIssuerChargesQueryService = cardIssuerChargesQueryService;
    }

    /**
     * {@code POST  /card-issuer-charges} : Create a new cardIssuerCharges.
     *
     * @param cardIssuerChargesDTO the cardIssuerChargesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardIssuerChargesDTO, or with status {@code 400 (Bad Request)} if the cardIssuerCharges has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-issuer-charges")
    public ResponseEntity<CardIssuerChargesDTO> createCardIssuerCharges(@Valid @RequestBody CardIssuerChargesDTO cardIssuerChargesDTO)
        throws URISyntaxException {
        log.debug("REST request to save CardIssuerCharges : {}", cardIssuerChargesDTO);
        if (cardIssuerChargesDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardIssuerCharges cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardIssuerChargesDTO result = cardIssuerChargesService.save(cardIssuerChargesDTO);
        return ResponseEntity
            .created(new URI("/api/card-issuer-charges/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-issuer-charges/:id} : Updates an existing cardIssuerCharges.
     *
     * @param id the id of the cardIssuerChargesDTO to save.
     * @param cardIssuerChargesDTO the cardIssuerChargesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardIssuerChargesDTO,
     * or with status {@code 400 (Bad Request)} if the cardIssuerChargesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardIssuerChargesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-issuer-charges/{id}")
    public ResponseEntity<CardIssuerChargesDTO> updateCardIssuerCharges(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardIssuerChargesDTO cardIssuerChargesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardIssuerCharges : {}, {}", id, cardIssuerChargesDTO);
        if (cardIssuerChargesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardIssuerChargesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardIssuerChargesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardIssuerChargesDTO result = cardIssuerChargesService.save(cardIssuerChargesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardIssuerChargesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-issuer-charges/:id} : Partial updates given fields of an existing cardIssuerCharges, field will ignore if it is null
     *
     * @param id the id of the cardIssuerChargesDTO to save.
     * @param cardIssuerChargesDTO the cardIssuerChargesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardIssuerChargesDTO,
     * or with status {@code 400 (Bad Request)} if the cardIssuerChargesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardIssuerChargesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardIssuerChargesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-issuer-charges/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardIssuerChargesDTO> partialUpdateCardIssuerCharges(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardIssuerChargesDTO cardIssuerChargesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardIssuerCharges partially : {}, {}", id, cardIssuerChargesDTO);
        if (cardIssuerChargesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardIssuerChargesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardIssuerChargesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardIssuerChargesDTO> result = cardIssuerChargesService.partialUpdate(cardIssuerChargesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardIssuerChargesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-issuer-charges} : get all the cardIssuerCharges.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardIssuerCharges in body.
     */
    @GetMapping("/card-issuer-charges")
    public ResponseEntity<List<CardIssuerChargesDTO>> getAllCardIssuerCharges(CardIssuerChargesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CardIssuerCharges by criteria: {}", criteria);
        Page<CardIssuerChargesDTO> page = cardIssuerChargesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-issuer-charges/count} : count all the cardIssuerCharges.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-issuer-charges/count")
    public ResponseEntity<Long> countCardIssuerCharges(CardIssuerChargesCriteria criteria) {
        log.debug("REST request to count CardIssuerCharges by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardIssuerChargesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-issuer-charges/:id} : get the "id" cardIssuerCharges.
     *
     * @param id the id of the cardIssuerChargesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardIssuerChargesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-issuer-charges/{id}")
    public ResponseEntity<CardIssuerChargesDTO> getCardIssuerCharges(@PathVariable Long id) {
        log.debug("REST request to get CardIssuerCharges : {}", id);
        Optional<CardIssuerChargesDTO> cardIssuerChargesDTO = cardIssuerChargesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardIssuerChargesDTO);
    }

    /**
     * {@code DELETE  /card-issuer-charges/:id} : delete the "id" cardIssuerCharges.
     *
     * @param id the id of the cardIssuerChargesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-issuer-charges/{id}")
    public ResponseEntity<Void> deleteCardIssuerCharges(@PathVariable Long id) {
        log.debug("REST request to delete CardIssuerCharges : {}", id);
        cardIssuerChargesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-issuer-charges?query=:query} : search for the cardIssuerCharges corresponding
     * to the query.
     *
     * @param query the query of the cardIssuerCharges search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-issuer-charges")
    public ResponseEntity<List<CardIssuerChargesDTO>> searchCardIssuerCharges(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardIssuerCharges for query {}", query);
        Page<CardIssuerChargesDTO> page = cardIssuerChargesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
