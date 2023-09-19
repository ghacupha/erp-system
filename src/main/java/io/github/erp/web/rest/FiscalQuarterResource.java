package io.github.erp.web.rest;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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

import io.github.erp.repository.FiscalQuarterRepository;
import io.github.erp.service.FiscalQuarterQueryService;
import io.github.erp.service.FiscalQuarterService;
import io.github.erp.service.criteria.FiscalQuarterCriteria;
import io.github.erp.service.dto.FiscalQuarterDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FiscalQuarter}.
 */
@RestController
@RequestMapping("/api")
public class FiscalQuarterResource {

    private final Logger log = LoggerFactory.getLogger(FiscalQuarterResource.class);

    private static final String ENTITY_NAME = "fiscalQuarter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FiscalQuarterService fiscalQuarterService;

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final FiscalQuarterQueryService fiscalQuarterQueryService;

    public FiscalQuarterResource(
        FiscalQuarterService fiscalQuarterService,
        FiscalQuarterRepository fiscalQuarterRepository,
        FiscalQuarterQueryService fiscalQuarterQueryService
    ) {
        this.fiscalQuarterService = fiscalQuarterService;
        this.fiscalQuarterRepository = fiscalQuarterRepository;
        this.fiscalQuarterQueryService = fiscalQuarterQueryService;
    }

    /**
     * {@code POST  /fiscal-quarters} : Create a new fiscalQuarter.
     *
     * @param fiscalQuarterDTO the fiscalQuarterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fiscalQuarterDTO, or with status {@code 400 (Bad Request)} if the fiscalQuarter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fiscal-quarters")
    public ResponseEntity<FiscalQuarterDTO> createFiscalQuarter(@Valid @RequestBody FiscalQuarterDTO fiscalQuarterDTO)
        throws URISyntaxException {
        log.debug("REST request to save FiscalQuarter : {}", fiscalQuarterDTO);
        if (fiscalQuarterDTO.getId() != null) {
            throw new BadRequestAlertException("A new fiscalQuarter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FiscalQuarterDTO result = fiscalQuarterService.save(fiscalQuarterDTO);
        return ResponseEntity
            .created(new URI("/api/fiscal-quarters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fiscal-quarters/:id} : Updates an existing fiscalQuarter.
     *
     * @param id the id of the fiscalQuarterDTO to save.
     * @param fiscalQuarterDTO the fiscalQuarterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiscalQuarterDTO,
     * or with status {@code 400 (Bad Request)} if the fiscalQuarterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fiscalQuarterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fiscal-quarters/{id}")
    public ResponseEntity<FiscalQuarterDTO> updateFiscalQuarter(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FiscalQuarterDTO fiscalQuarterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FiscalQuarter : {}, {}", id, fiscalQuarterDTO);
        if (fiscalQuarterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fiscalQuarterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fiscalQuarterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FiscalQuarterDTO result = fiscalQuarterService.save(fiscalQuarterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fiscalQuarterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fiscal-quarters/:id} : Partial updates given fields of an existing fiscalQuarter, field will ignore if it is null
     *
     * @param id the id of the fiscalQuarterDTO to save.
     * @param fiscalQuarterDTO the fiscalQuarterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiscalQuarterDTO,
     * or with status {@code 400 (Bad Request)} if the fiscalQuarterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fiscalQuarterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fiscalQuarterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fiscal-quarters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FiscalQuarterDTO> partialUpdateFiscalQuarter(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FiscalQuarterDTO fiscalQuarterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FiscalQuarter partially : {}, {}", id, fiscalQuarterDTO);
        if (fiscalQuarterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fiscalQuarterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fiscalQuarterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FiscalQuarterDTO> result = fiscalQuarterService.partialUpdate(fiscalQuarterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fiscalQuarterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fiscal-quarters} : get all the fiscalQuarters.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fiscalQuarters in body.
     */
    @GetMapping("/fiscal-quarters")
    public ResponseEntity<List<FiscalQuarterDTO>> getAllFiscalQuarters(FiscalQuarterCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FiscalQuarters by criteria: {}", criteria);
        Page<FiscalQuarterDTO> page = fiscalQuarterQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fiscal-quarters/count} : count all the fiscalQuarters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fiscal-quarters/count")
    public ResponseEntity<Long> countFiscalQuarters(FiscalQuarterCriteria criteria) {
        log.debug("REST request to count FiscalQuarters by criteria: {}", criteria);
        return ResponseEntity.ok().body(fiscalQuarterQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fiscal-quarters/:id} : get the "id" fiscalQuarter.
     *
     * @param id the id of the fiscalQuarterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fiscalQuarterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fiscal-quarters/{id}")
    public ResponseEntity<FiscalQuarterDTO> getFiscalQuarter(@PathVariable Long id) {
        log.debug("REST request to get FiscalQuarter : {}", id);
        Optional<FiscalQuarterDTO> fiscalQuarterDTO = fiscalQuarterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fiscalQuarterDTO);
    }

    /**
     * {@code DELETE  /fiscal-quarters/:id} : delete the "id" fiscalQuarter.
     *
     * @param id the id of the fiscalQuarterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fiscal-quarters/{id}")
    public ResponseEntity<Void> deleteFiscalQuarter(@PathVariable Long id) {
        log.debug("REST request to delete FiscalQuarter : {}", id);
        fiscalQuarterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fiscal-quarters?query=:query} : search for the fiscalQuarter corresponding
     * to the query.
     *
     * @param query the query of the fiscalQuarter search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fiscal-quarters")
    public ResponseEntity<List<FiscalQuarterDTO>> searchFiscalQuarters(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FiscalQuarters for query {}", query);
        Page<FiscalQuarterDTO> page = fiscalQuarterService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
