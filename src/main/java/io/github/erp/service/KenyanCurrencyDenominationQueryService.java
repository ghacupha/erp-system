package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 1 (Hilkiah Series) Server ver 1.6.0
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
import io.github.erp.domain.KenyanCurrencyDenomination;
import io.github.erp.repository.KenyanCurrencyDenominationRepository;
import io.github.erp.repository.search.KenyanCurrencyDenominationSearchRepository;
import io.github.erp.service.criteria.KenyanCurrencyDenominationCriteria;
import io.github.erp.service.dto.KenyanCurrencyDenominationDTO;
import io.github.erp.service.mapper.KenyanCurrencyDenominationMapper;
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
 * Service for executing complex queries for {@link KenyanCurrencyDenomination} entities in the database.
 * The main input is a {@link KenyanCurrencyDenominationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link KenyanCurrencyDenominationDTO} or a {@link Page} of {@link KenyanCurrencyDenominationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class KenyanCurrencyDenominationQueryService extends QueryService<KenyanCurrencyDenomination> {

    private final Logger log = LoggerFactory.getLogger(KenyanCurrencyDenominationQueryService.class);

    private final KenyanCurrencyDenominationRepository kenyanCurrencyDenominationRepository;

    private final KenyanCurrencyDenominationMapper kenyanCurrencyDenominationMapper;

    private final KenyanCurrencyDenominationSearchRepository kenyanCurrencyDenominationSearchRepository;

    public KenyanCurrencyDenominationQueryService(
        KenyanCurrencyDenominationRepository kenyanCurrencyDenominationRepository,
        KenyanCurrencyDenominationMapper kenyanCurrencyDenominationMapper,
        KenyanCurrencyDenominationSearchRepository kenyanCurrencyDenominationSearchRepository
    ) {
        this.kenyanCurrencyDenominationRepository = kenyanCurrencyDenominationRepository;
        this.kenyanCurrencyDenominationMapper = kenyanCurrencyDenominationMapper;
        this.kenyanCurrencyDenominationSearchRepository = kenyanCurrencyDenominationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link KenyanCurrencyDenominationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<KenyanCurrencyDenominationDTO> findByCriteria(KenyanCurrencyDenominationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<KenyanCurrencyDenomination> specification = createSpecification(criteria);
        return kenyanCurrencyDenominationMapper.toDto(kenyanCurrencyDenominationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link KenyanCurrencyDenominationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<KenyanCurrencyDenominationDTO> findByCriteria(KenyanCurrencyDenominationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<KenyanCurrencyDenomination> specification = createSpecification(criteria);
        return kenyanCurrencyDenominationRepository.findAll(specification, page).map(kenyanCurrencyDenominationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(KenyanCurrencyDenominationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<KenyanCurrencyDenomination> specification = createSpecification(criteria);
        return kenyanCurrencyDenominationRepository.count(specification);
    }

    /**
     * Function to convert {@link KenyanCurrencyDenominationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<KenyanCurrencyDenomination> createSpecification(KenyanCurrencyDenominationCriteria criteria) {
        Specification<KenyanCurrencyDenomination> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), KenyanCurrencyDenomination_.id));
            }
            if (criteria.getCurrencyDenominationCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCurrencyDenominationCode(),
                            KenyanCurrencyDenomination_.currencyDenominationCode
                        )
                    );
            }
            if (criteria.getCurrencyDenominationType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCurrencyDenominationType(),
                            KenyanCurrencyDenomination_.currencyDenominationType
                        )
                    );
            }
            if (criteria.getCurrencyDenominationTypeDetails() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCurrencyDenominationTypeDetails(),
                            KenyanCurrencyDenomination_.currencyDenominationTypeDetails
                        )
                    );
            }
        }
        return specification;
    }
}
