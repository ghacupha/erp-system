package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.GdiMasterDataIndex;
import io.github.erp.repository.GdiMasterDataIndexRepository;
import io.github.erp.repository.search.GdiMasterDataIndexSearchRepository;
import io.github.erp.service.criteria.GdiMasterDataIndexCriteria;
import io.github.erp.service.dto.GdiMasterDataIndexDTO;
import io.github.erp.service.mapper.GdiMasterDataIndexMapper;
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
 * Service for executing complex queries for {@link GdiMasterDataIndex} entities in the database.
 * The main input is a {@link GdiMasterDataIndexCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GdiMasterDataIndexDTO} or a {@link Page} of {@link GdiMasterDataIndexDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GdiMasterDataIndexQueryService extends QueryService<GdiMasterDataIndex> {

    private final Logger log = LoggerFactory.getLogger(GdiMasterDataIndexQueryService.class);

    private final GdiMasterDataIndexRepository gdiMasterDataIndexRepository;

    private final GdiMasterDataIndexMapper gdiMasterDataIndexMapper;

    private final GdiMasterDataIndexSearchRepository gdiMasterDataIndexSearchRepository;

    public GdiMasterDataIndexQueryService(
        GdiMasterDataIndexRepository gdiMasterDataIndexRepository,
        GdiMasterDataIndexMapper gdiMasterDataIndexMapper,
        GdiMasterDataIndexSearchRepository gdiMasterDataIndexSearchRepository
    ) {
        this.gdiMasterDataIndexRepository = gdiMasterDataIndexRepository;
        this.gdiMasterDataIndexMapper = gdiMasterDataIndexMapper;
        this.gdiMasterDataIndexSearchRepository = gdiMasterDataIndexSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GdiMasterDataIndexDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GdiMasterDataIndexDTO> findByCriteria(GdiMasterDataIndexCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GdiMasterDataIndex> specification = createSpecification(criteria);
        return gdiMasterDataIndexMapper.toDto(gdiMasterDataIndexRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GdiMasterDataIndexDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GdiMasterDataIndexDTO> findByCriteria(GdiMasterDataIndexCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GdiMasterDataIndex> specification = createSpecification(criteria);
        return gdiMasterDataIndexRepository.findAll(specification, page).map(gdiMasterDataIndexMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GdiMasterDataIndexCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GdiMasterDataIndex> specification = createSpecification(criteria);
        return gdiMasterDataIndexRepository.count(specification);
    }

    /**
     * Function to convert {@link GdiMasterDataIndexCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GdiMasterDataIndex> createSpecification(GdiMasterDataIndexCriteria criteria) {
        Specification<GdiMasterDataIndex> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GdiMasterDataIndex_.id));
            }
            if (criteria.getEntityName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEntityName(), GdiMasterDataIndex_.entityName));
            }
            if (criteria.getDatabaseName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDatabaseName(), GdiMasterDataIndex_.databaseName));
            }
            if (criteria.getDataPath() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDataPath(), GdiMasterDataIndex_.dataPath));
            }
        }
        return specification;
    }
}
