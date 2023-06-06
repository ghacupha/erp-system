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
import io.github.erp.domain.LeaseContract;
import io.github.erp.repository.LeaseContractRepository;
import io.github.erp.repository.search.LeaseContractSearchRepository;
import io.github.erp.service.criteria.LeaseContractCriteria;
import io.github.erp.service.dto.LeaseContractDTO;
import io.github.erp.service.mapper.LeaseContractMapper;
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
 * Service for executing complex queries for {@link LeaseContract} entities in the database.
 * The main input is a {@link LeaseContractCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeaseContractDTO} or a {@link Page} of {@link LeaseContractDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeaseContractQueryService extends QueryService<LeaseContract> {

    private final Logger log = LoggerFactory.getLogger(LeaseContractQueryService.class);

    private final LeaseContractRepository leaseContractRepository;

    private final LeaseContractMapper leaseContractMapper;

    private final LeaseContractSearchRepository leaseContractSearchRepository;

    public LeaseContractQueryService(
        LeaseContractRepository leaseContractRepository,
        LeaseContractMapper leaseContractMapper,
        LeaseContractSearchRepository leaseContractSearchRepository
    ) {
        this.leaseContractRepository = leaseContractRepository;
        this.leaseContractMapper = leaseContractMapper;
        this.leaseContractSearchRepository = leaseContractSearchRepository;
    }

    /**
     * Return a {@link List} of {@link LeaseContractDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeaseContractDTO> findByCriteria(LeaseContractCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<LeaseContract> specification = createSpecification(criteria);
        return leaseContractMapper.toDto(leaseContractRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeaseContractDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeaseContractDTO> findByCriteria(LeaseContractCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeaseContract> specification = createSpecification(criteria);
        return leaseContractRepository.findAll(specification, page).map(leaseContractMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeaseContractCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<LeaseContract> specification = createSpecification(criteria);
        return leaseContractRepository.count(specification);
    }

    /**
     * Function to convert {@link LeaseContractCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeaseContract> createSpecification(LeaseContractCriteria criteria) {
        Specification<LeaseContract> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), LeaseContract_.id));
            }
            if (criteria.getBookingId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getBookingId(), LeaseContract_.bookingId));
            }
            if (criteria.getLeaseTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeaseTitle(), LeaseContract_.leaseTitle));
            }
            if (criteria.getIdentifier() != null) {
                specification = specification.and(buildSpecification(criteria.getIdentifier(), LeaseContract_.identifier));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), LeaseContract_.description));
            }
            if (criteria.getCommencementDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCommencementDate(), LeaseContract_.commencementDate));
            }
            if (criteria.getTerminalDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTerminalDate(), LeaseContract_.terminalDate));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(LeaseContract_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getSystemMappingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSystemMappingsId(),
                            root -> root.join(LeaseContract_.systemMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
            if (criteria.getBusinessDocumentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBusinessDocumentId(),
                            root -> root.join(LeaseContract_.businessDocuments, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getContractMetadataId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContractMetadataId(),
                            root -> root.join(LeaseContract_.contractMetadata, JoinType.LEFT).get(ContractMetadata_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
