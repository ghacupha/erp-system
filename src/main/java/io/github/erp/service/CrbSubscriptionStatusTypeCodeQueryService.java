package io.github.erp.service;

/*-
 * Erp System - Mark X No 7 (Jehoiada Series) Server ver 1.7.9
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
import io.github.erp.domain.CrbSubscriptionStatusTypeCode;
import io.github.erp.repository.CrbSubscriptionStatusTypeCodeRepository;
import io.github.erp.repository.search.CrbSubscriptionStatusTypeCodeSearchRepository;
import io.github.erp.service.criteria.CrbSubscriptionStatusTypeCodeCriteria;
import io.github.erp.service.dto.CrbSubscriptionStatusTypeCodeDTO;
import io.github.erp.service.mapper.CrbSubscriptionStatusTypeCodeMapper;
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
 * Service for executing complex queries for {@link CrbSubscriptionStatusTypeCode} entities in the database.
 * The main input is a {@link CrbSubscriptionStatusTypeCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbSubscriptionStatusTypeCodeDTO} or a {@link Page} of {@link CrbSubscriptionStatusTypeCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbSubscriptionStatusTypeCodeQueryService extends QueryService<CrbSubscriptionStatusTypeCode> {

    private final Logger log = LoggerFactory.getLogger(CrbSubscriptionStatusTypeCodeQueryService.class);

    private final CrbSubscriptionStatusTypeCodeRepository crbSubscriptionStatusTypeCodeRepository;

    private final CrbSubscriptionStatusTypeCodeMapper crbSubscriptionStatusTypeCodeMapper;

    private final CrbSubscriptionStatusTypeCodeSearchRepository crbSubscriptionStatusTypeCodeSearchRepository;

    public CrbSubscriptionStatusTypeCodeQueryService(
        CrbSubscriptionStatusTypeCodeRepository crbSubscriptionStatusTypeCodeRepository,
        CrbSubscriptionStatusTypeCodeMapper crbSubscriptionStatusTypeCodeMapper,
        CrbSubscriptionStatusTypeCodeSearchRepository crbSubscriptionStatusTypeCodeSearchRepository
    ) {
        this.crbSubscriptionStatusTypeCodeRepository = crbSubscriptionStatusTypeCodeRepository;
        this.crbSubscriptionStatusTypeCodeMapper = crbSubscriptionStatusTypeCodeMapper;
        this.crbSubscriptionStatusTypeCodeSearchRepository = crbSubscriptionStatusTypeCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbSubscriptionStatusTypeCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbSubscriptionStatusTypeCodeDTO> findByCriteria(CrbSubscriptionStatusTypeCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbSubscriptionStatusTypeCode> specification = createSpecification(criteria);
        return crbSubscriptionStatusTypeCodeMapper.toDto(crbSubscriptionStatusTypeCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbSubscriptionStatusTypeCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbSubscriptionStatusTypeCodeDTO> findByCriteria(CrbSubscriptionStatusTypeCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbSubscriptionStatusTypeCode> specification = createSpecification(criteria);
        return crbSubscriptionStatusTypeCodeRepository.findAll(specification, page).map(crbSubscriptionStatusTypeCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbSubscriptionStatusTypeCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbSubscriptionStatusTypeCode> specification = createSpecification(criteria);
        return crbSubscriptionStatusTypeCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbSubscriptionStatusTypeCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbSubscriptionStatusTypeCode> createSpecification(CrbSubscriptionStatusTypeCodeCriteria criteria) {
        Specification<CrbSubscriptionStatusTypeCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbSubscriptionStatusTypeCode_.id));
            }
            if (criteria.getSubscriptionStatusTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSubscriptionStatusTypeCode(),
                            CrbSubscriptionStatusTypeCode_.subscriptionStatusTypeCode
                        )
                    );
            }
            if (criteria.getSubscriptionStatusType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(
                            criteria.getSubscriptionStatusType(),
                            CrbSubscriptionStatusTypeCode_.subscriptionStatusType
                        )
                    );
            }
        }
        return specification;
    }
}
