package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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

import io.github.erp.repository.FxCustomerTypeRepository;
import io.github.erp.service.FxCustomerTypeQueryService;
import io.github.erp.service.FxCustomerTypeService;
import io.github.erp.service.criteria.FxCustomerTypeCriteria;
import io.github.erp.service.dto.FxCustomerTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FxCustomerType}.
 */
@RestController
@RequestMapping("/api")
public class FxCustomerTypeResource {

    private final Logger log = LoggerFactory.getLogger(FxCustomerTypeResource.class);

    private static final String ENTITY_NAME = "fxCustomerType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FxCustomerTypeService fxCustomerTypeService;

    private final FxCustomerTypeRepository fxCustomerTypeRepository;

    private final FxCustomerTypeQueryService fxCustomerTypeQueryService;

    public FxCustomerTypeResource(
        FxCustomerTypeService fxCustomerTypeService,
        FxCustomerTypeRepository fxCustomerTypeRepository,
        FxCustomerTypeQueryService fxCustomerTypeQueryService
    ) {
        this.fxCustomerTypeService = fxCustomerTypeService;
        this.fxCustomerTypeRepository = fxCustomerTypeRepository;
        this.fxCustomerTypeQueryService = fxCustomerTypeQueryService;
    }

    /**
     * {@code POST  /fx-customer-types} : Create a new fxCustomerType.
     *
     * @param fxCustomerTypeDTO the fxCustomerTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fxCustomerTypeDTO, or with status {@code 400 (Bad Request)} if the fxCustomerType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fx-customer-types")
    public ResponseEntity<FxCustomerTypeDTO> createFxCustomerType(@Valid @RequestBody FxCustomerTypeDTO fxCustomerTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save FxCustomerType : {}", fxCustomerTypeDTO);
        if (fxCustomerTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new fxCustomerType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FxCustomerTypeDTO result = fxCustomerTypeService.save(fxCustomerTypeDTO);
        return ResponseEntity
            .created(new URI("/api/fx-customer-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fx-customer-types/:id} : Updates an existing fxCustomerType.
     *
     * @param id the id of the fxCustomerTypeDTO to save.
     * @param fxCustomerTypeDTO the fxCustomerTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fxCustomerTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fxCustomerTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fxCustomerTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fx-customer-types/{id}")
    public ResponseEntity<FxCustomerTypeDTO> updateFxCustomerType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FxCustomerTypeDTO fxCustomerTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FxCustomerType : {}, {}", id, fxCustomerTypeDTO);
        if (fxCustomerTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fxCustomerTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fxCustomerTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FxCustomerTypeDTO result = fxCustomerTypeService.save(fxCustomerTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fxCustomerTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fx-customer-types/:id} : Partial updates given fields of an existing fxCustomerType, field will ignore if it is null
     *
     * @param id the id of the fxCustomerTypeDTO to save.
     * @param fxCustomerTypeDTO the fxCustomerTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fxCustomerTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fxCustomerTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fxCustomerTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fxCustomerTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fx-customer-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FxCustomerTypeDTO> partialUpdateFxCustomerType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FxCustomerTypeDTO fxCustomerTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FxCustomerType partially : {}, {}", id, fxCustomerTypeDTO);
        if (fxCustomerTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fxCustomerTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fxCustomerTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FxCustomerTypeDTO> result = fxCustomerTypeService.partialUpdate(fxCustomerTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fxCustomerTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fx-customer-types} : get all the fxCustomerTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fxCustomerTypes in body.
     */
    @GetMapping("/fx-customer-types")
    public ResponseEntity<List<FxCustomerTypeDTO>> getAllFxCustomerTypes(FxCustomerTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FxCustomerTypes by criteria: {}", criteria);
        Page<FxCustomerTypeDTO> page = fxCustomerTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fx-customer-types/count} : count all the fxCustomerTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fx-customer-types/count")
    public ResponseEntity<Long> countFxCustomerTypes(FxCustomerTypeCriteria criteria) {
        log.debug("REST request to count FxCustomerTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(fxCustomerTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fx-customer-types/:id} : get the "id" fxCustomerType.
     *
     * @param id the id of the fxCustomerTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fxCustomerTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fx-customer-types/{id}")
    public ResponseEntity<FxCustomerTypeDTO> getFxCustomerType(@PathVariable Long id) {
        log.debug("REST request to get FxCustomerType : {}", id);
        Optional<FxCustomerTypeDTO> fxCustomerTypeDTO = fxCustomerTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fxCustomerTypeDTO);
    }

    /**
     * {@code DELETE  /fx-customer-types/:id} : delete the "id" fxCustomerType.
     *
     * @param id the id of the fxCustomerTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fx-customer-types/{id}")
    public ResponseEntity<Void> deleteFxCustomerType(@PathVariable Long id) {
        log.debug("REST request to delete FxCustomerType : {}", id);
        fxCustomerTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fx-customer-types?query=:query} : search for the fxCustomerType corresponding
     * to the query.
     *
     * @param query the query of the fxCustomerType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fx-customer-types")
    public ResponseEntity<List<FxCustomerTypeDTO>> searchFxCustomerTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FxCustomerTypes for query {}", query);
        Page<FxCustomerTypeDTO> page = fxCustomerTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
