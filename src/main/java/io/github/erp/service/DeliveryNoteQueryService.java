package io.github.erp.service;

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.DeliveryNote;
import io.github.erp.repository.DeliveryNoteRepository;
import io.github.erp.repository.search.DeliveryNoteSearchRepository;
import io.github.erp.service.criteria.DeliveryNoteCriteria;
import io.github.erp.service.dto.DeliveryNoteDTO;
import io.github.erp.service.mapper.DeliveryNoteMapper;
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
 * Service for executing complex queries for {@link DeliveryNote} entities in the database.
 * The main input is a {@link DeliveryNoteCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DeliveryNoteDTO} or a {@link Page} of {@link DeliveryNoteDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DeliveryNoteQueryService extends QueryService<DeliveryNote> {

    private final Logger log = LoggerFactory.getLogger(DeliveryNoteQueryService.class);

    private final DeliveryNoteRepository deliveryNoteRepository;

    private final DeliveryNoteMapper deliveryNoteMapper;

    private final DeliveryNoteSearchRepository deliveryNoteSearchRepository;

    public DeliveryNoteQueryService(
        DeliveryNoteRepository deliveryNoteRepository,
        DeliveryNoteMapper deliveryNoteMapper,
        DeliveryNoteSearchRepository deliveryNoteSearchRepository
    ) {
        this.deliveryNoteRepository = deliveryNoteRepository;
        this.deliveryNoteMapper = deliveryNoteMapper;
        this.deliveryNoteSearchRepository = deliveryNoteSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DeliveryNoteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DeliveryNoteDTO> findByCriteria(DeliveryNoteCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<DeliveryNote> specification = createSpecification(criteria);
        return deliveryNoteMapper.toDto(deliveryNoteRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DeliveryNoteDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DeliveryNoteDTO> findByCriteria(DeliveryNoteCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<DeliveryNote> specification = createSpecification(criteria);
        return deliveryNoteRepository.findAll(specification, page).map(deliveryNoteMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DeliveryNoteCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<DeliveryNote> specification = createSpecification(criteria);
        return deliveryNoteRepository.count(specification);
    }

    /**
     * Function to convert {@link DeliveryNoteCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<DeliveryNote> createSpecification(DeliveryNoteCriteria criteria) {
        Specification<DeliveryNote> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), DeliveryNote_.id));
            }
            if (criteria.getDeliveryNoteNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getDeliveryNoteNumber(), DeliveryNote_.deliveryNoteNumber));
            }
            if (criteria.getDocumentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDocumentDate(), DeliveryNote_.documentDate));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), DeliveryNote_.description));
            }
            if (criteria.getSerialNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSerialNumber(), DeliveryNote_.serialNumber));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), DeliveryNote_.quantity));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(DeliveryNote_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getReceivedById() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getReceivedById(),
                            root -> root.join(DeliveryNote_.receivedBy, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getDeliveryStampsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDeliveryStampsId(),
                            root -> root.join(DeliveryNote_.deliveryStamps, JoinType.LEFT).get(BusinessStamp_.id)
                        )
                    );
            }
            if (criteria.getPurchaseOrderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPurchaseOrderId(),
                            root -> root.join(DeliveryNote_.purchaseOrder, JoinType.LEFT).get(PurchaseOrder_.id)
                        )
                    );
            }
            if (criteria.getSupplierId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSupplierId(),
                            root -> root.join(DeliveryNote_.supplier, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getSignatoriesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSignatoriesId(),
                            root -> root.join(DeliveryNote_.signatories, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
