package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.repository.CrbGlCodeRepository;
import io.github.erp.service.CrbGlCodeQueryService;
import io.github.erp.service.CrbGlCodeService;
import io.github.erp.service.criteria.CrbGlCodeCriteria;
import io.github.erp.service.dto.CrbGlCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbGlCode}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CrbGlCodeResourceProd {

    private final Logger log = LoggerFactory.getLogger(CrbGlCodeResourceProd.class);

    private static final String ENTITY_NAME = "crbGlCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbGlCodeService crbGlCodeService;

    private final CrbGlCodeRepository crbGlCodeRepository;

    private final CrbGlCodeQueryService crbGlCodeQueryService;

    public CrbGlCodeResourceProd(
        CrbGlCodeService crbGlCodeService,
        CrbGlCodeRepository crbGlCodeRepository,
        CrbGlCodeQueryService crbGlCodeQueryService
    ) {
        this.crbGlCodeService = crbGlCodeService;
        this.crbGlCodeRepository = crbGlCodeRepository;
        this.crbGlCodeQueryService = crbGlCodeQueryService;
    }

    /**
     * {@code POST  /crb-gl-codes} : Create a new crbGlCode.
     *
     * @param crbGlCodeDTO the crbGlCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbGlCodeDTO, or with status {@code 400 (Bad Request)} if the crbGlCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-gl-codes")
    public ResponseEntity<CrbGlCodeDTO> createCrbGlCode(@Valid @RequestBody CrbGlCodeDTO crbGlCodeDTO) throws URISyntaxException {
        log.debug("REST request to save CrbGlCode : {}", crbGlCodeDTO);
        if (crbGlCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbGlCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbGlCodeDTO result = crbGlCodeService.save(crbGlCodeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-gl-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-gl-codes/:id} : Updates an existing crbGlCode.
     *
     * @param id the id of the crbGlCodeDTO to save.
     * @param crbGlCodeDTO the crbGlCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbGlCodeDTO,
     * or with status {@code 400 (Bad Request)} if the crbGlCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbGlCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-gl-codes/{id}")
    public ResponseEntity<CrbGlCodeDTO> updateCrbGlCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbGlCodeDTO crbGlCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbGlCode : {}, {}", id, crbGlCodeDTO);
        if (crbGlCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbGlCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbGlCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbGlCodeDTO result = crbGlCodeService.save(crbGlCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbGlCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-gl-codes/:id} : Partial updates given fields of an existing crbGlCode, field will ignore if it is null
     *
     * @param id the id of the crbGlCodeDTO to save.
     * @param crbGlCodeDTO the crbGlCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbGlCodeDTO,
     * or with status {@code 400 (Bad Request)} if the crbGlCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbGlCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbGlCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-gl-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbGlCodeDTO> partialUpdateCrbGlCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbGlCodeDTO crbGlCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbGlCode partially : {}, {}", id, crbGlCodeDTO);
        if (crbGlCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbGlCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbGlCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbGlCodeDTO> result = crbGlCodeService.partialUpdate(crbGlCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbGlCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-gl-codes} : get all the crbGlCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbGlCodes in body.
     */
    @GetMapping("/crb-gl-codes")
    public ResponseEntity<List<CrbGlCodeDTO>> getAllCrbGlCodes(CrbGlCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CrbGlCodes by criteria: {}", criteria);
        Page<CrbGlCodeDTO> page = crbGlCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-gl-codes/count} : count all the crbGlCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-gl-codes/count")
    public ResponseEntity<Long> countCrbGlCodes(CrbGlCodeCriteria criteria) {
        log.debug("REST request to count CrbGlCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbGlCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-gl-codes/:id} : get the "id" crbGlCode.
     *
     * @param id the id of the crbGlCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbGlCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-gl-codes/{id}")
    public ResponseEntity<CrbGlCodeDTO> getCrbGlCode(@PathVariable Long id) {
        log.debug("REST request to get CrbGlCode : {}", id);
        Optional<CrbGlCodeDTO> crbGlCodeDTO = crbGlCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbGlCodeDTO);
    }

    /**
     * {@code DELETE  /crb-gl-codes/:id} : delete the "id" crbGlCode.
     *
     * @param id the id of the crbGlCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-gl-codes/{id}")
    public ResponseEntity<Void> deleteCrbGlCode(@PathVariable Long id) {
        log.debug("REST request to delete CrbGlCode : {}", id);
        crbGlCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-gl-codes?query=:query} : search for the crbGlCode corresponding
     * to the query.
     *
     * @param query the query of the crbGlCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-gl-codes")
    public ResponseEntity<List<CrbGlCodeDTO>> searchCrbGlCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbGlCodes for query {}", query);
        Page<CrbGlCodeDTO> page = crbGlCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
