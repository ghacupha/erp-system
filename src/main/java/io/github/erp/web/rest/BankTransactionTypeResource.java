package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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

import io.github.erp.repository.BankTransactionTypeRepository;
import io.github.erp.service.BankTransactionTypeQueryService;
import io.github.erp.service.BankTransactionTypeService;
import io.github.erp.service.criteria.BankTransactionTypeCriteria;
import io.github.erp.service.dto.BankTransactionTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.BankTransactionType}.
 */
@RestController
@RequestMapping("/api")
public class BankTransactionTypeResource {

    private final Logger log = LoggerFactory.getLogger(BankTransactionTypeResource.class);

    private static final String ENTITY_NAME = "bankTransactionType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankTransactionTypeService bankTransactionTypeService;

    private final BankTransactionTypeRepository bankTransactionTypeRepository;

    private final BankTransactionTypeQueryService bankTransactionTypeQueryService;

    public BankTransactionTypeResource(
        BankTransactionTypeService bankTransactionTypeService,
        BankTransactionTypeRepository bankTransactionTypeRepository,
        BankTransactionTypeQueryService bankTransactionTypeQueryService
    ) {
        this.bankTransactionTypeService = bankTransactionTypeService;
        this.bankTransactionTypeRepository = bankTransactionTypeRepository;
        this.bankTransactionTypeQueryService = bankTransactionTypeQueryService;
    }

    /**
     * {@code POST  /bank-transaction-types} : Create a new bankTransactionType.
     *
     * @param bankTransactionTypeDTO the bankTransactionTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankTransactionTypeDTO, or with status {@code 400 (Bad Request)} if the bankTransactionType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bank-transaction-types")
    public ResponseEntity<BankTransactionTypeDTO> createBankTransactionType(
        @Valid @RequestBody BankTransactionTypeDTO bankTransactionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save BankTransactionType : {}", bankTransactionTypeDTO);
        if (bankTransactionTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new bankTransactionType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BankTransactionTypeDTO result = bankTransactionTypeService.save(bankTransactionTypeDTO);
        return ResponseEntity
            .created(new URI("/api/bank-transaction-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bank-transaction-types/:id} : Updates an existing bankTransactionType.
     *
     * @param id the id of the bankTransactionTypeDTO to save.
     * @param bankTransactionTypeDTO the bankTransactionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankTransactionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the bankTransactionTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankTransactionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bank-transaction-types/{id}")
    public ResponseEntity<BankTransactionTypeDTO> updateBankTransactionType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BankTransactionTypeDTO bankTransactionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BankTransactionType : {}, {}", id, bankTransactionTypeDTO);
        if (bankTransactionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankTransactionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankTransactionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BankTransactionTypeDTO result = bankTransactionTypeService.save(bankTransactionTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankTransactionTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bank-transaction-types/:id} : Partial updates given fields of an existing bankTransactionType, field will ignore if it is null
     *
     * @param id the id of the bankTransactionTypeDTO to save.
     * @param bankTransactionTypeDTO the bankTransactionTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankTransactionTypeDTO,
     * or with status {@code 400 (Bad Request)} if the bankTransactionTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bankTransactionTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bankTransactionTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bank-transaction-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BankTransactionTypeDTO> partialUpdateBankTransactionType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BankTransactionTypeDTO bankTransactionTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BankTransactionType partially : {}, {}", id, bankTransactionTypeDTO);
        if (bankTransactionTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankTransactionTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankTransactionTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BankTransactionTypeDTO> result = bankTransactionTypeService.partialUpdate(bankTransactionTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankTransactionTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bank-transaction-types} : get all the bankTransactionTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankTransactionTypes in body.
     */
    @GetMapping("/bank-transaction-types")
    public ResponseEntity<List<BankTransactionTypeDTO>> getAllBankTransactionTypes(
        BankTransactionTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get BankTransactionTypes by criteria: {}", criteria);
        Page<BankTransactionTypeDTO> page = bankTransactionTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bank-transaction-types/count} : count all the bankTransactionTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bank-transaction-types/count")
    public ResponseEntity<Long> countBankTransactionTypes(BankTransactionTypeCriteria criteria) {
        log.debug("REST request to count BankTransactionTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(bankTransactionTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bank-transaction-types/:id} : get the "id" bankTransactionType.
     *
     * @param id the id of the bankTransactionTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankTransactionTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bank-transaction-types/{id}")
    public ResponseEntity<BankTransactionTypeDTO> getBankTransactionType(@PathVariable Long id) {
        log.debug("REST request to get BankTransactionType : {}", id);
        Optional<BankTransactionTypeDTO> bankTransactionTypeDTO = bankTransactionTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankTransactionTypeDTO);
    }

    /**
     * {@code DELETE  /bank-transaction-types/:id} : delete the "id" bankTransactionType.
     *
     * @param id the id of the bankTransactionTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bank-transaction-types/{id}")
    public ResponseEntity<Void> deleteBankTransactionType(@PathVariable Long id) {
        log.debug("REST request to delete BankTransactionType : {}", id);
        bankTransactionTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/bank-transaction-types?query=:query} : search for the bankTransactionType corresponding
     * to the query.
     *
     * @param query the query of the bankTransactionType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/bank-transaction-types")
    public ResponseEntity<List<BankTransactionTypeDTO>> searchBankTransactionTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BankTransactionTypes for query {}", query);
        Page<BankTransactionTypeDTO> page = bankTransactionTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
