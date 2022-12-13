package io.github.erp.service;

/*-
 * Erp System - Mark III No 5 (Caleb Series) Server ver 0.1.6-SNAPSHOT
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
import io.github.erp.domain.Settlement;
import io.github.erp.repository.SettlementRepository;
import io.github.erp.repository.search.SettlementSearchRepository;
import io.github.erp.service.criteria.SettlementCriteria;
import io.github.erp.service.dto.SettlementDTO;
import io.github.erp.service.mapper.SettlementMapper;
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
 * Service for executing complex queries for {@link Settlement} entities in the database.
 * The main input is a {@link SettlementCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SettlementDTO} or a {@link Page} of {@link SettlementDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SettlementQueryService extends QueryService<Settlement> {

    private final Logger log = LoggerFactory.getLogger(SettlementQueryService.class);

    private final SettlementRepository settlementRepository;

    private final SettlementMapper settlementMapper;

    private final SettlementSearchRepository settlementSearchRepository;

    public SettlementQueryService(
        SettlementRepository settlementRepository,
        SettlementMapper settlementMapper,
        SettlementSearchRepository settlementSearchRepository
    ) {
        this.settlementRepository = settlementRepository;
        this.settlementMapper = settlementMapper;
        this.settlementSearchRepository = settlementSearchRepository;
    }

    /**
     * Return a {@link List} of {@link SettlementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SettlementDTO> findByCriteria(SettlementCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Settlement> specification = createSpecification(criteria);
        return settlementMapper.toDto(settlementRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SettlementDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SettlementDTO> findByCriteria(SettlementCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Settlement> specification = createSpecification(criteria);
        return settlementRepository.findAll(specification, page).map(settlementMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SettlementCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Settlement> specification = createSpecification(criteria);
        return settlementRepository.count(specification);
    }

    /**
     * Function to convert {@link SettlementCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Settlement> createSpecification(SettlementCriteria criteria) {
        Specification<Settlement> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Settlement_.id));
            }
            if (criteria.getPaymentNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPaymentNumber(), Settlement_.paymentNumber));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), Settlement_.paymentDate));
            }
            if (criteria.getPaymentAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentAmount(), Settlement_.paymentAmount));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Settlement_.description));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), Settlement_.notes));
            }
            if (criteria.getFileUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUploadToken(), Settlement_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompilationToken(), Settlement_.compilationToken));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(Settlement_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(Settlement_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
            if (criteria.getPaymentLabelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentLabelId(),
                            root -> root.join(Settlement_.paymentLabels, JoinType.LEFT).get(PaymentLabel_.id)
                        )
                    );
            }
            if (criteria.getPaymentCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentCategoryId(),
                            root -> root.join(Settlement_.paymentCategory, JoinType.LEFT).get(PaymentCategory_.id)
                        )
                    );
            }
            if (criteria.getGroupSettlementId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getGroupSettlementId(),
                            root -> root.join(Settlement_.groupSettlement, JoinType.LEFT).get(Settlement_.id)
                        )
                    );
            }
            if (criteria.getBillerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getBillerId(), root -> root.join(Settlement_.biller, JoinType.LEFT).get(Dealer_.id))
                    );
            }
            if (criteria.getPaymentInvoiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentInvoiceId(),
                            root -> root.join(Settlement_.paymentInvoices, JoinType.LEFT).get(PaymentInvoice_.id)
                        )
                    );
            }
            if (criteria.getSignatoriesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSignatoriesId(),
                            root -> root.join(Settlement_.signatories, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
