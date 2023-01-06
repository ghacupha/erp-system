package io.github.erp.service;

/*-
 * Erp System - Mark III No 8 (Caleb Series) Server ver 0.3.0
 * Copyright © 2021 - 2022 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.SettlementRequisition;
import io.github.erp.repository.SettlementRequisitionRepository;
import io.github.erp.repository.search.SettlementRequisitionSearchRepository;
import io.github.erp.service.criteria.SettlementRequisitionCriteria;
import io.github.erp.service.dto.SettlementRequisitionDTO;
import io.github.erp.service.mapper.SettlementRequisitionMapper;
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
 * Service for executing complex queries for {@link SettlementRequisition} entities in the database.
 * The main input is a {@link SettlementRequisitionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SettlementRequisitionDTO} or a {@link Page} of {@link SettlementRequisitionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SettlementRequisitionQueryService extends QueryService<SettlementRequisition> {

    private final Logger log = LoggerFactory.getLogger(SettlementRequisitionQueryService.class);

    private final SettlementRequisitionRepository settlementRequisitionRepository;

    private final SettlementRequisitionMapper settlementRequisitionMapper;

    private final SettlementRequisitionSearchRepository settlementRequisitionSearchRepository;

    public SettlementRequisitionQueryService(
        SettlementRequisitionRepository settlementRequisitionRepository,
        SettlementRequisitionMapper settlementRequisitionMapper,
        SettlementRequisitionSearchRepository settlementRequisitionSearchRepository
    ) {
        this.settlementRequisitionRepository = settlementRequisitionRepository;
        this.settlementRequisitionMapper = settlementRequisitionMapper;
        this.settlementRequisitionSearchRepository = settlementRequisitionSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SettlementRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SettlementRequisitionDTO> findByCriteria(SettlementRequisitionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SettlementRequisition> specification = createSpecification(criteria);
        return settlementRequisitionMapper.toDto(settlementRequisitionRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SettlementRequisitionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SettlementRequisitionDTO> findByCriteria(SettlementRequisitionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SettlementRequisition> specification = createSpecification(criteria);
        return settlementRequisitionRepository.findAll(specification, page).map(settlementRequisitionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SettlementRequisitionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SettlementRequisition> specification = createSpecification(criteria);
        return settlementRequisitionRepository.count(specification);
    }

    /**
     * Function to convert {@link SettlementRequisitionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SettlementRequisition> createSpecification(SettlementRequisitionCriteria criteria) {
        Specification<SettlementRequisition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SettlementRequisition_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), SettlementRequisition_.description));
            }
            if (criteria.getSerialNumber() != null) {
                specification = specification.and(buildSpecification(criteria.getSerialNumber(), SettlementRequisition_.serialNumber));
            }
            if (criteria.getTimeOfRequisition() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTimeOfRequisition(), SettlementRequisition_.timeOfRequisition));
            }
            if (criteria.getRequisitionNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRequisitionNumber(), SettlementRequisition_.requisitionNumber));
            }
            if (criteria.getPaymentAmount() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getPaymentAmount(), SettlementRequisition_.paymentAmount));
            }
            if (criteria.getPaymentStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPaymentStatus(), SettlementRequisition_.paymentStatus));
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(SettlementRequisition_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getCurrentOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCurrentOwnerId(),
                            root -> root.join(SettlementRequisition_.currentOwner, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getNativeOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNativeOwnerId(),
                            root -> root.join(SettlementRequisition_.nativeOwner, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getNativeDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getNativeDepartmentId(),
                            root -> root.join(SettlementRequisition_.nativeDepartment, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getBillerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBillerId(),
                            root -> root.join(SettlementRequisition_.biller, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getPaymentInvoiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentInvoiceId(),
                            root -> root.join(SettlementRequisition_.paymentInvoices, JoinType.LEFT).get(PaymentInvoice_.id)
                        )
                    );
            }
            if (criteria.getDeliveryNoteId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeliveryNoteId(),
                            root -> root.join(SettlementRequisition_.deliveryNotes, JoinType.LEFT).get(DeliveryNote_.id)
                        )
                    );
            }
            if (criteria.getJobSheetId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getJobSheetId(),
                            root -> root.join(SettlementRequisition_.jobSheets, JoinType.LEFT).get(JobSheet_.id)
                        )
                    );
            }
            if (criteria.getSignaturesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSignaturesId(),
                            root -> root.join(SettlementRequisition_.signatures, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(SettlementRequisition_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getApplicationMappingId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getApplicationMappingId(),
                            root -> root.join(SettlementRequisition_.applicationMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(SettlementRequisition_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
