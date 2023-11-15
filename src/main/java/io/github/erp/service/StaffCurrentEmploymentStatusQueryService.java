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
import io.github.erp.domain.StaffCurrentEmploymentStatus;
import io.github.erp.repository.StaffCurrentEmploymentStatusRepository;
import io.github.erp.repository.search.StaffCurrentEmploymentStatusSearchRepository;
import io.github.erp.service.criteria.StaffCurrentEmploymentStatusCriteria;
import io.github.erp.service.dto.StaffCurrentEmploymentStatusDTO;
import io.github.erp.service.mapper.StaffCurrentEmploymentStatusMapper;
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
 * Service for executing complex queries for {@link StaffCurrentEmploymentStatus} entities in the database.
 * The main input is a {@link StaffCurrentEmploymentStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link StaffCurrentEmploymentStatusDTO} or a {@link Page} of {@link StaffCurrentEmploymentStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class StaffCurrentEmploymentStatusQueryService extends QueryService<StaffCurrentEmploymentStatus> {

    private final Logger log = LoggerFactory.getLogger(StaffCurrentEmploymentStatusQueryService.class);

    private final StaffCurrentEmploymentStatusRepository staffCurrentEmploymentStatusRepository;

    private final StaffCurrentEmploymentStatusMapper staffCurrentEmploymentStatusMapper;

    private final StaffCurrentEmploymentStatusSearchRepository staffCurrentEmploymentStatusSearchRepository;

    public StaffCurrentEmploymentStatusQueryService(
        StaffCurrentEmploymentStatusRepository staffCurrentEmploymentStatusRepository,
        StaffCurrentEmploymentStatusMapper staffCurrentEmploymentStatusMapper,
        StaffCurrentEmploymentStatusSearchRepository staffCurrentEmploymentStatusSearchRepository
    ) {
        this.staffCurrentEmploymentStatusRepository = staffCurrentEmploymentStatusRepository;
        this.staffCurrentEmploymentStatusMapper = staffCurrentEmploymentStatusMapper;
        this.staffCurrentEmploymentStatusSearchRepository = staffCurrentEmploymentStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link StaffCurrentEmploymentStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<StaffCurrentEmploymentStatusDTO> findByCriteria(StaffCurrentEmploymentStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<StaffCurrentEmploymentStatus> specification = createSpecification(criteria);
        return staffCurrentEmploymentStatusMapper.toDto(staffCurrentEmploymentStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link StaffCurrentEmploymentStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<StaffCurrentEmploymentStatusDTO> findByCriteria(StaffCurrentEmploymentStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<StaffCurrentEmploymentStatus> specification = createSpecification(criteria);
        return staffCurrentEmploymentStatusRepository.findAll(specification, page).map(staffCurrentEmploymentStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(StaffCurrentEmploymentStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<StaffCurrentEmploymentStatus> specification = createSpecification(criteria);
        return staffCurrentEmploymentStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link StaffCurrentEmploymentStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<StaffCurrentEmploymentStatus> createSpecification(StaffCurrentEmploymentStatusCriteria criteria) {
        Specification<StaffCurrentEmploymentStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), StaffCurrentEmploymentStatus_.id));
            }
            if (criteria.getStaffCurrentEmploymentStatusTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getStaffCurrentEmploymentStatusTypeCode(),
                            StaffCurrentEmploymentStatus_.staffCurrentEmploymentStatusTypeCode
                        )
                    );
            }
            if (criteria.getStaffCurrentEmploymentStatusType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getStaffCurrentEmploymentStatusType(),
                            StaffCurrentEmploymentStatus_.staffCurrentEmploymentStatusType
                        )
                    );
            }
        }
        return specification;
    }
}
