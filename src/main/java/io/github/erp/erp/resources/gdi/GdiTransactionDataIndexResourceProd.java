package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import io.github.erp.repository.GdiTransactionDataIndexRepository;
import io.github.erp.service.GdiTransactionDataIndexQueryService;
import io.github.erp.service.GdiTransactionDataIndexService;
import io.github.erp.service.criteria.GdiTransactionDataIndexCriteria;
import io.github.erp.service.dto.GdiTransactionDataIndexDTO;
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
 * REST controller for managing {@link io.github.erp.domain.GdiTransactionDataIndex}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class GdiTransactionDataIndexResourceProd {

    private final Logger log = LoggerFactory.getLogger(GdiTransactionDataIndexResourceProd.class);

    private static final String ENTITY_NAME = "gdiTransactionDataIndex";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GdiTransactionDataIndexService gdiTransactionDataIndexService;

    private final GdiTransactionDataIndexRepository gdiTransactionDataIndexRepository;

    private final GdiTransactionDataIndexQueryService gdiTransactionDataIndexQueryService;

    public GdiTransactionDataIndexResourceProd(
        GdiTransactionDataIndexService gdiTransactionDataIndexService,
        GdiTransactionDataIndexRepository gdiTransactionDataIndexRepository,
        GdiTransactionDataIndexQueryService gdiTransactionDataIndexQueryService
    ) {
        this.gdiTransactionDataIndexService = gdiTransactionDataIndexService;
        this.gdiTransactionDataIndexRepository = gdiTransactionDataIndexRepository;
        this.gdiTransactionDataIndexQueryService = gdiTransactionDataIndexQueryService;
    }

    /**
     * {@code POST  /gdi-transaction-data-indices} : Create a new gdiTransactionDataIndex.
     *
     * @param gdiTransactionDataIndexDTO the gdiTransactionDataIndexDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gdiTransactionDataIndexDTO, or with status {@code 400 (Bad Request)} if the gdiTransactionDataIndex has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gdi-transaction-data-indices")
    public ResponseEntity<GdiTransactionDataIndexDTO> createGdiTransactionDataIndex(
        @Valid @RequestBody GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO
    ) throws URISyntaxException {
        log.debug("REST request to save GdiTransactionDataIndex : {}", gdiTransactionDataIndexDTO);
        if (gdiTransactionDataIndexDTO.getId() != null) {
            throw new BadRequestAlertException("A new gdiTransactionDataIndex cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GdiTransactionDataIndexDTO result = gdiTransactionDataIndexService.save(gdiTransactionDataIndexDTO);
        return ResponseEntity
            .created(new URI("/api/gdi-transaction-data-indices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gdi-transaction-data-indices/:id} : Updates an existing gdiTransactionDataIndex.
     *
     * @param id the id of the gdiTransactionDataIndexDTO to save.
     * @param gdiTransactionDataIndexDTO the gdiTransactionDataIndexDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gdiTransactionDataIndexDTO,
     * or with status {@code 400 (Bad Request)} if the gdiTransactionDataIndexDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gdiTransactionDataIndexDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gdi-transaction-data-indices/{id}")
    public ResponseEntity<GdiTransactionDataIndexDTO> updateGdiTransactionDataIndex(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GdiTransactionDataIndex : {}, {}", id, gdiTransactionDataIndexDTO);
        if (gdiTransactionDataIndexDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gdiTransactionDataIndexDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gdiTransactionDataIndexRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GdiTransactionDataIndexDTO result = gdiTransactionDataIndexService.save(gdiTransactionDataIndexDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gdiTransactionDataIndexDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gdi-transaction-data-indices/:id} : Partial updates given fields of an existing gdiTransactionDataIndex, field will ignore if it is null
     *
     * @param id the id of the gdiTransactionDataIndexDTO to save.
     * @param gdiTransactionDataIndexDTO the gdiTransactionDataIndexDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gdiTransactionDataIndexDTO,
     * or with status {@code 400 (Bad Request)} if the gdiTransactionDataIndexDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gdiTransactionDataIndexDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gdiTransactionDataIndexDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gdi-transaction-data-indices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GdiTransactionDataIndexDTO> partialUpdateGdiTransactionDataIndex(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GdiTransactionDataIndexDTO gdiTransactionDataIndexDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GdiTransactionDataIndex partially : {}, {}", id, gdiTransactionDataIndexDTO);
        if (gdiTransactionDataIndexDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gdiTransactionDataIndexDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gdiTransactionDataIndexRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GdiTransactionDataIndexDTO> result = gdiTransactionDataIndexService.partialUpdate(gdiTransactionDataIndexDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gdiTransactionDataIndexDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gdi-transaction-data-indices} : get all the gdiTransactionDataIndices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gdiTransactionDataIndices in body.
     */
    @GetMapping("/gdi-transaction-data-indices")
    public ResponseEntity<List<GdiTransactionDataIndexDTO>> getAllGdiTransactionDataIndices(
        GdiTransactionDataIndexCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get GdiTransactionDataIndices by criteria: {}", criteria);
        Page<GdiTransactionDataIndexDTO> page = gdiTransactionDataIndexQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gdi-transaction-data-indices/count} : count all the gdiTransactionDataIndices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gdi-transaction-data-indices/count")
    public ResponseEntity<Long> countGdiTransactionDataIndices(GdiTransactionDataIndexCriteria criteria) {
        log.debug("REST request to count GdiTransactionDataIndices by criteria: {}", criteria);
        return ResponseEntity.ok().body(gdiTransactionDataIndexQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gdi-transaction-data-indices/:id} : get the "id" gdiTransactionDataIndex.
     *
     * @param id the id of the gdiTransactionDataIndexDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gdiTransactionDataIndexDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gdi-transaction-data-indices/{id}")
    public ResponseEntity<GdiTransactionDataIndexDTO> getGdiTransactionDataIndex(@PathVariable Long id) {
        log.debug("REST request to get GdiTransactionDataIndex : {}", id);
        Optional<GdiTransactionDataIndexDTO> gdiTransactionDataIndexDTO = gdiTransactionDataIndexService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gdiTransactionDataIndexDTO);
    }

    /**
     * {@code DELETE  /gdi-transaction-data-indices/:id} : delete the "id" gdiTransactionDataIndex.
     *
     * @param id the id of the gdiTransactionDataIndexDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gdi-transaction-data-indices/{id}")
    public ResponseEntity<Void> deleteGdiTransactionDataIndex(@PathVariable Long id) {
        log.debug("REST request to delete GdiTransactionDataIndex : {}", id);
        gdiTransactionDataIndexService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/gdi-transaction-data-indices?query=:query} : search for the gdiTransactionDataIndex corresponding
     * to the query.
     *
     * @param query the query of the gdiTransactionDataIndex search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/gdi-transaction-data-indices")
    public ResponseEntity<List<GdiTransactionDataIndexDTO>> searchGdiTransactionDataIndices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of GdiTransactionDataIndices for query {}", query);
        Page<GdiTransactionDataIndexDTO> page = gdiTransactionDataIndexService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
