package io.github.erp.erp.resources;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.repository.SystemModuleRepository;
import io.github.erp.service.SystemModuleQueryService;
import io.github.erp.service.SystemModuleService;
import io.github.erp.service.criteria.SystemModuleCriteria;
import io.github.erp.service.dto.SystemModuleDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SystemModule}.
 */
@RestController("systemModuleResourceProd")
@RequestMapping("/api/app")
public class SystemModuleResourceProd {

    private final Logger log = LoggerFactory.getLogger(SystemModuleResourceProd.class);

    private static final String ENTITY_NAME = "systemModule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SystemModuleService systemModuleService;

    private final SystemModuleRepository systemModuleRepository;

    private final SystemModuleQueryService systemModuleQueryService;

    public SystemModuleResourceProd(
        SystemModuleService systemModuleService,
        SystemModuleRepository systemModuleRepository,
        SystemModuleQueryService systemModuleQueryService
    ) {
        this.systemModuleService = systemModuleService;
        this.systemModuleRepository = systemModuleRepository;
        this.systemModuleQueryService = systemModuleQueryService;
    }

    /**
     * {@code POST  /system-modules} : Create a new systemModule.
     *
     * @param systemModuleDTO the systemModuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new systemModuleDTO, or with status {@code 400 (Bad Request)} if the systemModule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/system-modules")
    public ResponseEntity<SystemModuleDTO> createSystemModule(@Valid @RequestBody SystemModuleDTO systemModuleDTO)
        throws URISyntaxException {
        log.debug("REST request to save SystemModule : {}", systemModuleDTO);
        if (systemModuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new systemModule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SystemModuleDTO result = systemModuleService.save(systemModuleDTO);
        return ResponseEntity
            .created(new URI("/api/system-modules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /system-modules/:id} : Updates an existing systemModule.
     *
     * @param id the id of the systemModuleDTO to save.
     * @param systemModuleDTO the systemModuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemModuleDTO,
     * or with status {@code 400 (Bad Request)} if the systemModuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the systemModuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/system-modules/{id}")
    public ResponseEntity<SystemModuleDTO> updateSystemModule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SystemModuleDTO systemModuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SystemModule : {}, {}", id, systemModuleDTO);
        if (systemModuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemModuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemModuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SystemModuleDTO result = systemModuleService.save(systemModuleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, systemModuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /system-modules/:id} : Partial updates given fields of an existing systemModule, field will ignore if it is null
     *
     * @param id the id of the systemModuleDTO to save.
     * @param systemModuleDTO the systemModuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated systemModuleDTO,
     * or with status {@code 400 (Bad Request)} if the systemModuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the systemModuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the systemModuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/system-modules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SystemModuleDTO> partialUpdateSystemModule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SystemModuleDTO systemModuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SystemModule partially : {}, {}", id, systemModuleDTO);
        if (systemModuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, systemModuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!systemModuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SystemModuleDTO> result = systemModuleService.partialUpdate(systemModuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, systemModuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /system-modules} : get all the systemModules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of systemModules in body.
     */
    @GetMapping("/system-modules")
    public ResponseEntity<List<SystemModuleDTO>> getAllSystemModules(SystemModuleCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SystemModules by criteria: {}", criteria);
        Page<SystemModuleDTO> page = systemModuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /system-modules/count} : count all the systemModules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/system-modules/count")
    public ResponseEntity<Long> countSystemModules(SystemModuleCriteria criteria) {
        log.debug("REST request to count SystemModules by criteria: {}", criteria);
        return ResponseEntity.ok().body(systemModuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /system-modules/:id} : get the "id" systemModule.
     *
     * @param id the id of the systemModuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the systemModuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/system-modules/{id}")
    public ResponseEntity<SystemModuleDTO> getSystemModule(@PathVariable Long id) {
        log.debug("REST request to get SystemModule : {}", id);
        Optional<SystemModuleDTO> systemModuleDTO = systemModuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(systemModuleDTO);
    }

    /**
     * {@code DELETE  /system-modules/:id} : delete the "id" systemModule.
     *
     * @param id the id of the systemModuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/system-modules/{id}")
    public ResponseEntity<Void> deleteSystemModule(@PathVariable Long id) {
        log.debug("REST request to delete SystemModule : {}", id);
        systemModuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/system-modules?query=:query} : search for the systemModule corresponding
     * to the query.
     *
     * @param query the query of the systemModule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/system-modules")
    public ResponseEntity<List<SystemModuleDTO>> searchSystemModules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SystemModules for query {}", query);
        Page<SystemModuleDTO> page = systemModuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
