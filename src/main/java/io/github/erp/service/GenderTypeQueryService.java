package io.github.erp.service;

/*-
 * Erp System - Mark VII No 3 (Gideon Series) Server ver 1.5.7
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
import io.github.erp.domain.GenderType;
import io.github.erp.repository.GenderTypeRepository;
import io.github.erp.repository.search.GenderTypeSearchRepository;
import io.github.erp.service.criteria.GenderTypeCriteria;
import io.github.erp.service.dto.GenderTypeDTO;
import io.github.erp.service.mapper.GenderTypeMapper;
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
 * Service for executing complex queries for {@link GenderType} entities in the database.
 * The main input is a {@link GenderTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link GenderTypeDTO} or a {@link Page} of {@link GenderTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class GenderTypeQueryService extends QueryService<GenderType> {

    private final Logger log = LoggerFactory.getLogger(GenderTypeQueryService.class);

    private final GenderTypeRepository genderTypeRepository;

    private final GenderTypeMapper genderTypeMapper;

    private final GenderTypeSearchRepository genderTypeSearchRepository;

    public GenderTypeQueryService(
        GenderTypeRepository genderTypeRepository,
        GenderTypeMapper genderTypeMapper,
        GenderTypeSearchRepository genderTypeSearchRepository
    ) {
        this.genderTypeRepository = genderTypeRepository;
        this.genderTypeMapper = genderTypeMapper;
        this.genderTypeSearchRepository = genderTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link GenderTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<GenderTypeDTO> findByCriteria(GenderTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<GenderType> specification = createSpecification(criteria);
        return genderTypeMapper.toDto(genderTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link GenderTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<GenderTypeDTO> findByCriteria(GenderTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<GenderType> specification = createSpecification(criteria);
        return genderTypeRepository.findAll(specification, page).map(genderTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(GenderTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<GenderType> specification = createSpecification(criteria);
        return genderTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link GenderTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<GenderType> createSpecification(GenderTypeCriteria criteria) {
        Specification<GenderType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), GenderType_.id));
            }
            if (criteria.getGenderCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getGenderCode(), GenderType_.genderCode));
            }
            if (criteria.getGenderType() != null) {
                specification = specification.and(buildSpecification(criteria.getGenderType(), GenderType_.genderType));
            }
        }
        return specification;
    }
}
