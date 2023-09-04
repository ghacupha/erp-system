package io.github.erp.service;

/*-
 * Erp System - Mark IV No 6 (Ehud Series) Server ver 1.4.0
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
import io.github.erp.domain.FixedAssetDepreciation;
import io.github.erp.repository.FixedAssetDepreciationRepository;
import io.github.erp.repository.search.FixedAssetDepreciationSearchRepository;
import io.github.erp.service.criteria.FixedAssetDepreciationCriteria;
import io.github.erp.service.dto.FixedAssetDepreciationDTO;
import io.github.erp.service.mapper.FixedAssetDepreciationMapper;
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
 * Service for executing complex queries for {@link FixedAssetDepreciation} entities in the database.
 * The main input is a {@link FixedAssetDepreciationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FixedAssetDepreciationDTO} or a {@link Page} of {@link FixedAssetDepreciationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FixedAssetDepreciationQueryService extends QueryService<FixedAssetDepreciation> {

    private final Logger log = LoggerFactory.getLogger(FixedAssetDepreciationQueryService.class);

    private final FixedAssetDepreciationRepository fixedAssetDepreciationRepository;

    private final FixedAssetDepreciationMapper fixedAssetDepreciationMapper;

    private final FixedAssetDepreciationSearchRepository fixedAssetDepreciationSearchRepository;

    public FixedAssetDepreciationQueryService(
        FixedAssetDepreciationRepository fixedAssetDepreciationRepository,
        FixedAssetDepreciationMapper fixedAssetDepreciationMapper,
        FixedAssetDepreciationSearchRepository fixedAssetDepreciationSearchRepository
    ) {
        this.fixedAssetDepreciationRepository = fixedAssetDepreciationRepository;
        this.fixedAssetDepreciationMapper = fixedAssetDepreciationMapper;
        this.fixedAssetDepreciationSearchRepository = fixedAssetDepreciationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link FixedAssetDepreciationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FixedAssetDepreciationDTO> findByCriteria(FixedAssetDepreciationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<FixedAssetDepreciation> specification = createSpecification(criteria);
        return fixedAssetDepreciationMapper.toDto(fixedAssetDepreciationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FixedAssetDepreciationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FixedAssetDepreciationDTO> findByCriteria(FixedAssetDepreciationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<FixedAssetDepreciation> specification = createSpecification(criteria);
        return fixedAssetDepreciationRepository.findAll(specification, page).map(fixedAssetDepreciationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FixedAssetDepreciationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<FixedAssetDepreciation> specification = createSpecification(criteria);
        return fixedAssetDepreciationRepository.count(specification);
    }

    /**
     * Function to convert {@link FixedAssetDepreciationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<FixedAssetDepreciation> createSpecification(FixedAssetDepreciationCriteria criteria) {
        Specification<FixedAssetDepreciation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), FixedAssetDepreciation_.id));
            }
            if (criteria.getAssetNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetNumber(), FixedAssetDepreciation_.assetNumber));
            }
            if (criteria.getServiceOutletCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getServiceOutletCode(), FixedAssetDepreciation_.serviceOutletCode));
            }
            if (criteria.getAssetTag() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAssetTag(), FixedAssetDepreciation_.assetTag));
            }
            if (criteria.getAssetDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetDescription(), FixedAssetDepreciation_.assetDescription));
            }
            if (criteria.getDepreciationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDepreciationDate(), FixedAssetDepreciation_.depreciationDate));
            }
            if (criteria.getAssetCategory() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetCategory(), FixedAssetDepreciation_.assetCategory));
            }
            if (criteria.getDepreciationAmount() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getDepreciationAmount(), FixedAssetDepreciation_.depreciationAmount)
                    );
            }
            if (criteria.getDepreciationRegime() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getDepreciationRegime(), FixedAssetDepreciation_.depreciationRegime));
            }
            if (criteria.getFileUploadToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getFileUploadToken(), FixedAssetDepreciation_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCompilationToken(), FixedAssetDepreciation_.compilationToken));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(FixedAssetDepreciation_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
