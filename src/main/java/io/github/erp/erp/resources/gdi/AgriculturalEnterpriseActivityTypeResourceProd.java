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
import io.github.erp.repository.AgriculturalEnterpriseActivityTypeRepository;
import io.github.erp.service.AgriculturalEnterpriseActivityTypeQueryService;
import io.github.erp.service.AgriculturalEnterpriseActivityTypeService;
import io.github.erp.service.criteria.AgriculturalEnterpriseActivityTypeCriteria;
import io.github.erp.service.dto.AgriculturalEnterpriseActivityTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AgriculturalEnterpriseActivityType}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class AgriculturalEnterpriseActivityTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(AgriculturalEnterpriseActivityTypeResourceProd.class);

    private static final String ENTITY_NAME = "agriculturalEnterpriseActivityType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgriculturalEnterpriseActivityTypeService agriculturalEnterpriseActivityTypeService;

    private final AgriculturalEnterpriseActivityTypeRepository agriculturalEnterpriseActivityTypeRepository;

    private final AgriculturalEnterpriseActivityTypeQueryService agriculturalEnterpriseActivityTypeQueryService;

    public AgriculturalEnterpriseActivityTypeResourceProd(
        AgriculturalEnterpriseActivityTypeService agriculturalEnterpriseActivityTypeService,
        AgriculturalEnterpriseActivityTypeRepository agriculturalEnterpriseActivityTypeRepository,
        AgriculturalEnterpriseActivityTypeQueryService agriculturalEnterpriseActivityTypeQueryService
    ) {
        this.agriculturalEnterpriseActivityTypeService = agriculturalEnterpriseActivityTypeService;
        this.agriculturalEnterpriseActivityTypeRepository = agriculturalEnterpriseActivityTypeRepository;
        this.agriculturalEnterpriseActivityTypeQueryService = agriculturalEnterpriseActivityTypeQueryService;
    }

    /**
     * {@code POST  /agricultural-enterprise-activity-types} : Create a new agriculturalEnterpriseActivityType.
     *
     * @param agriculturalEnterpriseActivityTypeDTO the agriculturalEnterpriseActivityTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agriculturalEnterpriseActivityTypeDTO, or with status {@code 400 (Bad Request)} if the agriculturalEnterpriseActivityType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agricultural-enterprise-activity-types")
    public ResponseEntity<AgriculturalEnterpriseActivityTypeDTO> createAgriculturalEnterpriseActivityType(
        @Valid @RequestBody AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AgriculturalEnterpriseActivityType : {}", agriculturalEnterpriseActivityTypeDTO);
        if (agriculturalEnterpriseActivityTypeDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new agriculturalEnterpriseActivityType cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        AgriculturalEnterpriseActivityTypeDTO result = agriculturalEnterpriseActivityTypeService.save(
            agriculturalEnterpriseActivityTypeDTO
        );
        return ResponseEntity
            .created(new URI("/api/agricultural-enterprise-activity-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agricultural-enterprise-activity-types/:id} : Updates an existing agriculturalEnterpriseActivityType.
     *
     * @param id the id of the agriculturalEnterpriseActivityTypeDTO to save.
     * @param agriculturalEnterpriseActivityTypeDTO the agriculturalEnterpriseActivityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agriculturalEnterpriseActivityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the agriculturalEnterpriseActivityTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agriculturalEnterpriseActivityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agricultural-enterprise-activity-types/{id}")
    public ResponseEntity<AgriculturalEnterpriseActivityTypeDTO> updateAgriculturalEnterpriseActivityType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AgriculturalEnterpriseActivityType : {}, {}", id, agriculturalEnterpriseActivityTypeDTO);
        if (agriculturalEnterpriseActivityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agriculturalEnterpriseActivityTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agriculturalEnterpriseActivityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AgriculturalEnterpriseActivityTypeDTO result = agriculturalEnterpriseActivityTypeService.save(
            agriculturalEnterpriseActivityTypeDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    agriculturalEnterpriseActivityTypeDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /agricultural-enterprise-activity-types/:id} : Partial updates given fields of an existing agriculturalEnterpriseActivityType, field will ignore if it is null
     *
     * @param id the id of the agriculturalEnterpriseActivityTypeDTO to save.
     * @param agriculturalEnterpriseActivityTypeDTO the agriculturalEnterpriseActivityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agriculturalEnterpriseActivityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the agriculturalEnterpriseActivityTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agriculturalEnterpriseActivityTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agriculturalEnterpriseActivityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agricultural-enterprise-activity-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgriculturalEnterpriseActivityTypeDTO> partialUpdateAgriculturalEnterpriseActivityType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgriculturalEnterpriseActivityTypeDTO agriculturalEnterpriseActivityTypeDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update AgriculturalEnterpriseActivityType partially : {}, {}",
            id,
            agriculturalEnterpriseActivityTypeDTO
        );
        if (agriculturalEnterpriseActivityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agriculturalEnterpriseActivityTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agriculturalEnterpriseActivityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgriculturalEnterpriseActivityTypeDTO> result = agriculturalEnterpriseActivityTypeService.partialUpdate(
            agriculturalEnterpriseActivityTypeDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agriculturalEnterpriseActivityTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agricultural-enterprise-activity-types} : get all the agriculturalEnterpriseActivityTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agriculturalEnterpriseActivityTypes in body.
     */
    @GetMapping("/agricultural-enterprise-activity-types")
    public ResponseEntity<List<AgriculturalEnterpriseActivityTypeDTO>> getAllAgriculturalEnterpriseActivityTypes(
        AgriculturalEnterpriseActivityTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AgriculturalEnterpriseActivityTypes by criteria: {}", criteria);
        Page<AgriculturalEnterpriseActivityTypeDTO> page = agriculturalEnterpriseActivityTypeQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agricultural-enterprise-activity-types/count} : count all the agriculturalEnterpriseActivityTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/agricultural-enterprise-activity-types/count")
    public ResponseEntity<Long> countAgriculturalEnterpriseActivityTypes(AgriculturalEnterpriseActivityTypeCriteria criteria) {
        log.debug("REST request to count AgriculturalEnterpriseActivityTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(agriculturalEnterpriseActivityTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agricultural-enterprise-activity-types/:id} : get the "id" agriculturalEnterpriseActivityType.
     *
     * @param id the id of the agriculturalEnterpriseActivityTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agriculturalEnterpriseActivityTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agricultural-enterprise-activity-types/{id}")
    public ResponseEntity<AgriculturalEnterpriseActivityTypeDTO> getAgriculturalEnterpriseActivityType(@PathVariable Long id) {
        log.debug("REST request to get AgriculturalEnterpriseActivityType : {}", id);
        Optional<AgriculturalEnterpriseActivityTypeDTO> agriculturalEnterpriseActivityTypeDTO = agriculturalEnterpriseActivityTypeService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(agriculturalEnterpriseActivityTypeDTO);
    }

    /**
     * {@code DELETE  /agricultural-enterprise-activity-types/:id} : delete the "id" agriculturalEnterpriseActivityType.
     *
     * @param id the id of the agriculturalEnterpriseActivityTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agricultural-enterprise-activity-types/{id}")
    public ResponseEntity<Void> deleteAgriculturalEnterpriseActivityType(@PathVariable Long id) {
        log.debug("REST request to delete AgriculturalEnterpriseActivityType : {}", id);
        agriculturalEnterpriseActivityTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/agricultural-enterprise-activity-types?query=:query} : search for the agriculturalEnterpriseActivityType corresponding
     * to the query.
     *
     * @param query the query of the agriculturalEnterpriseActivityType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/agricultural-enterprise-activity-types")
    public ResponseEntity<List<AgriculturalEnterpriseActivityTypeDTO>> searchAgriculturalEnterpriseActivityTypes(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of AgriculturalEnterpriseActivityTypes for query {}", query);
        Page<AgriculturalEnterpriseActivityTypeDTO> page = agriculturalEnterpriseActivityTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
