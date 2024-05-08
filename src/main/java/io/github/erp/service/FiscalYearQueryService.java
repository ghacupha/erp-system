package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.FiscalYear;
import io.github.erp.repository.FiscalYearRepository;
import io.github.erp.repository.search.FiscalYearSearchRepository;
import io.github.erp.service.criteria.FiscalYearCriteria;
import io.github.erp.service.dto.FiscalYearDTO;
import io.github.erp.service.mapper.FiscalYearMapper;
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
 * Service for executing complex queries for {@link FiscalYear} entities in the database.
 * The main input is a {@link FiscalYearCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FiscalYearDTO} or a {@link Page} of {@link FiscalYearDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FiscalYearQueryService extends QueryService<FiscalYear> {

    private final Logger log = LoggerFactory.getLogger(FiscalYearQueryService.class);

    private final FiscalYearRepository fiscalYearRepository;

    private final FiscalYearMapper fiscalYearMapper;

    private final FiscalYearSearchRepository fiscalYearSearchRepository;

    public FiscalYearQueryService(
        FiscalYearRepository fiscalYearRepository,
        FiscalYearMapper fiscalYearMapper,
        FiscalYearSearchRepository fiscalYearSearchRepository
    ) {
        this.fiscalYearRepository = fiscalYearRepository;
        this.fiscalYearMapper = fiscalYearMapper;
        this.fiscalYearSearchRepository = fiscalYearSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FiscalYearDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FiscalYearDTO> findByCriteria(FiscalYearCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FiscalYear> specification = createSpecification(criteria);
        return fiscalYearMapper.toDto(fiscalYearRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FiscalYearDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FiscalYearDTO> findByCriteria(FiscalYearCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FiscalYear> specification = createSpecification(criteria);
        return fiscalYearRepository.findAll(specification, page).map(fiscalYearMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FiscalYearCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FiscalYear> specification = createSpecification(criteria);
        return fiscalYearRepository.count(specification);
    }

    /**
     * Function to convert {@link FiscalYearCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FiscalYear> createSpecification(FiscalYearCriteria criteria) {
        Specification<FiscalYear> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FiscalYear_.id));
            }
            if (criteria.getFiscalYearCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFiscalYearCode(), FiscalYear_.fiscalYearCode));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), FiscalYear_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), FiscalYear_.endDate));
            }
            if (criteria.getFiscalYearStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getFiscalYearStatus(), FiscalYear_.fiscalYearStatus));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(FiscalYear_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getUniversallyUniqueMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUniversallyUniqueMappingId(),
                            root -> root.join(FiscalYear_.universallyUniqueMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(FiscalYear_.createdBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastUpdatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastUpdatedById(),
                            root -> root.join(FiscalYear_.lastUpdatedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
