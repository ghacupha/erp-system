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
import io.github.erp.domain.LeaseTemplate;
import io.github.erp.repository.LeaseTemplateRepository;
import io.github.erp.repository.search.LeaseTemplateSearchRepository;
import io.github.erp.service.criteria.LeaseTemplateCriteria;
import io.github.erp.service.dto.LeaseTemplateDTO;
import io.github.erp.service.mapper.LeaseTemplateMapper;
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
 * Service for executing complex queries for {@link LeaseTemplate} entities in the database.
 * The main input is a {@link LeaseTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseTemplateDTO} or a {@link Page} of {@link LeaseTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseTemplateQueryService extends QueryService<LeaseTemplate> {

    private final Logger log = LoggerFactory.getLogger(LeaseTemplateQueryService.class);

    private final LeaseTemplateRepository leaseTemplateRepository;

    private final LeaseTemplateMapper leaseTemplateMapper;

    private final LeaseTemplateSearchRepository leaseTemplateSearchRepository;

    public LeaseTemplateQueryService(
        LeaseTemplateRepository leaseTemplateRepository,
        LeaseTemplateMapper leaseTemplateMapper,
        LeaseTemplateSearchRepository leaseTemplateSearchRepository
    ) {
        this.leaseTemplateRepository = leaseTemplateRepository;
        this.leaseTemplateMapper = leaseTemplateMapper;
        this.leaseTemplateSearchRepository = leaseTemplateSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseTemplateDTO> findByCriteria(LeaseTemplateCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseTemplate> specification = createSpecification(criteria);
        return leaseTemplateMapper.toDto(leaseTemplateRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseTemplateDTO> findByCriteria(LeaseTemplateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseTemplate> specification = createSpecification(criteria);
        return leaseTemplateRepository.findAll(specification, page).map(leaseTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseTemplateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseTemplate> specification = createSpecification(criteria);
        return leaseTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseTemplate> createSpecification(LeaseTemplateCriteria criteria) {
        Specification<LeaseTemplate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseTemplate_.id));
            }
            if (criteria.getTemplateTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTemplateTitle(), LeaseTemplate_.templateTitle));
            }
            if (criteria.getAssetAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetAccountId(),
                            root -> root.join(LeaseTemplate_.assetAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getDepreciationAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepreciationAccountId(),
                            root -> root.join(LeaseTemplate_.depreciationAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getAccruedDepreciationAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAccruedDepreciationAccountId(),
                            root -> root.join(LeaseTemplate_.accruedDepreciationAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getInterestPaidTransferDebitAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInterestPaidTransferDebitAccountId(),
                            root -> root.join(LeaseTemplate_.interestPaidTransferDebitAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getInterestPaidTransferCreditAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInterestPaidTransferCreditAccountId(),
                            root -> root.join(LeaseTemplate_.interestPaidTransferCreditAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getInterestAccruedDebitAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInterestAccruedDebitAccountId(),
                            root -> root.join(LeaseTemplate_.interestAccruedDebitAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getInterestAccruedCreditAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getInterestAccruedCreditAccountId(),
                            root -> root.join(LeaseTemplate_.interestAccruedCreditAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getLeaseRecognitionDebitAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseRecognitionDebitAccountId(),
                            root -> root.join(LeaseTemplate_.leaseRecognitionDebitAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getLeaseRecognitionCreditAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseRecognitionCreditAccountId(),
                            root -> root.join(LeaseTemplate_.leaseRecognitionCreditAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getLeaseRepaymentDebitAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseRepaymentDebitAccountId(),
                            root -> root.join(LeaseTemplate_.leaseRepaymentDebitAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getLeaseRepaymentCreditAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseRepaymentCreditAccountId(),
                            root -> root.join(LeaseTemplate_.leaseRepaymentCreditAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getRouRecognitionCreditAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRouRecognitionCreditAccountId(),
                            root -> root.join(LeaseTemplate_.rouRecognitionCreditAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getRouRecognitionDebitAccountId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRouRecognitionDebitAccountId(),
                            root -> root.join(LeaseTemplate_.rouRecognitionDebitAccount, JoinType.LEFT).get(TransactionAccount_.id)
                        )
                    );
            }
            if (criteria.getAssetCategoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getAssetCategoryId(),
                            root -> root.join(LeaseTemplate_.assetCategory, JoinType.LEFT).get(AssetCategory_.id)
                        )
                    );
            }
            if (criteria.getServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getServiceOutletId(),
                            root -> root.join(LeaseTemplate_.serviceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getMainDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMainDealerId(),
                            root -> root.join(LeaseTemplate_.mainDealer, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(LeaseTemplate_.leaseContracts, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
