package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import io.github.erp.domain.OutletStatus;
import io.github.erp.repository.OutletStatusRepository;
import io.github.erp.repository.search.OutletStatusSearchRepository;
import io.github.erp.service.criteria.OutletStatusCriteria;
import io.github.erp.service.dto.OutletStatusDTO;
import io.github.erp.service.mapper.OutletStatusMapper;
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
 * Service for executing complex queries for {@link OutletStatus} entities in the database.
 * The main input is a {@link OutletStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OutletStatusDTO} or a {@link Page} of {@link OutletStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OutletStatusQueryService extends QueryService<OutletStatus> {

    private final Logger log = LoggerFactory.getLogger(OutletStatusQueryService.class);

    private final OutletStatusRepository outletStatusRepository;

    private final OutletStatusMapper outletStatusMapper;

    private final OutletStatusSearchRepository outletStatusSearchRepository;

    public OutletStatusQueryService(
        OutletStatusRepository outletStatusRepository,
        OutletStatusMapper outletStatusMapper,
        OutletStatusSearchRepository outletStatusSearchRepository
    ) {
        this.outletStatusRepository = outletStatusRepository;
        this.outletStatusMapper = outletStatusMapper;
        this.outletStatusSearchRepository = outletStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OutletStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OutletStatusDTO> findByCriteria(OutletStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OutletStatus> specification = createSpecification(criteria);
        return outletStatusMapper.toDto(outletStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OutletStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OutletStatusDTO> findByCriteria(OutletStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OutletStatus> specification = createSpecification(criteria);
        return outletStatusRepository.findAll(specification, page).map(outletStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OutletStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OutletStatus> specification = createSpecification(criteria);
        return outletStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link OutletStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OutletStatus> createSpecification(OutletStatusCriteria criteria) {
        Specification<OutletStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OutletStatus_.id));
            }
            if (criteria.getBranchStatusTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getBranchStatusTypeCode(), OutletStatus_.branchStatusTypeCode));
            }
            if (criteria.getBranchStatusType() != null) {
                specification = specification.and(buildSpecification(criteria.getBranchStatusType(), OutletStatus_.branchStatusType));
            }
            if (criteria.getBranchStatusTypeDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getBranchStatusTypeDescription(), OutletStatus_.branchStatusTypeDescription)
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(OutletStatus_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
