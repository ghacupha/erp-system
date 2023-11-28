package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import io.github.erp.repository.CrbAmountCategoryBandRepository;
import io.github.erp.service.CrbAmountCategoryBandQueryService;
import io.github.erp.service.CrbAmountCategoryBandService;
import io.github.erp.service.criteria.CrbAmountCategoryBandCriteria;
import io.github.erp.service.dto.CrbAmountCategoryBandDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbAmountCategoryBand}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CrbAmountCategoryBandResourceProd {

    private final Logger log = LoggerFactory.getLogger(CrbAmountCategoryBandResourceProd.class);

    private static final String ENTITY_NAME = "crbAmountCategoryBand";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbAmountCategoryBandService crbAmountCategoryBandService;

    private final CrbAmountCategoryBandRepository crbAmountCategoryBandRepository;

    private final CrbAmountCategoryBandQueryService crbAmountCategoryBandQueryService;

    public CrbAmountCategoryBandResourceProd(
        CrbAmountCategoryBandService crbAmountCategoryBandService,
        CrbAmountCategoryBandRepository crbAmountCategoryBandRepository,
        CrbAmountCategoryBandQueryService crbAmountCategoryBandQueryService
    ) {
        this.crbAmountCategoryBandService = crbAmountCategoryBandService;
        this.crbAmountCategoryBandRepository = crbAmountCategoryBandRepository;
        this.crbAmountCategoryBandQueryService = crbAmountCategoryBandQueryService;
    }

    /**
     * {@code POST  /crb-amount-category-bands} : Create a new crbAmountCategoryBand.
     *
     * @param crbAmountCategoryBandDTO the crbAmountCategoryBandDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbAmountCategoryBandDTO, or with status {@code 400 (Bad Request)} if the crbAmountCategoryBand has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-amount-category-bands")
    public ResponseEntity<CrbAmountCategoryBandDTO> createCrbAmountCategoryBand(
        @Valid @RequestBody CrbAmountCategoryBandDTO crbAmountCategoryBandDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbAmountCategoryBand : {}", crbAmountCategoryBandDTO);
        if (crbAmountCategoryBandDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbAmountCategoryBand cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbAmountCategoryBandDTO result = crbAmountCategoryBandService.save(crbAmountCategoryBandDTO);
        return ResponseEntity
            .created(new URI("/api/crb-amount-category-bands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-amount-category-bands/:id} : Updates an existing crbAmountCategoryBand.
     *
     * @param id the id of the crbAmountCategoryBandDTO to save.
     * @param crbAmountCategoryBandDTO the crbAmountCategoryBandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbAmountCategoryBandDTO,
     * or with status {@code 400 (Bad Request)} if the crbAmountCategoryBandDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbAmountCategoryBandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-amount-category-bands/{id}")
    public ResponseEntity<CrbAmountCategoryBandDTO> updateCrbAmountCategoryBand(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbAmountCategoryBandDTO crbAmountCategoryBandDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbAmountCategoryBand : {}, {}", id, crbAmountCategoryBandDTO);
        if (crbAmountCategoryBandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbAmountCategoryBandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbAmountCategoryBandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbAmountCategoryBandDTO result = crbAmountCategoryBandService.save(crbAmountCategoryBandDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbAmountCategoryBandDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-amount-category-bands/:id} : Partial updates given fields of an existing crbAmountCategoryBand, field will ignore if it is null
     *
     * @param id the id of the crbAmountCategoryBandDTO to save.
     * @param crbAmountCategoryBandDTO the crbAmountCategoryBandDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbAmountCategoryBandDTO,
     * or with status {@code 400 (Bad Request)} if the crbAmountCategoryBandDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbAmountCategoryBandDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbAmountCategoryBandDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-amount-category-bands/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbAmountCategoryBandDTO> partialUpdateCrbAmountCategoryBand(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbAmountCategoryBandDTO crbAmountCategoryBandDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbAmountCategoryBand partially : {}, {}", id, crbAmountCategoryBandDTO);
        if (crbAmountCategoryBandDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbAmountCategoryBandDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbAmountCategoryBandRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbAmountCategoryBandDTO> result = crbAmountCategoryBandService.partialUpdate(crbAmountCategoryBandDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbAmountCategoryBandDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-amount-category-bands} : get all the crbAmountCategoryBands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbAmountCategoryBands in body.
     */
    @GetMapping("/crb-amount-category-bands")
    public ResponseEntity<List<CrbAmountCategoryBandDTO>> getAllCrbAmountCategoryBands(
        CrbAmountCategoryBandCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbAmountCategoryBands by criteria: {}", criteria);
        Page<CrbAmountCategoryBandDTO> page = crbAmountCategoryBandQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-amount-category-bands/count} : count all the crbAmountCategoryBands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-amount-category-bands/count")
    public ResponseEntity<Long> countCrbAmountCategoryBands(CrbAmountCategoryBandCriteria criteria) {
        log.debug("REST request to count CrbAmountCategoryBands by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbAmountCategoryBandQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-amount-category-bands/:id} : get the "id" crbAmountCategoryBand.
     *
     * @param id the id of the crbAmountCategoryBandDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbAmountCategoryBandDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-amount-category-bands/{id}")
    public ResponseEntity<CrbAmountCategoryBandDTO> getCrbAmountCategoryBand(@PathVariable Long id) {
        log.debug("REST request to get CrbAmountCategoryBand : {}", id);
        Optional<CrbAmountCategoryBandDTO> crbAmountCategoryBandDTO = crbAmountCategoryBandService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbAmountCategoryBandDTO);
    }

    /**
     * {@code DELETE  /crb-amount-category-bands/:id} : delete the "id" crbAmountCategoryBand.
     *
     * @param id the id of the crbAmountCategoryBandDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-amount-category-bands/{id}")
    public ResponseEntity<Void> deleteCrbAmountCategoryBand(@PathVariable Long id) {
        log.debug("REST request to delete CrbAmountCategoryBand : {}", id);
        crbAmountCategoryBandService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-amount-category-bands?query=:query} : search for the crbAmountCategoryBand corresponding
     * to the query.
     *
     * @param query the query of the crbAmountCategoryBand search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-amount-category-bands")
    public ResponseEntity<List<CrbAmountCategoryBandDTO>> searchCrbAmountCategoryBands(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbAmountCategoryBands for query {}", query);
        Page<CrbAmountCategoryBandDTO> page = crbAmountCategoryBandService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
