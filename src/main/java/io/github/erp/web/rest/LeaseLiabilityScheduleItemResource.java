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

import io.github.erp.repository.LeaseLiabilityScheduleItemRepository;
import io.github.erp.service.LeaseLiabilityScheduleItemQueryService;
import io.github.erp.service.LeaseLiabilityScheduleItemService;
import io.github.erp.service.criteria.LeaseLiabilityScheduleItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityScheduleItemDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseLiabilityScheduleItem}.
 */
@RestController
@RequestMapping("/api")
public class LeaseLiabilityScheduleItemResource {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleItemResource.class);

    private static final String ENTITY_NAME = "leaseLiabilityScheduleItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseLiabilityScheduleItemService leaseLiabilityScheduleItemService;

    private final LeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository;

    private final LeaseLiabilityScheduleItemQueryService leaseLiabilityScheduleItemQueryService;

    public LeaseLiabilityScheduleItemResource(
        LeaseLiabilityScheduleItemService leaseLiabilityScheduleItemService,
        LeaseLiabilityScheduleItemRepository leaseLiabilityScheduleItemRepository,
        LeaseLiabilityScheduleItemQueryService leaseLiabilityScheduleItemQueryService
    ) {
        this.leaseLiabilityScheduleItemService = leaseLiabilityScheduleItemService;
        this.leaseLiabilityScheduleItemRepository = leaseLiabilityScheduleItemRepository;
        this.leaseLiabilityScheduleItemQueryService = leaseLiabilityScheduleItemQueryService;
    }

    /**
     * {@code POST  /lease-liability-schedule-items} : Create a new leaseLiabilityScheduleItem.
     *
     * @param leaseLiabilityScheduleItemDTO the leaseLiabilityScheduleItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseLiabilityScheduleItemDTO, or with status {@code 400 (Bad Request)} if the leaseLiabilityScheduleItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-liability-schedule-items")
    public ResponseEntity<LeaseLiabilityScheduleItemDTO> createLeaseLiabilityScheduleItem(
        @Valid @RequestBody LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to save LeaseLiabilityScheduleItem : {}", leaseLiabilityScheduleItemDTO);
        if (leaseLiabilityScheduleItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseLiabilityScheduleItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseLiabilityScheduleItemDTO result = leaseLiabilityScheduleItemService.save(leaseLiabilityScheduleItemDTO);
        return ResponseEntity
            .created(new URI("/api/lease-liability-schedule-items/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-liability-schedule-items/:id} : Updates an existing leaseLiabilityScheduleItem.
     *
     * @param id the id of the leaseLiabilityScheduleItemDTO to save.
     * @param leaseLiabilityScheduleItemDTO the leaseLiabilityScheduleItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityScheduleItemDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityScheduleItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityScheduleItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-liability-schedule-items/{id}")
    public ResponseEntity<LeaseLiabilityScheduleItemDTO> updateLeaseLiabilityScheduleItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseLiabilityScheduleItem : {}, {}", id, leaseLiabilityScheduleItemDTO);
        if (leaseLiabilityScheduleItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityScheduleItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityScheduleItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseLiabilityScheduleItemDTO result = leaseLiabilityScheduleItemService.save(leaseLiabilityScheduleItemDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityScheduleItemDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /lease-liability-schedule-items/:id} : Partial updates given fields of an existing leaseLiabilityScheduleItem, field will ignore if it is null
     *
     * @param id the id of the leaseLiabilityScheduleItemDTO to save.
     * @param leaseLiabilityScheduleItemDTO the leaseLiabilityScheduleItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseLiabilityScheduleItemDTO,
     * or with status {@code 400 (Bad Request)} if the leaseLiabilityScheduleItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseLiabilityScheduleItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseLiabilityScheduleItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-liability-schedule-items/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LeaseLiabilityScheduleItemDTO> partialUpdateLeaseLiabilityScheduleItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseLiabilityScheduleItemDTO leaseLiabilityScheduleItemDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseLiabilityScheduleItem partially : {}, {}", id, leaseLiabilityScheduleItemDTO);
        if (leaseLiabilityScheduleItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseLiabilityScheduleItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseLiabilityScheduleItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseLiabilityScheduleItemDTO> result = leaseLiabilityScheduleItemService.partialUpdate(leaseLiabilityScheduleItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseLiabilityScheduleItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-liability-schedule-items} : get all the leaseLiabilityScheduleItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseLiabilityScheduleItems in body.
     */
    @GetMapping("/lease-liability-schedule-items")
    public ResponseEntity<List<LeaseLiabilityScheduleItemDTO>> getAllLeaseLiabilityScheduleItems(
        LeaseLiabilityScheduleItemCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get LeaseLiabilityScheduleItems by criteria: {}", criteria);
        Page<LeaseLiabilityScheduleItemDTO> page = leaseLiabilityScheduleItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-liability-schedule-items/count} : count all the leaseLiabilityScheduleItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-liability-schedule-items/count")
    public ResponseEntity<Long> countLeaseLiabilityScheduleItems(LeaseLiabilityScheduleItemCriteria criteria) {
        log.debug("REST request to count LeaseLiabilityScheduleItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseLiabilityScheduleItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-liability-schedule-items/:id} : get the "id" leaseLiabilityScheduleItem.
     *
     * @param id the id of the leaseLiabilityScheduleItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseLiabilityScheduleItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-liability-schedule-items/{id}")
    public ResponseEntity<LeaseLiabilityScheduleItemDTO> getLeaseLiabilityScheduleItem(@PathVariable Long id) {
        log.debug("REST request to get LeaseLiabilityScheduleItem : {}", id);
        Optional<LeaseLiabilityScheduleItemDTO> leaseLiabilityScheduleItemDTO = leaseLiabilityScheduleItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseLiabilityScheduleItemDTO);
    }

    /**
     * {@code DELETE  /lease-liability-schedule-items/:id} : delete the "id" leaseLiabilityScheduleItem.
     *
     * @param id the id of the leaseLiabilityScheduleItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-liability-schedule-items/{id}")
    public ResponseEntity<Void> deleteLeaseLiabilityScheduleItem(@PathVariable Long id) {
        log.debug("REST request to delete LeaseLiabilityScheduleItem : {}", id);
        leaseLiabilityScheduleItemService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-liability-schedule-items?query=:query} : search for the leaseLiabilityScheduleItem corresponding
     * to the query.
     *
     * @param query the query of the leaseLiabilityScheduleItem search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-liability-schedule-items")
    public ResponseEntity<List<LeaseLiabilityScheduleItemDTO>> searchLeaseLiabilityScheduleItems(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of LeaseLiabilityScheduleItems for query {}", query);
        Page<LeaseLiabilityScheduleItemDTO> page = leaseLiabilityScheduleItemService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
