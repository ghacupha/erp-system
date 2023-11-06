package io.github.erp.web.rest;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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

import io.github.erp.repository.CustomerTypeRepository;
import io.github.erp.service.CustomerTypeQueryService;
import io.github.erp.service.CustomerTypeService;
import io.github.erp.service.criteria.CustomerTypeCriteria;
import io.github.erp.service.dto.CustomerTypeDTO;
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
 * REST controller for managing {@link io.github.erp.domain.CustomerType}.
 */
@RestController
@RequestMapping("/api")
public class CustomerTypeResource {

    private final Logger log = LoggerFactory.getLogger(CustomerTypeResource.class);

    private static final String ENTITY_NAME = "customerType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CustomerTypeService customerTypeService;

    private final CustomerTypeRepository customerTypeRepository;

    private final CustomerTypeQueryService customerTypeQueryService;

    public CustomerTypeResource(
        CustomerTypeService customerTypeService,
        CustomerTypeRepository customerTypeRepository,
        CustomerTypeQueryService customerTypeQueryService
    ) {
        this.customerTypeService = customerTypeService;
        this.customerTypeRepository = customerTypeRepository;
        this.customerTypeQueryService = customerTypeQueryService;
    }

    /**
     * {@code POST  /customer-types} : Create a new customerType.
     *
     * @param customerTypeDTO the customerTypeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new customerTypeDTO, or with status {@code 400 (Bad Request)} if the customerType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/customer-types")
    public ResponseEntity<CustomerTypeDTO> createCustomerType(@Valid @RequestBody CustomerTypeDTO customerTypeDTO)
        throws URISyntaxException {
        log.debug("REST request to save CustomerType : {}", customerTypeDTO);
        if (customerTypeDTO.getId() != null) {
            throw new BadRequestAlertException("A new customerType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CustomerTypeDTO result = customerTypeService.save(customerTypeDTO);
        return ResponseEntity
            .created(new URI("/api/customer-types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /customer-types/:id} : Updates an existing customerType.
     *
     * @param id the id of the customerTypeDTO to save.
     * @param customerTypeDTO the customerTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerTypeDTO,
     * or with status {@code 400 (Bad Request)} if the customerTypeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the customerTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/customer-types/{id}")
    public ResponseEntity<CustomerTypeDTO> updateCustomerType(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CustomerTypeDTO customerTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update CustomerType : {}, {}", id, customerTypeDTO);
        if (customerTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CustomerTypeDTO result = customerTypeService.save(customerTypeDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerTypeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /customer-types/:id} : Partial updates given fields of an existing customerType, field will ignore if it is null
     *
     * @param id the id of the customerTypeDTO to save.
     * @param customerTypeDTO the customerTypeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated customerTypeDTO,
     * or with status {@code 400 (Bad Request)} if the customerTypeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the customerTypeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the customerTypeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/customer-types/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<CustomerTypeDTO> partialUpdateCustomerType(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CustomerTypeDTO customerTypeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update CustomerType partially : {}, {}", id, customerTypeDTO);
        if (customerTypeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, customerTypeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!customerTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CustomerTypeDTO> result = customerTypeService.partialUpdate(customerTypeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, customerTypeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /customer-types} : get all the customerTypes.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customerTypes in body.
     */
    @GetMapping("/customer-types")
    public ResponseEntity<List<CustomerTypeDTO>> getAllCustomerTypes(CustomerTypeCriteria criteria, Pageable pageable) {
        log.debug("REST request to get CustomerTypes by criteria: {}", criteria);
        Page<CustomerTypeDTO> page = customerTypeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /customer-types/count} : count all the customerTypes.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/customer-types/count")
    public ResponseEntity<Long> countCustomerTypes(CustomerTypeCriteria criteria) {
        log.debug("REST request to count CustomerTypes by criteria: {}", criteria);
        return ResponseEntity.ok().body(customerTypeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /customer-types/:id} : get the "id" customerType.
     *
     * @param id the id of the customerTypeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the customerTypeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/customer-types/{id}")
    public ResponseEntity<CustomerTypeDTO> getCustomerType(@PathVariable Long id) {
        log.debug("REST request to get CustomerType : {}", id);
        Optional<CustomerTypeDTO> customerTypeDTO = customerTypeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(customerTypeDTO);
    }

    /**
     * {@code DELETE  /customer-types/:id} : delete the "id" customerType.
     *
     * @param id the id of the customerTypeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/customer-types/{id}")
    public ResponseEntity<Void> deleteCustomerType(@PathVariable Long id) {
        log.debug("REST request to delete CustomerType : {}", id);
        customerTypeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/customer-types?query=:query} : search for the customerType corresponding
     * to the query.
     *
     * @param query the query of the customerType search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/customer-types")
    public ResponseEntity<List<CustomerTypeDTO>> searchCustomerTypes(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of CustomerTypes for query {}", query);
        Page<CustomerTypeDTO> page = customerTypeService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
