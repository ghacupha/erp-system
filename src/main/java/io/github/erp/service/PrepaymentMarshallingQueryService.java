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
import io.github.erp.domain.PrepaymentMarshalling;
import io.github.erp.repository.PrepaymentMarshallingRepository;
import io.github.erp.repository.search.PrepaymentMarshallingSearchRepository;
import io.github.erp.service.criteria.PrepaymentMarshallingCriteria;
import io.github.erp.service.dto.PrepaymentMarshallingDTO;
import io.github.erp.service.mapper.PrepaymentMarshallingMapper;
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
 * Service for executing complex queries for {@link PrepaymentMarshalling} entities in the database.
 * The main input is a {@link PrepaymentMarshallingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentMarshallingDTO} or a {@link Page} of {@link PrepaymentMarshallingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentMarshallingQueryService extends QueryService<PrepaymentMarshalling> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentMarshallingQueryService.class);

    private final PrepaymentMarshallingRepository prepaymentMarshallingRepository;

    private final PrepaymentMarshallingMapper prepaymentMarshallingMapper;

    private final PrepaymentMarshallingSearchRepository prepaymentMarshallingSearchRepository;

    public PrepaymentMarshallingQueryService(
        PrepaymentMarshallingRepository prepaymentMarshallingRepository,
        PrepaymentMarshallingMapper prepaymentMarshallingMapper,
        PrepaymentMarshallingSearchRepository prepaymentMarshallingSearchRepository
    ) {
        this.prepaymentMarshallingRepository = prepaymentMarshallingRepository;
        this.prepaymentMarshallingMapper = prepaymentMarshallingMapper;
        this.prepaymentMarshallingSearchRepository = prepaymentMarshallingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentMarshallingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentMarshallingDTO> findByCriteria(PrepaymentMarshallingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrepaymentMarshalling> specification = createSpecification(criteria);
        return prepaymentMarshallingMapper.toDto(prepaymentMarshallingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentMarshallingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentMarshallingDTO> findByCriteria(PrepaymentMarshallingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrepaymentMarshalling> specification = createSpecification(criteria);
        return prepaymentMarshallingRepository.findAll(specification, page).map(prepaymentMarshallingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentMarshallingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrepaymentMarshalling> specification = createSpecification(criteria);
        return prepaymentMarshallingRepository.count(specification);
    }

    /**
     * Function to convert {@link PrepaymentMarshallingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrepaymentMarshalling> createSpecification(PrepaymentMarshallingCriteria criteria) {
        Specification<PrepaymentMarshalling> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrepaymentMarshalling_.id));
            }
            if (criteria.getInactive() != null) {
                specification = specification.and(buildSpecification(criteria.getInactive(), PrepaymentMarshalling_.inactive));
            }
            if (criteria.getAmortizationPeriods() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getAmortizationPeriods(), PrepaymentMarshalling_.amortizationPeriods)
                    );
            }
            if (criteria.getProcessed() != null) {
                specification = specification.and(buildSpecification(criteria.getProcessed(), PrepaymentMarshalling_.processed));
            }
            if (criteria.getCompilationToken() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getCompilationToken(), PrepaymentMarshalling_.compilationToken));
            }
            if (criteria.getPrepaymentAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrepaymentAccountId(),
                            root -> root.join(PrepaymentMarshalling_.prepaymentAccount, JoinType.LEFT).get(PrepaymentAccount_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(PrepaymentMarshalling_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getFirstFiscalMonthId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFirstFiscalMonthId(),
                            root -> root.join(PrepaymentMarshalling_.firstFiscalMonth, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
            if (criteria.getLastFiscalMonthId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastFiscalMonthId(),
                            root -> root.join(PrepaymentMarshalling_.lastFiscalMonth, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
