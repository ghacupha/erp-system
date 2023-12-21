package io.github.erp.service.impl;

/*-
 * Erp System - Mark IX No 4 (Iddo Series) Server ver 1.6.6
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
