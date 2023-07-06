package io.github.erp.service;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.1
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
import io.github.erp.domain.DepreciationJob;
import io.github.erp.repository.DepreciationJobRepository;
import io.github.erp.repository.search.DepreciationJobSearchRepository;
import io.github.erp.service.criteria.DepreciationJobCriteria;
import io.github.erp.service.dto.DepreciationJobDTO;
import io.github.erp.service.mapper.DepreciationJobMapper;
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
 * Service for executing complex queries for {@link DepreciationJob} entities in the database.
 * The main input is a {@link DepreciationJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepreciationJobDTO} or a {@link Page} of {@link DepreciationJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepreciationJobQueryService extends QueryService<DepreciationJob> {

    private final Logger log = LoggerFactory.getLogger(DepreciationJobQueryService.class);

    private final DepreciationJobRepository depreciationJobRepository;

    private final DepreciationJobMapper depreciationJobMapper;

    private final DepreciationJobSearchRepository depreciationJobSearchRepository;

    public DepreciationJobQueryService(
        DepreciationJobRepository depreciationJobRepository,
        DepreciationJobMapper depreciationJobMapper,
        DepreciationJobSearchRepository depreciationJobSearchRepository
    ) {
        this.depreciationJobRepository = depreciationJobRepository;
        this.depreciationJobMapper = depreciationJobMapper;
        this.depreciationJobSearchRepository = depreciationJobSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DepreciationJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepreciationJobDTO> findByCriteria(DepreciationJobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DepreciationJob> specification = createSpecification(criteria);
        return depreciationJobMapper.toDto(depreciationJobRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepreciationJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepreciationJobDTO> findByCriteria(DepreciationJobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepreciationJob> specification = createSpecification(criteria);
        return depreciationJobRepository.findAll(specification, page).map(depreciationJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepreciationJobCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DepreciationJob> specification = createSpecification(criteria);
        return depreciationJobRepository.count(specification);
    }

    /**
     * Function to convert {@link DepreciationJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepreciationJob> createSpecification(DepreciationJobCriteria criteria) {
        Specification<DepreciationJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DepreciationJob_.id));
            }
            if (criteria.getTimeOfCommencement() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfCommencement(), DepreciationJob_.timeOfCommencement));
            }
            if (criteria.getDepreciationJobStatus() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getDepreciationJobStatus(), DepreciationJob_.depreciationJobStatus));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), DepreciationJob_.description));
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(DepreciationJob_.createdBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getDepreciationPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationPeriodId(),
                            root -> root.join(DepreciationJob_.depreciationPeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
        }
        return specification;
    }
}