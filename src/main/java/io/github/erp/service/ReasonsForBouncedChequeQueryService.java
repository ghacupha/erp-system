package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.ReasonsForBouncedCheque;
import io.github.erp.repository.ReasonsForBouncedChequeRepository;
import io.github.erp.repository.search.ReasonsForBouncedChequeSearchRepository;
import io.github.erp.service.criteria.ReasonsForBouncedChequeCriteria;
import io.github.erp.service.dto.ReasonsForBouncedChequeDTO;
import io.github.erp.service.mapper.ReasonsForBouncedChequeMapper;
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
 * Service for executing complex queries for {@link ReasonsForBouncedCheque} entities in the database.
 * The main input is a {@link ReasonsForBouncedChequeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ReasonsForBouncedChequeDTO} or a {@link Page} of {@link ReasonsForBouncedChequeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReasonsForBouncedChequeQueryService extends QueryService<ReasonsForBouncedCheque> {

    private final Logger log = LoggerFactory.getLogger(ReasonsForBouncedChequeQueryService.class);

    private final ReasonsForBouncedChequeRepository reasonsForBouncedChequeRepository;

    private final ReasonsForBouncedChequeMapper reasonsForBouncedChequeMapper;

    private final ReasonsForBouncedChequeSearchRepository reasonsForBouncedChequeSearchRepository;

    public ReasonsForBouncedChequeQueryService(
        ReasonsForBouncedChequeRepository reasonsForBouncedChequeRepository,
        ReasonsForBouncedChequeMapper reasonsForBouncedChequeMapper,
        ReasonsForBouncedChequeSearchRepository reasonsForBouncedChequeSearchRepository
    ) {
        this.reasonsForBouncedChequeRepository = reasonsForBouncedChequeRepository;
        this.reasonsForBouncedChequeMapper = reasonsForBouncedChequeMapper;
        this.reasonsForBouncedChequeSearchRepository = reasonsForBouncedChequeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ReasonsForBouncedChequeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ReasonsForBouncedChequeDTO> findByCriteria(ReasonsForBouncedChequeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ReasonsForBouncedCheque> specification = createSpecification(criteria);
        return reasonsForBouncedChequeMapper.toDto(reasonsForBouncedChequeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ReasonsForBouncedChequeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReasonsForBouncedChequeDTO> findByCriteria(ReasonsForBouncedChequeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReasonsForBouncedCheque> specification = createSpecification(criteria);
        return reasonsForBouncedChequeRepository.findAll(specification, page).map(reasonsForBouncedChequeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReasonsForBouncedChequeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ReasonsForBouncedCheque> specification = createSpecification(criteria);
        return reasonsForBouncedChequeRepository.count(specification);
    }

    /**
     * Function to convert {@link ReasonsForBouncedChequeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReasonsForBouncedCheque> createSpecification(ReasonsForBouncedChequeCriteria criteria) {
        Specification<ReasonsForBouncedCheque> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ReasonsForBouncedCheque_.id));
            }
            if (criteria.getBouncedChequeReasonsTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getBouncedChequeReasonsTypeCode(),
                            ReasonsForBouncedCheque_.bouncedChequeReasonsTypeCode
                        )
                    );
            }
            if (criteria.getBouncedChequeReasonsType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getBouncedChequeReasonsType(), ReasonsForBouncedCheque_.bouncedChequeReasonsType)
                    );
            }
        }
        return specification;
    }
}
