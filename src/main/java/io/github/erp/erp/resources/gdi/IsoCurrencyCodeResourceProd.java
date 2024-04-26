package io.github.erp.erp.resources.gdi;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.repository.IsoCurrencyCodeRepository;
import io.github.erp.service.IsoCurrencyCodeQueryService;
import io.github.erp.service.IsoCurrencyCodeService;
import io.github.erp.service.criteria.IsoCurrencyCodeCriteria;
import io.github.erp.service.dto.IsoCurrencyCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.IsoCurrencyCode}.
 */
@RestController("IsoCurrencyCodeResourceProd")
@RequestMapping("/api/granular-data")
public class IsoCurrencyCodeResourceProd {

    private final Logger log = LoggerFactory.getLogger(IsoCurrencyCodeResourceProd.class);

    private static final String ENTITY_NAME = "isoCurrencyCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IsoCurrencyCodeService isoCurrencyCodeService;

    private final IsoCurrencyCodeRepository isoCurrencyCodeRepository;

    private final IsoCurrencyCodeQueryService isoCurrencyCodeQueryService;

    public IsoCurrencyCodeResourceProd(
        IsoCurrencyCodeService isoCurrencyCodeService,
        IsoCurrencyCodeRepository isoCurrencyCodeRepository,
        IsoCurrencyCodeQueryService isoCurrencyCodeQueryService
    ) {
        this.isoCurrencyCodeService = isoCurrencyCodeService;
        this.isoCurrencyCodeRepository = isoCurrencyCodeRepository;
        this.isoCurrencyCodeQueryService = isoCurrencyCodeQueryService;
    }

    /**
     * {@code POST  /iso-currency-codes} : Create a new isoCurrencyCode.
     *
     * @param isoCurrencyCodeDTO the isoCurrencyCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new isoCurrencyCodeDTO, or with status {@code 400 (Bad Request)} if the isoCurrencyCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/iso-currency-codes")
    public ResponseEntity<IsoCurrencyCodeDTO> createIsoCurrencyCode(@Valid @RequestBody IsoCurrencyCodeDTO isoCurrencyCodeDTO)
        throws URISyntaxException {
        log.debug("REST request to save IsoCurrencyCode : {}", isoCurrencyCodeDTO);
        if (isoCurrencyCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new isoCurrencyCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IsoCurrencyCodeDTO result = isoCurrencyCodeService.save(isoCurrencyCodeDTO);
        return ResponseEntity
            .created(new URI("/api/iso-currency-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /iso-currency-codes/:id} : Updates an existing isoCurrencyCode.
     *
     * @param id the id of the isoCurrencyCodeDTO to save.
     * @param isoCurrencyCodeDTO the isoCurrencyCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isoCurrencyCodeDTO,
     * or with status {@code 400 (Bad Request)} if the isoCurrencyCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the isoCurrencyCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/iso-currency-codes/{id}")
    public ResponseEntity<IsoCurrencyCodeDTO> updateIsoCurrencyCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IsoCurrencyCodeDTO isoCurrencyCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IsoCurrencyCode : {}, {}", id, isoCurrencyCodeDTO);
        if (isoCurrencyCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isoCurrencyCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isoCurrencyCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IsoCurrencyCodeDTO result = isoCurrencyCodeService.save(isoCurrencyCodeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isoCurrencyCodeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /iso-currency-codes/:id} : Partial updates given fields of an existing isoCurrencyCode, field will ignore if it is null
     *
     * @param id the id of the isoCurrencyCodeDTO to save.
     * @param isoCurrencyCodeDTO the isoCurrencyCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isoCurrencyCodeDTO,
     * or with status {@code 400 (Bad Request)} if the isoCurrencyCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the isoCurrencyCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the isoCurrencyCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/iso-currency-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IsoCurrencyCodeDTO> partialUpdateIsoCurrencyCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IsoCurrencyCodeDTO isoCurrencyCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IsoCurrencyCode partially : {}, {}", id, isoCurrencyCodeDTO);
        if (isoCurrencyCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isoCurrencyCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isoCurrencyCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IsoCurrencyCodeDTO> result = isoCurrencyCodeService.partialUpdate(isoCurrencyCodeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isoCurrencyCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /iso-currency-codes} : get all the isoCurrencyCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of isoCurrencyCodes in body.
     */
    @GetMapping("/iso-currency-codes")
    public ResponseEntity<List<IsoCurrencyCodeDTO>> getAllIsoCurrencyCodes(IsoCurrencyCodeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get IsoCurrencyCodes by criteria: {}", criteria);
        Page<IsoCurrencyCodeDTO> page = isoCurrencyCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /iso-currency-codes/count} : count all the isoCurrencyCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/iso-currency-codes/count")
    public ResponseEntity<Long> countIsoCurrencyCodes(IsoCurrencyCodeCriteria criteria) {
        log.debug("REST request to count IsoCurrencyCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(isoCurrencyCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /iso-currency-codes/:id} : get the "id" isoCurrencyCode.
     *
     * @param id the id of the isoCurrencyCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the isoCurrencyCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/iso-currency-codes/{id}")
    public ResponseEntity<IsoCurrencyCodeDTO> getIsoCurrencyCode(@PathVariable Long id) {
        log.debug("REST request to get IsoCurrencyCode : {}", id);
        Optional<IsoCurrencyCodeDTO> isoCurrencyCodeDTO = isoCurrencyCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(isoCurrencyCodeDTO);
    }

    /**
     * {@code DELETE  /iso-currency-codes/:id} : delete the "id" isoCurrencyCode.
     *
     * @param id the id of the isoCurrencyCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/iso-currency-codes/{id}")
    public ResponseEntity<Void> deleteIsoCurrencyCode(@PathVariable Long id) {
        log.debug("REST request to delete IsoCurrencyCode : {}", id);
        isoCurrencyCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/iso-currency-codes?query=:query} : search for the isoCurrencyCode corresponding
     * to the query.
     *
     * @param query the query of the isoCurrencyCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/iso-currency-codes")
    public ResponseEntity<List<IsoCurrencyCodeDTO>> searchIsoCurrencyCodes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IsoCurrencyCodes for query {}", query);
        Page<IsoCurrencyCodeDTO> page = isoCurrencyCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
