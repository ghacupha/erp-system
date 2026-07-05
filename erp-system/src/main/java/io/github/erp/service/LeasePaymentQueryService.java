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
import io.github.erp.domain.LeasePayment;
import io.github.erp.repository.LeasePaymentRepository;
import io.github.erp.repository.search.LeasePaymentSearchRepository;
import io.github.erp.service.criteria.LeasePaymentCriteria;
import io.github.erp.service.dto.LeasePaymentDTO;
import io.github.erp.service.mapper.LeasePaymentMapper;
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
 * Service for executing complex queries for {@link LeasePayment} entities in the database.
 * The main input is a {@link LeasePaymentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeasePaymentDTO} or a {@link Page} of {@link LeasePaymentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeasePaymentQueryService extends QueryService<LeasePayment> {

    private final Logger log = LoggerFactory.getLogger(LeasePaymentQueryService.class);

    private final LeasePaymentRepository leasePaymentRepository;

    private final LeasePaymentMapper leasePaymentMapper;

    private final LeasePaymentSearchRepository leasePaymentSearchRepository;

    public LeasePaymentQueryService(
        LeasePaymentRepository leasePaymentRepository,
        LeasePaymentMapper leasePaymentMapper,
        LeasePaymentSearchRepository leasePaymentSearchRepository
    ) {
        this.leasePaymentRepository = leasePaymentRepository;
        this.leasePaymentMapper = leasePaymentMapper;
        this.leasePaymentSearchRepository = leasePaymentSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeasePaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeasePaymentDTO> findByCriteria(LeasePaymentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeasePayment> specification = createSpecification(criteria);
        return leasePaymentMapper.toDto(leasePaymentRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeasePaymentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeasePaymentDTO> findByCriteria(LeasePaymentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeasePayment> specification = createSpecification(criteria);
        return leasePaymentRepository.findAll(specification, page).map(leasePaymentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeasePaymentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeasePayment> specification = createSpecification(criteria);
        return leasePaymentRepository.count(specification);
    }

    /**
     * Function to convert {@link LeasePaymentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeasePayment> createSpecification(LeasePaymentCriteria criteria) {
        Specification<LeasePayment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeasePayment_.id));
            }
            if (criteria.getPaymentAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentAmount(), LeasePayment_.paymentAmount));
            }
            if (criteria.getPaymentDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPaymentDate(), LeasePayment_.paymentDate));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), LeasePayment_.active));
            }
            if (criteria.getLeasePaymentUploadId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeasePaymentUploadId(),
                            root -> root.join(LeasePayment_.leasePaymentUpload, JoinType.LEFT).get(LeasePaymentUpload_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractId(),
                            root -> root.join(LeasePayment_.leaseContract, JoinType.LEFT).get(IFRS16LeaseContract_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
