package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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

import io.github.erp.repository.LoanApplicationTypeRepository;
import io.github.erp.service.LoanApplicationTypeQueryService;
import io.github.erp.service.LoanApplicationTypeService;
import io.github.erp.service.criteria.LoanApplicationTypeCriteria;
import io.github.erp.service.dto.LoanApplicationTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LoanApplicationType}.
 */
@RestController
@RequestMapping("/api")
public class LoanApplicationTypeResource {

    private final Logger log = LoggerFactory.getLogger(LoanApplicationTypeResource.class);

    private static final String ENTITY_NAME = "loanApplicationType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LoanApplicationTypeService loanApplicationTypeService;

    private final LoanApplicationTypeRepository loanApplicationTypeRepository;

    private final LoanApplicationTypeQueryService loanApplicationTypeQueryService;

    public LoanApplicationTypeResource(
        LoanApplicationTypeService loanApplicationTypeService,
        LoanApplicationTypeRepository loanApplicationTypeRepository,
        LoanApplicationTypeQueryService loanApplicationTypeQueryService
    ) {
        this.loanApplicationTypeService = loanApplicationTypeService;
        this.loanApplicationTypeRepository = loanApplicationTypeRepository;
        this.loanApplicationTypeQueryService = loanApplicationTypeQueryService;
    }

    /**
     * {@code POST  /loan-application-types} : Create a new loanApplicationType.
     *
     * @param loanApplicationTypeDTO the loanApplicationTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new loanApplicationTypeDTO, or with status {@code 400 (Bad Request)} if the loanApplicationType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/loan-application-types")
    public ResponseEntity<LoanApplicationTypeDTO> createLoanApplicationType(
        @Valid @RequestBody LoanApplicationTypeDTO loanApplicationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LoanApplicationType : {}", loanApplicationTypeDTO);
        if (loanApplicationTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new loanApplicationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LoanApplicationTypeDTO result = loanApplicationTypeService.save(loanApplicationTypeDTO);
        return ResponseEntity
            .created(new URI("/api/loan-application-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /loan-application-types/:id} : Updates an existing loanApplicationType.
     *
     * @param id the id of the loanApplicationTypeDTO to save.
     * @param loanApplicationTypeDTO the loanApplicationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanApplicationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the loanApplicationTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the loanApplicationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/loan-application-types/{id}")
    public ResponseEntity<LoanApplicationTypeDTO> updateLoanApplicationType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LoanApplicationTypeDTO loanApplicationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LoanApplicationType : {}, {}", id, loanApplicationTypeDTO);
        if (loanApplicationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanApplicationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanApplicationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LoanApplicationTypeDTO result = loanApplicationTypeService.save(loanApplicationTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanApplicationTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /loan-application-types/:id} : Partial updates given fields of an existing loanApplicationType, field will ignore if it is null
     *
     * @param id the id of the loanApplicationTypeDTO to save.
     * @param loanApplicationTypeDTO the loanApplicationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated loanApplicationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the loanApplicationTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the loanApplicationTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the loanApplicationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/loan-application-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LoanApplicationTypeDTO> partialUpdateLoanApplicationType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LoanApplicationTypeDTO loanApplicationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LoanApplicationType partially : {}, {}", id, loanApplicationTypeDTO);
        if (loanApplicationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, loanApplicationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!loanApplicationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LoanApplicationTypeDTO> result = loanApplicationTypeService.partialUpdate(loanApplicationTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, loanApplicationTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /loan-application-types} : get all the loanApplicationTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of loanApplicationTypes in body.
     */
    @GetMapping("/loan-application-types")
    public ResponseEntity<List<LoanApplicationTypeDTO>> getAllLoanApplicationTypes(
        LoanApplicationTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LoanApplicationTypes by criteria: {}", criteria);
        Page<LoanApplicationTypeDTO> page = loanApplicationTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /loan-application-types/count} : count all the loanApplicationTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/loan-application-types/count")
    public ResponseEntity<Long> countLoanApplicationTypes(LoanApplicationTypeCriteria criteria) {
        log.debug("REST request to count LoanApplicationTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(loanApplicationTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /loan-application-types/:id} : get the "id" loanApplicationType.
     *
     * @param id the id of the loanApplicationTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the loanApplicationTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/loan-application-types/{id}")
    public ResponseEntity<LoanApplicationTypeDTO> getLoanApplicationType(@PathVariable Long id) {
        log.debug("REST request to get LoanApplicationType : {}", id);
        Optional<LoanApplicationTypeDTO> loanApplicationTypeDTO = loanApplicationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(loanApplicationTypeDTO);
    }

    /**
     * {@code DELETE  /loan-application-types/:id} : delete the "id" loanApplicationType.
     *
     * @param id the id of the loanApplicationTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/loan-application-types/{id}")
    public ResponseEntity<Void> deleteLoanApplicationType(@PathVariable Long id) {
        log.debug("REST request to delete LoanApplicationType : {}", id);
        loanApplicationTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/loan-application-types?query=:query} : search for the loanApplicationType corresponding
     * to the query.
     *
     * @param query the query of the loanApplicationType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/loan-application-types")
    public ResponseEntity<List<LoanApplicationTypeDTO>> searchLoanApplicationTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LoanApplicationTypes for query {}", query);
        Page<LoanApplicationTypeDTO> page = loanApplicationTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
