package io.github.erp.service;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import io.github.erp.domain.AccountStatusType;
import io.github.erp.repository.AccountStatusTypeRepository;
import io.github.erp.repository.search.AccountStatusTypeSearchRepository;
import io.github.erp.service.criteria.AccountStatusTypeCriteria;
import io.github.erp.service.dto.AccountStatusTypeDTO;
import io.github.erp.service.mapper.AccountStatusTypeMapper;
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
 * Service for executing complex queries for {@link AccountStatusType} entities in the database.
 * The main input is a {@link AccountStatusTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AccountStatusTypeDTO} or a {@link Page} of {@link AccountStatusTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccountStatusTypeQueryService extends QueryService<AccountStatusType> {

    private final Logger log = LoggerFactory.getLogger(AccountStatusTypeQueryService.class);

    private final AccountStatusTypeRepository accountStatusTypeRepository;

    private final AccountStatusTypeMapper accountStatusTypeMapper;

    private final AccountStatusTypeSearchRepository accountStatusTypeSearchRepository;

    public AccountStatusTypeQueryService(
        AccountStatusTypeRepository accountStatusTypeRepository,
        AccountStatusTypeMapper accountStatusTypeMapper,
        AccountStatusTypeSearchRepository accountStatusTypeSearchRepository
    ) {
        this.accountStatusTypeRepository = accountStatusTypeRepository;
        this.accountStatusTypeMapper = accountStatusTypeMapper;
        this.accountStatusTypeSearchRepository = accountStatusTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AccountStatusTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AccountStatusTypeDTO> findByCriteria(AccountStatusTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AccountStatusType> specification = createSpecification(criteria);
        return accountStatusTypeMapper.toDto(accountStatusTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AccountStatusTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountStatusTypeDTO> findByCriteria(AccountStatusTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccountStatusType> specification = createSpecification(criteria);
        return accountStatusTypeRepository.findAll(specification, page).map(accountStatusTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccountStatusTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AccountStatusType> specification = createSpecification(criteria);
        return accountStatusTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link AccountStatusTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccountStatusType> createSpecification(AccountStatusTypeCriteria criteria) {
        Specification<AccountStatusType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AccountStatusType_.id));
            }
            if (criteria.getAccountStatusCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAccountStatusCode(), AccountStatusType_.accountStatusCode));
            }
            if (criteria.getAccountStatusType() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getAccountStatusType(), AccountStatusType_.accountStatusType));
            }
        }
        return specification;
    }
}
