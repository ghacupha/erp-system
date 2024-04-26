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

import io.github.erp.domain.ManagementMemberType;
import io.github.erp.repository.ManagementMemberTypeRepository;
import io.github.erp.repository.search.ManagementMemberTypeSearchRepository;
import io.github.erp.service.ManagementMemberTypeService;
import io.github.erp.service.dto.ManagementMemberTypeDTO;
import io.github.erp.service.mapper.ManagementMemberTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ManagementMemberType}.
 */
@Service
@Transactional
public class ManagementMemberTypeServiceImpl implements ManagementMemberTypeService {

    private final Logger log = LoggerFactory.getLogger(ManagementMemberTypeServiceImpl.class);

    private final ManagementMemberTypeRepository managementMemberTypeRepository;

    private final ManagementMemberTypeMapper managementMemberTypeMapper;

    private final ManagementMemberTypeSearchRepository managementMemberTypeSearchRepository;

    public ManagementMemberTypeServiceImpl(
        ManagementMemberTypeRepository managementMemberTypeRepository,
        ManagementMemberTypeMapper managementMemberTypeMapper,
        ManagementMemberTypeSearchRepository managementMemberTypeSearchRepository
    ) {
        this.managementMemberTypeRepository = managementMemberTypeRepository;
        this.managementMemberTypeMapper = managementMemberTypeMapper;
        this.managementMemberTypeSearchRepository = managementMemberTypeSearchRepository;
    }

    @Override
    public ManagementMemberTypeDTO save(ManagementMemberTypeDTO managementMemberTypeDTO) {
        log.debug("Request to save ManagementMemberType : {}", managementMemberTypeDTO);
        ManagementMemberType managementMemberType = managementMemberTypeMapper.toEntity(managementMemberTypeDTO);
        managementMemberType = managementMemberTypeRepository.save(managementMemberType);
        ManagementMemberTypeDTO result = managementMemberTypeMapper.toDto(managementMemberType);
        managementMemberTypeSearchRepository.save(managementMemberType);
        return result;
    }

    @Override
    public Optional<ManagementMemberTypeDTO> partialUpdate(ManagementMemberTypeDTO managementMemberTypeDTO) {
        log.debug("Request to partially update ManagementMemberType : {}", managementMemberTypeDTO);

        return managementMemberTypeRepository
            .findById(managementMemberTypeDTO.getId())
            .map(existingManagementMemberType -> {
                managementMemberTypeMapper.partialUpdate(existingManagementMemberType, managementMemberTypeDTO);

                return existingManagementMemberType;
            })
            .map(managementMemberTypeRepository::save)
            .map(savedManagementMemberType -> {
                managementMemberTypeSearchRepository.save(savedManagementMemberType);

                return savedManagementMemberType;
            })
            .map(managementMemberTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManagementMemberTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ManagementMemberTypes");
        return managementMemberTypeRepository.findAll(pageable).map(managementMemberTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ManagementMemberTypeDTO> findOne(Long id) {
        log.debug("Request to get ManagementMemberType : {}", id);
        return managementMemberTypeRepository.findById(id).map(managementMemberTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ManagementMemberType : {}", id);
        managementMemberTypeRepository.deleteById(id);
        managementMemberTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ManagementMemberTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ManagementMemberTypes for query {}", query);
        return managementMemberTypeSearchRepository.search(query, pageable).map(managementMemberTypeMapper::toDto);
    }
}
