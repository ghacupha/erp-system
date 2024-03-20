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
import io.github.erp.domain.RouAssetListReportItem;
import io.github.erp.repository.RouAssetListReportItemRepository;
import io.github.erp.repository.search.RouAssetListReportItemSearchRepository;
import io.github.erp.service.criteria.RouAssetListReportItemCriteria;
import io.github.erp.service.dto.RouAssetListReportItemDTO;
import io.github.erp.service.mapper.RouAssetListReportItemMapper;
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
 * Service for executing complex queries for {@link RouAssetListReportItem} entities in the database.
 * The main input is a {@link RouAssetListReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouAssetListReportItemDTO} or a {@link Page} of {@link RouAssetListReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouAssetListReportItemQueryService extends QueryService<RouAssetListReportItem> {

    private final Logger log = LoggerFactory.getLogger(RouAssetListReportItemQueryService.class);

    private final RouAssetListReportItemRepository rouAssetListReportItemRepository;

    private final RouAssetListReportItemMapper rouAssetListReportItemMapper;

    private final RouAssetListReportItemSearchRepository rouAssetListReportItemSearchRepository;

    public RouAssetListReportItemQueryService(
        RouAssetListReportItemRepository rouAssetListReportItemRepository,
        RouAssetListReportItemMapper rouAssetListReportItemMapper,
        RouAssetListReportItemSearchRepository rouAssetListReportItemSearchRepository
    ) {
        this.rouAssetListReportItemRepository = rouAssetListReportItemRepository;
        this.rouAssetListReportItemMapper = rouAssetListReportItemMapper;
        this.rouAssetListReportItemSearchRepository = rouAssetListReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouAssetListReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouAssetListReportItemDTO> findByCriteria(RouAssetListReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouAssetListReportItem> specification = createSpecification(criteria);
        return rouAssetListReportItemMapper.toDto(rouAssetListReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouAssetListReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouAssetListReportItemDTO> findByCriteria(RouAssetListReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouAssetListReportItem> specification = createSpecification(criteria);
        return rouAssetListReportItemRepository.findAll(specification, page).map(rouAssetListReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouAssetListReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouAssetListReportItem> specification = createSpecification(criteria);
        return rouAssetListReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link RouAssetListReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouAssetListReportItem> createSpecification(RouAssetListReportItemCriteria criteria) {
        Specification<RouAssetListReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouAssetListReportItem_.id));
            }
            if (criteria.getModelTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelTitle(), RouAssetListReportItem_.modelTitle));
            }
            if (criteria.getModelVersion() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getModelVersion(), RouAssetListReportItem_.modelVersion));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), RouAssetListReportItem_.description));
            }
            if (criteria.getLeaseTermPeriods() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLeaseTermPeriods(), RouAssetListReportItem_.leaseTermPeriods));
            }
            if (criteria.getRouModelReference() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getRouModelReference(), RouAssetListReportItem_.rouModelReference));
            }
            if (criteria.getCommencementDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCommencementDate(), RouAssetListReportItem_.commencementDate));
            }
            if (criteria.getExpirationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getExpirationDate(), RouAssetListReportItem_.expirationDate));
            }
            if (criteria.getLeaseContractTitle() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLeaseContractTitle(), RouAssetListReportItem_.leaseContractTitle)
                    );
            }
            if (criteria.getAssetAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAssetAccountNumber(), RouAssetListReportItem_.assetAccountNumber)
                    );
            }
            if (criteria.getDepreciationAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getDepreciationAccountNumber(), RouAssetListReportItem_.depreciationAccountNumber)
                    );
            }
            if (criteria.getAccruedDepreciationAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getAccruedDepreciationAccountNumber(),
                            RouAssetListReportItem_.accruedDepreciationAccountNumber
                        )
                    );
            }
            if (criteria.getAssetCategoryName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetCategoryName(), RouAssetListReportItem_.assetCategoryName));
            }
            if (criteria.getLeaseAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeaseAmount(), RouAssetListReportItem_.leaseAmount));
            }
            if (criteria.getLeaseContractSerialNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLeaseContractSerialNumber(), RouAssetListReportItem_.leaseContractSerialNumber)
                    );
            }
        }
        return specification;
    }
}
