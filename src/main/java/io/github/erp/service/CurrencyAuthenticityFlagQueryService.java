package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.CurrencyAuthenticityFlag;
import io.github.erp.repository.CurrencyAuthenticityFlagRepository;
import io.github.erp.repository.search.CurrencyAuthenticityFlagSearchRepository;
import io.github.erp.service.criteria.CurrencyAuthenticityFlagCriteria;
import io.github.erp.service.dto.CurrencyAuthenticityFlagDTO;
import io.github.erp.service.mapper.CurrencyAuthenticityFlagMapper;
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
 * Service for executing complex queries for {@link CurrencyAuthenticityFlag} entities in the database.
 * The main input is a {@link CurrencyAuthenticityFlagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CurrencyAuthenticityFlagDTO} or a {@link Page} of {@link CurrencyAuthenticityFlagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CurrencyAuthenticityFlagQueryService extends QueryService<CurrencyAuthenticityFlag> {

    private final Logger log = LoggerFactory.getLogger(CurrencyAuthenticityFlagQueryService.class);

    private final CurrencyAuthenticityFlagRepository currencyAuthenticityFlagRepository;

    private final CurrencyAuthenticityFlagMapper currencyAuthenticityFlagMapper;

    private final CurrencyAuthenticityFlagSearchRepository currencyAuthenticityFlagSearchRepository;

    public CurrencyAuthenticityFlagQueryService(
        CurrencyAuthenticityFlagRepository currencyAuthenticityFlagRepository,
        CurrencyAuthenticityFlagMapper currencyAuthenticityFlagMapper,
        CurrencyAuthenticityFlagSearchRepository currencyAuthenticityFlagSearchRepository
    ) {
        this.currencyAuthenticityFlagRepository = currencyAuthenticityFlagRepository;
        this.currencyAuthenticityFlagMapper = currencyAuthenticityFlagMapper;
        this.currencyAuthenticityFlagSearchRepository = currencyAuthenticityFlagSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CurrencyAuthenticityFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CurrencyAuthenticityFlagDTO> findByCriteria(CurrencyAuthenticityFlagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CurrencyAuthenticityFlag> specification = createSpecification(criteria);
        return currencyAuthenticityFlagMapper.toDto(currencyAuthenticityFlagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CurrencyAuthenticityFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CurrencyAuthenticityFlagDTO> findByCriteria(CurrencyAuthenticityFlagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CurrencyAuthenticityFlag> specification = createSpecification(criteria);
        return currencyAuthenticityFlagRepository.findAll(specification, page).map(currencyAuthenticityFlagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CurrencyAuthenticityFlagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CurrencyAuthenticityFlag> specification = createSpecification(criteria);
        return currencyAuthenticityFlagRepository.count(specification);
    }

    /**
     * Function to convert {@link CurrencyAuthenticityFlagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CurrencyAuthenticityFlag> createSpecification(CurrencyAuthenticityFlagCriteria criteria) {
        Specification<CurrencyAuthenticityFlag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CurrencyAuthenticityFlag_.id));
            }
            if (criteria.getCurrencyAuthenticityFlag() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCurrencyAuthenticityFlag(), CurrencyAuthenticityFlag_.currencyAuthenticityFlag)
                    );
            }
            if (criteria.getCurrencyAuthenticityType() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCurrencyAuthenticityType(), CurrencyAuthenticityFlag_.currencyAuthenticityType)
                    );
            }
        }
        return specification;
    }
}
