package io.github.erp.erp.resources;

/*-
 * Erp System - Mark II No 7 (Artaxerxes Series)
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.repository.ServiceOutletRepository;
import io.github.erp.service.ServiceOutletQueryService;
import io.github.erp.service.ServiceOutletService;
import io.github.erp.service.criteria.ServiceOutletCriteria;
import io.github.erp.service.dto.ServiceOutletDTO;
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
 * REST controller for managing {@link io.github.erp.domain.ServiceOutlet}.
 */
@RestController
@RequestMapping("/api/granular-data")
public class ServiceOutletResource {

    private final Logger log = LoggerFactory.getLogger(ServiceOutletResource.class);

    private static final String ENTITY_NAME = "serviceOutlet";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceOutletService serviceOutletService;

    private final ServiceOutletRepository serviceOutletRepository;

    private final ServiceOutletQueryService serviceOutletQueryService;

    public ServiceOutletResource(
        ServiceOutletService serviceOutletService,
        ServiceOutletRepository serviceOutletRepository,
        ServiceOutletQueryService serviceOutletQueryService
    ) {
        this.serviceOutletService = serviceOutletService;
        this.serviceOutletRepository = serviceOutletRepository;
        this.serviceOutletQueryService = serviceOutletQueryService;
    }

    /**
     * {@code POST  /service-outlets} : Create a new serviceOutlet.
     *
     * @param serviceOutletDTO the serviceOutletDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceOutletDTO, or with status {@code 400 (Bad Request)} if the serviceOutlet has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/service-outlets")
    public ResponseEntity<ServiceOutletDTO> createServiceOutlet(@Valid @RequestBody ServiceOutletDTO serviceOutletDTO)
        throws URISyntaxException {
        log.debug("REST request to save ServiceOutlet : {}", serviceOutletDTO);
        if (serviceOutletDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceOutlet cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ServiceOutletDTO result = serviceOutletService.save(serviceOutletDTO);
        return ResponseEntity
            .created(new URI("/api/service-outlets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /service-outlets/:id} : Updates an existing serviceOutlet.
     *
     * @param id the id of the serviceOutletDTO to save.
     * @param serviceOutletDTO the serviceOutletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceOutletDTO,
     * or with status {@code 400 (Bad Request)} if the serviceOutletDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceOutletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/service-outlets/{id}")
    public ResponseEntity<ServiceOutletDTO> updateServiceOutlet(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceOutletDTO serviceOutletDTO
    ) throws URISyntaxException {
        log.debug("REST request to update ServiceOutlet : {}, {}", id, serviceOutletDTO);
        if (serviceOutletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceOutletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceOutletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ServiceOutletDTO result = serviceOutletService.save(serviceOutletDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceOutletDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /service-outlets/:id} : Partial updates given fields of an existing serviceOutlet, field will ignore if it is null
     *
     * @param id the id of the serviceOutletDTO to save.
     * @param serviceOutletDTO the serviceOutletDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceOutletDTO,
     * or with status {@code 400 (Bad Request)} if the serviceOutletDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceOutletDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceOutletDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/service-outlets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceOutletDTO> partialUpdateServiceOutlet(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceOutletDTO serviceOutletDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update ServiceOutlet partially : {}, {}", id, serviceOutletDTO);
        if (serviceOutletDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceOutletDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceOutletRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceOutletDTO> result = serviceOutletService.partialUpdate(serviceOutletDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, serviceOutletDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-outlets} : get all the serviceOutlets.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceOutlets in body.
     */
    @GetMapping("/service-outlets")
    public ResponseEntity<List<ServiceOutletDTO>> getAllServiceOutlets(ServiceOutletCriteria criteria, Pageable pageable) {
        log.debug("REST request to get ServiceOutlets by criteria: {}", criteria);
        Page<ServiceOutletDTO> page = serviceOutletQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-outlets/count} : count all the serviceOutlets.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/service-outlets/count")
    public ResponseEntity<Long> countServiceOutlets(ServiceOutletCriteria criteria) {
        log.debug("REST request to count ServiceOutlets by criteria: {}", criteria);
        return ResponseEntity.ok().body(serviceOutletQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /service-outlets/:id} : get the "id" serviceOutlet.
     *
     * @param id the id of the serviceOutletDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceOutletDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/service-outlets/{id}")
    public ResponseEntity<ServiceOutletDTO> getServiceOutlet(@PathVariable Long id) {
        log.debug("REST request to get ServiceOutlet : {}", id);
        Optional<ServiceOutletDTO> serviceOutletDTO = serviceOutletService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceOutletDTO);
    }

    /**
     * {@code DELETE  /service-outlets/:id} : delete the "id" serviceOutlet.
     *
     * @param id the id of the serviceOutletDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/service-outlets/{id}")
    public ResponseEntity<Void> deleteServiceOutlet(@PathVariable Long id) {
        log.debug("REST request to delete ServiceOutlet : {}", id);
        serviceOutletService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/service-outlets?query=:query} : search for the serviceOutlet corresponding
     * to the query.
     *
     * @param query the query of the serviceOutlet search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/service-outlets")
    public ResponseEntity<List<ServiceOutletDTO>> searchServiceOutlets(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of ServiceOutlets for query {}", query);
        Page<ServiceOutletDTO> page = serviceOutletService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
