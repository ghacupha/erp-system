package io.github.erp.web.rest;

/*-
 * Copyright © 2021 Edwin Njeru (mailnjeru@gmail.com)
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

import io.github.erp.service.PaymentRequisitionService;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import io.github.erp.service.dto.PaymentRequisitionDTO;
import io.github.erp.service.dto.PaymentRequisitionCriteria;
import io.github.erp.service.PaymentRequisitionQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link io.github.erp.domain.PaymentRequisition}.
 */
@RestController
@RequestMapping("/api")
public class PaymentRequisitionResource {

    private final Logger log = LoggerFactory.getLogger(PaymentRequisitionResource.class);

    private static final String ENTITY_NAME = "paymentsPaymentRequisition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentRequisitionService paymentRequisitionService;

    private final PaymentRequisitionQueryService paymentRequisitionQueryService;

    public PaymentRequisitionResource(PaymentRequisitionService paymentRequisitionService, PaymentRequisitionQueryService paymentRequisitionQueryService) {
        this.paymentRequisitionService = paymentRequisitionService;
        this.paymentRequisitionQueryService = paymentRequisitionQueryService;
    }

    /**
     * {@code POST  /payment-requisitions} : Create a new paymentRequisition.
     *
     * @param paymentRequisitionDTO the paymentRequisitionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentRequisitionDTO, or with status {@code 400 (Bad Request)} if the paymentRequisition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-requisitions")
    public ResponseEntity<PaymentRequisitionDTO> createPaymentRequisition(@RequestBody PaymentRequisitionDTO paymentRequisitionDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentRequisition : {}", paymentRequisitionDTO);
        if (paymentRequisitionDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentRequisition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentRequisitionDTO result = paymentRequisitionService.save(paymentRequisitionDTO);
        return ResponseEntity.created(new URI("/api/payment-requisitions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-requisitions} : Updates an existing paymentRequisition.
     *
     * @param paymentRequisitionDTO the paymentRequisitionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentRequisitionDTO,
     * or with status {@code 400 (Bad Request)} if the paymentRequisitionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentRequisitionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-requisitions")
    public ResponseEntity<PaymentRequisitionDTO> updatePaymentRequisition(@RequestBody PaymentRequisitionDTO paymentRequisitionDTO) throws URISyntaxException {
        log.debug("REST request to update PaymentRequisition : {}", paymentRequisitionDTO);
        if (paymentRequisitionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentRequisitionDTO result = paymentRequisitionService.save(paymentRequisitionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentRequisitionDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payment-requisitions} : get all the paymentRequisitions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentRequisitions in body.
     */
    @GetMapping("/payment-requisitions")
    public ResponseEntity<List<PaymentRequisitionDTO>> getAllPaymentRequisitions(PaymentRequisitionCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PaymentRequisitions by criteria: {}", criteria);
        Page<PaymentRequisitionDTO> page = paymentRequisitionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-requisitions/count} : count all the paymentRequisitions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/payment-requisitions/count")
    public ResponseEntity<Long> countPaymentRequisitions(PaymentRequisitionCriteria criteria) {
        log.debug("REST request to count PaymentRequisitions by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentRequisitionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-requisitions/:id} : get the "id" paymentRequisition.
     *
     * @param id the id of the paymentRequisitionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentRequisitionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-requisitions/{id}")
    public ResponseEntity<PaymentRequisitionDTO> getPaymentRequisition(@PathVariable Long id) {
        log.debug("REST request to get PaymentRequisition : {}", id);
        Optional<PaymentRequisitionDTO> paymentRequisitionDTO = paymentRequisitionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentRequisitionDTO);
    }

    /**
     * {@code DELETE  /payment-requisitions/:id} : delete the "id" paymentRequisition.
     *
     * @param id the id of the paymentRequisitionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-requisitions/{id}")
    public ResponseEntity<Void> deletePaymentRequisition(@PathVariable Long id) {
        log.debug("REST request to delete PaymentRequisition : {}", id);
        paymentRequisitionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/payment-requisitions?query=:query} : search for the paymentRequisition corresponding
     * to the query.
     *
     * @param query the query of the paymentRequisition search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payment-requisitions")
    public ResponseEntity<List<PaymentRequisitionDTO>> searchPaymentRequisitions(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PaymentRequisitions for query {}", query);
        Page<PaymentRequisitionDTO> page = paymentRequisitionService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
