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
import io.github.erp.domain.Algorithm;
import io.github.erp.repository.AlgorithmRepository;
import io.github.erp.repository.search.AlgorithmSearchRepository;
import io.github.erp.service.criteria.AlgorithmCriteria;
import io.github.erp.service.dto.AlgorithmDTO;
import io.github.erp.service.mapper.AlgorithmMapper;
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
 * Service for executing complex queries for {@link Algorithm} entities in the database.
 * The main input is a {@link AlgorithmCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AlgorithmDTO} or a {@link Page} of {@link AlgorithmDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AlgorithmQueryService extends QueryService<Algorithm> {

    private final Logger log = LoggerFactory.getLogger(AlgorithmQueryService.class);

    private final AlgorithmRepository algorithmRepository;

    private final AlgorithmMapper algorithmMapper;

    private final AlgorithmSearchRepository algorithmSearchRepository;

    public AlgorithmQueryService(
        AlgorithmRepository algorithmRepository,
        AlgorithmMapper algorithmMapper,
        AlgorithmSearchRepository algorithmSearchRepository
    ) {
        this.algorithmRepository = algorithmRepository;
        this.algorithmMapper = algorithmMapper;
        this.algorithmSearchRepository = algorithmSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AlgorithmDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AlgorithmDTO> findByCriteria(AlgorithmCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Algorithm> specification = createSpecification(criteria);
        return algorithmMapper.toDto(algorithmRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AlgorithmDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AlgorithmDTO> findByCriteria(AlgorithmCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Algorithm> specification = createSpecification(criteria);
        return algorithmRepository.findAll(specification, page).map(algorithmMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AlgorithmCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Algorithm> specification = createSpecification(criteria);
        return algorithmRepository.count(specification);
    }

    /**
     * Function to convert {@link AlgorithmCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Algorithm> createSpecification(AlgorithmCriteria criteria) {
        Specification<Algorithm> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Algorithm_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Algorithm_.name));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(Algorithm_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getParametersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getParametersId(),
                            root -> root.join(Algorithm_.parameters, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
