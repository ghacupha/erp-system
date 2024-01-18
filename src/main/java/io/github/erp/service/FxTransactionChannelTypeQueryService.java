package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.FxTransactionChannelType;
import io.github.erp.repository.FxTransactionChannelTypeRepository;
import io.github.erp.repository.search.FxTransactionChannelTypeSearchRepository;
import io.github.erp.service.criteria.FxTransactionChannelTypeCriteria;
import io.github.erp.service.dto.FxTransactionChannelTypeDTO;
import io.github.erp.service.mapper.FxTransactionChannelTypeMapper;
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
 * Service for executing complex queries for {@link FxTransactionChannelType} entities in the database.
 * The main input is a {@link FxTransactionChannelTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FxTransactionChannelTypeDTO} or a {@link Page} of {@link FxTransactionChannelTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FxTransactionChannelTypeQueryService extends QueryService<FxTransactionChannelType> {

    private final Logger log = LoggerFactory.getLogger(FxTransactionChannelTypeQueryService.class);

    private final FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository;

    private final FxTransactionChannelTypeMapper fxTransactionChannelTypeMapper;

    private final FxTransactionChannelTypeSearchRepository fxTransactionChannelTypeSearchRepository;

    public FxTransactionChannelTypeQueryService(
        FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository,
        FxTransactionChannelTypeMapper fxTransactionChannelTypeMapper,
        FxTransactionChannelTypeSearchRepository fxTransactionChannelTypeSearchRepository
    ) {
        this.fxTransactionChannelTypeRepository = fxTransactionChannelTypeRepository;
        this.fxTransactionChannelTypeMapper = fxTransactionChannelTypeMapper;
        this.fxTransactionChannelTypeSearchRepository = fxTransactionChannelTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FxTransactionChannelTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FxTransactionChannelTypeDTO> findByCriteria(FxTransactionChannelTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FxTransactionChannelType> specification = createSpecification(criteria);
        return fxTransactionChannelTypeMapper.toDto(fxTransactionChannelTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FxTransactionChannelTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FxTransactionChannelTypeDTO> findByCriteria(FxTransactionChannelTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FxTransactionChannelType> specification = createSpecification(criteria);
        return fxTransactionChannelTypeRepository.findAll(specification, page).map(fxTransactionChannelTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FxTransactionChannelTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FxTransactionChannelType> specification = createSpecification(criteria);
        return fxTransactionChannelTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FxTransactionChannelTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FxTransactionChannelType> createSpecification(FxTransactionChannelTypeCriteria criteria) {
        Specification<FxTransactionChannelType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FxTransactionChannelType_.id));
            }
            if (criteria.getFxTransactionChannelCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFxTransactionChannelCode(), FxTransactionChannelType_.fxTransactionChannelCode)
                    );
            }
            if (criteria.getFxTransactionChannelType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFxTransactionChannelType(), FxTransactionChannelType_.fxTransactionChannelType)
                    );
            }
        }
        return specification;
    }
}
