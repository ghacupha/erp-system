package io.github.erp.erp.resources.prepayments;

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
import io.github.erp.internal.service.prepayments.InternalAmortizationPeriodService;
import io.github.erp.repository.AmortizationPeriodRepository;
import io.github.erp.service.AmortizationPeriodQueryService;
import io.github.erp.service.criteria.AmortizationPeriodCriteria;
import io.github.erp.service.dto.AmortizationPeriodDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AmortizationPeriod}.
 */
@RestController
@RequestMapping("/api/prepayments")
public class AmortizationPeriodResourceProd {

    private final Logger log = LoggerFactory.getLogger(AmortizationPeriodResourceProd.class);

    private static final String ENTITY_NAME = "amortizationPeriod";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternalAmortizationPeriodService amortizationPeriodService;

    private final AmortizationPeriodRepository amortizationPeriodRepository;

    private final AmortizationPeriodQueryService amortizationPeriodQueryService;

    public AmortizationPeriodResourceProd(
        InternalAmortizationPeriodService amortizationPeriodService,
        AmortizationPeriodRepository amortizationPeriodRepository,
        AmortizationPeriodQueryService amortizationPeriodQueryService
    ) {
        this.amortizationPeriodService = amortizationPeriodService;
        this.amortizationPeriodRepository = amortizationPeriodRepository;
        this.amortizationPeriodQueryService = amortizationPeriodQueryService;
    }

    /**
     * {@code POST  /amortization-periods} : Create a new amortizationPeriod.
     *
     * @param amortizationPeriodDTO the amortizationPeriodDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new amortizationPeriodDTO, or with status {@code 400 (Bad Request)} if the amortizationPeriod has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/amortization-periods")
    public ResponseEntity<AmortizationPeriodDTO> createAmortizationPeriod(@Valid @RequestBody AmortizationPeriodDTO amortizationPeriodDTO)
        throws URISyntaxException {
        log.debug("REST request to save AmortizationPeriod : {}", amortizationPeriodDTO);
        if (amortizationPeriodDTO.getId() != null) {
            throw new BadRequestAlertException("A new amortizationPeriod cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AmortizationPeriodDTO result = amortizationPeriodService.save(amortizationPeriodDTO);
        return ResponseEntity
            .created(new URI("/api/amortization-periods/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /amortization-periods/:id} : Updates an existing amortizationPeriod.
     *
     * @param id the id of the amortizationPeriodDTO to save.
     * @param amortizationPeriodDTO the amortizationPeriodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amortizationPeriodDTO,
     * or with status {@code 400 (Bad Request)} if the amortizationPeriodDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the amortizationPeriodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/amortization-periods/{id}")
    public ResponseEntity<AmortizationPeriodDTO> updateAmortizationPeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AmortizationPeriodDTO amortizationPeriodDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AmortizationPeriod : {}, {}", id, amortizationPeriodDTO);
        if (amortizationPeriodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amortizationPeriodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amortizationPeriodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AmortizationPeriodDTO result = amortizationPeriodService.save(amortizationPeriodDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amortizationPeriodDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /amortization-periods/:id} : Partial updates given fields of an existing amortizationPeriod, field will ignore if it is null
     *
     * @param id the id of the amortizationPeriodDTO to save.
     * @param amortizationPeriodDTO the amortizationPeriodDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated amortizationPeriodDTO,
     * or with status {@code 400 (Bad Request)} if the amortizationPeriodDTO is not valid,
     * or with status {@code 404 (Not Found)} if the amortizationPeriodDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the amortizationPeriodDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/amortization-periods/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AmortizationPeriodDTO> partialUpdateAmortizationPeriod(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AmortizationPeriodDTO amortizationPeriodDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AmortizationPeriod partially : {}, {}", id, amortizationPeriodDTO);
        if (amortizationPeriodDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, amortizationPeriodDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!amortizationPeriodRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AmortizationPeriodDTO> result = amortizationPeriodService.partialUpdate(amortizationPeriodDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, amortizationPeriodDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /amortization-periods} : get all the amortizationPeriods.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of amortizationPeriods in body.
     */
    @GetMapping("/amortization-periods")
    public ResponseEntity<List<AmortizationPeriodDTO>> getAllAmortizationPeriods(AmortizationPeriodCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AmortizationPeriods by criteria: {}", criteria);
        Page<AmortizationPeriodDTO> page = amortizationPeriodQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /amortization-periods/count} : count all the amortizationPeriods.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/amortization-periods/count")
    public ResponseEntity<Long> countAmortizationPeriods(AmortizationPeriodCriteria criteria) {
        log.debug("REST request to count AmortizationPeriods by criteria: {}", criteria);
        return ResponseEntity.ok().body(amortizationPeriodQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /amortization-periods/:id} : get the "id" amortizationPeriod.
     *
     * @param id the id of the amortizationPeriodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amortizationPeriodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amortization-periods/{id}")
    public ResponseEntity<AmortizationPeriodDTO> getAmortizationPeriod(@PathVariable Long id) {
        log.debug("REST request to get AmortizationPeriod : {}", id);
        Optional<AmortizationPeriodDTO> amortizationPeriodDTO = amortizationPeriodService.findOne(id);
        return ResponseUtil.wrapOrNotFound(amortizationPeriodDTO);
    }

    /**
     * {@code GET  /amortization-periods/by-date/:id} : get the "id" amortizationPeriod.
     *
     * @param queryDate the date of the amortizationPeriodDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the amortizationPeriodDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/amortization-periods/by-date/{queryDate}")
    public ResponseEntity<AmortizationPeriodDTO> getAmortizationPeriod(@PathVariable String queryDate) {
        log.debug("REST request to get AmortizationPeriod for the date given : {}", queryDate);
        Optional<AmortizationPeriodDTO> amortizationPeriodDTO = amortizationPeriodService.findOneByDate(queryDate);
        return ResponseUtil.wrapOrNotFound(amortizationPeriodDTO);
    }

    /**
     * {@code DELETE  /amortization-periods/:id} : delete the "id" amortizationPeriod.
     *
     * @param id the id of the amortizationPeriodDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/amortization-periods/{id}")
    public ResponseEntity<Void> deleteAmortizationPeriod(@PathVariable Long id) {
        log.debug("REST request to delete AmortizationPeriod : {}", id);
        amortizationPeriodService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/amortization-periods?query=:query} : search for the amortizationPeriod corresponding
     * to the query.
     *
     * @param query the query of the amortizationPeriod search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/amortization-periods")
    public ResponseEntity<List<AmortizationPeriodDTO>> searchAmortizationPeriods(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AmortizationPeriods for query {}", query);
        Page<AmortizationPeriodDTO> page = amortizationPeriodService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
