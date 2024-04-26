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
import io.github.erp.domain.MonthlyPrepaymentOutstandingReportItem;
import io.github.erp.repository.MonthlyPrepaymentOutstandingReportItemRepository;
import io.github.erp.repository.search.MonthlyPrepaymentOutstandingReportItemSearchRepository;
import io.github.erp.service.criteria.MonthlyPrepaymentOutstandingReportItemCriteria;
import io.github.erp.service.dto.MonthlyPrepaymentOutstandingReportItemDTO;
import io.github.erp.service.mapper.MonthlyPrepaymentOutstandingReportItemMapper;
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
 * Service for executing complex queries for {@link MonthlyPrepaymentOutstandingReportItem} entities in the database.
 * The main input is a {@link MonthlyPrepaymentOutstandingReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MonthlyPrepaymentOutstandingReportItemDTO} or a {@link Page} of {@link MonthlyPrepaymentOutstandingReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MonthlyPrepaymentOutstandingReportItemQueryService extends QueryService<MonthlyPrepaymentOutstandingReportItem> {

    private final Logger log = LoggerFactory.getLogger(MonthlyPrepaymentOutstandingReportItemQueryService.class);

    private final MonthlyPrepaymentOutstandingReportItemRepository monthlyPrepaymentOutstandingReportItemRepository;

    private final MonthlyPrepaymentOutstandingReportItemMapper monthlyPrepaymentOutstandingReportItemMapper;

    private final MonthlyPrepaymentOutstandingReportItemSearchRepository monthlyPrepaymentOutstandingReportItemSearchRepository;

    public MonthlyPrepaymentOutstandingReportItemQueryService(
        MonthlyPrepaymentOutstandingReportItemRepository monthlyPrepaymentOutstandingReportItemRepository,
        MonthlyPrepaymentOutstandingReportItemMapper monthlyPrepaymentOutstandingReportItemMapper,
        MonthlyPrepaymentOutstandingReportItemSearchRepository monthlyPrepaymentOutstandingReportItemSearchRepository
    ) {
        this.monthlyPrepaymentOutstandingReportItemRepository = monthlyPrepaymentOutstandingReportItemRepository;
        this.monthlyPrepaymentOutstandingReportItemMapper = monthlyPrepaymentOutstandingReportItemMapper;
        this.monthlyPrepaymentOutstandingReportItemSearchRepository = monthlyPrepaymentOutstandingReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link MonthlyPrepaymentOutstandingReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MonthlyPrepaymentOutstandingReportItemDTO> findByCriteria(MonthlyPrepaymentOutstandingReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MonthlyPrepaymentOutstandingReportItem> specification = createSpecification(criteria);
        return monthlyPrepaymentOutstandingReportItemMapper.toDto(monthlyPrepaymentOutstandingReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MonthlyPrepaymentOutstandingReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MonthlyPrepaymentOutstandingReportItemDTO> findByCriteria(
        MonthlyPrepaymentOutstandingReportItemCriteria criteria,
        Pageable page
    ) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MonthlyPrepaymentOutstandingReportItem> specification = createSpecification(criteria);
        return monthlyPrepaymentOutstandingReportItemRepository
            .findAll(specification, page)
            .map(monthlyPrepaymentOutstandingReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MonthlyPrepaymentOutstandingReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MonthlyPrepaymentOutstandingReportItem> specification = createSpecification(criteria);
        return monthlyPrepaymentOutstandingReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link MonthlyPrepaymentOutstandingReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MonthlyPrepaymentOutstandingReportItem> createSpecification(
        MonthlyPrepaymentOutstandingReportItemCriteria criteria
    ) {
        Specification<MonthlyPrepaymentOutstandingReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MonthlyPrepaymentOutstandingReportItem_.id));
            }
            if (criteria.getFiscalMonthEndDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getFiscalMonthEndDate(),
                            MonthlyPrepaymentOutstandingReportItem_.fiscalMonthEndDate
                        )
                    );
            }
            if (criteria.getTotalPrepaymentAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalPrepaymentAmount(),
                            MonthlyPrepaymentOutstandingReportItem_.totalPrepaymentAmount
                        )
                    );
            }
            if (criteria.getTotalAmortisedAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalAmortisedAmount(),
                            MonthlyPrepaymentOutstandingReportItem_.totalAmortisedAmount
                        )
                    );
            }
            if (criteria.getTotalOutstandingAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalOutstandingAmount(),
                            MonthlyPrepaymentOutstandingReportItem_.totalOutstandingAmount
                        )
                    );
            }
            if (criteria.getNumberOfPrepaymentAccounts() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getNumberOfPrepaymentAccounts(),
                            MonthlyPrepaymentOutstandingReportItem_.numberOfPrepaymentAccounts
                        )
                    );
            }
        }
        return specification;
    }
}
