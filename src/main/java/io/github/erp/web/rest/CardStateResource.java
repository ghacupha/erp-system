package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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

import io.github.erp.repository.CardStateRepository;
import io.github.erp.service.CardStateQueryService;
import io.github.erp.service.CardStateService;
import io.github.erp.service.criteria.CardStateCriteria;
import io.github.erp.service.dto.CardStateDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardState}.
 */
@RestController
@RequestMapping("/api")
public class CardStateResource {

    private final Logger log = LoggerFactory.getLogger(CardStateResource.class);

    private static final String ENTITY_NAME = "gdiDataCardState";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardStateService cardStateService;

    private final CardStateRepository cardStateRepository;

    private final CardStateQueryService cardStateQueryService;

    public CardStateResource(
        CardStateService cardStateService,
        CardStateRepository cardStateRepository,
        CardStateQueryService cardStateQueryService
    ) {
        this.cardStateService = cardStateService;
        this.cardStateRepository = cardStateRepository;
        this.cardStateQueryService = cardStateQueryService;
    }

    /**
     * {@code POST  /card-states} : Create a new cardState.
     *
     * @param cardStateDTO the cardStateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardStateDTO, or with status {@code 400 (Bad Request)} if the cardState has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-states")
    public ResponseEntity<CardStateDTO> createCardState(@Valid @RequestBody CardStateDTO cardStateDTO) throws URISyntaxException {
        log.debug("REST request to save CardState : {}", cardStateDTO);
        if (cardStateDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardState cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardStateDTO result = cardStateService.save(cardStateDTO);
        return ResponseEntity
            .created(new URI("/api/card-states/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-states/:id} : Updates an existing cardState.
     *
     * @param id the id of the cardStateDTO to save.
     * @param cardStateDTO the cardStateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardStateDTO,
     * or with status {@code 400 (Bad Request)} if the cardStateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardStateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-states/{id}")
    public ResponseEntity<CardStateDTO> updateCardState(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardStateDTO cardStateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardState : {}, {}", id, cardStateDTO);
        if (cardStateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardStateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardStateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardStateDTO result = cardStateService.save(cardStateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardStateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-states/:id} : Partial updates given fields of an existing cardState, field will ignore if it is null
     *
     * @param id the id of the cardStateDTO to save.
     * @param cardStateDTO the cardStateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardStateDTO,
     * or with status {@code 400 (Bad Request)} if the cardStateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardStateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardStateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-states/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardStateDTO> partialUpdateCardState(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardStateDTO cardStateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardState partially : {}, {}", id, cardStateDTO);
        if (cardStateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardStateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardStateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardStateDTO> result = cardStateService.partialUpdate(cardStateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardStateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-states} : get all the cardStates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardStates in body.
     */
    @GetMapping("/card-states")
    public ResponseEntity<List<CardStateDTO>> getAllCardStates(CardStateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CardStates by criteria: {}", criteria);
        Page<CardStateDTO> page = cardStateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-states/count} : count all the cardStates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-states/count")
    public ResponseEntity<Long> countCardStates(CardStateCriteria criteria) {
        log.debug("REST request to count CardStates by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardStateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-states/:id} : get the "id" cardState.
     *
     * @param id the id of the cardStateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardStateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-states/{id}")
    public ResponseEntity<CardStateDTO> getCardState(@PathVariable Long id) {
        log.debug("REST request to get CardState : {}", id);
        Optional<CardStateDTO> cardStateDTO = cardStateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardStateDTO);
    }

    /**
     * {@code DELETE  /card-states/:id} : delete the "id" cardState.
     *
     * @param id the id of the cardStateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-states/{id}")
    public ResponseEntity<Void> deleteCardState(@PathVariable Long id) {
        log.debug("REST request to delete CardState : {}", id);
        cardStateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-states?query=:query} : search for the cardState corresponding
     * to the query.
     *
     * @param query the query of the cardState search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-states")
    public ResponseEntity<List<CardStateDTO>> searchCardStates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardStates for query {}", query);
        Page<CardStateDTO> page = cardStateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
