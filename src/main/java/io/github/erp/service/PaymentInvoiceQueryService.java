package io.github.erp.service;

/*-
 * Erp System - Mark III No 10 (Caleb Series) Server ver 0.5.0
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
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.PaymentInvoice;
import io.github.erp.repository.PaymentInvoiceRepository;
import io.github.erp.repository.search.PaymentInvoiceSearchRepository;
import io.github.erp.service.criteria.PaymentInvoiceCriteria;
import io.github.erp.service.dto.PaymentInvoiceDTO;
import io.github.erp.service.mapper.PaymentInvoiceMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PaymentInvoice} entities in the database.
 * The main input is a {@link PaymentInvoiceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentInvoiceDTO} or a {@link Page} of {@link PaymentInvoiceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentInvoiceQueryService extends QueryService<PaymentInvoice> {

    private final Logger log = LoggerFactory.getLogger(PaymentInvoiceQueryService.class);

    private final PaymentInvoiceRepository paymentInvoiceRepository;

    private final PaymentInvoiceMapper paymentInvoiceMapper;

    private final PaymentInvoiceSearchRepository paymentInvoiceSearchRepository;

    public PaymentInvoiceQueryService(
        PaymentInvoiceRepository paymentInvoiceRepository,
        PaymentInvoiceMapper paymentInvoiceMapper,
        PaymentInvoiceSearchRepository paymentInvoiceSearchRepository
    ) {
        this.paymentInvoiceRepository = paymentInvoiceRepository;
        this.paymentInvoiceMapper = paymentInvoiceMapper;
        this.paymentInvoiceSearchRepository = paymentInvoiceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentInvoiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentInvoiceDTO> findByCriteria(PaymentInvoiceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentInvoice> specification = createSpecification(criteria);
        return paymentInvoiceMapper.toDto(paymentInvoiceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentInvoiceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentInvoiceDTO> findByCriteria(PaymentInvoiceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentInvoice> specification = createSpecification(criteria);
        return paymentInvoiceRepository.findAll(specification, page).map(paymentInvoiceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentInvoiceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentInvoice> specification = createSpecification(criteria);
        return paymentInvoiceRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentInvoiceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentInvoice> createSpecification(PaymentInvoiceCriteria criteria) {
        Specification<PaymentInvoice> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentInvoice_.id));
            }
            if (criteria.getInvoiceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getInvoiceNumber(), PaymentInvoice_.invoiceNumber));
            }
            if (criteria.getInvoiceDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInvoiceDate(), PaymentInvoice_.invoiceDate));
            }
            if (criteria.getInvoiceAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInvoiceAmount(), PaymentInvoice_.invoiceAmount));
            }
            if (criteria.getFileUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUploadToken(), PaymentInvoice_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCompilationToken(), PaymentInvoice_.compilationToken));
            }
            if (criteria.getPurchaseOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPurchaseOrderId(),
                            root -> root.join(PaymentInvoice_.purchaseOrders, JoinType.LEFT).get(PurchaseOrder_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(PaymentInvoice_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getPaymentLabelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentLabelId(),
                            root -> root.join(PaymentInvoice_.paymentLabels, JoinType.LEFT).get(PaymentLabel_.id)
                        )
                    );
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(PaymentInvoice_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getBillerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBillerId(), root -> root.join(PaymentInvoice_.biller, JoinType.LEFT).get(Dealer_.id))
                    );
            }
            if (criteria.getDeliveryNoteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeliveryNoteId(),
                            root -> root.join(PaymentInvoice_.deliveryNotes, JoinType.LEFT).get(DeliveryNote_.id)
                        )
                    );
            }
            if (criteria.getJobSheetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getJobSheetId(),
                            root -> root.join(PaymentInvoice_.jobSheets, JoinType.LEFT).get(JobSheet_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(PaymentInvoice_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
