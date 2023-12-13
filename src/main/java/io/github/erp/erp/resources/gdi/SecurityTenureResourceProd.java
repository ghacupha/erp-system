package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark IX No 2 (Iddo Series) Server ver 1.6.4
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.repository.SecurityTenureRepository;
import io.github.erp.service.SecurityTenureQueryService;
import io.github.erp.service.SecurityTenureService;
import io.github.erp.service.criteria.SecurityTenureCriteria;
import io.github.erp.service.dto.SecurityTenureDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SecurityTenure}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class SecurityTenureResourceProd {

    private final Logger log = LoggerFactory.getLogger(SecurityTenureResourceProd.class);

    private static final String ENTITY_NAME = "securityTenure";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SecurityTenureService securityTenureService;

    private final SecurityTenureRepository securityTenureRepository;

    private final SecurityTenureQueryService securityTenureQueryService;

    public SecurityTenureResourceProd(
        SecurityTenureService securityTenureService,
        SecurityTenureRepository securityTenureRepository,
        SecurityTenureQueryService securityTenureQueryService
    ) {
        this.securityTenureService = securityTenureService;
        this.securityTenureRepository = securityTenureRepository;
        this.securityTenureQueryService = securityTenureQueryService;
    }

    /**
     * {@code POST  /security-tenures} : Create a new securityTenure.
     *
     * @param securityTenureDTO the securityTenureDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new securityTenureDTO, or with status {@code 400 (Bad Request)} if the securityTenure has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/security-tenures")
    public ResponseEntity<SecurityTenureDTO> createSecurityTenure(@Valid @RequestBody SecurityTenureDTO securityTenureDTO)
        throws URISyntaxException {
        log.debug("REST request to save SecurityTenure : {}", securityTenureDTO);
        if (securityTenureDTO.getId() != null) {
            throw new BadRequestAlertException("A new securityTenure cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SecurityTenureDTO result = securityTenureService.save(securityTenureDTO);
        return ResponseEntity
            .created(new URI("/api/security-tenures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /security-tenures/:id} : Updates an existing securityTenure.
     *
     * @param id the id of the securityTenureDTO to save.
     * @param securityTenureDTO the securityTenureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityTenureDTO,
     * or with status {@code 400 (Bad Request)} if the securityTenureDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the securityTenureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/security-tenures/{id}")
    public ResponseEntity<SecurityTenureDTO> updateSecurityTenure(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SecurityTenureDTO securityTenureDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SecurityTenure : {}, {}", id, securityTenureDTO);
        if (securityTenureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityTenureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityTenureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SecurityTenureDTO result = securityTenureService.save(securityTenureDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securityTenureDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /security-tenures/:id} : Partial updates given fields of an existing securityTenure, field will ignore if it is null
     *
     * @param id the id of the securityTenureDTO to save.
     * @param securityTenureDTO the securityTenureDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated securityTenureDTO,
     * or with status {@code 400 (Bad Request)} if the securityTenureDTO is not valid,
     * or with status {@code 404 (Not Found)} if the securityTenureDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the securityTenureDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/security-tenures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SecurityTenureDTO> partialUpdateSecurityTenure(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SecurityTenureDTO securityTenureDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SecurityTenure partially : {}, {}", id, securityTenureDTO);
        if (securityTenureDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, securityTenureDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!securityTenureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SecurityTenureDTO> result = securityTenureService.partialUpdate(securityTenureDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, securityTenureDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /security-tenures} : get all the securityTenures.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of securityTenures in body.
     */
    @GetMapping("/security-tenures")
    public ResponseEntity<List<SecurityTenureDTO>> getAllSecurityTenures(SecurityTenureCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SecurityTenures by criteria: {}", criteria);
        Page<SecurityTenureDTO> page = securityTenureQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /security-tenures/count} : count all the securityTenures.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/security-tenures/count")
    public ResponseEntity<Long> countSecurityTenures(SecurityTenureCriteria criteria) {
        log.debug("REST request to count SecurityTenures by criteria: {}", criteria);
        return ResponseEntity.ok().body(securityTenureQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /security-tenures/:id} : get the "id" securityTenure.
     *
     * @param id the id of the securityTenureDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the securityTenureDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/security-tenures/{id}")
    public ResponseEntity<SecurityTenureDTO> getSecurityTenure(@PathVariable Long id) {
        log.debug("REST request to get SecurityTenure : {}", id);
        Optional<SecurityTenureDTO> securityTenureDTO = securityTenureService.findOne(id);
        return ResponseUtil.wrapOrNotFound(securityTenureDTO);
    }

    /**
     * {@code DELETE  /security-tenures/:id} : delete the "id" securityTenure.
     *
     * @param id the id of the securityTenureDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/security-tenures/{id}")
    public ResponseEntity<Void> deleteSecurityTenure(@PathVariable Long id) {
        log.debug("REST request to delete SecurityTenure : {}", id);
        securityTenureService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/security-tenures?query=:query} : search for the securityTenure corresponding
     * to the query.
     *
     * @param query the query of the securityTenure search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/security-tenures")
    public ResponseEntity<List<SecurityTenureDTO>> searchSecurityTenures(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SecurityTenures for query {}", query);
        Page<SecurityTenureDTO> page = securityTenureService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
