package io.github.erp.service;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.LeaseLiabilityByAccountReport;
import io.github.erp.repository.LeaseLiabilityByAccountReportRepository;
import io.github.erp.repository.search.LeaseLiabilityByAccountReportSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityByAccountReportCriteria;
import io.github.erp.service.dto.LeaseLiabilityByAccountReportDTO;
import io.github.erp.service.mapper.LeaseLiabilityByAccountReportMapper;
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
 * Service for executing complex queries for {@link LeaseLiabilityByAccountReport} entities in the database.
 * The main input is a {@link LeaseLiabilityByAccountReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseLiabilityByAccountReportDTO} or a {@link Page} of {@link LeaseLiabilityByAccountReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseLiabilityByAccountReportQueryService extends QueryService<LeaseLiabilityByAccountReport> {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityByAccountReportQueryService.class);

    private final LeaseLiabilityByAccountReportRepository leaseLiabilityByAccountReportRepository;

    private final LeaseLiabilityByAccountReportMapper leaseLiabilityByAccountReportMapper;

    private final LeaseLiabilityByAccountReportSearchRepository leaseLiabilityByAccountReportSearchRepository;

    public LeaseLiabilityByAccountReportQueryService(
        LeaseLiabilityByAccountReportRepository leaseLiabilityByAccountReportRepository,
        LeaseLiabilityByAccountReportMapper leaseLiabilityByAccountReportMapper,
        LeaseLiabilityByAccountReportSearchRepository leaseLiabilityByAccountReportSearchRepository
    ) {
        this.leaseLiabilityByAccountReportRepository = leaseLiabilityByAccountReportRepository;
        this.leaseLiabilityByAccountReportMapper = leaseLiabilityByAccountReportMapper;
        this.leaseLiabilityByAccountReportSearchRepository = leaseLiabilityByAccountReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseLiabilityByAccountReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseLiabilityByAccountReportDTO> findByCriteria(LeaseLiabilityByAccountReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseLiabilityByAccountReport> specification = createSpecification(criteria);
        return leaseLiabilityByAccountReportMapper.toDto(leaseLiabilityByAccountReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseLiabilityByAccountReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityByAccountReportDTO> findByCriteria(LeaseLiabilityByAccountReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseLiabilityByAccountReport> specification = createSpecification(criteria);
        return leaseLiabilityByAccountReportRepository.findAll(specification, page).map(leaseLiabilityByAccountReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseLiabilityByAccountReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseLiabilityByAccountReport> specification = createSpecification(criteria);
        return leaseLiabilityByAccountReportRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseLiabilityByAccountReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseLiabilityByAccountReport> createSpecification(LeaseLiabilityByAccountReportCriteria criteria) {
        Specification<LeaseLiabilityByAccountReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseLiabilityByAccountReport_.id));
            }
            if (criteria.getReportId() != null) {
                specification = specification.and(buildSpecification(criteria.getReportId(), LeaseLiabilityByAccountReport_.reportId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), LeaseLiabilityByAccountReport_.timeOfRequest));
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileChecksum(), LeaseLiabilityByAccountReport_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), LeaseLiabilityByAccountReport_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), LeaseLiabilityByAccountReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getReportParameters(), LeaseLiabilityByAccountReport_.reportParameters)
                    );
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(LeaseLiabilityByAccountReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLeasePeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeasePeriodId(),
                            root -> root.join(LeaseLiabilityByAccountReport_.leasePeriod, JoinType.LEFT).get(LeasePeriod_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
