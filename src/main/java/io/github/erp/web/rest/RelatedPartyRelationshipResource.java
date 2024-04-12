package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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

import io.github.erp.repository.RelatedPartyRelationshipRepository;
import io.github.erp.service.RelatedPartyRelationshipQueryService;
import io.github.erp.service.RelatedPartyRelationshipService;
import io.github.erp.service.criteria.RelatedPartyRelationshipCriteria;
import io.github.erp.service.dto.RelatedPartyRelationshipDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RelatedPartyRelationship}.
 */
@RestController
@RequestMapping("/api")
public class RelatedPartyRelationshipResource {

    private final Logger log = LoggerFactory.getLogger(RelatedPartyRelationshipResource.class);

    private static final String ENTITY_NAME = "gdiDataRelatedPartyRelationship";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RelatedPartyRelationshipService relatedPartyRelationshipService;

    private final RelatedPartyRelationshipRepository relatedPartyRelationshipRepository;

    private final RelatedPartyRelationshipQueryService relatedPartyRelationshipQueryService;

    public RelatedPartyRelationshipResource(
        RelatedPartyRelationshipService relatedPartyRelationshipService,
        RelatedPartyRelationshipRepository relatedPartyRelationshipRepository,
        RelatedPartyRelationshipQueryService relatedPartyRelationshipQueryService
    ) {
        this.relatedPartyRelationshipService = relatedPartyRelationshipService;
        this.relatedPartyRelationshipRepository = relatedPartyRelationshipRepository;
        this.relatedPartyRelationshipQueryService = relatedPartyRelationshipQueryService;
    }

    /**
     * {@code POST  /related-party-relationships} : Create a new relatedPartyRelationship.
     *
     * @param relatedPartyRelationshipDTO the relatedPartyRelationshipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new relatedPartyRelationshipDTO, or with status {@code 400 (Bad Request)} if the relatedPartyRelationship has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/related-party-relationships")
    public ResponseEntity<RelatedPartyRelationshipDTO> createRelatedPartyRelationship(
        @Valid @RequestBody RelatedPartyRelationshipDTO relatedPartyRelationshipDTO
    ) throws URISyntaxException {
        log.debug("REST request to save RelatedPartyRelationship : {}", relatedPartyRelationshipDTO);
        if (relatedPartyRelationshipDTO.getId() != null) {
            throw new BadRequestAlertException("A new relatedPartyRelationship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RelatedPartyRelationshipDTO result = relatedPartyRelationshipService.save(relatedPartyRelationshipDTO);
        return ResponseEntity
            .created(new URI("/api/related-party-relationships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /related-party-relationships/:id} : Updates an existing relatedPartyRelationship.
     *
     * @param id the id of the relatedPartyRelationshipDTO to save.
     * @param relatedPartyRelationshipDTO the relatedPartyRelationshipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatedPartyRelationshipDTO,
     * or with status {@code 400 (Bad Request)} if the relatedPartyRelationshipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the relatedPartyRelationshipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/related-party-relationships/{id}")
    public ResponseEntity<RelatedPartyRelationshipDTO> updateRelatedPartyRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RelatedPartyRelationshipDTO relatedPartyRelationshipDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RelatedPartyRelationship : {}, {}", id, relatedPartyRelationshipDTO);
        if (relatedPartyRelationshipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatedPartyRelationshipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relatedPartyRelationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RelatedPartyRelationshipDTO result = relatedPartyRelationshipService.save(relatedPartyRelationshipDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relatedPartyRelationshipDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /related-party-relationships/:id} : Partial updates given fields of an existing relatedPartyRelationship, field will ignore if it is null
     *
     * @param id the id of the relatedPartyRelationshipDTO to save.
     * @param relatedPartyRelationshipDTO the relatedPartyRelationshipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated relatedPartyRelationshipDTO,
     * or with status {@code 400 (Bad Request)} if the relatedPartyRelationshipDTO is not valid,
     * or with status {@code 404 (Not Found)} if the relatedPartyRelationshipDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the relatedPartyRelationshipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/related-party-relationships/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RelatedPartyRelationshipDTO> partialUpdateRelatedPartyRelationship(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RelatedPartyRelationshipDTO relatedPartyRelationshipDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RelatedPartyRelationship partially : {}, {}", id, relatedPartyRelationshipDTO);
        if (relatedPartyRelationshipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, relatedPartyRelationshipDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!relatedPartyRelationshipRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RelatedPartyRelationshipDTO> result = relatedPartyRelationshipService.partialUpdate(relatedPartyRelationshipDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, relatedPartyRelationshipDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /related-party-relationships} : get all the relatedPartyRelationships.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of relatedPartyRelationships in body.
     */
    @GetMapping("/related-party-relationships")
    public ResponseEntity<List<RelatedPartyRelationshipDTO>> getAllRelatedPartyRelationships(
        RelatedPartyRelationshipCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get RelatedPartyRelationships by criteria: {}", criteria);
        Page<RelatedPartyRelationshipDTO> page = relatedPartyRelationshipQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /related-party-relationships/count} : count all the relatedPartyRelationships.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/related-party-relationships/count")
    public ResponseEntity<Long> countRelatedPartyRelationships(RelatedPartyRelationshipCriteria criteria) {
        log.debug("REST request to count RelatedPartyRelationships by criteria: {}", criteria);
        return ResponseEntity.ok().body(relatedPartyRelationshipQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /related-party-relationships/:id} : get the "id" relatedPartyRelationship.
     *
     * @param id the id of the relatedPartyRelationshipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the relatedPartyRelationshipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/related-party-relationships/{id}")
    public ResponseEntity<RelatedPartyRelationshipDTO> getRelatedPartyRelationship(@PathVariable Long id) {
        log.debug("REST request to get RelatedPartyRelationship : {}", id);
        Optional<RelatedPartyRelationshipDTO> relatedPartyRelationshipDTO = relatedPartyRelationshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(relatedPartyRelationshipDTO);
    }

    /**
     * {@code DELETE  /related-party-relationships/:id} : delete the "id" relatedPartyRelationship.
     *
     * @param id the id of the relatedPartyRelationshipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/related-party-relationships/{id}")
    public ResponseEntity<Void> deleteRelatedPartyRelationship(@PathVariable Long id) {
        log.debug("REST request to delete RelatedPartyRelationship : {}", id);
        relatedPartyRelationshipService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/related-party-relationships?query=:query} : search for the relatedPartyRelationship corresponding
     * to the query.
     *
     * @param query the query of the relatedPartyRelationship search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/related-party-relationships")
    public ResponseEntity<List<RelatedPartyRelationshipDTO>> searchRelatedPartyRelationships(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of RelatedPartyRelationships for query {}", query);
        Page<RelatedPartyRelationshipDTO> page = relatedPartyRelationshipService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
