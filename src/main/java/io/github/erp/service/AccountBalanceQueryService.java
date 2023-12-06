package io.github.erp.service;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import io.github.erp.domain.AccountBalance;
import io.github.erp.repository.AccountBalanceRepository;
import io.github.erp.repository.search.AccountBalanceSearchRepository;
import io.github.erp.service.criteria.AccountBalanceCriteria;
import io.github.erp.service.dto.AccountBalanceDTO;
import io.github.erp.service.mapper.AccountBalanceMapper;
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
 * Service for executing complex queries for {@link AccountBalance} entities in the database.
 * The main input is a {@link AccountBalanceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AccountBalanceDTO} or a {@link Page} of {@link AccountBalanceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AccountBalanceQueryService extends QueryService<AccountBalance> {

    private final Logger log = LoggerFactory.getLogger(AccountBalanceQueryService.class);

    private final AccountBalanceRepository accountBalanceRepository;

    private final AccountBalanceMapper accountBalanceMapper;

    private final AccountBalanceSearchRepository accountBalanceSearchRepository;

    public AccountBalanceQueryService(
        AccountBalanceRepository accountBalanceRepository,
        AccountBalanceMapper accountBalanceMapper,
        AccountBalanceSearchRepository accountBalanceSearchRepository
    ) {
        this.accountBalanceRepository = accountBalanceRepository;
        this.accountBalanceMapper = accountBalanceMapper;
        this.accountBalanceSearchRepository = accountBalanceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AccountBalanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AccountBalanceDTO> findByCriteria(AccountBalanceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AccountBalance> specification = createSpecification(criteria);
        return accountBalanceMapper.toDto(accountBalanceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AccountBalanceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AccountBalanceDTO> findByCriteria(AccountBalanceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AccountBalance> specification = createSpecification(criteria);
        return accountBalanceRepository.findAll(specification, page).map(accountBalanceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AccountBalanceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AccountBalance> specification = createSpecification(criteria);
        return accountBalanceRepository.count(specification);
    }

    /**
     * Function to convert {@link AccountBalanceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AccountBalance> createSpecification(AccountBalanceCriteria criteria) {
        Specification<AccountBalance> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AccountBalance_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportingDate(), AccountBalance_.reportingDate));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerId(), AccountBalance_.customerId));
            }
            if (criteria.getAccountContractNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAccountContractNumber(), AccountBalance_.accountContractNumber));
            }
            if (criteria.getAccruedInterestBalanceFCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getAccruedInterestBalanceFCY(), AccountBalance_.accruedInterestBalanceFCY)
                    );
            }
            if (criteria.getAccruedInterestBalanceLCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getAccruedInterestBalanceLCY(), AccountBalance_.accruedInterestBalanceLCY)
                    );
            }
            if (criteria.getAccountBalanceFCY() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountBalanceFCY(), AccountBalance_.accountBalanceFCY));
            }
            if (criteria.getAccountBalanceLCY() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountBalanceLCY(), AccountBalance_.accountBalanceLCY));
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(AccountBalance_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getBranchIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchIdId(),
                            root -> root.join(AccountBalance_.branchId, JoinType.LEFT).get(BankBranchCode_.id)
                        )
                    );
            }
            if (criteria.getCurrencyCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCurrencyCodeId(),
                            root -> root.join(AccountBalance_.currencyCode, JoinType.LEFT).get(IsoCurrencyCode_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
