package io.github.erp.service;

/*-
 * Erp System - Mark VII No 2 (Gideon Series) Server ver 1.5.6
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
import io.github.erp.domain.FxCustomerType;
import io.github.erp.repository.FxCustomerTypeRepository;
import io.github.erp.repository.search.FxCustomerTypeSearchRepository;
import io.github.erp.service.criteria.FxCustomerTypeCriteria;
import io.github.erp.service.dto.FxCustomerTypeDTO;
import io.github.erp.service.mapper.FxCustomerTypeMapper;
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
 * Service for executing complex queries for {@link FxCustomerType} entities in the database.
 * The main input is a {@link FxCustomerTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FxCustomerTypeDTO} or a {@link Page} of {@link FxCustomerTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FxCustomerTypeQueryService extends QueryService<FxCustomerType> {

    private final Logger log = LoggerFactory.getLogger(FxCustomerTypeQueryService.class);

    private final FxCustomerTypeRepository fxCustomerTypeRepository;

    private final FxCustomerTypeMapper fxCustomerTypeMapper;

    private final FxCustomerTypeSearchRepository fxCustomerTypeSearchRepository;

    public FxCustomerTypeQueryService(
        FxCustomerTypeRepository fxCustomerTypeRepository,
        FxCustomerTypeMapper fxCustomerTypeMapper,
        FxCustomerTypeSearchRepository fxCustomerTypeSearchRepository
    ) {
        this.fxCustomerTypeRepository = fxCustomerTypeRepository;
        this.fxCustomerTypeMapper = fxCustomerTypeMapper;
        this.fxCustomerTypeSearchRepository = fxCustomerTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FxCustomerTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FxCustomerTypeDTO> findByCriteria(FxCustomerTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FxCustomerType> specification = createSpecification(criteria);
        return fxCustomerTypeMapper.toDto(fxCustomerTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FxCustomerTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FxCustomerTypeDTO> findByCriteria(FxCustomerTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FxCustomerType> specification = createSpecification(criteria);
        return fxCustomerTypeRepository.findAll(specification, page).map(fxCustomerTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FxCustomerTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FxCustomerType> specification = createSpecification(criteria);
        return fxCustomerTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FxCustomerTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FxCustomerType> createSpecification(FxCustomerTypeCriteria criteria) {
        Specification<FxCustomerType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FxCustomerType_.id));
            }
            if (criteria.getForeignExchangeCustomerTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getForeignExchangeCustomerTypeCode(),
                            FxCustomerType_.foreignExchangeCustomerTypeCode
                        )
                    );
            }
            if (criteria.getForeignCustomerType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getForeignCustomerType(), FxCustomerType_.foreignCustomerType));
            }
        }
        return specification;
    }
}
