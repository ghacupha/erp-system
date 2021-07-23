package io.github.erp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import io.github.erp.domain.PaymentCategory;
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.repository.PaymentCategoryRepository;
import io.github.erp.repository.search.PaymentCategorySearchRepository;
import io.github.erp.service.dto.PaymentCategoryCriteria;
import io.github.erp.service.dto.PaymentCategoryDTO;
import io.github.erp.service.mapper.PaymentCategoryMapper;

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

    public PaymentCategoryQueryService(PaymentCategoryRepository paymentCategoryRepository, PaymentCategoryMapper paymentCategoryMapper, PaymentCategorySearchRepository paymentCategorySearchRepository) {
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
        return paymentCategoryRepository.findAll(specification, page)
            .map(paymentCategoryMapper::toDto);
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
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PaymentCategory_.id));
            }
            if (criteria.getCategoryName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategoryName(), PaymentCategory_.categoryName));
            }
            if (criteria.getCategoryDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategoryDescription(), PaymentCategory_.categoryDescription));
            }
            if (criteria.getCategoryType() != null) {
                specification = specification.and(buildSpecification(criteria.getCategoryType(), PaymentCategory_.categoryType));
            }
        }
        return specification;
    }
}
