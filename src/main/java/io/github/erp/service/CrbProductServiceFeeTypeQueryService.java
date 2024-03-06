package io.github.erp.service;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.CrbProductServiceFeeType;
import io.github.erp.repository.CrbProductServiceFeeTypeRepository;
import io.github.erp.repository.search.CrbProductServiceFeeTypeSearchRepository;
import io.github.erp.service.criteria.CrbProductServiceFeeTypeCriteria;
import io.github.erp.service.dto.CrbProductServiceFeeTypeDTO;
import io.github.erp.service.mapper.CrbProductServiceFeeTypeMapper;
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
 * Service for executing complex queries for {@link CrbProductServiceFeeType} entities in the database.
 * The main input is a {@link CrbProductServiceFeeTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbProductServiceFeeTypeDTO} or a {@link Page} of {@link CrbProductServiceFeeTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbProductServiceFeeTypeQueryService extends QueryService<CrbProductServiceFeeType> {

    private final Logger log = LoggerFactory.getLogger(CrbProductServiceFeeTypeQueryService.class);

    private final CrbProductServiceFeeTypeRepository crbProductServiceFeeTypeRepository;

    private final CrbProductServiceFeeTypeMapper crbProductServiceFeeTypeMapper;

    private final CrbProductServiceFeeTypeSearchRepository crbProductServiceFeeTypeSearchRepository;

    public CrbProductServiceFeeTypeQueryService(
        CrbProductServiceFeeTypeRepository crbProductServiceFeeTypeRepository,
        CrbProductServiceFeeTypeMapper crbProductServiceFeeTypeMapper,
        CrbProductServiceFeeTypeSearchRepository crbProductServiceFeeTypeSearchRepository
    ) {
        this.crbProductServiceFeeTypeRepository = crbProductServiceFeeTypeRepository;
        this.crbProductServiceFeeTypeMapper = crbProductServiceFeeTypeMapper;
        this.crbProductServiceFeeTypeSearchRepository = crbProductServiceFeeTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbProductServiceFeeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbProductServiceFeeTypeDTO> findByCriteria(CrbProductServiceFeeTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbProductServiceFeeType> specification = createSpecification(criteria);
        return crbProductServiceFeeTypeMapper.toDto(crbProductServiceFeeTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbProductServiceFeeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbProductServiceFeeTypeDTO> findByCriteria(CrbProductServiceFeeTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbProductServiceFeeType> specification = createSpecification(criteria);
        return crbProductServiceFeeTypeRepository.findAll(specification, page).map(crbProductServiceFeeTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbProductServiceFeeTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbProductServiceFeeType> specification = createSpecification(criteria);
        return crbProductServiceFeeTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbProductServiceFeeTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbProductServiceFeeType> createSpecification(CrbProductServiceFeeTypeCriteria criteria) {
        Specification<CrbProductServiceFeeType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbProductServiceFeeType_.id));
            }
            if (criteria.getChargeTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getChargeTypeCode(), CrbProductServiceFeeType_.chargeTypeCode));
            }
            if (criteria.getChargeTypeDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getChargeTypeDescription(), CrbProductServiceFeeType_.chargeTypeDescription)
                    );
            }
            if (criteria.getChargeTypeCategory() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getChargeTypeCategory(), CrbProductServiceFeeType_.chargeTypeCategory)
                    );
            }
        }
        return specification;
    }
}
