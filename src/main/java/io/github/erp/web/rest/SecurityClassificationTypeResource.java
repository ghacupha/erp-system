package io.github.erp.web.rest;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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

import io.github.erp.repository.SecurityClassificationTypeRepository;
import io.github.erp.service.SecurityClassificationTypeQueryService;
import io.github.erp.service.SecurityClassificationTypeService;
import io.github.erp.service.criteria.SecurityClassificationTypeCriteria;
import io.github.erp.service.dto.SecurityClassificationTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SecurityClassificationType}.
 */
@RestController
@RequestMapping("/api")
public class SecurityClassificationTypeResource {

    private final Logger log = LoggerFactory.getLogger(SecurityClassificationTypeResource.class);

    private static final String ENTITY_NAME = "securityClassificationType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SecurityClassificationTypeService securityClassificationTypeService;

    private final SecurityClassificationTypeRepository securityClassificationTypeRepository;

    private final SecurityClassificationTypeQueryService securityClassificationTypeQueryService;

    public SecurityClassificationTypeResource(
        SecurityClassificationTypeService securityClassificationTypeService,
        SecurityClassificationTypeRepository securityClassificationTypeRepository,
        SecurityClassificationTypeQueryService securityClassificationTypeQueryService
    ) {
        this.securityClassificationTypeService = securityClassificationTypeService;
        this.securityClassificationTypeRepository = securityClassificationTypeRepository;
        this.securityClassificationTypeQueryService = securityClassificationTypeQueryService;
    }

    /**
     * {@code POST  /security-classification-types} : Create a new securityClassificationType.
     *
     * @param securityClassificationTypeDTO the securityClassificationTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new securityClassificationTypeDTO, or with status {@code 400 (Bad Request)} if the securityClassificationType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/security-classification-types")
    public ResponseEntity<SecurityClassificationTypeDTO> createSecurityClassificationType(
        @Valid @RequestBody SecurityClassificationTypeDTO securityClassificationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save SecurityClassificationType : {}", securityClassificationTypeDTO);
        if (securityClassificationTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new securityClassificationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SecurityClassificationTypeDTO result = securityClassificationTypeService.save(securityClassificationTypeDTO);
        return ResponseEntity
            .created(new URI("/api/security-classification-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /security-classification-types/:id} : Updates an existing securityClassificationType.
     *
     * @param id the id of the securityClassificationTypeDTO to save.
     * @param securityClassificationTypeDTO the securityClassificationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityClassificationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the securityClassificationTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the securityClassificationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/security-classification-types/{id}")
    public ResponseEntity<SecurityClassificationTypeDTO> updateSecurityClassificationType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SecurityClassificationTypeDTO securityClassificationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SecurityClassificationType : {}, {}", id, securityClassificationTypeDTO);
        if (securityClassificationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityClassificationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityClassificationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SecurityClassificationTypeDTO result = securityClassificationTypeService.save(securityClassificationTypeDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securityClassificationTypeDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /security-classification-types/:id} : Partial updates given fields of an existing securityClassificationType, field will ignore if it is null
     *
     * @param id the id of the securityClassificationTypeDTO to save.
     * @param securityClassificationTypeDTO the securityClassificationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityClassificationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the securityClassificationTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the securityClassificationTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the securityClassificationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/security-classification-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SecurityClassificationTypeDTO> partialUpdateSecurityClassificationType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SecurityClassificationTypeDTO securityClassificationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SecurityClassificationType partially : {}, {}", id, securityClassificationTypeDTO);
        if (securityClassificationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityClassificationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityClassificationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SecurityClassificationTypeDTO> result = securityClassificationTypeService.partialUpdate(securityClassificationTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securityClassificationTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /security-classification-types} : get all the securityClassificationTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of securityClassificationTypes in body.
     */
    @GetMapping("/security-classification-types")
    public ResponseEntity<List<SecurityClassificationTypeDTO>> getAllSecurityClassificationTypes(
        SecurityClassificationTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get SecurityClassificationTypes by criteria: {}", criteria);
        Page<SecurityClassificationTypeDTO> page = securityClassificationTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /security-classification-types/count} : count all the securityClassificationTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/security-classification-types/count")
    public ResponseEntity<Long> countSecurityClassificationTypes(SecurityClassificationTypeCriteria criteria) {
        log.debug("REST request to count SecurityClassificationTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(securityClassificationTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /security-classification-types/:id} : get the "id" securityClassificationType.
     *
     * @param id the id of the securityClassificationTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the securityClassificationTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/security-classification-types/{id}")
    public ResponseEntity<SecurityClassificationTypeDTO> getSecurityClassificationType(@PathVariable Long id) {
        log.debug("REST request to get SecurityClassificationType : {}", id);
        Optional<SecurityClassificationTypeDTO> securityClassificationTypeDTO = securityClassificationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(securityClassificationTypeDTO);
    }

    /**
     * {@code DELETE  /security-classification-types/:id} : delete the "id" securityClassificationType.
     *
     * @param id the id of the securityClassificationTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/security-classification-types/{id}")
    public ResponseEntity<Void> deleteSecurityClassificationType(@PathVariable Long id) {
        log.debug("REST request to delete SecurityClassificationType : {}", id);
        securityClassificationTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/security-classification-types?query=:query} : search for the securityClassificationType corresponding
     * to the query.
     *
     * @param query the query of the securityClassificationType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/security-classification-types")
    public ResponseEntity<List<SecurityClassificationTypeDTO>> searchSecurityClassificationTypes(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of SecurityClassificationTypes for query {}", query);
        Page<SecurityClassificationTypeDTO> page = securityClassificationTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
