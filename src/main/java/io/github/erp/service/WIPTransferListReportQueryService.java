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
import io.github.erp.domain.WIPTransferListReport;
import io.github.erp.repository.WIPTransferListReportRepository;
import io.github.erp.repository.search.WIPTransferListReportSearchRepository;
import io.github.erp.service.criteria.WIPTransferListReportCriteria;
import io.github.erp.service.dto.WIPTransferListReportDTO;
import io.github.erp.service.mapper.WIPTransferListReportMapper;
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
 * Service for executing complex queries for {@link WIPTransferListReport} entities in the database.
 * The main input is a {@link WIPTransferListReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WIPTransferListReportDTO} or a {@link Page} of {@link WIPTransferListReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WIPTransferListReportQueryService extends QueryService<WIPTransferListReport> {

    private final Logger log = LoggerFactory.getLogger(WIPTransferListReportQueryService.class);

    private final WIPTransferListReportRepository wIPTransferListReportRepository;

    private final WIPTransferListReportMapper wIPTransferListReportMapper;

    private final WIPTransferListReportSearchRepository wIPTransferListReportSearchRepository;

    public WIPTransferListReportQueryService(
        WIPTransferListReportRepository wIPTransferListReportRepository,
        WIPTransferListReportMapper wIPTransferListReportMapper,
        WIPTransferListReportSearchRepository wIPTransferListReportSearchRepository
    ) {
        this.wIPTransferListReportRepository = wIPTransferListReportRepository;
        this.wIPTransferListReportMapper = wIPTransferListReportMapper;
        this.wIPTransferListReportSearchRepository = wIPTransferListReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WIPTransferListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WIPTransferListReportDTO> findByCriteria(WIPTransferListReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WIPTransferListReport> specification = createSpecification(criteria);
        return wIPTransferListReportMapper.toDto(wIPTransferListReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WIPTransferListReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WIPTransferListReportDTO> findByCriteria(WIPTransferListReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WIPTransferListReport> specification = createSpecification(criteria);
        return wIPTransferListReportRepository.findAll(specification, page).map(wIPTransferListReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WIPTransferListReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WIPTransferListReport> specification = createSpecification(criteria);
        return wIPTransferListReportRepository.count(specification);
    }

    /**
     * Function to convert {@link WIPTransferListReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WIPTransferListReport> createSpecification(WIPTransferListReportCriteria criteria) {
        Specification<WIPTransferListReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WIPTransferListReport_.id));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), WIPTransferListReport_.timeOfRequest));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), WIPTransferListReport_.requestId));
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileChecksum(), WIPTransferListReport_.fileChecksum));
            }
            if (criteria.getTempered() != null) {
                specification = specification.and(buildSpecification(criteria.getTempered(), WIPTransferListReport_.tempered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), WIPTransferListReport_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportParameters(), WIPTransferListReport_.reportParameters));
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(WIPTransferListReport_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
