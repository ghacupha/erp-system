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

import io.github.erp.repository.CrbAgingBandsRepository;
import io.github.erp.service.CrbAgingBandsQueryService;
import io.github.erp.service.CrbAgingBandsService;
import io.github.erp.service.criteria.CrbAgingBandsCriteria;
import io.github.erp.service.dto.CrbAgingBandsDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbAgingBands}.
 */
@RestController
@RequestMapping("/api")
public class CrbAgingBandsResource {

    private final Logger log = LoggerFactory.getLogger(CrbAgingBandsResource.class);

    private static final String ENTITY_NAME = "crbAgingBands";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbAgingBandsService crbAgingBandsService;

    private final CrbAgingBandsRepository crbAgingBandsRepository;

    private final CrbAgingBandsQueryService crbAgingBandsQueryService;

    public CrbAgingBandsResource(
        CrbAgingBandsService crbAgingBandsService,
        CrbAgingBandsRepository crbAgingBandsRepository,
        CrbAgingBandsQueryService crbAgingBandsQueryService
    ) {
        this.crbAgingBandsService = crbAgingBandsService;
        this.crbAgingBandsRepository = crbAgingBandsRepository;
        this.crbAgingBandsQueryService = crbAgingBandsQueryService;
    }

    /**
     * {@code POST  /crb-aging-bands} : Create a new crbAgingBands.
     *
     * @param crbAgingBandsDTO the crbAgingBandsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbAgingBandsDTO, or with status {@code 400 (Bad Request)} if the crbAgingBands has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-aging-bands")
    public ResponseEntity<CrbAgingBandsDTO> createCrbAgingBands(@Valid @RequestBody CrbAgingBandsDTO crbAgingBandsDTO)
        throws URISyntaxException {
        log.debug("REST request to save CrbAgingBands : {}", crbAgingBandsDTO);
        if (crbAgingBandsDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbAgingBands cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbAgingBandsDTO result = crbAgingBandsService.save(crbAgingBandsDTO);
        return ResponseEntity
            .created(new URI("/api/crb-aging-bands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-aging-bands/:id} : Updates an existing crbAgingBands.
     *
     * @param id the id of the crbAgingBandsDTO to save.
     * @param crbAgingBandsDTO the crbAgingBandsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbAgingBandsDTO,
     * or with status {@code 400 (Bad Request)} if the crbAgingBandsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbAgingBandsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-aging-bands/{id}")
    public ResponseEntity<CrbAgingBandsDTO> updateCrbAgingBands(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbAgingBandsDTO crbAgingBandsDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbAgingBands : {}, {}", id, crbAgingBandsDTO);
        if (crbAgingBandsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbAgingBandsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbAgingBandsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbAgingBandsDTO result = crbAgingBandsService.save(crbAgingBandsDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbAgingBandsDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-aging-bands/:id} : Partial updates given fields of an existing crbAgingBands, field will ignore if it is null
     *
     * @param id the id of the crbAgingBandsDTO to save.
     * @param crbAgingBandsDTO the crbAgingBandsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbAgingBandsDTO,
     * or with status {@code 400 (Bad Request)} if the crbAgingBandsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbAgingBandsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbAgingBandsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-aging-bands/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbAgingBandsDTO> partialUpdateCrbAgingBands(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbAgingBandsDTO crbAgingBandsDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbAgingBands partially : {}, {}", id, crbAgingBandsDTO);
        if (crbAgingBandsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbAgingBandsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbAgingBandsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbAgingBandsDTO> result = crbAgingBandsService.partialUpdate(crbAgingBandsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbAgingBandsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-aging-bands} : get all the crbAgingBands.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbAgingBands in body.
     */
    @GetMapping("/crb-aging-bands")
    public ResponseEntity<List<CrbAgingBandsDTO>> getAllCrbAgingBands(CrbAgingBandsCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CrbAgingBands by criteria: {}", criteria);
        Page<CrbAgingBandsDTO> page = crbAgingBandsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-aging-bands/count} : count all the crbAgingBands.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-aging-bands/count")
    public ResponseEntity<Long> countCrbAgingBands(CrbAgingBandsCriteria criteria) {
        log.debug("REST request to count CrbAgingBands by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbAgingBandsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-aging-bands/:id} : get the "id" crbAgingBands.
     *
     * @param id the id of the crbAgingBandsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbAgingBandsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-aging-bands/{id}")
    public ResponseEntity<CrbAgingBandsDTO> getCrbAgingBands(@PathVariable Long id) {
        log.debug("REST request to get CrbAgingBands : {}", id);
        Optional<CrbAgingBandsDTO> crbAgingBandsDTO = crbAgingBandsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbAgingBandsDTO);
    }

    /**
     * {@code DELETE  /crb-aging-bands/:id} : delete the "id" crbAgingBands.
     *
     * @param id the id of the crbAgingBandsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-aging-bands/{id}")
    public ResponseEntity<Void> deleteCrbAgingBands(@PathVariable Long id) {
        log.debug("REST request to delete CrbAgingBands : {}", id);
        crbAgingBandsService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-aging-bands?query=:query} : search for the crbAgingBands corresponding
     * to the query.
     *
     * @param query the query of the crbAgingBands search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-aging-bands")
    public ResponseEntity<List<CrbAgingBandsDTO>> searchCrbAgingBands(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbAgingBands for query {}", query);
        Page<CrbAgingBandsDTO> page = crbAgingBandsService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
