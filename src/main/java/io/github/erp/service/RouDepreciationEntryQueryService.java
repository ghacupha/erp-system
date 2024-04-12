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
import io.github.erp.domain.RouDepreciationEntry;
import io.github.erp.repository.RouDepreciationEntryRepository;
import io.github.erp.repository.search.RouDepreciationEntrySearchRepository;
import io.github.erp.service.criteria.RouDepreciationEntryCriteria;
import io.github.erp.service.dto.RouDepreciationEntryDTO;
import io.github.erp.service.mapper.RouDepreciationEntryMapper;
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
 * Service for executing complex queries for {@link RouDepreciationEntry} entities in the database.
 * The main input is a {@link RouDepreciationEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouDepreciationEntryDTO} or a {@link Page} of {@link RouDepreciationEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouDepreciationEntryQueryService extends QueryService<RouDepreciationEntry> {

    private final Logger log = LoggerFactory.getLogger(RouDepreciationEntryQueryService.class);

    private final RouDepreciationEntryRepository rouDepreciationEntryRepository;

    private final RouDepreciationEntryMapper rouDepreciationEntryMapper;

    private final RouDepreciationEntrySearchRepository rouDepreciationEntrySearchRepository;

    public RouDepreciationEntryQueryService(
        RouDepreciationEntryRepository rouDepreciationEntryRepository,
        RouDepreciationEntryMapper rouDepreciationEntryMapper,
        RouDepreciationEntrySearchRepository rouDepreciationEntrySearchRepository
    ) {
        this.rouDepreciationEntryRepository = rouDepreciationEntryRepository;
        this.rouDepreciationEntryMapper = rouDepreciationEntryMapper;
        this.rouDepreciationEntrySearchRepository = rouDepreciationEntrySearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouDepreciationEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouDepreciationEntryDTO> findByCriteria(RouDepreciationEntryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouDepreciationEntry> specification = createSpecification(criteria);
        return rouDepreciationEntryMapper.toDto(rouDepreciationEntryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouDepreciationEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouDepreciationEntryDTO> findByCriteria(RouDepreciationEntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouDepreciationEntry> specification = createSpecification(criteria);
        return rouDepreciationEntryRepository.findAll(specification, page).map(rouDepreciationEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouDepreciationEntryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouDepreciationEntry> specification = createSpecification(criteria);
        return rouDepreciationEntryRepository.count(specification);
    }

    /**
     * Function to convert {@link RouDepreciationEntryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouDepreciationEntry> createSpecification(RouDepreciationEntryCriteria criteria) {
        Specification<RouDepreciationEntry> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouDepreciationEntry_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), RouDepreciationEntry_.description));
            }
            if (criteria.getDepreciationAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDepreciationAmount(), RouDepreciationEntry_.depreciationAmount));
            }
            if (criteria.getOutstandingAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutstandingAmount(), RouDepreciationEntry_.outstandingAmount));
            }
            if (criteria.getRouAssetIdentifier() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getRouAssetIdentifier(), RouDepreciationEntry_.rouAssetIdentifier));
            }
            if (criteria.getRouDepreciationIdentifier() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getRouDepreciationIdentifier(), RouDepreciationEntry_.rouDepreciationIdentifier)
                    );
            }
            if (criteria.getSequenceNumber() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getSequenceNumber(), RouDepreciationEntry_.sequenceNumber));
            }
            if (criteria.getDebitAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDebitAccountId(),
                            root -> root.join(RouDepreciationEntry_.debitAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getCreditAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCreditAccountId(),
                            root -> root.join(RouDepreciationEntry_.creditAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getAssetCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetCategoryId(),
                            root -> root.join(RouDepreciationEntry_.assetCategory, JoinType.LEFT).get(AssetCategory_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(RouDepreciationEntry_.leaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
            if (criteria.getRouMetadataId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRouMetadataId(),
                            root -> root.join(RouDepreciationEntry_.rouMetadata, JoinType.LEFT).get(RouModelMetadata_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
