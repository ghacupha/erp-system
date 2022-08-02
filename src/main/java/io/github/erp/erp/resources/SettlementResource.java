package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 23 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.repository.SettlementRepository;
import io.github.erp.service.SettlementQueryService;
import io.github.erp.service.SettlementService;
import io.github.erp.service.criteria.SettlementCriteria;
import io.github.erp.service.dto.SettlementDTO;
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
 * REST controller for managing {@link io.github.erp.domain.Settlement}.
 */
@RestController
@RequestMapping("/api/payments")
public class SettlementResource {

    private final Logger log = LoggerFactory.getLogger(SettlementResource.class);

    private static final String ENTITY_NAME = "settlement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SettlementService settlementService;

    private final SettlementRepository settlementRepository;

    private final SettlementQueryService settlementQueryService;

    public SettlementResource(
        SettlementService settlementService,
        SettlementRepository settlementRepository,
        SettlementQueryService settlementQueryService
    ) {
        this.settlementService = settlementService;
        this.settlementRepository = settlementRepository;
        this.settlementQueryService = settlementQueryService;
    }

    /**
     * {@code POST  /settlements} : Create a new settlement.
     *
     * @param settlementDTO the settlementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new settlementDTO, or with status {@code 400 (Bad Request)} if the settlement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/settlements")
    public ResponseEntity<SettlementDTO> createSettlement(@Valid @RequestBody SettlementDTO settlementDTO) throws URISyntaxException {
        log.debug("REST request to save Settlement : {}", settlementDTO);
        if (settlementDTO.getId() != null) {
            throw new BadRequestAlertException("A new settlement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SettlementDTO result = settlementService.save(settlementDTO);
        return ResponseEntity
            .created(new URI("/api/settlements/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /settlements/:id} : Updates an existing settlement.
     *
     * @param id the id of the settlementDTO to save.
     * @param settlementDTO the settlementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settlementDTO,
     * or with status {@code 400 (Bad Request)} if the settlementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the settlementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/settlements/{id}")
    public ResponseEntity<SettlementDTO> updateSettlement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SettlementDTO settlementDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Settlement : {}, {}", id, settlementDTO);
        if (settlementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settlementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settlementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SettlementDTO result = settlementService.save(settlementDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, settlementDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /settlements/:id} : Partial updates given fields of an existing settlement, field will ignore if it is null
     *
     * @param id the id of the settlementDTO to save.
     * @param settlementDTO the settlementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated settlementDTO,
     * or with status {@code 400 (Bad Request)} if the settlementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the settlementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the settlementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/settlements/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SettlementDTO> partialUpdateSettlement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SettlementDTO settlementDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Settlement partially : {}, {}", id, settlementDTO);
        if (settlementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, settlementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!settlementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SettlementDTO> result = settlementService.partialUpdate(settlementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, settlementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /settlements} : get all the settlements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of settlements in body.
     */
    @GetMapping("/settlements")
    public ResponseEntity<List<SettlementDTO>> getAllSettlements(SettlementCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Settlements by criteria: {}", criteria);
        Page<SettlementDTO> page = settlementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /settlements/count} : count all the settlements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/settlements/count")
    public ResponseEntity<Long> countSettlements(SettlementCriteria criteria) {
        log.debug("REST request to count Settlements by criteria: {}", criteria);
        return ResponseEntity.ok().body(settlementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /settlements/:id} : get the "id" settlement.
     *
     * @param id the id of the settlementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the settlementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/settlements/{id}")
    public ResponseEntity<SettlementDTO> getSettlement(@PathVariable Long id) {
        log.debug("REST request to get Settlement : {}", id);
        Optional<SettlementDTO> settlementDTO = settlementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(settlementDTO);
    }

    /**
     * {@code DELETE  /settlements/:id} : delete the "id" settlement.
     *
     * @param id the id of the settlementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/settlements/{id}")
    public ResponseEntity<Void> deleteSettlement(@PathVariable Long id) {
        log.debug("REST request to delete Settlement : {}", id);
        settlementService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/settlements?query=:query} : search for the settlement corresponding
     * to the query.
     *
     * @param query the query of the settlement search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/settlements")
    public ResponseEntity<List<SettlementDTO>> searchSettlements(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of Settlements for query {}", query);
        Page<SettlementDTO> page = settlementService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
