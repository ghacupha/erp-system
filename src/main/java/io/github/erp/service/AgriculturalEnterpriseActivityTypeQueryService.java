package io.github.erp.service;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.AgriculturalEnterpriseActivityType;
import io.github.erp.repository.AgriculturalEnterpriseActivityTypeRepository;
import io.github.erp.repository.search.AgriculturalEnterpriseActivityTypeSearchRepository;
import io.github.erp.service.criteria.AgriculturalEnterpriseActivityTypeCriteria;
import io.github.erp.service.dto.AgriculturalEnterpriseActivityTypeDTO;
import io.github.erp.service.mapper.AgriculturalEnterpriseActivityTypeMapper;
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
 * Service for executing complex queries for {@link AgriculturalEnterpriseActivityType} entities in the database.
 * The main input is a {@link AgriculturalEnterpriseActivityTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AgriculturalEnterpriseActivityTypeDTO} or a {@link Page} of {@link AgriculturalEnterpriseActivityTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgriculturalEnterpriseActivityTypeQueryService extends QueryService<AgriculturalEnterpriseActivityType> {

    private final Logger log = LoggerFactory.getLogger(AgriculturalEnterpriseActivityTypeQueryService.class);

    private final AgriculturalEnterpriseActivityTypeRepository agriculturalEnterpriseActivityTypeRepository;

    private final AgriculturalEnterpriseActivityTypeMapper agriculturalEnterpriseActivityTypeMapper;

    private final AgriculturalEnterpriseActivityTypeSearchRepository agriculturalEnterpriseActivityTypeSearchRepository;

    public AgriculturalEnterpriseActivityTypeQueryService(
        AgriculturalEnterpriseActivityTypeRepository agriculturalEnterpriseActivityTypeRepository,
        AgriculturalEnterpriseActivityTypeMapper agriculturalEnterpriseActivityTypeMapper,
        AgriculturalEnterpriseActivityTypeSearchRepository agriculturalEnterpriseActivityTypeSearchRepository
    ) {
        this.agriculturalEnterpriseActivityTypeRepository = agriculturalEnterpriseActivityTypeRepository;
        this.agriculturalEnterpriseActivityTypeMapper = agriculturalEnterpriseActivityTypeMapper;
        this.agriculturalEnterpriseActivityTypeSearchRepository = agriculturalEnterpriseActivityTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AgriculturalEnterpriseActivityTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AgriculturalEnterpriseActivityTypeDTO> findByCriteria(AgriculturalEnterpriseActivityTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AgriculturalEnterpriseActivityType> specification = createSpecification(criteria);
        return agriculturalEnterpriseActivityTypeMapper.toDto(agriculturalEnterpriseActivityTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AgriculturalEnterpriseActivityTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AgriculturalEnterpriseActivityTypeDTO> findByCriteria(AgriculturalEnterpriseActivityTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AgriculturalEnterpriseActivityType> specification = createSpecification(criteria);
        return agriculturalEnterpriseActivityTypeRepository
            .findAll(specification, page)
            .map(agriculturalEnterpriseActivityTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgriculturalEnterpriseActivityTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AgriculturalEnterpriseActivityType> specification = createSpecification(criteria);
        return agriculturalEnterpriseActivityTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link AgriculturalEnterpriseActivityTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AgriculturalEnterpriseActivityType> createSpecification(AgriculturalEnterpriseActivityTypeCriteria criteria) {
        Specification<AgriculturalEnterpriseActivityType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AgriculturalEnterpriseActivityType_.id));
            }
            if (criteria.getAgriculturalEnterpriseActivityTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAgriculturalEnterpriseActivityTypeCode(),
                            AgriculturalEnterpriseActivityType_.agriculturalEnterpriseActivityTypeCode
                        )
                    );
            }
            if (criteria.getAgriculturalEnterpriseActivityType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAgriculturalEnterpriseActivityType(),
                            AgriculturalEnterpriseActivityType_.agriculturalEnterpriseActivityType
                        )
                    );
            }
        }
        return specification;
    }
}
