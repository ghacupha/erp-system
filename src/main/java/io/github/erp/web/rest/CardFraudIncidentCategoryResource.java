package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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

import io.github.erp.repository.CardFraudIncidentCategoryRepository;
import io.github.erp.service.CardFraudIncidentCategoryQueryService;
import io.github.erp.service.CardFraudIncidentCategoryService;
import io.github.erp.service.criteria.CardFraudIncidentCategoryCriteria;
import io.github.erp.service.dto.CardFraudIncidentCategoryDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardFraudIncidentCategory}.
 */
@RestController
@RequestMapping("/api")
public class CardFraudIncidentCategoryResource {

    private final Logger log = LoggerFactory.getLogger(CardFraudIncidentCategoryResource.class);

    private static final String ENTITY_NAME = "cardFraudIncidentCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardFraudIncidentCategoryService cardFraudIncidentCategoryService;

    private final CardFraudIncidentCategoryRepository cardFraudIncidentCategoryRepository;

    private final CardFraudIncidentCategoryQueryService cardFraudIncidentCategoryQueryService;

    public CardFraudIncidentCategoryResource(
        CardFraudIncidentCategoryService cardFraudIncidentCategoryService,
        CardFraudIncidentCategoryRepository cardFraudIncidentCategoryRepository,
        CardFraudIncidentCategoryQueryService cardFraudIncidentCategoryQueryService
    ) {
        this.cardFraudIncidentCategoryService = cardFraudIncidentCategoryService;
        this.cardFraudIncidentCategoryRepository = cardFraudIncidentCategoryRepository;
        this.cardFraudIncidentCategoryQueryService = cardFraudIncidentCategoryQueryService;
    }

    /**
     * {@code POST  /card-fraud-incident-categories} : Create a new cardFraudIncidentCategory.
     *
     * @param cardFraudIncidentCategoryDTO the cardFraudIncidentCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardFraudIncidentCategoryDTO, or with status {@code 400 (Bad Request)} if the cardFraudIncidentCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-fraud-incident-categories")
    public ResponseEntity<CardFraudIncidentCategoryDTO> createCardFraudIncidentCategory(
        @Valid @RequestBody CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CardFraudIncidentCategory : {}", cardFraudIncidentCategoryDTO);
        if (cardFraudIncidentCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardFraudIncidentCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardFraudIncidentCategoryDTO result = cardFraudIncidentCategoryService.save(cardFraudIncidentCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/card-fraud-incident-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-fraud-incident-categories/:id} : Updates an existing cardFraudIncidentCategory.
     *
     * @param id the id of the cardFraudIncidentCategoryDTO to save.
     * @param cardFraudIncidentCategoryDTO the cardFraudIncidentCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardFraudIncidentCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the cardFraudIncidentCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardFraudIncidentCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-fraud-incident-categories/{id}")
    public ResponseEntity<CardFraudIncidentCategoryDTO> updateCardFraudIncidentCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardFraudIncidentCategory : {}, {}", id, cardFraudIncidentCategoryDTO);
        if (cardFraudIncidentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardFraudIncidentCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardFraudIncidentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardFraudIncidentCategoryDTO result = cardFraudIncidentCategoryService.save(cardFraudIncidentCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardFraudIncidentCategoryDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /card-fraud-incident-categories/:id} : Partial updates given fields of an existing cardFraudIncidentCategory, field will ignore if it is null
     *
     * @param id the id of the cardFraudIncidentCategoryDTO to save.
     * @param cardFraudIncidentCategoryDTO the cardFraudIncidentCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardFraudIncidentCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the cardFraudIncidentCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardFraudIncidentCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardFraudIncidentCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-fraud-incident-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardFraudIncidentCategoryDTO> partialUpdateCardFraudIncidentCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardFraudIncidentCategoryDTO cardFraudIncidentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardFraudIncidentCategory partially : {}, {}", id, cardFraudIncidentCategoryDTO);
        if (cardFraudIncidentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardFraudIncidentCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardFraudIncidentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardFraudIncidentCategoryDTO> result = cardFraudIncidentCategoryService.partialUpdate(cardFraudIncidentCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardFraudIncidentCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-fraud-incident-categories} : get all the cardFraudIncidentCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardFraudIncidentCategories in body.
     */
    @GetMapping("/card-fraud-incident-categories")
    public ResponseEntity<List<CardFraudIncidentCategoryDTO>> getAllCardFraudIncidentCategories(
        CardFraudIncidentCategoryCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CardFraudIncidentCategories by criteria: {}", criteria);
        Page<CardFraudIncidentCategoryDTO> page = cardFraudIncidentCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-fraud-incident-categories/count} : count all the cardFraudIncidentCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-fraud-incident-categories/count")
    public ResponseEntity<Long> countCardFraudIncidentCategories(CardFraudIncidentCategoryCriteria criteria) {
        log.debug("REST request to count CardFraudIncidentCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardFraudIncidentCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-fraud-incident-categories/:id} : get the "id" cardFraudIncidentCategory.
     *
     * @param id the id of the cardFraudIncidentCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardFraudIncidentCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-fraud-incident-categories/{id}")
    public ResponseEntity<CardFraudIncidentCategoryDTO> getCardFraudIncidentCategory(@PathVariable Long id) {
        log.debug("REST request to get CardFraudIncidentCategory : {}", id);
        Optional<CardFraudIncidentCategoryDTO> cardFraudIncidentCategoryDTO = cardFraudIncidentCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardFraudIncidentCategoryDTO);
    }

    /**
     * {@code DELETE  /card-fraud-incident-categories/:id} : delete the "id" cardFraudIncidentCategory.
     *
     * @param id the id of the cardFraudIncidentCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-fraud-incident-categories/{id}")
    public ResponseEntity<Void> deleteCardFraudIncidentCategory(@PathVariable Long id) {
        log.debug("REST request to delete CardFraudIncidentCategory : {}", id);
        cardFraudIncidentCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-fraud-incident-categories?query=:query} : search for the cardFraudIncidentCategory corresponding
     * to the query.
     *
     * @param query the query of the cardFraudIncidentCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-fraud-incident-categories")
    public ResponseEntity<List<CardFraudIncidentCategoryDTO>> searchCardFraudIncidentCategories(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CardFraudIncidentCategories for query {}", query);
        Page<CardFraudIncidentCategoryDTO> page = cardFraudIncidentCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
