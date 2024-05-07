package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.repository.PartyRelationTypeRepository;
import io.github.erp.service.PartyRelationTypeQueryService;
import io.github.erp.service.PartyRelationTypeService;
import io.github.erp.service.criteria.PartyRelationTypeCriteria;
import io.github.erp.service.dto.PartyRelationTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.PartyRelationType}.
 */
@RestController
@RequestMapping("/api")
public class PartyRelationTypeResource {

    private final Logger log = LoggerFactory.getLogger(PartyRelationTypeResource.class);

    private static final String ENTITY_NAME = "partyRelationType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PartyRelationTypeService partyRelationTypeService;

    private final PartyRelationTypeRepository partyRelationTypeRepository;

    private final PartyRelationTypeQueryService partyRelationTypeQueryService;

    public PartyRelationTypeResource(
        PartyRelationTypeService partyRelationTypeService,
        PartyRelationTypeRepository partyRelationTypeRepository,
        PartyRelationTypeQueryService partyRelationTypeQueryService
    ) {
        this.partyRelationTypeService = partyRelationTypeService;
        this.partyRelationTypeRepository = partyRelationTypeRepository;
        this.partyRelationTypeQueryService = partyRelationTypeQueryService;
    }

    /**
     * {@code POST  /party-relation-types} : Create a new partyRelationType.
     *
     * @param partyRelationTypeDTO the partyRelationTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new partyRelationTypeDTO, or with status {@code 400 (Bad Request)} if the partyRelationType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/party-relation-types")
    public ResponseEntity<PartyRelationTypeDTO> createPartyRelationType(@Valid @RequestBody PartyRelationTypeDTO partyRelationTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save PartyRelationType : {}", partyRelationTypeDTO);
        if (partyRelationTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new partyRelationType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PartyRelationTypeDTO result = partyRelationTypeService.save(partyRelationTypeDTO);
        return ResponseEntity
            .created(new URI("/api/party-relation-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /party-relation-types/:id} : Updates an existing partyRelationType.
     *
     * @param id the id of the partyRelationTypeDTO to save.
     * @param partyRelationTypeDTO the partyRelationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyRelationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the partyRelationTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the partyRelationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/party-relation-types/{id}")
    public ResponseEntity<PartyRelationTypeDTO> updatePartyRelationType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PartyRelationTypeDTO partyRelationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PartyRelationType : {}, {}", id, partyRelationTypeDTO);
        if (partyRelationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyRelationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyRelationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PartyRelationTypeDTO result = partyRelationTypeService.save(partyRelationTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyRelationTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /party-relation-types/:id} : Partial updates given fields of an existing partyRelationType, field will ignore if it is null
     *
     * @param id the id of the partyRelationTypeDTO to save.
     * @param partyRelationTypeDTO the partyRelationTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated partyRelationTypeDTO,
     * or with status {@code 400 (Bad Request)} if the partyRelationTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the partyRelationTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the partyRelationTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/party-relation-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PartyRelationTypeDTO> partialUpdatePartyRelationType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PartyRelationTypeDTO partyRelationTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PartyRelationType partially : {}, {}", id, partyRelationTypeDTO);
        if (partyRelationTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, partyRelationTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!partyRelationTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PartyRelationTypeDTO> result = partyRelationTypeService.partialUpdate(partyRelationTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, partyRelationTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /party-relation-types} : get all the partyRelationTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of partyRelationTypes in body.
     */
    @GetMapping("/party-relation-types")
    public ResponseEntity<List<PartyRelationTypeDTO>> getAllPartyRelationTypes(PartyRelationTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PartyRelationTypes by criteria: {}", criteria);
        Page<PartyRelationTypeDTO> page = partyRelationTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /party-relation-types/count} : count all the partyRelationTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/party-relation-types/count")
    public ResponseEntity<Long> countPartyRelationTypes(PartyRelationTypeCriteria criteria) {
        log.debug("REST request to count PartyRelationTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(partyRelationTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /party-relation-types/:id} : get the "id" partyRelationType.
     *
     * @param id the id of the partyRelationTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the partyRelationTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/party-relation-types/{id}")
    public ResponseEntity<PartyRelationTypeDTO> getPartyRelationType(@PathVariable Long id) {
        log.debug("REST request to get PartyRelationType : {}", id);
        Optional<PartyRelationTypeDTO> partyRelationTypeDTO = partyRelationTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(partyRelationTypeDTO);
    }

    /**
     * {@code DELETE  /party-relation-types/:id} : delete the "id" partyRelationType.
     *
     * @param id the id of the partyRelationTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/party-relation-types/{id}")
    public ResponseEntity<Void> deletePartyRelationType(@PathVariable Long id) {
        log.debug("REST request to delete PartyRelationType : {}", id);
        partyRelationTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/party-relation-types?query=:query} : search for the partyRelationType corresponding
     * to the query.
     *
     * @param query the query of the partyRelationType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/party-relation-types")
    public ResponseEntity<List<PartyRelationTypeDTO>> searchPartyRelationTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PartyRelationTypes for query {}", query);
        Page<PartyRelationTypeDTO> page = partyRelationTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
