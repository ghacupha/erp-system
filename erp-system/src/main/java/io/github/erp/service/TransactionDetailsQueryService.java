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
import io.github.erp.domain.TransactionDetails;
import io.github.erp.repository.TransactionDetailsRepository;
import io.github.erp.repository.search.TransactionDetailsSearchRepository;
import io.github.erp.service.criteria.TransactionDetailsCriteria;
import io.github.erp.service.dto.TransactionDetailsDTO;
import io.github.erp.service.mapper.TransactionDetailsMapper;
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
 * Service for executing complex queries for {@link TransactionDetails} entities in the database.
 * The main input is a {@link TransactionDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TransactionDetailsDTO} or a {@link Page} of {@link TransactionDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TransactionDetailsQueryService extends QueryService<TransactionDetails> {

    private final Logger log = LoggerFactory.getLogger(TransactionDetailsQueryService.class);

    private final TransactionDetailsRepository transactionDetailsRepository;

    private final TransactionDetailsMapper transactionDetailsMapper;

    private final TransactionDetailsSearchRepository transactionDetailsSearchRepository;

    public TransactionDetailsQueryService(
        TransactionDetailsRepository transactionDetailsRepository,
        TransactionDetailsMapper transactionDetailsMapper,
        TransactionDetailsSearchRepository transactionDetailsSearchRepository
    ) {
        this.transactionDetailsRepository = transactionDetailsRepository;
        this.transactionDetailsMapper = transactionDetailsMapper;
        this.transactionDetailsSearchRepository = transactionDetailsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TransactionDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TransactionDetailsDTO> findByCriteria(TransactionDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TransactionDetails> specification = createSpecification(criteria);
        return transactionDetailsMapper.toDto(transactionDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TransactionDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TransactionDetailsDTO> findByCriteria(TransactionDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TransactionDetails> specification = createSpecification(criteria);
        return transactionDetailsRepository.findAll(specification, page).map(transactionDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TransactionDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TransactionDetails> specification = createSpecification(criteria);
        return transactionDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link TransactionDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TransactionDetails> createSpecification(TransactionDetailsCriteria criteria) {
        Specification<TransactionDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TransactionDetails_.id));
            }
            if (criteria.getEntryId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEntryId(), TransactionDetails_.entryId));
            }
            if (criteria.getTransactionDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionDate(), TransactionDetails_.transactionDate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), TransactionDetails_.description));
            }
            if (criteria.getAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAmount(), TransactionDetails_.amount));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), TransactionDetails_.isDeleted));
            }
            if (criteria.getPostingId() != null) {
                specification = specification.and(buildSpecification(criteria.getPostingId(), TransactionDetails_.postingId));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), TransactionDetails_.createdAt));
            }
            if (criteria.getModifiedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModifiedAt(), TransactionDetails_.modifiedAt));
            }
            if (criteria.getTransactionType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTransactionType(), TransactionDetails_.transactionType));
            }
            if (criteria.getDebitAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDebitAccountId(),
                            root -> root.join(TransactionDetails_.debitAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getCreditAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreditAccountId(),
                            root -> root.join(TransactionDetails_.creditAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(TransactionDetails_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getPostedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPostedById(),
                            root -> root.join(TransactionDetails_.postedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
