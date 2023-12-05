package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
import io.github.erp.domain.CrbCustomerType;
import io.github.erp.repository.CrbCustomerTypeRepository;
import io.github.erp.repository.search.CrbCustomerTypeSearchRepository;
import io.github.erp.service.criteria.CrbCustomerTypeCriteria;
import io.github.erp.service.dto.CrbCustomerTypeDTO;
import io.github.erp.service.mapper.CrbCustomerTypeMapper;
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
 * Service for executing complex queries for {@link CrbCustomerType} entities in the database.
 * The main input is a {@link CrbCustomerTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbCustomerTypeDTO} or a {@link Page} of {@link CrbCustomerTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbCustomerTypeQueryService extends QueryService<CrbCustomerType> {

    private final Logger log = LoggerFactory.getLogger(CrbCustomerTypeQueryService.class);

    private final CrbCustomerTypeRepository crbCustomerTypeRepository;

    private final CrbCustomerTypeMapper crbCustomerTypeMapper;

    private final CrbCustomerTypeSearchRepository crbCustomerTypeSearchRepository;

    public CrbCustomerTypeQueryService(
        CrbCustomerTypeRepository crbCustomerTypeRepository,
        CrbCustomerTypeMapper crbCustomerTypeMapper,
        CrbCustomerTypeSearchRepository crbCustomerTypeSearchRepository
    ) {
        this.crbCustomerTypeRepository = crbCustomerTypeRepository;
        this.crbCustomerTypeMapper = crbCustomerTypeMapper;
        this.crbCustomerTypeSearchRepository = crbCustomerTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbCustomerTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbCustomerTypeDTO> findByCriteria(CrbCustomerTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbCustomerType> specification = createSpecification(criteria);
        return crbCustomerTypeMapper.toDto(crbCustomerTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbCustomerTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbCustomerTypeDTO> findByCriteria(CrbCustomerTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbCustomerType> specification = createSpecification(criteria);
        return crbCustomerTypeRepository.findAll(specification, page).map(crbCustomerTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbCustomerTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbCustomerType> specification = createSpecification(criteria);
        return crbCustomerTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbCustomerTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbCustomerType> createSpecification(CrbCustomerTypeCriteria criteria) {
        Specification<CrbCustomerType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbCustomerType_.id));
            }
            if (criteria.getCustomerTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCustomerTypeCode(), CrbCustomerType_.customerTypeCode));
            }
            if (criteria.getCustomerType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerType(), CrbCustomerType_.customerType));
            }
        }
        return specification;
    }
}
