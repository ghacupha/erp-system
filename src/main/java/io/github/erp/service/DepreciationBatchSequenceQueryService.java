package io.github.erp.service;

/*-
 * Erp System - Mark V No 7 (Ehud Series) Server ver 1.5.0
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
import io.github.erp.domain.DepreciationBatchSequence;
import io.github.erp.repository.DepreciationBatchSequenceRepository;
import io.github.erp.repository.search.DepreciationBatchSequenceSearchRepository;
import io.github.erp.service.criteria.DepreciationBatchSequenceCriteria;
import io.github.erp.service.dto.DepreciationBatchSequenceDTO;
import io.github.erp.service.mapper.DepreciationBatchSequenceMapper;
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
 * Service for executing complex queries for {@link DepreciationBatchSequence} entities in the database.
 * The main input is a {@link DepreciationBatchSequenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepreciationBatchSequenceDTO} or a {@link Page} of {@link DepreciationBatchSequenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepreciationBatchSequenceQueryService extends QueryService<DepreciationBatchSequence> {

    private final Logger log = LoggerFactory.getLogger(DepreciationBatchSequenceQueryService.class);

    private final DepreciationBatchSequenceRepository depreciationBatchSequenceRepository;

    private final DepreciationBatchSequenceMapper depreciationBatchSequenceMapper;

    private final DepreciationBatchSequenceSearchRepository depreciationBatchSequenceSearchRepository;

    public DepreciationBatchSequenceQueryService(
        DepreciationBatchSequenceRepository depreciationBatchSequenceRepository,
        DepreciationBatchSequenceMapper depreciationBatchSequenceMapper,
        DepreciationBatchSequenceSearchRepository depreciationBatchSequenceSearchRepository
    ) {
        this.depreciationBatchSequenceRepository = depreciationBatchSequenceRepository;
        this.depreciationBatchSequenceMapper = depreciationBatchSequenceMapper;
        this.depreciationBatchSequenceSearchRepository = depreciationBatchSequenceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DepreciationBatchSequenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepreciationBatchSequenceDTO> findByCriteria(DepreciationBatchSequenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DepreciationBatchSequence> specification = createSpecification(criteria);
        return depreciationBatchSequenceMapper.toDto(depreciationBatchSequenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepreciationBatchSequenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepreciationBatchSequenceDTO> findByCriteria(DepreciationBatchSequenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepreciationBatchSequence> specification = createSpecification(criteria);
        return depreciationBatchSequenceRepository.findAll(specification, page).map(depreciationBatchSequenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepreciationBatchSequenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DepreciationBatchSequence> specification = createSpecification(criteria);
        return depreciationBatchSequenceRepository.count(specification);
    }

    /**
     * Function to convert {@link DepreciationBatchSequenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepreciationBatchSequence> createSpecification(DepreciationBatchSequenceCriteria criteria) {
        Specification<DepreciationBatchSequence> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DepreciationBatchSequence_.id));
            }
            if (criteria.getStartIndex() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartIndex(), DepreciationBatchSequence_.startIndex));
            }
            if (criteria.getEndIndex() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndIndex(), DepreciationBatchSequence_.endIndex));
            }
            if (criteria.getCreatedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedAt(), DepreciationBatchSequence_.createdAt));
            }
            if (criteria.getDepreciationBatchStatus() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDepreciationBatchStatus(), DepreciationBatchSequence_.depreciationBatchStatus)
                    );
            }
            if (criteria.getDepreciationJobId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationJobId(),
                            root -> root.join(DepreciationBatchSequence_.depreciationJob, JoinType.LEFT).get(DepreciationJob_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
