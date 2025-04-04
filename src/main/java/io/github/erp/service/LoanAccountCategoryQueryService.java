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
import io.github.erp.domain.LoanAccountCategory;
import io.github.erp.repository.LoanAccountCategoryRepository;
import io.github.erp.repository.search.LoanAccountCategorySearchRepository;
import io.github.erp.service.criteria.LoanAccountCategoryCriteria;
import io.github.erp.service.dto.LoanAccountCategoryDTO;
import io.github.erp.service.mapper.LoanAccountCategoryMapper;
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
 * Service for executing complex queries for {@link LoanAccountCategory} entities in the database.
 * The main input is a {@link LoanAccountCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoanAccountCategoryDTO} or a {@link Page} of {@link LoanAccountCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoanAccountCategoryQueryService extends QueryService<LoanAccountCategory> {

    private final Logger log = LoggerFactory.getLogger(LoanAccountCategoryQueryService.class);

    private final LoanAccountCategoryRepository loanAccountCategoryRepository;

    private final LoanAccountCategoryMapper loanAccountCategoryMapper;

    private final LoanAccountCategorySearchRepository loanAccountCategorySearchRepository;

    public LoanAccountCategoryQueryService(
        LoanAccountCategoryRepository loanAccountCategoryRepository,
        LoanAccountCategoryMapper loanAccountCategoryMapper,
        LoanAccountCategorySearchRepository loanAccountCategorySearchRepository
    ) {
        this.loanAccountCategoryRepository = loanAccountCategoryRepository;
        this.loanAccountCategoryMapper = loanAccountCategoryMapper;
        this.loanAccountCategorySearchRepository = loanAccountCategorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoanAccountCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoanAccountCategoryDTO> findByCriteria(LoanAccountCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoanAccountCategory> specification = createSpecification(criteria);
        return loanAccountCategoryMapper.toDto(loanAccountCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoanAccountCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanAccountCategoryDTO> findByCriteria(LoanAccountCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoanAccountCategory> specification = createSpecification(criteria);
        return loanAccountCategoryRepository.findAll(specification, page).map(loanAccountCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoanAccountCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoanAccountCategory> specification = createSpecification(criteria);
        return loanAccountCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link LoanAccountCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoanAccountCategory> createSpecification(LoanAccountCategoryCriteria criteria) {
        Specification<LoanAccountCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoanAccountCategory_.id));
            }
            if (criteria.getLoanAccountMutationCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLoanAccountMutationCode(), LoanAccountCategory_.loanAccountMutationCode)
                    );
            }
            if (criteria.getLoanAccountMutationType() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getLoanAccountMutationType(), LoanAccountCategory_.loanAccountMutationType)
                    );
            }
            if (criteria.getLoanAccountMutationDetails() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLoanAccountMutationDetails(), LoanAccountCategory_.loanAccountMutationDetails)
                    );
            }
        }
        return specification;
    }
}
