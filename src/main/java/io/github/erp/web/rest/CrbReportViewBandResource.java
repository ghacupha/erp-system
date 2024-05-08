package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.repository.CrbReportViewBandRepository;
import io.github.erp.service.CrbReportViewBandQueryService;
import io.github.erp.service.CrbReportViewBandService;
import io.github.erp.service.criteria.CrbReportViewBandCriteria;
import io.github.erp.service.dto.CrbReportViewBandDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbReportViewBand}.
 */
@RestController
@RequestMapping("/api")
public class CrbReportViewBandResource {

    private final Logger log = LoggerFactory.getLogger(CrbReportViewBandResource.class);

    private static final String ENTITY_NAME = "crbReportViewBand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbReportViewBandService crbReportViewBandService;

    private final CrbReportViewBandRepository crbReportViewBandRepository;

    private final CrbReportViewBandQueryService crbReportViewBandQueryService;

    public CrbReportViewBandResource(
        CrbReportViewBandService crbReportViewBandService,
        CrbReportViewBandRepository crbReportViewBandRepository,
        CrbReportViewBandQueryService crbReportViewBandQueryService
    ) {
        this.crbReportViewBandService = crbReportViewBandService;
        this.crbReportViewBandRepository = crbReportViewBandRepository;
        this.crbReportViewBandQueryService = crbReportViewBandQueryService;
    }

    /**
     * {@code POST  /crb-report-view-bands} : Create a new crbReportViewBand.
     *
     * @param crbReportViewBandDTO the crbReportViewBandDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbReportViewBandDTO, or with status {@code 400 (Bad Request)} if the crbReportViewBand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-report-view-bands")
    public ResponseEntity<CrbReportViewBandDTO> createCrbReportViewBand(@Valid @RequestBody CrbReportViewBandDTO crbReportViewBandDTO)
        throws URISyntaxException {
        log.debug("REST request to save CrbReportViewBand : {}", crbReportViewBandDTO);
        if (crbReportViewBandDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbReportViewBand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbReportViewBandDTO result = crbReportViewBandService.save(crbReportViewBandDTO);
        return ResponseEntity
            .created(new URI("/api/crb-report-view-bands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-report-view-bands/:id} : Updates an existing crbReportViewBand.
     *
     * @param id the id of the crbReportViewBandDTO to save.
     * @param crbReportViewBandDTO the crbReportViewBandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbReportViewBandDTO,
     * or with status {@code 400 (Bad Request)} if the crbReportViewBandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbReportViewBandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-report-view-bands/{id}")
    public ResponseEntity<CrbReportViewBandDTO> updateCrbReportViewBand(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbReportViewBandDTO crbReportViewBandDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbReportViewBand : {}, {}", id, crbReportViewBandDTO);
        if (crbReportViewBandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbReportViewBandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbReportViewBandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbReportViewBandDTO result = crbReportViewBandService.save(crbReportViewBandDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbReportViewBandDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-report-view-bands/:id} : Partial updates given fields of an existing crbReportViewBand, field will ignore if it is null
     *
     * @param id the id of the crbReportViewBandDTO to save.
     * @param crbReportViewBandDTO the crbReportViewBandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbReportViewBandDTO,
     * or with status {@code 400 (Bad Request)} if the crbReportViewBandDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbReportViewBandDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbReportViewBandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-report-view-bands/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbReportViewBandDTO> partialUpdateCrbReportViewBand(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbReportViewBandDTO crbReportViewBandDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbReportViewBand partially : {}, {}", id, crbReportViewBandDTO);
        if (crbReportViewBandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbReportViewBandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbReportViewBandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbReportViewBandDTO> result = crbReportViewBandService.partialUpdate(crbReportViewBandDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbReportViewBandDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-report-view-bands} : get all the crbReportViewBands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbReportViewBands in body.
     */
    @GetMapping("/crb-report-view-bands")
    public ResponseEntity<List<CrbReportViewBandDTO>> getAllCrbReportViewBands(CrbReportViewBandCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CrbReportViewBands by criteria: {}", criteria);
        Page<CrbReportViewBandDTO> page = crbReportViewBandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-report-view-bands/count} : count all the crbReportViewBands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-report-view-bands/count")
    public ResponseEntity<Long> countCrbReportViewBands(CrbReportViewBandCriteria criteria) {
        log.debug("REST request to count CrbReportViewBands by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbReportViewBandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-report-view-bands/:id} : get the "id" crbReportViewBand.
     *
     * @param id the id of the crbReportViewBandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbReportViewBandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-report-view-bands/{id}")
    public ResponseEntity<CrbReportViewBandDTO> getCrbReportViewBand(@PathVariable Long id) {
        log.debug("REST request to get CrbReportViewBand : {}", id);
        Optional<CrbReportViewBandDTO> crbReportViewBandDTO = crbReportViewBandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbReportViewBandDTO);
    }

    /**
     * {@code DELETE  /crb-report-view-bands/:id} : delete the "id" crbReportViewBand.
     *
     * @param id the id of the crbReportViewBandDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-report-view-bands/{id}")
    public ResponseEntity<Void> deleteCrbReportViewBand(@PathVariable Long id) {
        log.debug("REST request to delete CrbReportViewBand : {}", id);
        crbReportViewBandService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-report-view-bands?query=:query} : search for the crbReportViewBand corresponding
     * to the query.
     *
     * @param query the query of the crbReportViewBand search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-report-view-bands")
    public ResponseEntity<List<CrbReportViewBandDTO>> searchCrbReportViewBands(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbReportViewBands for query {}", query);
        Page<CrbReportViewBandDTO> page = crbReportViewBandService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
