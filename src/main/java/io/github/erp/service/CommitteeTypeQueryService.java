package io.github.erp.service;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import io.github.erp.domain.CommitteeType;
import io.github.erp.repository.CommitteeTypeRepository;
import io.github.erp.repository.search.CommitteeTypeSearchRepository;
import io.github.erp.service.criteria.CommitteeTypeCriteria;
import io.github.erp.service.dto.CommitteeTypeDTO;
import io.github.erp.service.mapper.CommitteeTypeMapper;
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
 * Service for executing complex queries for {@link CommitteeType} entities in the database.
 * The main input is a {@link CommitteeTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CommitteeTypeDTO} or a {@link Page} of {@link CommitteeTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CommitteeTypeQueryService extends QueryService<CommitteeType> {

    private final Logger log = LoggerFactory.getLogger(CommitteeTypeQueryService.class);

    private final CommitteeTypeRepository committeeTypeRepository;

    private final CommitteeTypeMapper committeeTypeMapper;

    private final CommitteeTypeSearchRepository committeeTypeSearchRepository;

    public CommitteeTypeQueryService(
        CommitteeTypeRepository committeeTypeRepository,
        CommitteeTypeMapper committeeTypeMapper,
        CommitteeTypeSearchRepository committeeTypeSearchRepository
    ) {
        this.committeeTypeRepository = committeeTypeRepository;
        this.committeeTypeMapper = committeeTypeMapper;
        this.committeeTypeSearchRepository = committeeTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CommitteeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CommitteeTypeDTO> findByCriteria(CommitteeTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CommitteeType> specification = createSpecification(criteria);
        return committeeTypeMapper.toDto(committeeTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CommitteeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CommitteeTypeDTO> findByCriteria(CommitteeTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CommitteeType> specification = createSpecification(criteria);
        return committeeTypeRepository.findAll(specification, page).map(committeeTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CommitteeTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CommitteeType> specification = createSpecification(criteria);
        return committeeTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CommitteeTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CommitteeType> createSpecification(CommitteeTypeCriteria criteria) {
        Specification<CommitteeType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CommitteeType_.id));
            }
            if (criteria.getCommitteeTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCommitteeTypeCode(), CommitteeType_.committeeTypeCode));
            }
            if (criteria.getCommitteeType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCommitteeType(), CommitteeType_.committeeType));
            }
        }
        return specification;
    }
}