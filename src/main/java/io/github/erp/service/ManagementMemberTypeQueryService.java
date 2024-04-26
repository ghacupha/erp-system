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
import io.github.erp.domain.ManagementMemberType;
import io.github.erp.repository.ManagementMemberTypeRepository;
import io.github.erp.repository.search.ManagementMemberTypeSearchRepository;
import io.github.erp.service.criteria.ManagementMemberTypeCriteria;
import io.github.erp.service.dto.ManagementMemberTypeDTO;
import io.github.erp.service.mapper.ManagementMemberTypeMapper;
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
 * Service for executing complex queries for {@link ManagementMemberType} entities in the database.
 * The main input is a {@link ManagementMemberTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ManagementMemberTypeDTO} or a {@link Page} of {@link ManagementMemberTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ManagementMemberTypeQueryService extends QueryService<ManagementMemberType> {

    private final Logger log = LoggerFactory.getLogger(ManagementMemberTypeQueryService.class);

    private final ManagementMemberTypeRepository managementMemberTypeRepository;

    private final ManagementMemberTypeMapper managementMemberTypeMapper;

    private final ManagementMemberTypeSearchRepository managementMemberTypeSearchRepository;

    public ManagementMemberTypeQueryService(
        ManagementMemberTypeRepository managementMemberTypeRepository,
        ManagementMemberTypeMapper managementMemberTypeMapper,
        ManagementMemberTypeSearchRepository managementMemberTypeSearchRepository
    ) {
        this.managementMemberTypeRepository = managementMemberTypeRepository;
        this.managementMemberTypeMapper = managementMemberTypeMapper;
        this.managementMemberTypeSearchRepository = managementMemberTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ManagementMemberTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ManagementMemberTypeDTO> findByCriteria(ManagementMemberTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ManagementMemberType> specification = createSpecification(criteria);
        return managementMemberTypeMapper.toDto(managementMemberTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ManagementMemberTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ManagementMemberTypeDTO> findByCriteria(ManagementMemberTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ManagementMemberType> specification = createSpecification(criteria);
        return managementMemberTypeRepository.findAll(specification, page).map(managementMemberTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ManagementMemberTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ManagementMemberType> specification = createSpecification(criteria);
        return managementMemberTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ManagementMemberTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ManagementMemberType> createSpecification(ManagementMemberTypeCriteria criteria) {
        Specification<ManagementMemberType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ManagementMemberType_.id));
            }
            if (criteria.getManagementMemberTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getManagementMemberTypeCode(), ManagementMemberType_.managementMemberTypeCode)
                    );
            }
            if (criteria.getManagementMemberType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getManagementMemberType(), ManagementMemberType_.managementMemberType)
                    );
            }
        }
        return specification;
    }
}
