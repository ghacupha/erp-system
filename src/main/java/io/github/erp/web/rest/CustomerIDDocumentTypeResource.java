package io.github.erp.web.rest;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.4
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

import io.github.erp.repository.CustomerIDDocumentTypeRepository;
import io.github.erp.service.CustomerIDDocumentTypeQueryService;
import io.github.erp.service.CustomerIDDocumentTypeService;
import io.github.erp.service.criteria.CustomerIDDocumentTypeCriteria;
import io.github.erp.service.dto.CustomerIDDocumentTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CustomerIDDocumentType}.
 */
@RestController
@RequestMapping("/api")
public class CustomerIDDocumentTypeResource {

    private final Logger log = LoggerFactory.getLogger(CustomerIDDocumentTypeResource.class);

    private static final String ENTITY_NAME = "customerIDDocumentType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerIDDocumentTypeService customerIDDocumentTypeService;

    private final CustomerIDDocumentTypeRepository customerIDDocumentTypeRepository;

    private final CustomerIDDocumentTypeQueryService customerIDDocumentTypeQueryService;

    public CustomerIDDocumentTypeResource(
        CustomerIDDocumentTypeService customerIDDocumentTypeService,
        CustomerIDDocumentTypeRepository customerIDDocumentTypeRepository,
        CustomerIDDocumentTypeQueryService customerIDDocumentTypeQueryService
    ) {
        this.customerIDDocumentTypeService = customerIDDocumentTypeService;
        this.customerIDDocumentTypeRepository = customerIDDocumentTypeRepository;
        this.customerIDDocumentTypeQueryService = customerIDDocumentTypeQueryService;
    }

    /**
     * {@code POST  /customer-id-document-types} : Create a new customerIDDocumentType.
     *
     * @param customerIDDocumentTypeDTO the customerIDDocumentTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerIDDocumentTypeDTO, or with status {@code 400 (Bad Request)} if the customerIDDocumentType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-id-document-types")
    public ResponseEntity<CustomerIDDocumentTypeDTO> createCustomerIDDocumentType(
        @Valid @RequestBody CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CustomerIDDocumentType : {}", customerIDDocumentTypeDTO);
        if (customerIDDocumentTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerIDDocumentType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerIDDocumentTypeDTO result = customerIDDocumentTypeService.save(customerIDDocumentTypeDTO);
        return ResponseEntity
            .created(new URI("/api/customer-id-document-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-id-document-types/:id} : Updates an existing customerIDDocumentType.
     *
     * @param id the id of the customerIDDocumentTypeDTO to save.
     * @param customerIDDocumentTypeDTO the customerIDDocumentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerIDDocumentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the customerIDDocumentTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerIDDocumentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-id-document-types/{id}")
    public ResponseEntity<CustomerIDDocumentTypeDTO> updateCustomerIDDocumentType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerIDDocumentType : {}, {}", id, customerIDDocumentTypeDTO);
        if (customerIDDocumentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerIDDocumentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerIDDocumentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerIDDocumentTypeDTO result = customerIDDocumentTypeService.save(customerIDDocumentTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerIDDocumentTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-id-document-types/:id} : Partial updates given fields of an existing customerIDDocumentType, field will ignore if it is null
     *
     * @param id the id of the customerIDDocumentTypeDTO to save.
     * @param customerIDDocumentTypeDTO the customerIDDocumentTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerIDDocumentTypeDTO,
     * or with status {@code 400 (Bad Request)} if the customerIDDocumentTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerIDDocumentTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerIDDocumentTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-id-document-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerIDDocumentTypeDTO> partialUpdateCustomerIDDocumentType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerIDDocumentTypeDTO customerIDDocumentTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerIDDocumentType partially : {}, {}", id, customerIDDocumentTypeDTO);
        if (customerIDDocumentTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerIDDocumentTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerIDDocumentTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerIDDocumentTypeDTO> result = customerIDDocumentTypeService.partialUpdate(customerIDDocumentTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, customerIDDocumentTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-id-document-types} : get all the customerIDDocumentTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerIDDocumentTypes in body.
     */
    @GetMapping("/customer-id-document-types")
    public ResponseEntity<List<CustomerIDDocumentTypeDTO>> getAllCustomerIDDocumentTypes(
        CustomerIDDocumentTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CustomerIDDocumentTypes by criteria: {}", criteria);
        Page<CustomerIDDocumentTypeDTO> page = customerIDDocumentTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-id-document-types/count} : count all the customerIDDocumentTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customer-id-document-types/count")
    public ResponseEntity<Long> countCustomerIDDocumentTypes(CustomerIDDocumentTypeCriteria criteria) {
        log.debug("REST request to count CustomerIDDocumentTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerIDDocumentTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-id-document-types/:id} : get the "id" customerIDDocumentType.
     *
     * @param id the id of the customerIDDocumentTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerIDDocumentTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-id-document-types/{id}")
    public ResponseEntity<CustomerIDDocumentTypeDTO> getCustomerIDDocumentType(@PathVariable Long id) {
        log.debug("REST request to get CustomerIDDocumentType : {}", id);
        Optional<CustomerIDDocumentTypeDTO> customerIDDocumentTypeDTO = customerIDDocumentTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerIDDocumentTypeDTO);
    }

    /**
     * {@code DELETE  /customer-id-document-types/:id} : delete the "id" customerIDDocumentType.
     *
     * @param id the id of the customerIDDocumentTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-id-document-types/{id}")
    public ResponseEntity<Void> deleteCustomerIDDocumentType(@PathVariable Long id) {
        log.debug("REST request to delete CustomerIDDocumentType : {}", id);
        customerIDDocumentTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/customer-id-document-types?query=:query} : search for the customerIDDocumentType corresponding
     * to the query.
     *
     * @param query the query of the customerIDDocumentType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/customer-id-document-types")
    public ResponseEntity<List<CustomerIDDocumentTypeDTO>> searchCustomerIDDocumentTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CustomerIDDocumentTypes for query {}", query);
        Page<CustomerIDDocumentTypeDTO> page = customerIDDocumentTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
