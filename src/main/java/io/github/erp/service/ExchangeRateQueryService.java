package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 3 (Hilkiah Series) Server ver 1.6.2
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.ExchangeRate;
import io.github.erp.repository.ExchangeRateRepository;
import io.github.erp.repository.search.ExchangeRateSearchRepository;
import io.github.erp.service.criteria.ExchangeRateCriteria;
import io.github.erp.service.dto.ExchangeRateDTO;
import io.github.erp.service.mapper.ExchangeRateMapper;
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
 * Service for executing complex queries for {@link ExchangeRate} entities in the database.
 * The main input is a {@link ExchangeRateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ExchangeRateDTO} or a {@link Page} of {@link ExchangeRateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ExchangeRateQueryService extends QueryService<ExchangeRate> {

    private final Logger log = LoggerFactory.getLogger(ExchangeRateQueryService.class);

    private final ExchangeRateRepository exchangeRateRepository;

    private final ExchangeRateMapper exchangeRateMapper;

    private final ExchangeRateSearchRepository exchangeRateSearchRepository;

    public ExchangeRateQueryService(
        ExchangeRateRepository exchangeRateRepository,
        ExchangeRateMapper exchangeRateMapper,
        ExchangeRateSearchRepository exchangeRateSearchRepository
    ) {
        this.exchangeRateRepository = exchangeRateRepository;
        this.exchangeRateMapper = exchangeRateMapper;
        this.exchangeRateSearchRepository = exchangeRateSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ExchangeRateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ExchangeRateDTO> findByCriteria(ExchangeRateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ExchangeRate> specification = createSpecification(criteria);
        return exchangeRateMapper.toDto(exchangeRateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ExchangeRateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ExchangeRateDTO> findByCriteria(ExchangeRateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ExchangeRate> specification = createSpecification(criteria);
        return exchangeRateRepository.findAll(specification, page).map(exchangeRateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ExchangeRateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ExchangeRate> specification = createSpecification(criteria);
        return exchangeRateRepository.count(specification);
    }

    /**
     * Function to convert {@link ExchangeRateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ExchangeRate> createSpecification(ExchangeRateCriteria criteria) {
        Specification<ExchangeRate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ExchangeRate_.id));
            }
            if (criteria.getBusinessReportingDay() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getBusinessReportingDay(), ExchangeRate_.businessReportingDay));
            }
            if (criteria.getBuyingRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBuyingRate(), ExchangeRate_.buyingRate));
            }
            if (criteria.getSellingRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSellingRate(), ExchangeRate_.sellingRate));
            }
            if (criteria.getMeanRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getMeanRate(), ExchangeRate_.meanRate));
            }
            if (criteria.getClosingBidRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClosingBidRate(), ExchangeRate_.closingBidRate));
            }
            if (criteria.getClosingOfferRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getClosingOfferRate(), ExchangeRate_.closingOfferRate));
            }
            if (criteria.getUsdCrossRate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsdCrossRate(), ExchangeRate_.usdCrossRate));
            }
            if (criteria.getInstitutionCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInstitutionCodeId(),
                            root -> root.join(ExchangeRate_.institutionCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getCurrencyCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCurrencyCodeId(),
                            root -> root.join(ExchangeRate_.currencyCode, JoinType.LEFT).get(IsoCurrencyCode_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
