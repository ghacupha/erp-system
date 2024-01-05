package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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

import io.github.erp.repository.CardCategoryTypeRepository;
import io.github.erp.service.CardCategoryTypeQueryService;
import io.github.erp.service.CardCategoryTypeService;
import io.github.erp.service.criteria.CardCategoryTypeCriteria;
import io.github.erp.service.dto.CardCategoryTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardCategoryType}.
 */
@RestController
@RequestMapping("/api")
public class CardCategoryTypeResource {

    private final Logger log = LoggerFactory.getLogger(CardCategoryTypeResource.class);

    private static final String ENTITY_NAME = "cardCategoryType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardCategoryTypeService cardCategoryTypeService;

    private final CardCategoryTypeRepository cardCategoryTypeRepository;

    private final CardCategoryTypeQueryService cardCategoryTypeQueryService;

    public CardCategoryTypeResource(
        CardCategoryTypeService cardCategoryTypeService,
        CardCategoryTypeRepository cardCategoryTypeRepository,
        CardCategoryTypeQueryService cardCategoryTypeQueryService
    ) {
        this.cardCategoryTypeService = cardCategoryTypeService;
        this.cardCategoryTypeRepository = cardCategoryTypeRepository;
        this.cardCategoryTypeQueryService = cardCategoryTypeQueryService;
    }

    /**
     * {@code POST  /card-category-types} : Create a new cardCategoryType.
     *
     * @param cardCategoryTypeDTO the cardCategoryTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardCategoryTypeDTO, or with status {@code 400 (Bad Request)} if the cardCategoryType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-category-types")
    public ResponseEntity<CardCategoryTypeDTO> createCardCategoryType(@Valid @RequestBody CardCategoryTypeDTO cardCategoryTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CardCategoryType : {}", cardCategoryTypeDTO);
        if (cardCategoryTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardCategoryType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardCategoryTypeDTO result = cardCategoryTypeService.save(cardCategoryTypeDTO);
        return ResponseEntity
            .created(new URI("/api/card-category-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-category-types/:id} : Updates an existing cardCategoryType.
     *
     * @param id the id of the cardCategoryTypeDTO to save.
     * @param cardCategoryTypeDTO the cardCategoryTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardCategoryTypeDTO,
     * or with status {@code 400 (Bad Request)} if the cardCategoryTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardCategoryTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-category-types/{id}")
    public ResponseEntity<CardCategoryTypeDTO> updateCardCategoryType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardCategoryTypeDTO cardCategoryTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardCategoryType : {}, {}", id, cardCategoryTypeDTO);
        if (cardCategoryTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardCategoryTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardCategoryTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardCategoryTypeDTO result = cardCategoryTypeService.save(cardCategoryTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardCategoryTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-category-types/:id} : Partial updates given fields of an existing cardCategoryType, field will ignore if it is null
     *
     * @param id the id of the cardCategoryTypeDTO to save.
     * @param cardCategoryTypeDTO the cardCategoryTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardCategoryTypeDTO,
     * or with status {@code 400 (Bad Request)} if the cardCategoryTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardCategoryTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardCategoryTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-category-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardCategoryTypeDTO> partialUpdateCardCategoryType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardCategoryTypeDTO cardCategoryTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardCategoryType partially : {}, {}", id, cardCategoryTypeDTO);
        if (cardCategoryTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardCategoryTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardCategoryTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardCategoryTypeDTO> result = cardCategoryTypeService.partialUpdate(cardCategoryTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardCategoryTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-category-types} : get all the cardCategoryTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardCategoryTypes in body.
     */
    @GetMapping("/card-category-types")
    public ResponseEntity<List<CardCategoryTypeDTO>> getAllCardCategoryTypes(CardCategoryTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CardCategoryTypes by criteria: {}", criteria);
        Page<CardCategoryTypeDTO> page = cardCategoryTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-category-types/count} : count all the cardCategoryTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-category-types/count")
    public ResponseEntity<Long> countCardCategoryTypes(CardCategoryTypeCriteria criteria) {
        log.debug("REST request to count CardCategoryTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardCategoryTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-category-types/:id} : get the "id" cardCategoryType.
     *
     * @param id the id of the cardCategoryTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardCategoryTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-category-types/{id}")
    public ResponseEntity<CardCategoryTypeDTO> getCardCategoryType(@PathVariable Long id) {
        log.debug("REST request to get CardCategoryType : {}", id);
        Optional<CardCategoryTypeDTO> cardCategoryTypeDTO = cardCategoryTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardCategoryTypeDTO);
    }

    /**
     * {@code DELETE  /card-category-types/:id} : delete the "id" cardCategoryType.
     *
     * @param id the id of the cardCategoryTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-category-types/{id}")
    public ResponseEntity<Void> deleteCardCategoryType(@PathVariable Long id) {
        log.debug("REST request to delete CardCategoryType : {}", id);
        cardCategoryTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-category-types?query=:query} : search for the cardCategoryType corresponding
     * to the query.
     *
     * @param query the query of the cardCategoryType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-category-types")
    public ResponseEntity<List<CardCategoryTypeDTO>> searchCardCategoryTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardCategoryTypes for query {}", query);
        Page<CardCategoryTypeDTO> page = cardCategoryTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
