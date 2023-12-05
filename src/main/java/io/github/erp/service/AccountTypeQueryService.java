package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
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
import io.github.erp.domain.AccountType;
import io.github.erp.repository.AccountTypeRepository;
import io.github.erp.repository.search.AccountTypeSearchRepository;
import io.github.erp.service.criteria.AccountTypeCriteria;
import io.github.erp.service.dto.AccountTypeDTO;
import io.github.erp.service.mapper.AccountTypeMapper;
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
 * Service for executing complex queries for {@link AccountType} entities in the database.
 * The main input is a {@link AccountTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AccountTypeDTO} or a {@link Page} of {@link AccountTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccountTypeQueryService extends QueryService<AccountType> {

    private final Logger log = LoggerFactory.getLogger(AccountTypeQueryService.class);

    private final AccountTypeRepository accountTypeRepository;

    private final AccountTypeMapper accountTypeMapper;

    private final AccountTypeSearchRepository accountTypeSearchRepository;

    public AccountTypeQueryService(
        AccountTypeRepository accountTypeRepository,
        AccountTypeMapper accountTypeMapper,
        AccountTypeSearchRepository accountTypeSearchRepository
    ) {
        this.accountTypeRepository = accountTypeRepository;
        this.accountTypeMapper = accountTypeMapper;
        this.accountTypeSearchRepository = accountTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AccountTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AccountTypeDTO> findByCriteria(AccountTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AccountType> specification = createSpecification(criteria);
        return accountTypeMapper.toDto(accountTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AccountTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountTypeDTO> findByCriteria(AccountTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccountType> specification = createSpecification(criteria);
        return accountTypeRepository.findAll(specification, page).map(accountTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccountTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AccountType> specification = createSpecification(criteria);
        return accountTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link AccountTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccountType> createSpecification(AccountTypeCriteria criteria) {
        Specification<AccountType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AccountType_.id));
            }
            if (criteria.getAccountTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountTypeCode(), AccountType_.accountTypeCode));
            }
            if (criteria.getAccountType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountType(), AccountType_.accountType));
            }
        }
        return specification;
    }
}
