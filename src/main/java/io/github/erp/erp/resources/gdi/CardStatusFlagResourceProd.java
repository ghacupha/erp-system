package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import io.github.erp.repository.CardStatusFlagRepository;
import io.github.erp.service.CardStatusFlagQueryService;
import io.github.erp.service.CardStatusFlagService;
import io.github.erp.service.criteria.CardStatusFlagCriteria;
import io.github.erp.service.dto.CardStatusFlagDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardStatusFlag}.
 */
@RestController("CardStatusFlagResourceProd")
@RequestMapping("/api/granular-data")
public class CardStatusFlagResourceProd {

    private final Logger log = LoggerFactory.getLogger(CardStatusFlagResourceProd.class);

    private static final String ENTITY_NAME = "cardStatusFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardStatusFlagService cardStatusFlagService;

    private final CardStatusFlagRepository cardStatusFlagRepository;

    private final CardStatusFlagQueryService cardStatusFlagQueryService;

    public CardStatusFlagResourceProd(
        CardStatusFlagService cardStatusFlagService,
        CardStatusFlagRepository cardStatusFlagRepository,
        CardStatusFlagQueryService cardStatusFlagQueryService
    ) {
        this.cardStatusFlagService = cardStatusFlagService;
        this.cardStatusFlagRepository = cardStatusFlagRepository;
        this.cardStatusFlagQueryService = cardStatusFlagQueryService;
    }

    /**
     * {@code POST  /card-status-flags} : Create a new cardStatusFlag.
     *
     * @param cardStatusFlagDTO the cardStatusFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardStatusFlagDTO, or with status {@code 400 (Bad Request)} if the cardStatusFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-status-flags")
    public ResponseEntity<CardStatusFlagDTO> createCardStatusFlag(@Valid @RequestBody CardStatusFlagDTO cardStatusFlagDTO)
        throws URISyntaxException {
        log.debug("REST request to save CardStatusFlag : {}", cardStatusFlagDTO);
        if (cardStatusFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardStatusFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardStatusFlagDTO result = cardStatusFlagService.save(cardStatusFlagDTO);
        return ResponseEntity
            .created(new URI("/api/card-status-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-status-flags/:id} : Updates an existing cardStatusFlag.
     *
     * @param id the id of the cardStatusFlagDTO to save.
     * @param cardStatusFlagDTO the cardStatusFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardStatusFlagDTO,
     * or with status {@code 400 (Bad Request)} if the cardStatusFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardStatusFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-status-flags/{id}")
    public ResponseEntity<CardStatusFlagDTO> updateCardStatusFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardStatusFlagDTO cardStatusFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardStatusFlag : {}, {}", id, cardStatusFlagDTO);
        if (cardStatusFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardStatusFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardStatusFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardStatusFlagDTO result = cardStatusFlagService.save(cardStatusFlagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardStatusFlagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-status-flags/:id} : Partial updates given fields of an existing cardStatusFlag, field will ignore if it is null
     *
     * @param id the id of the cardStatusFlagDTO to save.
     * @param cardStatusFlagDTO the cardStatusFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardStatusFlagDTO,
     * or with status {@code 400 (Bad Request)} if the cardStatusFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardStatusFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardStatusFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-status-flags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardStatusFlagDTO> partialUpdateCardStatusFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardStatusFlagDTO cardStatusFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardStatusFlag partially : {}, {}", id, cardStatusFlagDTO);
        if (cardStatusFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardStatusFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardStatusFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardStatusFlagDTO> result = cardStatusFlagService.partialUpdate(cardStatusFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardStatusFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-status-flags} : get all the cardStatusFlags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardStatusFlags in body.
     */
    @GetMapping("/card-status-flags")
    public ResponseEntity<List<CardStatusFlagDTO>> getAllCardStatusFlags(CardStatusFlagCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CardStatusFlags by criteria: {}", criteria);
        Page<CardStatusFlagDTO> page = cardStatusFlagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-status-flags/count} : count all the cardStatusFlags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-status-flags/count")
    public ResponseEntity<Long> countCardStatusFlags(CardStatusFlagCriteria criteria) {
        log.debug("REST request to count CardStatusFlags by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardStatusFlagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-status-flags/:id} : get the "id" cardStatusFlag.
     *
     * @param id the id of the cardStatusFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardStatusFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-status-flags/{id}")
    public ResponseEntity<CardStatusFlagDTO> getCardStatusFlag(@PathVariable Long id) {
        log.debug("REST request to get CardStatusFlag : {}", id);
        Optional<CardStatusFlagDTO> cardStatusFlagDTO = cardStatusFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardStatusFlagDTO);
    }

    /**
     * {@code DELETE  /card-status-flags/:id} : delete the "id" cardStatusFlag.
     *
     * @param id the id of the cardStatusFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-status-flags/{id}")
    public ResponseEntity<Void> deleteCardStatusFlag(@PathVariable Long id) {
        log.debug("REST request to delete CardStatusFlag : {}", id);
        cardStatusFlagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-status-flags?query=:query} : search for the cardStatusFlag corresponding
     * to the query.
     *
     * @param query the query of the cardStatusFlag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-status-flags")
    public ResponseEntity<List<CardStatusFlagDTO>> searchCardStatusFlags(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardStatusFlags for query {}", query);
        Page<CardStatusFlagDTO> page = cardStatusFlagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
