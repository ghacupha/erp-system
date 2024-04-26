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
import io.github.erp.domain.FxReceiptPurposeType;
import io.github.erp.repository.FxReceiptPurposeTypeRepository;
import io.github.erp.repository.search.FxReceiptPurposeTypeSearchRepository;
import io.github.erp.service.criteria.FxReceiptPurposeTypeCriteria;
import io.github.erp.service.dto.FxReceiptPurposeTypeDTO;
import io.github.erp.service.mapper.FxReceiptPurposeTypeMapper;
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
 * Service for executing complex queries for {@link FxReceiptPurposeType} entities in the database.
 * The main input is a {@link FxReceiptPurposeTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FxReceiptPurposeTypeDTO} or a {@link Page} of {@link FxReceiptPurposeTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FxReceiptPurposeTypeQueryService extends QueryService<FxReceiptPurposeType> {

    private final Logger log = LoggerFactory.getLogger(FxReceiptPurposeTypeQueryService.class);

    private final FxReceiptPurposeTypeRepository fxReceiptPurposeTypeRepository;

    private final FxReceiptPurposeTypeMapper fxReceiptPurposeTypeMapper;

    private final FxReceiptPurposeTypeSearchRepository fxReceiptPurposeTypeSearchRepository;

    public FxReceiptPurposeTypeQueryService(
        FxReceiptPurposeTypeRepository fxReceiptPurposeTypeRepository,
        FxReceiptPurposeTypeMapper fxReceiptPurposeTypeMapper,
        FxReceiptPurposeTypeSearchRepository fxReceiptPurposeTypeSearchRepository
    ) {
        this.fxReceiptPurposeTypeRepository = fxReceiptPurposeTypeRepository;
        this.fxReceiptPurposeTypeMapper = fxReceiptPurposeTypeMapper;
        this.fxReceiptPurposeTypeSearchRepository = fxReceiptPurposeTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FxReceiptPurposeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FxReceiptPurposeTypeDTO> findByCriteria(FxReceiptPurposeTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FxReceiptPurposeType> specification = createSpecification(criteria);
        return fxReceiptPurposeTypeMapper.toDto(fxReceiptPurposeTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FxReceiptPurposeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FxReceiptPurposeTypeDTO> findByCriteria(FxReceiptPurposeTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FxReceiptPurposeType> specification = createSpecification(criteria);
        return fxReceiptPurposeTypeRepository.findAll(specification, page).map(fxReceiptPurposeTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FxReceiptPurposeTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FxReceiptPurposeType> specification = createSpecification(criteria);
        return fxReceiptPurposeTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link FxReceiptPurposeTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FxReceiptPurposeType> createSpecification(FxReceiptPurposeTypeCriteria criteria) {
        Specification<FxReceiptPurposeType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FxReceiptPurposeType_.id));
            }
            if (criteria.getItemCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getItemCode(), FxReceiptPurposeType_.itemCode));
            }
            if (criteria.getAttribute1ReceiptPaymentPurposeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAttribute1ReceiptPaymentPurposeCode(),
                            FxReceiptPurposeType_.attribute1ReceiptPaymentPurposeCode
                        )
                    );
            }
            if (criteria.getAttribute1ReceiptPaymentPurposeType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAttribute1ReceiptPaymentPurposeType(),
                            FxReceiptPurposeType_.attribute1ReceiptPaymentPurposeType
                        )
                    );
            }
            if (criteria.getAttribute2ReceiptPaymentPurposeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAttribute2ReceiptPaymentPurposeCode(),
                            FxReceiptPurposeType_.attribute2ReceiptPaymentPurposeCode
                        )
                    );
            }
            if (criteria.getAttribute2ReceiptPaymentPurposeDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAttribute2ReceiptPaymentPurposeDescription(),
                            FxReceiptPurposeType_.attribute2ReceiptPaymentPurposeDescription
                        )
                    );
            }
            if (criteria.getAttribute3ReceiptPaymentPurposeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAttribute3ReceiptPaymentPurposeCode(),
                            FxReceiptPurposeType_.attribute3ReceiptPaymentPurposeCode
                        )
                    );
            }
            if (criteria.getAttribute3ReceiptPaymentPurposeDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAttribute3ReceiptPaymentPurposeDescription(),
                            FxReceiptPurposeType_.attribute3ReceiptPaymentPurposeDescription
                        )
                    );
            }
            if (criteria.getAttribute4ReceiptPaymentPurposeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAttribute4ReceiptPaymentPurposeCode(),
                            FxReceiptPurposeType_.attribute4ReceiptPaymentPurposeCode
                        )
                    );
            }
            if (criteria.getAttribute4ReceiptPaymentPurposeDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAttribute4ReceiptPaymentPurposeDescription(),
                            FxReceiptPurposeType_.attribute4ReceiptPaymentPurposeDescription
                        )
                    );
            }
            if (criteria.getAttribute5ReceiptPaymentPurposeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAttribute5ReceiptPaymentPurposeCode(),
                            FxReceiptPurposeType_.attribute5ReceiptPaymentPurposeCode
                        )
                    );
            }
            if (criteria.getAttribute5ReceiptPaymentPurposeDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAttribute5ReceiptPaymentPurposeDescription(),
                            FxReceiptPurposeType_.attribute5ReceiptPaymentPurposeDescription
                        )
                    );
            }
            if (criteria.getLastChild() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastChild(), FxReceiptPurposeType_.lastChild));
            }
        }
        return specification;
    }
}
