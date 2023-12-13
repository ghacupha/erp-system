package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
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
import io.github.erp.repository.CrbProductServiceFeeTypeRepository;
import io.github.erp.service.CrbProductServiceFeeTypeQueryService;
import io.github.erp.service.CrbProductServiceFeeTypeService;
import io.github.erp.service.criteria.CrbProductServiceFeeTypeCriteria;
import io.github.erp.service.dto.CrbProductServiceFeeTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbProductServiceFeeType}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CrbProductServiceFeeTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(CrbProductServiceFeeTypeResourceProd.class);

    private static final String ENTITY_NAME = "crbProductServiceFeeType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbProductServiceFeeTypeService crbProductServiceFeeTypeService;

    private final CrbProductServiceFeeTypeRepository crbProductServiceFeeTypeRepository;

    private final CrbProductServiceFeeTypeQueryService crbProductServiceFeeTypeQueryService;

    public CrbProductServiceFeeTypeResourceProd(
        CrbProductServiceFeeTypeService crbProductServiceFeeTypeService,
        CrbProductServiceFeeTypeRepository crbProductServiceFeeTypeRepository,
        CrbProductServiceFeeTypeQueryService crbProductServiceFeeTypeQueryService
    ) {
        this.crbProductServiceFeeTypeService = crbProductServiceFeeTypeService;
        this.crbProductServiceFeeTypeRepository = crbProductServiceFeeTypeRepository;
        this.crbProductServiceFeeTypeQueryService = crbProductServiceFeeTypeQueryService;
    }

    /**
     * {@code POST  /crb-product-service-fee-types} : Create a new crbProductServiceFeeType.
     *
     * @param crbProductServiceFeeTypeDTO the crbProductServiceFeeTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbProductServiceFeeTypeDTO, or with status {@code 400 (Bad Request)} if the crbProductServiceFeeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-product-service-fee-types")
    public ResponseEntity<CrbProductServiceFeeTypeDTO> createCrbProductServiceFeeType(
        @Valid @RequestBody CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbProductServiceFeeType : {}", crbProductServiceFeeTypeDTO);
        if (crbProductServiceFeeTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbProductServiceFeeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbProductServiceFeeTypeDTO result = crbProductServiceFeeTypeService.save(crbProductServiceFeeTypeDTO);
        return ResponseEntity
            .created(new URI("/api/crb-product-service-fee-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-product-service-fee-types/:id} : Updates an existing crbProductServiceFeeType.
     *
     * @param id the id of the crbProductServiceFeeTypeDTO to save.
     * @param crbProductServiceFeeTypeDTO the crbProductServiceFeeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbProductServiceFeeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbProductServiceFeeTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbProductServiceFeeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-product-service-fee-types/{id}")
    public ResponseEntity<CrbProductServiceFeeTypeDTO> updateCrbProductServiceFeeType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbProductServiceFeeType : {}, {}", id, crbProductServiceFeeTypeDTO);
        if (crbProductServiceFeeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbProductServiceFeeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbProductServiceFeeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbProductServiceFeeTypeDTO result = crbProductServiceFeeTypeService.save(crbProductServiceFeeTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbProductServiceFeeTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-product-service-fee-types/:id} : Partial updates given fields of an existing crbProductServiceFeeType, field will ignore if it is null
     *
     * @param id the id of the crbProductServiceFeeTypeDTO to save.
     * @param crbProductServiceFeeTypeDTO the crbProductServiceFeeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbProductServiceFeeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the crbProductServiceFeeTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbProductServiceFeeTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbProductServiceFeeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-product-service-fee-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbProductServiceFeeTypeDTO> partialUpdateCrbProductServiceFeeType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbProductServiceFeeTypeDTO crbProductServiceFeeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbProductServiceFeeType partially : {}, {}", id, crbProductServiceFeeTypeDTO);
        if (crbProductServiceFeeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbProductServiceFeeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbProductServiceFeeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbProductServiceFeeTypeDTO> result = crbProductServiceFeeTypeService.partialUpdate(crbProductServiceFeeTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbProductServiceFeeTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-product-service-fee-types} : get all the crbProductServiceFeeTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbProductServiceFeeTypes in body.
     */
    @GetMapping("/crb-product-service-fee-types")
    public ResponseEntity<List<CrbProductServiceFeeTypeDTO>> getAllCrbProductServiceFeeTypes(
        CrbProductServiceFeeTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbProductServiceFeeTypes by criteria: {}", criteria);
        Page<CrbProductServiceFeeTypeDTO> page = crbProductServiceFeeTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-product-service-fee-types/count} : count all the crbProductServiceFeeTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-product-service-fee-types/count")
    public ResponseEntity<Long> countCrbProductServiceFeeTypes(CrbProductServiceFeeTypeCriteria criteria) {
        log.debug("REST request to count CrbProductServiceFeeTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbProductServiceFeeTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-product-service-fee-types/:id} : get the "id" crbProductServiceFeeType.
     *
     * @param id the id of the crbProductServiceFeeTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbProductServiceFeeTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-product-service-fee-types/{id}")
    public ResponseEntity<CrbProductServiceFeeTypeDTO> getCrbProductServiceFeeType(@PathVariable Long id) {
        log.debug("REST request to get CrbProductServiceFeeType : {}", id);
        Optional<CrbProductServiceFeeTypeDTO> crbProductServiceFeeTypeDTO = crbProductServiceFeeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbProductServiceFeeTypeDTO);
    }

    /**
     * {@code DELETE  /crb-product-service-fee-types/:id} : delete the "id" crbProductServiceFeeType.
     *
     * @param id the id of the crbProductServiceFeeTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-product-service-fee-types/{id}")
    public ResponseEntity<Void> deleteCrbProductServiceFeeType(@PathVariable Long id) {
        log.debug("REST request to delete CrbProductServiceFeeType : {}", id);
        crbProductServiceFeeTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-product-service-fee-types?query=:query} : search for the crbProductServiceFeeType corresponding
     * to the query.
     *
     * @param query the query of the crbProductServiceFeeType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-product-service-fee-types")
    public ResponseEntity<List<CrbProductServiceFeeTypeDTO>> searchCrbProductServiceFeeTypes(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CrbProductServiceFeeTypes for query {}", query);
        Page<CrbProductServiceFeeTypeDTO> page = crbProductServiceFeeTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
