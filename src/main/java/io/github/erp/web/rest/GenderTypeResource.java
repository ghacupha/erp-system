package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.GenderTypeRepository;
import io.github.erp.service.GenderTypeQueryService;
import io.github.erp.service.GenderTypeService;
import io.github.erp.service.criteria.GenderTypeCriteria;
import io.github.erp.service.dto.GenderTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.GenderType}.
 */
@RestController
@RequestMapping("/api")
public class GenderTypeResource {

    private final Logger log = LoggerFactory.getLogger(GenderTypeResource.class);

    private static final String ENTITY_NAME = "genderType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GenderTypeService genderTypeService;

    private final GenderTypeRepository genderTypeRepository;

    private final GenderTypeQueryService genderTypeQueryService;

    public GenderTypeResource(
        GenderTypeService genderTypeService,
        GenderTypeRepository genderTypeRepository,
        GenderTypeQueryService genderTypeQueryService
    ) {
        this.genderTypeService = genderTypeService;
        this.genderTypeRepository = genderTypeRepository;
        this.genderTypeQueryService = genderTypeQueryService;
    }

    /**
     * {@code POST  /gender-types} : Create a new genderType.
     *
     * @param genderTypeDTO the genderTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new genderTypeDTO, or with status {@code 400 (Bad Request)} if the genderType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/gender-types")
    public ResponseEntity<GenderTypeDTO> createGenderType(@Valid @RequestBody GenderTypeDTO genderTypeDTO) throws URISyntaxException {
        log.debug("REST request to save GenderType : {}", genderTypeDTO);
        if (genderTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new genderType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        GenderTypeDTO result = genderTypeService.save(genderTypeDTO);
        return ResponseEntity
            .created(new URI("/api/gender-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /gender-types/:id} : Updates an existing genderType.
     *
     * @param id the id of the genderTypeDTO to save.
     * @param genderTypeDTO the genderTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genderTypeDTO,
     * or with status {@code 400 (Bad Request)} if the genderTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the genderTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/gender-types/{id}")
    public ResponseEntity<GenderTypeDTO> updateGenderType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody GenderTypeDTO genderTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update GenderType : {}, {}", id, genderTypeDTO);
        if (genderTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genderTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        GenderTypeDTO result = genderTypeService.save(genderTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, genderTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /gender-types/:id} : Partial updates given fields of an existing genderType, field will ignore if it is null
     *
     * @param id the id of the genderTypeDTO to save.
     * @param genderTypeDTO the genderTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated genderTypeDTO,
     * or with status {@code 400 (Bad Request)} if the genderTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the genderTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the genderTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/gender-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<GenderTypeDTO> partialUpdateGenderType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody GenderTypeDTO genderTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update GenderType partially : {}, {}", id, genderTypeDTO);
        if (genderTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, genderTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!genderTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<GenderTypeDTO> result = genderTypeService.partialUpdate(genderTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, genderTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /gender-types} : get all the genderTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of genderTypes in body.
     */
    @GetMapping("/gender-types")
    public ResponseEntity<List<GenderTypeDTO>> getAllGenderTypes(GenderTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get GenderTypes by criteria: {}", criteria);
        Page<GenderTypeDTO> page = genderTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /gender-types/count} : count all the genderTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/gender-types/count")
    public ResponseEntity<Long> countGenderTypes(GenderTypeCriteria criteria) {
        log.debug("REST request to count GenderTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(genderTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /gender-types/:id} : get the "id" genderType.
     *
     * @param id the id of the genderTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the genderTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/gender-types/{id}")
    public ResponseEntity<GenderTypeDTO> getGenderType(@PathVariable Long id) {
        log.debug("REST request to get GenderType : {}", id);
        Optional<GenderTypeDTO> genderTypeDTO = genderTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(genderTypeDTO);
    }

    /**
     * {@code DELETE  /gender-types/:id} : delete the "id" genderType.
     *
     * @param id the id of the genderTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/gender-types/{id}")
    public ResponseEntity<Void> deleteGenderType(@PathVariable Long id) {
        log.debug("REST request to delete GenderType : {}", id);
        genderTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/gender-types?query=:query} : search for the genderType corresponding
     * to the query.
     *
     * @param query the query of the genderType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/gender-types")
    public ResponseEntity<List<GenderTypeDTO>> searchGenderTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of GenderTypes for query {}", query);
        Page<GenderTypeDTO> page = genderTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
