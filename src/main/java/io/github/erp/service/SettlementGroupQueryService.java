package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.SettlementGroup;
import io.github.erp.repository.SettlementGroupRepository;
import io.github.erp.service.criteria.SettlementGroupCriteria;
import io.github.erp.service.dto.SettlementGroupDTO;
import io.github.erp.service.mapper.SettlementGroupMapper;
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
 * Service for executing complex queries for {@link SettlementGroup} entities in the database.
 * The main input is a {@link SettlementGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SettlementGroupDTO} or a {@link Page} of {@link SettlementGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SettlementGroupQueryService extends QueryService<SettlementGroup> {

    private final Logger log = LoggerFactory.getLogger(SettlementGroupQueryService.class);

    private final SettlementGroupRepository settlementGroupRepository;

    private final SettlementGroupMapper settlementGroupMapper;

    public SettlementGroupQueryService(SettlementGroupRepository settlementGroupRepository, SettlementGroupMapper settlementGroupMapper) {
        this.settlementGroupRepository = settlementGroupRepository;
        this.settlementGroupMapper = settlementGroupMapper;
    }

    /**
     * Return a {@link List} of {@link SettlementGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SettlementGroupDTO> findByCriteria(SettlementGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SettlementGroup> specification = createSpecification(criteria);
        return settlementGroupMapper.toDto(settlementGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SettlementGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SettlementGroupDTO> findByCriteria(SettlementGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SettlementGroup> specification = createSpecification(criteria);
        return settlementGroupRepository.findAll(specification, page).map(settlementGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SettlementGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SettlementGroup> specification = createSpecification(criteria);
        return settlementGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link SettlementGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SettlementGroup> createSpecification(SettlementGroupCriteria criteria) {
        Specification<SettlementGroup> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SettlementGroup_.id));
            }
            if (criteria.getGroupName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGroupName(), SettlementGroup_.groupName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), SettlementGroup_.description));
            }
            if (criteria.getParentGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParentGroupId(),
                            root -> root.join(SettlementGroup_.parentGroup, JoinType.LEFT).get(SettlementGroup_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(SettlementGroup_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
