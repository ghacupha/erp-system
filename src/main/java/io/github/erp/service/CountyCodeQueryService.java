package io.github.erp.service;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0-SNAPSHOT
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
import io.github.erp.domain.CountyCode;
import io.github.erp.repository.CountyCodeRepository;
import io.github.erp.repository.search.CountyCodeSearchRepository;
import io.github.erp.service.criteria.CountyCodeCriteria;
import io.github.erp.service.dto.CountyCodeDTO;
import io.github.erp.service.mapper.CountyCodeMapper;
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
 * Service for executing complex queries for {@link CountyCode} entities in the database.
 * The main input is a {@link CountyCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CountyCodeDTO} or a {@link Page} of {@link CountyCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CountyCodeQueryService extends QueryService<CountyCode> {

    private final Logger log = LoggerFactory.getLogger(CountyCodeQueryService.class);

    private final CountyCodeRepository countyCodeRepository;

    private final CountyCodeMapper countyCodeMapper;

    private final CountyCodeSearchRepository countyCodeSearchRepository;

    public CountyCodeQueryService(
        CountyCodeRepository countyCodeRepository,
        CountyCodeMapper countyCodeMapper,
        CountyCodeSearchRepository countyCodeSearchRepository
    ) {
        this.countyCodeRepository = countyCodeRepository;
        this.countyCodeMapper = countyCodeMapper;
        this.countyCodeSearchRepository = countyCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CountyCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CountyCodeDTO> findByCriteria(CountyCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CountyCode> specification = createSpecification(criteria);
        return countyCodeMapper.toDto(countyCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CountyCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CountyCodeDTO> findByCriteria(CountyCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CountyCode> specification = createSpecification(criteria);
        return countyCodeRepository.findAll(specification, page).map(countyCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CountyCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CountyCode> specification = createSpecification(criteria);
        return countyCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link CountyCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CountyCode> createSpecification(CountyCodeCriteria criteria) {
        Specification<CountyCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CountyCode_.id));
            }
            if (criteria.getCountyCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCountyCode(), CountyCode_.countyCode));
            }
            if (criteria.getCountyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCountyName(), CountyCode_.countyName));
            }
            if (criteria.getSubCountyCode() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubCountyCode(), CountyCode_.subCountyCode));
            }
            if (criteria.getSubCountyName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubCountyName(), CountyCode_.subCountyName));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(CountyCode_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
