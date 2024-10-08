package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.NbvCompilationJobRepository;
import io.github.erp.service.NbvCompilationJobQueryService;
import io.github.erp.service.NbvCompilationJobService;
import io.github.erp.service.criteria.NbvCompilationJobCriteria;
import io.github.erp.service.dto.NbvCompilationJobDTO;
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
 * REST controller for managing {@link io.github.erp.domain.NbvCompilationJob}.
 */
@RestController
@RequestMapping("/api")
public class NbvCompilationJobResource {

    private final Logger log = LoggerFactory.getLogger(NbvCompilationJobResource.class);

    private static final String ENTITY_NAME = "nbvCompilationJob";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NbvCompilationJobService nbvCompilationJobService;

    private final NbvCompilationJobRepository nbvCompilationJobRepository;

    private final NbvCompilationJobQueryService nbvCompilationJobQueryService;

    public NbvCompilationJobResource(
        NbvCompilationJobService nbvCompilationJobService,
        NbvCompilationJobRepository nbvCompilationJobRepository,
        NbvCompilationJobQueryService nbvCompilationJobQueryService
    ) {
        this.nbvCompilationJobService = nbvCompilationJobService;
        this.nbvCompilationJobRepository = nbvCompilationJobRepository;
        this.nbvCompilationJobQueryService = nbvCompilationJobQueryService;
    }

    /**
     * {@code POST  /nbv-compilation-jobs} : Create a new nbvCompilationJob.
     *
     * @param nbvCompilationJobDTO the nbvCompilationJobDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nbvCompilationJobDTO, or with status {@code 400 (Bad Request)} if the nbvCompilationJob has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nbv-compilation-jobs")
    public ResponseEntity<NbvCompilationJobDTO> createNbvCompilationJob(@Valid @RequestBody NbvCompilationJobDTO nbvCompilationJobDTO)
        throws URISyntaxException {
        log.debug("REST request to save NbvCompilationJob : {}", nbvCompilationJobDTO);
        if (nbvCompilationJobDTO.getId() != null) {
            throw new BadRequestAlertException("A new nbvCompilationJob cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NbvCompilationJobDTO result = nbvCompilationJobService.save(nbvCompilationJobDTO);
        return ResponseEntity
            .created(new URI("/api/nbv-compilation-jobs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nbv-compilation-jobs/:id} : Updates an existing nbvCompilationJob.
     *
     * @param id the id of the nbvCompilationJobDTO to save.
     * @param nbvCompilationJobDTO the nbvCompilationJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nbvCompilationJobDTO,
     * or with status {@code 400 (Bad Request)} if the nbvCompilationJobDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nbvCompilationJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nbv-compilation-jobs/{id}")
    public ResponseEntity<NbvCompilationJobDTO> updateNbvCompilationJob(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NbvCompilationJobDTO nbvCompilationJobDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NbvCompilationJob : {}, {}", id, nbvCompilationJobDTO);
        if (nbvCompilationJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nbvCompilationJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nbvCompilationJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NbvCompilationJobDTO result = nbvCompilationJobService.save(nbvCompilationJobDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nbvCompilationJobDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nbv-compilation-jobs/:id} : Partial updates given fields of an existing nbvCompilationJob, field will ignore if it is null
     *
     * @param id the id of the nbvCompilationJobDTO to save.
     * @param nbvCompilationJobDTO the nbvCompilationJobDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nbvCompilationJobDTO,
     * or with status {@code 400 (Bad Request)} if the nbvCompilationJobDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nbvCompilationJobDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nbvCompilationJobDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nbv-compilation-jobs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NbvCompilationJobDTO> partialUpdateNbvCompilationJob(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NbvCompilationJobDTO nbvCompilationJobDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NbvCompilationJob partially : {}, {}", id, nbvCompilationJobDTO);
        if (nbvCompilationJobDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nbvCompilationJobDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nbvCompilationJobRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NbvCompilationJobDTO> result = nbvCompilationJobService.partialUpdate(nbvCompilationJobDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nbvCompilationJobDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nbv-compilation-jobs} : get all the nbvCompilationJobs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nbvCompilationJobs in body.
     */
    @GetMapping("/nbv-compilation-jobs")
    public ResponseEntity<List<NbvCompilationJobDTO>> getAllNbvCompilationJobs(NbvCompilationJobCriteria criteria, Pageable pageable) {
        log.debug("REST request to get NbvCompilationJobs by criteria: {}", criteria);
        Page<NbvCompilationJobDTO> page = nbvCompilationJobQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nbv-compilation-jobs/count} : count all the nbvCompilationJobs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/nbv-compilation-jobs/count")
    public ResponseEntity<Long> countNbvCompilationJobs(NbvCompilationJobCriteria criteria) {
        log.debug("REST request to count NbvCompilationJobs by criteria: {}", criteria);
        return ResponseEntity.ok().body(nbvCompilationJobQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /nbv-compilation-jobs/:id} : get the "id" nbvCompilationJob.
     *
     * @param id the id of the nbvCompilationJobDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nbvCompilationJobDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nbv-compilation-jobs/{id}")
    public ResponseEntity<NbvCompilationJobDTO> getNbvCompilationJob(@PathVariable Long id) {
        log.debug("REST request to get NbvCompilationJob : {}", id);
        Optional<NbvCompilationJobDTO> nbvCompilationJobDTO = nbvCompilationJobService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nbvCompilationJobDTO);
    }

    /**
     * {@code DELETE  /nbv-compilation-jobs/:id} : delete the "id" nbvCompilationJob.
     *
     * @param id the id of the nbvCompilationJobDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nbv-compilation-jobs/{id}")
    public ResponseEntity<Void> deleteNbvCompilationJob(@PathVariable Long id) {
        log.debug("REST request to delete NbvCompilationJob : {}", id);
        nbvCompilationJobService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/nbv-compilation-jobs?query=:query} : search for the nbvCompilationJob corresponding
     * to the query.
     *
     * @param query the query of the nbvCompilationJob search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/nbv-compilation-jobs")
    public ResponseEntity<List<NbvCompilationJobDTO>> searchNbvCompilationJobs(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of NbvCompilationJobs for query {}", query);
        Page<NbvCompilationJobDTO> page = nbvCompilationJobService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
