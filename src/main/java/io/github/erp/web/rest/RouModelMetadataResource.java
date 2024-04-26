package io.github.erp.web.rest;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.RouModelMetadataRepository;
import io.github.erp.service.RouModelMetadataQueryService;
import io.github.erp.service.RouModelMetadataService;
import io.github.erp.service.criteria.RouModelMetadataCriteria;
import io.github.erp.service.dto.RouModelMetadataDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RouModelMetadata}.
 */
@RestController
@RequestMapping("/api")
public class RouModelMetadataResource {

    private final Logger log = LoggerFactory.getLogger(RouModelMetadataResource.class);

    private static final String ENTITY_NAME = "rouModelMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RouModelMetadataService rouModelMetadataService;

    private final RouModelMetadataRepository rouModelMetadataRepository;

    private final RouModelMetadataQueryService rouModelMetadataQueryService;

    public RouModelMetadataResource(
        RouModelMetadataService rouModelMetadataService,
        RouModelMetadataRepository rouModelMetadataRepository,
        RouModelMetadataQueryService rouModelMetadataQueryService
    ) {
        this.rouModelMetadataService = rouModelMetadataService;
        this.rouModelMetadataRepository = rouModelMetadataRepository;
        this.rouModelMetadataQueryService = rouModelMetadataQueryService;
    }

    /**
     * {@code POST  /rou-model-metadata} : Create a new rouModelMetadata.
     *
     * @param rouModelMetadataDTO the rouModelMetadataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new rouModelMetadataDTO, or with status {@code 400 (Bad Request)} if the rouModelMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/rou-model-metadata")
    public ResponseEntity<RouModelMetadataDTO> createRouModelMetadata(@Valid @RequestBody RouModelMetadataDTO rouModelMetadataDTO)
        throws URISyntaxException {
        log.debug("REST request to save RouModelMetadata : {}", rouModelMetadataDTO);
        if (rouModelMetadataDTO.getId() != null) {
            throw new BadRequestAlertException("A new rouModelMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RouModelMetadataDTO result = rouModelMetadataService.save(rouModelMetadataDTO);
        return ResponseEntity
            .created(new URI("/api/rou-model-metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /rou-model-metadata/:id} : Updates an existing rouModelMetadata.
     *
     * @param id the id of the rouModelMetadataDTO to save.
     * @param rouModelMetadataDTO the rouModelMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouModelMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the rouModelMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the rouModelMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/rou-model-metadata/{id}")
    public ResponseEntity<RouModelMetadataDTO> updateRouModelMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RouModelMetadataDTO rouModelMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RouModelMetadata : {}, {}", id, rouModelMetadataDTO);
        if (rouModelMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouModelMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouModelMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RouModelMetadataDTO result = rouModelMetadataService.save(rouModelMetadataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouModelMetadataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /rou-model-metadata/:id} : Partial updates given fields of an existing rouModelMetadata, field will ignore if it is null
     *
     * @param id the id of the rouModelMetadataDTO to save.
     * @param rouModelMetadataDTO the rouModelMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated rouModelMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the rouModelMetadataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the rouModelMetadataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the rouModelMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/rou-model-metadata/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RouModelMetadataDTO> partialUpdateRouModelMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RouModelMetadataDTO rouModelMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RouModelMetadata partially : {}, {}", id, rouModelMetadataDTO);
        if (rouModelMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, rouModelMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!rouModelMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RouModelMetadataDTO> result = rouModelMetadataService.partialUpdate(rouModelMetadataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, rouModelMetadataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /rou-model-metadata} : get all the rouModelMetadata.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of rouModelMetadata in body.
     */
    @GetMapping("/rou-model-metadata")
    public ResponseEntity<List<RouModelMetadataDTO>> getAllRouModelMetadata(RouModelMetadataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RouModelMetadata by criteria: {}", criteria);
        Page<RouModelMetadataDTO> page = rouModelMetadataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /rou-model-metadata/count} : count all the rouModelMetadata.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/rou-model-metadata/count")
    public ResponseEntity<Long> countRouModelMetadata(RouModelMetadataCriteria criteria) {
        log.debug("REST request to count RouModelMetadata by criteria: {}", criteria);
        return ResponseEntity.ok().body(rouModelMetadataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /rou-model-metadata/:id} : get the "id" rouModelMetadata.
     *
     * @param id the id of the rouModelMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the rouModelMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/rou-model-metadata/{id}")
    public ResponseEntity<RouModelMetadataDTO> getRouModelMetadata(@PathVariable Long id) {
        log.debug("REST request to get RouModelMetadata : {}", id);
        Optional<RouModelMetadataDTO> rouModelMetadataDTO = rouModelMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(rouModelMetadataDTO);
    }

    /**
     * {@code DELETE  /rou-model-metadata/:id} : delete the "id" rouModelMetadata.
     *
     * @param id the id of the rouModelMetadataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/rou-model-metadata/{id}")
    public ResponseEntity<Void> deleteRouModelMetadata(@PathVariable Long id) {
        log.debug("REST request to delete RouModelMetadata : {}", id);
        rouModelMetadataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/rou-model-metadata?query=:query} : search for the rouModelMetadata corresponding
     * to the query.
     *
     * @param query the query of the rouModelMetadata search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/rou-model-metadata")
    public ResponseEntity<List<RouModelMetadataDTO>> searchRouModelMetadata(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RouModelMetadata for query {}", query);
        Page<RouModelMetadataDTO> page = rouModelMetadataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
