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
import io.github.erp.domain.ProductType;
import io.github.erp.repository.ProductTypeRepository;
import io.github.erp.repository.search.ProductTypeSearchRepository;
import io.github.erp.service.criteria.ProductTypeCriteria;
import io.github.erp.service.dto.ProductTypeDTO;
import io.github.erp.service.mapper.ProductTypeMapper;
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
 * Service for executing complex queries for {@link ProductType} entities in the database.
 * The main input is a {@link ProductTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ProductTypeDTO} or a {@link Page} of {@link ProductTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ProductTypeQueryService extends QueryService<ProductType> {

    private final Logger log = LoggerFactory.getLogger(ProductTypeQueryService.class);

    private final ProductTypeRepository productTypeRepository;

    private final ProductTypeMapper productTypeMapper;

    private final ProductTypeSearchRepository productTypeSearchRepository;

    public ProductTypeQueryService(
        ProductTypeRepository productTypeRepository,
        ProductTypeMapper productTypeMapper,
        ProductTypeSearchRepository productTypeSearchRepository
    ) {
        this.productTypeRepository = productTypeRepository;
        this.productTypeMapper = productTypeMapper;
        this.productTypeSearchRepository = productTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ProductTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ProductTypeDTO> findByCriteria(ProductTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeMapper.toDto(productTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ProductTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ProductTypeDTO> findByCriteria(ProductTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeRepository.findAll(specification, page).map(productTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ProductTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ProductType> specification = createSpecification(criteria);
        return productTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ProductTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ProductType> createSpecification(ProductTypeCriteria criteria) {
        Specification<ProductType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ProductType_.id));
            }
            if (criteria.getProductCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductCode(), ProductType_.productCode));
            }
            if (criteria.getProductType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getProductType(), ProductType_.productType));
            }
        }
        return specification;
    }
}
