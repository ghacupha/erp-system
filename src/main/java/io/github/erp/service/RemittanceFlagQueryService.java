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
import io.github.erp.domain.RemittanceFlag;
import io.github.erp.repository.RemittanceFlagRepository;
import io.github.erp.repository.search.RemittanceFlagSearchRepository;
import io.github.erp.service.criteria.RemittanceFlagCriteria;
import io.github.erp.service.dto.RemittanceFlagDTO;
import io.github.erp.service.mapper.RemittanceFlagMapper;
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
 * Service for executing complex queries for {@link RemittanceFlag} entities in the database.
 * The main input is a {@link RemittanceFlagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RemittanceFlagDTO} or a {@link Page} of {@link RemittanceFlagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RemittanceFlagQueryService extends QueryService<RemittanceFlag> {

    private final Logger log = LoggerFactory.getLogger(RemittanceFlagQueryService.class);

    private final RemittanceFlagRepository remittanceFlagRepository;

    private final RemittanceFlagMapper remittanceFlagMapper;

    private final RemittanceFlagSearchRepository remittanceFlagSearchRepository;

    public RemittanceFlagQueryService(
        RemittanceFlagRepository remittanceFlagRepository,
        RemittanceFlagMapper remittanceFlagMapper,
        RemittanceFlagSearchRepository remittanceFlagSearchRepository
    ) {
        this.remittanceFlagRepository = remittanceFlagRepository;
        this.remittanceFlagMapper = remittanceFlagMapper;
        this.remittanceFlagSearchRepository = remittanceFlagSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RemittanceFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RemittanceFlagDTO> findByCriteria(RemittanceFlagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RemittanceFlag> specification = createSpecification(criteria);
        return remittanceFlagMapper.toDto(remittanceFlagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RemittanceFlagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RemittanceFlagDTO> findByCriteria(RemittanceFlagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RemittanceFlag> specification = createSpecification(criteria);
        return remittanceFlagRepository.findAll(specification, page).map(remittanceFlagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RemittanceFlagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RemittanceFlag> specification = createSpecification(criteria);
        return remittanceFlagRepository.count(specification);
    }

    /**
     * Function to convert {@link RemittanceFlagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RemittanceFlag> createSpecification(RemittanceFlagCriteria criteria) {
        Specification<RemittanceFlag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RemittanceFlag_.id));
            }
            if (criteria.getRemittanceTypeFlag() != null) {
                specification = specification.and(buildSpecification(criteria.getRemittanceTypeFlag(), RemittanceFlag_.remittanceTypeFlag));
            }
            if (criteria.getRemittanceType() != null) {
                specification = specification.and(buildSpecification(criteria.getRemittanceType(), RemittanceFlag_.remittanceType));
            }
        }
        return specification;
    }
}
