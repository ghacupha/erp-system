package io.github.erp.service;

/*-
 * Erp System - Mark X No 8 (Jehoiada Series) Server ver 1.8.0
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
import io.github.erp.domain.CrbCreditFacilityType;
import io.github.erp.repository.CrbCreditFacilityTypeRepository;
import io.github.erp.repository.search.CrbCreditFacilityTypeSearchRepository;
import io.github.erp.service.criteria.CrbCreditFacilityTypeCriteria;
import io.github.erp.service.dto.CrbCreditFacilityTypeDTO;
import io.github.erp.service.mapper.CrbCreditFacilityTypeMapper;
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
 * Service for executing complex queries for {@link CrbCreditFacilityType} entities in the database.
 * The main input is a {@link CrbCreditFacilityTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrbCreditFacilityTypeDTO} or a {@link Page} of {@link CrbCreditFacilityTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrbCreditFacilityTypeQueryService extends QueryService<CrbCreditFacilityType> {

    private final Logger log = LoggerFactory.getLogger(CrbCreditFacilityTypeQueryService.class);

    private final CrbCreditFacilityTypeRepository crbCreditFacilityTypeRepository;

    private final CrbCreditFacilityTypeMapper crbCreditFacilityTypeMapper;

    private final CrbCreditFacilityTypeSearchRepository crbCreditFacilityTypeSearchRepository;

    public CrbCreditFacilityTypeQueryService(
        CrbCreditFacilityTypeRepository crbCreditFacilityTypeRepository,
        CrbCreditFacilityTypeMapper crbCreditFacilityTypeMapper,
        CrbCreditFacilityTypeSearchRepository crbCreditFacilityTypeSearchRepository
    ) {
        this.crbCreditFacilityTypeRepository = crbCreditFacilityTypeRepository;
        this.crbCreditFacilityTypeMapper = crbCreditFacilityTypeMapper;
        this.crbCreditFacilityTypeSearchRepository = crbCreditFacilityTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link CrbCreditFacilityTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrbCreditFacilityTypeDTO> findByCriteria(CrbCreditFacilityTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<CrbCreditFacilityType> specification = createSpecification(criteria);
        return crbCreditFacilityTypeMapper.toDto(crbCreditFacilityTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrbCreditFacilityTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrbCreditFacilityTypeDTO> findByCriteria(CrbCreditFacilityTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<CrbCreditFacilityType> specification = createSpecification(criteria);
        return crbCreditFacilityTypeRepository.findAll(specification, page).map(crbCreditFacilityTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrbCreditFacilityTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<CrbCreditFacilityType> specification = createSpecification(criteria);
        return crbCreditFacilityTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link CrbCreditFacilityTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<CrbCreditFacilityType> createSpecification(CrbCreditFacilityTypeCriteria criteria) {
        Specification<CrbCreditFacilityType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), CrbCreditFacilityType_.id));
            }
            if (criteria.getCreditFacilityTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getCreditFacilityTypeCode(), CrbCreditFacilityType_.creditFacilityTypeCode)
                    );
            }
            if (criteria.getCreditFacilityType() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getCreditFacilityType(), CrbCreditFacilityType_.creditFacilityType)
                    );
            }
        }
        return specification;
    }
}
