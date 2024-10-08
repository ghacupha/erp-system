package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.FraudType;
import io.github.erp.repository.FraudTypeRepository;
import io.github.erp.repository.search.FraudTypeSearchRepository;
import io.github.erp.service.criteria.FraudTypeCriteria;
import io.github.erp.service.dto.FraudTypeDTO;
import io.github.erp.service.mapper.FraudTypeMapper;
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
 * Service for executing complex queries for {@link FraudType} entities in the database.
 * The main input is a {@link FraudTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FraudTypeDTO} or a {@link Page} of {@link FraudTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FraudTypeQueryService extends QueryService<FraudType> {

    private final Logger log = LoggerFactory.getLogger(FraudTypeQueryService.class);

    private final FraudTypeRepository fraudTypeRepository;

    private final FraudTypeMapper fraudTypeMapper;

    private final FraudTypeSearchRepository fraudTypeSearchRepository;

    public FraudTypeQueryService(
        FraudTypeRepository fraudTypeRepository,
        FraudTypeMapper fraudTypeMapper,
        FraudTypeSearchRepository fraudTypeSearchRepository
    ) {
        this.fraudTypeRepository = fraudTypeRepository;
        this.fraudTypeMapper = fraudTypeMapper;
        this.fraudTypeSearchRepository = fraudTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FraudTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FraudTypeDTO> findByCriteria(FraudTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FraudType> specification = createSpecification(criteria);
        return fraudTypeMapper.toDto(fraudTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FraudTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FraudTypeDTO> findByCriteria(FraudTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FraudType> specification = createSpecification(criteria);
        return fraudTypeRepository.findAll(specification, page).map(fraudTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FraudTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FraudType> specification = createSpecification(criteria);
        return fraudTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FraudTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FraudType> createSpecification(FraudTypeCriteria criteria) {
        Specification<FraudType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FraudType_.id));
            }
            if (criteria.getFraudTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFraudTypeCode(), FraudType_.fraudTypeCode));
            }
            if (criteria.getFraudType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFraudType(), FraudType_.fraudType));
            }
        }
        return specification;
    }
}
