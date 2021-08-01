package io.github.erp.service;

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.PaymentCalculation;
import io.github.erp.repository.PaymentCalculationRepository;
import io.github.erp.repository.search.PaymentCalculationSearchRepository;
import io.github.erp.service.criteria.PaymentCalculationCriteria;
import io.github.erp.service.dto.PaymentCalculationDTO;
import io.github.erp.service.mapper.PaymentCalculationMapper;
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
 * Service for executing complex queries for {@link PaymentCalculation} entities in the database.
 * The main input is a {@link PaymentCalculationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentCalculationDTO} or a {@link Page} of {@link PaymentCalculationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentCalculationQueryService extends QueryService<PaymentCalculation> {

    private final Logger log = LoggerFactory.getLogger(PaymentCalculationQueryService.class);

    private final PaymentCalculationRepository paymentCalculationRepository;

    private final PaymentCalculationMapper paymentCalculationMapper;

    private final PaymentCalculationSearchRepository paymentCalculationSearchRepository;

    public PaymentCalculationQueryService(
        PaymentCalculationRepository paymentCalculationRepository,
        PaymentCalculationMapper paymentCalculationMapper,
        PaymentCalculationSearchRepository paymentCalculationSearchRepository
    ) {
        this.paymentCalculationRepository = paymentCalculationRepository;
        this.paymentCalculationMapper = paymentCalculationMapper;
        this.paymentCalculationSearchRepository = paymentCalculationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentCalculationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentCalculationDTO> findByCriteria(PaymentCalculationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentCalculation> specification = createSpecification(criteria);
        return paymentCalculationMapper.toDto(paymentCalculationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentCalculationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentCalculationDTO> findByCriteria(PaymentCalculationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentCalculation> specification = createSpecification(criteria);
        return paymentCalculationRepository.findAll(specification, page).map(paymentCalculationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentCalculationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentCalculation> specification = createSpecification(criteria);
        return paymentCalculationRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentCalculationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentCalculation> createSpecification(PaymentCalculationCriteria criteria) {
        Specification<PaymentCalculation> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentCalculation_.id));
            }
            if (criteria.getPaymentNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentNumber(), PaymentCalculation_.paymentNumber));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), PaymentCalculation_.paymentDate));
            }
            if (criteria.getPaymentCategory() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPaymentCategory(), PaymentCalculation_.paymentCategory));
            }
            if (criteria.getPaymentExpense() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPaymentExpense(), PaymentCalculation_.paymentExpense));
            }
            if (criteria.getWithholdingVAT() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getWithholdingVAT(), PaymentCalculation_.withholdingVAT));
            }
            if (criteria.getWithholdingTax() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getWithholdingTax(), PaymentCalculation_.withholdingTax));
            }
            if (criteria.getPaymentAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentAmount(), PaymentCalculation_.paymentAmount));
            }
            if (criteria.getPaymentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentId(),
                            root -> root.join(PaymentCalculation_.payment, JoinType.LEFT).get(Payment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
