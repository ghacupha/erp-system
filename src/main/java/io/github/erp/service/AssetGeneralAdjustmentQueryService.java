package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.AssetGeneralAdjustment;
import io.github.erp.repository.AssetGeneralAdjustmentRepository;
import io.github.erp.repository.search.AssetGeneralAdjustmentSearchRepository;
import io.github.erp.service.criteria.AssetGeneralAdjustmentCriteria;
import io.github.erp.service.dto.AssetGeneralAdjustmentDTO;
import io.github.erp.service.mapper.AssetGeneralAdjustmentMapper;
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
 * Service for executing complex queries for {@link AssetGeneralAdjustment} entities in the database.
 * The main input is a {@link AssetGeneralAdjustmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetGeneralAdjustmentDTO} or a {@link Page} of {@link AssetGeneralAdjustmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetGeneralAdjustmentQueryService extends QueryService<AssetGeneralAdjustment> {

    private final Logger log = LoggerFactory.getLogger(AssetGeneralAdjustmentQueryService.class);

    private final AssetGeneralAdjustmentRepository assetGeneralAdjustmentRepository;

    private final AssetGeneralAdjustmentMapper assetGeneralAdjustmentMapper;

    private final AssetGeneralAdjustmentSearchRepository assetGeneralAdjustmentSearchRepository;

    public AssetGeneralAdjustmentQueryService(
        AssetGeneralAdjustmentRepository assetGeneralAdjustmentRepository,
        AssetGeneralAdjustmentMapper assetGeneralAdjustmentMapper,
        AssetGeneralAdjustmentSearchRepository assetGeneralAdjustmentSearchRepository
    ) {
        this.assetGeneralAdjustmentRepository = assetGeneralAdjustmentRepository;
        this.assetGeneralAdjustmentMapper = assetGeneralAdjustmentMapper;
        this.assetGeneralAdjustmentSearchRepository = assetGeneralAdjustmentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AssetGeneralAdjustmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetGeneralAdjustmentDTO> findByCriteria(AssetGeneralAdjustmentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetGeneralAdjustment> specification = createSpecification(criteria);
        return assetGeneralAdjustmentMapper.toDto(assetGeneralAdjustmentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetGeneralAdjustmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetGeneralAdjustmentDTO> findByCriteria(AssetGeneralAdjustmentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetGeneralAdjustment> specification = createSpecification(criteria);
        return assetGeneralAdjustmentRepository.findAll(specification, page).map(assetGeneralAdjustmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetGeneralAdjustmentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetGeneralAdjustment> specification = createSpecification(criteria);
        return assetGeneralAdjustmentRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetGeneralAdjustmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetGeneralAdjustment> createSpecification(AssetGeneralAdjustmentCriteria criteria) {
        Specification<AssetGeneralAdjustment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetGeneralAdjustment_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AssetGeneralAdjustment_.description));
            }
            if (criteria.getDevaluationAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDevaluationAmount(), AssetGeneralAdjustment_.devaluationAmount));
            }
            if (criteria.getAdjustmentDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAdjustmentDate(), AssetGeneralAdjustment_.adjustmentDate));
            }
            if (criteria.getTimeOfCreation() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfCreation(), AssetGeneralAdjustment_.timeOfCreation));
            }
            if (criteria.getAdjustmentReferenceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getAdjustmentReferenceId(), AssetGeneralAdjustment_.adjustmentReferenceId)
                    );
            }
            if (criteria.getEffectivePeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEffectivePeriodId(),
                            root -> root.join(AssetGeneralAdjustment_.effectivePeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
            if (criteria.getAssetRegistrationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetRegistrationId(),
                            root -> root.join(AssetGeneralAdjustment_.assetRegistration, JoinType.LEFT).get(AssetRegistration_.id)
                        )
                    );
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(AssetGeneralAdjustment_.createdBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastModifiedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastModifiedById(),
                            root -> root.join(AssetGeneralAdjustment_.lastModifiedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastAccessedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastAccessedById(),
                            root -> root.join(AssetGeneralAdjustment_.lastAccessedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AssetGeneralAdjustment_.placeholder, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
