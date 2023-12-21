package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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

import io.github.erp.repository.CardBrandTypeRepository;
import io.github.erp.service.CardBrandTypeQueryService;
import io.github.erp.service.CardBrandTypeService;
import io.github.erp.service.criteria.CardBrandTypeCriteria;
import io.github.erp.service.dto.CardBrandTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardBrandType}.
 */
@RestController
@RequestMapping("/api")
public class CardBrandTypeResource {

    private final Logger log = LoggerFactory.getLogger(CardBrandTypeResource.class);

    private static final String ENTITY_NAME = "cardBrandType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardBrandTypeService cardBrandTypeService;

    private final CardBrandTypeRepository cardBrandTypeRepository;

    private final CardBrandTypeQueryService cardBrandTypeQueryService;

    public CardBrandTypeResource(
        CardBrandTypeService cardBrandTypeService,
        CardBrandTypeRepository cardBrandTypeRepository,
        CardBrandTypeQueryService cardBrandTypeQueryService
    ) {
        this.cardBrandTypeService = cardBrandTypeService;
        this.cardBrandTypeRepository = cardBrandTypeRepository;
        this.cardBrandTypeQueryService = cardBrandTypeQueryService;
    }

    /**
     * {@code POST  /card-brand-types} : Create a new cardBrandType.
     *
     * @param cardBrandTypeDTO the cardBrandTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardBrandTypeDTO, or with status {@code 400 (Bad Request)} if the cardBrandType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-brand-types")
    public ResponseEntity<CardBrandTypeDTO> createCardBrandType(@Valid @RequestBody CardBrandTypeDTO cardBrandTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CardBrandType : {}", cardBrandTypeDTO);
        if (cardBrandTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardBrandType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardBrandTypeDTO result = cardBrandTypeService.save(cardBrandTypeDTO);
        return ResponseEntity
            .created(new URI("/api/card-brand-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-brand-types/:id} : Updates an existing cardBrandType.
     *
     * @param id the id of the cardBrandTypeDTO to save.
     * @param cardBrandTypeDTO the cardBrandTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardBrandTypeDTO,
     * or with status {@code 400 (Bad Request)} if the cardBrandTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardBrandTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-brand-types/{id}")
    public ResponseEntity<CardBrandTypeDTO> updateCardBrandType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardBrandTypeDTO cardBrandTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardBrandType : {}, {}", id, cardBrandTypeDTO);
        if (cardBrandTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardBrandTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardBrandTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardBrandTypeDTO result = cardBrandTypeService.save(cardBrandTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardBrandTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-brand-types/:id} : Partial updates given fields of an existing cardBrandType, field will ignore if it is null
     *
     * @param id the id of the cardBrandTypeDTO to save.
     * @param cardBrandTypeDTO the cardBrandTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardBrandTypeDTO,
     * or with status {@code 400 (Bad Request)} if the cardBrandTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardBrandTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardBrandTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-brand-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardBrandTypeDTO> partialUpdateCardBrandType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardBrandTypeDTO cardBrandTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardBrandType partially : {}, {}", id, cardBrandTypeDTO);
        if (cardBrandTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardBrandTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardBrandTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardBrandTypeDTO> result = cardBrandTypeService.partialUpdate(cardBrandTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardBrandTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-brand-types} : get all the cardBrandTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardBrandTypes in body.
     */
    @GetMapping("/card-brand-types")
    public ResponseEntity<List<CardBrandTypeDTO>> getAllCardBrandTypes(CardBrandTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CardBrandTypes by criteria: {}", criteria);
        Page<CardBrandTypeDTO> page = cardBrandTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-brand-types/count} : count all the cardBrandTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-brand-types/count")
    public ResponseEntity<Long> countCardBrandTypes(CardBrandTypeCriteria criteria) {
        log.debug("REST request to count CardBrandTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardBrandTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-brand-types/:id} : get the "id" cardBrandType.
     *
     * @param id the id of the cardBrandTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardBrandTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-brand-types/{id}")
    public ResponseEntity<CardBrandTypeDTO> getCardBrandType(@PathVariable Long id) {
        log.debug("REST request to get CardBrandType : {}", id);
        Optional<CardBrandTypeDTO> cardBrandTypeDTO = cardBrandTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardBrandTypeDTO);
    }

    /**
     * {@code DELETE  /card-brand-types/:id} : delete the "id" cardBrandType.
     *
     * @param id the id of the cardBrandTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-brand-types/{id}")
    public ResponseEntity<Void> deleteCardBrandType(@PathVariable Long id) {
        log.debug("REST request to delete CardBrandType : {}", id);
        cardBrandTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-brand-types?query=:query} : search for the cardBrandType corresponding
     * to the query.
     *
     * @param query the query of the cardBrandType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-brand-types")
    public ResponseEntity<List<CardBrandTypeDTO>> searchCardBrandTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardBrandTypes for query {}", query);
        Page<CardBrandTypeDTO> page = cardBrandTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
