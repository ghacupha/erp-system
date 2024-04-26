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
import io.github.erp.domain.FxTransactionChannelType;
import io.github.erp.repository.FxTransactionChannelTypeRepository;
import io.github.erp.repository.search.FxTransactionChannelTypeSearchRepository;
import io.github.erp.service.criteria.FxTransactionChannelTypeCriteria;
import io.github.erp.service.dto.FxTransactionChannelTypeDTO;
import io.github.erp.service.mapper.FxTransactionChannelTypeMapper;
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
 * Service for executing complex queries for {@link FxTransactionChannelType} entities in the database.
 * The main input is a {@link FxTransactionChannelTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FxTransactionChannelTypeDTO} or a {@link Page} of {@link FxTransactionChannelTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FxTransactionChannelTypeQueryService extends QueryService<FxTransactionChannelType> {

    private final Logger log = LoggerFactory.getLogger(FxTransactionChannelTypeQueryService.class);

    private final FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository;

    private final FxTransactionChannelTypeMapper fxTransactionChannelTypeMapper;

    private final FxTransactionChannelTypeSearchRepository fxTransactionChannelTypeSearchRepository;

    public FxTransactionChannelTypeQueryService(
        FxTransactionChannelTypeRepository fxTransactionChannelTypeRepository,
        FxTransactionChannelTypeMapper fxTransactionChannelTypeMapper,
        FxTransactionChannelTypeSearchRepository fxTransactionChannelTypeSearchRepository
    ) {
        this.fxTransactionChannelTypeRepository = fxTransactionChannelTypeRepository;
        this.fxTransactionChannelTypeMapper = fxTransactionChannelTypeMapper;
        this.fxTransactionChannelTypeSearchRepository = fxTransactionChannelTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FxTransactionChannelTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FxTransactionChannelTypeDTO> findByCriteria(FxTransactionChannelTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FxTransactionChannelType> specification = createSpecification(criteria);
        return fxTransactionChannelTypeMapper.toDto(fxTransactionChannelTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FxTransactionChannelTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FxTransactionChannelTypeDTO> findByCriteria(FxTransactionChannelTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FxTransactionChannelType> specification = createSpecification(criteria);
        return fxTransactionChannelTypeRepository.findAll(specification, page).map(fxTransactionChannelTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FxTransactionChannelTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FxTransactionChannelType> specification = createSpecification(criteria);
        return fxTransactionChannelTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FxTransactionChannelTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FxTransactionChannelType> createSpecification(FxTransactionChannelTypeCriteria criteria) {
        Specification<FxTransactionChannelType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FxTransactionChannelType_.id));
            }
            if (criteria.getFxTransactionChannelCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFxTransactionChannelCode(), FxTransactionChannelType_.fxTransactionChannelCode)
                    );
            }
            if (criteria.getFxTransactionChannelType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFxTransactionChannelType(), FxTransactionChannelType_.fxTransactionChannelType)
                    );
            }
        }
        return specification;
    }
}
