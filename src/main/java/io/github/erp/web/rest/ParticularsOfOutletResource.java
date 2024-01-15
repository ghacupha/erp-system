package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import io.github.erp.repository.ParticularsOfOutletRepository;
import io.github.erp.service.ParticularsOfOutletQueryService;
import io.github.erp.service.ParticularsOfOutletService;
import io.github.erp.service.criteria.ParticularsOfOutletCriteria;
import io.github.erp.service.dto.ParticularsOfOutletDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ParticularsOfOutlet}.
 */
@RestController
@RequestMapping("/api")
public class ParticularsOfOutletResource {

    private final Logger log = LoggerFactory.getLogger(ParticularsOfOutletResource.class);

    private static final String ENTITY_NAME = "gdiDataParticularsOfOutlet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticularsOfOutletService particularsOfOutletService;

    private final ParticularsOfOutletRepository particularsOfOutletRepository;

    private final ParticularsOfOutletQueryService particularsOfOutletQueryService;

    public ParticularsOfOutletResource(
        ParticularsOfOutletService particularsOfOutletService,
        ParticularsOfOutletRepository particularsOfOutletRepository,
        ParticularsOfOutletQueryService particularsOfOutletQueryService
    ) {
        this.particularsOfOutletService = particularsOfOutletService;
        this.particularsOfOutletRepository = particularsOfOutletRepository;
        this.particularsOfOutletQueryService = particularsOfOutletQueryService;
    }

    /**
     * {@code POST  /particulars-of-outlets} : Create a new particularsOfOutlet.
     *
     * @param particularsOfOutletDTO the particularsOfOutletDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new particularsOfOutletDTO, or with status {@code 400 (Bad Request)} if the particularsOfOutlet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/particulars-of-outlets")
    public ResponseEntity<ParticularsOfOutletDTO> createParticularsOfOutlet(
        @Valid @RequestBody ParticularsOfOutletDTO particularsOfOutletDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ParticularsOfOutlet : {}", particularsOfOutletDTO);
        if (particularsOfOutletDTO.getId() != null) {
            throw new BadRequestAlertException("A new particularsOfOutlet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParticularsOfOutletDTO result = particularsOfOutletService.save(particularsOfOutletDTO);
        return ResponseEntity
            .created(new URI("/api/particulars-of-outlets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /particulars-of-outlets/:id} : Updates an existing particularsOfOutlet.
     *
     * @param id the id of the particularsOfOutletDTO to save.
     * @param particularsOfOutletDTO the particularsOfOutletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated particularsOfOutletDTO,
     * or with status {@code 400 (Bad Request)} if the particularsOfOutletDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the particularsOfOutletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/particulars-of-outlets/{id}")
    public ResponseEntity<ParticularsOfOutletDTO> updateParticularsOfOutlet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ParticularsOfOutletDTO particularsOfOutletDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ParticularsOfOutlet : {}, {}", id, particularsOfOutletDTO);
        if (particularsOfOutletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, particularsOfOutletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!particularsOfOutletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ParticularsOfOutletDTO result = particularsOfOutletService.save(particularsOfOutletDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, particularsOfOutletDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /particulars-of-outlets/:id} : Partial updates given fields of an existing particularsOfOutlet, field will ignore if it is null
     *
     * @param id the id of the particularsOfOutletDTO to save.
     * @param particularsOfOutletDTO the particularsOfOutletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated particularsOfOutletDTO,
     * or with status {@code 400 (Bad Request)} if the particularsOfOutletDTO is not valid,
     * or with status {@code 404 (Not Found)} if the particularsOfOutletDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the particularsOfOutletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/particulars-of-outlets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ParticularsOfOutletDTO> partialUpdateParticularsOfOutlet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ParticularsOfOutletDTO particularsOfOutletDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ParticularsOfOutlet partially : {}, {}", id, particularsOfOutletDTO);
        if (particularsOfOutletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, particularsOfOutletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!particularsOfOutletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ParticularsOfOutletDTO> result = particularsOfOutletService.partialUpdate(particularsOfOutletDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, particularsOfOutletDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /particulars-of-outlets} : get all the particularsOfOutlets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of particularsOfOutlets in body.
     */
    @GetMapping("/particulars-of-outlets")
    public ResponseEntity<List<ParticularsOfOutletDTO>> getAllParticularsOfOutlets(
        ParticularsOfOutletCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ParticularsOfOutlets by criteria: {}", criteria);
        Page<ParticularsOfOutletDTO> page = particularsOfOutletQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /particulars-of-outlets/count} : count all the particularsOfOutlets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/particulars-of-outlets/count")
    public ResponseEntity<Long> countParticularsOfOutlets(ParticularsOfOutletCriteria criteria) {
        log.debug("REST request to count ParticularsOfOutlets by criteria: {}", criteria);
        return ResponseEntity.ok().body(particularsOfOutletQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /particulars-of-outlets/:id} : get the "id" particularsOfOutlet.
     *
     * @param id the id of the particularsOfOutletDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the particularsOfOutletDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/particulars-of-outlets/{id}")
    public ResponseEntity<ParticularsOfOutletDTO> getParticularsOfOutlet(@PathVariable Long id) {
        log.debug("REST request to get ParticularsOfOutlet : {}", id);
        Optional<ParticularsOfOutletDTO> particularsOfOutletDTO = particularsOfOutletService.findOne(id);
        return ResponseUtil.wrapOrNotFound(particularsOfOutletDTO);
    }

    /**
     * {@code DELETE  /particulars-of-outlets/:id} : delete the "id" particularsOfOutlet.
     *
     * @param id the id of the particularsOfOutletDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/particulars-of-outlets/{id}")
    public ResponseEntity<Void> deleteParticularsOfOutlet(@PathVariable Long id) {
        log.debug("REST request to delete ParticularsOfOutlet : {}", id);
        particularsOfOutletService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/particulars-of-outlets?query=:query} : search for the particularsOfOutlet corresponding
     * to the query.
     *
     * @param query the query of the particularsOfOutlet search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/particulars-of-outlets")
    public ResponseEntity<List<ParticularsOfOutletDTO>> searchParticularsOfOutlets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ParticularsOfOutlets for query {}", query);
        Page<ParticularsOfOutletDTO> page = particularsOfOutletService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
