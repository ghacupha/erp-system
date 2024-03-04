package io.github.erp.service;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.FxTransactionRateType;
import io.github.erp.repository.FxTransactionRateTypeRepository;
import io.github.erp.repository.search.FxTransactionRateTypeSearchRepository;
import io.github.erp.service.criteria.FxTransactionRateTypeCriteria;
import io.github.erp.service.dto.FxTransactionRateTypeDTO;
import io.github.erp.service.mapper.FxTransactionRateTypeMapper;
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
 * Service for executing complex queries for {@link FxTransactionRateType} entities in the database.
 * The main input is a {@link FxTransactionRateTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FxTransactionRateTypeDTO} or a {@link Page} of {@link FxTransactionRateTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FxTransactionRateTypeQueryService extends QueryService<FxTransactionRateType> {

    private final Logger log = LoggerFactory.getLogger(FxTransactionRateTypeQueryService.class);

    private final FxTransactionRateTypeRepository fxTransactionRateTypeRepository;

    private final FxTransactionRateTypeMapper fxTransactionRateTypeMapper;

    private final FxTransactionRateTypeSearchRepository fxTransactionRateTypeSearchRepository;

    public FxTransactionRateTypeQueryService(
        FxTransactionRateTypeRepository fxTransactionRateTypeRepository,
        FxTransactionRateTypeMapper fxTransactionRateTypeMapper,
        FxTransactionRateTypeSearchRepository fxTransactionRateTypeSearchRepository
    ) {
        this.fxTransactionRateTypeRepository = fxTransactionRateTypeRepository;
        this.fxTransactionRateTypeMapper = fxTransactionRateTypeMapper;
        this.fxTransactionRateTypeSearchRepository = fxTransactionRateTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FxTransactionRateTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FxTransactionRateTypeDTO> findByCriteria(FxTransactionRateTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FxTransactionRateType> specification = createSpecification(criteria);
        return fxTransactionRateTypeMapper.toDto(fxTransactionRateTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FxTransactionRateTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FxTransactionRateTypeDTO> findByCriteria(FxTransactionRateTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FxTransactionRateType> specification = createSpecification(criteria);
        return fxTransactionRateTypeRepository.findAll(specification, page).map(fxTransactionRateTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FxTransactionRateTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FxTransactionRateType> specification = createSpecification(criteria);
        return fxTransactionRateTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FxTransactionRateTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FxTransactionRateType> createSpecification(FxTransactionRateTypeCriteria criteria) {
        Specification<FxTransactionRateType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FxTransactionRateType_.id));
            }
            if (criteria.getFxTransactionRateTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFxTransactionRateTypeCode(), FxTransactionRateType_.fxTransactionRateTypeCode)
                    );
            }
            if (criteria.getFxTransactionRateType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFxTransactionRateType(), FxTransactionRateType_.fxTransactionRateType)
                    );
            }
        }
        return specification;
    }
}
