package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.repository.PlaceholderRepository;
import io.github.erp.service.PlaceholderQueryService;
import io.github.erp.service.PlaceholderService;
import io.github.erp.service.criteria.PlaceholderCriteria;
import io.github.erp.service.dto.PlaceholderDTO;
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
 * REST controller for managing {@link io.github.erp.domain.Placeholder}.
 */
@RestController
@RequestMapping("/api")
public class PlaceholderResource {

    private final Logger log = LoggerFactory.getLogger(PlaceholderResource.class);

    private static final String ENTITY_NAME = "erpServicePlaceholder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaceholderService placeholderService;

    private final PlaceholderRepository placeholderRepository;

    private final PlaceholderQueryService placeholderQueryService;

    public PlaceholderResource(
        PlaceholderService placeholderService,
        PlaceholderRepository placeholderRepository,
        PlaceholderQueryService placeholderQueryService
    ) {
        this.placeholderService = placeholderService;
        this.placeholderRepository = placeholderRepository;
        this.placeholderQueryService = placeholderQueryService;
    }

    /**
     * {@code POST  /placeholders} : Create a new placeholder.
     *
     * @param placeholderDTO the placeholderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new placeholderDTO, or with status {@code 400 (Bad Request)} if the placeholder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/placeholders")
    public ResponseEntity<PlaceholderDTO> createPlaceholder(@Valid @RequestBody PlaceholderDTO placeholderDTO) throws URISyntaxException {
        log.debug("REST request to save Placeholder : {}", placeholderDTO);
        if (placeholderDTO.getId() != null) {
            throw new BadRequestAlertException("A new placeholder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PlaceholderDTO result = placeholderService.save(placeholderDTO);
        return ResponseEntity
            .created(new URI("/api/placeholders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /placeholders/:id} : Updates an existing placeholder.
     *
     * @param id the id of the placeholderDTO to save.
     * @param placeholderDTO the placeholderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated placeholderDTO,
     * or with status {@code 400 (Bad Request)} if the placeholderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the placeholderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/placeholders/{id}")
    public ResponseEntity<PlaceholderDTO> updatePlaceholder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PlaceholderDTO placeholderDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Placeholder : {}, {}", id, placeholderDTO);
        if (placeholderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, placeholderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!placeholderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PlaceholderDTO result = placeholderService.save(placeholderDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, placeholderDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /placeholders/:id} : Partial updates given fields of an existing placeholder, field will ignore if it is null
     *
     * @param id the id of the placeholderDTO to save.
     * @param placeholderDTO the placeholderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated placeholderDTO,
     * or with status {@code 400 (Bad Request)} if the placeholderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the placeholderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the placeholderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/placeholders/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PlaceholderDTO> partialUpdatePlaceholder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PlaceholderDTO placeholderDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Placeholder partially : {}, {}", id, placeholderDTO);
        if (placeholderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, placeholderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!placeholderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PlaceholderDTO> result = placeholderService.partialUpdate(placeholderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, placeholderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /placeholders} : get all the placeholders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of placeholders in body.
     */
    @GetMapping("/placeholders")
    public ResponseEntity<List<PlaceholderDTO>> getAllPlaceholders(PlaceholderCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Placeholders by criteria: {}", criteria);
        Page<PlaceholderDTO> page = placeholderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /placeholders/count} : count all the placeholders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/placeholders/count")
    public ResponseEntity<Long> countPlaceholders(PlaceholderCriteria criteria) {
        log.debug("REST request to count Placeholders by criteria: {}", criteria);
        return ResponseEntity.ok().body(placeholderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /placeholders/:id} : get the "id" placeholder.
     *
     * @param id the id of the placeholderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the placeholderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/placeholders/{id}")
    public ResponseEntity<PlaceholderDTO> getPlaceholder(@PathVariable Long id) {
        log.debug("REST request to get Placeholder : {}", id);
        Optional<PlaceholderDTO> placeholderDTO = placeholderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(placeholderDTO);
    }

    /**
     * {@code DELETE  /placeholders/:id} : delete the "id" placeholder.
     *
     * @param id the id of the placeholderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/placeholders/{id}")
    public ResponseEntity<Void> deletePlaceholder(@PathVariable Long id) {
        log.debug("REST request to delete Placeholder : {}", id);
        placeholderService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/placeholders?query=:query} : search for the placeholder corresponding
     * to the query.
     *
     * @param query the query of the placeholder search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/placeholders")
    public ResponseEntity<List<PlaceholderDTO>> searchPlaceholders(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Placeholders for query {}", query);
        Page<PlaceholderDTO> page = placeholderService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
