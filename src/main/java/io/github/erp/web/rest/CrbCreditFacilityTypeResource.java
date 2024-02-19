package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.repository.CrbCreditFacilityTypeRepository;
import io.github.erp.service.CrbCreditFacilityTypeQueryService;
import io.github.erp.service.CrbCreditFacilityTypeService;
import io.github.erp.service.criteria.CrbCreditFacilityTypeCriteria;
import io.github.erp.service.dto.CrbCreditFacilityTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbCreditFacilityType}.
 */
@RestController
@RequestMapping("/api")
public class CrbCreditFacilityTypeResource {

    private final Logger log = LoggerFactory.getLogger(CrbCreditFacilityTypeResource.class);

    private static final String ENTITY_NAME = "crbCreditFacilityType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbCreditFacilityTypeService crbCreditFacilityTypeService;

    private final CrbCreditFacilityTypeRepository crbCreditFacilityTypeRepository;

    private final CrbCreditFacilityTypeQueryService crbCreditFacilityTypeQueryService;

    public CrbCreditFacilityTypeResource(
        CrbCreditFacilityTypeService crbCreditFacilityTypeService,
        CrbCreditFacilityTypeRepository crbCreditFacilityTypeRepository,
        CrbCreditFacilityTypeQueryService crbCreditFacilityTypeQueryService
    ) {
        this.crbCreditFacilityTypeService = crbCreditFacilityTypeService;
        this.crbCreditFacilityTypeRepository = crbCreditFacilityTypeRepository;
        this.crbCreditFacilityTypeQueryService = crbCreditFacilityTypeQueryService;
    }

    /**
     * {@code POST  /crb-credit-facility-types} : Create a new crbCreditFacilityType.
     *
     * @param crbCreditFacilityTypeDTO the crbCreditFacilityTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbCreditFacilityTypeDTO, or with status {@code 400 (Bad Request)} if the crbCreditFacilityType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-credit-facility-types")
    public ResponseEntity<CrbCreditFacilityTypeDTO> createCrbCreditFacilityType(
        @Valid @RequestBody CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbCreditFacilityType : {}", crbCreditFacilityTypeDTO);
        if (crbCreditFacilityTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbCreditFacilityType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbCreditFacilityTypeDTO result = crbCreditFacilityTypeService.save(crbCreditFacilityTypeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-credit-facility-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-credit-facility-types/:id} : Updates an existing crbCreditFacilityType.
     *
     * @param id the id of the crbCreditFacilityTypeDTO to save.
     * @param crbCreditFacilityTypeDTO the crbCreditFacilityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbCreditFacilityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbCreditFacilityTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbCreditFacilityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-credit-facility-types/{id}")
    public ResponseEntity<CrbCreditFacilityTypeDTO> updateCrbCreditFacilityType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbCreditFacilityType : {}, {}", id, crbCreditFacilityTypeDTO);
        if (crbCreditFacilityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbCreditFacilityTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbCreditFacilityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbCreditFacilityTypeDTO result = crbCreditFacilityTypeService.save(crbCreditFacilityTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbCreditFacilityTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-credit-facility-types/:id} : Partial updates given fields of an existing crbCreditFacilityType, field will ignore if it is null
     *
     * @param id the id of the crbCreditFacilityTypeDTO to save.
     * @param crbCreditFacilityTypeDTO the crbCreditFacilityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbCreditFacilityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbCreditFacilityTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbCreditFacilityTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbCreditFacilityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-credit-facility-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbCreditFacilityTypeDTO> partialUpdateCrbCreditFacilityType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbCreditFacilityTypeDTO crbCreditFacilityTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbCreditFacilityType partially : {}, {}", id, crbCreditFacilityTypeDTO);
        if (crbCreditFacilityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbCreditFacilityTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbCreditFacilityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbCreditFacilityTypeDTO> result = crbCreditFacilityTypeService.partialUpdate(crbCreditFacilityTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbCreditFacilityTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-credit-facility-types} : get all the crbCreditFacilityTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbCreditFacilityTypes in body.
     */
    @GetMapping("/crb-credit-facility-types")
    public ResponseEntity<List<CrbCreditFacilityTypeDTO>> getAllCrbCreditFacilityTypes(
        CrbCreditFacilityTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbCreditFacilityTypes by criteria: {}", criteria);
        Page<CrbCreditFacilityTypeDTO> page = crbCreditFacilityTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-credit-facility-types/count} : count all the crbCreditFacilityTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-credit-facility-types/count")
    public ResponseEntity<Long> countCrbCreditFacilityTypes(CrbCreditFacilityTypeCriteria criteria) {
        log.debug("REST request to count CrbCreditFacilityTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbCreditFacilityTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-credit-facility-types/:id} : get the "id" crbCreditFacilityType.
     *
     * @param id the id of the crbCreditFacilityTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbCreditFacilityTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-credit-facility-types/{id}")
    public ResponseEntity<CrbCreditFacilityTypeDTO> getCrbCreditFacilityType(@PathVariable Long id) {
        log.debug("REST request to get CrbCreditFacilityType : {}", id);
        Optional<CrbCreditFacilityTypeDTO> crbCreditFacilityTypeDTO = crbCreditFacilityTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbCreditFacilityTypeDTO);
    }

    /**
     * {@code DELETE  /crb-credit-facility-types/:id} : delete the "id" crbCreditFacilityType.
     *
     * @param id the id of the crbCreditFacilityTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-credit-facility-types/{id}")
    public ResponseEntity<Void> deleteCrbCreditFacilityType(@PathVariable Long id) {
        log.debug("REST request to delete CrbCreditFacilityType : {}", id);
        crbCreditFacilityTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-credit-facility-types?query=:query} : search for the crbCreditFacilityType corresponding
     * to the query.
     *
     * @param query the query of the crbCreditFacilityType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-credit-facility-types")
    public ResponseEntity<List<CrbCreditFacilityTypeDTO>> searchCrbCreditFacilityTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbCreditFacilityTypes for query {}", query);
        Page<CrbCreditFacilityTypeDTO> page = crbCreditFacilityTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
