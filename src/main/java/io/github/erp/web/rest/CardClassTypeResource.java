package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.repository.CardClassTypeRepository;
import io.github.erp.service.CardClassTypeQueryService;
import io.github.erp.service.CardClassTypeService;
import io.github.erp.service.criteria.CardClassTypeCriteria;
import io.github.erp.service.dto.CardClassTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardClassType}.
 */
@RestController
@RequestMapping("/api")
public class CardClassTypeResource {

    private final Logger log = LoggerFactory.getLogger(CardClassTypeResource.class);

    private static final String ENTITY_NAME = "cardClassType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardClassTypeService cardClassTypeService;

    private final CardClassTypeRepository cardClassTypeRepository;

    private final CardClassTypeQueryService cardClassTypeQueryService;

    public CardClassTypeResource(
        CardClassTypeService cardClassTypeService,
        CardClassTypeRepository cardClassTypeRepository,
        CardClassTypeQueryService cardClassTypeQueryService
    ) {
        this.cardClassTypeService = cardClassTypeService;
        this.cardClassTypeRepository = cardClassTypeRepository;
        this.cardClassTypeQueryService = cardClassTypeQueryService;
    }

    /**
     * {@code POST  /card-class-types} : Create a new cardClassType.
     *
     * @param cardClassTypeDTO the cardClassTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardClassTypeDTO, or with status {@code 400 (Bad Request)} if the cardClassType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-class-types")
    public ResponseEntity<CardClassTypeDTO> createCardClassType(@Valid @RequestBody CardClassTypeDTO cardClassTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CardClassType : {}", cardClassTypeDTO);
        if (cardClassTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardClassType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardClassTypeDTO result = cardClassTypeService.save(cardClassTypeDTO);
        return ResponseEntity
            .created(new URI("/api/card-class-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-class-types/:id} : Updates an existing cardClassType.
     *
     * @param id the id of the cardClassTypeDTO to save.
     * @param cardClassTypeDTO the cardClassTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardClassTypeDTO,
     * or with status {@code 400 (Bad Request)} if the cardClassTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardClassTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-class-types/{id}")
    public ResponseEntity<CardClassTypeDTO> updateCardClassType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardClassTypeDTO cardClassTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardClassType : {}, {}", id, cardClassTypeDTO);
        if (cardClassTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardClassTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardClassTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardClassTypeDTO result = cardClassTypeService.save(cardClassTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardClassTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-class-types/:id} : Partial updates given fields of an existing cardClassType, field will ignore if it is null
     *
     * @param id the id of the cardClassTypeDTO to save.
     * @param cardClassTypeDTO the cardClassTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardClassTypeDTO,
     * or with status {@code 400 (Bad Request)} if the cardClassTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardClassTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardClassTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-class-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardClassTypeDTO> partialUpdateCardClassType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardClassTypeDTO cardClassTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardClassType partially : {}, {}", id, cardClassTypeDTO);
        if (cardClassTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardClassTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardClassTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardClassTypeDTO> result = cardClassTypeService.partialUpdate(cardClassTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardClassTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-class-types} : get all the cardClassTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardClassTypes in body.
     */
    @GetMapping("/card-class-types")
    public ResponseEntity<List<CardClassTypeDTO>> getAllCardClassTypes(CardClassTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CardClassTypes by criteria: {}", criteria);
        Page<CardClassTypeDTO> page = cardClassTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-class-types/count} : count all the cardClassTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-class-types/count")
    public ResponseEntity<Long> countCardClassTypes(CardClassTypeCriteria criteria) {
        log.debug("REST request to count CardClassTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardClassTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-class-types/:id} : get the "id" cardClassType.
     *
     * @param id the id of the cardClassTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardClassTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-class-types/{id}")
    public ResponseEntity<CardClassTypeDTO> getCardClassType(@PathVariable Long id) {
        log.debug("REST request to get CardClassType : {}", id);
        Optional<CardClassTypeDTO> cardClassTypeDTO = cardClassTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardClassTypeDTO);
    }

    /**
     * {@code DELETE  /card-class-types/:id} : delete the "id" cardClassType.
     *
     * @param id the id of the cardClassTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-class-types/{id}")
    public ResponseEntity<Void> deleteCardClassType(@PathVariable Long id) {
        log.debug("REST request to delete CardClassType : {}", id);
        cardClassTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-class-types?query=:query} : search for the cardClassType corresponding
     * to the query.
     *
     * @param query the query of the cardClassType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-class-types")
    public ResponseEntity<List<CardClassTypeDTO>> searchCardClassTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardClassTypes for query {}", query);
        Page<CardClassTypeDTO> page = cardClassTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
