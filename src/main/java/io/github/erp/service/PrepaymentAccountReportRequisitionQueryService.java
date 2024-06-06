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
import io.github.erp.domain.PrepaymentAccountReportRequisition;
import io.github.erp.repository.PrepaymentAccountReportRequisitionRepository;
import io.github.erp.repository.search.PrepaymentAccountReportRequisitionSearchRepository;
import io.github.erp.service.criteria.PrepaymentAccountReportRequisitionCriteria;
import io.github.erp.service.dto.PrepaymentAccountReportRequisitionDTO;
import io.github.erp.service.mapper.PrepaymentAccountReportRequisitionMapper;
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
 * Service for executing complex queries for {@link PrepaymentAccountReportRequisition} entities in the database.
 * The main input is a {@link PrepaymentAccountReportRequisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentAccountReportRequisitionDTO} or a {@link Page} of {@link PrepaymentAccountReportRequisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentAccountReportRequisitionQueryService extends QueryService<PrepaymentAccountReportRequisition> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentAccountReportRequisitionQueryService.class);

    private final PrepaymentAccountReportRequisitionRepository prepaymentAccountReportRequisitionRepository;

    private final PrepaymentAccountReportRequisitionMapper prepaymentAccountReportRequisitionMapper;

    private final PrepaymentAccountReportRequisitionSearchRepository prepaymentAccountReportRequisitionSearchRepository;

    public PrepaymentAccountReportRequisitionQueryService(
        PrepaymentAccountReportRequisitionRepository prepaymentAccountReportRequisitionRepository,
        PrepaymentAccountReportRequisitionMapper prepaymentAccountReportRequisitionMapper,
        PrepaymentAccountReportRequisitionSearchRepository prepaymentAccountReportRequisitionSearchRepository
    ) {
        this.prepaymentAccountReportRequisitionRepository = prepaymentAccountReportRequisitionRepository;
        this.prepaymentAccountReportRequisitionMapper = prepaymentAccountReportRequisitionMapper;
        this.prepaymentAccountReportRequisitionSearchRepository = prepaymentAccountReportRequisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentAccountReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentAccountReportRequisitionDTO> findByCriteria(PrepaymentAccountReportRequisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrepaymentAccountReportRequisition> specification = createSpecification(criteria);
        return prepaymentAccountReportRequisitionMapper.toDto(prepaymentAccountReportRequisitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentAccountReportRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentAccountReportRequisitionDTO> findByCriteria(PrepaymentAccountReportRequisitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrepaymentAccountReportRequisition> specification = createSpecification(criteria);
        return prepaymentAccountReportRequisitionRepository
            .findAll(specification, page)
            .map(prepaymentAccountReportRequisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentAccountReportRequisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrepaymentAccountReportRequisition> specification = createSpecification(criteria);
        return prepaymentAccountReportRequisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link PrepaymentAccountReportRequisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrepaymentAccountReportRequisition> createSpecification(PrepaymentAccountReportRequisitionCriteria criteria) {
        Specification<PrepaymentAccountReportRequisition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrepaymentAccountReportRequisition_.id));
            }
            if (criteria.getReportName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getReportName(), PrepaymentAccountReportRequisition_.reportName));
            }
            if (criteria.getTimeOfRequisition() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTimeOfRequisition(), PrepaymentAccountReportRequisition_.timeOfRequisition)
                    );
            }
            if (criteria.getFileChecksum() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFileChecksum(), PrepaymentAccountReportRequisition_.fileChecksum)
                    );
            }
            if (criteria.getTampered() != null) {
                specification = specification.and(buildSpecification(criteria.getTampered(), PrepaymentAccountReportRequisition_.tampered));
            }
            if (criteria.getFilename() != null) {
                specification = specification.and(buildSpecification(criteria.getFilename(), PrepaymentAccountReportRequisition_.filename));
            }
            if (criteria.getReportDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportDate(), PrepaymentAccountReportRequisition_.reportDate));
            }
            if (criteria.getReportParameters() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getReportParameters(), PrepaymentAccountReportRequisition_.reportParameters)
                    );
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(PrepaymentAccountReportRequisition_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastAccessedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastAccessedById(),
                            root -> root.join(PrepaymentAccountReportRequisition_.lastAccessedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
