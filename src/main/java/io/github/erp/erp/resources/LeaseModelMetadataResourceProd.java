package io.github.erp.erp.resources;

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
import io.github.erp.repository.LeaseModelMetadataRepository;
import io.github.erp.service.LeaseModelMetadataQueryService;
import io.github.erp.service.LeaseModelMetadataService;
import io.github.erp.service.criteria.LeaseModelMetadataCriteria;
import io.github.erp.service.dto.LeaseModelMetadataDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseModelMetadata}.
 */
@RestController("leaseModelMetadataResourceProd")
@RequestMapping("/api/leases")
public class LeaseModelMetadataResourceProd {

    private final Logger log = LoggerFactory.getLogger(LeaseModelMetadataResourceProd.class);

    private static final String ENTITY_NAME = "leaseModelMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseModelMetadataService leaseModelMetadataService;

    private final LeaseModelMetadataRepository leaseModelMetadataRepository;

    private final LeaseModelMetadataQueryService leaseModelMetadataQueryService;

    public LeaseModelMetadataResourceProd(
        LeaseModelMetadataService leaseModelMetadataService,
        LeaseModelMetadataRepository leaseModelMetadataRepository,
        LeaseModelMetadataQueryService leaseModelMetadataQueryService
    ) {
        this.leaseModelMetadataService = leaseModelMetadataService;
        this.leaseModelMetadataRepository = leaseModelMetadataRepository;
        this.leaseModelMetadataQueryService = leaseModelMetadataQueryService;
    }

    /**
     * {@code POST  /lease-model-metadata} : Create a new leaseModelMetadata.
     *
     * @param leaseModelMetadataDTO the leaseModelMetadataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseModelMetadataDTO, or with status {@code 400 (Bad Request)} if the leaseModelMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-model-metadata")
    public ResponseEntity<LeaseModelMetadataDTO> createLeaseModelMetadata(@Valid @RequestBody LeaseModelMetadataDTO leaseModelMetadataDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaseModelMetadata : {}", leaseModelMetadataDTO);
        if (leaseModelMetadataDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseModelMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseModelMetadataDTO result = leaseModelMetadataService.save(leaseModelMetadataDTO);
        return ResponseEntity
            .created(new URI("/api/lease-model-metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-model-metadata/:id} : Updates an existing leaseModelMetadata.
     *
     * @param id the id of the leaseModelMetadataDTO to save.
     * @param leaseModelMetadataDTO the leaseModelMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseModelMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the leaseModelMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseModelMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-model-metadata/{id}")
    public ResponseEntity<LeaseModelMetadataDTO> updateLeaseModelMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseModelMetadataDTO leaseModelMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseModelMetadata : {}, {}", id, leaseModelMetadataDTO);
        if (leaseModelMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseModelMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseModelMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseModelMetadataDTO result = leaseModelMetadataService.save(leaseModelMetadataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leaseModelMetadataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lease-model-metadata/:id} : Partial updates given fields of an existing leaseModelMetadata, field will ignore if it is null
     *
     * @param id the id of the leaseModelMetadataDTO to save.
     * @param leaseModelMetadataDTO the leaseModelMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseModelMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the leaseModelMetadataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseModelMetadataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseModelMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-model-metadata/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseModelMetadataDTO> partialUpdateLeaseModelMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseModelMetadataDTO leaseModelMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseModelMetadata partially : {}, {}", id, leaseModelMetadataDTO);
        if (leaseModelMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseModelMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseModelMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseModelMetadataDTO> result = leaseModelMetadataService.partialUpdate(leaseModelMetadataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, leaseModelMetadataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-model-metadata} : get all the leaseModelMetadata.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseModelMetadata in body.
     */
    @GetMapping("/lease-model-metadata")
    public ResponseEntity<List<LeaseModelMetadataDTO>> getAllLeaseModelMetadata(LeaseModelMetadataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaseModelMetadata by criteria: {}", criteria);
        Page<LeaseModelMetadataDTO> page = leaseModelMetadataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-model-metadata/count} : count all the leaseModelMetadata.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-model-metadata/count")
    public ResponseEntity<Long> countLeaseModelMetadata(LeaseModelMetadataCriteria criteria) {
        log.debug("REST request to count LeaseModelMetadata by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseModelMetadataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-model-metadata/:id} : get the "id" leaseModelMetadata.
     *
     * @param id the id of the leaseModelMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseModelMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-model-metadata/{id}")
    public ResponseEntity<LeaseModelMetadataDTO> getLeaseModelMetadata(@PathVariable Long id) {
        log.debug("REST request to get LeaseModelMetadata : {}", id);
        Optional<LeaseModelMetadataDTO> leaseModelMetadataDTO = leaseModelMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseModelMetadataDTO);
    }

    /**
     * {@code DELETE  /lease-model-metadata/:id} : delete the "id" leaseModelMetadata.
     *
     * @param id the id of the leaseModelMetadataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-model-metadata/{id}")
    public ResponseEntity<Void> deleteLeaseModelMetadata(@PathVariable Long id) {
        log.debug("REST request to delete LeaseModelMetadata : {}", id);
        leaseModelMetadataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-model-metadata?query=:query} : search for the leaseModelMetadata corresponding
     * to the query.
     *
     * @param query the query of the leaseModelMetadata search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-model-metadata")
    public ResponseEntity<List<LeaseModelMetadataDTO>> searchLeaseModelMetadata(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeaseModelMetadata for query {}", query);
        Page<LeaseModelMetadataDTO> page = leaseModelMetadataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
