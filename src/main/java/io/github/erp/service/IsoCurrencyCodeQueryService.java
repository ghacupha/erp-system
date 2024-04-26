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
import io.github.erp.domain.IsoCurrencyCode;
import io.github.erp.repository.IsoCurrencyCodeRepository;
import io.github.erp.repository.search.IsoCurrencyCodeSearchRepository;
import io.github.erp.service.criteria.IsoCurrencyCodeCriteria;
import io.github.erp.service.dto.IsoCurrencyCodeDTO;
import io.github.erp.service.mapper.IsoCurrencyCodeMapper;
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
 * Service for executing complex queries for {@link IsoCurrencyCode} entities in the database.
 * The main input is a {@link IsoCurrencyCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IsoCurrencyCodeDTO} or a {@link Page} of {@link IsoCurrencyCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IsoCurrencyCodeQueryService extends QueryService<IsoCurrencyCode> {

    private final Logger log = LoggerFactory.getLogger(IsoCurrencyCodeQueryService.class);

    private final IsoCurrencyCodeRepository isoCurrencyCodeRepository;

    private final IsoCurrencyCodeMapper isoCurrencyCodeMapper;

    private final IsoCurrencyCodeSearchRepository isoCurrencyCodeSearchRepository;

    public IsoCurrencyCodeQueryService(
        IsoCurrencyCodeRepository isoCurrencyCodeRepository,
        IsoCurrencyCodeMapper isoCurrencyCodeMapper,
        IsoCurrencyCodeSearchRepository isoCurrencyCodeSearchRepository
    ) {
        this.isoCurrencyCodeRepository = isoCurrencyCodeRepository;
        this.isoCurrencyCodeMapper = isoCurrencyCodeMapper;
        this.isoCurrencyCodeSearchRepository = isoCurrencyCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link IsoCurrencyCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IsoCurrencyCodeDTO> findByCriteria(IsoCurrencyCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IsoCurrencyCode> specification = createSpecification(criteria);
        return isoCurrencyCodeMapper.toDto(isoCurrencyCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IsoCurrencyCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IsoCurrencyCodeDTO> findByCriteria(IsoCurrencyCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IsoCurrencyCode> specification = createSpecification(criteria);
        return isoCurrencyCodeRepository.findAll(specification, page).map(isoCurrencyCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IsoCurrencyCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IsoCurrencyCode> specification = createSpecification(criteria);
        return isoCurrencyCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link IsoCurrencyCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IsoCurrencyCode> createSpecification(IsoCurrencyCodeCriteria criteria) {
        Specification<IsoCurrencyCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IsoCurrencyCode_.id));
            }
            if (criteria.getAlphabeticCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAlphabeticCode(), IsoCurrencyCode_.alphabeticCode));
            }
            if (criteria.getNumericCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumericCode(), IsoCurrencyCode_.numericCode));
            }
            if (criteria.getMinorUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMinorUnit(), IsoCurrencyCode_.minorUnit));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), IsoCurrencyCode_.currency));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), IsoCurrencyCode_.country));
            }
        }
        return specification;
    }
}
