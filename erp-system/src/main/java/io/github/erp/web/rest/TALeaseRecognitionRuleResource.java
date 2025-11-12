package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.TALeaseRecognitionRuleRepository;
import io.github.erp.service.TALeaseRecognitionRuleQueryService;
import io.github.erp.service.TALeaseRecognitionRuleService;
import io.github.erp.service.criteria.TALeaseRecognitionRuleCriteria;
import io.github.erp.service.dto.TALeaseRecognitionRuleDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TALeaseRecognitionRule}.
 */
@RestController
@RequestMapping("/api")
public class TALeaseRecognitionRuleResource {

    private final Logger log = LoggerFactory.getLogger(TALeaseRecognitionRuleResource.class);

    private static final String ENTITY_NAME = "accountingTaLeaseRecognitionRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TALeaseRecognitionRuleService tALeaseRecognitionRuleService;

    private final TALeaseRecognitionRuleRepository tALeaseRecognitionRuleRepository;

    private final TALeaseRecognitionRuleQueryService tALeaseRecognitionRuleQueryService;

    public TALeaseRecognitionRuleResource(
        TALeaseRecognitionRuleService tALeaseRecognitionRuleService,
        TALeaseRecognitionRuleRepository tALeaseRecognitionRuleRepository,
        TALeaseRecognitionRuleQueryService tALeaseRecognitionRuleQueryService
    ) {
        this.tALeaseRecognitionRuleService = tALeaseRecognitionRuleService;
        this.tALeaseRecognitionRuleRepository = tALeaseRecognitionRuleRepository;
        this.tALeaseRecognitionRuleQueryService = tALeaseRecognitionRuleQueryService;
    }

    /**
     * {@code POST  /ta-lease-recognition-rules} : Create a new tALeaseRecognitionRule.
     *
     * @param tALeaseRecognitionRuleDTO the tALeaseRecognitionRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tALeaseRecognitionRuleDTO, or with status {@code 400 (Bad Request)} if the tALeaseRecognitionRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ta-lease-recognition-rules")
    public ResponseEntity<TALeaseRecognitionRuleDTO> createTALeaseRecognitionRule(
        @Valid @RequestBody TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TALeaseRecognitionRule : {}", tALeaseRecognitionRuleDTO);
        if (tALeaseRecognitionRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new tALeaseRecognitionRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TALeaseRecognitionRuleDTO result = tALeaseRecognitionRuleService.save(tALeaseRecognitionRuleDTO);
        return ResponseEntity
            .created(new URI("/api/ta-lease-recognition-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ta-lease-recognition-rules/:id} : Updates an existing tALeaseRecognitionRule.
     *
     * @param id the id of the tALeaseRecognitionRuleDTO to save.
     * @param tALeaseRecognitionRuleDTO the tALeaseRecognitionRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tALeaseRecognitionRuleDTO,
     * or with status {@code 400 (Bad Request)} if the tALeaseRecognitionRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tALeaseRecognitionRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ta-lease-recognition-rules/{id}")
    public ResponseEntity<TALeaseRecognitionRuleDTO> updateTALeaseRecognitionRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TALeaseRecognitionRule : {}, {}", id, tALeaseRecognitionRuleDTO);
        if (tALeaseRecognitionRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tALeaseRecognitionRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tALeaseRecognitionRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TALeaseRecognitionRuleDTO result = tALeaseRecognitionRuleService.save(tALeaseRecognitionRuleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tALeaseRecognitionRuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ta-lease-recognition-rules/:id} : Partial updates given fields of an existing tALeaseRecognitionRule, field will ignore if it is null
     *
     * @param id the id of the tALeaseRecognitionRuleDTO to save.
     * @param tALeaseRecognitionRuleDTO the tALeaseRecognitionRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tALeaseRecognitionRuleDTO,
     * or with status {@code 400 (Bad Request)} if the tALeaseRecognitionRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tALeaseRecognitionRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tALeaseRecognitionRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ta-lease-recognition-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TALeaseRecognitionRuleDTO> partialUpdateTALeaseRecognitionRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TALeaseRecognitionRuleDTO tALeaseRecognitionRuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TALeaseRecognitionRule partially : {}, {}", id, tALeaseRecognitionRuleDTO);
        if (tALeaseRecognitionRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tALeaseRecognitionRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tALeaseRecognitionRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TALeaseRecognitionRuleDTO> result = tALeaseRecognitionRuleService.partialUpdate(tALeaseRecognitionRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tALeaseRecognitionRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ta-lease-recognition-rules} : get all the tALeaseRecognitionRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tALeaseRecognitionRules in body.
     */
    @GetMapping("/ta-lease-recognition-rules")
    public ResponseEntity<List<TALeaseRecognitionRuleDTO>> getAllTALeaseRecognitionRules(
        TALeaseRecognitionRuleCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TALeaseRecognitionRules by criteria: {}", criteria);
        Page<TALeaseRecognitionRuleDTO> page = tALeaseRecognitionRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ta-lease-recognition-rules/count} : count all the tALeaseRecognitionRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ta-lease-recognition-rules/count")
    public ResponseEntity<Long> countTALeaseRecognitionRules(TALeaseRecognitionRuleCriteria criteria) {
        log.debug("REST request to count TALeaseRecognitionRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(tALeaseRecognitionRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ta-lease-recognition-rules/:id} : get the "id" tALeaseRecognitionRule.
     *
     * @param id the id of the tALeaseRecognitionRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tALeaseRecognitionRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ta-lease-recognition-rules/{id}")
    public ResponseEntity<TALeaseRecognitionRuleDTO> getTALeaseRecognitionRule(@PathVariable Long id) {
        log.debug("REST request to get TALeaseRecognitionRule : {}", id);
        Optional<TALeaseRecognitionRuleDTO> tALeaseRecognitionRuleDTO = tALeaseRecognitionRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tALeaseRecognitionRuleDTO);
    }

    /**
     * {@code DELETE  /ta-lease-recognition-rules/:id} : delete the "id" tALeaseRecognitionRule.
     *
     * @param id the id of the tALeaseRecognitionRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ta-lease-recognition-rules/{id}")
    public ResponseEntity<Void> deleteTALeaseRecognitionRule(@PathVariable Long id) {
        log.debug("REST request to delete TALeaseRecognitionRule : {}", id);
        tALeaseRecognitionRuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ta-lease-recognition-rules?query=:query} : search for the tALeaseRecognitionRule corresponding
     * to the query.
     *
     * @param query the query of the tALeaseRecognitionRule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ta-lease-recognition-rules")
    public ResponseEntity<List<TALeaseRecognitionRuleDTO>> searchTALeaseRecognitionRules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TALeaseRecognitionRules for query {}", query);
        Page<TALeaseRecognitionRuleDTO> page = tALeaseRecognitionRuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
