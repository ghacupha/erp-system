package io.github.erp.erp.resources.prepayments;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import com.hazelcast.map.IMap;
import io.github.erp.internal.service.InternalPrepaymentCompilationRequestService;
import io.github.erp.internal.service.PrepaymentCompilationService;
import io.github.erp.repository.PrepaymentCompilationRequestRepository;
import io.github.erp.service.PrepaymentCompilationRequestQueryService;
import io.github.erp.service.criteria.PrepaymentCompilationRequestCriteria;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link io.github.erp.domain.PrepaymentCompilationRequest}.
 */
@RestController("PrepaymentCompilationRequestResourceProd")
@RequestMapping("/api/prepayments")
public class PrepaymentCompilationRequestResourceProd {

    private final Logger log = LoggerFactory.getLogger(PrepaymentCompilationRequestResourceProd.class);

    private static final String ENTITY_NAME = "prepaymentCompilationRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    public final IMap<String, String> prepaymentsReportCache;

    private final PrepaymentCompilationService prepaymentCompilationService;

    private final InternalPrepaymentCompilationRequestService prepaymentCompilationRequestService;

    private final PrepaymentCompilationRequestRepository prepaymentCompilationRequestRepository;

    private final PrepaymentCompilationRequestQueryService prepaymentCompilationRequestQueryService;

    public PrepaymentCompilationRequestResourceProd(
        IMap<String, String> prepaymentsReportCache, PrepaymentCompilationService prepaymentCompilationService,
        InternalPrepaymentCompilationRequestService prepaymentCompilationRequestService,
        PrepaymentCompilationRequestRepository prepaymentCompilationRequestRepository,
        PrepaymentCompilationRequestQueryService prepaymentCompilationRequestQueryService
    ) {
        this.prepaymentsReportCache = prepaymentsReportCache;
        this.prepaymentCompilationService = prepaymentCompilationService;
        this.prepaymentCompilationRequestService = prepaymentCompilationRequestService;
        this.prepaymentCompilationRequestRepository = prepaymentCompilationRequestRepository;
        this.prepaymentCompilationRequestQueryService = prepaymentCompilationRequestQueryService;
    }

