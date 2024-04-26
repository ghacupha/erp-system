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

import io.github.erp.domain.SystemModule;
import io.github.erp.repository.SystemModuleRepository;
import io.github.erp.repository.search.SystemModuleSearchRepository;
import io.github.erp.service.SystemModuleService;
import io.github.erp.service.dto.SystemModuleDTO;
import io.github.erp.service.mapper.SystemModuleMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link SystemModule}.
 */
@Service
@Transactional
public class SystemModuleServiceImpl implements SystemModuleService {

    private final Logger log = LoggerFactory.getLogger(SystemModuleServiceImpl.class);

    private final SystemModuleRepository systemModuleRepository;

    private final SystemModuleMapper systemModuleMapper;

    private final SystemModuleSearchRepository systemModuleSearchRepository;

    public SystemModuleServiceImpl(
        SystemModuleRepository systemModuleRepository,
        SystemModuleMapper systemModuleMapper,
        SystemModuleSearchRepository systemModuleSearchRepository
    ) {
        this.systemModuleRepository = systemModuleRepository;
        this.systemModuleMapper = systemModuleMapper;
        this.systemModuleSearchRepository = systemModuleSearchRepository;
    }

    @Override
    public SystemModuleDTO save(SystemModuleDTO systemModuleDTO) {
        log.debug("Request to save SystemModule : {}", systemModuleDTO);
        SystemModule systemModule = systemModuleMapper.toEntity(systemModuleDTO);
        systemModule = systemModuleRepository.save(systemModule);
        SystemModuleDTO result = systemModuleMapper.toDto(systemModule);
        systemModuleSearchRepository.save(systemModule);
        return result;
    }

    @Override
    public Optional<SystemModuleDTO> partialUpdate(SystemModuleDTO systemModuleDTO) {
        log.debug("Request to partially update SystemModule : {}", systemModuleDTO);

        return systemModuleRepository
            .findById(systemModuleDTO.getId())
            .map(existingSystemModule -> {
                systemModuleMapper.partialUpdate(existingSystemModule, systemModuleDTO);

                return existingSystemModule;
            })
            .map(systemModuleRepository::save)
            .map(savedSystemModule -> {
                systemModuleSearchRepository.save(savedSystemModule);

                return savedSystemModule;
            })
            .map(systemModuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemModuleDTO> findAll(Pageable pageable) {
        log.debug("Request to get all SystemModules");
        return systemModuleRepository.findAll(pageable).map(systemModuleMapper::toDto);
    }

    public Page<SystemModuleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return systemModuleRepository.findAllWithEagerRelationships(pageable).map(systemModuleMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<SystemModuleDTO> findOne(Long id) {
        log.debug("Request to get SystemModule : {}", id);
        return systemModuleRepository.findOneWithEagerRelationships(id).map(systemModuleMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete SystemModule : {}", id);
        systemModuleRepository.deleteById(id);
        systemModuleSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SystemModuleDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of SystemModules for query {}", query);
        return systemModuleSearchRepository.search(query, pageable).map(systemModuleMapper::toDto);
    }
}
