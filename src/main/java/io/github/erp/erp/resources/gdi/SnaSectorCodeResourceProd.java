package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.repository.SnaSectorCodeRepository;
import io.github.erp.service.SnaSectorCodeQueryService;
import io.github.erp.service.SnaSectorCodeService;
import io.github.erp.service.criteria.SnaSectorCodeCriteria;
import io.github.erp.service.dto.SnaSectorCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SnaSectorCode}.
 */
@RestController("SnaSectorCodeResourceProd")
@RequestMapping("/api/granular-data")
public class SnaSectorCodeResourceProd {

    private final Logger log = LoggerFactory.getLogger(SnaSectorCodeResourceProd.class);

    private static final String ENTITY_NAME = "snaSectorCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SnaSectorCodeService snaSectorCodeService;

    private final SnaSectorCodeRepository snaSectorCodeRepository;

    private final SnaSectorCodeQueryService snaSectorCodeQueryService;

    public SnaSectorCodeResourceProd(
        SnaSectorCodeService snaSectorCodeService,
        SnaSectorCodeRepository snaSectorCodeRepository,
        SnaSectorCodeQueryService snaSectorCodeQueryService
    ) {
        this.snaSectorCodeService = snaSectorCodeService;
        this.snaSectorCodeRepository = snaSectorCodeRepository;
        this.snaSectorCodeQueryService = snaSectorCodeQueryService;
    }

    /**
     * {@code POST  /sna-sector-codes} : Create a new snaSectorCode.
     *
     * @param snaSectorCodeDTO the snaSectorCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new snaSectorCodeDTO, or with status {@code 400 (Bad Request)} if the snaSectorCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sna-sector-codes")
    public ResponseEntity<SnaSectorCodeDTO> createSnaSectorCode(@Valid @RequestBody SnaSectorCodeDTO snaSectorCodeDTO)
        throws URISyntaxException {
        log.debug("REST request to save SnaSectorCode : {}", snaSectorCodeDTO);
        if (snaSectorCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new snaSectorCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SnaSectorCodeDTO result = snaSectorCodeService.save(snaSectorCodeDTO);
        return ResponseEntity
            .created(new URI("/api/sna-sector-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sna-sector-codes/:id} : Updates an existing snaSectorCode.
     *
     * @param id the id of the snaSectorCodeDTO to save.
     * @param snaSectorCodeDTO the snaSectorCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated snaSectorCodeDTO,
     * or with status {@code 400 (Bad Request)} if the snaSectorCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the snaSectorCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sna-sector-codes/{id}")
    public ResponseEntity<SnaSectorCodeDTO> updateSnaSectorCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SnaSectorCodeDTO snaSectorCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SnaSectorCode : {}, {}", id, snaSectorCodeDTO);
        if (snaSectorCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, snaSectorCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!snaSectorCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SnaSectorCodeDTO result = snaSectorCodeService.save(snaSectorCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, snaSectorCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /sna-sector-codes/:id} : Partial updates given fields of an existing snaSectorCode, field will ignore if it is null
     *
     * @param id the id of the snaSectorCodeDTO to save.
     * @param snaSectorCodeDTO the snaSectorCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated snaSectorCodeDTO,
     * or with status {@code 400 (Bad Request)} if the snaSectorCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the snaSectorCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the snaSectorCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/sna-sector-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SnaSectorCodeDTO> partialUpdateSnaSectorCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SnaSectorCodeDTO snaSectorCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SnaSectorCode partially : {}, {}", id, snaSectorCodeDTO);
        if (snaSectorCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, snaSectorCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!snaSectorCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SnaSectorCodeDTO> result = snaSectorCodeService.partialUpdate(snaSectorCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, snaSectorCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /sna-sector-codes} : get all the snaSectorCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of snaSectorCodes in body.
     */
    @GetMapping("/sna-sector-codes")
    public ResponseEntity<List<SnaSectorCodeDTO>> getAllSnaSectorCodes(SnaSectorCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SnaSectorCodes by criteria: {}", criteria);
        Page<SnaSectorCodeDTO> page = snaSectorCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /sna-sector-codes/count} : count all the snaSectorCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/sna-sector-codes/count")
    public ResponseEntity<Long> countSnaSectorCodes(SnaSectorCodeCriteria criteria) {
        log.debug("REST request to count SnaSectorCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(snaSectorCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /sna-sector-codes/:id} : get the "id" snaSectorCode.
     *
     * @param id the id of the snaSectorCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the snaSectorCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sna-sector-codes/{id}")
    public ResponseEntity<SnaSectorCodeDTO> getSnaSectorCode(@PathVariable Long id) {
        log.debug("REST request to get SnaSectorCode : {}", id);
        Optional<SnaSectorCodeDTO> snaSectorCodeDTO = snaSectorCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(snaSectorCodeDTO);
    }

    /**
     * {@code DELETE  /sna-sector-codes/:id} : delete the "id" snaSectorCode.
     *
     * @param id the id of the snaSectorCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sna-sector-codes/{id}")
    public ResponseEntity<Void> deleteSnaSectorCode(@PathVariable Long id) {
        log.debug("REST request to delete SnaSectorCode : {}", id);
        snaSectorCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/sna-sector-codes?query=:query} : search for the snaSectorCode corresponding
     * to the query.
     *
     * @param query the query of the snaSectorCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/sna-sector-codes")
    public ResponseEntity<List<SnaSectorCodeDTO>> searchSnaSectorCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SnaSectorCodes for query {}", query);
        Page<SnaSectorCodeDTO> page = snaSectorCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
