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
import io.github.erp.domain.AssetAdditionsReportItem;
import io.github.erp.repository.AssetAdditionsReportItemRepository;
import io.github.erp.repository.search.AssetAdditionsReportItemSearchRepository;
import io.github.erp.service.criteria.AssetAdditionsReportItemCriteria;
import io.github.erp.service.dto.AssetAdditionsReportItemDTO;
import io.github.erp.service.mapper.AssetAdditionsReportItemMapper;
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
 * Service for executing complex queries for {@link AssetAdditionsReportItem} entities in the database.
 * The main input is a {@link AssetAdditionsReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetAdditionsReportItemDTO} or a {@link Page} of {@link AssetAdditionsReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetAdditionsReportItemQueryService extends QueryService<AssetAdditionsReportItem> {

    private final Logger log = LoggerFactory.getLogger(AssetAdditionsReportItemQueryService.class);

    private final AssetAdditionsReportItemRepository assetAdditionsReportItemRepository;

    private final AssetAdditionsReportItemMapper assetAdditionsReportItemMapper;

    private final AssetAdditionsReportItemSearchRepository assetAdditionsReportItemSearchRepository;

    public AssetAdditionsReportItemQueryService(
        AssetAdditionsReportItemRepository assetAdditionsReportItemRepository,
        AssetAdditionsReportItemMapper assetAdditionsReportItemMapper,
        AssetAdditionsReportItemSearchRepository assetAdditionsReportItemSearchRepository
    ) {
        this.assetAdditionsReportItemRepository = assetAdditionsReportItemRepository;
        this.assetAdditionsReportItemMapper = assetAdditionsReportItemMapper;
        this.assetAdditionsReportItemSearchRepository = assetAdditionsReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link AssetAdditionsReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetAdditionsReportItemDTO> findByCriteria(AssetAdditionsReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetAdditionsReportItem> specification = createSpecification(criteria);
        return assetAdditionsReportItemMapper.toDto(assetAdditionsReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetAdditionsReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetAdditionsReportItemDTO> findByCriteria(AssetAdditionsReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetAdditionsReportItem> specification = createSpecification(criteria);
        return assetAdditionsReportItemRepository.findAll(specification, page).map(assetAdditionsReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetAdditionsReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetAdditionsReportItem> specification = createSpecification(criteria);
        return assetAdditionsReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetAdditionsReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetAdditionsReportItem> createSpecification(AssetAdditionsReportItemCriteria criteria) {
        Specification<AssetAdditionsReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetAdditionsReportItem_.id));
            }
            if (criteria.getAssetNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetNumber(), AssetAdditionsReportItem_.assetNumber));
            }
            if (criteria.getAssetTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetTag(), AssetAdditionsReportItem_.assetTag));
            }
            if (criteria.getServiceOutletCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getServiceOutletCode(), AssetAdditionsReportItem_.serviceOutletCode)
                    );
            }
            if (criteria.getTransactionId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTransactionId(), AssetAdditionsReportItem_.transactionId));
            }
            if (criteria.getTransactionDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransactionDate(), AssetAdditionsReportItem_.transactionDate));
            }
            if (criteria.getCapitalizationDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getCapitalizationDate(), AssetAdditionsReportItem_.capitalizationDate)
                    );
            }
            if (criteria.getAssetCategory() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetCategory(), AssetAdditionsReportItem_.assetCategory));
            }
            if (criteria.getAssetDetails() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetDetails(), AssetAdditionsReportItem_.assetDetails));
            }
            if (criteria.getAssetCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetCost(), AssetAdditionsReportItem_.assetCost));
            }
            if (criteria.getSupplier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSupplier(), AssetAdditionsReportItem_.supplier));
            }
            if (criteria.getHistoricalCost() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getHistoricalCost(), AssetAdditionsReportItem_.historicalCost));
            }
            if (criteria.getRegistrationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRegistrationDate(), AssetAdditionsReportItem_.registrationDate));
            }
        }
        return specification;
    }
}
