package io.github.erp.web.rest;

import io.github.erp.service.PaymentCalculationService;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import io.github.erp.service.dto.PaymentCalculationDTO;
import io.github.erp.service.dto.PaymentCalculationCriteria;
import io.github.erp.service.PaymentCalculationQueryService;

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
 * REST controller for managing {@link io.github.erp.domain.PaymentCalculation}.
 */
@RestController
@RequestMapping("/api")
public class PaymentCalculationResource {

    private final Logger log = LoggerFactory.getLogger(PaymentCalculationResource.class);

    private static final String ENTITY_NAME = "erpServicePaymentCalculation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentCalculationService paymentCalculationService;

    private final PaymentCalculationQueryService paymentCalculationQueryService;

    public PaymentCalculationResource(PaymentCalculationService paymentCalculationService, PaymentCalculationQueryService paymentCalculationQueryService) {
        this.paymentCalculationService = paymentCalculationService;
        this.paymentCalculationQueryService = paymentCalculationQueryService;
    }

    /**
     * {@code POST  /payment-calculations} : Create a new paymentCalculation.
     *
     * @param paymentCalculationDTO the paymentCalculationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentCalculationDTO, or with status {@code 400 (Bad Request)} if the paymentCalculation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-calculations")
    public ResponseEntity<PaymentCalculationDTO> createPaymentCalculation(@RequestBody PaymentCalculationDTO paymentCalculationDTO) throws URISyntaxException {
        log.debug("REST request to save PaymentCalculation : {}", paymentCalculationDTO);
        if (paymentCalculationDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentCalculation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentCalculationDTO result = paymentCalculationService.save(paymentCalculationDTO);
        return ResponseEntity.created(new URI("/api/payment-calculations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-calculations} : Updates an existing paymentCalculation.
     *
     * @param paymentCalculationDTO the paymentCalculationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentCalculationDTO,
     * or with status {@code 400 (Bad Request)} if the paymentCalculationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentCalculationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-calculations")
    public ResponseEntity<PaymentCalculationDTO> updatePaymentCalculation(@RequestBody PaymentCalculationDTO paymentCalculationDTO) throws URISyntaxException {
        log.debug("REST request to update PaymentCalculation : {}", paymentCalculationDTO);
        if (paymentCalculationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PaymentCalculationDTO result = paymentCalculationService.save(paymentCalculationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentCalculationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /payment-calculations} : get all the paymentCalculations.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentCalculations in body.
     */
    @GetMapping("/payment-calculations")
    public ResponseEntity<List<PaymentCalculationDTO>> getAllPaymentCalculations(PaymentCalculationCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PaymentCalculations by criteria: {}", criteria);
        Page<PaymentCalculationDTO> page = paymentCalculationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-calculations/count} : count all the paymentCalculations.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/payment-calculations/count")
    public ResponseEntity<Long> countPaymentCalculations(PaymentCalculationCriteria criteria) {
        log.debug("REST request to count PaymentCalculations by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentCalculationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-calculations/:id} : get the "id" paymentCalculation.
     *
     * @param id the id of the paymentCalculationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentCalculationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-calculations/{id}")
    public ResponseEntity<PaymentCalculationDTO> getPaymentCalculation(@PathVariable Long id) {
        log.debug("REST request to get PaymentCalculation : {}", id);
        Optional<PaymentCalculationDTO> paymentCalculationDTO = paymentCalculationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentCalculationDTO);
    }

    /**
     * {@code DELETE  /payment-calculations/:id} : delete the "id" paymentCalculation.
     *
     * @param id the id of the paymentCalculationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-calculations/{id}")
    public ResponseEntity<Void> deletePaymentCalculation(@PathVariable Long id) {
        log.debug("REST request to delete PaymentCalculation : {}", id);
        paymentCalculationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/payment-calculations?query=:query} : search for the paymentCalculation corresponding
     * to the query.
     *
     * @param query the query of the paymentCalculation search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payment-calculations")
    public ResponseEntity<List<PaymentCalculationDTO>> searchPaymentCalculations(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PaymentCalculations for query {}", query);
        Page<PaymentCalculationDTO> page = paymentCalculationService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
        }
}
