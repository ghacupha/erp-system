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

import io.github.erp.repository.MonthlyPrepaymentReportRequisitionRepository;
import io.github.erp.service.MonthlyPrepaymentReportRequisitionQueryService;
import io.github.erp.service.MonthlyPrepaymentReportRequisitionService;
import io.github.erp.service.criteria.MonthlyPrepaymentReportRequisitionCriteria;
import io.github.erp.service.dto.MonthlyPrepaymentReportRequisitionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.MonthlyPrepaymentReportRequisition}.
 */
@RestController
@RequestMapping("/api")
public class MonthlyPrepaymentReportRequisitionResource {

    private final Logger log = LoggerFactory.getLogger(MonthlyPrepaymentReportRequisitionResource.class);

    private static final String ENTITY_NAME = "monthlyPrepaymentReportRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonthlyPrepaymentReportRequisitionService monthlyPrepaymentReportRequisitionService;

    private final MonthlyPrepaymentReportRequisitionRepository monthlyPrepaymentReportRequisitionRepository;

    private final MonthlyPrepaymentReportRequisitionQueryService monthlyPrepaymentReportRequisitionQueryService;

    public MonthlyPrepaymentReportRequisitionResource(
        MonthlyPrepaymentReportRequisitionService monthlyPrepaymentReportRequisitionService,
        MonthlyPrepaymentReportRequisitionRepository monthlyPrepaymentReportRequisitionRepository,
        MonthlyPrepaymentReportRequisitionQueryService monthlyPrepaymentReportRequisitionQueryService
    ) {
        this.monthlyPrepaymentReportRequisitionService = monthlyPrepaymentReportRequisitionService;
        this.monthlyPrepaymentReportRequisitionRepository = monthlyPrepaymentReportRequisitionRepository;
        this.monthlyPrepaymentReportRequisitionQueryService = monthlyPrepaymentReportRequisitionQueryService;
    }

