package io.github.erp.service;

/*-
 * Erp System - Mark III No 16 (Caleb Series) Server ver 1.2.7
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
import io.github.erp.domain.InstitutionCode;
import io.github.erp.repository.InstitutionCodeRepository;
import io.github.erp.repository.search.InstitutionCodeSearchRepository;
import io.github.erp.service.criteria.InstitutionCodeCriteria;
import io.github.erp.service.dto.InstitutionCodeDTO;
import io.github.erp.service.mapper.InstitutionCodeMapper;
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
 * Service for executing complex queries for {@link InstitutionCode} entities in the database.
 * The main input is a {@link InstitutionCodeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link InstitutionCodeDTO} or a {@link Page} of {@link InstitutionCodeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InstitutionCodeQueryService extends QueryService<InstitutionCode> {

    private final Logger log = LoggerFactory.getLogger(InstitutionCodeQueryService.class);

    private final InstitutionCodeRepository institutionCodeRepository;

    private final InstitutionCodeMapper institutionCodeMapper;

    private final InstitutionCodeSearchRepository institutionCodeSearchRepository;

    public InstitutionCodeQueryService(
        InstitutionCodeRepository institutionCodeRepository,
        InstitutionCodeMapper institutionCodeMapper,
        InstitutionCodeSearchRepository institutionCodeSearchRepository
    ) {
        this.institutionCodeRepository = institutionCodeRepository;
        this.institutionCodeMapper = institutionCodeMapper;
        this.institutionCodeSearchRepository = institutionCodeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link InstitutionCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<InstitutionCodeDTO> findByCriteria(InstitutionCodeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<InstitutionCode> specification = createSpecification(criteria);
        return institutionCodeMapper.toDto(institutionCodeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link InstitutionCodeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InstitutionCodeDTO> findByCriteria(InstitutionCodeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InstitutionCode> specification = createSpecification(criteria);
        return institutionCodeRepository.findAll(specification, page).map(institutionCodeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InstitutionCodeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<InstitutionCode> specification = createSpecification(criteria);
        return institutionCodeRepository.count(specification);
    }

    /**
     * Function to convert {@link InstitutionCodeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InstitutionCode> createSpecification(InstitutionCodeCriteria criteria) {
        Specification<InstitutionCode> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), InstitutionCode_.id));
            }
            if (criteria.getInstitutionCode() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInstitutionCode(), InstitutionCode_.institutionCode));
            }
            if (criteria.getInstitutionName() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInstitutionName(), InstitutionCode_.institutionName));
            }
            if (criteria.getShortName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getShortName(), InstitutionCode_.shortName));
            }
            if (criteria.getCategory() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCategory(), InstitutionCode_.category));
            }
            if (criteria.getInstitutionCategory() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getInstitutionCategory(), InstitutionCode_.institutionCategory));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(InstitutionCode_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
