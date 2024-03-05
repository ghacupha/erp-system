package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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

import io.github.erp.repository.LoanProductTypeRepository;
import io.github.erp.service.LoanProductTypeQueryService;
import io.github.erp.service.LoanProductTypeService;
import io.github.erp.service.criteria.LoanProductTypeCriteria;
import io.github.erp.service.dto.LoanProductTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LoanProductType}.
 */
@RestController
@RequestMapping("/api")
public class LoanProductTypeResource {

    private final Logger log = LoggerFactory.getLogger(LoanProductTypeResource.class);

    private static final String ENTITY_NAME = "loanProductType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanProductTypeService loanProductTypeService;

    private final LoanProductTypeRepository loanProductTypeRepository;

    private final LoanProductTypeQueryService loanProductTypeQueryService;

    public LoanProductTypeResource(
        LoanProductTypeService loanProductTypeService,
        LoanProductTypeRepository loanProductTypeRepository,
        LoanProductTypeQueryService loanProductTypeQueryService
    ) {
        this.loanProductTypeService = loanProductTypeService;
        this.loanProductTypeRepository = loanProductTypeRepository;
        this.loanProductTypeQueryService = loanProductTypeQueryService;
    }

    /**
     * {@code POST  /loan-product-types} : Create a new loanProductType.
     *
     * @param loanProductTypeDTO the loanProductTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanProductTypeDTO, or with status {@code 400 (Bad Request)} if the loanProductType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loan-product-types")
    public ResponseEntity<LoanProductTypeDTO> createLoanProductType(@Valid @RequestBody LoanProductTypeDTO loanProductTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save LoanProductType : {}", loanProductTypeDTO);
        if (loanProductTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanProductType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanProductTypeDTO result = loanProductTypeService.save(loanProductTypeDTO);
        return ResponseEntity
            .created(new URI("/api/loan-product-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loan-product-types/:id} : Updates an existing loanProductType.
     *
     * @param id the id of the loanProductTypeDTO to save.
     * @param loanProductTypeDTO the loanProductTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanProductTypeDTO,
     * or with status {@code 400 (Bad Request)} if the loanProductTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanProductTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loan-product-types/{id}")
    public ResponseEntity<LoanProductTypeDTO> updateLoanProductType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoanProductTypeDTO loanProductTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoanProductType : {}, {}", id, loanProductTypeDTO);
        if (loanProductTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanProductTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanProductTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoanProductTypeDTO result = loanProductTypeService.save(loanProductTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanProductTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loan-product-types/:id} : Partial updates given fields of an existing loanProductType, field will ignore if it is null
     *
     * @param id the id of the loanProductTypeDTO to save.
     * @param loanProductTypeDTO the loanProductTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanProductTypeDTO,
     * or with status {@code 400 (Bad Request)} if the loanProductTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanProductTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanProductTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loan-product-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanProductTypeDTO> partialUpdateLoanProductType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoanProductTypeDTO loanProductTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanProductType partially : {}, {}", id, loanProductTypeDTO);
        if (loanProductTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanProductTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanProductTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanProductTypeDTO> result = loanProductTypeService.partialUpdate(loanProductTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanProductTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loan-product-types} : get all the loanProductTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanProductTypes in body.
     */
    @GetMapping("/loan-product-types")
    public ResponseEntity<List<LoanProductTypeDTO>> getAllLoanProductTypes(LoanProductTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LoanProductTypes by criteria: {}", criteria);
        Page<LoanProductTypeDTO> page = loanProductTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loan-product-types/count} : count all the loanProductTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/loan-product-types/count")
    public ResponseEntity<Long> countLoanProductTypes(LoanProductTypeCriteria criteria) {
        log.debug("REST request to count LoanProductTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(loanProductTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /loan-product-types/:id} : get the "id" loanProductType.
     *
     * @param id the id of the loanProductTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanProductTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loan-product-types/{id}")
    public ResponseEntity<LoanProductTypeDTO> getLoanProductType(@PathVariable Long id) {
        log.debug("REST request to get LoanProductType : {}", id);
        Optional<LoanProductTypeDTO> loanProductTypeDTO = loanProductTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanProductTypeDTO);
    }

    /**
     * {@code DELETE  /loan-product-types/:id} : delete the "id" loanProductType.
     *
     * @param id the id of the loanProductTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loan-product-types/{id}")
    public ResponseEntity<Void> deleteLoanProductType(@PathVariable Long id) {
        log.debug("REST request to delete LoanProductType : {}", id);
        loanProductTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loan-product-types?query=:query} : search for the loanProductType corresponding
     * to the query.
     *
     * @param query the query of the loanProductType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loan-product-types")
    public ResponseEntity<List<LoanProductTypeDTO>> searchLoanProductTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoanProductTypes for query {}", query);
        Page<LoanProductTypeDTO> page = loanProductTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
