package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.FiscalMonth;
import io.github.erp.repository.FiscalMonthRepository;
import io.github.erp.repository.search.FiscalMonthSearchRepository;
import io.github.erp.service.criteria.FiscalMonthCriteria;
import io.github.erp.service.dto.FiscalMonthDTO;
import io.github.erp.service.mapper.FiscalMonthMapper;
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
 * Service for executing complex queries for {@link FiscalMonth} entities in the database.
 * The main input is a {@link FiscalMonthCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FiscalMonthDTO} or a {@link Page} of {@link FiscalMonthDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FiscalMonthQueryService extends QueryService<FiscalMonth> {

    private final Logger log = LoggerFactory.getLogger(FiscalMonthQueryService.class);

    private final FiscalMonthRepository fiscalMonthRepository;

    private final FiscalMonthMapper fiscalMonthMapper;

    private final FiscalMonthSearchRepository fiscalMonthSearchRepository;

    public FiscalMonthQueryService(
        FiscalMonthRepository fiscalMonthRepository,
        FiscalMonthMapper fiscalMonthMapper,
        FiscalMonthSearchRepository fiscalMonthSearchRepository
    ) {
        this.fiscalMonthRepository = fiscalMonthRepository;
        this.fiscalMonthMapper = fiscalMonthMapper;
        this.fiscalMonthSearchRepository = fiscalMonthSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FiscalMonthDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FiscalMonthDTO> findByCriteria(FiscalMonthCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FiscalMonth> specification = createSpecification(criteria);
        return fiscalMonthMapper.toDto(fiscalMonthRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FiscalMonthDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FiscalMonthDTO> findByCriteria(FiscalMonthCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FiscalMonth> specification = createSpecification(criteria);
        return fiscalMonthRepository.findAll(specification, page).map(fiscalMonthMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FiscalMonthCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FiscalMonth> specification = createSpecification(criteria);
        return fiscalMonthRepository.count(specification);
    }

    /**
     * Function to convert {@link FiscalMonthCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FiscalMonth> createSpecification(FiscalMonthCriteria criteria) {
        Specification<FiscalMonth> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FiscalMonth_.id));
            }
            if (criteria.getMonthNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMonthNumber(), FiscalMonth_.monthNumber));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), FiscalMonth_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), FiscalMonth_.endDate));
            }
            if (criteria.getFiscalMonthCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFiscalMonthCode(), FiscalMonth_.fiscalMonthCode));
            }
            if (criteria.getFiscalYearId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalYearId(),
                            root -> root.join(FiscalMonth_.fiscalYear, JoinType.LEFT).get(FiscalYear_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(FiscalMonth_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getUniversallyUniqueMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUniversallyUniqueMappingId(),
                            root -> root.join(FiscalMonth_.universallyUniqueMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getFiscalQuarterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalQuarterId(),
                            root -> root.join(FiscalMonth_.fiscalQuarter, JoinType.LEFT).get(FiscalQuarter_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
