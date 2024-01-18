package io.github.erp.service;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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
import io.github.erp.domain.ParticularsOfOutlet;
import io.github.erp.repository.ParticularsOfOutletRepository;
import io.github.erp.repository.search.ParticularsOfOutletSearchRepository;
import io.github.erp.service.criteria.ParticularsOfOutletCriteria;
import io.github.erp.service.dto.ParticularsOfOutletDTO;
import io.github.erp.service.mapper.ParticularsOfOutletMapper;
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
 * Service for executing complex queries for {@link ParticularsOfOutlet} entities in the database.
 * The main input is a {@link ParticularsOfOutletCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ParticularsOfOutletDTO} or a {@link Page} of {@link ParticularsOfOutletDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ParticularsOfOutletQueryService extends QueryService<ParticularsOfOutlet> {

    private final Logger log = LoggerFactory.getLogger(ParticularsOfOutletQueryService.class);

    private final ParticularsOfOutletRepository particularsOfOutletRepository;

    private final ParticularsOfOutletMapper particularsOfOutletMapper;

    private final ParticularsOfOutletSearchRepository particularsOfOutletSearchRepository;

    public ParticularsOfOutletQueryService(
        ParticularsOfOutletRepository particularsOfOutletRepository,
        ParticularsOfOutletMapper particularsOfOutletMapper,
        ParticularsOfOutletSearchRepository particularsOfOutletSearchRepository
    ) {
        this.particularsOfOutletRepository = particularsOfOutletRepository;
        this.particularsOfOutletMapper = particularsOfOutletMapper;
        this.particularsOfOutletSearchRepository = particularsOfOutletSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ParticularsOfOutletDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ParticularsOfOutletDTO> findByCriteria(ParticularsOfOutletCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ParticularsOfOutlet> specification = createSpecification(criteria);
        return particularsOfOutletMapper.toDto(particularsOfOutletRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ParticularsOfOutletDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ParticularsOfOutletDTO> findByCriteria(ParticularsOfOutletCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ParticularsOfOutlet> specification = createSpecification(criteria);
        return particularsOfOutletRepository.findAll(specification, page).map(particularsOfOutletMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ParticularsOfOutletCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ParticularsOfOutlet> specification = createSpecification(criteria);
        return particularsOfOutletRepository.count(specification);
    }

    /**
     * Function to convert {@link ParticularsOfOutletCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ParticularsOfOutlet> createSpecification(ParticularsOfOutletCriteria criteria) {
        Specification<ParticularsOfOutlet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ParticularsOfOutlet_.id));
            }
            if (criteria.getBusinessReportingDate() != null) {
                specification =
                    specification.and(
                        buildRangeSpecification(criteria.getBusinessReportingDate(), ParticularsOfOutlet_.businessReportingDate)
                    );
            }
            if (criteria.getOutletName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutletName(), ParticularsOfOutlet_.outletName));
            }
            if (criteria.getTown() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTown(), ParticularsOfOutlet_.town));
            }
            if (criteria.getIso6709Latitute() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getIso6709Latitute(), ParticularsOfOutlet_.iso6709Latitute));
            }
            if (criteria.getIso6709Longitude() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getIso6709Longitude(), ParticularsOfOutlet_.iso6709Longitude));
            }
            if (criteria.getCbkApprovalDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getCbkApprovalDate(), ParticularsOfOutlet_.cbkApprovalDate));
            }
            if (criteria.getOutletOpeningDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutletOpeningDate(), ParticularsOfOutlet_.outletOpeningDate));
            }
            if (criteria.getOutletClosureDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutletClosureDate(), ParticularsOfOutlet_.outletClosureDate));
            }
            if (criteria.getLicenseFeePayable() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLicenseFeePayable(), ParticularsOfOutlet_.licenseFeePayable));
            }
            if (criteria.getSubCountyCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubCountyCodeId(),
                            root -> root.join(ParticularsOfOutlet_.subCountyCode, JoinType.LEFT).get(CountySubCountyCode_.id)
                        )
                    );
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(ParticularsOfOutlet_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getOutletIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOutletIdId(),
                            root -> root.join(ParticularsOfOutlet_.outletId, JoinType.LEFT).get(BankBranchCode_.id)
                        )
                    );
            }
            if (criteria.getTypeOfOutletId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTypeOfOutletId(),
                            root -> root.join(ParticularsOfOutlet_.typeOfOutlet, JoinType.LEFT).get(OutletType_.id)
                        )
                    );
            }
            if (criteria.getOutletStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOutletStatusId(),
                            root -> root.join(ParticularsOfOutlet_.outletStatus, JoinType.LEFT).get(OutletStatus_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
