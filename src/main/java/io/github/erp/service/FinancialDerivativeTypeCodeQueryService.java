package io.github.erp.service;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.FinancialDerivativeTypeCode;
import io.github.erp.repository.FinancialDerivativeTypeCodeRepository;
import io.github.erp.repository.search.FinancialDerivativeTypeCodeSearchRepository;
import io.github.erp.service.criteria.FinancialDerivativeTypeCodeCriteria;
import io.github.erp.service.dto.FinancialDerivativeTypeCodeDTO;
import io.github.erp.service.mapper.FinancialDerivativeTypeCodeMapper;
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
 * Service for executing complex queries for {@link FinancialDerivativeTypeCode} entities in the database.
 * The main input is a {@link FinancialDerivativeTypeCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FinancialDerivativeTypeCodeDTO} or a {@link Page} of {@link FinancialDerivativeTypeCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FinancialDerivativeTypeCodeQueryService extends QueryService<FinancialDerivativeTypeCode> {

    private final Logger log = LoggerFactory.getLogger(FinancialDerivativeTypeCodeQueryService.class);

    private final FinancialDerivativeTypeCodeRepository financialDerivativeTypeCodeRepository;

    private final FinancialDerivativeTypeCodeMapper financialDerivativeTypeCodeMapper;

    private final FinancialDerivativeTypeCodeSearchRepository financialDerivativeTypeCodeSearchRepository;

    public FinancialDerivativeTypeCodeQueryService(
        FinancialDerivativeTypeCodeRepository financialDerivativeTypeCodeRepository,
        FinancialDerivativeTypeCodeMapper financialDerivativeTypeCodeMapper,
        FinancialDerivativeTypeCodeSearchRepository financialDerivativeTypeCodeSearchRepository
    ) {
        this.financialDerivativeTypeCodeRepository = financialDerivativeTypeCodeRepository;
        this.financialDerivativeTypeCodeMapper = financialDerivativeTypeCodeMapper;
        this.financialDerivativeTypeCodeSearchRepository = financialDerivativeTypeCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FinancialDerivativeTypeCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FinancialDerivativeTypeCodeDTO> findByCriteria(FinancialDerivativeTypeCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FinancialDerivativeTypeCode> specification = createSpecification(criteria);
        return financialDerivativeTypeCodeMapper.toDto(financialDerivativeTypeCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FinancialDerivativeTypeCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FinancialDerivativeTypeCodeDTO> findByCriteria(FinancialDerivativeTypeCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FinancialDerivativeTypeCode> specification = createSpecification(criteria);
        return financialDerivativeTypeCodeRepository.findAll(specification, page).map(financialDerivativeTypeCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FinancialDerivativeTypeCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FinancialDerivativeTypeCode> specification = createSpecification(criteria);
        return financialDerivativeTypeCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link FinancialDerivativeTypeCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FinancialDerivativeTypeCode> createSpecification(FinancialDerivativeTypeCodeCriteria criteria) {
        Specification<FinancialDerivativeTypeCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FinancialDerivativeTypeCode_.id));
            }
            if (criteria.getFinancialDerivativeTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getFinancialDerivativeTypeCode(),
                            FinancialDerivativeTypeCode_.financialDerivativeTypeCode
                        )
                    );
            }
            if (criteria.getFinancialDerivativeType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getFinancialDerivativeType(),
                            FinancialDerivativeTypeCode_.financialDerivativeType
                        )
                    );
            }
        }
        return specification;
    }
}
