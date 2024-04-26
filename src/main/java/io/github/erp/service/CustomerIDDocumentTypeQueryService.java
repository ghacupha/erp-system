package io.github.erp.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.CustomerIDDocumentType;
import io.github.erp.repository.CustomerIDDocumentTypeRepository;
import io.github.erp.repository.search.CustomerIDDocumentTypeSearchRepository;
import io.github.erp.service.criteria.CustomerIDDocumentTypeCriteria;
import io.github.erp.service.dto.CustomerIDDocumentTypeDTO;
import io.github.erp.service.mapper.CustomerIDDocumentTypeMapper;
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
 * Service for executing complex queries for {@link CustomerIDDocumentType} entities in the database.
 * The main input is a {@link CustomerIDDocumentTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CustomerIDDocumentTypeDTO} or a {@link Page} of {@link CustomerIDDocumentTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CustomerIDDocumentTypeQueryService extends QueryService<CustomerIDDocumentType> {

    private final Logger log = LoggerFactory.getLogger(CustomerIDDocumentTypeQueryService.class);

    private final CustomerIDDocumentTypeRepository customerIDDocumentTypeRepository;

    private final CustomerIDDocumentTypeMapper customerIDDocumentTypeMapper;

    private final CustomerIDDocumentTypeSearchRepository customerIDDocumentTypeSearchRepository;

    public CustomerIDDocumentTypeQueryService(
        CustomerIDDocumentTypeRepository customerIDDocumentTypeRepository,
        CustomerIDDocumentTypeMapper customerIDDocumentTypeMapper,
        CustomerIDDocumentTypeSearchRepository customerIDDocumentTypeSearchRepository
    ) {
        this.customerIDDocumentTypeRepository = customerIDDocumentTypeRepository;
        this.customerIDDocumentTypeMapper = customerIDDocumentTypeMapper;
        this.customerIDDocumentTypeSearchRepository = customerIDDocumentTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CustomerIDDocumentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CustomerIDDocumentTypeDTO> findByCriteria(CustomerIDDocumentTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CustomerIDDocumentType> specification = createSpecification(criteria);
        return customerIDDocumentTypeMapper.toDto(customerIDDocumentTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CustomerIDDocumentTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CustomerIDDocumentTypeDTO> findByCriteria(CustomerIDDocumentTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CustomerIDDocumentType> specification = createSpecification(criteria);
        return customerIDDocumentTypeRepository.findAll(specification, page).map(customerIDDocumentTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CustomerIDDocumentTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CustomerIDDocumentType> specification = createSpecification(criteria);
        return customerIDDocumentTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CustomerIDDocumentTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CustomerIDDocumentType> createSpecification(CustomerIDDocumentTypeCriteria criteria) {
        Specification<CustomerIDDocumentType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CustomerIDDocumentType_.id));
            }
            if (criteria.getDocumentCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDocumentCode(), CustomerIDDocumentType_.documentCode));
            }
            if (criteria.getDocumentType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDocumentType(), CustomerIDDocumentType_.documentType));
            }
            if (criteria.getDocumentTypeDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getDocumentTypeDescription(), CustomerIDDocumentType_.documentTypeDescription)
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(CustomerIDDocumentType_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
