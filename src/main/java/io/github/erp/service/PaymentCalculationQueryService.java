package io.github.erp.service;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentCalculation_.id));
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
            if (criteria.getFileUploadToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileUploadToken(), PaymentCalculation_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCompilationToken(), PaymentCalculation_.compilationToken));
            }
            if (criteria.getPaymentLabelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentLabelId(),
                            root -> root.join(PaymentCalculation_.paymentLabels, JoinType.LEFT).get(PaymentLabel_.id)
                        )
                    );
            }
            if (criteria.getPaymentCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentCategoryId(),
                            root -> root.join(PaymentCalculation_.paymentCategory, JoinType.LEFT).get(PaymentCategory_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(PaymentCalculation_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
