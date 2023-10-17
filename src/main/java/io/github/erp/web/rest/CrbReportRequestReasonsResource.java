package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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

import io.github.erp.repository.CrbReportRequestReasonsRepository;
import io.github.erp.service.CrbReportRequestReasonsQueryService;
import io.github.erp.service.CrbReportRequestReasonsService;
import io.github.erp.service.criteria.CrbReportRequestReasonsCriteria;
import io.github.erp.service.dto.CrbReportRequestReasonsDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbReportRequestReasons}.
 */
@RestController
@RequestMapping("/api")
public class CrbReportRequestReasonsResource {

    private final Logger log = LoggerFactory.getLogger(CrbReportRequestReasonsResource.class);

    private static final String ENTITY_NAME = "crbReportRequestReasons";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbReportRequestReasonsService crbReportRequestReasonsService;

    private final CrbReportRequestReasonsRepository crbReportRequestReasonsRepository;

    private final CrbReportRequestReasonsQueryService crbReportRequestReasonsQueryService;

    public CrbReportRequestReasonsResource(
        CrbReportRequestReasonsService crbReportRequestReasonsService,
        CrbReportRequestReasonsRepository crbReportRequestReasonsRepository,
        CrbReportRequestReasonsQueryService crbReportRequestReasonsQueryService
    ) {
        this.crbReportRequestReasonsService = crbReportRequestReasonsService;
        this.crbReportRequestReasonsRepository = crbReportRequestReasonsRepository;
        this.crbReportRequestReasonsQueryService = crbReportRequestReasonsQueryService;
    }

    /**
     * {@code POST  /crb-report-request-reasons} : Create a new crbReportRequestReasons.
     *
     * @param crbReportRequestReasonsDTO the crbReportRequestReasonsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbReportRequestReasonsDTO, or with status {@code 400 (Bad Request)} if the crbReportRequestReasons has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-report-request-reasons")
    public ResponseEntity<CrbReportRequestReasonsDTO> createCrbReportRequestReasons(
        @Valid @RequestBody CrbReportRequestReasonsDTO crbReportRequestReasonsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbReportRequestReasons : {}", crbReportRequestReasonsDTO);
        if (crbReportRequestReasonsDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbReportRequestReasons cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbReportRequestReasonsDTO result = crbReportRequestReasonsService.save(crbReportRequestReasonsDTO);
        return ResponseEntity
            .created(new URI("/api/crb-report-request-reasons/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-report-request-reasons/:id} : Updates an existing crbReportRequestReasons.
     *
     * @param id the id of the crbReportRequestReasonsDTO to save.
     * @param crbReportRequestReasonsDTO the crbReportRequestReasonsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbReportRequestReasonsDTO,
     * or with status {@code 400 (Bad Request)} if the crbReportRequestReasonsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbReportRequestReasonsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-report-request-reasons/{id}")
    public ResponseEntity<CrbReportRequestReasonsDTO> updateCrbReportRequestReasons(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbReportRequestReasonsDTO crbReportRequestReasonsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbReportRequestReasons : {}, {}", id, crbReportRequestReasonsDTO);
        if (crbReportRequestReasonsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbReportRequestReasonsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbReportRequestReasonsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbReportRequestReasonsDTO result = crbReportRequestReasonsService.save(crbReportRequestReasonsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbReportRequestReasonsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-report-request-reasons/:id} : Partial updates given fields of an existing crbReportRequestReasons, field will ignore if it is null
     *
     * @param id the id of the crbReportRequestReasonsDTO to save.
     * @param crbReportRequestReasonsDTO the crbReportRequestReasonsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbReportRequestReasonsDTO,
     * or with status {@code 400 (Bad Request)} if the crbReportRequestReasonsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbReportRequestReasonsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbReportRequestReasonsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-report-request-reasons/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbReportRequestReasonsDTO> partialUpdateCrbReportRequestReasons(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbReportRequestReasonsDTO crbReportRequestReasonsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbReportRequestReasons partially : {}, {}", id, crbReportRequestReasonsDTO);
        if (crbReportRequestReasonsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbReportRequestReasonsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbReportRequestReasonsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbReportRequestReasonsDTO> result = crbReportRequestReasonsService.partialUpdate(crbReportRequestReasonsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbReportRequestReasonsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-report-request-reasons} : get all the crbReportRequestReasons.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbReportRequestReasons in body.
     */
    @GetMapping("/crb-report-request-reasons")
    public ResponseEntity<List<CrbReportRequestReasonsDTO>> getAllCrbReportRequestReasons(
        CrbReportRequestReasonsCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbReportRequestReasons by criteria: {}", criteria);
        Page<CrbReportRequestReasonsDTO> page = crbReportRequestReasonsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-report-request-reasons/count} : count all the crbReportRequestReasons.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-report-request-reasons/count")
    public ResponseEntity<Long> countCrbReportRequestReasons(CrbReportRequestReasonsCriteria criteria) {
        log.debug("REST request to count CrbReportRequestReasons by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbReportRequestReasonsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-report-request-reasons/:id} : get the "id" crbReportRequestReasons.
     *
     * @param id the id of the crbReportRequestReasonsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbReportRequestReasonsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-report-request-reasons/{id}")
    public ResponseEntity<CrbReportRequestReasonsDTO> getCrbReportRequestReasons(@PathVariable Long id) {
        log.debug("REST request to get CrbReportRequestReasons : {}", id);
        Optional<CrbReportRequestReasonsDTO> crbReportRequestReasonsDTO = crbReportRequestReasonsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbReportRequestReasonsDTO);
    }

    /**
     * {@code DELETE  /crb-report-request-reasons/:id} : delete the "id" crbReportRequestReasons.
     *
     * @param id the id of the crbReportRequestReasonsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-report-request-reasons/{id}")
    public ResponseEntity<Void> deleteCrbReportRequestReasons(@PathVariable Long id) {
        log.debug("REST request to delete CrbReportRequestReasons : {}", id);
        crbReportRequestReasonsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-report-request-reasons?query=:query} : search for the crbReportRequestReasons corresponding
     * to the query.
     *
     * @param query the query of the crbReportRequestReasons search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-report-request-reasons")
    public ResponseEntity<List<CrbReportRequestReasonsDTO>> searchCrbReportRequestReasons(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbReportRequestReasons for query {}", query);
        Page<CrbReportRequestReasonsDTO> page = crbReportRequestReasonsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
