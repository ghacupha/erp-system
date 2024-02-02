package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.2-SNAPSHOT
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
import io.github.erp.repository.CrbFileTransmissionStatusRepository;
import io.github.erp.service.CrbFileTransmissionStatusQueryService;
import io.github.erp.service.CrbFileTransmissionStatusService;
import io.github.erp.service.criteria.CrbFileTransmissionStatusCriteria;
import io.github.erp.service.dto.CrbFileTransmissionStatusDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbFileTransmissionStatus}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CrbFileTransmissionStatusResourceProd {

    private final Logger log = LoggerFactory.getLogger(CrbFileTransmissionStatusResourceProd.class);

    private static final String ENTITY_NAME = "crbFileTransmissionStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbFileTransmissionStatusService crbFileTransmissionStatusService;

    private final CrbFileTransmissionStatusRepository crbFileTransmissionStatusRepository;

    private final CrbFileTransmissionStatusQueryService crbFileTransmissionStatusQueryService;

    public CrbFileTransmissionStatusResourceProd(
        CrbFileTransmissionStatusService crbFileTransmissionStatusService,
        CrbFileTransmissionStatusRepository crbFileTransmissionStatusRepository,
        CrbFileTransmissionStatusQueryService crbFileTransmissionStatusQueryService
    ) {
        this.crbFileTransmissionStatusService = crbFileTransmissionStatusService;
        this.crbFileTransmissionStatusRepository = crbFileTransmissionStatusRepository;
        this.crbFileTransmissionStatusQueryService = crbFileTransmissionStatusQueryService;
    }

    /**
     * {@code POST  /crb-file-transmission-statuses} : Create a new crbFileTransmissionStatus.
     *
     * @param crbFileTransmissionStatusDTO the crbFileTransmissionStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbFileTransmissionStatusDTO, or with status {@code 400 (Bad Request)} if the crbFileTransmissionStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-file-transmission-statuses")
    public ResponseEntity<CrbFileTransmissionStatusDTO> createCrbFileTransmissionStatus(
        @Valid @RequestBody CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbFileTransmissionStatus : {}", crbFileTransmissionStatusDTO);
        if (crbFileTransmissionStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbFileTransmissionStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbFileTransmissionStatusDTO result = crbFileTransmissionStatusService.save(crbFileTransmissionStatusDTO);
        return ResponseEntity
            .created(new URI("/api/crb-file-transmission-statuses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-file-transmission-statuses/:id} : Updates an existing crbFileTransmissionStatus.
     *
     * @param id the id of the crbFileTransmissionStatusDTO to save.
     * @param crbFileTransmissionStatusDTO the crbFileTransmissionStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbFileTransmissionStatusDTO,
     * or with status {@code 400 (Bad Request)} if the crbFileTransmissionStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbFileTransmissionStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-file-transmission-statuses/{id}")
    public ResponseEntity<CrbFileTransmissionStatusDTO> updateCrbFileTransmissionStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbFileTransmissionStatus : {}, {}", id, crbFileTransmissionStatusDTO);
        if (crbFileTransmissionStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbFileTransmissionStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbFileTransmissionStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbFileTransmissionStatusDTO result = crbFileTransmissionStatusService.save(crbFileTransmissionStatusDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbFileTransmissionStatusDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /crb-file-transmission-statuses/:id} : Partial updates given fields of an existing crbFileTransmissionStatus, field will ignore if it is null
     *
     * @param id the id of the crbFileTransmissionStatusDTO to save.
     * @param crbFileTransmissionStatusDTO the crbFileTransmissionStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbFileTransmissionStatusDTO,
     * or with status {@code 400 (Bad Request)} if the crbFileTransmissionStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbFileTransmissionStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbFileTransmissionStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-file-transmission-statuses/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbFileTransmissionStatusDTO> partialUpdateCrbFileTransmissionStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbFileTransmissionStatusDTO crbFileTransmissionStatusDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbFileTransmissionStatus partially : {}, {}", id, crbFileTransmissionStatusDTO);
        if (crbFileTransmissionStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbFileTransmissionStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbFileTransmissionStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbFileTransmissionStatusDTO> result = crbFileTransmissionStatusService.partialUpdate(crbFileTransmissionStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbFileTransmissionStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-file-transmission-statuses} : get all the crbFileTransmissionStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbFileTransmissionStatuses in body.
     */
    @GetMapping("/crb-file-transmission-statuses")
    public ResponseEntity<List<CrbFileTransmissionStatusDTO>> getAllCrbFileTransmissionStatuses(
        CrbFileTransmissionStatusCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbFileTransmissionStatuses by criteria: {}", criteria);
        Page<CrbFileTransmissionStatusDTO> page = crbFileTransmissionStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-file-transmission-statuses/count} : count all the crbFileTransmissionStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-file-transmission-statuses/count")
    public ResponseEntity<Long> countCrbFileTransmissionStatuses(CrbFileTransmissionStatusCriteria criteria) {
        log.debug("REST request to count CrbFileTransmissionStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbFileTransmissionStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-file-transmission-statuses/:id} : get the "id" crbFileTransmissionStatus.
     *
     * @param id the id of the crbFileTransmissionStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbFileTransmissionStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-file-transmission-statuses/{id}")
    public ResponseEntity<CrbFileTransmissionStatusDTO> getCrbFileTransmissionStatus(@PathVariable Long id) {
        log.debug("REST request to get CrbFileTransmissionStatus : {}", id);
        Optional<CrbFileTransmissionStatusDTO> crbFileTransmissionStatusDTO = crbFileTransmissionStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbFileTransmissionStatusDTO);
    }

    /**
     * {@code DELETE  /crb-file-transmission-statuses/:id} : delete the "id" crbFileTransmissionStatus.
     *
     * @param id the id of the crbFileTransmissionStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-file-transmission-statuses/{id}")
    public ResponseEntity<Void> deleteCrbFileTransmissionStatus(@PathVariable Long id) {
        log.debug("REST request to delete CrbFileTransmissionStatus : {}", id);
        crbFileTransmissionStatusService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-file-transmission-statuses?query=:query} : search for the crbFileTransmissionStatus corresponding
     * to the query.
     *
     * @param query the query of the crbFileTransmissionStatus search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-file-transmission-statuses")
    public ResponseEntity<List<CrbFileTransmissionStatusDTO>> searchCrbFileTransmissionStatuses(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CrbFileTransmissionStatuses for query {}", query);
        Page<CrbFileTransmissionStatusDTO> page = crbFileTransmissionStatusService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
