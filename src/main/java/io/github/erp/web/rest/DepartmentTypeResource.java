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

import io.github.erp.repository.DepartmentTypeRepository;
import io.github.erp.service.DepartmentTypeQueryService;
import io.github.erp.service.DepartmentTypeService;
import io.github.erp.service.criteria.DepartmentTypeCriteria;
import io.github.erp.service.dto.DepartmentTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DepartmentType}.
 */
@RestController
@RequestMapping("/api")
public class DepartmentTypeResource {

    private final Logger log = LoggerFactory.getLogger(DepartmentTypeResource.class);

    private static final String ENTITY_NAME = "departmentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepartmentTypeService departmentTypeService;

    private final DepartmentTypeRepository departmentTypeRepository;

    private final DepartmentTypeQueryService departmentTypeQueryService;

    public DepartmentTypeResource(
        DepartmentTypeService departmentTypeService,
        DepartmentTypeRepository departmentTypeRepository,
        DepartmentTypeQueryService departmentTypeQueryService
    ) {
        this.departmentTypeService = departmentTypeService;
        this.departmentTypeRepository = departmentTypeRepository;
        this.departmentTypeQueryService = departmentTypeQueryService;
    }

    /**
     * {@code POST  /department-types} : Create a new departmentType.
     *
     * @param departmentTypeDTO the departmentTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new departmentTypeDTO, or with status {@code 400 (Bad Request)} if the departmentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/department-types")
    public ResponseEntity<DepartmentTypeDTO> createDepartmentType(@Valid @RequestBody DepartmentTypeDTO departmentTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save DepartmentType : {}", departmentTypeDTO);
        if (departmentTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new departmentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepartmentTypeDTO result = departmentTypeService.save(departmentTypeDTO);
        return ResponseEntity
            .created(new URI("/api/department-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /department-types/:id} : Updates an existing departmentType.
     *
     * @param id the id of the departmentTypeDTO to save.
     * @param departmentTypeDTO the departmentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the departmentTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the departmentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/department-types/{id}")
    public ResponseEntity<DepartmentTypeDTO> updateDepartmentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepartmentTypeDTO departmentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DepartmentType : {}, {}", id, departmentTypeDTO);
        if (departmentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepartmentTypeDTO result = departmentTypeService.save(departmentTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /department-types/:id} : Partial updates given fields of an existing departmentType, field will ignore if it is null
     *
     * @param id the id of the departmentTypeDTO to save.
     * @param departmentTypeDTO the departmentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated departmentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the departmentTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the departmentTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the departmentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/department-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepartmentTypeDTO> partialUpdateDepartmentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepartmentTypeDTO departmentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DepartmentType partially : {}, {}", id, departmentTypeDTO);
        if (departmentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, departmentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!departmentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepartmentTypeDTO> result = departmentTypeService.partialUpdate(departmentTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, departmentTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /department-types} : get all the departmentTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of departmentTypes in body.
     */
    @GetMapping("/department-types")
    public ResponseEntity<List<DepartmentTypeDTO>> getAllDepartmentTypes(DepartmentTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DepartmentTypes by criteria: {}", criteria);
        Page<DepartmentTypeDTO> page = departmentTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /department-types/count} : count all the departmentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/department-types/count")
    public ResponseEntity<Long> countDepartmentTypes(DepartmentTypeCriteria criteria) {
        log.debug("REST request to count DepartmentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(departmentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /department-types/:id} : get the "id" departmentType.
     *
     * @param id the id of the departmentTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the departmentTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/department-types/{id}")
    public ResponseEntity<DepartmentTypeDTO> getDepartmentType(@PathVariable Long id) {
        log.debug("REST request to get DepartmentType : {}", id);
        Optional<DepartmentTypeDTO> departmentTypeDTO = departmentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(departmentTypeDTO);
    }

    /**
     * {@code DELETE  /department-types/:id} : delete the "id" departmentType.
     *
     * @param id the id of the departmentTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/department-types/{id}")
    public ResponseEntity<Void> deleteDepartmentType(@PathVariable Long id) {
        log.debug("REST request to delete DepartmentType : {}", id);
        departmentTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/department-types?query=:query} : search for the departmentType corresponding
     * to the query.
     *
     * @param query the query of the departmentType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/department-types")
    public ResponseEntity<List<DepartmentTypeDTO>> searchDepartmentTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DepartmentTypes for query {}", query);
        Page<DepartmentTypeDTO> page = departmentTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
