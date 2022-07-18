package io.github.erp.web.rest.api;

/*-
 * Erp System - Mark II No 20 (Baruch Series)
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.repository.PaymentCategoryRepository;
import io.github.erp.service.PaymentCategoryQueryService;
import io.github.erp.service.PaymentCategoryService;
import io.github.erp.service.criteria.PaymentCategoryCriteria;
import io.github.erp.service.dto.PaymentCategoryDTO;
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
 * REST controller for managing {@link io.github.erp.domain.PaymentCategory}.
 */
@RestController("DevPaymentCategoryResource")
@RequestMapping("/api/dev")
public class PaymentCategoryResource {

    private final Logger log = LoggerFactory.getLogger(PaymentCategoryResource.class);

    private static final String ENTITY_NAME = "paymentsPaymentCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PaymentCategoryService paymentCategoryService;

    private final PaymentCategoryRepository paymentCategoryRepository;

    private final PaymentCategoryQueryService paymentCategoryQueryService;

    public PaymentCategoryResource(
        PaymentCategoryService paymentCategoryService,
        PaymentCategoryRepository paymentCategoryRepository,
        PaymentCategoryQueryService paymentCategoryQueryService
    ) {
        this.paymentCategoryService = paymentCategoryService;
        this.paymentCategoryRepository = paymentCategoryRepository;
        this.paymentCategoryQueryService = paymentCategoryQueryService;
    }

    /**
     * {@code POST  /payment-categories} : Create a new paymentCategory.
     *
     * @param paymentCategoryDTO the paymentCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new paymentCategoryDTO, or with status {@code 400 (Bad Request)} if the paymentCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/payment-categories")
    public ResponseEntity<PaymentCategoryDTO> createPaymentCategory(@Valid @RequestBody PaymentCategoryDTO paymentCategoryDTO)
        throws URISyntaxException {
        log.debug("REST request to save PaymentCategory : {}", paymentCategoryDTO);
        if (paymentCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new paymentCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PaymentCategoryDTO result = paymentCategoryService.save(paymentCategoryDTO);
        return ResponseEntity
            .created(new URI("/api/payment-categories/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /payment-categories/:id} : Updates an existing paymentCategory.
     *
     * @param id the id of the paymentCategoryDTO to save.
     * @param paymentCategoryDTO the paymentCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the paymentCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the paymentCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/payment-categories/{id}")
    public ResponseEntity<PaymentCategoryDTO> updatePaymentCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PaymentCategoryDTO paymentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update PaymentCategory : {}, {}", id, paymentCategoryDTO);
        if (paymentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PaymentCategoryDTO result = paymentCategoryService.save(paymentCategoryDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentCategoryDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /payment-categories/:id} : Partial updates given fields of an existing paymentCategory, field will ignore if it is null
     *
     * @param id the id of the paymentCategoryDTO to save.
     * @param paymentCategoryDTO the paymentCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated paymentCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the paymentCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the paymentCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the paymentCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/payment-categories/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PaymentCategoryDTO> partialUpdatePaymentCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PaymentCategoryDTO paymentCategoryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update PaymentCategory partially : {}, {}", id, paymentCategoryDTO);
        if (paymentCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, paymentCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!paymentCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PaymentCategoryDTO> result = paymentCategoryService.partialUpdate(paymentCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, paymentCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /payment-categories} : get all the paymentCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of paymentCategories in body.
     */
    @GetMapping("/payment-categories")
    public ResponseEntity<List<PaymentCategoryDTO>> getAllPaymentCategories(PaymentCategoryCriteria criteria, Pageable pageable) {
        log.debug("REST request to get PaymentCategories by criteria: {}", criteria);
        Page<PaymentCategoryDTO> page = paymentCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /payment-categories/count} : count all the paymentCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/payment-categories/count")
    public ResponseEntity<Long> countPaymentCategories(PaymentCategoryCriteria criteria) {
        log.debug("REST request to count PaymentCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(paymentCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /payment-categories/:id} : get the "id" paymentCategory.
     *
     * @param id the id of the paymentCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the paymentCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/payment-categories/{id}")
    public ResponseEntity<PaymentCategoryDTO> getPaymentCategory(@PathVariable Long id) {
        log.debug("REST request to get PaymentCategory : {}", id);
        Optional<PaymentCategoryDTO> paymentCategoryDTO = paymentCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(paymentCategoryDTO);
    }

    /**
     * {@code DELETE  /payment-categories/:id} : delete the "id" paymentCategory.
     *
     * @param id the id of the paymentCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/payment-categories/{id}")
    public ResponseEntity<Void> deletePaymentCategory(@PathVariable Long id) {
        log.debug("REST request to delete PaymentCategory : {}", id);
        paymentCategoryService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /_search/payment-categories?query=:query} : search for the paymentCategory corresponding
     * to the query.
     *
     * @param query the query of the paymentCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search/payment-categories")
    public ResponseEntity<List<PaymentCategoryDTO>> searchPaymentCategories(@RequestParam String query, Pageable pageable) {
        log.debug("REST request to search for a page of PaymentCategories for query {}", query);
        Page<PaymentCategoryDTO> page = paymentCategoryService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }
}
