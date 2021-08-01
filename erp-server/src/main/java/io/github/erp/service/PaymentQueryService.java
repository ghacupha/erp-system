package io.github.erp.service;

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.Payment;
import io.github.erp.repository.PaymentRepository;
import io.github.erp.repository.search.PaymentSearchRepository;
import io.github.erp.service.criteria.PaymentCriteria;
import io.github.erp.service.dto.PaymentDTO;
import io.github.erp.service.mapper.PaymentMapper;
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
 * Service for executing complex queries for {@link Payment} entities in the database.
 * The main input is a {@link PaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentDTO} or a {@link Page} of {@link PaymentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentQueryService extends QueryService<Payment> {

    private final Logger log = LoggerFactory.getLogger(PaymentQueryService.class);

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper;

    private final PaymentSearchRepository paymentSearchRepository;

    public PaymentQueryService(
        PaymentRepository paymentRepository,
        PaymentMapper paymentMapper,
        PaymentSearchRepository paymentSearchRepository
    ) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.paymentSearchRepository = paymentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentDTO> findByCriteria(PaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentMapper.toDto(paymentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentDTO> findByCriteria(PaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.findAll(specification, page).map(paymentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Payment> specification = createSpecification(criteria);
        return paymentRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Payment> createSpecification(PaymentCriteria criteria) {
        Specification<Payment> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Payment_.id));
            }
            if (criteria.getPaymentNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentNumber(), Payment_.paymentNumber));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), Payment_.paymentDate));
            }
            if (criteria.getPaymentAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentAmount(), Payment_.paymentAmount));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Payment_.description));
            }
            if (criteria.getOwnedInvoiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOwnedInvoiceId(),
                            root -> root.join(Payment_.ownedInvoices, JoinType.LEFT).get(Invoice_.id)
                        )
                    );
            }
            if (criteria.getPaymentRequisitionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentRequisitionId(),
                            root -> root.join(Payment_.paymentRequisition, JoinType.LEFT).get(PaymentRequisition_.id)
                        )
                    );
            }
            if (criteria.getDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDealerId(), root -> root.join(Payment_.dealers, JoinType.LEFT).get(Dealer_.id))
                    );
            }
            if (criteria.getTaxRuleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTaxRuleId(), root -> root.join(Payment_.taxRule, JoinType.LEFT).get(TaxRule_.id))
                    );
            }
            if (criteria.getPaymentCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentCategoryId(),
                            root -> root.join(Payment_.paymentCategory, JoinType.LEFT).get(PaymentCategory_.id)
                        )
                    );
            }
            if (criteria.getPaymentCalculationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentCalculationId(),
                            root -> root.join(Payment_.paymentCalculation, JoinType.LEFT).get(PaymentCalculation_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
