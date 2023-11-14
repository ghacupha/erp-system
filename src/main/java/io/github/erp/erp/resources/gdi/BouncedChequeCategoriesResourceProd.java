package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.repository.BouncedChequeCategoriesRepository;
import io.github.erp.service.BouncedChequeCategoriesQueryService;
import io.github.erp.service.BouncedChequeCategoriesService;
import io.github.erp.service.criteria.BouncedChequeCategoriesCriteria;
import io.github.erp.service.dto.BouncedChequeCategoriesDTO;
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
 * REST controller for managing {@link io.github.erp.domain.BouncedChequeCategories}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class BouncedChequeCategoriesResourceProd {

    private final Logger log = LoggerFactory.getLogger(BouncedChequeCategoriesResourceProd.class);

    private static final String ENTITY_NAME = "bouncedChequeCategories";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BouncedChequeCategoriesService bouncedChequeCategoriesService;

    private final BouncedChequeCategoriesRepository bouncedChequeCategoriesRepository;

    private final BouncedChequeCategoriesQueryService bouncedChequeCategoriesQueryService;

    public BouncedChequeCategoriesResourceProd(
        BouncedChequeCategoriesService bouncedChequeCategoriesService,
        BouncedChequeCategoriesRepository bouncedChequeCategoriesRepository,
        BouncedChequeCategoriesQueryService bouncedChequeCategoriesQueryService
    ) {
        this.bouncedChequeCategoriesService = bouncedChequeCategoriesService;
        this.bouncedChequeCategoriesRepository = bouncedChequeCategoriesRepository;
        this.bouncedChequeCategoriesQueryService = bouncedChequeCategoriesQueryService;
    }

    /**
     * {@code POST  /bounced-cheque-categories} : Create a new bouncedChequeCategories.
     *
     * @param bouncedChequeCategoriesDTO the bouncedChequeCategoriesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bouncedChequeCategoriesDTO, or with status {@code 400 (Bad Request)} if the bouncedChequeCategories has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bounced-cheque-categories")
    public ResponseEntity<BouncedChequeCategoriesDTO> createBouncedChequeCategories(
        @Valid @RequestBody BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO
    ) throws URISyntaxException {
        log.debug("REST request to save BouncedChequeCategories : {}", bouncedChequeCategoriesDTO);
        if (bouncedChequeCategoriesDTO.getId() != null) {
            throw new BadRequestAlertException("A new bouncedChequeCategories cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BouncedChequeCategoriesDTO result = bouncedChequeCategoriesService.save(bouncedChequeCategoriesDTO);
        return ResponseEntity
            .created(new URI("/api/bounced-cheque-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bounced-cheque-categories/:id} : Updates an existing bouncedChequeCategories.
     *
     * @param id the id of the bouncedChequeCategoriesDTO to save.
     * @param bouncedChequeCategoriesDTO the bouncedChequeCategoriesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bouncedChequeCategoriesDTO,
     * or with status {@code 400 (Bad Request)} if the bouncedChequeCategoriesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bouncedChequeCategoriesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bounced-cheque-categories/{id}")
    public ResponseEntity<BouncedChequeCategoriesDTO> updateBouncedChequeCategories(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BouncedChequeCategories : {}, {}", id, bouncedChequeCategoriesDTO);
        if (bouncedChequeCategoriesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bouncedChequeCategoriesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bouncedChequeCategoriesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BouncedChequeCategoriesDTO result = bouncedChequeCategoriesService.save(bouncedChequeCategoriesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bouncedChequeCategoriesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bounced-cheque-categories/:id} : Partial updates given fields of an existing bouncedChequeCategories, field will ignore if it is null
     *
     * @param id the id of the bouncedChequeCategoriesDTO to save.
     * @param bouncedChequeCategoriesDTO the bouncedChequeCategoriesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bouncedChequeCategoriesDTO,
     * or with status {@code 400 (Bad Request)} if the bouncedChequeCategoriesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bouncedChequeCategoriesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bouncedChequeCategoriesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bounced-cheque-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BouncedChequeCategoriesDTO> partialUpdateBouncedChequeCategories(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BouncedChequeCategoriesDTO bouncedChequeCategoriesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BouncedChequeCategories partially : {}, {}", id, bouncedChequeCategoriesDTO);
        if (bouncedChequeCategoriesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bouncedChequeCategoriesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bouncedChequeCategoriesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BouncedChequeCategoriesDTO> result = bouncedChequeCategoriesService.partialUpdate(bouncedChequeCategoriesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bouncedChequeCategoriesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bounced-cheque-categories} : get all the bouncedChequeCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bouncedChequeCategories in body.
     */
    @GetMapping("/bounced-cheque-categories")
    public ResponseEntity<List<BouncedChequeCategoriesDTO>> getAllBouncedChequeCategories(
        BouncedChequeCategoriesCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get BouncedChequeCategories by criteria: {}", criteria);
        Page<BouncedChequeCategoriesDTO> page = bouncedChequeCategoriesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bounced-cheque-categories/count} : count all the bouncedChequeCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bounced-cheque-categories/count")
    public ResponseEntity<Long> countBouncedChequeCategories(BouncedChequeCategoriesCriteria criteria) {
        log.debug("REST request to count BouncedChequeCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(bouncedChequeCategoriesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bounced-cheque-categories/:id} : get the "id" bouncedChequeCategories.
     *
     * @param id the id of the bouncedChequeCategoriesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bouncedChequeCategoriesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bounced-cheque-categories/{id}")
    public ResponseEntity<BouncedChequeCategoriesDTO> getBouncedChequeCategories(@PathVariable Long id) {
        log.debug("REST request to get BouncedChequeCategories : {}", id);
        Optional<BouncedChequeCategoriesDTO> bouncedChequeCategoriesDTO = bouncedChequeCategoriesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bouncedChequeCategoriesDTO);
    }

    /**
     * {@code DELETE  /bounced-cheque-categories/:id} : delete the "id" bouncedChequeCategories.
     *
     * @param id the id of the bouncedChequeCategoriesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bounced-cheque-categories/{id}")
    public ResponseEntity<Void> deleteBouncedChequeCategories(@PathVariable Long id) {
        log.debug("REST request to delete BouncedChequeCategories : {}", id);
        bouncedChequeCategoriesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/bounced-cheque-categories?query=:query} : search for the bouncedChequeCategories corresponding
     * to the query.
     *
     * @param query the query of the bouncedChequeCategories search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/bounced-cheque-categories")
    public ResponseEntity<List<BouncedChequeCategoriesDTO>> searchBouncedChequeCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BouncedChequeCategories for query {}", query);
        Page<BouncedChequeCategoriesDTO> page = bouncedChequeCategoriesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
