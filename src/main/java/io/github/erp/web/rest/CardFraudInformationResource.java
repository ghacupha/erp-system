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

import io.github.erp.repository.CardFraudInformationRepository;
import io.github.erp.service.CardFraudInformationQueryService;
import io.github.erp.service.CardFraudInformationService;
import io.github.erp.service.criteria.CardFraudInformationCriteria;
import io.github.erp.service.dto.CardFraudInformationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardFraudInformation}.
 */
@RestController
@RequestMapping("/api")
public class CardFraudInformationResource {

    private final Logger log = LoggerFactory.getLogger(CardFraudInformationResource.class);

    private static final String ENTITY_NAME = "gdiDataCardFraudInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardFraudInformationService cardFraudInformationService;

    private final CardFraudInformationRepository cardFraudInformationRepository;

    private final CardFraudInformationQueryService cardFraudInformationQueryService;

    public CardFraudInformationResource(
        CardFraudInformationService cardFraudInformationService,
        CardFraudInformationRepository cardFraudInformationRepository,
        CardFraudInformationQueryService cardFraudInformationQueryService
    ) {
        this.cardFraudInformationService = cardFraudInformationService;
        this.cardFraudInformationRepository = cardFraudInformationRepository;
        this.cardFraudInformationQueryService = cardFraudInformationQueryService;
    }

    /**
     * {@code POST  /card-fraud-informations} : Create a new cardFraudInformation.
     *
     * @param cardFraudInformationDTO the cardFraudInformationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardFraudInformationDTO, or with status {@code 400 (Bad Request)} if the cardFraudInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-fraud-informations")
    public ResponseEntity<CardFraudInformationDTO> createCardFraudInformation(
        @Valid @RequestBody CardFraudInformationDTO cardFraudInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CardFraudInformation : {}", cardFraudInformationDTO);
        if (cardFraudInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardFraudInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardFraudInformationDTO result = cardFraudInformationService.save(cardFraudInformationDTO);
        return ResponseEntity
            .created(new URI("/api/card-fraud-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-fraud-informations/:id} : Updates an existing cardFraudInformation.
     *
     * @param id the id of the cardFraudInformationDTO to save.
     * @param cardFraudInformationDTO the cardFraudInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardFraudInformationDTO,
     * or with status {@code 400 (Bad Request)} if the cardFraudInformationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardFraudInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-fraud-informations/{id}")
    public ResponseEntity<CardFraudInformationDTO> updateCardFraudInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardFraudInformationDTO cardFraudInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardFraudInformation : {}, {}", id, cardFraudInformationDTO);
        if (cardFraudInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardFraudInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardFraudInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardFraudInformationDTO result = cardFraudInformationService.save(cardFraudInformationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardFraudInformationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-fraud-informations/:id} : Partial updates given fields of an existing cardFraudInformation, field will ignore if it is null
     *
     * @param id the id of the cardFraudInformationDTO to save.
     * @param cardFraudInformationDTO the cardFraudInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardFraudInformationDTO,
     * or with status {@code 400 (Bad Request)} if the cardFraudInformationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardFraudInformationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardFraudInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-fraud-informations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardFraudInformationDTO> partialUpdateCardFraudInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardFraudInformationDTO cardFraudInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardFraudInformation partially : {}, {}", id, cardFraudInformationDTO);
        if (cardFraudInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardFraudInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardFraudInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardFraudInformationDTO> result = cardFraudInformationService.partialUpdate(cardFraudInformationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardFraudInformationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-fraud-informations} : get all the cardFraudInformations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardFraudInformations in body.
     */
    @GetMapping("/card-fraud-informations")
    public ResponseEntity<List<CardFraudInformationDTO>> getAllCardFraudInformations(
        CardFraudInformationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CardFraudInformations by criteria: {}", criteria);
        Page<CardFraudInformationDTO> page = cardFraudInformationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-fraud-informations/count} : count all the cardFraudInformations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-fraud-informations/count")
    public ResponseEntity<Long> countCardFraudInformations(CardFraudInformationCriteria criteria) {
        log.debug("REST request to count CardFraudInformations by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardFraudInformationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-fraud-informations/:id} : get the "id" cardFraudInformation.
     *
     * @param id the id of the cardFraudInformationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardFraudInformationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-fraud-informations/{id}")
    public ResponseEntity<CardFraudInformationDTO> getCardFraudInformation(@PathVariable Long id) {
        log.debug("REST request to get CardFraudInformation : {}", id);
        Optional<CardFraudInformationDTO> cardFraudInformationDTO = cardFraudInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardFraudInformationDTO);
    }

    /**
     * {@code DELETE  /card-fraud-informations/:id} : delete the "id" cardFraudInformation.
     *
     * @param id the id of the cardFraudInformationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-fraud-informations/{id}")
    public ResponseEntity<Void> deleteCardFraudInformation(@PathVariable Long id) {
        log.debug("REST request to delete CardFraudInformation : {}", id);
        cardFraudInformationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-fraud-informations?query=:query} : search for the cardFraudInformation corresponding
     * to the query.
     *
     * @param query the query of the cardFraudInformation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-fraud-informations")
    public ResponseEntity<List<CardFraudInformationDTO>> searchCardFraudInformations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardFraudInformations for query {}", query);
        Page<CardFraudInformationDTO> page = cardFraudInformationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
