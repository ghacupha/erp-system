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
import io.github.erp.repository.CrbNatureOfInformationRepository;
import io.github.erp.service.CrbNatureOfInformationQueryService;
import io.github.erp.service.CrbNatureOfInformationService;
import io.github.erp.service.criteria.CrbNatureOfInformationCriteria;
import io.github.erp.service.dto.CrbNatureOfInformationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CrbNatureOfInformation}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CrbNatureOfInformationResourceProd {

    private final Logger log = LoggerFactory.getLogger(CrbNatureOfInformationResourceProd.class);

    private static final String ENTITY_NAME = "crbNatureOfInformation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CrbNatureOfInformationService crbNatureOfInformationService;

    private final CrbNatureOfInformationRepository crbNatureOfInformationRepository;

    private final CrbNatureOfInformationQueryService crbNatureOfInformationQueryService;

    public CrbNatureOfInformationResourceProd(
        CrbNatureOfInformationService crbNatureOfInformationService,
        CrbNatureOfInformationRepository crbNatureOfInformationRepository,
        CrbNatureOfInformationQueryService crbNatureOfInformationQueryService
    ) {
        this.crbNatureOfInformationService = crbNatureOfInformationService;
        this.crbNatureOfInformationRepository = crbNatureOfInformationRepository;
        this.crbNatureOfInformationQueryService = crbNatureOfInformationQueryService;
    }

    /**
     * {@code POST  /crb-nature-of-informations} : Create a new crbNatureOfInformation.
     *
     * @param crbNatureOfInformationDTO the crbNatureOfInformationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new crbNatureOfInformationDTO, or with status {@code 400 (Bad Request)} if the crbNatureOfInformation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/crb-nature-of-informations")
    public ResponseEntity<CrbNatureOfInformationDTO> createCrbNatureOfInformation(
        @Valid @RequestBody CrbNatureOfInformationDTO crbNatureOfInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CrbNatureOfInformation : {}", crbNatureOfInformationDTO);
        if (crbNatureOfInformationDTO.getId() != null) {
            throw new BadRequestAlertException("A new crbNatureOfInformation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CrbNatureOfInformationDTO result = crbNatureOfInformationService.save(crbNatureOfInformationDTO);
        return ResponseEntity
            .created(new URI("/api/crb-nature-of-informations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /crb-nature-of-informations/:id} : Updates an existing crbNatureOfInformation.
     *
     * @param id the id of the crbNatureOfInformationDTO to save.
     * @param crbNatureOfInformationDTO the crbNatureOfInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbNatureOfInformationDTO,
     * or with status {@code 400 (Bad Request)} if the crbNatureOfInformationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the crbNatureOfInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/crb-nature-of-informations/{id}")
    public ResponseEntity<CrbNatureOfInformationDTO> updateCrbNatureOfInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CrbNatureOfInformationDTO crbNatureOfInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CrbNatureOfInformation : {}, {}", id, crbNatureOfInformationDTO);
        if (crbNatureOfInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbNatureOfInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbNatureOfInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CrbNatureOfInformationDTO result = crbNatureOfInformationService.save(crbNatureOfInformationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbNatureOfInformationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /crb-nature-of-informations/:id} : Partial updates given fields of an existing crbNatureOfInformation, field will ignore if it is null
     *
     * @param id the id of the crbNatureOfInformationDTO to save.
     * @param crbNatureOfInformationDTO the crbNatureOfInformationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated crbNatureOfInformationDTO,
     * or with status {@code 400 (Bad Request)} if the crbNatureOfInformationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the crbNatureOfInformationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the crbNatureOfInformationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/crb-nature-of-informations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CrbNatureOfInformationDTO> partialUpdateCrbNatureOfInformation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CrbNatureOfInformationDTO crbNatureOfInformationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CrbNatureOfInformation partially : {}, {}", id, crbNatureOfInformationDTO);
        if (crbNatureOfInformationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, crbNatureOfInformationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!crbNatureOfInformationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CrbNatureOfInformationDTO> result = crbNatureOfInformationService.partialUpdate(crbNatureOfInformationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, crbNatureOfInformationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /crb-nature-of-informations} : get all the crbNatureOfInformations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crbNatureOfInformations in body.
     */
    @GetMapping("/crb-nature-of-informations")
    public ResponseEntity<List<CrbNatureOfInformationDTO>> getAllCrbNatureOfInformations(
        CrbNatureOfInformationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CrbNatureOfInformations by criteria: {}", criteria);
        Page<CrbNatureOfInformationDTO> page = crbNatureOfInformationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crb-nature-of-informations/count} : count all the crbNatureOfInformations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/crb-nature-of-informations/count")
    public ResponseEntity<Long> countCrbNatureOfInformations(CrbNatureOfInformationCriteria criteria) {
        log.debug("REST request to count CrbNatureOfInformations by criteria: {}", criteria);
        return ResponseEntity.ok().body(crbNatureOfInformationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /crb-nature-of-informations/:id} : get the "id" crbNatureOfInformation.
     *
     * @param id the id of the crbNatureOfInformationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crbNatureOfInformationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crb-nature-of-informations/{id}")
    public ResponseEntity<CrbNatureOfInformationDTO> getCrbNatureOfInformation(@PathVariable Long id) {
        log.debug("REST request to get CrbNatureOfInformation : {}", id);
        Optional<CrbNatureOfInformationDTO> crbNatureOfInformationDTO = crbNatureOfInformationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crbNatureOfInformationDTO);
    }

    /**
     * {@code DELETE  /crb-nature-of-informations/:id} : delete the "id" crbNatureOfInformation.
     *
     * @param id the id of the crbNatureOfInformationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/crb-nature-of-informations/{id}")
    public ResponseEntity<Void> deleteCrbNatureOfInformation(@PathVariable Long id) {
        log.debug("REST request to delete CrbNatureOfInformation : {}", id);
        crbNatureOfInformationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/crb-nature-of-informations?query=:query} : search for the crbNatureOfInformation corresponding
     * to the query.
     *
     * @param query the query of the crbNatureOfInformation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/crb-nature-of-informations")
    public ResponseEntity<List<CrbNatureOfInformationDTO>> searchCrbNatureOfInformations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CrbNatureOfInformations for query {}", query);
        Page<CrbNatureOfInformationDTO> page = crbNatureOfInformationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
