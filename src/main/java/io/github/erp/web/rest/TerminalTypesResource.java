package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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

import io.github.erp.repository.TerminalTypesRepository;
import io.github.erp.service.TerminalTypesQueryService;
import io.github.erp.service.TerminalTypesService;
import io.github.erp.service.criteria.TerminalTypesCriteria;
import io.github.erp.service.dto.TerminalTypesDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TerminalTypes}.
 */
@RestController
@RequestMapping("/api")
public class TerminalTypesResource {

    private final Logger log = LoggerFactory.getLogger(TerminalTypesResource.class);

    private static final String ENTITY_NAME = "terminalTypes";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TerminalTypesService terminalTypesService;

    private final TerminalTypesRepository terminalTypesRepository;

    private final TerminalTypesQueryService terminalTypesQueryService;

    public TerminalTypesResource(
        TerminalTypesService terminalTypesService,
        TerminalTypesRepository terminalTypesRepository,
        TerminalTypesQueryService terminalTypesQueryService
    ) {
        this.terminalTypesService = terminalTypesService;
        this.terminalTypesRepository = terminalTypesRepository;
        this.terminalTypesQueryService = terminalTypesQueryService;
    }

    /**
     * {@code POST  /terminal-types} : Create a new terminalTypes.
     *
     * @param terminalTypesDTO the terminalTypesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new terminalTypesDTO, or with status {@code 400 (Bad Request)} if the terminalTypes has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/terminal-types")
    public ResponseEntity<TerminalTypesDTO> createTerminalTypes(@Valid @RequestBody TerminalTypesDTO terminalTypesDTO)
        throws URISyntaxException {
        log.debug("REST request to save TerminalTypes : {}", terminalTypesDTO);
        if (terminalTypesDTO.getId() != null) {
            throw new BadRequestAlertException("A new terminalTypes cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TerminalTypesDTO result = terminalTypesService.save(terminalTypesDTO);
        return ResponseEntity
            .created(new URI("/api/terminal-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /terminal-types/:id} : Updates an existing terminalTypes.
     *
     * @param id the id of the terminalTypesDTO to save.
     * @param terminalTypesDTO the terminalTypesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated terminalTypesDTO,
     * or with status {@code 400 (Bad Request)} if the terminalTypesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the terminalTypesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/terminal-types/{id}")
    public ResponseEntity<TerminalTypesDTO> updateTerminalTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TerminalTypesDTO terminalTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TerminalTypes : {}, {}", id, terminalTypesDTO);
        if (terminalTypesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, terminalTypesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!terminalTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TerminalTypesDTO result = terminalTypesService.save(terminalTypesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, terminalTypesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /terminal-types/:id} : Partial updates given fields of an existing terminalTypes, field will ignore if it is null
     *
     * @param id the id of the terminalTypesDTO to save.
     * @param terminalTypesDTO the terminalTypesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated terminalTypesDTO,
     * or with status {@code 400 (Bad Request)} if the terminalTypesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the terminalTypesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the terminalTypesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/terminal-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TerminalTypesDTO> partialUpdateTerminalTypes(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TerminalTypesDTO terminalTypesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TerminalTypes partially : {}, {}", id, terminalTypesDTO);
        if (terminalTypesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, terminalTypesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!terminalTypesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TerminalTypesDTO> result = terminalTypesService.partialUpdate(terminalTypesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, terminalTypesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /terminal-types} : get all the terminalTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of terminalTypes in body.
     */
    @GetMapping("/terminal-types")
    public ResponseEntity<List<TerminalTypesDTO>> getAllTerminalTypes(TerminalTypesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TerminalTypes by criteria: {}", criteria);
        Page<TerminalTypesDTO> page = terminalTypesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /terminal-types/count} : count all the terminalTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/terminal-types/count")
    public ResponseEntity<Long> countTerminalTypes(TerminalTypesCriteria criteria) {
        log.debug("REST request to count TerminalTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(terminalTypesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /terminal-types/:id} : get the "id" terminalTypes.
     *
     * @param id the id of the terminalTypesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the terminalTypesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/terminal-types/{id}")
    public ResponseEntity<TerminalTypesDTO> getTerminalTypes(@PathVariable Long id) {
        log.debug("REST request to get TerminalTypes : {}", id);
        Optional<TerminalTypesDTO> terminalTypesDTO = terminalTypesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(terminalTypesDTO);
    }

    /**
     * {@code DELETE  /terminal-types/:id} : delete the "id" terminalTypes.
     *
     * @param id the id of the terminalTypesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/terminal-types/{id}")
    public ResponseEntity<Void> deleteTerminalTypes(@PathVariable Long id) {
        log.debug("REST request to delete TerminalTypes : {}", id);
        terminalTypesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/terminal-types?query=:query} : search for the terminalTypes corresponding
     * to the query.
     *
     * @param query the query of the terminalTypes search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/terminal-types")
    public ResponseEntity<List<TerminalTypesDTO>> searchTerminalTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TerminalTypes for query {}", query);
        Page<TerminalTypesDTO> page = terminalTypesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
