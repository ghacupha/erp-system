package io.github.erp.service;

/*-
 * Erp System - Mark X No 9 (Jehoiada Series) Server ver 1.8.1
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
import io.github.erp.domain.IFRS16LeaseContract;
import io.github.erp.repository.IFRS16LeaseContractRepository;
import io.github.erp.repository.search.IFRS16LeaseContractSearchRepository;
import io.github.erp.service.criteria.IFRS16LeaseContractCriteria;
import io.github.erp.service.dto.IFRS16LeaseContractDTO;
import io.github.erp.service.mapper.IFRS16LeaseContractMapper;
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
 * Service for executing complex queries for {@link IFRS16LeaseContract} entities in the database.
 * The main input is a {@link IFRS16LeaseContractCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link IFRS16LeaseContractDTO} or a {@link Page} of {@link IFRS16LeaseContractDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class IFRS16LeaseContractQueryService extends QueryService<IFRS16LeaseContract> {

    private final Logger log = LoggerFactory.getLogger(IFRS16LeaseContractQueryService.class);

    private final IFRS16LeaseContractRepository iFRS16LeaseContractRepository;

    private final IFRS16LeaseContractMapper iFRS16LeaseContractMapper;

    private final IFRS16LeaseContractSearchRepository iFRS16LeaseContractSearchRepository;

    public IFRS16LeaseContractQueryService(
        IFRS16LeaseContractRepository iFRS16LeaseContractRepository,
        IFRS16LeaseContractMapper iFRS16LeaseContractMapper,
        IFRS16LeaseContractSearchRepository iFRS16LeaseContractSearchRepository
    ) {
        this.iFRS16LeaseContractRepository = iFRS16LeaseContractRepository;
        this.iFRS16LeaseContractMapper = iFRS16LeaseContractMapper;
        this.iFRS16LeaseContractSearchRepository = iFRS16LeaseContractSearchRepository;
    }

    /**
     * Return a {@link List} of {@link IFRS16LeaseContractDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<IFRS16LeaseContractDTO> findByCriteria(IFRS16LeaseContractCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<IFRS16LeaseContract> specification = createSpecification(criteria);
        return iFRS16LeaseContractMapper.toDto(iFRS16LeaseContractRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link IFRS16LeaseContractDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<IFRS16LeaseContractDTO> findByCriteria(IFRS16LeaseContractCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<IFRS16LeaseContract> specification = createSpecification(criteria);
        return iFRS16LeaseContractRepository.findAll(specification, page).map(iFRS16LeaseContractMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(IFRS16LeaseContractCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<IFRS16LeaseContract> specification = createSpecification(criteria);
        return iFRS16LeaseContractRepository.count(specification);
    }

    /**
     * Function to convert {@link IFRS16LeaseContractCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<IFRS16LeaseContract> createSpecification(IFRS16LeaseContractCriteria criteria) {
        Specification<IFRS16LeaseContract> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), IFRS16LeaseContract_.id));
            }
            if (criteria.getBookingId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBookingId(), IFRS16LeaseContract_.bookingId));
            }
            if (criteria.getLeaseTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeaseTitle(), IFRS16LeaseContract_.leaseTitle));
            }
            if (criteria.getShortTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortTitle(), IFRS16LeaseContract_.shortTitle));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), IFRS16LeaseContract_.description));
            }
            if (criteria.getInceptionDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getInceptionDate(), IFRS16LeaseContract_.inceptionDate));
            }
            if (criteria.getCommencementDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCommencementDate(), IFRS16LeaseContract_.commencementDate));
            }
            if (criteria.getSerialNumber() != null) {
                specification = specification.and(buildSpecification(criteria.getSerialNumber(), IFRS16LeaseContract_.serialNumber));
            }
            if (criteria.getSuperintendentServiceOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSuperintendentServiceOutletId(),
                            root -> root.join(IFRS16LeaseContract_.superintendentServiceOutlet, JoinType.LEFT).get(ServiceOutlet_.id)
                        )
                    );
            }
            if (criteria.getMainDealerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getMainDealerId(),
                            root -> root.join(IFRS16LeaseContract_.mainDealer, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getFirstReportingPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getFirstReportingPeriodId(),
                            root -> root.join(IFRS16LeaseContract_.firstReportingPeriod, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
            if (criteria.getLastReportingPeriodId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLastReportingPeriodId(),
                            root -> root.join(IFRS16LeaseContract_.lastReportingPeriod, JoinType.LEFT).get(FiscalMonth_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractDocumentId(),
                            root -> root.join(IFRS16LeaseContract_.leaseContractDocument, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getLeaseContractCalculationsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeaseContractCalculationsId(),
                            root -> root.join(IFRS16LeaseContract_.leaseContractCalculations, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
