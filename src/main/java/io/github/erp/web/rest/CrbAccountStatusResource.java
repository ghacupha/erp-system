package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.repository.CrbAccountStatusRepository;
import io.github.erp.service.CrbAccountStatusQueryService;
import io.github.erp.service.CrbAccountStatusService;
import io.github.erp.service.criteria.CrbAccountStatusCriteria;
import io.github.erp.service.dto.CrbAccountStatusDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbAccountStatus}.
 */
@RestController
@RequestMapping("/api")
public class CrbAccountStatusResource {

    private final Logger log = LoggerFactory.getLogger(CrbAccountStatusResource.class);

    private static final String ENTITY_NAME = "crbAccountStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbAccountStatusService crbAccountStatusService;

    private final CrbAccountStatusRepository crbAccountStatusRepository;

    private final CrbAccountStatusQueryService crbAccountStatusQueryService;

    public CrbAccountStatusResource(
        CrbAccountStatusService crbAccountStatusService,
        CrbAccountStatusRepository crbAccountStatusRepository,
        CrbAccountStatusQueryService crbAccountStatusQueryService
    ) {
        this.crbAccountStatusService = crbAccountStatusService;
        this.crbAccountStatusRepository = crbAccountStatusRepository;
        this.crbAccountStatusQueryService = crbAccountStatusQueryService;
    }

    /**
     * {@code POST  /crb-account-statuses} : Create a new crbAccountStatus.
     *
     * @param crbAccountStatusDTO the crbAccountStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbAccountStatusDTO, or with status {@code 400 (Bad Request)} if the crbAccountStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-account-statuses")
    public ResponseEntity<CrbAccountStatusDTO> createCrbAccountStatus(@Valid @RequestBody CrbAccountStatusDTO crbAccountStatusDTO)
        throws URISyntaxException {
        log.debug("REST request to save CrbAccountStatus : {}", crbAccountStatusDTO);
        if (crbAccountStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbAccountStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbAccountStatusDTO result = crbAccountStatusService.save(crbAccountStatusDTO);
        return ResponseEntity
            .created(new URI("/api/crb-account-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-account-statuses/:id} : Updates an existing crbAccountStatus.
     *
     * @param id the id of the crbAccountStatusDTO to save.
     * @param crbAccountStatusDTO the crbAccountStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbAccountStatusDTO,
     * or with status {@code 400 (Bad Request)} if the crbAccountStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbAccountStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-account-statuses/{id}")
    public ResponseEntity<CrbAccountStatusDTO> updateCrbAccountStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbAccountStatusDTO crbAccountStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbAccountStatus : {}, {}", id, crbAccountStatusDTO);
        if (crbAccountStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbAccountStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbAccountStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbAccountStatusDTO result = crbAccountStatusService.save(crbAccountStatusDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbAccountStatusDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-account-statuses/:id} : Partial updates given fields of an existing crbAccountStatus, field will ignore if it is null
     *
     * @param id the id of the crbAccountStatusDTO to save.
     * @param crbAccountStatusDTO the crbAccountStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbAccountStatusDTO,
     * or with status {@code 400 (Bad Request)} if the crbAccountStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbAccountStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbAccountStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-account-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbAccountStatusDTO> partialUpdateCrbAccountStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbAccountStatusDTO crbAccountStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbAccountStatus partially : {}, {}", id, crbAccountStatusDTO);
        if (crbAccountStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbAccountStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbAccountStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbAccountStatusDTO> result = crbAccountStatusService.partialUpdate(crbAccountStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbAccountStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-account-statuses} : get all the crbAccountStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbAccountStatuses in body.
     */
    @GetMapping("/crb-account-statuses")
    public ResponseEntity<List<CrbAccountStatusDTO>> getAllCrbAccountStatuses(CrbAccountStatusCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CrbAccountStatuses by criteria: {}", criteria);
        Page<CrbAccountStatusDTO> page = crbAccountStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-account-statuses/count} : count all the crbAccountStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-account-statuses/count")
    public ResponseEntity<Long> countCrbAccountStatuses(CrbAccountStatusCriteria criteria) {
        log.debug("REST request to count CrbAccountStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbAccountStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-account-statuses/:id} : get the "id" crbAccountStatus.
     *
     * @param id the id of the crbAccountStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbAccountStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-account-statuses/{id}")
    public ResponseEntity<CrbAccountStatusDTO> getCrbAccountStatus(@PathVariable Long id) {
        log.debug("REST request to get CrbAccountStatus : {}", id);
        Optional<CrbAccountStatusDTO> crbAccountStatusDTO = crbAccountStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbAccountStatusDTO);
    }

    /**
     * {@code DELETE  /crb-account-statuses/:id} : delete the "id" crbAccountStatus.
     *
     * @param id the id of the crbAccountStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-account-statuses/{id}")
    public ResponseEntity<Void> deleteCrbAccountStatus(@PathVariable Long id) {
        log.debug("REST request to delete CrbAccountStatus : {}", id);
        crbAccountStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-account-statuses?query=:query} : search for the crbAccountStatus corresponding
     * to the query.
     *
     * @param query the query of the crbAccountStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-account-statuses")
    public ResponseEntity<List<CrbAccountStatusDTO>> searchCrbAccountStatuses(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbAccountStatuses for query {}", query);
        Page<CrbAccountStatusDTO> page = crbAccountStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
