package io.github.erp.service;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.LoanApplicationStatus;
import io.github.erp.repository.LoanApplicationStatusRepository;
import io.github.erp.repository.search.LoanApplicationStatusSearchRepository;
import io.github.erp.service.criteria.LoanApplicationStatusCriteria;
import io.github.erp.service.dto.LoanApplicationStatusDTO;
import io.github.erp.service.mapper.LoanApplicationStatusMapper;
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
 * Service for executing complex queries for {@link LoanApplicationStatus} entities in the database.
 * The main input is a {@link LoanApplicationStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoanApplicationStatusDTO} or a {@link Page} of {@link LoanApplicationStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoanApplicationStatusQueryService extends QueryService<LoanApplicationStatus> {

    private final Logger log = LoggerFactory.getLogger(LoanApplicationStatusQueryService.class);

    private final LoanApplicationStatusRepository loanApplicationStatusRepository;

    private final LoanApplicationStatusMapper loanApplicationStatusMapper;

    private final LoanApplicationStatusSearchRepository loanApplicationStatusSearchRepository;

    public LoanApplicationStatusQueryService(
        LoanApplicationStatusRepository loanApplicationStatusRepository,
        LoanApplicationStatusMapper loanApplicationStatusMapper,
        LoanApplicationStatusSearchRepository loanApplicationStatusSearchRepository
    ) {
        this.loanApplicationStatusRepository = loanApplicationStatusRepository;
        this.loanApplicationStatusMapper = loanApplicationStatusMapper;
        this.loanApplicationStatusSearchRepository = loanApplicationStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoanApplicationStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoanApplicationStatusDTO> findByCriteria(LoanApplicationStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoanApplicationStatus> specification = createSpecification(criteria);
        return loanApplicationStatusMapper.toDto(loanApplicationStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoanApplicationStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanApplicationStatusDTO> findByCriteria(LoanApplicationStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoanApplicationStatus> specification = createSpecification(criteria);
        return loanApplicationStatusRepository.findAll(specification, page).map(loanApplicationStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoanApplicationStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoanApplicationStatus> specification = createSpecification(criteria);
        return loanApplicationStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link LoanApplicationStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoanApplicationStatus> createSpecification(LoanApplicationStatusCriteria criteria) {
        Specification<LoanApplicationStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoanApplicationStatus_.id));
            }
            if (criteria.getLoanApplicationStatusTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getLoanApplicationStatusTypeCode(),
                            LoanApplicationStatus_.loanApplicationStatusTypeCode
                        )
                    );
            }
            if (criteria.getLoanApplicationStatusType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLoanApplicationStatusType(), LoanApplicationStatus_.loanApplicationStatusType)
                    );
            }
        }
        return specification;
    }
}
