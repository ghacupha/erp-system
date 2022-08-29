package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 27 (Baruch Series) Server ver 0.0.7-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.repository.JobSheetRepository;
import io.github.erp.service.JobSheetQueryService;
import io.github.erp.service.JobSheetService;
import io.github.erp.service.criteria.JobSheetCriteria;
import io.github.erp.service.dto.JobSheetDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link io.github.erp.domain.JobSheet}.
 */
@RestController
@RequestMapping("/api/payments")
public class JobSheetResource {

    private final Logger log = LoggerFactory.getLogger(JobSheetResource.class);

    private static final String ENTITY_NAME = "jobSheet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JobSheetService jobSheetService;

    private final JobSheetRepository jobSheetRepository;

    private final JobSheetQueryService jobSheetQueryService;

    public JobSheetResource(
        JobSheetService jobSheetService,
        JobSheetRepository jobSheetRepository,
        JobSheetQueryService jobSheetQueryService
    ) {
        this.jobSheetService = jobSheetService;
        this.jobSheetRepository = jobSheetRepository;
        this.jobSheetQueryService = jobSheetQueryService;
    }

    /**
     * {@code POST  /job-sheets} : Create a new jobSheet.
     *
     * @param jobSheetDTO the jobSheetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jobSheetDTO, or with status {@code 400 (Bad Request)} if the jobSheet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/job-sheets")
    public ResponseEntity<JobSheetDTO> createJobSheet(@Valid @RequestBody JobSheetDTO jobSheetDTO) throws URISyntaxException {
        log.debug("REST request to save JobSheet : {}", jobSheetDTO);
        if (jobSheetDTO.getId() != null) {
            throw new BadRequestAlertException("A new jobSheet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JobSheetDTO result = jobSheetService.save(jobSheetDTO);
        return ResponseEntity
            .created(new URI("/api/job-sheets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /job-sheets/:id} : Updates an existing jobSheet.
     *
     * @param id the id of the jobSheetDTO to save.
     * @param jobSheetDTO the jobSheetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobSheetDTO,
     * or with status {@code 400 (Bad Request)} if the jobSheetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jobSheetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/job-sheets/{id}")
    public ResponseEntity<JobSheetDTO> updateJobSheet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody JobSheetDTO jobSheetDTO
    ) throws URISyntaxException {
        log.debug("REST request to update JobSheet : {}, {}", id, jobSheetDTO);
        if (jobSheetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobSheetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobSheetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JobSheetDTO result = jobSheetService.save(jobSheetDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jobSheetDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /job-sheets/:id} : Partial updates given fields of an existing jobSheet, field will ignore if it is null
     *
     * @param id the id of the jobSheetDTO to save.
     * @param jobSheetDTO the jobSheetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jobSheetDTO,
     * or with status {@code 400 (Bad Request)} if the jobSheetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the jobSheetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the jobSheetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/job-sheets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JobSheetDTO> partialUpdateJobSheet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody JobSheetDTO jobSheetDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update JobSheet partially : {}, {}", id, jobSheetDTO);
        if (jobSheetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jobSheetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jobSheetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JobSheetDTO> result = jobSheetService.partialUpdate(jobSheetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, jobSheetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /job-sheets} : get all the jobSheets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jobSheets in body.
     */
    @GetMapping("/job-sheets")
    public ResponseEntity<List<JobSheetDTO>> getAllJobSheets(JobSheetCriteria criteria, Pageable pageable) {
        log.debug("REST request to get JobSheets by criteria: {}", criteria);
        Page<JobSheetDTO> page = jobSheetQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /job-sheets/count} : count all the jobSheets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/job-sheets/count")
    public ResponseEntity<Long> countJobSheets(JobSheetCriteria criteria) {
        log.debug("REST request to count JobSheets by criteria: {}", criteria);
        return ResponseEntity.ok().body(jobSheetQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /job-sheets/:id} : get the "id" jobSheet.
     *
     * @param id the id of the jobSheetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jobSheetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/job-sheets/{id}")
    public ResponseEntity<JobSheetDTO> getJobSheet(@PathVariable Long id) {
        log.debug("REST request to get JobSheet : {}", id);
        Optional<JobSheetDTO> jobSheetDTO = jobSheetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(jobSheetDTO);
    }

    /**
     * {@code DELETE  /job-sheets/:id} : delete the "id" jobSheet.
     *
     * @param id the id of the jobSheetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/job-sheets/{id}")
    public ResponseEntity<Void> deleteJobSheet(@PathVariable Long id) {
        log.debug("REST request to delete JobSheet : {}", id);
        jobSheetService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/job-sheets?query=:query} : search for the jobSheet corresponding
     * to the query.
     *
     * @param query the query of the jobSheet search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/job-sheets")
    public ResponseEntity<List<JobSheetDTO>> searchJobSheets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of JobSheets for query {}", query);
        Page<JobSheetDTO> page = jobSheetService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
