package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.WeeklyCashHolding;
import io.github.erp.repository.WeeklyCashHoldingRepository;
import io.github.erp.repository.search.WeeklyCashHoldingSearchRepository;
import io.github.erp.service.criteria.WeeklyCashHoldingCriteria;
import io.github.erp.service.dto.WeeklyCashHoldingDTO;
import io.github.erp.service.mapper.WeeklyCashHoldingMapper;
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
 * Service for executing complex queries for {@link WeeklyCashHolding} entities in the database.
 * The main input is a {@link WeeklyCashHoldingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WeeklyCashHoldingDTO} or a {@link Page} of {@link WeeklyCashHoldingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WeeklyCashHoldingQueryService extends QueryService<WeeklyCashHolding> {

    private final Logger log = LoggerFactory.getLogger(WeeklyCashHoldingQueryService.class);

    private final WeeklyCashHoldingRepository weeklyCashHoldingRepository;

    private final WeeklyCashHoldingMapper weeklyCashHoldingMapper;

    private final WeeklyCashHoldingSearchRepository weeklyCashHoldingSearchRepository;

    public WeeklyCashHoldingQueryService(
        WeeklyCashHoldingRepository weeklyCashHoldingRepository,
        WeeklyCashHoldingMapper weeklyCashHoldingMapper,
        WeeklyCashHoldingSearchRepository weeklyCashHoldingSearchRepository
    ) {
        this.weeklyCashHoldingRepository = weeklyCashHoldingRepository;
        this.weeklyCashHoldingMapper = weeklyCashHoldingMapper;
        this.weeklyCashHoldingSearchRepository = weeklyCashHoldingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WeeklyCashHoldingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WeeklyCashHoldingDTO> findByCriteria(WeeklyCashHoldingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WeeklyCashHolding> specification = createSpecification(criteria);
        return weeklyCashHoldingMapper.toDto(weeklyCashHoldingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WeeklyCashHoldingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WeeklyCashHoldingDTO> findByCriteria(WeeklyCashHoldingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WeeklyCashHolding> specification = createSpecification(criteria);
        return weeklyCashHoldingRepository.findAll(specification, page).map(weeklyCashHoldingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WeeklyCashHoldingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WeeklyCashHolding> specification = createSpecification(criteria);
        return weeklyCashHoldingRepository.count(specification);
    }

    /**
     * Function to convert {@link WeeklyCashHoldingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WeeklyCashHolding> createSpecification(WeeklyCashHoldingCriteria criteria) {
        Specification<WeeklyCashHolding> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WeeklyCashHolding_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportingDate(), WeeklyCashHolding_.reportingDate));
            }
            if (criteria.getFitUnits() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFitUnits(), WeeklyCashHolding_.fitUnits));
            }
            if (criteria.getUnfitUnits() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnfitUnits(), WeeklyCashHolding_.unfitUnits));
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(WeeklyCashHolding_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getBranchIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchIdId(),
                            root -> root.join(WeeklyCashHolding_.branchId, JoinType.LEFT).get(BankBranchCode_.id)
                        )
                    );
            }
            if (criteria.getSubCountyCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubCountyCodeId(),
                            root -> root.join(WeeklyCashHolding_.subCountyCode, JoinType.LEFT).get(CountySubCountyCode_.id)
                        )
                    );
            }
            if (criteria.getDenominationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDenominationId(),
                            root -> root.join(WeeklyCashHolding_.denomination, JoinType.LEFT).get(KenyanCurrencyDenomination_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
