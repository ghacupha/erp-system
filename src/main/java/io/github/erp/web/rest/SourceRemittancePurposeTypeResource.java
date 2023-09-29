package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

import io.github.erp.repository.SourceRemittancePurposeTypeRepository;
import io.github.erp.service.SourceRemittancePurposeTypeQueryService;
import io.github.erp.service.SourceRemittancePurposeTypeService;
import io.github.erp.service.criteria.SourceRemittancePurposeTypeCriteria;
import io.github.erp.service.dto.SourceRemittancePurposeTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SourceRemittancePurposeType}.
 */
@RestController
@RequestMapping("/api")
public class SourceRemittancePurposeTypeResource {

    private final Logger log = LoggerFactory.getLogger(SourceRemittancePurposeTypeResource.class);

    private static final String ENTITY_NAME = "sourceRemittancePurposeType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SourceRemittancePurposeTypeService sourceRemittancePurposeTypeService;

    private final SourceRemittancePurposeTypeRepository sourceRemittancePurposeTypeRepository;

    private final SourceRemittancePurposeTypeQueryService sourceRemittancePurposeTypeQueryService;

    public SourceRemittancePurposeTypeResource(
        SourceRemittancePurposeTypeService sourceRemittancePurposeTypeService,
        SourceRemittancePurposeTypeRepository sourceRemittancePurposeTypeRepository,
        SourceRemittancePurposeTypeQueryService sourceRemittancePurposeTypeQueryService
    ) {
        this.sourceRemittancePurposeTypeService = sourceRemittancePurposeTypeService;
        this.sourceRemittancePurposeTypeRepository = sourceRemittancePurposeTypeRepository;
        this.sourceRemittancePurposeTypeQueryService = sourceRemittancePurposeTypeQueryService;
    }

    /**
     * {@code POST  /source-remittance-purpose-types} : Create a new sourceRemittancePurposeType.
     *
     * @param sourceRemittancePurposeTypeDTO the sourceRemittancePurposeTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sourceRemittancePurposeTypeDTO, or with status {@code 400 (Bad Request)} if the sourceRemittancePurposeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/source-remittance-purpose-types")
    public ResponseEntity<SourceRemittancePurposeTypeDTO> createSourceRemittancePurposeType(
        @Valid @RequestBody SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SourceRemittancePurposeType : {}", sourceRemittancePurposeTypeDTO);
        if (sourceRemittancePurposeTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new sourceRemittancePurposeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SourceRemittancePurposeTypeDTO result = sourceRemittancePurposeTypeService.save(sourceRemittancePurposeTypeDTO);
        return ResponseEntity
            .created(new URI("/api/source-remittance-purpose-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /source-remittance-purpose-types/:id} : Updates an existing sourceRemittancePurposeType.
     *
     * @param id the id of the sourceRemittancePurposeTypeDTO to save.
     * @param sourceRemittancePurposeTypeDTO the sourceRemittancePurposeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceRemittancePurposeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the sourceRemittancePurposeTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sourceRemittancePurposeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/source-remittance-purpose-types/{id}")
    public ResponseEntity<SourceRemittancePurposeTypeDTO> updateSourceRemittancePurposeType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SourceRemittancePurposeType : {}, {}", id, sourceRemittancePurposeTypeDTO);
        if (sourceRemittancePurposeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceRemittancePurposeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceRemittancePurposeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SourceRemittancePurposeTypeDTO result = sourceRemittancePurposeTypeService.save(sourceRemittancePurposeTypeDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceRemittancePurposeTypeDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /source-remittance-purpose-types/:id} : Partial updates given fields of an existing sourceRemittancePurposeType, field will ignore if it is null
     *
     * @param id the id of the sourceRemittancePurposeTypeDTO to save.
     * @param sourceRemittancePurposeTypeDTO the sourceRemittancePurposeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sourceRemittancePurposeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the sourceRemittancePurposeTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the sourceRemittancePurposeTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the sourceRemittancePurposeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/source-remittance-purpose-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SourceRemittancePurposeTypeDTO> partialUpdateSourceRemittancePurposeType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SourceRemittancePurposeTypeDTO sourceRemittancePurposeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SourceRemittancePurposeType partially : {}, {}", id, sourceRemittancePurposeTypeDTO);
        if (sourceRemittancePurposeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, sourceRemittancePurposeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!sourceRemittancePurposeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SourceRemittancePurposeTypeDTO> result = sourceRemittancePurposeTypeService.partialUpdate(sourceRemittancePurposeTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sourceRemittancePurposeTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /source-remittance-purpose-types} : get all the sourceRemittancePurposeTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sourceRemittancePurposeTypes in body.
     */
    @GetMapping("/source-remittance-purpose-types")
    public ResponseEntity<List<SourceRemittancePurposeTypeDTO>> getAllSourceRemittancePurposeTypes(
        SourceRemittancePurposeTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get SourceRemittancePurposeTypes by criteria: {}", criteria);
        Page<SourceRemittancePurposeTypeDTO> page = sourceRemittancePurposeTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /source-remittance-purpose-types/count} : count all the sourceRemittancePurposeTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/source-remittance-purpose-types/count")
    public ResponseEntity<Long> countSourceRemittancePurposeTypes(SourceRemittancePurposeTypeCriteria criteria) {
        log.debug("REST request to count SourceRemittancePurposeTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(sourceRemittancePurposeTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /source-remittance-purpose-types/:id} : get the "id" sourceRemittancePurposeType.
     *
     * @param id the id of the sourceRemittancePurposeTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sourceRemittancePurposeTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/source-remittance-purpose-types/{id}")
    public ResponseEntity<SourceRemittancePurposeTypeDTO> getSourceRemittancePurposeType(@PathVariable Long id) {
        log.debug("REST request to get SourceRemittancePurposeType : {}", id);
        Optional<SourceRemittancePurposeTypeDTO> sourceRemittancePurposeTypeDTO = sourceRemittancePurposeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(sourceRemittancePurposeTypeDTO);
    }

    /**
     * {@code DELETE  /source-remittance-purpose-types/:id} : delete the "id" sourceRemittancePurposeType.
     *
     * @param id the id of the sourceRemittancePurposeTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/source-remittance-purpose-types/{id}")
    public ResponseEntity<Void> deleteSourceRemittancePurposeType(@PathVariable Long id) {
        log.debug("REST request to delete SourceRemittancePurposeType : {}", id);
        sourceRemittancePurposeTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/source-remittance-purpose-types?query=:query} : search for the sourceRemittancePurposeType corresponding
     * to the query.
     *
     * @param query the query of the sourceRemittancePurposeType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/source-remittance-purpose-types")
    public ResponseEntity<List<SourceRemittancePurposeTypeDTO>> searchSourceRemittancePurposeTypes(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of SourceRemittancePurposeTypes for query {}", query);
        Page<SourceRemittancePurposeTypeDTO> page = sourceRemittancePurposeTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
