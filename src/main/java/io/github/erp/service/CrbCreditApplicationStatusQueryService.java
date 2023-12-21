package io.github.erp.service;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.CrbCreditApplicationStatus;
import io.github.erp.repository.CrbCreditApplicationStatusRepository;
import io.github.erp.repository.search.CrbCreditApplicationStatusSearchRepository;
import io.github.erp.service.criteria.CrbCreditApplicationStatusCriteria;
import io.github.erp.service.dto.CrbCreditApplicationStatusDTO;
import io.github.erp.service.mapper.CrbCreditApplicationStatusMapper;
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
 * Service for executing complex queries for {@link CrbCreditApplicationStatus} entities in the database.
 * The main input is a {@link CrbCreditApplicationStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbCreditApplicationStatusDTO} or a {@link Page} of {@link CrbCreditApplicationStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbCreditApplicationStatusQueryService extends QueryService<CrbCreditApplicationStatus> {

    private final Logger log = LoggerFactory.getLogger(CrbCreditApplicationStatusQueryService.class);

    private final CrbCreditApplicationStatusRepository crbCreditApplicationStatusRepository;

    private final CrbCreditApplicationStatusMapper crbCreditApplicationStatusMapper;

    private final CrbCreditApplicationStatusSearchRepository crbCreditApplicationStatusSearchRepository;

    public CrbCreditApplicationStatusQueryService(
        CrbCreditApplicationStatusRepository crbCreditApplicationStatusRepository,
        CrbCreditApplicationStatusMapper crbCreditApplicationStatusMapper,
        CrbCreditApplicationStatusSearchRepository crbCreditApplicationStatusSearchRepository
    ) {
        this.crbCreditApplicationStatusRepository = crbCreditApplicationStatusRepository;
        this.crbCreditApplicationStatusMapper = crbCreditApplicationStatusMapper;
        this.crbCreditApplicationStatusSearchRepository = crbCreditApplicationStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbCreditApplicationStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbCreditApplicationStatusDTO> findByCriteria(CrbCreditApplicationStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbCreditApplicationStatus> specification = createSpecification(criteria);
        return crbCreditApplicationStatusMapper.toDto(crbCreditApplicationStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbCreditApplicationStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbCreditApplicationStatusDTO> findByCriteria(CrbCreditApplicationStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbCreditApplicationStatus> specification = createSpecification(criteria);
        return crbCreditApplicationStatusRepository.findAll(specification, page).map(crbCreditApplicationStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbCreditApplicationStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbCreditApplicationStatus> specification = createSpecification(criteria);
        return crbCreditApplicationStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbCreditApplicationStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbCreditApplicationStatus> createSpecification(CrbCreditApplicationStatusCriteria criteria) {
        Specification<CrbCreditApplicationStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbCreditApplicationStatus_.id));
            }
            if (criteria.getCrbCreditApplicationStatusTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCrbCreditApplicationStatusTypeCode(),
                            CrbCreditApplicationStatus_.crbCreditApplicationStatusTypeCode
                        )
                    );
            }
            if (criteria.getCrbCreditApplicationStatusType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getCrbCreditApplicationStatusType(),
                            CrbCreditApplicationStatus_.crbCreditApplicationStatusType
                        )
                    );
            }
        }
        return specification;
    }
}
