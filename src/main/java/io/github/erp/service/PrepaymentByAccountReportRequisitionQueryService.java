package io.github.erp.service;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.PrepaymentByAccountReportRequisition;
import io.github.erp.repository.PrepaymentByAccountReportRequisitionRepository;
import io.github.erp.repository.search.PrepaymentByAccountReportRequisitionSearchRepository;
import io.github.erp.service.criteria.PrepaymentByAccountReportRequisitionCriteria;
import io.github.erp.service.dto.PrepaymentByAccountReportRequisitionDTO;
import io.github.erp.service.mapper.PrepaymentByAccountReportRequisitionMapper;
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
 * Service for executing complex queries for {@link PrepaymentByAccountReportRequisition} entities in the database.
 * The main input is a {@link PrepaymentByAccountReportRequisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentByAccountReportRequisitionDTO} or a {@link Page} of {@link PrepaymentByAccountReportRequisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentByAccountReportRequisitionQueryService extends QueryService<PrepaymentByAccountReportRequisition> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentByAccountReportRequisitionQueryService.class);

    private final PrepaymentByAccountReportRequisitionRepository prepaymentByAccountReportRequisitionRepository;

    private final PrepaymentByAccountReportRequisitionMapper prepaymentByAccountReportRequisitionMapper;

    private final PrepaymentByAccountReportRequisitionSearchRepository prepaymentByAccountReportRequisitionSearchRepository;

    public PrepaymentByAccountReportRequisitionQueryService(
        PrepaymentByAccountReportRequisitionRepository prepaymentByAccountReportRequisitionRepository,
        PrepaymentByAccountReportRequisitionMapper prepaymentByAccountReportRequisitionMapper,
        PrepaymentByAccountReportRequisitionSearchRepository prepaymentByAccountReportRequisitionSearchRepository
    ) {
        this.prepaymentByAccountReportRequisitionRepository = prepaymentByAccountReportRequisitionRepository;
        this.prepaymentByAccountReportRequisitionMapper = prepaymentByAccountReportRequisitionMapper;
        this.prepaymentByAccountReportRequisitionSearchRepository = prepaymentByAccountReportRequisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentByAccountReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentByAccountReportRequisitionDTO> findByCriteria(PrepaymentByAccountReportRequisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrepaymentByAccountReportRequisition> specification = createSpecification(criteria);
        return prepaymentByAccountReportRequisitionMapper.toDto(prepaymentByAccountReportRequisitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentByAccountReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentByAccountReportRequisitionDTO> findByCriteria(
        PrepaymentByAccountReportRequisitionCriteria criteria,
        Pageable page
    ) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrepaymentByAccountReportRequisition> specification = createSpecification(criteria);
        return prepaymentByAccountReportRequisitionRepository
            .findAll(specification, page)
            .map(prepaymentByAccountReportRequisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentByAccountReportRequisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrepaymentByAccountReportRequisition> specification = createSpecification(criteria);
        return prepaymentByAccountReportRequisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link PrepaymentByAccountReportRequisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrepaymentByAccountReportRequisition> createSpecification(
        PrepaymentByAccountReportRequisitionCriteria criteria
    ) {
        Specification<PrepaymentByAccountReportRequisition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrepaymentByAccountReportRequisition_.id));
            }
            if (criteria.getRequestId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getRequestId(), PrepaymentByAccountReportRequisition_.requestId));
            }
            if (criteria.getTimeOfRequisition() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTimeOfRequisition(), PrepaymentByAccountReportRequisition_.timeOfRequisition)
                    );
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFileChecksum(), PrepaymentByAccountReportRequisition_.fileChecksum)
                    );
            }
            if (criteria.getFilename() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getFilename(), PrepaymentByAccountReportRequisition_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getReportParameters(), PrepaymentByAccountReportRequisition_.reportParameters)
                    );
            }
            if (criteria.getReportDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportDate(), PrepaymentByAccountReportRequisition_.reportDate));
            }
            if (criteria.getTampered() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getTampered(), PrepaymentByAccountReportRequisition_.tampered));
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(PrepaymentByAccountReportRequisition_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastAccessedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastAccessedById(),
                            root -> root.join(PrepaymentByAccountReportRequisition_.lastAccessedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
