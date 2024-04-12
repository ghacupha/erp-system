package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.7
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
import io.github.erp.domain.RouAssetNBVReportItem;
import io.github.erp.repository.RouAssetNBVReportItemRepository;
import io.github.erp.repository.search.RouAssetNBVReportItemSearchRepository;
import io.github.erp.service.criteria.RouAssetNBVReportItemCriteria;
import io.github.erp.service.dto.RouAssetNBVReportItemDTO;
import io.github.erp.service.mapper.RouAssetNBVReportItemMapper;
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
 * Service for executing complex queries for {@link RouAssetNBVReportItem} entities in the database.
 * The main input is a {@link RouAssetNBVReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouAssetNBVReportItemDTO} or a {@link Page} of {@link RouAssetNBVReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouAssetNBVReportItemQueryService extends QueryService<RouAssetNBVReportItem> {

    private final Logger log = LoggerFactory.getLogger(RouAssetNBVReportItemQueryService.class);

    private final RouAssetNBVReportItemRepository rouAssetNBVReportItemRepository;

    private final RouAssetNBVReportItemMapper rouAssetNBVReportItemMapper;

    private final RouAssetNBVReportItemSearchRepository rouAssetNBVReportItemSearchRepository;

    public RouAssetNBVReportItemQueryService(
        RouAssetNBVReportItemRepository rouAssetNBVReportItemRepository,
        RouAssetNBVReportItemMapper rouAssetNBVReportItemMapper,
        RouAssetNBVReportItemSearchRepository rouAssetNBVReportItemSearchRepository
    ) {
        this.rouAssetNBVReportItemRepository = rouAssetNBVReportItemRepository;
        this.rouAssetNBVReportItemMapper = rouAssetNBVReportItemMapper;
        this.rouAssetNBVReportItemSearchRepository = rouAssetNBVReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouAssetNBVReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouAssetNBVReportItemDTO> findByCriteria(RouAssetNBVReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouAssetNBVReportItem> specification = createSpecification(criteria);
        return rouAssetNBVReportItemMapper.toDto(rouAssetNBVReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouAssetNBVReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouAssetNBVReportItemDTO> findByCriteria(RouAssetNBVReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouAssetNBVReportItem> specification = createSpecification(criteria);
        return rouAssetNBVReportItemRepository.findAll(specification, page).map(rouAssetNBVReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouAssetNBVReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouAssetNBVReportItem> specification = createSpecification(criteria);
        return rouAssetNBVReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link RouAssetNBVReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouAssetNBVReportItem> createSpecification(RouAssetNBVReportItemCriteria criteria) {
        Specification<RouAssetNBVReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouAssetNBVReportItem_.id));
            }
            if (criteria.getModelTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelTitle(), RouAssetNBVReportItem_.modelTitle));
            }
            if (criteria.getModelVersion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModelVersion(), RouAssetNBVReportItem_.modelVersion));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), RouAssetNBVReportItem_.description));
            }
            if (criteria.getRouModelReference() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRouModelReference(), RouAssetNBVReportItem_.rouModelReference));
            }
            if (criteria.getCommencementDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCommencementDate(), RouAssetNBVReportItem_.commencementDate));
            }
            if (criteria.getExpirationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getExpirationDate(), RouAssetNBVReportItem_.expirationDate));
            }
            if (criteria.getAssetCategoryName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetCategoryName(), RouAssetNBVReportItem_.assetCategoryName));
            }
            if (criteria.getAssetAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAssetAccountNumber(), RouAssetNBVReportItem_.assetAccountNumber)
                    );
            }
            if (criteria.getDepreciationAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getDepreciationAccountNumber(), RouAssetNBVReportItem_.depreciationAccountNumber)
                    );
            }
            if (criteria.getFiscalPeriodEndDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getFiscalPeriodEndDate(), RouAssetNBVReportItem_.fiscalPeriodEndDate)
                    );
            }
            if (criteria.getLeaseAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeaseAmount(), RouAssetNBVReportItem_.leaseAmount));
            }
            if (criteria.getNetBookValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNetBookValue(), RouAssetNBVReportItem_.netBookValue));
            }
        }
        return specification;
    }
}
