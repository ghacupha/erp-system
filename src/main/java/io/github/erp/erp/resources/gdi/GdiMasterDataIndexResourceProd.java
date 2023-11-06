package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.repository.GdiMasterDataIndexRepository;
import io.github.erp.service.GdiMasterDataIndexQueryService;
import io.github.erp.service.GdiMasterDataIndexService;
import io.github.erp.service.criteria.GdiMasterDataIndexCriteria;
import io.github.erp.service.dto.GdiMasterDataIndexDTO;
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
 * REST controller for managing {@link io.github.erp.domain.GdiMasterDataIndex}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class GdiMasterDataIndexResourceProd {

    private final Logger log = LoggerFactory.getLogger(GdiMasterDataIndexResourceProd.class);

    private static final String ENTITY_NAME = "gdiMasterDataIndex";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GdiMasterDataIndexService gdiMasterDataIndexService;

    private final GdiMasterDataIndexRepository gdiMasterDataIndexRepository;

    private final GdiMasterDataIndexQueryService gdiMasterDataIndexQueryService;

    public GdiMasterDataIndexResourceProd(
        GdiMasterDataIndexService gdiMasterDataIndexService,
        GdiMasterDataIndexRepository gdiMasterDataIndexRepository,
        GdiMasterDataIndexQueryService gdiMasterDataIndexQueryService
    ) {
        this.gdiMasterDataIndexService = gdiMasterDataIndexService;
        this.gdiMasterDataIndexRepository = gdiMasterDataIndexRepository;
        this.gdiMasterDataIndexQueryService = gdiMasterDataIndexQueryService;
    }

    /**
     * {@code POST  /gdi-master-data-indices} : Create a new gdiMasterDataIndex.
     *
     * @param gdiMasterDataIndexDTO the gdiMasterDataIndexDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new gdiMasterDataIndexDTO, or with status {@code 400 (Bad Request)} if the gdiMasterDataIndex has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gdi-master-data-indices")
    public ResponseEntity<GdiMasterDataIndexDTO> createGdiMasterDataIndex(@Valid @RequestBody GdiMasterDataIndexDTO gdiMasterDataIndexDTO)
        throws URISyntaxException {
        log.debug("REST request to save GdiMasterDataIndex : {}", gdiMasterDataIndexDTO);
        if (gdiMasterDataIndexDTO.getId() != null) {
            throw new BadRequestAlertException("A new gdiMasterDataIndex cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GdiMasterDataIndexDTO result = gdiMasterDataIndexService.save(gdiMasterDataIndexDTO);
        return ResponseEntity
            .created(new URI("/api/gdi-master-data-indices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gdi-master-data-indices/:id} : Updates an existing gdiMasterDataIndex.
     *
     * @param id the id of the gdiMasterDataIndexDTO to save.
     * @param gdiMasterDataIndexDTO the gdiMasterDataIndexDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gdiMasterDataIndexDTO,
     * or with status {@code 400 (Bad Request)} if the gdiMasterDataIndexDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the gdiMasterDataIndexDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gdi-master-data-indices/{id}")
    public ResponseEntity<GdiMasterDataIndexDTO> updateGdiMasterDataIndex(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GdiMasterDataIndexDTO gdiMasterDataIndexDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GdiMasterDataIndex : {}, {}", id, gdiMasterDataIndexDTO);
        if (gdiMasterDataIndexDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gdiMasterDataIndexDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gdiMasterDataIndexRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GdiMasterDataIndexDTO result = gdiMasterDataIndexService.save(gdiMasterDataIndexDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gdiMasterDataIndexDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gdi-master-data-indices/:id} : Partial updates given fields of an existing gdiMasterDataIndex, field will ignore if it is null
     *
     * @param id the id of the gdiMasterDataIndexDTO to save.
     * @param gdiMasterDataIndexDTO the gdiMasterDataIndexDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated gdiMasterDataIndexDTO,
     * or with status {@code 400 (Bad Request)} if the gdiMasterDataIndexDTO is not valid,
     * or with status {@code 404 (Not Found)} if the gdiMasterDataIndexDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the gdiMasterDataIndexDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gdi-master-data-indices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GdiMasterDataIndexDTO> partialUpdateGdiMasterDataIndex(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GdiMasterDataIndexDTO gdiMasterDataIndexDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GdiMasterDataIndex partially : {}, {}", id, gdiMasterDataIndexDTO);
        if (gdiMasterDataIndexDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, gdiMasterDataIndexDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!gdiMasterDataIndexRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GdiMasterDataIndexDTO> result = gdiMasterDataIndexService.partialUpdate(gdiMasterDataIndexDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, gdiMasterDataIndexDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gdi-master-data-indices} : get all the gdiMasterDataIndices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of gdiMasterDataIndices in body.
     */
    @GetMapping("/gdi-master-data-indices")
    public ResponseEntity<List<GdiMasterDataIndexDTO>> getAllGdiMasterDataIndices(GdiMasterDataIndexCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GdiMasterDataIndices by criteria: {}", criteria);
        Page<GdiMasterDataIndexDTO> page = gdiMasterDataIndexQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gdi-master-data-indices/count} : count all the gdiMasterDataIndices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gdi-master-data-indices/count")
    public ResponseEntity<Long> countGdiMasterDataIndices(GdiMasterDataIndexCriteria criteria) {
        log.debug("REST request to count GdiMasterDataIndices by criteria: {}", criteria);
        return ResponseEntity.ok().body(gdiMasterDataIndexQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gdi-master-data-indices/:id} : get the "id" gdiMasterDataIndex.
     *
     * @param id the id of the gdiMasterDataIndexDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the gdiMasterDataIndexDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gdi-master-data-indices/{id}")
    public ResponseEntity<GdiMasterDataIndexDTO> getGdiMasterDataIndex(@PathVariable Long id) {
        log.debug("REST request to get GdiMasterDataIndex : {}", id);
        Optional<GdiMasterDataIndexDTO> gdiMasterDataIndexDTO = gdiMasterDataIndexService.findOne(id);
        return ResponseUtil.wrapOrNotFound(gdiMasterDataIndexDTO);
    }

    /**
     * {@code DELETE  /gdi-master-data-indices/:id} : delete the "id" gdiMasterDataIndex.
     *
     * @param id the id of the gdiMasterDataIndexDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gdi-master-data-indices/{id}")
    public ResponseEntity<Void> deleteGdiMasterDataIndex(@PathVariable Long id) {
        log.debug("REST request to delete GdiMasterDataIndex : {}", id);
        gdiMasterDataIndexService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/gdi-master-data-indices?query=:query} : search for the gdiMasterDataIndex corresponding
     * to the query.
     *
     * @param query the query of the gdiMasterDataIndex search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/gdi-master-data-indices")
    public ResponseEntity<List<GdiMasterDataIndexDTO>> searchGdiMasterDataIndices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of GdiMasterDataIndices for query {}", query);
        Page<GdiMasterDataIndexDTO> page = gdiMasterDataIndexService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
