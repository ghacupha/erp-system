package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import io.github.erp.repository.FxTransactionChannelTypeRepository;
import io.github.erp.service.FxTransactionChannelTypeQueryService;
import io.github.erp.service.FxTransactionChannelTypeService;
import io.github.erp.service.criteria.FxTransactionChannelTypeCriteria;
import io.github.erp.service.dto.FxTransactionChannelTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FxTransactionChannelType}.
 */
@RestController
@RequestMapping("/api")
public class FxTransactionChannelTypeResource {

    private final Logger log = LoggerFactory.getLogger(FxTransactionChannelTypeResource.class);

    private static final String ENTITY_NAME = "fxTransactionChannelType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FxTransactionChannelTypeService fxTransactionChannelTypeService;

    private final FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository;

    private final FxTransactionChannelTypeQueryService fxTransactionChannelTypeQueryService;

    public FxTransactionChannelTypeResource(
        FxTransactionChannelTypeService fxTransactionChannelTypeService,
        FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository,
        FxTransactionChannelTypeQueryService fxTransactionChannelTypeQueryService
    ) {
        this.fxTransactionChannelTypeService = fxTransactionChannelTypeService;
        this.fxTransactionChannelTypeRepository = fxTransactionChannelTypeRepository;
        this.fxTransactionChannelTypeQueryService = fxTransactionChannelTypeQueryService;
    }

    /**
     * {@code POST  /fx-transaction-channel-types} : Create a new fxTransactionChannelType.
     *
     * @param fxTransactionChannelTypeDTO the fxTransactionChannelTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fxTransactionChannelTypeDTO, or with status {@code 400 (Bad Request)} if the fxTransactionChannelType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fx-transaction-channel-types")
    public ResponseEntity<FxTransactionChannelTypeDTO> createFxTransactionChannelType(
        @Valid @RequestBody FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FxTransactionChannelType : {}", fxTransactionChannelTypeDTO);
        if (fxTransactionChannelTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new fxTransactionChannelType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FxTransactionChannelTypeDTO result = fxTransactionChannelTypeService.save(fxTransactionChannelTypeDTO);
        return ResponseEntity
            .created(new URI("/api/fx-transaction-channel-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fx-transaction-channel-types/:id} : Updates an existing fxTransactionChannelType.
     *
     * @param id the id of the fxTransactionChannelTypeDTO to save.
     * @param fxTransactionChannelTypeDTO the fxTransactionChannelTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fxTransactionChannelTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fxTransactionChannelTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fxTransactionChannelTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fx-transaction-channel-types/{id}")
    public ResponseEntity<FxTransactionChannelTypeDTO> updateFxTransactionChannelType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FxTransactionChannelType : {}, {}", id, fxTransactionChannelTypeDTO);
        if (fxTransactionChannelTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fxTransactionChannelTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fxTransactionChannelTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FxTransactionChannelTypeDTO result = fxTransactionChannelTypeService.save(fxTransactionChannelTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fxTransactionChannelTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fx-transaction-channel-types/:id} : Partial updates given fields of an existing fxTransactionChannelType, field will ignore if it is null
     *
     * @param id the id of the fxTransactionChannelTypeDTO to save.
     * @param fxTransactionChannelTypeDTO the fxTransactionChannelTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fxTransactionChannelTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fxTransactionChannelTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fxTransactionChannelTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fxTransactionChannelTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fx-transaction-channel-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FxTransactionChannelTypeDTO> partialUpdateFxTransactionChannelType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FxTransactionChannelTypeDTO fxTransactionChannelTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FxTransactionChannelType partially : {}, {}", id, fxTransactionChannelTypeDTO);
        if (fxTransactionChannelTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fxTransactionChannelTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fxTransactionChannelTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FxTransactionChannelTypeDTO> result = fxTransactionChannelTypeService.partialUpdate(fxTransactionChannelTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fxTransactionChannelTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fx-transaction-channel-types} : get all the fxTransactionChannelTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fxTransactionChannelTypes in body.
     */
    @GetMapping("/fx-transaction-channel-types")
    public ResponseEntity<List<FxTransactionChannelTypeDTO>> getAllFxTransactionChannelTypes(
        FxTransactionChannelTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get FxTransactionChannelTypes by criteria: {}", criteria);
        Page<FxTransactionChannelTypeDTO> page = fxTransactionChannelTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fx-transaction-channel-types/count} : count all the fxTransactionChannelTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fx-transaction-channel-types/count")
    public ResponseEntity<Long> countFxTransactionChannelTypes(FxTransactionChannelTypeCriteria criteria) {
        log.debug("REST request to count FxTransactionChannelTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(fxTransactionChannelTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fx-transaction-channel-types/:id} : get the "id" fxTransactionChannelType.
     *
     * @param id the id of the fxTransactionChannelTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fxTransactionChannelTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fx-transaction-channel-types/{id}")
    public ResponseEntity<FxTransactionChannelTypeDTO> getFxTransactionChannelType(@PathVariable Long id) {
        log.debug("REST request to get FxTransactionChannelType : {}", id);
        Optional<FxTransactionChannelTypeDTO> fxTransactionChannelTypeDTO = fxTransactionChannelTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fxTransactionChannelTypeDTO);
    }

    /**
     * {@code DELETE  /fx-transaction-channel-types/:id} : delete the "id" fxTransactionChannelType.
     *
     * @param id the id of the fxTransactionChannelTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fx-transaction-channel-types/{id}")
    public ResponseEntity<Void> deleteFxTransactionChannelType(@PathVariable Long id) {
        log.debug("REST request to delete FxTransactionChannelType : {}", id);
        fxTransactionChannelTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fx-transaction-channel-types?query=:query} : search for the fxTransactionChannelType corresponding
     * to the query.
     *
     * @param query the query of the fxTransactionChannelType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fx-transaction-channel-types")
    public ResponseEntity<List<FxTransactionChannelTypeDTO>> searchFxTransactionChannelTypes(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of FxTransactionChannelTypes for query {}", query);
        Page<FxTransactionChannelTypeDTO> page = fxTransactionChannelTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
