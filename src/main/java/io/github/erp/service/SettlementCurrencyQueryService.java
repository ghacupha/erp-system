package io.github.erp.service;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.2
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
import io.github.erp.domain.SettlementCurrency;
import io.github.erp.repository.SettlementCurrencyRepository;
import io.github.erp.repository.search.SettlementCurrencySearchRepository;
import io.github.erp.service.criteria.SettlementCurrencyCriteria;
import io.github.erp.service.dto.SettlementCurrencyDTO;
import io.github.erp.service.mapper.SettlementCurrencyMapper;
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
 * Service for executing complex queries for {@link SettlementCurrency} entities in the database.
 * The main input is a {@link SettlementCurrencyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SettlementCurrencyDTO} or a {@link Page} of {@link SettlementCurrencyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SettlementCurrencyQueryService extends QueryService<SettlementCurrency> {

    private final Logger log = LoggerFactory.getLogger(SettlementCurrencyQueryService.class);

    private final SettlementCurrencyRepository settlementCurrencyRepository;

    private final SettlementCurrencyMapper settlementCurrencyMapper;

    private final SettlementCurrencySearchRepository settlementCurrencySearchRepository;

    public SettlementCurrencyQueryService(
        SettlementCurrencyRepository settlementCurrencyRepository,
        SettlementCurrencyMapper settlementCurrencyMapper,
        SettlementCurrencySearchRepository settlementCurrencySearchRepository
    ) {
        this.settlementCurrencyRepository = settlementCurrencyRepository;
        this.settlementCurrencyMapper = settlementCurrencyMapper;
        this.settlementCurrencySearchRepository = settlementCurrencySearchRepository;
    }

    /**
     * Return a {@link List} of {@link SettlementCurrencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SettlementCurrencyDTO> findByCriteria(SettlementCurrencyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SettlementCurrency> specification = createSpecification(criteria);
        return settlementCurrencyMapper.toDto(settlementCurrencyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SettlementCurrencyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SettlementCurrencyDTO> findByCriteria(SettlementCurrencyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SettlementCurrency> specification = createSpecification(criteria);
        return settlementCurrencyRepository.findAll(specification, page).map(settlementCurrencyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SettlementCurrencyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SettlementCurrency> specification = createSpecification(criteria);
        return settlementCurrencyRepository.count(specification);
    }

    /**
     * Function to convert {@link SettlementCurrencyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SettlementCurrency> createSpecification(SettlementCurrencyCriteria criteria) {
        Specification<SettlementCurrency> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SettlementCurrency_.id));
            }
            if (criteria.getIso4217CurrencyCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getIso4217CurrencyCode(), SettlementCurrency_.iso4217CurrencyCode));
            }
            if (criteria.getCurrencyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrencyName(), SettlementCurrency_.currencyName));
            }
            if (criteria.getCountry() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountry(), SettlementCurrency_.country));
            }
            if (criteria.getNumericCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNumericCode(), SettlementCurrency_.numericCode));
            }
            if (criteria.getMinorUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMinorUnit(), SettlementCurrency_.minorUnit));
            }
            if (criteria.getFileUploadToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileUploadToken(), SettlementCurrency_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCompilationToken(), SettlementCurrency_.compilationToken));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(SettlementCurrency_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
