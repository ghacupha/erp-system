package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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

import io.github.erp.repository.TransactionAccountPostingProcessTypeRepository;
import io.github.erp.service.TransactionAccountPostingProcessTypeQueryService;
import io.github.erp.service.TransactionAccountPostingProcessTypeService;
import io.github.erp.service.criteria.TransactionAccountPostingProcessTypeCriteria;
import io.github.erp.service.dto.TransactionAccountPostingProcessTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.TransactionAccountPostingProcessType}.
 */
@RestController
@RequestMapping("/api")
public class TransactionAccountPostingProcessTypeResource {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountPostingProcessTypeResource.class);

    private static final String ENTITY_NAME = "transactionAccountPostingProcessType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TransactionAccountPostingProcessTypeService transactionAccountPostingProcessTypeService;

    private final TransactionAccountPostingProcessTypeRepository transactionAccountPostingProcessTypeRepository;

    private final TransactionAccountPostingProcessTypeQueryService transactionAccountPostingProcessTypeQueryService;

    public TransactionAccountPostingProcessTypeResource(
        TransactionAccountPostingProcessTypeService transactionAccountPostingProcessTypeService,
        TransactionAccountPostingProcessTypeRepository transactionAccountPostingProcessTypeRepository,
        TransactionAccountPostingProcessTypeQueryService transactionAccountPostingProcessTypeQueryService
    ) {
        this.transactionAccountPostingProcessTypeService = transactionAccountPostingProcessTypeService;
        this.transactionAccountPostingProcessTypeRepository = transactionAccountPostingProcessTypeRepository;
        this.transactionAccountPostingProcessTypeQueryService = transactionAccountPostingProcessTypeQueryService;
    }

    /**
     * {@code POST  /transaction-account-posting-process-types} : Create a new transactionAccountPostingProcessType.
     *
     * @param transactionAccountPostingProcessTypeDTO the transactionAccountPostingProcessTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new transactionAccountPostingProcessTypeDTO, or with status {@code 400 (Bad Request)} if the transactionAccountPostingProcessType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/transaction-account-posting-process-types")
    public ResponseEntity<TransactionAccountPostingProcessTypeDTO> createTransactionAccountPostingProcessType(
        @Valid @RequestBody TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save TransactionAccountPostingProcessType : {}", transactionAccountPostingProcessTypeDTO);
        if (transactionAccountPostingProcessTypeDTO.getId() != null) {
            throw new BadRequestAlertException(
                "A new transactionAccountPostingProcessType cannot already have an ID",
                ENTITY_NAME,
                "idexists"
            );
        }
        TransactionAccountPostingProcessTypeDTO result = transactionAccountPostingProcessTypeService.save(
            transactionAccountPostingProcessTypeDTO
        );
        return ResponseEntity
            .created(new URI("/api/transaction-account-posting-process-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /transaction-account-posting-process-types/:id} : Updates an existing transactionAccountPostingProcessType.
     *
     * @param id the id of the transactionAccountPostingProcessTypeDTO to save.
     * @param transactionAccountPostingProcessTypeDTO the transactionAccountPostingProcessTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountPostingProcessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountPostingProcessTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountPostingProcessTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/transaction-account-posting-process-types/{id}")
    public ResponseEntity<TransactionAccountPostingProcessTypeDTO> updateTransactionAccountPostingProcessType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update TransactionAccountPostingProcessType : {}, {}", id, transactionAccountPostingProcessTypeDTO);
        if (transactionAccountPostingProcessTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountPostingProcessTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountPostingProcessTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TransactionAccountPostingProcessTypeDTO result = transactionAccountPostingProcessTypeService.save(
            transactionAccountPostingProcessTypeDTO
        );
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    transactionAccountPostingProcessTypeDTO.getId().toString()
                )
            )
            .body(result);
    }

    /**
     * {@code PATCH  /transaction-account-posting-process-types/:id} : Partial updates given fields of an existing transactionAccountPostingProcessType, field will ignore if it is null
     *
     * @param id the id of the transactionAccountPostingProcessTypeDTO to save.
     * @param transactionAccountPostingProcessTypeDTO the transactionAccountPostingProcessTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated transactionAccountPostingProcessTypeDTO,
     * or with status {@code 400 (Bad Request)} if the transactionAccountPostingProcessTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the transactionAccountPostingProcessTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the transactionAccountPostingProcessTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(
        value = "/transaction-account-posting-process-types/{id}",
        consumes = { "application/json", "application/merge-patch+json" }
    )
    public ResponseEntity<TransactionAccountPostingProcessTypeDTO> partialUpdateTransactionAccountPostingProcessType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TransactionAccountPostingProcessTypeDTO transactionAccountPostingProcessTypeDTO
    ) throws URISyntaxException {
        log.debug(
            "REST request to partial update TransactionAccountPostingProcessType partially : {}, {}",
            id,
            transactionAccountPostingProcessTypeDTO
        );
        if (transactionAccountPostingProcessTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, transactionAccountPostingProcessTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!transactionAccountPostingProcessTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TransactionAccountPostingProcessTypeDTO> result = transactionAccountPostingProcessTypeService.partialUpdate(
            transactionAccountPostingProcessTypeDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(
                applicationName,
                true,
                ENTITY_NAME,
                transactionAccountPostingProcessTypeDTO.getId().toString()
            )
        );
    }

    /**
     * {@code GET  /transaction-account-posting-process-types} : get all the transactionAccountPostingProcessTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of transactionAccountPostingProcessTypes in body.
     */
    @GetMapping("/transaction-account-posting-process-types")
    public ResponseEntity<List<TransactionAccountPostingProcessTypeDTO>> getAllTransactionAccountPostingProcessTypes(
        TransactionAccountPostingProcessTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get TransactionAccountPostingProcessTypes by criteria: {}", criteria);
        Page<TransactionAccountPostingProcessTypeDTO> page = transactionAccountPostingProcessTypeQueryService.findByCriteria(
            criteria,
            pageable
        );
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /transaction-account-posting-process-types/count} : count all the transactionAccountPostingProcessTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/transaction-account-posting-process-types/count")
    public ResponseEntity<Long> countTransactionAccountPostingProcessTypes(TransactionAccountPostingProcessTypeCriteria criteria) {
        log.debug("REST request to count TransactionAccountPostingProcessTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(transactionAccountPostingProcessTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /transaction-account-posting-process-types/:id} : get the "id" transactionAccountPostingProcessType.
     *
     * @param id the id of the transactionAccountPostingProcessTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the transactionAccountPostingProcessTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/transaction-account-posting-process-types/{id}")
    public ResponseEntity<TransactionAccountPostingProcessTypeDTO> getTransactionAccountPostingProcessType(@PathVariable Long id) {
        log.debug("REST request to get TransactionAccountPostingProcessType : {}", id);
        Optional<TransactionAccountPostingProcessTypeDTO> transactionAccountPostingProcessTypeDTO = transactionAccountPostingProcessTypeService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(transactionAccountPostingProcessTypeDTO);
    }

    /**
     * {@code DELETE  /transaction-account-posting-process-types/:id} : delete the "id" transactionAccountPostingProcessType.
     *
     * @param id the id of the transactionAccountPostingProcessTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/transaction-account-posting-process-types/{id}")
    public ResponseEntity<Void> deleteTransactionAccountPostingProcessType(@PathVariable Long id) {
        log.debug("REST request to delete TransactionAccountPostingProcessType : {}", id);
        transactionAccountPostingProcessTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/transaction-account-posting-process-types?query=:query} : search for the transactionAccountPostingProcessType corresponding
     * to the query.
     *
     * @param query the query of the transactionAccountPostingProcessType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/transaction-account-posting-process-types")
    public ResponseEntity<List<TransactionAccountPostingProcessTypeDTO>> searchTransactionAccountPostingProcessTypes(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of TransactionAccountPostingProcessTypes for query {}", query);
        Page<TransactionAccountPostingProcessTypeDTO> page = transactionAccountPostingProcessTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
