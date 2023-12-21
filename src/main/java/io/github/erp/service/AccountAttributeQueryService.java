package io.github.erp.service;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import io.github.erp.domain.AccountAttribute;
import io.github.erp.repository.AccountAttributeRepository;
import io.github.erp.repository.search.AccountAttributeSearchRepository;
import io.github.erp.service.criteria.AccountAttributeCriteria;
import io.github.erp.service.dto.AccountAttributeDTO;
import io.github.erp.service.mapper.AccountAttributeMapper;
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
 * Service for executing complex queries for {@link AccountAttribute} entities in the database.
 * The main input is a {@link AccountAttributeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AccountAttributeDTO} or a {@link Page} of {@link AccountAttributeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccountAttributeQueryService extends QueryService<AccountAttribute> {

    private final Logger log = LoggerFactory.getLogger(AccountAttributeQueryService.class);

    private final AccountAttributeRepository accountAttributeRepository;

    private final AccountAttributeMapper accountAttributeMapper;

    private final AccountAttributeSearchRepository accountAttributeSearchRepository;

    public AccountAttributeQueryService(
        AccountAttributeRepository accountAttributeRepository,
        AccountAttributeMapper accountAttributeMapper,
        AccountAttributeSearchRepository accountAttributeSearchRepository
    ) {
        this.accountAttributeRepository = accountAttributeRepository;
        this.accountAttributeMapper = accountAttributeMapper;
        this.accountAttributeSearchRepository = accountAttributeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AccountAttributeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AccountAttributeDTO> findByCriteria(AccountAttributeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AccountAttribute> specification = createSpecification(criteria);
        return accountAttributeMapper.toDto(accountAttributeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AccountAttributeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountAttributeDTO> findByCriteria(AccountAttributeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccountAttribute> specification = createSpecification(criteria);
        return accountAttributeRepository.findAll(specification, page).map(accountAttributeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccountAttributeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AccountAttribute> specification = createSpecification(criteria);
        return accountAttributeRepository.count(specification);
    }

    /**
     * Function to convert {@link AccountAttributeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccountAttribute> createSpecification(AccountAttributeCriteria criteria) {
        Specification<AccountAttribute> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AccountAttribute_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportingDate(), AccountAttribute_.reportingDate));
            }
            if (criteria.getCustomerNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerNumber(), AccountAttribute_.customerNumber));
            }
            if (criteria.getAccountContractNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAccountContractNumber(), AccountAttribute_.accountContractNumber)
                    );
            }
            if (criteria.getAccountName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountName(), AccountAttribute_.accountName));
            }
            if (criteria.getAccountOpeningDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountOpeningDate(), AccountAttribute_.accountOpeningDate));
            }
            if (criteria.getAccountClosingDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountClosingDate(), AccountAttribute_.accountClosingDate));
            }
            if (criteria.getDebitInterestRate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDebitInterestRate(), AccountAttribute_.debitInterestRate));
            }
            if (criteria.getCreditInterestRate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCreditInterestRate(), AccountAttribute_.creditInterestRate));
            }
            if (criteria.getSanctionedAccountLimitFcy() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSanctionedAccountLimitFcy(), AccountAttribute_.sanctionedAccountLimitFcy)
                    );
            }
            if (criteria.getSanctionedAccountLimitLcy() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSanctionedAccountLimitLcy(), AccountAttribute_.sanctionedAccountLimitLcy)
                    );
            }
            if (criteria.getAccountStatusChangeDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getAccountStatusChangeDate(), AccountAttribute_.accountStatusChangeDate)
                    );
            }
            if (criteria.getExpiryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiryDate(), AccountAttribute_.expiryDate));
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(AccountAttribute_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getBranchCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchCodeId(),
                            root -> root.join(AccountAttribute_.branchCode, JoinType.LEFT).get(BankBranchCode_.id)
                        )
                    );
            }
            if (criteria.getAccountOwnershipTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAccountOwnershipTypeId(),
                            root -> root.join(AccountAttribute_.accountOwnershipType, JoinType.LEFT).get(AccountOwnershipType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
