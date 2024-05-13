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
import io.github.erp.domain.AssetWriteOff;
import io.github.erp.repository.AssetWriteOffRepository;
import io.github.erp.repository.search.AssetWriteOffSearchRepository;
import io.github.erp.service.criteria.AssetWriteOffCriteria;
import io.github.erp.service.dto.AssetWriteOffDTO;
import io.github.erp.service.mapper.AssetWriteOffMapper;
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
 * Service for executing complex queries for {@link AssetWriteOff} entities in the database.
 * The main input is a {@link AssetWriteOffCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetWriteOffDTO} or a {@link Page} of {@link AssetWriteOffDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetWriteOffQueryService extends QueryService<AssetWriteOff> {

    private final Logger log = LoggerFactory.getLogger(AssetWriteOffQueryService.class);

    private final AssetWriteOffRepository assetWriteOffRepository;

    private final AssetWriteOffMapper assetWriteOffMapper;

    private final AssetWriteOffSearchRepository assetWriteOffSearchRepository;

    public AssetWriteOffQueryService(
        AssetWriteOffRepository assetWriteOffRepository,
        AssetWriteOffMapper assetWriteOffMapper,
        AssetWriteOffSearchRepository assetWriteOffSearchRepository
    ) {
        this.assetWriteOffRepository = assetWriteOffRepository;
        this.assetWriteOffMapper = assetWriteOffMapper;
        this.assetWriteOffSearchRepository = assetWriteOffSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AssetWriteOffDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetWriteOffDTO> findByCriteria(AssetWriteOffCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetWriteOff> specification = createSpecification(criteria);
        return assetWriteOffMapper.toDto(assetWriteOffRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetWriteOffDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetWriteOffDTO> findByCriteria(AssetWriteOffCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetWriteOff> specification = createSpecification(criteria);
        return assetWriteOffRepository.findAll(specification, page).map(assetWriteOffMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetWriteOffCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetWriteOff> specification = createSpecification(criteria);
        return assetWriteOffRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetWriteOffCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetWriteOff> createSpecification(AssetWriteOffCriteria criteria) {
        Specification<AssetWriteOff> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetWriteOff_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AssetWriteOff_.description));
            }
            if (criteria.getWriteOffAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWriteOffAmount(), AssetWriteOff_.writeOffAmount));
            }
            if (criteria.getWriteOffDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWriteOffDate(), AssetWriteOff_.writeOffDate));
            }
            if (criteria.getWriteOffReferenceId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getWriteOffReferenceId(), AssetWriteOff_.writeOffReferenceId));
            }
            if (criteria.getCreatedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreatedById(),
                            root -> root.join(AssetWriteOff_.createdBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getModifiedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getModifiedById(),
                            root -> root.join(AssetWriteOff_.modifiedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getLastAccessedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastAccessedById(),
                            root -> root.join(AssetWriteOff_.lastAccessedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getEffectivePeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEffectivePeriodId(),
                            root -> root.join(AssetWriteOff_.effectivePeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AssetWriteOff_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getAssetWrittenOffId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetWrittenOffId(),
                            root -> root.join(AssetWriteOff_.assetWrittenOff, JoinType.LEFT).get(AssetRegistration_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
