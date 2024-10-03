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

import io.github.erp.repository.CrbSubscriptionStatusTypeCodeRepository;
import io.github.erp.service.CrbSubscriptionStatusTypeCodeQueryService;
import io.github.erp.service.CrbSubscriptionStatusTypeCodeService;
import io.github.erp.service.criteria.CrbSubscriptionStatusTypeCodeCriteria;
import io.github.erp.service.dto.CrbSubscriptionStatusTypeCodeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbSubscriptionStatusTypeCode}.
 */
@RestController
@RequestMapping("/api")
public class CrbSubscriptionStatusTypeCodeResource {

    private final Logger log = LoggerFactory.getLogger(CrbSubscriptionStatusTypeCodeResource.class);

    private static final String ENTITY_NAME = "crbSubscriptionStatusTypeCode";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbSubscriptionStatusTypeCodeService crbSubscriptionStatusTypeCodeService;

    private final CrbSubscriptionStatusTypeCodeRepository crbSubscriptionStatusTypeCodeRepository;

    private final CrbSubscriptionStatusTypeCodeQueryService crbSubscriptionStatusTypeCodeQueryService;

    public CrbSubscriptionStatusTypeCodeResource(
        CrbSubscriptionStatusTypeCodeService crbSubscriptionStatusTypeCodeService,
        CrbSubscriptionStatusTypeCodeRepository crbSubscriptionStatusTypeCodeRepository,
        CrbSubscriptionStatusTypeCodeQueryService crbSubscriptionStatusTypeCodeQueryService
    ) {
        this.crbSubscriptionStatusTypeCodeService = crbSubscriptionStatusTypeCodeService;
        this.crbSubscriptionStatusTypeCodeRepository = crbSubscriptionStatusTypeCodeRepository;
        this.crbSubscriptionStatusTypeCodeQueryService = crbSubscriptionStatusTypeCodeQueryService;
    }

