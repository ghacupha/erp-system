package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.repository.InternalMemoRepository;
import io.github.erp.service.InternalMemoQueryService;
import io.github.erp.service.InternalMemoService;
import io.github.erp.service.criteria.InternalMemoCriteria;
import io.github.erp.service.dto.InternalMemoDTO;
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
 * REST controller for managing {@link io.github.erp.domain.InternalMemo}.
 */
@RestController
@RequestMapping("/api")
public class InternalMemoResource {

    private final Logger log = LoggerFactory.getLogger(InternalMemoResource.class);

    private static final String ENTITY_NAME = "internalMemo";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalMemoService internalMemoService;

    private final InternalMemoRepository internalMemoRepository;

    private final InternalMemoQueryService internalMemoQueryService;

    public InternalMemoResource(
        InternalMemoService internalMemoService,
        InternalMemoRepository internalMemoRepository,
        InternalMemoQueryService internalMemoQueryService
    ) {
        this.internalMemoService = internalMemoService;
        this.internalMemoRepository = internalMemoRepository;
        this.internalMemoQueryService = internalMemoQueryService;
    }

    /**
     * {@code POST  /internal-memos} : Create a new internalMemo.
     *
     * @param internalMemoDTO the internalMemoDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new internalMemoDTO, or with status {@code 400 (Bad Request)} if the internalMemo has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/internal-memos")
    public ResponseEntity<InternalMemoDTO> createInternalMemo(@Valid @RequestBody InternalMemoDTO internalMemoDTO)
        throws URISyntaxException {
        log.debug("REST request to save InternalMemo : {}", internalMemoDTO);
        if (internalMemoDTO.getId() != null) {
            throw new BadRequestAlertException("A new internalMemo cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InternalMemoDTO result = internalMemoService.save(internalMemoDTO);
        return ResponseEntity
            .created(new URI("/api/internal-memos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /internal-memos/:id} : Updates an existing internalMemo.
     *
     * @param id the id of the internalMemoDTO to save.
     * @param internalMemoDTO the internalMemoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internalMemoDTO,
     * or with status {@code 400 (Bad Request)} if the internalMemoDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the internalMemoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/internal-memos/{id}")
    public ResponseEntity<InternalMemoDTO> updateInternalMemo(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InternalMemoDTO internalMemoDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InternalMemo : {}, {}", id, internalMemoDTO);
        if (internalMemoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internalMemoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internalMemoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InternalMemoDTO result = internalMemoService.save(internalMemoDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, internalMemoDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /internal-memos/:id} : Partial updates given fields of an existing internalMemo, field will ignore if it is null
     *
     * @param id the id of the internalMemoDTO to save.
     * @param internalMemoDTO the internalMemoDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internalMemoDTO,
     * or with status {@code 400 (Bad Request)} if the internalMemoDTO is not valid,
     * or with status {@code 404 (Not Found)} if the internalMemoDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the internalMemoDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/internal-memos/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InternalMemoDTO> partialUpdateInternalMemo(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InternalMemoDTO internalMemoDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InternalMemo partially : {}, {}", id, internalMemoDTO);
        if (internalMemoDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internalMemoDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internalMemoRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InternalMemoDTO> result = internalMemoService.partialUpdate(internalMemoDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, internalMemoDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /internal-memos} : get all the internalMemos.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of internalMemos in body.
     */
    @GetMapping("/internal-memos")
    public ResponseEntity<List<InternalMemoDTO>> getAllInternalMemos(InternalMemoCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InternalMemos by criteria: {}", criteria);
        Page<InternalMemoDTO> page = internalMemoQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /internal-memos/count} : count all the internalMemos.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/internal-memos/count")
    public ResponseEntity<Long> countInternalMemos(InternalMemoCriteria criteria) {
        log.debug("REST request to count InternalMemos by criteria: {}", criteria);
        return ResponseEntity.ok().body(internalMemoQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /internal-memos/:id} : get the "id" internalMemo.
     *
     * @param id the id of the internalMemoDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the internalMemoDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/internal-memos/{id}")
    public ResponseEntity<InternalMemoDTO> getInternalMemo(@PathVariable Long id) {
        log.debug("REST request to get InternalMemo : {}", id);
        Optional<InternalMemoDTO> internalMemoDTO = internalMemoService.findOne(id);
        return ResponseUtil.wrapOrNotFound(internalMemoDTO);
    }

    /**
     * {@code DELETE  /internal-memos/:id} : delete the "id" internalMemo.
     *
     * @param id the id of the internalMemoDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/internal-memos/{id}")
    public ResponseEntity<Void> deleteInternalMemo(@PathVariable Long id) {
        log.debug("REST request to delete InternalMemo : {}", id);
        internalMemoService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/internal-memos?query=:query} : search for the internalMemo corresponding
     * to the query.
     *
     * @param query the query of the internalMemo search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/internal-memos")
    public ResponseEntity<List<InternalMemoDTO>> searchInternalMemos(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InternalMemos for query {}", query);
        Page<InternalMemoDTO> page = internalMemoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
