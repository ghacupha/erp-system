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
import io.github.erp.domain.RouModelMetadata;
import io.github.erp.repository.RouModelMetadataRepository;
import io.github.erp.repository.search.RouModelMetadataSearchRepository;
import io.github.erp.service.criteria.RouModelMetadataCriteria;
import io.github.erp.service.dto.RouModelMetadataDTO;
import io.github.erp.service.mapper.RouModelMetadataMapper;
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
 * Service for executing complex queries for {@link RouModelMetadata} entities in the database.
 * The main input is a {@link RouModelMetadataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RouModelMetadataDTO} or a {@link Page} of {@link RouModelMetadataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RouModelMetadataQueryService extends QueryService<RouModelMetadata> {

    private final Logger log = LoggerFactory.getLogger(RouModelMetadataQueryService.class);

    private final RouModelMetadataRepository rouModelMetadataRepository;

    private final RouModelMetadataMapper rouModelMetadataMapper;

    private final RouModelMetadataSearchRepository rouModelMetadataSearchRepository;

    public RouModelMetadataQueryService(
        RouModelMetadataRepository rouModelMetadataRepository,
        RouModelMetadataMapper rouModelMetadataMapper,
        RouModelMetadataSearchRepository rouModelMetadataSearchRepository
    ) {
        this.rouModelMetadataRepository = rouModelMetadataRepository;
        this.rouModelMetadataMapper = rouModelMetadataMapper;
        this.rouModelMetadataSearchRepository = rouModelMetadataSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RouModelMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RouModelMetadataDTO> findByCriteria(RouModelMetadataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RouModelMetadata> specification = createSpecification(criteria);
        return rouModelMetadataMapper.toDto(rouModelMetadataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RouModelMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RouModelMetadataDTO> findByCriteria(RouModelMetadataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RouModelMetadata> specification = createSpecification(criteria);
        return rouModelMetadataRepository.findAll(specification, page).map(rouModelMetadataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RouModelMetadataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RouModelMetadata> specification = createSpecification(criteria);
        return rouModelMetadataRepository.count(specification);
    }

    /**
     * Function to convert {@link RouModelMetadataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RouModelMetadata> createSpecification(RouModelMetadataCriteria criteria) {
        Specification<RouModelMetadata> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RouModelMetadata_.id));
            }
            if (criteria.getModelTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelTitle(), RouModelMetadata_.modelTitle));
            }
            if (criteria.getModelVersion() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getModelVersion(), RouModelMetadata_.modelVersion));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), RouModelMetadata_.description));
            }
            if (criteria.getLeaseTermPeriods() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLeaseTermPeriods(), RouModelMetadata_.leaseTermPeriods));
            }
            if (criteria.getLeaseAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLeaseAmount(), RouModelMetadata_.leaseAmount));
            }
            if (criteria.getRouModelReference() != null) {
                specification = specification.and(buildSpecification(criteria.getRouModelReference(), RouModelMetadata_.rouModelReference));
            }
            if (criteria.getCommencementDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCommencementDate(), RouModelMetadata_.commencementDate));
            }
            if (criteria.getExpirationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpirationDate(), RouModelMetadata_.expirationDate));
            }
            if (criteria.getHasBeenFullyAmortised() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getHasBeenFullyAmortised(), RouModelMetadata_.hasBeenFullyAmortised));
            }
            if (criteria.getHasBeenDecommissioned() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getHasBeenDecommissioned(), RouModelMetadata_.hasBeenDecommissioned));
            }
            if (criteria.getIfrs16LeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIfrs16LeaseContractId(),
                            root -> root.join(RouModelMetadata_.ifrs16LeaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
            if (criteria.getAssetAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetAccountId(),
                            root -> root.join(RouModelMetadata_.assetAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getDepreciationAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationAccountId(),
                            root -> root.join(RouModelMetadata_.depreciationAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getAccruedDepreciationAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAccruedDepreciationAccountId(),
                            root -> root.join(RouModelMetadata_.accruedDepreciationAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getAssetCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetCategoryId(),
                            root -> root.join(RouModelMetadata_.assetCategory, JoinType.LEFT).get(AssetCategory_.id)
                        )
                    );
            }
            if (criteria.getDocumentAttachmentsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDocumentAttachmentsId(),
                            root -> root.join(RouModelMetadata_.documentAttachments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
