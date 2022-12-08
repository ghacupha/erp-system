package io.github.erp.service;

/*-
 * Erp System - Mark III No 4 (Caleb Series) Server ver 0.1.4-SNAPSHOT
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.FixedAssetAcquisition;
import io.github.erp.repository.FixedAssetAcquisitionRepository;
import io.github.erp.repository.search.FixedAssetAcquisitionSearchRepository;
import io.github.erp.service.criteria.FixedAssetAcquisitionCriteria;
import io.github.erp.service.dto.FixedAssetAcquisitionDTO;
import io.github.erp.service.mapper.FixedAssetAcquisitionMapper;
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
 * Service for executing complex queries for {@link FixedAssetAcquisition} entities in the database.
 * The main input is a {@link FixedAssetAcquisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FixedAssetAcquisitionDTO} or a {@link Page} of {@link FixedAssetAcquisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FixedAssetAcquisitionQueryService extends QueryService<FixedAssetAcquisition> {

    private final Logger log = LoggerFactory.getLogger(FixedAssetAcquisitionQueryService.class);

    private final FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository;

    private final FixedAssetAcquisitionMapper fixedAssetAcquisitionMapper;

    private final FixedAssetAcquisitionSearchRepository fixedAssetAcquisitionSearchRepository;

    public FixedAssetAcquisitionQueryService(
        FixedAssetAcquisitionRepository fixedAssetAcquisitionRepository,
        FixedAssetAcquisitionMapper fixedAssetAcquisitionMapper,
        FixedAssetAcquisitionSearchRepository fixedAssetAcquisitionSearchRepository
    ) {
        this.fixedAssetAcquisitionRepository = fixedAssetAcquisitionRepository;
        this.fixedAssetAcquisitionMapper = fixedAssetAcquisitionMapper;
        this.fixedAssetAcquisitionSearchRepository = fixedAssetAcquisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FixedAssetAcquisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FixedAssetAcquisitionDTO> findByCriteria(FixedAssetAcquisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FixedAssetAcquisition> specification = createSpecification(criteria);
        return fixedAssetAcquisitionMapper.toDto(fixedAssetAcquisitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FixedAssetAcquisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FixedAssetAcquisitionDTO> findByCriteria(FixedAssetAcquisitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FixedAssetAcquisition> specification = createSpecification(criteria);
        return fixedAssetAcquisitionRepository.findAll(specification, page).map(fixedAssetAcquisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FixedAssetAcquisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FixedAssetAcquisition> specification = createSpecification(criteria);
        return fixedAssetAcquisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link FixedAssetAcquisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FixedAssetAcquisition> createSpecification(FixedAssetAcquisitionCriteria criteria) {
        Specification<FixedAssetAcquisition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FixedAssetAcquisition_.id));
            }
            if (criteria.getAssetNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetNumber(), FixedAssetAcquisition_.assetNumber));
            }
            if (criteria.getServiceOutletCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getServiceOutletCode(), FixedAssetAcquisition_.serviceOutletCode));
            }
            if (criteria.getAssetTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetTag(), FixedAssetAcquisition_.assetTag));
            }
            if (criteria.getAssetDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetDescription(), FixedAssetAcquisition_.assetDescription));
            }
            if (criteria.getPurchaseDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPurchaseDate(), FixedAssetAcquisition_.purchaseDate));
            }
            if (criteria.getAssetCategory() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetCategory(), FixedAssetAcquisition_.assetCategory));
            }
            if (criteria.getPurchasePrice() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPurchasePrice(), FixedAssetAcquisition_.purchasePrice));
            }
            if (criteria.getFileUploadToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileUploadToken(), FixedAssetAcquisition_.fileUploadToken));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(FixedAssetAcquisition_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
