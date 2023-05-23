package io.github.erp.web.rest;

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.repository.PaymentInvoiceRepository;
import io.github.erp.service.PaymentInvoiceQueryService;
import io.github.erp.service.PaymentInvoiceService;
import io.github.erp.service.criteria.PaymentInvoiceCriteria;
import io.github.erp.service.dto.PaymentInvoiceDTO;
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
 * REST controller for managing {@link io.github.erp.domain.PaymentInvoice}.
 */
@RestController
@RequestMapping("/api")
public class PaymentInvoiceResource {

    private final Logger log = LoggerFactory.getLogger(PaymentInvoiceResource.class);

    private static final String ENTITY_NAME = "paymentInvoice";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentInvoiceService paymentInvoiceService;

    private final PaymentInvoiceRepository paymentInvoiceRepository;

    private final PaymentInvoiceQueryService paymentInvoiceQueryService;

    public PaymentInvoiceResource(
        PaymentInvoiceService paymentInvoiceService,
        PaymentInvoiceRepository paymentInvoiceRepository,
        PaymentInvoiceQueryService paymentInvoiceQueryService
    ) {
        this.paymentInvoiceService = paymentInvoiceService;
        this.paymentInvoiceRepository = paymentInvoiceRepository;
        this.paymentInvoiceQueryService = paymentInvoiceQueryService;
    }

    /**
     * {@code POST  /payment-invoices} : Create a new paymentInvoice.
     *
     * @param paymentInvoiceDTO the paymentInvoiceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentInvoiceDTO, or with status {@code 400 (Bad Request)} if the paymentInvoice has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-invoices")
    public ResponseEntity<PaymentInvoiceDTO> createPaymentInvoice(@Valid @RequestBody PaymentInvoiceDTO paymentInvoiceDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaymentInvoice : {}", paymentInvoiceDTO);
        if (paymentInvoiceDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentInvoice cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentInvoiceDTO result = paymentInvoiceService.save(paymentInvoiceDTO);
        return ResponseEntity
            .created(new URI("/api/payment-invoices/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-invoices/:id} : Updates an existing paymentInvoice.
     *
     * @param id the id of the paymentInvoiceDTO to save.
     * @param paymentInvoiceDTO the paymentInvoiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentInvoiceDTO,
     * or with status {@code 400 (Bad Request)} if the paymentInvoiceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentInvoiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-invoices/{id}")
    public ResponseEntity<PaymentInvoiceDTO> updatePaymentInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentInvoiceDTO paymentInvoiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentInvoice : {}, {}", id, paymentInvoiceDTO);
        if (paymentInvoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentInvoiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentInvoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentInvoiceDTO result = paymentInvoiceService.save(paymentInvoiceDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentInvoiceDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-invoices/:id} : Partial updates given fields of an existing paymentInvoice, field will ignore if it is null
     *
     * @param id the id of the paymentInvoiceDTO to save.
     * @param paymentInvoiceDTO the paymentInvoiceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentInvoiceDTO,
     * or with status {@code 400 (Bad Request)} if the paymentInvoiceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentInvoiceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentInvoiceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-invoices/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentInvoiceDTO> partialUpdatePaymentInvoice(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentInvoiceDTO paymentInvoiceDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentInvoice partially : {}, {}", id, paymentInvoiceDTO);
        if (paymentInvoiceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentInvoiceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentInvoiceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentInvoiceDTO> result = paymentInvoiceService.partialUpdate(paymentInvoiceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentInvoiceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-invoices} : get all the paymentInvoices.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentInvoices in body.
     */
    @GetMapping("/payment-invoices")
    public ResponseEntity<List<PaymentInvoiceDTO>> getAllPaymentInvoices(PaymentInvoiceCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PaymentInvoices by criteria: {}", criteria);
        Page<PaymentInvoiceDTO> page = paymentInvoiceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-invoices/count} : count all the paymentInvoices.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/payment-invoices/count")
    public ResponseEntity<Long> countPaymentInvoices(PaymentInvoiceCriteria criteria) {
        log.debug("REST request to count PaymentInvoices by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentInvoiceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-invoices/:id} : get the "id" paymentInvoice.
     *
     * @param id the id of the paymentInvoiceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentInvoiceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-invoices/{id}")
    public ResponseEntity<PaymentInvoiceDTO> getPaymentInvoice(@PathVariable Long id) {
        log.debug("REST request to get PaymentInvoice : {}", id);
        Optional<PaymentInvoiceDTO> paymentInvoiceDTO = paymentInvoiceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentInvoiceDTO);
    }

    /**
     * {@code DELETE  /payment-invoices/:id} : delete the "id" paymentInvoice.
     *
     * @param id the id of the paymentInvoiceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-invoices/{id}")
    public ResponseEntity<Void> deletePaymentInvoice(@PathVariable Long id) {
        log.debug("REST request to delete PaymentInvoice : {}", id);
        paymentInvoiceService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/payment-invoices?query=:query} : search for the paymentInvoice corresponding
     * to the query.
     *
     * @param query the query of the paymentInvoice search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payment-invoices")
    public ResponseEntity<List<PaymentInvoiceDTO>> searchPaymentInvoices(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PaymentInvoices for query {}", query);
        Page<PaymentInvoiceDTO> page = paymentInvoiceService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
