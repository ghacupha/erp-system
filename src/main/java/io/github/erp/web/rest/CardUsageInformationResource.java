package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

import io.github.erp.repository.CardUsageInformationRepository;
import io.github.erp.service.CardUsageInformationQueryService;
import io.github.erp.service.CardUsageInformationService;
import io.github.erp.service.criteria.CardUsageInformationCriteria;
import io.github.erp.service.dto.CardUsageInformationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CardUsageInformation}.
 */
@RestController
@RequestMapping("/api")
public class CardUsageInformationResource {

    private final Logger log = LoggerFactory.getLogger(CardUsageInformationResource.class);

    private static final String ENTITY_NAME = "gdiDataCardUsageInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CardUsageInformationService cardUsageInformationService;

    private final CardUsageInformationRepository cardUsageInformationRepository;

    private final CardUsageInformationQueryService cardUsageInformationQueryService;

    public CardUsageInformationResource(
        CardUsageInformationService cardUsageInformationService,
        CardUsageInformationRepository cardUsageInformationRepository,
        CardUsageInformationQueryService cardUsageInformationQueryService
    ) {
        this.cardUsageInformationService = cardUsageInformationService;
        this.cardUsageInformationRepository = cardUsageInformationRepository;
        this.cardUsageInformationQueryService = cardUsageInformationQueryService;
    }

    /**
     * {@code POST  /card-usage-informations} : Create a new cardUsageInformation.
     *
     * @param cardUsageInformationDTO the cardUsageInformationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cardUsageInformationDTO, or with status {@code 400 (Bad Request)} if the cardUsageInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/card-usage-informations")
    public ResponseEntity<CardUsageInformationDTO> createCardUsageInformation(
        @Valid @RequestBody CardUsageInformationDTO cardUsageInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CardUsageInformation : {}", cardUsageInformationDTO);
        if (cardUsageInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new cardUsageInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CardUsageInformationDTO result = cardUsageInformationService.save(cardUsageInformationDTO);
        return ResponseEntity
            .created(new URI("/api/card-usage-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /card-usage-informations/:id} : Updates an existing cardUsageInformation.
     *
     * @param id the id of the cardUsageInformationDTO to save.
     * @param cardUsageInformationDTO the cardUsageInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardUsageInformationDTO,
     * or with status {@code 400 (Bad Request)} if the cardUsageInformationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cardUsageInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/card-usage-informations/{id}")
    public ResponseEntity<CardUsageInformationDTO> updateCardUsageInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CardUsageInformationDTO cardUsageInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CardUsageInformation : {}, {}", id, cardUsageInformationDTO);
        if (cardUsageInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardUsageInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardUsageInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CardUsageInformationDTO result = cardUsageInformationService.save(cardUsageInformationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardUsageInformationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /card-usage-informations/:id} : Partial updates given fields of an existing cardUsageInformation, field will ignore if it is null
     *
     * @param id the id of the cardUsageInformationDTO to save.
     * @param cardUsageInformationDTO the cardUsageInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cardUsageInformationDTO,
     * or with status {@code 400 (Bad Request)} if the cardUsageInformationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the cardUsageInformationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the cardUsageInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/card-usage-informations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CardUsageInformationDTO> partialUpdateCardUsageInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CardUsageInformationDTO cardUsageInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CardUsageInformation partially : {}, {}", id, cardUsageInformationDTO);
        if (cardUsageInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cardUsageInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cardUsageInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CardUsageInformationDTO> result = cardUsageInformationService.partialUpdate(cardUsageInformationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cardUsageInformationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /card-usage-informations} : get all the cardUsageInformations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cardUsageInformations in body.
     */
    @GetMapping("/card-usage-informations")
    public ResponseEntity<List<CardUsageInformationDTO>> getAllCardUsageInformations(
        CardUsageInformationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CardUsageInformations by criteria: {}", criteria);
        Page<CardUsageInformationDTO> page = cardUsageInformationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /card-usage-informations/count} : count all the cardUsageInformations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/card-usage-informations/count")
    public ResponseEntity<Long> countCardUsageInformations(CardUsageInformationCriteria criteria) {
        log.debug("REST request to count CardUsageInformations by criteria: {}", criteria);
        return ResponseEntity.ok().body(cardUsageInformationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /card-usage-informations/:id} : get the "id" cardUsageInformation.
     *
     * @param id the id of the cardUsageInformationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cardUsageInformationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/card-usage-informations/{id}")
    public ResponseEntity<CardUsageInformationDTO> getCardUsageInformation(@PathVariable Long id) {
        log.debug("REST request to get CardUsageInformation : {}", id);
        Optional<CardUsageInformationDTO> cardUsageInformationDTO = cardUsageInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(cardUsageInformationDTO);
    }

    /**
     * {@code DELETE  /card-usage-informations/:id} : delete the "id" cardUsageInformation.
     *
     * @param id the id of the cardUsageInformationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/card-usage-informations/{id}")
    public ResponseEntity<Void> deleteCardUsageInformation(@PathVariable Long id) {
        log.debug("REST request to delete CardUsageInformation : {}", id);
        cardUsageInformationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/card-usage-informations?query=:query} : search for the cardUsageInformation corresponding
     * to the query.
     *
     * @param query the query of the cardUsageInformation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/card-usage-informations")
    public ResponseEntity<List<CardUsageInformationDTO>> searchCardUsageInformations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CardUsageInformations for query {}", query);
        Page<CardUsageInformationDTO> page = cardUsageInformationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
