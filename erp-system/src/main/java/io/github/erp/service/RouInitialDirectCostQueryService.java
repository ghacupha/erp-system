package io.github.erp.service;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.RouInitialDirectCost;
import io.github.erp.repository.RouInitialDirectCostRepository;
import io.github.erp.repository.search.RouInitialDirectCostSearchRepository;
import io.github.erp.service.criteria.RouInitialDirectCostCriteria;
import io.github.erp.service.dto.RouInitialDirectCostDTO;
import io.github.erp.service.mapper.RouInitialDirectCostMapper;
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
 * Service for executing complex queries for {@link RouInitialDirectCost} entities in the database.
 * The main input is a {@link RouInitialDirectCostCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouInitialDirectCostDTO} or a {@link Page} of {@link RouInitialDirectCostDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouInitialDirectCostQueryService extends QueryService<RouInitialDirectCost> {

    private final Logger log = LoggerFactory.getLogger(RouInitialDirectCostQueryService.class);

    private final RouInitialDirectCostRepository rouInitialDirectCostRepository;

    private final RouInitialDirectCostMapper rouInitialDirectCostMapper;

    private final RouInitialDirectCostSearchRepository rouInitialDirectCostSearchRepository;

    public RouInitialDirectCostQueryService(
        RouInitialDirectCostRepository rouInitialDirectCostRepository,
        RouInitialDirectCostMapper rouInitialDirectCostMapper,
        RouInitialDirectCostSearchRepository rouInitialDirectCostSearchRepository
    ) {
        this.rouInitialDirectCostRepository = rouInitialDirectCostRepository;
        this.rouInitialDirectCostMapper = rouInitialDirectCostMapper;
        this.rouInitialDirectCostSearchRepository = rouInitialDirectCostSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouInitialDirectCostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouInitialDirectCostDTO> findByCriteria(RouInitialDirectCostCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouInitialDirectCost> specification = createSpecification(criteria);
        return rouInitialDirectCostMapper.toDto(rouInitialDirectCostRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouInitialDirectCostDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouInitialDirectCostDTO> findByCriteria(RouInitialDirectCostCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouInitialDirectCost> specification = createSpecification(criteria);
        return rouInitialDirectCostRepository.findAll(specification, page).map(rouInitialDirectCostMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouInitialDirectCostCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouInitialDirectCost> specification = createSpecification(criteria);
        return rouInitialDirectCostRepository.count(specification);
    }

    /**
     * Function to convert {@link RouInitialDirectCostCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouInitialDirectCost> createSpecification(RouInitialDirectCostCriteria criteria) {
        Specification<RouInitialDirectCost> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouInitialDirectCost_.id));
            }
            if (criteria.getTransactionDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionDate(), RouInitialDirectCost_.transactionDate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), RouInitialDirectCost_.description));
            }
            if (criteria.getCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCost(), RouInitialDirectCost_.cost));
            }
            if (criteria.getReferenceNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReferenceNumber(), RouInitialDirectCost_.referenceNumber));
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(RouInitialDirectCost_.leaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
            if (criteria.getSettlementDetailsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementDetailsId(),
                            root -> root.join(RouInitialDirectCost_.settlementDetails, JoinType.LEFT).get(Settlement_.id)
                        )
                    );
            }
            if (criteria.getTargetROUAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTargetROUAccountId(),
                            root -> root.join(RouInitialDirectCost_.targetROUAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getTransferAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTransferAccountId(),
                            root -> root.join(RouInitialDirectCost_.transferAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(RouInitialDirectCost_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
