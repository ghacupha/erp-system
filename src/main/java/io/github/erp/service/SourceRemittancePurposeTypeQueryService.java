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
import io.github.erp.domain.SourceRemittancePurposeType;
import io.github.erp.repository.SourceRemittancePurposeTypeRepository;
import io.github.erp.repository.search.SourceRemittancePurposeTypeSearchRepository;
import io.github.erp.service.criteria.SourceRemittancePurposeTypeCriteria;
import io.github.erp.service.dto.SourceRemittancePurposeTypeDTO;
import io.github.erp.service.mapper.SourceRemittancePurposeTypeMapper;
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
 * Service for executing complex queries for {@link SourceRemittancePurposeType} entities in the database.
 * The main input is a {@link SourceRemittancePurposeTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SourceRemittancePurposeTypeDTO} or a {@link Page} of {@link SourceRemittancePurposeTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SourceRemittancePurposeTypeQueryService extends QueryService<SourceRemittancePurposeType> {

    private final Logger log = LoggerFactory.getLogger(SourceRemittancePurposeTypeQueryService.class);

    private final SourceRemittancePurposeTypeRepository sourceRemittancePurposeTypeRepository;

    private final SourceRemittancePurposeTypeMapper sourceRemittancePurposeTypeMapper;

    private final SourceRemittancePurposeTypeSearchRepository sourceRemittancePurposeTypeSearchRepository;

    public SourceRemittancePurposeTypeQueryService(
        SourceRemittancePurposeTypeRepository sourceRemittancePurposeTypeRepository,
        SourceRemittancePurposeTypeMapper sourceRemittancePurposeTypeMapper,
        SourceRemittancePurposeTypeSearchRepository sourceRemittancePurposeTypeSearchRepository
    ) {
        this.sourceRemittancePurposeTypeRepository = sourceRemittancePurposeTypeRepository;
        this.sourceRemittancePurposeTypeMapper = sourceRemittancePurposeTypeMapper;
        this.sourceRemittancePurposeTypeSearchRepository = sourceRemittancePurposeTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SourceRemittancePurposeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SourceRemittancePurposeTypeDTO> findByCriteria(SourceRemittancePurposeTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SourceRemittancePurposeType> specification = createSpecification(criteria);
        return sourceRemittancePurposeTypeMapper.toDto(sourceRemittancePurposeTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SourceRemittancePurposeTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SourceRemittancePurposeTypeDTO> findByCriteria(SourceRemittancePurposeTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SourceRemittancePurposeType> specification = createSpecification(criteria);
        return sourceRemittancePurposeTypeRepository.findAll(specification, page).map(sourceRemittancePurposeTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SourceRemittancePurposeTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SourceRemittancePurposeType> specification = createSpecification(criteria);
        return sourceRemittancePurposeTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link SourceRemittancePurposeTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SourceRemittancePurposeType> createSpecification(SourceRemittancePurposeTypeCriteria criteria) {
        Specification<SourceRemittancePurposeType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SourceRemittancePurposeType_.id));
            }
            if (criteria.getSourceOrPurposeTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSourceOrPurposeTypeCode(),
                            SourceRemittancePurposeType_.sourceOrPurposeTypeCode
                        )
                    );
            }
            if (criteria.getSourceOrPurposeOfRemittanceFlag() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSourceOrPurposeOfRemittanceFlag(),
                            SourceRemittancePurposeType_.sourceOrPurposeOfRemittanceFlag
                        )
                    );
            }
            if (criteria.getSourceOrPurposeOfRemittanceType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSourceOrPurposeOfRemittanceType(),
                            SourceRemittancePurposeType_.sourceOrPurposeOfRemittanceType
                        )
                    );
            }
        }
        return specification;
    }
}
