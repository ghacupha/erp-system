package io.github.erp.service.impl;

/*-
 * Erp System - Mark X No 2 (Jehoiada Series) Server ver 1.7.1
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

import static org.elasticsearch.index.query.QueryBuilders.*;

import io.github.erp.domain.ChannelType;
import io.github.erp.repository.ChannelTypeRepository;
import io.github.erp.repository.search.ChannelTypeSearchRepository;
import io.github.erp.service.ChannelTypeService;
import io.github.erp.service.dto.ChannelTypeDTO;
import io.github.erp.service.mapper.ChannelTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ChannelType}.
 */
@Service
@Transactional
public class ChannelTypeServiceImpl implements ChannelTypeService {

    private final Logger log = LoggerFactory.getLogger(ChannelTypeServiceImpl.class);

    private final ChannelTypeRepository channelTypeRepository;

    private final ChannelTypeMapper channelTypeMapper;

    private final ChannelTypeSearchRepository channelTypeSearchRepository;

    public ChannelTypeServiceImpl(
        ChannelTypeRepository channelTypeRepository,
        ChannelTypeMapper channelTypeMapper,
        ChannelTypeSearchRepository channelTypeSearchRepository
    ) {
        this.channelTypeRepository = channelTypeRepository;
        this.channelTypeMapper = channelTypeMapper;
        this.channelTypeSearchRepository = channelTypeSearchRepository;
    }

    @Override
    public ChannelTypeDTO save(ChannelTypeDTO channelTypeDTO) {
        log.debug("Request to save ChannelType : {}", channelTypeDTO);
        ChannelType channelType = channelTypeMapper.toEntity(channelTypeDTO);
        channelType = channelTypeRepository.save(channelType);
        ChannelTypeDTO result = channelTypeMapper.toDto(channelType);
        channelTypeSearchRepository.save(channelType);
        return result;
    }

    @Override
    public Optional<ChannelTypeDTO> partialUpdate(ChannelTypeDTO channelTypeDTO) {
        log.debug("Request to partially update ChannelType : {}", channelTypeDTO);

        return channelTypeRepository
            .findById(channelTypeDTO.getId())
            .map(existingChannelType -> {
                channelTypeMapper.partialUpdate(existingChannelType, channelTypeDTO);

                return existingChannelType;
            })
            .map(channelTypeRepository::save)
            .map(savedChannelType -> {
                channelTypeSearchRepository.save(savedChannelType);

                return savedChannelType;
            })
            .map(channelTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChannelTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ChannelTypes");
        return channelTypeRepository.findAll(pageable).map(channelTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChannelTypeDTO> findOne(Long id) {
        log.debug("Request to get ChannelType : {}", id);
        return channelTypeRepository.findById(id).map(channelTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ChannelType : {}", id);
        channelTypeRepository.deleteById(id);
        channelTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ChannelTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ChannelTypes for query {}", query);
        return channelTypeSearchRepository.search(query, pageable).map(channelTypeMapper::toDto);
    }
}
