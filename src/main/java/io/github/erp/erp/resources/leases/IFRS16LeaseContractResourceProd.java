package io.github.erp.erp.resources.leases;

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
import io.github.erp.repository.IFRS16LeaseContractRepository;
import io.github.erp.service.IFRS16LeaseContractQueryService;
import io.github.erp.service.IFRS16LeaseContractService;
import io.github.erp.service.criteria.IFRS16LeaseContractCriteria;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
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
 * REST controller for managing {@link io.github.erp.domain.IFRS16LeaseContract}.
 */
@RestController
@RequestMapping("/api/leases")
public class IFRS16LeaseContractResourceProd {

    private final Logger log = LoggerFactory.getLogger(IFRS16LeaseContractResourceProd.class);

    private static final String ENTITY_NAME = "iFRS16LeaseContract";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IFRS16LeaseContractService iFRS16LeaseContractService;

    private final IFRS16LeaseContractRepository iFRS16LeaseContractRepository;

    private final IFRS16LeaseContractQueryService iFRS16LeaseContractQueryService;

    public IFRS16LeaseContractResourceProd(
        IFRS16LeaseContractService iFRS16LeaseContractService,
        IFRS16LeaseContractRepository iFRS16LeaseContractRepository,
        IFRS16LeaseContractQueryService iFRS16LeaseContractQueryService
    ) {
        this.iFRS16LeaseContractService = iFRS16LeaseContractService;
        this.iFRS16LeaseContractRepository = iFRS16LeaseContractRepository;
        this.iFRS16LeaseContractQueryService = iFRS16LeaseContractQueryService;
    }

    /**
     * {@code POST  /ifrs-16-lease-contracts} : Create a new iFRS16LeaseContract.
     *
     * @param iFRS16LeaseContractDTO the iFRS16LeaseContractDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new iFRS16LeaseContractDTO, or with status {@code 400 (Bad Request)} if the iFRS16LeaseContract has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ifrs-16-lease-contracts")
    public ResponseEntity<IFRS16LeaseContractDTO> createIFRS16LeaseContract(
        @Valid @RequestBody IFRS16LeaseContractDTO iFRS16LeaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to save IFRS16LeaseContract : {}", iFRS16LeaseContractDTO);
        if (iFRS16LeaseContractDTO.getId() != null) {
            throw new BadRequestAlertException("A new iFRS16LeaseContract cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IFRS16LeaseContractDTO result = iFRS16LeaseContractService.save(iFRS16LeaseContractDTO);
        return ResponseEntity
            .created(new URI("/api/ifrs-16-lease-contracts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ifrs-16-lease-contracts/:id} : Updates an existing iFRS16LeaseContract.
     *
     * @param id the id of the iFRS16LeaseContractDTO to save.
     * @param iFRS16LeaseContractDTO the iFRS16LeaseContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iFRS16LeaseContractDTO,
     * or with status {@code 400 (Bad Request)} if the iFRS16LeaseContractDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the iFRS16LeaseContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ifrs-16-lease-contracts/{id}")
    public ResponseEntity<IFRS16LeaseContractDTO> updateIFRS16LeaseContract(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IFRS16LeaseContractDTO iFRS16LeaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IFRS16LeaseContract : {}, {}", id, iFRS16LeaseContractDTO);
        if (iFRS16LeaseContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iFRS16LeaseContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iFRS16LeaseContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IFRS16LeaseContractDTO result = iFRS16LeaseContractService.save(iFRS16LeaseContractDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iFRS16LeaseContractDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ifrs-16-lease-contracts/:id} : Partial updates given fields of an existing iFRS16LeaseContract, field will ignore if it is null
     *
     * @param id the id of the iFRS16LeaseContractDTO to save.
     * @param iFRS16LeaseContractDTO the iFRS16LeaseContractDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated iFRS16LeaseContractDTO,
     * or with status {@code 400 (Bad Request)} if the iFRS16LeaseContractDTO is not valid,
     * or with status {@code 404 (Not Found)} if the iFRS16LeaseContractDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the iFRS16LeaseContractDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ifrs-16-lease-contracts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IFRS16LeaseContractDTO> partialUpdateIFRS16LeaseContract(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IFRS16LeaseContractDTO iFRS16LeaseContractDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IFRS16LeaseContract partially : {}, {}", id, iFRS16LeaseContractDTO);
        if (iFRS16LeaseContractDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, iFRS16LeaseContractDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!iFRS16LeaseContractRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IFRS16LeaseContractDTO> result = iFRS16LeaseContractService.partialUpdate(iFRS16LeaseContractDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, iFRS16LeaseContractDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ifrs-16-lease-contracts} : get all the iFRS16LeaseContracts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of iFRS16LeaseContracts in body.
     */
    @GetMapping("/ifrs-16-lease-contracts")
    public ResponseEntity<List<IFRS16LeaseContractDTO>> getAllIFRS16LeaseContracts(
        IFRS16LeaseContractCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get IFRS16LeaseContracts by criteria: {}", criteria);
        Page<IFRS16LeaseContractDTO> page = iFRS16LeaseContractQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ifrs-16-lease-contracts/count} : count all the iFRS16LeaseContracts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ifrs-16-lease-contracts/count")
    public ResponseEntity<Long> countIFRS16LeaseContracts(IFRS16LeaseContractCriteria criteria) {
        log.debug("REST request to count IFRS16LeaseContracts by criteria: {}", criteria);
        return ResponseEntity.ok().body(iFRS16LeaseContractQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ifrs-16-lease-contracts/:id} : get the "id" iFRS16LeaseContract.
     *
     * @param id the id of the iFRS16LeaseContractDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the iFRS16LeaseContractDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ifrs-16-lease-contracts/{id}")
    public ResponseEntity<IFRS16LeaseContractDTO> getIFRS16LeaseContract(@PathVariable Long id) {
        log.debug("REST request to get IFRS16LeaseContract : {}", id);
        Optional<IFRS16LeaseContractDTO> iFRS16LeaseContractDTO = iFRS16LeaseContractService.findOne(id);
        return ResponseUtil.wrapOrNotFound(iFRS16LeaseContractDTO);
    }

    /**
     * {@code DELETE  /ifrs-16-lease-contracts/:id} : delete the "id" iFRS16LeaseContract.
     *
     * @param id the id of the iFRS16LeaseContractDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ifrs-16-lease-contracts/{id}")
    public ResponseEntity<Void> deleteIFRS16LeaseContract(@PathVariable Long id) {
        log.debug("REST request to delete IFRS16LeaseContract : {}", id);
        iFRS16LeaseContractService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ifrs-16-lease-contracts?query=:query} : search for the iFRS16LeaseContract corresponding
     * to the query.
     *
     * @param query the query of the iFRS16LeaseContract search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ifrs-16-lease-contracts")
    public ResponseEntity<List<IFRS16LeaseContractDTO>> searchIFRS16LeaseContracts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IFRS16LeaseContracts for query {}", query);
        Page<IFRS16LeaseContractDTO> page = iFRS16LeaseContractService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
