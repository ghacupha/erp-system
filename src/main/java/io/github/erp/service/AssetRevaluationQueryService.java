package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.AssetRevaluation;
import io.github.erp.repository.AssetRevaluationRepository;
import io.github.erp.repository.search.AssetRevaluationSearchRepository;
import io.github.erp.service.criteria.AssetRevaluationCriteria;
import io.github.erp.service.dto.AssetRevaluationDTO;
import io.github.erp.service.mapper.AssetRevaluationMapper;
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
 * Service for executing complex queries for {@link AssetRevaluation} entities in the database.
 * The main input is a {@link AssetRevaluationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetRevaluationDTO} or a {@link Page} of {@link AssetRevaluationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetRevaluationQueryService extends QueryService<AssetRevaluation> {

    private final Logger log = LoggerFactory.getLogger(AssetRevaluationQueryService.class);

    private final AssetRevaluationRepository assetRevaluationRepository;

    private final AssetRevaluationMapper assetRevaluationMapper;

    private final AssetRevaluationSearchRepository assetRevaluationSearchRepository;

    public AssetRevaluationQueryService(
        AssetRevaluationRepository assetRevaluationRepository,
        AssetRevaluationMapper assetRevaluationMapper,
        AssetRevaluationSearchRepository assetRevaluationSearchRepository
    ) {
        this.assetRevaluationRepository = assetRevaluationRepository;
        this.assetRevaluationMapper = assetRevaluationMapper;
        this.assetRevaluationSearchRepository = assetRevaluationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AssetRevaluationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetRevaluationDTO> findByCriteria(AssetRevaluationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetRevaluation> specification = createSpecification(criteria);
        return assetRevaluationMapper.toDto(assetRevaluationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetRevaluationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetRevaluationDTO> findByCriteria(AssetRevaluationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetRevaluation> specification = createSpecification(criteria);
        return assetRevaluationRepository.findAll(specification, page).map(assetRevaluationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetRevaluationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetRevaluation> specification = createSpecification(criteria);
        return assetRevaluationRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetRevaluationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetRevaluation> createSpecification(AssetRevaluationCriteria criteria) {
        Specification<AssetRevaluation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetRevaluation_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AssetRevaluation_.description));
            }
            if (criteria.getDevaluationAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDevaluationAmount(), AssetRevaluation_.devaluationAmount));
            }
            if (criteria.getRevaluationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRevaluationDate(), AssetRevaluation_.revaluationDate));
            }
            if (criteria.getRevaluationReferenceId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getRevaluationReferenceId(), AssetRevaluation_.revaluationReferenceId));
            }
            if (criteria.getTimeOfCreation() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTimeOfCreation(), AssetRevaluation_.timeOfCreation));
            }
            if (criteria.getRevaluerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRevaluerId(),
                            root -> root.join(AssetRevaluation_.revaluer, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(AssetRevaluation_.createdBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastModifiedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastModifiedById(),
                            root -> root.join(AssetRevaluation_.lastModifiedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastAccessedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastAccessedById(),
                            root -> root.join(AssetRevaluation_.lastAccessedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getEffectivePeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEffectivePeriodId(),
                            root -> root.join(AssetRevaluation_.effectivePeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
            if (criteria.getRevaluedAssetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRevaluedAssetId(),
                            root -> root.join(AssetRevaluation_.revaluedAsset, JoinType.LEFT).get(AssetRegistration_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AssetRevaluation_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
