package io.github.erp.web.rest.api;

import io.github.erp.repository.PaymentCalculationRepository;
import io.github.erp.service.PaymentCalculationQueryService;
import io.github.erp.service.PaymentCalculationService;
import io.github.erp.service.criteria.PaymentCalculationCriteria;
import io.github.erp.service.dto.PaymentCalculationDTO;
import io.github.erp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link io.github.erp.domain.PaymentCalculation}.
 */
@RestController("DevPaymentCalculationResource")
@RequestMapping("/api/dev")
public class PaymentCalculationResource {

    private final Logger log = LoggerFactory.getLogger(PaymentCalculationResource.class);

    private static final String ENTITY_NAME = "paymentsPaymentCalculation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentCalculationService paymentCalculationService;

    private final PaymentCalculationRepository paymentCalculationRepository;

    private final PaymentCalculationQueryService paymentCalculationQueryService;

    public PaymentCalculationResource(
        PaymentCalculationService paymentCalculationService,
        PaymentCalculationRepository paymentCalculationRepository,
        PaymentCalculationQueryService paymentCalculationQueryService
    ) {
        this.paymentCalculationService = paymentCalculationService;
        this.paymentCalculationRepository = paymentCalculationRepository;
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
    public ResponseEntity<PaymentCalculationDTO> createPaymentCalculation(@RequestBody PaymentCalculationDTO paymentCalculationDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaymentCalculation : {}", paymentCalculationDTO);
        if (paymentCalculationDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentCalculation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentCalculationDTO result = paymentCalculationService.save(paymentCalculationDTO);
        return ResponseEntity
            .created(new URI("/api/payment-calculations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-calculations/:id} : Updates an existing paymentCalculation.
     *
     * @param id the id of the paymentCalculationDTO to save.
     * @param paymentCalculationDTO the paymentCalculationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentCalculationDTO,
     * or with status {@code 400 (Bad Request)} if the paymentCalculationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentCalculationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-calculations/{id}")
    public ResponseEntity<PaymentCalculationDTO> updatePaymentCalculation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentCalculationDTO paymentCalculationDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentCalculation : {}, {}", id, paymentCalculationDTO);
        if (paymentCalculationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCalculationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentCalculationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentCalculationDTO result = paymentCalculationService.save(paymentCalculationDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentCalculationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-calculations/:id} : Partial updates given fields of an existing paymentCalculation, field will ignore if it is null
     *
     * @param id the id of the paymentCalculationDTO to save.
     * @param paymentCalculationDTO the paymentCalculationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentCalculationDTO,
     * or with status {@code 400 (Bad Request)} if the paymentCalculationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentCalculationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentCalculationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-calculations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentCalculationDTO> partialUpdatePaymentCalculation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PaymentCalculationDTO paymentCalculationDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentCalculation partially : {}, {}", id, paymentCalculationDTO);
        if (paymentCalculationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCalculationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentCalculationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentCalculationDTO> result = paymentCalculationService.partialUpdate(paymentCalculationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentCalculationDTO.getId().toString())
        );
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
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
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
