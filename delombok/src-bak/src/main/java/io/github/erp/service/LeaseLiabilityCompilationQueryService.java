package io.github.erp.service;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.LeaseLiabilityCompilation;
import io.github.erp.repository.LeaseLiabilityCompilationRepository;
import io.github.erp.repository.search.LeaseLiabilityCompilationSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityCompilationCriteria;
import io.github.erp.service.dto.LeaseLiabilityCompilationDTO;
import io.github.erp.service.mapper.LeaseLiabilityCompilationMapper;
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
 * Service for executing complex queries for {@link LeaseLiabilityCompilation} entities in the database.
 * The main input is a {@link LeaseLiabilityCompilationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseLiabilityCompilationDTO} or a {@link Page} of {@link LeaseLiabilityCompilationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseLiabilityCompilationQueryService extends QueryService<LeaseLiabilityCompilation> {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityCompilationQueryService.class);

    private final LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository;

    private final LeaseLiabilityCompilationMapper leaseLiabilityCompilationMapper;

    private final LeaseLiabilityCompilationSearchRepository leaseLiabilityCompilationSearchRepository;

    public LeaseLiabilityCompilationQueryService(
        LeaseLiabilityCompilationRepository leaseLiabilityCompilationRepository,
        LeaseLiabilityCompilationMapper leaseLiabilityCompilationMapper,
        LeaseLiabilityCompilationSearchRepository leaseLiabilityCompilationSearchRepository
    ) {
        this.leaseLiabilityCompilationRepository = leaseLiabilityCompilationRepository;
        this.leaseLiabilityCompilationMapper = leaseLiabilityCompilationMapper;
        this.leaseLiabilityCompilationSearchRepository = leaseLiabilityCompilationSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseLiabilityCompilationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseLiabilityCompilationDTO> findByCriteria(LeaseLiabilityCompilationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseLiabilityCompilation> specification = createSpecification(criteria);
        return leaseLiabilityCompilationMapper.toDto(leaseLiabilityCompilationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseLiabilityCompilationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityCompilationDTO> findByCriteria(LeaseLiabilityCompilationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseLiabilityCompilation> specification = createSpecification(criteria);
        return leaseLiabilityCompilationRepository.findAll(specification, page).map(leaseLiabilityCompilationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseLiabilityCompilationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseLiabilityCompilation> specification = createSpecification(criteria);
        return leaseLiabilityCompilationRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseLiabilityCompilationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseLiabilityCompilation> createSpecification(LeaseLiabilityCompilationCriteria criteria) {
        Specification<LeaseLiabilityCompilation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseLiabilityCompilation_.id));
            }
            if (criteria.getRequestId() != null) {
                specification = specification.and(buildSpecification(criteria.getRequestId(), LeaseLiabilityCompilation_.requestId));
            }
            if (criteria.getTimeOfRequest() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequest(), LeaseLiabilityCompilation_.timeOfRequest));
            }
            if (criteria.getRequestedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRequestedById(),
                            root -> root.join(LeaseLiabilityCompilation_.requestedBy, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
