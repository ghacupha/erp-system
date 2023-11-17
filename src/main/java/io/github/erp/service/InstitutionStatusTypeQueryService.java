package io.github.erp.service;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import io.github.erp.domain.InstitutionStatusType;
import io.github.erp.repository.InstitutionStatusTypeRepository;
import io.github.erp.repository.search.InstitutionStatusTypeSearchRepository;
import io.github.erp.service.criteria.InstitutionStatusTypeCriteria;
import io.github.erp.service.dto.InstitutionStatusTypeDTO;
import io.github.erp.service.mapper.InstitutionStatusTypeMapper;
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
 * Service for executing complex queries for {@link InstitutionStatusType} entities in the database.
 * The main input is a {@link InstitutionStatusTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InstitutionStatusTypeDTO} or a {@link Page} of {@link InstitutionStatusTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InstitutionStatusTypeQueryService extends QueryService<InstitutionStatusType> {

    private final Logger log = LoggerFactory.getLogger(InstitutionStatusTypeQueryService.class);

    private final InstitutionStatusTypeRepository institutionStatusTypeRepository;

    private final InstitutionStatusTypeMapper institutionStatusTypeMapper;

    private final InstitutionStatusTypeSearchRepository institutionStatusTypeSearchRepository;

    public InstitutionStatusTypeQueryService(
        InstitutionStatusTypeRepository institutionStatusTypeRepository,
        InstitutionStatusTypeMapper institutionStatusTypeMapper,
        InstitutionStatusTypeSearchRepository institutionStatusTypeSearchRepository
    ) {
        this.institutionStatusTypeRepository = institutionStatusTypeRepository;
        this.institutionStatusTypeMapper = institutionStatusTypeMapper;
        this.institutionStatusTypeSearchRepository = institutionStatusTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InstitutionStatusTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InstitutionStatusTypeDTO> findByCriteria(InstitutionStatusTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InstitutionStatusType> specification = createSpecification(criteria);
        return institutionStatusTypeMapper.toDto(institutionStatusTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InstitutionStatusTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InstitutionStatusTypeDTO> findByCriteria(InstitutionStatusTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InstitutionStatusType> specification = createSpecification(criteria);
        return institutionStatusTypeRepository.findAll(specification, page).map(institutionStatusTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InstitutionStatusTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InstitutionStatusType> specification = createSpecification(criteria);
        return institutionStatusTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link InstitutionStatusTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InstitutionStatusType> createSpecification(InstitutionStatusTypeCriteria criteria) {
        Specification<InstitutionStatusType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InstitutionStatusType_.id));
            }
            if (criteria.getInstitutionStatusCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getInstitutionStatusCode(), InstitutionStatusType_.institutionStatusCode)
                    );
            }
            if (criteria.getInstitutionStatusType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getInstitutionStatusType(), InstitutionStatusType_.institutionStatusType)
                    );
            }
        }
        return specification;
    }
}
