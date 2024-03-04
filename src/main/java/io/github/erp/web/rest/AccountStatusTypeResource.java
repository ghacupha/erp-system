package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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

import io.github.erp.repository.AccountStatusTypeRepository;
import io.github.erp.service.AccountStatusTypeQueryService;
import io.github.erp.service.AccountStatusTypeService;
import io.github.erp.service.criteria.AccountStatusTypeCriteria;
import io.github.erp.service.dto.AccountStatusTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AccountStatusType}.
 */
@RestController
@RequestMapping("/api")
public class AccountStatusTypeResource {

    private final Logger log = LoggerFactory.getLogger(AccountStatusTypeResource.class);

    private static final String ENTITY_NAME = "accountStatusType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountStatusTypeService accountStatusTypeService;

    private final AccountStatusTypeRepository accountStatusTypeRepository;

    private final AccountStatusTypeQueryService accountStatusTypeQueryService;

    public AccountStatusTypeResource(
        AccountStatusTypeService accountStatusTypeService,
        AccountStatusTypeRepository accountStatusTypeRepository,
        AccountStatusTypeQueryService accountStatusTypeQueryService
    ) {
        this.accountStatusTypeService = accountStatusTypeService;
        this.accountStatusTypeRepository = accountStatusTypeRepository;
        this.accountStatusTypeQueryService = accountStatusTypeQueryService;
    }

    /**
     * {@code POST  /account-status-types} : Create a new accountStatusType.
     *
     * @param accountStatusTypeDTO the accountStatusTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountStatusTypeDTO, or with status {@code 400 (Bad Request)} if the accountStatusType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-status-types")
    public ResponseEntity<AccountStatusTypeDTO> createAccountStatusType(@Valid @RequestBody AccountStatusTypeDTO accountStatusTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save AccountStatusType : {}", accountStatusTypeDTO);
        if (accountStatusTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountStatusType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountStatusTypeDTO result = accountStatusTypeService.save(accountStatusTypeDTO);
        return ResponseEntity
            .created(new URI("/api/account-status-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-status-types/:id} : Updates an existing accountStatusType.
     *
     * @param id the id of the accountStatusTypeDTO to save.
     * @param accountStatusTypeDTO the accountStatusTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountStatusTypeDTO,
     * or with status {@code 400 (Bad Request)} if the accountStatusTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountStatusTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-status-types/{id}")
    public ResponseEntity<AccountStatusTypeDTO> updateAccountStatusType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountStatusTypeDTO accountStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountStatusType : {}, {}", id, accountStatusTypeDTO);
        if (accountStatusTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountStatusTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountStatusTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountStatusTypeDTO result = accountStatusTypeService.save(accountStatusTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountStatusTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-status-types/:id} : Partial updates given fields of an existing accountStatusType, field will ignore if it is null
     *
     * @param id the id of the accountStatusTypeDTO to save.
     * @param accountStatusTypeDTO the accountStatusTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountStatusTypeDTO,
     * or with status {@code 400 (Bad Request)} if the accountStatusTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountStatusTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountStatusTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-status-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountStatusTypeDTO> partialUpdateAccountStatusType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountStatusTypeDTO accountStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountStatusType partially : {}, {}", id, accountStatusTypeDTO);
        if (accountStatusTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountStatusTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountStatusTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountStatusTypeDTO> result = accountStatusTypeService.partialUpdate(accountStatusTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountStatusTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /account-status-types} : get all the accountStatusTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountStatusTypes in body.
     */
    @GetMapping("/account-status-types")
    public ResponseEntity<List<AccountStatusTypeDTO>> getAllAccountStatusTypes(AccountStatusTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AccountStatusTypes by criteria: {}", criteria);
        Page<AccountStatusTypeDTO> page = accountStatusTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-status-types/count} : count all the accountStatusTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/account-status-types/count")
    public ResponseEntity<Long> countAccountStatusTypes(AccountStatusTypeCriteria criteria) {
        log.debug("REST request to count AccountStatusTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(accountStatusTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /account-status-types/:id} : get the "id" accountStatusType.
     *
     * @param id the id of the accountStatusTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountStatusTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-status-types/{id}")
    public ResponseEntity<AccountStatusTypeDTO> getAccountStatusType(@PathVariable Long id) {
        log.debug("REST request to get AccountStatusType : {}", id);
        Optional<AccountStatusTypeDTO> accountStatusTypeDTO = accountStatusTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountStatusTypeDTO);
    }

    /**
     * {@code DELETE  /account-status-types/:id} : delete the "id" accountStatusType.
     *
     * @param id the id of the accountStatusTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-status-types/{id}")
    public ResponseEntity<Void> deleteAccountStatusType(@PathVariable Long id) {
        log.debug("REST request to delete AccountStatusType : {}", id);
        accountStatusTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/account-status-types?query=:query} : search for the accountStatusType corresponding
     * to the query.
     *
     * @param query the query of the accountStatusType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/account-status-types")
    public ResponseEntity<List<AccountStatusTypeDTO>> searchAccountStatusTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccountStatusTypes for query {}", query);
        Page<AccountStatusTypeDTO> page = accountStatusTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
