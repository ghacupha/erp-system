package io.github.erp.web.rest;

/*-
 * Erp System - Mark IV No 5 (Ehud Series) Server ver 1.3.6
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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.IsoCountryCodeRepository;
import io.github.erp.service.IsoCountryCodeQueryService;
import io.github.erp.service.IsoCountryCodeService;
import io.github.erp.service.criteria.IsoCountryCodeCriteria;
import io.github.erp.service.dto.IsoCountryCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.IsoCountryCode}.
 */
@RestController
@RequestMapping("/api")
public class IsoCountryCodeResource {

    private final Logger log = LoggerFactory.getLogger(IsoCountryCodeResource.class);

    private static final String ENTITY_NAME = "isoCountryCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IsoCountryCodeService isoCountryCodeService;

    private final IsoCountryCodeRepository isoCountryCodeRepository;

    private final IsoCountryCodeQueryService isoCountryCodeQueryService;

    public IsoCountryCodeResource(
        IsoCountryCodeService isoCountryCodeService,
        IsoCountryCodeRepository isoCountryCodeRepository,
        IsoCountryCodeQueryService isoCountryCodeQueryService
    ) {
        this.isoCountryCodeService = isoCountryCodeService;
        this.isoCountryCodeRepository = isoCountryCodeRepository;
        this.isoCountryCodeQueryService = isoCountryCodeQueryService;
    }

    /**
     * {@code POST  /iso-country-codes} : Create a new isoCountryCode.
     *
     * @param isoCountryCodeDTO the isoCountryCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new isoCountryCodeDTO, or with status {@code 400 (Bad Request)} if the isoCountryCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/iso-country-codes")
    public ResponseEntity<IsoCountryCodeDTO> createIsoCountryCode(@RequestBody IsoCountryCodeDTO isoCountryCodeDTO)
        throws URISyntaxException {
        log.debug("REST request to save IsoCountryCode : {}", isoCountryCodeDTO);
        if (isoCountryCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new isoCountryCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IsoCountryCodeDTO result = isoCountryCodeService.save(isoCountryCodeDTO);
        return ResponseEntity
            .created(new URI("/api/iso-country-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /iso-country-codes/:id} : Updates an existing isoCountryCode.
     *
     * @param id the id of the isoCountryCodeDTO to save.
     * @param isoCountryCodeDTO the isoCountryCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isoCountryCodeDTO,
     * or with status {@code 400 (Bad Request)} if the isoCountryCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the isoCountryCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/iso-country-codes/{id}")
    public ResponseEntity<IsoCountryCodeDTO> updateIsoCountryCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IsoCountryCodeDTO isoCountryCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IsoCountryCode : {}, {}", id, isoCountryCodeDTO);
        if (isoCountryCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isoCountryCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isoCountryCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IsoCountryCodeDTO result = isoCountryCodeService.save(isoCountryCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isoCountryCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /iso-country-codes/:id} : Partial updates given fields of an existing isoCountryCode, field will ignore if it is null
     *
     * @param id the id of the isoCountryCodeDTO to save.
     * @param isoCountryCodeDTO the isoCountryCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isoCountryCodeDTO,
     * or with status {@code 400 (Bad Request)} if the isoCountryCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the isoCountryCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the isoCountryCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/iso-country-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IsoCountryCodeDTO> partialUpdateIsoCountryCode(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IsoCountryCodeDTO isoCountryCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IsoCountryCode partially : {}, {}", id, isoCountryCodeDTO);
        if (isoCountryCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isoCountryCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isoCountryCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IsoCountryCodeDTO> result = isoCountryCodeService.partialUpdate(isoCountryCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isoCountryCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /iso-country-codes} : get all the isoCountryCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of isoCountryCodes in body.
     */
    @GetMapping("/iso-country-codes")
    public ResponseEntity<List<IsoCountryCodeDTO>> getAllIsoCountryCodes(IsoCountryCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get IsoCountryCodes by criteria: {}", criteria);
        Page<IsoCountryCodeDTO> page = isoCountryCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /iso-country-codes/count} : count all the isoCountryCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/iso-country-codes/count")
    public ResponseEntity<Long> countIsoCountryCodes(IsoCountryCodeCriteria criteria) {
        log.debug("REST request to count IsoCountryCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(isoCountryCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /iso-country-codes/:id} : get the "id" isoCountryCode.
     *
     * @param id the id of the isoCountryCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the isoCountryCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/iso-country-codes/{id}")
    public ResponseEntity<IsoCountryCodeDTO> getIsoCountryCode(@PathVariable Long id) {
        log.debug("REST request to get IsoCountryCode : {}", id);
        Optional<IsoCountryCodeDTO> isoCountryCodeDTO = isoCountryCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(isoCountryCodeDTO);
    }

    /**
     * {@code DELETE  /iso-country-codes/:id} : delete the "id" isoCountryCode.
     *
     * @param id the id of the isoCountryCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/iso-country-codes/{id}")
    public ResponseEntity<Void> deleteIsoCountryCode(@PathVariable Long id) {
        log.debug("REST request to delete IsoCountryCode : {}", id);
        isoCountryCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/iso-country-codes?query=:query} : search for the isoCountryCode corresponding
     * to the query.
     *
     * @param query the query of the isoCountryCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/iso-country-codes")
    public ResponseEntity<List<IsoCountryCodeDTO>> searchIsoCountryCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IsoCountryCodes for query {}", query);
        Page<IsoCountryCodeDTO> page = isoCountryCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
