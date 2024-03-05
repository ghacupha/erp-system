package io.github.erp.service;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.PrepaymentAccountReport;
import io.github.erp.repository.PrepaymentAccountReportRepository;
import io.github.erp.repository.search.PrepaymentAccountReportSearchRepository;
import io.github.erp.service.criteria.PrepaymentAccountReportCriteria;
import io.github.erp.service.dto.PrepaymentAccountReportDTO;
import io.github.erp.service.mapper.PrepaymentAccountReportMapper;
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
 * Service for executing complex queries for {@link PrepaymentAccountReport} entities in the database.
 * The main input is a {@link PrepaymentAccountReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentAccountReportDTO} or a {@link Page} of {@link PrepaymentAccountReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentAccountReportQueryService extends QueryService<PrepaymentAccountReport> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentAccountReportQueryService.class);

    private final PrepaymentAccountReportRepository prepaymentAccountReportRepository;

    private final PrepaymentAccountReportMapper prepaymentAccountReportMapper;

    private final PrepaymentAccountReportSearchRepository prepaymentAccountReportSearchRepository;

    public PrepaymentAccountReportQueryService(
        PrepaymentAccountReportRepository prepaymentAccountReportRepository,
        PrepaymentAccountReportMapper prepaymentAccountReportMapper,
        PrepaymentAccountReportSearchRepository prepaymentAccountReportSearchRepository
    ) {
        this.prepaymentAccountReportRepository = prepaymentAccountReportRepository;
        this.prepaymentAccountReportMapper = prepaymentAccountReportMapper;
        this.prepaymentAccountReportSearchRepository = prepaymentAccountReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentAccountReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentAccountReportDTO> findByCriteria(PrepaymentAccountReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrepaymentAccountReport> specification = createSpecification(criteria);
        return prepaymentAccountReportMapper.toDto(prepaymentAccountReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentAccountReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentAccountReportDTO> findByCriteria(PrepaymentAccountReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrepaymentAccountReport> specification = createSpecification(criteria);
        return prepaymentAccountReportRepository.findAll(specification, page).map(prepaymentAccountReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentAccountReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrepaymentAccountReport> specification = createSpecification(criteria);
        return prepaymentAccountReportRepository.count(specification);
    }

    /**
     * Function to convert {@link PrepaymentAccountReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrepaymentAccountReport> createSpecification(PrepaymentAccountReportCriteria criteria) {
        Specification<PrepaymentAccountReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrepaymentAccountReport_.id));
            }
            if (criteria.getPrepaymentAccount() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getPrepaymentAccount(), PrepaymentAccountReport_.prepaymentAccount)
                    );
            }
            if (criteria.getPrepaymentAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPrepaymentAmount(), PrepaymentAccountReport_.prepaymentAmount));
            }
            if (criteria.getAmortisedAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAmortisedAmount(), PrepaymentAccountReport_.amortisedAmount));
            }
            if (criteria.getOutstandingAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutstandingAmount(), PrepaymentAccountReport_.outstandingAmount));
            }
            if (criteria.getNumberOfPrepaymentAccounts() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getNumberOfPrepaymentAccounts(),
                            PrepaymentAccountReport_.numberOfPrepaymentAccounts
                        )
                    );
            }
            if (criteria.getNumberOfAmortisedItems() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNumberOfAmortisedItems(), PrepaymentAccountReport_.numberOfAmortisedItems)
                    );
            }
        }
        return specification;
    }
}
