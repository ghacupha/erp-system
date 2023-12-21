package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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

import io.github.erp.repository.TerminalsAndPOSRepository;
import io.github.erp.service.TerminalsAndPOSQueryService;
import io.github.erp.service.TerminalsAndPOSService;
import io.github.erp.service.criteria.TerminalsAndPOSCriteria;
import io.github.erp.service.dto.TerminalsAndPOSDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TerminalsAndPOS}.
 */
@RestController
@RequestMapping("/api")
public class TerminalsAndPOSResource {

    private final Logger log = LoggerFactory.getLogger(TerminalsAndPOSResource.class);

    private static final String ENTITY_NAME = "gdiDataTerminalsAndPos";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TerminalsAndPOSService terminalsAndPOSService;

    private final TerminalsAndPOSRepository terminalsAndPOSRepository;

    private final TerminalsAndPOSQueryService terminalsAndPOSQueryService;

    public TerminalsAndPOSResource(
        TerminalsAndPOSService terminalsAndPOSService,
        TerminalsAndPOSRepository terminalsAndPOSRepository,
        TerminalsAndPOSQueryService terminalsAndPOSQueryService
    ) {
        this.terminalsAndPOSService = terminalsAndPOSService;
        this.terminalsAndPOSRepository = terminalsAndPOSRepository;
        this.terminalsAndPOSQueryService = terminalsAndPOSQueryService;
    }

    /**
     * {@code POST  /terminals-and-pos} : Create a new terminalsAndPOS.
     *
     * @param terminalsAndPOSDTO the terminalsAndPOSDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new terminalsAndPOSDTO, or with status {@code 400 (Bad Request)} if the terminalsAndPOS has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/terminals-and-pos")
    public ResponseEntity<TerminalsAndPOSDTO> createTerminalsAndPOS(@Valid @RequestBody TerminalsAndPOSDTO terminalsAndPOSDTO)
        throws URISyntaxException {
        log.debug("REST request to save TerminalsAndPOS : {}", terminalsAndPOSDTO);
        if (terminalsAndPOSDTO.getId() != null) {
            throw new BadRequestAlertException("A new terminalsAndPOS cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TerminalsAndPOSDTO result = terminalsAndPOSService.save(terminalsAndPOSDTO);
        return ResponseEntity
            .created(new URI("/api/terminals-and-pos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /terminals-and-pos/:id} : Updates an existing terminalsAndPOS.
     *
     * @param id the id of the terminalsAndPOSDTO to save.
     * @param terminalsAndPOSDTO the terminalsAndPOSDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated terminalsAndPOSDTO,
     * or with status {@code 400 (Bad Request)} if the terminalsAndPOSDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the terminalsAndPOSDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/terminals-and-pos/{id}")
    public ResponseEntity<TerminalsAndPOSDTO> updateTerminalsAndPOS(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TerminalsAndPOSDTO terminalsAndPOSDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TerminalsAndPOS : {}, {}", id, terminalsAndPOSDTO);
        if (terminalsAndPOSDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, terminalsAndPOSDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!terminalsAndPOSRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TerminalsAndPOSDTO result = terminalsAndPOSService.save(terminalsAndPOSDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, terminalsAndPOSDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /terminals-and-pos/:id} : Partial updates given fields of an existing terminalsAndPOS, field will ignore if it is null
     *
     * @param id the id of the terminalsAndPOSDTO to save.
     * @param terminalsAndPOSDTO the terminalsAndPOSDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated terminalsAndPOSDTO,
     * or with status {@code 400 (Bad Request)} if the terminalsAndPOSDTO is not valid,
     * or with status {@code 404 (Not Found)} if the terminalsAndPOSDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the terminalsAndPOSDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/terminals-and-pos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TerminalsAndPOSDTO> partialUpdateTerminalsAndPOS(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TerminalsAndPOSDTO terminalsAndPOSDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TerminalsAndPOS partially : {}, {}", id, terminalsAndPOSDTO);
        if (terminalsAndPOSDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, terminalsAndPOSDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!terminalsAndPOSRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TerminalsAndPOSDTO> result = terminalsAndPOSService.partialUpdate(terminalsAndPOSDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, terminalsAndPOSDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /terminals-and-pos} : get all the terminalsAndPOS.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of terminalsAndPOS in body.
     */
    @GetMapping("/terminals-and-pos")
    public ResponseEntity<List<TerminalsAndPOSDTO>> getAllTerminalsAndPOS(TerminalsAndPOSCriteria criteria, Pageable pageable) {
        log.debug("REST request to get TerminalsAndPOS by criteria: {}", criteria);
        Page<TerminalsAndPOSDTO> page = terminalsAndPOSQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /terminals-and-pos/count} : count all the terminalsAndPOS.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/terminals-and-pos/count")
    public ResponseEntity<Long> countTerminalsAndPOS(TerminalsAndPOSCriteria criteria) {
        log.debug("REST request to count TerminalsAndPOS by criteria: {}", criteria);
        return ResponseEntity.ok().body(terminalsAndPOSQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /terminals-and-pos/:id} : get the "id" terminalsAndPOS.
     *
     * @param id the id of the terminalsAndPOSDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the terminalsAndPOSDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/terminals-and-pos/{id}")
    public ResponseEntity<TerminalsAndPOSDTO> getTerminalsAndPOS(@PathVariable Long id) {
        log.debug("REST request to get TerminalsAndPOS : {}", id);
        Optional<TerminalsAndPOSDTO> terminalsAndPOSDTO = terminalsAndPOSService.findOne(id);
        return ResponseUtil.wrapOrNotFound(terminalsAndPOSDTO);
    }

    /**
     * {@code DELETE  /terminals-and-pos/:id} : delete the "id" terminalsAndPOS.
     *
     * @param id the id of the terminalsAndPOSDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/terminals-and-pos/{id}")
    public ResponseEntity<Void> deleteTerminalsAndPOS(@PathVariable Long id) {
        log.debug("REST request to delete TerminalsAndPOS : {}", id);
        terminalsAndPOSService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/terminals-and-pos?query=:query} : search for the terminalsAndPOS corresponding
     * to the query.
     *
     * @param query the query of the terminalsAndPOS search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/terminals-and-pos")
    public ResponseEntity<List<TerminalsAndPOSDTO>> searchTerminalsAndPOS(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TerminalsAndPOS for query {}", query);
        Page<TerminalsAndPOSDTO> page = terminalsAndPOSService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
