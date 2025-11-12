package io.github.erp.service;

/*-
 * Erp System - Mark X No 11 (Jehoiada Series) Server ver 1.8.3
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
import io.github.erp.domain.WIPTransferListItem;
import io.github.erp.repository.WIPTransferListItemRepository;
import io.github.erp.repository.search.WIPTransferListItemSearchRepository;
import io.github.erp.service.criteria.WIPTransferListItemCriteria;
import io.github.erp.service.dto.WIPTransferListItemDTO;
import io.github.erp.service.mapper.WIPTransferListItemMapper;
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
 * Service for executing complex queries for {@link WIPTransferListItem} entities in the database.
 * The main input is a {@link WIPTransferListItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WIPTransferListItemDTO} or a {@link Page} of {@link WIPTransferListItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WIPTransferListItemQueryService extends QueryService<WIPTransferListItem> {

    private final Logger log = LoggerFactory.getLogger(WIPTransferListItemQueryService.class);

    private final WIPTransferListItemRepository wIPTransferListItemRepository;

    private final WIPTransferListItemMapper wIPTransferListItemMapper;

    private final WIPTransferListItemSearchRepository wIPTransferListItemSearchRepository;

    public WIPTransferListItemQueryService(
        WIPTransferListItemRepository wIPTransferListItemRepository,
        WIPTransferListItemMapper wIPTransferListItemMapper,
        WIPTransferListItemSearchRepository wIPTransferListItemSearchRepository
    ) {
        this.wIPTransferListItemRepository = wIPTransferListItemRepository;
        this.wIPTransferListItemMapper = wIPTransferListItemMapper;
        this.wIPTransferListItemSearchRepository = wIPTransferListItemSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WIPTransferListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WIPTransferListItemDTO> findByCriteria(WIPTransferListItemCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WIPTransferListItem> specification = createSpecification(criteria);
        return wIPTransferListItemMapper.toDto(wIPTransferListItemRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WIPTransferListItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WIPTransferListItemDTO> findByCriteria(WIPTransferListItemCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WIPTransferListItem> specification = createSpecification(criteria);
        return wIPTransferListItemRepository.findAll(specification, page).map(wIPTransferListItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WIPTransferListItemCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WIPTransferListItem> specification = createSpecification(criteria);
        return wIPTransferListItemRepository.count(specification);
    }

    /**
     * Function to convert {@link WIPTransferListItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WIPTransferListItem> createSpecification(WIPTransferListItemCriteria criteria) {
        Specification<WIPTransferListItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WIPTransferListItem_.id));
            }
            if (criteria.getWipSequence() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWipSequence(), WIPTransferListItem_.wipSequence));
            }
            if (criteria.getWipParticulars() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getWipParticulars(), WIPTransferListItem_.wipParticulars));
            }
            if (criteria.getTransferType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTransferType(), WIPTransferListItem_.transferType));
            }
            if (criteria.getTransferSettlement() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTransferSettlement(), WIPTransferListItem_.transferSettlement));
            }
            if (criteria.getTransferSettlementDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getTransferSettlementDate(), WIPTransferListItem_.transferSettlementDate)
                    );
            }
            if (criteria.getTransferAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransferAmount(), WIPTransferListItem_.transferAmount));
            }
            if (criteria.getWipTransferDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getWipTransferDate(), WIPTransferListItem_.wipTransferDate));
            }
            if (criteria.getOriginalSettlement() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getOriginalSettlement(), WIPTransferListItem_.originalSettlement));
            }
            if (criteria.getOriginalSettlementDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getOriginalSettlementDate(), WIPTransferListItem_.originalSettlementDate)
                    );
            }
            if (criteria.getAssetCategory() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getAssetCategory(), WIPTransferListItem_.assetCategory));
            }
            if (criteria.getServiceOutlet() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getServiceOutlet(), WIPTransferListItem_.serviceOutlet));
            }
            if (criteria.getWorkProject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getWorkProject(), WIPTransferListItem_.workProject));
            }
        }
        return specification;
    }
}
