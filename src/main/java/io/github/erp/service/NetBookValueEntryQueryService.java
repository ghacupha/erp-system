package io.github.erp.service;

/*-
 * Erp System - Mark X No 4 (Jehoiada Series) Server ver 1.7.4
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
import io.github.erp.domain.NetBookValueEntry;
import io.github.erp.repository.NetBookValueEntryRepository;
import io.github.erp.repository.search.NetBookValueEntrySearchRepository;
import io.github.erp.service.criteria.NetBookValueEntryCriteria;
import io.github.erp.service.dto.NetBookValueEntryDTO;
import io.github.erp.service.mapper.NetBookValueEntryMapper;
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
 * Service for executing complex queries for {@link NetBookValueEntry} entities in the database.
 * The main input is a {@link NetBookValueEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link NetBookValueEntryDTO} or a {@link Page} of {@link NetBookValueEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class NetBookValueEntryQueryService extends QueryService<NetBookValueEntry> {

    private final Logger log = LoggerFactory.getLogger(NetBookValueEntryQueryService.class);

    private final NetBookValueEntryRepository netBookValueEntryRepository;

    private final NetBookValueEntryMapper netBookValueEntryMapper;

    private final NetBookValueEntrySearchRepository netBookValueEntrySearchRepository;

    public NetBookValueEntryQueryService(
        NetBookValueEntryRepository netBookValueEntryRepository,
        NetBookValueEntryMapper netBookValueEntryMapper,
        NetBookValueEntrySearchRepository netBookValueEntrySearchRepository
    ) {
        this.netBookValueEntryRepository = netBookValueEntryRepository;
        this.netBookValueEntryMapper = netBookValueEntryMapper;
        this.netBookValueEntrySearchRepository = netBookValueEntrySearchRepository;
    }

    /**
     * Return a {@link List} of {@link NetBookValueEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<NetBookValueEntryDTO> findByCriteria(NetBookValueEntryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<NetBookValueEntry> specification = createSpecification(criteria);
        return netBookValueEntryMapper.toDto(netBookValueEntryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link NetBookValueEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<NetBookValueEntryDTO> findByCriteria(NetBookValueEntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<NetBookValueEntry> specification = createSpecification(criteria);
        return netBookValueEntryRepository.findAll(specification, page).map(netBookValueEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(NetBookValueEntryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<NetBookValueEntry> specification = createSpecification(criteria);
        return netBookValueEntryRepository.count(specification);
    }

    /**
     * Function to convert {@link NetBookValueEntryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<NetBookValueEntry> createSpecification(NetBookValueEntryCriteria criteria) {
        Specification<NetBookValueEntry> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), NetBookValueEntry_.id));
            }
            if (criteria.getAssetNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetNumber(), NetBookValueEntry_.assetNumber));
            }
            if (criteria.getAssetTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetTag(), NetBookValueEntry_.assetTag));
            }
            if (criteria.getAssetDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetDescription(), NetBookValueEntry_.assetDescription));
            }
            if (criteria.getNbvIdentifier() != null) {
                specification = specification.and(buildSpecification(criteria.getNbvIdentifier(), NetBookValueEntry_.nbvIdentifier));
            }
            if (criteria.getCompilationJobIdentifier() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompilationJobIdentifier(), NetBookValueEntry_.compilationJobIdentifier)
                    );
            }
            if (criteria.getCompilationBatchIdentifier() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCompilationBatchIdentifier(), NetBookValueEntry_.compilationBatchIdentifier)
                    );
            }
            if (criteria.getElapsedMonths() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getElapsedMonths(), NetBookValueEntry_.elapsedMonths));
            }
            if (criteria.getPriorMonths() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPriorMonths(), NetBookValueEntry_.priorMonths));
            }
            if (criteria.getUsefulLifeYears() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getUsefulLifeYears(), NetBookValueEntry_.usefulLifeYears));
            }
            if (criteria.getNetBookValueAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getNetBookValueAmount(), NetBookValueEntry_.netBookValueAmount));
            }
            if (criteria.getPreviousNetBookValueAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getPreviousNetBookValueAmount(), NetBookValueEntry_.previousNetBookValueAmount)
                    );
            }
            if (criteria.getHistoricalCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getHistoricalCost(), NetBookValueEntry_.historicalCost));
            }
            if (criteria.getCapitalizationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCapitalizationDate(), NetBookValueEntry_.capitalizationDate));
            }
            if (criteria.getServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceOutletId(),
                            root -> root.join(NetBookValueEntry_.serviceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getDepreciationPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationPeriodId(),
                            root -> root.join(NetBookValueEntry_.depreciationPeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
            if (criteria.getFiscalMonthId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFiscalMonthId(),
                            root -> root.join(NetBookValueEntry_.fiscalMonth, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
            if (criteria.getDepreciationMethodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationMethodId(),
                            root -> root.join(NetBookValueEntry_.depreciationMethod, JoinType.LEFT).get(DepreciationMethod_.id)
                        )
                    );
            }
            if (criteria.getAssetRegistrationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetRegistrationId(),
                            root -> root.join(NetBookValueEntry_.assetRegistration, JoinType.LEFT).get(AssetRegistration_.id)
                        )
                    );
            }
            if (criteria.getAssetCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetCategoryId(),
                            root -> root.join(NetBookValueEntry_.assetCategory, JoinType.LEFT).get(AssetCategory_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(NetBookValueEntry_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
