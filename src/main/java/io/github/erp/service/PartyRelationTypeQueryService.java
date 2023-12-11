package io.github.erp.service;

/*-
 * Erp System - Mark IX No 1 (Iddo Series) Server ver 1.6.3
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
import io.github.erp.domain.PartyRelationType;
import io.github.erp.repository.PartyRelationTypeRepository;
import io.github.erp.repository.search.PartyRelationTypeSearchRepository;
import io.github.erp.service.criteria.PartyRelationTypeCriteria;
import io.github.erp.service.dto.PartyRelationTypeDTO;
import io.github.erp.service.mapper.PartyRelationTypeMapper;
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
 * Service for executing complex queries for {@link PartyRelationType} entities in the database.
 * The main input is a {@link PartyRelationTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PartyRelationTypeDTO} or a {@link Page} of {@link PartyRelationTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PartyRelationTypeQueryService extends QueryService<PartyRelationType> {

    private final Logger log = LoggerFactory.getLogger(PartyRelationTypeQueryService.class);

    private final PartyRelationTypeRepository partyRelationTypeRepository;

    private final PartyRelationTypeMapper partyRelationTypeMapper;

    private final PartyRelationTypeSearchRepository partyRelationTypeSearchRepository;

    public PartyRelationTypeQueryService(
        PartyRelationTypeRepository partyRelationTypeRepository,
        PartyRelationTypeMapper partyRelationTypeMapper,
        PartyRelationTypeSearchRepository partyRelationTypeSearchRepository
    ) {
        this.partyRelationTypeRepository = partyRelationTypeRepository;
        this.partyRelationTypeMapper = partyRelationTypeMapper;
        this.partyRelationTypeSearchRepository = partyRelationTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link PartyRelationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PartyRelationTypeDTO> findByCriteria(PartyRelationTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PartyRelationType> specification = createSpecification(criteria);
        return partyRelationTypeMapper.toDto(partyRelationTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PartyRelationTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PartyRelationTypeDTO> findByCriteria(PartyRelationTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PartyRelationType> specification = createSpecification(criteria);
        return partyRelationTypeRepository.findAll(specification, page).map(partyRelationTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PartyRelationTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PartyRelationType> specification = createSpecification(criteria);
        return partyRelationTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link PartyRelationTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PartyRelationType> createSpecification(PartyRelationTypeCriteria criteria) {
        Specification<PartyRelationType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PartyRelationType_.id));
            }
            if (criteria.getPartyRelationTypeCode() != null) {
                specification =
                    specification.and(
                        buildStringSpecification(criteria.getPartyRelationTypeCode(), PartyRelationType_.partyRelationTypeCode)
                    );
            }
            if (criteria.getPartyRelationType() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getPartyRelationType(), PartyRelationType_.partyRelationType));
            }
        }
        return specification;
    }
}
