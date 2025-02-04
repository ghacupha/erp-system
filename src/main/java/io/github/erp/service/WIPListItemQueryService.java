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
import io.github.erp.domain.WIPListItem;
import io.github.erp.internal.framework.Mapping;
import io.github.erp.internal.repository.InternalWIPListItemRepository;
import io.github.erp.repository.WIPListItemRepository;
import io.github.erp.repository.search.WIPListItemSearchRepository;
import io.github.erp.service.criteria.WIPListItemCriteria;
import io.github.erp.service.dto.WIPListItemDTO;
import io.github.erp.service.mapper.WIPListItemMapper;
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
 * Service for executing complex queries for {@link WIPListItem} entities in the database.
 * The main input is a {@link WIPListItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WIPListItemDTO} or a {@link Page} of {@link WIPListItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WIPListItemQueryService extends QueryService<WIPListItem> {

    private final Logger log = LoggerFactory.getLogger(WIPListItemQueryService.class);

    private final InternalWIPListItemRepository wIPListItemRepository;

    private final WIPListItemMapper wIPListItemMapper;

    private final Mapping<WIPListItemREPO, WIPListItemDTO> wipListItemMapper;

    private final WIPListItemSearchRepository wIPListItemSearchRepository;

    public WIPListItemQueryService(
        InternalWIPListItemRepository wIPListItemRepository,
        WIPListItemMapper wIPListItemMapper, Mapping<WIPListItemREPO, WIPListItemDTO> wipListItemMapper,
        WIPListItemSearchRepository wIPListItemSearchRepository
    ) {
        this.wIPListItemRepository = wIPListItemRepository;
        this.wIPListItemMapper = wIPListItemMapper;
        this.wipListItemMapper = wipListItemMapper;
        this.wIPListItemSearchRepository = wIPListItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WIPListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WIPListItemDTO> findByCriteria(WIPListItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WIPListItem> specification = createSpecification(criteria);
        return wIPListItemMapper.toDto(wIPListItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WIPListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WIPListItemDTO> findByCriteria(WIPListItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WIPListItem> specification = createSpecification(criteria);
        return wIPListItemRepository.findAllSpecifiedReportItems(specification, page).map(wipListItemMapper::toValue2);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WIPListItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WIPListItem> specification = createSpecification(criteria);
        return wIPListItemRepository.count(specification);
    }

    /**
     * Function to convert {@link WIPListItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WIPListItem> createSpecification(WIPListItemCriteria criteria) {
        Specification<WIPListItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WIPListItem_.id));
            }
            if (criteria.getSequenceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSequenceNumber(), WIPListItem_.sequenceNumber));
            }
            if (criteria.getParticulars() != null) {
                specification = specification.and(buildStringSpecification(criteria.getParticulars(), WIPListItem_.particulars));
            }
            if (criteria.getInstalmentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInstalmentDate(), WIPListItem_.instalmentDate));
            }
            if (criteria.getInstalmentAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInstalmentAmount(), WIPListItem_.instalmentAmount));
            }
            if (criteria.getSettlementCurrency() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSettlementCurrency(), WIPListItem_.settlementCurrency));
            }
            if (criteria.getOutletCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutletCode(), WIPListItem_.outletCode));
            }
            if (criteria.getSettlementTransaction() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getSettlementTransaction(), WIPListItem_.settlementTransaction));
            }
            if (criteria.getSettlementTransactionDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getSettlementTransactionDate(), WIPListItem_.settlementTransactionDate)
                    );
            }
            if (criteria.getDealerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDealerName(), WIPListItem_.dealerName));
            }
            if (criteria.getWorkProject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWorkProject(), WIPListItem_.workProject));
            }
        }
        return specification;
    }
}
