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
import io.github.erp.domain.AgentBankingActivity;
import io.github.erp.repository.AgentBankingActivityRepository;
import io.github.erp.repository.search.AgentBankingActivitySearchRepository;
import io.github.erp.service.criteria.AgentBankingActivityCriteria;
import io.github.erp.service.dto.AgentBankingActivityDTO;
import io.github.erp.service.mapper.AgentBankingActivityMapper;
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
 * Service for executing complex queries for {@link AgentBankingActivity} entities in the database.
 * The main input is a {@link AgentBankingActivityCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AgentBankingActivityDTO} or a {@link Page} of {@link AgentBankingActivityDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AgentBankingActivityQueryService extends QueryService<AgentBankingActivity> {

    private final Logger log = LoggerFactory.getLogger(AgentBankingActivityQueryService.class);

    private final AgentBankingActivityRepository agentBankingActivityRepository;

    private final AgentBankingActivityMapper agentBankingActivityMapper;

    private final AgentBankingActivitySearchRepository agentBankingActivitySearchRepository;

    public AgentBankingActivityQueryService(
        AgentBankingActivityRepository agentBankingActivityRepository,
        AgentBankingActivityMapper agentBankingActivityMapper,
        AgentBankingActivitySearchRepository agentBankingActivitySearchRepository
    ) {
        this.agentBankingActivityRepository = agentBankingActivityRepository;
        this.agentBankingActivityMapper = agentBankingActivityMapper;
        this.agentBankingActivitySearchRepository = agentBankingActivitySearchRepository;
    }

    /**
     * Return a {@link List} of {@link AgentBankingActivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AgentBankingActivityDTO> findByCriteria(AgentBankingActivityCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AgentBankingActivity> specification = createSpecification(criteria);
        return agentBankingActivityMapper.toDto(agentBankingActivityRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AgentBankingActivityDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AgentBankingActivityDTO> findByCriteria(AgentBankingActivityCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AgentBankingActivity> specification = createSpecification(criteria);
        return agentBankingActivityRepository.findAll(specification, page).map(agentBankingActivityMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AgentBankingActivityCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AgentBankingActivity> specification = createSpecification(criteria);
        return agentBankingActivityRepository.count(specification);
    }

    /**
     * Function to convert {@link AgentBankingActivityCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AgentBankingActivity> createSpecification(AgentBankingActivityCriteria criteria) {
        Specification<AgentBankingActivity> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AgentBankingActivity_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportingDate(), AgentBankingActivity_.reportingDate));
            }
            if (criteria.getAgentUniqueId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAgentUniqueId(), AgentBankingActivity_.agentUniqueId));
            }
            if (criteria.getTerminalUniqueId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTerminalUniqueId(), AgentBankingActivity_.terminalUniqueId));
            }
            if (criteria.getTotalCountOfTransactions() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTotalCountOfTransactions(), AgentBankingActivity_.totalCountOfTransactions)
                    );
            }
            if (criteria.getTotalValueOfTransactionsInLCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalValueOfTransactionsInLCY(),
                            AgentBankingActivity_.totalValueOfTransactionsInLCY
                        )
                    );
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(AgentBankingActivity_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getBranchCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchCodeId(),
                            root -> root.join(AgentBankingActivity_.branchCode, JoinType.LEFT).get(BankBranchCode_.id)
                        )
                    );
            }
            if (criteria.getTransactionTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransactionTypeId(),
                            root -> root.join(AgentBankingActivity_.transactionType, JoinType.LEFT).get(BankTransactionType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
