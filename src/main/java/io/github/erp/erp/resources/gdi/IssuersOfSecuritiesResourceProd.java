package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VI No 3 (Phoebe Series) Server ver 1.5.4
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
import io.github.erp.repository.IssuersOfSecuritiesRepository;
import io.github.erp.service.IssuersOfSecuritiesQueryService;
import io.github.erp.service.IssuersOfSecuritiesService;
import io.github.erp.service.criteria.IssuersOfSecuritiesCriteria;
import io.github.erp.service.dto.IssuersOfSecuritiesDTO;
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
 * REST controller for managing {@link io.github.erp.domain.IssuersOfSecurities}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class IssuersOfSecuritiesResourceProd {

    private final Logger log = LoggerFactory.getLogger(IssuersOfSecuritiesResourceProd.class);

    private static final String ENTITY_NAME = "issuersOfSecurities";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IssuersOfSecuritiesService issuersOfSecuritiesService;

    private final IssuersOfSecuritiesRepository issuersOfSecuritiesRepository;

    private final IssuersOfSecuritiesQueryService issuersOfSecuritiesQueryService;

    public IssuersOfSecuritiesResourceProd(
        IssuersOfSecuritiesService issuersOfSecuritiesService,
        IssuersOfSecuritiesRepository issuersOfSecuritiesRepository,
        IssuersOfSecuritiesQueryService issuersOfSecuritiesQueryService
    ) {
        this.issuersOfSecuritiesService = issuersOfSecuritiesService;
        this.issuersOfSecuritiesRepository = issuersOfSecuritiesRepository;
        this.issuersOfSecuritiesQueryService = issuersOfSecuritiesQueryService;
    }

    /**
     * {@code POST  /issuers-of-securities} : Create a new issuersOfSecurities.
     *
     * @param issuersOfSecuritiesDTO the issuersOfSecuritiesDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new issuersOfSecuritiesDTO, or with status {@code 400 (Bad Request)} if the issuersOfSecurities has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/issuers-of-securities")
    public ResponseEntity<IssuersOfSecuritiesDTO> createIssuersOfSecurities(
        @Valid @RequestBody IssuersOfSecuritiesDTO issuersOfSecuritiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to save IssuersOfSecurities : {}", issuersOfSecuritiesDTO);
        if (issuersOfSecuritiesDTO.getId() != null) {
            throw new BadRequestAlertException("A new issuersOfSecurities cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IssuersOfSecuritiesDTO result = issuersOfSecuritiesService.save(issuersOfSecuritiesDTO);
        return ResponseEntity
            .created(new URI("/api/issuers-of-securities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /issuers-of-securities/:id} : Updates an existing issuersOfSecurities.
     *
     * @param id the id of the issuersOfSecuritiesDTO to save.
     * @param issuersOfSecuritiesDTO the issuersOfSecuritiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated issuersOfSecuritiesDTO,
     * or with status {@code 400 (Bad Request)} if the issuersOfSecuritiesDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the issuersOfSecuritiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/issuers-of-securities/{id}")
    public ResponseEntity<IssuersOfSecuritiesDTO> updateIssuersOfSecurities(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody IssuersOfSecuritiesDTO issuersOfSecuritiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to update IssuersOfSecurities : {}, {}", id, issuersOfSecuritiesDTO);
        if (issuersOfSecuritiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, issuersOfSecuritiesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!issuersOfSecuritiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IssuersOfSecuritiesDTO result = issuersOfSecuritiesService.save(issuersOfSecuritiesDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, issuersOfSecuritiesDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /issuers-of-securities/:id} : Partial updates given fields of an existing issuersOfSecurities, field will ignore if it is null
     *
     * @param id the id of the issuersOfSecuritiesDTO to save.
     * @param issuersOfSecuritiesDTO the issuersOfSecuritiesDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated issuersOfSecuritiesDTO,
     * or with status {@code 400 (Bad Request)} if the issuersOfSecuritiesDTO is not valid,
     * or with status {@code 404 (Not Found)} if the issuersOfSecuritiesDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the issuersOfSecuritiesDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/issuers-of-securities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IssuersOfSecuritiesDTO> partialUpdateIssuersOfSecurities(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody IssuersOfSecuritiesDTO issuersOfSecuritiesDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update IssuersOfSecurities partially : {}, {}", id, issuersOfSecuritiesDTO);
        if (issuersOfSecuritiesDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, issuersOfSecuritiesDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!issuersOfSecuritiesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IssuersOfSecuritiesDTO> result = issuersOfSecuritiesService.partialUpdate(issuersOfSecuritiesDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, issuersOfSecuritiesDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /issuers-of-securities} : get all the issuersOfSecurities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of issuersOfSecurities in body.
     */
    @GetMapping("/issuers-of-securities")
    public ResponseEntity<List<IssuersOfSecuritiesDTO>> getAllIssuersOfSecurities(IssuersOfSecuritiesCriteria criteria, Pageable pageable) {
        log.debug("REST request to get IssuersOfSecurities by criteria: {}", criteria);
        Page<IssuersOfSecuritiesDTO> page = issuersOfSecuritiesQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /issuers-of-securities/count} : count all the issuersOfSecurities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/issuers-of-securities/count")
    public ResponseEntity<Long> countIssuersOfSecurities(IssuersOfSecuritiesCriteria criteria) {
        log.debug("REST request to count IssuersOfSecurities by criteria: {}", criteria);
        return ResponseEntity.ok().body(issuersOfSecuritiesQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /issuers-of-securities/:id} : get the "id" issuersOfSecurities.
     *
     * @param id the id of the issuersOfSecuritiesDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the issuersOfSecuritiesDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/issuers-of-securities/{id}")
    public ResponseEntity<IssuersOfSecuritiesDTO> getIssuersOfSecurities(@PathVariable Long id) {
        log.debug("REST request to get IssuersOfSecurities : {}", id);
        Optional<IssuersOfSecuritiesDTO> issuersOfSecuritiesDTO = issuersOfSecuritiesService.findOne(id);
        return ResponseUtil.wrapOrNotFound(issuersOfSecuritiesDTO);
    }

    /**
     * {@code DELETE  /issuers-of-securities/:id} : delete the "id" issuersOfSecurities.
     *
     * @param id the id of the issuersOfSecuritiesDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/issuers-of-securities/{id}")
    public ResponseEntity<Void> deleteIssuersOfSecurities(@PathVariable Long id) {
        log.debug("REST request to delete IssuersOfSecurities : {}", id);
        issuersOfSecuritiesService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/issuers-of-securities?query=:query} : search for the issuersOfSecurities corresponding
     * to the query.
     *
     * @param query the query of the issuersOfSecurities search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/issuers-of-securities")
    public ResponseEntity<List<IssuersOfSecuritiesDTO>> searchIssuersOfSecurities(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of IssuersOfSecurities for query {}", query);
        Page<IssuersOfSecuritiesDTO> page = issuersOfSecuritiesService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
