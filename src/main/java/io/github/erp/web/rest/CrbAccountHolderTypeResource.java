package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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

import io.github.erp.repository.CrbAccountHolderTypeRepository;
import io.github.erp.service.CrbAccountHolderTypeQueryService;
import io.github.erp.service.CrbAccountHolderTypeService;
import io.github.erp.service.criteria.CrbAccountHolderTypeCriteria;
import io.github.erp.service.dto.CrbAccountHolderTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbAccountHolderType}.
 */
@RestController
@RequestMapping("/api")
public class CrbAccountHolderTypeResource {

    private final Logger log = LoggerFactory.getLogger(CrbAccountHolderTypeResource.class);

    private static final String ENTITY_NAME = "crbAccountHolderType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbAccountHolderTypeService crbAccountHolderTypeService;

    private final CrbAccountHolderTypeRepository crbAccountHolderTypeRepository;

    private final CrbAccountHolderTypeQueryService crbAccountHolderTypeQueryService;

    public CrbAccountHolderTypeResource(
        CrbAccountHolderTypeService crbAccountHolderTypeService,
        CrbAccountHolderTypeRepository crbAccountHolderTypeRepository,
        CrbAccountHolderTypeQueryService crbAccountHolderTypeQueryService
    ) {
        this.crbAccountHolderTypeService = crbAccountHolderTypeService;
        this.crbAccountHolderTypeRepository = crbAccountHolderTypeRepository;
        this.crbAccountHolderTypeQueryService = crbAccountHolderTypeQueryService;
    }

    /**
     * {@code POST  /crb-account-holder-types} : Create a new crbAccountHolderType.
     *
     * @param crbAccountHolderTypeDTO the crbAccountHolderTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbAccountHolderTypeDTO, or with status {@code 400 (Bad Request)} if the crbAccountHolderType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-account-holder-types")
    public ResponseEntity<CrbAccountHolderTypeDTO> createCrbAccountHolderType(
        @Valid @RequestBody CrbAccountHolderTypeDTO crbAccountHolderTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbAccountHolderType : {}", crbAccountHolderTypeDTO);
        if (crbAccountHolderTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbAccountHolderType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbAccountHolderTypeDTO result = crbAccountHolderTypeService.save(crbAccountHolderTypeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-account-holder-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-account-holder-types/:id} : Updates an existing crbAccountHolderType.
     *
     * @param id the id of the crbAccountHolderTypeDTO to save.
     * @param crbAccountHolderTypeDTO the crbAccountHolderTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbAccountHolderTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbAccountHolderTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbAccountHolderTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-account-holder-types/{id}")
    public ResponseEntity<CrbAccountHolderTypeDTO> updateCrbAccountHolderType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbAccountHolderTypeDTO crbAccountHolderTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbAccountHolderType : {}, {}", id, crbAccountHolderTypeDTO);
        if (crbAccountHolderTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbAccountHolderTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbAccountHolderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbAccountHolderTypeDTO result = crbAccountHolderTypeService.save(crbAccountHolderTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbAccountHolderTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-account-holder-types/:id} : Partial updates given fields of an existing crbAccountHolderType, field will ignore if it is null
     *
     * @param id the id of the crbAccountHolderTypeDTO to save.
     * @param crbAccountHolderTypeDTO the crbAccountHolderTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbAccountHolderTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbAccountHolderTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbAccountHolderTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbAccountHolderTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-account-holder-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbAccountHolderTypeDTO> partialUpdateCrbAccountHolderType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbAccountHolderTypeDTO crbAccountHolderTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbAccountHolderType partially : {}, {}", id, crbAccountHolderTypeDTO);
        if (crbAccountHolderTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbAccountHolderTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbAccountHolderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbAccountHolderTypeDTO> result = crbAccountHolderTypeService.partialUpdate(crbAccountHolderTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbAccountHolderTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-account-holder-types} : get all the crbAccountHolderTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbAccountHolderTypes in body.
     */
    @GetMapping("/crb-account-holder-types")
    public ResponseEntity<List<CrbAccountHolderTypeDTO>> getAllCrbAccountHolderTypes(
        CrbAccountHolderTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbAccountHolderTypes by criteria: {}", criteria);
        Page<CrbAccountHolderTypeDTO> page = crbAccountHolderTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-account-holder-types/count} : count all the crbAccountHolderTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-account-holder-types/count")
    public ResponseEntity<Long> countCrbAccountHolderTypes(CrbAccountHolderTypeCriteria criteria) {
        log.debug("REST request to count CrbAccountHolderTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbAccountHolderTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-account-holder-types/:id} : get the "id" crbAccountHolderType.
     *
     * @param id the id of the crbAccountHolderTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbAccountHolderTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-account-holder-types/{id}")
    public ResponseEntity<CrbAccountHolderTypeDTO> getCrbAccountHolderType(@PathVariable Long id) {
        log.debug("REST request to get CrbAccountHolderType : {}", id);
        Optional<CrbAccountHolderTypeDTO> crbAccountHolderTypeDTO = crbAccountHolderTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbAccountHolderTypeDTO);
    }

    /**
     * {@code DELETE  /crb-account-holder-types/:id} : delete the "id" crbAccountHolderType.
     *
     * @param id the id of the crbAccountHolderTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-account-holder-types/{id}")
    public ResponseEntity<Void> deleteCrbAccountHolderType(@PathVariable Long id) {
        log.debug("REST request to delete CrbAccountHolderType : {}", id);
        crbAccountHolderTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-account-holder-types?query=:query} : search for the crbAccountHolderType corresponding
     * to the query.
     *
     * @param query the query of the crbAccountHolderType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-account-holder-types")
    public ResponseEntity<List<CrbAccountHolderTypeDTO>> searchCrbAccountHolderTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbAccountHolderTypes for query {}", query);
        Page<CrbAccountHolderTypeDTO> page = crbAccountHolderTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
