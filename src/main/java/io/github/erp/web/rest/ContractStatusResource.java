package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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

import io.github.erp.repository.ContractStatusRepository;
import io.github.erp.service.ContractStatusQueryService;
import io.github.erp.service.ContractStatusService;
import io.github.erp.service.criteria.ContractStatusCriteria;
import io.github.erp.service.dto.ContractStatusDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ContractStatus}.
 */
@RestController
@RequestMapping("/api")
public class ContractStatusResource {

    private final Logger log = LoggerFactory.getLogger(ContractStatusResource.class);

    private static final String ENTITY_NAME = "contractStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ContractStatusService contractStatusService;

    private final ContractStatusRepository contractStatusRepository;

    private final ContractStatusQueryService contractStatusQueryService;

    public ContractStatusResource(
        ContractStatusService contractStatusService,
        ContractStatusRepository contractStatusRepository,
        ContractStatusQueryService contractStatusQueryService
    ) {
        this.contractStatusService = contractStatusService;
        this.contractStatusRepository = contractStatusRepository;
        this.contractStatusQueryService = contractStatusQueryService;
    }

    /**
     * {@code POST  /contract-statuses} : Create a new contractStatus.
     *
     * @param contractStatusDTO the contractStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractStatusDTO, or with status {@code 400 (Bad Request)} if the contractStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/contract-statuses")
    public ResponseEntity<ContractStatusDTO> createContractStatus(@Valid @RequestBody ContractStatusDTO contractStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to save ContractStatus : {}", contractStatusDTO);
        if (contractStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new contractStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ContractStatusDTO result = contractStatusService.save(contractStatusDTO);
        return ResponseEntity
            .created(new URI("/api/contract-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /contract-statuses/:id} : Updates an existing contractStatus.
     *
     * @param id the id of the contractStatusDTO to save.
     * @param contractStatusDTO the contractStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractStatusDTO,
     * or with status {@code 400 (Bad Request)} if the contractStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/contract-statuses/{id}")
    public ResponseEntity<ContractStatusDTO> updateContractStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractStatusDTO contractStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ContractStatus : {}, {}", id, contractStatusDTO);
        if (contractStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ContractStatusDTO result = contractStatusService.save(contractStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /contract-statuses/:id} : Partial updates given fields of an existing contractStatus, field will ignore if it is null
     *
     * @param id the id of the contractStatusDTO to save.
     * @param contractStatusDTO the contractStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractStatusDTO,
     * or with status {@code 400 (Bad Request)} if the contractStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contractStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/contract-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractStatusDTO> partialUpdateContractStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractStatusDTO contractStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ContractStatus partially : {}, {}", id, contractStatusDTO);
        if (contractStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractStatusDTO> result = contractStatusService.partialUpdate(contractStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contract-statuses} : get all the contractStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractStatuses in body.
     */
    @GetMapping("/contract-statuses")
    public ResponseEntity<List<ContractStatusDTO>> getAllContractStatuses(ContractStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ContractStatuses by criteria: {}", criteria);
        Page<ContractStatusDTO> page = contractStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contract-statuses/count} : count all the contractStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/contract-statuses/count")
    public ResponseEntity<Long> countContractStatuses(ContractStatusCriteria criteria) {
        log.debug("REST request to count ContractStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(contractStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /contract-statuses/:id} : get the "id" contractStatus.
     *
     * @param id the id of the contractStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/contract-statuses/{id}")
    public ResponseEntity<ContractStatusDTO> getContractStatus(@PathVariable Long id) {
        log.debug("REST request to get ContractStatus : {}", id);
        Optional<ContractStatusDTO> contractStatusDTO = contractStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractStatusDTO);
    }

    /**
     * {@code DELETE  /contract-statuses/:id} : delete the "id" contractStatus.
     *
     * @param id the id of the contractStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/contract-statuses/{id}")
    public ResponseEntity<Void> deleteContractStatus(@PathVariable Long id) {
        log.debug("REST request to delete ContractStatus : {}", id);
        contractStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/contract-statuses?query=:query} : search for the contractStatus corresponding
     * to the query.
     *
     * @param query the query of the contractStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/contract-statuses")
    public ResponseEntity<List<ContractStatusDTO>> searchContractStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ContractStatuses for query {}", query);
        Page<ContractStatusDTO> page = contractStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
