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
import io.github.erp.repository.DerivativeSubTypeRepository;
import io.github.erp.service.DerivativeSubTypeQueryService;
import io.github.erp.service.DerivativeSubTypeService;
import io.github.erp.service.criteria.DerivativeSubTypeCriteria;
import io.github.erp.service.dto.DerivativeSubTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DerivativeSubType}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class DerivativeSubTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(DerivativeSubTypeResourceProd.class);

    private static final String ENTITY_NAME = "derivativeSubType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DerivativeSubTypeService derivativeSubTypeService;

    private final DerivativeSubTypeRepository derivativeSubTypeRepository;

    private final DerivativeSubTypeQueryService derivativeSubTypeQueryService;

    public DerivativeSubTypeResourceProd(
        DerivativeSubTypeService derivativeSubTypeService,
        DerivativeSubTypeRepository derivativeSubTypeRepository,
        DerivativeSubTypeQueryService derivativeSubTypeQueryService
    ) {
        this.derivativeSubTypeService = derivativeSubTypeService;
        this.derivativeSubTypeRepository = derivativeSubTypeRepository;
        this.derivativeSubTypeQueryService = derivativeSubTypeQueryService;
    }

    /**
     * {@code POST  /derivative-sub-types} : Create a new derivativeSubType.
     *
     * @param derivativeSubTypeDTO the derivativeSubTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new derivativeSubTypeDTO, or with status {@code 400 (Bad Request)} if the derivativeSubType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/derivative-sub-types")
    public ResponseEntity<DerivativeSubTypeDTO> createDerivativeSubType(@Valid @RequestBody DerivativeSubTypeDTO derivativeSubTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save DerivativeSubType : {}", derivativeSubTypeDTO);
        if (derivativeSubTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new derivativeSubType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DerivativeSubTypeDTO result = derivativeSubTypeService.save(derivativeSubTypeDTO);
        return ResponseEntity
            .created(new URI("/api/derivative-sub-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /derivative-sub-types/:id} : Updates an existing derivativeSubType.
     *
     * @param id the id of the derivativeSubTypeDTO to save.
     * @param derivativeSubTypeDTO the derivativeSubTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated derivativeSubTypeDTO,
     * or with status {@code 400 (Bad Request)} if the derivativeSubTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the derivativeSubTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/derivative-sub-types/{id}")
    public ResponseEntity<DerivativeSubTypeDTO> updateDerivativeSubType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DerivativeSubTypeDTO derivativeSubTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DerivativeSubType : {}, {}", id, derivativeSubTypeDTO);
        if (derivativeSubTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, derivativeSubTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!derivativeSubTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DerivativeSubTypeDTO result = derivativeSubTypeService.save(derivativeSubTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, derivativeSubTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /derivative-sub-types/:id} : Partial updates given fields of an existing derivativeSubType, field will ignore if it is null
     *
     * @param id the id of the derivativeSubTypeDTO to save.
     * @param derivativeSubTypeDTO the derivativeSubTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated derivativeSubTypeDTO,
     * or with status {@code 400 (Bad Request)} if the derivativeSubTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the derivativeSubTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the derivativeSubTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/derivative-sub-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DerivativeSubTypeDTO> partialUpdateDerivativeSubType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DerivativeSubTypeDTO derivativeSubTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DerivativeSubType partially : {}, {}", id, derivativeSubTypeDTO);
        if (derivativeSubTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, derivativeSubTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!derivativeSubTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DerivativeSubTypeDTO> result = derivativeSubTypeService.partialUpdate(derivativeSubTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, derivativeSubTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /derivative-sub-types} : get all the derivativeSubTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of derivativeSubTypes in body.
     */
    @GetMapping("/derivative-sub-types")
    public ResponseEntity<List<DerivativeSubTypeDTO>> getAllDerivativeSubTypes(DerivativeSubTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DerivativeSubTypes by criteria: {}", criteria);
        Page<DerivativeSubTypeDTO> page = derivativeSubTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /derivative-sub-types/count} : count all the derivativeSubTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/derivative-sub-types/count")
    public ResponseEntity<Long> countDerivativeSubTypes(DerivativeSubTypeCriteria criteria) {
        log.debug("REST request to count DerivativeSubTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(derivativeSubTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /derivative-sub-types/:id} : get the "id" derivativeSubType.
     *
     * @param id the id of the derivativeSubTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the derivativeSubTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/derivative-sub-types/{id}")
    public ResponseEntity<DerivativeSubTypeDTO> getDerivativeSubType(@PathVariable Long id) {
        log.debug("REST request to get DerivativeSubType : {}", id);
        Optional<DerivativeSubTypeDTO> derivativeSubTypeDTO = derivativeSubTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(derivativeSubTypeDTO);
    }

    /**
     * {@code DELETE  /derivative-sub-types/:id} : delete the "id" derivativeSubType.
     *
     * @param id the id of the derivativeSubTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/derivative-sub-types/{id}")
    public ResponseEntity<Void> deleteDerivativeSubType(@PathVariable Long id) {
        log.debug("REST request to delete DerivativeSubType : {}", id);
        derivativeSubTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/derivative-sub-types?query=:query} : search for the derivativeSubType corresponding
     * to the query.
     *
     * @param query the query of the derivativeSubType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/derivative-sub-types")
    public ResponseEntity<List<DerivativeSubTypeDTO>> searchDerivativeSubTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DerivativeSubTypes for query {}", query);
        Page<DerivativeSubTypeDTO> page = derivativeSubTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
