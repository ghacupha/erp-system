package io.github.erp.erp.resources.gdi;

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
import io.github.erp.repository.RemittanceFlagRepository;
import io.github.erp.service.RemittanceFlagQueryService;
import io.github.erp.service.RemittanceFlagService;
import io.github.erp.service.criteria.RemittanceFlagCriteria;
import io.github.erp.service.dto.RemittanceFlagDTO;
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
 * REST controller for managing {@link io.github.erp.domain.RemittanceFlag}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class RemittanceFlagResourceProd {

    private final Logger log = LoggerFactory.getLogger(RemittanceFlagResourceProd.class);

    private static final String ENTITY_NAME = "remittanceFlag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RemittanceFlagService remittanceFlagService;

    private final RemittanceFlagRepository remittanceFlagRepository;

    private final RemittanceFlagQueryService remittanceFlagQueryService;

    public RemittanceFlagResourceProd(
        RemittanceFlagService remittanceFlagService,
        RemittanceFlagRepository remittanceFlagRepository,
        RemittanceFlagQueryService remittanceFlagQueryService
    ) {
        this.remittanceFlagService = remittanceFlagService;
        this.remittanceFlagRepository = remittanceFlagRepository;
        this.remittanceFlagQueryService = remittanceFlagQueryService;
    }

    /**
     * {@code POST  /remittance-flags} : Create a new remittanceFlag.
     *
     * @param remittanceFlagDTO the remittanceFlagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new remittanceFlagDTO, or with status {@code 400 (Bad Request)} if the remittanceFlag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/remittance-flags")
    public ResponseEntity<RemittanceFlagDTO> createRemittanceFlag(@Valid @RequestBody RemittanceFlagDTO remittanceFlagDTO)
        throws URISyntaxException {
        log.debug("REST request to save RemittanceFlag : {}", remittanceFlagDTO);
        if (remittanceFlagDTO.getId() != null) {
            throw new BadRequestAlertException("A new remittanceFlag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RemittanceFlagDTO result = remittanceFlagService.save(remittanceFlagDTO);
        return ResponseEntity
            .created(new URI("/api/remittance-flags/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /remittance-flags/:id} : Updates an existing remittanceFlag.
     *
     * @param id the id of the remittanceFlagDTO to save.
     * @param remittanceFlagDTO the remittanceFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated remittanceFlagDTO,
     * or with status {@code 400 (Bad Request)} if the remittanceFlagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the remittanceFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/remittance-flags/{id}")
    public ResponseEntity<RemittanceFlagDTO> updateRemittanceFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody RemittanceFlagDTO remittanceFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to update RemittanceFlag : {}, {}", id, remittanceFlagDTO);
        if (remittanceFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, remittanceFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!remittanceFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RemittanceFlagDTO result = remittanceFlagService.save(remittanceFlagDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, remittanceFlagDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /remittance-flags/:id} : Partial updates given fields of an existing remittanceFlag, field will ignore if it is null
     *
     * @param id the id of the remittanceFlagDTO to save.
     * @param remittanceFlagDTO the remittanceFlagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated remittanceFlagDTO,
     * or with status {@code 400 (Bad Request)} if the remittanceFlagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the remittanceFlagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the remittanceFlagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/remittance-flags/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RemittanceFlagDTO> partialUpdateRemittanceFlag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody RemittanceFlagDTO remittanceFlagDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update RemittanceFlag partially : {}, {}", id, remittanceFlagDTO);
        if (remittanceFlagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, remittanceFlagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!remittanceFlagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RemittanceFlagDTO> result = remittanceFlagService.partialUpdate(remittanceFlagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, remittanceFlagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /remittance-flags} : get all the remittanceFlags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of remittanceFlags in body.
     */
    @GetMapping("/remittance-flags")
    public ResponseEntity<List<RemittanceFlagDTO>> getAllRemittanceFlags(RemittanceFlagCriteria criteria, Pageable pageable) {
        log.debug("REST request to get RemittanceFlags by criteria: {}", criteria);
        Page<RemittanceFlagDTO> page = remittanceFlagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /remittance-flags/count} : count all the remittanceFlags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/remittance-flags/count")
    public ResponseEntity<Long> countRemittanceFlags(RemittanceFlagCriteria criteria) {
        log.debug("REST request to count RemittanceFlags by criteria: {}", criteria);
        return ResponseEntity.ok().body(remittanceFlagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /remittance-flags/:id} : get the "id" remittanceFlag.
     *
     * @param id the id of the remittanceFlagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the remittanceFlagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/remittance-flags/{id}")
    public ResponseEntity<RemittanceFlagDTO> getRemittanceFlag(@PathVariable Long id) {
        log.debug("REST request to get RemittanceFlag : {}", id);
        Optional<RemittanceFlagDTO> remittanceFlagDTO = remittanceFlagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(remittanceFlagDTO);
    }

    /**
     * {@code DELETE  /remittance-flags/:id} : delete the "id" remittanceFlag.
     *
     * @param id the id of the remittanceFlagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/remittance-flags/{id}")
    public ResponseEntity<Void> deleteRemittanceFlag(@PathVariable Long id) {
        log.debug("REST request to delete RemittanceFlag : {}", id);
        remittanceFlagService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/remittance-flags?query=:query} : search for the remittanceFlag corresponding
     * to the query.
     *
     * @param query the query of the remittanceFlag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/remittance-flags")
    public ResponseEntity<List<RemittanceFlagDTO>> searchRemittanceFlags(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of RemittanceFlags for query {}", query);
        Page<RemittanceFlagDTO> page = remittanceFlagService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
