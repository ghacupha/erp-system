package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import io.github.erp.repository.FraudCategoryFlagRepository;
import io.github.erp.service.FraudCategoryFlagService;
import io.github.erp.service.dto.FraudCategoryFlagDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FraudCategoryFlag}.
 */
@RestController("FraudCategoryFlagResourceProd")
@RequestMapping("/api/granular-data")
public class FraudCategoryFlagResourceProd {

    private final Logger log = LoggerFactory.getLogger(FraudCategoryFlagResourceProd.class);

    private static final String ENTITY_NAME = "fraudCategoryFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FraudCategoryFlagService fraudCategoryFlagService;

    private final FraudCategoryFlagRepository fraudCategoryFlagRepository;

    public FraudCategoryFlagResourceProd(
        FraudCategoryFlagService fraudCategoryFlagService,
        FraudCategoryFlagRepository fraudCategoryFlagRepository
    ) {
        this.fraudCategoryFlagService = fraudCategoryFlagService;
        this.fraudCategoryFlagRepository = fraudCategoryFlagRepository;
    }

    /**
     * {@code POST  /fraud-category-flags} : Create a new fraudCategoryFlag.
     *
     * @param fraudCategoryFlagDTO the fraudCategoryFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fraudCategoryFlagDTO, or with status {@code 400 (Bad Request)} if the fraudCategoryFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fraud-category-flags")
    public ResponseEntity<FraudCategoryFlagDTO> createFraudCategoryFlag(@Valid @RequestBody FraudCategoryFlagDTO fraudCategoryFlagDTO)
        throws URISyntaxException {
        log.debug("REST request to save FraudCategoryFlag : {}", fraudCategoryFlagDTO);
        if (fraudCategoryFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new fraudCategoryFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FraudCategoryFlagDTO result = fraudCategoryFlagService.save(fraudCategoryFlagDTO);
        return ResponseEntity
            .created(new URI("/api/fraud-category-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fraud-category-flags/:id} : Updates an existing fraudCategoryFlag.
     *
     * @param id the id of the fraudCategoryFlagDTO to save.
     * @param fraudCategoryFlagDTO the fraudCategoryFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraudCategoryFlagDTO,
     * or with status {@code 400 (Bad Request)} if the fraudCategoryFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fraudCategoryFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fraud-category-flags/{id}")
    public ResponseEntity<FraudCategoryFlagDTO> updateFraudCategoryFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FraudCategoryFlagDTO fraudCategoryFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FraudCategoryFlag : {}, {}", id, fraudCategoryFlagDTO);
        if (fraudCategoryFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraudCategoryFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraudCategoryFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FraudCategoryFlagDTO result = fraudCategoryFlagService.save(fraudCategoryFlagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraudCategoryFlagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fraud-category-flags/:id} : Partial updates given fields of an existing fraudCategoryFlag, field will ignore if it is null
     *
     * @param id the id of the fraudCategoryFlagDTO to save.
     * @param fraudCategoryFlagDTO the fraudCategoryFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraudCategoryFlagDTO,
     * or with status {@code 400 (Bad Request)} if the fraudCategoryFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fraudCategoryFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fraudCategoryFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fraud-category-flags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FraudCategoryFlagDTO> partialUpdateFraudCategoryFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FraudCategoryFlagDTO fraudCategoryFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FraudCategoryFlag partially : {}, {}", id, fraudCategoryFlagDTO);
        if (fraudCategoryFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraudCategoryFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraudCategoryFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FraudCategoryFlagDTO> result = fraudCategoryFlagService.partialUpdate(fraudCategoryFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraudCategoryFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fraud-category-flags} : get all the fraudCategoryFlags.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fraudCategoryFlags in body.
     */
    @GetMapping("/fraud-category-flags")
    public ResponseEntity<List<FraudCategoryFlagDTO>> getAllFraudCategoryFlags(Pageable pageable) {
        log.debug("REST request to get a page of FraudCategoryFlags");
        Page<FraudCategoryFlagDTO> page = fraudCategoryFlagService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fraud-category-flags/:id} : get the "id" fraudCategoryFlag.
     *
     * @param id the id of the fraudCategoryFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fraudCategoryFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fraud-category-flags/{id}")
    public ResponseEntity<FraudCategoryFlagDTO> getFraudCategoryFlag(@PathVariable Long id) {
        log.debug("REST request to get FraudCategoryFlag : {}", id);
        Optional<FraudCategoryFlagDTO> fraudCategoryFlagDTO = fraudCategoryFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fraudCategoryFlagDTO);
    }

    /**
     * {@code DELETE  /fraud-category-flags/:id} : delete the "id" fraudCategoryFlag.
     *
     * @param id the id of the fraudCategoryFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fraud-category-flags/{id}")
    public ResponseEntity<Void> deleteFraudCategoryFlag(@PathVariable Long id) {
        log.debug("REST request to delete FraudCategoryFlag : {}", id);
        fraudCategoryFlagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fraud-category-flags?query=:query} : search for the fraudCategoryFlag corresponding
     * to the query.
     *
     * @param query the query of the fraudCategoryFlag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fraud-category-flags")
    public ResponseEntity<List<FraudCategoryFlagDTO>> searchFraudCategoryFlags(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FraudCategoryFlags for query {}", query);
        Page<FraudCategoryFlagDTO> page = fraudCategoryFlagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
