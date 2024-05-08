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
import io.github.erp.domain.RouDepreciationRequest;
import io.github.erp.repository.RouDepreciationRequestRepository;
import io.github.erp.repository.search.RouDepreciationRequestSearchRepository;
import io.github.erp.service.criteria.RouDepreciationRequestCriteria;
import io.github.erp.service.dto.RouDepreciationRequestDTO;
import io.github.erp.service.mapper.RouDepreciationRequestMapper;
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
 * Service for executing complex queries for {@link RouDepreciationRequest} entities in the database.
 * The main input is a {@link RouDepreciationRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouDepreciationRequestDTO} or a {@link Page} of {@link RouDepreciationRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouDepreciationRequestQueryService extends QueryService<RouDepreciationRequest> {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationRequestQueryService.class);

    private final RouDepreciationRequestRepository rouDepreciationRequestRepository;

    private final RouDepreciationRequestMapper rouDepreciationRequestMapper;

    private final RouDepreciationRequestSearchRepository rouDepreciationRequestSearchRepository;

    public RouDepreciationRequestQueryService(
        RouDepreciationRequestRepository rouDepreciationRequestRepository,
        RouDepreciationRequestMapper rouDepreciationRequestMapper,
        RouDepreciationRequestSearchRepository rouDepreciationRequestSearchRepository
    ) {
        this.rouDepreciationRequestRepository = rouDepreciationRequestRepository;
        this.rouDepreciationRequestMapper = rouDepreciationRequestMapper;
        this.rouDepreciationRequestSearchRepository = rouDepreciationRequestSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouDepreciationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouDepreciationRequestDTO> findByCriteria(RouDepreciationRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouDepreciationRequest> specification = createSpecification(criteria);
        return rouDepreciationRequestMapper.toDto(rouDepreciationRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouDepreciationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouDepreciationRequestDTO> findByCriteria(RouDepreciationRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouDepreciationRequest> specification = createSpecification(criteria);
        return rouDepreciationRequestRepository.findAll(specification, page).map(rouDepreciationRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouDepreciationRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouDepreciationRequest> specification = createSpecification(criteria);
        return rouDepreciationRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link RouDepreciationRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouDepreciationRequest> createSpecification(RouDepreciationRequestCriteria criteria) {
        Specification<RouDepreciationRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouDepreciationRequest_.id));
            }
            if (criteria.getRequisitionId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequisitionId(), RouDepreciationRequest_.requisitionId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), RouDepreciationRequest_.timeOfRequest));
            }
            if (criteria.getDepreciationProcessStatus() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDepreciationProcessStatus(), RouDepreciationRequest_.depreciationProcessStatus)
                    );
            }
            if (criteria.getNumberOfEnumeratedItems() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getNumberOfEnumeratedItems(), RouDepreciationRequest_.numberOfEnumeratedItems)
                    );
            }
            if (criteria.getInitiatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInitiatedById(),
                            root -> root.join(RouDepreciationRequest_.initiatedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
