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

import io.github.erp.domain.ExecutiveCategoryType;
import io.github.erp.repository.ExecutiveCategoryTypeRepository;
import io.github.erp.repository.search.ExecutiveCategoryTypeSearchRepository;
import io.github.erp.service.ExecutiveCategoryTypeService;
import io.github.erp.service.dto.ExecutiveCategoryTypeDTO;
import io.github.erp.service.mapper.ExecutiveCategoryTypeMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link ExecutiveCategoryType}.
 */
@Service
@Transactional
public class ExecutiveCategoryTypeServiceImpl implements ExecutiveCategoryTypeService {

    private final Logger log = LoggerFactory.getLogger(ExecutiveCategoryTypeServiceImpl.class);

    private final ExecutiveCategoryTypeRepository executiveCategoryTypeRepository;

    private final ExecutiveCategoryTypeMapper executiveCategoryTypeMapper;

    private final ExecutiveCategoryTypeSearchRepository executiveCategoryTypeSearchRepository;

    public ExecutiveCategoryTypeServiceImpl(
        ExecutiveCategoryTypeRepository executiveCategoryTypeRepository,
        ExecutiveCategoryTypeMapper executiveCategoryTypeMapper,
        ExecutiveCategoryTypeSearchRepository executiveCategoryTypeSearchRepository
    ) {
        this.executiveCategoryTypeRepository = executiveCategoryTypeRepository;
        this.executiveCategoryTypeMapper = executiveCategoryTypeMapper;
        this.executiveCategoryTypeSearchRepository = executiveCategoryTypeSearchRepository;
    }

    @Override
    public ExecutiveCategoryTypeDTO save(ExecutiveCategoryTypeDTO executiveCategoryTypeDTO) {
        log.debug("Request to save ExecutiveCategoryType : {}", executiveCategoryTypeDTO);
        ExecutiveCategoryType executiveCategoryType = executiveCategoryTypeMapper.toEntity(executiveCategoryTypeDTO);
        executiveCategoryType = executiveCategoryTypeRepository.save(executiveCategoryType);
        ExecutiveCategoryTypeDTO result = executiveCategoryTypeMapper.toDto(executiveCategoryType);
        executiveCategoryTypeSearchRepository.save(executiveCategoryType);
        return result;
    }

    @Override
    public Optional<ExecutiveCategoryTypeDTO> partialUpdate(ExecutiveCategoryTypeDTO executiveCategoryTypeDTO) {
        log.debug("Request to partially update ExecutiveCategoryType : {}", executiveCategoryTypeDTO);

        return executiveCategoryTypeRepository
            .findById(executiveCategoryTypeDTO.getId())
            .map(existingExecutiveCategoryType -> {
                executiveCategoryTypeMapper.partialUpdate(existingExecutiveCategoryType, executiveCategoryTypeDTO);

                return existingExecutiveCategoryType;
            })
            .map(executiveCategoryTypeRepository::save)
            .map(savedExecutiveCategoryType -> {
                executiveCategoryTypeSearchRepository.save(savedExecutiveCategoryType);

                return savedExecutiveCategoryType;
            })
            .map(executiveCategoryTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExecutiveCategoryTypeDTO> findAll(Pageable pageable) {
        log.debug("Request to get all ExecutiveCategoryTypes");
        return executiveCategoryTypeRepository.findAll(pageable).map(executiveCategoryTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ExecutiveCategoryTypeDTO> findOne(Long id) {
        log.debug("Request to get ExecutiveCategoryType : {}", id);
        return executiveCategoryTypeRepository.findById(id).map(executiveCategoryTypeMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ExecutiveCategoryType : {}", id);
        executiveCategoryTypeRepository.deleteById(id);
        executiveCategoryTypeSearchRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExecutiveCategoryTypeDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of ExecutiveCategoryTypes for query {}", query);
        return executiveCategoryTypeSearchRepository.search(query, pageable).map(executiveCategoryTypeMapper::toDto);
    }
}
