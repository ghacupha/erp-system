package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import io.github.erp.repository.CreditCardFacilityRepository;
import io.github.erp.service.CreditCardFacilityQueryService;
import io.github.erp.service.CreditCardFacilityService;
import io.github.erp.service.criteria.CreditCardFacilityCriteria;
import io.github.erp.service.dto.CreditCardFacilityDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link io.github.erp.domain.CreditCardFacility}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class CreditCardFacilityResourceProd {

    private final Logger log = LoggerFactory.getLogger(CreditCardFacilityResourceProd.class);

    private static final String ENTITY_NAME = "gdiDataCreditCardFacility";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CreditCardFacilityService creditCardFacilityService;

    private final CreditCardFacilityRepository creditCardFacilityRepository;

    private final CreditCardFacilityQueryService creditCardFacilityQueryService;

    public CreditCardFacilityResourceProd(
        CreditCardFacilityService creditCardFacilityService,
        CreditCardFacilityRepository creditCardFacilityRepository,
        CreditCardFacilityQueryService creditCardFacilityQueryService
    ) {
        this.creditCardFacilityService = creditCardFacilityService;
        this.creditCardFacilityRepository = creditCardFacilityRepository;
        this.creditCardFacilityQueryService = creditCardFacilityQueryService;
    }

    /**
     * {@code POST  /credit-card-facilities} : Create a new creditCardFacility.
     *
     * @param creditCardFacilityDTO the creditCardFacilityDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new creditCardFacilityDTO, or with status {@code 400 (Bad Request)} if the creditCardFacility has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/credit-card-facilities")
    public ResponseEntity<CreditCardFacilityDTO> createCreditCardFacility(@Valid @RequestBody CreditCardFacilityDTO creditCardFacilityDTO)
        throws URISyntaxException {
        log.debug("REST request to save CreditCardFacility : {}", creditCardFacilityDTO);
        if (creditCardFacilityDTO.getId() != null) {
            throw new BadRequestAlertException("A new creditCardFacility cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CreditCardFacilityDTO result = creditCardFacilityService.save(creditCardFacilityDTO);
        return ResponseEntity
            .created(new URI("/api/credit-card-facilities/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /credit-card-facilities/:id} : Updates an existing creditCardFacility.
     *
     * @param id the id of the creditCardFacilityDTO to save.
     * @param creditCardFacilityDTO the creditCardFacilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditCardFacilityDTO,
     * or with status {@code 400 (Bad Request)} if the creditCardFacilityDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the creditCardFacilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/credit-card-facilities/{id}")
    public ResponseEntity<CreditCardFacilityDTO> updateCreditCardFacility(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CreditCardFacilityDTO creditCardFacilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CreditCardFacility : {}, {}", id, creditCardFacilityDTO);
        if (creditCardFacilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditCardFacilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditCardFacilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CreditCardFacilityDTO result = creditCardFacilityService.save(creditCardFacilityDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, creditCardFacilityDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /credit-card-facilities/:id} : Partial updates given fields of an existing creditCardFacility, field will ignore if it is null
     *
     * @param id the id of the creditCardFacilityDTO to save.
     * @param creditCardFacilityDTO the creditCardFacilityDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated creditCardFacilityDTO,
     * or with status {@code 400 (Bad Request)} if the creditCardFacilityDTO is not valid,
     * or with status {@code 404 (Not Found)} if the creditCardFacilityDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the creditCardFacilityDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/credit-card-facilities/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CreditCardFacilityDTO> partialUpdateCreditCardFacility(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CreditCardFacilityDTO creditCardFacilityDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CreditCardFacility partially : {}, {}", id, creditCardFacilityDTO);
        if (creditCardFacilityDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, creditCardFacilityDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!creditCardFacilityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CreditCardFacilityDTO> result = creditCardFacilityService.partialUpdate(creditCardFacilityDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, creditCardFacilityDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /credit-card-facilities} : get all the creditCardFacilities.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of creditCardFacilities in body.
     */
    @GetMapping("/credit-card-facilities")
    public ResponseEntity<List<CreditCardFacilityDTO>> getAllCreditCardFacilities(CreditCardFacilityCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CreditCardFacilities by criteria: {}", criteria);
        Page<CreditCardFacilityDTO> page = creditCardFacilityQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /credit-card-facilities/count} : count all the creditCardFacilities.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/credit-card-facilities/count")
    public ResponseEntity<Long> countCreditCardFacilities(CreditCardFacilityCriteria criteria) {
        log.debug("REST request to count CreditCardFacilities by criteria: {}", criteria);
        return ResponseEntity.ok().body(creditCardFacilityQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /credit-card-facilities/:id} : get the "id" creditCardFacility.
     *
     * @param id the id of the creditCardFacilityDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the creditCardFacilityDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/credit-card-facilities/{id}")
    public ResponseEntity<CreditCardFacilityDTO> getCreditCardFacility(@PathVariable Long id) {
        log.debug("REST request to get CreditCardFacility : {}", id);
        Optional<CreditCardFacilityDTO> creditCardFacilityDTO = creditCardFacilityService.findOne(id);
        return ResponseUtil.wrapOrNotFound(creditCardFacilityDTO);
    }

    /**
     * {@code DELETE  /credit-card-facilities/:id} : delete the "id" creditCardFacility.
     *
     * @param id the id of the creditCardFacilityDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/credit-card-facilities/{id}")
    public ResponseEntity<Void> deleteCreditCardFacility(@PathVariable Long id) {
        log.debug("REST request to delete CreditCardFacility : {}", id);
        creditCardFacilityService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/credit-card-facilities?query=:query} : search for the creditCardFacility corresponding
     * to the query.
     *
     * @param query the query of the creditCardFacility search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/credit-card-facilities")
    public ResponseEntity<List<CreditCardFacilityDTO>> searchCreditCardFacilities(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CreditCardFacilities for query {}", query);
        Page<CreditCardFacilityDTO> page = creditCardFacilityService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
