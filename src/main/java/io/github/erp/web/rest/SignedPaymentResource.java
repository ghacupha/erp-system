package io.github.erp.web.rest;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.SignedPaymentRepository;
import io.github.erp.service.SignedPaymentQueryService;
import io.github.erp.service.SignedPaymentService;
import io.github.erp.service.criteria.SignedPaymentCriteria;
import io.github.erp.service.dto.SignedPaymentDTO;
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
 * REST controller for managing {@link io.github.erp.domain.SignedPayment}.
 */
@RestController
@RequestMapping("/api")
public class SignedPaymentResource {

    private final Logger log = LoggerFactory.getLogger(SignedPaymentResource.class);

    private static final String ENTITY_NAME = "signedPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SignedPaymentService signedPaymentService;

    private final SignedPaymentRepository signedPaymentRepository;

    private final SignedPaymentQueryService signedPaymentQueryService;

    public SignedPaymentResource(
        SignedPaymentService signedPaymentService,
        SignedPaymentRepository signedPaymentRepository,
        SignedPaymentQueryService signedPaymentQueryService
    ) {
        this.signedPaymentService = signedPaymentService;
        this.signedPaymentRepository = signedPaymentRepository;
        this.signedPaymentQueryService = signedPaymentQueryService;
    }

    /**
     * {@code POST  /signed-payments} : Create a new signedPayment.
     *
     * @param signedPaymentDTO the signedPaymentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new signedPaymentDTO, or with status {@code 400 (Bad Request)} if the signedPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/signed-payments")
    public ResponseEntity<SignedPaymentDTO> createSignedPayment(@Valid @RequestBody SignedPaymentDTO signedPaymentDTO)
        throws URISyntaxException {
        log.debug("REST request to save SignedPayment : {}", signedPaymentDTO);
        if (signedPaymentDTO.getId() != null) {
            throw new BadRequestAlertException("A new signedPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SignedPaymentDTO result = signedPaymentService.save(signedPaymentDTO);
        return ResponseEntity
            .created(new URI("/api/signed-payments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /signed-payments/:id} : Updates an existing signedPayment.
     *
     * @param id the id of the signedPaymentDTO to save.
     * @param signedPaymentDTO the signedPaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signedPaymentDTO,
     * or with status {@code 400 (Bad Request)} if the signedPaymentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the signedPaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/signed-payments/{id}")
    public ResponseEntity<SignedPaymentDTO> updateSignedPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SignedPaymentDTO signedPaymentDTO
    ) throws URISyntaxException {
        log.debug("REST request to update SignedPayment : {}, {}", id, signedPaymentDTO);
        if (signedPaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signedPaymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signedPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        SignedPaymentDTO result = signedPaymentService.save(signedPaymentDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signedPaymentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /signed-payments/:id} : Partial updates given fields of an existing signedPayment, field will ignore if it is null
     *
     * @param id the id of the signedPaymentDTO to save.
     * @param signedPaymentDTO the signedPaymentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated signedPaymentDTO,
     * or with status {@code 400 (Bad Request)} if the signedPaymentDTO is not valid,
     * or with status {@code 404 (Not Found)} if the signedPaymentDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the signedPaymentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/signed-payments/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SignedPaymentDTO> partialUpdateSignedPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SignedPaymentDTO signedPaymentDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update SignedPayment partially : {}, {}", id, signedPaymentDTO);
        if (signedPaymentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, signedPaymentDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!signedPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SignedPaymentDTO> result = signedPaymentService.partialUpdate(signedPaymentDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, signedPaymentDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /signed-payments} : get all the signedPayments.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of signedPayments in body.
     */
    @GetMapping("/signed-payments")
    public ResponseEntity<List<SignedPaymentDTO>> getAllSignedPayments(SignedPaymentCriteria criteria, Pageable pageable) {
        log.debug("REST request to get SignedPayments by criteria: {}", criteria);
        Page<SignedPaymentDTO> page = signedPaymentQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /signed-payments/count} : count all the signedPayments.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/signed-payments/count")
    public ResponseEntity<Long> countSignedPayments(SignedPaymentCriteria criteria) {
        log.debug("REST request to count SignedPayments by criteria: {}", criteria);
        return ResponseEntity.ok().body(signedPaymentQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /signed-payments/:id} : get the "id" signedPayment.
     *
     * @param id the id of the signedPaymentDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the signedPaymentDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/signed-payments/{id}")
    public ResponseEntity<SignedPaymentDTO> getSignedPayment(@PathVariable Long id) {
        log.debug("REST request to get SignedPayment : {}", id);
        Optional<SignedPaymentDTO> signedPaymentDTO = signedPaymentService.findOne(id);
        return ResponseUtil.wrapOrNotFound(signedPaymentDTO);
    }

    /**
     * {@code DELETE  /signed-payments/:id} : delete the "id" signedPayment.
     *
     * @param id the id of the signedPaymentDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/signed-payments/{id}")
    public ResponseEntity<Void> deleteSignedPayment(@PathVariable Long id) {
        log.debug("REST request to delete SignedPayment : {}", id);
        signedPaymentService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/signed-payments?query=:query} : search for the signedPayment corresponding
     * to the query.
     *
     * @param query the query of the signedPayment search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/signed-payments")
    public ResponseEntity<List<SignedPaymentDTO>> searchSignedPayments(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of SignedPayments for query {}", query);
        Page<SignedPaymentDTO> page = signedPaymentService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
