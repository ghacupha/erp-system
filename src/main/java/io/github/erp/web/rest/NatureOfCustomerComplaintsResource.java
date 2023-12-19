package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.NatureOfCustomerComplaintsRepository;
import io.github.erp.service.NatureOfCustomerComplaintsQueryService;
import io.github.erp.service.NatureOfCustomerComplaintsService;
import io.github.erp.service.criteria.NatureOfCustomerComplaintsCriteria;
import io.github.erp.service.dto.NatureOfCustomerComplaintsDTO;
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
 * REST controller for managing {@link io.github.erp.domain.NatureOfCustomerComplaints}.
 */
@RestController
@RequestMapping("/api")
public class NatureOfCustomerComplaintsResource {

    private final Logger log = LoggerFactory.getLogger(NatureOfCustomerComplaintsResource.class);

    private static final String ENTITY_NAME = "natureOfCustomerComplaints";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NatureOfCustomerComplaintsService natureOfCustomerComplaintsService;

    private final NatureOfCustomerComplaintsRepository natureOfCustomerComplaintsRepository;

    private final NatureOfCustomerComplaintsQueryService natureOfCustomerComplaintsQueryService;

    public NatureOfCustomerComplaintsResource(
        NatureOfCustomerComplaintsService natureOfCustomerComplaintsService,
        NatureOfCustomerComplaintsRepository natureOfCustomerComplaintsRepository,
        NatureOfCustomerComplaintsQueryService natureOfCustomerComplaintsQueryService
    ) {
        this.natureOfCustomerComplaintsService = natureOfCustomerComplaintsService;
        this.natureOfCustomerComplaintsRepository = natureOfCustomerComplaintsRepository;
        this.natureOfCustomerComplaintsQueryService = natureOfCustomerComplaintsQueryService;
    }

    /**
     * {@code POST  /nature-of-customer-complaints} : Create a new natureOfCustomerComplaints.
     *
     * @param natureOfCustomerComplaintsDTO the natureOfCustomerComplaintsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new natureOfCustomerComplaintsDTO, or with status {@code 400 (Bad Request)} if the natureOfCustomerComplaints has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nature-of-customer-complaints")
    public ResponseEntity<NatureOfCustomerComplaintsDTO> createNatureOfCustomerComplaints(
        @Valid @RequestBody NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO
    ) throws URISyntaxException {
        log.debug("REST request to save NatureOfCustomerComplaints : {}", natureOfCustomerComplaintsDTO);
        if (natureOfCustomerComplaintsDTO.getId() != null) {
            throw new BadRequestAlertException("A new natureOfCustomerComplaints cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NatureOfCustomerComplaintsDTO result = natureOfCustomerComplaintsService.save(natureOfCustomerComplaintsDTO);
        return ResponseEntity
            .created(new URI("/api/nature-of-customer-complaints/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nature-of-customer-complaints/:id} : Updates an existing natureOfCustomerComplaints.
     *
     * @param id the id of the natureOfCustomerComplaintsDTO to save.
     * @param natureOfCustomerComplaintsDTO the natureOfCustomerComplaintsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated natureOfCustomerComplaintsDTO,
     * or with status {@code 400 (Bad Request)} if the natureOfCustomerComplaintsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the natureOfCustomerComplaintsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nature-of-customer-complaints/{id}")
    public ResponseEntity<NatureOfCustomerComplaintsDTO> updateNatureOfCustomerComplaints(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update NatureOfCustomerComplaints : {}, {}", id, natureOfCustomerComplaintsDTO);
        if (natureOfCustomerComplaintsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, natureOfCustomerComplaintsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!natureOfCustomerComplaintsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NatureOfCustomerComplaintsDTO result = natureOfCustomerComplaintsService.save(natureOfCustomerComplaintsDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, natureOfCustomerComplaintsDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /nature-of-customer-complaints/:id} : Partial updates given fields of an existing natureOfCustomerComplaints, field will ignore if it is null
     *
     * @param id the id of the natureOfCustomerComplaintsDTO to save.
     * @param natureOfCustomerComplaintsDTO the natureOfCustomerComplaintsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated natureOfCustomerComplaintsDTO,
     * or with status {@code 400 (Bad Request)} if the natureOfCustomerComplaintsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the natureOfCustomerComplaintsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the natureOfCustomerComplaintsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nature-of-customer-complaints/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NatureOfCustomerComplaintsDTO> partialUpdateNatureOfCustomerComplaints(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NatureOfCustomerComplaintsDTO natureOfCustomerComplaintsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update NatureOfCustomerComplaints partially : {}, {}", id, natureOfCustomerComplaintsDTO);
        if (natureOfCustomerComplaintsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, natureOfCustomerComplaintsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!natureOfCustomerComplaintsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NatureOfCustomerComplaintsDTO> result = natureOfCustomerComplaintsService.partialUpdate(natureOfCustomerComplaintsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, natureOfCustomerComplaintsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /nature-of-customer-complaints} : get all the natureOfCustomerComplaints.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of natureOfCustomerComplaints in body.
     */
    @GetMapping("/nature-of-customer-complaints")
    public ResponseEntity<List<NatureOfCustomerComplaintsDTO>> getAllNatureOfCustomerComplaints(
        NatureOfCustomerComplaintsCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get NatureOfCustomerComplaints by criteria: {}", criteria);
        Page<NatureOfCustomerComplaintsDTO> page = natureOfCustomerComplaintsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nature-of-customer-complaints/count} : count all the natureOfCustomerComplaints.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/nature-of-customer-complaints/count")
    public ResponseEntity<Long> countNatureOfCustomerComplaints(NatureOfCustomerComplaintsCriteria criteria) {
        log.debug("REST request to count NatureOfCustomerComplaints by criteria: {}", criteria);
        return ResponseEntity.ok().body(natureOfCustomerComplaintsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /nature-of-customer-complaints/:id} : get the "id" natureOfCustomerComplaints.
     *
     * @param id the id of the natureOfCustomerComplaintsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the natureOfCustomerComplaintsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nature-of-customer-complaints/{id}")
    public ResponseEntity<NatureOfCustomerComplaintsDTO> getNatureOfCustomerComplaints(@PathVariable Long id) {
        log.debug("REST request to get NatureOfCustomerComplaints : {}", id);
        Optional<NatureOfCustomerComplaintsDTO> natureOfCustomerComplaintsDTO = natureOfCustomerComplaintsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(natureOfCustomerComplaintsDTO);
    }

    /**
     * {@code DELETE  /nature-of-customer-complaints/:id} : delete the "id" natureOfCustomerComplaints.
     *
     * @param id the id of the natureOfCustomerComplaintsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nature-of-customer-complaints/{id}")
    public ResponseEntity<Void> deleteNatureOfCustomerComplaints(@PathVariable Long id) {
        log.debug("REST request to delete NatureOfCustomerComplaints : {}", id);
        natureOfCustomerComplaintsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/nature-of-customer-complaints?query=:query} : search for the natureOfCustomerComplaints corresponding
     * to the query.
     *
     * @param query the query of the natureOfCustomerComplaints search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/nature-of-customer-complaints")
    public ResponseEntity<List<NatureOfCustomerComplaintsDTO>> searchNatureOfCustomerComplaints(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of NatureOfCustomerComplaints for query {}", query);
        Page<NatureOfCustomerComplaintsDTO> page = natureOfCustomerComplaintsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
