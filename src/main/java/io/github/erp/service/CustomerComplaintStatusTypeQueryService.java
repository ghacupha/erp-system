package io.github.erp.service;

/*-
 * Erp System - Mark X No 1 (Jehoiada Series) Server ver 1.7.0
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
import io.github.erp.domain.CustomerComplaintStatusType;
import io.github.erp.repository.CustomerComplaintStatusTypeRepository;
import io.github.erp.repository.search.CustomerComplaintStatusTypeSearchRepository;
import io.github.erp.service.criteria.CustomerComplaintStatusTypeCriteria;
import io.github.erp.service.dto.CustomerComplaintStatusTypeDTO;
import io.github.erp.service.mapper.CustomerComplaintStatusTypeMapper;
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
 * Service for executing complex queries for {@link CustomerComplaintStatusType} entities in the database.
 * The main input is a {@link CustomerComplaintStatusTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerComplaintStatusTypeDTO} or a {@link Page} of {@link CustomerComplaintStatusTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerComplaintStatusTypeQueryService extends QueryService<CustomerComplaintStatusType> {

    private final Logger log = LoggerFactory.getLogger(CustomerComplaintStatusTypeQueryService.class);

    private final CustomerComplaintStatusTypeRepository customerComplaintStatusTypeRepository;

    private final CustomerComplaintStatusTypeMapper customerComplaintStatusTypeMapper;

    private final CustomerComplaintStatusTypeSearchRepository customerComplaintStatusTypeSearchRepository;

    public CustomerComplaintStatusTypeQueryService(
        CustomerComplaintStatusTypeRepository customerComplaintStatusTypeRepository,
        CustomerComplaintStatusTypeMapper customerComplaintStatusTypeMapper,
        CustomerComplaintStatusTypeSearchRepository customerComplaintStatusTypeSearchRepository
    ) {
        this.customerComplaintStatusTypeRepository = customerComplaintStatusTypeRepository;
        this.customerComplaintStatusTypeMapper = customerComplaintStatusTypeMapper;
        this.customerComplaintStatusTypeSearchRepository = customerComplaintStatusTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CustomerComplaintStatusTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerComplaintStatusTypeDTO> findByCriteria(CustomerComplaintStatusTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerComplaintStatusType> specification = createSpecification(criteria);
        return customerComplaintStatusTypeMapper.toDto(customerComplaintStatusTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerComplaintStatusTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerComplaintStatusTypeDTO> findByCriteria(CustomerComplaintStatusTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerComplaintStatusType> specification = createSpecification(criteria);
        return customerComplaintStatusTypeRepository.findAll(specification, page).map(customerComplaintStatusTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerComplaintStatusTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerComplaintStatusType> specification = createSpecification(criteria);
        return customerComplaintStatusTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerComplaintStatusTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerComplaintStatusType> createSpecification(CustomerComplaintStatusTypeCriteria criteria) {
        Specification<CustomerComplaintStatusType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerComplaintStatusType_.id));
            }
            if (criteria.getCustomerComplaintStatusTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCustomerComplaintStatusTypeCode(),
                            CustomerComplaintStatusType_.customerComplaintStatusTypeCode
                        )
                    );
            }
            if (criteria.getCustomerComplaintStatusType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCustomerComplaintStatusType(),
                            CustomerComplaintStatusType_.customerComplaintStatusType
                        )
                    );
            }
        }
        return specification;
    }
}
