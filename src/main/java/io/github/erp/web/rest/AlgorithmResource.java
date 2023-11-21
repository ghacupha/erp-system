package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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

import io.github.erp.repository.AlgorithmRepository;
import io.github.erp.service.AlgorithmQueryService;
import io.github.erp.service.AlgorithmService;
import io.github.erp.service.criteria.AlgorithmCriteria;
import io.github.erp.service.dto.AlgorithmDTO;
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
 * REST controller for managing {@link io.github.erp.domain.Algorithm}.
 */
@RestController
@RequestMapping("/api")
public class AlgorithmResource {

    private final Logger log = LoggerFactory.getLogger(AlgorithmResource.class);

    private static final String ENTITY_NAME = "algorithm";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AlgorithmService algorithmService;

    private final AlgorithmRepository algorithmRepository;

    private final AlgorithmQueryService algorithmQueryService;

    public AlgorithmResource(
        AlgorithmService algorithmService,
        AlgorithmRepository algorithmRepository,
        AlgorithmQueryService algorithmQueryService
    ) {
        this.algorithmService = algorithmService;
        this.algorithmRepository = algorithmRepository;
        this.algorithmQueryService = algorithmQueryService;
    }

    /**
     * {@code POST  /algorithms} : Create a new algorithm.
     *
     * @param algorithmDTO the algorithmDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new algorithmDTO, or with status {@code 400 (Bad Request)} if the algorithm has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/algorithms")
    public ResponseEntity<AlgorithmDTO> createAlgorithm(@Valid @RequestBody AlgorithmDTO algorithmDTO) throws URISyntaxException {
        log.debug("REST request to save Algorithm : {}", algorithmDTO);
        if (algorithmDTO.getId() != null) {
            throw new BadRequestAlertException("A new algorithm cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AlgorithmDTO result = algorithmService.save(algorithmDTO);
        return ResponseEntity
            .created(new URI("/api/algorithms/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /algorithms/:id} : Updates an existing algorithm.
     *
     * @param id the id of the algorithmDTO to save.
     * @param algorithmDTO the algorithmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated algorithmDTO,
     * or with status {@code 400 (Bad Request)} if the algorithmDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the algorithmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/algorithms/{id}")
    public ResponseEntity<AlgorithmDTO> updateAlgorithm(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AlgorithmDTO algorithmDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Algorithm : {}, {}", id, algorithmDTO);
        if (algorithmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, algorithmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!algorithmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AlgorithmDTO result = algorithmService.save(algorithmDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, algorithmDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /algorithms/:id} : Partial updates given fields of an existing algorithm, field will ignore if it is null
     *
     * @param id the id of the algorithmDTO to save.
     * @param algorithmDTO the algorithmDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated algorithmDTO,
     * or with status {@code 400 (Bad Request)} if the algorithmDTO is not valid,
     * or with status {@code 404 (Not Found)} if the algorithmDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the algorithmDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/algorithms/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AlgorithmDTO> partialUpdateAlgorithm(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AlgorithmDTO algorithmDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Algorithm partially : {}, {}", id, algorithmDTO);
        if (algorithmDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, algorithmDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!algorithmRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AlgorithmDTO> result = algorithmService.partialUpdate(algorithmDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, algorithmDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /algorithms} : get all the algorithms.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of algorithms in body.
     */
    @GetMapping("/algorithms")
    public ResponseEntity<List<AlgorithmDTO>> getAllAlgorithms(AlgorithmCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Algorithms by criteria: {}", criteria);
        Page<AlgorithmDTO> page = algorithmQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /algorithms/count} : count all the algorithms.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/algorithms/count")
    public ResponseEntity<Long> countAlgorithms(AlgorithmCriteria criteria) {
        log.debug("REST request to count Algorithms by criteria: {}", criteria);
        return ResponseEntity.ok().body(algorithmQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /algorithms/:id} : get the "id" algorithm.
     *
     * @param id the id of the algorithmDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the algorithmDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/algorithms/{id}")
    public ResponseEntity<AlgorithmDTO> getAlgorithm(@PathVariable Long id) {
        log.debug("REST request to get Algorithm : {}", id);
        Optional<AlgorithmDTO> algorithmDTO = algorithmService.findOne(id);
        return ResponseUtil.wrapOrNotFound(algorithmDTO);
    }

    /**
     * {@code DELETE  /algorithms/:id} : delete the "id" algorithm.
     *
     * @param id the id of the algorithmDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/algorithms/{id}")
    public ResponseEntity<Void> deleteAlgorithm(@PathVariable Long id) {
        log.debug("REST request to delete Algorithm : {}", id);
        algorithmService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/algorithms?query=:query} : search for the algorithm corresponding
     * to the query.
     *
     * @param query the query of the algorithm search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/algorithms")
    public ResponseEntity<List<AlgorithmDTO>> searchAlgorithms(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Algorithms for query {}", query);
        Page<AlgorithmDTO> page = algorithmService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
