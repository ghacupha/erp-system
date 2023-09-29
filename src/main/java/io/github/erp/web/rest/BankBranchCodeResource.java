package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

import io.github.erp.repository.BankBranchCodeRepository;
import io.github.erp.service.BankBranchCodeQueryService;
import io.github.erp.service.BankBranchCodeService;
import io.github.erp.service.criteria.BankBranchCodeCriteria;
import io.github.erp.service.dto.BankBranchCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.BankBranchCode}.
 */
@RestController
@RequestMapping("/api")
public class BankBranchCodeResource {

    private final Logger log = LoggerFactory.getLogger(BankBranchCodeResource.class);

    private static final String ENTITY_NAME = "bankBranchCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankBranchCodeService bankBranchCodeService;

    private final BankBranchCodeRepository bankBranchCodeRepository;

    private final BankBranchCodeQueryService bankBranchCodeQueryService;

    public BankBranchCodeResource(
        BankBranchCodeService bankBranchCodeService,
        BankBranchCodeRepository bankBranchCodeRepository,
        BankBranchCodeQueryService bankBranchCodeQueryService
    ) {
        this.bankBranchCodeService = bankBranchCodeService;
        this.bankBranchCodeRepository = bankBranchCodeRepository;
        this.bankBranchCodeQueryService = bankBranchCodeQueryService;
    }

    /**
     * {@code POST  /bank-branch-codes} : Create a new bankBranchCode.
     *
     * @param bankBranchCodeDTO the bankBranchCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankBranchCodeDTO, or with status {@code 400 (Bad Request)} if the bankBranchCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bank-branch-codes")
    public ResponseEntity<BankBranchCodeDTO> createBankBranchCode(@Valid @RequestBody BankBranchCodeDTO bankBranchCodeDTO)
        throws URISyntaxException {
        log.debug("REST request to save BankBranchCode : {}", bankBranchCodeDTO);
        if (bankBranchCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new bankBranchCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BankBranchCodeDTO result = bankBranchCodeService.save(bankBranchCodeDTO);
        return ResponseEntity
            .created(new URI("/api/bank-branch-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bank-branch-codes/:id} : Updates an existing bankBranchCode.
     *
     * @param id the id of the bankBranchCodeDTO to save.
     * @param bankBranchCodeDTO the bankBranchCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankBranchCodeDTO,
     * or with status {@code 400 (Bad Request)} if the bankBranchCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankBranchCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bank-branch-codes/{id}")
    public ResponseEntity<BankBranchCodeDTO> updateBankBranchCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BankBranchCodeDTO bankBranchCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BankBranchCode : {}, {}", id, bankBranchCodeDTO);
        if (bankBranchCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankBranchCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankBranchCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BankBranchCodeDTO result = bankBranchCodeService.save(bankBranchCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankBranchCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /bank-branch-codes/:id} : Partial updates given fields of an existing bankBranchCode, field will ignore if it is null
     *
     * @param id the id of the bankBranchCodeDTO to save.
     * @param bankBranchCodeDTO the bankBranchCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankBranchCodeDTO,
     * or with status {@code 400 (Bad Request)} if the bankBranchCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bankBranchCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bankBranchCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/bank-branch-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BankBranchCodeDTO> partialUpdateBankBranchCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BankBranchCodeDTO bankBranchCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BankBranchCode partially : {}, {}", id, bankBranchCodeDTO);
        if (bankBranchCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankBranchCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankBranchCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BankBranchCodeDTO> result = bankBranchCodeService.partialUpdate(bankBranchCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankBranchCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bank-branch-codes} : get all the bankBranchCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankBranchCodes in body.
     */
    @GetMapping("/bank-branch-codes")
    public ResponseEntity<List<BankBranchCodeDTO>> getAllBankBranchCodes(BankBranchCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BankBranchCodes by criteria: {}", criteria);
        Page<BankBranchCodeDTO> page = bankBranchCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bank-branch-codes/count} : count all the bankBranchCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/bank-branch-codes/count")
    public ResponseEntity<Long> countBankBranchCodes(BankBranchCodeCriteria criteria) {
        log.debug("REST request to count BankBranchCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(bankBranchCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bank-branch-codes/:id} : get the "id" bankBranchCode.
     *
     * @param id the id of the bankBranchCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankBranchCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bank-branch-codes/{id}")
    public ResponseEntity<BankBranchCodeDTO> getBankBranchCode(@PathVariable Long id) {
        log.debug("REST request to get BankBranchCode : {}", id);
        Optional<BankBranchCodeDTO> bankBranchCodeDTO = bankBranchCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankBranchCodeDTO);
    }

    /**
     * {@code DELETE  /bank-branch-codes/:id} : delete the "id" bankBranchCode.
     *
     * @param id the id of the bankBranchCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bank-branch-codes/{id}")
    public ResponseEntity<Void> deleteBankBranchCode(@PathVariable Long id) {
        log.debug("REST request to delete BankBranchCode : {}", id);
        bankBranchCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/bank-branch-codes?query=:query} : search for the bankBranchCode corresponding
     * to the query.
     *
     * @param query the query of the bankBranchCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/bank-branch-codes")
    public ResponseEntity<List<BankBranchCodeDTO>> searchBankBranchCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BankBranchCodes for query {}", query);
        Page<BankBranchCodeDTO> page = bankBranchCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
