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
import io.github.erp.domain.TACompilationRequest;
import io.github.erp.repository.TACompilationRequestRepository;
import io.github.erp.repository.search.TACompilationRequestSearchRepository;
import io.github.erp.service.criteria.TACompilationRequestCriteria;
import io.github.erp.service.dto.TACompilationRequestDTO;
import io.github.erp.service.mapper.TACompilationRequestMapper;
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
 * Service for executing complex queries for {@link TACompilationRequest} entities in the database.
 * The main input is a {@link TACompilationRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TACompilationRequestDTO} or a {@link Page} of {@link TACompilationRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TACompilationRequestQueryService extends QueryService<TACompilationRequest> {

    private final Logger log = LoggerFactory.getLogger(TACompilationRequestQueryService.class);

    private final TACompilationRequestRepository tACompilationRequestRepository;

    private final TACompilationRequestMapper tACompilationRequestMapper;

    private final TACompilationRequestSearchRepository tACompilationRequestSearchRepository;

    public TACompilationRequestQueryService(
        TACompilationRequestRepository tACompilationRequestRepository,
        TACompilationRequestMapper tACompilationRequestMapper,
        TACompilationRequestSearchRepository tACompilationRequestSearchRepository
    ) {
        this.tACompilationRequestRepository = tACompilationRequestRepository;
        this.tACompilationRequestMapper = tACompilationRequestMapper;
        this.tACompilationRequestSearchRepository = tACompilationRequestSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TACompilationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TACompilationRequestDTO> findByCriteria(TACompilationRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TACompilationRequest> specification = createSpecification(criteria);
        return tACompilationRequestMapper.toDto(tACompilationRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TACompilationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TACompilationRequestDTO> findByCriteria(TACompilationRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TACompilationRequest> specification = createSpecification(criteria);
        return tACompilationRequestRepository.findAll(specification, page).map(tACompilationRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TACompilationRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TACompilationRequest> specification = createSpecification(criteria);
        return tACompilationRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link TACompilationRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TACompilationRequest> createSpecification(TACompilationRequestCriteria criteria) {
        Specification<TACompilationRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TACompilationRequest_.id));
            }
            if (criteria.getRequisitionId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequisitionId(), TACompilationRequest_.requisitionId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), TACompilationRequest_.timeOfRequest));
            }
            if (criteria.getCompilationProcessStatus() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompilationProcessStatus(), TACompilationRequest_.compilationProcessStatus)
                    );
            }
            if (criteria.getNumberOfEnumeratedItems() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNumberOfEnumeratedItems(), TACompilationRequest_.numberOfEnumeratedItems)
                    );
            }
            if (criteria.getBatchJobIdentifier() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getBatchJobIdentifier(), TACompilationRequest_.batchJobIdentifier));
            }
            if (criteria.getCompilationTime() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCompilationTime(), TACompilationRequest_.compilationTime));
            }
            if (criteria.getInvalidated() != null) {
                specification = specification.and(buildSpecification(criteria.getInvalidated(), TACompilationRequest_.invalidated));
            }
            if (criteria.getInitiatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInitiatedById(),
                            root -> root.join(TACompilationRequest_.initiatedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
