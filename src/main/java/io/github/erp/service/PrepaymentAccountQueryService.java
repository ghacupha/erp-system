package io.github.erp.service;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.0-SNAPSHOT
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
import io.github.erp.domain.PrepaymentAccount;
import io.github.erp.repository.PrepaymentAccountRepository;
import io.github.erp.repository.search.PrepaymentAccountSearchRepository;
import io.github.erp.service.criteria.PrepaymentAccountCriteria;
import io.github.erp.service.dto.PrepaymentAccountDTO;
import io.github.erp.service.mapper.PrepaymentAccountMapper;
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
 * Service for executing complex queries for {@link PrepaymentAccount} entities in the database.
 * The main input is a {@link PrepaymentAccountCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentAccountDTO} or a {@link Page} of {@link PrepaymentAccountDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentAccountQueryService extends QueryService<PrepaymentAccount> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentAccountQueryService.class);

    private final PrepaymentAccountRepository prepaymentAccountRepository;

    private final PrepaymentAccountMapper prepaymentAccountMapper;

    private final PrepaymentAccountSearchRepository prepaymentAccountSearchRepository;

    public PrepaymentAccountQueryService(
        PrepaymentAccountRepository prepaymentAccountRepository,
        PrepaymentAccountMapper prepaymentAccountMapper,
        PrepaymentAccountSearchRepository prepaymentAccountSearchRepository
    ) {
        this.prepaymentAccountRepository = prepaymentAccountRepository;
        this.prepaymentAccountMapper = prepaymentAccountMapper;
        this.prepaymentAccountSearchRepository = prepaymentAccountSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentAccountDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentAccountDTO> findByCriteria(PrepaymentAccountCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrepaymentAccount> specification = createSpecification(criteria);
        return prepaymentAccountMapper.toDto(prepaymentAccountRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentAccountDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentAccountDTO> findByCriteria(PrepaymentAccountCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrepaymentAccount> specification = createSpecification(criteria);
        return prepaymentAccountRepository.findAll(specification, page).map(prepaymentAccountMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentAccountCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrepaymentAccount> specification = createSpecification(criteria);
        return prepaymentAccountRepository.count(specification);
    }

    /**
     * Function to convert {@link PrepaymentAccountCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrepaymentAccount> createSpecification(PrepaymentAccountCriteria criteria) {
        Specification<PrepaymentAccount> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrepaymentAccount_.id));
            }
            if (criteria.getCatalogueNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCatalogueNumber(), PrepaymentAccount_.catalogueNumber));
            }
            if (criteria.getParticulars() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParticulars(), PrepaymentAccount_.particulars));
            }
            if (criteria.getPrepaymentAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPrepaymentAmount(), PrepaymentAccount_.prepaymentAmount));
            }
            if (criteria.getPrepaymentGuid() != null) {
                specification = specification.and(buildSpecification(criteria.getPrepaymentGuid(), PrepaymentAccount_.prepaymentGuid));
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(PrepaymentAccount_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getPrepaymentTransactionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrepaymentTransactionId(),
                            root -> root.join(PrepaymentAccount_.prepaymentTransaction, JoinType.LEFT).get(Settlement_.id)
                        )
                    );
            }
            if (criteria.getServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceOutletId(),
                            root -> root.join(PrepaymentAccount_.serviceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDealerId(),
                            root -> root.join(PrepaymentAccount_.dealer, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getDebitAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDebitAccountId(),
                            root -> root.join(PrepaymentAccount_.debitAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getTransferAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransferAccountId(),
                            root -> root.join(PrepaymentAccount_.transferAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(PrepaymentAccount_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getGeneralParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGeneralParametersId(),
                            root -> root.join(PrepaymentAccount_.generalParameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getPrepaymentParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPrepaymentParametersId(),
                            root -> root.join(PrepaymentAccount_.prepaymentParameters, JoinType.LEFT).get(PrepaymentMapping_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(PrepaymentAccount_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
