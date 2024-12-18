package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.LeaseLiabilityByAccountReportItem;
import io.github.erp.repository.LeaseLiabilityByAccountReportItemRepository;
import io.github.erp.repository.search.LeaseLiabilityByAccountReportItemSearchRepository;
import io.github.erp.service.criteria.LeaseLiabilityByAccountReportItemCriteria;
import io.github.erp.service.dto.LeaseLiabilityByAccountReportItemDTO;
import io.github.erp.service.mapper.LeaseLiabilityByAccountReportItemMapper;
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
 * Service for executing complex queries for {@link LeaseLiabilityByAccountReportItem} entities in the database.
 * The main input is a {@link LeaseLiabilityByAccountReportItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseLiabilityByAccountReportItemDTO} or a {@link Page} of {@link LeaseLiabilityByAccountReportItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseLiabilityByAccountReportItemQueryService extends QueryService<LeaseLiabilityByAccountReportItem> {

    private final Logger log = LoggerFactory.getLogger(LeaseLiabilityByAccountReportItemQueryService.class);

    private final LeaseLiabilityByAccountReportItemRepository leaseLiabilityByAccountReportItemRepository;

    private final LeaseLiabilityByAccountReportItemMapper leaseLiabilityByAccountReportItemMapper;

    private final LeaseLiabilityByAccountReportItemSearchRepository leaseLiabilityByAccountReportItemSearchRepository;

    public LeaseLiabilityByAccountReportItemQueryService(
        LeaseLiabilityByAccountReportItemRepository leaseLiabilityByAccountReportItemRepository,
        LeaseLiabilityByAccountReportItemMapper leaseLiabilityByAccountReportItemMapper,
        LeaseLiabilityByAccountReportItemSearchRepository leaseLiabilityByAccountReportItemSearchRepository
    ) {
        this.leaseLiabilityByAccountReportItemRepository = leaseLiabilityByAccountReportItemRepository;
        this.leaseLiabilityByAccountReportItemMapper = leaseLiabilityByAccountReportItemMapper;
        this.leaseLiabilityByAccountReportItemSearchRepository = leaseLiabilityByAccountReportItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseLiabilityByAccountReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseLiabilityByAccountReportItemDTO> findByCriteria(LeaseLiabilityByAccountReportItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseLiabilityByAccountReportItem> specification = createSpecification(criteria);
        return leaseLiabilityByAccountReportItemMapper.toDto(leaseLiabilityByAccountReportItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseLiabilityByAccountReportItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseLiabilityByAccountReportItemDTO> findByCriteria(LeaseLiabilityByAccountReportItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseLiabilityByAccountReportItem> specification = createSpecification(criteria);
        return leaseLiabilityByAccountReportItemRepository.findAll(specification, page).map(leaseLiabilityByAccountReportItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseLiabilityByAccountReportItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseLiabilityByAccountReportItem> specification = createSpecification(criteria);
        return leaseLiabilityByAccountReportItemRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseLiabilityByAccountReportItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseLiabilityByAccountReportItem> createSpecification(LeaseLiabilityByAccountReportItemCriteria criteria) {
        Specification<LeaseLiabilityByAccountReportItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseLiabilityByAccountReportItem_.id));
            }
            if (criteria.getAccountName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAccountName(), LeaseLiabilityByAccountReportItem_.accountName));
            }
            if (criteria.getAccountNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getAccountNumber(), LeaseLiabilityByAccountReportItem_.accountNumber)
                    );
            }
            if (criteria.getDescription() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDescription(), LeaseLiabilityByAccountReportItem_.description));
            }
            if (criteria.getAccountBalance() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getAccountBalance(), LeaseLiabilityByAccountReportItem_.accountBalance)
                    );
            }
        }
        return specification;
    }
}
