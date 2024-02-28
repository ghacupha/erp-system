package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.repository.CrbCreditApplicationStatusRepository;
import io.github.erp.service.CrbCreditApplicationStatusQueryService;
import io.github.erp.service.CrbCreditApplicationStatusService;
import io.github.erp.service.criteria.CrbCreditApplicationStatusCriteria;
import io.github.erp.service.dto.CrbCreditApplicationStatusDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbCreditApplicationStatus}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CrbCreditApplicationStatusResourceProd {

    private final Logger log = LoggerFactory.getLogger(CrbCreditApplicationStatusResourceProd.class);

    private static final String ENTITY_NAME = "crbCreditApplicationStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbCreditApplicationStatusService crbCreditApplicationStatusService;

    private final CrbCreditApplicationStatusRepository crbCreditApplicationStatusRepository;

    private final CrbCreditApplicationStatusQueryService crbCreditApplicationStatusQueryService;

    public CrbCreditApplicationStatusResourceProd(
        CrbCreditApplicationStatusService crbCreditApplicationStatusService,
        CrbCreditApplicationStatusRepository crbCreditApplicationStatusRepository,
        CrbCreditApplicationStatusQueryService crbCreditApplicationStatusQueryService
    ) {
        this.crbCreditApplicationStatusService = crbCreditApplicationStatusService;
        this.crbCreditApplicationStatusRepository = crbCreditApplicationStatusRepository;
        this.crbCreditApplicationStatusQueryService = crbCreditApplicationStatusQueryService;
    }

    /**
     * {@code POST  /crb-credit-application-statuses} : Create a new crbCreditApplicationStatus.
     *
     * @param crbCreditApplicationStatusDTO the crbCreditApplicationStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbCreditApplicationStatusDTO, or with status {@code 400 (Bad Request)} if the crbCreditApplicationStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-credit-application-statuses")
    public ResponseEntity<CrbCreditApplicationStatusDTO> createCrbCreditApplicationStatus(
        @Valid @RequestBody CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbCreditApplicationStatus : {}", crbCreditApplicationStatusDTO);
        if (crbCreditApplicationStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbCreditApplicationStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbCreditApplicationStatusDTO result = crbCreditApplicationStatusService.save(crbCreditApplicationStatusDTO);
        return ResponseEntity
            .created(new URI("/api/crb-credit-application-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-credit-application-statuses/:id} : Updates an existing crbCreditApplicationStatus.
     *
     * @param id the id of the crbCreditApplicationStatusDTO to save.
     * @param crbCreditApplicationStatusDTO the crbCreditApplicationStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbCreditApplicationStatusDTO,
     * or with status {@code 400 (Bad Request)} if the crbCreditApplicationStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbCreditApplicationStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-credit-application-statuses/{id}")
    public ResponseEntity<CrbCreditApplicationStatusDTO> updateCrbCreditApplicationStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbCreditApplicationStatus : {}, {}", id, crbCreditApplicationStatusDTO);
        if (crbCreditApplicationStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbCreditApplicationStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbCreditApplicationStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbCreditApplicationStatusDTO result = crbCreditApplicationStatusService.save(crbCreditApplicationStatusDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbCreditApplicationStatusDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /crb-credit-application-statuses/:id} : Partial updates given fields of an existing crbCreditApplicationStatus, field will ignore if it is null
     *
     * @param id the id of the crbCreditApplicationStatusDTO to save.
     * @param crbCreditApplicationStatusDTO the crbCreditApplicationStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbCreditApplicationStatusDTO,
     * or with status {@code 400 (Bad Request)} if the crbCreditApplicationStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbCreditApplicationStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbCreditApplicationStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-credit-application-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbCreditApplicationStatusDTO> partialUpdateCrbCreditApplicationStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbCreditApplicationStatusDTO crbCreditApplicationStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbCreditApplicationStatus partially : {}, {}", id, crbCreditApplicationStatusDTO);
        if (crbCreditApplicationStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbCreditApplicationStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbCreditApplicationStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbCreditApplicationStatusDTO> result = crbCreditApplicationStatusService.partialUpdate(crbCreditApplicationStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbCreditApplicationStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-credit-application-statuses} : get all the crbCreditApplicationStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbCreditApplicationStatuses in body.
     */
    @GetMapping("/crb-credit-application-statuses")
    public ResponseEntity<List<CrbCreditApplicationStatusDTO>> getAllCrbCreditApplicationStatuses(
        CrbCreditApplicationStatusCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbCreditApplicationStatuses by criteria: {}", criteria);
        Page<CrbCreditApplicationStatusDTO> page = crbCreditApplicationStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-credit-application-statuses/count} : count all the crbCreditApplicationStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-credit-application-statuses/count")
    public ResponseEntity<Long> countCrbCreditApplicationStatuses(CrbCreditApplicationStatusCriteria criteria) {
        log.debug("REST request to count CrbCreditApplicationStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbCreditApplicationStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-credit-application-statuses/:id} : get the "id" crbCreditApplicationStatus.
     *
     * @param id the id of the crbCreditApplicationStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbCreditApplicationStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-credit-application-statuses/{id}")
    public ResponseEntity<CrbCreditApplicationStatusDTO> getCrbCreditApplicationStatus(@PathVariable Long id) {
        log.debug("REST request to get CrbCreditApplicationStatus : {}", id);
        Optional<CrbCreditApplicationStatusDTO> crbCreditApplicationStatusDTO = crbCreditApplicationStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbCreditApplicationStatusDTO);
    }

    /**
     * {@code DELETE  /crb-credit-application-statuses/:id} : delete the "id" crbCreditApplicationStatus.
     *
     * @param id the id of the crbCreditApplicationStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-credit-application-statuses/{id}")
    public ResponseEntity<Void> deleteCrbCreditApplicationStatus(@PathVariable Long id) {
        log.debug("REST request to delete CrbCreditApplicationStatus : {}", id);
        crbCreditApplicationStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-credit-application-statuses?query=:query} : search for the crbCreditApplicationStatus corresponding
     * to the query.
     *
     * @param query the query of the crbCreditApplicationStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-credit-application-statuses")
    public ResponseEntity<List<CrbCreditApplicationStatusDTO>> searchCrbCreditApplicationStatuses(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CrbCreditApplicationStatuses for query {}", query);
        Page<CrbCreditApplicationStatusDTO> page = crbCreditApplicationStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
