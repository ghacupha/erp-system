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

import io.github.erp.repository.FraudTypeRepository;
import io.github.erp.service.FraudTypeQueryService;
import io.github.erp.service.FraudTypeService;
import io.github.erp.service.criteria.FraudTypeCriteria;
import io.github.erp.service.dto.FraudTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FraudType}.
 */
@RestController
@RequestMapping("/api")
public class FraudTypeResource {

    private final Logger log = LoggerFactory.getLogger(FraudTypeResource.class);

    private static final String ENTITY_NAME = "fraudType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FraudTypeService fraudTypeService;

    private final FraudTypeRepository fraudTypeRepository;

    private final FraudTypeQueryService fraudTypeQueryService;

    public FraudTypeResource(
        FraudTypeService fraudTypeService,
        FraudTypeRepository fraudTypeRepository,
        FraudTypeQueryService fraudTypeQueryService
    ) {
        this.fraudTypeService = fraudTypeService;
        this.fraudTypeRepository = fraudTypeRepository;
        this.fraudTypeQueryService = fraudTypeQueryService;
    }

    /**
     * {@code POST  /fraud-types} : Create a new fraudType.
     *
     * @param fraudTypeDTO the fraudTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fraudTypeDTO, or with status {@code 400 (Bad Request)} if the fraudType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fraud-types")
    public ResponseEntity<FraudTypeDTO> createFraudType(@Valid @RequestBody FraudTypeDTO fraudTypeDTO) throws URISyntaxException {
        log.debug("REST request to save FraudType : {}", fraudTypeDTO);
        if (fraudTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new fraudType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FraudTypeDTO result = fraudTypeService.save(fraudTypeDTO);
        return ResponseEntity
            .created(new URI("/api/fraud-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fraud-types/:id} : Updates an existing fraudType.
     *
     * @param id the id of the fraudTypeDTO to save.
     * @param fraudTypeDTO the fraudTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraudTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fraudTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fraudTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fraud-types/{id}")
    public ResponseEntity<FraudTypeDTO> updateFraudType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FraudTypeDTO fraudTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FraudType : {}, {}", id, fraudTypeDTO);
        if (fraudTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraudTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraudTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FraudTypeDTO result = fraudTypeService.save(fraudTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraudTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fraud-types/:id} : Partial updates given fields of an existing fraudType, field will ignore if it is null
     *
     * @param id the id of the fraudTypeDTO to save.
     * @param fraudTypeDTO the fraudTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fraudTypeDTO,
     * or with status {@code 400 (Bad Request)} if the fraudTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fraudTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fraudTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fraud-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FraudTypeDTO> partialUpdateFraudType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FraudTypeDTO fraudTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FraudType partially : {}, {}", id, fraudTypeDTO);
        if (fraudTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fraudTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fraudTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FraudTypeDTO> result = fraudTypeService.partialUpdate(fraudTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fraudTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fraud-types} : get all the fraudTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fraudTypes in body.
     */
    @GetMapping("/fraud-types")
    public ResponseEntity<List<FraudTypeDTO>> getAllFraudTypes(FraudTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FraudTypes by criteria: {}", criteria);
        Page<FraudTypeDTO> page = fraudTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fraud-types/count} : count all the fraudTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fraud-types/count")
    public ResponseEntity<Long> countFraudTypes(FraudTypeCriteria criteria) {
        log.debug("REST request to count FraudTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(fraudTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fraud-types/:id} : get the "id" fraudType.
     *
     * @param id the id of the fraudTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fraudTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fraud-types/{id}")
    public ResponseEntity<FraudTypeDTO> getFraudType(@PathVariable Long id) {
        log.debug("REST request to get FraudType : {}", id);
        Optional<FraudTypeDTO> fraudTypeDTO = fraudTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fraudTypeDTO);
    }

    /**
     * {@code DELETE  /fraud-types/:id} : delete the "id" fraudType.
     *
     * @param id the id of the fraudTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fraud-types/{id}")
    public ResponseEntity<Void> deleteFraudType(@PathVariable Long id) {
        log.debug("REST request to delete FraudType : {}", id);
        fraudTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fraud-types?query=:query} : search for the fraudType corresponding
     * to the query.
     *
     * @param query the query of the fraudType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fraud-types")
    public ResponseEntity<List<FraudTypeDTO>> searchFraudTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FraudTypes for query {}", query);
        Page<FraudTypeDTO> page = fraudTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
