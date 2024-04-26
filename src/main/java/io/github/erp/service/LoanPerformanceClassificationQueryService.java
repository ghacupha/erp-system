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
import io.github.erp.domain.LoanPerformanceClassification;
import io.github.erp.repository.LoanPerformanceClassificationRepository;
import io.github.erp.repository.search.LoanPerformanceClassificationSearchRepository;
import io.github.erp.service.criteria.LoanPerformanceClassificationCriteria;
import io.github.erp.service.dto.LoanPerformanceClassificationDTO;
import io.github.erp.service.mapper.LoanPerformanceClassificationMapper;
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
 * Service for executing complex queries for {@link LoanPerformanceClassification} entities in the database.
 * The main input is a {@link LoanPerformanceClassificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LoanPerformanceClassificationDTO} or a {@link Page} of {@link LoanPerformanceClassificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LoanPerformanceClassificationQueryService extends QueryService<LoanPerformanceClassification> {

    private final Logger log = LoggerFactory.getLogger(LoanPerformanceClassificationQueryService.class);

    private final LoanPerformanceClassificationRepository loanPerformanceClassificationRepository;

    private final LoanPerformanceClassificationMapper loanPerformanceClassificationMapper;

    private final LoanPerformanceClassificationSearchRepository loanPerformanceClassificationSearchRepository;

    public LoanPerformanceClassificationQueryService(
        LoanPerformanceClassificationRepository loanPerformanceClassificationRepository,
        LoanPerformanceClassificationMapper loanPerformanceClassificationMapper,
        LoanPerformanceClassificationSearchRepository loanPerformanceClassificationSearchRepository
    ) {
        this.loanPerformanceClassificationRepository = loanPerformanceClassificationRepository;
        this.loanPerformanceClassificationMapper = loanPerformanceClassificationMapper;
        this.loanPerformanceClassificationSearchRepository = loanPerformanceClassificationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LoanPerformanceClassificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LoanPerformanceClassificationDTO> findByCriteria(LoanPerformanceClassificationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LoanPerformanceClassification> specification = createSpecification(criteria);
        return loanPerformanceClassificationMapper.toDto(loanPerformanceClassificationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LoanPerformanceClassificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LoanPerformanceClassificationDTO> findByCriteria(LoanPerformanceClassificationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LoanPerformanceClassification> specification = createSpecification(criteria);
        return loanPerformanceClassificationRepository.findAll(specification, page).map(loanPerformanceClassificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LoanPerformanceClassificationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LoanPerformanceClassification> specification = createSpecification(criteria);
        return loanPerformanceClassificationRepository.count(specification);
    }

    /**
     * Function to convert {@link LoanPerformanceClassificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LoanPerformanceClassification> createSpecification(LoanPerformanceClassificationCriteria criteria) {
        Specification<LoanPerformanceClassification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LoanPerformanceClassification_.id));
            }
            if (criteria.getLoanPerformanceClassificationCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getLoanPerformanceClassificationCode(),
                            LoanPerformanceClassification_.loanPerformanceClassificationCode
                        )
                    );
            }
            if (criteria.getLoanPerformanceClassificationType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getLoanPerformanceClassificationType(),
                            LoanPerformanceClassification_.loanPerformanceClassificationType
                        )
                    );
            }
        }
        return specification;
    }
}
