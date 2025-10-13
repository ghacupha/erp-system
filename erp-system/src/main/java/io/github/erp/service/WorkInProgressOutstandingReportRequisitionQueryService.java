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
import io.github.erp.domain.WorkInProgressOutstandingReportRequisition;
import io.github.erp.repository.WorkInProgressOutstandingReportRequisitionRepository;
import io.github.erp.repository.search.WorkInProgressOutstandingReportRequisitionSearchRepository;
import io.github.erp.service.criteria.WorkInProgressOutstandingReportRequisitionCriteria;
import io.github.erp.service.dto.WorkInProgressOutstandingReportRequisitionDTO;
import io.github.erp.service.mapper.WorkInProgressOutstandingReportRequisitionMapper;
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
 * Service for executing complex queries for {@link WorkInProgressOutstandingReportRequisition} entities in the database.
 * The main input is a {@link WorkInProgressOutstandingReportRequisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WorkInProgressOutstandingReportRequisitionDTO} or a {@link Page} of {@link WorkInProgressOutstandingReportRequisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkInProgressOutstandingReportRequisitionQueryService extends QueryService<WorkInProgressOutstandingReportRequisition> {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressOutstandingReportRequisitionQueryService.class);

    private final WorkInProgressOutstandingReportRequisitionRepository workInProgressOutstandingReportRequisitionRepository;

    private final WorkInProgressOutstandingReportRequisitionMapper workInProgressOutstandingReportRequisitionMapper;

    private final WorkInProgressOutstandingReportRequisitionSearchRepository workInProgressOutstandingReportRequisitionSearchRepository;

    public WorkInProgressOutstandingReportRequisitionQueryService(
        WorkInProgressOutstandingReportRequisitionRepository workInProgressOutstandingReportRequisitionRepository,
        WorkInProgressOutstandingReportRequisitionMapper workInProgressOutstandingReportRequisitionMapper,
        WorkInProgressOutstandingReportRequisitionSearchRepository workInProgressOutstandingReportRequisitionSearchRepository
    ) {
        this.workInProgressOutstandingReportRequisitionRepository = workInProgressOutstandingReportRequisitionRepository;
        this.workInProgressOutstandingReportRequisitionMapper = workInProgressOutstandingReportRequisitionMapper;
        this.workInProgressOutstandingReportRequisitionSearchRepository = workInProgressOutstandingReportRequisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WorkInProgressOutstandingReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WorkInProgressOutstandingReportRequisitionDTO> findByCriteria(WorkInProgressOutstandingReportRequisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WorkInProgressOutstandingReportRequisition> specification = createSpecification(criteria);
        return workInProgressOutstandingReportRequisitionMapper.toDto(
            workInProgressOutstandingReportRequisitionRepository.findAll(specification)
        );
    }

    /**
     * Return a {@link Page} of {@link WorkInProgressOutstandingReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkInProgressOutstandingReportRequisitionDTO> findByCriteria(
        WorkInProgressOutstandingReportRequisitionCriteria criteria,
        Pageable page
    ) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkInProgressOutstandingReportRequisition> specification = createSpecification(criteria);
        return workInProgressOutstandingReportRequisitionRepository
            .findAll(specification, page)
            .map(workInProgressOutstandingReportRequisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkInProgressOutstandingReportRequisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WorkInProgressOutstandingReportRequisition> specification = createSpecification(criteria);
        return workInProgressOutstandingReportRequisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkInProgressOutstandingReportRequisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkInProgressOutstandingReportRequisition> createSpecification(
        WorkInProgressOutstandingReportRequisitionCriteria criteria
    ) {
        Specification<WorkInProgressOutstandingReportRequisition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getId(), WorkInProgressOutstandingReportRequisition_.id));
            }
            if (criteria.getRequestId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getRequestId(), WorkInProgressOutstandingReportRequisition_.requestId));
            }
            if (criteria.getReportDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getReportDate(), WorkInProgressOutstandingReportRequisition_.reportDate)
                    );
            }
            if (criteria.getTimeOfRequisition() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTimeOfRequisition(),
                            WorkInProgressOutstandingReportRequisition_.timeOfRequisition
                        )
                    );
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFileChecksum(), WorkInProgressOutstandingReportRequisition_.fileChecksum)
                    );
            }
            if (criteria.getTampered() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getTampered(), WorkInProgressOutstandingReportRequisition_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getFilename(), WorkInProgressOutstandingReportRequisition_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getReportParameters(),
                            WorkInProgressOutstandingReportRequisition_.reportParameters
                        )
                    );
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root ->
                                root.join(WorkInProgressOutstandingReportRequisition_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastAccessedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastAccessedById(),
                            root ->
                                root
                                    .join(WorkInProgressOutstandingReportRequisition_.lastAccessedBy, JoinType.LEFT)
                                    .get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
