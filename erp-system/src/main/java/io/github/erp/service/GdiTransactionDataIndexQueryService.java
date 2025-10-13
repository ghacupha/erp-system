package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.GdiTransactionDataIndex;
import io.github.erp.repository.GdiTransactionDataIndexRepository;
import io.github.erp.repository.search.GdiTransactionDataIndexSearchRepository;
import io.github.erp.service.criteria.GdiTransactionDataIndexCriteria;
import io.github.erp.service.dto.GdiTransactionDataIndexDTO;
import io.github.erp.service.mapper.GdiTransactionDataIndexMapper;
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
 * Service for executing complex queries for {@link GdiTransactionDataIndex} entities in the database.
 * The main input is a {@link GdiTransactionDataIndexCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GdiTransactionDataIndexDTO} or a {@link Page} of {@link GdiTransactionDataIndexDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GdiTransactionDataIndexQueryService extends QueryService<GdiTransactionDataIndex> {

    private final Logger log = LoggerFactory.getLogger(GdiTransactionDataIndexQueryService.class);

    private final GdiTransactionDataIndexRepository gdiTransactionDataIndexRepository;

    private final GdiTransactionDataIndexMapper gdiTransactionDataIndexMapper;

    private final GdiTransactionDataIndexSearchRepository gdiTransactionDataIndexSearchRepository;

    public GdiTransactionDataIndexQueryService(
        GdiTransactionDataIndexRepository gdiTransactionDataIndexRepository,
        GdiTransactionDataIndexMapper gdiTransactionDataIndexMapper,
        GdiTransactionDataIndexSearchRepository gdiTransactionDataIndexSearchRepository
    ) {
        this.gdiTransactionDataIndexRepository = gdiTransactionDataIndexRepository;
        this.gdiTransactionDataIndexMapper = gdiTransactionDataIndexMapper;
        this.gdiTransactionDataIndexSearchRepository = gdiTransactionDataIndexSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GdiTransactionDataIndexDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GdiTransactionDataIndexDTO> findByCriteria(GdiTransactionDataIndexCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GdiTransactionDataIndex> specification = createSpecification(criteria);
        return gdiTransactionDataIndexMapper.toDto(gdiTransactionDataIndexRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GdiTransactionDataIndexDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GdiTransactionDataIndexDTO> findByCriteria(GdiTransactionDataIndexCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GdiTransactionDataIndex> specification = createSpecification(criteria);
        return gdiTransactionDataIndexRepository.findAll(specification, page).map(gdiTransactionDataIndexMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GdiTransactionDataIndexCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GdiTransactionDataIndex> specification = createSpecification(criteria);
        return gdiTransactionDataIndexRepository.count(specification);
    }

    /**
     * Function to convert {@link GdiTransactionDataIndexCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GdiTransactionDataIndex> createSpecification(GdiTransactionDataIndexCriteria criteria) {
        Specification<GdiTransactionDataIndex> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GdiTransactionDataIndex_.id));
            }
            if (criteria.getDatasetName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDatasetName(), GdiTransactionDataIndex_.datasetName));
            }
            if (criteria.getDatabaseName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDatabaseName(), GdiTransactionDataIndex_.databaseName));
            }
            if (criteria.getUpdateFrequency() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getUpdateFrequency(), GdiTransactionDataIndex_.updateFrequency));
            }
            if (criteria.getDatasetBehavior() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getDatasetBehavior(), GdiTransactionDataIndex_.datasetBehavior));
            }
            if (criteria.getMinimumDataRowsPerRequest() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getMinimumDataRowsPerRequest(), GdiTransactionDataIndex_.minimumDataRowsPerRequest)
                    );
            }
            if (criteria.getMaximumDataRowsPerRequest() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getMaximumDataRowsPerRequest(), GdiTransactionDataIndex_.maximumDataRowsPerRequest)
                    );
            }
            if (criteria.getDataPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataPath(), GdiTransactionDataIndex_.dataPath));
            }
            if (criteria.getMasterDataItemId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMasterDataItemId(),
                            root -> root.join(GdiTransactionDataIndex_.masterDataItems, JoinType.LEFT).get(GdiMasterDataIndex_.id)
                        )
                    );
            }
            if (criteria.getBusinessTeamId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessTeamId(),
                            root -> root.join(GdiTransactionDataIndex_.businessTeam, JoinType.LEFT).get(BusinessTeam_.id)
                        )
                    );
            }
            if (criteria.getDataSetTemplateId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDataSetTemplateId(),
                            root -> root.join(GdiTransactionDataIndex_.dataSetTemplate, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(GdiTransactionDataIndex_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
