package io.github.erp.service;

/*-
 * Copyright © 2021 - 2024 Edwin Njeru and the ERP System Contributors (mailnjeru@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
