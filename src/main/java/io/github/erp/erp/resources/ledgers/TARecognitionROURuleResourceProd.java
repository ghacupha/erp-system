package io.github.erp.erp.resources.ledgers;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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

import io.github.erp.internal.repository.InternalTARecognitionROURuleRepository;
import io.github.erp.internal.service.ledgers.InternalTARecognitionROURuleService;
import io.github.erp.repository.TARecognitionROURuleRepository;
import io.github.erp.service.TARecognitionROURuleQueryService;
import io.github.erp.service.TARecognitionROURuleService;
import io.github.erp.service.criteria.TARecognitionROURuleCriteria;
import io.github.erp.service.dto.TARecognitionROURuleDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TARecognitionROURule}.
 */
@RestController
@RequestMapping("/api")
public class TARecognitionROURuleResourceProd {

    private final Logger log = LoggerFactory.getLogger(TARecognitionROURuleResourceProd.class);

    private static final String ENTITY_NAME = "tARecognitionROURule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalTARecognitionROURuleService tARecognitionROURuleService;

    private final InternalTARecognitionROURuleRepository tARecognitionROURuleRepository;

    private final TARecognitionROURuleQueryService tARecognitionROURuleQueryService;

    public TARecognitionROURuleResourceProd(
        InternalTARecognitionROURuleService tARecognitionROURuleService,
        InternalTARecognitionROURuleRepository tARecognitionROURuleRepository,
        TARecognitionROURuleQueryService tARecognitionROURuleQueryService
    ) {
        this.tARecognitionROURuleService = tARecognitionROURuleService;
        this.tARecognitionROURuleRepository = tARecognitionROURuleRepository;
        this.tARecognitionROURuleQueryService = tARecognitionROURuleQueryService;
    }

    /**
     * {@code POST  /ta-recognition-rou-rules} : Create a new tARecognitionROURule.
     *
     * @param tARecognitionROURuleDTO the tARecognitionROURuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tARecognitionROURuleDTO, or with status {@code 400 (Bad Request)} if the tARecognitionROURule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ta-recognition-rou-rules")
    public ResponseEntity<TARecognitionROURuleDTO> createTARecognitionROURule(
        @Valid @RequestBody TARecognitionROURuleDTO tARecognitionROURuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TARecognitionROURule : {}", tARecognitionROURuleDTO);
        if (tARecognitionROURuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new tARecognitionROURule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TARecognitionROURuleDTO result = tARecognitionROURuleService.save(tARecognitionROURuleDTO);
        return ResponseEntity
            .created(new URI("/api/ta-recognition-rou-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ta-recognition-rou-rules/:id} : Updates an existing tARecognitionROURule.
     *
     * @param id the id of the tARecognitionROURuleDTO to save.
     * @param tARecognitionROURuleDTO the tARecognitionROURuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tARecognitionROURuleDTO,
     * or with status {@code 400 (Bad Request)} if the tARecognitionROURuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tARecognitionROURuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ta-recognition-rou-rules/{id}")
    public ResponseEntity<TARecognitionROURuleDTO> updateTARecognitionROURule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TARecognitionROURuleDTO tARecognitionROURuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TARecognitionROURule : {}, {}", id, tARecognitionROURuleDTO);
        if (tARecognitionROURuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tARecognitionROURuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tARecognitionROURuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TARecognitionROURuleDTO result = tARecognitionROURuleService.save(tARecognitionROURuleDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tARecognitionROURuleDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ta-recognition-rou-rules/:id} : Partial updates given fields of an existing tARecognitionROURule, field will ignore if it is null
     *
     * @param id the id of the tARecognitionROURuleDTO to save.
     * @param tARecognitionROURuleDTO the tARecognitionROURuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tARecognitionROURuleDTO,
     * or with status {@code 400 (Bad Request)} if the tARecognitionROURuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tARecognitionROURuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tARecognitionROURuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ta-recognition-rou-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TARecognitionROURuleDTO> partialUpdateTARecognitionROURule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TARecognitionROURuleDTO tARecognitionROURuleDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update TARecognitionROURule partially : {}, {}", id, tARecognitionROURuleDTO);
        if (tARecognitionROURuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tARecognitionROURuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tARecognitionROURuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TARecognitionROURuleDTO> result = tARecognitionROURuleService.partialUpdate(tARecognitionROURuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tARecognitionROURuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ta-recognition-rou-rules} : get all the tARecognitionROURules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tARecognitionROURules in body.
     */
    @GetMapping("/ta-recognition-rou-rules")
    public ResponseEntity<List<TARecognitionROURuleDTO>> getAllTARecognitionROURules(
        TARecognitionROURuleCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TARecognitionROURules by criteria: {}", criteria);
        Page<TARecognitionROURuleDTO> page = tARecognitionROURuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ta-recognition-rou-rules/count} : count all the tARecognitionROURules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/ta-recognition-rou-rules/count")
    public ResponseEntity<Long> countTARecognitionROURules(TARecognitionROURuleCriteria criteria) {
        log.debug("REST request to count TARecognitionROURules by criteria: {}", criteria);
        return ResponseEntity.ok().body(tARecognitionROURuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /ta-recognition-rou-rules/:id} : get the "id" tARecognitionROURule.
     *
     * @param id the id of the tARecognitionROURuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tARecognitionROURuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ta-recognition-rou-rules/{id}")
    public ResponseEntity<TARecognitionROURuleDTO> getTARecognitionROURule(@PathVariable Long id) {
        log.debug("REST request to get TARecognitionROURule : {}", id);
        Optional<TARecognitionROURuleDTO> tARecognitionROURuleDTO = tARecognitionROURuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tARecognitionROURuleDTO);
    }

    /**
     * {@code DELETE  /ta-recognition-rou-rules/:id} : delete the "id" tARecognitionROURule.
     *
     * @param id the id of the tARecognitionROURuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ta-recognition-rou-rules/{id}")
    public ResponseEntity<Void> deleteTARecognitionROURule(@PathVariable Long id) {
        log.debug("REST request to delete TARecognitionROURule : {}", id);
        tARecognitionROURuleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/ta-recognition-rou-rules?query=:query} : search for the tARecognitionROURule corresponding
     * to the query.
     *
     * @param query the query of the tARecognitionROURule search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/ta-recognition-rou-rules")
    public ResponseEntity<List<TARecognitionROURuleDTO>> searchTARecognitionROURules(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of TARecognitionROURules for query {}", query);
        Page<TARecognitionROURuleDTO> page = tARecognitionROURuleService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
