package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.TransactionAccountPostingProcessType;
import io.github.erp.repository.TransactionAccountPostingProcessTypeRepository;
import io.github.erp.repository.search.TransactionAccountPostingProcessTypeSearchRepository;
import io.github.erp.service.criteria.TransactionAccountPostingProcessTypeCriteria;
import io.github.erp.service.dto.TransactionAccountPostingProcessTypeDTO;
import io.github.erp.service.mapper.TransactionAccountPostingProcessTypeMapper;
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
 * Service for executing complex queries for {@link TransactionAccountPostingProcessType} entities in the database.
 * The main input is a {@link TransactionAccountPostingProcessTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionAccountPostingProcessTypeDTO} or a {@link Page} of {@link TransactionAccountPostingProcessTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionAccountPostingProcessTypeQueryService extends QueryService<TransactionAccountPostingProcessType> {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountPostingProcessTypeQueryService.class);

    private final TransactionAccountPostingProcessTypeRepository transactionAccountPostingProcessTypeRepository;

    private final TransactionAccountPostingProcessTypeMapper transactionAccountPostingProcessTypeMapper;

    private final TransactionAccountPostingProcessTypeSearchRepository transactionAccountPostingProcessTypeSearchRepository;

    public TransactionAccountPostingProcessTypeQueryService(
        TransactionAccountPostingProcessTypeRepository transactionAccountPostingProcessTypeRepository,
        TransactionAccountPostingProcessTypeMapper transactionAccountPostingProcessTypeMapper,
        TransactionAccountPostingProcessTypeSearchRepository transactionAccountPostingProcessTypeSearchRepository
    ) {
        this.transactionAccountPostingProcessTypeRepository = transactionAccountPostingProcessTypeRepository;
        this.transactionAccountPostingProcessTypeMapper = transactionAccountPostingProcessTypeMapper;
        this.transactionAccountPostingProcessTypeSearchRepository = transactionAccountPostingProcessTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionAccountPostingProcessTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionAccountPostingProcessTypeDTO> findByCriteria(TransactionAccountPostingProcessTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransactionAccountPostingProcessType> specification = createSpecification(criteria);
        return transactionAccountPostingProcessTypeMapper.toDto(transactionAccountPostingProcessTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransactionAccountPostingProcessTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingProcessTypeDTO> findByCriteria(
        TransactionAccountPostingProcessTypeCriteria criteria,
        Pageable page
    ) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionAccountPostingProcessType> specification = createSpecification(criteria);
        return transactionAccountPostingProcessTypeRepository
            .findAll(specification, page)
            .map(transactionAccountPostingProcessTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionAccountPostingProcessTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransactionAccountPostingProcessType> specification = createSpecification(criteria);
        return transactionAccountPostingProcessTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionAccountPostingProcessTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionAccountPostingProcessType> createSpecification(
        TransactionAccountPostingProcessTypeCriteria criteria
    ) {
        Specification<TransactionAccountPostingProcessType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransactionAccountPostingProcessType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TransactionAccountPostingProcessType_.name));
            }
            if (criteria.getDebitAccountTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDebitAccountTypeId(),
                            root ->
                                root
                                    .join(TransactionAccountPostingProcessType_.debitAccountType, JoinType.LEFT)
                                    .get(TransactionAccountCategory_.id)
                        )
                    );
            }
            if (criteria.getCreditAccountTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreditAccountTypeId(),
                            root ->
                                root
                                    .join(TransactionAccountPostingProcessType_.creditAccountType, JoinType.LEFT)
                                    .get(TransactionAccountCategory_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(TransactionAccountPostingProcessType_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