    /**
     * {@code POST  /crb-subscription-status-type-codes} : Create a new crbSubscriptionStatusTypeCode.
     *
     * @param crbSubscriptionStatusTypeCodeDTO the crbSubscriptionStatusTypeCodeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbSubscriptionStatusTypeCodeDTO, or with status {@code 400 (Bad Request)} if the crbSubscriptionStatusTypeCode has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-subscription-status-type-codes")
    public ResponseEntity<CrbSubscriptionStatusTypeCodeDTO> createCrbSubscriptionStatusTypeCode(
        @Valid @RequestBody CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbSubscriptionStatusTypeCode : {}", crbSubscriptionStatusTypeCodeDTO);
        if (crbSubscriptionStatusTypeCodeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbSubscriptionStatusTypeCode cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbSubscriptionStatusTypeCodeDTO result = crbSubscriptionStatusTypeCodeService.save(crbSubscriptionStatusTypeCodeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-subscription-status-type-codes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-subscription-status-type-codes/:id} : Updates an existing crbSubscriptionStatusTypeCode.
     *
     * @param id the id of the crbSubscriptionStatusTypeCodeDTO to save.
     * @param crbSubscriptionStatusTypeCodeDTO the crbSubscriptionStatusTypeCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbSubscriptionStatusTypeCodeDTO,
     * or with status {@code 400 (Bad Request)} if the crbSubscriptionStatusTypeCodeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbSubscriptionStatusTypeCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-subscription-status-type-codes/{id}")
    public ResponseEntity<CrbSubscriptionStatusTypeCodeDTO> updateCrbSubscriptionStatusTypeCode(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbSubscriptionStatusTypeCode : {}, {}", id, crbSubscriptionStatusTypeCodeDTO);
        if (crbSubscriptionStatusTypeCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbSubscriptionStatusTypeCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbSubscriptionStatusTypeCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbSubscriptionStatusTypeCodeDTO result = crbSubscriptionStatusTypeCodeService.save(crbSubscriptionStatusTypeCodeDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbSubscriptionStatusTypeCodeDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /crb-subscription-status-type-codes/:id} : Partial updates given fields of an existing crbSubscriptionStatusTypeCode, field will ignore if it is null
     *
     * @param id the id of the crbSubscriptionStatusTypeCodeDTO to save.
     * @param crbSubscriptionStatusTypeCodeDTO the crbSubscriptionStatusTypeCodeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbSubscriptionStatusTypeCodeDTO,
     * or with status {@code 400 (Bad Request)} if the crbSubscriptionStatusTypeCodeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbSubscriptionStatusTypeCodeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbSubscriptionStatusTypeCodeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-subscription-status-type-codes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbSubscriptionStatusTypeCodeDTO> partialUpdateCrbSubscriptionStatusTypeCode(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbSubscriptionStatusTypeCodeDTO crbSubscriptionStatusTypeCodeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbSubscriptionStatusTypeCode partially : {}, {}", id, crbSubscriptionStatusTypeCodeDTO);
        if (crbSubscriptionStatusTypeCodeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbSubscriptionStatusTypeCodeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbSubscriptionStatusTypeCodeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbSubscriptionStatusTypeCodeDTO> result = crbSubscriptionStatusTypeCodeService.partialUpdate(
            crbSubscriptionStatusTypeCodeDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbSubscriptionStatusTypeCodeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-subscription-status-type-codes} : get all the crbSubscriptionStatusTypeCodes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbSubscriptionStatusTypeCodes in body.
     */
    @GetMapping("/crb-subscription-status-type-codes")
    public ResponseEntity<List<CrbSubscriptionStatusTypeCodeDTO>> getAllCrbSubscriptionStatusTypeCodes(
        CrbSubscriptionStatusTypeCodeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbSubscriptionStatusTypeCodes by criteria: {}", criteria);
        Page<CrbSubscriptionStatusTypeCodeDTO> page = crbSubscriptionStatusTypeCodeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-subscription-status-type-codes/count} : count all the crbSubscriptionStatusTypeCodes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-subscription-status-type-codes/count")
    public ResponseEntity<Long> countCrbSubscriptionStatusTypeCodes(CrbSubscriptionStatusTypeCodeCriteria criteria) {
        log.debug("REST request to count CrbSubscriptionStatusTypeCodes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbSubscriptionStatusTypeCodeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-subscription-status-type-codes/:id} : get the "id" crbSubscriptionStatusTypeCode.
     *
     * @param id the id of the crbSubscriptionStatusTypeCodeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbSubscriptionStatusTypeCodeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-subscription-status-type-codes/{id}")
    public ResponseEntity<CrbSubscriptionStatusTypeCodeDTO> getCrbSubscriptionStatusTypeCode(@PathVariable Long id) {
        log.debug("REST request to get CrbSubscriptionStatusTypeCode : {}", id);
        Optional<CrbSubscriptionStatusTypeCodeDTO> crbSubscriptionStatusTypeCodeDTO = crbSubscriptionStatusTypeCodeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbSubscriptionStatusTypeCodeDTO);
    }

    /**
     * {@code DELETE  /crb-subscription-status-type-codes/:id} : delete the "id" crbSubscriptionStatusTypeCode.
     *
     * @param id the id of the crbSubscriptionStatusTypeCodeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-subscription-status-type-codes/{id}")
    public ResponseEntity<Void> deleteCrbSubscriptionStatusTypeCode(@PathVariable Long id) {
        log.debug("REST request to delete CrbSubscriptionStatusTypeCode : {}", id);
        crbSubscriptionStatusTypeCodeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-subscription-status-type-codes?query=:query} : search for the crbSubscriptionStatusTypeCode corresponding
     * to the query.
     *
     * @param query the query of the crbSubscriptionStatusTypeCode search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-subscription-status-type-codes")
    public ResponseEntity<List<CrbSubscriptionStatusTypeCodeDTO>> searchCrbSubscriptionStatusTypeCodes(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CrbSubscriptionStatusTypeCodes for query {}", query);
        Page<CrbSubscriptionStatusTypeCodeDTO> page = crbSubscriptionStatusTypeCodeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
