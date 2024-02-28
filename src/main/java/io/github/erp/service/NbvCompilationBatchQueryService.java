package io.github.erp.service;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.NbvCompilationBatch;
import io.github.erp.repository.NbvCompilationBatchRepository;
import io.github.erp.repository.search.NbvCompilationBatchSearchRepository;
import io.github.erp.service.criteria.NbvCompilationBatchCriteria;
import io.github.erp.service.dto.NbvCompilationBatchDTO;
import io.github.erp.service.mapper.NbvCompilationBatchMapper;
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
 * Service for executing complex queries for {@link NbvCompilationBatch} entities in the database.
 * The main input is a {@link NbvCompilationBatchCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NbvCompilationBatchDTO} or a {@link Page} of {@link NbvCompilationBatchDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NbvCompilationBatchQueryService extends QueryService<NbvCompilationBatch> {

    private final Logger log = LoggerFactory.getLogger(NbvCompilationBatchQueryService.class);

    private final NbvCompilationBatchRepository nbvCompilationBatchRepository;

    private final NbvCompilationBatchMapper nbvCompilationBatchMapper;

    private final NbvCompilationBatchSearchRepository nbvCompilationBatchSearchRepository;

    public NbvCompilationBatchQueryService(
        NbvCompilationBatchRepository nbvCompilationBatchRepository,
        NbvCompilationBatchMapper nbvCompilationBatchMapper,
        NbvCompilationBatchSearchRepository nbvCompilationBatchSearchRepository
    ) {
        this.nbvCompilationBatchRepository = nbvCompilationBatchRepository;
        this.nbvCompilationBatchMapper = nbvCompilationBatchMapper;
        this.nbvCompilationBatchSearchRepository = nbvCompilationBatchSearchRepository;
    }

    /**
     * Return a {@link List} of {@link NbvCompilationBatchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NbvCompilationBatchDTO> findByCriteria(NbvCompilationBatchCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NbvCompilationBatch> specification = createSpecification(criteria);
        return nbvCompilationBatchMapper.toDto(nbvCompilationBatchRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NbvCompilationBatchDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NbvCompilationBatchDTO> findByCriteria(NbvCompilationBatchCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NbvCompilationBatch> specification = createSpecification(criteria);
        return nbvCompilationBatchRepository.findAll(specification, page).map(nbvCompilationBatchMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NbvCompilationBatchCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NbvCompilationBatch> specification = createSpecification(criteria);
        return nbvCompilationBatchRepository.count(specification);
    }

    /**
     * Function to convert {@link NbvCompilationBatchCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NbvCompilationBatch> createSpecification(NbvCompilationBatchCriteria criteria) {
        Specification<NbvCompilationBatch> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NbvCompilationBatch_.id));
            }
            if (criteria.getStartIndex() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartIndex(), NbvCompilationBatch_.startIndex));
            }
            if (criteria.getEndIndex() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndIndex(), NbvCompilationBatch_.endIndex));
            }
            if (criteria.getCompilationBatchStatus() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompilationBatchStatus(), NbvCompilationBatch_.compilationBatchStatus)
                    );
            }
            if (criteria.getCompilationBatchIdentifier() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompilationBatchIdentifier(), NbvCompilationBatch_.compilationBatchIdentifier)
                    );
            }
            if (criteria.getCompilationJobidentifier() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompilationJobidentifier(), NbvCompilationBatch_.compilationJobidentifier)
                    );
            }
            if (criteria.getDepreciationPeriodIdentifier() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDepreciationPeriodIdentifier(), NbvCompilationBatch_.depreciationPeriodIdentifier)
                    );
            }
            if (criteria.getFiscalMonthIdentifier() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getFiscalMonthIdentifier(), NbvCompilationBatch_.fiscalMonthIdentifier));
            }
            if (criteria.getBatchSize() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBatchSize(), NbvCompilationBatch_.batchSize));
            }
            if (criteria.getProcessedItems() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getProcessedItems(), NbvCompilationBatch_.processedItems));
            }
            if (criteria.getSequenceNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSequenceNumber(), NbvCompilationBatch_.sequenceNumber));
            }
            if (criteria.getIsLastBatch() != null) {
                specification = specification.and(buildSpecification(criteria.getIsLastBatch(), NbvCompilationBatch_.isLastBatch));
            }
            if (criteria.getProcessingTime() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getProcessingTime(), NbvCompilationBatch_.processingTime));
            }
            if (criteria.getTotalItems() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalItems(), NbvCompilationBatch_.totalItems));
            }
            if (criteria.getNbvCompilationJobId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNbvCompilationJobId(),
                            root -> root.join(NbvCompilationBatch_.nbvCompilationJob, JoinType.LEFT).get(NbvCompilationJob_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
