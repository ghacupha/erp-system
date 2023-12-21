package io.github.erp.service;

/*-
 * Erp System - Mark IX No 5 (Iddo Series) Server ver 1.6.7
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
import io.github.erp.domain.CrbAccountStatus;
import io.github.erp.repository.CrbAccountStatusRepository;
import io.github.erp.repository.search.CrbAccountStatusSearchRepository;
import io.github.erp.service.criteria.CrbAccountStatusCriteria;
import io.github.erp.service.dto.CrbAccountStatusDTO;
import io.github.erp.service.mapper.CrbAccountStatusMapper;
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
 * Service for executing complex queries for {@link CrbAccountStatus} entities in the database.
 * The main input is a {@link CrbAccountStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbAccountStatusDTO} or a {@link Page} of {@link CrbAccountStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbAccountStatusQueryService extends QueryService<CrbAccountStatus> {

    private final Logger log = LoggerFactory.getLogger(CrbAccountStatusQueryService.class);

    private final CrbAccountStatusRepository crbAccountStatusRepository;

    private final CrbAccountStatusMapper crbAccountStatusMapper;

    private final CrbAccountStatusSearchRepository crbAccountStatusSearchRepository;

    public CrbAccountStatusQueryService(
        CrbAccountStatusRepository crbAccountStatusRepository,
        CrbAccountStatusMapper crbAccountStatusMapper,
        CrbAccountStatusSearchRepository crbAccountStatusSearchRepository
    ) {
        this.crbAccountStatusRepository = crbAccountStatusRepository;
        this.crbAccountStatusMapper = crbAccountStatusMapper;
        this.crbAccountStatusSearchRepository = crbAccountStatusSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbAccountStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbAccountStatusDTO> findByCriteria(CrbAccountStatusCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbAccountStatus> specification = createSpecification(criteria);
        return crbAccountStatusMapper.toDto(crbAccountStatusRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbAccountStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbAccountStatusDTO> findByCriteria(CrbAccountStatusCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbAccountStatus> specification = createSpecification(criteria);
        return crbAccountStatusRepository.findAll(specification, page).map(crbAccountStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbAccountStatusCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbAccountStatus> specification = createSpecification(criteria);
        return crbAccountStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbAccountStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbAccountStatus> createSpecification(CrbAccountStatusCriteria criteria) {
        Specification<CrbAccountStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbAccountStatus_.id));
            }
            if (criteria.getAccountStatusTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAccountStatusTypeCode(), CrbAccountStatus_.accountStatusTypeCode)
                    );
            }
            if (criteria.getAccountStatusType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAccountStatusType(), CrbAccountStatus_.accountStatusType));
            }
        }
        return specification;
    }
}
