package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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

import io.github.erp.repository.CounterPartyDealTypeRepository;
import io.github.erp.service.CounterPartyDealTypeQueryService;
import io.github.erp.service.CounterPartyDealTypeService;
import io.github.erp.service.criteria.CounterPartyDealTypeCriteria;
import io.github.erp.service.dto.CounterPartyDealTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CounterPartyDealType}.
 */
@RestController
@RequestMapping("/api")
public class CounterPartyDealTypeResource {

    private final Logger log = LoggerFactory.getLogger(CounterPartyDealTypeResource.class);

    private static final String ENTITY_NAME = "counterPartyDealType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CounterPartyDealTypeService counterPartyDealTypeService;

    private final CounterPartyDealTypeRepository counterPartyDealTypeRepository;

    private final CounterPartyDealTypeQueryService counterPartyDealTypeQueryService;

    public CounterPartyDealTypeResource(
        CounterPartyDealTypeService counterPartyDealTypeService,
        CounterPartyDealTypeRepository counterPartyDealTypeRepository,
        CounterPartyDealTypeQueryService counterPartyDealTypeQueryService
    ) {
        this.counterPartyDealTypeService = counterPartyDealTypeService;
        this.counterPartyDealTypeRepository = counterPartyDealTypeRepository;
        this.counterPartyDealTypeQueryService = counterPartyDealTypeQueryService;
    }

    /**
     * {@code POST  /counter-party-deal-types} : Create a new counterPartyDealType.
     *
     * @param counterPartyDealTypeDTO the counterPartyDealTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new counterPartyDealTypeDTO, or with status {@code 400 (Bad Request)} if the counterPartyDealType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/counter-party-deal-types")
    public ResponseEntity<CounterPartyDealTypeDTO> createCounterPartyDealType(
        @Valid @RequestBody CounterPartyDealTypeDTO counterPartyDealTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CounterPartyDealType : {}", counterPartyDealTypeDTO);
        if (counterPartyDealTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new counterPartyDealType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CounterPartyDealTypeDTO result = counterPartyDealTypeService.save(counterPartyDealTypeDTO);
        return ResponseEntity
            .created(new URI("/api/counter-party-deal-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /counter-party-deal-types/:id} : Updates an existing counterPartyDealType.
     *
     * @param id the id of the counterPartyDealTypeDTO to save.
     * @param counterPartyDealTypeDTO the counterPartyDealTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterPartyDealTypeDTO,
     * or with status {@code 400 (Bad Request)} if the counterPartyDealTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the counterPartyDealTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/counter-party-deal-types/{id}")
    public ResponseEntity<CounterPartyDealTypeDTO> updateCounterPartyDealType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CounterPartyDealTypeDTO counterPartyDealTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CounterPartyDealType : {}, {}", id, counterPartyDealTypeDTO);
        if (counterPartyDealTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counterPartyDealTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterPartyDealTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CounterPartyDealTypeDTO result = counterPartyDealTypeService.save(counterPartyDealTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterPartyDealTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /counter-party-deal-types/:id} : Partial updates given fields of an existing counterPartyDealType, field will ignore if it is null
     *
     * @param id the id of the counterPartyDealTypeDTO to save.
     * @param counterPartyDealTypeDTO the counterPartyDealTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterPartyDealTypeDTO,
     * or with status {@code 400 (Bad Request)} if the counterPartyDealTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the counterPartyDealTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the counterPartyDealTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/counter-party-deal-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CounterPartyDealTypeDTO> partialUpdateCounterPartyDealType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CounterPartyDealTypeDTO counterPartyDealTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CounterPartyDealType partially : {}, {}", id, counterPartyDealTypeDTO);
        if (counterPartyDealTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counterPartyDealTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterPartyDealTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CounterPartyDealTypeDTO> result = counterPartyDealTypeService.partialUpdate(counterPartyDealTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterPartyDealTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /counter-party-deal-types} : get all the counterPartyDealTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of counterPartyDealTypes in body.
     */
    @GetMapping("/counter-party-deal-types")
    public ResponseEntity<List<CounterPartyDealTypeDTO>> getAllCounterPartyDealTypes(
        CounterPartyDealTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CounterPartyDealTypes by criteria: {}", criteria);
        Page<CounterPartyDealTypeDTO> page = counterPartyDealTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /counter-party-deal-types/count} : count all the counterPartyDealTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/counter-party-deal-types/count")
    public ResponseEntity<Long> countCounterPartyDealTypes(CounterPartyDealTypeCriteria criteria) {
        log.debug("REST request to count CounterPartyDealTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(counterPartyDealTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /counter-party-deal-types/:id} : get the "id" counterPartyDealType.
     *
     * @param id the id of the counterPartyDealTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the counterPartyDealTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/counter-party-deal-types/{id}")
    public ResponseEntity<CounterPartyDealTypeDTO> getCounterPartyDealType(@PathVariable Long id) {
        log.debug("REST request to get CounterPartyDealType : {}", id);
        Optional<CounterPartyDealTypeDTO> counterPartyDealTypeDTO = counterPartyDealTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(counterPartyDealTypeDTO);
    }

    /**
     * {@code DELETE  /counter-party-deal-types/:id} : delete the "id" counterPartyDealType.
     *
     * @param id the id of the counterPartyDealTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/counter-party-deal-types/{id}")
    public ResponseEntity<Void> deleteCounterPartyDealType(@PathVariable Long id) {
        log.debug("REST request to delete CounterPartyDealType : {}", id);
        counterPartyDealTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/counter-party-deal-types?query=:query} : search for the counterPartyDealType corresponding
     * to the query.
     *
     * @param query the query of the counterPartyDealType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/counter-party-deal-types")
    public ResponseEntity<List<CounterPartyDealTypeDTO>> searchCounterPartyDealTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CounterPartyDealTypes for query {}", query);
        Page<CounterPartyDealTypeDTO> page = counterPartyDealTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
