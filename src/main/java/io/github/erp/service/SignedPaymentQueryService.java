package io.github.erp.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.SignedPayment;
import io.github.erp.repository.SignedPaymentRepository;
import io.github.erp.repository.search.SignedPaymentSearchRepository;
import io.github.erp.service.criteria.SignedPaymentCriteria;
import io.github.erp.service.dto.SignedPaymentDTO;
import io.github.erp.service.mapper.SignedPaymentMapper;
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
 * Service for executing complex queries for {@link SignedPayment} entities in the database.
 * The main input is a {@link SignedPaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SignedPaymentDTO} or a {@link Page} of {@link SignedPaymentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SignedPaymentQueryService extends QueryService<SignedPayment> {

    private final Logger log = LoggerFactory.getLogger(SignedPaymentQueryService.class);

    private final SignedPaymentRepository signedPaymentRepository;

    private final SignedPaymentMapper signedPaymentMapper;

    private final SignedPaymentSearchRepository signedPaymentSearchRepository;

    public SignedPaymentQueryService(
        SignedPaymentRepository signedPaymentRepository,
        SignedPaymentMapper signedPaymentMapper,
        SignedPaymentSearchRepository signedPaymentSearchRepository
    ) {
        this.signedPaymentRepository = signedPaymentRepository;
        this.signedPaymentMapper = signedPaymentMapper;
        this.signedPaymentSearchRepository = signedPaymentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SignedPaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SignedPaymentDTO> findByCriteria(SignedPaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SignedPayment> specification = createSpecification(criteria);
        return signedPaymentMapper.toDto(signedPaymentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SignedPaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SignedPaymentDTO> findByCriteria(SignedPaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SignedPayment> specification = createSpecification(criteria);
        return signedPaymentRepository.findAll(specification, page).map(signedPaymentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SignedPaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SignedPayment> specification = createSpecification(criteria);
        return signedPaymentRepository.count(specification);
    }

    /**
     * Function to convert {@link SignedPaymentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SignedPayment> createSpecification(SignedPaymentCriteria criteria) {
        Specification<SignedPayment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SignedPayment_.id));
            }
            if (criteria.getTransactionNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTransactionNumber(), SignedPayment_.transactionNumber));
            }
            if (criteria.getTransactionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTransactionDate(), SignedPayment_.transactionDate));
            }
            if (criteria.getTransactionCurrency() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getTransactionCurrency(), SignedPayment_.transactionCurrency));
            }
            if (criteria.getTransactionAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionAmount(), SignedPayment_.transactionAmount));
            }
            if (criteria.getDealerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDealerName(), SignedPayment_.dealerName));
            }
            if (criteria.getFileUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUploadToken(), SignedPayment_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCompilationToken(), SignedPayment_.compilationToken));
            }
            if (criteria.getPaymentLabelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentLabelId(),
                            root -> root.join(SignedPayment_.paymentLabels, JoinType.LEFT).get(PaymentLabel_.id)
                        )
                    );
            }
            if (criteria.getPaymentCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentCategoryId(),
                            root -> root.join(SignedPayment_.paymentCategory, JoinType.LEFT).get(PaymentCategory_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(SignedPayment_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getSignedPaymentGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSignedPaymentGroupId(),
                            root -> root.join(SignedPayment_.signedPaymentGroup, JoinType.LEFT).get(SignedPayment_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
