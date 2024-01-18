package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.repository.WeeklyCounterfeitHoldingRepository;
import io.github.erp.service.WeeklyCounterfeitHoldingQueryService;
import io.github.erp.service.WeeklyCounterfeitHoldingService;
import io.github.erp.service.criteria.WeeklyCounterfeitHoldingCriteria;
import io.github.erp.service.dto.WeeklyCounterfeitHoldingDTO;
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
 * REST controller for managing {@link io.github.erp.domain.WeeklyCounterfeitHolding}.
 */
@RestController
@RequestMapping("/api")
public class WeeklyCounterfeitHoldingResource {

    private final Logger log = LoggerFactory.getLogger(WeeklyCounterfeitHoldingResource.class);

    private static final String ENTITY_NAME = "gdiDataWeeklyCounterfeitHolding";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeeklyCounterfeitHoldingService weeklyCounterfeitHoldingService;

    private final WeeklyCounterfeitHoldingRepository weeklyCounterfeitHoldingRepository;

    private final WeeklyCounterfeitHoldingQueryService weeklyCounterfeitHoldingQueryService;

    public WeeklyCounterfeitHoldingResource(
        WeeklyCounterfeitHoldingService weeklyCounterfeitHoldingService,
        WeeklyCounterfeitHoldingRepository weeklyCounterfeitHoldingRepository,
        WeeklyCounterfeitHoldingQueryService weeklyCounterfeitHoldingQueryService
    ) {
        this.weeklyCounterfeitHoldingService = weeklyCounterfeitHoldingService;
        this.weeklyCounterfeitHoldingRepository = weeklyCounterfeitHoldingRepository;
        this.weeklyCounterfeitHoldingQueryService = weeklyCounterfeitHoldingQueryService;
    }

    /**
     * {@code POST  /weekly-counterfeit-holdings} : Create a new weeklyCounterfeitHolding.
     *
     * @param weeklyCounterfeitHoldingDTO the weeklyCounterfeitHoldingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weeklyCounterfeitHoldingDTO, or with status {@code 400 (Bad Request)} if the weeklyCounterfeitHolding has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weekly-counterfeit-holdings")
    public ResponseEntity<WeeklyCounterfeitHoldingDTO> createWeeklyCounterfeitHolding(
        @Valid @RequestBody WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO
    ) throws URISyntaxException {
        log.debug("REST request to save WeeklyCounterfeitHolding : {}", weeklyCounterfeitHoldingDTO);
        if (weeklyCounterfeitHoldingDTO.getId() != null) {
            throw new BadRequestAlertException("A new weeklyCounterfeitHolding cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeeklyCounterfeitHoldingDTO result = weeklyCounterfeitHoldingService.save(weeklyCounterfeitHoldingDTO);
        return ResponseEntity
            .created(new URI("/api/weekly-counterfeit-holdings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weekly-counterfeit-holdings/:id} : Updates an existing weeklyCounterfeitHolding.
     *
     * @param id the id of the weeklyCounterfeitHoldingDTO to save.
     * @param weeklyCounterfeitHoldingDTO the weeklyCounterfeitHoldingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weeklyCounterfeitHoldingDTO,
     * or with status {@code 400 (Bad Request)} if the weeklyCounterfeitHoldingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weeklyCounterfeitHoldingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weekly-counterfeit-holdings/{id}")
    public ResponseEntity<WeeklyCounterfeitHoldingDTO> updateWeeklyCounterfeitHolding(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WeeklyCounterfeitHolding : {}, {}", id, weeklyCounterfeitHoldingDTO);
        if (weeklyCounterfeitHoldingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weeklyCounterfeitHoldingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weeklyCounterfeitHoldingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WeeklyCounterfeitHoldingDTO result = weeklyCounterfeitHoldingService.save(weeklyCounterfeitHoldingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weeklyCounterfeitHoldingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /weekly-counterfeit-holdings/:id} : Partial updates given fields of an existing weeklyCounterfeitHolding, field will ignore if it is null
     *
     * @param id the id of the weeklyCounterfeitHoldingDTO to save.
     * @param weeklyCounterfeitHoldingDTO the weeklyCounterfeitHoldingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weeklyCounterfeitHoldingDTO,
     * or with status {@code 400 (Bad Request)} if the weeklyCounterfeitHoldingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the weeklyCounterfeitHoldingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the weeklyCounterfeitHoldingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/weekly-counterfeit-holdings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WeeklyCounterfeitHoldingDTO> partialUpdateWeeklyCounterfeitHolding(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WeeklyCounterfeitHoldingDTO weeklyCounterfeitHoldingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WeeklyCounterfeitHolding partially : {}, {}", id, weeklyCounterfeitHoldingDTO);
        if (weeklyCounterfeitHoldingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weeklyCounterfeitHoldingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weeklyCounterfeitHoldingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeeklyCounterfeitHoldingDTO> result = weeklyCounterfeitHoldingService.partialUpdate(weeklyCounterfeitHoldingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weeklyCounterfeitHoldingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /weekly-counterfeit-holdings} : get all the weeklyCounterfeitHoldings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weeklyCounterfeitHoldings in body.
     */
    @GetMapping("/weekly-counterfeit-holdings")
    public ResponseEntity<List<WeeklyCounterfeitHoldingDTO>> getAllWeeklyCounterfeitHoldings(
        WeeklyCounterfeitHoldingCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get WeeklyCounterfeitHoldings by criteria: {}", criteria);
        Page<WeeklyCounterfeitHoldingDTO> page = weeklyCounterfeitHoldingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /weekly-counterfeit-holdings/count} : count all the weeklyCounterfeitHoldings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/weekly-counterfeit-holdings/count")
    public ResponseEntity<Long> countWeeklyCounterfeitHoldings(WeeklyCounterfeitHoldingCriteria criteria) {
        log.debug("REST request to count WeeklyCounterfeitHoldings by criteria: {}", criteria);
        return ResponseEntity.ok().body(weeklyCounterfeitHoldingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /weekly-counterfeit-holdings/:id} : get the "id" weeklyCounterfeitHolding.
     *
     * @param id the id of the weeklyCounterfeitHoldingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weeklyCounterfeitHoldingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weekly-counterfeit-holdings/{id}")
    public ResponseEntity<WeeklyCounterfeitHoldingDTO> getWeeklyCounterfeitHolding(@PathVariable Long id) {
        log.debug("REST request to get WeeklyCounterfeitHolding : {}", id);
        Optional<WeeklyCounterfeitHoldingDTO> weeklyCounterfeitHoldingDTO = weeklyCounterfeitHoldingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weeklyCounterfeitHoldingDTO);
    }

    /**
     * {@code DELETE  /weekly-counterfeit-holdings/:id} : delete the "id" weeklyCounterfeitHolding.
     *
     * @param id the id of the weeklyCounterfeitHoldingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weekly-counterfeit-holdings/{id}")
    public ResponseEntity<Void> deleteWeeklyCounterfeitHolding(@PathVariable Long id) {
        log.debug("REST request to delete WeeklyCounterfeitHolding : {}", id);
        weeklyCounterfeitHoldingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/weekly-counterfeit-holdings?query=:query} : search for the weeklyCounterfeitHolding corresponding
     * to the query.
     *
     * @param query the query of the weeklyCounterfeitHolding search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/weekly-counterfeit-holdings")
    public ResponseEntity<List<WeeklyCounterfeitHoldingDTO>> searchWeeklyCounterfeitHoldings(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of WeeklyCounterfeitHoldings for query {}", query);
        Page<WeeklyCounterfeitHoldingDTO> page = weeklyCounterfeitHoldingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
