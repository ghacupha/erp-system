package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 26 (Baruch Series)
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
import io.github.erp.repository.PrepaymentMappingRepository;
import io.github.erp.service.PrepaymentMappingQueryService;
import io.github.erp.service.PrepaymentMappingService;
import io.github.erp.service.criteria.PrepaymentMappingCriteria;
import io.github.erp.service.dto.PrepaymentMappingDTO;
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
 * REST controller for managing {@link io.github.erp.domain.PrepaymentMapping}.
 */
@RestController
@RequestMapping("/api")
public class PrepaymentMappingResource {

    private final Logger log = LoggerFactory.getLogger(PrepaymentMappingResource.class);

    private static final String ENTITY_NAME = "prepaymentMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrepaymentMappingService prepaymentMappingService;

    private final PrepaymentMappingRepository prepaymentMappingRepository;

    private final PrepaymentMappingQueryService prepaymentMappingQueryService;

    public PrepaymentMappingResource(
        PrepaymentMappingService prepaymentMappingService,
        PrepaymentMappingRepository prepaymentMappingRepository,
        PrepaymentMappingQueryService prepaymentMappingQueryService
    ) {
        this.prepaymentMappingService = prepaymentMappingService;
        this.prepaymentMappingRepository = prepaymentMappingRepository;
        this.prepaymentMappingQueryService = prepaymentMappingQueryService;
    }

    /**
     * {@code POST  /prepayment-mappings} : Create a new prepaymentMapping.
     *
     * @param prepaymentMappingDTO the prepaymentMappingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prepaymentMappingDTO, or with status {@code 400 (Bad Request)} if the prepaymentMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prepayment-mappings")
    public ResponseEntity<PrepaymentMappingDTO> createPrepaymentMapping(@Valid @RequestBody PrepaymentMappingDTO prepaymentMappingDTO)
        throws URISyntaxException {
        log.debug("REST request to save PrepaymentMapping : {}", prepaymentMappingDTO);
        if (prepaymentMappingDTO.getId() != null) {
            throw new BadRequestAlertException("A new prepaymentMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrepaymentMappingDTO result = prepaymentMappingService.save(prepaymentMappingDTO);
        return ResponseEntity
            .created(new URI("/api/prepayment-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prepayment-mappings/:id} : Updates an existing prepaymentMapping.
     *
     * @param id the id of the prepaymentMappingDTO to save.
     * @param prepaymentMappingDTO the prepaymentMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentMappingDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentMappingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prepayment-mappings/{id}")
    public ResponseEntity<PrepaymentMappingDTO> updatePrepaymentMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrepaymentMappingDTO prepaymentMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrepaymentMapping : {}, {}", id, prepaymentMappingDTO);
        if (prepaymentMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrepaymentMappingDTO result = prepaymentMappingService.save(prepaymentMappingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prepaymentMappingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prepayment-mappings/:id} : Partial updates given fields of an existing prepaymentMapping, field will ignore if it is null
     *
     * @param id the id of the prepaymentMappingDTO to save.
     * @param prepaymentMappingDTO the prepaymentMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentMappingDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentMappingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prepaymentMappingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prepayment-mappings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrepaymentMappingDTO> partialUpdatePrepaymentMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrepaymentMappingDTO prepaymentMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrepaymentMapping partially : {}, {}", id, prepaymentMappingDTO);
        if (prepaymentMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrepaymentMappingDTO> result = prepaymentMappingService.partialUpdate(prepaymentMappingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prepaymentMappingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prepayment-mappings} : get all the prepaymentMappings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentMappings in body.
     */
    @GetMapping("/prepayment-mappings")
    public ResponseEntity<List<PrepaymentMappingDTO>> getAllPrepaymentMappings(PrepaymentMappingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PrepaymentMappings by criteria: {}", criteria);
        Page<PrepaymentMappingDTO> page = prepaymentMappingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-mappings/count} : count all the prepaymentMappings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-mappings/count")
    public ResponseEntity<Long> countPrepaymentMappings(PrepaymentMappingCriteria criteria) {
        log.debug("REST request to count PrepaymentMappings by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentMappingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-mappings/:id} : get the "id" prepaymentMapping.
     *
     * @param id the id of the prepaymentMappingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentMappingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-mappings/{id}")
    public ResponseEntity<PrepaymentMappingDTO> getPrepaymentMapping(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentMapping : {}", id);
        Optional<PrepaymentMappingDTO> prepaymentMappingDTO = prepaymentMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaymentMappingDTO);
    }

    /**
     * {@code DELETE  /prepayment-mappings/:id} : delete the "id" prepaymentMapping.
     *
     * @param id the id of the prepaymentMappingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prepayment-mappings/{id}")
    public ResponseEntity<Void> deletePrepaymentMapping(@PathVariable Long id) {
        log.debug("REST request to delete PrepaymentMapping : {}", id);
        prepaymentMappingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/prepayment-mappings?query=:query} : search for the prepaymentMapping corresponding
     * to the query.
     *
     * @param query the query of the prepaymentMapping search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-mappings")
    public ResponseEntity<List<PrepaymentMappingDTO>> searchPrepaymentMappings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PrepaymentMappings for query {}", query);
        Page<PrepaymentMappingDTO> page = prepaymentMappingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
