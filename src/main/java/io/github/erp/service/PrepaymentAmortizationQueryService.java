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
import io.github.erp.domain.PrepaymentAmortization;
import io.github.erp.repository.PrepaymentAmortizationRepository;
import io.github.erp.repository.search.PrepaymentAmortizationSearchRepository;
import io.github.erp.service.criteria.PrepaymentAmortizationCriteria;
import io.github.erp.service.dto.PrepaymentAmortizationDTO;
import io.github.erp.service.mapper.PrepaymentAmortizationMapper;
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
 * Service for executing complex queries for {@link PrepaymentAmortization} entities in the database.
 * The main input is a {@link PrepaymentAmortizationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentAmortizationDTO} or a {@link Page} of {@link PrepaymentAmortizationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentAmortizationQueryService extends QueryService<PrepaymentAmortization> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentAmortizationQueryService.class);

    private final PrepaymentAmortizationRepository prepaymentAmortizationRepository;

    private final PrepaymentAmortizationMapper prepaymentAmortizationMapper;

    private final PrepaymentAmortizationSearchRepository prepaymentAmortizationSearchRepository;

    public PrepaymentAmortizationQueryService(
        PrepaymentAmortizationRepository prepaymentAmortizationRepository,
        PrepaymentAmortizationMapper prepaymentAmortizationMapper,
        PrepaymentAmortizationSearchRepository prepaymentAmortizationSearchRepository
    ) {
        this.prepaymentAmortizationRepository = prepaymentAmortizationRepository;
        this.prepaymentAmortizationMapper = prepaymentAmortizationMapper;
        this.prepaymentAmortizationSearchRepository = prepaymentAmortizationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentAmortizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentAmortizationDTO> findByCriteria(PrepaymentAmortizationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrepaymentAmortization> specification = createSpecification(criteria);
        return prepaymentAmortizationMapper.toDto(prepaymentAmortizationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentAmortizationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentAmortizationDTO> findByCriteria(PrepaymentAmortizationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrepaymentAmortization> specification = createSpecification(criteria);
        return prepaymentAmortizationRepository.findAll(specification, page).map(prepaymentAmortizationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentAmortizationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrepaymentAmortization> specification = createSpecification(criteria);
        return prepaymentAmortizationRepository.count(specification);
    }

    /**
     * Function to convert {@link PrepaymentAmortizationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrepaymentAmortization> createSpecification(PrepaymentAmortizationCriteria criteria) {
        Specification<PrepaymentAmortization> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrepaymentAmortization_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), PrepaymentAmortization_.description));
            }
            if (criteria.getPrepaymentPeriod() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPrepaymentPeriod(), PrepaymentAmortization_.prepaymentPeriod));
            }
            if (criteria.getPrepaymentAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPrepaymentAmount(), PrepaymentAmortization_.prepaymentAmount));
            }
            if (criteria.getInactive() != null) {
                specification = specification.and(buildSpecification(criteria.getInactive(), PrepaymentAmortization_.inactive));
            }
            if (criteria.getPrepaymentAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrepaymentAccountId(),
                            root -> root.join(PrepaymentAmortization_.prepaymentAccount, JoinType.LEFT).get(PrepaymentAccount_.id)
                        )
                    );
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(PrepaymentAmortization_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getDebitAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDebitAccountId(),
                            root -> root.join(PrepaymentAmortization_.debitAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getCreditAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreditAccountId(),
                            root -> root.join(PrepaymentAmortization_.creditAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(PrepaymentAmortization_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getFiscalMonthId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalMonthId(),
                            root -> root.join(PrepaymentAmortization_.fiscalMonth, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
            if (criteria.getPrepaymentCompilationRequestId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrepaymentCompilationRequestId(),
                            root ->
                                root
                                    .join(PrepaymentAmortization_.prepaymentCompilationRequest, JoinType.LEFT)
                                    .get(PrepaymentCompilationRequest_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
