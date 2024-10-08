package io.github.erp.web.rest;

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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.AccountAttributeMetadataRepository;
import io.github.erp.service.AccountAttributeMetadataQueryService;
import io.github.erp.service.AccountAttributeMetadataService;
import io.github.erp.service.criteria.AccountAttributeMetadataCriteria;
import io.github.erp.service.dto.AccountAttributeMetadataDTO;
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
 * REST controller for managing {@link io.github.erp.domain.AccountAttributeMetadata}.
 */
@RestController
@RequestMapping("/api")
public class AccountAttributeMetadataResource {

    private final Logger log = LoggerFactory.getLogger(AccountAttributeMetadataResource.class);

    private static final String ENTITY_NAME = "gdiDataAccountAttributeMetadata";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccountAttributeMetadataService accountAttributeMetadataService;

    private final AccountAttributeMetadataRepository accountAttributeMetadataRepository;

    private final AccountAttributeMetadataQueryService accountAttributeMetadataQueryService;

    public AccountAttributeMetadataResource(
        AccountAttributeMetadataService accountAttributeMetadataService,
        AccountAttributeMetadataRepository accountAttributeMetadataRepository,
        AccountAttributeMetadataQueryService accountAttributeMetadataQueryService
    ) {
        this.accountAttributeMetadataService = accountAttributeMetadataService;
        this.accountAttributeMetadataRepository = accountAttributeMetadataRepository;
        this.accountAttributeMetadataQueryService = accountAttributeMetadataQueryService;
    }

    /**
     * {@code POST  /account-attribute-metadata} : Create a new accountAttributeMetadata.
     *
     * @param accountAttributeMetadataDTO the accountAttributeMetadataDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accountAttributeMetadataDTO, or with status {@code 400 (Bad Request)} if the accountAttributeMetadata has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/account-attribute-metadata")
    public ResponseEntity<AccountAttributeMetadataDTO> createAccountAttributeMetadata(
        @Valid @RequestBody AccountAttributeMetadataDTO accountAttributeMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to save AccountAttributeMetadata : {}", accountAttributeMetadataDTO);
        if (accountAttributeMetadataDTO.getId() != null) {
            throw new BadRequestAlertException("A new accountAttributeMetadata cannot already have an ID", ENTITY_NAME, "idexists");
        }
        AccountAttributeMetadataDTO result = accountAttributeMetadataService.save(accountAttributeMetadataDTO);
        return ResponseEntity
            .created(new URI("/api/account-attribute-metadata/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /account-attribute-metadata/:id} : Updates an existing accountAttributeMetadata.
     *
     * @param id the id of the accountAttributeMetadataDTO to save.
     * @param accountAttributeMetadataDTO the accountAttributeMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountAttributeMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the accountAttributeMetadataDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accountAttributeMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/account-attribute-metadata/{id}")
    public ResponseEntity<AccountAttributeMetadataDTO> updateAccountAttributeMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccountAttributeMetadataDTO accountAttributeMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to update AccountAttributeMetadata : {}, {}", id, accountAttributeMetadataDTO);
        if (accountAttributeMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountAttributeMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountAttributeMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        AccountAttributeMetadataDTO result = accountAttributeMetadataService.save(accountAttributeMetadataDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountAttributeMetadataDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /account-attribute-metadata/:id} : Partial updates given fields of an existing accountAttributeMetadata, field will ignore if it is null
     *
     * @param id the id of the accountAttributeMetadataDTO to save.
     * @param accountAttributeMetadataDTO the accountAttributeMetadataDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accountAttributeMetadataDTO,
     * or with status {@code 400 (Bad Request)} if the accountAttributeMetadataDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accountAttributeMetadataDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accountAttributeMetadataDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/account-attribute-metadata/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccountAttributeMetadataDTO> partialUpdateAccountAttributeMetadata(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccountAttributeMetadataDTO accountAttributeMetadataDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update AccountAttributeMetadata partially : {}, {}", id, accountAttributeMetadataDTO);
        if (accountAttributeMetadataDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accountAttributeMetadataDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accountAttributeMetadataRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccountAttributeMetadataDTO> result = accountAttributeMetadataService.partialUpdate(accountAttributeMetadataDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, accountAttributeMetadataDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /account-attribute-metadata} : get all the accountAttributeMetadata.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accountAttributeMetadata in body.
     */
    @GetMapping("/account-attribute-metadata")
    public ResponseEntity<List<AccountAttributeMetadataDTO>> getAllAccountAttributeMetadata(
        AccountAttributeMetadataCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get AccountAttributeMetadata by criteria: {}", criteria);
        Page<AccountAttributeMetadataDTO> page = accountAttributeMetadataQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /account-attribute-metadata/count} : count all the accountAttributeMetadata.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/account-attribute-metadata/count")
    public ResponseEntity<Long> countAccountAttributeMetadata(AccountAttributeMetadataCriteria criteria) {
        log.debug("REST request to count AccountAttributeMetadata by criteria: {}", criteria);
        return ResponseEntity.ok().body(accountAttributeMetadataQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /account-attribute-metadata/:id} : get the "id" accountAttributeMetadata.
     *
     * @param id the id of the accountAttributeMetadataDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accountAttributeMetadataDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/account-attribute-metadata/{id}")
    public ResponseEntity<AccountAttributeMetadataDTO> getAccountAttributeMetadata(@PathVariable Long id) {
        log.debug("REST request to get AccountAttributeMetadata : {}", id);
        Optional<AccountAttributeMetadataDTO> accountAttributeMetadataDTO = accountAttributeMetadataService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accountAttributeMetadataDTO);
    }

    /**
     * {@code DELETE  /account-attribute-metadata/:id} : delete the "id" accountAttributeMetadata.
     *
     * @param id the id of the accountAttributeMetadataDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/account-attribute-metadata/{id}")
    public ResponseEntity<Void> deleteAccountAttributeMetadata(@PathVariable Long id) {
        log.debug("REST request to delete AccountAttributeMetadata : {}", id);
        accountAttributeMetadataService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/account-attribute-metadata?query=:query} : search for the accountAttributeMetadata corresponding
     * to the query.
     *
     * @param query the query of the accountAttributeMetadata search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/account-attribute-metadata")
    public ResponseEntity<List<AccountAttributeMetadataDTO>> searchAccountAttributeMetadata(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of AccountAttributeMetadata for query {}", query);
        Page<AccountAttributeMetadataDTO> page = accountAttributeMetadataService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
