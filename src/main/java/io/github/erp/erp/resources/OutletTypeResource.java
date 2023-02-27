package io.github.erp.erp.resources;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.5.0
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
import io.github.erp.repository.OutletTypeRepository;
import io.github.erp.service.OutletTypeQueryService;
import io.github.erp.service.OutletTypeService;
import io.github.erp.service.criteria.OutletTypeCriteria;
import io.github.erp.service.dto.OutletTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.OutletType}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class OutletTypeResource {

    private final Logger log = LoggerFactory.getLogger(OutletTypeResource.class);

    private static final String ENTITY_NAME = "outletType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OutletTypeService outletTypeService;

    private final OutletTypeRepository outletTypeRepository;

    private final OutletTypeQueryService outletTypeQueryService;

    public OutletTypeResource(
        OutletTypeService outletTypeService,
        OutletTypeRepository outletTypeRepository,
        OutletTypeQueryService outletTypeQueryService
    ) {
        this.outletTypeService = outletTypeService;
        this.outletTypeRepository = outletTypeRepository;
        this.outletTypeQueryService = outletTypeQueryService;
    }

    /**
     * {@code POST  /outlet-types} : Create a new outletType.
     *
     * @param outletTypeDTO the outletTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new outletTypeDTO, or with status {@code 400 (Bad Request)} if the outletType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/outlet-types")
    public ResponseEntity<OutletTypeDTO> createOutletType(@Valid @RequestBody OutletTypeDTO outletTypeDTO) throws URISyntaxException {
        log.debug("REST request to save OutletType : {}", outletTypeDTO);
        if (outletTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new outletType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        OutletTypeDTO result = outletTypeService.save(outletTypeDTO);
        return ResponseEntity
            .created(new URI("/api/outlet-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /outlet-types/:id} : Updates an existing outletType.
     *
     * @param id the id of the outletTypeDTO to save.
     * @param outletTypeDTO the outletTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outletTypeDTO,
     * or with status {@code 400 (Bad Request)} if the outletTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the outletTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/outlet-types/{id}")
    public ResponseEntity<OutletTypeDTO> updateOutletType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OutletTypeDTO outletTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update OutletType : {}, {}", id, outletTypeDTO);
        if (outletTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outletTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outletTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        OutletTypeDTO result = outletTypeService.save(outletTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outletTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /outlet-types/:id} : Partial updates given fields of an existing outletType, field will ignore if it is null
     *
     * @param id the id of the outletTypeDTO to save.
     * @param outletTypeDTO the outletTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated outletTypeDTO,
     * or with status {@code 400 (Bad Request)} if the outletTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the outletTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the outletTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/outlet-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OutletTypeDTO> partialUpdateOutletType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OutletTypeDTO outletTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update OutletType partially : {}, {}", id, outletTypeDTO);
        if (outletTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, outletTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!outletTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OutletTypeDTO> result = outletTypeService.partialUpdate(outletTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, outletTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /outlet-types} : get all the outletTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of outletTypes in body.
     */
    @GetMapping("/outlet-types")
    public ResponseEntity<List<OutletTypeDTO>> getAllOutletTypes(OutletTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get OutletTypes by criteria: {}", criteria);
        Page<OutletTypeDTO> page = outletTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /outlet-types/count} : count all the outletTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/outlet-types/count")
    public ResponseEntity<Long> countOutletTypes(OutletTypeCriteria criteria) {
        log.debug("REST request to count OutletTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(outletTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /outlet-types/:id} : get the "id" outletType.
     *
     * @param id the id of the outletTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the outletTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/outlet-types/{id}")
    public ResponseEntity<OutletTypeDTO> getOutletType(@PathVariable Long id) {
        log.debug("REST request to get OutletType : {}", id);
        Optional<OutletTypeDTO> outletTypeDTO = outletTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(outletTypeDTO);
    }

    /**
     * {@code DELETE  /outlet-types/:id} : delete the "id" outletType.
     *
     * @param id the id of the outletTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/outlet-types/{id}")
    public ResponseEntity<Void> deleteOutletType(@PathVariable Long id) {
        log.debug("REST request to delete OutletType : {}", id);
        outletTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/outlet-types?query=:query} : search for the outletType corresponding
     * to the query.
     *
     * @param query the query of the outletType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/outlet-types")
    public ResponseEntity<List<OutletTypeDTO>> searchOutletTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of OutletTypes for query {}", query);
        Page<OutletTypeDTO> page = outletTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
