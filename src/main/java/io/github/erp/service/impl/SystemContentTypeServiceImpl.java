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

import io.github.erp.domain.SystemContentType;
import io.github.erp.repository.SystemContentTypeRepository;
import io.github.erp.repository.search.SystemContentTypeSearchRepository;
import io.github.erp.service.SystemContentTypeService;
import io.github.erp.service.dto.SystemContentTypeDTO;
import io.github.erp.service.mapper.SystemContentTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SystemContentType}.
 */
@Service
@Transactional
public class SystemContentTypeServiceImpl implements SystemContentTypeService {

    private final Logger log = LoggerFactory.getLogger(SystemContentTypeServiceImpl.class);

    private final SystemContentTypeRepository systemContentTypeRepository;

    private final SystemContentTypeMapper systemContentTypeMapper;

    private final SystemContentTypeSearchRepository systemContentTypeSearchRepository;

    public SystemContentTypeServiceImpl(
        SystemContentTypeRepository systemContentTypeRepository,
        SystemContentTypeMapper systemContentTypeMapper,
        SystemContentTypeSearchRepository systemContentTypeSearchRepository
    ) {
        this.systemContentTypeRepository = systemContentTypeRepository;
        this.systemContentTypeMapper = systemContentTypeMapper;
        this.systemContentTypeSearchRepository = systemContentTypeSearchRepository;
    }

    @Override
    public SystemContentTypeDTO save(SystemContentTypeDTO systemContentTypeDTO) {
        log.debug("Request to save SystemContentType : {}", systemContentTypeDTO);
        SystemContentType systemContentType = systemContentTypeMapper.toEntity(systemContentTypeDTO);
        systemContentType = systemContentTypeRepository.save(systemContentType);
        SystemContentTypeDTO result = systemContentTypeMapper.toDto(systemContentType);
        systemContentTypeSearchRepository.save(systemContentType);
        return result;
    }

    @Override
    public Optional<SystemContentTypeDTO> partialUpdate(SystemContentTypeDTO systemContentTypeDTO) {
        log.debug("Request to partially update SystemContentType : {}", systemContentTypeDTO);

        return systemContentTypeRepository
            .findById(systemContentTypeDTO.getId())
            .map(existingSystemContentType -> {
                systemContentTypeMapper.partialUpdate(existingSystemContentType, systemContentTypeDTO);

                return existingSystemContentType;
            })
            .map(systemContentTypeRepository::save)
            .map(savedSystemContentType -> {
                systemContentTypeSearchRepository.save(savedSystemContentType);

                return savedSystemContentType;
            })
            .map(systemContentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemContentTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemContentTypes");
        return systemContentTypeRepository.findAll(pageable).map(systemContentTypeMapper::toDto);
    }

    public Page<SystemContentTypeDTO> findAllWithEagerRelationships(Pageable pageable) {
        return systemContentTypeRepository.findAllWithEagerRelationships(pageable).map(systemContentTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemContentTypeDTO> findOne(Long id) {
        log.debug("Request to get SystemContentType : {}", id);
        return systemContentTypeRepository.findOneWithEagerRelationships(id).map(systemContentTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SystemContentType : {}", id);
        systemContentTypeRepository.deleteById(id);
        systemContentTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemContentTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SystemContentTypes for query {}", query);
        return systemContentTypeSearchRepository.search(query, pageable).map(systemContentTypeMapper::toDto);
    }
}
