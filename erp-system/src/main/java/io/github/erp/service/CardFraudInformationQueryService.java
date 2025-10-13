package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.CardFraudInformation;
import io.github.erp.repository.CardFraudInformationRepository;
import io.github.erp.repository.search.CardFraudInformationSearchRepository;
import io.github.erp.service.criteria.CardFraudInformationCriteria;
import io.github.erp.service.dto.CardFraudInformationDTO;
import io.github.erp.service.mapper.CardFraudInformationMapper;
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
 * Service for executing complex queries for {@link CardFraudInformation} entities in the database.
 * The main input is a {@link CardFraudInformationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardFraudInformationDTO} or a {@link Page} of {@link CardFraudInformationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardFraudInformationQueryService extends QueryService<CardFraudInformation> {

    private final Logger log = LoggerFactory.getLogger(CardFraudInformationQueryService.class);

    private final CardFraudInformationRepository cardFraudInformationRepository;

    private final CardFraudInformationMapper cardFraudInformationMapper;

    private final CardFraudInformationSearchRepository cardFraudInformationSearchRepository;

    public CardFraudInformationQueryService(
        CardFraudInformationRepository cardFraudInformationRepository,
        CardFraudInformationMapper cardFraudInformationMapper,
        CardFraudInformationSearchRepository cardFraudInformationSearchRepository
    ) {
        this.cardFraudInformationRepository = cardFraudInformationRepository;
        this.cardFraudInformationMapper = cardFraudInformationMapper;
        this.cardFraudInformationSearchRepository = cardFraudInformationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardFraudInformationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardFraudInformationDTO> findByCriteria(CardFraudInformationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardFraudInformation> specification = createSpecification(criteria);
        return cardFraudInformationMapper.toDto(cardFraudInformationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardFraudInformationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardFraudInformationDTO> findByCriteria(CardFraudInformationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardFraudInformation> specification = createSpecification(criteria);
        return cardFraudInformationRepository.findAll(specification, page).map(cardFraudInformationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardFraudInformationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardFraudInformation> specification = createSpecification(criteria);
        return cardFraudInformationRepository.count(specification);
    }

    /**
     * Function to convert {@link CardFraudInformationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardFraudInformation> createSpecification(CardFraudInformationCriteria criteria) {
        Specification<CardFraudInformation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardFraudInformation_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportingDate(), CardFraudInformation_.reportingDate));
            }
            if (criteria.getTotalNumberOfFraudIncidents() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(
                            criteria.getTotalNumberOfFraudIncidents(),
                            CardFraudInformation_.totalNumberOfFraudIncidents
                        )
                    );
            }
            if (criteria.getValueOfFraudIncedentsInLCY() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getValueOfFraudIncedentsInLCY(), CardFraudInformation_.valueOfFraudIncedentsInLCY)
                    );
            }
        }
        return specification;
    }
}
