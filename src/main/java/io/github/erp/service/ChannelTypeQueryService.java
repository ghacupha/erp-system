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
