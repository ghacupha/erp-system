package io.github.erp.service;

/*-
 * Erp System - Mark X No 3 (Jehoiada Series) Server ver 1.7.3
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
import io.github.erp.domain.CrbGlCode;
import io.github.erp.repository.CrbGlCodeRepository;
import io.github.erp.repository.search.CrbGlCodeSearchRepository;
import io.github.erp.service.criteria.CrbGlCodeCriteria;
import io.github.erp.service.dto.CrbGlCodeDTO;
import io.github.erp.service.mapper.CrbGlCodeMapper;
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
 * Service for executing complex queries for {@link CrbGlCode} entities in the database.
 * The main input is a {@link CrbGlCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbGlCodeDTO} or a {@link Page} of {@link CrbGlCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbGlCodeQueryService extends QueryService<CrbGlCode> {

    private final Logger log = LoggerFactory.getLogger(CrbGlCodeQueryService.class);

    private final CrbGlCodeRepository crbGlCodeRepository;

    private final CrbGlCodeMapper crbGlCodeMapper;

    private final CrbGlCodeSearchRepository crbGlCodeSearchRepository;

    public CrbGlCodeQueryService(
        CrbGlCodeRepository crbGlCodeRepository,
        CrbGlCodeMapper crbGlCodeMapper,
        CrbGlCodeSearchRepository crbGlCodeSearchRepository
    ) {
        this.crbGlCodeRepository = crbGlCodeRepository;
        this.crbGlCodeMapper = crbGlCodeMapper;
        this.crbGlCodeSearchRepository = crbGlCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbGlCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbGlCodeDTO> findByCriteria(CrbGlCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbGlCode> specification = createSpecification(criteria);
        return crbGlCodeMapper.toDto(crbGlCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbGlCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbGlCodeDTO> findByCriteria(CrbGlCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbGlCode> specification = createSpecification(criteria);
        return crbGlCodeRepository.findAll(specification, page).map(crbGlCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbGlCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbGlCode> specification = createSpecification(criteria);
        return crbGlCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbGlCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbGlCode> createSpecification(CrbGlCodeCriteria criteria) {
        Specification<CrbGlCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbGlCode_.id));
            }
            if (criteria.getGlCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGlCode(), CrbGlCode_.glCode));
            }
            if (criteria.getGlDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGlDescription(), CrbGlCode_.glDescription));
            }
            if (criteria.getGlType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGlType(), CrbGlCode_.glType));
            }
            if (criteria.getInstitutionCategory() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInstitutionCategory(), CrbGlCode_.institutionCategory));
            }
        }
        return specification;
    }
}
