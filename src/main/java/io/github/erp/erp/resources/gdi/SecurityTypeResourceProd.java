package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.repository.SecurityTypeRepository;
import io.github.erp.service.SecurityTypeQueryService;
import io.github.erp.service.SecurityTypeService;
import io.github.erp.service.criteria.SecurityTypeCriteria;
import io.github.erp.service.dto.SecurityTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SecurityType}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class SecurityTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(SecurityTypeResourceProd.class);

    private static final String ENTITY_NAME = "securityType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SecurityTypeService securityTypeService;

    private final SecurityTypeRepository securityTypeRepository;

    private final SecurityTypeQueryService securityTypeQueryService;

    public SecurityTypeResourceProd(
        SecurityTypeService securityTypeService,
        SecurityTypeRepository securityTypeRepository,
        SecurityTypeQueryService securityTypeQueryService
    ) {
        this.securityTypeService = securityTypeService;
        this.securityTypeRepository = securityTypeRepository;
        this.securityTypeQueryService = securityTypeQueryService;
    }

    /**
     * {@code POST  /security-types} : Create a new securityType.
     *
     * @param securityTypeDTO the securityTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new securityTypeDTO, or with status {@code 400 (Bad Request)} if the securityType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/security-types")
    public ResponseEntity<SecurityTypeDTO> createSecurityType(@Valid @RequestBody SecurityTypeDTO securityTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save SecurityType : {}", securityTypeDTO);
        if (securityTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new securityType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SecurityTypeDTO result = securityTypeService.save(securityTypeDTO);
        return ResponseEntity
            .created(new URI("/api/security-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /security-types/:id} : Updates an existing securityType.
     *
     * @param id the id of the securityTypeDTO to save.
     * @param securityTypeDTO the securityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the securityTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the securityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/security-types/{id}")
    public ResponseEntity<SecurityTypeDTO> updateSecurityType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SecurityTypeDTO securityTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SecurityType : {}, {}", id, securityTypeDTO);
        if (securityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SecurityTypeDTO result = securityTypeService.save(securityTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securityTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /security-types/:id} : Partial updates given fields of an existing securityType, field will ignore if it is null
     *
     * @param id the id of the securityTypeDTO to save.
     * @param securityTypeDTO the securityTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityTypeDTO,
     * or with status {@code 400 (Bad Request)} if the securityTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the securityTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the securityTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/security-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SecurityTypeDTO> partialUpdateSecurityType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SecurityTypeDTO securityTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SecurityType partially : {}, {}", id, securityTypeDTO);
        if (securityTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SecurityTypeDTO> result = securityTypeService.partialUpdate(securityTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securityTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /security-types} : get all the securityTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of securityTypes in body.
     */
    @GetMapping("/security-types")
    public ResponseEntity<List<SecurityTypeDTO>> getAllSecurityTypes(SecurityTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SecurityTypes by criteria: {}", criteria);
        Page<SecurityTypeDTO> page = securityTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /security-types/count} : count all the securityTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/security-types/count")
    public ResponseEntity<Long> countSecurityTypes(SecurityTypeCriteria criteria) {
        log.debug("REST request to count SecurityTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(securityTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /security-types/:id} : get the "id" securityType.
     *
     * @param id the id of the securityTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the securityTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/security-types/{id}")
    public ResponseEntity<SecurityTypeDTO> getSecurityType(@PathVariable Long id) {
        log.debug("REST request to get SecurityType : {}", id);
        Optional<SecurityTypeDTO> securityTypeDTO = securityTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(securityTypeDTO);
    }

    /**
     * {@code DELETE  /security-types/:id} : delete the "id" securityType.
     *
     * @param id the id of the securityTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/security-types/{id}")
    public ResponseEntity<Void> deleteSecurityType(@PathVariable Long id) {
        log.debug("REST request to delete SecurityType : {}", id);
        securityTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/security-types?query=:query} : search for the securityType corresponding
     * to the query.
     *
     * @param query the query of the securityType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/security-types")
    public ResponseEntity<List<SecurityTypeDTO>> searchSecurityTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SecurityTypes for query {}", query);
        Page<SecurityTypeDTO> page = securityTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
