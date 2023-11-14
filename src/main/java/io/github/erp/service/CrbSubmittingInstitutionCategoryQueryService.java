package io.github.erp.service;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.domain.CrbSubmittingInstitutionCategory;
import io.github.erp.repository.CrbSubmittingInstitutionCategoryRepository;
import io.github.erp.repository.search.CrbSubmittingInstitutionCategorySearchRepository;
import io.github.erp.service.criteria.CrbSubmittingInstitutionCategoryCriteria;
import io.github.erp.service.dto.CrbSubmittingInstitutionCategoryDTO;
import io.github.erp.service.mapper.CrbSubmittingInstitutionCategoryMapper;
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
 * Service for executing complex queries for {@link CrbSubmittingInstitutionCategory} entities in the database.
 * The main input is a {@link CrbSubmittingInstitutionCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbSubmittingInstitutionCategoryDTO} or a {@link Page} of {@link CrbSubmittingInstitutionCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbSubmittingInstitutionCategoryQueryService extends QueryService<CrbSubmittingInstitutionCategory> {

    private final Logger log = LoggerFactory.getLogger(CrbSubmittingInstitutionCategoryQueryService.class);

    private final CrbSubmittingInstitutionCategoryRepository crbSubmittingInstitutionCategoryRepository;

    private final CrbSubmittingInstitutionCategoryMapper crbSubmittingInstitutionCategoryMapper;

    private final CrbSubmittingInstitutionCategorySearchRepository crbSubmittingInstitutionCategorySearchRepository;

    public CrbSubmittingInstitutionCategoryQueryService(
        CrbSubmittingInstitutionCategoryRepository crbSubmittingInstitutionCategoryRepository,
        CrbSubmittingInstitutionCategoryMapper crbSubmittingInstitutionCategoryMapper,
        CrbSubmittingInstitutionCategorySearchRepository crbSubmittingInstitutionCategorySearchRepository
    ) {
        this.crbSubmittingInstitutionCategoryRepository = crbSubmittingInstitutionCategoryRepository;
        this.crbSubmittingInstitutionCategoryMapper = crbSubmittingInstitutionCategoryMapper;
        this.crbSubmittingInstitutionCategorySearchRepository = crbSubmittingInstitutionCategorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbSubmittingInstitutionCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbSubmittingInstitutionCategoryDTO> findByCriteria(CrbSubmittingInstitutionCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbSubmittingInstitutionCategory> specification = createSpecification(criteria);
        return crbSubmittingInstitutionCategoryMapper.toDto(crbSubmittingInstitutionCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbSubmittingInstitutionCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbSubmittingInstitutionCategoryDTO> findByCriteria(CrbSubmittingInstitutionCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbSubmittingInstitutionCategory> specification = createSpecification(criteria);
        return crbSubmittingInstitutionCategoryRepository.findAll(specification, page).map(crbSubmittingInstitutionCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbSubmittingInstitutionCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbSubmittingInstitutionCategory> specification = createSpecification(criteria);
        return crbSubmittingInstitutionCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbSubmittingInstitutionCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbSubmittingInstitutionCategory> createSpecification(CrbSubmittingInstitutionCategoryCriteria criteria) {
        Specification<CrbSubmittingInstitutionCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbSubmittingInstitutionCategory_.id));
            }
            if (criteria.getSubmittingInstitutionCategoryTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSubmittingInstitutionCategoryTypeCode(),
                            CrbSubmittingInstitutionCategory_.submittingInstitutionCategoryTypeCode
                        )
                    );
            }
            if (criteria.getSubmittingInstitutionCategoryType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSubmittingInstitutionCategoryType(),
                            CrbSubmittingInstitutionCategory_.submittingInstitutionCategoryType
                        )
                    );
            }
        }
        return specification;
    }
}
