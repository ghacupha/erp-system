package io.github.erp.service;

/*-
 * Erp System - Mark III No 14 (Caleb Series) Server ver 1.1.4-SNAPSHOT
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
import io.github.erp.domain.ServiceOutlet;
import io.github.erp.repository.ServiceOutletRepository;
import io.github.erp.repository.search.ServiceOutletSearchRepository;
import io.github.erp.service.criteria.ServiceOutletCriteria;
import io.github.erp.service.dto.ServiceOutletDTO;
import io.github.erp.service.mapper.ServiceOutletMapper;
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
 * Service for executing complex queries for {@link ServiceOutlet} entities in the database.
 * The main input is a {@link ServiceOutletCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ServiceOutletDTO} or a {@link Page} of {@link ServiceOutletDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ServiceOutletQueryService extends QueryService<ServiceOutlet> {

    private final Logger log = LoggerFactory.getLogger(ServiceOutletQueryService.class);

    private final ServiceOutletRepository serviceOutletRepository;

    private final ServiceOutletMapper serviceOutletMapper;

    private final ServiceOutletSearchRepository serviceOutletSearchRepository;

    public ServiceOutletQueryService(
        ServiceOutletRepository serviceOutletRepository,
        ServiceOutletMapper serviceOutletMapper,
        ServiceOutletSearchRepository serviceOutletSearchRepository
    ) {
        this.serviceOutletRepository = serviceOutletRepository;
        this.serviceOutletMapper = serviceOutletMapper;
        this.serviceOutletSearchRepository = serviceOutletSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ServiceOutletDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ServiceOutletDTO> findByCriteria(ServiceOutletCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ServiceOutlet> specification = createSpecification(criteria);
        return serviceOutletMapper.toDto(serviceOutletRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ServiceOutletDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ServiceOutletDTO> findByCriteria(ServiceOutletCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ServiceOutlet> specification = createSpecification(criteria);
        return serviceOutletRepository.findAll(specification, page).map(serviceOutletMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ServiceOutletCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ServiceOutlet> specification = createSpecification(criteria);
        return serviceOutletRepository.count(specification);
    }

    /**
     * Function to convert {@link ServiceOutletCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ServiceOutlet> createSpecification(ServiceOutletCriteria criteria) {
        Specification<ServiceOutlet> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ServiceOutlet_.id));
            }
            if (criteria.getOutletCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutletCode(), ServiceOutlet_.outletCode));
            }
            if (criteria.getOutletName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutletName(), ServiceOutlet_.outletName));
            }
            if (criteria.getTown() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTown(), ServiceOutlet_.town));
            }
            if (criteria.getParliamentaryConstituency() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getParliamentaryConstituency(), ServiceOutlet_.parliamentaryConstituency)
                    );
            }
            if (criteria.getGpsCoordinates() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGpsCoordinates(), ServiceOutlet_.gpsCoordinates));
            }
            if (criteria.getOutletOpeningDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutletOpeningDate(), ServiceOutlet_.outletOpeningDate));
            }
            if (criteria.getRegulatorApprovalDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getRegulatorApprovalDate(), ServiceOutlet_.regulatorApprovalDate));
            }
            if (criteria.getOutletClosureDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getOutletClosureDate(), ServiceOutlet_.outletClosureDate));
            }
            if (criteria.getDateLastModified() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDateLastModified(), ServiceOutlet_.dateLastModified));
            }
            if (criteria.getLicenseFeePayable() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getLicenseFeePayable(), ServiceOutlet_.licenseFeePayable));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(ServiceOutlet_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(ServiceOutlet_.bankCode, JoinType.LEFT).get(BankBranchCode_.id)
                        )
                    );
            }
            if (criteria.getOutletTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOutletTypeId(),
                            root -> root.join(ServiceOutlet_.outletType, JoinType.LEFT).get(OutletType_.id)
                        )
                    );
            }
            if (criteria.getOutletStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOutletStatusId(),
                            root -> root.join(ServiceOutlet_.outletStatus, JoinType.LEFT).get(OutletStatus_.id)
                        )
                    );
            }
            if (criteria.getCountyNameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCountyNameId(),
                            root -> root.join(ServiceOutlet_.countyName, JoinType.LEFT).get(CountyCode_.id)
                        )
                    );
            }
            if (criteria.getSubCountyNameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubCountyNameId(),
                            root -> root.join(ServiceOutlet_.subCountyName, JoinType.LEFT).get(CountyCode_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
