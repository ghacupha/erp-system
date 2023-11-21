package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
