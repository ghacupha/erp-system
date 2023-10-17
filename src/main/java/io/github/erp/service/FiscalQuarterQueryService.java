package io.github.erp.service;

/*-
 * Erp System - Mark VI No 2 (Phoebe Series) Server ver 1.5.3
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
import io.github.erp.domain.FiscalQuarter;
import io.github.erp.repository.FiscalQuarterRepository;
import io.github.erp.repository.search.FiscalQuarterSearchRepository;
import io.github.erp.service.criteria.FiscalQuarterCriteria;
import io.github.erp.service.dto.FiscalQuarterDTO;
import io.github.erp.service.mapper.FiscalQuarterMapper;
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
 * Service for executing complex queries for {@link FiscalQuarter} entities in the database.
 * The main input is a {@link FiscalQuarterCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FiscalQuarterDTO} or a {@link Page} of {@link FiscalQuarterDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FiscalQuarterQueryService extends QueryService<FiscalQuarter> {

    private final Logger log = LoggerFactory.getLogger(FiscalQuarterQueryService.class);

    private final FiscalQuarterRepository fiscalQuarterRepository;

    private final FiscalQuarterMapper fiscalQuarterMapper;

    private final FiscalQuarterSearchRepository fiscalQuarterSearchRepository;

    public FiscalQuarterQueryService(
        FiscalQuarterRepository fiscalQuarterRepository,
        FiscalQuarterMapper fiscalQuarterMapper,
        FiscalQuarterSearchRepository fiscalQuarterSearchRepository
    ) {
        this.fiscalQuarterRepository = fiscalQuarterRepository;
        this.fiscalQuarterMapper = fiscalQuarterMapper;
        this.fiscalQuarterSearchRepository = fiscalQuarterSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FiscalQuarterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FiscalQuarterDTO> findByCriteria(FiscalQuarterCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FiscalQuarter> specification = createSpecification(criteria);
        return fiscalQuarterMapper.toDto(fiscalQuarterRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FiscalQuarterDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FiscalQuarterDTO> findByCriteria(FiscalQuarterCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FiscalQuarter> specification = createSpecification(criteria);
        return fiscalQuarterRepository.findAll(specification, page).map(fiscalQuarterMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FiscalQuarterCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FiscalQuarter> specification = createSpecification(criteria);
        return fiscalQuarterRepository.count(specification);
    }

    /**
     * Function to convert {@link FiscalQuarterCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FiscalQuarter> createSpecification(FiscalQuarterCriteria criteria) {
        Specification<FiscalQuarter> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FiscalQuarter_.id));
            }
            if (criteria.getQuarterNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuarterNumber(), FiscalQuarter_.quarterNumber));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), FiscalQuarter_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), FiscalQuarter_.endDate));
            }
            if (criteria.getFiscalQuarterCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFiscalQuarterCode(), FiscalQuarter_.fiscalQuarterCode));
            }
            if (criteria.getFiscalYearId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalYearId(),
                            root -> root.join(FiscalQuarter_.fiscalYear, JoinType.LEFT).get(FiscalYear_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(FiscalQuarter_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getUniversallyUniqueMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUniversallyUniqueMappingId(),
                            root -> root.join(FiscalQuarter_.universallyUniqueMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
