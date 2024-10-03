package io.github.erp.service;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.LoanProductType;
import io.github.erp.repository.LoanProductTypeRepository;
import io.github.erp.repository.search.LoanProductTypeSearchRepository;
import io.github.erp.service.criteria.LoanProductTypeCriteria;
import io.github.erp.service.dto.LoanProductTypeDTO;
import io.github.erp.service.mapper.LoanProductTypeMapper;
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
 * Service for executing complex queries for {@link LoanProductType} entities in the database.
 * The main input is a {@link LoanProductTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoanProductTypeDTO} or a {@link Page} of {@link LoanProductTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoanProductTypeQueryService extends QueryService<LoanProductType> {

    private final Logger log = LoggerFactory.getLogger(LoanProductTypeQueryService.class);

    private final LoanProductTypeRepository loanProductTypeRepository;

    private final LoanProductTypeMapper loanProductTypeMapper;

    private final LoanProductTypeSearchRepository loanProductTypeSearchRepository;

    public LoanProductTypeQueryService(
        LoanProductTypeRepository loanProductTypeRepository,
        LoanProductTypeMapper loanProductTypeMapper,
        LoanProductTypeSearchRepository loanProductTypeSearchRepository
    ) {
        this.loanProductTypeRepository = loanProductTypeRepository;
        this.loanProductTypeMapper = loanProductTypeMapper;
        this.loanProductTypeSearchRepository = loanProductTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoanProductTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoanProductTypeDTO> findByCriteria(LoanProductTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoanProductType> specification = createSpecification(criteria);
        return loanProductTypeMapper.toDto(loanProductTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoanProductTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanProductTypeDTO> findByCriteria(LoanProductTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoanProductType> specification = createSpecification(criteria);
        return loanProductTypeRepository.findAll(specification, page).map(loanProductTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoanProductTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoanProductType> specification = createSpecification(criteria);
        return loanProductTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link LoanProductTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoanProductType> createSpecification(LoanProductTypeCriteria criteria) {
        Specification<LoanProductType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoanProductType_.id));
            }
            if (criteria.getProductCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductCode(), LoanProductType_.productCode));
            }
            if (criteria.getProductType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductType(), LoanProductType_.productType));
            }
        }
        return specification;
    }
}
