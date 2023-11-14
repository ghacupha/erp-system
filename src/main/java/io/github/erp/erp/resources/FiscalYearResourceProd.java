package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.repository.FiscalYearRepository;
import io.github.erp.service.FiscalYearQueryService;
import io.github.erp.service.FiscalYearService;
import io.github.erp.service.criteria.FiscalYearCriteria;
import io.github.erp.service.dto.FiscalYearDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FiscalYear}.
 */
@RestController("fiscalYearResourceProd")
@RequestMapping("/api/app")
public class FiscalYearResourceProd {

    private final Logger log = LoggerFactory.getLogger(FiscalYearResourceProd.class);

    private static final String ENTITY_NAME = "fiscalYearProd";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FiscalYearService fiscalYearService;

    private final FiscalYearRepository fiscalYearRepository;

    private final FiscalYearQueryService fiscalYearQueryService;

    public FiscalYearResourceProd(
        FiscalYearService fiscalYearService,
        FiscalYearRepository fiscalYearRepository,
        FiscalYearQueryService fiscalYearQueryService
    ) {
        this.fiscalYearService = fiscalYearService;
        this.fiscalYearRepository = fiscalYearRepository;
        this.fiscalYearQueryService = fiscalYearQueryService;
    }

    /**
     * {@code POST  /fiscal-years} : Create a new fiscalYear.
     *
     * @param fiscalYearDTO the fiscalYearDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fiscalYearDTO, or with status {@code 400 (Bad Request)} if the fiscalYear has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fiscal-years")
    public ResponseEntity<FiscalYearDTO> createFiscalYear(@Valid @RequestBody FiscalYearDTO fiscalYearDTO) throws URISyntaxException {
        log.debug("REST request to save FiscalYear : {}", fiscalYearDTO);
        if (fiscalYearDTO.getId() != null) {
            throw new BadRequestAlertException("A new fiscalYear cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FiscalYearDTO result = fiscalYearService.save(fiscalYearDTO);
        return ResponseEntity
            .created(new URI("/api/fiscal-years/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fiscal-years/:id} : Updates an existing fiscalYear.
     *
     * @param id the id of the fiscalYearDTO to save.
     * @param fiscalYearDTO the fiscalYearDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiscalYearDTO,
     * or with status {@code 400 (Bad Request)} if the fiscalYearDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fiscalYearDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fiscal-years/{id}")
    public ResponseEntity<FiscalYearDTO> updateFiscalYear(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FiscalYearDTO fiscalYearDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FiscalYear : {}, {}", id, fiscalYearDTO);
        if (fiscalYearDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fiscalYearDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fiscalYearRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FiscalYearDTO result = fiscalYearService.save(fiscalYearDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fiscalYearDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fiscal-years/:id} : Partial updates given fields of an existing fiscalYear, field will ignore if it is null
     *
     * @param id the id of the fiscalYearDTO to save.
     * @param fiscalYearDTO the fiscalYearDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiscalYearDTO,
     * or with status {@code 400 (Bad Request)} if the fiscalYearDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fiscalYearDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fiscalYearDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fiscal-years/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FiscalYearDTO> partialUpdateFiscalYear(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FiscalYearDTO fiscalYearDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FiscalYear partially : {}, {}", id, fiscalYearDTO);
        if (fiscalYearDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fiscalYearDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fiscalYearRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FiscalYearDTO> result = fiscalYearService.partialUpdate(fiscalYearDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fiscalYearDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fiscal-years} : get all the fiscalYears.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fiscalYears in body.
     */
    @GetMapping("/fiscal-years")
    public ResponseEntity<List<FiscalYearDTO>> getAllFiscalYears(FiscalYearCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FiscalYears by criteria: {}", criteria);
        Page<FiscalYearDTO> page = fiscalYearQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fiscal-years/count} : count all the fiscalYears.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fiscal-years/count")
    public ResponseEntity<Long> countFiscalYears(FiscalYearCriteria criteria) {
        log.debug("REST request to count FiscalYears by criteria: {}", criteria);
        return ResponseEntity.ok().body(fiscalYearQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fiscal-years/:id} : get the "id" fiscalYear.
     *
     * @param id the id of the fiscalYearDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fiscalYearDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fiscal-years/{id}")
    public ResponseEntity<FiscalYearDTO> getFiscalYear(@PathVariable Long id) {
        log.debug("REST request to get FiscalYear : {}", id);
        Optional<FiscalYearDTO> fiscalYearDTO = fiscalYearService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fiscalYearDTO);
    }

    /**
     * {@code DELETE  /fiscal-years/:id} : delete the "id" fiscalYear.
     *
     * @param id the id of the fiscalYearDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fiscal-years/{id}")
    public ResponseEntity<Void> deleteFiscalYear(@PathVariable Long id) {
        log.debug("REST request to delete FiscalYear : {}", id);
        fiscalYearService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fiscal-years?query=:query} : search for the fiscalYear corresponding
     * to the query.
     *
     * @param query the query of the fiscalYear search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fiscal-years")
    public ResponseEntity<List<FiscalYearDTO>> searchFiscalYears(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FiscalYears for query {}", query);
        Page<FiscalYearDTO> page = fiscalYearService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
