package io.github.erp.service;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.domain.AssetDisposal;
import io.github.erp.repository.AssetDisposalRepository;
import io.github.erp.repository.search.AssetDisposalSearchRepository;
import io.github.erp.service.criteria.AssetDisposalCriteria;
import io.github.erp.service.dto.AssetDisposalDTO;
import io.github.erp.service.mapper.AssetDisposalMapper;
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
 * Service for executing complex queries for {@link AssetDisposal} entities in the database.
 * The main input is a {@link AssetDisposalCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetDisposalDTO} or a {@link Page} of {@link AssetDisposalDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetDisposalQueryService extends QueryService<AssetDisposal> {

    private final Logger log = LoggerFactory.getLogger(AssetDisposalQueryService.class);

    private final AssetDisposalRepository assetDisposalRepository;

    private final AssetDisposalMapper assetDisposalMapper;

    private final AssetDisposalSearchRepository assetDisposalSearchRepository;

    public AssetDisposalQueryService(
        AssetDisposalRepository assetDisposalRepository,
        AssetDisposalMapper assetDisposalMapper,
        AssetDisposalSearchRepository assetDisposalSearchRepository
    ) {
        this.assetDisposalRepository = assetDisposalRepository;
        this.assetDisposalMapper = assetDisposalMapper;
        this.assetDisposalSearchRepository = assetDisposalSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AssetDisposalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetDisposalDTO> findByCriteria(AssetDisposalCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetDisposal> specification = createSpecification(criteria);
        return assetDisposalMapper.toDto(assetDisposalRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetDisposalDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetDisposalDTO> findByCriteria(AssetDisposalCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetDisposal> specification = createSpecification(criteria);
        return assetDisposalRepository.findAll(specification, page).map(assetDisposalMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetDisposalCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetDisposal> specification = createSpecification(criteria);
        return assetDisposalRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetDisposalCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetDisposal> createSpecification(AssetDisposalCriteria criteria) {
        Specification<AssetDisposal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetDisposal_.id));
            }
            if (criteria.getAssetDisposalReference() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getAssetDisposalReference(), AssetDisposal_.assetDisposalReference));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AssetDisposal_.description));
            }
            if (criteria.getAssetCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetCost(), AssetDisposal_.assetCost));
            }
            if (criteria.getHistoricalCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHistoricalCost(), AssetDisposal_.historicalCost));
            }
            if (criteria.getAccruedDepreciation() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getAccruedDepreciation(), AssetDisposal_.accruedDepreciation));
            }
            if (criteria.getNetBookValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNetBookValue(), AssetDisposal_.netBookValue));
            }
            if (criteria.getDecommissioningDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDecommissioningDate(), AssetDisposal_.decommissioningDate));
            }
            if (criteria.getDisposalDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDisposalDate(), AssetDisposal_.disposalDate));
            }
            if (criteria.getDormant() != null) {
                specification = specification.and(buildSpecification(criteria.getDormant(), AssetDisposal_.dormant));
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(AssetDisposal_.createdBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getModifiedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getModifiedById(),
                            root -> root.join(AssetDisposal_.modifiedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastAccessedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastAccessedById(),
                            root -> root.join(AssetDisposal_.lastAccessedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getEffectivePeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEffectivePeriodId(),
                            root -> root.join(AssetDisposal_.effectivePeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AssetDisposal_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getAssetDisposedId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetDisposedId(),
                            root -> root.join(AssetDisposal_.assetDisposed, JoinType.LEFT).get(AssetRegistration_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
