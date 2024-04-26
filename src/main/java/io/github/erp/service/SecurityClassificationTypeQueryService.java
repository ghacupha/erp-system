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
import io.github.erp.domain.SecurityClassificationType;
import io.github.erp.repository.SecurityClassificationTypeRepository;
import io.github.erp.repository.search.SecurityClassificationTypeSearchRepository;
import io.github.erp.service.criteria.SecurityClassificationTypeCriteria;
import io.github.erp.service.dto.SecurityClassificationTypeDTO;
import io.github.erp.service.mapper.SecurityClassificationTypeMapper;
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
 * Service for executing complex queries for {@link SecurityClassificationType} entities in the database.
 * The main input is a {@link SecurityClassificationTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SecurityClassificationTypeDTO} or a {@link Page} of {@link SecurityClassificationTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SecurityClassificationTypeQueryService extends QueryService<SecurityClassificationType> {

    private final Logger log = LoggerFactory.getLogger(SecurityClassificationTypeQueryService.class);

    private final SecurityClassificationTypeRepository securityClassificationTypeRepository;

    private final SecurityClassificationTypeMapper securityClassificationTypeMapper;

    private final SecurityClassificationTypeSearchRepository securityClassificationTypeSearchRepository;

    public SecurityClassificationTypeQueryService(
        SecurityClassificationTypeRepository securityClassificationTypeRepository,
        SecurityClassificationTypeMapper securityClassificationTypeMapper,
        SecurityClassificationTypeSearchRepository securityClassificationTypeSearchRepository
    ) {
        this.securityClassificationTypeRepository = securityClassificationTypeRepository;
        this.securityClassificationTypeMapper = securityClassificationTypeMapper;
        this.securityClassificationTypeSearchRepository = securityClassificationTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SecurityClassificationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SecurityClassificationTypeDTO> findByCriteria(SecurityClassificationTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SecurityClassificationType> specification = createSpecification(criteria);
        return securityClassificationTypeMapper.toDto(securityClassificationTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SecurityClassificationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SecurityClassificationTypeDTO> findByCriteria(SecurityClassificationTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SecurityClassificationType> specification = createSpecification(criteria);
        return securityClassificationTypeRepository.findAll(specification, page).map(securityClassificationTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SecurityClassificationTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SecurityClassificationType> specification = createSpecification(criteria);
        return securityClassificationTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link SecurityClassificationTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SecurityClassificationType> createSpecification(SecurityClassificationTypeCriteria criteria) {
        Specification<SecurityClassificationType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SecurityClassificationType_.id));
            }
            if (criteria.getSecurityClassificationTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSecurityClassificationTypeCode(),
                            SecurityClassificationType_.securityClassificationTypeCode
                        )
                    );
            }
            if (criteria.getSecurityClassificationType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSecurityClassificationType(),
                            SecurityClassificationType_.securityClassificationType
                        )
                    );
            }
        }
        return specification;
    }
}
