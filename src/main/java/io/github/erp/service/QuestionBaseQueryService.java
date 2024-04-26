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
import io.github.erp.domain.QuestionBase;
import io.github.erp.repository.QuestionBaseRepository;
import io.github.erp.repository.search.QuestionBaseSearchRepository;
import io.github.erp.service.criteria.QuestionBaseCriteria;
import io.github.erp.service.dto.QuestionBaseDTO;
import io.github.erp.service.mapper.QuestionBaseMapper;
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
 * Service for executing complex queries for {@link QuestionBase} entities in the database.
 * The main input is a {@link QuestionBaseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestionBaseDTO} or a {@link Page} of {@link QuestionBaseDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionBaseQueryService extends QueryService<QuestionBase> {

    private final Logger log = LoggerFactory.getLogger(QuestionBaseQueryService.class);

    private final QuestionBaseRepository questionBaseRepository;

    private final QuestionBaseMapper questionBaseMapper;

    private final QuestionBaseSearchRepository questionBaseSearchRepository;

    public QuestionBaseQueryService(
        QuestionBaseRepository questionBaseRepository,
        QuestionBaseMapper questionBaseMapper,
        QuestionBaseSearchRepository questionBaseSearchRepository
    ) {
        this.questionBaseRepository = questionBaseRepository;
        this.questionBaseMapper = questionBaseMapper;
        this.questionBaseSearchRepository = questionBaseSearchRepository;
    }

    /**
     * Return a {@link List} of {@link QuestionBaseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionBaseDTO> findByCriteria(QuestionBaseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<QuestionBase> specification = createSpecification(criteria);
        return questionBaseMapper.toDto(questionBaseRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuestionBaseDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionBaseDTO> findByCriteria(QuestionBaseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuestionBase> specification = createSpecification(criteria);
        return questionBaseRepository.findAll(specification, page).map(questionBaseMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionBaseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QuestionBase> specification = createSpecification(criteria);
        return questionBaseRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionBaseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuestionBase> createSpecification(QuestionBaseCriteria criteria) {
        Specification<QuestionBase> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), QuestionBase_.id));
            }
            if (criteria.getContext() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContext(), QuestionBase_.context));
            }
            if (criteria.getSerial() != null) {
                specification = specification.and(buildSpecification(criteria.getSerial(), QuestionBase_.serial));
            }
            if (criteria.getQuestionBaseValue() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getQuestionBaseValue(), QuestionBase_.questionBaseValue));
            }
            if (criteria.getQuestionBaseKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuestionBaseKey(), QuestionBase_.questionBaseKey));
            }
            if (criteria.getQuestionBaseLabel() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getQuestionBaseLabel(), QuestionBase_.questionBaseLabel));
            }
            if (criteria.getRequired() != null) {
                specification = specification.and(buildSpecification(criteria.getRequired(), QuestionBase_.required));
            }
            if (criteria.getOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrder(), QuestionBase_.order));
            }
            if (criteria.getControlType() != null) {
                specification = specification.and(buildSpecification(criteria.getControlType(), QuestionBase_.controlType));
            }
            if (criteria.getPlaceholder() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPlaceholder(), QuestionBase_.placeholder));
            }
            if (criteria.getIterable() != null) {
                specification = specification.and(buildSpecification(criteria.getIterable(), QuestionBase_.iterable));
            }
            if (criteria.getParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParametersId(),
                            root -> root.join(QuestionBase_.parameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderItemId(),
                            root -> root.join(QuestionBase_.placeholderItems, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
