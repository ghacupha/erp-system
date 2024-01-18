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

import io.github.erp.repository.CardTypesRepository;
import io.github.erp.service.CardTypesQueryService;
import io.github.erp.service.CardTypesService;
import io.github.erp.service.criteria.CardTypesCriteria;
import io.github.erp.service.dto.CardTypesDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardTypes}.
 */
@RestController
@RequestMapping("/api")
public class CardTypesResource {

    private final Logger log = LoggerFactory.getLogger(CardTypesResource.class);

    private static final String ENTITY_NAME = "cardTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardTypesService cardTypesService;

    private final CardTypesRepository cardTypesRepository;

    private final CardTypesQueryService cardTypesQueryService;

    public CardTypesResource(
        CardTypesService cardTypesService,
        CardTypesRepository cardTypesRepository,
        CardTypesQueryService cardTypesQueryService
    ) {
        this.cardTypesService = cardTypesService;
        this.cardTypesRepository = cardTypesRepository;
        this.cardTypesQueryService = cardTypesQueryService;
    }

    /**
     * {@code POST  /card-types} : Create a new cardTypes.
     *
     * @param cardTypesDTO the cardTypesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardTypesDTO, or with status {@code 400 (Bad Request)} if the cardTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-types")
    public ResponseEntity<CardTypesDTO> createCardTypes(@Valid @RequestBody CardTypesDTO cardTypesDTO) throws URISyntaxException {
        log.debug("REST request to save CardTypes : {}", cardTypesDTO);
        if (cardTypesDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardTypesDTO result = cardTypesService.save(cardTypesDTO);
        return ResponseEntity
            .created(new URI("/api/card-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-types/:id} : Updates an existing cardTypes.
     *
     * @param id the id of the cardTypesDTO to save.
     * @param cardTypesDTO the cardTypesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardTypesDTO,
     * or with status {@code 400 (Bad Request)} if the cardTypesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardTypesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-types/{id}")
    public ResponseEntity<CardTypesDTO> updateCardTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardTypesDTO cardTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardTypes : {}, {}", id, cardTypesDTO);
        if (cardTypesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardTypesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardTypesDTO result = cardTypesService.save(cardTypesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardTypesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-types/:id} : Partial updates given fields of an existing cardTypes, field will ignore if it is null
     *
     * @param id the id of the cardTypesDTO to save.
     * @param cardTypesDTO the cardTypesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardTypesDTO,
     * or with status {@code 400 (Bad Request)} if the cardTypesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardTypesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardTypesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardTypesDTO> partialUpdateCardTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardTypesDTO cardTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardTypes partially : {}, {}", id, cardTypesDTO);
        if (cardTypesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardTypesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardTypesDTO> result = cardTypesService.partialUpdate(cardTypesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardTypesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-types} : get all the cardTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardTypes in body.
     */
    @GetMapping("/card-types")
    public ResponseEntity<List<CardTypesDTO>> getAllCardTypes(CardTypesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CardTypes by criteria: {}", criteria);
        Page<CardTypesDTO> page = cardTypesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-types/count} : count all the cardTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-types/count")
    public ResponseEntity<Long> countCardTypes(CardTypesCriteria criteria) {
        log.debug("REST request to count CardTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardTypesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-types/:id} : get the "id" cardTypes.
     *
     * @param id the id of the cardTypesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardTypesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-types/{id}")
    public ResponseEntity<CardTypesDTO> getCardTypes(@PathVariable Long id) {
        log.debug("REST request to get CardTypes : {}", id);
        Optional<CardTypesDTO> cardTypesDTO = cardTypesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardTypesDTO);
    }

    /**
     * {@code DELETE  /card-types/:id} : delete the "id" cardTypes.
     *
     * @param id the id of the cardTypesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-types/{id}")
    public ResponseEntity<Void> deleteCardTypes(@PathVariable Long id) {
        log.debug("REST request to delete CardTypes : {}", id);
        cardTypesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-types?query=:query} : search for the cardTypes corresponding
     * to the query.
     *
     * @param query the query of the cardTypes search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-types")
    public ResponseEntity<List<CardTypesDTO>> searchCardTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardTypes for query {}", query);
        Page<CardTypesDTO> page = cardTypesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
