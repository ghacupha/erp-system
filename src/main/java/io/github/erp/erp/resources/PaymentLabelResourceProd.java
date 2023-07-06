package io.github.erp.erp.resources;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.1
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
import io.github.erp.repository.PaymentLabelRepository;
import io.github.erp.service.PaymentLabelQueryService;
import io.github.erp.service.PaymentLabelService;
import io.github.erp.service.criteria.PaymentLabelCriteria;
import io.github.erp.service.dto.PaymentLabelDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
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

/**
 * REST controller for managing {@link io.github.erp.domain.PaymentLabel}.
 */
@RestController("PaymentLabelResourceProd")
@RequestMapping("/api/payments")
public class PaymentLabelResourceProd {

    private final Logger log = LoggerFactory.getLogger(PaymentLabelResourceProd.class);

    private static final String ENTITY_NAME = "paymentLabel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentLabelService paymentLabelService;

    private final PaymentLabelRepository paymentLabelRepository;

    private final PaymentLabelQueryService paymentLabelQueryService;

    public PaymentLabelResourceProd(
        PaymentLabelService paymentLabelService,
        PaymentLabelRepository paymentLabelRepository,
        PaymentLabelQueryService paymentLabelQueryService
    ) {
        this.paymentLabelService = paymentLabelService;
        this.paymentLabelRepository = paymentLabelRepository;
        this.paymentLabelQueryService = paymentLabelQueryService;
    }

    /**
     * {@code POST  /payment-labels} : Create a new paymentLabel.
     *
     * @param paymentLabelDTO the paymentLabelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentLabelDTO, or with status {@code 400 (Bad Request)} if the paymentLabel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-labels")
    public ResponseEntity<PaymentLabelDTO> createPaymentLabel(@Valid @RequestBody PaymentLabelDTO paymentLabelDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaymentLabel : {}", paymentLabelDTO);
        if (paymentLabelDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentLabel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentLabelDTO result = paymentLabelService.save(paymentLabelDTO);
        return ResponseEntity
            .created(new URI("/api/payment-labels/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-labels/:id} : Updates an existing paymentLabel.
     *
     * @param id the id of the paymentLabelDTO to save.
     * @param paymentLabelDTO the paymentLabelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentLabelDTO,
     * or with status {@code 400 (Bad Request)} if the paymentLabelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentLabelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-labels/{id}")
    public ResponseEntity<PaymentLabelDTO> updatePaymentLabel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentLabelDTO paymentLabelDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentLabel : {}, {}", id, paymentLabelDTO);
        if (paymentLabelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentLabelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentLabelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentLabelDTO result = paymentLabelService.save(paymentLabelDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentLabelDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-labels/:id} : Partial updates given fields of an existing paymentLabel, field will ignore if it is null
     *
     * @param id the id of the paymentLabelDTO to save.
     * @param paymentLabelDTO the paymentLabelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentLabelDTO,
     * or with status {@code 400 (Bad Request)} if the paymentLabelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentLabelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentLabelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-labels/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentLabelDTO> partialUpdatePaymentLabel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentLabelDTO paymentLabelDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentLabel partially : {}, {}", id, paymentLabelDTO);
        if (paymentLabelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentLabelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentLabelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentLabelDTO> result = paymentLabelService.partialUpdate(paymentLabelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentLabelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-labels} : get all the paymentLabels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentLabels in body.
     */
    @GetMapping("/payment-labels")
    public ResponseEntity<List<PaymentLabelDTO>> getAllPaymentLabels(PaymentLabelCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PaymentLabels by criteria: {}", criteria);
        Page<PaymentLabelDTO> page = paymentLabelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-labels/count} : count all the paymentLabels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/payment-labels/count")
    public ResponseEntity<Long> countPaymentLabels(PaymentLabelCriteria criteria) {
        log.debug("REST request to count PaymentLabels by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentLabelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-labels/:id} : get the "id" paymentLabel.
     *
     * @param id the id of the paymentLabelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentLabelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-labels/{id}")
    public ResponseEntity<PaymentLabelDTO> getPaymentLabel(@PathVariable Long id) {
        log.debug("REST request to get PaymentLabel : {}", id);
        Optional<PaymentLabelDTO> paymentLabelDTO = paymentLabelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentLabelDTO);
    }

    /**
     * {@code DELETE  /payment-labels/:id} : delete the "id" paymentLabel.
     *
     * @param id the id of the paymentLabelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-labels/{id}")
    public ResponseEntity<Void> deletePaymentLabel(@PathVariable Long id) {
        log.debug("REST request to delete PaymentLabel : {}", id);
        paymentLabelService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/payment-labels?query=:query} : search for the paymentLabel corresponding
     * to the query.
     *
     * @param query the query of the paymentLabel search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payment-labels")
    public ResponseEntity<List<PaymentLabelDTO>> searchPaymentLabels(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PaymentLabels for query {}", query);
        Page<PaymentLabelDTO> page = paymentLabelService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
