package io.github.erp.erp.resources.gdi;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.repository.CustomerComplaintStatusTypeRepository;
import io.github.erp.service.CustomerComplaintStatusTypeQueryService;
import io.github.erp.service.CustomerComplaintStatusTypeService;
import io.github.erp.service.criteria.CustomerComplaintStatusTypeCriteria;
import io.github.erp.service.dto.CustomerComplaintStatusTypeDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.CustomerComplaintStatusType}.
 */
@RestController("CustomerComplaintStatusTypeResourceProd")
@RequestMapping("/api/granular-data")
public class CustomerComplaintStatusTypeResourceProd {

    private final Logger log = LoggerFactory.getLogger(CustomerComplaintStatusTypeResourceProd.class);

    private static final String ENTITY_NAME = "customerComplaintStatusType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerComplaintStatusTypeService customerComplaintStatusTypeService;

    private final CustomerComplaintStatusTypeRepository customerComplaintStatusTypeRepository;

    private final CustomerComplaintStatusTypeQueryService customerComplaintStatusTypeQueryService;

    public CustomerComplaintStatusTypeResourceProd(
        CustomerComplaintStatusTypeService customerComplaintStatusTypeService,
        CustomerComplaintStatusTypeRepository customerComplaintStatusTypeRepository,
        CustomerComplaintStatusTypeQueryService customerComplaintStatusTypeQueryService
    ) {
        this.customerComplaintStatusTypeService = customerComplaintStatusTypeService;
        this.customerComplaintStatusTypeRepository = customerComplaintStatusTypeRepository;
        this.customerComplaintStatusTypeQueryService = customerComplaintStatusTypeQueryService;
    }

    /**
     * {@code POST  /customer-complaint-status-types} : Create a new customerComplaintStatusType.
     *
     * @param customerComplaintStatusTypeDTO the customerComplaintStatusTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerComplaintStatusTypeDTO, or with status {@code 400 (Bad Request)} if the customerComplaintStatusType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-complaint-status-types")
    public ResponseEntity<CustomerComplaintStatusTypeDTO> createCustomerComplaintStatusType(
        @Valid @RequestBody CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to save CustomerComplaintStatusType : {}", customerComplaintStatusTypeDTO);
        if (customerComplaintStatusTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerComplaintStatusType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerComplaintStatusTypeDTO result = customerComplaintStatusTypeService.save(customerComplaintStatusTypeDTO);
        return ResponseEntity
            .created(new URI("/api/customer-complaint-status-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-complaint-status-types/:id} : Updates an existing customerComplaintStatusType.
     *
     * @param id the id of the customerComplaintStatusTypeDTO to save.
     * @param customerComplaintStatusTypeDTO the customerComplaintStatusTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerComplaintStatusTypeDTO,
     * or with status {@code 400 (Bad Request)} if the customerComplaintStatusTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerComplaintStatusTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-complaint-status-types/{id}")
    public ResponseEntity<CustomerComplaintStatusTypeDTO> updateCustomerComplaintStatusType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerComplaintStatusType : {}, {}", id, customerComplaintStatusTypeDTO);
        if (customerComplaintStatusTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerComplaintStatusTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerComplaintStatusTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerComplaintStatusTypeDTO result = customerComplaintStatusTypeService.save(customerComplaintStatusTypeDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerComplaintStatusTypeDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /customer-complaint-status-types/:id} : Partial updates given fields of an existing customerComplaintStatusType, field will ignore if it is null
     *
     * @param id the id of the customerComplaintStatusTypeDTO to save.
     * @param customerComplaintStatusTypeDTO the customerComplaintStatusTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerComplaintStatusTypeDTO,
     * or with status {@code 400 (Bad Request)} if the customerComplaintStatusTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerComplaintStatusTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerComplaintStatusTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-complaint-status-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerComplaintStatusTypeDTO> partialUpdateCustomerComplaintStatusType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerComplaintStatusTypeDTO customerComplaintStatusTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerComplaintStatusType partially : {}, {}", id, customerComplaintStatusTypeDTO);
        if (customerComplaintStatusTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerComplaintStatusTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerComplaintStatusTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerComplaintStatusTypeDTO> result = customerComplaintStatusTypeService.partialUpdate(customerComplaintStatusTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerComplaintStatusTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-complaint-status-types} : get all the customerComplaintStatusTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerComplaintStatusTypes in body.
     */
    @GetMapping("/customer-complaint-status-types")
    public ResponseEntity<List<CustomerComplaintStatusTypeDTO>> getAllCustomerComplaintStatusTypes(
        CustomerComplaintStatusTypeCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get CustomerComplaintStatusTypes by criteria: {}", criteria);
        Page<CustomerComplaintStatusTypeDTO> page = customerComplaintStatusTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-complaint-status-types/count} : count all the customerComplaintStatusTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customer-complaint-status-types/count")
    public ResponseEntity<Long> countCustomerComplaintStatusTypes(CustomerComplaintStatusTypeCriteria criteria) {
        log.debug("REST request to count CustomerComplaintStatusTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerComplaintStatusTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-complaint-status-types/:id} : get the "id" customerComplaintStatusType.
     *
     * @param id the id of the customerComplaintStatusTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerComplaintStatusTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-complaint-status-types/{id}")
    public ResponseEntity<CustomerComplaintStatusTypeDTO> getCustomerComplaintStatusType(@PathVariable Long id) {
        log.debug("REST request to get CustomerComplaintStatusType : {}", id);
        Optional<CustomerComplaintStatusTypeDTO> customerComplaintStatusTypeDTO = customerComplaintStatusTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerComplaintStatusTypeDTO);
    }

    /**
     * {@code DELETE  /customer-complaint-status-types/:id} : delete the "id" customerComplaintStatusType.
     *
     * @param id the id of the customerComplaintStatusTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-complaint-status-types/{id}")
    public ResponseEntity<Void> deleteCustomerComplaintStatusType(@PathVariable Long id) {
        log.debug("REST request to delete CustomerComplaintStatusType : {}", id);
        customerComplaintStatusTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/customer-complaint-status-types?query=:query} : search for the customerComplaintStatusType corresponding
     * to the query.
     *
     * @param query the query of the customerComplaintStatusType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/customer-complaint-status-types")
    public ResponseEntity<List<CustomerComplaintStatusTypeDTO>> searchCustomerComplaintStatusTypes(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of CustomerComplaintStatusTypes for query {}", query);
        Page<CustomerComplaintStatusTypeDTO> page = customerComplaintStatusTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
