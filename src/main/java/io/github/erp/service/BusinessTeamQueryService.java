package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.BusinessTeam;
import io.github.erp.repository.BusinessTeamRepository;
import io.github.erp.repository.search.BusinessTeamSearchRepository;
import io.github.erp.service.criteria.BusinessTeamCriteria;
import io.github.erp.service.dto.BusinessTeamDTO;
import io.github.erp.service.mapper.BusinessTeamMapper;
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
 * Service for executing complex queries for {@link BusinessTeam} entities in the database.
 * The main input is a {@link BusinessTeamCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BusinessTeamDTO} or a {@link Page} of {@link BusinessTeamDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BusinessTeamQueryService extends QueryService<BusinessTeam> {

    private final Logger log = LoggerFactory.getLogger(BusinessTeamQueryService.class);

    private final BusinessTeamRepository businessTeamRepository;

    private final BusinessTeamMapper businessTeamMapper;

    private final BusinessTeamSearchRepository businessTeamSearchRepository;

    public BusinessTeamQueryService(
        BusinessTeamRepository businessTeamRepository,
        BusinessTeamMapper businessTeamMapper,
        BusinessTeamSearchRepository businessTeamSearchRepository
    ) {
        this.businessTeamRepository = businessTeamRepository;
        this.businessTeamMapper = businessTeamMapper;
        this.businessTeamSearchRepository = businessTeamSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BusinessTeamDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BusinessTeamDTO> findByCriteria(BusinessTeamCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BusinessTeam> specification = createSpecification(criteria);
        return businessTeamMapper.toDto(businessTeamRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BusinessTeamDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BusinessTeamDTO> findByCriteria(BusinessTeamCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BusinessTeam> specification = createSpecification(criteria);
        return businessTeamRepository.findAll(specification, page).map(businessTeamMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BusinessTeamCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BusinessTeam> specification = createSpecification(criteria);
        return businessTeamRepository.count(specification);
    }

    /**
     * Function to convert {@link BusinessTeamCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BusinessTeam> createSpecification(BusinessTeamCriteria criteria) {
        Specification<BusinessTeam> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BusinessTeam_.id));
            }
            if (criteria.getBusinessTeam() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBusinessTeam(), BusinessTeam_.businessTeam));
            }
            if (criteria.getTeamMembersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTeamMembersId(),
                            root -> root.join(BusinessTeam_.teamMembers, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
