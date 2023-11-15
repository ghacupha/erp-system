package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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

import io.github.erp.repository.FxTransactionRateTypeRepository;
import io.github.erp.service.FxTransactionRateTypeQueryService;
import io.github.erp.service.FxTransactionRateTypeService;
import io.github.erp.service.criteria.FxTransactionRateTypeCriteria;
import io.github.erp.service.dto.FxTransactionRateTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FxTransactionRateType}.
 */
@RestController
@RequestMapping("/api")
public class FxTransactionRateTypeResource {

    private final Logger log = LoggerFactory.getLogger(FxTransactionRateTypeResource.class);

    private static final String ENTITY_NAME = "fxTransactionRateType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FxTransactionRateTypeService fxTransactionRateTypeService;

    private final FxTransactionRateTypeRepository fxTransactionRateTypeRepository;

    private final FxTransactionRateTypeQueryService fxTransactionRateTypeQueryService;

    public FxTransactionRateTypeResource(
        FxTransactionRateTypeService fxTransactionRateTypeService,
        FxTransactionRateTypeRepository fxTransactionRateTypeRepository,
        FxTransactionRateTypeQueryService fxTransactionRateTypeQueryService
    ) {
        this.fxTransactionRateTypeService = fxTransactionRateTypeService;
        this.fxTransactionRateTypeRepository = fxTransactionRateTypeRepository;
        this.fxTransactionRateTypeQueryService = fxTransactionRateTypeQueryService;
    }

    /**
     * {@code POST  /fx-transaction-rate-types} : Create a new fxTransactionRateType.
     *
     * @param fxTransactionRateTypeDTO the fxTransactionRateTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fxTransactionRateTypeDTO, or with status {@code 400 (Bad Request)} if the fxTransactionRateType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fx-transaction-rate-types")
    public ResponseEntity<FxTransactionRateTypeDTO> createFxTransactionRateType(
        @Valid @RequestBody FxTransactionRateTypeDTO fxTransactionRateTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FxTransactionRateType : {}", fxTransactionRateTypeDTO);
        if (fxTransactionRateTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new fxTransactionRateType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FxTransactionRateTypeDTO result = fxTransactionRateTypeService.save(fxTransactionRateTypeDTO);
        return ResponseEntity
            .created(new URI("/api/fx-transaction-rate-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fx-transaction-rate-types/:id} : Updates an existing fxTransactionRateType.
     *
     * @param id the id of the fxTransactionRateTypeDTO to save.
     * @param fxTransactionRateTypeDTO the fxTransactionRateTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fxTransactionRateTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fxTransactionRateTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fxTransactionRateTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fx-transaction-rate-types/{id}")
    public ResponseEntity<FxTransactionRateTypeDTO> updateFxTransactionRateType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FxTransactionRateTypeDTO fxTransactionRateTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FxTransactionRateType : {}, {}", id, fxTransactionRateTypeDTO);
        if (fxTransactionRateTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fxTransactionRateTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fxTransactionRateTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FxTransactionRateTypeDTO result = fxTransactionRateTypeService.save(fxTransactionRateTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fxTransactionRateTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fx-transaction-rate-types/:id} : Partial updates given fields of an existing fxTransactionRateType, field will ignore if it is null
     *
     * @param id the id of the fxTransactionRateTypeDTO to save.
     * @param fxTransactionRateTypeDTO the fxTransactionRateTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fxTransactionRateTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fxTransactionRateTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fxTransactionRateTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fxTransactionRateTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fx-transaction-rate-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FxTransactionRateTypeDTO> partialUpdateFxTransactionRateType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FxTransactionRateTypeDTO fxTransactionRateTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FxTransactionRateType partially : {}, {}", id, fxTransactionRateTypeDTO);
        if (fxTransactionRateTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fxTransactionRateTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fxTransactionRateTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FxTransactionRateTypeDTO> result = fxTransactionRateTypeService.partialUpdate(fxTransactionRateTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fxTransactionRateTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fx-transaction-rate-types} : get all the fxTransactionRateTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fxTransactionRateTypes in body.
     */
    @GetMapping("/fx-transaction-rate-types")
    public ResponseEntity<List<FxTransactionRateTypeDTO>> getAllFxTransactionRateTypes(
        FxTransactionRateTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get FxTransactionRateTypes by criteria: {}", criteria);
        Page<FxTransactionRateTypeDTO> page = fxTransactionRateTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fx-transaction-rate-types/count} : count all the fxTransactionRateTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fx-transaction-rate-types/count")
    public ResponseEntity<Long> countFxTransactionRateTypes(FxTransactionRateTypeCriteria criteria) {
        log.debug("REST request to count FxTransactionRateTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(fxTransactionRateTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fx-transaction-rate-types/:id} : get the "id" fxTransactionRateType.
     *
     * @param id the id of the fxTransactionRateTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fxTransactionRateTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fx-transaction-rate-types/{id}")
    public ResponseEntity<FxTransactionRateTypeDTO> getFxTransactionRateType(@PathVariable Long id) {
        log.debug("REST request to get FxTransactionRateType : {}", id);
        Optional<FxTransactionRateTypeDTO> fxTransactionRateTypeDTO = fxTransactionRateTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fxTransactionRateTypeDTO);
    }

    /**
     * {@code DELETE  /fx-transaction-rate-types/:id} : delete the "id" fxTransactionRateType.
     *
     * @param id the id of the fxTransactionRateTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fx-transaction-rate-types/{id}")
    public ResponseEntity<Void> deleteFxTransactionRateType(@PathVariable Long id) {
        log.debug("REST request to delete FxTransactionRateType : {}", id);
        fxTransactionRateTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fx-transaction-rate-types?query=:query} : search for the fxTransactionRateType corresponding
     * to the query.
     *
     * @param query the query of the fxTransactionRateType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fx-transaction-rate-types")
    public ResponseEntity<List<FxTransactionRateTypeDTO>> searchFxTransactionRateTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FxTransactionRateTypes for query {}", query);
        Page<FxTransactionRateTypeDTO> page = fxTransactionRateTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
