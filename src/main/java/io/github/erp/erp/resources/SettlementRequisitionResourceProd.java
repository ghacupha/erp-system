package io.github.erp.erp.resources;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.repository.SettlementRequisitionRepository;
import io.github.erp.service.SettlementRequisitionQueryService;
import io.github.erp.service.SettlementRequisitionService;
import io.github.erp.service.criteria.SettlementRequisitionCriteria;
import io.github.erp.service.dto.SettlementRequisitionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SettlementRequisition}.
 */
@RestController("settlementRequisitionResourceProd")
@RequestMapping("/api/requisition")
public class SettlementRequisitionResourceProd {

    private final Logger log = LoggerFactory.getLogger(SettlementRequisitionResourceProd.class);

    private static final String ENTITY_NAME = "settlementRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettlementRequisitionService settlementRequisitionService;

    private final SettlementRequisitionRepository settlementRequisitionRepository;

    private final SettlementRequisitionQueryService settlementRequisitionQueryService;

    // private final SettlementRequisitionReIndexingService reIndexerService;

    public SettlementRequisitionResourceProd(
        SettlementRequisitionService settlementRequisitionService,
        SettlementRequisitionRepository settlementRequisitionRepository,
        SettlementRequisitionQueryService settlementRequisitionQueryService) {
        this.settlementRequisitionService = settlementRequisitionService;
        this.settlementRequisitionRepository = settlementRequisitionRepository;
        this.settlementRequisitionQueryService = settlementRequisitionQueryService;
    }

    /**
     * {@code POST  /settlement-requisitions} : Create a new settlementRequisition.
     *
     * @param settlementRequisitionDTO the settlementRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new settlementRequisitionDTO, or with status {@code 400 (Bad Request)} if the settlementRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/settlement-requisitions")
    public ResponseEntity<SettlementRequisitionDTO> createSettlementRequisition(
        @Valid @RequestBody SettlementRequisitionDTO settlementRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SettlementRequisition : {}", settlementRequisitionDTO);
        if (settlementRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new settlementRequisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SettlementRequisitionDTO result = settlementRequisitionService.save(settlementRequisitionDTO);
        return ResponseEntity
            .created(new URI("/api/settlement-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /settlement-requisitions/:id} : Updates an existing settlementRequisition.
     *
     * @param id the id of the settlementRequisitionDTO to save.
     * @param settlementRequisitionDTO the settlementRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settlementRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the settlementRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the settlementRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/settlement-requisitions/{id}")
    public ResponseEntity<SettlementRequisitionDTO> updateSettlementRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SettlementRequisitionDTO settlementRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SettlementRequisition : {}, {}", id, settlementRequisitionDTO);
        if (settlementRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settlementRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settlementRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SettlementRequisitionDTO result = settlementRequisitionService.save(settlementRequisitionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, settlementRequisitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /settlement-requisitions/:id} : Partial updates given fields of an existing settlementRequisition, field will ignore if it is null
     *
     * @param id the id of the settlementRequisitionDTO to save.
     * @param settlementRequisitionDTO the settlementRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settlementRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the settlementRequisitionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the settlementRequisitionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the settlementRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/settlement-requisitions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SettlementRequisitionDTO> partialUpdateSettlementRequisition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SettlementRequisitionDTO settlementRequisitionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SettlementRequisition partially : {}, {}", id, settlementRequisitionDTO);
        if (settlementRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settlementRequisitionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settlementRequisitionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SettlementRequisitionDTO> result = settlementRequisitionService.partialUpdate(settlementRequisitionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, settlementRequisitionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /settlement-requisitions} : get all the settlementRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of settlementRequisitions in body.
     */
    @GetMapping("/settlement-requisitions")
    public ResponseEntity<List<SettlementRequisitionDTO>> getAllSettlementRequisitions(
        SettlementRequisitionCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get SettlementRequisitions by criteria: {}", criteria);
        Page<SettlementRequisitionDTO> page = settlementRequisitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /settlement-requisitions/count} : count all the settlementRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/settlement-requisitions/count")
    public ResponseEntity<Long> countSettlementRequisitions(SettlementRequisitionCriteria criteria) {
        log.debug("REST request to count SettlementRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(settlementRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /settlement-requisitions/:id} : get the "id" settlementRequisition.
     *
     * @param id the id of the settlementRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the settlementRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/settlement-requisitions/{id}")
    public ResponseEntity<SettlementRequisitionDTO> getSettlementRequisition(@PathVariable Long id) {
        log.debug("REST request to get SettlementRequisition : {}", id);
        Optional<SettlementRequisitionDTO> settlementRequisitionDTO = settlementRequisitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(settlementRequisitionDTO);
    }

    /**
     * {@code DELETE  /settlement-requisitions/:id} : delete the "id" settlementRequisition.
     *
     * @param id the id of the settlementRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/settlement-requisitions/{id}")
    public ResponseEntity<Void> deleteSettlementRequisition(@PathVariable Long id) {
        log.debug("REST request to delete SettlementRequisition : {}", id);
        settlementRequisitionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/settlement-requisitions?query=:query} : search for the settlementRequisition corresponding
     * to the query.
     *
     * @param query the query of the settlementRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/settlement-requisitions")
    public ResponseEntity<List<SettlementRequisitionDTO>> searchSettlementRequisitions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SettlementRequisitions for query {}", query);
        Page<SettlementRequisitionDTO> page = settlementRequisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
