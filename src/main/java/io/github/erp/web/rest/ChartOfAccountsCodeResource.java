package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.ChartOfAccountsCodeRepository;
import io.github.erp.service.ChartOfAccountsCodeQueryService;
import io.github.erp.service.ChartOfAccountsCodeService;
import io.github.erp.service.criteria.ChartOfAccountsCodeCriteria;
import io.github.erp.service.dto.ChartOfAccountsCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ChartOfAccountsCode}.
 */
@RestController
@RequestMapping("/api")
public class ChartOfAccountsCodeResource {

    private final Logger log = LoggerFactory.getLogger(ChartOfAccountsCodeResource.class);

    private static final String ENTITY_NAME = "chartOfAccountsCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChartOfAccountsCodeService chartOfAccountsCodeService;

    private final ChartOfAccountsCodeRepository chartOfAccountsCodeRepository;

    private final ChartOfAccountsCodeQueryService chartOfAccountsCodeQueryService;

    public ChartOfAccountsCodeResource(
        ChartOfAccountsCodeService chartOfAccountsCodeService,
        ChartOfAccountsCodeRepository chartOfAccountsCodeRepository,
        ChartOfAccountsCodeQueryService chartOfAccountsCodeQueryService
    ) {
        this.chartOfAccountsCodeService = chartOfAccountsCodeService;
        this.chartOfAccountsCodeRepository = chartOfAccountsCodeRepository;
        this.chartOfAccountsCodeQueryService = chartOfAccountsCodeQueryService;
    }

    /**
     * {@code POST  /chart-of-accounts-codes} : Create a new chartOfAccountsCode.
     *
     * @param chartOfAccountsCodeDTO the chartOfAccountsCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new chartOfAccountsCodeDTO, or with status {@code 400 (Bad Request)} if the chartOfAccountsCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/chart-of-accounts-codes")
    public ResponseEntity<ChartOfAccountsCodeDTO> createChartOfAccountsCode(
        @Valid @RequestBody ChartOfAccountsCodeDTO chartOfAccountsCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ChartOfAccountsCode : {}", chartOfAccountsCodeDTO);
        if (chartOfAccountsCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new chartOfAccountsCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChartOfAccountsCodeDTO result = chartOfAccountsCodeService.save(chartOfAccountsCodeDTO);
        return ResponseEntity
            .created(new URI("/api/chart-of-accounts-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /chart-of-accounts-codes/:id} : Updates an existing chartOfAccountsCode.
     *
     * @param id the id of the chartOfAccountsCodeDTO to save.
     * @param chartOfAccountsCodeDTO the chartOfAccountsCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chartOfAccountsCodeDTO,
     * or with status {@code 400 (Bad Request)} if the chartOfAccountsCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the chartOfAccountsCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/chart-of-accounts-codes/{id}")
    public ResponseEntity<ChartOfAccountsCodeDTO> updateChartOfAccountsCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ChartOfAccountsCodeDTO chartOfAccountsCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ChartOfAccountsCode : {}, {}", id, chartOfAccountsCodeDTO);
        if (chartOfAccountsCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chartOfAccountsCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chartOfAccountsCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ChartOfAccountsCodeDTO result = chartOfAccountsCodeService.save(chartOfAccountsCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chartOfAccountsCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /chart-of-accounts-codes/:id} : Partial updates given fields of an existing chartOfAccountsCode, field will ignore if it is null
     *
     * @param id the id of the chartOfAccountsCodeDTO to save.
     * @param chartOfAccountsCodeDTO the chartOfAccountsCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated chartOfAccountsCodeDTO,
     * or with status {@code 400 (Bad Request)} if the chartOfAccountsCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the chartOfAccountsCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the chartOfAccountsCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/chart-of-accounts-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ChartOfAccountsCodeDTO> partialUpdateChartOfAccountsCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ChartOfAccountsCodeDTO chartOfAccountsCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ChartOfAccountsCode partially : {}, {}", id, chartOfAccountsCodeDTO);
        if (chartOfAccountsCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, chartOfAccountsCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!chartOfAccountsCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ChartOfAccountsCodeDTO> result = chartOfAccountsCodeService.partialUpdate(chartOfAccountsCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, chartOfAccountsCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /chart-of-accounts-codes} : get all the chartOfAccountsCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of chartOfAccountsCodes in body.
     */
    @GetMapping("/chart-of-accounts-codes")
    public ResponseEntity<List<ChartOfAccountsCodeDTO>> getAllChartOfAccountsCodes(
        ChartOfAccountsCodeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ChartOfAccountsCodes by criteria: {}", criteria);
        Page<ChartOfAccountsCodeDTO> page = chartOfAccountsCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /chart-of-accounts-codes/count} : count all the chartOfAccountsCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/chart-of-accounts-codes/count")
    public ResponseEntity<Long> countChartOfAccountsCodes(ChartOfAccountsCodeCriteria criteria) {
        log.debug("REST request to count ChartOfAccountsCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(chartOfAccountsCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /chart-of-accounts-codes/:id} : get the "id" chartOfAccountsCode.
     *
     * @param id the id of the chartOfAccountsCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the chartOfAccountsCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/chart-of-accounts-codes/{id}")
    public ResponseEntity<ChartOfAccountsCodeDTO> getChartOfAccountsCode(@PathVariable Long id) {
        log.debug("REST request to get ChartOfAccountsCode : {}", id);
        Optional<ChartOfAccountsCodeDTO> chartOfAccountsCodeDTO = chartOfAccountsCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(chartOfAccountsCodeDTO);
    }

    /**
     * {@code DELETE  /chart-of-accounts-codes/:id} : delete the "id" chartOfAccountsCode.
     *
     * @param id the id of the chartOfAccountsCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/chart-of-accounts-codes/{id}")
    public ResponseEntity<Void> deleteChartOfAccountsCode(@PathVariable Long id) {
        log.debug("REST request to delete ChartOfAccountsCode : {}", id);
        chartOfAccountsCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/chart-of-accounts-codes?query=:query} : search for the chartOfAccountsCode corresponding
     * to the query.
     *
     * @param query the query of the chartOfAccountsCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/chart-of-accounts-codes")
    public ResponseEntity<List<ChartOfAccountsCodeDTO>> searchChartOfAccountsCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ChartOfAccountsCodes for query {}", query);
        Page<ChartOfAccountsCodeDTO> page = chartOfAccountsCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
