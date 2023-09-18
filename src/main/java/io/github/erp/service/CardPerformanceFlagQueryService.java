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
import io.github.erp.domain.CardPerformanceFlag;
import io.github.erp.repository.CardPerformanceFlagRepository;
import io.github.erp.repository.search.CardPerformanceFlagSearchRepository;
import io.github.erp.service.criteria.CardPerformanceFlagCriteria;
import io.github.erp.service.dto.CardPerformanceFlagDTO;
import io.github.erp.service.mapper.CardPerformanceFlagMapper;
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
 * Service for executing complex queries for {@link CardPerformanceFlag} entities in the database.
 * The main input is a {@link CardPerformanceFlagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CardPerformanceFlagDTO} or a {@link Page} of {@link CardPerformanceFlagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CardPerformanceFlagQueryService extends QueryService<CardPerformanceFlag> {

    private final Logger log = LoggerFactory.getLogger(CardPerformanceFlagQueryService.class);

    private final CardPerformanceFlagRepository cardPerformanceFlagRepository;

    private final CardPerformanceFlagMapper cardPerformanceFlagMapper;

    private final CardPerformanceFlagSearchRepository cardPerformanceFlagSearchRepository;

    public CardPerformanceFlagQueryService(
        CardPerformanceFlagRepository cardPerformanceFlagRepository,
        CardPerformanceFlagMapper cardPerformanceFlagMapper,
        CardPerformanceFlagSearchRepository cardPerformanceFlagSearchRepository
    ) {
        this.cardPerformanceFlagRepository = cardPerformanceFlagRepository;
        this.cardPerformanceFlagMapper = cardPerformanceFlagMapper;
        this.cardPerformanceFlagSearchRepository = cardPerformanceFlagSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CardPerformanceFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CardPerformanceFlagDTO> findByCriteria(CardPerformanceFlagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CardPerformanceFlag> specification = createSpecification(criteria);
        return cardPerformanceFlagMapper.toDto(cardPerformanceFlagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CardPerformanceFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CardPerformanceFlagDTO> findByCriteria(CardPerformanceFlagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CardPerformanceFlag> specification = createSpecification(criteria);
        return cardPerformanceFlagRepository.findAll(specification, page).map(cardPerformanceFlagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CardPerformanceFlagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CardPerformanceFlag> specification = createSpecification(criteria);
        return cardPerformanceFlagRepository.count(specification);
    }

    /**
     * Function to convert {@link CardPerformanceFlagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CardPerformanceFlag> createSpecification(CardPerformanceFlagCriteria criteria) {
        Specification<CardPerformanceFlag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CardPerformanceFlag_.id));
            }
            if (criteria.getCardPerformanceFlag() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getCardPerformanceFlag(), CardPerformanceFlag_.cardPerformanceFlag));
            }
            if (criteria.getCardPerformanceFlagDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCardPerformanceFlagDescription(),
                            CardPerformanceFlag_.cardPerformanceFlagDescription
                        )
                    );
            }
        }
        return specification;
    }
}
