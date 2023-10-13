package io.github.erp.service;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import io.github.erp.domain.CreditCardOwnership;
import io.github.erp.repository.CreditCardOwnershipRepository;
import io.github.erp.repository.search.CreditCardOwnershipSearchRepository;
import io.github.erp.service.criteria.CreditCardOwnershipCriteria;
import io.github.erp.service.dto.CreditCardOwnershipDTO;
import io.github.erp.service.mapper.CreditCardOwnershipMapper;
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
 * Service for executing complex queries for {@link CreditCardOwnership} entities in the database.
 * The main input is a {@link CreditCardOwnershipCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CreditCardOwnershipDTO} or a {@link Page} of {@link CreditCardOwnershipDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CreditCardOwnershipQueryService extends QueryService<CreditCardOwnership> {

    private final Logger log = LoggerFactory.getLogger(CreditCardOwnershipQueryService.class);

    private final CreditCardOwnershipRepository creditCardOwnershipRepository;

    private final CreditCardOwnershipMapper creditCardOwnershipMapper;

    private final CreditCardOwnershipSearchRepository creditCardOwnershipSearchRepository;

    public CreditCardOwnershipQueryService(
        CreditCardOwnershipRepository creditCardOwnershipRepository,
        CreditCardOwnershipMapper creditCardOwnershipMapper,
        CreditCardOwnershipSearchRepository creditCardOwnershipSearchRepository
    ) {
        this.creditCardOwnershipRepository = creditCardOwnershipRepository;
        this.creditCardOwnershipMapper = creditCardOwnershipMapper;
        this.creditCardOwnershipSearchRepository = creditCardOwnershipSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CreditCardOwnershipDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CreditCardOwnershipDTO> findByCriteria(CreditCardOwnershipCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CreditCardOwnership> specification = createSpecification(criteria);
        return creditCardOwnershipMapper.toDto(creditCardOwnershipRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CreditCardOwnershipDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CreditCardOwnershipDTO> findByCriteria(CreditCardOwnershipCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CreditCardOwnership> specification = createSpecification(criteria);
        return creditCardOwnershipRepository.findAll(specification, page).map(creditCardOwnershipMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CreditCardOwnershipCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CreditCardOwnership> specification = createSpecification(criteria);
        return creditCardOwnershipRepository.count(specification);
    }

    /**
     * Function to convert {@link CreditCardOwnershipCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CreditCardOwnership> createSpecification(CreditCardOwnershipCriteria criteria) {
        Specification<CreditCardOwnership> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CreditCardOwnership_.id));
            }
            if (criteria.getCreditCardOwnershipCategoryCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCreditCardOwnershipCategoryCode(),
                            CreditCardOwnership_.creditCardOwnershipCategoryCode
                        )
                    );
            }
            if (criteria.getCreditCardOwnershipCategoryType() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreditCardOwnershipCategoryType(),
                            CreditCardOwnership_.creditCardOwnershipCategoryType
                        )
                    );
            }
        }
        return specification;
    }
}
