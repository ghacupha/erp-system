package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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

import io.github.erp.repository.MoratoriumItemRepository;
import io.github.erp.service.MoratoriumItemQueryService;
import io.github.erp.service.MoratoriumItemService;
import io.github.erp.service.criteria.MoratoriumItemCriteria;
import io.github.erp.service.dto.MoratoriumItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.MoratoriumItem}.
 */
@RestController
@RequestMapping("/api")
public class MoratoriumItemResource {

    private final Logger log = LoggerFactory.getLogger(MoratoriumItemResource.class);

    private static final String ENTITY_NAME = "moratoriumItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MoratoriumItemService moratoriumItemService;

    private final MoratoriumItemRepository moratoriumItemRepository;

    private final MoratoriumItemQueryService moratoriumItemQueryService;

    public MoratoriumItemResource(
        MoratoriumItemService moratoriumItemService,
        MoratoriumItemRepository moratoriumItemRepository,
        MoratoriumItemQueryService moratoriumItemQueryService
    ) {
        this.moratoriumItemService = moratoriumItemService;
        this.moratoriumItemRepository = moratoriumItemRepository;
        this.moratoriumItemQueryService = moratoriumItemQueryService;
    }

    /**
     * {@code POST  /moratorium-items} : Create a new moratoriumItem.
     *
     * @param moratoriumItemDTO the moratoriumItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new moratoriumItemDTO, or with status {@code 400 (Bad Request)} if the moratoriumItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/moratorium-items")
    public ResponseEntity<MoratoriumItemDTO> createMoratoriumItem(@Valid @RequestBody MoratoriumItemDTO moratoriumItemDTO)
        throws URISyntaxException {
        log.debug("REST request to save MoratoriumItem : {}", moratoriumItemDTO);
        if (moratoriumItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new moratoriumItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MoratoriumItemDTO result = moratoriumItemService.save(moratoriumItemDTO);
        return ResponseEntity
            .created(new URI("/api/moratorium-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /moratorium-items/:id} : Updates an existing moratoriumItem.
     *
     * @param id the id of the moratoriumItemDTO to save.
     * @param moratoriumItemDTO the moratoriumItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moratoriumItemDTO,
     * or with status {@code 400 (Bad Request)} if the moratoriumItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the moratoriumItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/moratorium-items/{id}")
    public ResponseEntity<MoratoriumItemDTO> updateMoratoriumItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MoratoriumItemDTO moratoriumItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MoratoriumItem : {}, {}", id, moratoriumItemDTO);
        if (moratoriumItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moratoriumItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moratoriumItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MoratoriumItemDTO result = moratoriumItemService.save(moratoriumItemDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moratoriumItemDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /moratorium-items/:id} : Partial updates given fields of an existing moratoriumItem, field will ignore if it is null
     *
     * @param id the id of the moratoriumItemDTO to save.
     * @param moratoriumItemDTO the moratoriumItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated moratoriumItemDTO,
     * or with status {@code 400 (Bad Request)} if the moratoriumItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the moratoriumItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the moratoriumItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/moratorium-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MoratoriumItemDTO> partialUpdateMoratoriumItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MoratoriumItemDTO moratoriumItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MoratoriumItem partially : {}, {}", id, moratoriumItemDTO);
        if (moratoriumItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, moratoriumItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!moratoriumItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MoratoriumItemDTO> result = moratoriumItemService.partialUpdate(moratoriumItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, moratoriumItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /moratorium-items} : get all the moratoriumItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of moratoriumItems in body.
     */
    @GetMapping("/moratorium-items")
    public ResponseEntity<List<MoratoriumItemDTO>> getAllMoratoriumItems(MoratoriumItemCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MoratoriumItems by criteria: {}", criteria);
        Page<MoratoriumItemDTO> page = moratoriumItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /moratorium-items/count} : count all the moratoriumItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/moratorium-items/count")
    public ResponseEntity<Long> countMoratoriumItems(MoratoriumItemCriteria criteria) {
        log.debug("REST request to count MoratoriumItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(moratoriumItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /moratorium-items/:id} : get the "id" moratoriumItem.
     *
     * @param id the id of the moratoriumItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the moratoriumItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/moratorium-items/{id}")
    public ResponseEntity<MoratoriumItemDTO> getMoratoriumItem(@PathVariable Long id) {
        log.debug("REST request to get MoratoriumItem : {}", id);
        Optional<MoratoriumItemDTO> moratoriumItemDTO = moratoriumItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(moratoriumItemDTO);
    }

    /**
     * {@code DELETE  /moratorium-items/:id} : delete the "id" moratoriumItem.
     *
     * @param id the id of the moratoriumItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/moratorium-items/{id}")
    public ResponseEntity<Void> deleteMoratoriumItem(@PathVariable Long id) {
        log.debug("REST request to delete MoratoriumItem : {}", id);
        moratoriumItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/moratorium-items?query=:query} : search for the moratoriumItem corresponding
     * to the query.
     *
     * @param query the query of the moratoriumItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/moratorium-items")
    public ResponseEntity<List<MoratoriumItemDTO>> searchMoratoriumItems(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MoratoriumItems for query {}", query);
        Page<MoratoriumItemDTO> page = moratoriumItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
