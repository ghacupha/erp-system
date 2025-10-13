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
import io.github.erp.domain.LeaseLiabilityScheduleReport;
import io.github.erp.repository.LeaseLiabilityScheduleReportRepository;
import io.github.erp.repository.search.LeaseLiabilityScheduleReportSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityScheduleReportCriteria;
import io.github.erp.service.dto.LeaseLiabilityScheduleReportDTO;
import io.github.erp.service.mapper.LeaseLiabilityScheduleReportMapper;
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
 * Service for executing complex queries for {@link LeaseLiabilityScheduleReport} entities in the database.
 * The main input is a {@link LeaseLiabilityScheduleReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseLiabilityScheduleReportDTO} or a {@link Page} of {@link LeaseLiabilityScheduleReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseLiabilityScheduleReportQueryService extends QueryService<LeaseLiabilityScheduleReport> {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityScheduleReportQueryService.class);

    private final LeaseLiabilityScheduleReportRepository leaseLiabilityScheduleReportRepository;

    private final LeaseLiabilityScheduleReportMapper leaseLiabilityScheduleReportMapper;

    private final LeaseLiabilityScheduleReportSearchRepository leaseLiabilityScheduleReportSearchRepository;

    public LeaseLiabilityScheduleReportQueryService(
        LeaseLiabilityScheduleReportRepository leaseLiabilityScheduleReportRepository,
        LeaseLiabilityScheduleReportMapper leaseLiabilityScheduleReportMapper,
        LeaseLiabilityScheduleReportSearchRepository leaseLiabilityScheduleReportSearchRepository
    ) {
        this.leaseLiabilityScheduleReportRepository = leaseLiabilityScheduleReportRepository;
        this.leaseLiabilityScheduleReportMapper = leaseLiabilityScheduleReportMapper;
        this.leaseLiabilityScheduleReportSearchRepository = leaseLiabilityScheduleReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseLiabilityScheduleReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseLiabilityScheduleReportDTO> findByCriteria(LeaseLiabilityScheduleReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseLiabilityScheduleReport> specification = createSpecification(criteria);
        return leaseLiabilityScheduleReportMapper.toDto(leaseLiabilityScheduleReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseLiabilityScheduleReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityScheduleReportDTO> findByCriteria(LeaseLiabilityScheduleReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseLiabilityScheduleReport> specification = createSpecification(criteria);
        return leaseLiabilityScheduleReportRepository.findAll(specification, page).map(leaseLiabilityScheduleReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseLiabilityScheduleReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseLiabilityScheduleReport> specification = createSpecification(criteria);
        return leaseLiabilityScheduleReportRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseLiabilityScheduleReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseLiabilityScheduleReport> createSpecification(LeaseLiabilityScheduleReportCriteria criteria) {
        Specification<LeaseLiabilityScheduleReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseLiabilityScheduleReport_.id));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), LeaseLiabilityScheduleReport_.requestId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), LeaseLiabilityScheduleReport_.timeOfRequest));
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileChecksum(), LeaseLiabilityScheduleReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), LeaseLiabilityScheduleReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), LeaseLiabilityScheduleReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getReportParameters(), LeaseLiabilityScheduleReport_.reportParameters)
                    );
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(LeaseLiabilityScheduleReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLeaseAmortizationScheduleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseAmortizationScheduleId(),
                            root ->
                                root
                                    .join(LeaseLiabilityScheduleReport_.leaseAmortizationSchedule, JoinType.LEFT)
                                    .get(LeaseAmortizationSchedule_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
