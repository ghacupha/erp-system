package io.github.erp.service;

/*-
 * Erp System - Mark IV No 4 (Ehud Series) Server ver 1.3.4
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
import io.github.erp.domain.DepreciationEntry;
import io.github.erp.repository.DepreciationEntryRepository;
import io.github.erp.repository.search.DepreciationEntrySearchRepository;
import io.github.erp.service.criteria.DepreciationEntryCriteria;
import io.github.erp.service.dto.DepreciationEntryDTO;
import io.github.erp.service.mapper.DepreciationEntryMapper;
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
 * Service for executing complex queries for {@link DepreciationEntry} entities in the database.
 * The main input is a {@link DepreciationEntryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepreciationEntryDTO} or a {@link Page} of {@link DepreciationEntryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepreciationEntryQueryService extends QueryService<DepreciationEntry> {

    private final Logger log = LoggerFactory.getLogger(DepreciationEntryQueryService.class);

    private final DepreciationEntryRepository depreciationEntryRepository;

    private final DepreciationEntryMapper depreciationEntryMapper;

    private final DepreciationEntrySearchRepository depreciationEntrySearchRepository;

    public DepreciationEntryQueryService(
        DepreciationEntryRepository depreciationEntryRepository,
        DepreciationEntryMapper depreciationEntryMapper,
        DepreciationEntrySearchRepository depreciationEntrySearchRepository
    ) {
        this.depreciationEntryRepository = depreciationEntryRepository;
        this.depreciationEntryMapper = depreciationEntryMapper;
        this.depreciationEntrySearchRepository = depreciationEntrySearchRepository;
    }

    /**
     * Return a {@link List} of {@link DepreciationEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepreciationEntryDTO> findByCriteria(DepreciationEntryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DepreciationEntry> specification = createSpecification(criteria);
        return depreciationEntryMapper.toDto(depreciationEntryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepreciationEntryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepreciationEntryDTO> findByCriteria(DepreciationEntryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepreciationEntry> specification = createSpecification(criteria);
        return depreciationEntryRepository.findAll(specification, page).map(depreciationEntryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepreciationEntryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DepreciationEntry> specification = createSpecification(criteria);
        return depreciationEntryRepository.count(specification);
    }

    /**
     * Function to convert {@link DepreciationEntryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepreciationEntry> createSpecification(DepreciationEntryCriteria criteria) {
        Specification<DepreciationEntry> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DepreciationEntry_.id));
            }
            if (criteria.getPostedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPostedAt(), DepreciationEntry_.postedAt));
            }
            if (criteria.getDepreciationAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getDepreciationAmount(), DepreciationEntry_.depreciationAmount));
            }
            if (criteria.getAssetNumber() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssetNumber(), DepreciationEntry_.assetNumber));
            }
            if (criteria.getServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceOutletId(),
                            root -> root.join(DepreciationEntry_.serviceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getAssetCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetCategoryId(),
                            root -> root.join(DepreciationEntry_.assetCategory, JoinType.LEFT).get(AssetCategory_.id)
                        )
                    );
            }
            if (criteria.getDepreciationMethodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationMethodId(),
                            root -> root.join(DepreciationEntry_.depreciationMethod, JoinType.LEFT).get(DepreciationMethod_.id)
                        )
                    );
            }
            if (criteria.getAssetRegistrationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetRegistrationId(),
                            root -> root.join(DepreciationEntry_.assetRegistration, JoinType.LEFT).get(AssetRegistration_.id)
                        )
                    );
            }
            if (criteria.getDepreciationPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationPeriodId(),
                            root -> root.join(DepreciationEntry_.depreciationPeriod, JoinType.LEFT).get(DepreciationPeriod_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
