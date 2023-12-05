package io.github.erp.erp.resources;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
import io.github.erp.repository.CountyCodeRepository;
import io.github.erp.service.CountyCodeQueryService;
import io.github.erp.service.CountyCodeService;
import io.github.erp.service.criteria.CountyCodeCriteria;
import io.github.erp.service.dto.CountyCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CountyCode}.
 */
@RestController("countyCodeResourceProd")
@RequestMapping("/api/granular-data")
public class CountyCodeResourceProd {

    private final Logger log = LoggerFactory.getLogger(CountyCodeResourceProd.class);

    private static final String ENTITY_NAME = "countyCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CountyCodeService countyCodeService;

    private final CountyCodeRepository countyCodeRepository;

    private final CountyCodeQueryService countyCodeQueryService;

    public CountyCodeResourceProd(
        CountyCodeService countyCodeService,
        CountyCodeRepository countyCodeRepository,
        CountyCodeQueryService countyCodeQueryService
    ) {
        this.countyCodeService = countyCodeService;
        this.countyCodeRepository = countyCodeRepository;
        this.countyCodeQueryService = countyCodeQueryService;
    }

    /**
     * {@code POST  /county-codes} : Create a new countyCode.
     *
     * @param countyCodeDTO the countyCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new countyCodeDTO, or with status {@code 400 (Bad Request)} if the countyCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/county-codes")
    public ResponseEntity<CountyCodeDTO> createCountyCode(@Valid @RequestBody CountyCodeDTO countyCodeDTO) throws URISyntaxException {
        log.debug("REST request to save CountyCode : {}", countyCodeDTO);
        if (countyCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new countyCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CountyCodeDTO result = countyCodeService.save(countyCodeDTO);
        return ResponseEntity
            .created(new URI("/api/county-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /county-codes/:id} : Updates an existing countyCode.
     *
     * @param id the id of the countyCodeDTO to save.
     * @param countyCodeDTO the countyCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyCodeDTO,
     * or with status {@code 400 (Bad Request)} if the countyCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the countyCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/county-codes/{id}")
    public ResponseEntity<CountyCodeDTO> updateCountyCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CountyCodeDTO countyCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CountyCode : {}, {}", id, countyCodeDTO);
        if (countyCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CountyCodeDTO result = countyCodeService.save(countyCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /county-codes/:id} : Partial updates given fields of an existing countyCode, field will ignore if it is null
     *
     * @param id the id of the countyCodeDTO to save.
     * @param countyCodeDTO the countyCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated countyCodeDTO,
     * or with status {@code 400 (Bad Request)} if the countyCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the countyCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the countyCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/county-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CountyCodeDTO> partialUpdateCountyCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CountyCodeDTO countyCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CountyCode partially : {}, {}", id, countyCodeDTO);
        if (countyCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, countyCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!countyCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CountyCodeDTO> result = countyCodeService.partialUpdate(countyCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, countyCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /county-codes} : get all the countyCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of countyCodes in body.
     */
    @GetMapping("/county-codes")
    public ResponseEntity<List<CountyCodeDTO>> getAllCountyCodes(CountyCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CountyCodes by criteria: {}", criteria);
        Page<CountyCodeDTO> page = countyCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /county-codes/count} : count all the countyCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/county-codes/count")
    public ResponseEntity<Long> countCountyCodes(CountyCodeCriteria criteria) {
        log.debug("REST request to count CountyCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(countyCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /county-codes/:id} : get the "id" countyCode.
     *
     * @param id the id of the countyCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the countyCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/county-codes/{id}")
    public ResponseEntity<CountyCodeDTO> getCountyCode(@PathVariable Long id) {
        log.debug("REST request to get CountyCode : {}", id);
        Optional<CountyCodeDTO> countyCodeDTO = countyCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(countyCodeDTO);
    }

    /**
     * {@code DELETE  /county-codes/:id} : delete the "id" countyCode.
     *
     * @param id the id of the countyCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/county-codes/{id}")
    public ResponseEntity<Void> deleteCountyCode(@PathVariable Long id) {
        log.debug("REST request to delete CountyCode : {}", id);
        countyCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/county-codes?query=:query} : search for the countyCode corresponding
     * to the query.
     *
     * @param query the query of the countyCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/county-codes")
    public ResponseEntity<List<CountyCodeDTO>> searchCountyCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CountyCodes for query {}", query);
        Page<CountyCodeDTO> page = countyCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
