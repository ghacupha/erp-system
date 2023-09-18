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

import io.github.erp.repository.FiscalMonthRepository;
import io.github.erp.service.FiscalMonthQueryService;
import io.github.erp.service.FiscalMonthService;
import io.github.erp.service.criteria.FiscalMonthCriteria;
import io.github.erp.service.dto.FiscalMonthDTO;
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
 * REST controller for managing {@link io.github.erp.domain.FiscalMonth}.
 */
@RestController
@RequestMapping("/api")
public class FiscalMonthResource {

    private final Logger log = LoggerFactory.getLogger(FiscalMonthResource.class);

    private static final String ENTITY_NAME = "fiscalMonth";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FiscalMonthService fiscalMonthService;

    private final FiscalMonthRepository fiscalMonthRepository;

    private final FiscalMonthQueryService fiscalMonthQueryService;

    public FiscalMonthResource(
        FiscalMonthService fiscalMonthService,
        FiscalMonthRepository fiscalMonthRepository,
        FiscalMonthQueryService fiscalMonthQueryService
    ) {
        this.fiscalMonthService = fiscalMonthService;
        this.fiscalMonthRepository = fiscalMonthRepository;
        this.fiscalMonthQueryService = fiscalMonthQueryService;
    }

    /**
     * {@code POST  /fiscal-months} : Create a new fiscalMonth.
     *
     * @param fiscalMonthDTO the fiscalMonthDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new fiscalMonthDTO, or with status {@code 400 (Bad Request)} if the fiscalMonth has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/fiscal-months")
    public ResponseEntity<FiscalMonthDTO> createFiscalMonth(@Valid @RequestBody FiscalMonthDTO fiscalMonthDTO) throws URISyntaxException {
        log.debug("REST request to save FiscalMonth : {}", fiscalMonthDTO);
        if (fiscalMonthDTO.getId() != null) {
            throw new BadRequestAlertException("A new fiscalMonth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FiscalMonthDTO result = fiscalMonthService.save(fiscalMonthDTO);
        return ResponseEntity
            .created(new URI("/api/fiscal-months/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /fiscal-months/:id} : Updates an existing fiscalMonth.
     *
     * @param id the id of the fiscalMonthDTO to save.
     * @param fiscalMonthDTO the fiscalMonthDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiscalMonthDTO,
     * or with status {@code 400 (Bad Request)} if the fiscalMonthDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the fiscalMonthDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/fiscal-months/{id}")
    public ResponseEntity<FiscalMonthDTO> updateFiscalMonth(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody FiscalMonthDTO fiscalMonthDTO
    ) throws URISyntaxException {
        log.debug("REST request to update FiscalMonth : {}, {}", id, fiscalMonthDTO);
        if (fiscalMonthDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fiscalMonthDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fiscalMonthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FiscalMonthDTO result = fiscalMonthService.save(fiscalMonthDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fiscalMonthDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /fiscal-months/:id} : Partial updates given fields of an existing fiscalMonth, field will ignore if it is null
     *
     * @param id the id of the fiscalMonthDTO to save.
     * @param fiscalMonthDTO the fiscalMonthDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated fiscalMonthDTO,
     * or with status {@code 400 (Bad Request)} if the fiscalMonthDTO is not valid,
     * or with status {@code 404 (Not Found)} if the fiscalMonthDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the fiscalMonthDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/fiscal-months/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FiscalMonthDTO> partialUpdateFiscalMonth(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody FiscalMonthDTO fiscalMonthDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update FiscalMonth partially : {}, {}", id, fiscalMonthDTO);
        if (fiscalMonthDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, fiscalMonthDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!fiscalMonthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FiscalMonthDTO> result = fiscalMonthService.partialUpdate(fiscalMonthDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, fiscalMonthDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /fiscal-months} : get all the fiscalMonths.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of fiscalMonths in body.
     */
    @GetMapping("/fiscal-months")
    public ResponseEntity<List<FiscalMonthDTO>> getAllFiscalMonths(FiscalMonthCriteria criteria, Pageable pageable) {
        log.debug("REST request to get FiscalMonths by criteria: {}", criteria);
        Page<FiscalMonthDTO> page = fiscalMonthQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /fiscal-months/count} : count all the fiscalMonths.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/fiscal-months/count")
    public ResponseEntity<Long> countFiscalMonths(FiscalMonthCriteria criteria) {
        log.debug("REST request to count FiscalMonths by criteria: {}", criteria);
        return ResponseEntity.ok().body(fiscalMonthQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /fiscal-months/:id} : get the "id" fiscalMonth.
     *
     * @param id the id of the fiscalMonthDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the fiscalMonthDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/fiscal-months/{id}")
    public ResponseEntity<FiscalMonthDTO> getFiscalMonth(@PathVariable Long id) {
        log.debug("REST request to get FiscalMonth : {}", id);
        Optional<FiscalMonthDTO> fiscalMonthDTO = fiscalMonthService.findOne(id);
        return ResponseUtil.wrapOrNotFound(fiscalMonthDTO);
    }

    /**
     * {@code DELETE  /fiscal-months/:id} : delete the "id" fiscalMonth.
     *
     * @param id the id of the fiscalMonthDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/fiscal-months/{id}")
    public ResponseEntity<Void> deleteFiscalMonth(@PathVariable Long id) {
        log.debug("REST request to delete FiscalMonth : {}", id);
        fiscalMonthService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/fiscal-months?query=:query} : search for the fiscalMonth corresponding
     * to the query.
     *
     * @param query the query of the fiscalMonth search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/fiscal-months")
    public ResponseEntity<List<FiscalMonthDTO>> searchFiscalMonths(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of FiscalMonths for query {}", query);
        Page<FiscalMonthDTO> page = fiscalMonthService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
