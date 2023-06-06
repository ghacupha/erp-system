package io.github.erp.service;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.5
 * Copyright © 2021 - 2023 Edwin Njeru (mailnjeru@gmail.com)
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
import io.github.erp.domain.Dealer;
import io.github.erp.repository.DealerRepository;
import io.github.erp.repository.search.DealerSearchRepository;
import io.github.erp.service.criteria.DealerCriteria;
import io.github.erp.service.dto.DealerDTO;
import io.github.erp.service.mapper.DealerMapper;
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
 * Service for executing complex queries for {@link Dealer} entities in the database.
 * The main input is a {@link DealerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link DealerDTO} or a {@link Page} of {@link DealerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DealerQueryService extends QueryService<Dealer> {

    private final Logger log = LoggerFactory.getLogger(DealerQueryService.class);

    private final DealerRepository dealerRepository;

    private final DealerMapper dealerMapper;

    private final DealerSearchRepository dealerSearchRepository;

    public DealerQueryService(DealerRepository dealerRepository, DealerMapper dealerMapper, DealerSearchRepository dealerSearchRepository) {
        this.dealerRepository = dealerRepository;
        this.dealerMapper = dealerMapper;
        this.dealerSearchRepository = dealerSearchRepository;
    }

    /**
     * Return a {@link List} of {@link DealerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<DealerDTO> findByCriteria(DealerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Dealer> specification = createSpecification(criteria);
        return dealerMapper.toDto(dealerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link DealerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DealerDTO> findByCriteria(DealerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Dealer> specification = createSpecification(criteria);
        return dealerRepository.findAll(specification, page).map(dealerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DealerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Dealer> specification = createSpecification(criteria);
        return dealerRepository.count(specification);
    }

    /**
     * Function to convert {@link DealerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Dealer> createSpecification(DealerCriteria criteria) {
        Specification<Dealer> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Dealer_.id));
            }
            if (criteria.getDealerName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDealerName(), Dealer_.dealerName));
            }
            if (criteria.getTaxNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTaxNumber(), Dealer_.taxNumber));
            }
            if (criteria.getIdentificationDocumentNumber() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getIdentificationDocumentNumber(), Dealer_.identificationDocumentNumber)
                    );
            }
            if (criteria.getOrganizationName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOrganizationName(), Dealer_.organizationName));
            }
            if (criteria.getDepartment() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDepartment(), Dealer_.department));
            }
            if (criteria.getPosition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPosition(), Dealer_.position));
            }
            if (criteria.getPostalAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPostalAddress(), Dealer_.postalAddress));
            }
            if (criteria.getPhysicalAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhysicalAddress(), Dealer_.physicalAddress));
            }
            if (criteria.getAccountName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountName(), Dealer_.accountName));
            }
            if (criteria.getAccountNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAccountNumber(), Dealer_.accountNumber));
            }
            if (criteria.getBankersName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankersName(), Dealer_.bankersName));
            }
            if (criteria.getBankersBranch() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankersBranch(), Dealer_.bankersBranch));
            }
            if (criteria.getBankersSwiftCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBankersSwiftCode(), Dealer_.bankersSwiftCode));
            }
            if (criteria.getFileUploadToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFileUploadToken(), Dealer_.fileUploadToken));
            }
            if (criteria.getCompilationToken() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCompilationToken(), Dealer_.compilationToken));
            }
            if (criteria.getOtherNames() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOtherNames(), Dealer_.otherNames));
            }
            if (criteria.getPaymentLabelId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentLabelId(),
                            root -> root.join(Dealer_.paymentLabels, JoinType.LEFT).get(PaymentLabel_.id)
                        )
                    );
            }
            if (criteria.getDealerGroupId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDealerGroupId(),
                            root -> root.join(Dealer_.dealerGroup, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(Dealer_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
