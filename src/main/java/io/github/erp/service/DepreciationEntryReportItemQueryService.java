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
import io.github.erp.domain.DepreciationEntryReportItem;
import io.github.erp.repository.DepreciationEntryReportItemRepository;
import io.github.erp.repository.search.DepreciationEntryReportItemSearchRepository;
import io.github.erp.service.criteria.DepreciationEntryReportItemCriteria;
import io.github.erp.service.dto.DepreciationEntryReportItemDTO;
import io.github.erp.service.mapper.DepreciationEntryReportItemMapper;
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
 * Service for executing complex queries for {@link DepreciationEntryReportItem} entities in the database.
 * The main input is a {@link DepreciationEntryReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepreciationEntryReportItemDTO} or a {@link Page} of {@link DepreciationEntryReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepreciationEntryReportItemQueryService extends QueryService<DepreciationEntryReportItem> {

    private final Logger log = LoggerFactory.getLogger(DepreciationEntryReportItemQueryService.class);

    private final DepreciationEntryReportItemRepository depreciationEntryReportItemRepository;

    private final DepreciationEntryReportItemMapper depreciationEntryReportItemMapper;

    private final DepreciationEntryReportItemSearchRepository depreciationEntryReportItemSearchRepository;

    public DepreciationEntryReportItemQueryService(
        DepreciationEntryReportItemRepository depreciationEntryReportItemRepository,
        DepreciationEntryReportItemMapper depreciationEntryReportItemMapper,
        DepreciationEntryReportItemSearchRepository depreciationEntryReportItemSearchRepository
    ) {
        this.depreciationEntryReportItemRepository = depreciationEntryReportItemRepository;
        this.depreciationEntryReportItemMapper = depreciationEntryReportItemMapper;
        this.depreciationEntryReportItemSearchRepository = depreciationEntryReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DepreciationEntryReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepreciationEntryReportItemDTO> findByCriteria(DepreciationEntryReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DepreciationEntryReportItem> specification = createSpecification(criteria);
        return depreciationEntryReportItemMapper.toDto(depreciationEntryReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepreciationEntryReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepreciationEntryReportItemDTO> findByCriteria(DepreciationEntryReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepreciationEntryReportItem> specification = createSpecification(criteria);
        return depreciationEntryReportItemRepository.findAll(specification, page).map(depreciationEntryReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepreciationEntryReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DepreciationEntryReportItem> specification = createSpecification(criteria);
        return depreciationEntryReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link DepreciationEntryReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepreciationEntryReportItem> createSpecification(DepreciationEntryReportItemCriteria criteria) {
        Specification<DepreciationEntryReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DepreciationEntryReportItem_.id));
            }
            if (criteria.getAssetRegistrationDetails() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAssetRegistrationDetails(),
                            DepreciationEntryReportItem_.assetRegistrationDetails
                        )
                    );
            }
            if (criteria.getPostedAt() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostedAt(), DepreciationEntryReportItem_.postedAt));
            }
            if (criteria.getAssetNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAssetNumber(), DepreciationEntryReportItem_.assetNumber));
            }
            if (criteria.getServiceOutlet() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getServiceOutlet(), DepreciationEntryReportItem_.serviceOutlet));
            }
            if (criteria.getAssetCategory() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetCategory(), DepreciationEntryReportItem_.assetCategory));
            }
            if (criteria.getDepreciationMethod() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getDepreciationMethod(), DepreciationEntryReportItem_.depreciationMethod)
                    );
            }
            if (criteria.getDepreciationPeriod() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getDepreciationPeriod(), DepreciationEntryReportItem_.depreciationPeriod)
                    );
            }
            if (criteria.getFiscalMonthCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFiscalMonthCode(), DepreciationEntryReportItem_.fiscalMonthCode)
                    );
            }
            if (criteria.getAssetRegistrationCost() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getAssetRegistrationCost(), DepreciationEntryReportItem_.assetRegistrationCost)
                    );
            }
            if (criteria.getDepreciationAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getDepreciationAmount(), DepreciationEntryReportItem_.depreciationAmount)
                    );
            }
            if (criteria.getElapsedMonths() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getElapsedMonths(), DepreciationEntryReportItem_.elapsedMonths));
            }
            if (criteria.getPriorMonths() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPriorMonths(), DepreciationEntryReportItem_.priorMonths));
            }
            if (criteria.getUsefulLifeYears() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getUsefulLifeYears(), DepreciationEntryReportItem_.usefulLifeYears));
            }
            if (criteria.getPreviousNBV() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPreviousNBV(), DepreciationEntryReportItem_.previousNBV));
            }
            if (criteria.getNetBookValue() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getNetBookValue(), DepreciationEntryReportItem_.netBookValue));
            }
            if (criteria.getDepreciationPeriodStartDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getDepreciationPeriodStartDate(),
                            DepreciationEntryReportItem_.depreciationPeriodStartDate
                        )
                    );
            }
            if (criteria.getDepreciationPeriodEndDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getDepreciationPeriodEndDate(),
                            DepreciationEntryReportItem_.depreciationPeriodEndDate
                        )
                    );
            }
        }
        return specification;
    }
}
