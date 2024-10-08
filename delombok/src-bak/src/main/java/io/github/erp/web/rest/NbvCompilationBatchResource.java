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

import io.github.erp.repository.NbvCompilationBatchRepository;
import io.github.erp.service.NbvCompilationBatchQueryService;
import io.github.erp.service.NbvCompilationBatchService;
import io.github.erp.service.criteria.NbvCompilationBatchCriteria;
import io.github.erp.service.dto.NbvCompilationBatchDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link io.github.erp.domain.NbvCompilationBatch}.
 */
@RestController
@RequestMapping("/api")
public class NbvCompilationBatchResource {

    private final Logger log = LoggerFactory.getLogger(NbvCompilationBatchResource.class);

    private static final String ENTITY_NAME = "nbvCompilationBatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NbvCompilationBatchService nbvCompilationBatchService;

    private final NbvCompilationBatchRepository nbvCompilationBatchRepository;

    private final NbvCompilationBatchQueryService nbvCompilationBatchQueryService;

    public NbvCompilationBatchResource(
        NbvCompilationBatchService nbvCompilationBatchService,
        NbvCompilationBatchRepository nbvCompilationBatchRepository,
        NbvCompilationBatchQueryService nbvCompilationBatchQueryService
    ) {
        this.nbvCompilationBatchService = nbvCompilationBatchService;
        this.nbvCompilationBatchRepository = nbvCompilationBatchRepository;
        this.nbvCompilationBatchQueryService = nbvCompilationBatchQueryService;
    }

    /**
     * {@code POST  /nbv-compilation-batches} : Create a new nbvCompilationBatch.
     *
     * @param nbvCompilationBatchDTO the nbvCompilationBatchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nbvCompilationBatchDTO, or with status {@code 400 (Bad Request)} if the nbvCompilationBatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nbv-compilation-batches")
    public ResponseEntity<NbvCompilationBatchDTO> createNbvCompilationBatch(@RequestBody NbvCompilationBatchDTO nbvCompilationBatchDTO)
        throws URISyntaxException {
        log.debug("REST request to save NbvCompilationBatch : {}", nbvCompilationBatchDTO);
        if (nbvCompilationBatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new nbvCompilationBatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NbvCompilationBatchDTO result = nbvCompilationBatchService.save(nbvCompilationBatchDTO);
        return ResponseEntity
            .created(new URI("/api/nbv-compilation-batches/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nbv-compilation-batches/:id} : Updates an existing nbvCompilationBatch.
     *
     * @param id the id of the nbvCompilationBatchDTO to save.
     * @param nbvCompilationBatchDTO the nbvCompilationBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nbvCompilationBatchDTO,
     * or with status {@code 400 (Bad Request)} if the nbvCompilationBatchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nbvCompilationBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nbv-compilation-batches/{id}")
    public ResponseEntity<NbvCompilationBatchDTO> updateNbvCompilationBatch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NbvCompilationBatchDTO nbvCompilationBatchDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NbvCompilationBatch : {}, {}", id, nbvCompilationBatchDTO);
        if (nbvCompilationBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nbvCompilationBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nbvCompilationBatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NbvCompilationBatchDTO result = nbvCompilationBatchService.save(nbvCompilationBatchDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nbvCompilationBatchDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nbv-compilation-batches/:id} : Partial updates given fields of an existing nbvCompilationBatch, field will ignore if it is null
     *
     * @param id the id of the nbvCompilationBatchDTO to save.
     * @param nbvCompilationBatchDTO the nbvCompilationBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nbvCompilationBatchDTO,
     * or with status {@code 400 (Bad Request)} if the nbvCompilationBatchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the nbvCompilationBatchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the nbvCompilationBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nbv-compilation-batches/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NbvCompilationBatchDTO> partialUpdateNbvCompilationBatch(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NbvCompilationBatchDTO nbvCompilationBatchDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NbvCompilationBatch partially : {}, {}", id, nbvCompilationBatchDTO);
        if (nbvCompilationBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nbvCompilationBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nbvCompilationBatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NbvCompilationBatchDTO> result = nbvCompilationBatchService.partialUpdate(nbvCompilationBatchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nbvCompilationBatchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nbv-compilation-batches} : get all the nbvCompilationBatches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nbvCompilationBatches in body.
     */
    @GetMapping("/nbv-compilation-batches")
    public ResponseEntity<List<NbvCompilationBatchDTO>> getAllNbvCompilationBatches(
        NbvCompilationBatchCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get NbvCompilationBatches by criteria: {}", criteria);
        Page<NbvCompilationBatchDTO> page = nbvCompilationBatchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nbv-compilation-batches/count} : count all the nbvCompilationBatches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/nbv-compilation-batches/count")
    public ResponseEntity<Long> countNbvCompilationBatches(NbvCompilationBatchCriteria criteria) {
        log.debug("REST request to count NbvCompilationBatches by criteria: {}", criteria);
        return ResponseEntity.ok().body(nbvCompilationBatchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /nbv-compilation-batches/:id} : get the "id" nbvCompilationBatch.
     *
     * @param id the id of the nbvCompilationBatchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nbvCompilationBatchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nbv-compilation-batches/{id}")
    public ResponseEntity<NbvCompilationBatchDTO> getNbvCompilationBatch(@PathVariable Long id) {
        log.debug("REST request to get NbvCompilationBatch : {}", id);
        Optional<NbvCompilationBatchDTO> nbvCompilationBatchDTO = nbvCompilationBatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nbvCompilationBatchDTO);
    }

    /**
     * {@code DELETE  /nbv-compilation-batches/:id} : delete the "id" nbvCompilationBatch.
     *
     * @param id the id of the nbvCompilationBatchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nbv-compilation-batches/{id}")
    public ResponseEntity<Void> deleteNbvCompilationBatch(@PathVariable Long id) {
        log.debug("REST request to delete NbvCompilationBatch : {}", id);
        nbvCompilationBatchService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/nbv-compilation-batches?query=:query} : search for the nbvCompilationBatch corresponding
     * to the query.
     *
     * @param query the query of the nbvCompilationBatch search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/nbv-compilation-batches")
    public ResponseEntity<List<NbvCompilationBatchDTO>> searchNbvCompilationBatches(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of NbvCompilationBatches for query {}", query);
        Page<NbvCompilationBatchDTO> page = nbvCompilationBatchService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
