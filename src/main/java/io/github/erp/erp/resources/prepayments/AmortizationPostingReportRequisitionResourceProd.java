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
import io.github.erp.internal.service.prepayments.InternalAmortizationPostingReportRequisitionService;
import io.github.erp.repository.AmortizationPostingReportRequisitionRepository;
import io.github.erp.service.AmortizationPostingReportRequisitionQueryService;
import io.github.erp.service.criteria.AmortizationPostingReportRequisitionCriteria;
import io.github.erp.service.dto.AmortizationPostingReportRequisitionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AmortizationPostingReportRequisition}.
 */
@RestController
@RequestMapping("/api/prepayments")
public class AmortizationPostingReportRequisitionResourceProd {

    private final Logger log = LoggerFactory.getLogger(AmortizationPostingReportRequisitionResourceProd.class);

    private static final String ENTITY_NAME = "amortizationPostingReportRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalAmortizationPostingReportRequisitionService amortizationPostingReportRequisitionService;

    private final AmortizationPostingReportRequisitionRepository amortizationPostingReportRequisitionRepository;

    private final AmortizationPostingReportRequisitionQueryService amortizationPostingReportRequisitionQueryService;

    public AmortizationPostingReportRequisitionResourceProd(
        InternalAmortizationPostingReportRequisitionService amortizationPostingReportRequisitionService,
        AmortizationPostingReportRequisitionRepository amortizationPostingReportRequisitionRepository,
        AmortizationPostingReportRequisitionQueryService amortizationPostingReportRequisitionQueryService
    ) {
        this.amortizationPostingReportRequisitionService = amortizationPostingReportRequisitionService;
        this.amortizationPostingReportRequisitionRepository = amortizationPostingReportRequisitionRepository;
        this.amortizationPostingReportRequisitionQueryService = amortizationPostingReportRequisitionQueryService;
    }

    /**
     * {@code POST  /amortization-posting-report-requisitions} : Create a new amortizationPostingReportRequisition.
     *
     * @param amortizationPostingReportRequisitionDTO the amortizationPostingReportRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new amortizationPostingReportRequisitionDTO, or with status {@code 400 (Bad Request)} if the amortizationPostingReportRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/amortization-posting-report-requisitions")
    public ResponseEntity<AmortizationPostingReportRequisitionDTO> createAmortizationPostingReportRequisition(
        @Valid @RequestBody AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AmortizationPostingReportRequisition : {}", amortizationPostingReportRequisitionDTO);
        if (amortizationPostingReportRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new amortizationPostingReportRequisition cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        AmortizationPostingReportRequisitionDTO result = amortizationPostingReportRequisitionService.save(
            amortizationPostingReportRequisitionDTO
        );
        return ResponseEntity
            .created(new URI("/api/amortization-posting-report-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /amortization-posting-report-requisitions/:id} : Updates an existing amortizationPostingReportRequisition.
     *
     * @param id the id of the amortizationPostingReportRequisitionDTO to save.
     * @param amortizationPostingReportRequisitionDTO the amortizationPostingReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amortizationPostingReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the amortizationPostingReportRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the amortizationPostingReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/amortization-posting-report-requisitions/{id}")
    public ResponseEntity<AmortizationPostingReportRequisitionDTO> updateAmortizationPostingReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AmortizationPostingReportRequisition : {}, {}", id, amortizationPostingReportRequisitionDTO);
        if (amortizationPostingReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amortizationPostingReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amortizationPostingReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AmortizationPostingReportRequisitionDTO result = amortizationPostingReportRequisitionService.save(
            amortizationPostingReportRequisitionDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    amortizationPostingReportRequisitionDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /amortization-posting-report-requisitions/:id} : Partial updates given fields of an existing amortizationPostingReportRequisition, field will ignore if it is null
     *
     * @param id the id of the amortizationPostingReportRequisitionDTO to save.
     * @param amortizationPostingReportRequisitionDTO the amortizationPostingReportRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amortizationPostingReportRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the amortizationPostingReportRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the amortizationPostingReportRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the amortizationPostingReportRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(
        value = "/amortization-posting-report-requisitions/{id}",
        consumes = { "application/json", "application/merge-patch+json" }
    )
    public ResponseEntity<AmortizationPostingReportRequisitionDTO> partialUpdateAmortizationPostingReportRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AmortizationPostingReportRequisitionDTO amortizationPostingReportRequisitionDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update AmortizationPostingReportRequisition partially : {}, {}",
            id,
            amortizationPostingReportRequisitionDTO
        );
        if (amortizationPostingReportRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amortizationPostingReportRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amortizationPostingReportRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AmortizationPostingReportRequisitionDTO> result = amortizationPostingReportRequisitionService.partialUpdate(
            amortizationPostingReportRequisitionDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                true,
                ENTITY_NAME,
                amortizationPostingReportRequisitionDTO.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /amortization-posting-report-requisitions} : get all the amortizationPostingReportRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amortizationPostingReportRequisitions in body.
     */
    @GetMapping("/amortization-posting-report-requisitions")
    public ResponseEntity<List<AmortizationPostingReportRequisitionDTO>> getAllAmortizationPostingReportRequisitions(
        AmortizationPostingReportRequisitionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AmortizationPostingReportRequisitions by criteria: {}", criteria);
        Page<AmortizationPostingReportRequisitionDTO> page = amortizationPostingReportRequisitionQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /amortization-posting-report-requisitions/count} : count all the amortizationPostingReportRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/amortization-posting-report-requisitions/count")
    public ResponseEntity<Long> countAmortizationPostingReportRequisitions(AmortizationPostingReportRequisitionCriteria criteria) {
        log.debug("REST request to count AmortizationPostingReportRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(amortizationPostingReportRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /amortization-posting-report-requisitions/:id} : get the "id" amortizationPostingReportRequisition.
     *
     * @param id the id of the amortizationPostingReportRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amortizationPostingReportRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amortization-posting-report-requisitions/{id}")
    public ResponseEntity<AmortizationPostingReportRequisitionDTO> getAmortizationPostingReportRequisition(@PathVariable Long id) {
        log.debug("REST request to get AmortizationPostingReportRequisition : {}", id);
        Optional<AmortizationPostingReportRequisitionDTO> amortizationPostingReportRequisitionDTO = amortizationPostingReportRequisitionService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(amortizationPostingReportRequisitionDTO);
    }

    /**
     * {@code DELETE  /amortization-posting-report-requisitions/:id} : delete the "id" amortizationPostingReportRequisition.
     *
     * @param id the id of the amortizationPostingReportRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/amortization-posting-report-requisitions/{id}")
    public ResponseEntity<Void> deleteAmortizationPostingReportRequisition(@PathVariable Long id) {
        log.debug("REST request to delete AmortizationPostingReportRequisition : {}", id);
        amortizationPostingReportRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/amortization-posting-report-requisitions?query=:query} : search for the amortizationPostingReportRequisition corresponding
     * to the query.
     *
     * @param query the query of the amortizationPostingReportRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/amortization-posting-report-requisitions")
    public ResponseEntity<List<AmortizationPostingReportRequisitionDTO>> searchAmortizationPostingReportRequisitions(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of AmortizationPostingReportRequisitions for query {}", query);
        Page<AmortizationPostingReportRequisitionDTO> page = amortizationPostingReportRequisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
