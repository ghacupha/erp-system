package io.github.erp.service;

/*-
 * Erp System - Mark VIII No 2 (Hilkiah Series) Server ver 1.6.1
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
import io.github.erp.domain.CrbSourceOfInformationType;
import io.github.erp.repository.CrbSourceOfInformationTypeRepository;
import io.github.erp.repository.search.CrbSourceOfInformationTypeSearchRepository;
import io.github.erp.service.criteria.CrbSourceOfInformationTypeCriteria;
import io.github.erp.service.dto.CrbSourceOfInformationTypeDTO;
import io.github.erp.service.mapper.CrbSourceOfInformationTypeMapper;
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
 * Service for executing complex queries for {@link CrbSourceOfInformationType} entities in the database.
 * The main input is a {@link CrbSourceOfInformationTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbSourceOfInformationTypeDTO} or a {@link Page} of {@link CrbSourceOfInformationTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbSourceOfInformationTypeQueryService extends QueryService<CrbSourceOfInformationType> {

    private final Logger log = LoggerFactory.getLogger(CrbSourceOfInformationTypeQueryService.class);

    private final CrbSourceOfInformationTypeRepository crbSourceOfInformationTypeRepository;

    private final CrbSourceOfInformationTypeMapper crbSourceOfInformationTypeMapper;

    private final CrbSourceOfInformationTypeSearchRepository crbSourceOfInformationTypeSearchRepository;

    public CrbSourceOfInformationTypeQueryService(
        CrbSourceOfInformationTypeRepository crbSourceOfInformationTypeRepository,
        CrbSourceOfInformationTypeMapper crbSourceOfInformationTypeMapper,
        CrbSourceOfInformationTypeSearchRepository crbSourceOfInformationTypeSearchRepository
    ) {
        this.crbSourceOfInformationTypeRepository = crbSourceOfInformationTypeRepository;
        this.crbSourceOfInformationTypeMapper = crbSourceOfInformationTypeMapper;
        this.crbSourceOfInformationTypeSearchRepository = crbSourceOfInformationTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbSourceOfInformationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbSourceOfInformationTypeDTO> findByCriteria(CrbSourceOfInformationTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbSourceOfInformationType> specification = createSpecification(criteria);
        return crbSourceOfInformationTypeMapper.toDto(crbSourceOfInformationTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbSourceOfInformationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbSourceOfInformationTypeDTO> findByCriteria(CrbSourceOfInformationTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbSourceOfInformationType> specification = createSpecification(criteria);
        return crbSourceOfInformationTypeRepository.findAll(specification, page).map(crbSourceOfInformationTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbSourceOfInformationTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbSourceOfInformationType> specification = createSpecification(criteria);
        return crbSourceOfInformationTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbSourceOfInformationTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbSourceOfInformationType> createSpecification(CrbSourceOfInformationTypeCriteria criteria) {
        Specification<CrbSourceOfInformationType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbSourceOfInformationType_.id));
            }
            if (criteria.getSourceOfInformationTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSourceOfInformationTypeCode(),
                            CrbSourceOfInformationType_.sourceOfInformationTypeCode
                        )
                    );
            }
            if (criteria.getSourceOfInformationTypeDescription() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSourceOfInformationTypeDescription(),
                            CrbSourceOfInformationType_.sourceOfInformationTypeDescription
                        )
                    );
            }
        }
        return specification;
    }
}
