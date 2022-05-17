package io.github.erp.service;

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.CreditNote;
import io.github.erp.repository.CreditNoteRepository;
import io.github.erp.repository.search.CreditNoteSearchRepository;
import io.github.erp.service.criteria.CreditNoteCriteria;
import io.github.erp.service.dto.CreditNoteDTO;
import io.github.erp.service.mapper.CreditNoteMapper;
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
 * Service for executing complex queries for {@link CreditNote} entities in the database.
 * The main input is a {@link CreditNoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CreditNoteDTO} or a {@link Page} of {@link CreditNoteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CreditNoteQueryService extends QueryService<CreditNote> {

    private final Logger log = LoggerFactory.getLogger(CreditNoteQueryService.class);

    private final CreditNoteRepository creditNoteRepository;

    private final CreditNoteMapper creditNoteMapper;

    private final CreditNoteSearchRepository creditNoteSearchRepository;

    public CreditNoteQueryService(
        CreditNoteRepository creditNoteRepository,
        CreditNoteMapper creditNoteMapper,
        CreditNoteSearchRepository creditNoteSearchRepository
    ) {
        this.creditNoteRepository = creditNoteRepository;
        this.creditNoteMapper = creditNoteMapper;
        this.creditNoteSearchRepository = creditNoteSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CreditNoteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CreditNoteDTO> findByCriteria(CreditNoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CreditNote> specification = createSpecification(criteria);
        return creditNoteMapper.toDto(creditNoteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CreditNoteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CreditNoteDTO> findByCriteria(CreditNoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CreditNote> specification = createSpecification(criteria);
        return creditNoteRepository.findAll(specification, page).map(creditNoteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CreditNoteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CreditNote> specification = createSpecification(criteria);
        return creditNoteRepository.count(specification);
    }

    /**
     * Function to convert {@link CreditNoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CreditNote> createSpecification(CreditNoteCriteria criteria) {
        Specification<CreditNote> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CreditNote_.id));
            }
            if (criteria.getCreditNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreditNumber(), CreditNote_.creditNumber));
            }
            if (criteria.getCreditNoteDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditNoteDate(), CreditNote_.creditNoteDate));
            }
            if (criteria.getCreditAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreditAmount(), CreditNote_.creditAmount));
            }
            if (criteria.getPurchaseOrdersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPurchaseOrdersId(),
                            root -> root.join(CreditNote_.purchaseOrders, JoinType.LEFT).get(PurchaseOrder_.id)
                        )
                    );
            }
            if (criteria.getInvoicesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInvoicesId(),
                            root -> root.join(CreditNote_.invoices, JoinType.LEFT).get(PaymentInvoice_.id)
                        )
                    );
            }
            if (criteria.getPaymentLabelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentLabelId(),
                            root -> root.join(CreditNote_.paymentLabels, JoinType.LEFT).get(PaymentLabel_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(CreditNote_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getSettlementCurrencyId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSettlementCurrencyId(),
                            root -> root.join(CreditNote_.settlementCurrency, JoinType.LEFT).get(SettlementCurrency_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
