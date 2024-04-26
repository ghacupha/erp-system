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
import io.github.erp.domain.FxTransactionType;
import io.github.erp.repository.FxTransactionTypeRepository;
import io.github.erp.repository.search.FxTransactionTypeSearchRepository;
import io.github.erp.service.criteria.FxTransactionTypeCriteria;
import io.github.erp.service.dto.FxTransactionTypeDTO;
import io.github.erp.service.mapper.FxTransactionTypeMapper;
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
 * Service for executing complex queries for {@link FxTransactionType} entities in the database.
 * The main input is a {@link FxTransactionTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FxTransactionTypeDTO} or a {@link Page} of {@link FxTransactionTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FxTransactionTypeQueryService extends QueryService<FxTransactionType> {

    private final Logger log = LoggerFactory.getLogger(FxTransactionTypeQueryService.class);

    private final FxTransactionTypeRepository fxTransactionTypeRepository;

    private final FxTransactionTypeMapper fxTransactionTypeMapper;

    private final FxTransactionTypeSearchRepository fxTransactionTypeSearchRepository;

    public FxTransactionTypeQueryService(
        FxTransactionTypeRepository fxTransactionTypeRepository,
        FxTransactionTypeMapper fxTransactionTypeMapper,
        FxTransactionTypeSearchRepository fxTransactionTypeSearchRepository
    ) {
        this.fxTransactionTypeRepository = fxTransactionTypeRepository;
        this.fxTransactionTypeMapper = fxTransactionTypeMapper;
        this.fxTransactionTypeSearchRepository = fxTransactionTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FxTransactionTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FxTransactionTypeDTO> findByCriteria(FxTransactionTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FxTransactionType> specification = createSpecification(criteria);
        return fxTransactionTypeMapper.toDto(fxTransactionTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FxTransactionTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FxTransactionTypeDTO> findByCriteria(FxTransactionTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FxTransactionType> specification = createSpecification(criteria);
        return fxTransactionTypeRepository.findAll(specification, page).map(fxTransactionTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FxTransactionTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FxTransactionType> specification = createSpecification(criteria);
        return fxTransactionTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FxTransactionTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FxTransactionType> createSpecification(FxTransactionTypeCriteria criteria) {
        Specification<FxTransactionType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FxTransactionType_.id));
            }
            if (criteria.getFxTransactionTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFxTransactionTypeCode(), FxTransactionType_.fxTransactionTypeCode)
                    );
            }
            if (criteria.getFxTransactionType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFxTransactionType(), FxTransactionType_.fxTransactionType));
            }
        }
        return specification;
    }
}
