package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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

import io.github.erp.repository.AccountTypeRepository;
import io.github.erp.service.AccountTypeQueryService;
import io.github.erp.service.AccountTypeService;
import io.github.erp.service.criteria.AccountTypeCriteria;
import io.github.erp.service.dto.AccountTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AccountType}.
 */
@RestController
@RequestMapping("/api")
public class AccountTypeResource {

    private final Logger log = LoggerFactory.getLogger(AccountTypeResource.class);

    private static final String ENTITY_NAME = "accountType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountTypeService accountTypeService;

    private final AccountTypeRepository accountTypeRepository;

    private final AccountTypeQueryService accountTypeQueryService;

    public AccountTypeResource(
        AccountTypeService accountTypeService,
        AccountTypeRepository accountTypeRepository,
        AccountTypeQueryService accountTypeQueryService
    ) {
        this.accountTypeService = accountTypeService;
        this.accountTypeRepository = accountTypeRepository;
        this.accountTypeQueryService = accountTypeQueryService;
    }

    /**
     * {@code POST  /account-types} : Create a new accountType.
     *
     * @param accountTypeDTO the accountTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountTypeDTO, or with status {@code 400 (Bad Request)} if the accountType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-types")
    public ResponseEntity<AccountTypeDTO> createAccountType(@Valid @RequestBody AccountTypeDTO accountTypeDTO) throws URISyntaxException {
        log.debug("REST request to save AccountType : {}", accountTypeDTO);
        if (accountTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountTypeDTO result = accountTypeService.save(accountTypeDTO);
        return ResponseEntity
            .created(new URI("/api/account-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-types/:id} : Updates an existing accountType.
     *
     * @param id the id of the accountTypeDTO to save.
     * @param accountTypeDTO the accountTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountTypeDTO,
     * or with status {@code 400 (Bad Request)} if the accountTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-types/{id}")
    public ResponseEntity<AccountTypeDTO> updateAccountType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountTypeDTO accountTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountType : {}, {}", id, accountTypeDTO);
        if (accountTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountTypeDTO result = accountTypeService.save(accountTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-types/:id} : Partial updates given fields of an existing accountType, field will ignore if it is null
     *
     * @param id the id of the accountTypeDTO to save.
     * @param accountTypeDTO the accountTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountTypeDTO,
     * or with status {@code 400 (Bad Request)} if the accountTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountTypeDTO> partialUpdateAccountType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountTypeDTO accountTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountType partially : {}, {}", id, accountTypeDTO);
        if (accountTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountTypeDTO> result = accountTypeService.partialUpdate(accountTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /account-types} : get all the accountTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountTypes in body.
     */
    @GetMapping("/account-types")
    public ResponseEntity<List<AccountTypeDTO>> getAllAccountTypes(AccountTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AccountTypes by criteria: {}", criteria);
        Page<AccountTypeDTO> page = accountTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-types/count} : count all the accountTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/account-types/count")
    public ResponseEntity<Long> countAccountTypes(AccountTypeCriteria criteria) {
        log.debug("REST request to count AccountTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(accountTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /account-types/:id} : get the "id" accountType.
     *
     * @param id the id of the accountTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-types/{id}")
    public ResponseEntity<AccountTypeDTO> getAccountType(@PathVariable Long id) {
        log.debug("REST request to get AccountType : {}", id);
        Optional<AccountTypeDTO> accountTypeDTO = accountTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountTypeDTO);
    }

    /**
     * {@code DELETE  /account-types/:id} : delete the "id" accountType.
     *
     * @param id the id of the accountTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-types/{id}")
    public ResponseEntity<Void> deleteAccountType(@PathVariable Long id) {
        log.debug("REST request to delete AccountType : {}", id);
        accountTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/account-types?query=:query} : search for the accountType corresponding
     * to the query.
     *
     * @param query the query of the accountType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/account-types")
    public ResponseEntity<List<AccountTypeDTO>> searchAccountTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccountTypes for query {}", query);
        Page<AccountTypeDTO> page = accountTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
