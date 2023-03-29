package io.github.erp.service;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.2-SNAPSHOT
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
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.PaymentCategory;
import io.github.erp.repository.PaymentCategoryRepository;
import io.github.erp.repository.search.PaymentCategorySearchRepository;
import io.github.erp.service.criteria.PaymentCategoryCriteria;
import io.github.erp.service.dto.PaymentCategoryDTO;
import io.github.erp.service.mapper.PaymentCategoryMapper;
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
 * Service for executing complex queries for {@link PaymentCategory} entities in the database.
 * The main input is a {@link PaymentCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PaymentCategoryDTO} or a {@link Page} of {@link PaymentCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PaymentCategoryQueryService extends QueryService<PaymentCategory> {

    private final Logger log = LoggerFactory.getLogger(PaymentCategoryQueryService.class);

    private final PaymentCategoryRepository paymentCategoryRepository;

    private final PaymentCategoryMapper paymentCategoryMapper;

    private final PaymentCategorySearchRepository paymentCategorySearchRepository;

    public PaymentCategoryQueryService(
        PaymentCategoryRepository paymentCategoryRepository,
        PaymentCategoryMapper paymentCategoryMapper,
        PaymentCategorySearchRepository paymentCategorySearchRepository
    ) {
        this.paymentCategoryRepository = paymentCategoryRepository;
        this.paymentCategoryMapper = paymentCategoryMapper;
        this.paymentCategorySearchRepository = paymentCategorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link PaymentCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PaymentCategoryDTO> findByCriteria(PaymentCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PaymentCategory> specification = createSpecification(criteria);
        return paymentCategoryMapper.toDto(paymentCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PaymentCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PaymentCategoryDTO> findByCriteria(PaymentCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PaymentCategory> specification = createSpecification(criteria);
        return paymentCategoryRepository.findAll(specification, page).map(paymentCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PaymentCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PaymentCategory> specification = createSpecification(criteria);
        return paymentCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link PaymentCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PaymentCategory> createSpecification(PaymentCategoryCriteria criteria) {
        Specification<PaymentCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentCategory_.id));
            }
            if (criteria.getCategoryName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategoryName(), PaymentCategory_.categoryName));
            }
            if (criteria.getCategoryDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCategoryDescription(), PaymentCategory_.categoryDescription));
            }
            if (criteria.getCategoryType() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryType(), PaymentCategory_.categoryType));
            }
            if (criteria.getFileUploadToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileUploadToken(), PaymentCategory_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCompilationToken(), PaymentCategory_.compilationToken));
            }
            if (criteria.getPaymentLabelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentLabelId(),
                            root -> root.join(PaymentCategory_.paymentLabels, JoinType.LEFT).get(PaymentLabel_.id)
                        )
                    );
            }
            if (criteria.getPaymentCalculationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentCalculationId(),
                            root -> root.join(PaymentCategory_.paymentCalculations, JoinType.LEFT).get(PaymentCalculation_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(PaymentCategory_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
