package io.github.erp.service;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.StaffRoleType;
import io.github.erp.repository.StaffRoleTypeRepository;
import io.github.erp.repository.search.StaffRoleTypeSearchRepository;
import io.github.erp.service.criteria.StaffRoleTypeCriteria;
import io.github.erp.service.dto.StaffRoleTypeDTO;
import io.github.erp.service.mapper.StaffRoleTypeMapper;
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
 * Service for executing complex queries for {@link StaffRoleType} entities in the database.
 * The main input is a {@link StaffRoleTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StaffRoleTypeDTO} or a {@link Page} of {@link StaffRoleTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StaffRoleTypeQueryService extends QueryService<StaffRoleType> {

    private final Logger log = LoggerFactory.getLogger(StaffRoleTypeQueryService.class);

    private final StaffRoleTypeRepository staffRoleTypeRepository;

    private final StaffRoleTypeMapper staffRoleTypeMapper;

    private final StaffRoleTypeSearchRepository staffRoleTypeSearchRepository;

    public StaffRoleTypeQueryService(
        StaffRoleTypeRepository staffRoleTypeRepository,
        StaffRoleTypeMapper staffRoleTypeMapper,
        StaffRoleTypeSearchRepository staffRoleTypeSearchRepository
    ) {
        this.staffRoleTypeRepository = staffRoleTypeRepository;
        this.staffRoleTypeMapper = staffRoleTypeMapper;
        this.staffRoleTypeSearchRepository = staffRoleTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StaffRoleTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StaffRoleTypeDTO> findByCriteria(StaffRoleTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StaffRoleType> specification = createSpecification(criteria);
        return staffRoleTypeMapper.toDto(staffRoleTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StaffRoleTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StaffRoleTypeDTO> findByCriteria(StaffRoleTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StaffRoleType> specification = createSpecification(criteria);
        return staffRoleTypeRepository.findAll(specification, page).map(staffRoleTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StaffRoleTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StaffRoleType> specification = createSpecification(criteria);
        return staffRoleTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link StaffRoleTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StaffRoleType> createSpecification(StaffRoleTypeCriteria criteria) {
        Specification<StaffRoleType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StaffRoleType_.id));
            }
            if (criteria.getStaffRoleTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getStaffRoleTypeCode(), StaffRoleType_.staffRoleTypeCode));
            }
            if (criteria.getStaffRoleType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStaffRoleType(), StaffRoleType_.staffRoleType));
            }
        }
        return specification;
    }
}
