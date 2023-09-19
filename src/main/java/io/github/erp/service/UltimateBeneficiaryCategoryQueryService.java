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
import io.github.erp.domain.UltimateBeneficiaryCategory;
import io.github.erp.repository.UltimateBeneficiaryCategoryRepository;
import io.github.erp.repository.search.UltimateBeneficiaryCategorySearchRepository;
import io.github.erp.service.criteria.UltimateBeneficiaryCategoryCriteria;
import io.github.erp.service.dto.UltimateBeneficiaryCategoryDTO;
import io.github.erp.service.mapper.UltimateBeneficiaryCategoryMapper;
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
 * Service for executing complex queries for {@link UltimateBeneficiaryCategory} entities in the database.
 * The main input is a {@link UltimateBeneficiaryCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link UltimateBeneficiaryCategoryDTO} or a {@link Page} of {@link UltimateBeneficiaryCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UltimateBeneficiaryCategoryQueryService extends QueryService<UltimateBeneficiaryCategory> {

    private final Logger log = LoggerFactory.getLogger(UltimateBeneficiaryCategoryQueryService.class);

    private final UltimateBeneficiaryCategoryRepository ultimateBeneficiaryCategoryRepository;

    private final UltimateBeneficiaryCategoryMapper ultimateBeneficiaryCategoryMapper;

    private final UltimateBeneficiaryCategorySearchRepository ultimateBeneficiaryCategorySearchRepository;

    public UltimateBeneficiaryCategoryQueryService(
        UltimateBeneficiaryCategoryRepository ultimateBeneficiaryCategoryRepository,
        UltimateBeneficiaryCategoryMapper ultimateBeneficiaryCategoryMapper,
        UltimateBeneficiaryCategorySearchRepository ultimateBeneficiaryCategorySearchRepository
    ) {
        this.ultimateBeneficiaryCategoryRepository = ultimateBeneficiaryCategoryRepository;
        this.ultimateBeneficiaryCategoryMapper = ultimateBeneficiaryCategoryMapper;
        this.ultimateBeneficiaryCategorySearchRepository = ultimateBeneficiaryCategorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link UltimateBeneficiaryCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<UltimateBeneficiaryCategoryDTO> findByCriteria(UltimateBeneficiaryCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<UltimateBeneficiaryCategory> specification = createSpecification(criteria);
        return ultimateBeneficiaryCategoryMapper.toDto(ultimateBeneficiaryCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link UltimateBeneficiaryCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UltimateBeneficiaryCategoryDTO> findByCriteria(UltimateBeneficiaryCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UltimateBeneficiaryCategory> specification = createSpecification(criteria);
        return ultimateBeneficiaryCategoryRepository.findAll(specification, page).map(ultimateBeneficiaryCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UltimateBeneficiaryCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UltimateBeneficiaryCategory> specification = createSpecification(criteria);
        return ultimateBeneficiaryCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link UltimateBeneficiaryCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UltimateBeneficiaryCategory> createSpecification(UltimateBeneficiaryCategoryCriteria criteria) {
        Specification<UltimateBeneficiaryCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UltimateBeneficiaryCategory_.id));
            }
            if (criteria.getUltimateBeneficiaryCategoryTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getUltimateBeneficiaryCategoryTypeCode(),
                            UltimateBeneficiaryCategory_.ultimateBeneficiaryCategoryTypeCode
                        )
                    );
            }
            if (criteria.getUltimateBeneficiaryType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getUltimateBeneficiaryType(),
                            UltimateBeneficiaryCategory_.ultimateBeneficiaryType
                        )
                    );
            }
        }
        return specification;
    }
}
