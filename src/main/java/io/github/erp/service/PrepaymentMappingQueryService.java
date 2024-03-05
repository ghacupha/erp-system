package io.github.erp.service;

/*-
 * Erp System - Mark X No 5 (Jehoiada Series) Server ver 1.7.5
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
import io.github.erp.domain.PrepaymentMapping;
import io.github.erp.repository.PrepaymentMappingRepository;
import io.github.erp.repository.search.PrepaymentMappingSearchRepository;
import io.github.erp.service.criteria.PrepaymentMappingCriteria;
import io.github.erp.service.dto.PrepaymentMappingDTO;
import io.github.erp.service.mapper.PrepaymentMappingMapper;
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
 * Service for executing complex queries for {@link PrepaymentMapping} entities in the database.
 * The main input is a {@link PrepaymentMappingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PrepaymentMappingDTO} or a {@link Page} of {@link PrepaymentMappingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PrepaymentMappingQueryService extends QueryService<PrepaymentMapping> {

    private final Logger log = LoggerFactory.getLogger(PrepaymentMappingQueryService.class);

    private final PrepaymentMappingRepository prepaymentMappingRepository;

    private final PrepaymentMappingMapper prepaymentMappingMapper;

    private final PrepaymentMappingSearchRepository prepaymentMappingSearchRepository;

    public PrepaymentMappingQueryService(
        PrepaymentMappingRepository prepaymentMappingRepository,
        PrepaymentMappingMapper prepaymentMappingMapper,
        PrepaymentMappingSearchRepository prepaymentMappingSearchRepository
    ) {
        this.prepaymentMappingRepository = prepaymentMappingRepository;
        this.prepaymentMappingMapper = prepaymentMappingMapper;
        this.prepaymentMappingSearchRepository = prepaymentMappingSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PrepaymentMappingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PrepaymentMappingDTO> findByCriteria(PrepaymentMappingCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PrepaymentMapping> specification = createSpecification(criteria);
        return prepaymentMappingMapper.toDto(prepaymentMappingRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PrepaymentMappingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PrepaymentMappingDTO> findByCriteria(PrepaymentMappingCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PrepaymentMapping> specification = createSpecification(criteria);
        return prepaymentMappingRepository.findAll(specification, page).map(prepaymentMappingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PrepaymentMappingCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PrepaymentMapping> specification = createSpecification(criteria);
        return prepaymentMappingRepository.count(specification);
    }

    /**
     * Function to convert {@link PrepaymentMappingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PrepaymentMapping> createSpecification(PrepaymentMappingCriteria criteria) {
        Specification<PrepaymentMapping> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PrepaymentMapping_.id));
            }
            if (criteria.getParameterKey() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParameterKey(), PrepaymentMapping_.parameterKey));
            }
            if (criteria.getParameterGuid() != null) {
                specification = specification.and(buildSpecification(criteria.getParameterGuid(), PrepaymentMapping_.parameterGuid));
            }
            if (criteria.getParameter() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParameter(), PrepaymentMapping_.parameter));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(PrepaymentMapping_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
