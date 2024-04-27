package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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

import io.github.erp.repository.AccountOwnershipTypeRepository;
import io.github.erp.service.AccountOwnershipTypeQueryService;
import io.github.erp.service.AccountOwnershipTypeService;
import io.github.erp.service.criteria.AccountOwnershipTypeCriteria;
import io.github.erp.service.dto.AccountOwnershipTypeDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.validation.Valid;
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
 * REST controller for managing {@link io.github.erp.domain.AccountOwnershipType}.
 */
@RestController
@RequestMapping("/api")
public class AccountOwnershipTypeResource {

    private final Logger log = LoggerFactory.getLogger(AccountOwnershipTypeResource.class);

    private final AccountOwnershipTypeService accountOwnershipTypeService;

    private final AccountOwnershipTypeRepository accountOwnershipTypeRepository;

    private final AccountOwnershipTypeQueryService accountOwnershipTypeQueryService;

    public AccountOwnershipTypeResource(
        AccountOwnershipTypeService accountOwnershipTypeService,
        AccountOwnershipTypeRepository accountOwnershipTypeRepository,
        AccountOwnershipTypeQueryService accountOwnershipTypeQueryService
    ) {
        this.accountOwnershipTypeService = accountOwnershipTypeService;
        this.accountOwnershipTypeRepository = accountOwnershipTypeRepository;
        this.accountOwnershipTypeQueryService = accountOwnershipTypeQueryService;
    }

    /**
     * {@code GET  /account-ownership-types} : get all the accountOwnershipTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountOwnershipTypes in body.
     */
    @GetMapping("/account-ownership-types")
    public ResponseEntity<List<AccountOwnershipTypeDTO>> getAllAccountOwnershipTypes(
        AccountOwnershipTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AccountOwnershipTypes by criteria: {}", criteria);
        Page<AccountOwnershipTypeDTO> page = accountOwnershipTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-ownership-types/count} : count all the accountOwnershipTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/account-ownership-types/count")
    public ResponseEntity<Long> countAccountOwnershipTypes(AccountOwnershipTypeCriteria criteria) {
        log.debug("REST request to count AccountOwnershipTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(accountOwnershipTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /account-ownership-types/:id} : get the "id" accountOwnershipType.
     *
     * @param id the id of the accountOwnershipTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountOwnershipTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-ownership-types/{id}")
    public ResponseEntity<AccountOwnershipTypeDTO> getAccountOwnershipType(@PathVariable Long id) {
        log.debug("REST request to get AccountOwnershipType : {}", id);
        Optional<AccountOwnershipTypeDTO> accountOwnershipTypeDTO = accountOwnershipTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountOwnershipTypeDTO);
    }

    /**
     * {@code SEARCH  /_search/account-ownership-types?query=:query} : search for the accountOwnershipType corresponding
     * to the query.
     *
     * @param query the query of the accountOwnershipType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/account-ownership-types")
    public ResponseEntity<List<AccountOwnershipTypeDTO>> searchAccountOwnershipTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccountOwnershipTypes for query {}", query);
        Page<AccountOwnershipTypeDTO> page = accountOwnershipTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
