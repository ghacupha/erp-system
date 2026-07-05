package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.repository.CollateralTypeRepository;
import io.github.erp.service.CollateralTypeQueryService;
import io.github.erp.service.CollateralTypeService;
import io.github.erp.service.criteria.CollateralTypeCriteria;
import io.github.erp.service.dto.CollateralTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CollateralType}.
 */
@RestController
@RequestMapping("/api")
public class CollateralTypeResource {

    private final Logger log = LoggerFactory.getLogger(CollateralTypeResource.class);

    private static final String ENTITY_NAME = "collateralType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CollateralTypeService collateralTypeService;

    private final CollateralTypeRepository collateralTypeRepository;

    private final CollateralTypeQueryService collateralTypeQueryService;

    public CollateralTypeResource(
        CollateralTypeService collateralTypeService,
        CollateralTypeRepository collateralTypeRepository,
        CollateralTypeQueryService collateralTypeQueryService
    ) {
        this.collateralTypeService = collateralTypeService;
        this.collateralTypeRepository = collateralTypeRepository;
        this.collateralTypeQueryService = collateralTypeQueryService;
    }

    /**
     * {@code POST  /collateral-types} : Create a new collateralType.
     *
     * @param collateralTypeDTO the collateralTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new collateralTypeDTO, or with status {@code 400 (Bad Request)} if the collateralType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/collateral-types")
    public ResponseEntity<CollateralTypeDTO> createCollateralType(@Valid @RequestBody CollateralTypeDTO collateralTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CollateralType : {}", collateralTypeDTO);
        if (collateralTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new collateralType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CollateralTypeDTO result = collateralTypeService.save(collateralTypeDTO);
        return ResponseEntity
            .created(new URI("/api/collateral-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /collateral-types/:id} : Updates an existing collateralType.
     *
     * @param id the id of the collateralTypeDTO to save.
     * @param collateralTypeDTO the collateralTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collateralTypeDTO,
     * or with status {@code 400 (Bad Request)} if the collateralTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the collateralTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/collateral-types/{id}")
    public ResponseEntity<CollateralTypeDTO> updateCollateralType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CollateralTypeDTO collateralTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CollateralType : {}, {}", id, collateralTypeDTO);
        if (collateralTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collateralTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collateralTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CollateralTypeDTO result = collateralTypeService.save(collateralTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collateralTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /collateral-types/:id} : Partial updates given fields of an existing collateralType, field will ignore if it is null
     *
     * @param id the id of the collateralTypeDTO to save.
     * @param collateralTypeDTO the collateralTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated collateralTypeDTO,
     * or with status {@code 400 (Bad Request)} if the collateralTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the collateralTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the collateralTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/collateral-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CollateralTypeDTO> partialUpdateCollateralType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CollateralTypeDTO collateralTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CollateralType partially : {}, {}", id, collateralTypeDTO);
        if (collateralTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, collateralTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!collateralTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CollateralTypeDTO> result = collateralTypeService.partialUpdate(collateralTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, collateralTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /collateral-types} : get all the collateralTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of collateralTypes in body.
     */
    @GetMapping("/collateral-types")
    public ResponseEntity<List<CollateralTypeDTO>> getAllCollateralTypes(CollateralTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CollateralTypes by criteria: {}", criteria);
        Page<CollateralTypeDTO> page = collateralTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /collateral-types/count} : count all the collateralTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/collateral-types/count")
    public ResponseEntity<Long> countCollateralTypes(CollateralTypeCriteria criteria) {
        log.debug("REST request to count CollateralTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(collateralTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /collateral-types/:id} : get the "id" collateralType.
     *
     * @param id the id of the collateralTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the collateralTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/collateral-types/{id}")
    public ResponseEntity<CollateralTypeDTO> getCollateralType(@PathVariable Long id) {
        log.debug("REST request to get CollateralType : {}", id);
        Optional<CollateralTypeDTO> collateralTypeDTO = collateralTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(collateralTypeDTO);
    }

    /**
     * {@code DELETE  /collateral-types/:id} : delete the "id" collateralType.
     *
     * @param id the id of the collateralTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/collateral-types/{id}")
    public ResponseEntity<Void> deleteCollateralType(@PathVariable Long id) {
        log.debug("REST request to delete CollateralType : {}", id);
        collateralTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/collateral-types?query=:query} : search for the collateralType corresponding
     * to the query.
     *
     * @param query the query of the collateralType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/collateral-types")
    public ResponseEntity<List<CollateralTypeDTO>> searchCollateralTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CollateralTypes for query {}", query);
        Page<CollateralTypeDTO> page = collateralTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
