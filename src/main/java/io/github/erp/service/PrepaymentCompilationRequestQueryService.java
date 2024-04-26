package io.github.erp.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.PrepaymentCompilationRequest;
import io.github.erp.repository.PrepaymentCompilationRequestRepository;
import io.github.erp.repository.search.PrepaymentCompilationRequestSearchRepository;
import io.github.erp.service.criteria.PrepaymentCompilationRequestCriteria;
import io.github.erp.service.dto.PrepaymentCompilationRequestDTO;
import io.github.erp.service.mapper.PrepaymentCompilationRequestMapper;
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
 * Service for executing complex queries for {@link PrepaymentCompilationRequest} entities in the database.
 * The main input is a {@link PrepaymentCompilationRequestCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentCompilationRequestDTO} or a {@link Page} of {@link PrepaymentCompilationRequestDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentCompilationRequestQueryService extends QueryService<PrepaymentCompilationRequest> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentCompilationRequestQueryService.class);

    private final PrepaymentCompilationRequestRepository prepaymentCompilationRequestRepository;

    private final PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper;

    private final PrepaymentCompilationRequestSearchRepository prepaymentCompilationRequestSearchRepository;

    public PrepaymentCompilationRequestQueryService(
        PrepaymentCompilationRequestRepository prepaymentCompilationRequestRepository,
        PrepaymentCompilationRequestMapper prepaymentCompilationRequestMapper,
        PrepaymentCompilationRequestSearchRepository prepaymentCompilationRequestSearchRepository
    ) {
        this.prepaymentCompilationRequestRepository = prepaymentCompilationRequestRepository;
        this.prepaymentCompilationRequestMapper = prepaymentCompilationRequestMapper;
        this.prepaymentCompilationRequestSearchRepository = prepaymentCompilationRequestSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentCompilationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentCompilationRequestDTO> findByCriteria(PrepaymentCompilationRequestCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrepaymentCompilationRequest> specification = createSpecification(criteria);
        return prepaymentCompilationRequestMapper.toDto(prepaymentCompilationRequestRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentCompilationRequestDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentCompilationRequestDTO> findByCriteria(PrepaymentCompilationRequestCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrepaymentCompilationRequest> specification = createSpecification(criteria);
        return prepaymentCompilationRequestRepository.findAll(specification, page).map(prepaymentCompilationRequestMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentCompilationRequestCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrepaymentCompilationRequest> specification = createSpecification(criteria);
        return prepaymentCompilationRequestRepository.count(specification);
    }

    /**
     * Function to convert {@link PrepaymentCompilationRequestCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrepaymentCompilationRequest> createSpecification(PrepaymentCompilationRequestCriteria criteria) {
        Specification<PrepaymentCompilationRequest> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrepaymentCompilationRequest_.id));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), PrepaymentCompilationRequest_.timeOfRequest));
            }
            if (criteria.getCompilationStatus() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getCompilationStatus(), PrepaymentCompilationRequest_.compilationStatus));
            }
            if (criteria.getItemsProcessed() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getItemsProcessed(), PrepaymentCompilationRequest_.itemsProcessed));
            }
            if (criteria.getCompilationToken() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getCompilationToken(), PrepaymentCompilationRequest_.compilationToken));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(PrepaymentCompilationRequest_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
