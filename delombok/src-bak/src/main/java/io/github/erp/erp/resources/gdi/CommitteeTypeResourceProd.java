package io.github.erp.erp.resources.gdi;

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
import io.github.erp.repository.CommitteeTypeRepository;
import io.github.erp.service.CommitteeTypeQueryService;
import io.github.erp.service.CommitteeTypeService;
import io.github.erp.service.criteria.CommitteeTypeCriteria;
import io.github.erp.service.dto.CommitteeTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CommitteeType}.
 */
@RestController("CommitteeTypeResourceProd")
@RequestMapping("/api/granular-data")
public class CommitteeTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(CommitteeTypeResourceProd.class);

    private static final String ENTITY_NAME = "committeeType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommitteeTypeService committeeTypeService;

    private final CommitteeTypeRepository committeeTypeRepository;

    private final CommitteeTypeQueryService committeeTypeQueryService;

    public CommitteeTypeResourceProd(
        CommitteeTypeService committeeTypeService,
        CommitteeTypeRepository committeeTypeRepository,
        CommitteeTypeQueryService committeeTypeQueryService
    ) {
        this.committeeTypeService = committeeTypeService;
        this.committeeTypeRepository = committeeTypeRepository;
        this.committeeTypeQueryService = committeeTypeQueryService;
    }

    /**
     * {@code POST  /committee-types} : Create a new committeeType.
     *
     * @param committeeTypeDTO the committeeTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new committeeTypeDTO, or with status {@code 400 (Bad Request)} if the committeeType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/committee-types")
    public ResponseEntity<CommitteeTypeDTO> createCommitteeType(@Valid @RequestBody CommitteeTypeDTO committeeTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CommitteeType : {}", committeeTypeDTO);
        if (committeeTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new committeeType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommitteeTypeDTO result = committeeTypeService.save(committeeTypeDTO);
        return ResponseEntity
            .created(new URI("/api/committee-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /committee-types/:id} : Updates an existing committeeType.
     *
     * @param id the id of the committeeTypeDTO to save.
     * @param committeeTypeDTO the committeeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated committeeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the committeeTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the committeeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/committee-types/{id}")
    public ResponseEntity<CommitteeTypeDTO> updateCommitteeType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CommitteeTypeDTO committeeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CommitteeType : {}, {}", id, committeeTypeDTO);
        if (committeeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, committeeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!committeeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CommitteeTypeDTO result = committeeTypeService.save(committeeTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, committeeTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /committee-types/:id} : Partial updates given fields of an existing committeeType, field will ignore if it is null
     *
     * @param id the id of the committeeTypeDTO to save.
     * @param committeeTypeDTO the committeeTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated committeeTypeDTO,
     * or with status {@code 400 (Bad Request)} if the committeeTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the committeeTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the committeeTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/committee-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CommitteeTypeDTO> partialUpdateCommitteeType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CommitteeTypeDTO committeeTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CommitteeType partially : {}, {}", id, committeeTypeDTO);
        if (committeeTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, committeeTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!committeeTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CommitteeTypeDTO> result = committeeTypeService.partialUpdate(committeeTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, committeeTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /committee-types} : get all the committeeTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of committeeTypes in body.
     */
    @GetMapping("/committee-types")
    public ResponseEntity<List<CommitteeTypeDTO>> getAllCommitteeTypes(CommitteeTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CommitteeTypes by criteria: {}", criteria);
        Page<CommitteeTypeDTO> page = committeeTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /committee-types/count} : count all the committeeTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/committee-types/count")
    public ResponseEntity<Long> countCommitteeTypes(CommitteeTypeCriteria criteria) {
        log.debug("REST request to count CommitteeTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(committeeTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /committee-types/:id} : get the "id" committeeType.
     *
     * @param id the id of the committeeTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the committeeTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/committee-types/{id}")
    public ResponseEntity<CommitteeTypeDTO> getCommitteeType(@PathVariable Long id) {
        log.debug("REST request to get CommitteeType : {}", id);
        Optional<CommitteeTypeDTO> committeeTypeDTO = committeeTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(committeeTypeDTO);
    }

    /**
     * {@code DELETE  /committee-types/:id} : delete the "id" committeeType.
     *
     * @param id the id of the committeeTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/committee-types/{id}")
    public ResponseEntity<Void> deleteCommitteeType(@PathVariable Long id) {
        log.debug("REST request to delete CommitteeType : {}", id);
        committeeTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/committee-types?query=:query} : search for the committeeType corresponding
     * to the query.
     *
     * @param query the query of the committeeType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/committee-types")
    public ResponseEntity<List<CommitteeTypeDTO>> searchCommitteeTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CommitteeTypes for query {}", query);
        Page<CommitteeTypeDTO> page = committeeTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
