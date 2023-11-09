package io.github.erp.service;

/*-
 * Erp System - Mark VII No 1 (Gideon Series) Server ver 1.5.5
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
import io.github.erp.domain.WorkInProgressOutstandingReport;
import io.github.erp.repository.WorkInProgressOutstandingReportRepository;
import io.github.erp.repository.search.WorkInProgressOutstandingReportSearchRepository;
import io.github.erp.service.criteria.WorkInProgressOutstandingReportCriteria;
import io.github.erp.service.dto.WorkInProgressOutstandingReportDTO;
import io.github.erp.service.mapper.WorkInProgressOutstandingReportMapper;
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
 * Service for executing complex queries for {@link WorkInProgressOutstandingReport} entities in the database.
 * The main input is a {@link WorkInProgressOutstandingReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WorkInProgressOutstandingReportDTO} or a {@link Page} of {@link WorkInProgressOutstandingReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkInProgressOutstandingReportQueryService extends QueryService<WorkInProgressOutstandingReport> {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressOutstandingReportQueryService.class);

    private final WorkInProgressOutstandingReportRepository workInProgressOutstandingReportRepository;

    private final WorkInProgressOutstandingReportMapper workInProgressOutstandingReportMapper;

    private final WorkInProgressOutstandingReportSearchRepository workInProgressOutstandingReportSearchRepository;

    public WorkInProgressOutstandingReportQueryService(
        WorkInProgressOutstandingReportRepository workInProgressOutstandingReportRepository,
        WorkInProgressOutstandingReportMapper workInProgressOutstandingReportMapper,
        WorkInProgressOutstandingReportSearchRepository workInProgressOutstandingReportSearchRepository
    ) {
        this.workInProgressOutstandingReportRepository = workInProgressOutstandingReportRepository;
        this.workInProgressOutstandingReportMapper = workInProgressOutstandingReportMapper;
        this.workInProgressOutstandingReportSearchRepository = workInProgressOutstandingReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WorkInProgressOutstandingReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WorkInProgressOutstandingReportDTO> findByCriteria(WorkInProgressOutstandingReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WorkInProgressOutstandingReport> specification = createSpecification(criteria);
        return workInProgressOutstandingReportMapper.toDto(workInProgressOutstandingReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WorkInProgressOutstandingReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkInProgressOutstandingReportDTO> findByCriteria(WorkInProgressOutstandingReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkInProgressOutstandingReport> specification = createSpecification(criteria);
        return workInProgressOutstandingReportRepository.findAll(specification, page).map(workInProgressOutstandingReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkInProgressOutstandingReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WorkInProgressOutstandingReport> specification = createSpecification(criteria);
        return workInProgressOutstandingReportRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkInProgressOutstandingReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkInProgressOutstandingReport> createSpecification(WorkInProgressOutstandingReportCriteria criteria) {
        Specification<WorkInProgressOutstandingReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WorkInProgressOutstandingReport_.id));
            }
            if (criteria.getSequenceNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getSequenceNumber(), WorkInProgressOutstandingReport_.sequenceNumber)
                    );
            }
            if (criteria.getParticulars() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getParticulars(), WorkInProgressOutstandingReport_.particulars));
            }
            if (criteria.getDealerName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDealerName(), WorkInProgressOutstandingReport_.dealerName));
            }
            if (criteria.getIso4217Code() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getIso4217Code(), WorkInProgressOutstandingReport_.iso4217Code));
            }
            if (criteria.getInstalmentAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getInstalmentAmount(), WorkInProgressOutstandingReport_.instalmentAmount)
                    );
            }
            if (criteria.getTotalTransferAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTotalTransferAmount(), WorkInProgressOutstandingReport_.totalTransferAmount)
                    );
            }
            if (criteria.getOutstandingAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getOutstandingAmount(), WorkInProgressOutstandingReport_.outstandingAmount)
                    );
            }
        }
        return specification;
    }
}
