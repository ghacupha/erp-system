package io.github.erp.service;

/*-
 * Erp System - Mark VII No 5 (Gideon Series) Server ver 1.5.9
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
import io.github.erp.domain.AssetCategory;
import io.github.erp.repository.AssetCategoryRepository;
import io.github.erp.repository.search.AssetCategorySearchRepository;
import io.github.erp.service.criteria.AssetCategoryCriteria;
import io.github.erp.service.dto.AssetCategoryDTO;
import io.github.erp.service.mapper.AssetCategoryMapper;
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
 * Service for executing complex queries for {@link AssetCategory} entities in the database.
 * The main input is a {@link AssetCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetCategoryDTO} or a {@link Page} of {@link AssetCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetCategoryQueryService extends QueryService<AssetCategory> {

    private final Logger log = LoggerFactory.getLogger(AssetCategoryQueryService.class);

    private final AssetCategoryRepository assetCategoryRepository;

    private final AssetCategoryMapper assetCategoryMapper;

    private final AssetCategorySearchRepository assetCategorySearchRepository;

    public AssetCategoryQueryService(
        AssetCategoryRepository assetCategoryRepository,
        AssetCategoryMapper assetCategoryMapper,
        AssetCategorySearchRepository assetCategorySearchRepository
    ) {
        this.assetCategoryRepository = assetCategoryRepository;
        this.assetCategoryMapper = assetCategoryMapper;
        this.assetCategorySearchRepository = assetCategorySearchRepository;
    }

    /**
     * Return a {@link List} of {@link AssetCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetCategoryDTO> findByCriteria(AssetCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetCategory> specification = createSpecification(criteria);
        return assetCategoryMapper.toDto(assetCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetCategoryDTO> findByCriteria(AssetCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetCategory> specification = createSpecification(criteria);
        return assetCategoryRepository.findAll(specification, page).map(assetCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetCategory> specification = createSpecification(criteria);
        return assetCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetCategory> createSpecification(AssetCategoryCriteria criteria) {
        Specification<AssetCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetCategory_.id));
            }
            if (criteria.getAssetCategoryName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetCategoryName(), AssetCategory_.assetCategoryName));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AssetCategory_.description));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), AssetCategory_.notes));
            }
            if (criteria.getDepreciationRateYearly() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDepreciationRateYearly(), AssetCategory_.depreciationRateYearly));
            }
            if (criteria.getDepreciationMethodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationMethodId(),
                            root -> root.join(AssetCategory_.depreciationMethod, JoinType.LEFT).get(DepreciationMethod_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AssetCategory_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
