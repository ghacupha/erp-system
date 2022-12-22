package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark III No 6 (Caleb Series) Server ver 0.2.0
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
import io.github.erp.repository.DepreciationMethodRepository;
import io.github.erp.service.DepreciationMethodQueryService;
import io.github.erp.service.DepreciationMethodService;
import io.github.erp.service.criteria.DepreciationMethodCriteria;
import io.github.erp.service.dto.DepreciationMethodDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DepreciationMethod}.
 */
@RestController
@RequestMapping("/api/dev")
public class DepreciationMethodResourceDev {

    private final Logger log = LoggerFactory.getLogger(DepreciationMethodResourceDev.class);

    private static final String ENTITY_NAME = "depreciationMethod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepreciationMethodService depreciationMethodService;

    private final DepreciationMethodRepository depreciationMethodRepository;

    private final DepreciationMethodQueryService depreciationMethodQueryService;

    public DepreciationMethodResourceDev(
        DepreciationMethodService depreciationMethodService,
        DepreciationMethodRepository depreciationMethodRepository,
        DepreciationMethodQueryService depreciationMethodQueryService
    ) {
        this.depreciationMethodService = depreciationMethodService;
        this.depreciationMethodRepository = depreciationMethodRepository;
        this.depreciationMethodQueryService = depreciationMethodQueryService;
    }

    /**
     * {@code POST  /depreciation-methods} : Create a new depreciationMethod.
     *
     * @param depreciationMethodDTO the depreciationMethodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depreciationMethodDTO, or with status {@code 400 (Bad Request)} if the depreciationMethod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depreciation-methods")
    public ResponseEntity<DepreciationMethodDTO> createDepreciationMethod(@Valid @RequestBody DepreciationMethodDTO depreciationMethodDTO)
        throws URISyntaxException {
        log.debug("REST request to save DepreciationMethod : {}", depreciationMethodDTO);
        if (depreciationMethodDTO.getId() != null) {
            throw new BadRequestAlertException("A new depreciationMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepreciationMethodDTO result = depreciationMethodService.save(depreciationMethodDTO);
        return ResponseEntity
            .created(new URI("/api/depreciation-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depreciation-methods/:id} : Updates an existing depreciationMethod.
     *
     * @param id the id of the depreciationMethodDTO to save.
     * @param depreciationMethodDTO the depreciationMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationMethodDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationMethodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depreciationMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depreciation-methods/{id}")
    public ResponseEntity<DepreciationMethodDTO> updateDepreciationMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepreciationMethodDTO depreciationMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DepreciationMethod : {}, {}", id, depreciationMethodDTO);
        if (depreciationMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepreciationMethodDTO result = depreciationMethodService.save(depreciationMethodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, depreciationMethodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depreciation-methods/:id} : Partial updates given fields of an existing depreciationMethod, field will ignore if it is null
     *
     * @param id the id of the depreciationMethodDTO to save.
     * @param depreciationMethodDTO the depreciationMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationMethodDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationMethodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depreciationMethodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depreciationMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depreciation-methods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepreciationMethodDTO> partialUpdateDepreciationMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepreciationMethodDTO depreciationMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DepreciationMethod partially : {}, {}", id, depreciationMethodDTO);
        if (depreciationMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepreciationMethodDTO> result = depreciationMethodService.partialUpdate(depreciationMethodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, depreciationMethodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depreciation-methods} : get all the depreciationMethods.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depreciationMethods in body.
     */
    @GetMapping("/depreciation-methods")
    public ResponseEntity<List<DepreciationMethodDTO>> getAllDepreciationMethods(DepreciationMethodCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DepreciationMethods by criteria: {}", criteria);
        Page<DepreciationMethodDTO> page = depreciationMethodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depreciation-methods/count} : count all the depreciationMethods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/depreciation-methods/count")
    public ResponseEntity<Long> countDepreciationMethods(DepreciationMethodCriteria criteria) {
        log.debug("REST request to count DepreciationMethods by criteria: {}", criteria);
        return ResponseEntity.ok().body(depreciationMethodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /depreciation-methods/:id} : get the "id" depreciationMethod.
     *
     * @param id the id of the depreciationMethodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depreciationMethodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depreciation-methods/{id}")
    public ResponseEntity<DepreciationMethodDTO> getDepreciationMethod(@PathVariable Long id) {
        log.debug("REST request to get DepreciationMethod : {}", id);
        Optional<DepreciationMethodDTO> depreciationMethodDTO = depreciationMethodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depreciationMethodDTO);
    }

    /**
     * {@code DELETE  /depreciation-methods/:id} : delete the "id" depreciationMethod.
     *
     * @param id the id of the depreciationMethodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depreciation-methods/{id}")
    public ResponseEntity<Void> deleteDepreciationMethod(@PathVariable Long id) {
        log.debug("REST request to delete DepreciationMethod : {}", id);
        depreciationMethodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/depreciation-methods?query=:query} : search for the depreciationMethod corresponding
     * to the query.
     *
     * @param query the query of the depreciationMethod search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/depreciation-methods")
    public ResponseEntity<List<DepreciationMethodDTO>> searchDepreciationMethods(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DepreciationMethods for query {}", query);
        Page<DepreciationMethodDTO> page = depreciationMethodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
