package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.repository.FinancialDerivativeTypeCodeRepository;
import io.github.erp.service.FinancialDerivativeTypeCodeQueryService;
import io.github.erp.service.FinancialDerivativeTypeCodeService;
import io.github.erp.service.criteria.FinancialDerivativeTypeCodeCriteria;
import io.github.erp.service.dto.FinancialDerivativeTypeCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FinancialDerivativeTypeCode}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class FinancialDerivativeTypeCodeResourceProd {

    private final Logger log = LoggerFactory.getLogger(FinancialDerivativeTypeCodeResourceProd.class);

    private static final String ENTITY_NAME = "financialDerivativeTypeCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FinancialDerivativeTypeCodeService financialDerivativeTypeCodeService;

    private final FinancialDerivativeTypeCodeRepository financialDerivativeTypeCodeRepository;

    private final FinancialDerivativeTypeCodeQueryService financialDerivativeTypeCodeQueryService;

    public FinancialDerivativeTypeCodeResourceProd(
        FinancialDerivativeTypeCodeService financialDerivativeTypeCodeService,
        FinancialDerivativeTypeCodeRepository financialDerivativeTypeCodeRepository,
        FinancialDerivativeTypeCodeQueryService financialDerivativeTypeCodeQueryService
    ) {
        this.financialDerivativeTypeCodeService = financialDerivativeTypeCodeService;
        this.financialDerivativeTypeCodeRepository = financialDerivativeTypeCodeRepository;
        this.financialDerivativeTypeCodeQueryService = financialDerivativeTypeCodeQueryService;
    }

    /**
     * {@code POST  /financial-derivative-type-codes} : Create a new financialDerivativeTypeCode.
     *
     * @param financialDerivativeTypeCodeDTO the financialDerivativeTypeCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new financialDerivativeTypeCodeDTO, or with status {@code 400 (Bad Request)} if the financialDerivativeTypeCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/financial-derivative-type-codes")
    public ResponseEntity<FinancialDerivativeTypeCodeDTO> createFinancialDerivativeTypeCode(
        @Valid @RequestBody FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save FinancialDerivativeTypeCode : {}", financialDerivativeTypeCodeDTO);
        if (financialDerivativeTypeCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new financialDerivativeTypeCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FinancialDerivativeTypeCodeDTO result = financialDerivativeTypeCodeService.save(financialDerivativeTypeCodeDTO);
        return ResponseEntity
            .created(new URI("/api/financial-derivative-type-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /financial-derivative-type-codes/:id} : Updates an existing financialDerivativeTypeCode.
     *
     * @param id the id of the financialDerivativeTypeCodeDTO to save.
     * @param financialDerivativeTypeCodeDTO the financialDerivativeTypeCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialDerivativeTypeCodeDTO,
     * or with status {@code 400 (Bad Request)} if the financialDerivativeTypeCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the financialDerivativeTypeCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/financial-derivative-type-codes/{id}")
    public ResponseEntity<FinancialDerivativeTypeCodeDTO> updateFinancialDerivativeTypeCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FinancialDerivativeTypeCode : {}, {}", id, financialDerivativeTypeCodeDTO);
        if (financialDerivativeTypeCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialDerivativeTypeCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialDerivativeTypeCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FinancialDerivativeTypeCodeDTO result = financialDerivativeTypeCodeService.save(financialDerivativeTypeCodeDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialDerivativeTypeCodeDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /financial-derivative-type-codes/:id} : Partial updates given fields of an existing financialDerivativeTypeCode, field will ignore if it is null
     *
     * @param id the id of the financialDerivativeTypeCodeDTO to save.
     * @param financialDerivativeTypeCodeDTO the financialDerivativeTypeCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated financialDerivativeTypeCodeDTO,
     * or with status {@code 400 (Bad Request)} if the financialDerivativeTypeCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the financialDerivativeTypeCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the financialDerivativeTypeCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/financial-derivative-type-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FinancialDerivativeTypeCodeDTO> partialUpdateFinancialDerivativeTypeCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FinancialDerivativeTypeCodeDTO financialDerivativeTypeCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FinancialDerivativeTypeCode partially : {}, {}", id, financialDerivativeTypeCodeDTO);
        if (financialDerivativeTypeCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, financialDerivativeTypeCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!financialDerivativeTypeCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FinancialDerivativeTypeCodeDTO> result = financialDerivativeTypeCodeService.partialUpdate(financialDerivativeTypeCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, financialDerivativeTypeCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /financial-derivative-type-codes} : get all the financialDerivativeTypeCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of financialDerivativeTypeCodes in body.
     */
    @GetMapping("/financial-derivative-type-codes")
    public ResponseEntity<List<FinancialDerivativeTypeCodeDTO>> getAllFinancialDerivativeTypeCodes(
        FinancialDerivativeTypeCodeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get FinancialDerivativeTypeCodes by criteria: {}", criteria);
        Page<FinancialDerivativeTypeCodeDTO> page = financialDerivativeTypeCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /financial-derivative-type-codes/count} : count all the financialDerivativeTypeCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/financial-derivative-type-codes/count")
    public ResponseEntity<Long> countFinancialDerivativeTypeCodes(FinancialDerivativeTypeCodeCriteria criteria) {
        log.debug("REST request to count FinancialDerivativeTypeCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(financialDerivativeTypeCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /financial-derivative-type-codes/:id} : get the "id" financialDerivativeTypeCode.
     *
     * @param id the id of the financialDerivativeTypeCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the financialDerivativeTypeCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/financial-derivative-type-codes/{id}")
    public ResponseEntity<FinancialDerivativeTypeCodeDTO> getFinancialDerivativeTypeCode(@PathVariable Long id) {
        log.debug("REST request to get FinancialDerivativeTypeCode : {}", id);
        Optional<FinancialDerivativeTypeCodeDTO> financialDerivativeTypeCodeDTO = financialDerivativeTypeCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(financialDerivativeTypeCodeDTO);
    }

    /**
     * {@code DELETE  /financial-derivative-type-codes/:id} : delete the "id" financialDerivativeTypeCode.
     *
     * @param id the id of the financialDerivativeTypeCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/financial-derivative-type-codes/{id}")
    public ResponseEntity<Void> deleteFinancialDerivativeTypeCode(@PathVariable Long id) {
        log.debug("REST request to delete FinancialDerivativeTypeCode : {}", id);
        financialDerivativeTypeCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/financial-derivative-type-codes?query=:query} : search for the financialDerivativeTypeCode corresponding
     * to the query.
     *
     * @param query the query of the financialDerivativeTypeCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/financial-derivative-type-codes")
    public ResponseEntity<List<FinancialDerivativeTypeCodeDTO>> searchFinancialDerivativeTypeCodes(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of FinancialDerivativeTypeCodes for query {}", query);
        Page<FinancialDerivativeTypeCodeDTO> page = financialDerivativeTypeCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
