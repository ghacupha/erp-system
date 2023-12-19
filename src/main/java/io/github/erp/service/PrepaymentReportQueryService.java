package io.github.erp.service;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.PrepaymentReport;
import io.github.erp.repository.PrepaymentReportRepository;
import io.github.erp.repository.search.PrepaymentReportSearchRepository;
import io.github.erp.service.criteria.PrepaymentReportCriteria;
import io.github.erp.service.dto.PrepaymentReportDTO;
import io.github.erp.service.mapper.PrepaymentReportMapper;
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
 * Service for executing complex queries for {@link PrepaymentReport} entities in the database.
 * The main input is a {@link PrepaymentReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentReportDTO} or a {@link Page} of {@link PrepaymentReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentReportQueryService extends QueryService<PrepaymentReport> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentReportQueryService.class);

    private final PrepaymentReportRepository prepaymentReportRepository;

    private final PrepaymentReportMapper prepaymentReportMapper;

    private final PrepaymentReportSearchRepository prepaymentReportSearchRepository;

    public PrepaymentReportQueryService(
        PrepaymentReportRepository prepaymentReportRepository,
        PrepaymentReportMapper prepaymentReportMapper,
        PrepaymentReportSearchRepository prepaymentReportSearchRepository
    ) {
        this.prepaymentReportRepository = prepaymentReportRepository;
        this.prepaymentReportMapper = prepaymentReportMapper;
        this.prepaymentReportSearchRepository = prepaymentReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentReportDTO> findByCriteria(PrepaymentReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrepaymentReport> specification = createSpecification(criteria);
        return prepaymentReportMapper.toDto(prepaymentReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentReportDTO> findByCriteria(PrepaymentReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrepaymentReport> specification = createSpecification(criteria);
        return prepaymentReportRepository.findAll(specification, page).map(prepaymentReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrepaymentReport> specification = createSpecification(criteria);
        return prepaymentReportRepository.count(specification);
    }

    /**
     * Function to convert {@link PrepaymentReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrepaymentReport> createSpecification(PrepaymentReportCriteria criteria) {
        Specification<PrepaymentReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrepaymentReport_.id));
            }
            if (criteria.getCatalogueNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCatalogueNumber(), PrepaymentReport_.catalogueNumber));
            }
            if (criteria.getParticulars() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParticulars(), PrepaymentReport_.particulars));
            }
            if (criteria.getDealerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDealerName(), PrepaymentReport_.dealerName));
            }
            if (criteria.getPaymentNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentNumber(), PrepaymentReport_.paymentNumber));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), PrepaymentReport_.paymentDate));
            }
            if (criteria.getCurrencyCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyCode(), PrepaymentReport_.currencyCode));
            }
            if (criteria.getPrepaymentAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPrepaymentAmount(), PrepaymentReport_.prepaymentAmount));
            }
            if (criteria.getAmortisedAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAmortisedAmount(), PrepaymentReport_.amortisedAmount));
            }
            if (criteria.getOutstandingAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutstandingAmount(), PrepaymentReport_.outstandingAmount));
            }
        }
        return specification;
    }
}
