package io.github.erp.web.rest;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.AccountAttributeRepository;
import io.github.erp.service.AccountAttributeQueryService;
import io.github.erp.service.AccountAttributeService;
import io.github.erp.service.criteria.AccountAttributeCriteria;
import io.github.erp.service.dto.AccountAttributeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AccountAttribute}.
 */
@RestController
@RequestMapping("/api")
public class AccountAttributeResource {

    private final Logger log = LoggerFactory.getLogger(AccountAttributeResource.class);

    private static final String ENTITY_NAME = "gdiDataAccountAttribute";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountAttributeService accountAttributeService;

    private final AccountAttributeRepository accountAttributeRepository;

    private final AccountAttributeQueryService accountAttributeQueryService;

    public AccountAttributeResource(
        AccountAttributeService accountAttributeService,
        AccountAttributeRepository accountAttributeRepository,
        AccountAttributeQueryService accountAttributeQueryService
    ) {
        this.accountAttributeService = accountAttributeService;
        this.accountAttributeRepository = accountAttributeRepository;
        this.accountAttributeQueryService = accountAttributeQueryService;
    }

    /**
     * {@code POST  /account-attributes} : Create a new accountAttribute.
     *
     * @param accountAttributeDTO the accountAttributeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountAttributeDTO, or with status {@code 400 (Bad Request)} if the accountAttribute has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-attributes")
    public ResponseEntity<AccountAttributeDTO> createAccountAttribute(@Valid @RequestBody AccountAttributeDTO accountAttributeDTO)
        throws URISyntaxException {
        log.debug("REST request to save AccountAttribute : {}", accountAttributeDTO);
        if (accountAttributeDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountAttribute cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountAttributeDTO result = accountAttributeService.save(accountAttributeDTO);
        return ResponseEntity
            .created(new URI("/api/account-attributes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-attributes/:id} : Updates an existing accountAttribute.
     *
     * @param id the id of the accountAttributeDTO to save.
     * @param accountAttributeDTO the accountAttributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountAttributeDTO,
     * or with status {@code 400 (Bad Request)} if the accountAttributeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountAttributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-attributes/{id}")
    public ResponseEntity<AccountAttributeDTO> updateAccountAttribute(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountAttributeDTO accountAttributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountAttribute : {}, {}", id, accountAttributeDTO);
        if (accountAttributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountAttributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountAttributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountAttributeDTO result = accountAttributeService.save(accountAttributeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountAttributeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-attributes/:id} : Partial updates given fields of an existing accountAttribute, field will ignore if it is null
     *
     * @param id the id of the accountAttributeDTO to save.
     * @param accountAttributeDTO the accountAttributeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountAttributeDTO,
     * or with status {@code 400 (Bad Request)} if the accountAttributeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountAttributeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountAttributeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-attributes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountAttributeDTO> partialUpdateAccountAttribute(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountAttributeDTO accountAttributeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountAttribute partially : {}, {}", id, accountAttributeDTO);
        if (accountAttributeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountAttributeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountAttributeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountAttributeDTO> result = accountAttributeService.partialUpdate(accountAttributeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountAttributeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /account-attributes} : get all the accountAttributes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountAttributes in body.
     */
    @GetMapping("/account-attributes")
    public ResponseEntity<List<AccountAttributeDTO>> getAllAccountAttributes(AccountAttributeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get AccountAttributes by criteria: {}", criteria);
        Page<AccountAttributeDTO> page = accountAttributeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-attributes/count} : count all the accountAttributes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/account-attributes/count")
    public ResponseEntity<Long> countAccountAttributes(AccountAttributeCriteria criteria) {
        log.debug("REST request to count AccountAttributes by criteria: {}", criteria);
        return ResponseEntity.ok().body(accountAttributeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /account-attributes/:id} : get the "id" accountAttribute.
     *
     * @param id the id of the accountAttributeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountAttributeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-attributes/{id}")
    public ResponseEntity<AccountAttributeDTO> getAccountAttribute(@PathVariable Long id) {
        log.debug("REST request to get AccountAttribute : {}", id);
        Optional<AccountAttributeDTO> accountAttributeDTO = accountAttributeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountAttributeDTO);
    }

    /**
     * {@code DELETE  /account-attributes/:id} : delete the "id" accountAttribute.
     *
     * @param id the id of the accountAttributeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-attributes/{id}")
    public ResponseEntity<Void> deleteAccountAttribute(@PathVariable Long id) {
        log.debug("REST request to delete AccountAttribute : {}", id);
        accountAttributeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/account-attributes?query=:query} : search for the accountAttribute corresponding
     * to the query.
     *
     * @param query the query of the accountAttribute search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/account-attributes")
    public ResponseEntity<List<AccountAttributeDTO>> searchAccountAttributes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccountAttributes for query {}", query);
        Page<AccountAttributeDTO> page = accountAttributeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
