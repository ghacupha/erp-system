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
import io.github.erp.domain.TaxReference;
import io.github.erp.repository.TaxReferenceRepository;
import io.github.erp.repository.search.TaxReferenceSearchRepository;
import io.github.erp.service.criteria.TaxReferenceCriteria;
import io.github.erp.service.dto.TaxReferenceDTO;
import io.github.erp.service.mapper.TaxReferenceMapper;
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
 * Service for executing complex queries for {@link TaxReference} entities in the database.
 * The main input is a {@link TaxReferenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaxReferenceDTO} or a {@link Page} of {@link TaxReferenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaxReferenceQueryService extends QueryService<TaxReference> {

    private final Logger log = LoggerFactory.getLogger(TaxReferenceQueryService.class);

    private final TaxReferenceRepository taxReferenceRepository;

    private final TaxReferenceMapper taxReferenceMapper;

    private final TaxReferenceSearchRepository taxReferenceSearchRepository;

    public TaxReferenceQueryService(
        TaxReferenceRepository taxReferenceRepository,
        TaxReferenceMapper taxReferenceMapper,
        TaxReferenceSearchRepository taxReferenceSearchRepository
    ) {
        this.taxReferenceRepository = taxReferenceRepository;
        this.taxReferenceMapper = taxReferenceMapper;
        this.taxReferenceSearchRepository = taxReferenceSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TaxReferenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaxReferenceDTO> findByCriteria(TaxReferenceCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TaxReference> specification = createSpecification(criteria);
        return taxReferenceMapper.toDto(taxReferenceRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TaxReferenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaxReferenceDTO> findByCriteria(TaxReferenceCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaxReference> specification = createSpecification(criteria);
        return taxReferenceRepository.findAll(specification, page).map(taxReferenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaxReferenceCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TaxReference> specification = createSpecification(criteria);
        return taxReferenceRepository.count(specification);
    }

    /**
     * Function to convert {@link TaxReferenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaxReference> createSpecification(TaxReferenceCriteria criteria) {
        Specification<TaxReference> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TaxReference_.id));
            }
            if (criteria.getTaxName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxName(), TaxReference_.taxName));
            }
            if (criteria.getTaxDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxDescription(), TaxReference_.taxDescription));
            }
            if (criteria.getTaxPercentage() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTaxPercentage(), TaxReference_.taxPercentage));
            }
            if (criteria.getTaxReferenceType() != null) {
                specification = specification.and(buildSpecification(criteria.getTaxReferenceType(), TaxReference_.taxReferenceType));
            }
            if (criteria.getFileUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUploadToken(), TaxReference_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompilationToken(), TaxReference_.compilationToken));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(TaxReference_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
