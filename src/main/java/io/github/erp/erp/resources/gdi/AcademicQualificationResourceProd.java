package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import io.github.erp.repository.AcademicQualificationRepository;
import io.github.erp.service.AcademicQualificationQueryService;
import io.github.erp.service.AcademicQualificationService;
import io.github.erp.service.criteria.AcademicQualificationCriteria;
import io.github.erp.service.dto.AcademicQualificationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AcademicQualification}.
 */
@RestController("AcademicQualificationResourceProd")
@RequestMapping("/api/granular-data")
public class AcademicQualificationResourceProd {

    private final Logger log = LoggerFactory.getLogger(AcademicQualificationResourceProd.class);

    private static final String ENTITY_NAME = "academicQualification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AcademicQualificationService academicQualificationService;

    private final AcademicQualificationRepository academicQualificationRepository;

    private final AcademicQualificationQueryService academicQualificationQueryService;

    public AcademicQualificationResourceProd(
        AcademicQualificationService academicQualificationService,
        AcademicQualificationRepository academicQualificationRepository,
        AcademicQualificationQueryService academicQualificationQueryService
    ) {
        this.academicQualificationService = academicQualificationService;
        this.academicQualificationRepository = academicQualificationRepository;
        this.academicQualificationQueryService = academicQualificationQueryService;
    }

    /**
     * {@code POST  /academic-qualifications} : Create a new academicQualification.
     *
     * @param academicQualificationDTO the academicQualificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new academicQualificationDTO, or with status {@code 400 (Bad Request)} if the academicQualification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/academic-qualifications")
    public ResponseEntity<AcademicQualificationDTO> createAcademicQualification(
        @Valid @RequestBody AcademicQualificationDTO academicQualificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AcademicQualification : {}", academicQualificationDTO);
        if (academicQualificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new academicQualification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AcademicQualificationDTO result = academicQualificationService.save(academicQualificationDTO);
        return ResponseEntity
            .created(new URI("/api/academic-qualifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /academic-qualifications/:id} : Updates an existing academicQualification.
     *
     * @param id the id of the academicQualificationDTO to save.
     * @param academicQualificationDTO the academicQualificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated academicQualificationDTO,
     * or with status {@code 400 (Bad Request)} if the academicQualificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the academicQualificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/academic-qualifications/{id}")
    public ResponseEntity<AcademicQualificationDTO> updateAcademicQualification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AcademicQualificationDTO academicQualificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AcademicQualification : {}, {}", id, academicQualificationDTO);
        if (academicQualificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, academicQualificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!academicQualificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AcademicQualificationDTO result = academicQualificationService.save(academicQualificationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, academicQualificationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /academic-qualifications/:id} : Partial updates given fields of an existing academicQualification, field will ignore if it is null
     *
     * @param id the id of the academicQualificationDTO to save.
     * @param academicQualificationDTO the academicQualificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated academicQualificationDTO,
     * or with status {@code 400 (Bad Request)} if the academicQualificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the academicQualificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the academicQualificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/academic-qualifications/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AcademicQualificationDTO> partialUpdateAcademicQualification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AcademicQualificationDTO academicQualificationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AcademicQualification partially : {}, {}", id, academicQualificationDTO);
        if (academicQualificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, academicQualificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!academicQualificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AcademicQualificationDTO> result = academicQualificationService.partialUpdate(academicQualificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, academicQualificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /academic-qualifications} : get all the academicQualifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of academicQualifications in body.
     */
    @GetMapping("/academic-qualifications")
    public ResponseEntity<List<AcademicQualificationDTO>> getAllAcademicQualifications(
        AcademicQualificationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AcademicQualifications by criteria: {}", criteria);
        Page<AcademicQualificationDTO> page = academicQualificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /academic-qualifications/count} : count all the academicQualifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/academic-qualifications/count")
    public ResponseEntity<Long> countAcademicQualifications(AcademicQualificationCriteria criteria) {
        log.debug("REST request to count AcademicQualifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(academicQualificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /academic-qualifications/:id} : get the "id" academicQualification.
     *
     * @param id the id of the academicQualificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the academicQualificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/academic-qualifications/{id}")
    public ResponseEntity<AcademicQualificationDTO> getAcademicQualification(@PathVariable Long id) {
        log.debug("REST request to get AcademicQualification : {}", id);
        Optional<AcademicQualificationDTO> academicQualificationDTO = academicQualificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(academicQualificationDTO);
    }

    /**
     * {@code DELETE  /academic-qualifications/:id} : delete the "id" academicQualification.
     *
     * @param id the id of the academicQualificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/academic-qualifications/{id}")
    public ResponseEntity<Void> deleteAcademicQualification(@PathVariable Long id) {
        log.debug("REST request to delete AcademicQualification : {}", id);
        academicQualificationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/academic-qualifications?query=:query} : search for the academicQualification corresponding
     * to the query.
     *
     * @param query the query of the academicQualification search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/academic-qualifications")
    public ResponseEntity<List<AcademicQualificationDTO>> searchAcademicQualifications(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AcademicQualifications for query {}", query);
        Page<AcademicQualificationDTO> page = academicQualificationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
