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
import io.github.erp.domain.CrbAccountHolderType;
import io.github.erp.repository.CrbAccountHolderTypeRepository;
import io.github.erp.repository.search.CrbAccountHolderTypeSearchRepository;
import io.github.erp.service.criteria.CrbAccountHolderTypeCriteria;
import io.github.erp.service.dto.CrbAccountHolderTypeDTO;
import io.github.erp.service.mapper.CrbAccountHolderTypeMapper;
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
 * Service for executing complex queries for {@link CrbAccountHolderType} entities in the database.
 * The main input is a {@link CrbAccountHolderTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbAccountHolderTypeDTO} or a {@link Page} of {@link CrbAccountHolderTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbAccountHolderTypeQueryService extends QueryService<CrbAccountHolderType> {

    private final Logger log = LoggerFactory.getLogger(CrbAccountHolderTypeQueryService.class);

    private final CrbAccountHolderTypeRepository crbAccountHolderTypeRepository;

    private final CrbAccountHolderTypeMapper crbAccountHolderTypeMapper;

    private final CrbAccountHolderTypeSearchRepository crbAccountHolderTypeSearchRepository;

    public CrbAccountHolderTypeQueryService(
        CrbAccountHolderTypeRepository crbAccountHolderTypeRepository,
        CrbAccountHolderTypeMapper crbAccountHolderTypeMapper,
        CrbAccountHolderTypeSearchRepository crbAccountHolderTypeSearchRepository
    ) {
        this.crbAccountHolderTypeRepository = crbAccountHolderTypeRepository;
        this.crbAccountHolderTypeMapper = crbAccountHolderTypeMapper;
        this.crbAccountHolderTypeSearchRepository = crbAccountHolderTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbAccountHolderTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbAccountHolderTypeDTO> findByCriteria(CrbAccountHolderTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbAccountHolderType> specification = createSpecification(criteria);
        return crbAccountHolderTypeMapper.toDto(crbAccountHolderTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbAccountHolderTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbAccountHolderTypeDTO> findByCriteria(CrbAccountHolderTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbAccountHolderType> specification = createSpecification(criteria);
        return crbAccountHolderTypeRepository.findAll(specification, page).map(crbAccountHolderTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbAccountHolderTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbAccountHolderType> specification = createSpecification(criteria);
        return crbAccountHolderTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbAccountHolderTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbAccountHolderType> createSpecification(CrbAccountHolderTypeCriteria criteria) {
        Specification<CrbAccountHolderType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbAccountHolderType_.id));
            }
            if (criteria.getAccountHolderCategoryTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAccountHolderCategoryTypeCode(),
                            CrbAccountHolderType_.accountHolderCategoryTypeCode
                        )
                    );
            }
            if (criteria.getAccountHolderCategoryType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAccountHolderCategoryType(), CrbAccountHolderType_.accountHolderCategoryType)
                    );
            }
        }
        return specification;
    }
}
