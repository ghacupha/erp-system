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
import io.github.erp.domain.LoanRepaymentFrequency;
import io.github.erp.repository.LoanRepaymentFrequencyRepository;
import io.github.erp.repository.search.LoanRepaymentFrequencySearchRepository;
import io.github.erp.service.criteria.LoanRepaymentFrequencyCriteria;
import io.github.erp.service.dto.LoanRepaymentFrequencyDTO;
import io.github.erp.service.mapper.LoanRepaymentFrequencyMapper;
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
 * Service for executing complex queries for {@link LoanRepaymentFrequency} entities in the database.
 * The main input is a {@link LoanRepaymentFrequencyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoanRepaymentFrequencyDTO} or a {@link Page} of {@link LoanRepaymentFrequencyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoanRepaymentFrequencyQueryService extends QueryService<LoanRepaymentFrequency> {

    private final Logger log = LoggerFactory.getLogger(LoanRepaymentFrequencyQueryService.class);

    private final LoanRepaymentFrequencyRepository loanRepaymentFrequencyRepository;

    private final LoanRepaymentFrequencyMapper loanRepaymentFrequencyMapper;

    private final LoanRepaymentFrequencySearchRepository loanRepaymentFrequencySearchRepository;

    public LoanRepaymentFrequencyQueryService(
        LoanRepaymentFrequencyRepository loanRepaymentFrequencyRepository,
        LoanRepaymentFrequencyMapper loanRepaymentFrequencyMapper,
        LoanRepaymentFrequencySearchRepository loanRepaymentFrequencySearchRepository
    ) {
        this.loanRepaymentFrequencyRepository = loanRepaymentFrequencyRepository;
        this.loanRepaymentFrequencyMapper = loanRepaymentFrequencyMapper;
        this.loanRepaymentFrequencySearchRepository = loanRepaymentFrequencySearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoanRepaymentFrequencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoanRepaymentFrequencyDTO> findByCriteria(LoanRepaymentFrequencyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoanRepaymentFrequency> specification = createSpecification(criteria);
        return loanRepaymentFrequencyMapper.toDto(loanRepaymentFrequencyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoanRepaymentFrequencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanRepaymentFrequencyDTO> findByCriteria(LoanRepaymentFrequencyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoanRepaymentFrequency> specification = createSpecification(criteria);
        return loanRepaymentFrequencyRepository.findAll(specification, page).map(loanRepaymentFrequencyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoanRepaymentFrequencyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoanRepaymentFrequency> specification = createSpecification(criteria);
        return loanRepaymentFrequencyRepository.count(specification);
    }

    /**
     * Function to convert {@link LoanRepaymentFrequencyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoanRepaymentFrequency> createSpecification(LoanRepaymentFrequencyCriteria criteria) {
        Specification<LoanRepaymentFrequency> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoanRepaymentFrequency_.id));
            }
            if (criteria.getFrequencyTypeCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFrequencyTypeCode(), LoanRepaymentFrequency_.frequencyTypeCode));
            }
            if (criteria.getFrequencyType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFrequencyType(), LoanRepaymentFrequency_.frequencyType));
            }
        }
        return specification;
    }
}
