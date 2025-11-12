package io.github.erp.service;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.LeasePeriod;
import io.github.erp.repository.LeasePeriodRepository;
import io.github.erp.repository.search.LeasePeriodSearchRepository;
import io.github.erp.service.criteria.LeasePeriodCriteria;
import io.github.erp.service.dto.LeasePeriodDTO;
import io.github.erp.service.mapper.LeasePeriodMapper;
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
 * Service for executing complex queries for {@link LeasePeriod} entities in the database.
 * The main input is a {@link LeasePeriodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeasePeriodDTO} or a {@link Page} of {@link LeasePeriodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeasePeriodQueryService extends QueryService<LeasePeriod> {

    private final Logger log = LoggerFactory.getLogger(LeasePeriodQueryService.class);

    private final LeasePeriodRepository leasePeriodRepository;

    private final LeasePeriodMapper leasePeriodMapper;

    private final LeasePeriodSearchRepository leasePeriodSearchRepository;

    public LeasePeriodQueryService(
        LeasePeriodRepository leasePeriodRepository,
        LeasePeriodMapper leasePeriodMapper,
        LeasePeriodSearchRepository leasePeriodSearchRepository
    ) {
        this.leasePeriodRepository = leasePeriodRepository;
        this.leasePeriodMapper = leasePeriodMapper;
        this.leasePeriodSearchRepository = leasePeriodSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeasePeriodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeasePeriodDTO> findByCriteria(LeasePeriodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeasePeriod> specification = createSpecification(criteria);
        return leasePeriodMapper.toDto(leasePeriodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeasePeriodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeasePeriodDTO> findByCriteria(LeasePeriodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeasePeriod> specification = createSpecification(criteria);
        return leasePeriodRepository.findAll(specification, page).map(leasePeriodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeasePeriodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeasePeriod> specification = createSpecification(criteria);
        return leasePeriodRepository.count(specification);
    }

    /**
     * Function to convert {@link LeasePeriodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeasePeriod> createSpecification(LeasePeriodCriteria criteria) {
        Specification<LeasePeriod> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeasePeriod_.id));
            }
            if (criteria.getSequenceNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSequenceNumber(), LeasePeriod_.sequenceNumber));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), LeasePeriod_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), LeasePeriod_.endDate));
            }
            if (criteria.getPeriodCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPeriodCode(), LeasePeriod_.periodCode));
            }
            if (criteria.getFiscalMonthId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalMonthId(),
                            root -> root.join(LeasePeriod_.fiscalMonth, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
