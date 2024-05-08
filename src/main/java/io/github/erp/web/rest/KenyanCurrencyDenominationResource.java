package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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

import io.github.erp.repository.KenyanCurrencyDenominationRepository;
import io.github.erp.service.KenyanCurrencyDenominationQueryService;
import io.github.erp.service.KenyanCurrencyDenominationService;
import io.github.erp.service.criteria.KenyanCurrencyDenominationCriteria;
import io.github.erp.service.dto.KenyanCurrencyDenominationDTO;
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
 * REST controller for managing {@link io.github.erp.domain.KenyanCurrencyDenomination}.
 */
@RestController
@RequestMapping("/api")
public class KenyanCurrencyDenominationResource {

    private final Logger log = LoggerFactory.getLogger(KenyanCurrencyDenominationResource.class);

    private static final String ENTITY_NAME = "kenyanCurrencyDenomination";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KenyanCurrencyDenominationService kenyanCurrencyDenominationService;

    private final KenyanCurrencyDenominationRepository kenyanCurrencyDenominationRepository;

    private final KenyanCurrencyDenominationQueryService kenyanCurrencyDenominationQueryService;

    public KenyanCurrencyDenominationResource(
        KenyanCurrencyDenominationService kenyanCurrencyDenominationService,
        KenyanCurrencyDenominationRepository kenyanCurrencyDenominationRepository,
        KenyanCurrencyDenominationQueryService kenyanCurrencyDenominationQueryService
    ) {
        this.kenyanCurrencyDenominationService = kenyanCurrencyDenominationService;
        this.kenyanCurrencyDenominationRepository = kenyanCurrencyDenominationRepository;
        this.kenyanCurrencyDenominationQueryService = kenyanCurrencyDenominationQueryService;
    }

    /**
     * {@code POST  /kenyan-currency-denominations} : Create a new kenyanCurrencyDenomination.
     *
     * @param kenyanCurrencyDenominationDTO the kenyanCurrencyDenominationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kenyanCurrencyDenominationDTO, or with status {@code 400 (Bad Request)} if the kenyanCurrencyDenomination has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kenyan-currency-denominations")
    public ResponseEntity<KenyanCurrencyDenominationDTO> createKenyanCurrencyDenomination(
        @Valid @RequestBody KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO
    ) throws URISyntaxException {
        log.debug("REST request to save KenyanCurrencyDenomination : {}", kenyanCurrencyDenominationDTO);
        if (kenyanCurrencyDenominationDTO.getId() != null) {
            throw new BadRequestAlertException("A new kenyanCurrencyDenomination cannot already have an ID", ENTITY_NAME, "idexists");
        }
        KenyanCurrencyDenominationDTO result = kenyanCurrencyDenominationService.save(kenyanCurrencyDenominationDTO);
        return ResponseEntity
            .created(new URI("/api/kenyan-currency-denominations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kenyan-currency-denominations/:id} : Updates an existing kenyanCurrencyDenomination.
     *
     * @param id the id of the kenyanCurrencyDenominationDTO to save.
     * @param kenyanCurrencyDenominationDTO the kenyanCurrencyDenominationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kenyanCurrencyDenominationDTO,
     * or with status {@code 400 (Bad Request)} if the kenyanCurrencyDenominationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kenyanCurrencyDenominationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kenyan-currency-denominations/{id}")
    public ResponseEntity<KenyanCurrencyDenominationDTO> updateKenyanCurrencyDenomination(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update KenyanCurrencyDenomination : {}, {}", id, kenyanCurrencyDenominationDTO);
        if (kenyanCurrencyDenominationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kenyanCurrencyDenominationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kenyanCurrencyDenominationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        KenyanCurrencyDenominationDTO result = kenyanCurrencyDenominationService.save(kenyanCurrencyDenominationDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kenyanCurrencyDenominationDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /kenyan-currency-denominations/:id} : Partial updates given fields of an existing kenyanCurrencyDenomination, field will ignore if it is null
     *
     * @param id the id of the kenyanCurrencyDenominationDTO to save.
     * @param kenyanCurrencyDenominationDTO the kenyanCurrencyDenominationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kenyanCurrencyDenominationDTO,
     * or with status {@code 400 (Bad Request)} if the kenyanCurrencyDenominationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the kenyanCurrencyDenominationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the kenyanCurrencyDenominationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/kenyan-currency-denominations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<KenyanCurrencyDenominationDTO> partialUpdateKenyanCurrencyDenomination(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody KenyanCurrencyDenominationDTO kenyanCurrencyDenominationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update KenyanCurrencyDenomination partially : {}, {}", id, kenyanCurrencyDenominationDTO);
        if (kenyanCurrencyDenominationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kenyanCurrencyDenominationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kenyanCurrencyDenominationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<KenyanCurrencyDenominationDTO> result = kenyanCurrencyDenominationService.partialUpdate(kenyanCurrencyDenominationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kenyanCurrencyDenominationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /kenyan-currency-denominations} : get all the kenyanCurrencyDenominations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kenyanCurrencyDenominations in body.
     */
    @GetMapping("/kenyan-currency-denominations")
    public ResponseEntity<List<KenyanCurrencyDenominationDTO>> getAllKenyanCurrencyDenominations(
        KenyanCurrencyDenominationCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get KenyanCurrencyDenominations by criteria: {}", criteria);
        Page<KenyanCurrencyDenominationDTO> page = kenyanCurrencyDenominationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /kenyan-currency-denominations/count} : count all the kenyanCurrencyDenominations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/kenyan-currency-denominations/count")
    public ResponseEntity<Long> countKenyanCurrencyDenominations(KenyanCurrencyDenominationCriteria criteria) {
        log.debug("REST request to count KenyanCurrencyDenominations by criteria: {}", criteria);
        return ResponseEntity.ok().body(kenyanCurrencyDenominationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /kenyan-currency-denominations/:id} : get the "id" kenyanCurrencyDenomination.
     *
     * @param id the id of the kenyanCurrencyDenominationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kenyanCurrencyDenominationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kenyan-currency-denominations/{id}")
    public ResponseEntity<KenyanCurrencyDenominationDTO> getKenyanCurrencyDenomination(@PathVariable Long id) {
        log.debug("REST request to get KenyanCurrencyDenomination : {}", id);
        Optional<KenyanCurrencyDenominationDTO> kenyanCurrencyDenominationDTO = kenyanCurrencyDenominationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(kenyanCurrencyDenominationDTO);
    }

    /**
     * {@code DELETE  /kenyan-currency-denominations/:id} : delete the "id" kenyanCurrencyDenomination.
     *
     * @param id the id of the kenyanCurrencyDenominationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kenyan-currency-denominations/{id}")
    public ResponseEntity<Void> deleteKenyanCurrencyDenomination(@PathVariable Long id) {
        log.debug("REST request to delete KenyanCurrencyDenomination : {}", id);
        kenyanCurrencyDenominationService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/kenyan-currency-denominations?query=:query} : search for the kenyanCurrencyDenomination corresponding
     * to the query.
     *
     * @param query the query of the kenyanCurrencyDenomination search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/kenyan-currency-denominations")
    public ResponseEntity<List<KenyanCurrencyDenominationDTO>> searchKenyanCurrencyDenominations(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of KenyanCurrencyDenominations for query {}", query);
        Page<KenyanCurrencyDenominationDTO> page = kenyanCurrencyDenominationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
