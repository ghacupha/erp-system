package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.8
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
import io.github.erp.domain.CollateralType;
import io.github.erp.repository.CollateralTypeRepository;
import io.github.erp.repository.search.CollateralTypeSearchRepository;
import io.github.erp.service.criteria.CollateralTypeCriteria;
import io.github.erp.service.dto.CollateralTypeDTO;
import io.github.erp.service.mapper.CollateralTypeMapper;
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
 * Service for executing complex queries for {@link CollateralType} entities in the database.
 * The main input is a {@link CollateralTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CollateralTypeDTO} or a {@link Page} of {@link CollateralTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CollateralTypeQueryService extends QueryService<CollateralType> {

    private final Logger log = LoggerFactory.getLogger(CollateralTypeQueryService.class);

    private final CollateralTypeRepository collateralTypeRepository;

    private final CollateralTypeMapper collateralTypeMapper;

    private final CollateralTypeSearchRepository collateralTypeSearchRepository;

    public CollateralTypeQueryService(
        CollateralTypeRepository collateralTypeRepository,
        CollateralTypeMapper collateralTypeMapper,
        CollateralTypeSearchRepository collateralTypeSearchRepository
    ) {
        this.collateralTypeRepository = collateralTypeRepository;
        this.collateralTypeMapper = collateralTypeMapper;
        this.collateralTypeSearchRepository = collateralTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CollateralTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CollateralTypeDTO> findByCriteria(CollateralTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CollateralType> specification = createSpecification(criteria);
        return collateralTypeMapper.toDto(collateralTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CollateralTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CollateralTypeDTO> findByCriteria(CollateralTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CollateralType> specification = createSpecification(criteria);
        return collateralTypeRepository.findAll(specification, page).map(collateralTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CollateralTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CollateralType> specification = createSpecification(criteria);
        return collateralTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CollateralTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CollateralType> createSpecification(CollateralTypeCriteria criteria) {
        Specification<CollateralType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CollateralType_.id));
            }
            if (criteria.getCollateralTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCollateralTypeCode(), CollateralType_.collateralTypeCode));
            }
            if (criteria.getCollateralType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCollateralType(), CollateralType_.collateralType));
            }
        }
        return specification;
    }
}
