package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

import io.github.erp.repository.FxReceiptPurposeTypeRepository;
import io.github.erp.service.FxReceiptPurposeTypeQueryService;
import io.github.erp.service.FxReceiptPurposeTypeService;
import io.github.erp.service.criteria.FxReceiptPurposeTypeCriteria;
import io.github.erp.service.dto.FxReceiptPurposeTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FxReceiptPurposeType}.
 */
@RestController
@RequestMapping("/api")
public class FxReceiptPurposeTypeResource {

    private final Logger log = LoggerFactory.getLogger(FxReceiptPurposeTypeResource.class);

    private static final String ENTITY_NAME = "fxReceiptPurposeType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FxReceiptPurposeTypeService fxReceiptPurposeTypeService;

    private final FxReceiptPurposeTypeRepository fxReceiptPurposeTypeRepository;

    private final FxReceiptPurposeTypeQueryService fxReceiptPurposeTypeQueryService;

    public FxReceiptPurposeTypeResource(
        FxReceiptPurposeTypeService fxReceiptPurposeTypeService,
        FxReceiptPurposeTypeRepository fxReceiptPurposeTypeRepository,
        FxReceiptPurposeTypeQueryService fxReceiptPurposeTypeQueryService
    ) {
        this.fxReceiptPurposeTypeService = fxReceiptPurposeTypeService;
        this.fxReceiptPurposeTypeRepository = fxReceiptPurposeTypeRepository;
        this.fxReceiptPurposeTypeQueryService = fxReceiptPurposeTypeQueryService;
    }

    /**
     * {@code POST  /fx-receipt-purpose-types} : Create a new fxReceiptPurposeType.
     *
     * @param fxReceiptPurposeTypeDTO the fxReceiptPurposeTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fxReceiptPurposeTypeDTO, or with status {@code 400 (Bad Request)} if the fxReceiptPurposeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fx-receipt-purpose-types")
    public ResponseEntity<FxReceiptPurposeTypeDTO> createFxReceiptPurposeType(
        @Valid @RequestBody FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FxReceiptPurposeType : {}", fxReceiptPurposeTypeDTO);
        if (fxReceiptPurposeTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new fxReceiptPurposeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FxReceiptPurposeTypeDTO result = fxReceiptPurposeTypeService.save(fxReceiptPurposeTypeDTO);
        return ResponseEntity
            .created(new URI("/api/fx-receipt-purpose-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fx-receipt-purpose-types/:id} : Updates an existing fxReceiptPurposeType.
     *
     * @param id the id of the fxReceiptPurposeTypeDTO to save.
     * @param fxReceiptPurposeTypeDTO the fxReceiptPurposeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fxReceiptPurposeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fxReceiptPurposeTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fxReceiptPurposeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fx-receipt-purpose-types/{id}")
    public ResponseEntity<FxReceiptPurposeTypeDTO> updateFxReceiptPurposeType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FxReceiptPurposeType : {}, {}", id, fxReceiptPurposeTypeDTO);
        if (fxReceiptPurposeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fxReceiptPurposeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fxReceiptPurposeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FxReceiptPurposeTypeDTO result = fxReceiptPurposeTypeService.save(fxReceiptPurposeTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fxReceiptPurposeTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fx-receipt-purpose-types/:id} : Partial updates given fields of an existing fxReceiptPurposeType, field will ignore if it is null
     *
     * @param id the id of the fxReceiptPurposeTypeDTO to save.
     * @param fxReceiptPurposeTypeDTO the fxReceiptPurposeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fxReceiptPurposeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fxReceiptPurposeTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fxReceiptPurposeTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fxReceiptPurposeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fx-receipt-purpose-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FxReceiptPurposeTypeDTO> partialUpdateFxReceiptPurposeType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FxReceiptPurposeTypeDTO fxReceiptPurposeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FxReceiptPurposeType partially : {}, {}", id, fxReceiptPurposeTypeDTO);
        if (fxReceiptPurposeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fxReceiptPurposeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fxReceiptPurposeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FxReceiptPurposeTypeDTO> result = fxReceiptPurposeTypeService.partialUpdate(fxReceiptPurposeTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fxReceiptPurposeTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fx-receipt-purpose-types} : get all the fxReceiptPurposeTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fxReceiptPurposeTypes in body.
     */
    @GetMapping("/fx-receipt-purpose-types")
    public ResponseEntity<List<FxReceiptPurposeTypeDTO>> getAllFxReceiptPurposeTypes(
        FxReceiptPurposeTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get FxReceiptPurposeTypes by criteria: {}", criteria);
        Page<FxReceiptPurposeTypeDTO> page = fxReceiptPurposeTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fx-receipt-purpose-types/count} : count all the fxReceiptPurposeTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fx-receipt-purpose-types/count")
    public ResponseEntity<Long> countFxReceiptPurposeTypes(FxReceiptPurposeTypeCriteria criteria) {
        log.debug("REST request to count FxReceiptPurposeTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(fxReceiptPurposeTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fx-receipt-purpose-types/:id} : get the "id" fxReceiptPurposeType.
     *
     * @param id the id of the fxReceiptPurposeTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fxReceiptPurposeTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fx-receipt-purpose-types/{id}")
    public ResponseEntity<FxReceiptPurposeTypeDTO> getFxReceiptPurposeType(@PathVariable Long id) {
        log.debug("REST request to get FxReceiptPurposeType : {}", id);
        Optional<FxReceiptPurposeTypeDTO> fxReceiptPurposeTypeDTO = fxReceiptPurposeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fxReceiptPurposeTypeDTO);
    }

    /**
     * {@code DELETE  /fx-receipt-purpose-types/:id} : delete the "id" fxReceiptPurposeType.
     *
     * @param id the id of the fxReceiptPurposeTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fx-receipt-purpose-types/{id}")
    public ResponseEntity<Void> deleteFxReceiptPurposeType(@PathVariable Long id) {
        log.debug("REST request to delete FxReceiptPurposeType : {}", id);
        fxReceiptPurposeTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fx-receipt-purpose-types?query=:query} : search for the fxReceiptPurposeType corresponding
     * to the query.
     *
     * @param query the query of the fxReceiptPurposeType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fx-receipt-purpose-types")
    public ResponseEntity<List<FxReceiptPurposeTypeDTO>> searchFxReceiptPurposeTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FxReceiptPurposeTypes for query {}", query);
        Page<FxReceiptPurposeTypeDTO> page = fxReceiptPurposeTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
