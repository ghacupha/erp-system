package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.repository.MemoActionRepository;
import io.github.erp.service.MemoActionService;
import io.github.erp.service.dto.MemoActionDTO;
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
 * REST controller for managing {@link io.github.erp.domain.MemoAction}.
 */
@RestController
@RequestMapping("/api")
public class MemoActionResource {

    private final Logger log = LoggerFactory.getLogger(MemoActionResource.class);

    private static final String ENTITY_NAME = "memoAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemoActionService memoActionService;

    private final MemoActionRepository memoActionRepository;

    public MemoActionResource(MemoActionService memoActionService, MemoActionRepository memoActionRepository) {
        this.memoActionService = memoActionService;
        this.memoActionRepository = memoActionRepository;
    }

    /**
     * {@code POST  /memo-actions} : Create a new memoAction.
     *
     * @param memoActionDTO the memoActionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memoActionDTO, or with status {@code 400 (Bad Request)} if the memoAction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/memo-actions")
    public ResponseEntity<MemoActionDTO> createMemoAction(@Valid @RequestBody MemoActionDTO memoActionDTO) throws URISyntaxException {
        log.debug("REST request to save MemoAction : {}", memoActionDTO);
        if (memoActionDTO.getId() != null) {
            throw new BadRequestAlertException("A new memoAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemoActionDTO result = memoActionService.save(memoActionDTO);
        return ResponseEntity
            .created(new URI("/api/memo-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /memo-actions/:id} : Updates an existing memoAction.
     *
     * @param id the id of the memoActionDTO to save.
     * @param memoActionDTO the memoActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memoActionDTO,
     * or with status {@code 400 (Bad Request)} if the memoActionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memoActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/memo-actions/{id}")
    public ResponseEntity<MemoActionDTO> updateMemoAction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MemoActionDTO memoActionDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MemoAction : {}, {}", id, memoActionDTO);
        if (memoActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memoActionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memoActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MemoActionDTO result = memoActionService.save(memoActionDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memoActionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /memo-actions/:id} : Partial updates given fields of an existing memoAction, field will ignore if it is null
     *
     * @param id the id of the memoActionDTO to save.
     * @param memoActionDTO the memoActionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memoActionDTO,
     * or with status {@code 400 (Bad Request)} if the memoActionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the memoActionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the memoActionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/memo-actions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MemoActionDTO> partialUpdateMemoAction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MemoActionDTO memoActionDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MemoAction partially : {}, {}", id, memoActionDTO);
        if (memoActionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memoActionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memoActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MemoActionDTO> result = memoActionService.partialUpdate(memoActionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memoActionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /memo-actions} : get all the memoActions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memoActions in body.
     */
    @GetMapping("/memo-actions")
    public ResponseEntity<List<MemoActionDTO>> getAllMemoActions(Pageable pageable) {
        log.debug("REST request to get a page of MemoActions");
        Page<MemoActionDTO> page = memoActionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /memo-actions/:id} : get the "id" memoAction.
     *
     * @param id the id of the memoActionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memoActionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/memo-actions/{id}")
    public ResponseEntity<MemoActionDTO> getMemoAction(@PathVariable Long id) {
        log.debug("REST request to get MemoAction : {}", id);
        Optional<MemoActionDTO> memoActionDTO = memoActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(memoActionDTO);
    }

    /**
     * {@code DELETE  /memo-actions/:id} : delete the "id" memoAction.
     *
     * @param id the id of the memoActionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/memo-actions/{id}")
    public ResponseEntity<Void> deleteMemoAction(@PathVariable Long id) {
        log.debug("REST request to delete MemoAction : {}", id);
        memoActionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/memo-actions?query=:query} : search for the memoAction corresponding
     * to the query.
     *
     * @param query the query of the memoAction search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/memo-actions")
    public ResponseEntity<List<MemoActionDTO>> searchMemoActions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MemoActions for query {}", query);
        Page<MemoActionDTO> page = memoActionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
