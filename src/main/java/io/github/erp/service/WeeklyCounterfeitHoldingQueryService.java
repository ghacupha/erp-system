package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import io.github.erp.domain.WeeklyCounterfeitHolding;
import io.github.erp.repository.WeeklyCounterfeitHoldingRepository;
import io.github.erp.repository.search.WeeklyCounterfeitHoldingSearchRepository;
import io.github.erp.service.criteria.WeeklyCounterfeitHoldingCriteria;
import io.github.erp.service.dto.WeeklyCounterfeitHoldingDTO;
import io.github.erp.service.mapper.WeeklyCounterfeitHoldingMapper;
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
 * Service for executing complex queries for {@link WeeklyCounterfeitHolding} entities in the database.
 * The main input is a {@link WeeklyCounterfeitHoldingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WeeklyCounterfeitHoldingDTO} or a {@link Page} of {@link WeeklyCounterfeitHoldingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WeeklyCounterfeitHoldingQueryService extends QueryService<WeeklyCounterfeitHolding> {

    private final Logger log = LoggerFactory.getLogger(WeeklyCounterfeitHoldingQueryService.class);

    private final WeeklyCounterfeitHoldingRepository weeklyCounterfeitHoldingRepository;

    private final WeeklyCounterfeitHoldingMapper weeklyCounterfeitHoldingMapper;

    private final WeeklyCounterfeitHoldingSearchRepository weeklyCounterfeitHoldingSearchRepository;

    public WeeklyCounterfeitHoldingQueryService(
        WeeklyCounterfeitHoldingRepository weeklyCounterfeitHoldingRepository,
        WeeklyCounterfeitHoldingMapper weeklyCounterfeitHoldingMapper,
        WeeklyCounterfeitHoldingSearchRepository weeklyCounterfeitHoldingSearchRepository
    ) {
        this.weeklyCounterfeitHoldingRepository = weeklyCounterfeitHoldingRepository;
        this.weeklyCounterfeitHoldingMapper = weeklyCounterfeitHoldingMapper;
        this.weeklyCounterfeitHoldingSearchRepository = weeklyCounterfeitHoldingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WeeklyCounterfeitHoldingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WeeklyCounterfeitHoldingDTO> findByCriteria(WeeklyCounterfeitHoldingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WeeklyCounterfeitHolding> specification = createSpecification(criteria);
        return weeklyCounterfeitHoldingMapper.toDto(weeklyCounterfeitHoldingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WeeklyCounterfeitHoldingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WeeklyCounterfeitHoldingDTO> findByCriteria(WeeklyCounterfeitHoldingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WeeklyCounterfeitHolding> specification = createSpecification(criteria);
        return weeklyCounterfeitHoldingRepository.findAll(specification, page).map(weeklyCounterfeitHoldingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WeeklyCounterfeitHoldingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WeeklyCounterfeitHolding> specification = createSpecification(criteria);
        return weeklyCounterfeitHoldingRepository.count(specification);
    }

    /**
     * Function to convert {@link WeeklyCounterfeitHoldingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WeeklyCounterfeitHolding> createSpecification(WeeklyCounterfeitHoldingCriteria criteria) {
        Specification<WeeklyCounterfeitHolding> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WeeklyCounterfeitHolding_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportingDate(), WeeklyCounterfeitHolding_.reportingDate));
            }
            if (criteria.getDateConfiscated() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDateConfiscated(), WeeklyCounterfeitHolding_.dateConfiscated));
            }
            if (criteria.getSerialNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSerialNumber(), WeeklyCounterfeitHolding_.serialNumber));
            }
            if (criteria.getDepositorsNames() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDepositorsNames(), WeeklyCounterfeitHolding_.depositorsNames));
            }
            if (criteria.getTellersNames() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTellersNames(), WeeklyCounterfeitHolding_.tellersNames));
            }
            if (criteria.getDateSubmittedToCBK() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getDateSubmittedToCBK(), WeeklyCounterfeitHolding_.dateSubmittedToCBK)
                    );
            }
        }
        return specification;
    }
}
