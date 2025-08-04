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

import io.github.erp.repository.SettlementGroupRepository;
import io.github.erp.service.SettlementGroupQueryService;
import io.github.erp.service.SettlementGroupService;
import io.github.erp.service.criteria.SettlementGroupCriteria;
import io.github.erp.service.dto.SettlementGroupDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link io.github.erp.domain.SettlementGroup}.
 */
@RestController
@RequestMapping("/api")
public class SettlementGroupResource {

    private final Logger log = LoggerFactory.getLogger(SettlementGroupResource.class);

    private static final String ENTITY_NAME = "settlementGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettlementGroupService settlementGroupService;

    private final SettlementGroupRepository settlementGroupRepository;

    private final SettlementGroupQueryService settlementGroupQueryService;

    public SettlementGroupResource(
        SettlementGroupService settlementGroupService,
        SettlementGroupRepository settlementGroupRepository,
        SettlementGroupQueryService settlementGroupQueryService
    ) {
        this.settlementGroupService = settlementGroupService;
        this.settlementGroupRepository = settlementGroupRepository;
        this.settlementGroupQueryService = settlementGroupQueryService;
    }

    /**
     * {@code POST  /settlement-groups} : Create a new settlementGroup.
     *
     * @param settlementGroupDTO the settlementGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new settlementGroupDTO, or with status {@code 400 (Bad Request)} if the settlementGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/settlement-groups")
    public ResponseEntity<SettlementGroupDTO> createSettlementGroup(@Valid @RequestBody SettlementGroupDTO settlementGroupDTO)
        throws URISyntaxException {
        log.debug("REST request to save SettlementGroup : {}", settlementGroupDTO);
        if (settlementGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new settlementGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SettlementGroupDTO result = settlementGroupService.save(settlementGroupDTO);
        return ResponseEntity
            .created(new URI("/api/settlement-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /settlement-groups/:id} : Updates an existing settlementGroup.
     *
     * @param id the id of the settlementGroupDTO to save.
     * @param settlementGroupDTO the settlementGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settlementGroupDTO,
     * or with status {@code 400 (Bad Request)} if the settlementGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the settlementGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/settlement-groups/{id}")
    public ResponseEntity<SettlementGroupDTO> updateSettlementGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SettlementGroupDTO settlementGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SettlementGroup : {}, {}", id, settlementGroupDTO);
        if (settlementGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settlementGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settlementGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SettlementGroupDTO result = settlementGroupService.update(settlementGroupDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, settlementGroupDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /settlement-groups/:id} : Partial updates given fields of an existing settlementGroup, field will ignore if it is null
     *
     * @param id the id of the settlementGroupDTO to save.
     * @param settlementGroupDTO the settlementGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settlementGroupDTO,
     * or with status {@code 400 (Bad Request)} if the settlementGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the settlementGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the settlementGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/settlement-groups/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SettlementGroupDTO> partialUpdateSettlementGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SettlementGroupDTO settlementGroupDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SettlementGroup partially : {}, {}", id, settlementGroupDTO);
        if (settlementGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settlementGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settlementGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SettlementGroupDTO> result = settlementGroupService.partialUpdate(settlementGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, settlementGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /settlement-groups} : get all the settlementGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of settlementGroups in body.
     */
    @GetMapping("/settlement-groups")
    public ResponseEntity<List<SettlementGroupDTO>> getAllSettlementGroups(SettlementGroupCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SettlementGroups by criteria: {}", criteria);
        Page<SettlementGroupDTO> page = settlementGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /settlement-groups/count} : count all the settlementGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/settlement-groups/count")
    public ResponseEntity<Long> countSettlementGroups(SettlementGroupCriteria criteria) {
        log.debug("REST request to count SettlementGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(settlementGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /settlement-groups/:id} : get the "id" settlementGroup.
     *
     * @param id the id of the settlementGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the settlementGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/settlement-groups/{id}")
    public ResponseEntity<SettlementGroupDTO> getSettlementGroup(@PathVariable Long id) {
        log.debug("REST request to get SettlementGroup : {}", id);
        Optional<SettlementGroupDTO> settlementGroupDTO = settlementGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(settlementGroupDTO);
    }

    /**
     * {@code DELETE  /settlement-groups/:id} : delete the "id" settlementGroup.
     *
     * @param id the id of the settlementGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (No Content)}.
     */
    @DeleteMapping("/settlement-groups/{id}")
    public ResponseEntity<Void> deleteSettlementGroup(@PathVariable Long id) {
        log.debug("REST request to delete SettlementGroup : {}", id);
        settlementGroupService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/settlement-groups?query=:query} : search for the settlementGroup corresponding
     * to the query.
     *
     * @param query the query of the settlementGroup search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/settlement-groups")
    public ResponseEntity<List<SettlementGroupDTO>> searchSettlementGroups(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SettlementGroups for query {}", query);
        Page<SettlementGroupDTO> page = settlementGroupService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
