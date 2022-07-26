package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 21 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.repository.SystemContentTypeRepository;
import io.github.erp.service.SystemContentTypeQueryService;
import io.github.erp.service.SystemContentTypeService;
import io.github.erp.service.criteria.SystemContentTypeCriteria;
import io.github.erp.service.dto.SystemContentTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SystemContentType}.
 */
@RestController
@RequestMapping("/api/dev-test")
public class SystemContentTypeResourceDev {

    private final Logger log = LoggerFactory.getLogger(SystemContentTypeResourceDev.class);

    private static final String ENTITY_NAME = "systemContentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemContentTypeService systemContentTypeService;

    private final SystemContentTypeRepository systemContentTypeRepository;

    private final SystemContentTypeQueryService systemContentTypeQueryService;

    public SystemContentTypeResourceDev(
        SystemContentTypeService systemContentTypeService,
        SystemContentTypeRepository systemContentTypeRepository,
        SystemContentTypeQueryService systemContentTypeQueryService
    ) {
        this.systemContentTypeService = systemContentTypeService;
        this.systemContentTypeRepository = systemContentTypeRepository;
        this.systemContentTypeQueryService = systemContentTypeQueryService;
    }

    /**
     * {@code POST  /system-content-types} : Create a new systemContentType.
     *
     * @param systemContentTypeDTO the systemContentTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemContentTypeDTO, or with status {@code 400 (Bad Request)} if the systemContentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/system-content-types")
    public ResponseEntity<SystemContentTypeDTO> createSystemContentType(@Valid @RequestBody SystemContentTypeDTO systemContentTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save SystemContentType : {}", systemContentTypeDTO);
        if (systemContentTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemContentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemContentTypeDTO result = systemContentTypeService.save(systemContentTypeDTO);
        return ResponseEntity
            .created(new URI("/api/system-content-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /system-content-types/:id} : Updates an existing systemContentType.
     *
     * @param id the id of the systemContentTypeDTO to save.
     * @param systemContentTypeDTO the systemContentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemContentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the systemContentTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemContentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/system-content-types/{id}")
    public ResponseEntity<SystemContentTypeDTO> updateSystemContentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemContentTypeDTO systemContentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SystemContentType : {}, {}", id, systemContentTypeDTO);
        if (systemContentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemContentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemContentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemContentTypeDTO result = systemContentTypeService.save(systemContentTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, systemContentTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /system-content-types/:id} : Partial updates given fields of an existing systemContentType, field will ignore if it is null
     *
     * @param id the id of the systemContentTypeDTO to save.
     * @param systemContentTypeDTO the systemContentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemContentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the systemContentTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemContentTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemContentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/system-content-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemContentTypeDTO> partialUpdateSystemContentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemContentTypeDTO systemContentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemContentType partially : {}, {}", id, systemContentTypeDTO);
        if (systemContentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemContentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemContentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemContentTypeDTO> result = systemContentTypeService.partialUpdate(systemContentTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, systemContentTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-content-types} : get all the systemContentTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemContentTypes in body.
     */
    @GetMapping("/system-content-types")
    public ResponseEntity<List<SystemContentTypeDTO>> getAllSystemContentTypes(SystemContentTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SystemContentTypes by criteria: {}", criteria);
        Page<SystemContentTypeDTO> page = systemContentTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-content-types/count} : count all the systemContentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/system-content-types/count")
    public ResponseEntity<Long> countSystemContentTypes(SystemContentTypeCriteria criteria) {
        log.debug("REST request to count SystemContentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemContentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /system-content-types/:id} : get the "id" systemContentType.
     *
     * @param id the id of the systemContentTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemContentTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/system-content-types/{id}")
    public ResponseEntity<SystemContentTypeDTO> getSystemContentType(@PathVariable Long id) {
        log.debug("REST request to get SystemContentType : {}", id);
        Optional<SystemContentTypeDTO> systemContentTypeDTO = systemContentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemContentTypeDTO);
    }

    /**
     * {@code DELETE  /system-content-types/:id} : delete the "id" systemContentType.
     *
     * @param id the id of the systemContentTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/system-content-types/{id}")
    public ResponseEntity<Void> deleteSystemContentType(@PathVariable Long id) {
        log.debug("REST request to delete SystemContentType : {}", id);
        systemContentTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/system-content-types?query=:query} : search for the systemContentType corresponding
     * to the query.
     *
     * @param query the query of the systemContentType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/system-content-types")
    public ResponseEntity<List<SystemContentTypeDTO>> searchSystemContentTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SystemContentTypes for query {}", query);
        Page<SystemContentTypeDTO> page = systemContentTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
