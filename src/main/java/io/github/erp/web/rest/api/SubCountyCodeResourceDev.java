package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 23 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.repository.SubCountyCodeRepository;
import io.github.erp.service.SubCountyCodeQueryService;
import io.github.erp.service.SubCountyCodeService;
import io.github.erp.service.criteria.SubCountyCodeCriteria;
import io.github.erp.service.dto.SubCountyCodeDTO;
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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.SubCountyCode}.
 */
@RestController
@RequestMapping("/api/dev")
public class SubCountyCodeResourceDev {

    private final Logger log = LoggerFactory.getLogger(SubCountyCodeResourceDev.class);

    private static final String ENTITY_NAME = "subCountyCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SubCountyCodeService subCountyCodeService;

    private final SubCountyCodeRepository subCountyCodeRepository;

    private final SubCountyCodeQueryService subCountyCodeQueryService;

    public SubCountyCodeResourceDev(
        SubCountyCodeService subCountyCodeService,
        SubCountyCodeRepository subCountyCodeRepository,
        SubCountyCodeQueryService subCountyCodeQueryService
    ) {
        this.subCountyCodeService = subCountyCodeService;
        this.subCountyCodeRepository = subCountyCodeRepository;
        this.subCountyCodeQueryService = subCountyCodeQueryService;
    }

    /**
     * {@code POST  /sub-county-codes} : Create a new subCountyCode.
     *
     * @param subCountyCodeDTO the subCountyCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new subCountyCodeDTO, or with status {@code 400 (Bad Request)} if the subCountyCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sub-county-codes")
    public ResponseEntity<SubCountyCodeDTO> createSubCountyCode(@RequestBody SubCountyCodeDTO subCountyCodeDTO) throws URISyntaxException {
        log.debug("REST request to save SubCountyCode : {}", subCountyCodeDTO);
        if (subCountyCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new subCountyCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SubCountyCodeDTO result = subCountyCodeService.save(subCountyCodeDTO);
        return ResponseEntity
            .created(new URI("/api/sub-county-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sub-county-codes/:id} : Updates an existing subCountyCode.
     *
     * @param id the id of the subCountyCodeDTO to save.
     * @param subCountyCodeDTO the subCountyCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCountyCodeDTO,
     * or with status {@code 400 (Bad Request)} if the subCountyCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the subCountyCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sub-county-codes/{id}")
    public ResponseEntity<SubCountyCodeDTO> updateSubCountyCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubCountyCodeDTO subCountyCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SubCountyCode : {}, {}", id, subCountyCodeDTO);
        if (subCountyCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCountyCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCountyCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SubCountyCodeDTO result = subCountyCodeService.save(subCountyCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subCountyCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sub-county-codes/:id} : Partial updates given fields of an existing subCountyCode, field will ignore if it is null
     *
     * @param id the id of the subCountyCodeDTO to save.
     * @param subCountyCodeDTO the subCountyCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated subCountyCodeDTO,
     * or with status {@code 400 (Bad Request)} if the subCountyCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the subCountyCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the subCountyCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sub-county-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SubCountyCodeDTO> partialUpdateSubCountyCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody SubCountyCodeDTO subCountyCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SubCountyCode partially : {}, {}", id, subCountyCodeDTO);
        if (subCountyCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, subCountyCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!subCountyCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SubCountyCodeDTO> result = subCountyCodeService.partialUpdate(subCountyCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, subCountyCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sub-county-codes} : get all the subCountyCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of subCountyCodes in body.
     */
    @GetMapping("/sub-county-codes")
    public ResponseEntity<List<SubCountyCodeDTO>> getAllSubCountyCodes(SubCountyCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SubCountyCodes by criteria: {}", criteria);
        Page<SubCountyCodeDTO> page = subCountyCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sub-county-codes/count} : count all the subCountyCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sub-county-codes/count")
    public ResponseEntity<Long> countSubCountyCodes(SubCountyCodeCriteria criteria) {
        log.debug("REST request to count SubCountyCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(subCountyCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sub-county-codes/:id} : get the "id" subCountyCode.
     *
     * @param id the id of the subCountyCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the subCountyCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sub-county-codes/{id}")
    public ResponseEntity<SubCountyCodeDTO> getSubCountyCode(@PathVariable Long id) {
        log.debug("REST request to get SubCountyCode : {}", id);
        Optional<SubCountyCodeDTO> subCountyCodeDTO = subCountyCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(subCountyCodeDTO);
    }

    /**
     * {@code DELETE  /sub-county-codes/:id} : delete the "id" subCountyCode.
     *
     * @param id the id of the subCountyCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sub-county-codes/{id}")
    public ResponseEntity<Void> deleteSubCountyCode(@PathVariable Long id) {
        log.debug("REST request to delete SubCountyCode : {}", id);
        subCountyCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/sub-county-codes?query=:query} : search for the subCountyCode corresponding
     * to the query.
     *
     * @param query the query of the subCountyCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/sub-county-codes")
    public ResponseEntity<List<SubCountyCodeDTO>> searchSubCountyCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SubCountyCodes for query {}", query);
        Page<SubCountyCodeDTO> page = subCountyCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
