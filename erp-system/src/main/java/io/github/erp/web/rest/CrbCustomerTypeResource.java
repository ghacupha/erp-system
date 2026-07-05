package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.repository.CrbCustomerTypeRepository;
import io.github.erp.service.CrbCustomerTypeQueryService;
import io.github.erp.service.CrbCustomerTypeService;
import io.github.erp.service.criteria.CrbCustomerTypeCriteria;
import io.github.erp.service.dto.CrbCustomerTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbCustomerType}.
 */
@RestController
@RequestMapping("/api")
public class CrbCustomerTypeResource {

    private final Logger log = LoggerFactory.getLogger(CrbCustomerTypeResource.class);

    private static final String ENTITY_NAME = "crbCustomerType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbCustomerTypeService crbCustomerTypeService;

    private final CrbCustomerTypeRepository crbCustomerTypeRepository;

    private final CrbCustomerTypeQueryService crbCustomerTypeQueryService;

    public CrbCustomerTypeResource(
        CrbCustomerTypeService crbCustomerTypeService,
        CrbCustomerTypeRepository crbCustomerTypeRepository,
        CrbCustomerTypeQueryService crbCustomerTypeQueryService
    ) {
        this.crbCustomerTypeService = crbCustomerTypeService;
        this.crbCustomerTypeRepository = crbCustomerTypeRepository;
        this.crbCustomerTypeQueryService = crbCustomerTypeQueryService;
    }

    /**
     * {@code POST  /crb-customer-types} : Create a new crbCustomerType.
     *
     * @param crbCustomerTypeDTO the crbCustomerTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbCustomerTypeDTO, or with status {@code 400 (Bad Request)} if the crbCustomerType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-customer-types")
    public ResponseEntity<CrbCustomerTypeDTO> createCrbCustomerType(@Valid @RequestBody CrbCustomerTypeDTO crbCustomerTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CrbCustomerType : {}", crbCustomerTypeDTO);
        if (crbCustomerTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbCustomerType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbCustomerTypeDTO result = crbCustomerTypeService.save(crbCustomerTypeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-customer-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-customer-types/:id} : Updates an existing crbCustomerType.
     *
     * @param id the id of the crbCustomerTypeDTO to save.
     * @param crbCustomerTypeDTO the crbCustomerTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbCustomerTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbCustomerTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbCustomerTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-customer-types/{id}")
    public ResponseEntity<CrbCustomerTypeDTO> updateCrbCustomerType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbCustomerTypeDTO crbCustomerTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbCustomerType : {}, {}", id, crbCustomerTypeDTO);
        if (crbCustomerTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbCustomerTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbCustomerTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbCustomerTypeDTO result = crbCustomerTypeService.save(crbCustomerTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbCustomerTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-customer-types/:id} : Partial updates given fields of an existing crbCustomerType, field will ignore if it is null
     *
     * @param id the id of the crbCustomerTypeDTO to save.
     * @param crbCustomerTypeDTO the crbCustomerTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbCustomerTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbCustomerTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbCustomerTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbCustomerTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-customer-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbCustomerTypeDTO> partialUpdateCrbCustomerType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbCustomerTypeDTO crbCustomerTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbCustomerType partially : {}, {}", id, crbCustomerTypeDTO);
        if (crbCustomerTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbCustomerTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbCustomerTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbCustomerTypeDTO> result = crbCustomerTypeService.partialUpdate(crbCustomerTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbCustomerTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-customer-types} : get all the crbCustomerTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbCustomerTypes in body.
     */
    @GetMapping("/crb-customer-types")
    public ResponseEntity<List<CrbCustomerTypeDTO>> getAllCrbCustomerTypes(CrbCustomerTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CrbCustomerTypes by criteria: {}", criteria);
        Page<CrbCustomerTypeDTO> page = crbCustomerTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-customer-types/count} : count all the crbCustomerTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-customer-types/count")
    public ResponseEntity<Long> countCrbCustomerTypes(CrbCustomerTypeCriteria criteria) {
        log.debug("REST request to count CrbCustomerTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbCustomerTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-customer-types/:id} : get the "id" crbCustomerType.
     *
     * @param id the id of the crbCustomerTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbCustomerTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-customer-types/{id}")
    public ResponseEntity<CrbCustomerTypeDTO> getCrbCustomerType(@PathVariable Long id) {
        log.debug("REST request to get CrbCustomerType : {}", id);
        Optional<CrbCustomerTypeDTO> crbCustomerTypeDTO = crbCustomerTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbCustomerTypeDTO);
    }

    /**
     * {@code DELETE  /crb-customer-types/:id} : delete the "id" crbCustomerType.
     *
     * @param id the id of the crbCustomerTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-customer-types/{id}")
    public ResponseEntity<Void> deleteCrbCustomerType(@PathVariable Long id) {
        log.debug("REST request to delete CrbCustomerType : {}", id);
        crbCustomerTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-customer-types?query=:query} : search for the crbCustomerType corresponding
     * to the query.
     *
     * @param query the query of the crbCustomerType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-customer-types")
    public ResponseEntity<List<CrbCustomerTypeDTO>> searchCrbCustomerTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbCustomerTypes for query {}", query);
        Page<CrbCustomerTypeDTO> page = crbCustomerTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
