package io.github.erp.service;

/*-
 * Erp System - Mark X No 6 (Jehoiada Series) Server ver 1.7.6
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
import io.github.erp.domain.CountySubCountyCode;
import io.github.erp.repository.CountySubCountyCodeRepository;
import io.github.erp.repository.search.CountySubCountyCodeSearchRepository;
import io.github.erp.service.criteria.CountySubCountyCodeCriteria;
import io.github.erp.service.dto.CountySubCountyCodeDTO;
import io.github.erp.service.mapper.CountySubCountyCodeMapper;
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
 * Service for executing complex queries for {@link CountySubCountyCode} entities in the database.
 * The main input is a {@link CountySubCountyCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountySubCountyCodeDTO} or a {@link Page} of {@link CountySubCountyCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountySubCountyCodeQueryService extends QueryService<CountySubCountyCode> {

    private final Logger log = LoggerFactory.getLogger(CountySubCountyCodeQueryService.class);

    private final CountySubCountyCodeRepository countySubCountyCodeRepository;

    private final CountySubCountyCodeMapper countySubCountyCodeMapper;

    private final CountySubCountyCodeSearchRepository countySubCountyCodeSearchRepository;

    public CountySubCountyCodeQueryService(
        CountySubCountyCodeRepository countySubCountyCodeRepository,
        CountySubCountyCodeMapper countySubCountyCodeMapper,
        CountySubCountyCodeSearchRepository countySubCountyCodeSearchRepository
    ) {
        this.countySubCountyCodeRepository = countySubCountyCodeRepository;
        this.countySubCountyCodeMapper = countySubCountyCodeMapper;
        this.countySubCountyCodeSearchRepository = countySubCountyCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CountySubCountyCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountySubCountyCodeDTO> findByCriteria(CountySubCountyCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CountySubCountyCode> specification = createSpecification(criteria);
        return countySubCountyCodeMapper.toDto(countySubCountyCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CountySubCountyCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountySubCountyCodeDTO> findByCriteria(CountySubCountyCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CountySubCountyCode> specification = createSpecification(criteria);
        return countySubCountyCodeRepository.findAll(specification, page).map(countySubCountyCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountySubCountyCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CountySubCountyCode> specification = createSpecification(criteria);
        return countySubCountyCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link CountySubCountyCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CountySubCountyCode> createSpecification(CountySubCountyCodeCriteria criteria) {
        Specification<CountySubCountyCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CountySubCountyCode_.id));
            }
            if (criteria.getSubCountyCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSubCountyCode(), CountySubCountyCode_.subCountyCode));
            }
            if (criteria.getSubCountyName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSubCountyName(), CountySubCountyCode_.subCountyName));
            }
            if (criteria.getCountyCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountyCode(), CountySubCountyCode_.countyCode));
            }
            if (criteria.getCountyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountyName(), CountySubCountyCode_.countyName));
            }
        }
        return specification;
    }
}
