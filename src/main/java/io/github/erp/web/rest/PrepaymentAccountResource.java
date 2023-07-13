package io.github.erp.web.rest;

/*-
 * Erp System - Mark IV No 2 (Ehud Series) Server ver 1.3.2
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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.PrepaymentAccountRepository;
import io.github.erp.service.PrepaymentAccountQueryService;
import io.github.erp.service.PrepaymentAccountService;
import io.github.erp.service.criteria.PrepaymentAccountCriteria;
import io.github.erp.service.dto.PrepaymentAccountDTO;
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
 * REST controller for managing {@link io.github.erp.domain.PrepaymentAccount}.
 */
@RestController
@RequestMapping("/api")
public class PrepaymentAccountResource {

    private final Logger log = LoggerFactory.getLogger(PrepaymentAccountResource.class);

    private static final String ENTITY_NAME = "prepaymentAccount";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PrepaymentAccountService prepaymentAccountService;

    private final PrepaymentAccountRepository prepaymentAccountRepository;

    private final PrepaymentAccountQueryService prepaymentAccountQueryService;

    public PrepaymentAccountResource(
        PrepaymentAccountService prepaymentAccountService,
        PrepaymentAccountRepository prepaymentAccountRepository,
        PrepaymentAccountQueryService prepaymentAccountQueryService
    ) {
        this.prepaymentAccountService = prepaymentAccountService;
        this.prepaymentAccountRepository = prepaymentAccountRepository;
        this.prepaymentAccountQueryService = prepaymentAccountQueryService;
    }

    /**
     * {@code POST  /prepayment-accounts} : Create a new prepaymentAccount.
     *
     * @param prepaymentAccountDTO the prepaymentAccountDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prepaymentAccountDTO, or with status {@code 400 (Bad Request)} if the prepaymentAccount has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prepayment-accounts")
    public ResponseEntity<PrepaymentAccountDTO> createPrepaymentAccount(@Valid @RequestBody PrepaymentAccountDTO prepaymentAccountDTO)
        throws URISyntaxException {
        log.debug("REST request to save PrepaymentAccount : {}", prepaymentAccountDTO);
        if (prepaymentAccountDTO.getId() != null) {
            throw new BadRequestAlertException("A new prepaymentAccount cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrepaymentAccountDTO result = prepaymentAccountService.save(prepaymentAccountDTO);
        return ResponseEntity
            .created(new URI("/api/prepayment-accounts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /prepayment-accounts/:id} : Updates an existing prepaymentAccount.
     *
     * @param id the id of the prepaymentAccountDTO to save.
     * @param prepaymentAccountDTO the prepaymentAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentAccountDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentAccountDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prepayment-accounts/{id}")
    public ResponseEntity<PrepaymentAccountDTO> updatePrepaymentAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PrepaymentAccountDTO prepaymentAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrepaymentAccount : {}, {}", id, prepaymentAccountDTO);
        if (prepaymentAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrepaymentAccountDTO result = prepaymentAccountService.save(prepaymentAccountDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prepaymentAccountDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /prepayment-accounts/:id} : Partial updates given fields of an existing prepaymentAccount, field will ignore if it is null
     *
     * @param id the id of the prepaymentAccountDTO to save.
     * @param prepaymentAccountDTO the prepaymentAccountDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentAccountDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentAccountDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prepaymentAccountDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentAccountDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prepayment-accounts/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrepaymentAccountDTO> partialUpdatePrepaymentAccount(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PrepaymentAccountDTO prepaymentAccountDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrepaymentAccount partially : {}, {}", id, prepaymentAccountDTO);
        if (prepaymentAccountDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentAccountDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentAccountRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrepaymentAccountDTO> result = prepaymentAccountService.partialUpdate(prepaymentAccountDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, prepaymentAccountDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prepayment-accounts} : get all the prepaymentAccounts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentAccounts in body.
     */
    @GetMapping("/prepayment-accounts")
    public ResponseEntity<List<PrepaymentAccountDTO>> getAllPrepaymentAccounts(PrepaymentAccountCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PrepaymentAccounts by criteria: {}", criteria);
        Page<PrepaymentAccountDTO> page = prepaymentAccountQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-accounts/count} : count all the prepaymentAccounts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-accounts/count")
    public ResponseEntity<Long> countPrepaymentAccounts(PrepaymentAccountCriteria criteria) {
        log.debug("REST request to count PrepaymentAccounts by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentAccountQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-accounts/:id} : get the "id" prepaymentAccount.
     *
     * @param id the id of the prepaymentAccountDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentAccountDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-accounts/{id}")
    public ResponseEntity<PrepaymentAccountDTO> getPrepaymentAccount(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentAccount : {}", id);
        Optional<PrepaymentAccountDTO> prepaymentAccountDTO = prepaymentAccountService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaymentAccountDTO);
    }

    /**
     * {@code DELETE  /prepayment-accounts/:id} : delete the "id" prepaymentAccount.
     *
     * @param id the id of the prepaymentAccountDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prepayment-accounts/{id}")
    public ResponseEntity<Void> deletePrepaymentAccount(@PathVariable Long id) {
        log.debug("REST request to delete PrepaymentAccount : {}", id);
        prepaymentAccountService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/prepayment-accounts?query=:query} : search for the prepaymentAccount corresponding
     * to the query.
     *
     * @param query the query of the prepaymentAccount search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-accounts")
    public ResponseEntity<List<PrepaymentAccountDTO>> searchPrepaymentAccounts(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PrepaymentAccounts for query {}", query);
        Page<PrepaymentAccountDTO> page = prepaymentAccountService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
