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
import io.github.erp.domain.ChannelType;
import io.github.erp.repository.ChannelTypeRepository;
import io.github.erp.repository.search.ChannelTypeSearchRepository;
import io.github.erp.service.criteria.ChannelTypeCriteria;
import io.github.erp.service.dto.ChannelTypeDTO;
import io.github.erp.service.mapper.ChannelTypeMapper;
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
 * Service for executing complex queries for {@link ChannelType} entities in the database.
 * The main input is a {@link ChannelTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ChannelTypeDTO} or a {@link Page} of {@link ChannelTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ChannelTypeQueryService extends QueryService<ChannelType> {

    private final Logger log = LoggerFactory.getLogger(ChannelTypeQueryService.class);

    private final ChannelTypeRepository channelTypeRepository;

    private final ChannelTypeMapper channelTypeMapper;

    private final ChannelTypeSearchRepository channelTypeSearchRepository;

    public ChannelTypeQueryService(
        ChannelTypeRepository channelTypeRepository,
        ChannelTypeMapper channelTypeMapper,
        ChannelTypeSearchRepository channelTypeSearchRepository
    ) {
        this.channelTypeRepository = channelTypeRepository;
        this.channelTypeMapper = channelTypeMapper;
        this.channelTypeSearchRepository = channelTypeSearchRepository;
    }

    /**
     * Return a {@link List} of {@link ChannelTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ChannelTypeDTO> findByCriteria(ChannelTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<ChannelType> specification = createSpecification(criteria);
        return channelTypeMapper.toDto(channelTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ChannelTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ChannelTypeDTO> findByCriteria(ChannelTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ChannelType> specification = createSpecification(criteria);
        return channelTypeRepository.findAll(specification, page).map(channelTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ChannelTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<ChannelType> specification = createSpecification(criteria);
        return channelTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link ChannelTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ChannelType> createSpecification(ChannelTypeCriteria criteria) {
        Specification<ChannelType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), ChannelType_.id));
            }
            if (criteria.getChannelsTypeCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChannelsTypeCode(), ChannelType_.channelsTypeCode));
            }
            if (criteria.getChannelTypes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getChannelTypes(), ChannelType_.channelTypes));
            }
        }
        return specification;
    }
}
