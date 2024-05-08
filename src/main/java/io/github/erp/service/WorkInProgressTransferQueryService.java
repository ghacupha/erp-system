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
import io.github.erp.domain.WorkInProgressTransfer;
import io.github.erp.repository.WorkInProgressTransferRepository;
import io.github.erp.repository.search.WorkInProgressTransferSearchRepository;
import io.github.erp.service.criteria.WorkInProgressTransferCriteria;
import io.github.erp.service.dto.WorkInProgressTransferDTO;
import io.github.erp.service.mapper.WorkInProgressTransferMapper;
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
 * Service for executing complex queries for {@link WorkInProgressTransfer} entities in the database.
 * The main input is a {@link WorkInProgressTransferCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link WorkInProgressTransferDTO} or a {@link Page} of {@link WorkInProgressTransferDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class WorkInProgressTransferQueryService extends QueryService<WorkInProgressTransfer> {

    private final Logger log = LoggerFactory.getLogger(WorkInProgressTransferQueryService.class);

    private final WorkInProgressTransferRepository workInProgressTransferRepository;

    private final WorkInProgressTransferMapper workInProgressTransferMapper;

    private final WorkInProgressTransferSearchRepository workInProgressTransferSearchRepository;

    public WorkInProgressTransferQueryService(
        WorkInProgressTransferRepository workInProgressTransferRepository,
        WorkInProgressTransferMapper workInProgressTransferMapper,
        WorkInProgressTransferSearchRepository workInProgressTransferSearchRepository
    ) {
        this.workInProgressTransferRepository = workInProgressTransferRepository;
        this.workInProgressTransferMapper = workInProgressTransferMapper;
        this.workInProgressTransferSearchRepository = workInProgressTransferSearchRepository;
    }

    /**
     * Return a {@link List} of {@link WorkInProgressTransferDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<WorkInProgressTransferDTO> findByCriteria(WorkInProgressTransferCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<WorkInProgressTransfer> specification = createSpecification(criteria);
        return workInProgressTransferMapper.toDto(workInProgressTransferRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link WorkInProgressTransferDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<WorkInProgressTransferDTO> findByCriteria(WorkInProgressTransferCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<WorkInProgressTransfer> specification = createSpecification(criteria);
        return workInProgressTransferRepository.findAll(specification, page).map(workInProgressTransferMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(WorkInProgressTransferCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<WorkInProgressTransfer> specification = createSpecification(criteria);
        return workInProgressTransferRepository.count(specification);
    }

    /**
     * Function to convert {@link WorkInProgressTransferCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<WorkInProgressTransfer> createSpecification(WorkInProgressTransferCriteria criteria) {
        Specification<WorkInProgressTransfer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), WorkInProgressTransfer_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), WorkInProgressTransfer_.description));
            }
            if (criteria.getTargetAssetNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTargetAssetNumber(), WorkInProgressTransfer_.targetAssetNumber));
            }
            if (criteria.getTransferAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransferAmount(), WorkInProgressTransfer_.transferAmount));
            }
            if (criteria.getTransferDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTransferDate(), WorkInProgressTransfer_.transferDate));
            }
            if (criteria.getTransferType() != null) {
                specification = specification.and(buildSpecification(criteria.getTransferType(), WorkInProgressTransfer_.transferType));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(WorkInProgressTransfer_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(WorkInProgressTransfer_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getAssetCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetCategoryId(),
                            root -> root.join(WorkInProgressTransfer_.assetCategory, JoinType.LEFT).get(AssetCategory_.id)
                        )
                    );
            }
            if (criteria.getWorkInProgressRegistrationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWorkInProgressRegistrationId(),
                            root ->
                                root
                                    .join(WorkInProgressTransfer_.workInProgressRegistration, JoinType.LEFT)
                                    .get(WorkInProgressRegistration_.id)
                        )
                    );
            }
            if (criteria.getServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceOutletId(),
                            root -> root.join(WorkInProgressTransfer_.serviceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getSettlementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementId(),
                            root -> root.join(WorkInProgressTransfer_.settlement, JoinType.LEFT).get(Settlement_.id)
                        )
                    );
            }
            if (criteria.getWorkProjectRegisterId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getWorkProjectRegisterId(),
                            root -> root.join(WorkInProgressTransfer_.workProjectRegister, JoinType.LEFT).get(WorkProjectRegister_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
