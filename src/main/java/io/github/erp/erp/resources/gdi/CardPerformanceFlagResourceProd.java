package io.github.erp.erp.resources.gdi;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.repository.CardPerformanceFlagRepository;
import io.github.erp.service.CardPerformanceFlagQueryService;
import io.github.erp.service.CardPerformanceFlagService;
import io.github.erp.service.criteria.CardPerformanceFlagCriteria;
import io.github.erp.service.dto.CardPerformanceFlagDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardPerformanceFlag}.
 */
@RestController("CardPerformanceFlagResourceProd")
@RequestMapping("/api/granular-data")
public class CardPerformanceFlagResourceProd {

    private final Logger log = LoggerFactory.getLogger(CardPerformanceFlagResourceProd.class);

    private static final String ENTITY_NAME = "cardPerformanceFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardPerformanceFlagService cardPerformanceFlagService;

    private final CardPerformanceFlagRepository cardPerformanceFlagRepository;

    private final CardPerformanceFlagQueryService cardPerformanceFlagQueryService;

    public CardPerformanceFlagResourceProd(
        CardPerformanceFlagService cardPerformanceFlagService,
        CardPerformanceFlagRepository cardPerformanceFlagRepository,
        CardPerformanceFlagQueryService cardPerformanceFlagQueryService
    ) {
        this.cardPerformanceFlagService = cardPerformanceFlagService;
        this.cardPerformanceFlagRepository = cardPerformanceFlagRepository;
        this.cardPerformanceFlagQueryService = cardPerformanceFlagQueryService;
    }

    /**
     * {@code POST  /card-performance-flags} : Create a new cardPerformanceFlag.
     *
     * @param cardPerformanceFlagDTO the cardPerformanceFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardPerformanceFlagDTO, or with status {@code 400 (Bad Request)} if the cardPerformanceFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-performance-flags")
    public ResponseEntity<CardPerformanceFlagDTO> createCardPerformanceFlag(
        @Valid @RequestBody CardPerformanceFlagDTO cardPerformanceFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CardPerformanceFlag : {}", cardPerformanceFlagDTO);
        if (cardPerformanceFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardPerformanceFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardPerformanceFlagDTO result = cardPerformanceFlagService.save(cardPerformanceFlagDTO);
        return ResponseEntity
            .created(new URI("/api/card-performance-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-performance-flags/:id} : Updates an existing cardPerformanceFlag.
     *
     * @param id the id of the cardPerformanceFlagDTO to save.
     * @param cardPerformanceFlagDTO the cardPerformanceFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardPerformanceFlagDTO,
     * or with status {@code 400 (Bad Request)} if the cardPerformanceFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardPerformanceFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-performance-flags/{id}")
    public ResponseEntity<CardPerformanceFlagDTO> updateCardPerformanceFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardPerformanceFlagDTO cardPerformanceFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardPerformanceFlag : {}, {}", id, cardPerformanceFlagDTO);
        if (cardPerformanceFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardPerformanceFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardPerformanceFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardPerformanceFlagDTO result = cardPerformanceFlagService.save(cardPerformanceFlagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardPerformanceFlagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-performance-flags/:id} : Partial updates given fields of an existing cardPerformanceFlag, field will ignore if it is null
     *
     * @param id the id of the cardPerformanceFlagDTO to save.
     * @param cardPerformanceFlagDTO the cardPerformanceFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardPerformanceFlagDTO,
     * or with status {@code 400 (Bad Request)} if the cardPerformanceFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardPerformanceFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardPerformanceFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-performance-flags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardPerformanceFlagDTO> partialUpdateCardPerformanceFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardPerformanceFlagDTO cardPerformanceFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardPerformanceFlag partially : {}, {}", id, cardPerformanceFlagDTO);
        if (cardPerformanceFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardPerformanceFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardPerformanceFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardPerformanceFlagDTO> result = cardPerformanceFlagService.partialUpdate(cardPerformanceFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardPerformanceFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-performance-flags} : get all the cardPerformanceFlags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardPerformanceFlags in body.
     */
    @GetMapping("/card-performance-flags")
    public ResponseEntity<List<CardPerformanceFlagDTO>> getAllCardPerformanceFlags(
        CardPerformanceFlagCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CardPerformanceFlags by criteria: {}", criteria);
        Page<CardPerformanceFlagDTO> page = cardPerformanceFlagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-performance-flags/count} : count all the cardPerformanceFlags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-performance-flags/count")
    public ResponseEntity<Long> countCardPerformanceFlags(CardPerformanceFlagCriteria criteria) {
        log.debug("REST request to count CardPerformanceFlags by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardPerformanceFlagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-performance-flags/:id} : get the "id" cardPerformanceFlag.
     *
     * @param id the id of the cardPerformanceFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardPerformanceFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-performance-flags/{id}")
    public ResponseEntity<CardPerformanceFlagDTO> getCardPerformanceFlag(@PathVariable Long id) {
        log.debug("REST request to get CardPerformanceFlag : {}", id);
        Optional<CardPerformanceFlagDTO> cardPerformanceFlagDTO = cardPerformanceFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardPerformanceFlagDTO);
    }

    /**
     * {@code DELETE  /card-performance-flags/:id} : delete the "id" cardPerformanceFlag.
     *
     * @param id the id of the cardPerformanceFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-performance-flags/{id}")
    public ResponseEntity<Void> deleteCardPerformanceFlag(@PathVariable Long id) {
        log.debug("REST request to delete CardPerformanceFlag : {}", id);
        cardPerformanceFlagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-performance-flags?query=:query} : search for the cardPerformanceFlag corresponding
     * to the query.
     *
     * @param query the query of the cardPerformanceFlag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-performance-flags")
    public ResponseEntity<List<CardPerformanceFlagDTO>> searchCardPerformanceFlags(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardPerformanceFlags for query {}", query);
        Page<CardPerformanceFlagDTO> page = cardPerformanceFlagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
