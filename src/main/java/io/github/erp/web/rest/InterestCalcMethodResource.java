package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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

import io.github.erp.repository.InterestCalcMethodRepository;
import io.github.erp.service.InterestCalcMethodQueryService;
import io.github.erp.service.InterestCalcMethodService;
import io.github.erp.service.criteria.InterestCalcMethodCriteria;
import io.github.erp.service.dto.InterestCalcMethodDTO;
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
 * REST controller for managing {@link io.github.erp.domain.InterestCalcMethod}.
 */
@RestController
@RequestMapping("/api")
public class InterestCalcMethodResource {

    private final Logger log = LoggerFactory.getLogger(InterestCalcMethodResource.class);

    private static final String ENTITY_NAME = "interestCalcMethod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InterestCalcMethodService interestCalcMethodService;

    private final InterestCalcMethodRepository interestCalcMethodRepository;

    private final InterestCalcMethodQueryService interestCalcMethodQueryService;

    public InterestCalcMethodResource(
        InterestCalcMethodService interestCalcMethodService,
        InterestCalcMethodRepository interestCalcMethodRepository,
        InterestCalcMethodQueryService interestCalcMethodQueryService
    ) {
        this.interestCalcMethodService = interestCalcMethodService;
        this.interestCalcMethodRepository = interestCalcMethodRepository;
        this.interestCalcMethodQueryService = interestCalcMethodQueryService;
    }

    /**
     * {@code POST  /interest-calc-methods} : Create a new interestCalcMethod.
     *
     * @param interestCalcMethodDTO the interestCalcMethodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new interestCalcMethodDTO, or with status {@code 400 (Bad Request)} if the interestCalcMethod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/interest-calc-methods")
    public ResponseEntity<InterestCalcMethodDTO> createInterestCalcMethod(@Valid @RequestBody InterestCalcMethodDTO interestCalcMethodDTO)
        throws URISyntaxException {
        log.debug("REST request to save InterestCalcMethod : {}", interestCalcMethodDTO);
        if (interestCalcMethodDTO.getId() != null) {
            throw new BadRequestAlertException("A new interestCalcMethod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        InterestCalcMethodDTO result = interestCalcMethodService.save(interestCalcMethodDTO);
        return ResponseEntity
            .created(new URI("/api/interest-calc-methods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /interest-calc-methods/:id} : Updates an existing interestCalcMethod.
     *
     * @param id the id of the interestCalcMethodDTO to save.
     * @param interestCalcMethodDTO the interestCalcMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interestCalcMethodDTO,
     * or with status {@code 400 (Bad Request)} if the interestCalcMethodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the interestCalcMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/interest-calc-methods/{id}")
    public ResponseEntity<InterestCalcMethodDTO> updateInterestCalcMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InterestCalcMethodDTO interestCalcMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update InterestCalcMethod : {}, {}", id, interestCalcMethodDTO);
        if (interestCalcMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interestCalcMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interestCalcMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        InterestCalcMethodDTO result = interestCalcMethodService.save(interestCalcMethodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interestCalcMethodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /interest-calc-methods/:id} : Partial updates given fields of an existing interestCalcMethod, field will ignore if it is null
     *
     * @param id the id of the interestCalcMethodDTO to save.
     * @param interestCalcMethodDTO the interestCalcMethodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated interestCalcMethodDTO,
     * or with status {@code 400 (Bad Request)} if the interestCalcMethodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the interestCalcMethodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the interestCalcMethodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/interest-calc-methods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InterestCalcMethodDTO> partialUpdateInterestCalcMethod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InterestCalcMethodDTO interestCalcMethodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update InterestCalcMethod partially : {}, {}", id, interestCalcMethodDTO);
        if (interestCalcMethodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, interestCalcMethodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!interestCalcMethodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InterestCalcMethodDTO> result = interestCalcMethodService.partialUpdate(interestCalcMethodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, interestCalcMethodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /interest-calc-methods} : get all the interestCalcMethods.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of interestCalcMethods in body.
     */
    @GetMapping("/interest-calc-methods")
    public ResponseEntity<List<InterestCalcMethodDTO>> getAllInterestCalcMethods(InterestCalcMethodCriteria criteria, Pageable pageable) {
        log.debug("REST request to get InterestCalcMethods by criteria: {}", criteria);
        Page<InterestCalcMethodDTO> page = interestCalcMethodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /interest-calc-methods/count} : count all the interestCalcMethods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/interest-calc-methods/count")
    public ResponseEntity<Long> countInterestCalcMethods(InterestCalcMethodCriteria criteria) {
        log.debug("REST request to count InterestCalcMethods by criteria: {}", criteria);
        return ResponseEntity.ok().body(interestCalcMethodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /interest-calc-methods/:id} : get the "id" interestCalcMethod.
     *
     * @param id the id of the interestCalcMethodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the interestCalcMethodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/interest-calc-methods/{id}")
    public ResponseEntity<InterestCalcMethodDTO> getInterestCalcMethod(@PathVariable Long id) {
        log.debug("REST request to get InterestCalcMethod : {}", id);
        Optional<InterestCalcMethodDTO> interestCalcMethodDTO = interestCalcMethodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(interestCalcMethodDTO);
    }

    /**
     * {@code DELETE  /interest-calc-methods/:id} : delete the "id" interestCalcMethod.
     *
     * @param id the id of the interestCalcMethodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/interest-calc-methods/{id}")
    public ResponseEntity<Void> deleteInterestCalcMethod(@PathVariable Long id) {
        log.debug("REST request to delete InterestCalcMethod : {}", id);
        interestCalcMethodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/interest-calc-methods?query=:query} : search for the interestCalcMethod corresponding
     * to the query.
     *
     * @param query the query of the interestCalcMethod search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/interest-calc-methods")
    public ResponseEntity<List<InterestCalcMethodDTO>> searchInterestCalcMethods(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of InterestCalcMethods for query {}", query);
        Page<InterestCalcMethodDTO> page = interestCalcMethodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
