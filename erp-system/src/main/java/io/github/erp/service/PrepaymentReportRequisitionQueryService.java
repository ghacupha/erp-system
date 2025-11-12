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
import io.github.erp.domain.PrepaymentReportRequisition;
import io.github.erp.repository.PrepaymentReportRequisitionRepository;
import io.github.erp.repository.search.PrepaymentReportRequisitionSearchRepository;
import io.github.erp.service.criteria.PrepaymentReportRequisitionCriteria;
import io.github.erp.service.dto.PrepaymentReportRequisitionDTO;
import io.github.erp.service.mapper.PrepaymentReportRequisitionMapper;
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
 * Service for executing complex queries for {@link PrepaymentReportRequisition} entities in the database.
 * The main input is a {@link PrepaymentReportRequisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentReportRequisitionDTO} or a {@link Page} of {@link PrepaymentReportRequisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentReportRequisitionQueryService extends QueryService<PrepaymentReportRequisition> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentReportRequisitionQueryService.class);

    private final PrepaymentReportRequisitionRepository prepaymentReportRequisitionRepository;

    private final PrepaymentReportRequisitionMapper prepaymentReportRequisitionMapper;

    private final PrepaymentReportRequisitionSearchRepository prepaymentReportRequisitionSearchRepository;

    public PrepaymentReportRequisitionQueryService(
        PrepaymentReportRequisitionRepository prepaymentReportRequisitionRepository,
        PrepaymentReportRequisitionMapper prepaymentReportRequisitionMapper,
        PrepaymentReportRequisitionSearchRepository prepaymentReportRequisitionSearchRepository
    ) {
        this.prepaymentReportRequisitionRepository = prepaymentReportRequisitionRepository;
        this.prepaymentReportRequisitionMapper = prepaymentReportRequisitionMapper;
        this.prepaymentReportRequisitionSearchRepository = prepaymentReportRequisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentReportRequisitionDTO> findByCriteria(PrepaymentReportRequisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrepaymentReportRequisition> specification = createSpecification(criteria);
        return prepaymentReportRequisitionMapper.toDto(prepaymentReportRequisitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentReportRequisitionDTO> findByCriteria(PrepaymentReportRequisitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrepaymentReportRequisition> specification = createSpecification(criteria);
        return prepaymentReportRequisitionRepository.findAll(specification, page).map(prepaymentReportRequisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentReportRequisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrepaymentReportRequisition> specification = createSpecification(criteria);
        return prepaymentReportRequisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link PrepaymentReportRequisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrepaymentReportRequisition> createSpecification(PrepaymentReportRequisitionCriteria criteria) {
        Specification<PrepaymentReportRequisition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrepaymentReportRequisition_.id));
            }
            if (criteria.getReportName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportName(), PrepaymentReportRequisition_.reportName));
            }
            if (criteria.getReportDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportDate(), PrepaymentReportRequisition_.reportDate));
            }
            if (criteria.getTimeOfRequisition() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTimeOfRequisition(), PrepaymentReportRequisition_.timeOfRequisition)
                    );
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileChecksum(), PrepaymentReportRequisition_.fileChecksum));
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), PrepaymentReportRequisition_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), PrepaymentReportRequisition_.filename));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getReportParameters(), PrepaymentReportRequisition_.reportParameters)
                    );
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(PrepaymentReportRequisition_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastAccessedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastAccessedById(),
                            root -> root.join(PrepaymentReportRequisition_.lastAccessedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