    /**
     * {@code POST  /prepayment-compilation-requests} : Create a new prepaymentCompilationRequest.
     *
     * @param prepaymentCompilationRequestDTO the prepaymentCompilationRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new prepaymentCompilationRequestDTO, or with status {@code 400 (Bad Request)} if the prepaymentCompilationRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/prepayment-compilation-requests")
    public ResponseEntity<PrepaymentCompilationRequestDTO> createPrepaymentCompilationRequest(
        @RequestBody PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to save PrepaymentCompilationRequest : {}", prepaymentCompilationRequestDTO);
        if (prepaymentCompilationRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new prepaymentCompilationRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PrepaymentCompilationRequestDTO result = prepaymentCompilationRequestService.save(prepaymentCompilationRequestDTO);

        compilationSequence(result);

        // reset report cache
        prepaymentsReportCache.clear();

        return ResponseEntity
            .created(new URI("/api/prepayment-compilation-requests/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    @Async
    void compilationSequence(PrepaymentCompilationRequestDTO result) {
        prepaymentCompilationService.compile(result);
    }

    /**
     * {@code PUT  /prepayment-compilation-requests/:id} : Updates an existing prepaymentCompilationRequest.
     *
     * @param id the id of the prepaymentCompilationRequestDTO to save.
     * @param prepaymentCompilationRequestDTO the prepaymentCompilationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentCompilationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentCompilationRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentCompilationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/prepayment-compilation-requests/{id}")
    public ResponseEntity<PrepaymentCompilationRequestDTO> updatePrepaymentCompilationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PrepaymentCompilationRequest : {}, {}", id, prepaymentCompilationRequestDTO);
        if (prepaymentCompilationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentCompilationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentCompilationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PrepaymentCompilationRequestDTO result = prepaymentCompilationRequestService.save(prepaymentCompilationRequestDTO);
        return ResponseEntity
            .ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prepaymentCompilationRequestDTO.getId().toString())
            )
            .body(result);
    }

    /**
     * {@code PATCH  /prepayment-compilation-requests/:id} : Partial updates given fields of an existing prepaymentCompilationRequest, field will ignore if it is null
     *
     * @param id the id of the prepaymentCompilationRequestDTO to save.
     * @param prepaymentCompilationRequestDTO the prepaymentCompilationRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated prepaymentCompilationRequestDTO,
     * or with status {@code 400 (Bad Request)} if the prepaymentCompilationRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the prepaymentCompilationRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the prepaymentCompilationRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/prepayment-compilation-requests/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PrepaymentCompilationRequestDTO> partialUpdatePrepaymentCompilationRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PrepaymentCompilationRequestDTO prepaymentCompilationRequestDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PrepaymentCompilationRequest partially : {}, {}", id, prepaymentCompilationRequestDTO);
        if (prepaymentCompilationRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, prepaymentCompilationRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!prepaymentCompilationRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PrepaymentCompilationRequestDTO> result = prepaymentCompilationRequestService.partialUpdate(
            prepaymentCompilationRequestDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, prepaymentCompilationRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /prepayment-compilation-requests} : get all the prepaymentCompilationRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of prepaymentCompilationRequests in body.
     */
    @GetMapping("/prepayment-compilation-requests")
    public ResponseEntity<List<PrepaymentCompilationRequestDTO>> getAllPrepaymentCompilationRequests(
        PrepaymentCompilationRequestCriteria criteria,
        Pageable pageable
    ) {
        log.debug("REST request to get PrepaymentCompilationRequests by criteria: {}", criteria);
        Page<PrepaymentCompilationRequestDTO> page = prepaymentCompilationRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /prepayment-compilation-requests/count} : count all the prepaymentCompilationRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/prepayment-compilation-requests/count")
    public ResponseEntity<Long> countPrepaymentCompilationRequests(PrepaymentCompilationRequestCriteria criteria) {
        log.debug("REST request to count PrepaymentCompilationRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(prepaymentCompilationRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /prepayment-compilation-requests/:id} : get the "id" prepaymentCompilationRequest.
     *
     * @param id the id of the prepaymentCompilationRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the prepaymentCompilationRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/prepayment-compilation-requests/{id}")
    public ResponseEntity<PrepaymentCompilationRequestDTO> getPrepaymentCompilationRequest(@PathVariable Long id) {
        log.debug("REST request to get PrepaymentCompilationRequest : {}", id);
        Optional<PrepaymentCompilationRequestDTO> prepaymentCompilationRequestDTO = prepaymentCompilationRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(prepaymentCompilationRequestDTO);
    }

    /**
     * {@code DELETE  /prepayment-compilation-requests/:id} : delete the "id" prepaymentCompilationRequest.
     *
     * @param id the id of the prepaymentCompilationRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/prepayment-compilation-requests/{id}")
    public ResponseEntity<Void> deletePrepaymentCompilationRequest(@PathVariable Long id) {
        log.debug("REST request to delete PrepaymentCompilationRequest : {}", id);
        prepaymentCompilationRequestService.delete(id);

        // reset report cache
        prepaymentsReportCache.clear();

        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/prepayment-compilation-requests?query=:query} : search for the prepaymentCompilationRequest corresponding
     * to the query.
     *
     * @param query the query of the prepaymentCompilationRequest search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/prepayment-compilation-requests")
    public ResponseEntity<List<PrepaymentCompilationRequestDTO>> searchPrepaymentCompilationRequests(
        @RequestParam String query,
        Pageable pageable
    ) {
        log.debug("REST request to search for a page of PrepaymentCompilationRequests for query {}", query);
        Page<PrepaymentCompilationRequestDTO> page = prepaymentCompilationRequestService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
