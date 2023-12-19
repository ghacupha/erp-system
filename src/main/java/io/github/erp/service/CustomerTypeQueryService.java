package io.github.erp.service;

/*-
 * Erp System - Mark IX No 3 (Iddo Series) Server ver 1.6.5
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
import io.github.erp.domain.CustomerType;
import io.github.erp.repository.CustomerTypeRepository;
import io.github.erp.repository.search.CustomerTypeSearchRepository;
import io.github.erp.service.criteria.CustomerTypeCriteria;
import io.github.erp.service.dto.CustomerTypeDTO;
import io.github.erp.service.mapper.CustomerTypeMapper;
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
 * Service for executing complex queries for {@link CustomerType} entities in the database.
 * The main input is a {@link CustomerTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerTypeDTO} or a {@link Page} of {@link CustomerTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerTypeQueryService extends QueryService<CustomerType> {

    private final Logger log = LoggerFactory.getLogger(CustomerTypeQueryService.class);

    private final CustomerTypeRepository customerTypeRepository;

    private final CustomerTypeMapper customerTypeMapper;

    private final CustomerTypeSearchRepository customerTypeSearchRepository;

    public CustomerTypeQueryService(
        CustomerTypeRepository customerTypeRepository,
        CustomerTypeMapper customerTypeMapper,
        CustomerTypeSearchRepository customerTypeSearchRepository
    ) {
        this.customerTypeRepository = customerTypeRepository;
        this.customerTypeMapper = customerTypeMapper;
        this.customerTypeSearchRepository = customerTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CustomerTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerTypeDTO> findByCriteria(CustomerTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerType> specification = createSpecification(criteria);
        return customerTypeMapper.toDto(customerTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerTypeDTO> findByCriteria(CustomerTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerType> specification = createSpecification(criteria);
        return customerTypeRepository.findAll(specification, page).map(customerTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerType> specification = createSpecification(criteria);
        return customerTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerType> createSpecification(CustomerTypeCriteria criteria) {
        Specification<CustomerType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerType_.id));
            }
            if (criteria.getCustomerTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerTypeCode(), CustomerType_.customerTypeCode));
            }
            if (criteria.getCustomerType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerType(), CustomerType_.customerType));
            }
        }
        return specification;
    }
}
