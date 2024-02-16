package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.ProfessionalQualificationRepository;
import io.github.erp.service.ProfessionalQualificationQueryService;
import io.github.erp.service.ProfessionalQualificationService;
import io.github.erp.service.criteria.ProfessionalQualificationCriteria;
import io.github.erp.service.dto.ProfessionalQualificationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ProfessionalQualification}.
 */
@RestController
@RequestMapping("/api")
public class ProfessionalQualificationResource {

    private final Logger log = LoggerFactory.getLogger(ProfessionalQualificationResource.class);

    private static final String ENTITY_NAME = "professionalQualification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProfessionalQualificationService professionalQualificationService;

    private final ProfessionalQualificationRepository professionalQualificationRepository;

    private final ProfessionalQualificationQueryService professionalQualificationQueryService;

    public ProfessionalQualificationResource(
        ProfessionalQualificationService professionalQualificationService,
        ProfessionalQualificationRepository professionalQualificationRepository,
        ProfessionalQualificationQueryService professionalQualificationQueryService
    ) {
        this.professionalQualificationService = professionalQualificationService;
        this.professionalQualificationRepository = professionalQualificationRepository;
        this.professionalQualificationQueryService = professionalQualificationQueryService;
    }

    /**
     * {@code POST  /professional-qualifications} : Create a new professionalQualification.
     *
     * @param professionalQualificationDTO the professionalQualificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new professionalQualificationDTO, or with status {@code 400 (Bad Request)} if the professionalQualification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/professional-qualifications")
    public ResponseEntity<ProfessionalQualificationDTO> createProfessionalQualification(
        @Valid @RequestBody ProfessionalQualificationDTO professionalQualificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save ProfessionalQualification : {}", professionalQualificationDTO);
        if (professionalQualificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new professionalQualification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ProfessionalQualificationDTO result = professionalQualificationService.save(professionalQualificationDTO);
        return ResponseEntity
            .created(new URI("/api/professional-qualifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /professional-qualifications/:id} : Updates an existing professionalQualification.
     *
     * @param id the id of the professionalQualificationDTO to save.
     * @param professionalQualificationDTO the professionalQualificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalQualificationDTO,
     * or with status {@code 400 (Bad Request)} if the professionalQualificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the professionalQualificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/professional-qualifications/{id}")
    public ResponseEntity<ProfessionalQualificationDTO> updateProfessionalQualification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProfessionalQualificationDTO professionalQualificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ProfessionalQualification : {}, {}", id, professionalQualificationDTO);
        if (professionalQualificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalQualificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalQualificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ProfessionalQualificationDTO result = professionalQualificationService.save(professionalQualificationDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professionalQualificationDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /professional-qualifications/:id} : Partial updates given fields of an existing professionalQualification, field will ignore if it is null
     *
     * @param id the id of the professionalQualificationDTO to save.
     * @param professionalQualificationDTO the professionalQualificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated professionalQualificationDTO,
     * or with status {@code 400 (Bad Request)} if the professionalQualificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the professionalQualificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the professionalQualificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/professional-qualifications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProfessionalQualificationDTO> partialUpdateProfessionalQualification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProfessionalQualificationDTO professionalQualificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ProfessionalQualification partially : {}, {}", id, professionalQualificationDTO);
        if (professionalQualificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, professionalQualificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!professionalQualificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProfessionalQualificationDTO> result = professionalQualificationService.partialUpdate(professionalQualificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, professionalQualificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /professional-qualifications} : get all the professionalQualifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of professionalQualifications in body.
     */
    @GetMapping("/professional-qualifications")
    public ResponseEntity<List<ProfessionalQualificationDTO>> getAllProfessionalQualifications(
        ProfessionalQualificationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get ProfessionalQualifications by criteria: {}", criteria);
        Page<ProfessionalQualificationDTO> page = professionalQualificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /professional-qualifications/count} : count all the professionalQualifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/professional-qualifications/count")
    public ResponseEntity<Long> countProfessionalQualifications(ProfessionalQualificationCriteria criteria) {
        log.debug("REST request to count ProfessionalQualifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(professionalQualificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /professional-qualifications/:id} : get the "id" professionalQualification.
     *
     * @param id the id of the professionalQualificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the professionalQualificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/professional-qualifications/{id}")
    public ResponseEntity<ProfessionalQualificationDTO> getProfessionalQualification(@PathVariable Long id) {
        log.debug("REST request to get ProfessionalQualification : {}", id);
        Optional<ProfessionalQualificationDTO> professionalQualificationDTO = professionalQualificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(professionalQualificationDTO);
    }

    /**
     * {@code DELETE  /professional-qualifications/:id} : delete the "id" professionalQualification.
     *
     * @param id the id of the professionalQualificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/professional-qualifications/{id}")
    public ResponseEntity<Void> deleteProfessionalQualification(@PathVariable Long id) {
        log.debug("REST request to delete ProfessionalQualification : {}", id);
        professionalQualificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/professional-qualifications?query=:query} : search for the professionalQualification corresponding
     * to the query.
     *
     * @param query the query of the professionalQualification search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/professional-qualifications")
    public ResponseEntity<List<ProfessionalQualificationDTO>> searchProfessionalQualifications(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of ProfessionalQualifications for query {}", query);
        Page<ProfessionalQualificationDTO> page = professionalQualificationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