    /**
     * {@code POST  /monthly-prepayment-report-requisitions} : Create a new monthlyPrepaymentReportRequisition.
     *
     * @param monthlyPrepaymentReportRequisitionDTO the monthlyPrepaymentReportRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monthlyPrepaymentReportRequisitionDTO, or with status {@code 400 (Bad Request)} if the monthlyPrepaymentReportRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/monthly-prepayment-report-requisitions")
    public ResponseEntity<MonthlyPrepaymentReportRequisitionDTO> createMonthlyPrepaymentReportRequisition(
        @Valid @RequestBody MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save MonthlyPrepaymentReportRequisition : {}", monthlyPrepaymentReportRequisitionDTO);
        if (monthlyPrepaymentReportRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new monthlyPrepaymentReportRequisition cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        MonthlyPrepaymentReportRequisitionDTO result = monthlyPrepaymentReportRequisitionService.save(
            monthlyPrepaymentReportRequisitionDTO
        );
        return ResponseEntity
            .created(new URI("/api/monthly-prepayment-report-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /monthly-prepayment-report-requisitions/:id} : Updates an existing monthlyPrepaymentReportRequisition.
     *
     * @param id the id of the monthlyPrepaymentReportRequisitionDTO to save.
     * @param monthlyPrepaymentReportRequisitionDTO the monthlyPrepaymentReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monthlyPrepaymentReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the monthlyPrepaymentReportRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monthlyPrepaymentReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/monthly-prepayment-report-requisitions/{id}")
    public ResponseEntity<MonthlyPrepaymentReportRequisitionDTO> updateMonthlyPrepaymentReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MonthlyPrepaymentReportRequisition : {}, {}", id, monthlyPrepaymentReportRequisitionDTO);
        if (monthlyPrepaymentReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monthlyPrepaymentReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monthlyPrepaymentReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MonthlyPrepaymentReportRequisitionDTO result = monthlyPrepaymentReportRequisitionService.save(
            monthlyPrepaymentReportRequisitionDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    monthlyPrepaymentReportRequisitionDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /monthly-prepayment-report-requisitions/:id} : Partial updates given fields of an existing monthlyPrepaymentReportRequisition, field will ignore if it is null
     *
     * @param id the id of the monthlyPrepaymentReportRequisitionDTO to save.
     * @param monthlyPrepaymentReportRequisitionDTO the monthlyPrepaymentReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monthlyPrepaymentReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the monthlyPrepaymentReportRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monthlyPrepaymentReportRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monthlyPrepaymentReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/monthly-prepayment-report-requisitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonthlyPrepaymentReportRequisitionDTO> partialUpdateMonthlyPrepaymentReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MonthlyPrepaymentReportRequisitionDTO monthlyPrepaymentReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update MonthlyPrepaymentReportRequisition partially : {}, {}",
            id,
            monthlyPrepaymentReportRequisitionDTO
        );
        if (monthlyPrepaymentReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monthlyPrepaymentReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monthlyPrepaymentReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonthlyPrepaymentReportRequisitionDTO> result = monthlyPrepaymentReportRequisitionService.partialUpdate(
            monthlyPrepaymentReportRequisitionDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monthlyPrepaymentReportRequisitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monthly-prepayment-report-requisitions} : get all the monthlyPrepaymentReportRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monthlyPrepaymentReportRequisitions in body.
     */
    @GetMapping("/monthly-prepayment-report-requisitions")
    public ResponseEntity<List<MonthlyPrepaymentReportRequisitionDTO>> getAllMonthlyPrepaymentReportRequisitions(
        MonthlyPrepaymentReportRequisitionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get MonthlyPrepaymentReportRequisitions by criteria: {}", criteria);
        Page<MonthlyPrepaymentReportRequisitionDTO> page = monthlyPrepaymentReportRequisitionQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monthly-prepayment-report-requisitions/count} : count all the monthlyPrepaymentReportRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/monthly-prepayment-report-requisitions/count")
    public ResponseEntity<Long> countMonthlyPrepaymentReportRequisitions(MonthlyPrepaymentReportRequisitionCriteria criteria) {
        log.debug("REST request to count MonthlyPrepaymentReportRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(monthlyPrepaymentReportRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monthly-prepayment-report-requisitions/:id} : get the "id" monthlyPrepaymentReportRequisition.
     *
     * @param id the id of the monthlyPrepaymentReportRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monthlyPrepaymentReportRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/monthly-prepayment-report-requisitions/{id}")
    public ResponseEntity<MonthlyPrepaymentReportRequisitionDTO> getMonthlyPrepaymentReportRequisition(@PathVariable Long id) {
        log.debug("REST request to get MonthlyPrepaymentReportRequisition : {}", id);
        Optional<MonthlyPrepaymentReportRequisitionDTO> monthlyPrepaymentReportRequisitionDTO = monthlyPrepaymentReportRequisitionService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(monthlyPrepaymentReportRequisitionDTO);
    }

    /**
     * {@code DELETE  /monthly-prepayment-report-requisitions/:id} : delete the "id" monthlyPrepaymentReportRequisition.
     *
     * @param id the id of the monthlyPrepaymentReportRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/monthly-prepayment-report-requisitions/{id}")
    public ResponseEntity<Void> deleteMonthlyPrepaymentReportRequisition(@PathVariable Long id) {
        log.debug("REST request to delete MonthlyPrepaymentReportRequisition : {}", id);
        monthlyPrepaymentReportRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/monthly-prepayment-report-requisitions?query=:query} : search for the monthlyPrepaymentReportRequisition corresponding
     * to the query.
     *
     * @param query the query of the monthlyPrepaymentReportRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/monthly-prepayment-report-requisitions")
    public ResponseEntity<List<MonthlyPrepaymentReportRequisitionDTO>> searchMonthlyPrepaymentReportRequisitions(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of MonthlyPrepaymentReportRequisitions for query {}", query);
        Page<MonthlyPrepaymentReportRequisitionDTO> page = monthlyPrepaymentReportRequisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
