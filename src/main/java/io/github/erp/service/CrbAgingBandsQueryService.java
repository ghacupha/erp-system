package io.github.erp.service;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.domain.CrbAgingBands;
import io.github.erp.repository.CrbAgingBandsRepository;
import io.github.erp.repository.search.CrbAgingBandsSearchRepository;
import io.github.erp.service.criteria.CrbAgingBandsCriteria;
import io.github.erp.service.dto.CrbAgingBandsDTO;
import io.github.erp.service.mapper.CrbAgingBandsMapper;
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
 * Service for executing complex queries for {@link CrbAgingBands} entities in the database.
 * The main input is a {@link CrbAgingBandsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbAgingBandsDTO} or a {@link Page} of {@link CrbAgingBandsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbAgingBandsQueryService extends QueryService<CrbAgingBands> {

    private final Logger log = LoggerFactory.getLogger(CrbAgingBandsQueryService.class);

    private final CrbAgingBandsRepository crbAgingBandsRepository;

    private final CrbAgingBandsMapper crbAgingBandsMapper;

    private final CrbAgingBandsSearchRepository crbAgingBandsSearchRepository;

    public CrbAgingBandsQueryService(
        CrbAgingBandsRepository crbAgingBandsRepository,
        CrbAgingBandsMapper crbAgingBandsMapper,
        CrbAgingBandsSearchRepository crbAgingBandsSearchRepository
    ) {
        this.crbAgingBandsRepository = crbAgingBandsRepository;
        this.crbAgingBandsMapper = crbAgingBandsMapper;
        this.crbAgingBandsSearchRepository = crbAgingBandsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbAgingBandsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbAgingBandsDTO> findByCriteria(CrbAgingBandsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbAgingBands> specification = createSpecification(criteria);
        return crbAgingBandsMapper.toDto(crbAgingBandsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbAgingBandsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbAgingBandsDTO> findByCriteria(CrbAgingBandsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbAgingBands> specification = createSpecification(criteria);
        return crbAgingBandsRepository.findAll(specification, page).map(crbAgingBandsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbAgingBandsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbAgingBands> specification = createSpecification(criteria);
        return crbAgingBandsRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbAgingBandsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbAgingBands> createSpecification(CrbAgingBandsCriteria criteria) {
        Specification<CrbAgingBands> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbAgingBands_.id));
            }
            if (criteria.getAgingBandCategoryCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAgingBandCategoryCode(), CrbAgingBands_.agingBandCategoryCode));
            }
            if (criteria.getAgingBandCategory() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAgingBandCategory(), CrbAgingBands_.agingBandCategory));
            }
            if (criteria.getAgingBandCategoryDetails() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAgingBandCategoryDetails(), CrbAgingBands_.agingBandCategoryDetails)
                    );
            }
        }
        return specification;
    }
}
