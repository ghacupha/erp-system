package io.github.erp.erp.resources.gdi;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.repository.CounterpartyTypeRepository;
import io.github.erp.service.CounterpartyTypeQueryService;
import io.github.erp.service.CounterpartyTypeService;
import io.github.erp.service.criteria.CounterpartyTypeCriteria;
import io.github.erp.service.dto.CounterpartyTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CounterpartyType}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CounterpartyTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(CounterpartyTypeResourceProd.class);

    private static final String ENTITY_NAME = "counterpartyType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CounterpartyTypeService counterpartyTypeService;

    private final CounterpartyTypeRepository counterpartyTypeRepository;

    private final CounterpartyTypeQueryService counterpartyTypeQueryService;

    public CounterpartyTypeResourceProd(
        CounterpartyTypeService counterpartyTypeService,
        CounterpartyTypeRepository counterpartyTypeRepository,
        CounterpartyTypeQueryService counterpartyTypeQueryService
    ) {
        this.counterpartyTypeService = counterpartyTypeService;
        this.counterpartyTypeRepository = counterpartyTypeRepository;
        this.counterpartyTypeQueryService = counterpartyTypeQueryService;
    }

    /**
     * {@code POST  /counterparty-types} : Create a new counterpartyType.
     *
     * @param counterpartyTypeDTO the counterpartyTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new counterpartyTypeDTO, or with status {@code 400 (Bad Request)} if the counterpartyType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/counterparty-types")
    public ResponseEntity<CounterpartyTypeDTO> createCounterpartyType(@Valid @RequestBody CounterpartyTypeDTO counterpartyTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CounterpartyType : {}", counterpartyTypeDTO);
        if (counterpartyTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new counterpartyType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CounterpartyTypeDTO result = counterpartyTypeService.save(counterpartyTypeDTO);
        return ResponseEntity
            .created(new URI("/api/counterparty-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /counterparty-types/:id} : Updates an existing counterpartyType.
     *
     * @param id the id of the counterpartyTypeDTO to save.
     * @param counterpartyTypeDTO the counterpartyTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterpartyTypeDTO,
     * or with status {@code 400 (Bad Request)} if the counterpartyTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the counterpartyTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/counterparty-types/{id}")
    public ResponseEntity<CounterpartyTypeDTO> updateCounterpartyType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CounterpartyTypeDTO counterpartyTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CounterpartyType : {}, {}", id, counterpartyTypeDTO);
        if (counterpartyTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counterpartyTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterpartyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CounterpartyTypeDTO result = counterpartyTypeService.save(counterpartyTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterpartyTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /counterparty-types/:id} : Partial updates given fields of an existing counterpartyType, field will ignore if it is null
     *
     * @param id the id of the counterpartyTypeDTO to save.
     * @param counterpartyTypeDTO the counterpartyTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated counterpartyTypeDTO,
     * or with status {@code 400 (Bad Request)} if the counterpartyTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the counterpartyTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the counterpartyTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/counterparty-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CounterpartyTypeDTO> partialUpdateCounterpartyType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CounterpartyTypeDTO counterpartyTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CounterpartyType partially : {}, {}", id, counterpartyTypeDTO);
        if (counterpartyTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, counterpartyTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!counterpartyTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CounterpartyTypeDTO> result = counterpartyTypeService.partialUpdate(counterpartyTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, counterpartyTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /counterparty-types} : get all the counterpartyTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of counterpartyTypes in body.
     */
    @GetMapping("/counterparty-types")
    public ResponseEntity<List<CounterpartyTypeDTO>> getAllCounterpartyTypes(CounterpartyTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CounterpartyTypes by criteria: {}", criteria);
        Page<CounterpartyTypeDTO> page = counterpartyTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /counterparty-types/count} : count all the counterpartyTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/counterparty-types/count")
    public ResponseEntity<Long> countCounterpartyTypes(CounterpartyTypeCriteria criteria) {
        log.debug("REST request to count CounterpartyTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(counterpartyTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /counterparty-types/:id} : get the "id" counterpartyType.
     *
     * @param id the id of the counterpartyTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the counterpartyTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/counterparty-types/{id}")
    public ResponseEntity<CounterpartyTypeDTO> getCounterpartyType(@PathVariable Long id) {
        log.debug("REST request to get CounterpartyType : {}", id);
        Optional<CounterpartyTypeDTO> counterpartyTypeDTO = counterpartyTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(counterpartyTypeDTO);
    }

    /**
     * {@code DELETE  /counterparty-types/:id} : delete the "id" counterpartyType.
     *
     * @param id the id of the counterpartyTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/counterparty-types/{id}")
    public ResponseEntity<Void> deleteCounterpartyType(@PathVariable Long id) {
        log.debug("REST request to delete CounterpartyType : {}", id);
        counterpartyTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/counterparty-types?query=:query} : search for the counterpartyType corresponding
     * to the query.
     *
     * @param query the query of the counterpartyType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/counterparty-types")
    public ResponseEntity<List<CounterpartyTypeDTO>> searchCounterpartyTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CounterpartyTypes for query {}", query);
        Page<CounterpartyTypeDTO> page = counterpartyTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
