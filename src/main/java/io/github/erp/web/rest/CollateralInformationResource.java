package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 4 (Gideon Series) Server ver 1.5.8
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

import io.github.erp.repository.CollateralInformationRepository;
import io.github.erp.service.CollateralInformationQueryService;
import io.github.erp.service.CollateralInformationService;
import io.github.erp.service.criteria.CollateralInformationCriteria;
import io.github.erp.service.dto.CollateralInformationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CollateralInformation}.
 */
@RestController
@RequestMapping("/api")
public class CollateralInformationResource {

    private final Logger log = LoggerFactory.getLogger(CollateralInformationResource.class);

    private static final String ENTITY_NAME = "gdiDataCollateralInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollateralInformationService collateralInformationService;

    private final CollateralInformationRepository collateralInformationRepository;

    private final CollateralInformationQueryService collateralInformationQueryService;

    public CollateralInformationResource(
        CollateralInformationService collateralInformationService,
        CollateralInformationRepository collateralInformationRepository,
        CollateralInformationQueryService collateralInformationQueryService
    ) {
        this.collateralInformationService = collateralInformationService;
        this.collateralInformationRepository = collateralInformationRepository;
        this.collateralInformationQueryService = collateralInformationQueryService;
    }

    /**
     * {@code POST  /collateral-informations} : Create a new collateralInformation.
     *
     * @param collateralInformationDTO the collateralInformationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collateralInformationDTO, or with status {@code 400 (Bad Request)} if the collateralInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collateral-informations")
    public ResponseEntity<CollateralInformationDTO> createCollateralInformation(
        @Valid @RequestBody CollateralInformationDTO collateralInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CollateralInformation : {}", collateralInformationDTO);
        if (collateralInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new collateralInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollateralInformationDTO result = collateralInformationService.save(collateralInformationDTO);
        return ResponseEntity
            .created(new URI("/api/collateral-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collateral-informations/:id} : Updates an existing collateralInformation.
     *
     * @param id the id of the collateralInformationDTO to save.
     * @param collateralInformationDTO the collateralInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collateralInformationDTO,
     * or with status {@code 400 (Bad Request)} if the collateralInformationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collateralInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collateral-informations/{id}")
    public ResponseEntity<CollateralInformationDTO> updateCollateralInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CollateralInformationDTO collateralInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CollateralInformation : {}, {}", id, collateralInformationDTO);
        if (collateralInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collateralInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collateralInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CollateralInformationDTO result = collateralInformationService.save(collateralInformationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collateralInformationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /collateral-informations/:id} : Partial updates given fields of an existing collateralInformation, field will ignore if it is null
     *
     * @param id the id of the collateralInformationDTO to save.
     * @param collateralInformationDTO the collateralInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collateralInformationDTO,
     * or with status {@code 400 (Bad Request)} if the collateralInformationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the collateralInformationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the collateralInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/collateral-informations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CollateralInformationDTO> partialUpdateCollateralInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CollateralInformationDTO collateralInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CollateralInformation partially : {}, {}", id, collateralInformationDTO);
        if (collateralInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collateralInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collateralInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CollateralInformationDTO> result = collateralInformationService.partialUpdate(collateralInformationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collateralInformationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /collateral-informations} : get all the collateralInformations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collateralInformations in body.
     */
    @GetMapping("/collateral-informations")
    public ResponseEntity<List<CollateralInformationDTO>> getAllCollateralInformations(
        CollateralInformationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CollateralInformations by criteria: {}", criteria);
        Page<CollateralInformationDTO> page = collateralInformationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /collateral-informations/count} : count all the collateralInformations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/collateral-informations/count")
    public ResponseEntity<Long> countCollateralInformations(CollateralInformationCriteria criteria) {
        log.debug("REST request to count CollateralInformations by criteria: {}", criteria);
        return ResponseEntity.ok().body(collateralInformationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /collateral-informations/:id} : get the "id" collateralInformation.
     *
     * @param id the id of the collateralInformationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collateralInformationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collateral-informations/{id}")
    public ResponseEntity<CollateralInformationDTO> getCollateralInformation(@PathVariable Long id) {
        log.debug("REST request to get CollateralInformation : {}", id);
        Optional<CollateralInformationDTO> collateralInformationDTO = collateralInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collateralInformationDTO);
    }

    /**
     * {@code DELETE  /collateral-informations/:id} : delete the "id" collateralInformation.
     *
     * @param id the id of the collateralInformationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collateral-informations/{id}")
    public ResponseEntity<Void> deleteCollateralInformation(@PathVariable Long id) {
        log.debug("REST request to delete CollateralInformation : {}", id);
        collateralInformationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/collateral-informations?query=:query} : search for the collateralInformation corresponding
     * to the query.
     *
     * @param query the query of the collateralInformation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/collateral-informations")
    public ResponseEntity<List<CollateralInformationDTO>> searchCollateralInformations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CollateralInformations for query {}", query);
        Page<CollateralInformationDTO> page = collateralInformationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
