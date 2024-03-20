package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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

import io.github.erp.repository.BusinessStampRepository;
import io.github.erp.service.BusinessStampQueryService;
import io.github.erp.service.BusinessStampService;
import io.github.erp.service.criteria.BusinessStampCriteria;
import io.github.erp.service.dto.BusinessStampDTO;
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
 * REST controller for managing {@link io.github.erp.domain.BusinessStamp}.
 */
@RestController
@RequestMapping("/api")
public class BusinessStampResource {

    private final Logger log = LoggerFactory.getLogger(BusinessStampResource.class);

    private static final String ENTITY_NAME = "businessStamp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessStampService businessStampService;

    private final BusinessStampRepository businessStampRepository;

    private final BusinessStampQueryService businessStampQueryService;

    public BusinessStampResource(
        BusinessStampService businessStampService,
        BusinessStampRepository businessStampRepository,
        BusinessStampQueryService businessStampQueryService
    ) {
        this.businessStampService = businessStampService;
        this.businessStampRepository = businessStampRepository;
        this.businessStampQueryService = businessStampQueryService;
    }

    /**
     * {@code POST  /business-stamps} : Create a new businessStamp.
     *
     * @param businessStampDTO the businessStampDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessStampDTO, or with status {@code 400 (Bad Request)} if the businessStamp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-stamps")
    public ResponseEntity<BusinessStampDTO> createBusinessStamp(@Valid @RequestBody BusinessStampDTO businessStampDTO)
        throws URISyntaxException {
        log.debug("REST request to save BusinessStamp : {}", businessStampDTO);
        if (businessStampDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessStamp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessStampDTO result = businessStampService.save(businessStampDTO);
        return ResponseEntity
            .created(new URI("/api/business-stamps/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-stamps/:id} : Updates an existing businessStamp.
     *
     * @param id the id of the businessStampDTO to save.
     * @param businessStampDTO the businessStampDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessStampDTO,
     * or with status {@code 400 (Bad Request)} if the businessStampDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessStampDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-stamps/{id}")
    public ResponseEntity<BusinessStampDTO> updateBusinessStamp(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessStampDTO businessStampDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessStamp : {}, {}", id, businessStampDTO);
        if (businessStampDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessStampDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessStampRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessStampDTO result = businessStampService.save(businessStampDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessStampDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-stamps/:id} : Partial updates given fields of an existing businessStamp, field will ignore if it is null
     *
     * @param id the id of the businessStampDTO to save.
     * @param businessStampDTO the businessStampDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessStampDTO,
     * or with status {@code 400 (Bad Request)} if the businessStampDTO is not valid,
     * or with status {@code 404 (Not Found)} if the businessStampDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessStampDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/business-stamps/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessStampDTO> partialUpdateBusinessStamp(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessStampDTO businessStampDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessStamp partially : {}, {}", id, businessStampDTO);
        if (businessStampDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessStampDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessStampRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessStampDTO> result = businessStampService.partialUpdate(businessStampDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessStampDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /business-stamps} : get all the businessStamps.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessStamps in body.
     */
    @GetMapping("/business-stamps")
    public ResponseEntity<List<BusinessStampDTO>> getAllBusinessStamps(BusinessStampCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BusinessStamps by criteria: {}", criteria);
        Page<BusinessStampDTO> page = businessStampQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-stamps/count} : count all the businessStamps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/business-stamps/count")
    public ResponseEntity<Long> countBusinessStamps(BusinessStampCriteria criteria) {
        log.debug("REST request to count BusinessStamps by criteria: {}", criteria);
        return ResponseEntity.ok().body(businessStampQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /business-stamps/:id} : get the "id" businessStamp.
     *
     * @param id the id of the businessStampDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessStampDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-stamps/{id}")
    public ResponseEntity<BusinessStampDTO> getBusinessStamp(@PathVariable Long id) {
        log.debug("REST request to get BusinessStamp : {}", id);
        Optional<BusinessStampDTO> businessStampDTO = businessStampService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessStampDTO);
    }

    /**
     * {@code DELETE  /business-stamps/:id} : delete the "id" businessStamp.
     *
     * @param id the id of the businessStampDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-stamps/{id}")
    public ResponseEntity<Void> deleteBusinessStamp(@PathVariable Long id) {
        log.debug("REST request to delete BusinessStamp : {}", id);
        businessStampService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/business-stamps?query=:query} : search for the businessStamp corresponding
     * to the query.
     *
     * @param query the query of the businessStamp search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/business-stamps")
    public ResponseEntity<List<BusinessStampDTO>> searchBusinessStamps(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BusinessStamps for query {}", query);
        Page<BusinessStampDTO> page = businessStampService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
