package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.AmortizationPostingReport;
import io.github.erp.repository.AmortizationPostingReportRepository;
import io.github.erp.repository.search.AmortizationPostingReportSearchRepository;
import io.github.erp.service.criteria.AmortizationPostingReportCriteria;
import io.github.erp.service.dto.AmortizationPostingReportDTO;
import io.github.erp.service.mapper.AmortizationPostingReportMapper;
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
 * Service for executing complex queries for {@link AmortizationPostingReport} entities in the database.
 * The main input is a {@link AmortizationPostingReportCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AmortizationPostingReportDTO} or a {@link Page} of {@link AmortizationPostingReportDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AmortizationPostingReportQueryService extends QueryService<AmortizationPostingReport> {

    private final Logger log = LoggerFactory.getLogger(AmortizationPostingReportQueryService.class);

    private final AmortizationPostingReportRepository amortizationPostingReportRepository;

    private final AmortizationPostingReportMapper amortizationPostingReportMapper;

    private final AmortizationPostingReportSearchRepository amortizationPostingReportSearchRepository;

    public AmortizationPostingReportQueryService(
        AmortizationPostingReportRepository amortizationPostingReportRepository,
        AmortizationPostingReportMapper amortizationPostingReportMapper,
        AmortizationPostingReportSearchRepository amortizationPostingReportSearchRepository
    ) {
        this.amortizationPostingReportRepository = amortizationPostingReportRepository;
        this.amortizationPostingReportMapper = amortizationPostingReportMapper;
        this.amortizationPostingReportSearchRepository = amortizationPostingReportSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AmortizationPostingReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AmortizationPostingReportDTO> findByCriteria(AmortizationPostingReportCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AmortizationPostingReport> specification = createSpecification(criteria);
        return amortizationPostingReportMapper.toDto(amortizationPostingReportRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AmortizationPostingReportDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AmortizationPostingReportDTO> findByCriteria(AmortizationPostingReportCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AmortizationPostingReport> specification = createSpecification(criteria);
        return amortizationPostingReportRepository.findAll(specification, page).map(amortizationPostingReportMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AmortizationPostingReportCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AmortizationPostingReport> specification = createSpecification(criteria);
        return amortizationPostingReportRepository.count(specification);
    }

    /**
     * Function to convert {@link AmortizationPostingReportCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AmortizationPostingReport> createSpecification(AmortizationPostingReportCriteria criteria) {
        Specification<AmortizationPostingReport> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AmortizationPostingReport_.id));
            }
            if (criteria.getCatalogueNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCatalogueNumber(), AmortizationPostingReport_.catalogueNumber));
            }
            if (criteria.getDebitAccount() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDebitAccount(), AmortizationPostingReport_.debitAccount));
            }
            if (criteria.getCreditAccount() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCreditAccount(), AmortizationPostingReport_.creditAccount));
            }
            if (criteria.getDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDescription(), AmortizationPostingReport_.description));
            }
            if (criteria.getAmortizationAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getAmortizationAmount(), AmortizationPostingReport_.amortizationAmount)
                    );
            }
        }
        return specification;
    }
}
