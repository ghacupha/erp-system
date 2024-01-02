package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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

import io.github.erp.repository.DepreciationJobRepository;
import io.github.erp.service.DepreciationJobQueryService;
import io.github.erp.service.DepreciationJobService;
import io.github.erp.service.criteria.DepreciationJobCriteria;
import io.github.erp.service.dto.DepreciationJobDTO;
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
 * REST controller for managing {@link io.github.erp.domain.DepreciationJob}.
 */
@RestController
@RequestMapping("/api")
public class DepreciationJobResource {

    private final Logger log = LoggerFactory.getLogger(DepreciationJobResource.class);

    private static final String ENTITY_NAME = "depreciationJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DepreciationJobService depreciationJobService;

    private final DepreciationJobRepository depreciationJobRepository;

    private final DepreciationJobQueryService depreciationJobQueryService;

    public DepreciationJobResource(
        DepreciationJobService depreciationJobService,
        DepreciationJobRepository depreciationJobRepository,
        DepreciationJobQueryService depreciationJobQueryService
    ) {
        this.depreciationJobService = depreciationJobService;
        this.depreciationJobRepository = depreciationJobRepository;
        this.depreciationJobQueryService = depreciationJobQueryService;
    }

    /**
     * {@code POST  /depreciation-jobs} : Create a new depreciationJob.
     *
     * @param depreciationJobDTO the depreciationJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new depreciationJobDTO, or with status {@code 400 (Bad Request)} if the depreciationJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/depreciation-jobs")
    public ResponseEntity<DepreciationJobDTO> createDepreciationJob(@Valid @RequestBody DepreciationJobDTO depreciationJobDTO)
        throws URISyntaxException {
        log.debug("REST request to save DepreciationJob : {}", depreciationJobDTO);
        if (depreciationJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new depreciationJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DepreciationJobDTO result = depreciationJobService.save(depreciationJobDTO);
        return ResponseEntity
            .created(new URI("/api/depreciation-jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /depreciation-jobs/:id} : Updates an existing depreciationJob.
     *
     * @param id the id of the depreciationJobDTO to save.
     * @param depreciationJobDTO the depreciationJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationJobDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the depreciationJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/depreciation-jobs/{id}")
    public ResponseEntity<DepreciationJobDTO> updateDepreciationJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DepreciationJobDTO depreciationJobDTO
    ) throws URISyntaxException {
        log.debug("REST request to update DepreciationJob : {}, {}", id, depreciationJobDTO);
        if (depreciationJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        DepreciationJobDTO result = depreciationJobService.save(depreciationJobDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depreciationJobDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /depreciation-jobs/:id} : Partial updates given fields of an existing depreciationJob, field will ignore if it is null
     *
     * @param id the id of the depreciationJobDTO to save.
     * @param depreciationJobDTO the depreciationJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated depreciationJobDTO,
     * or with status {@code 400 (Bad Request)} if the depreciationJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the depreciationJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the depreciationJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/depreciation-jobs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DepreciationJobDTO> partialUpdateDepreciationJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DepreciationJobDTO depreciationJobDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update DepreciationJob partially : {}, {}", id, depreciationJobDTO);
        if (depreciationJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, depreciationJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!depreciationJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DepreciationJobDTO> result = depreciationJobService.partialUpdate(depreciationJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, depreciationJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /depreciation-jobs} : get all the depreciationJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of depreciationJobs in body.
     */
    @GetMapping("/depreciation-jobs")
    public ResponseEntity<List<DepreciationJobDTO>> getAllDepreciationJobs(DepreciationJobCriteria criteria, Pageable pageable) {
        log.debug("REST request to get DepreciationJobs by criteria: {}", criteria);
        Page<DepreciationJobDTO> page = depreciationJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /depreciation-jobs/count} : count all the depreciationJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/depreciation-jobs/count")
    public ResponseEntity<Long> countDepreciationJobs(DepreciationJobCriteria criteria) {
        log.debug("REST request to count DepreciationJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(depreciationJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /depreciation-jobs/:id} : get the "id" depreciationJob.
     *
     * @param id the id of the depreciationJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the depreciationJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/depreciation-jobs/{id}")
    public ResponseEntity<DepreciationJobDTO> getDepreciationJob(@PathVariable Long id) {
        log.debug("REST request to get DepreciationJob : {}", id);
        Optional<DepreciationJobDTO> depreciationJobDTO = depreciationJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(depreciationJobDTO);
    }

    /**
     * {@code DELETE  /depreciation-jobs/:id} : delete the "id" depreciationJob.
     *
     * @param id the id of the depreciationJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/depreciation-jobs/{id}")
    public ResponseEntity<Void> deleteDepreciationJob(@PathVariable Long id) {
        log.debug("REST request to delete DepreciationJob : {}", id);
        depreciationJobService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/depreciation-jobs?query=:query} : search for the depreciationJob corresponding
     * to the query.
     *
     * @param query the query of the depreciationJob search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/depreciation-jobs")
    public ResponseEntity<List<DepreciationJobDTO>> searchDepreciationJobs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of DepreciationJobs for query {}", query);
        Page<DepreciationJobDTO> page = depreciationJobService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
