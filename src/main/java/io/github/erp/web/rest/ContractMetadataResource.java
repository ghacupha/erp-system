package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.1
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

import io.github.erp.repository.ContractMetadataRepository;
import io.github.erp.service.ContractMetadataQueryService;
import io.github.erp.service.ContractMetadataService;
import io.github.erp.service.criteria.ContractMetadataCriteria;
import io.github.erp.service.dto.ContractMetadataDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ContractMetadata}.
 */
@RestController
@RequestMapping("/api")
public class ContractMetadataResource {

    private final Logger log = LoggerFactory.getLogger(ContractMetadataResource.class);

    private static final String ENTITY_NAME = "contractMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractMetadataService contractMetadataService;

    private final ContractMetadataRepository contractMetadataRepository;

    private final ContractMetadataQueryService contractMetadataQueryService;

    public ContractMetadataResource(
        ContractMetadataService contractMetadataService,
        ContractMetadataRepository contractMetadataRepository,
        ContractMetadataQueryService contractMetadataQueryService
    ) {
        this.contractMetadataService = contractMetadataService;
        this.contractMetadataRepository = contractMetadataRepository;
        this.contractMetadataQueryService = contractMetadataQueryService;
    }

    /**
     * {@code POST  /contract-metadata} : Create a new contractMetadata.
     *
     * @param contractMetadataDTO the contractMetadataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractMetadataDTO, or with status {@code 400 (Bad Request)} if the contractMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contract-metadata")
    public ResponseEntity<ContractMetadataDTO> createContractMetadata(@Valid @RequestBody ContractMetadataDTO contractMetadataDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContractMetadata : {}", contractMetadataDTO);
        if (contractMetadataDTO.getId() != null) {
            throw new BadRequestAlertException("A new contractMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContractMetadataDTO result = contractMetadataService.save(contractMetadataDTO);
        return ResponseEntity
            .created(new URI("/api/contract-metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contract-metadata/:id} : Updates an existing contractMetadata.
     *
     * @param id the id of the contractMetadataDTO to save.
     * @param contractMetadataDTO the contractMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the contractMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contract-metadata/{id}")
    public ResponseEntity<ContractMetadataDTO> updateContractMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractMetadataDTO contractMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContractMetadata : {}, {}", id, contractMetadataDTO);
        if (contractMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContractMetadataDTO result = contractMetadataService.save(contractMetadataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contractMetadataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contract-metadata/:id} : Partial updates given fields of an existing contractMetadata, field will ignore if it is null
     *
     * @param id the id of the contractMetadataDTO to save.
     * @param contractMetadataDTO the contractMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the contractMetadataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contractMetadataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contract-metadata/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractMetadataDTO> partialUpdateContractMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractMetadataDTO contractMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContractMetadata partially : {}, {}", id, contractMetadataDTO);
        if (contractMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractMetadataDTO> result = contractMetadataService.partialUpdate(contractMetadataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, contractMetadataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contract-metadata} : get all the contractMetadata.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractMetadata in body.
     */
    @GetMapping("/contract-metadata")
    public ResponseEntity<List<ContractMetadataDTO>> getAllContractMetadata(ContractMetadataCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContractMetadata by criteria: {}", criteria);
        Page<ContractMetadataDTO> page = contractMetadataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contract-metadata/count} : count all the contractMetadata.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contract-metadata/count")
    public ResponseEntity<Long> countContractMetadata(ContractMetadataCriteria criteria) {
        log.debug("REST request to count ContractMetadata by criteria: {}", criteria);
        return ResponseEntity.ok().body(contractMetadataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contract-metadata/:id} : get the "id" contractMetadata.
     *
     * @param id the id of the contractMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contract-metadata/{id}")
    public ResponseEntity<ContractMetadataDTO> getContractMetadata(@PathVariable Long id) {
        log.debug("REST request to get ContractMetadata : {}", id);
        Optional<ContractMetadataDTO> contractMetadataDTO = contractMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractMetadataDTO);
    }

    /**
     * {@code DELETE  /contract-metadata/:id} : delete the "id" contractMetadata.
     *
     * @param id the id of the contractMetadataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contract-metadata/{id}")
    public ResponseEntity<Void> deleteContractMetadata(@PathVariable Long id) {
        log.debug("REST request to delete ContractMetadata : {}", id);
        contractMetadataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/contract-metadata?query=:query} : search for the contractMetadata corresponding
     * to the query.
     *
     * @param query the query of the contractMetadata search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/contract-metadata")
    public ResponseEntity<List<ContractMetadataDTO>> searchContractMetadata(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ContractMetadata for query {}", query);
        Page<ContractMetadataDTO> page = contractMetadataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
