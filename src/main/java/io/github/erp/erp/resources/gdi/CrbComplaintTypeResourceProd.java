package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.repository.CrbComplaintTypeRepository;
import io.github.erp.service.CrbComplaintTypeQueryService;
import io.github.erp.service.CrbComplaintTypeService;
import io.github.erp.service.criteria.CrbComplaintTypeCriteria;
import io.github.erp.service.dto.CrbComplaintTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbComplaintType}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CrbComplaintTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(CrbComplaintTypeResourceProd.class);

    private static final String ENTITY_NAME = "crbComplaintType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbComplaintTypeService crbComplaintTypeService;

    private final CrbComplaintTypeRepository crbComplaintTypeRepository;

    private final CrbComplaintTypeQueryService crbComplaintTypeQueryService;

    public CrbComplaintTypeResourceProd(
        CrbComplaintTypeService crbComplaintTypeService,
        CrbComplaintTypeRepository crbComplaintTypeRepository,
        CrbComplaintTypeQueryService crbComplaintTypeQueryService
    ) {
        this.crbComplaintTypeService = crbComplaintTypeService;
        this.crbComplaintTypeRepository = crbComplaintTypeRepository;
        this.crbComplaintTypeQueryService = crbComplaintTypeQueryService;
    }

    /**
     * {@code POST  /crb-complaint-types} : Create a new crbComplaintType.
     *
     * @param crbComplaintTypeDTO the crbComplaintTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbComplaintTypeDTO, or with status {@code 400 (Bad Request)} if the crbComplaintType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-complaint-types")
    public ResponseEntity<CrbComplaintTypeDTO> createCrbComplaintType(@Valid @RequestBody CrbComplaintTypeDTO crbComplaintTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CrbComplaintType : {}", crbComplaintTypeDTO);
        if (crbComplaintTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbComplaintType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbComplaintTypeDTO result = crbComplaintTypeService.save(crbComplaintTypeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-complaint-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-complaint-types/:id} : Updates an existing crbComplaintType.
     *
     * @param id the id of the crbComplaintTypeDTO to save.
     * @param crbComplaintTypeDTO the crbComplaintTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbComplaintTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbComplaintTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbComplaintTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-complaint-types/{id}")
    public ResponseEntity<CrbComplaintTypeDTO> updateCrbComplaintType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbComplaintTypeDTO crbComplaintTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbComplaintType : {}, {}", id, crbComplaintTypeDTO);
        if (crbComplaintTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbComplaintTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbComplaintTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbComplaintTypeDTO result = crbComplaintTypeService.save(crbComplaintTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbComplaintTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-complaint-types/:id} : Partial updates given fields of an existing crbComplaintType, field will ignore if it is null
     *
     * @param id the id of the crbComplaintTypeDTO to save.
     * @param crbComplaintTypeDTO the crbComplaintTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbComplaintTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbComplaintTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbComplaintTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbComplaintTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-complaint-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbComplaintTypeDTO> partialUpdateCrbComplaintType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbComplaintTypeDTO crbComplaintTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbComplaintType partially : {}, {}", id, crbComplaintTypeDTO);
        if (crbComplaintTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbComplaintTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbComplaintTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbComplaintTypeDTO> result = crbComplaintTypeService.partialUpdate(crbComplaintTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbComplaintTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-complaint-types} : get all the crbComplaintTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbComplaintTypes in body.
     */
    @GetMapping("/crb-complaint-types")
    public ResponseEntity<List<CrbComplaintTypeDTO>> getAllCrbComplaintTypes(CrbComplaintTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CrbComplaintTypes by criteria: {}", criteria);
        Page<CrbComplaintTypeDTO> page = crbComplaintTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-complaint-types/count} : count all the crbComplaintTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-complaint-types/count")
    public ResponseEntity<Long> countCrbComplaintTypes(CrbComplaintTypeCriteria criteria) {
        log.debug("REST request to count CrbComplaintTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbComplaintTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-complaint-types/:id} : get the "id" crbComplaintType.
     *
     * @param id the id of the crbComplaintTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbComplaintTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-complaint-types/{id}")
    public ResponseEntity<CrbComplaintTypeDTO> getCrbComplaintType(@PathVariable Long id) {
        log.debug("REST request to get CrbComplaintType : {}", id);
        Optional<CrbComplaintTypeDTO> crbComplaintTypeDTO = crbComplaintTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbComplaintTypeDTO);
    }

    /**
     * {@code DELETE  /crb-complaint-types/:id} : delete the "id" crbComplaintType.
     *
     * @param id the id of the crbComplaintTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-complaint-types/{id}")
    public ResponseEntity<Void> deleteCrbComplaintType(@PathVariable Long id) {
        log.debug("REST request to delete CrbComplaintType : {}", id);
        crbComplaintTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-complaint-types?query=:query} : search for the crbComplaintType corresponding
     * to the query.
     *
     * @param query the query of the crbComplaintType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-complaint-types")
    public ResponseEntity<List<CrbComplaintTypeDTO>> searchCrbComplaintTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbComplaintTypes for query {}", query);
        Page<CrbComplaintTypeDTO> page = crbComplaintTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
