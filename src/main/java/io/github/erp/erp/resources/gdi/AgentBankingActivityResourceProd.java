package io.github.erp.erp.resources.gdi;

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
import io.github.erp.repository.AgentBankingActivityRepository;
import io.github.erp.service.AgentBankingActivityQueryService;
import io.github.erp.service.AgentBankingActivityService;
import io.github.erp.service.criteria.AgentBankingActivityCriteria;
import io.github.erp.service.dto.AgentBankingActivityDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AgentBankingActivity}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class AgentBankingActivityResourceProd {

    private final Logger log = LoggerFactory.getLogger(AgentBankingActivityResourceProd.class);

    private static final String ENTITY_NAME = "gdiDataAgentBankingActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AgentBankingActivityService agentBankingActivityService;

    private final AgentBankingActivityRepository agentBankingActivityRepository;

    private final AgentBankingActivityQueryService agentBankingActivityQueryService;

    public AgentBankingActivityResourceProd(
        AgentBankingActivityService agentBankingActivityService,
        AgentBankingActivityRepository agentBankingActivityRepository,
        AgentBankingActivityQueryService agentBankingActivityQueryService
    ) {
        this.agentBankingActivityService = agentBankingActivityService;
        this.agentBankingActivityRepository = agentBankingActivityRepository;
        this.agentBankingActivityQueryService = agentBankingActivityQueryService;
    }

    /**
     * {@code POST  /agent-banking-activities} : Create a new agentBankingActivity.
     *
     * @param agentBankingActivityDTO the agentBankingActivityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new agentBankingActivityDTO, or with status {@code 400 (Bad Request)} if the agentBankingActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/agent-banking-activities")
    public ResponseEntity<AgentBankingActivityDTO> createAgentBankingActivity(
        @Valid @RequestBody AgentBankingActivityDTO agentBankingActivityDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AgentBankingActivity : {}", agentBankingActivityDTO);
        if (agentBankingActivityDTO.getId() != null) {
            throw new BadRequestAlertException("A new agentBankingActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AgentBankingActivityDTO result = agentBankingActivityService.save(agentBankingActivityDTO);
        return ResponseEntity
            .created(new URI("/api/agent-banking-activities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /agent-banking-activities/:id} : Updates an existing agentBankingActivity.
     *
     * @param id the id of the agentBankingActivityDTO to save.
     * @param agentBankingActivityDTO the agentBankingActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentBankingActivityDTO,
     * or with status {@code 400 (Bad Request)} if the agentBankingActivityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the agentBankingActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/agent-banking-activities/{id}")
    public ResponseEntity<AgentBankingActivityDTO> updateAgentBankingActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AgentBankingActivityDTO agentBankingActivityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AgentBankingActivity : {}, {}", id, agentBankingActivityDTO);
        if (agentBankingActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agentBankingActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentBankingActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AgentBankingActivityDTO result = agentBankingActivityService.save(agentBankingActivityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentBankingActivityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /agent-banking-activities/:id} : Partial updates given fields of an existing agentBankingActivity, field will ignore if it is null
     *
     * @param id the id of the agentBankingActivityDTO to save.
     * @param agentBankingActivityDTO the agentBankingActivityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated agentBankingActivityDTO,
     * or with status {@code 400 (Bad Request)} if the agentBankingActivityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the agentBankingActivityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the agentBankingActivityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/agent-banking-activities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AgentBankingActivityDTO> partialUpdateAgentBankingActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AgentBankingActivityDTO agentBankingActivityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AgentBankingActivity partially : {}, {}", id, agentBankingActivityDTO);
        if (agentBankingActivityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, agentBankingActivityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!agentBankingActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AgentBankingActivityDTO> result = agentBankingActivityService.partialUpdate(agentBankingActivityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, agentBankingActivityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /agent-banking-activities} : get all the agentBankingActivities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of agentBankingActivities in body.
     */
    @GetMapping("/agent-banking-activities")
    public ResponseEntity<List<AgentBankingActivityDTO>> getAllAgentBankingActivities(
        AgentBankingActivityCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AgentBankingActivities by criteria: {}", criteria);
        Page<AgentBankingActivityDTO> page = agentBankingActivityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /agent-banking-activities/count} : count all the agentBankingActivities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/agent-banking-activities/count")
    public ResponseEntity<Long> countAgentBankingActivities(AgentBankingActivityCriteria criteria) {
        log.debug("REST request to count AgentBankingActivities by criteria: {}", criteria);
        return ResponseEntity.ok().body(agentBankingActivityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /agent-banking-activities/:id} : get the "id" agentBankingActivity.
     *
     * @param id the id of the agentBankingActivityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the agentBankingActivityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/agent-banking-activities/{id}")
    public ResponseEntity<AgentBankingActivityDTO> getAgentBankingActivity(@PathVariable Long id) {
        log.debug("REST request to get AgentBankingActivity : {}", id);
        Optional<AgentBankingActivityDTO> agentBankingActivityDTO = agentBankingActivityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(agentBankingActivityDTO);
    }

    /**
     * {@code DELETE  /agent-banking-activities/:id} : delete the "id" agentBankingActivity.
     *
     * @param id the id of the agentBankingActivityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/agent-banking-activities/{id}")
    public ResponseEntity<Void> deleteAgentBankingActivity(@PathVariable Long id) {
        log.debug("REST request to delete AgentBankingActivity : {}", id);
        agentBankingActivityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/agent-banking-activities?query=:query} : search for the agentBankingActivity corresponding
     * to the query.
     *
     * @param query the query of the agentBankingActivity search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/agent-banking-activities")
    public ResponseEntity<List<AgentBankingActivityDTO>> searchAgentBankingActivities(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AgentBankingActivities for query {}", query);
        Page<AgentBankingActivityDTO> page = agentBankingActivityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
