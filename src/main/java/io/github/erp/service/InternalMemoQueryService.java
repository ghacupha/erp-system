package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.InternalMemo;
import io.github.erp.repository.InternalMemoRepository;
import io.github.erp.repository.search.InternalMemoSearchRepository;
import io.github.erp.service.criteria.InternalMemoCriteria;
import io.github.erp.service.dto.InternalMemoDTO;
import io.github.erp.service.mapper.InternalMemoMapper;
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
 * Service for executing complex queries for {@link InternalMemo} entities in the database.
 * The main input is a {@link InternalMemoCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InternalMemoDTO} or a {@link Page} of {@link InternalMemoDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InternalMemoQueryService extends QueryService<InternalMemo> {

    private final Logger log = LoggerFactory.getLogger(InternalMemoQueryService.class);

    private final InternalMemoRepository internalMemoRepository;

    private final InternalMemoMapper internalMemoMapper;

    private final InternalMemoSearchRepository internalMemoSearchRepository;

    public InternalMemoQueryService(
        InternalMemoRepository internalMemoRepository,
        InternalMemoMapper internalMemoMapper,
        InternalMemoSearchRepository internalMemoSearchRepository
    ) {
        this.internalMemoRepository = internalMemoRepository;
        this.internalMemoMapper = internalMemoMapper;
        this.internalMemoSearchRepository = internalMemoSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InternalMemoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InternalMemoDTO> findByCriteria(InternalMemoCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InternalMemo> specification = createSpecification(criteria);
        return internalMemoMapper.toDto(internalMemoRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InternalMemoDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InternalMemoDTO> findByCriteria(InternalMemoCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InternalMemo> specification = createSpecification(criteria);
        return internalMemoRepository.findAll(specification, page).map(internalMemoMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InternalMemoCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InternalMemo> specification = createSpecification(criteria);
        return internalMemoRepository.count(specification);
    }

    /**
     * Function to convert {@link InternalMemoCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InternalMemo> createSpecification(InternalMemoCriteria criteria) {
        Specification<InternalMemo> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InternalMemo_.id));
            }
            if (criteria.getCatalogueNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCatalogueNumber(), InternalMemo_.catalogueNumber));
            }
            if (criteria.getReferenceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReferenceNumber(), InternalMemo_.referenceNumber));
            }
            if (criteria.getMemoDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMemoDate(), InternalMemo_.memoDate));
            }
            if (criteria.getPurposeDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPurposeDescription(), InternalMemo_.purposeDescription));
            }
            if (criteria.getActionRequiredId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getActionRequiredId(),
                            root -> root.join(InternalMemo_.actionRequired, JoinType.LEFT).get(MemoAction_.id)
                        )
                    );
            }
            if (criteria.getAddressedToId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAddressedToId(),
                            root -> root.join(InternalMemo_.addressedTo, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getFromId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getFromId(), root -> root.join(InternalMemo_.from, JoinType.LEFT).get(Dealer_.id))
                    );
            }
            if (criteria.getPreparedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPreparedById(),
                            root -> root.join(InternalMemo_.preparedBies, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getReviewedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReviewedById(),
                            root -> root.join(InternalMemo_.reviewedBies, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getApprovedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApprovedById(),
                            root -> root.join(InternalMemo_.approvedBies, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getMemoDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMemoDocumentId(),
                            root -> root.join(InternalMemo_.memoDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(InternalMemo_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
