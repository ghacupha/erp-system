package io.github.erp.service;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import io.github.erp.domain.BusinessSegmentTypes;
import io.github.erp.repository.BusinessSegmentTypesRepository;
import io.github.erp.repository.search.BusinessSegmentTypesSearchRepository;
import io.github.erp.service.criteria.BusinessSegmentTypesCriteria;
import io.github.erp.service.dto.BusinessSegmentTypesDTO;
import io.github.erp.service.mapper.BusinessSegmentTypesMapper;
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
 * Service for executing complex queries for {@link BusinessSegmentTypes} entities in the database.
 * The main input is a {@link BusinessSegmentTypesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusinessSegmentTypesDTO} or a {@link Page} of {@link BusinessSegmentTypesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusinessSegmentTypesQueryService extends QueryService<BusinessSegmentTypes> {

    private final Logger log = LoggerFactory.getLogger(BusinessSegmentTypesQueryService.class);

    private final BusinessSegmentTypesRepository businessSegmentTypesRepository;

    private final BusinessSegmentTypesMapper businessSegmentTypesMapper;

    private final BusinessSegmentTypesSearchRepository businessSegmentTypesSearchRepository;

    public BusinessSegmentTypesQueryService(
        BusinessSegmentTypesRepository businessSegmentTypesRepository,
        BusinessSegmentTypesMapper businessSegmentTypesMapper,
        BusinessSegmentTypesSearchRepository businessSegmentTypesSearchRepository
    ) {
        this.businessSegmentTypesRepository = businessSegmentTypesRepository;
        this.businessSegmentTypesMapper = businessSegmentTypesMapper;
        this.businessSegmentTypesSearchRepository = businessSegmentTypesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BusinessSegmentTypesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessSegmentTypesDTO> findByCriteria(BusinessSegmentTypesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BusinessSegmentTypes> specification = createSpecification(criteria);
        return businessSegmentTypesMapper.toDto(businessSegmentTypesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusinessSegmentTypesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessSegmentTypesDTO> findByCriteria(BusinessSegmentTypesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BusinessSegmentTypes> specification = createSpecification(criteria);
        return businessSegmentTypesRepository.findAll(specification, page).map(businessSegmentTypesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusinessSegmentTypesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BusinessSegmentTypes> specification = createSpecification(criteria);
        return businessSegmentTypesRepository.count(specification);
    }

    /**
     * Function to convert {@link BusinessSegmentTypesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BusinessSegmentTypes> createSpecification(BusinessSegmentTypesCriteria criteria) {
        Specification<BusinessSegmentTypes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BusinessSegmentTypes_.id));
            }
            if (criteria.getBusinessEconomicSegmentCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getBusinessEconomicSegmentCode(),
                            BusinessSegmentTypes_.businessEconomicSegmentCode
                        )
                    );
            }
            if (criteria.getBusinessEconomicSegment() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getBusinessEconomicSegment(), BusinessSegmentTypes_.businessEconomicSegment)
                    );
            }
        }
        return specification;
    }
}
