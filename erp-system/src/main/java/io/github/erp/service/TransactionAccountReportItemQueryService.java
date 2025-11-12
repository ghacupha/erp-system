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
import io.github.erp.domain.TransactionAccountReportItem;
import io.github.erp.repository.TransactionAccountReportItemRepository;
import io.github.erp.repository.search.TransactionAccountReportItemSearchRepository;
import io.github.erp.service.criteria.TransactionAccountReportItemCriteria;
import io.github.erp.service.dto.TransactionAccountReportItemDTO;
import io.github.erp.service.mapper.TransactionAccountReportItemMapper;
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
 * Service for executing complex queries for {@link TransactionAccountReportItem} entities in the database.
 * The main input is a {@link TransactionAccountReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionAccountReportItemDTO} or a {@link Page} of {@link TransactionAccountReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionAccountReportItemQueryService extends QueryService<TransactionAccountReportItem> {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountReportItemQueryService.class);

    private final TransactionAccountReportItemRepository transactionAccountReportItemRepository;

    private final TransactionAccountReportItemMapper transactionAccountReportItemMapper;

    private final TransactionAccountReportItemSearchRepository transactionAccountReportItemSearchRepository;

    public TransactionAccountReportItemQueryService(
        TransactionAccountReportItemRepository transactionAccountReportItemRepository,
        TransactionAccountReportItemMapper transactionAccountReportItemMapper,
        TransactionAccountReportItemSearchRepository transactionAccountReportItemSearchRepository
    ) {
        this.transactionAccountReportItemRepository = transactionAccountReportItemRepository;
        this.transactionAccountReportItemMapper = transactionAccountReportItemMapper;
        this.transactionAccountReportItemSearchRepository = transactionAccountReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionAccountReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionAccountReportItemDTO> findByCriteria(TransactionAccountReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransactionAccountReportItem> specification = createSpecification(criteria);
        return transactionAccountReportItemMapper.toDto(transactionAccountReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransactionAccountReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionAccountReportItemDTO> findByCriteria(TransactionAccountReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionAccountReportItem> specification = createSpecification(criteria);
        return transactionAccountReportItemRepository.findAll(specification, page).map(transactionAccountReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionAccountReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransactionAccountReportItem> specification = createSpecification(criteria);
        return transactionAccountReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionAccountReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionAccountReportItem> createSpecification(TransactionAccountReportItemCriteria criteria) {
        Specification<TransactionAccountReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransactionAccountReportItem_.id));
            }
            if (criteria.getAccountName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAccountName(), TransactionAccountReportItem_.accountName));
            }
            if (criteria.getAccountNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAccountNumber(), TransactionAccountReportItem_.accountNumber));
            }
            if (criteria.getAccountBalance() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccountBalance(), TransactionAccountReportItem_.accountBalance));
            }
        }
        return specification;
    }
}
