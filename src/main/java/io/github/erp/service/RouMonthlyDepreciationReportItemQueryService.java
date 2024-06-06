package io.github.erp.service;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.RouMonthlyDepreciationReportItem;
import io.github.erp.repository.RouMonthlyDepreciationReportItemRepository;
import io.github.erp.repository.search.RouMonthlyDepreciationReportItemSearchRepository;
import io.github.erp.service.criteria.RouMonthlyDepreciationReportItemCriteria;
import io.github.erp.service.dto.RouMonthlyDepreciationReportItemDTO;
import io.github.erp.service.mapper.RouMonthlyDepreciationReportItemMapper;
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
 * Service for executing complex queries for {@link RouMonthlyDepreciationReportItem} entities in the database.
 * The main input is a {@link RouMonthlyDepreciationReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouMonthlyDepreciationReportItemDTO} or a {@link Page} of {@link RouMonthlyDepreciationReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouMonthlyDepreciationReportItemQueryService extends QueryService<RouMonthlyDepreciationReportItem> {

    private final Logger log = LoggerFactory.getLogger(RouMonthlyDepreciationReportItemQueryService.class);

    private final RouMonthlyDepreciationReportItemRepository rouMonthlyDepreciationReportItemRepository;

    private final RouMonthlyDepreciationReportItemMapper rouMonthlyDepreciationReportItemMapper;

    private final RouMonthlyDepreciationReportItemSearchRepository rouMonthlyDepreciationReportItemSearchRepository;

    public RouMonthlyDepreciationReportItemQueryService(
        RouMonthlyDepreciationReportItemRepository rouMonthlyDepreciationReportItemRepository,
        RouMonthlyDepreciationReportItemMapper rouMonthlyDepreciationReportItemMapper,
        RouMonthlyDepreciationReportItemSearchRepository rouMonthlyDepreciationReportItemSearchRepository
    ) {
        this.rouMonthlyDepreciationReportItemRepository = rouMonthlyDepreciationReportItemRepository;
        this.rouMonthlyDepreciationReportItemMapper = rouMonthlyDepreciationReportItemMapper;
        this.rouMonthlyDepreciationReportItemSearchRepository = rouMonthlyDepreciationReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouMonthlyDepreciationReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouMonthlyDepreciationReportItemDTO> findByCriteria(RouMonthlyDepreciationReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouMonthlyDepreciationReportItem> specification = createSpecification(criteria);
        return rouMonthlyDepreciationReportItemMapper.toDto(rouMonthlyDepreciationReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouMonthlyDepreciationReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouMonthlyDepreciationReportItemDTO> findByCriteria(RouMonthlyDepreciationReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouMonthlyDepreciationReportItem> specification = createSpecification(criteria);
        return rouMonthlyDepreciationReportItemRepository.findAll(specification, page).map(rouMonthlyDepreciationReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouMonthlyDepreciationReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouMonthlyDepreciationReportItem> specification = createSpecification(criteria);
        return rouMonthlyDepreciationReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link RouMonthlyDepreciationReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouMonthlyDepreciationReportItem> createSpecification(RouMonthlyDepreciationReportItemCriteria criteria) {
        Specification<RouMonthlyDepreciationReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouMonthlyDepreciationReportItem_.id));
            }
            if (criteria.getFiscalMonthStartDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getFiscalMonthStartDate(), RouMonthlyDepreciationReportItem_.fiscalMonthStartDate)
                    );
            }
            if (criteria.getFiscalMonthEndDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getFiscalMonthEndDate(), RouMonthlyDepreciationReportItem_.fiscalMonthEndDate)
                    );
            }
            if (criteria.getTotalDepreciationAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalDepreciationAmount(),
                            RouMonthlyDepreciationReportItem_.totalDepreciationAmount
                        )
                    );
            }
        }
        return specification;
    }
}
