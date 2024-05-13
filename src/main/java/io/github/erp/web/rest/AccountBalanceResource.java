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

import io.github.erp.repository.AccountBalanceRepository;
import io.github.erp.service.AccountBalanceQueryService;
import io.github.erp.service.AccountBalanceService;
import io.github.erp.service.criteria.AccountBalanceCriteria;
import io.github.erp.service.dto.AccountBalanceDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AccountBalance}.
 */
@RestController
@RequestMapping("/api")
public class AccountBalanceResource {

    private final Logger log = LoggerFactory.getLogger(AccountBalanceResource.class);

    private static final String ENTITY_NAME = "gdiDataAccountBalance";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountBalanceService accountBalanceService;

    private final AccountBalanceRepository accountBalanceRepository;

    private final AccountBalanceQueryService accountBalanceQueryService;

    public AccountBalanceResource(
        AccountBalanceService accountBalanceService,
        AccountBalanceRepository accountBalanceRepository,
        AccountBalanceQueryService accountBalanceQueryService
    ) {
        this.accountBalanceService = accountBalanceService;
        this.accountBalanceRepository = accountBalanceRepository;
        this.accountBalanceQueryService = accountBalanceQueryService;
    }

    /**
     * {@code POST  /account-balances} : Create a new accountBalance.
     *
     * @param accountBalanceDTO the accountBalanceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountBalanceDTO, or with status {@code 400 (Bad Request)} if the accountBalance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-balances")
    public ResponseEntity<AccountBalanceDTO> createAccountBalance(@Valid @RequestBody AccountBalanceDTO accountBalanceDTO)
        throws URISyntaxException {
        log.debug("REST request to save AccountBalance : {}", accountBalanceDTO);
        if (accountBalanceDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountBalance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountBalanceDTO result = accountBalanceService.save(accountBalanceDTO);
        return ResponseEntity
            .created(new URI("/api/account-balances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-balances/:id} : Updates an existing accountBalance.
     *
     * @param id the id of the accountBalanceDTO to save.
     * @param accountBalanceDTO the accountBalanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountBalanceDTO,
     * or with status {@code 400 (Bad Request)} if the accountBalanceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountBalanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-balances/{id}")
    public ResponseEntity<AccountBalanceDTO> updateAccountBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountBalanceDTO accountBalanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountBalance : {}, {}", id, accountBalanceDTO);
        if (accountBalanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountBalanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountBalanceDTO result = accountBalanceService.save(accountBalanceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountBalanceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-balances/:id} : Partial updates given fields of an existing accountBalance, field will ignore if it is null
     *
     * @param id the id of the accountBalanceDTO to save.
     * @param accountBalanceDTO the accountBalanceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountBalanceDTO,
     * or with status {@code 400 (Bad Request)} if the accountBalanceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountBalanceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountBalanceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-balances/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountBalanceDTO> partialUpdateAccountBalance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountBalanceDTO accountBalanceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountBalance partially : {}, {}", id, accountBalanceDTO);
        if (accountBalanceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountBalanceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountBalanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountBalanceDTO> result = accountBalanceService.partialUpdate(accountBalanceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountBalanceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /account-balances} : get all the accountBalances.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountBalances in body.
     */
    @GetMapping("/account-balances")
    public ResponseEntity<List<AccountBalanceDTO>> getAllAccountBalances(AccountBalanceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AccountBalances by criteria: {}", criteria);
        Page<AccountBalanceDTO> page = accountBalanceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-balances/count} : count all the accountBalances.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/account-balances/count")
    public ResponseEntity<Long> countAccountBalances(AccountBalanceCriteria criteria) {
        log.debug("REST request to count AccountBalances by criteria: {}", criteria);
        return ResponseEntity.ok().body(accountBalanceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /account-balances/:id} : get the "id" accountBalance.
     *
     * @param id the id of the accountBalanceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountBalanceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-balances/{id}")
    public ResponseEntity<AccountBalanceDTO> getAccountBalance(@PathVariable Long id) {
        log.debug("REST request to get AccountBalance : {}", id);
        Optional<AccountBalanceDTO> accountBalanceDTO = accountBalanceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountBalanceDTO);
    }

    /**
     * {@code DELETE  /account-balances/:id} : delete the "id" accountBalance.
     *
     * @param id the id of the accountBalanceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-balances/{id}")
    public ResponseEntity<Void> deleteAccountBalance(@PathVariable Long id) {
        log.debug("REST request to delete AccountBalance : {}", id);
        accountBalanceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/account-balances?query=:query} : search for the accountBalance corresponding
     * to the query.
     *
     * @param query the query of the accountBalance search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/account-balances")
    public ResponseEntity<List<AccountBalanceDTO>> searchAccountBalances(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccountBalances for query {}", query);
        Page<AccountBalanceDTO> page = accountBalanceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
