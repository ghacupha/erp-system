package io.github.erp.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.RouDepreciationEntryReportItem;
import io.github.erp.repository.RouDepreciationEntryReportItemRepository;
import io.github.erp.repository.search.RouDepreciationEntryReportItemSearchRepository;
import io.github.erp.service.criteria.RouDepreciationEntryReportItemCriteria;
import io.github.erp.service.dto.RouDepreciationEntryReportItemDTO;
import io.github.erp.service.mapper.RouDepreciationEntryReportItemMapper;
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
 * Service for executing complex queries for {@link RouDepreciationEntryReportItem} entities in the database.
 * The main input is a {@link RouDepreciationEntryReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouDepreciationEntryReportItemDTO} or a {@link Page} of {@link RouDepreciationEntryReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouDepreciationEntryReportItemQueryService extends QueryService<RouDepreciationEntryReportItem> {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationEntryReportItemQueryService.class);

    private final RouDepreciationEntryReportItemRepository rouDepreciationEntryReportItemRepository;

    private final RouDepreciationEntryReportItemMapper rouDepreciationEntryReportItemMapper;

    private final RouDepreciationEntryReportItemSearchRepository rouDepreciationEntryReportItemSearchRepository;

    public RouDepreciationEntryReportItemQueryService(
        RouDepreciationEntryReportItemRepository rouDepreciationEntryReportItemRepository,
        RouDepreciationEntryReportItemMapper rouDepreciationEntryReportItemMapper,
        RouDepreciationEntryReportItemSearchRepository rouDepreciationEntryReportItemSearchRepository
    ) {
        this.rouDepreciationEntryReportItemRepository = rouDepreciationEntryReportItemRepository;
        this.rouDepreciationEntryReportItemMapper = rouDepreciationEntryReportItemMapper;
        this.rouDepreciationEntryReportItemSearchRepository = rouDepreciationEntryReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouDepreciationEntryReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouDepreciationEntryReportItemDTO> findByCriteria(RouDepreciationEntryReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouDepreciationEntryReportItem> specification = createSpecification(criteria);
        return rouDepreciationEntryReportItemMapper.toDto(rouDepreciationEntryReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouDepreciationEntryReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryReportItemDTO> findByCriteria(RouDepreciationEntryReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouDepreciationEntryReportItem> specification = createSpecification(criteria);
        return rouDepreciationEntryReportItemRepository.findAll(specification, page).map(rouDepreciationEntryReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouDepreciationEntryReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouDepreciationEntryReportItem> specification = createSpecification(criteria);
        return rouDepreciationEntryReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link RouDepreciationEntryReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouDepreciationEntryReportItem> createSpecification(RouDepreciationEntryReportItemCriteria criteria) {
        Specification<RouDepreciationEntryReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouDepreciationEntryReportItem_.id));
            }
            if (criteria.getLeaseContractNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getLeaseContractNumber(), RouDepreciationEntryReportItem_.leaseContractNumber)
                    );
            }
            if (criteria.getFiscalPeriodCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getFiscalPeriodCode(), RouDepreciationEntryReportItem_.fiscalPeriodCode)
                    );
            }
            if (criteria.getFiscalPeriodEndDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getFiscalPeriodEndDate(), RouDepreciationEntryReportItem_.fiscalPeriodEndDate)
                    );
            }
            if (criteria.getAssetCategoryName() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAssetCategoryName(), RouDepreciationEntryReportItem_.assetCategoryName)
                    );
            }
            if (criteria.getDebitAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getDebitAccountNumber(), RouDepreciationEntryReportItem_.debitAccountNumber)
                    );
            }
            if (criteria.getCreditAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getCreditAccountNumber(), RouDepreciationEntryReportItem_.creditAccountNumber)
                    );
            }
            if (criteria.getDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDescription(), RouDepreciationEntryReportItem_.description));
            }
            if (criteria.getShortTitle() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getShortTitle(), RouDepreciationEntryReportItem_.shortTitle));
            }
            if (criteria.getRouAssetIdentifier() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getRouAssetIdentifier(), RouDepreciationEntryReportItem_.rouAssetIdentifier)
                    );
            }
            if (criteria.getSequenceNumber() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSequenceNumber(), RouDepreciationEntryReportItem_.sequenceNumber)
                    );
            }
            if (criteria.getDepreciationAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getDepreciationAmount(), RouDepreciationEntryReportItem_.depreciationAmount)
                    );
            }
            if (criteria.getOutstandingAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getOutstandingAmount(), RouDepreciationEntryReportItem_.outstandingAmount)
                    );
            }
        }
        return specification;
    }
}
