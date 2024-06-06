package io.github.erp.service;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.InsiderCategoryTypes;
import io.github.erp.repository.InsiderCategoryTypesRepository;
import io.github.erp.repository.search.InsiderCategoryTypesSearchRepository;
import io.github.erp.service.criteria.InsiderCategoryTypesCriteria;
import io.github.erp.service.dto.InsiderCategoryTypesDTO;
import io.github.erp.service.mapper.InsiderCategoryTypesMapper;
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
 * Service for executing complex queries for {@link InsiderCategoryTypes} entities in the database.
 * The main input is a {@link InsiderCategoryTypesCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InsiderCategoryTypesDTO} or a {@link Page} of {@link InsiderCategoryTypesDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InsiderCategoryTypesQueryService extends QueryService<InsiderCategoryTypes> {

    private final Logger log = LoggerFactory.getLogger(InsiderCategoryTypesQueryService.class);

    private final InsiderCategoryTypesRepository insiderCategoryTypesRepository;

    private final InsiderCategoryTypesMapper insiderCategoryTypesMapper;

    private final InsiderCategoryTypesSearchRepository insiderCategoryTypesSearchRepository;

    public InsiderCategoryTypesQueryService(
        InsiderCategoryTypesRepository insiderCategoryTypesRepository,
        InsiderCategoryTypesMapper insiderCategoryTypesMapper,
        InsiderCategoryTypesSearchRepository insiderCategoryTypesSearchRepository
    ) {
        this.insiderCategoryTypesRepository = insiderCategoryTypesRepository;
        this.insiderCategoryTypesMapper = insiderCategoryTypesMapper;
        this.insiderCategoryTypesSearchRepository = insiderCategoryTypesSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InsiderCategoryTypesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InsiderCategoryTypesDTO> findByCriteria(InsiderCategoryTypesCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InsiderCategoryTypes> specification = createSpecification(criteria);
        return insiderCategoryTypesMapper.toDto(insiderCategoryTypesRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InsiderCategoryTypesDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InsiderCategoryTypesDTO> findByCriteria(InsiderCategoryTypesCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InsiderCategoryTypes> specification = createSpecification(criteria);
        return insiderCategoryTypesRepository.findAll(specification, page).map(insiderCategoryTypesMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InsiderCategoryTypesCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InsiderCategoryTypes> specification = createSpecification(criteria);
        return insiderCategoryTypesRepository.count(specification);
    }

    /**
     * Function to convert {@link InsiderCategoryTypesCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InsiderCategoryTypes> createSpecification(InsiderCategoryTypesCriteria criteria) {
        Specification<InsiderCategoryTypes> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InsiderCategoryTypes_.id));
            }
            if (criteria.getInsiderCategoryTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getInsiderCategoryTypeCode(), InsiderCategoryTypes_.insiderCategoryTypeCode)
                    );
            }
            if (criteria.getInsiderCategoryTypeDetail() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getInsiderCategoryTypeDetail(), InsiderCategoryTypes_.insiderCategoryTypeDetail)
                    );
            }
        }
        return specification;
    }
}
