package io.github.erp.service;

/*-
 * Erp System - Mark VI No 1 (Phoebe Series) Server ver 1.5.2
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
import io.github.erp.domain.BouncedChequeCategories;
import io.github.erp.repository.BouncedChequeCategoriesRepository;
import io.github.erp.repository.search.BouncedChequeCategoriesSearchRepository;
import io.github.erp.service.criteria.BouncedChequeCategoriesCriteria;
import io.github.erp.service.dto.BouncedChequeCategoriesDTO;
import io.github.erp.service.mapper.BouncedChequeCategoriesMapper;
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
 * Service for executing complex queries for {@link BouncedChequeCategories} entities in the database.
 * The main input is a {@link BouncedChequeCategoriesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link BouncedChequeCategoriesDTO} or a {@link Page} of {@link BouncedChequeCategoriesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class BouncedChequeCategoriesQueryService extends QueryService<BouncedChequeCategories> {

    private final Logger log = LoggerFactory.getLogger(BouncedChequeCategoriesQueryService.class);

    private final BouncedChequeCategoriesRepository bouncedChequeCategoriesRepository;

    private final BouncedChequeCategoriesMapper bouncedChequeCategoriesMapper;

    private final BouncedChequeCategoriesSearchRepository bouncedChequeCategoriesSearchRepository;

    public BouncedChequeCategoriesQueryService(
        BouncedChequeCategoriesRepository bouncedChequeCategoriesRepository,
        BouncedChequeCategoriesMapper bouncedChequeCategoriesMapper,
        BouncedChequeCategoriesSearchRepository bouncedChequeCategoriesSearchRepository
    ) {
        this.bouncedChequeCategoriesRepository = bouncedChequeCategoriesRepository;
        this.bouncedChequeCategoriesMapper = bouncedChequeCategoriesMapper;
        this.bouncedChequeCategoriesSearchRepository = bouncedChequeCategoriesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link BouncedChequeCategoriesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<BouncedChequeCategoriesDTO> findByCriteria(BouncedChequeCategoriesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<BouncedChequeCategories> specification = createSpecification(criteria);
        return bouncedChequeCategoriesMapper.toDto(bouncedChequeCategoriesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link BouncedChequeCategoriesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<BouncedChequeCategoriesDTO> findByCriteria(BouncedChequeCategoriesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<BouncedChequeCategories> specification = createSpecification(criteria);
        return bouncedChequeCategoriesRepository.findAll(specification, page).map(bouncedChequeCategoriesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(BouncedChequeCategoriesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<BouncedChequeCategories> specification = createSpecification(criteria);
        return bouncedChequeCategoriesRepository.count(specification);
    }

    /**
     * Function to convert {@link BouncedChequeCategoriesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<BouncedChequeCategories> createSpecification(BouncedChequeCategoriesCriteria criteria) {
        Specification<BouncedChequeCategories> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), BouncedChequeCategories_.id));
            }
            if (criteria.getBouncedChequeCategoryTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getBouncedChequeCategoryTypeCode(),
                            BouncedChequeCategories_.bouncedChequeCategoryTypeCode
                        )
                    );
            }
            if (criteria.getBouncedChequeCategoryType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getBouncedChequeCategoryType(),
                            BouncedChequeCategories_.bouncedChequeCategoryType
                        )
                    );
            }
        }
        return specification;
    }
}
