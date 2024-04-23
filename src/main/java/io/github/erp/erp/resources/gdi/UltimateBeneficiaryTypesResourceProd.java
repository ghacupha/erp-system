package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.repository.UltimateBeneficiaryTypesRepository;
import io.github.erp.service.UltimateBeneficiaryTypesQueryService;
import io.github.erp.service.UltimateBeneficiaryTypesService;
import io.github.erp.service.criteria.UltimateBeneficiaryTypesCriteria;
import io.github.erp.service.dto.UltimateBeneficiaryTypesDTO;
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
 * REST controller for managing {@link io.github.erp.domain.UltimateBeneficiaryTypes}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class UltimateBeneficiaryTypesResourceProd {

    private final Logger log = LoggerFactory.getLogger(UltimateBeneficiaryTypesResourceProd.class);

    private static final String ENTITY_NAME = "ultimateBeneficiaryTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UltimateBeneficiaryTypesService ultimateBeneficiaryTypesService;

    private final UltimateBeneficiaryTypesRepository ultimateBeneficiaryTypesRepository;

    private final UltimateBeneficiaryTypesQueryService ultimateBeneficiaryTypesQueryService;

    public UltimateBeneficiaryTypesResourceProd(
        UltimateBeneficiaryTypesService ultimateBeneficiaryTypesService,
        UltimateBeneficiaryTypesRepository ultimateBeneficiaryTypesRepository,
        UltimateBeneficiaryTypesQueryService ultimateBeneficiaryTypesQueryService
    ) {
        this.ultimateBeneficiaryTypesService = ultimateBeneficiaryTypesService;
        this.ultimateBeneficiaryTypesRepository = ultimateBeneficiaryTypesRepository;
        this.ultimateBeneficiaryTypesQueryService = ultimateBeneficiaryTypesQueryService;
    }

    /**
     * {@code POST  /ultimate-beneficiary-types} : Create a new ultimateBeneficiaryTypes.
     *
     * @param ultimateBeneficiaryTypesDTO the ultimateBeneficiaryTypesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ultimateBeneficiaryTypesDTO, or with status {@code 400 (Bad Request)} if the ultimateBeneficiaryTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ultimate-beneficiary-types")
    public ResponseEntity<UltimateBeneficiaryTypesDTO> createUltimateBeneficiaryTypes(
        @Valid @RequestBody UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to save UltimateBeneficiaryTypes : {}", ultimateBeneficiaryTypesDTO);
        if (ultimateBeneficiaryTypesDTO.getId() != null) {
            throw new BadRequestAlertException("A new ultimateBeneficiaryTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        UltimateBeneficiaryTypesDTO result = ultimateBeneficiaryTypesService.save(ultimateBeneficiaryTypesDTO);
        return ResponseEntity
            .created(new URI("/api/ultimate-beneficiary-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ultimate-beneficiary-types/:id} : Updates an existing ultimateBeneficiaryTypes.
     *
     * @param id the id of the ultimateBeneficiaryTypesDTO to save.
     * @param ultimateBeneficiaryTypesDTO the ultimateBeneficiaryTypesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ultimateBeneficiaryTypesDTO,
     * or with status {@code 400 (Bad Request)} if the ultimateBeneficiaryTypesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ultimateBeneficiaryTypesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ultimate-beneficiary-types/{id}")
    public ResponseEntity<UltimateBeneficiaryTypesDTO> updateUltimateBeneficiaryTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UltimateBeneficiaryTypes : {}, {}", id, ultimateBeneficiaryTypesDTO);
        if (ultimateBeneficiaryTypesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ultimateBeneficiaryTypesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ultimateBeneficiaryTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        UltimateBeneficiaryTypesDTO result = ultimateBeneficiaryTypesService.save(ultimateBeneficiaryTypesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ultimateBeneficiaryTypesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ultimate-beneficiary-types/:id} : Partial updates given fields of an existing ultimateBeneficiaryTypes, field will ignore if it is null
     *
     * @param id the id of the ultimateBeneficiaryTypesDTO to save.
     * @param ultimateBeneficiaryTypesDTO the ultimateBeneficiaryTypesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ultimateBeneficiaryTypesDTO,
     * or with status {@code 400 (Bad Request)} if the ultimateBeneficiaryTypesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ultimateBeneficiaryTypesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ultimateBeneficiaryTypesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ultimate-beneficiary-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UltimateBeneficiaryTypesDTO> partialUpdateUltimateBeneficiaryTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UltimateBeneficiaryTypesDTO ultimateBeneficiaryTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UltimateBeneficiaryTypes partially : {}, {}", id, ultimateBeneficiaryTypesDTO);
        if (ultimateBeneficiaryTypesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ultimateBeneficiaryTypesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ultimateBeneficiaryTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UltimateBeneficiaryTypesDTO> result = ultimateBeneficiaryTypesService.partialUpdate(ultimateBeneficiaryTypesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ultimateBeneficiaryTypesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ultimate-beneficiary-types} : get all the ultimateBeneficiaryTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ultimateBeneficiaryTypes in body.
     */
    @GetMapping("/ultimate-beneficiary-types")
    public ResponseEntity<List<UltimateBeneficiaryTypesDTO>> getAllUltimateBeneficiaryTypes(
        UltimateBeneficiaryTypesCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get UltimateBeneficiaryTypes by criteria: {}", criteria);
        Page<UltimateBeneficiaryTypesDTO> page = ultimateBeneficiaryTypesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ultimate-beneficiary-types/count} : count all the ultimateBeneficiaryTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ultimate-beneficiary-types/count")
    public ResponseEntity<Long> countUltimateBeneficiaryTypes(UltimateBeneficiaryTypesCriteria criteria) {
        log.debug("REST request to count UltimateBeneficiaryTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(ultimateBeneficiaryTypesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ultimate-beneficiary-types/:id} : get the "id" ultimateBeneficiaryTypes.
     *
     * @param id the id of the ultimateBeneficiaryTypesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ultimateBeneficiaryTypesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ultimate-beneficiary-types/{id}")
    public ResponseEntity<UltimateBeneficiaryTypesDTO> getUltimateBeneficiaryTypes(@PathVariable Long id) {
        log.debug("REST request to get UltimateBeneficiaryTypes : {}", id);
        Optional<UltimateBeneficiaryTypesDTO> ultimateBeneficiaryTypesDTO = ultimateBeneficiaryTypesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ultimateBeneficiaryTypesDTO);
    }

    /**
     * {@code DELETE  /ultimate-beneficiary-types/:id} : delete the "id" ultimateBeneficiaryTypes.
     *
     * @param id the id of the ultimateBeneficiaryTypesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ultimate-beneficiary-types/{id}")
    public ResponseEntity<Void> deleteUltimateBeneficiaryTypes(@PathVariable Long id) {
        log.debug("REST request to delete UltimateBeneficiaryTypes : {}", id);
        ultimateBeneficiaryTypesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ultimate-beneficiary-types?query=:query} : search for the ultimateBeneficiaryTypes corresponding
     * to the query.
     *
     * @param query the query of the ultimateBeneficiaryTypes search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ultimate-beneficiary-types")
    public ResponseEntity<List<UltimateBeneficiaryTypesDTO>> searchUltimateBeneficiaryTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of UltimateBeneficiaryTypes for query {}", query);
        Page<UltimateBeneficiaryTypesDTO> page = ultimateBeneficiaryTypesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
