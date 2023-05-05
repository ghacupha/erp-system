package io.github.erp.service;

/*-
 * Erp System - Mark III No 15 (Caleb Series) Server ver 1.2.0-SNAPSHOT
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
import io.github.erp.domain.ContractMetadata;
import io.github.erp.repository.ContractMetadataRepository;
import io.github.erp.repository.search.ContractMetadataSearchRepository;
import io.github.erp.service.criteria.ContractMetadataCriteria;
import io.github.erp.service.dto.ContractMetadataDTO;
import io.github.erp.service.mapper.ContractMetadataMapper;
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
 * Service for executing complex queries for {@link ContractMetadata} entities in the database.
 * The main input is a {@link ContractMetadataCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ContractMetadataDTO} or a {@link Page} of {@link ContractMetadataDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ContractMetadataQueryService extends QueryService<ContractMetadata> {

    private final Logger log = LoggerFactory.getLogger(ContractMetadataQueryService.class);

    private final ContractMetadataRepository contractMetadataRepository;

    private final ContractMetadataMapper contractMetadataMapper;

    private final ContractMetadataSearchRepository contractMetadataSearchRepository;

    public ContractMetadataQueryService(
        ContractMetadataRepository contractMetadataRepository,
        ContractMetadataMapper contractMetadataMapper,
        ContractMetadataSearchRepository contractMetadataSearchRepository
    ) {
        this.contractMetadataRepository = contractMetadataRepository;
        this.contractMetadataMapper = contractMetadataMapper;
        this.contractMetadataSearchRepository = contractMetadataSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ContractMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ContractMetadataDTO> findByCriteria(ContractMetadataCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ContractMetadata> specification = createSpecification(criteria);
        return contractMetadataMapper.toDto(contractMetadataRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ContractMetadataDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ContractMetadataDTO> findByCriteria(ContractMetadataCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ContractMetadata> specification = createSpecification(criteria);
        return contractMetadataRepository.findAll(specification, page).map(contractMetadataMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ContractMetadataCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ContractMetadata> specification = createSpecification(criteria);
        return contractMetadataRepository.count(specification);
    }

    /**
     * Function to convert {@link ContractMetadataCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ContractMetadata> createSpecification(ContractMetadataCriteria criteria) {
        Specification<ContractMetadata> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ContractMetadata_.id));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), ContractMetadata_.description));
            }
            if (criteria.getTypeOfContract() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeOfContract(), ContractMetadata_.typeOfContract));
            }
            if (criteria.getContractStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getContractStatus(), ContractMetadata_.contractStatus));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), ContractMetadata_.startDate));
            }
            if (criteria.getTerminationDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getTerminationDate(), ContractMetadata_.terminationDate));
            }
            if (criteria.getContractTitle() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContractTitle(), ContractMetadata_.contractTitle));
            }
            if (criteria.getContractIdentifier() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getContractIdentifier(), ContractMetadata_.contractIdentifier));
            }
            if (criteria.getContractIdentifierShort() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getContractIdentifierShort(), ContractMetadata_.contractIdentifierShort)
                    );
            }
            if (criteria.getRelatedContractsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRelatedContractsId(),
                            root -> root.join(ContractMetadata_.relatedContracts, JoinType.LEFT).get(ContractMetadata_.id)
                        )
                    );
            }
            if (criteria.getDepartmentId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getDepartmentId(),
                            root -> root.join(ContractMetadata_.department, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getContractPartnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContractPartnerId(),
                            root -> root.join(ContractMetadata_.contractPartner, JoinType.LEFT).get(Dealer_.id)
                        )
                    );
            }
            if (criteria.getResponsiblePersonId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getResponsiblePersonId(),
                            root -> root.join(ContractMetadata_.responsiblePerson, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getSignatoryId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSignatoryId(),
                            root -> root.join(ContractMetadata_.signatories, JoinType.LEFT).get(ApplicationUser_.id)
                        )
                    );
            }
            if (criteria.getSecurityClearanceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSecurityClearanceId(),
                            root -> root.join(ContractMetadata_.securityClearance, JoinType.LEFT).get(SecurityClearance_.id)
                        )
                    );
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(ContractMetadata_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
            if (criteria.getContractDocumentFileId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContractDocumentFileId(),
                            root -> root.join(ContractMetadata_.contractDocumentFiles, JoinType.LEFT).get(BusinessDocument_.id)
                        )
                    );
            }
            if (criteria.getContractMappingsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getContractMappingsId(),
                            root -> root.join(ContractMetadata_.contractMappings, JoinType.LEFT).get(UniversallyUniqueMapping_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
