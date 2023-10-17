package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

import io.github.erp.repository.CrbAgentServiceTypeRepository;
import io.github.erp.service.CrbAgentServiceTypeQueryService;
import io.github.erp.service.CrbAgentServiceTypeService;
import io.github.erp.service.criteria.CrbAgentServiceTypeCriteria;
import io.github.erp.service.dto.CrbAgentServiceTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbAgentServiceType}.
 */
@RestController
@RequestMapping("/api")
public class CrbAgentServiceTypeResource {

    private final Logger log = LoggerFactory.getLogger(CrbAgentServiceTypeResource.class);

    private static final String ENTITY_NAME = "crbAgentServiceType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbAgentServiceTypeService crbAgentServiceTypeService;

    private final CrbAgentServiceTypeRepository crbAgentServiceTypeRepository;

    private final CrbAgentServiceTypeQueryService crbAgentServiceTypeQueryService;

    public CrbAgentServiceTypeResource(
        CrbAgentServiceTypeService crbAgentServiceTypeService,
        CrbAgentServiceTypeRepository crbAgentServiceTypeRepository,
        CrbAgentServiceTypeQueryService crbAgentServiceTypeQueryService
    ) {
        this.crbAgentServiceTypeService = crbAgentServiceTypeService;
        this.crbAgentServiceTypeRepository = crbAgentServiceTypeRepository;
        this.crbAgentServiceTypeQueryService = crbAgentServiceTypeQueryService;
    }

    /**
     * {@code POST  /crb-agent-service-types} : Create a new crbAgentServiceType.
     *
     * @param crbAgentServiceTypeDTO the crbAgentServiceTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbAgentServiceTypeDTO, or with status {@code 400 (Bad Request)} if the crbAgentServiceType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-agent-service-types")
    public ResponseEntity<CrbAgentServiceTypeDTO> createCrbAgentServiceType(
        @Valid @RequestBody CrbAgentServiceTypeDTO crbAgentServiceTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbAgentServiceType : {}", crbAgentServiceTypeDTO);
        if (crbAgentServiceTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbAgentServiceType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbAgentServiceTypeDTO result = crbAgentServiceTypeService.save(crbAgentServiceTypeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-agent-service-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-agent-service-types/:id} : Updates an existing crbAgentServiceType.
     *
     * @param id the id of the crbAgentServiceTypeDTO to save.
     * @param crbAgentServiceTypeDTO the crbAgentServiceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbAgentServiceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbAgentServiceTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbAgentServiceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-agent-service-types/{id}")
    public ResponseEntity<CrbAgentServiceTypeDTO> updateCrbAgentServiceType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbAgentServiceTypeDTO crbAgentServiceTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbAgentServiceType : {}, {}", id, crbAgentServiceTypeDTO);
        if (crbAgentServiceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbAgentServiceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbAgentServiceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbAgentServiceTypeDTO result = crbAgentServiceTypeService.save(crbAgentServiceTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbAgentServiceTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-agent-service-types/:id} : Partial updates given fields of an existing crbAgentServiceType, field will ignore if it is null
     *
     * @param id the id of the crbAgentServiceTypeDTO to save.
     * @param crbAgentServiceTypeDTO the crbAgentServiceTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbAgentServiceTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbAgentServiceTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbAgentServiceTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbAgentServiceTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-agent-service-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbAgentServiceTypeDTO> partialUpdateCrbAgentServiceType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbAgentServiceTypeDTO crbAgentServiceTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbAgentServiceType partially : {}, {}", id, crbAgentServiceTypeDTO);
        if (crbAgentServiceTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbAgentServiceTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbAgentServiceTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbAgentServiceTypeDTO> result = crbAgentServiceTypeService.partialUpdate(crbAgentServiceTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbAgentServiceTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-agent-service-types} : get all the crbAgentServiceTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbAgentServiceTypes in body.
     */
    @GetMapping("/crb-agent-service-types")
    public ResponseEntity<List<CrbAgentServiceTypeDTO>> getAllCrbAgentServiceTypes(
        CrbAgentServiceTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbAgentServiceTypes by criteria: {}", criteria);
        Page<CrbAgentServiceTypeDTO> page = crbAgentServiceTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-agent-service-types/count} : count all the crbAgentServiceTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-agent-service-types/count")
    public ResponseEntity<Long> countCrbAgentServiceTypes(CrbAgentServiceTypeCriteria criteria) {
        log.debug("REST request to count CrbAgentServiceTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbAgentServiceTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-agent-service-types/:id} : get the "id" crbAgentServiceType.
     *
     * @param id the id of the crbAgentServiceTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbAgentServiceTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-agent-service-types/{id}")
    public ResponseEntity<CrbAgentServiceTypeDTO> getCrbAgentServiceType(@PathVariable Long id) {
        log.debug("REST request to get CrbAgentServiceType : {}", id);
        Optional<CrbAgentServiceTypeDTO> crbAgentServiceTypeDTO = crbAgentServiceTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbAgentServiceTypeDTO);
    }

    /**
     * {@code DELETE  /crb-agent-service-types/:id} : delete the "id" crbAgentServiceType.
     *
     * @param id the id of the crbAgentServiceTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-agent-service-types/{id}")
    public ResponseEntity<Void> deleteCrbAgentServiceType(@PathVariable Long id) {
        log.debug("REST request to delete CrbAgentServiceType : {}", id);
        crbAgentServiceTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-agent-service-types?query=:query} : search for the crbAgentServiceType corresponding
     * to the query.
     *
     * @param query the query of the crbAgentServiceType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-agent-service-types")
    public ResponseEntity<List<CrbAgentServiceTypeDTO>> searchCrbAgentServiceTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbAgentServiceTypes for query {}", query);
        Page<CrbAgentServiceTypeDTO> page = crbAgentServiceTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
