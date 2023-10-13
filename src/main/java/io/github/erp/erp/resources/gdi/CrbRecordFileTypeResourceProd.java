package io.github.erp.erp.resources.gdi;

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
import io.github.erp.repository.CrbRecordFileTypeRepository;
import io.github.erp.service.CrbRecordFileTypeQueryService;
import io.github.erp.service.CrbRecordFileTypeService;
import io.github.erp.service.criteria.CrbRecordFileTypeCriteria;
import io.github.erp.service.dto.CrbRecordFileTypeDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.CrbRecordFileType}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CrbRecordFileTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(CrbRecordFileTypeResourceProd.class);

    private static final String ENTITY_NAME = "crbRecordFileType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbRecordFileTypeService crbRecordFileTypeService;

    private final CrbRecordFileTypeRepository crbRecordFileTypeRepository;

    private final CrbRecordFileTypeQueryService crbRecordFileTypeQueryService;

    public CrbRecordFileTypeResourceProd(
        CrbRecordFileTypeService crbRecordFileTypeService,
        CrbRecordFileTypeRepository crbRecordFileTypeRepository,
        CrbRecordFileTypeQueryService crbRecordFileTypeQueryService
    ) {
        this.crbRecordFileTypeService = crbRecordFileTypeService;
        this.crbRecordFileTypeRepository = crbRecordFileTypeRepository;
        this.crbRecordFileTypeQueryService = crbRecordFileTypeQueryService;
    }

    /**
     * {@code POST  /crb-record-file-types} : Create a new crbRecordFileType.
     *
     * @param crbRecordFileTypeDTO the crbRecordFileTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbRecordFileTypeDTO, or with status {@code 400 (Bad Request)} if the crbRecordFileType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-record-file-types")
    public ResponseEntity<CrbRecordFileTypeDTO> createCrbRecordFileType(@Valid @RequestBody CrbRecordFileTypeDTO crbRecordFileTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CrbRecordFileType : {}", crbRecordFileTypeDTO);
        if (crbRecordFileTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbRecordFileType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbRecordFileTypeDTO result = crbRecordFileTypeService.save(crbRecordFileTypeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-record-file-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-record-file-types/:id} : Updates an existing crbRecordFileType.
     *
     * @param id the id of the crbRecordFileTypeDTO to save.
     * @param crbRecordFileTypeDTO the crbRecordFileTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbRecordFileTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbRecordFileTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbRecordFileTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-record-file-types/{id}")
    public ResponseEntity<CrbRecordFileTypeDTO> updateCrbRecordFileType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbRecordFileTypeDTO crbRecordFileTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbRecordFileType : {}, {}", id, crbRecordFileTypeDTO);
        if (crbRecordFileTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbRecordFileTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbRecordFileTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbRecordFileTypeDTO result = crbRecordFileTypeService.save(crbRecordFileTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbRecordFileTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-record-file-types/:id} : Partial updates given fields of an existing crbRecordFileType, field will ignore if it is null
     *
     * @param id the id of the crbRecordFileTypeDTO to save.
     * @param crbRecordFileTypeDTO the crbRecordFileTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbRecordFileTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbRecordFileTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbRecordFileTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbRecordFileTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-record-file-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbRecordFileTypeDTO> partialUpdateCrbRecordFileType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbRecordFileTypeDTO crbRecordFileTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbRecordFileType partially : {}, {}", id, crbRecordFileTypeDTO);
        if (crbRecordFileTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbRecordFileTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbRecordFileTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbRecordFileTypeDTO> result = crbRecordFileTypeService.partialUpdate(crbRecordFileTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbRecordFileTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-record-file-types} : get all the crbRecordFileTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbRecordFileTypes in body.
     */
    @GetMapping("/crb-record-file-types")
    public ResponseEntity<List<CrbRecordFileTypeDTO>> getAllCrbRecordFileTypes(CrbRecordFileTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CrbRecordFileTypes by criteria: {}", criteria);
        Page<CrbRecordFileTypeDTO> page = crbRecordFileTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-record-file-types/count} : count all the crbRecordFileTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-record-file-types/count")
    public ResponseEntity<Long> countCrbRecordFileTypes(CrbRecordFileTypeCriteria criteria) {
        log.debug("REST request to count CrbRecordFileTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbRecordFileTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-record-file-types/:id} : get the "id" crbRecordFileType.
     *
     * @param id the id of the crbRecordFileTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbRecordFileTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-record-file-types/{id}")
    public ResponseEntity<CrbRecordFileTypeDTO> getCrbRecordFileType(@PathVariable Long id) {
        log.debug("REST request to get CrbRecordFileType : {}", id);
        Optional<CrbRecordFileTypeDTO> crbRecordFileTypeDTO = crbRecordFileTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbRecordFileTypeDTO);
    }

    /**
     * {@code DELETE  /crb-record-file-types/:id} : delete the "id" crbRecordFileType.
     *
     * @param id the id of the crbRecordFileTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-record-file-types/{id}")
    public ResponseEntity<Void> deleteCrbRecordFileType(@PathVariable Long id) {
        log.debug("REST request to delete CrbRecordFileType : {}", id);
        crbRecordFileTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-record-file-types?query=:query} : search for the crbRecordFileType corresponding
     * to the query.
     *
     * @param query the query of the crbRecordFileType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-record-file-types")
    public ResponseEntity<List<CrbRecordFileTypeDTO>> searchCrbRecordFileTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbRecordFileTypes for query {}", query);
        Page<CrbRecordFileTypeDTO> page = crbRecordFileTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
