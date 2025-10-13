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
import io.github.erp.domain.InstitutionContactDetails;
import io.github.erp.repository.InstitutionContactDetailsRepository;
import io.github.erp.repository.search.InstitutionContactDetailsSearchRepository;
import io.github.erp.service.criteria.InstitutionContactDetailsCriteria;
import io.github.erp.service.dto.InstitutionContactDetailsDTO;
import io.github.erp.service.mapper.InstitutionContactDetailsMapper;
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
 * Service for executing complex queries for {@link InstitutionContactDetails} entities in the database.
 * The main input is a {@link InstitutionContactDetailsCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InstitutionContactDetailsDTO} or a {@link Page} of {@link InstitutionContactDetailsDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InstitutionContactDetailsQueryService extends QueryService<InstitutionContactDetails> {

    private final Logger log = LoggerFactory.getLogger(InstitutionContactDetailsQueryService.class);

    private final InstitutionContactDetailsRepository institutionContactDetailsRepository;

    private final InstitutionContactDetailsMapper institutionContactDetailsMapper;

    private final InstitutionContactDetailsSearchRepository institutionContactDetailsSearchRepository;

    public InstitutionContactDetailsQueryService(
        InstitutionContactDetailsRepository institutionContactDetailsRepository,
        InstitutionContactDetailsMapper institutionContactDetailsMapper,
        InstitutionContactDetailsSearchRepository institutionContactDetailsSearchRepository
    ) {
        this.institutionContactDetailsRepository = institutionContactDetailsRepository;
        this.institutionContactDetailsMapper = institutionContactDetailsMapper;
        this.institutionContactDetailsSearchRepository = institutionContactDetailsSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InstitutionContactDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InstitutionContactDetailsDTO> findByCriteria(InstitutionContactDetailsCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InstitutionContactDetails> specification = createSpecification(criteria);
        return institutionContactDetailsMapper.toDto(institutionContactDetailsRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InstitutionContactDetailsDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InstitutionContactDetailsDTO> findByCriteria(InstitutionContactDetailsCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InstitutionContactDetails> specification = createSpecification(criteria);
        return institutionContactDetailsRepository.findAll(specification, page).map(institutionContactDetailsMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InstitutionContactDetailsCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InstitutionContactDetails> specification = createSpecification(criteria);
        return institutionContactDetailsRepository.count(specification);
    }

    /**
     * Function to convert {@link InstitutionContactDetailsCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InstitutionContactDetails> createSpecification(InstitutionContactDetailsCriteria criteria) {
        Specification<InstitutionContactDetails> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InstitutionContactDetails_.id));
            }
            if (criteria.getEntityId() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEntityId(), InstitutionContactDetails_.entityId));
            }
            if (criteria.getEntityName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getEntityName(), InstitutionContactDetails_.entityName));
            }
            if (criteria.getContactType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContactType(), InstitutionContactDetails_.contactType));
            }
            if (criteria.getContactLevel() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContactLevel(), InstitutionContactDetails_.contactLevel));
            }
            if (criteria.getContactValue() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContactValue(), InstitutionContactDetails_.contactValue));
            }
            if (criteria.getContactName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getContactName(), InstitutionContactDetails_.contactName));
            }
            if (criteria.getContactDesignation() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getContactDesignation(), InstitutionContactDetails_.contactDesignation)
                    );
            }
        }
        return specification;
    }
}
