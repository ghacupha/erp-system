package io.github.erp.web.rest;

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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.MfbBranchCodeRepository;
import io.github.erp.service.MfbBranchCodeQueryService;
import io.github.erp.service.MfbBranchCodeService;
import io.github.erp.service.criteria.MfbBranchCodeCriteria;
import io.github.erp.service.dto.MfbBranchCodeDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
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
 * REST controller for managing {@link io.github.erp.domain.MfbBranchCode}.
 */
@RestController
@RequestMapping("/api")
public class MfbBranchCodeResource {

    private final Logger log = LoggerFactory.getLogger(MfbBranchCodeResource.class);

    private static final String ENTITY_NAME = "mfbBranchCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MfbBranchCodeService mfbBranchCodeService;

    private final MfbBranchCodeRepository mfbBranchCodeRepository;

    private final MfbBranchCodeQueryService mfbBranchCodeQueryService;

    public MfbBranchCodeResource(
        MfbBranchCodeService mfbBranchCodeService,
        MfbBranchCodeRepository mfbBranchCodeRepository,
        MfbBranchCodeQueryService mfbBranchCodeQueryService
    ) {
        this.mfbBranchCodeService = mfbBranchCodeService;
        this.mfbBranchCodeRepository = mfbBranchCodeRepository;
        this.mfbBranchCodeQueryService = mfbBranchCodeQueryService;
    }

    /**
     * {@code POST  /mfb-branch-codes} : Create a new mfbBranchCode.
     *
     * @param mfbBranchCodeDTO the mfbBranchCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new mfbBranchCodeDTO, or with status {@code 400 (Bad Request)} if the mfbBranchCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/mfb-branch-codes")
    public ResponseEntity<MfbBranchCodeDTO> createMfbBranchCode(@RequestBody MfbBranchCodeDTO mfbBranchCodeDTO) throws URISyntaxException {
        log.debug("REST request to save MfbBranchCode : {}", mfbBranchCodeDTO);
        if (mfbBranchCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new mfbBranchCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MfbBranchCodeDTO result = mfbBranchCodeService.save(mfbBranchCodeDTO);
        return ResponseEntity
            .created(new URI("/api/mfb-branch-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /mfb-branch-codes/:id} : Updates an existing mfbBranchCode.
     *
     * @param id the id of the mfbBranchCodeDTO to save.
     * @param mfbBranchCodeDTO the mfbBranchCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mfbBranchCodeDTO,
     * or with status {@code 400 (Bad Request)} if the mfbBranchCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the mfbBranchCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/mfb-branch-codes/{id}")
    public ResponseEntity<MfbBranchCodeDTO> updateMfbBranchCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MfbBranchCodeDTO mfbBranchCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update MfbBranchCode : {}, {}", id, mfbBranchCodeDTO);
        if (mfbBranchCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mfbBranchCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mfbBranchCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MfbBranchCodeDTO result = mfbBranchCodeService.save(mfbBranchCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mfbBranchCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /mfb-branch-codes/:id} : Partial updates given fields of an existing mfbBranchCode, field will ignore if it is null
     *
     * @param id the id of the mfbBranchCodeDTO to save.
     * @param mfbBranchCodeDTO the mfbBranchCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated mfbBranchCodeDTO,
     * or with status {@code 400 (Bad Request)} if the mfbBranchCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the mfbBranchCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the mfbBranchCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/mfb-branch-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MfbBranchCodeDTO> partialUpdateMfbBranchCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody MfbBranchCodeDTO mfbBranchCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update MfbBranchCode partially : {}, {}", id, mfbBranchCodeDTO);
        if (mfbBranchCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, mfbBranchCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!mfbBranchCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MfbBranchCodeDTO> result = mfbBranchCodeService.partialUpdate(mfbBranchCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, mfbBranchCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /mfb-branch-codes} : get all the mfbBranchCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of mfbBranchCodes in body.
     */
    @GetMapping("/mfb-branch-codes")
    public ResponseEntity<List<MfbBranchCodeDTO>> getAllMfbBranchCodes(MfbBranchCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get MfbBranchCodes by criteria: {}", criteria);
        Page<MfbBranchCodeDTO> page = mfbBranchCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /mfb-branch-codes/count} : count all the mfbBranchCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/mfb-branch-codes/count")
    public ResponseEntity<Long> countMfbBranchCodes(MfbBranchCodeCriteria criteria) {
        log.debug("REST request to count MfbBranchCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(mfbBranchCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /mfb-branch-codes/:id} : get the "id" mfbBranchCode.
     *
     * @param id the id of the mfbBranchCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the mfbBranchCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/mfb-branch-codes/{id}")
    public ResponseEntity<MfbBranchCodeDTO> getMfbBranchCode(@PathVariable Long id) {
        log.debug("REST request to get MfbBranchCode : {}", id);
        Optional<MfbBranchCodeDTO> mfbBranchCodeDTO = mfbBranchCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(mfbBranchCodeDTO);
    }

    /**
     * {@code DELETE  /mfb-branch-codes/:id} : delete the "id" mfbBranchCode.
     *
     * @param id the id of the mfbBranchCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/mfb-branch-codes/{id}")
    public ResponseEntity<Void> deleteMfbBranchCode(@PathVariable Long id) {
        log.debug("REST request to delete MfbBranchCode : {}", id);
        mfbBranchCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/mfb-branch-codes?query=:query} : search for the mfbBranchCode corresponding
     * to the query.
     *
     * @param query the query of the mfbBranchCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/mfb-branch-codes")
    public ResponseEntity<List<MfbBranchCodeDTO>> searchMfbBranchCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of MfbBranchCodes for query {}", query);
        Page<MfbBranchCodeDTO> page = mfbBranchCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
