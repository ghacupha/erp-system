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
import io.github.erp.domain.FxRateType;
import io.github.erp.repository.FxRateTypeRepository;
import io.github.erp.repository.search.FxRateTypeSearchRepository;
import io.github.erp.service.criteria.FxRateTypeCriteria;
import io.github.erp.service.dto.FxRateTypeDTO;
import io.github.erp.service.mapper.FxRateTypeMapper;
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
 * Service for executing complex queries for {@link FxRateType} entities in the database.
 * The main input is a {@link FxRateTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FxRateTypeDTO} or a {@link Page} of {@link FxRateTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FxRateTypeQueryService extends QueryService<FxRateType> {

    private final Logger log = LoggerFactory.getLogger(FxRateTypeQueryService.class);

    private final FxRateTypeRepository fxRateTypeRepository;

    private final FxRateTypeMapper fxRateTypeMapper;

    private final FxRateTypeSearchRepository fxRateTypeSearchRepository;

    public FxRateTypeQueryService(
        FxRateTypeRepository fxRateTypeRepository,
        FxRateTypeMapper fxRateTypeMapper,
        FxRateTypeSearchRepository fxRateTypeSearchRepository
    ) {
        this.fxRateTypeRepository = fxRateTypeRepository;
        this.fxRateTypeMapper = fxRateTypeMapper;
        this.fxRateTypeSearchRepository = fxRateTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FxRateTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FxRateTypeDTO> findByCriteria(FxRateTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FxRateType> specification = createSpecification(criteria);
        return fxRateTypeMapper.toDto(fxRateTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FxRateTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FxRateTypeDTO> findByCriteria(FxRateTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FxRateType> specification = createSpecification(criteria);
        return fxRateTypeRepository.findAll(specification, page).map(fxRateTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FxRateTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FxRateType> specification = createSpecification(criteria);
        return fxRateTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FxRateTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FxRateType> createSpecification(FxRateTypeCriteria criteria) {
        Specification<FxRateType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FxRateType_.id));
            }
            if (criteria.getFxRateCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFxRateCode(), FxRateType_.fxRateCode));
            }
            if (criteria.getFxRateType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFxRateType(), FxRateType_.fxRateType));
            }
        }
        return specification;
    }
}
