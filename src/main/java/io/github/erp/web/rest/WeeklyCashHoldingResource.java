package io.github.erp.web.rest;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.repository.WeeklyCashHoldingRepository;
import io.github.erp.service.WeeklyCashHoldingQueryService;
import io.github.erp.service.WeeklyCashHoldingService;
import io.github.erp.service.criteria.WeeklyCashHoldingCriteria;
import io.github.erp.service.dto.WeeklyCashHoldingDTO;
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
 * REST controller for managing {@link io.github.erp.domain.WeeklyCashHolding}.
 */
@RestController
@RequestMapping("/api")
public class WeeklyCashHoldingResource {

    private final Logger log = LoggerFactory.getLogger(WeeklyCashHoldingResource.class);

    private static final String ENTITY_NAME = "gdiDataWeeklyCashHolding";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WeeklyCashHoldingService weeklyCashHoldingService;

    private final WeeklyCashHoldingRepository weeklyCashHoldingRepository;

    private final WeeklyCashHoldingQueryService weeklyCashHoldingQueryService;

    public WeeklyCashHoldingResource(
        WeeklyCashHoldingService weeklyCashHoldingService,
        WeeklyCashHoldingRepository weeklyCashHoldingRepository,
        WeeklyCashHoldingQueryService weeklyCashHoldingQueryService
    ) {
        this.weeklyCashHoldingService = weeklyCashHoldingService;
        this.weeklyCashHoldingRepository = weeklyCashHoldingRepository;
        this.weeklyCashHoldingQueryService = weeklyCashHoldingQueryService;
    }

    /**
     * {@code POST  /weekly-cash-holdings} : Create a new weeklyCashHolding.
     *
     * @param weeklyCashHoldingDTO the weeklyCashHoldingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new weeklyCashHoldingDTO, or with status {@code 400 (Bad Request)} if the weeklyCashHolding has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/weekly-cash-holdings")
    public ResponseEntity<WeeklyCashHoldingDTO> createWeeklyCashHolding(@Valid @RequestBody WeeklyCashHoldingDTO weeklyCashHoldingDTO)
        throws URISyntaxException {
        log.debug("REST request to save WeeklyCashHolding : {}", weeklyCashHoldingDTO);
        if (weeklyCashHoldingDTO.getId() != null) {
            throw new BadRequestAlertException("A new weeklyCashHolding cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WeeklyCashHoldingDTO result = weeklyCashHoldingService.save(weeklyCashHoldingDTO);
        return ResponseEntity
            .created(new URI("/api/weekly-cash-holdings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /weekly-cash-holdings/:id} : Updates an existing weeklyCashHolding.
     *
     * @param id the id of the weeklyCashHoldingDTO to save.
     * @param weeklyCashHoldingDTO the weeklyCashHoldingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weeklyCashHoldingDTO,
     * or with status {@code 400 (Bad Request)} if the weeklyCashHoldingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the weeklyCashHoldingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/weekly-cash-holdings/{id}")
    public ResponseEntity<WeeklyCashHoldingDTO> updateWeeklyCashHolding(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WeeklyCashHoldingDTO weeklyCashHoldingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update WeeklyCashHolding : {}, {}", id, weeklyCashHoldingDTO);
        if (weeklyCashHoldingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weeklyCashHoldingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weeklyCashHoldingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WeeklyCashHoldingDTO result = weeklyCashHoldingService.save(weeklyCashHoldingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weeklyCashHoldingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /weekly-cash-holdings/:id} : Partial updates given fields of an existing weeklyCashHolding, field will ignore if it is null
     *
     * @param id the id of the weeklyCashHoldingDTO to save.
     * @param weeklyCashHoldingDTO the weeklyCashHoldingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated weeklyCashHoldingDTO,
     * or with status {@code 400 (Bad Request)} if the weeklyCashHoldingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the weeklyCashHoldingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the weeklyCashHoldingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/weekly-cash-holdings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WeeklyCashHoldingDTO> partialUpdateWeeklyCashHolding(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WeeklyCashHoldingDTO weeklyCashHoldingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update WeeklyCashHolding partially : {}, {}", id, weeklyCashHoldingDTO);
        if (weeklyCashHoldingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, weeklyCashHoldingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!weeklyCashHoldingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WeeklyCashHoldingDTO> result = weeklyCashHoldingService.partialUpdate(weeklyCashHoldingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, weeklyCashHoldingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /weekly-cash-holdings} : get all the weeklyCashHoldings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of weeklyCashHoldings in body.
     */
    @GetMapping("/weekly-cash-holdings")
    public ResponseEntity<List<WeeklyCashHoldingDTO>> getAllWeeklyCashHoldings(WeeklyCashHoldingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get WeeklyCashHoldings by criteria: {}", criteria);
        Page<WeeklyCashHoldingDTO> page = weeklyCashHoldingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /weekly-cash-holdings/count} : count all the weeklyCashHoldings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/weekly-cash-holdings/count")
    public ResponseEntity<Long> countWeeklyCashHoldings(WeeklyCashHoldingCriteria criteria) {
        log.debug("REST request to count WeeklyCashHoldings by criteria: {}", criteria);
        return ResponseEntity.ok().body(weeklyCashHoldingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /weekly-cash-holdings/:id} : get the "id" weeklyCashHolding.
     *
     * @param id the id of the weeklyCashHoldingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the weeklyCashHoldingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/weekly-cash-holdings/{id}")
    public ResponseEntity<WeeklyCashHoldingDTO> getWeeklyCashHolding(@PathVariable Long id) {
        log.debug("REST request to get WeeklyCashHolding : {}", id);
        Optional<WeeklyCashHoldingDTO> weeklyCashHoldingDTO = weeklyCashHoldingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(weeklyCashHoldingDTO);
    }

    /**
     * {@code DELETE  /weekly-cash-holdings/:id} : delete the "id" weeklyCashHolding.
     *
     * @param id the id of the weeklyCashHoldingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/weekly-cash-holdings/{id}")
    public ResponseEntity<Void> deleteWeeklyCashHolding(@PathVariable Long id) {
        log.debug("REST request to delete WeeklyCashHolding : {}", id);
        weeklyCashHoldingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/weekly-cash-holdings?query=:query} : search for the weeklyCashHolding corresponding
     * to the query.
     *
     * @param query the query of the weeklyCashHolding search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/weekly-cash-holdings")
    public ResponseEntity<List<WeeklyCashHoldingDTO>> searchWeeklyCashHoldings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of WeeklyCashHoldings for query {}", query);
        Page<WeeklyCashHoldingDTO> page = weeklyCashHoldingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
