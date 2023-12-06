package io.github.erp.web.rest;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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

import io.github.erp.repository.InterbankSectorCodeRepository;
import io.github.erp.service.InterbankSectorCodeQueryService;
import io.github.erp.service.InterbankSectorCodeService;
import io.github.erp.service.criteria.InterbankSectorCodeCriteria;
import io.github.erp.service.dto.InterbankSectorCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.InterbankSectorCode}.
 */
@RestController
@RequestMapping("/api")
public class InterbankSectorCodeResource {

    private final Logger log = LoggerFactory.getLogger(InterbankSectorCodeResource.class);

    private static final String ENTITY_NAME = "interbankSectorCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterbankSectorCodeService interbankSectorCodeService;

    private final InterbankSectorCodeRepository interbankSectorCodeRepository;

    private final InterbankSectorCodeQueryService interbankSectorCodeQueryService;

    public InterbankSectorCodeResource(
        InterbankSectorCodeService interbankSectorCodeService,
        InterbankSectorCodeRepository interbankSectorCodeRepository,
        InterbankSectorCodeQueryService interbankSectorCodeQueryService
    ) {
        this.interbankSectorCodeService = interbankSectorCodeService;
        this.interbankSectorCodeRepository = interbankSectorCodeRepository;
        this.interbankSectorCodeQueryService = interbankSectorCodeQueryService;
    }

    /**
     * {@code POST  /interbank-sector-codes} : Create a new interbankSectorCode.
     *
     * @param interbankSectorCodeDTO the interbankSectorCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interbankSectorCodeDTO, or with status {@code 400 (Bad Request)} if the interbankSectorCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interbank-sector-codes")
    public ResponseEntity<InterbankSectorCodeDTO> createInterbankSectorCode(
        @Valid @RequestBody InterbankSectorCodeDTO interbankSectorCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save InterbankSectorCode : {}", interbankSectorCodeDTO);
        if (interbankSectorCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new interbankSectorCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterbankSectorCodeDTO result = interbankSectorCodeService.save(interbankSectorCodeDTO);
        return ResponseEntity
            .created(new URI("/api/interbank-sector-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interbank-sector-codes/:id} : Updates an existing interbankSectorCode.
     *
     * @param id the id of the interbankSectorCodeDTO to save.
     * @param interbankSectorCodeDTO the interbankSectorCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interbankSectorCodeDTO,
     * or with status {@code 400 (Bad Request)} if the interbankSectorCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interbankSectorCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interbank-sector-codes/{id}")
    public ResponseEntity<InterbankSectorCodeDTO> updateInterbankSectorCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InterbankSectorCodeDTO interbankSectorCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InterbankSectorCode : {}, {}", id, interbankSectorCodeDTO);
        if (interbankSectorCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interbankSectorCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interbankSectorCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InterbankSectorCodeDTO result = interbankSectorCodeService.save(interbankSectorCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interbankSectorCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /interbank-sector-codes/:id} : Partial updates given fields of an existing interbankSectorCode, field will ignore if it is null
     *
     * @param id the id of the interbankSectorCodeDTO to save.
     * @param interbankSectorCodeDTO the interbankSectorCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interbankSectorCodeDTO,
     * or with status {@code 400 (Bad Request)} if the interbankSectorCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the interbankSectorCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the interbankSectorCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/interbank-sector-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InterbankSectorCodeDTO> partialUpdateInterbankSectorCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InterbankSectorCodeDTO interbankSectorCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InterbankSectorCode partially : {}, {}", id, interbankSectorCodeDTO);
        if (interbankSectorCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interbankSectorCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interbankSectorCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InterbankSectorCodeDTO> result = interbankSectorCodeService.partialUpdate(interbankSectorCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interbankSectorCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /interbank-sector-codes} : get all the interbankSectorCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interbankSectorCodes in body.
     */
    @GetMapping("/interbank-sector-codes")
    public ResponseEntity<List<InterbankSectorCodeDTO>> getAllInterbankSectorCodes(
        InterbankSectorCodeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get InterbankSectorCodes by criteria: {}", criteria);
        Page<InterbankSectorCodeDTO> page = interbankSectorCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /interbank-sector-codes/count} : count all the interbankSectorCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/interbank-sector-codes/count")
    public ResponseEntity<Long> countInterbankSectorCodes(InterbankSectorCodeCriteria criteria) {
        log.debug("REST request to count InterbankSectorCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(interbankSectorCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /interbank-sector-codes/:id} : get the "id" interbankSectorCode.
     *
     * @param id the id of the interbankSectorCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interbankSectorCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interbank-sector-codes/{id}")
    public ResponseEntity<InterbankSectorCodeDTO> getInterbankSectorCode(@PathVariable Long id) {
        log.debug("REST request to get InterbankSectorCode : {}", id);
        Optional<InterbankSectorCodeDTO> interbankSectorCodeDTO = interbankSectorCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interbankSectorCodeDTO);
    }

    /**
     * {@code DELETE  /interbank-sector-codes/:id} : delete the "id" interbankSectorCode.
     *
     * @param id the id of the interbankSectorCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interbank-sector-codes/{id}")
    public ResponseEntity<Void> deleteInterbankSectorCode(@PathVariable Long id) {
        log.debug("REST request to delete InterbankSectorCode : {}", id);
        interbankSectorCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/interbank-sector-codes?query=:query} : search for the interbankSectorCode corresponding
     * to the query.
     *
     * @param query the query of the interbankSectorCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/interbank-sector-codes")
    public ResponseEntity<List<InterbankSectorCodeDTO>> searchInterbankSectorCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InterbankSectorCodes for query {}", query);
        Page<InterbankSectorCodeDTO> page = interbankSectorCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
