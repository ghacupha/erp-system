package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.repository.PrepaymentReportRequisitionRepository;
import io.github.erp.service.PrepaymentReportRequisitionQueryService;
import io.github.erp.service.PrepaymentReportRequisitionService;
import io.github.erp.service.criteria.PrepaymentReportRequisitionCriteria;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.PrepaymentReportRequisition}.
 */
@RestController
@RequestMapping("/api")
public class PrepaymentReportRequisitionResource {

    private final Logger log = LoggerFactory.getLogger(PrepaymentReportRequisitionResource.class);

    private static final String ENTITY_NAME = "prepaymentReportRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrepaymentReportRequisitionService prepaymentReportRequisitionService;

    private final PrepaymentReportRequisitionRepository prepaymentReportRequisitionRepository;

    private final PrepaymentReportRequisitionQueryService prepaymentReportRequisitionQueryService;

    public PrepaymentReportRequisitionResource(
        PrepaymentReportRequisitionService prepaymentReportRequisitionService,
        PrepaymentReportRequisitionRepository prepaymentReportRequisitionRepository,
        PrepaymentReportRequisitionQueryService prepaymentReportRequisitionQueryService
    ) {
        this.prepaymentReportRequisitionService = prepaymentReportRequisitionService;
        this.prepaymentReportRequisitionRepository = prepaymentReportRequisitionRepository;
        this.prepaymentReportRequisitionQueryService = prepaymentReportRequisitionQueryService;
    }

    /**
     * {@code POST  /prepayment-report-requisitions} : Create a new prepaymentReportRequisition.
     *
     * @param prepaymentReportRequisitionDTO the prepaymentReportRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prepaymentReportRequisitionDTO, or with status {@code 400 (Bad Request)} if the prepaymentReportRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prepayment-report-requisitions")
    public ResponseEntity<PrepaymentReportRequisitionDTO> createPrepaymentReportRequisition(
        @Valid @RequestBody PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PrepaymentReportRequisition : {}", prepaymentReportRequisitionDTO);
        if (prepaymentReportRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new prepaymentReportRequisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrepaymentReportRequisitionDTO result = prepaymentReportRequisitionService.save(prepaymentReportRequisitionDTO);
        return ResponseEntity
            .created(new URI("/api/prepayment-report-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prepayment-report-requisitions/:id} : Updates an existing prepaymentReportRequisition.
     *
     * @param id the id of the prepaymentReportRequisitionDTO to save.
     * @param prepaymentReportRequisitionDTO the prepaymentReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentReportRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prepayment-report-requisitions/{id}")
    public ResponseEntity<PrepaymentReportRequisitionDTO> updatePrepaymentReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrepaymentReportRequisition : {}, {}", id, prepaymentReportRequisitionDTO);
        if (prepaymentReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrepaymentReportRequisitionDTO result = prepaymentReportRequisitionService.save(prepaymentReportRequisitionDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prepaymentReportRequisitionDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /prepayment-report-requisitions/:id} : Partial updates given fields of an existing prepaymentReportRequisition, field will ignore if it is null
     *
     * @param id the id of the prepaymentReportRequisitionDTO to save.
     * @param prepaymentReportRequisitionDTO the prepaymentReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentReportRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prepaymentReportRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prepayment-report-requisitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrepaymentReportRequisitionDTO> partialUpdatePrepaymentReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrepaymentReportRequisitionDTO prepaymentReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrepaymentReportRequisition partially : {}, {}", id, prepaymentReportRequisitionDTO);
        if (prepaymentReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrepaymentReportRequisitionDTO> result = prepaymentReportRequisitionService.partialUpdate(prepaymentReportRequisitionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prepaymentReportRequisitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prepayment-report-requisitions} : get all the prepaymentReportRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentReportRequisitions in body.
     */
    @GetMapping("/prepayment-report-requisitions")
    public ResponseEntity<List<PrepaymentReportRequisitionDTO>> getAllPrepaymentReportRequisitions(
        PrepaymentReportRequisitionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get PrepaymentReportRequisitions by criteria: {}", criteria);
        Page<PrepaymentReportRequisitionDTO> page = prepaymentReportRequisitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-report-requisitions/count} : count all the prepaymentReportRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-report-requisitions/count")
    public ResponseEntity<Long> countPrepaymentReportRequisitions(PrepaymentReportRequisitionCriteria criteria) {
        log.debug("REST request to count PrepaymentReportRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentReportRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-report-requisitions/:id} : get the "id" prepaymentReportRequisition.
     *
     * @param id the id of the prepaymentReportRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentReportRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-report-requisitions/{id}")
    public ResponseEntity<PrepaymentReportRequisitionDTO> getPrepaymentReportRequisition(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentReportRequisition : {}", id);
        Optional<PrepaymentReportRequisitionDTO> prepaymentReportRequisitionDTO = prepaymentReportRequisitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaymentReportRequisitionDTO);
    }

    /**
     * {@code DELETE  /prepayment-report-requisitions/:id} : delete the "id" prepaymentReportRequisition.
     *
     * @param id the id of the prepaymentReportRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prepayment-report-requisitions/{id}")
    public ResponseEntity<Void> deletePrepaymentReportRequisition(@PathVariable Long id) {
        log.debug("REST request to delete PrepaymentReportRequisition : {}", id);
        prepaymentReportRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/prepayment-report-requisitions?query=:query} : search for the prepaymentReportRequisition corresponding
     * to the query.
     *
     * @param query the query of the prepaymentReportRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-report-requisitions")
    public ResponseEntity<List<PrepaymentReportRequisitionDTO>> searchPrepaymentReportRequisitions(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of PrepaymentReportRequisitions for query {}", query);
        Page<PrepaymentReportRequisitionDTO> page = prepaymentReportRequisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
