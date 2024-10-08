package io.github.erp.erp.resources.prepayments;

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
import io.github.erp.repository.PrepaymentByAccountReportRequisitionRepository;
import io.github.erp.service.PrepaymentByAccountReportRequisitionQueryService;
import io.github.erp.service.PrepaymentByAccountReportRequisitionService;
import io.github.erp.service.criteria.PrepaymentByAccountReportRequisitionCriteria;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.PrepaymentByAccountReportRequisition}.
 */
@RestController
@RequestMapping("/api/prepayments")
public class PrepaymentByAccountReportRequisitionResourceProd {

    private final Logger log = LoggerFactory.getLogger(PrepaymentByAccountReportRequisitionResourceProd.class);

    private static final String ENTITY_NAME = "prepaymentByAccountReportRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrepaymentByAccountReportRequisitionService prepaymentByAccountReportRequisitionService;

    private final PrepaymentByAccountReportRequisitionRepository prepaymentByAccountReportRequisitionRepository;

    private final PrepaymentByAccountReportRequisitionQueryService prepaymentByAccountReportRequisitionQueryService;

    public PrepaymentByAccountReportRequisitionResourceProd(
        PrepaymentByAccountReportRequisitionService prepaymentByAccountReportRequisitionService,
        PrepaymentByAccountReportRequisitionRepository prepaymentByAccountReportRequisitionRepository,
        PrepaymentByAccountReportRequisitionQueryService prepaymentByAccountReportRequisitionQueryService
    ) {
        this.prepaymentByAccountReportRequisitionService = prepaymentByAccountReportRequisitionService;
        this.prepaymentByAccountReportRequisitionRepository = prepaymentByAccountReportRequisitionRepository;
        this.prepaymentByAccountReportRequisitionQueryService = prepaymentByAccountReportRequisitionQueryService;
    }

    /**
     * {@code POST  /prepayment-by-account-report-requisitions} : Create a new prepaymentByAccountReportRequisition.
     *
     * @param prepaymentByAccountReportRequisitionDTO the prepaymentByAccountReportRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prepaymentByAccountReportRequisitionDTO, or with status {@code 400 (Bad Request)} if the prepaymentByAccountReportRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prepayment-by-account-report-requisitions")
    public ResponseEntity<PrepaymentByAccountReportRequisitionDTO> createPrepaymentByAccountReportRequisition(
        @Valid @RequestBody PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PrepaymentByAccountReportRequisition : {}", prepaymentByAccountReportRequisitionDTO);
        if (prepaymentByAccountReportRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new prepaymentByAccountReportRequisition cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        PrepaymentByAccountReportRequisitionDTO result = prepaymentByAccountReportRequisitionService.save(
            prepaymentByAccountReportRequisitionDTO
        );
        return ResponseEntity
            .created(new URI("/api/prepayment-by-account-report-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prepayment-by-account-report-requisitions/:id} : Updates an existing prepaymentByAccountReportRequisition.
     *
     * @param id the id of the prepaymentByAccountReportRequisitionDTO to save.
     * @param prepaymentByAccountReportRequisitionDTO the prepaymentByAccountReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentByAccountReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentByAccountReportRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentByAccountReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prepayment-by-account-report-requisitions/{id}")
    public ResponseEntity<PrepaymentByAccountReportRequisitionDTO> updatePrepaymentByAccountReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrepaymentByAccountReportRequisition : {}, {}", id, prepaymentByAccountReportRequisitionDTO);
        if (prepaymentByAccountReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentByAccountReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentByAccountReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrepaymentByAccountReportRequisitionDTO result = prepaymentByAccountReportRequisitionService.save(
            prepaymentByAccountReportRequisitionDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    prepaymentByAccountReportRequisitionDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /prepayment-by-account-report-requisitions/:id} : Partial updates given fields of an existing prepaymentByAccountReportRequisition, field will ignore if it is null
     *
     * @param id the id of the prepaymentByAccountReportRequisitionDTO to save.
     * @param prepaymentByAccountReportRequisitionDTO the prepaymentByAccountReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentByAccountReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentByAccountReportRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prepaymentByAccountReportRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentByAccountReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(
        value = "/prepayment-by-account-report-requisitions/{id}",
        consumes = { "application/json", "application/merge-patch+json" }
    )
    public ResponseEntity<PrepaymentByAccountReportRequisitionDTO> partialUpdatePrepaymentByAccountReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrepaymentByAccountReportRequisitionDTO prepaymentByAccountReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update PrepaymentByAccountReportRequisition partially : {}, {}",
            id,
            prepaymentByAccountReportRequisitionDTO
        );
        if (prepaymentByAccountReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentByAccountReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentByAccountReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrepaymentByAccountReportRequisitionDTO> result = prepaymentByAccountReportRequisitionService.partialUpdate(
            prepaymentByAccountReportRequisitionDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                true,
                ENTITY_NAME,
                prepaymentByAccountReportRequisitionDTO.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /prepayment-by-account-report-requisitions} : get all the prepaymentByAccountReportRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentByAccountReportRequisitions in body.
     */
    @GetMapping("/prepayment-by-account-report-requisitions")
    public ResponseEntity<List<PrepaymentByAccountReportRequisitionDTO>> getAllPrepaymentByAccountReportRequisitions(
        PrepaymentByAccountReportRequisitionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get PrepaymentByAccountReportRequisitions by criteria: {}", criteria);
        Page<PrepaymentByAccountReportRequisitionDTO> page = prepaymentByAccountReportRequisitionQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-by-account-report-requisitions/count} : count all the prepaymentByAccountReportRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-by-account-report-requisitions/count")
    public ResponseEntity<Long> countPrepaymentByAccountReportRequisitions(PrepaymentByAccountReportRequisitionCriteria criteria) {
        log.debug("REST request to count PrepaymentByAccountReportRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentByAccountReportRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-by-account-report-requisitions/:id} : get the "id" prepaymentByAccountReportRequisition.
     *
     * @param id the id of the prepaymentByAccountReportRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentByAccountReportRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-by-account-report-requisitions/{id}")
    public ResponseEntity<PrepaymentByAccountReportRequisitionDTO> getPrepaymentByAccountReportRequisition(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentByAccountReportRequisition : {}", id);
        Optional<PrepaymentByAccountReportRequisitionDTO> prepaymentByAccountReportRequisitionDTO = prepaymentByAccountReportRequisitionService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(prepaymentByAccountReportRequisitionDTO);
    }

    /**
     * {@code DELETE  /prepayment-by-account-report-requisitions/:id} : delete the "id" prepaymentByAccountReportRequisition.
     *
     * @param id the id of the prepaymentByAccountReportRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prepayment-by-account-report-requisitions/{id}")
    public ResponseEntity<Void> deletePrepaymentByAccountReportRequisition(@PathVariable Long id) {
        log.debug("REST request to delete PrepaymentByAccountReportRequisition : {}", id);
        prepaymentByAccountReportRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/prepayment-by-account-report-requisitions?query=:query} : search for the prepaymentByAccountReportRequisition corresponding
     * to the query.
     *
     * @param query the query of the prepaymentByAccountReportRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-by-account-report-requisitions")
    public ResponseEntity<List<PrepaymentByAccountReportRequisitionDTO>> searchPrepaymentByAccountReportRequisitions(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of PrepaymentByAccountReportRequisitions for query {}", query);
        Page<PrepaymentByAccountReportRequisitionDTO> page = prepaymentByAccountReportRequisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
