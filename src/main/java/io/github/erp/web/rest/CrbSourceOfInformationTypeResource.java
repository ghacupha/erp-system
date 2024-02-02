package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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

import io.github.erp.repository.CrbSourceOfInformationTypeRepository;
import io.github.erp.service.CrbSourceOfInformationTypeQueryService;
import io.github.erp.service.CrbSourceOfInformationTypeService;
import io.github.erp.service.criteria.CrbSourceOfInformationTypeCriteria;
import io.github.erp.service.dto.CrbSourceOfInformationTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbSourceOfInformationType}.
 */
@RestController
@RequestMapping("/api")
public class CrbSourceOfInformationTypeResource {

    private final Logger log = LoggerFactory.getLogger(CrbSourceOfInformationTypeResource.class);

    private static final String ENTITY_NAME = "crbSourceOfInformationType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbSourceOfInformationTypeService crbSourceOfInformationTypeService;

    private final CrbSourceOfInformationTypeRepository crbSourceOfInformationTypeRepository;

    private final CrbSourceOfInformationTypeQueryService crbSourceOfInformationTypeQueryService;

    public CrbSourceOfInformationTypeResource(
        CrbSourceOfInformationTypeService crbSourceOfInformationTypeService,
        CrbSourceOfInformationTypeRepository crbSourceOfInformationTypeRepository,
        CrbSourceOfInformationTypeQueryService crbSourceOfInformationTypeQueryService
    ) {
        this.crbSourceOfInformationTypeService = crbSourceOfInformationTypeService;
        this.crbSourceOfInformationTypeRepository = crbSourceOfInformationTypeRepository;
        this.crbSourceOfInformationTypeQueryService = crbSourceOfInformationTypeQueryService;
    }

    /**
     * {@code POST  /crb-source-of-information-types} : Create a new crbSourceOfInformationType.
     *
     * @param crbSourceOfInformationTypeDTO the crbSourceOfInformationTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbSourceOfInformationTypeDTO, or with status {@code 400 (Bad Request)} if the crbSourceOfInformationType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-source-of-information-types")
    public ResponseEntity<CrbSourceOfInformationTypeDTO> createCrbSourceOfInformationType(
        @Valid @RequestBody CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbSourceOfInformationType : {}", crbSourceOfInformationTypeDTO);
        if (crbSourceOfInformationTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbSourceOfInformationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbSourceOfInformationTypeDTO result = crbSourceOfInformationTypeService.save(crbSourceOfInformationTypeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-source-of-information-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-source-of-information-types/:id} : Updates an existing crbSourceOfInformationType.
     *
     * @param id the id of the crbSourceOfInformationTypeDTO to save.
     * @param crbSourceOfInformationTypeDTO the crbSourceOfInformationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbSourceOfInformationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbSourceOfInformationTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbSourceOfInformationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-source-of-information-types/{id}")
    public ResponseEntity<CrbSourceOfInformationTypeDTO> updateCrbSourceOfInformationType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbSourceOfInformationType : {}, {}", id, crbSourceOfInformationTypeDTO);
        if (crbSourceOfInformationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbSourceOfInformationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbSourceOfInformationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbSourceOfInformationTypeDTO result = crbSourceOfInformationTypeService.save(crbSourceOfInformationTypeDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbSourceOfInformationTypeDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /crb-source-of-information-types/:id} : Partial updates given fields of an existing crbSourceOfInformationType, field will ignore if it is null
     *
     * @param id the id of the crbSourceOfInformationTypeDTO to save.
     * @param crbSourceOfInformationTypeDTO the crbSourceOfInformationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbSourceOfInformationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbSourceOfInformationTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbSourceOfInformationTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbSourceOfInformationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-source-of-information-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbSourceOfInformationTypeDTO> partialUpdateCrbSourceOfInformationType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbSourceOfInformationTypeDTO crbSourceOfInformationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbSourceOfInformationType partially : {}, {}", id, crbSourceOfInformationTypeDTO);
        if (crbSourceOfInformationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbSourceOfInformationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbSourceOfInformationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbSourceOfInformationTypeDTO> result = crbSourceOfInformationTypeService.partialUpdate(crbSourceOfInformationTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbSourceOfInformationTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-source-of-information-types} : get all the crbSourceOfInformationTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbSourceOfInformationTypes in body.
     */
    @GetMapping("/crb-source-of-information-types")
    public ResponseEntity<List<CrbSourceOfInformationTypeDTO>> getAllCrbSourceOfInformationTypes(
        CrbSourceOfInformationTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbSourceOfInformationTypes by criteria: {}", criteria);
        Page<CrbSourceOfInformationTypeDTO> page = crbSourceOfInformationTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-source-of-information-types/count} : count all the crbSourceOfInformationTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-source-of-information-types/count")
    public ResponseEntity<Long> countCrbSourceOfInformationTypes(CrbSourceOfInformationTypeCriteria criteria) {
        log.debug("REST request to count CrbSourceOfInformationTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbSourceOfInformationTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-source-of-information-types/:id} : get the "id" crbSourceOfInformationType.
     *
     * @param id the id of the crbSourceOfInformationTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbSourceOfInformationTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-source-of-information-types/{id}")
    public ResponseEntity<CrbSourceOfInformationTypeDTO> getCrbSourceOfInformationType(@PathVariable Long id) {
        log.debug("REST request to get CrbSourceOfInformationType : {}", id);
        Optional<CrbSourceOfInformationTypeDTO> crbSourceOfInformationTypeDTO = crbSourceOfInformationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbSourceOfInformationTypeDTO);
    }

    /**
     * {@code DELETE  /crb-source-of-information-types/:id} : delete the "id" crbSourceOfInformationType.
     *
     * @param id the id of the crbSourceOfInformationTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-source-of-information-types/{id}")
    public ResponseEntity<Void> deleteCrbSourceOfInformationType(@PathVariable Long id) {
        log.debug("REST request to delete CrbSourceOfInformationType : {}", id);
        crbSourceOfInformationTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-source-of-information-types?query=:query} : search for the crbSourceOfInformationType corresponding
     * to the query.
     *
     * @param query the query of the crbSourceOfInformationType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-source-of-information-types")
    public ResponseEntity<List<CrbSourceOfInformationTypeDTO>> searchCrbSourceOfInformationTypes(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CrbSourceOfInformationTypes for query {}", query);
        Page<CrbSourceOfInformationTypeDTO> page = crbSourceOfInformationTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
