package io.github.erp.erp.resources.memo;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.MemoAction;
import io.github.erp.repository.MemoActionRepository;
import io.github.erp.repository.search.MemoActionSearchRepository;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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
 * REST controller for managing {@link MemoAction}.
 */
@RestController
@RequestMapping("/api/payments")
@Transactional
public class MemoActionResourceProd {

    private final Logger log = LoggerFactory.getLogger(MemoActionResourceProd.class);

    private static final String ENTITY_NAME = "memoAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MemoActionRepository memoActionRepository;

    private final MemoActionSearchRepository memoActionSearchRepository;

    public MemoActionResourceProd(MemoActionRepository memoActionRepository, MemoActionSearchRepository memoActionSearchRepository) {
        this.memoActionRepository = memoActionRepository;
        this.memoActionSearchRepository = memoActionSearchRepository;
    }

    /**
     * {@code POST  /memo-actions} : Create a new memoAction.
     *
     * @param memoAction the memoAction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new memoAction, or with status {@code 400 (Bad Request)} if the memoAction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/memo-actions")
    public ResponseEntity<MemoAction> createMemoAction(@Valid @RequestBody MemoAction memoAction) throws URISyntaxException {
        log.debug("REST request to save MemoAction : {}", memoAction);
        if (memoAction.getId() != null) {
            throw new BadRequestAlertException("A new memoAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MemoAction result = memoActionRepository.save(memoAction);
        memoActionSearchRepository.save(result);
        return ResponseEntity
            .created(new URI("/api/memo-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /memo-actions/:id} : Updates an existing memoAction.
     *
     * @param id the id of the memoAction to save.
     * @param memoAction the memoAction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memoAction,
     * or with status {@code 400 (Bad Request)} if the memoAction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the memoAction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/memo-actions/{id}")
    public ResponseEntity<MemoAction> updateMemoAction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MemoAction memoAction
    ) throws URISyntaxException {
        log.debug("REST request to update MemoAction : {}, {}", id, memoAction);
        if (memoAction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memoAction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memoActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MemoAction result = memoActionRepository.save(memoAction);
        memoActionSearchRepository.save(result);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memoAction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /memo-actions/:id} : Partial updates given fields of an existing memoAction, field will ignore if it is null
     *
     * @param id the id of the memoAction to save.
     * @param memoAction the memoAction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated memoAction,
     * or with status {@code 400 (Bad Request)} if the memoAction is not valid,
     * or with status {@code 404 (Not Found)} if the memoAction is not found,
     * or with status {@code 500 (Internal Server Error)} if the memoAction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/memo-actions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MemoAction> partialUpdateMemoAction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MemoAction memoAction
    ) throws URISyntaxException {
        log.debug("REST request to partial update MemoAction partially : {}, {}", id, memoAction);
        if (memoAction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, memoAction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!memoActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MemoAction> result = memoActionRepository
            .findById(memoAction.getId())
            .map(existingMemoAction -> {
                if (memoAction.getAction() != null) {
                    existingMemoAction.setAction(memoAction.getAction());
                }

                return existingMemoAction;
            })
            .map(memoActionRepository::save)
            .map(savedMemoAction -> {
                memoActionSearchRepository.save(savedMemoAction);

                return savedMemoAction;
            });

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, memoAction.getId().toString())
        );
    }

    /**
     * {@code GET  /memo-actions} : get all the memoActions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of memoActions in body.
     */
    @GetMapping("/memo-actions")
    public ResponseEntity<List<MemoAction>> getAllMemoActions(Pageable pageable) {
        log.debug("REST request to get a page of MemoActions");
        Page<MemoAction> page = memoActionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /memo-actions/:id} : get the "id" memoAction.
     *
     * @param id the id of the memoAction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the memoAction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/memo-actions/{id}")
    public ResponseEntity<MemoAction> getMemoAction(@PathVariable Long id) {
        log.debug("REST request to get MemoAction : {}", id);
        Optional<MemoAction> memoAction = memoActionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(memoAction);
    }

    /**
     * {@code DELETE  /memo-actions/:id} : delete the "id" memoAction.
     *
     * @param id the id of the memoAction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/memo-actions/{id}")
    public ResponseEntity<Void> deleteMemoAction(@PathVariable Long id) {
        log.debug("REST request to delete MemoAction : {}", id);
        memoActionRepository.deleteById(id);
        memoActionSearchRepository.deleteById(id);
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
    public ResponseEntity<List<MemoAction>> searchMemoActions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MemoActions for query {}", query);
        Page<MemoAction> page = memoActionSearchRepository.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
