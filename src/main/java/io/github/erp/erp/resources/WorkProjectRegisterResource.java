package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 20 (Baruch Series)
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
import io.github.erp.repository.WorkProjectRegisterRepository;
import io.github.erp.service.WorkProjectRegisterQueryService;
import io.github.erp.service.WorkProjectRegisterService;
import io.github.erp.service.criteria.WorkProjectRegisterCriteria;
import io.github.erp.service.dto.WorkProjectRegisterDTO;
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
 * REST controller for managing {@link io.github.erp.domain.WorkProjectRegister}.
 */
@RestController
@RequestMapping("/api/fixed-asset")
public class WorkProjectRegisterResource {

    private final Logger log = LoggerFactory.getLogger(WorkProjectRegisterResource.class);

    private static final String ENTITY_NAME = "workProjectRegister";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WorkProjectRegisterService workProjectRegisterService;

    private final WorkProjectRegisterRepository workProjectRegisterRepository;

    private final WorkProjectRegisterQueryService workProjectRegisterQueryService;

    public WorkProjectRegisterResource(
        WorkProjectRegisterService workProjectRegisterService,
        WorkProjectRegisterRepository workProjectRegisterRepository,
        WorkProjectRegisterQueryService workProjectRegisterQueryService
    ) {
        this.workProjectRegisterService = workProjectRegisterService;
        this.workProjectRegisterRepository = workProjectRegisterRepository;
        this.workProjectRegisterQueryService = workProjectRegisterQueryService;
    }

    /**
     * {@code POST  /work-project-registers} : Create a new workProjectRegister.
     *
     * @param workProjectRegisterDTO the workProjectRegisterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new workProjectRegisterDTO, or with status {@code 400 (Bad Request)} if the workProjectRegister has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/work-project-registers")
    public ResponseEntity<WorkProjectRegisterDTO> createWorkProjectRegister(
        @Valid @RequestBody WorkProjectRegisterDTO workProjectRegisterDTO
    ) throws URISyntaxException {
        log.debug("REST request to save WorkProjectRegister : {}", workProjectRegisterDTO);
        if (workProjectRegisterDTO.getId() != null) {
            throw new BadRequestAlertException("A new workProjectRegister cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WorkProjectRegisterDTO result = workProjectRegisterService.save(workProjectRegisterDTO);
        return ResponseEntity
            .created(new URI("/api/work-project-registers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /work-project-registers/:id} : Updates an existing workProjectRegister.
     *
     * @param id the id of the workProjectRegisterDTO to save.
     * @param workProjectRegisterDTO the workProjectRegisterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workProjectRegisterDTO,
     * or with status {@code 400 (Bad Request)} if the workProjectRegisterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the workProjectRegisterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/work-project-registers/{id}")
    public ResponseEntity<WorkProjectRegisterDTO> updateWorkProjectRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WorkProjectRegisterDTO workProjectRegisterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WorkProjectRegister : {}, {}", id, workProjectRegisterDTO);
        if (workProjectRegisterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workProjectRegisterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workProjectRegisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WorkProjectRegisterDTO result = workProjectRegisterService.save(workProjectRegisterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workProjectRegisterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /work-project-registers/:id} : Partial updates given fields of an existing workProjectRegister, field will ignore if it is null
     *
     * @param id the id of the workProjectRegisterDTO to save.
     * @param workProjectRegisterDTO the workProjectRegisterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated workProjectRegisterDTO,
     * or with status {@code 400 (Bad Request)} if the workProjectRegisterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the workProjectRegisterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the workProjectRegisterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/work-project-registers/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WorkProjectRegisterDTO> partialUpdateWorkProjectRegister(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WorkProjectRegisterDTO workProjectRegisterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WorkProjectRegister partially : {}, {}", id, workProjectRegisterDTO);
        if (workProjectRegisterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, workProjectRegisterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!workProjectRegisterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WorkProjectRegisterDTO> result = workProjectRegisterService.partialUpdate(workProjectRegisterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, workProjectRegisterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /work-project-registers} : get all the workProjectRegisters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of workProjectRegisters in body.
     */
    @GetMapping("/work-project-registers")
    public ResponseEntity<List<WorkProjectRegisterDTO>> getAllWorkProjectRegisters(
        WorkProjectRegisterCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get WorkProjectRegisters by criteria: {}", criteria);
        Page<WorkProjectRegisterDTO> page = workProjectRegisterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /work-project-registers/count} : count all the workProjectRegisters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/work-project-registers/count")
    public ResponseEntity<Long> countWorkProjectRegisters(WorkProjectRegisterCriteria criteria) {
        log.debug("REST request to count WorkProjectRegisters by criteria: {}", criteria);
        return ResponseEntity.ok().body(workProjectRegisterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /work-project-registers/:id} : get the "id" workProjectRegister.
     *
     * @param id the id of the workProjectRegisterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the workProjectRegisterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/work-project-registers/{id}")
    public ResponseEntity<WorkProjectRegisterDTO> getWorkProjectRegister(@PathVariable Long id) {
        log.debug("REST request to get WorkProjectRegister : {}", id);
        Optional<WorkProjectRegisterDTO> workProjectRegisterDTO = workProjectRegisterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(workProjectRegisterDTO);
    }

    /**
     * {@code DELETE  /work-project-registers/:id} : delete the "id" workProjectRegister.
     *
     * @param id the id of the workProjectRegisterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/work-project-registers/{id}")
    public ResponseEntity<Void> deleteWorkProjectRegister(@PathVariable Long id) {
        log.debug("REST request to delete WorkProjectRegister : {}", id);
        workProjectRegisterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/work-project-registers?query=:query} : search for the workProjectRegister corresponding
     * to the query.
     *
     * @param query the query of the workProjectRegister search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/work-project-registers")
    public ResponseEntity<List<WorkProjectRegisterDTO>> searchWorkProjectRegisters(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WorkProjectRegisters for query {}", query);
        Page<WorkProjectRegisterDTO> page = workProjectRegisterService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
