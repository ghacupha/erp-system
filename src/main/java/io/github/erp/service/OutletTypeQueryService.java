package io.github.erp.service;

/*-
 * Erp System - Mark III No 12 (Caleb Series) Server ver 1.0.2-SNAPSHOT
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
import io.github.erp.domain.OutletType;
import io.github.erp.repository.OutletTypeRepository;
import io.github.erp.repository.search.OutletTypeSearchRepository;
import io.github.erp.service.criteria.OutletTypeCriteria;
import io.github.erp.service.dto.OutletTypeDTO;
import io.github.erp.service.mapper.OutletTypeMapper;
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
 * Service for executing complex queries for {@link OutletType} entities in the database.
 * The main input is a {@link OutletTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link OutletTypeDTO} or a {@link Page} of {@link OutletTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class OutletTypeQueryService extends QueryService<OutletType> {

    private final Logger log = LoggerFactory.getLogger(OutletTypeQueryService.class);

    private final OutletTypeRepository outletTypeRepository;

    private final OutletTypeMapper outletTypeMapper;

    private final OutletTypeSearchRepository outletTypeSearchRepository;

    public OutletTypeQueryService(
        OutletTypeRepository outletTypeRepository,
        OutletTypeMapper outletTypeMapper,
        OutletTypeSearchRepository outletTypeSearchRepository
    ) {
        this.outletTypeRepository = outletTypeRepository;
        this.outletTypeMapper = outletTypeMapper;
        this.outletTypeSearchRepository = outletTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link OutletTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<OutletTypeDTO> findByCriteria(OutletTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<OutletType> specification = createSpecification(criteria);
        return outletTypeMapper.toDto(outletTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link OutletTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<OutletTypeDTO> findByCriteria(OutletTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<OutletType> specification = createSpecification(criteria);
        return outletTypeRepository.findAll(specification, page).map(outletTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(OutletTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<OutletType> specification = createSpecification(criteria);
        return outletTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link OutletTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<OutletType> createSpecification(OutletTypeCriteria criteria) {
        Specification<OutletType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), OutletType_.id));
            }
            if (criteria.getOutletTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutletTypeCode(), OutletType_.outletTypeCode));
            }
            if (criteria.getOutletType() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutletType(), OutletType_.outletType));
            }
            if (criteria.getOutletTypeDetails() != null) {
                specification = specification.and(buildStringSpecification(criteria.getOutletTypeDetails(), OutletType_.outletTypeDetails));
            }
            if (criteria.getPlaceholderId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPlaceholderId(),
                            root -> root.join(OutletType_.placeholders, JoinType.LEFT).get(Placeholder_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
