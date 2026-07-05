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
import io.github.erp.domain.TransactionAccountPostingRun;
import io.github.erp.repository.TransactionAccountPostingRunRepository;
import io.github.erp.repository.search.TransactionAccountPostingRunSearchRepository;
import io.github.erp.service.criteria.TransactionAccountPostingRunCriteria;
import io.github.erp.service.dto.TransactionAccountPostingRunDTO;
import io.github.erp.service.mapper.TransactionAccountPostingRunMapper;
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
 * Service for executing complex queries for {@link TransactionAccountPostingRun} entities in the database.
 * The main input is a {@link TransactionAccountPostingRunCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionAccountPostingRunDTO} or a {@link Page} of {@link TransactionAccountPostingRunDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionAccountPostingRunQueryService extends QueryService<TransactionAccountPostingRun> {

    private final Logger log = LoggerFactory.getLogger(TransactionAccountPostingRunQueryService.class);

    private final TransactionAccountPostingRunRepository transactionAccountPostingRunRepository;

    private final TransactionAccountPostingRunMapper transactionAccountPostingRunMapper;

    private final TransactionAccountPostingRunSearchRepository transactionAccountPostingRunSearchRepository;

    public TransactionAccountPostingRunQueryService(
        TransactionAccountPostingRunRepository transactionAccountPostingRunRepository,
        TransactionAccountPostingRunMapper transactionAccountPostingRunMapper,
        TransactionAccountPostingRunSearchRepository transactionAccountPostingRunSearchRepository
    ) {
        this.transactionAccountPostingRunRepository = transactionAccountPostingRunRepository;
        this.transactionAccountPostingRunMapper = transactionAccountPostingRunMapper;
        this.transactionAccountPostingRunSearchRepository = transactionAccountPostingRunSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionAccountPostingRunDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionAccountPostingRunDTO> findByCriteria(TransactionAccountPostingRunCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransactionAccountPostingRun> specification = createSpecification(criteria);
        return transactionAccountPostingRunMapper.toDto(transactionAccountPostingRunRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransactionAccountPostingRunDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionAccountPostingRunDTO> findByCriteria(TransactionAccountPostingRunCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionAccountPostingRun> specification = createSpecification(criteria);
        return transactionAccountPostingRunRepository.findAll(specification, page).map(transactionAccountPostingRunMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionAccountPostingRunCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransactionAccountPostingRun> specification = createSpecification(criteria);
        return transactionAccountPostingRunRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionAccountPostingRunCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionAccountPostingRun> createSpecification(TransactionAccountPostingRunCriteria criteria) {
        Specification<TransactionAccountPostingRun> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransactionAccountPostingRun_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), TransactionAccountPostingRun_.name));
            }
            if (criteria.getIdentifier() != null) {
                specification = specification.and(buildSpecification(criteria.getIdentifier(), TransactionAccountPostingRun_.identifier));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(TransactionAccountPostingRun_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
