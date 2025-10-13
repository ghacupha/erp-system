package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.RouAccountBalanceReportItem;
import io.github.erp.repository.RouAccountBalanceReportItemRepository;
import io.github.erp.repository.search.RouAccountBalanceReportItemSearchRepository;
import io.github.erp.service.criteria.RouAccountBalanceReportItemCriteria;
import io.github.erp.service.dto.RouAccountBalanceReportItemDTO;
import io.github.erp.service.mapper.RouAccountBalanceReportItemMapper;
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
 * Service for executing complex queries for {@link RouAccountBalanceReportItem} entities in the database.
 * The main input is a {@link RouAccountBalanceReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouAccountBalanceReportItemDTO} or a {@link Page} of {@link RouAccountBalanceReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouAccountBalanceReportItemQueryService extends QueryService<RouAccountBalanceReportItem> {

    private final Logger log = LoggerFactory.getLogger(RouAccountBalanceReportItemQueryService.class);

    private final RouAccountBalanceReportItemRepository rouAccountBalanceReportItemRepository;

    private final RouAccountBalanceReportItemMapper rouAccountBalanceReportItemMapper;

    private final RouAccountBalanceReportItemSearchRepository rouAccountBalanceReportItemSearchRepository;

    public RouAccountBalanceReportItemQueryService(
        RouAccountBalanceReportItemRepository rouAccountBalanceReportItemRepository,
        RouAccountBalanceReportItemMapper rouAccountBalanceReportItemMapper,
        RouAccountBalanceReportItemSearchRepository rouAccountBalanceReportItemSearchRepository
    ) {
        this.rouAccountBalanceReportItemRepository = rouAccountBalanceReportItemRepository;
        this.rouAccountBalanceReportItemMapper = rouAccountBalanceReportItemMapper;
        this.rouAccountBalanceReportItemSearchRepository = rouAccountBalanceReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouAccountBalanceReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouAccountBalanceReportItemDTO> findByCriteria(RouAccountBalanceReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouAccountBalanceReportItem> specification = createSpecification(criteria);
        return rouAccountBalanceReportItemMapper.toDto(rouAccountBalanceReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouAccountBalanceReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouAccountBalanceReportItemDTO> findByCriteria(RouAccountBalanceReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouAccountBalanceReportItem> specification = createSpecification(criteria);
        return rouAccountBalanceReportItemRepository.findAll(specification, page).map(rouAccountBalanceReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouAccountBalanceReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouAccountBalanceReportItem> specification = createSpecification(criteria);
        return rouAccountBalanceReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link RouAccountBalanceReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouAccountBalanceReportItem> createSpecification(RouAccountBalanceReportItemCriteria criteria) {
        Specification<RouAccountBalanceReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouAccountBalanceReportItem_.id));
            }
            if (criteria.getAssetAccountName() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAssetAccountName(), RouAccountBalanceReportItem_.assetAccountName)
                    );
            }
            if (criteria.getAssetAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAssetAccountNumber(), RouAccountBalanceReportItem_.assetAccountNumber)
                    );
            }
            if (criteria.getDepreciationAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getDepreciationAccountNumber(),
                            RouAccountBalanceReportItem_.depreciationAccountNumber
                        )
                    );
            }
            if (criteria.getTotalLeaseAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTotalLeaseAmount(), RouAccountBalanceReportItem_.totalLeaseAmount)
                    );
            }
            if (criteria.getAccruedDepreciationAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getAccruedDepreciationAmount(),
                            RouAccountBalanceReportItem_.accruedDepreciationAmount
                        )
                    );
            }
            if (criteria.getCurrentPeriodDepreciationAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getCurrentPeriodDepreciationAmount(),
                            RouAccountBalanceReportItem_.currentPeriodDepreciationAmount
                        )
                    );
            }
            if (criteria.getNetBookValue() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getNetBookValue(), RouAccountBalanceReportItem_.netBookValue));
            }
            if (criteria.getFiscalPeriodEndDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getFiscalPeriodEndDate(), RouAccountBalanceReportItem_.fiscalPeriodEndDate)
                    );
            }
        }
        return specification;
    }
}
