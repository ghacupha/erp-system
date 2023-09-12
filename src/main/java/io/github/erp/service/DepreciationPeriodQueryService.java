package io.github.erp.service;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.domain.DepreciationPeriod;
import io.github.erp.repository.DepreciationPeriodRepository;
import io.github.erp.repository.search.DepreciationPeriodSearchRepository;
import io.github.erp.service.criteria.DepreciationPeriodCriteria;
import io.github.erp.service.dto.DepreciationPeriodDTO;
import io.github.erp.service.mapper.DepreciationPeriodMapper;
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
 * Service for executing complex queries for {@link DepreciationPeriod} entities in the database.
 * The main input is a {@link DepreciationPeriodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepreciationPeriodDTO} or a {@link Page} of {@link DepreciationPeriodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepreciationPeriodQueryService extends QueryService<DepreciationPeriod> {

    private final Logger log = LoggerFactory.getLogger(DepreciationPeriodQueryService.class);

    private final DepreciationPeriodRepository depreciationPeriodRepository;

    private final DepreciationPeriodMapper depreciationPeriodMapper;

    private final DepreciationPeriodSearchRepository depreciationPeriodSearchRepository;

    public DepreciationPeriodQueryService(
        DepreciationPeriodRepository depreciationPeriodRepository,
        DepreciationPeriodMapper depreciationPeriodMapper,
        DepreciationPeriodSearchRepository depreciationPeriodSearchRepository
    ) {
        this.depreciationPeriodRepository = depreciationPeriodRepository;
        this.depreciationPeriodMapper = depreciationPeriodMapper;
        this.depreciationPeriodSearchRepository = depreciationPeriodSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DepreciationPeriodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepreciationPeriodDTO> findByCriteria(DepreciationPeriodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DepreciationPeriod> specification = createSpecification(criteria);
        return depreciationPeriodMapper.toDto(depreciationPeriodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepreciationPeriodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepreciationPeriodDTO> findByCriteria(DepreciationPeriodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepreciationPeriod> specification = createSpecification(criteria);
        return depreciationPeriodRepository.findAll(specification, page).map(depreciationPeriodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepreciationPeriodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DepreciationPeriod> specification = createSpecification(criteria);
        return depreciationPeriodRepository.count(specification);
    }

    /**
     * Function to convert {@link DepreciationPeriodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepreciationPeriod> createSpecification(DepreciationPeriodCriteria criteria) {
        Specification<DepreciationPeriod> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DepreciationPeriod_.id));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), DepreciationPeriod_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), DepreciationPeriod_.endDate));
            }
            if (criteria.getDepreciationPeriodStatus() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDepreciationPeriodStatus(), DepreciationPeriod_.depreciationPeriodStatus)
                    );
            }
            if (criteria.getPeriodCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodCode(), DepreciationPeriod_.periodCode));
            }
            if (criteria.getProcessLocked() != null) {
                specification = specification.and(buildSpecification(criteria.getProcessLocked(), DepreciationPeriod_.processLocked));
            }
            if (criteria.getPreviousPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPreviousPeriodId(),
                            root -> root.join(DepreciationPeriod_.previousPeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(DepreciationPeriod_.createdBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getFiscalYearId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalYearId(),
                            root -> root.join(DepreciationPeriod_.fiscalYear, JoinType.LEFT).get(FiscalYear_.id)
                        )
                    );
            }
            if (criteria.getFiscalMonthId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalMonthId(),
                            root -> root.join(DepreciationPeriod_.fiscalMonth, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
            if (criteria.getFiscalQuarterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalQuarterId(),
                            root -> root.join(DepreciationPeriod_.fiscalQuarter, JoinType.LEFT).get(FiscalQuarter_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
