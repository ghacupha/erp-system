package io.github.erp.service;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
 * Copyright © 2021 - 2023 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
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
import io.github.erp.domain.RelatedPartyRelationship;
import io.github.erp.repository.RelatedPartyRelationshipRepository;
import io.github.erp.repository.search.RelatedPartyRelationshipSearchRepository;
import io.github.erp.service.criteria.RelatedPartyRelationshipCriteria;
import io.github.erp.service.dto.RelatedPartyRelationshipDTO;
import io.github.erp.service.mapper.RelatedPartyRelationshipMapper;
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
 * Service for executing complex queries for {@link RelatedPartyRelationship} entities in the database.
 * The main input is a {@link RelatedPartyRelationshipCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link RelatedPartyRelationshipDTO} or a {@link Page} of {@link RelatedPartyRelationshipDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RelatedPartyRelationshipQueryService extends QueryService<RelatedPartyRelationship> {

    private final Logger log = LoggerFactory.getLogger(RelatedPartyRelationshipQueryService.class);

    private final RelatedPartyRelationshipRepository relatedPartyRelationshipRepository;

    private final RelatedPartyRelationshipMapper relatedPartyRelationshipMapper;

    private final RelatedPartyRelationshipSearchRepository relatedPartyRelationshipSearchRepository;

    public RelatedPartyRelationshipQueryService(
        RelatedPartyRelationshipRepository relatedPartyRelationshipRepository,
        RelatedPartyRelationshipMapper relatedPartyRelationshipMapper,
        RelatedPartyRelationshipSearchRepository relatedPartyRelationshipSearchRepository
    ) {
        this.relatedPartyRelationshipRepository = relatedPartyRelationshipRepository;
        this.relatedPartyRelationshipMapper = relatedPartyRelationshipMapper;
        this.relatedPartyRelationshipSearchRepository = relatedPartyRelationshipSearchRepository;
    }

    /**
     * Return a {@link List} of {@link RelatedPartyRelationshipDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<RelatedPartyRelationshipDTO> findByCriteria(RelatedPartyRelationshipCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<RelatedPartyRelationship> specification = createSpecification(criteria);
        return relatedPartyRelationshipMapper.toDto(relatedPartyRelationshipRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link RelatedPartyRelationshipDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RelatedPartyRelationshipDTO> findByCriteria(RelatedPartyRelationshipCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RelatedPartyRelationship> specification = createSpecification(criteria);
        return relatedPartyRelationshipRepository.findAll(specification, page).map(relatedPartyRelationshipMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RelatedPartyRelationshipCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<RelatedPartyRelationship> specification = createSpecification(criteria);
        return relatedPartyRelationshipRepository.count(specification);
    }

    /**
     * Function to convert {@link RelatedPartyRelationshipCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RelatedPartyRelationship> createSpecification(RelatedPartyRelationshipCriteria criteria) {
        Specification<RelatedPartyRelationship> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), RelatedPartyRelationship_.id));
            }
            if (criteria.getReportingDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getReportingDate(), RelatedPartyRelationship_.reportingDate));
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCustomerId(), RelatedPartyRelationship_.customerId));
            }
            if (criteria.getRelatedPartyId() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getRelatedPartyId(), RelatedPartyRelationship_.relatedPartyId));
            }
            if (criteria.getBankCodeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBankCodeId(),
                            root -> root.join(RelatedPartyRelationship_.bankCode, JoinType.LEFT).get(InstitutionCode_.id)
                        )
                    );
            }
            if (criteria.getBranchIdId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getBranchIdId(),
                            root -> root.join(RelatedPartyRelationship_.branchId, JoinType.LEFT).get(BankBranchCode_.id)
                        )
                    );
            }
            if (criteria.getRelationshipTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getRelationshipTypeId(),
                            root -> root.join(RelatedPartyRelationship_.relationshipType, JoinType.LEFT).get(PartyRelationType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
