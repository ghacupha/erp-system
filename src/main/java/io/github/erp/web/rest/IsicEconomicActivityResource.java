package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.IsicEconomicActivityRepository;
import io.github.erp.service.IsicEconomicActivityQueryService;
import io.github.erp.service.IsicEconomicActivityService;
import io.github.erp.service.criteria.IsicEconomicActivityCriteria;
import io.github.erp.service.dto.IsicEconomicActivityDTO;
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
 * REST controller for managing {@link io.github.erp.domain.IsicEconomicActivity}.
 */
@RestController
@RequestMapping("/api")
public class IsicEconomicActivityResource {

    private final Logger log = LoggerFactory.getLogger(IsicEconomicActivityResource.class);

    private static final String ENTITY_NAME = "isicEconomicActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IsicEconomicActivityService isicEconomicActivityService;

    private final IsicEconomicActivityRepository isicEconomicActivityRepository;

    private final IsicEconomicActivityQueryService isicEconomicActivityQueryService;

    public IsicEconomicActivityResource(
        IsicEconomicActivityService isicEconomicActivityService,
        IsicEconomicActivityRepository isicEconomicActivityRepository,
        IsicEconomicActivityQueryService isicEconomicActivityQueryService
    ) {
        this.isicEconomicActivityService = isicEconomicActivityService;
        this.isicEconomicActivityRepository = isicEconomicActivityRepository;
        this.isicEconomicActivityQueryService = isicEconomicActivityQueryService;
    }

    /**
     * {@code POST  /isic-economic-activities} : Create a new isicEconomicActivity.
     *
     * @param isicEconomicActivityDTO the isicEconomicActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new isicEconomicActivityDTO, or with status {@code 400 (Bad Request)} if the isicEconomicActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/isic-economic-activities")
    public ResponseEntity<IsicEconomicActivityDTO> createIsicEconomicActivity(
        @Valid @RequestBody IsicEconomicActivityDTO isicEconomicActivityDTO
    ) throws URISyntaxException {
        log.debug("REST request to save IsicEconomicActivity : {}", isicEconomicActivityDTO);
        if (isicEconomicActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new isicEconomicActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IsicEconomicActivityDTO result = isicEconomicActivityService.save(isicEconomicActivityDTO);
        return ResponseEntity
            .created(new URI("/api/isic-economic-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /isic-economic-activities/:id} : Updates an existing isicEconomicActivity.
     *
     * @param id the id of the isicEconomicActivityDTO to save.
     * @param isicEconomicActivityDTO the isicEconomicActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isicEconomicActivityDTO,
     * or with status {@code 400 (Bad Request)} if the isicEconomicActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the isicEconomicActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/isic-economic-activities/{id}")
    public ResponseEntity<IsicEconomicActivityDTO> updateIsicEconomicActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IsicEconomicActivityDTO isicEconomicActivityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IsicEconomicActivity : {}, {}", id, isicEconomicActivityDTO);
        if (isicEconomicActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isicEconomicActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isicEconomicActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IsicEconomicActivityDTO result = isicEconomicActivityService.save(isicEconomicActivityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isicEconomicActivityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /isic-economic-activities/:id} : Partial updates given fields of an existing isicEconomicActivity, field will ignore if it is null
     *
     * @param id the id of the isicEconomicActivityDTO to save.
     * @param isicEconomicActivityDTO the isicEconomicActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated isicEconomicActivityDTO,
     * or with status {@code 400 (Bad Request)} if the isicEconomicActivityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the isicEconomicActivityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the isicEconomicActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/isic-economic-activities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IsicEconomicActivityDTO> partialUpdateIsicEconomicActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IsicEconomicActivityDTO isicEconomicActivityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IsicEconomicActivity partially : {}, {}", id, isicEconomicActivityDTO);
        if (isicEconomicActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, isicEconomicActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!isicEconomicActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IsicEconomicActivityDTO> result = isicEconomicActivityService.partialUpdate(isicEconomicActivityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, isicEconomicActivityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /isic-economic-activities} : get all the isicEconomicActivities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of isicEconomicActivities in body.
     */
    @GetMapping("/isic-economic-activities")
    public ResponseEntity<List<IsicEconomicActivityDTO>> getAllIsicEconomicActivities(
        IsicEconomicActivityCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get IsicEconomicActivities by criteria: {}", criteria);
        Page<IsicEconomicActivityDTO> page = isicEconomicActivityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /isic-economic-activities/count} : count all the isicEconomicActivities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/isic-economic-activities/count")
    public ResponseEntity<Long> countIsicEconomicActivities(IsicEconomicActivityCriteria criteria) {
        log.debug("REST request to count IsicEconomicActivities by criteria: {}", criteria);
        return ResponseEntity.ok().body(isicEconomicActivityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /isic-economic-activities/:id} : get the "id" isicEconomicActivity.
     *
     * @param id the id of the isicEconomicActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the isicEconomicActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/isic-economic-activities/{id}")
    public ResponseEntity<IsicEconomicActivityDTO> getIsicEconomicActivity(@PathVariable Long id) {
        log.debug("REST request to get IsicEconomicActivity : {}", id);
        Optional<IsicEconomicActivityDTO> isicEconomicActivityDTO = isicEconomicActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(isicEconomicActivityDTO);
    }

    /**
     * {@code DELETE  /isic-economic-activities/:id} : delete the "id" isicEconomicActivity.
     *
     * @param id the id of the isicEconomicActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/isic-economic-activities/{id}")
    public ResponseEntity<Void> deleteIsicEconomicActivity(@PathVariable Long id) {
        log.debug("REST request to delete IsicEconomicActivity : {}", id);
        isicEconomicActivityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/isic-economic-activities?query=:query} : search for the isicEconomicActivity corresponding
     * to the query.
     *
     * @param query the query of the isicEconomicActivity search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/isic-economic-activities")
    public ResponseEntity<List<IsicEconomicActivityDTO>> searchIsicEconomicActivities(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IsicEconomicActivities for query {}", query);
        Page<IsicEconomicActivityDTO> page = isicEconomicActivityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
