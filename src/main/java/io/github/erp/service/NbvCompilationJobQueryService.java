package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.NbvCompilationJob;
import io.github.erp.repository.NbvCompilationJobRepository;
import io.github.erp.repository.search.NbvCompilationJobSearchRepository;
import io.github.erp.service.criteria.NbvCompilationJobCriteria;
import io.github.erp.service.dto.NbvCompilationJobDTO;
import io.github.erp.service.mapper.NbvCompilationJobMapper;
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
 * Service for executing complex queries for {@link NbvCompilationJob} entities in the database.
 * The main input is a {@link NbvCompilationJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NbvCompilationJobDTO} or a {@link Page} of {@link NbvCompilationJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NbvCompilationJobQueryService extends QueryService<NbvCompilationJob> {

    private final Logger log = LoggerFactory.getLogger(NbvCompilationJobQueryService.class);

    private final NbvCompilationJobRepository nbvCompilationJobRepository;

    private final NbvCompilationJobMapper nbvCompilationJobMapper;

    private final NbvCompilationJobSearchRepository nbvCompilationJobSearchRepository;

    public NbvCompilationJobQueryService(
        NbvCompilationJobRepository nbvCompilationJobRepository,
        NbvCompilationJobMapper nbvCompilationJobMapper,
        NbvCompilationJobSearchRepository nbvCompilationJobSearchRepository
    ) {
        this.nbvCompilationJobRepository = nbvCompilationJobRepository;
        this.nbvCompilationJobMapper = nbvCompilationJobMapper;
        this.nbvCompilationJobSearchRepository = nbvCompilationJobSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NbvCompilationJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NbvCompilationJobDTO> findByCriteria(NbvCompilationJobCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NbvCompilationJob> specification = createSpecification(criteria);
        return nbvCompilationJobMapper.toDto(nbvCompilationJobRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NbvCompilationJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NbvCompilationJobDTO> findByCriteria(NbvCompilationJobCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NbvCompilationJob> specification = createSpecification(criteria);
        return nbvCompilationJobRepository.findAll(specification, page).map(nbvCompilationJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NbvCompilationJobCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NbvCompilationJob> specification = createSpecification(criteria);
        return nbvCompilationJobRepository.count(specification);
    }

    /**
     * Function to convert {@link NbvCompilationJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NbvCompilationJob> createSpecification(NbvCompilationJobCriteria criteria) {
        Specification<NbvCompilationJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NbvCompilationJob_.id));
            }
            if (criteria.getCompilationJobIdentifier() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompilationJobIdentifier(), NbvCompilationJob_.compilationJobIdentifier)
                    );
            }
            if (criteria.getCompilationJobTimeOfRequest() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getCompilationJobTimeOfRequest(), NbvCompilationJob_.compilationJobTimeOfRequest)
                    );
            }
            if (criteria.getEntitiesAffected() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getEntitiesAffected(), NbvCompilationJob_.entitiesAffected));
            }
            if (criteria.getCompilationStatus() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getCompilationStatus(), NbvCompilationJob_.compilationStatus));
            }
            if (criteria.getJobTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getJobTitle(), NbvCompilationJob_.jobTitle));
            }
            if (criteria.getNumberOfBatches() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getNumberOfBatches(), NbvCompilationJob_.numberOfBatches));
            }
            if (criteria.getNumberOfProcessedBatches() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNumberOfProcessedBatches(), NbvCompilationJob_.numberOfProcessedBatches)
                    );
            }
            if (criteria.getProcessingTime() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getProcessingTime(), NbvCompilationJob_.processingTime));
            }
            if (criteria.getActivePeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getActivePeriodId(),
                            root -> root.join(NbvCompilationJob_.activePeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
            if (criteria.getInitiatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInitiatedById(),
                            root -> root.join(NbvCompilationJob_.initiatedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
