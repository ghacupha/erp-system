package io.github.erp.service;

/*-
 * Erp System - Mark IV No 1 (Ehud Series) Server ver 1.3.0
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.AssetWarranty;
import io.github.erp.repository.AssetWarrantyRepository;
import io.github.erp.repository.search.AssetWarrantySearchRepository;
import io.github.erp.service.criteria.AssetWarrantyCriteria;
import io.github.erp.service.dto.AssetWarrantyDTO;
import io.github.erp.service.mapper.AssetWarrantyMapper;
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
 * Service for executing complex queries for {@link AssetWarranty} entities in the database.
 * The main input is a {@link AssetWarrantyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link AssetWarrantyDTO} or a {@link Page} of {@link AssetWarrantyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class AssetWarrantyQueryService extends QueryService<AssetWarranty> {

    private final Logger log = LoggerFactory.getLogger(AssetWarrantyQueryService.class);

    private final AssetWarrantyRepository assetWarrantyRepository;

    private final AssetWarrantyMapper assetWarrantyMapper;

    private final AssetWarrantySearchRepository assetWarrantySearchRepository;

    public AssetWarrantyQueryService(
        AssetWarrantyRepository assetWarrantyRepository,
        AssetWarrantyMapper assetWarrantyMapper,
        AssetWarrantySearchRepository assetWarrantySearchRepository
    ) {
        this.assetWarrantyRepository = assetWarrantyRepository;
        this.assetWarrantyMapper = assetWarrantyMapper;
        this.assetWarrantySearchRepository = assetWarrantySearchRepository;
    }

    /**
     * Return a {@link List} of {@link AssetWarrantyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<AssetWarrantyDTO> findByCriteria(AssetWarrantyCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<AssetWarranty> specification = createSpecification(criteria);
        return assetWarrantyMapper.toDto(assetWarrantyRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link AssetWarrantyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<AssetWarrantyDTO> findByCriteria(AssetWarrantyCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<AssetWarranty> specification = createSpecification(criteria);
        return assetWarrantyRepository.findAll(specification, page).map(assetWarrantyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(AssetWarrantyCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<AssetWarranty> specification = createSpecification(criteria);
        return assetWarrantyRepository.count(specification);
    }

    /**
     * Function to convert {@link AssetWarrantyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<AssetWarranty> createSpecification(AssetWarrantyCriteria criteria) {
        Specification<AssetWarranty> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), AssetWarranty_.id));
            }
            if (criteria.getAssetTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetTag(), AssetWarranty_.assetTag));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), AssetWarranty_.description));
            }
            if (criteria.getModelNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getModelNumber(), AssetWarranty_.modelNumber));
            }
            if (criteria.getSerialNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerialNumber(), AssetWarranty_.serialNumber));
            }
            if (criteria.getExpiryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpiryDate(), AssetWarranty_.expiryDate));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(AssetWarranty_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getUniversallyUniqueMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getUniversallyUniqueMappingId(),
                            root -> root.join(AssetWarranty_.universallyUniqueMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDealerId(), root -> root.join(AssetWarranty_.dealer, JoinType.LEFT).get(Dealer_.id))
                    );
            }
            if (criteria.getWarrantyAttachmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWarrantyAttachmentId(),
                            root -> root.join(AssetWarranty_.warrantyAttachments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
