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

import io.github.erp.repository.CrbComplaintStatusTypeRepository;
import io.github.erp.service.CrbComplaintStatusTypeQueryService;
import io.github.erp.service.CrbComplaintStatusTypeService;
import io.github.erp.service.criteria.CrbComplaintStatusTypeCriteria;
import io.github.erp.service.dto.CrbComplaintStatusTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbComplaintStatusType}.
 */
@RestController
@RequestMapping("/api")
public class CrbComplaintStatusTypeResource {

    private final Logger log = LoggerFactory.getLogger(CrbComplaintStatusTypeResource.class);

    private static final String ENTITY_NAME = "crbComplaintStatusType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbComplaintStatusTypeService crbComplaintStatusTypeService;

    private final CrbComplaintStatusTypeRepository crbComplaintStatusTypeRepository;

    private final CrbComplaintStatusTypeQueryService crbComplaintStatusTypeQueryService;

    public CrbComplaintStatusTypeResource(
        CrbComplaintStatusTypeService crbComplaintStatusTypeService,
        CrbComplaintStatusTypeRepository crbComplaintStatusTypeRepository,
        CrbComplaintStatusTypeQueryService crbComplaintStatusTypeQueryService
    ) {
        this.crbComplaintStatusTypeService = crbComplaintStatusTypeService;
        this.crbComplaintStatusTypeRepository = crbComplaintStatusTypeRepository;
        this.crbComplaintStatusTypeQueryService = crbComplaintStatusTypeQueryService;
    }

    /**
     * {@code POST  /crb-complaint-status-types} : Create a new crbComplaintStatusType.
     *
     * @param crbComplaintStatusTypeDTO the crbComplaintStatusTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbComplaintStatusTypeDTO, or with status {@code 400 (Bad Request)} if the crbComplaintStatusType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-complaint-status-types")
    public ResponseEntity<CrbComplaintStatusTypeDTO> createCrbComplaintStatusType(
        @Valid @RequestBody CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbComplaintStatusType : {}", crbComplaintStatusTypeDTO);
        if (crbComplaintStatusTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbComplaintStatusType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbComplaintStatusTypeDTO result = crbComplaintStatusTypeService.save(crbComplaintStatusTypeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-complaint-status-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-complaint-status-types/:id} : Updates an existing crbComplaintStatusType.
     *
     * @param id the id of the crbComplaintStatusTypeDTO to save.
     * @param crbComplaintStatusTypeDTO the crbComplaintStatusTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbComplaintStatusTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbComplaintStatusTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbComplaintStatusTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-complaint-status-types/{id}")
    public ResponseEntity<CrbComplaintStatusTypeDTO> updateCrbComplaintStatusType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbComplaintStatusType : {}, {}", id, crbComplaintStatusTypeDTO);
        if (crbComplaintStatusTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbComplaintStatusTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbComplaintStatusTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbComplaintStatusTypeDTO result = crbComplaintStatusTypeService.save(crbComplaintStatusTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbComplaintStatusTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-complaint-status-types/:id} : Partial updates given fields of an existing crbComplaintStatusType, field will ignore if it is null
     *
     * @param id the id of the crbComplaintStatusTypeDTO to save.
     * @param crbComplaintStatusTypeDTO the crbComplaintStatusTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbComplaintStatusTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbComplaintStatusTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbComplaintStatusTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbComplaintStatusTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-complaint-status-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbComplaintStatusTypeDTO> partialUpdateCrbComplaintStatusType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbComplaintStatusTypeDTO crbComplaintStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbComplaintStatusType partially : {}, {}", id, crbComplaintStatusTypeDTO);
        if (crbComplaintStatusTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbComplaintStatusTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbComplaintStatusTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbComplaintStatusTypeDTO> result = crbComplaintStatusTypeService.partialUpdate(crbComplaintStatusTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbComplaintStatusTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-complaint-status-types} : get all the crbComplaintStatusTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbComplaintStatusTypes in body.
     */
    @GetMapping("/crb-complaint-status-types")
    public ResponseEntity<List<CrbComplaintStatusTypeDTO>> getAllCrbComplaintStatusTypes(
        CrbComplaintStatusTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbComplaintStatusTypes by criteria: {}", criteria);
        Page<CrbComplaintStatusTypeDTO> page = crbComplaintStatusTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-complaint-status-types/count} : count all the crbComplaintStatusTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-complaint-status-types/count")
    public ResponseEntity<Long> countCrbComplaintStatusTypes(CrbComplaintStatusTypeCriteria criteria) {
        log.debug("REST request to count CrbComplaintStatusTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbComplaintStatusTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-complaint-status-types/:id} : get the "id" crbComplaintStatusType.
     *
     * @param id the id of the crbComplaintStatusTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbComplaintStatusTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-complaint-status-types/{id}")
    public ResponseEntity<CrbComplaintStatusTypeDTO> getCrbComplaintStatusType(@PathVariable Long id) {
        log.debug("REST request to get CrbComplaintStatusType : {}", id);
        Optional<CrbComplaintStatusTypeDTO> crbComplaintStatusTypeDTO = crbComplaintStatusTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbComplaintStatusTypeDTO);
    }

    /**
     * {@code DELETE  /crb-complaint-status-types/:id} : delete the "id" crbComplaintStatusType.
     *
     * @param id the id of the crbComplaintStatusTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-complaint-status-types/{id}")
    public ResponseEntity<Void> deleteCrbComplaintStatusType(@PathVariable Long id) {
        log.debug("REST request to delete CrbComplaintStatusType : {}", id);
        crbComplaintStatusTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-complaint-status-types?query=:query} : search for the crbComplaintStatusType corresponding
     * to the query.
     *
     * @param query the query of the crbComplaintStatusType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-complaint-status-types")
    public ResponseEntity<List<CrbComplaintStatusTypeDTO>> searchCrbComplaintStatusTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbComplaintStatusTypes for query {}", query);
        Page<CrbComplaintStatusTypeDTO> page = crbComplaintStatusTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
