package io.github.erp.web.rest;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.FxRateTypeRepository;
import io.github.erp.service.FxRateTypeQueryService;
import io.github.erp.service.FxRateTypeService;
import io.github.erp.service.criteria.FxRateTypeCriteria;
import io.github.erp.service.dto.FxRateTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FxRateType}.
 */
@RestController
@RequestMapping("/api")
public class FxRateTypeResource {

    private final Logger log = LoggerFactory.getLogger(FxRateTypeResource.class);

    private static final String ENTITY_NAME = "fxRateType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FxRateTypeService fxRateTypeService;

    private final FxRateTypeRepository fxRateTypeRepository;

    private final FxRateTypeQueryService fxRateTypeQueryService;

    public FxRateTypeResource(
        FxRateTypeService fxRateTypeService,
        FxRateTypeRepository fxRateTypeRepository,
        FxRateTypeQueryService fxRateTypeQueryService
    ) {
        this.fxRateTypeService = fxRateTypeService;
        this.fxRateTypeRepository = fxRateTypeRepository;
        this.fxRateTypeQueryService = fxRateTypeQueryService;
    }

    /**
     * {@code POST  /fx-rate-types} : Create a new fxRateType.
     *
     * @param fxRateTypeDTO the fxRateTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fxRateTypeDTO, or with status {@code 400 (Bad Request)} if the fxRateType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fx-rate-types")
    public ResponseEntity<FxRateTypeDTO> createFxRateType(@Valid @RequestBody FxRateTypeDTO fxRateTypeDTO) throws URISyntaxException {
        log.debug("REST request to save FxRateType : {}", fxRateTypeDTO);
        if (fxRateTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new fxRateType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FxRateTypeDTO result = fxRateTypeService.save(fxRateTypeDTO);
        return ResponseEntity
            .created(new URI("/api/fx-rate-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fx-rate-types/:id} : Updates an existing fxRateType.
     *
     * @param id the id of the fxRateTypeDTO to save.
     * @param fxRateTypeDTO the fxRateTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fxRateTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fxRateTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fxRateTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fx-rate-types/{id}")
    public ResponseEntity<FxRateTypeDTO> updateFxRateType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FxRateTypeDTO fxRateTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FxRateType : {}, {}", id, fxRateTypeDTO);
        if (fxRateTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fxRateTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fxRateTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FxRateTypeDTO result = fxRateTypeService.save(fxRateTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fxRateTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fx-rate-types/:id} : Partial updates given fields of an existing fxRateType, field will ignore if it is null
     *
     * @param id the id of the fxRateTypeDTO to save.
     * @param fxRateTypeDTO the fxRateTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fxRateTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fxRateTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fxRateTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fxRateTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fx-rate-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FxRateTypeDTO> partialUpdateFxRateType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FxRateTypeDTO fxRateTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FxRateType partially : {}, {}", id, fxRateTypeDTO);
        if (fxRateTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fxRateTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fxRateTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FxRateTypeDTO> result = fxRateTypeService.partialUpdate(fxRateTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fxRateTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fx-rate-types} : get all the fxRateTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fxRateTypes in body.
     */
    @GetMapping("/fx-rate-types")
    public ResponseEntity<List<FxRateTypeDTO>> getAllFxRateTypes(FxRateTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FxRateTypes by criteria: {}", criteria);
        Page<FxRateTypeDTO> page = fxRateTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fx-rate-types/count} : count all the fxRateTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fx-rate-types/count")
    public ResponseEntity<Long> countFxRateTypes(FxRateTypeCriteria criteria) {
        log.debug("REST request to count FxRateTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(fxRateTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fx-rate-types/:id} : get the "id" fxRateType.
     *
     * @param id the id of the fxRateTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fxRateTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fx-rate-types/{id}")
    public ResponseEntity<FxRateTypeDTO> getFxRateType(@PathVariable Long id) {
        log.debug("REST request to get FxRateType : {}", id);
        Optional<FxRateTypeDTO> fxRateTypeDTO = fxRateTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fxRateTypeDTO);
    }

    /**
     * {@code DELETE  /fx-rate-types/:id} : delete the "id" fxRateType.
     *
     * @param id the id of the fxRateTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fx-rate-types/{id}")
    public ResponseEntity<Void> deleteFxRateType(@PathVariable Long id) {
        log.debug("REST request to delete FxRateType : {}", id);
        fxRateTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fx-rate-types?query=:query} : search for the fxRateType corresponding
     * to the query.
     *
     * @param query the query of the fxRateType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fx-rate-types")
    public ResponseEntity<List<FxRateTypeDTO>> searchFxRateTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FxRateTypes for query {}", query);
        Page<FxRateTypeDTO> page = fxRateTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
