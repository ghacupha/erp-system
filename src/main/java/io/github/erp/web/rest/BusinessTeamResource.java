package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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

import io.github.erp.repository.BusinessTeamRepository;
import io.github.erp.service.BusinessTeamQueryService;
import io.github.erp.service.BusinessTeamService;
import io.github.erp.service.criteria.BusinessTeamCriteria;
import io.github.erp.service.dto.BusinessTeamDTO;
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
 * REST controller for managing {@link io.github.erp.domain.BusinessTeam}.
 */
@RestController
@RequestMapping("/api")
public class BusinessTeamResource {

    private final Logger log = LoggerFactory.getLogger(BusinessTeamResource.class);

    private static final String ENTITY_NAME = "businessTeam";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BusinessTeamService businessTeamService;

    private final BusinessTeamRepository businessTeamRepository;

    private final BusinessTeamQueryService businessTeamQueryService;

    public BusinessTeamResource(
        BusinessTeamService businessTeamService,
        BusinessTeamRepository businessTeamRepository,
        BusinessTeamQueryService businessTeamQueryService
    ) {
        this.businessTeamService = businessTeamService;
        this.businessTeamRepository = businessTeamRepository;
        this.businessTeamQueryService = businessTeamQueryService;
    }

    /**
     * {@code POST  /business-teams} : Create a new businessTeam.
     *
     * @param businessTeamDTO the businessTeamDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new businessTeamDTO, or with status {@code 400 (Bad Request)} if the businessTeam has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/business-teams")
    public ResponseEntity<BusinessTeamDTO> createBusinessTeam(@Valid @RequestBody BusinessTeamDTO businessTeamDTO)
        throws URISyntaxException {
        log.debug("REST request to save BusinessTeam : {}", businessTeamDTO);
        if (businessTeamDTO.getId() != null) {
            throw new BadRequestAlertException("A new businessTeam cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BusinessTeamDTO result = businessTeamService.save(businessTeamDTO);
        return ResponseEntity
            .created(new URI("/api/business-teams/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /business-teams/:id} : Updates an existing businessTeam.
     *
     * @param id the id of the businessTeamDTO to save.
     * @param businessTeamDTO the businessTeamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTeamDTO,
     * or with status {@code 400 (Bad Request)} if the businessTeamDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the businessTeamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/business-teams/{id}")
    public ResponseEntity<BusinessTeamDTO> updateBusinessTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BusinessTeamDTO businessTeamDTO
    ) throws URISyntaxException {
        log.debug("REST request to update BusinessTeam : {}, {}", id, businessTeamDTO);
        if (businessTeamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessTeamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessTeamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BusinessTeamDTO result = businessTeamService.save(businessTeamDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessTeamDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /business-teams/:id} : Partial updates given fields of an existing businessTeam, field will ignore if it is null
     *
     * @param id the id of the businessTeamDTO to save.
     * @param businessTeamDTO the businessTeamDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated businessTeamDTO,
     * or with status {@code 400 (Bad Request)} if the businessTeamDTO is not valid,
     * or with status {@code 404 (Not Found)} if the businessTeamDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the businessTeamDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/business-teams/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BusinessTeamDTO> partialUpdateBusinessTeam(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BusinessTeamDTO businessTeamDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update BusinessTeam partially : {}, {}", id, businessTeamDTO);
        if (businessTeamDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, businessTeamDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!businessTeamRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BusinessTeamDTO> result = businessTeamService.partialUpdate(businessTeamDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, businessTeamDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /business-teams} : get all the businessTeams.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of businessTeams in body.
     */
    @GetMapping("/business-teams")
    public ResponseEntity<List<BusinessTeamDTO>> getAllBusinessTeams(BusinessTeamCriteria criteria, Pageable pageable) {
        log.debug("REST request to get BusinessTeams by criteria: {}", criteria);
        Page<BusinessTeamDTO> page = businessTeamQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /business-teams/count} : count all the businessTeams.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/business-teams/count")
    public ResponseEntity<Long> countBusinessTeams(BusinessTeamCriteria criteria) {
        log.debug("REST request to count BusinessTeams by criteria: {}", criteria);
        return ResponseEntity.ok().body(businessTeamQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /business-teams/:id} : get the "id" businessTeam.
     *
     * @param id the id of the businessTeamDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the businessTeamDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/business-teams/{id}")
    public ResponseEntity<BusinessTeamDTO> getBusinessTeam(@PathVariable Long id) {
        log.debug("REST request to get BusinessTeam : {}", id);
        Optional<BusinessTeamDTO> businessTeamDTO = businessTeamService.findOne(id);
        return ResponseUtil.wrapOrNotFound(businessTeamDTO);
    }

    /**
     * {@code DELETE  /business-teams/:id} : delete the "id" businessTeam.
     *
     * @param id the id of the businessTeamDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/business-teams/{id}")
    public ResponseEntity<Void> deleteBusinessTeam(@PathVariable Long id) {
        log.debug("REST request to delete BusinessTeam : {}", id);
        businessTeamService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/business-teams?query=:query} : search for the businessTeam corresponding
     * to the query.
     *
     * @param query the query of the businessTeam search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/business-teams")
    public ResponseEntity<List<BusinessTeamDTO>> searchBusinessTeams(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of BusinessTeams for query {}", query);
        Page<BusinessTeamDTO> page = businessTeamService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
