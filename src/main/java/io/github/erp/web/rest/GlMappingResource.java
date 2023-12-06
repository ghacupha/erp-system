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

import io.github.erp.repository.GlMappingRepository;
import io.github.erp.service.GlMappingQueryService;
import io.github.erp.service.GlMappingService;
import io.github.erp.service.criteria.GlMappingCriteria;
import io.github.erp.service.dto.GlMappingDTO;
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
 * REST controller for managing {@link io.github.erp.domain.GlMapping}.
 */
@RestController
@RequestMapping("/api")
public class GlMappingResource {

    private final Logger log = LoggerFactory.getLogger(GlMappingResource.class);

    private static final String ENTITY_NAME = "glMapping";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GlMappingService glMappingService;

    private final GlMappingRepository glMappingRepository;

    private final GlMappingQueryService glMappingQueryService;

    public GlMappingResource(
        GlMappingService glMappingService,
        GlMappingRepository glMappingRepository,
        GlMappingQueryService glMappingQueryService
    ) {
        this.glMappingService = glMappingService;
        this.glMappingRepository = glMappingRepository;
        this.glMappingQueryService = glMappingQueryService;
    }

    /**
     * {@code POST  /gl-mappings} : Create a new glMapping.
     *
     * @param glMappingDTO the glMappingDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new glMappingDTO, or with status {@code 400 (Bad Request)} if the glMapping has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gl-mappings")
    public ResponseEntity<GlMappingDTO> createGlMapping(@Valid @RequestBody GlMappingDTO glMappingDTO) throws URISyntaxException {
        log.debug("REST request to save GlMapping : {}", glMappingDTO);
        if (glMappingDTO.getId() != null) {
            throw new BadRequestAlertException("A new glMapping cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GlMappingDTO result = glMappingService.save(glMappingDTO);
        return ResponseEntity
            .created(new URI("/api/gl-mappings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gl-mappings/:id} : Updates an existing glMapping.
     *
     * @param id the id of the glMappingDTO to save.
     * @param glMappingDTO the glMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated glMappingDTO,
     * or with status {@code 400 (Bad Request)} if the glMappingDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the glMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gl-mappings/{id}")
    public ResponseEntity<GlMappingDTO> updateGlMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GlMappingDTO glMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GlMapping : {}, {}", id, glMappingDTO);
        if (glMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, glMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!glMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GlMappingDTO result = glMappingService.save(glMappingDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, glMappingDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gl-mappings/:id} : Partial updates given fields of an existing glMapping, field will ignore if it is null
     *
     * @param id the id of the glMappingDTO to save.
     * @param glMappingDTO the glMappingDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated glMappingDTO,
     * or with status {@code 400 (Bad Request)} if the glMappingDTO is not valid,
     * or with status {@code 404 (Not Found)} if the glMappingDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the glMappingDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gl-mappings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GlMappingDTO> partialUpdateGlMapping(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GlMappingDTO glMappingDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GlMapping partially : {}, {}", id, glMappingDTO);
        if (glMappingDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, glMappingDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!glMappingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GlMappingDTO> result = glMappingService.partialUpdate(glMappingDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, glMappingDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gl-mappings} : get all the glMappings.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of glMappings in body.
     */
    @GetMapping("/gl-mappings")
    public ResponseEntity<List<GlMappingDTO>> getAllGlMappings(GlMappingCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GlMappings by criteria: {}", criteria);
        Page<GlMappingDTO> page = glMappingQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gl-mappings/count} : count all the glMappings.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gl-mappings/count")
    public ResponseEntity<Long> countGlMappings(GlMappingCriteria criteria) {
        log.debug("REST request to count GlMappings by criteria: {}", criteria);
        return ResponseEntity.ok().body(glMappingQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gl-mappings/:id} : get the "id" glMapping.
     *
     * @param id the id of the glMappingDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the glMappingDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gl-mappings/{id}")
    public ResponseEntity<GlMappingDTO> getGlMapping(@PathVariable Long id) {
        log.debug("REST request to get GlMapping : {}", id);
        Optional<GlMappingDTO> glMappingDTO = glMappingService.findOne(id);
        return ResponseUtil.wrapOrNotFound(glMappingDTO);
    }

    /**
     * {@code DELETE  /gl-mappings/:id} : delete the "id" glMapping.
     *
     * @param id the id of the glMappingDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gl-mappings/{id}")
    public ResponseEntity<Void> deleteGlMapping(@PathVariable Long id) {
        log.debug("REST request to delete GlMapping : {}", id);
        glMappingService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/gl-mappings?query=:query} : search for the glMapping corresponding
     * to the query.
     *
     * @param query the query of the glMapping search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/gl-mappings")
    public ResponseEntity<List<GlMappingDTO>> searchGlMappings(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of GlMappings for query {}", query);
        Page<GlMappingDTO> page = glMappingService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
