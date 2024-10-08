package io.github.erp.service;

/*-
 * Erp System - Mark X No 10 (Jehoiada Series) Server ver 1.8.2
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
import io.github.erp.domain.TerminalsAndPOS;
import io.github.erp.repository.TerminalsAndPOSRepository;
import io.github.erp.repository.search.TerminalsAndPOSSearchRepository;
import io.github.erp.service.criteria.TerminalsAndPOSCriteria;
import io.github.erp.service.dto.TerminalsAndPOSDTO;
import io.github.erp.service.mapper.TerminalsAndPOSMapper;
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
 * Service for executing complex queries for {@link TerminalsAndPOS} entities in the database.
 * The main input is a {@link TerminalsAndPOSCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TerminalsAndPOSDTO} or a {@link Page} of {@link TerminalsAndPOSDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TerminalsAndPOSQueryService extends QueryService<TerminalsAndPOS> {

    private final Logger log = LoggerFactory.getLogger(TerminalsAndPOSQueryService.class);

    private final TerminalsAndPOSRepository terminalsAndPOSRepository;

    private final TerminalsAndPOSMapper terminalsAndPOSMapper;

    private final TerminalsAndPOSSearchRepository terminalsAndPOSSearchRepository;

    public TerminalsAndPOSQueryService(
        TerminalsAndPOSRepository terminalsAndPOSRepository,
        TerminalsAndPOSMapper terminalsAndPOSMapper,
        TerminalsAndPOSSearchRepository terminalsAndPOSSearchRepository
    ) {
        this.terminalsAndPOSRepository = terminalsAndPOSRepository;
        this.terminalsAndPOSMapper = terminalsAndPOSMapper;
        this.terminalsAndPOSSearchRepository = terminalsAndPOSSearchRepository;
    }

    /**
     * Return a {@link List} of {@link TerminalsAndPOSDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TerminalsAndPOSDTO> findByCriteria(TerminalsAndPOSCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TerminalsAndPOS> specification = createSpecification(criteria);
        return terminalsAndPOSMapper.toDto(terminalsAndPOSRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TerminalsAndPOSDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TerminalsAndPOSDTO> findByCriteria(TerminalsAndPOSCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TerminalsAndPOS> specification = createSpecification(criteria);
        return terminalsAndPOSRepository.findAll(specification, page).map(terminalsAndPOSMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TerminalsAndPOSCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TerminalsAndPOS> specification = createSpecification(criteria);
        return terminalsAndPOSRepository.count(specification);
    }

    /**
     * Function to convert {@link TerminalsAndPOSCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TerminalsAndPOS> createSpecification(TerminalsAndPOSCriteria criteria) {
        Specification<TerminalsAndPOS> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TerminalsAndPOS_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReportingDate(), TerminalsAndPOS_.reportingDate));
            }
            if (criteria.getTerminalId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTerminalId(), TerminalsAndPOS_.terminalId));
            }
            if (criteria.getMerchantId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getMerchantId(), TerminalsAndPOS_.merchantId));
            }
            if (criteria.getTerminalName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTerminalName(), TerminalsAndPOS_.terminalName));
            }
            if (criteria.getTerminalLocation() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getTerminalLocation(), TerminalsAndPOS_.terminalLocation));
            }
            if (criteria.getIso6709Latitute() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIso6709Latitute(), TerminalsAndPOS_.iso6709Latitute));
            }
            if (criteria.getIso6709Longitude() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getIso6709Longitude(), TerminalsAndPOS_.iso6709Longitude));
            }
            if (criteria.getTerminalOpeningDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTerminalOpeningDate(), TerminalsAndPOS_.terminalOpeningDate));
            }
            if (criteria.getTerminalClosureDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTerminalClosureDate(), TerminalsAndPOS_.terminalClosureDate));
            }
            if (criteria.getTerminalTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTerminalTypeId(),
                            root -> root.join(TerminalsAndPOS_.terminalType, JoinType.LEFT).get(TerminalTypes_.id)
                        )
                    );
            }
            if (criteria.getTerminalFunctionalityId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTerminalFunctionalityId(),
                            root -> root.join(TerminalsAndPOS_.terminalFunctionality, JoinType.LEFT).get(TerminalFunctions_.id)
                        )
                    );
            }
            if (criteria.getPhysicalLocationId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPhysicalLocationId(),
                            root -> root.join(TerminalsAndPOS_.physicalLocation, JoinType.LEFT).get(CountySubCountyCode_.id)
                        )
                    );
            }
            if (criteria.getBankIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankIdId(),
                            root -> root.join(TerminalsAndPOS_.bankId, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getBranchIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchIdId(),
                            root -> root.join(TerminalsAndPOS_.branchId, JoinType.LEFT).get(BankBranchCode_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
