package io.github.erp.erp.resources.leases;
/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.repository.LeaseTemplateRepository;
import io.github.erp.service.LeaseTemplateQueryService;
import io.github.erp.service.LeaseTemplateService;
import io.github.erp.service.criteria.LeaseTemplateCriteria;
import io.github.erp.service.dto.LeaseTemplateDTO;
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
 * REST controller for managing {@link io.github.erp.domain.LeaseTemplate}.
 */
@RestController
@RequestMapping("/api/leases")
public class LeaseTemplateResourceProd {

    private final Logger log = LoggerFactory.getLogger(LeaseTemplateResourceProd.class);

    private static final String ENTITY_NAME = "leaseTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LeaseTemplateService leaseTemplateService;

    private final LeaseTemplateRepository leaseTemplateRepository;

    private final LeaseTemplateQueryService leaseTemplateQueryService;

    public LeaseTemplateResourceProd(
        LeaseTemplateService leaseTemplateService,
        LeaseTemplateRepository leaseTemplateRepository,
        LeaseTemplateQueryService leaseTemplateQueryService
    ) {
        this.leaseTemplateService = leaseTemplateService;
        this.leaseTemplateRepository = leaseTemplateRepository;
        this.leaseTemplateQueryService = leaseTemplateQueryService;
    }

    /**
     * {@code POST  /lease-templates} : Create a new leaseTemplate.
     *
     * @param leaseTemplateDTO the leaseTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new leaseTemplateDTO, or with status {@code 400 (Bad Request)} if the leaseTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lease-templates")
    public ResponseEntity<LeaseTemplateDTO> createLeaseTemplate(@Valid @RequestBody LeaseTemplateDTO leaseTemplateDTO)
        throws URISyntaxException {
        log.debug("REST request to save LeaseTemplate : {}", leaseTemplateDTO);
        if (leaseTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new leaseTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        LeaseTemplateDTO result = leaseTemplateService.save(leaseTemplateDTO);
        return ResponseEntity
            .created(new URI("/api/leases/lease-templates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lease-templates/:id} : Updates an existing leaseTemplate.
     *
     * @param id the id of the leaseTemplateDTO to save.
     * @param leaseTemplateDTO the leaseTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the leaseTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the leaseTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lease-templates/{id}")
    public ResponseEntity<LeaseTemplateDTO> updateLeaseTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LeaseTemplateDTO leaseTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update LeaseTemplate : {}, {}", id, leaseTemplateDTO);
        if (leaseTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        LeaseTemplateDTO result = leaseTemplateService.save(leaseTemplateDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseTemplateDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lease-templates/:id} : Partial updates given fields of an existing leaseTemplate, field will ignore if it is null
     *
     * @param id the id of the leaseTemplateDTO to save.
     * @param leaseTemplateDTO the leaseTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated leaseTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the leaseTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the leaseTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the leaseTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lease-templates/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<LeaseTemplateDTO> partialUpdateLeaseTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LeaseTemplateDTO leaseTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update LeaseTemplate partially : {}, {}", id, leaseTemplateDTO);
        if (leaseTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, leaseTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!leaseTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LeaseTemplateDTO> result = leaseTemplateService.partialUpdate(leaseTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, leaseTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /lease-templates} : get all the leaseTemplates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of leaseTemplates in body.
     */
    @GetMapping("/lease-templates")
    public ResponseEntity<List<LeaseTemplateDTO>> getAllLeaseTemplates(LeaseTemplateCriteria criteria, Pageable pageable) {
        log.debug("REST request to get LeaseTemplates by criteria: {}", criteria);
        Page<LeaseTemplateDTO> page = leaseTemplateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lease-templates/count} : count all the leaseTemplates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/lease-templates/count")
    public ResponseEntity<Long> countLeaseTemplates(LeaseTemplateCriteria criteria) {
        log.debug("REST request to count LeaseTemplates by criteria: {}", criteria);
        return ResponseEntity.ok().body(leaseTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /lease-templates/:id} : get the "id" leaseTemplate.
     *
     * @param id the id of the leaseTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the leaseTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lease-templates/{id}")
    public ResponseEntity<LeaseTemplateDTO> getLeaseTemplate(@PathVariable Long id) {
        log.debug("REST request to get LeaseTemplate : {}", id);
        Optional<LeaseTemplateDTO> leaseTemplateDTO = leaseTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(leaseTemplateDTO);
    }

    /**
     * {@code DELETE  /lease-templates/:id} : delete the "id" leaseTemplate.
     *
     * @param id the id of the leaseTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lease-templates/{id}")
    public ResponseEntity<Void> deleteLeaseTemplate(@PathVariable Long id) {
        log.debug("REST request to delete LeaseTemplate : {}", id);
        leaseTemplateService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/lease-templates?query=:query} : search for the leaseTemplate corresponding
     * to the query.
     *
     * @param query the query of the leaseTemplate search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/lease-templates")
    public ResponseEntity<List<LeaseTemplateDTO>> searchLeaseTemplates(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of LeaseTemplates for query {}", query);
        Page<LeaseTemplateDTO> page = leaseTemplateService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
