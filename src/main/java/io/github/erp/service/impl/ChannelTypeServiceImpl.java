package io.github.erp.service.impl;

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
