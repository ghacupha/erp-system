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
import io.github.erp.domain.LoanDeclineReason;
import io.github.erp.repository.LoanDeclineReasonRepository;
import io.github.erp.repository.search.LoanDeclineReasonSearchRepository;
import io.github.erp.service.criteria.LoanDeclineReasonCriteria;
import io.github.erp.service.dto.LoanDeclineReasonDTO;
import io.github.erp.service.mapper.LoanDeclineReasonMapper;
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
 * Service for executing complex queries for {@link LoanDeclineReason} entities in the database.
 * The main input is a {@link LoanDeclineReasonCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoanDeclineReasonDTO} or a {@link Page} of {@link LoanDeclineReasonDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoanDeclineReasonQueryService extends QueryService<LoanDeclineReason> {

    private final Logger log = LoggerFactory.getLogger(LoanDeclineReasonQueryService.class);

    private final LoanDeclineReasonRepository loanDeclineReasonRepository;

    private final LoanDeclineReasonMapper loanDeclineReasonMapper;

    private final LoanDeclineReasonSearchRepository loanDeclineReasonSearchRepository;

    public LoanDeclineReasonQueryService(
        LoanDeclineReasonRepository loanDeclineReasonRepository,
        LoanDeclineReasonMapper loanDeclineReasonMapper,
        LoanDeclineReasonSearchRepository loanDeclineReasonSearchRepository
    ) {
        this.loanDeclineReasonRepository = loanDeclineReasonRepository;
        this.loanDeclineReasonMapper = loanDeclineReasonMapper;
        this.loanDeclineReasonSearchRepository = loanDeclineReasonSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoanDeclineReasonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoanDeclineReasonDTO> findByCriteria(LoanDeclineReasonCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoanDeclineReason> specification = createSpecification(criteria);
        return loanDeclineReasonMapper.toDto(loanDeclineReasonRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoanDeclineReasonDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanDeclineReasonDTO> findByCriteria(LoanDeclineReasonCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoanDeclineReason> specification = createSpecification(criteria);
        return loanDeclineReasonRepository.findAll(specification, page).map(loanDeclineReasonMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoanDeclineReasonCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoanDeclineReason> specification = createSpecification(criteria);
        return loanDeclineReasonRepository.count(specification);
    }

    /**
     * Function to convert {@link LoanDeclineReasonCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoanDeclineReason> createSpecification(LoanDeclineReasonCriteria criteria) {
        Specification<LoanDeclineReason> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoanDeclineReason_.id));
            }
            if (criteria.getLoanDeclineReasonTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLoanDeclineReasonTypeCode(), LoanDeclineReason_.loanDeclineReasonTypeCode)
                    );
            }
            if (criteria.getLoanDeclineReasonType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLoanDeclineReasonType(), LoanDeclineReason_.loanDeclineReasonType)
                    );
            }
        }
        return specification;
    }
}
