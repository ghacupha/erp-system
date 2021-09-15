package io.github.erp.service;

import io.github.erp.domain.*; // for static metamodels
import io.github.erp.domain.Placeholder;
import io.github.erp.repository.PlaceholderRepository;
import io.github.erp.repository.search.PlaceholderSearchRepository;
import io.github.erp.service.criteria.PlaceholderCriteria;
import io.github.erp.service.dto.PlaceholderDTO;
import io.github.erp.service.mapper.PlaceholderMapper;
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
 * Service for executing complex queries for {@link Placeholder} entities in the database.
 * The main input is a {@link PlaceholderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PlaceholderDTO} or a {@link Page} of {@link PlaceholderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PlaceholderQueryService extends QueryService<Placeholder> {

    private final Logger log = LoggerFactory.getLogger(PlaceholderQueryService.class);

    private final PlaceholderRepository placeholderRepository;

    private final PlaceholderMapper placeholderMapper;

    private final PlaceholderSearchRepository placeholderSearchRepository;

    public PlaceholderQueryService(
        PlaceholderRepository placeholderRepository,
        PlaceholderMapper placeholderMapper,
        PlaceholderSearchRepository placeholderSearchRepository
    ) {
        this.placeholderRepository = placeholderRepository;
        this.placeholderMapper = placeholderMapper;
        this.placeholderSearchRepository = placeholderSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PlaceholderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PlaceholderDTO> findByCriteria(PlaceholderCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Placeholder> specification = createSpecification(criteria);
        return placeholderMapper.toDto(placeholderRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PlaceholderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PlaceholderDTO> findByCriteria(PlaceholderCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Placeholder> specification = createSpecification(criteria);
        return placeholderRepository.findAll(specification, page).map(placeholderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PlaceholderCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Placeholder> specification = createSpecification(criteria);
        return placeholderRepository.count(specification);
    }

    /**
     * Function to convert {@link PlaceholderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Placeholder> createSpecification(PlaceholderCriteria criteria) {
        Specification<Placeholder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Placeholder_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), Placeholder_.description));
            }
            if (criteria.getToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getToken(), Placeholder_.token));
            }
            if (criteria.getContainingPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContainingPlaceholderId(),
                            root -> root.join(Placeholder_.containingPlaceholder, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getDealerId(), root -> root.join(Placeholder_.dealers, JoinType.LEFT).get(Dealer_.id))
                    );
            }
            if (criteria.getFileTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFileTypeId(),
                            root -> root.join(Placeholder_.fileTypes, JoinType.LEFT).get(FileType_.id)
                        )
                    );
            }
            if (criteria.getFileUploadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFileUploadId(),
                            root -> root.join(Placeholder_.fileUploads, JoinType.LEFT).get(FileUpload_.id)
                        )
                    );
            }
            if (criteria.getFixedAssetAcquisitionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFixedAssetAcquisitionId(),
                            root -> root.join(Placeholder_.fixedAssetAcquisitions, JoinType.LEFT).get(FixedAssetAcquisition_.id)
                        )
                    );
            }
            if (criteria.getFixedAssetDepreciationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFixedAssetDepreciationId(),
                            root -> root.join(Placeholder_.fixedAssetDepreciations, JoinType.LEFT).get(FixedAssetDepreciation_.id)
                        )
                    );
            }
            if (criteria.getFixedAssetNetBookValueId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFixedAssetNetBookValueId(),
                            root -> root.join(Placeholder_.fixedAssetNetBookValues, JoinType.LEFT).get(FixedAssetNetBookValue_.id)
                        )
                    );
            }
            if (criteria.getInvoiceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInvoiceId(),
                            root -> root.join(Placeholder_.invoices, JoinType.LEFT).get(Invoice_.id)
                        )
                    );
            }
            if (criteria.getMessageTokenId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMessageTokenId(),
                            root -> root.join(Placeholder_.messageTokens, JoinType.LEFT).get(MessageToken_.id)
                        )
                    );
            }
            if (criteria.getPaymentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentId(),
                            root -> root.join(Placeholder_.payments, JoinType.LEFT).get(Payment_.id)
                        )
                    );
            }
            if (criteria.getPaymentCalculationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentCalculationId(),
                            root -> root.join(Placeholder_.paymentCalculations, JoinType.LEFT).get(PaymentCalculation_.id)
                        )
                    );
            }
            if (criteria.getPaymentRequisitionId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentRequisitionId(),
                            root -> root.join(Placeholder_.paymentRequisitions, JoinType.LEFT).get(PaymentRequisition_.id)
                        )
                    );
            }
            if (criteria.getPaymentCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentCategoryId(),
                            root -> root.join(Placeholder_.paymentCategories, JoinType.LEFT).get(PaymentCategory_.id)
                        )
                    );
            }
            if (criteria.getTaxReferenceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTaxReferenceId(),
                            root -> root.join(Placeholder_.taxReferences, JoinType.LEFT).get(TaxReference_.id)
                        )
                    );
            }
            if (criteria.getTaxRuleId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTaxRuleId(),
                            root -> root.join(Placeholder_.taxRules, JoinType.LEFT).get(TaxRule_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
