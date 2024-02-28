package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.repository.TerminalFunctionsRepository;
import io.github.erp.service.TerminalFunctionsQueryService;
import io.github.erp.service.TerminalFunctionsService;
import io.github.erp.service.criteria.TerminalFunctionsCriteria;
import io.github.erp.service.dto.TerminalFunctionsDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TerminalFunctions}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class TerminalFunctionsResourceProd {

    private final Logger log = LoggerFactory.getLogger(TerminalFunctionsResourceProd.class);

    private static final String ENTITY_NAME = "terminalFunctions";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TerminalFunctionsService terminalFunctionsService;

    private final TerminalFunctionsRepository terminalFunctionsRepository;

    private final TerminalFunctionsQueryService terminalFunctionsQueryService;

    public TerminalFunctionsResourceProd(
        TerminalFunctionsService terminalFunctionsService,
        TerminalFunctionsRepository terminalFunctionsRepository,
        TerminalFunctionsQueryService terminalFunctionsQueryService
    ) {
        this.terminalFunctionsService = terminalFunctionsService;
        this.terminalFunctionsRepository = terminalFunctionsRepository;
        this.terminalFunctionsQueryService = terminalFunctionsQueryService;
    }

    /**
     * {@code POST  /terminal-functions} : Create a new terminalFunctions.
     *
     * @param terminalFunctionsDTO the terminalFunctionsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new terminalFunctionsDTO, or with status {@code 400 (Bad Request)} if the terminalFunctions has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/terminal-functions")
    public ResponseEntity<TerminalFunctionsDTO> createTerminalFunctions(@Valid @RequestBody TerminalFunctionsDTO terminalFunctionsDTO)
        throws URISyntaxException {
        log.debug("REST request to save TerminalFunctions : {}", terminalFunctionsDTO);
        if (terminalFunctionsDTO.getId() != null) {
            throw new BadRequestAlertException("A new terminalFunctions cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TerminalFunctionsDTO result = terminalFunctionsService.save(terminalFunctionsDTO);
        return ResponseEntity
            .created(new URI("/api/terminal-functions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /terminal-functions/:id} : Updates an existing terminalFunctions.
     *
     * @param id the id of the terminalFunctionsDTO to save.
     * @param terminalFunctionsDTO the terminalFunctionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated terminalFunctionsDTO,
     * or with status {@code 400 (Bad Request)} if the terminalFunctionsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the terminalFunctionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/terminal-functions/{id}")
    public ResponseEntity<TerminalFunctionsDTO> updateTerminalFunctions(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TerminalFunctionsDTO terminalFunctionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TerminalFunctions : {}, {}", id, terminalFunctionsDTO);
        if (terminalFunctionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, terminalFunctionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!terminalFunctionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TerminalFunctionsDTO result = terminalFunctionsService.save(terminalFunctionsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, terminalFunctionsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /terminal-functions/:id} : Partial updates given fields of an existing terminalFunctions, field will ignore if it is null
     *
     * @param id the id of the terminalFunctionsDTO to save.
     * @param terminalFunctionsDTO the terminalFunctionsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated terminalFunctionsDTO,
     * or with status {@code 400 (Bad Request)} if the terminalFunctionsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the terminalFunctionsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the terminalFunctionsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/terminal-functions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TerminalFunctionsDTO> partialUpdateTerminalFunctions(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TerminalFunctionsDTO terminalFunctionsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TerminalFunctions partially : {}, {}", id, terminalFunctionsDTO);
        if (terminalFunctionsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, terminalFunctionsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!terminalFunctionsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TerminalFunctionsDTO> result = terminalFunctionsService.partialUpdate(terminalFunctionsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, terminalFunctionsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /terminal-functions} : get all the terminalFunctions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of terminalFunctions in body.
     */
    @GetMapping("/terminal-functions")
    public ResponseEntity<List<TerminalFunctionsDTO>> getAllTerminalFunctions(TerminalFunctionsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TerminalFunctions by criteria: {}", criteria);
        Page<TerminalFunctionsDTO> page = terminalFunctionsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /terminal-functions/count} : count all the terminalFunctions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/terminal-functions/count")
    public ResponseEntity<Long> countTerminalFunctions(TerminalFunctionsCriteria criteria) {
        log.debug("REST request to count TerminalFunctions by criteria: {}", criteria);
        return ResponseEntity.ok().body(terminalFunctionsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /terminal-functions/:id} : get the "id" terminalFunctions.
     *
     * @param id the id of the terminalFunctionsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the terminalFunctionsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/terminal-functions/{id}")
    public ResponseEntity<TerminalFunctionsDTO> getTerminalFunctions(@PathVariable Long id) {
        log.debug("REST request to get TerminalFunctions : {}", id);
        Optional<TerminalFunctionsDTO> terminalFunctionsDTO = terminalFunctionsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(terminalFunctionsDTO);
    }

    /**
     * {@code DELETE  /terminal-functions/:id} : delete the "id" terminalFunctions.
     *
     * @param id the id of the terminalFunctionsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/terminal-functions/{id}")
    public ResponseEntity<Void> deleteTerminalFunctions(@PathVariable Long id) {
        log.debug("REST request to delete TerminalFunctions : {}", id);
        terminalFunctionsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/terminal-functions?query=:query} : search for the terminalFunctions corresponding
     * to the query.
     *
     * @param query the query of the terminalFunctions search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/terminal-functions")
    public ResponseEntity<List<TerminalFunctionsDTO>> searchTerminalFunctions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TerminalFunctions for query {}", query);
        Page<TerminalFunctionsDTO> page = terminalFunctionsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
