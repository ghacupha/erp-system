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
import io.github.erp.domain.DerivativeSubType;
import io.github.erp.repository.DerivativeSubTypeRepository;
import io.github.erp.repository.search.DerivativeSubTypeSearchRepository;
import io.github.erp.service.criteria.DerivativeSubTypeCriteria;
import io.github.erp.service.dto.DerivativeSubTypeDTO;
import io.github.erp.service.mapper.DerivativeSubTypeMapper;
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
 * Service for executing complex queries for {@link DerivativeSubType} entities in the database.
 * The main input is a {@link DerivativeSubTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DerivativeSubTypeDTO} or a {@link Page} of {@link DerivativeSubTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DerivativeSubTypeQueryService extends QueryService<DerivativeSubType> {

    private final Logger log = LoggerFactory.getLogger(DerivativeSubTypeQueryService.class);

    private final DerivativeSubTypeRepository derivativeSubTypeRepository;

    private final DerivativeSubTypeMapper derivativeSubTypeMapper;

    private final DerivativeSubTypeSearchRepository derivativeSubTypeSearchRepository;

    public DerivativeSubTypeQueryService(
        DerivativeSubTypeRepository derivativeSubTypeRepository,
        DerivativeSubTypeMapper derivativeSubTypeMapper,
        DerivativeSubTypeSearchRepository derivativeSubTypeSearchRepository
    ) {
        this.derivativeSubTypeRepository = derivativeSubTypeRepository;
        this.derivativeSubTypeMapper = derivativeSubTypeMapper;
        this.derivativeSubTypeSearchRepository = derivativeSubTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DerivativeSubTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DerivativeSubTypeDTO> findByCriteria(DerivativeSubTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DerivativeSubType> specification = createSpecification(criteria);
        return derivativeSubTypeMapper.toDto(derivativeSubTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DerivativeSubTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DerivativeSubTypeDTO> findByCriteria(DerivativeSubTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DerivativeSubType> specification = createSpecification(criteria);
        return derivativeSubTypeRepository.findAll(specification, page).map(derivativeSubTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DerivativeSubTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DerivativeSubType> specification = createSpecification(criteria);
        return derivativeSubTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link DerivativeSubTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DerivativeSubType> createSpecification(DerivativeSubTypeCriteria criteria) {
        Specification<DerivativeSubType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DerivativeSubType_.id));
            }
            if (criteria.getFinancialDerivativeSubTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getFinancialDerivativeSubTypeCode(),
                            DerivativeSubType_.financialDerivativeSubTypeCode
                        )
                    );
            }
            if (criteria.getFinancialDerivativeSubTye() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFinancialDerivativeSubTye(), DerivativeSubType_.financialDerivativeSubTye)
                    );
            }
        }
        return specification;
    }
}
