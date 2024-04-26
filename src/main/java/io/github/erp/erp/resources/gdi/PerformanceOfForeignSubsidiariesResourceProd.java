package io.github.erp.erp.resources.gdi;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.repository.PerformanceOfForeignSubsidiariesRepository;
import io.github.erp.service.PerformanceOfForeignSubsidiariesQueryService;
import io.github.erp.service.PerformanceOfForeignSubsidiariesService;
import io.github.erp.service.criteria.PerformanceOfForeignSubsidiariesCriteria;
import io.github.erp.service.dto.PerformanceOfForeignSubsidiariesDTO;
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
 * REST controller for managing {@link io.github.erp.domain.PerformanceOfForeignSubsidiaries}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class PerformanceOfForeignSubsidiariesResourceProd {

    private final Logger log = LoggerFactory.getLogger(PerformanceOfForeignSubsidiariesResourceProd.class);

    private static final String ENTITY_NAME = "gdiDataPerformanceOfForeignSubsidiaries";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PerformanceOfForeignSubsidiariesService performanceOfForeignSubsidiariesService;

    private final PerformanceOfForeignSubsidiariesRepository performanceOfForeignSubsidiariesRepository;

    private final PerformanceOfForeignSubsidiariesQueryService performanceOfForeignSubsidiariesQueryService;

    public PerformanceOfForeignSubsidiariesResourceProd(
        PerformanceOfForeignSubsidiariesService performanceOfForeignSubsidiariesService,
        PerformanceOfForeignSubsidiariesRepository performanceOfForeignSubsidiariesRepository,
        PerformanceOfForeignSubsidiariesQueryService performanceOfForeignSubsidiariesQueryService
    ) {
        this.performanceOfForeignSubsidiariesService = performanceOfForeignSubsidiariesService;
        this.performanceOfForeignSubsidiariesRepository = performanceOfForeignSubsidiariesRepository;
        this.performanceOfForeignSubsidiariesQueryService = performanceOfForeignSubsidiariesQueryService;
    }

    /**
     * {@code POST  /performance-of-foreign-subsidiaries} : Create a new performanceOfForeignSubsidiaries.
     *
     * @param performanceOfForeignSubsidiariesDTO the performanceOfForeignSubsidiariesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new performanceOfForeignSubsidiariesDTO, or with status {@code 400 (Bad Request)} if the performanceOfForeignSubsidiaries has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/performance-of-foreign-subsidiaries")
    public ResponseEntity<PerformanceOfForeignSubsidiariesDTO> createPerformanceOfForeignSubsidiaries(
        @Valid @RequestBody PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PerformanceOfForeignSubsidiaries : {}", performanceOfForeignSubsidiariesDTO);
        if (performanceOfForeignSubsidiariesDTO.getId() != null) {
            throw new BadRequestAlertException("A new performanceOfForeignSubsidiaries cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PerformanceOfForeignSubsidiariesDTO result = performanceOfForeignSubsidiariesService.save(performanceOfForeignSubsidiariesDTO);
        return ResponseEntity
            .created(new URI("/api/performance-of-foreign-subsidiaries/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /performance-of-foreign-subsidiaries/:id} : Updates an existing performanceOfForeignSubsidiaries.
     *
     * @param id the id of the performanceOfForeignSubsidiariesDTO to save.
     * @param performanceOfForeignSubsidiariesDTO the performanceOfForeignSubsidiariesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated performanceOfForeignSubsidiariesDTO,
     * or with status {@code 400 (Bad Request)} if the performanceOfForeignSubsidiariesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the performanceOfForeignSubsidiariesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/performance-of-foreign-subsidiaries/{id}")
    public ResponseEntity<PerformanceOfForeignSubsidiariesDTO> updatePerformanceOfForeignSubsidiaries(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PerformanceOfForeignSubsidiaries : {}, {}", id, performanceOfForeignSubsidiariesDTO);
        if (performanceOfForeignSubsidiariesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, performanceOfForeignSubsidiariesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!performanceOfForeignSubsidiariesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PerformanceOfForeignSubsidiariesDTO result = performanceOfForeignSubsidiariesService.save(performanceOfForeignSubsidiariesDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    performanceOfForeignSubsidiariesDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /performance-of-foreign-subsidiaries/:id} : Partial updates given fields of an existing performanceOfForeignSubsidiaries, field will ignore if it is null
     *
     * @param id the id of the performanceOfForeignSubsidiariesDTO to save.
     * @param performanceOfForeignSubsidiariesDTO the performanceOfForeignSubsidiariesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated performanceOfForeignSubsidiariesDTO,
     * or with status {@code 400 (Bad Request)} if the performanceOfForeignSubsidiariesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the performanceOfForeignSubsidiariesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the performanceOfForeignSubsidiariesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/performance-of-foreign-subsidiaries/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PerformanceOfForeignSubsidiariesDTO> partialUpdatePerformanceOfForeignSubsidiaries(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PerformanceOfForeignSubsidiariesDTO performanceOfForeignSubsidiariesDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update PerformanceOfForeignSubsidiaries partially : {}, {}",
            id,
            performanceOfForeignSubsidiariesDTO
        );
        if (performanceOfForeignSubsidiariesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, performanceOfForeignSubsidiariesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!performanceOfForeignSubsidiariesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PerformanceOfForeignSubsidiariesDTO> result = performanceOfForeignSubsidiariesService.partialUpdate(
            performanceOfForeignSubsidiariesDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, performanceOfForeignSubsidiariesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /performance-of-foreign-subsidiaries} : get all the performanceOfForeignSubsidiaries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of performanceOfForeignSubsidiaries in body.
     */
    @GetMapping("/performance-of-foreign-subsidiaries")
    public ResponseEntity<List<PerformanceOfForeignSubsidiariesDTO>> getAllPerformanceOfForeignSubsidiaries(
        PerformanceOfForeignSubsidiariesCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get PerformanceOfForeignSubsidiaries by criteria: {}", criteria);
        Page<PerformanceOfForeignSubsidiariesDTO> page = performanceOfForeignSubsidiariesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /performance-of-foreign-subsidiaries/count} : count all the performanceOfForeignSubsidiaries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/performance-of-foreign-subsidiaries/count")
    public ResponseEntity<Long> countPerformanceOfForeignSubsidiaries(PerformanceOfForeignSubsidiariesCriteria criteria) {
        log.debug("REST request to count PerformanceOfForeignSubsidiaries by criteria: {}", criteria);
        return ResponseEntity.ok().body(performanceOfForeignSubsidiariesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /performance-of-foreign-subsidiaries/:id} : get the "id" performanceOfForeignSubsidiaries.
     *
     * @param id the id of the performanceOfForeignSubsidiariesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the performanceOfForeignSubsidiariesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/performance-of-foreign-subsidiaries/{id}")
    public ResponseEntity<PerformanceOfForeignSubsidiariesDTO> getPerformanceOfForeignSubsidiaries(@PathVariable Long id) {
        log.debug("REST request to get PerformanceOfForeignSubsidiaries : {}", id);
        Optional<PerformanceOfForeignSubsidiariesDTO> performanceOfForeignSubsidiariesDTO = performanceOfForeignSubsidiariesService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(performanceOfForeignSubsidiariesDTO);
    }

    /**
     * {@code DELETE  /performance-of-foreign-subsidiaries/:id} : delete the "id" performanceOfForeignSubsidiaries.
     *
     * @param id the id of the performanceOfForeignSubsidiariesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/performance-of-foreign-subsidiaries/{id}")
    public ResponseEntity<Void> deletePerformanceOfForeignSubsidiaries(@PathVariable Long id) {
        log.debug("REST request to delete PerformanceOfForeignSubsidiaries : {}", id);
        performanceOfForeignSubsidiariesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/performance-of-foreign-subsidiaries?query=:query} : search for the performanceOfForeignSubsidiaries corresponding
     * to the query.
     *
     * @param query the query of the performanceOfForeignSubsidiaries search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/performance-of-foreign-subsidiaries")
    public ResponseEntity<List<PerformanceOfForeignSubsidiariesDTO>> searchPerformanceOfForeignSubsidiaries(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of PerformanceOfForeignSubsidiaries for query {}", query);
        Page<PerformanceOfForeignSubsidiariesDTO> page = performanceOfForeignSubsidiariesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
