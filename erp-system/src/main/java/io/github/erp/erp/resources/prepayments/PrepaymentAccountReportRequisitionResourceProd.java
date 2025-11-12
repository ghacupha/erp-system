package io.github.erp.erp.resources.prepayments;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.internal.service.prepayments.InternalPrepaymentAccountReportRequisitionService;
import io.github.erp.repository.PrepaymentAccountReportRequisitionRepository;
import io.github.erp.service.PrepaymentAccountReportRequisitionQueryService;
import io.github.erp.service.criteria.PrepaymentAccountReportRequisitionCriteria;
import io.github.erp.service.dto.PrepaymentAccountReportRequisitionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.PrepaymentAccountReportRequisition}.
 */
@RestController
@RequestMapping("/api/prepayments")
public class PrepaymentAccountReportRequisitionResourceProd {

    private final Logger log = LoggerFactory.getLogger(PrepaymentAccountReportRequisitionResourceProd.class);

    private static final String ENTITY_NAME = "prepaymentAccountReportRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalPrepaymentAccountReportRequisitionService prepaymentAccountReportRequisitionService;

    private final PrepaymentAccountReportRequisitionRepository prepaymentAccountReportRequisitionRepository;

    private final PrepaymentAccountReportRequisitionQueryService prepaymentAccountReportRequisitionQueryService;

    public PrepaymentAccountReportRequisitionResourceProd(
        InternalPrepaymentAccountReportRequisitionService prepaymentAccountReportRequisitionService,
        PrepaymentAccountReportRequisitionRepository prepaymentAccountReportRequisitionRepository,
        PrepaymentAccountReportRequisitionQueryService prepaymentAccountReportRequisitionQueryService
    ) {
        this.prepaymentAccountReportRequisitionService = prepaymentAccountReportRequisitionService;
        this.prepaymentAccountReportRequisitionRepository = prepaymentAccountReportRequisitionRepository;
        this.prepaymentAccountReportRequisitionQueryService = prepaymentAccountReportRequisitionQueryService;
    }

    /**
     * {@code POST  /prepayment-account-report-requisitions} : Create a new prepaymentAccountReportRequisition.
     *
     * @param prepaymentAccountReportRequisitionDTO the prepaymentAccountReportRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prepaymentAccountReportRequisitionDTO, or with status {@code 400 (Bad Request)} if the prepaymentAccountReportRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prepayment-account-report-requisitions")
    public ResponseEntity<PrepaymentAccountReportRequisitionDTO> createPrepaymentAccountReportRequisition(
        @Valid @RequestBody PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PrepaymentAccountReportRequisition : {}", prepaymentAccountReportRequisitionDTO);
        if (prepaymentAccountReportRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new prepaymentAccountReportRequisition cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        PrepaymentAccountReportRequisitionDTO result = prepaymentAccountReportRequisitionService.save(
            prepaymentAccountReportRequisitionDTO
        );
        return ResponseEntity
            .created(new URI("/api/prepayment-account-report-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prepayment-account-report-requisitions/:id} : Updates an existing prepaymentAccountReportRequisition.
     *
     * @param id the id of the prepaymentAccountReportRequisitionDTO to save.
     * @param prepaymentAccountReportRequisitionDTO the prepaymentAccountReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentAccountReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentAccountReportRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentAccountReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prepayment-account-report-requisitions/{id}")
    public ResponseEntity<PrepaymentAccountReportRequisitionDTO> updatePrepaymentAccountReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrepaymentAccountReportRequisition : {}, {}", id, prepaymentAccountReportRequisitionDTO);
        if (prepaymentAccountReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentAccountReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentAccountReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrepaymentAccountReportRequisitionDTO result = prepaymentAccountReportRequisitionService.save(
            prepaymentAccountReportRequisitionDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    prepaymentAccountReportRequisitionDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /prepayment-account-report-requisitions/:id} : Partial updates given fields of an existing prepaymentAccountReportRequisition, field will ignore if it is null
     *
     * @param id the id of the prepaymentAccountReportRequisitionDTO to save.
     * @param prepaymentAccountReportRequisitionDTO the prepaymentAccountReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentAccountReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentAccountReportRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prepaymentAccountReportRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentAccountReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prepayment-account-report-requisitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrepaymentAccountReportRequisitionDTO> partialUpdatePrepaymentAccountReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrepaymentAccountReportRequisitionDTO prepaymentAccountReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update PrepaymentAccountReportRequisition partially : {}, {}",
            id,
            prepaymentAccountReportRequisitionDTO
        );
        if (prepaymentAccountReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentAccountReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentAccountReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrepaymentAccountReportRequisitionDTO> result = prepaymentAccountReportRequisitionService.partialUpdate(
            prepaymentAccountReportRequisitionDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prepaymentAccountReportRequisitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prepayment-account-report-requisitions} : get all the prepaymentAccountReportRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentAccountReportRequisitions in body.
     */
    @GetMapping("/prepayment-account-report-requisitions")
    public ResponseEntity<List<PrepaymentAccountReportRequisitionDTO>> getAllPrepaymentAccountReportRequisitions(
        PrepaymentAccountReportRequisitionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get PrepaymentAccountReportRequisitions by criteria: {}", criteria);
        Page<PrepaymentAccountReportRequisitionDTO> page = prepaymentAccountReportRequisitionQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-account-report-requisitions/count} : count all the prepaymentAccountReportRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-account-report-requisitions/count")
    public ResponseEntity<Long> countPrepaymentAccountReportRequisitions(PrepaymentAccountReportRequisitionCriteria criteria) {
        log.debug("REST request to count PrepaymentAccountReportRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentAccountReportRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-account-report-requisitions/:id} : get the "id" prepaymentAccountReportRequisition.
     *
     * @param id the id of the prepaymentAccountReportRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentAccountReportRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-account-report-requisitions/{id}")
    public ResponseEntity<PrepaymentAccountReportRequisitionDTO> getPrepaymentAccountReportRequisition(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentAccountReportRequisition : {}", id);
        Optional<PrepaymentAccountReportRequisitionDTO> prepaymentAccountReportRequisitionDTO = prepaymentAccountReportRequisitionService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(prepaymentAccountReportRequisitionDTO);
    }

    /**
     * {@code DELETE  /prepayment-account-report-requisitions/:id} : delete the "id" prepaymentAccountReportRequisition.
     *
     * @param id the id of the prepaymentAccountReportRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prepayment-account-report-requisitions/{id}")
    public ResponseEntity<Void> deletePrepaymentAccountReportRequisition(@PathVariable Long id) {
        log.debug("REST request to delete PrepaymentAccountReportRequisition : {}", id);
        prepaymentAccountReportRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/prepayment-account-report-requisitions?query=:query} : search for the prepaymentAccountReportRequisition corresponding
     * to the query.
     *
     * @param query the query of the prepaymentAccountReportRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-account-report-requisitions")
    public ResponseEntity<List<PrepaymentAccountReportRequisitionDTO>> searchPrepaymentAccountReportRequisitions(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of PrepaymentAccountReportRequisitions for query {}", query);
        Page<PrepaymentAccountReportRequisitionDTO> page = prepaymentAccountReportRequisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
