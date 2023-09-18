package io.github.erp.service;

/*-
 * Erp System - Mark V No 8 (Ehud Series) Server ver 1.5.1
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
import io.github.erp.domain.DepreciationMethod;
import io.github.erp.repository.DepreciationMethodRepository;
import io.github.erp.repository.search.DepreciationMethodSearchRepository;
import io.github.erp.service.criteria.DepreciationMethodCriteria;
import io.github.erp.service.dto.DepreciationMethodDTO;
import io.github.erp.service.mapper.DepreciationMethodMapper;
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
 * Service for executing complex queries for {@link DepreciationMethod} entities in the database.
 * The main input is a {@link DepreciationMethodCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DepreciationMethodDTO} or a {@link Page} of {@link DepreciationMethodDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DepreciationMethodQueryService extends QueryService<DepreciationMethod> {

    private final Logger log = LoggerFactory.getLogger(DepreciationMethodQueryService.class);

    private final DepreciationMethodRepository depreciationMethodRepository;

    private final DepreciationMethodMapper depreciationMethodMapper;

    private final DepreciationMethodSearchRepository depreciationMethodSearchRepository;

    public DepreciationMethodQueryService(
        DepreciationMethodRepository depreciationMethodRepository,
        DepreciationMethodMapper depreciationMethodMapper,
        DepreciationMethodSearchRepository depreciationMethodSearchRepository
    ) {
        this.depreciationMethodRepository = depreciationMethodRepository;
        this.depreciationMethodMapper = depreciationMethodMapper;
        this.depreciationMethodSearchRepository = depreciationMethodSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DepreciationMethodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DepreciationMethodDTO> findByCriteria(DepreciationMethodCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DepreciationMethod> specification = createSpecification(criteria);
        return depreciationMethodMapper.toDto(depreciationMethodRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DepreciationMethodDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DepreciationMethodDTO> findByCriteria(DepreciationMethodCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DepreciationMethod> specification = createSpecification(criteria);
        return depreciationMethodRepository.findAll(specification, page).map(depreciationMethodMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DepreciationMethodCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DepreciationMethod> specification = createSpecification(criteria);
        return depreciationMethodRepository.count(specification);
    }

    /**
     * Function to convert {@link DepreciationMethodCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DepreciationMethod> createSpecification(DepreciationMethodCriteria criteria) {
        Specification<DepreciationMethod> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DepreciationMethod_.id));
            }
            if (criteria.getDepreciationMethodName() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getDepreciationMethodName(), DepreciationMethod_.depreciationMethodName)
                    );
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), DepreciationMethod_.description));
            }
            if (criteria.getDepreciationType() != null) {
                specification = specification.and(buildSpecification(criteria.getDepreciationType(), DepreciationMethod_.depreciationType));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(DepreciationMethod_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
